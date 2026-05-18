package com.pharmaconnect.automation.tests;

import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.pages.AdminSellerPage;
import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import com.pharmaconnect.automation.utils.TestContext;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;

public class AdminTest extends BaseTest {

    private AdminSellerPage adminPage;
    private WebDriverWait wait;

    @Override
    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser) {
        if (webDriver == null) {
            super.setup(browser);
            wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            loginAsAdmin();
        }
        adminPage = pageObjectManager.getAdminSellerPage();
    }

    @Override
    @org.testng.annotations.AfterMethod(alwaysRun = true)
    public void cleanUp() {
        // Keep browser alive
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        TestContext.clear();
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    private void loginAsAdmin() {
        webDriver.get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = pageObjectManager.getLoginPage();
        loginPage.enterEmail(ConfigReader.getProperty("test.admin.email"));
        loginPage.enterPassword(ConfigReader.getProperty("test.admin.password"));
        loginPage.clickLoginButton();
        loginPage.isRedirected("/admin");
    }

    // ══════════════════════════════════════════════════════════════
    // PAGE LOAD & NAVIGATION
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 1,
            testName = "Admin page loads correctly",
            description = "Login as admin and verify the Manage Pharmacies page loads with correct title")
    public void adminPageLoadsCorrectly() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(adminPage.getPageTitle(), "Manage Pharmacies",
                "Page title mismatch");
    }

    @Test(priority = 2,
            testName = "Admin page badge shows Admin",
            description = "Verify the page badge on the admin page shows Admin")
    public void adminPageBadgeIsAdmin() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(adminPage.getPageBadge(), "ADMIN",
                "Page badge mismatch");
    }

    @Test(priority = 3,
            testName = "Admin page has two tabs",
            description = "Verify the admin page shows exactly two tabs — Pending Applications and All Pharmacies")
    public void adminPageHasTwoTabs() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(adminPage.getTabCount(), 2,
                "Expected 2 tabs but found: " + adminPage.getTabCount());
    }

    // ══════════════════════════════════════════════════════════════
    // TABS
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 4,
            testName = "Pending Applications tab is active by default",
            description = "Verify the Pending Applications tab is active when the admin page first loads")
    public void pendingTabActiveByDefault() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertTrue(adminPage.isPendingTabActive(),
                "Pending Applications tab is not active by default");
    }

    @Test(priority = 5,
            testName = "Pending tab count badge shows number of pending applications",
            description = "Verify the count badge on the Pending Applications tab shows a number greater than zero")
    public void pendingTabCountIsVisible() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = adminPage.getPendingTabCount();
        Assert.assertTrue(count >= 0,
                "Pending tab count badge shows invalid value: " + count);
    }

    @Test(priority = 6,
            testName = "Pending tab count matches actual card count",
            description = "Verify the count shown in the Pending tab badge matches the actual number of pharmacy cards displayed")
    public void pendingTabCountMatchesCards() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        int badgeCount = adminPage.getPendingTabCount();
        int cardCount  = adminPage.getCardCount();
        Assert.assertEquals(badgeCount, cardCount,
                "Pending tab badge count [" + badgeCount
                        + "] does not match card count [" + cardCount + "]");
    }

    @Test(priority = 7,
            testName = "Clicking All Pharmacies tab switches active tab",
            description = "Click the All Pharmacies tab and verify it becomes active and Pending tab becomes inactive")
    public void allPharmaciesTabSwitchesActive() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        Assert.assertTrue(adminPage.isAllPharmaciesTabActive(),
                "All Pharmacies tab not active after clicking");
        Assert.assertFalse(adminPage.isPendingTabActive(),
                "Pending tab still active after switching to All Pharmacies");
    }

    @Test(priority = 8,
            testName = "Clicking Pending tab switches back from All Pharmacies",
            description = "After clicking All Pharmacies, click Pending Applications and verify it becomes active again")
    public void pendingTabSwitchesBackFromAllPharmacies() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.clickPendingTab();
        Assert.assertTrue(adminPage.isPendingTabActive(),
                "Pending Applications tab not active after switching back");
    }

    // ══════════════════════════════════════════════════════════════
    // PHARMACY CARDS
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 9,
            testName = "Each pending pharmacy card shows pharmacy name",
            description = "Verify the pharmacy name is visible on each pending application card")
    public void eachCardShowsPharmacyName() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = adminPage.getCardCount();
        Assert.assertTrue(count > 0, "No pharmacy cards found");

        for (int i = 0; i < Math.min(count, 5); i++) {
            Assert.assertFalse(adminPage.getPharmacyNameAt(i).isEmpty(),
                    "Pharmacy name empty at card index: " + i);
        }
    }

    @Test(priority = 10,
            testName = "Each pending pharmacy card shows Approve and Reject buttons",
            description = "Verify both Verify & Approve and Reject buttons are visible on each pending pharmacy card")
    public void eachCardShowsApproveAndRejectButtons() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = adminPage.getCardCount();
        Assert.assertTrue(count > 0, "No pharmacy cards found");

        for (int i = 0; i < Math.min(count, 3); i++) {
            Assert.assertTrue(adminPage.isApproveButtonVisibleAt(i),
                    "Approve button not visible at card index: " + i);
            Assert.assertTrue(adminPage.isRejectButtonVisibleAt(i),
                    "Reject button not visible at card index: " + i);
        }
    }

    @Test(priority = 11,
            testName = "Each card shows pharmacy ID hint",
            description = "Verify each pharmacy card shows a partial ID hint at the bottom of the actions bar")
    public void eachCardShowsPharmacyId() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = adminPage.getCardCount();
        Assert.assertTrue(count > 0, "No pharmacy cards found");

        String id = adminPage.getPharmacyIdAt(0);
        Assert.assertFalse(id.isEmpty(),
                "Pharmacy ID hint is empty on first card");
    }

    @Test(priority = 12,
            testName = "Each card shows owner name",
            description = "Verify the owner name is displayed in the Owner Information section of each card")
    public void eachCardShowsOwnerName() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = adminPage.getCardCount();
        Assert.assertTrue(count > 0, "No pharmacy cards found");

        Assert.assertFalse(adminPage.getOwnerNameAt(0).isEmpty(),
                "Owner name is empty on first card");
    }

    // ══════════════════════════════════════════════════════════════
    // SEARCH
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 13,
            testName = "Search filters pharmacy cards by name",
            description = "Type a pharmacy name in the search box and verify only matching cards are shown")
    public void searchFiltersPharmacyCards() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();

        int totalBefore = adminPage.getCardCount();
        if (totalBefore == 0) return;

        String firstName = adminPage.getPharmacyNameAt(0);
        String [] spl = firstName.split(" ");
        if(spl[spl.length-1].equalsIgnoreCase("INACTIVE")){
            spl[spl.length-1]="";
            firstName = String.join(" ",spl);
        }
        adminPage.searchPharmacy(firstName);


        int visibleCount = adminPage.getCardCount();
        Assert.assertTrue(visibleCount >= 1,
                "Search returned no results for: " + firstName);

        for (int i = 0; i < visibleCount; i++) {
            Assert.assertTrue(
                    adminPage.getPharmacyNameAt(i)
                            .toLowerCase()
                            .contains(firstName.toLowerCase()),
                    "Card [" + adminPage.getPharmacyNameAt(i)
                            + "] does not match search: " + firstName
            );
        }
    }

    @Test(priority = 14,
            testName = "Search with no match shows no cards",
            description = "Search for a pharmacy name that does not exist and verify no cards are shown")
    public void searchWithNoMatchShowsNoCards() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.searchPharmacy("zzznomatchpharmacy999");
        Assert.assertEquals(adminPage.getCardCount(), 0,
                "Expected 0 cards for non-existent pharmacy search");
    }



    // ══════════════════════════════════════════════════════════════
    // APPROVE PHARMACY
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 16,
            testName = "Approving a pharmacy removes it from pending list",
            description = "Click Verify and Approve on the first pending pharmacy and verify the pending count decreases by 1")
    public void approvingPharmacyRemovesFromPendingList() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickPendingTab();

        int countBefore = adminPage.getCardCount();
        if (countBefore == 0) return;

        String pharmacyNameBefore = adminPage.getPharmacyNameAt(0);
        TestContext.set("approvedPharmacy", pharmacyNameBefore);

        adminPage.clickApproveAt(0);

        // Wait for card to disappear or count to change
        wait.until(driver -> adminPage.getCardCount() < countBefore);

        int countAfter = adminPage.getCardCount();
        Assert.assertTrue(countAfter < countBefore,
                "Pending count did not decrease after approving. Before: "
                        + countBefore + " After: " + countAfter);
    }

    @Test(priority = 17,
            testName = "Approved pharmacy appears in All Pharmacies tab",
            description = "After approving, switch to All Pharmacies tab and verify the approved pharmacy is listed there",
            dependsOnMethods = "approvingPharmacyRemovesFromPendingList")
    public void approvedPharmacyAppearsInAllPharmacies() {
        String approvedName = TestContext.get("approvedPharmacy");
        if (approvedName == null) return;

        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.waitForCards();

        int index = adminPage.findCardIndexByName(approvedName);
        Assert.assertTrue(index >= 0,
                "Approved pharmacy [" + approvedName
                        + "] not found in All Pharmacies tab");
    }

    // ══════════════════════════════════════════════════════════════
    // REJECT PHARMACY
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 18,
            testName = "Rejecting a pharmacy removes it from pending list",
            description = "Click Reject on a pending pharmacy and verify the pending count decreases by 1")
    public void rejectingPharmacyRemovesFromPendingList() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickPendingTab();

        int countBefore = adminPage.getCardCount();
        if (countBefore == 0) return;

        adminPage.clickRejectAt(0);

        wait.until(driver -> adminPage.getCardCount() < countBefore);

        int countAfter = adminPage.getCardCount();
        Assert.assertTrue(countAfter < countBefore,
                "Pending count did not decrease after rejecting. Before: "
                        + countBefore + " After: " + countAfter);
    }

    // ══════════════════════════════════════════════════════════════
    // ALL PHARMACIES TAB
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 19,
            testName = "All Pharmacies tab shows more pharmacies than Pending",
            description = "Switch to All Pharmacies tab and verify it shows at least as many cards as the Pending tab")
    public void allPharmaciesTabShowsMoreThanPending() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));

        adminPage.clickPendingTab();
        int pendingCount = adminPage.getCardCount();

        adminPage.clickAllPharmaciesTab();
        int allCount = adminPage.getCardCount();

        Assert.assertTrue(allCount >= pendingCount,
                "All Pharmacies [" + allCount
                        + "] should be >= Pending [" + pendingCount + "]");
    }



    // ══════════════════════════════════════════════════════════════
