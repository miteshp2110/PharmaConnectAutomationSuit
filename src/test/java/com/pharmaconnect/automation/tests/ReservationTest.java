package com.pharmaconnect.automation.tests;

import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.pages.MyReservationsPage;
import com.pharmaconnect.automation.pages.SearchPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import com.pharmaconnect.automation.utils.TestContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ReservationTest extends BaseTest {

    private SearchPage searchPage;
    private MyReservationsPage myReservationsPage;

    // ── Override parent BeforeMethod — init driver only once ──────
    @Override
    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser) {
        if (webDriver == null) {
            super.setup(browser);
            loginOnce();
        }
        searchPage = pageObjectManager.getSearchPage();
        myReservationsPage = pageObjectManager.getMyReservationsPage();
    }

    // ── Override parent AfterMethod — don't quit between tests ────
    @Override
    @org.testng.annotations.AfterMethod(alwaysRun = true)
    public void cleanUp() {
        // Keep browser alive across chained tests
    }

    // ── Quit browser after ALL tests in this class ────────────────
    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        TestContext.clear();
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    // ── Login helper ──────────────────────────────────────────────
    private void loginOnce() {
        webDriver.get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = pageObjectManager.getLoginPage();
        loginPage.enterEmail(ConfigReader.getProperty("test.user.email"));
        loginPage.enterPassword(ConfigReader.getProperty("test.user.password"));
        loginPage.clickLoginButton();
        loginPage.isRedirected("/search");
    }

    // ── TC1 — Page Title ──────────────────────────────────────────
    @Test(priority = 1,
            testName = "My Reservations page title is correct",
            description = "Navigate to My Reservations and verify the page title shows My Reservations")
    public void verifyPageTitle() {
        myReservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(
                myReservationsPage.getPageTitle(), "My Reservations",
                "Page title mismatch"
        );
    }

    // ── TC2 — Search and Reserve ──────────────────────────────────
    @Test(priority = 2,
            testName = "Search and reserve a medicine",
            description = "Login, search for Paracetamol, click Reserve Now on the first result, enter quantity and confirm reservation")
    public void searchAndReserveMedicine() {
        webDriver.get(ConfigReader.getProperty("search.url"));
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();

        String medicine = searchPage.getMedicineNameAt(0);
        String pharmacy = searchPage.getPharmacyNameAt(0);

        TestContext.set("medicineName", medicine);
        TestContext.set("pharmacyName", pharmacy);
        TestContext.set("quantity", "2");

        searchPage.clickReserveNow(0);
        Assert.assertTrue(searchPage.isReserveFormVisible(0),
                "Reserve form did not open");

        searchPage.enterQuantity(0, "2");
        searchPage.clickConfirmReservation(0);

        Assert.assertTrue(searchPage.isReservationConfirmed(0),
                "Reservation was not confirmed");
    }

    // ── TC3 — Verify in My Reservations ──────────────────────────
    @Test(priority = 3,
            testName = "Reservation appears in My Reservations page",
            description = "After reserving, navigate to My Reservations and verify the reservation is listed with correct medicine, pharmacy and quantity",
            dependsOnMethods = "searchAndReserveMedicine")
    public void verifyReservationInMyReservations() {
        String medicine = TestContext.get("medicineName");
        String pharmacy = TestContext.get("pharmacyName");
        String quantity = TestContext.get("quantity");

        myReservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        myReservationsPage.waitForCards();

        Assert.assertTrue(
                myReservationsPage.isReservationPresent(medicine, pharmacy),
                "Reservation not found in My Reservations for: " + medicine + " @ " + pharmacy
        );
        Assert.assertEquals(
                myReservationsPage.getQuantityOf(medicine, pharmacy), quantity,
                "Quantity mismatch in reservation"
        );
    }

    // ── TC4 — Verify Status is Pending ───────────────────────────
    @Test(priority = 4,
            testName = "Newly created reservation status is PENDING",
            description = "After reserving, navigate to My Reservations and verify the reservation status shows as PENDING",
            dependsOnMethods = "verifyReservationInMyReservations")
    public void verifyReservationStatusIsPending() {
        String medicine = TestContext.get("medicineName");
        String pharmacy = TestContext.get("pharmacyName");

        myReservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        myReservationsPage.waitForCards();

        Assert.assertTrue(
                myReservationsPage.isCardPending(
                        myReservationsPage.findCardIndex(medicine, pharmacy)),
                "Reservation status is not PENDING"
        );
    }

    // ── TC5 — Reservation Count Matches Cards ────────────────────
    @Test(priority = 5,
            testName = "Reservation count badge matches actual card count",
            description = "Navigate to My Reservations and verify the count shown in the header badge matches the actual number of reservation cards",
            dependsOnMethods = "verifyReservationInMyReservations")
    public void reservationCountMatchesCards() {
        myReservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        myReservationsPage.waitForCards();

        int countFromBadge = myReservationsPage.getTotalReservationCount();
        int countFromCards = myReservationsPage.getTotalCardCount();

        Assert.assertEquals(countFromBadge, countFromCards,
                "Badge: " + countFromBadge + " Cards: " + countFromCards);
    }

    // ── TC6 — Cancel Button Visible on Pending ────────────────────
    @Test(priority = 6,
            testName = "Cancel button is visible on pending reservation",
            description = "Navigate to My Reservations and verify that the Cancel button is visible on a pending reservation card",
            dependsOnMethods = "verifyReservationStatusIsPending")
    public void cancelButtonVisibleOnPendingCard() {
        String medicine = TestContext.get("medicineName");
        String pharmacy = TestContext.get("pharmacyName");

        myReservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        myReservationsPage.waitForCards();

        int index = myReservationsPage.findCardIndex(medicine, pharmacy);
        Assert.assertTrue(
                myReservationsPage.isCancelButtonVisibleAt(index),
                "Cancel button not visible on pending reservation"
        );
    }

    // ── TC7 — Cancel Reservation ──────────────────────────────────
    @Test(priority = 7,
            testName = "Cancel a pending reservation",
            description = "Navigate to My Reservations, click Cancel on the pending reservation and verify the status changes to CANCELLED",
            dependsOnMethods = "cancelButtonVisibleOnPendingCard")
    public void cancelReservation() {
        String medicine = TestContext.get("medicineName");
        String pharmacy = TestContext.get("pharmacyName");

        myReservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        myReservationsPage.waitForCards();
        myReservationsPage.cancelReservation(medicine, pharmacy);
        myReservationsPage.waitForCancellation(medicine, pharmacy);

        Assert.assertTrue(
                myReservationsPage.isCardCancelled(
                        myReservationsPage.findCardIndex(medicine, pharmacy)),
                "Reservation status did not change to CANCELLED"
        );
    }

    // ── TC8 — Cancel Button Hidden After Cancellation ─────────────
    @Test(priority = 8,
            testName = "Cancel button is hidden after cancellation",
            description = "After cancelling a reservation verify that the Cancel button is no longer visible on the card",
            dependsOnMethods = "cancelReservation")
    public void cancelButtonHiddenAfterCancellation() {
        String medicine = TestContext.get("medicineName");
        String pharmacy = TestContext.get("pharmacyName");

        myReservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        myReservationsPage.waitForCards();

        int index = myReservationsPage.findCardIndex(medicine, pharmacy);
        Assert.assertFalse(
                myReservationsPage.isCancelButtonVisibleAt(index),
                "Cancel button still visible after cancellation"
        );
    }

    // ── TC9 — My Reservations Requires Login ─────────────────────
    // ⚠️ Always last — this logs out and breaks session for all tests above
    @Test(priority = 9,
            testName = "My Reservations page requires login",
            description = "Logout and navigate directly to My Reservations, verify redirect to login page")
    public void myReservationsRequiresLogin() {
        myReservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        myReservationsPage.clickLogout();
        webDriver.get(ConfigReader.getProperty("my.reservations.url"));
        Assert.assertTrue(
                myReservationsPage.isRedirected("/login"),
                "Expected redirect to login but got: " + webDriver.getCurrentUrl()
        );
    }
}