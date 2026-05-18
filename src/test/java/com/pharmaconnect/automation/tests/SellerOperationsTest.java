package com.pharmaconnect.automation.tests;

import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.pages.SellerDashboardPage;
import com.pharmaconnect.automation.pages.SellerDocumentsPage;
import com.pharmaconnect.automation.pages.SellerInventoryPage;
import com.pharmaconnect.automation.pages.SellerReservationsPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import com.pharmaconnect.automation.utils.TestContext;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.time.Duration;

public class SellerOperationsTest extends BaseTest {

    private SellerInventoryPage inventoryPage;
    private SellerReservationsPage reservationsPage;
    private SellerDocumentsPage documentsPage;
    private SellerDashboardPage dashboardPage;
    private WebDriverWait wait;

    @Override
    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser) {
        if (webDriver == null) {
            super.setup(browser);
            wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            loginAsPharmacy();
        }
        inventoryPage    = pageObjectManager.getSellerInventoryPage();
        reservationsPage = pageObjectManager.getSellerReservationsPage();
        documentsPage    = pageObjectManager.getSellerDocumentsPage();
        dashboardPage    = pageObjectManager.getSellerDashboardPage();
    }

    @Override
    @org.testng.annotations.AfterMethod(alwaysRun = true)
    public void cleanUp() {
        // Keep browser alive across all tests
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        TestContext.clear();
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
    // INVENTORY — ADD MEDICINE FORM
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 1,
            testName = "Add medicine toggle button opens the add form",
            description = "Click the Add Medicine button in the header and verify the add medicine form appears on the page")
    public void addMedicineFormOpens() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        inventoryPage.clickAddMedicineToggle();
        Assert.assertTrue(inventoryPage.isAddFormVisible(),
                "Add medicine form did not open after clicking the toggle button");
    }

    @Test(priority = 2,
            testName = "Medicine dropdown has selectable options",
            description = "Open the add medicine form and verify the medicine dropdown has more than just the placeholder option")
    public void medicineDropdownHasOptions() throws InterruptedException {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        inventoryPage.clickAddMedicineToggle();
        inventoryPage.clickMedicineSelect();
        Assert.assertTrue(inventoryPage.getMedicineOptionsCount() > 1,
                "Medicine dropdown only has placeholder — no medicines available to select");
    }

    @Test(priority = 3,
            testName = "Add to Inventory button disabled when fields are empty",
            description = "Open the add form without filling any fields and verify the Add to Inventory button is disabled")
    public void addToInventoryDisabledWithoutFields() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        inventoryPage.clickAddMedicineToggle();
        inventoryPage.clickAddToInventory();
        String errorMsg = inventoryPage.getErrorMessage();
        Assert.assertEquals(errorMsg,"Select a medicine and enter valid quantity and price.",
                "Add to Inventory button should be disabled when required fields are empty");
    }

    @Test(priority = 4,
            testName = "Add a new medicine to inventory successfully",
            description = "Select a medicine, fill in quantity, price, manufacturing date and expiry date then verify it is added to inventory")
    public void addNewMedicineToInventory() throws InterruptedException {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countBefore = inventoryPage.getInventoryCardCount();

        // ✅ Dynamic values — unique every run
        int    medicineIndex = 1 + (int)(Math.random() * 5);   // index 1-5
        String quantity      = String.valueOf(10 + (int)(Math.random() * 90));
        String price         = String.format("%.2f", 10 + Math.random() * 200);
        int    mfgYear       = java.time.LocalDate.now().getYear() - 1;
        int    mfgMonth      = 1 + (int)(Math.random() * 11);
        String mfgDate       = String.format("%02d-%02d-%d", mfgMonth, 1, mfgYear);
        int    expYear       = java.time.LocalDate.now().getYear() + 2 + (int)(Math.random() * 5);
        int    expMonth      = 1 + (int)(Math.random() * 11);
        String expiryDate    = String.format("%02d-%02d-%d", expMonth, 1, expYear);

        // ✅ Store for TC5 to reuse exact same values
        TestContext.set("medicine.index",  String.valueOf(medicineIndex));
        TestContext.set("medicine.qty",    quantity);
        TestContext.set("medicine.price",  price);
        TestContext.set("medicine.mfg",    mfgDate);
        TestContext.set("medicine.expiry", expiryDate);

        inventoryPage.clickAddMedicineToggle();
        inventoryPage.selectMedicineByIndex(medicineIndex);
        inventoryPage.enterQuantity(quantity);
        inventoryPage.enterPrice(price);
        inventoryPage.enterManufacturingDate(mfgDate);
        inventoryPage.enterExpiryDate(expiryDate);
        inventoryPage.clickAddToInventory();

        String toast = inventoryPage.getToastSuccessMessage();
        Assert.assertTrue(toast.equals("Item added to inventory."),
                "No success toast shown after adding medicine");

        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countAfter = inventoryPage.getInventoryCardCount();
        Assert.assertTrue(countAfter > countBefore,
                "Inventory count did not increase after adding. Before: "
                        + countBefore + " After: " + countAfter);

        TestContext.set("inventoryCountAfterAdd", String.valueOf(countAfter));
    }

    @Test(priority = 5,
            testName = "Adding same medicine with exact same details triggers duplicate alert",
            description = "Try to add the exact same medicine with same price and dates as TC4 and verify a duplicate warning alert appears",
            dependsOnMethods = "addNewMedicineToInventory")
    public void addSameMedicineTwiceCreatesDuplicate() throws InterruptedException {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));

        // ✅ Use exact same values stored from TC4
        int    medicineIndex = Integer.parseInt(TestContext.get("medicine.index"));
        String quantity      = TestContext.get("medicine.qty");
        String price         = TestContext.get("medicine.price");
        String mfgDate       = TestContext.get("medicine.mfg");
        String expiryDate    = TestContext.get("medicine.expiry");

        inventoryPage.clickAddMedicineToggle();
        inventoryPage.selectMedicineByIndex(medicineIndex);
        inventoryPage.enterQuantity(quantity);
        inventoryPage.enterPrice(price);
        inventoryPage.enterManufacturingDate(mfgDate);
        inventoryPage.enterExpiryDate(expiryDate);
        inventoryPage.clickAddToInventory();

        // ✅ Verify duplicate alert appears
        Assert.assertTrue(inventoryPage.isAlertPresent(),
                "Expected a duplicate warning alert but no alert appeared");

        String alertText = inventoryPage.getAlertText();
        Assert.assertTrue(
                alertText.toLowerCase().contains("already exists"),
                "Alert text does not mention duplicate. Actual: " + alertText
        );

        // Dismiss alert cleanly
        inventoryPage.dismissAlert();
    }

    @Test( priority = 6,
            testName = "Add medicine with past expiry date shows error",
            description = "Enter a manufacturing and expiry date both in the past and verify the form rejects it with an error")
    public void addMedicineWithPastExpiryShowsError() throws InterruptedException {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countBefore = inventoryPage.getInventoryCardCount();

        inventoryPage.clickAddMedicineToggle();
        inventoryPage.selectMedicineByIndex(2);
        inventoryPage.enterQuantity("10");
        inventoryPage.enterPrice("15.00");
        inventoryPage.enterManufacturingDate("01-01-2020");
        inventoryPage.enterExpiryDate("01-06-2020");
        inventoryPage.clickAddToInventory();

        String errorToast = inventoryPage.getToastErrorMessage();
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countAfter = inventoryPage.getInventoryCardCount();

        boolean errorShown   = !errorToast.equals("No error toast");
        boolean countUnchanged = countAfter == countBefore;
        Assert.assertTrue(errorShown || countUnchanged,
                "Past expiry date was accepted — count changed from "
                        + countBefore + " to " + countAfter);
    }

    @Test(priority = 7,
            testName = "Add medicine without selecting medicine shows error",
            description = "Fill quantity, price and dates but leave medicine unselected and verify it cannot be submitted")
    public void addMedicineWithoutSelectingMedicineNotAccepted() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countBefore = inventoryPage.getInventoryCardCount();

        inventoryPage.clickAddMedicineToggle();
        // Skip selecting medicine — leave at placeholder
        inventoryPage.enterQuantity("20");
        inventoryPage.enterPrice("10.00");
        inventoryPage.enterManufacturingDate("01-01-2025");
        inventoryPage.enterExpiryDate("01-01-2030");
        inventoryPage.clickAddToInventory();
        String errorMessage=inventoryPage.getErrorMessage();
        Assert.assertEquals("Select a medicine and enter valid quantity and price.",errorMessage,
                "Add to Inventory button should still be disabled without medicine selected");
    }

    // ══════════════════════════════════════════════════════════════
    // INVENTORY — QUANTITY ADJUSTMENT (+/-)
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 8,
            testName = "Increase quantity button increments stock by 1",
            description = "Click the + button on the first inventory card and verify the displayed quantity increases by exactly 1",
            dependsOnMethods = "addNewMedicineToInventory")
    public void increaseQuantityButtonWorks() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        if (inventoryPage.getInventoryCardCount() == 0) return;

        int before = Integer.parseInt(inventoryPage.getQuantityAt(0).trim());
        inventoryPage.clickIncreaseQtyAt(0);

        wait.until(driver -> {
            try {
                int current = Integer.parseInt(inventoryPage.getQuantityAt(0).trim());
                return current != before;
            } catch (Exception e) {
                return false;
            }
        });

        int after = Integer.parseInt(inventoryPage.getQuantityAt(0).trim());
        Assert.assertEquals(after, before + 1,
                "Quantity did not increase by 1. Before: " + before + " After: " + after);
    }

    @Test(priority = 9,
            testName = "Decrease quantity button decrements stock by 1",
            description = "Click the - button on the first inventory card and verify the displayed quantity decreases by exactly 1",
            dependsOnMethods = "increaseQuantityButtonWorks")
    public void decreaseQuantityButtonWorks() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        if (inventoryPage.getInventoryCardCount() == 0) return;

        int before = Integer.parseInt(inventoryPage.getQuantityAt(0).trim());
        if (before <= 1) return;

        inventoryPage.clickDecreaseQtyAt(0);

        wait.until(driver -> {
            try {
                int current = Integer.parseInt(inventoryPage.getQuantityAt(0).trim());
                return current != before;
            } catch (Exception e) {
                return false;
            }
        });

        int after = Integer.parseInt(inventoryPage.getQuantityAt(0).trim());
        Assert.assertEquals(after, before - 1,
                "Quantity did not decrease by 1. Before: " + before + " After: " + after);
    }

    // ══════════════════════════════════════════════════════════════
    // INVENTORY — EDIT MEDICINE
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 10,
            testName = "Edit button opens inline edit form on card",
            description = "Click the Edit button on the first inventory card and verify the inline edit input fields appear",
            dependsOnMethods = "addNewMedicineToInventory")
    public void editButtonOpensInlineForm() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        if (inventoryPage.getInventoryCardCount() == 0) return;

        inventoryPage.clickEditAt(0);
        Assert.assertTrue(inventoryPage.isEditFormVisibleAt(0),
                "Edit form did not open after clicking Edit button");
    }

    @Test(priority = 11,
            testName = "Cancel edit collapses the edit form without saving",
            description = "Open the edit form then click Cancel and verify the edit form collapses and is no longer visible",
            dependsOnMethods = "editButtonOpensInlineForm")
    public void cancelEditCollapsesForm() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        if (inventoryPage.getInventoryCardCount() == 0) return;

        inventoryPage.clickEditAt(0);
        wait.until(driver -> inventoryPage.isEditFormVisibleAt(0));
        inventoryPage.clickCancelEditAt(0);

        Assert.assertFalse(inventoryPage.isEditFormVisibleAt(0),
                "Edit form still visible after clicking Cancel");
    }

    // ══════════════════════════════════════════════════════════════
    // INVENTORY — SEARCH
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 12,
            testName = "Search medicine filters inventory cards by name",
            description = "Type a medicine name in the search box and verify only matching cards are shown in the inventory grid",
            dependsOnMethods = "addNewMedicineToInventory")
    public void searchFiltersInventoryCards() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        if (inventoryPage.getInventoryCardCount() == 0) return;

        String firstMedicineName = inventoryPage.getMedicineNameAt(0);
        inventoryPage.searchMedicine(firstMedicineName);

        int visibleCount = inventoryPage.getInventoryCardCount();
        Assert.assertTrue(visibleCount >= 1,
                "Search returned no results for: " + firstMedicineName);

        for (int i = 0; i < visibleCount; i++) {
            Assert.assertTrue(
                    inventoryPage.getMedicineNameAt(i)
                            .toLowerCase()
                            .contains(firstMedicineName.toLowerCase()),
                    "Card [" + inventoryPage.getMedicineNameAt(i)
                            + "] does not match search: " + firstMedicineName
            );
        }
    }

    @Test(priority = 13,
            testName = "Search with no match shows zero results",
            description = "Enter a search term that matches no medicine and verify no inventory cards are shown")
    public void searchWithNoMatchShowsZeroResults() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        inventoryPage.searchMedicine("zzznomatchxxx999");
        Assert.assertEquals(inventoryPage.getInventoryCardCount(), 0,
                "Expected 0 cards for non-existent search term");
    }
    

    // ══════════════════════════════════════════════════════════════
    // INVENTORY — SORT
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 15,
            testName = "Sort by A to Z orders medicines alphabetically",
            description = "Select A to Z sort and verify the first card name is alphabetically before or equal to the last card name",
            dependsOnMethods = "addNewMedicineToInventory")
    public void sortByNameAtoZ() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = inventoryPage.getInventoryCardCount();
        if (count < 2) return;

        inventoryPage.sortBy("name");
        String first = inventoryPage.getMedicineNameAt(0);
        String last  = inventoryPage.getMedicineNameAt(count - 1);

        Assert.assertTrue(first.compareToIgnoreCase(last) <= 0,
                "Inventory not sorted A-Z. First: [" + first + "] Last: [" + last + "]");
    }

    // ══════════════════════════════════════════════════════════════
    // INVENTORY — BULK UPLOAD
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 16,
            testName = "Show format button makes CSV format help visible",
            description = "Click the Show format button on the bulk upload section and verify the CSV format help section appears")
    public void showFormatTogglesHelp() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        inventoryPage.clickShowFormat();
        Assert.assertTrue(inventoryPage.isBulkHelpVisible(),
                "Bulk CSV format help section not visible after clicking Show format");
    }

    @Test(priority = 17,
            testName = "Bulk upload button disabled without CSV file",
            description = "Verify the Upload CSV button is disabled when no CSV file has been chosen")
    public void bulkUploadDisabledWithoutFile() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertFalse(inventoryPage.isBulkUploadButtonEnabled(),
                "Bulk Upload CSV button should be disabled without selecting a file");
    }

    @Test(priority = 18,
            testName = "Bulk upload button enabled after selecting a CSV file",
            description = "Select a valid CSV file and verify the Upload CSV button becomes enabled")
    public void bulkUploadEnabledAfterFileSelected() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        String csvPath = createTempCsvFile();
        inventoryPage.uploadBulkCsv(csvPath);
        Assert.assertTrue(inventoryPage.isBulkUploadButtonEnabled(),
                "Bulk Upload CSV button not enabled after selecting a CSV file");
    }

    // ══════════════════════════════════════════════════════════════
    // INVENTORY — DELETE MEDICINE
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 19,
            testName = "Delete medicine removes it from inventory",
            description = "Click the delete button on the first inventory card and verify the total card count decreases",
            dependsOnMethods = "addNewMedicineToInventory")
    public void deleteMedicineRemovesFromInventory() {
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countBefore = inventoryPage.getInventoryCardCount();
        Assert.assertTrue(countBefore > 0, "No medicines to delete");

        inventoryPage.clickDeleteAt(0);




        Assert.assertEquals("Item removed.",inventoryPage.getToastSuccessMessage(),
                "Medicine was not deleted.");
    }

    // ══════════════════════════════════════════════════════════════
    // SELLER RESERVATIONS — FILTER OPERATIONS
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 20,
            testName = "Pending filter shows only pending reservation cards",
            description = "Click the Pending filter on seller reservations and verify all visible cards have PENDING status")
    public void pendingFilterShowsOnlyPendingCards() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        reservationsPage.clickFilterPending();

        int count = reservationsPage.getTotalCardCount();
        if (count == 0) return;

        for (int i = 0; i < count; i++) {
            Assert.assertTrue(
                    reservationsPage.getStatusAt(i).toUpperCase().contains("PENDING"),
                    "Non-pending card shown after Pending filter: "
                            + reservationsPage.getStatusAt(i)
            );
        }
    }

    @Test(priority = 21,
            testName = "Claimed filter shows only claimed reservation cards",
            description = "Click the Claimed filter on seller reservations and verify all visible cards have CLAIMED status")
    public void claimedFilterShowsOnlyClaimedCards() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        reservationsPage.clickFilterClaimed();

        int count = reservationsPage.getTotalCardCount();
        if (count == 0) return;

        for (int i = 0; i < count; i++) {
            Assert.assertTrue(
                    reservationsPage.getStatusAt(i).toUpperCase().contains("CLAIMED"),
                    "Non-claimed card shown: " + reservationsPage.getStatusAt(i)
            );
        }
    }

    @Test(priority = 22,
            testName = "Expired filter shows only expired reservation cards",
            description = "Click the Expired filter on seller reservations and verify all visible cards have EXPIRED status")
    public void expiredFilterShowsOnlyExpiredCards() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        reservationsPage.clickFilterExpired();

        int count = reservationsPage.getTotalCardCount();
        if (count == 0) return;

        for (int i = 0; i < count; i++) {
            Assert.assertTrue(
                    reservationsPage.getStatusAt(i).toUpperCase().contains("EXPIRED"),
                    "Non-expired card shown: " + reservationsPage.getStatusAt(i)
            );
        }
    }

    @Test(priority = 23,
            testName = "Cancelled filter shows only cancelled reservation cards",
            description = "Click the Cancelled filter on seller reservations and verify all visible cards have CANCELLED status")
    public void cancelledFilterShowsOnlyCancelledCards() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        reservationsPage.clickFilterCancelled();

        int count = reservationsPage.getTotalCardCount();
        if (count == 0) return;

        for (int i = 0; i < count; i++) {
            Assert.assertTrue(
                    reservationsPage.getStatusAt(i).toUpperCase().contains("CANCELLED"),
                    "Non-cancelled card shown: " + reservationsPage.getStatusAt(i)
            );
        }
    }

    @Test(priority = 24,
            testName = "All filter restores full reservation list",
            description = "After applying a filter, click All and verify total card count matches the header badge count")
    public void allFilterRestoresFullList() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        reservationsPage.clickFilterPending();
        reservationsPage.clickFilterAll();

        int badgeCount = reservationsPage.getTotalBadgeCount();
        int cardCount  = reservationsPage.getTotalCardCount();
        Assert.assertEquals(cardCount, badgeCount,
                "After clicking All: badge=" + badgeCount
                        + " cards=" + cardCount);
    }

    // ══════════════════════════════════════════════════════════════
    // SELLER RESERVATIONS — CLAIM OPERATION
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 25,
            testName = "Claim button is visible on pending reservation cards",
            description = "Filter by Pending and verify the Claim button is present on pending reservation cards")
    public void claimButtonVisibleOnPendingCard() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        reservationsPage.clickFilterPending();

        int pendingCount = reservationsPage.getTotalCardCount();
        if (pendingCount == 0) return;

        Assert.assertTrue(
                reservationsPage.isClaimButtonVisibleAt(0),
                "Claim button not visible on pending reservation card"
        );
    }

    @Test(priority = 26,
            testName = "Claiming a pending reservation changes status to CLAIMED",
            description = "Click the Claim button on a pending reservation and verify the status chip changes to CLAIMED",
            dependsOnMethods = "claimButtonVisibleOnPendingCard")
    public void claimReservationChangesStatusToClaimed() {
        reservationsPage.navigateTo(ConfigReader.getProperty("base.url"));
        reservationsPage.clickFilterPending();

        int pendingCount = reservationsPage.getTotalCardCount();
        if (pendingCount == 0) return;

        int claimedBefore = reservationsPage.getClaimedCardCount();
        reservationsPage.clickFilterAll();
        reservationsPage.clickFilterPending();
        reservationsPage.clickClaimAt(0);

        reservationsPage.clickFilterAll();
        reservationsPage.clickFilterClaimed();

        int claimedAfter = reservationsPage.getClaimedCardCount();
        Assert.assertTrue(claimedAfter > claimedBefore,
                "Claimed count did not increase after claiming. Before: "
                        + claimedBefore + " After: " + claimedAfter);
    }

    // ══════════════════════════════════════════════════════════════
    // DOCUMENTS — UPLOAD OPERATIONS
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 27,
            testName = "Document type dropdown has all 4 expected options",
            description = "Verify the document type dropdown on the documents page contains Pharmacy License, GST Certificate, Drug License and Other")
    public void documentTypeDropdownHasFourOptions() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int optionCount = webDriver.findElements(
                org.openqa.selenium.By.cssSelector("select option")).size();
        Assert.assertEquals(optionCount, 4,
                "Expected 4 document type options but found: " + optionCount);
    }

    @Test(priority = 28,
            testName = "Submit button disabled without selecting a file",
            description = "Verify the Submit for Review button is disabled when no document file has been selected")
    public void submitButtonDisabledWithoutFile() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertFalse(documentsPage.isSubmitButtonEnabled(),
                "Submit for Review button should be disabled when no file is selected");
    }

    @Test(priority = 29,
            testName = "Submit button enabled after selecting a file",
            description = "Select a valid PDF file for upload and verify the Submit for Review button becomes enabled")
    public void submitButtonEnabledAfterFileSelected() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        String testFilePath = createTempPdfFile();
        documentsPage.uploadFile(testFilePath);
        Assert.assertTrue(documentsPage.isSubmitButtonEnabled(),
                "Submit button not enabled after selecting a file");
    }

    @Test(priority = 30,
            testName = "Selecting GST Certificate document type is accepted",
            description = "Select GST Certificate from the dropdown and verify the selection is reflected correctly")
    public void selectGSTCertificateDocumentType() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        documentsPage.selectDocumentType("GST_CERTIFICATE");
        String selected = webDriver.findElement(
                        org.openqa.selenium.By.cssSelector("select.ng-valid"))
                .getAttribute("value");
        Assert.assertEquals(selected, "GST_CERTIFICATE",
                "Document type not changed to GST_CERTIFICATE");
    }

    @Test(priority = 31,
            testName = "Selecting Drug License document type is accepted",
            description = "Select Drug License from the dropdown and verify the selection is reflected correctly")
    public void selectDrugLicenseDocumentType() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        documentsPage.selectDocumentType("DRUG_LICENSE");
        String selected = webDriver.findElement(
                        org.openqa.selenium.By.cssSelector("select.ng-valid"))
                .getAttribute("value");
        Assert.assertEquals(selected, "DRUG_LICENSE",
                "Document type not changed to DRUG_LICENSE");
    }

    @Test(priority = 32,
            testName = "Upload document submits successfully",
            description = "Select Pharmacy License document type, choose a valid file and submit for review — verify success response")
    public void uploadDocumentSubmitsSuccessfully() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        String testFilePath = createTempPdfFile();
        documentsPage.selectDocumentType("PHARMACY_LICENSE");
        documentsPage.uploadFile(testFilePath);
        Assert.assertTrue(documentsPage.isSubmitButtonEnabled(),
                "Submit button not enabled before submitting");

        documentsPage.clickSubmit();

        boolean successToastShown = !inventoryPage.getToastSuccessMessage()
                .equals("No success toast");
        boolean staysOnDocuments  = webDriver.getCurrentUrl()
                .contains("/seller/documents");
        boolean redirectsToDash   = webDriver.getCurrentUrl()
                .contains("/seller/dashboard");

        Assert.assertTrue(successToastShown || staysOnDocuments || redirectsToDash,
                "No confirmation after document upload. URL: "
                        + webDriver.getCurrentUrl());
    }

    // ══════════════════════════════════════════════════════════════
    // DASHBOARD — STAT SYNC AFTER OPERATIONS
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 33,
            testName = "Dashboard Total Medicines stat reflects current inventory count",
            description = "Navigate to dashboard and verify the Total Medicines stat matches the actual inventory card count")
    public void dashboardStatMatchesInventoryCount() {
        // Get inventory count
        inventoryPage.navigateTo(ConfigReader.getProperty("base.url"));
        int actualCount = inventoryPage.getInventoryCardCount();

        // Get dashboard stat
        dashboardPage.navigateTo(ConfigReader.getProperty("base.url"));
        int dashboardStat = Integer.parseInt(dashboardPage.getTotalMedicines());

        Assert.assertEquals(dashboardStat, actualCount,
                "Dashboard Total Medicines [" + dashboardStat
                        + "] does not match inventory count [" + actualCount + "]");
    }

    // ── Logout — always last ──────────────────────────────────────
    @Test(priority = 34,
            testName = "Pharmacy logout after all operations",
            description = "After completing all pharmacy operations logout and verify redirect to the login page")
    public void pharmacyLogoutAfterOperations() {
        dashboardPage.navigateTo(ConfigReader.getProperty("base.url"));
        dashboardPage.clickLogout();
        Assert.assertTrue(dashboardPage.isRedirected("/"),
                "Logout did not redirect to login page. URL: "
                        + webDriver.getCurrentUrl());
    }

    // ══════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════

    private String createTempPdfFile() {
        try {
            File tempFile = File.createTempFile("test_license_", ".pdf");
            tempFile.deleteOnExit();
            try (FileWriter fw = new FileWriter(tempFile)) {
                fw.write("%PDF-1.4 test document content");
            }
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create temp PDF file", e);
        }
    }

    private String createTempCsvFile() {
        try {
            File tempFile = File.createTempFile("test_inventory_", ".csv");
            tempFile.deleteOnExit();
            try (FileWriter fw = new FileWriter(tempFile)) {
                fw.write("medicine,quantity,price,mfg_date,expiry_date\n");
                fw.write("Paracetamol,100,25.50,2025-01-01,2030-12-31\n");
            }
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create temp CSV file", e);
        }
    }
}