// ALL PHARMACIES — FILTER CHIPS
// ══════════════════════════════════════════════════════════════

    @Test(priority = 21,
            testName = "All Pharmacies tab has filter chips for All Verified Unverified Inactive",
            description = "Switch to All Pharmacies tab and verify the filter bar shows 4 filter chips")
    public void allPharmaciesTabHasFourFilterChips() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        int chipCount = webDriver.findElements(
                org.openqa.selenium.By.cssSelector(".asl-fchip")).size();
        Assert.assertEquals(chipCount, 4,
                "Expected 4 filter chips but found: " + chipCount);
    }



    @Test(priority = 23,
            testName = "Verified filter shows only verified pharmacy cards",
            description = "Click Verified filter and verify all visible cards have the green Verified badge")
    public void verifiedFilterShowsOnlyVerifiedCards() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.clickFilterVerified();

        int count = adminPage.getCardCount();
        Assert.assertTrue(count > 0, "No cards shown after clicking Verified filter");

        for (int i = 0; i < count; i++) {
            Assert.assertTrue(
                    adminPage.getStatusBadgeAt(i).equalsIgnoreCase("Verified"),
                    "Non-verified card shown at index " + i
                            + ": " + adminPage.getStatusBadgeAt(i)
            );
        }
    }

    @Test(priority = 24,
            testName = "Unverified filter shows only unverified pharmacy cards",
            description = "Click Unverified filter and verify all visible cards have the amber Unverified badge")
    public void unverifiedFilterShowsOnlyUnverifiedCards() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.clickFilterUnverified();

        int count = adminPage.getCardCount();
        if (count == 0) return;

        for (int i = 0; i < count; i++) {
            Assert.assertTrue(
                    adminPage.getStatusBadgeAt(i).equalsIgnoreCase("Unverified"),
                    "Non-unverified card shown at index " + i
            );
        }
    }

    @Test(priority = 25,
            testName = "Inactive filter shows only inactive pharmacy cards",
            description = "Click Inactive filter and verify all visible cards have the Inactive tag")
    public void inactiveFilterShowsOnlyInactiveCards() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.clickFilterInactive();

        int count = adminPage.getCardCount();
        if (count == 0) return;

        for (int i = 0; i < count; i++) {
            Assert.assertTrue(adminPage.isCardInactive(i),
                    "Non-inactive card shown at index " + i + " after Inactive filter");
        }
    }

