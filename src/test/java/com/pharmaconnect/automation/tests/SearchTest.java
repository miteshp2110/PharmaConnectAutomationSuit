package com.pharmaconnect.automation.tests;
import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.pages.SearchPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class SearchTest extends BaseTest {

    @BeforeMethod
    public void setUp() {
        getWebDriver().get(ConfigReader.getProperty("search.url"));
    }

    // ── Search Functionality ──────────────────────────────────────
    @Test(testName = "Search with valid medicine name",
            description = "Enter a valid medicine name and verify that results are returned with count greater than zero")
    public void searchWithValidMedicine() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();
        Assert.assertTrue(searchPage.getResultCardCount() > 0,
                "No results found for valid medicine");
    }

    @Test(testName = "Search with non-existent medicine name",
            description = "Enter a medicine name that does not exist and verify that a no results message is displayed")
    public void searchWithNonExistentMedicine() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("xyzabc123");
        Assert.assertTrue(searchPage.isNoResultsMessageShown(),
                "Expected no results but results were shown");
    }

    @Test(testName = "Result count in toolbar matches rendered cards",
            description = "Search for a medicine and verify that the count shown in the toolbar matches the actual number of result cards on the page")
    public void resultCountMatchesToolbar() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();
        Assert.assertEquals(
                searchPage.getResultCardCount(),
                searchPage.getResultCountFromToolbar(),
                "Card count does not match toolbar count"
        );
    }

    @Test(testName = "Autocomplete suggestions appear while typing",
            description = "Type a partial medicine name and verify that the autocomplete dropdown appears with at least one suggestion")
    public void autocompleteSuggestionsAppear() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.enterMedicineName("Para");
        Assert.assertTrue(searchPage.isSuggestionDropdownVisible(),
                "Autocomplete dropdown not shown");
        Assert.assertTrue(searchPage.getSuggestions().size() > 0,
                "No suggestions in dropdown");
    }

    @Test(testName = "Clicking autocomplete suggestion fills the search input",
            description = "Type a partial name, click the first suggestion and verify that the search input is populated with the selected value")
    public void clickSuggestionFillsInput() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.enterMedicineName("Para");
        searchPage.clickSuggestion(0);
        Assert.assertFalse(searchPage.getSearchInputValue().isEmpty(),
                "Input not filled after clicking suggestion");
    }

    // ── Search Modes ──────────────────────────────────────────────
    @Test(testName = "Keyword search mode is active by default",
            description = "Verify that the Keyword mode button has the active state when the search page first loads")
    public void keywordModeActiveByDefault() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        Assert.assertTrue(searchPage.isKeywordModeActive(),
                "Keyword mode not active by default");
    }

    @Test(testName = "Only one search mode can be active at a time",
            description = "Switch between Nearest and Emergency modes and verify that only one mode button is active at any given time")
    public void onlyOneModeActiveAtATime() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.clickNearestMode();
        Assert.assertEquals(searchPage.getActiveModeCount(), 1,
                "More than one mode is active");
        searchPage.clickEmergencyMode();
        Assert.assertEquals(searchPage.getActiveModeCount(), 1,
                "More than one mode is active");
    }

    // ── Sort ──────────────────────────────────────────────────────
    @Test(testName = "Sort results by price low to high",
            description = "Search for a medicine, apply Price Low to High sort and verify that the first card price is less than or equal to the last card price")
    public void sortByPriceLowToHigh() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();
        searchPage.sortBy("price-asc");

        double first = searchPage.getPriceAt(0);
        double last  = searchPage.getPriceAt(searchPage.getResultCardCount() - 1);
        Assert.assertTrue(first <= last,
                "Results not sorted by price low to high. First: " + first + " Last: " + last);
    }

    @Test(testName = "Sort results by price high to low",
            description = "Search for a medicine, apply Price High to Low sort and verify that the first card price is greater than or equal to the last card price")
    public void sortByPriceHighToLow() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();
        searchPage.sortBy("price-desc");

        double first = searchPage.getPriceAt(0);
        double last  = searchPage.getPriceAt(searchPage.getResultCardCount() - 1);
        Assert.assertTrue(first >= last,
                "Results not sorted by price high to low. First: " + first + " Last: " + last);
    }

    @Test(testName = "Sort results by most stock first",
            description = "Search for a medicine, apply Most Stock First sort and verify that the first card stock count is greater than or equal to the last card stock count")
    public void sortByMostStockFirst() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();
        searchPage.sortBy("stock-desc");

        int first = searchPage.getStockAt(0);
        int last  = searchPage.getStockAt(searchPage.getResultCardCount() - 1);
        Assert.assertTrue(first >= last,
                "Results not sorted by stock. First: " + first + " Last: " + last);
    }

    // ── Emergency Mode ────────────────────────────────────────────
    @Test(testName = "Emergency mode banner is visible on page load",
            description = "Verify that the emergency mode banner with the Enable button is displayed when the search page loads")
    public void emergencyBannerVisibleOnLoad() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        Assert.assertTrue(searchPage.isEmergencyBannerVisible(),
                "Emergency banner not visible on page load");
    }

    @Test(testName = "Enabling emergency mode filters only 24x7 pharmacies",
            description = "Search for a medicine, enable emergency mode and verify that all result cards belong to 24x7 pharmacies only")
    public void enableEmergencyModeFilters24x7Only() throws InterruptedException {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();
        searchPage.clickEnableEmergencyMode();
        searchPage.clickSearchButton();


        Assert.assertTrue(searchPage.isEmergencyModeEnabled(),
                "Emergency mode not enabled");

        searchPage.waitForResults();
        int totalCards = searchPage.getResultCardCount();
        int badgeCount = 0;
        for (int i = 0; i < totalCards; i++) {
            if (searchPage.is247BadgeShownAt(i)) badgeCount++;
        }
        Assert.assertEquals(badgeCount, totalCards,
                "Non-24x7 pharmacies shown in emergency mode");
    }

    // ── Auth Guard ────────────────────────────────────────────────
    @Test(testName = "Search works without being logged in",
            description = "Navigate to the search page without logging in and verify that searching for a medicine returns results normally")
    public void searchWorksWithoutLogin() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();
        Assert.assertTrue(searchPage.getResultCardCount() > 0,
                "Search does not work without login");
    }

    @Test(testName = "Reserve Now redirects to login when not logged in",
            description = "Without logging in, search for a medicine and click Reserve Now on the first result, then verify the user is redirected to the login page")
    public void reserveNowRedirectsToLoginWhenNotLoggedIn() {
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();
        searchPage.clickReserveNow(0);

        Assert.assertTrue(searchPage.isRedirected("/login"),
                "Expected redirect to login but got: " + getWebDriver().getCurrentUrl());
    }

    // ── Reserve Form (needs login) ────────────────────────────────
    @Test(testName = "Reserve form opens on clicking Reserve Now",
            description = "Login, search for a medicine and click Reserve Now on the first result, then verify that the reservation form expands on the card")
    public void reserveFormOpensOnClick() {
        loginFirst();
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.clickSearchButton();
        searchPage.waitForResults();
        searchPage.clickReserveNow(0);
        Assert.assertTrue(searchPage.isReserveFormVisible(0),
                "Reserve form did not open");
    }

    @Test(testName = "Default quantity in reserve form is 1",
            description = "Login, open the reserve form on the first result and verify that the quantity input field defaults to a value of 1")
    public void defaultQuantityIsOne() {
        loginFirst();
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();
        searchPage.clickReserveNow(0);
        Assert.assertEquals(searchPage.getDefaultQuantity(0), "1",
                "Default quantity is not 1");
    }

    @Test(testName = "Cancel button collapses the reserve form",
            description = "Login, open the reserve form on the first result, click Cancel and verify that the form collapses and is no longer visible")
    public void cancelCollapseReserveForm() {
        loginFirst();
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();
        searchPage.clickReserveNow(0);
        searchPage.clickCancelReservation(0);
        Assert.assertFalse(searchPage.isReserveFormVisible(0),
                "Reserve form still visible after cancel");
    }

    @Test(testName = "Negative quantity is not accepted in reserve form",
            description = "Login, open the reserve form, enter a negative quantity and verify that the Confirm button is disabled and the reservation cannot be submitted")
    public void negativeQuantityNotAccepted() {
        loginFirst();
        SearchPage searchPage = getPageObjectManager().getSearchPage();
        searchPage.searchMedicine("Paracetamol");
        searchPage.waitForResults();
        searchPage.clickReserveNow(0);
        searchPage.enterQuantity(0, "-1");
        Assert.assertFalse(searchPage.isConfirmButtonEnabled(0),
                "Confirm button enabled for negative quantity");
    }

    // ── Helper ────────────────────────────────────────────────────
    private void loginFirst() {
        getWebDriver().get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = getPageObjectManager().getLoginPage();
        loginPage.enterEmail(ConfigReader.getProperty("test.user.email"));
        loginPage.enterPassword(ConfigReader.getProperty("test.user.password"));
        loginPage.clickLoginButton();
        loginPage.isRedirected("/search");
    }
}