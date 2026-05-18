package com.pharmaconnect.automation.tests;

import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.base.StatefulBaseTest;
import com.pharmaconnect.automation.pages.AdminMedicinesPage;
import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import com.pharmaconnect.automation.utils.TestContext;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class AdminMedicinesTest extends StatefulBaseTest {

    private AdminMedicinesPage medicinesPage;
    private WebDriverWait wait;

    @BeforeClass
    public void classSetup() {
        wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(10));
        medicinesPage = getPageObjectManager().getAdminMedicinesPage();
        loginAsAdmin();
    }





    private void loginAsAdmin() {
        getWebDriver().get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = getPageObjectManager().getLoginPage();
        loginPage.enterEmail(ConfigReader.getProperty("test.admin.email"));
        loginPage.enterPassword(ConfigReader.getProperty("test.admin.password"));
        loginPage.clickLoginButton();
        loginPage.isRedirected("/admin");
    }

    // ══════════════════════════════════════════════════════════════
    // PAGE LOAD
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 1,
            testName = "Medicine Catalog page loads with correct title",
            description = "Navigate to admin/medicines and verify the page title is Medicine Catalog")
    public void medicineCatalogPageLoads() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(medicinesPage.getPageTitle(), "Medicine Catalog",
                "Page title mismatch");
    }

    @Test(priority = 2,
            testName = "Page subtitle shows total medicine count",
            description = "Verify the subtitle shows the number of medicines currently in the system")
    public void subtitleShowsMedicineCount() throws InterruptedException {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        Thread.sleep(1000);
        int count = medicinesPage.getTotalMedicineCountFromSubtitle();
        Assert.assertTrue(count > 0,
                "Subtitle medicine count is 0 or not parseable: "
                        + medicinesPage.getPageSubtitle());
    }

    @Test(priority = 3,
            testName = "Medicine table is visible with rows on page load",
            description = "Verify the medicine table is visible and contains at least one row of data")
    public void medicineTableIsVisibleOnLoad() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertTrue(medicinesPage.getTableRowCount() > 0,
                "Medicine table has no rows on page load");
    }

    @Test(priority = 4,
            testName = "Table row count matches the count label",
            description = "Verify the count label shown above the table matches the actual number of rows")
    public void tableRowCountMatchesCountLabel() throws InterruptedException {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        Thread.sleep(1000);
        int labelCount = medicinesPage.getCountFromLabel();
        int rowCount   = medicinesPage.getTableRowCount();
        Assert.assertEquals(rowCount, labelCount,
                "Label count [" + labelCount
                        + "] does not match table rows [" + rowCount + "]");
    }

    @Test(priority = 5,
            testName = "Table has 6 column headers",
            description = "Verify the medicine table shows all 6 headers: Name, Generic Name, Category, Manufacturer, Form, Strength")
    public void tableHasSixColumnHeaders() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(medicinesPage.getTableHeaderCount(), 6,
                "Expected 6 column headers but found: "
                        + medicinesPage.getTableHeaderCount());
    }

    @Test(priority = 6,
            testName = "Each table row has a non-empty medicine name",
            description = "Verify the first 5 rows all have non-empty names in the Name column")
    public void eachRowHasMedicineName() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = Math.min(medicinesPage.getTableRowCount(), 5);
        Assert.assertTrue(count > 0, "No rows in table");
        for (int i = 0; i < count; i++) {
            Assert.assertFalse(medicinesPage.getMedicineNameAt(i).isEmpty(),
                    "Empty medicine name at row: " + i);
        }
    }

    // ══════════════════════════════════════════════════════════════
    // HEADER TOGGLE BUTTONS — DEFAULT STATE
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 7,
            testName = "Default state shows Add Medicine and Link Alternatives buttons",
            description = "On page load verify the header shows + Add Medicine and Link Alternatives toggle buttons and no forms are open")
    public void defaultStateShowsCorrectToggleButtons() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertTrue(
                medicinesPage.getAddMedicineToggleBtnText().contains("Add Medicine"),
                "Add Medicine toggle button text incorrect: "
                        + medicinesPage.getAddMedicineToggleBtnText());
        Assert.assertTrue(
                medicinesPage.getLinkAlternativesToggleBtnText().contains("Link Alternatives"),
                "Link Alternatives toggle button text incorrect: "
                        + medicinesPage.getLinkAlternativesToggleBtnText());
        Assert.assertFalse(medicinesPage.isAddMedicinePanelVisible(),
                "Add Medicine panel should NOT be visible by default");
        Assert.assertFalse(medicinesPage.isLinkAlternativesPanelVisible(),
                "Link Alternatives panel should NOT be visible by default");
    }

    // ══════════════════════════════════════════════════════════════
    // ADD MEDICINE TOGGLE
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 8,
            testName = "Clicking Add Medicine opens the add form panel",
            description = "Click the + Add Medicine button and verify the Add New Medicine form panel appears")
    public void clickAddMedicineToggleOpensPanel() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.clickAddMedicineToggle();
        Assert.assertTrue(medicinesPage.isAddMedicinePanelVisible(),
                "Add Medicine panel did not open after clicking toggle");
    }

    @Test(priority = 9,
            testName = "Add Medicine toggle button changes to Cancel when form is open",
            description = "After clicking + Add Medicine verify the button text changes to ✕ Cancel")
    public void addMedicineToggleBtnChangesToCancel() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.clickAddMedicineToggle();
        Assert.assertTrue(
                medicinesPage.getAddMedicineToggleBtnText().contains("Cancel"),
                "Toggle button did not change to Cancel. Actual: "
                        + medicinesPage.getAddMedicineToggleBtnText());
    }

    @Test(priority = 10,
            testName = "Clicking Cancel closes the Add Medicine panel",
            description = "Open the Add Medicine form then click the Cancel button and verify the panel closes")
    public void cancelClosesAddMedicinePanel() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.clickAddMedicineToggle();
        wait.until(driver -> medicinesPage.isAddMedicinePanelVisible());
        medicinesPage.clickAddMedicineToggle(); // now says Cancel
        Assert.assertFalse(medicinesPage.isAddMedicinePanelVisible(),
                "Add Medicine panel still visible after clicking Cancel");
    }

    // ══════════════════════════════════════════════════════════════
    // ADD MEDICINE — VALIDATION
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 11,
            testName = "Submitting Add Medicine without name shows validation error",
            description = "Open Add Medicine form, submit without filling the name and verify the error message")
    public void addMedicineWithoutNameShowsError() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.clickAddMedicineToggle();
        medicinesPage.clickAddMedicine();
        Assert.assertTrue(medicinesPage.isAddFormErrorVisible(),
                "No validation error shown when name is empty");
        Assert.assertEquals(medicinesPage.getAddFormError(),
                "Medicine name is required.",
                "Error message text mismatch");
    }

    @Test(priority = 12,
            testName = "Add medicine with only the required name field succeeds",
            description = "Open form, fill only the required Name field and submit — verify success toast and row count increases")
    public void addMedicineWithNameOnlySucceeds() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countBefore = medicinesPage.getTableRowCount();

        medicinesPage.clickAddMedicineToggle();
        String uniqueName = "TestMed" + System.currentTimeMillis();
        medicinesPage.enterName(uniqueName);
        medicinesPage.clickAddMedicine();

        String toast = medicinesPage.getToastSuccessMessage();
        Assert.assertFalse(toast.equals("No success toast"),
                "No success toast after adding medicine with name only");

        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countAfter = medicinesPage.getTableRowCount();
        Assert.assertTrue(countAfter > countBefore,
                "Row count did not increase. Before: "
                        + countBefore + " After: " + countAfter);

        TestContext.set("addedMedicine", uniqueName);
    }

    @Test(priority = 13,
            testName = "Add medicine with all 6 fields succeeds",
            description = "Fill all 6 fields (Name, Generic, Category, Manufacturer, Form, Strength) and verify the medicine is added")
    public void addMedicineWithAllFieldsSucceeds() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countBefore = medicinesPage.getTableRowCount();

        medicinesPage.clickAddMedicineToggle();
        String uniqueName = "FullMed" + System.currentTimeMillis();
        medicinesPage.enterName(uniqueName);
        medicinesPage.enterGenericName("TestGeneric");
        medicinesPage.enterCategory("TestCategory");
        medicinesPage.enterManufacturer("TestPharma");
        medicinesPage.enterDosageForm("Tablet");
        medicinesPage.enterStrength("100mg");
        medicinesPage.clickAddMedicine();

        String toast = medicinesPage.getToastSuccessMessage();
        Assert.assertFalse(toast.equals("No success toast"),
                "No success toast after adding medicine with all fields");

        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countAfter = medicinesPage.getTableRowCount();
        Assert.assertTrue(countAfter > countBefore,
                "Row count did not increase after adding full medicine");

        TestContext.set("fullMedicineName", uniqueName);
    }

    @Test(priority = 14,
            testName = "Newly added medicine appears in the table",
            description = "After adding a medicine with all fields verify it appears in the medicine table",
            dependsOnMethods = "addMedicineWithAllFieldsSucceeds")
    public void addedMedicineAppearsInTable() throws InterruptedException {
        String name = TestContext.get("fullMedicineName");
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        Thread.sleep(1000);
        Assert.assertTrue(medicinesPage.isMedicineInTable(name),
                "Added medicine [" + name + "] not found in table");
    }


    // ══════════════════════════════════════════════════════════════
    // LINK ALTERNATIVES TOGGLE
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 16,
            testName = "Clicking Link Alternatives opens the link alternatives panel",
            description = "Click the Link Alternatives button and verify the Link Generic Alternatives panel appears")
    public void clickLinkAlternativesToggleOpensPanel() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.clickLinkAlternativesToggle();
        Assert.assertTrue(medicinesPage.isLinkAlternativesPanelVisible(),
                "Link Alternatives panel did not open after clicking toggle");
    }

    @Test(priority = 17,
            testName = "Link Alternatives toggle button changes to Cancel when panel is open",
            description = "After clicking Link Alternatives verify the button text changes to ✕ Cancel")
    public void linkAlternativesToggleBtnChangesToCancel() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.clickLinkAlternativesToggle();
        Assert.assertTrue(
                medicinesPage.getLinkAlternativesToggleBtnText().contains("Cancel"),
                "Toggle button did not change to Cancel. Actual: "
                        + medicinesPage.getLinkAlternativesToggleBtnText());
    }

    @Test(priority = 18,
            testName = "Clicking Cancel closes the Link Alternatives panel",
            description = "Open the Link Alternatives panel then click Cancel and verify the panel closes")
    public void cancelClosesLinkAlternativesPanel() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.clickLinkAlternativesToggle();
        wait.until(driver -> medicinesPage.isLinkAlternativesPanelVisible());
        medicinesPage.clickLinkAlternativesToggle(); // now says Cancel
        Assert.assertFalse(medicinesPage.isLinkAlternativesPanelVisible(),
                "Link Alternatives panel still visible after clicking Cancel");
    }

    // ══════════════════════════════════════════════════════════════
    // LINK ALTERNATIVES — VALIDATION
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 19,
            testName = "Submitting Link Alternatives without selecting medicines shows error",
            description = "Open Link Alternatives form, submit without selecting any medicine and verify the error message")
    public void linkAlternativesWithoutSelectionShowsError() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.clickLinkAlternativesToggle();
        medicinesPage.clickLinkAlternatives();
        Assert.assertTrue(medicinesPage.isLinkFormErrorVisible(),
                "No error shown when submitting Link Alternatives without selection");
        Assert.assertEquals(medicinesPage.getLinkFormError(),
                "Please select both medicines.",
                "Link form error message mismatch");
    }

    @Test(priority = 20,
            testName = "Brand Medicine dropdown has selectable options",
            description = "Open Link Alternatives panel and verify the Brand Medicine dropdown contains medicines beyond the placeholder")
    public void brandMedicineDropdownHasOptions() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.clickLinkAlternativesToggle();
        Assert.assertTrue(medicinesPage.getBrandMedicineOptionsCount() > 1,
                "Brand Medicine dropdown only has placeholder — no options available");
    }

    @Test(priority = 21,
            testName = "Link two medicines as generic alternatives succeeds",
            description = "Select different brand and generic medicines with an equivalence note and click Link Alternatives")
    public void linkTwoMedicinesSucceeds() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.clickLinkAlternativesToggle();
        medicinesPage.selectBrandMedicine(1);      // index 0 is placeholder
        medicinesPage.selectGenericAlternative(2); // different medicine
        medicinesPage.enterEquivalenceNote("Same active ingredient, lower cost");
        medicinesPage.clickLinkAlternatives();

        String msg = medicinesPage.getInlineSuccessMessage();
        Assert.assertEquals(msg,"Medicines linked as alternatives successfully",
                "No response after clicking Link Alternatives");
    }

    // ══════════════════════════════════════════════════════════════
    // SEARCH
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 22,
            testName = "Searching by medicine name filters the table",
            description = "Type a medicine name in search and verify only matching rows are shown")
    public void searchByNameFiltersTable() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.searchMedicine("Paracetamol");
        int rowCount = medicinesPage.getTableRowCount();
        Assert.assertTrue(rowCount >= 1,
                "Search for Paracetamol returned no results");
        for (int i = 0; i < rowCount; i++) {
            Assert.assertTrue(
                    medicinesPage.getMedicineNameAt(i).toLowerCase().contains("paracetamol"),
                    "Non-matching row [" + medicinesPage.getMedicineNameAt(i)
                            + "] shown after searching Paracetamol"
            );
        }
    }

    @Test(priority = 23,
            testName = "Searching by generic name filters the table",
            description = "Type a generic name like Acetaminophen and verify only rows containing that generic are shown")
    public void searchByGenericNameFiltersTable() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.searchMedicine("Acetaminophen");
        Assert.assertTrue(medicinesPage.getTableRowCount() >= 1,
                "Search for Acetaminophen generic returned no results");
    }

    @Test(priority = 24,
            testName = "Searching by category filters the table",
            description = "Type a category name like Antibiotic and verify results belong to that category")
    public void searchByCategoryFiltersTable() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.searchMedicine("Antibiotic");
        Assert.assertTrue(medicinesPage.getTableRowCount() >= 1,
                "Search for Antibiotic category returned no results");
    }

    @Test(priority = 25,
            testName = "Search with no match shows zero rows",
            description = "Type a name that matches no medicine and verify the table shows zero rows")
    public void searchWithNoMatchShowsZeroRows() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.searchMedicine("zzznomatchmed999");
        Assert.assertEquals(medicinesPage.getTableRowCount(), 0,
                "Expected zero rows for no-match search");
    }

    @Test(priority = 26,
            testName = "Count label updates to reflect filtered row count",
            description = "Search for a term and verify the count label matches the filtered number of visible rows")
    public void countLabelUpdatesOnSearch() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.searchMedicine("Paracetamol");
        int labelCount = medicinesPage.getCountFromLabel();
        int rowCount   = medicinesPage.getTableRowCount();
        Assert.assertEquals(labelCount, rowCount,
                "Count label [" + labelCount
                        + "] does not match visible rows [" + rowCount + "]");
    }



    // ── Logout — always last ──────────────────────────────────────
    @Test(priority = 28,
            testName = "Admin logout from Medicine Catalog redirects to login",
            description = "Click Logout from the Medicine Catalog page and verify the browser is redirected to the login page")
    public void adminLogoutFromMedicinesPage() {
        medicinesPage.navigateTo(ConfigReader.getProperty("base.url"));
        medicinesPage.clickLogout();
        Assert.assertTrue(medicinesPage.isRedirected("/"),
                "Logout did not redirect to login. URL: "
                        + getWebDriver().getCurrentUrl());
    }
}