// ══════════════════════════════════════════════════════════════
// DEACTIVATE PHARMACY
// ══════════════════════════════════════════════════════════════

    @Test(priority = 26,
            testName = "Deactivate button visible on active verified pharmacy cards",
            description = "Switch to All Pharmacies, filter Verified and verify the Deactivate button is visible on verified active cards")
    public void deactivateButtonVisibleOnActiveVerifiedCard() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.clickFilterVerified();

        int count = adminPage.getCardCount();
        Assert.assertTrue(count > 0, "No verified cards found");

        // Find an active (non-inactive) verified card
        int activeIndex = adminPage.findFirstActiveVerifiedCardIndex();
        Assert.assertTrue(activeIndex >= 0, "No active verified card found");
        Assert.assertTrue(adminPage.isDeactivateButtonVisibleAt(activeIndex),
                "Deactivate button not visible on active verified card");
    }

    @Test(priority = 27,
            testName = "Deactivating a pharmacy marks it as Inactive",
            description = "Click Deactivate on an active verified pharmacy and verify the card gets the Inactive tag and Reactivate button appears")
    public void deactivatePharmacyMarksAsInactive() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.clickFilterVerified();

        int activeIndex = adminPage.findFirstActiveVerifiedCardIndex();
        if (activeIndex < 0) return;

        String pharmacyName = adminPage.getPharmacyNameAt(activeIndex).trim();
        TestContext.set("deactivatedPharmacy", pharmacyName);

        adminPage.clickDeactivateAt(activeIndex);

        // Wait for the page to update
        wait.until(driver -> {
            adminPage.navigateTo(ConfigReader.getProperty("base.url"));
            adminPage.clickAllPharmaciesTab();
            adminPage.clickFilterInactive();
            return adminPage.getCardCount() > 0;
        });

        // Verify it now appears under Inactive filter
        int inactiveCount = adminPage.getCardCount();
        Assert.assertTrue(inactiveCount > 0,
                "No inactive cards after deactivating: " + pharmacyName);
        Assert.assertTrue(adminPage.isCardInactive(0),
                "Deactivated pharmacy card does not show Inactive tag");
    }

    @Test(priority = 28,
            testName = "Deactivated pharmacy shows Reactivate button instead of Deactivate",
            description = "After deactivating, verify the card shows only Reactivate button and not Deactivate",
            dependsOnMethods = "deactivatePharmacyMarksAsInactive")
    public void deactivatedPharmacyShowsReactivateButton() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.clickFilterInactive();

        int count = adminPage.getCardCount();
        Assert.assertTrue(count > 0, "No inactive cards found");

        Assert.assertTrue(adminPage.isReactivateButtonVisibleAt(0),
                "Reactivate button not visible on inactive card");
        Assert.assertFalse(adminPage.isDeactivateButtonVisibleAt(0),
                "Deactivate button should NOT be visible on inactive card");
    }

