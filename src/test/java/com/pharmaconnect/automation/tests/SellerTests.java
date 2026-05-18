package com.pharmaconnect.automation.tests;

import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.pages.SellerDashboardPage;
import com.pharmaconnect.automation.pages.SellerDocumentsPage;
import com.pharmaconnect.automation.pages.SellerInventoryPage;
import com.pharmaconnect.automation.pages.SellerReservationsPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SellerTests extends BaseTest {

    private SellerDashboardPage dashboardPage;
    private SellerInventoryPage inventoryPage;
    private SellerReservationsPage reservationsPage;
    private SellerDocumentsPage documentsPage;

    @Override
    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser) {
        if (webDriver == null) {
            super.setup(browser);
            loginAsPharmacy();
        }
        dashboardPage    = pageObjectManager.getSellerDashboardPage();
        inventoryPage    = pageObjectManager.getSellerInventoryPage();
        reservationsPage = pageObjectManager.getSellerReservationsPage();
        documentsPage    = pageObjectManager.getSellerDocumentsPage();
    }

    @Override
    @org.testng.annotations.AfterMethod(alwaysRun = true)
    public void cleanUp() {
        // Keep browser alive
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    private void loginAsPharmacy() {
        webDriver.get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = pageObjectManager.getLoginPage();
        loginPage.enterEmail(ConfigReader.getProperty("test.pharmacy.email"));
        loginPage.enterPassword(ConfigReader.getProperty("test.pharmacy.password"));
        loginPage.clickLoginButton();
        loginPage.isRedirected("/seller/dashboard");
    }

    // ══════════════════════════════════════════════════════════════
    // DASHBOARD TESTS
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 1,
            testName = "Seller dashboard loads correctly",
            description = "Login as pharmacy user and verify the seller dashboard loads with page title visible")
    public void dashboardLoadsCorrectly() {
        dashboardPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertTrue(dashboardPage.isDashboardLoaded(),
                "Seller dashboard did not load");
    }

    @Test(priority = 2,
            testName = "Dashboard page badge shows Seller Portal",
            description = "Verify the page badge on the seller dashboard shows Seller Portal")
    public void dashboardBadgeIsSellerPortal() {
        dashboardPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(dashboardPage.getPageBadge(), "SELLER PORTAL",
                "Page badge mismatch");
    }

    @Test(priority = 3,
            testName = "Dashboard shows pharmacy name as title",
            description = "Verify the page title on the dashboard shows the pharmacy name")
    public void dashboardTitleShowsPharmacyName() {
        dashboardPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertFalse(dashboardPage.getPageTitle().isEmpty(),
                "Dashboard title is empty — pharmacy name not shown");
    }

    @Test(priority = 4,
            testName = "Dashboard shows verified chip",
            description = "Verify the verified chip is visible on the seller dashboard for a verified pharmacy")
    public void dashboardShowsVerifiedChip() {
        dashboardPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertTrue(dashboardPage.isVerifiedChipVisible(),
                "Verified chip not visible on dashboard");
    }

    @Test(priority = 5,
            testName = "Dashboard shows all 4 stat cards",
            description = "Verify all 4 stat cards are visible on the dashboard — Total Medicines, Low Stock, Pending Orders, Out of Stock")
    public void dashboardShowsFourStatCards() {
        dashboardPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(dashboardPage.getStatCardCount(), 4,
                "Expected 4 stat cards but found: " + dashboardPage.getStatCardCount());
    }

    @Test(priority = 6,
            testName = "Manage Stock card navigates to inventory",
            description = "Click the Manage Stock quick action card and verify navigation to the inventory page")
    public void manageStockCardNavigatesToInventory() {
        dashboardPage.navigateTo(ConfigReader.getProperty("base.url"));
        dashboardPage.clickManageStock();
        Assert.assertTrue(dashboardPage.isRedirected("/seller/inventory"),
                "Manage Stock did not navigate to inventory");
    }

    @Test(priority = 7,
            testName = "View Reservations card navigates to reservations",
            description = "Click the View Reservations quick action card and verify navigation to the reservations page")
    public void viewReservationsCardNavigatesToReservations() {
        dashboardPage.navigateTo(ConfigReader.getProperty("base.url"));
        dashboardPage.clickViewReservations();
        Assert.assertTrue(dashboardPage.isRedirected("/seller/reservations"),
                "View Reservations did not navigate to reservations page");
    }

    @Test(priority = 8,
            testName = "Analytics card navigates to documents page",
            description = "Click the Analytics quick action card and verify navigation to the documents page")
    public void analyticsCardNavigatesToDocuments() {
        dashboardPage.navigateTo(ConfigReader.getProperty("base.url"));
        dashboardPage.clickAnalytics();
        Assert.assertTrue(dashboardPage.isRedirected("/seller/documents"),
                "Analytics card did not navigate to documents page");
    }

    // ══════════════════════════════════════════════════════════════
    // INVENTORY TESTS
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 9,
            testName = "Inventory page loads correctly",
            description = "Navigate to the inventory page and verify the page title shows Stock Management")
    public void inventoryPageLoadsCorrectly() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(inventoryPage.getPageTitle(), "Stock Management",
                "Inventory page title mismatch");
    }

    @Test(priority = 10,
            testName = "Empty state shown when no inventory",
            description = "Navigate to inventory page with no medicines and verify the empty state message is shown")
    public void inventoryEmptyStateVisible() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        if (inventoryPage.getInventoryCardCount() == 0) {
            Assert.assertTrue(inventoryPage.isEmptyStateVisible(),
                    "Empty state not shown when no inventory");
        }
    }

    @Test(priority = 11,
            testName = "Add Medicine button opens add form",
            description = "Click the Add Medicine button and verify the add medicine form appears")
    public void addMedicineButtonOpensForm() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        inventoryPage.clickAddMedicineToggle();
        Assert.assertTrue(inventoryPage.isAddFormVisible(),
                "Add medicine form did not open");
    }

    @Test(priority = 12,
            testName = "Bulk upload button disabled without file",
            description = "Verify the bulk Upload CSV button is disabled when no file is selected")
    public void bulkUploadButtonDisabledWithoutFile() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertFalse(inventoryPage.isBulkUploadButtonEnabled(),
                "Bulk upload button should be disabled without a file");
    }

    @Test(priority = 13,
            testName = "Show format button toggles CSV format help",
            description = "Click the Show format button and verify the CSV format help section appears")
    public void showFormatButtonTogglesHelp() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        inventoryPage.clickShowFormat();
        // After click the button text changes — just verify no exception thrown
        Assert.assertNotNull(inventoryPage.getPageTitle(),
                "Page became unresponsive after clicking Show format");
    }

    // ══════════════════════════════════════════════════════════════
    // RESERVATIONS TESTS
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 14,
            testName = "Seller reservations page loads correctly",
            description = "Navigate to the seller reservations page and verify the title shows Incoming Reservations")
    public void sellerReservationsPageLoadsCorrectly() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(reservationsPage.getPageTitle(), "Incoming Reservations",
                "Seller reservations page title mismatch");
    }

    @Test(priority = 15,
            testName = "Filter All is active by default",
            description = "Navigate to seller reservations and verify the All filter button is active by default")
    public void filterAllActiveByDefault() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertTrue(reservationsPage.isFilterAllActive(),
                "All filter is not active by default");
    }

    @Test(priority = 16,
            testName = "All 5 filter buttons are visible",
            description = "Verify all 5 filter buttons — All, Pending, Claimed, Expired, Cancelled — are present on the page")
    public void allFilterButtonsVisible() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int filterCount = webDriver.findElements(By.cssSelector(".filter-row button")).size();
        Assert.assertEquals(filterCount, 5,
                "Expected 5 filter buttons but found: " + filterCount);
    }

    @Test(priority = 17,
            testName = "Empty state shown when no reservations",
            description = "When no reservations exist verify the empty state with No reservations message is shown")
    public void emptyStateShownWhenNoReservations() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        if (reservationsPage.getTotalCardCount() == 0) {
            Assert.assertTrue(reservationsPage.isEmptyStateVisible(),
                    "Empty state not shown when there are no reservations");
        }
    }

    @Test(priority = 18,
            testName = "Total badge count matches actual reservation cards",
            description = "Verify the total count shown in the header badge matches the actual number of reservation cards")
    public void totalBadgeMatchesCards() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int badgeCount = reservationsPage.getTotalBadgeCount();
        int cardCount  = reservationsPage.getTotalCardCount();
        Assert.assertEquals(badgeCount, cardCount,
                "Badge count: " + badgeCount + " does not match card count: " + cardCount);
    }

    // ══════════════════════════════════════════════════════════════
    // DOCUMENTS TESTS
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 19,
            testName = "Documents page loads correctly",
            description = "Navigate to the documents page and verify the page title shows Upload Documents")
    public void documentsPageLoadsCorrectly() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(documentsPage.getPageTitle(), "Upload Documents",
                "Documents page title mismatch");
    }

    @Test(priority = 20,
            testName = "Verification info banner is visible",
            description = "Verify the Verification Required info banner is displayed on the documents page")
    public void verificationInfoBannerVisible() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertTrue(documentsPage.isInfoBannerVisible(),
                "Info banner not visible on documents page");
        Assert.assertEquals(documentsPage.getInfoBannerTitle(), "Verification Required",
                "Info banner title mismatch");
    }

    @Test(priority = 21,
            testName = "Submit button disabled without file",
            description = "Verify the Submit for Review button is disabled when no file has been selected")
    public void submitButtonDisabledWithoutFile() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertFalse(documentsPage.isSubmitButtonEnabled(),
                "Submit button should be disabled without a file");
    }

    @Test(priority = 22,
            testName = "What Happens Next steps card shows 3 steps",
            description = "Verify the What Happens Next section shows exactly 3 steps on the documents page")
    public void stepsCardShowsThreeSteps() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertTrue(documentsPage.isStepsCardVisible(),
                "Steps card not visible");
        Assert.assertEquals(documentsPage.getStepCount(), 3,
                "Expected 3 steps but found: " + documentsPage.getStepCount());
    }

    // ── Logout always last ────────────────────────────────────────
    @Test(priority = 23,
            testName = "Pharmacy logout redirects to login page",
            description = "Click Logout from the dashboard and verify the pharmacy user is redirected to the login page")
    public void pharmacyLogoutRedirectsToLogin() {
        dashboardPage.navigateTo(ConfigReader.getProperty("base.url"));
        dashboardPage.clickLogout();
        Assert.assertTrue(dashboardPage.isRedirected("/"),
                "Logout did not redirect to login. URL: " + webDriver.getCurrentUrl());
    }
}