// ══════════════════════════════════════════════════════════════
// REACTIVATE PHARMACY
// ══════════════════════════════════════════════════════════════

    @Test(priority = 29,
            testName = "Reactivate button visible on inactive pharmacy cards",
            description = "Filter by Inactive and verify the Reactivate button is visible on inactive cards")
    public void reactivateButtonVisibleOnInactiveCard() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.clickFilterInactive();

        int count = adminPage.getCardCount();
        if (count == 0) return;

        Assert.assertTrue(adminPage.isReactivateButtonVisibleAt(0),
                "Reactivate button not visible on inactive card");
    }

    @Test(priority = 30,
            testName = "Reactivating a pharmacy removes Inactive tag and restores Deactivate button",
            description = "Click Reactivate on an inactive pharmacy and verify the Inactive tag disappears and Deactivate button returns")
    public void reactivatePharmacyRestoresActiveState() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.clickFilterInactive();

        int inactiveCountBefore = adminPage.getCardCount();
        if (inactiveCountBefore == 0) return;

        String pharmacyName = adminPage.getPharmacyNameAt(0).trim()
                .replace("INACTIVE", "").trim();
        TestContext.set("reactivatedPharmacy", pharmacyName);

        adminPage.clickReactivateAt(0);

        // Wait for inactive count to decrease
        wait.until(driver -> {
            adminPage.navigateTo(ConfigReader.getProperty("base.url"));
            adminPage.clickAllPharmaciesTab();
            adminPage.clickFilterInactive();
            return adminPage.getCardCount() < inactiveCountBefore;
        });

        int inactiveCountAfter = adminPage.getCardCount();
        Assert.assertTrue(inactiveCountAfter < inactiveCountBefore,
                "Inactive count did not decrease after reactivating. Before: "
                        + inactiveCountBefore + " After: " + inactiveCountAfter);
    }

    @Test(priority = 31,
            testName = "Reactivated pharmacy appears back in Verified filter",
            description = "After reactivating a pharmacy verify it shows up under the Verified filter with Deactivate button",
            dependsOnMethods = "reactivatePharmacyRestoresActiveState")
    public void reactivatedPharmacyAppearsInVerifiedFilter() {
        String pharmacyName = TestContext.get("reactivatedPharmacy");
        if (pharmacyName == null) return;

        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickAllPharmaciesTab();
        adminPage.clickFilterVerified();

        // Search for it
        adminPage.searchPharmacy(pharmacyName);
        int count = adminPage.getCardCount();
        Assert.assertTrue(count > 0,
                "Reactivated pharmacy [" + pharmacyName
                        + "] not found in Verified filter");

        // Verify Deactivate button is back
        Assert.assertTrue(adminPage.isDeactivateButtonVisibleAt(0),
                "Deactivate button not restored on reactivated pharmacy");
        Assert.assertFalse(adminPage.isReactivateButtonVisibleAt(0),
                "Reactivate button should NOT be visible on active pharmacy");
    }
    // ── Logout always last ────────────────────────────────────────
    @Test(priority = 32,
            testName = "Admin logout redirects to login page",
            description = "Click Logout from the admin page and verify redirect to the login page")
    public void adminLogoutRedirectsToLogin() {
        adminPage.navigateTo(ConfigReader.getProperty("base.url"));
        adminPage.clickLogout();
        Assert.assertTrue(adminPage.isRedirected("/"),
                "Logout did not redirect to login. URL: "
                        + webDriver.getCurrentUrl());
    }
}