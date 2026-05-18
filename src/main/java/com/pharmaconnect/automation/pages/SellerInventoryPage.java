package com.pharmaconnect.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SellerInventoryPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Navigation ────────────────────────────────────────────────
    private By logo      = By.cssSelector("a.pc-logo");
    private By logoutBtn = By.cssSelector("button.pc-logout");

    // ── Page Header ───────────────────────────────────────────────
    private By pageTitle         = By.cssSelector(".pc-page-title");
    private By pageSubtitle      = By.cssSelector(".pc-page-subtitle");
    private By addMedicineToggle = By.cssSelector(".pc-page-header button.pc-btn-primary");

    // ── Add Medicine Form ─────────────────────────────────────────
    private By addFormCard       = By.cssSelector(".add-form-card");
    private By medicineSelect    = By.cssSelector(".add-form-card select.pc-select");
    private By quantityInput     = By.cssSelector("input.pc-input[placeholder='e.g. 100']");
    private By priceInput        = By.cssSelector("input.pc-input[placeholder='e.g. 25.50']");
    private By mfgDateInput      = By.cssSelector(".add-form-card input[type='date']:nth-of-type(1)");
    private By expiryDateInput   = By.xpath("(//input[@type='date'])[2]");
    private By addToInventoryBtn = By.cssSelector(".add-form-card button.pc-btn-primary");

    // ── Edit Form (inline on card) ────────────────────────────────
    private By editInput  = By.cssSelector("input.edit-input");
    private By saveBtn    = By.cssSelector("button.btn-save");
    private By cancelBtn  = By.cssSelector("button.btn-cancel");

    // ── Bulk Upload ───────────────────────────────────────────────
    private By bulkFileInput  = By.cssSelector("input.bulk-file-input");
    private By bulkUploadBtn  = By.cssSelector("button.bulk-upload-btn");
    private By showFormatBtn  = By.cssSelector("button.bulk-help-toggle");
    private By bulkHelp       = By.cssSelector(".bulk-help");

    private By errorMessage = By.cssSelector(".pc-alert.pc-alert-error");
    // ── Search & Sort ─────────────────────────────────────────────
    private By searchInput  = By.cssSelector(".pc-search input");
    private By sortDropdown = By.cssSelector("select.pc-select[style*='padding']");

    // ── Inventory Cards ───────────────────────────────────────────
    private By allCards   = By.cssSelector(".inv-card");
    private By emptyState = By.cssSelector(".pc-state, .empty-state");

    // ── Card Inner Elements ───────────────────────────────────────
    private By invName     = By.cssSelector(".inv-name");
    private By stockBadge  = By.cssSelector(".stock-badge");
    private By statQty     = By.cssSelector(".stat-block:nth-child(1) .stat-val");
    private By statPrice   = By.cssSelector(".stat-block:nth-child(2) .stat-val");
    private By statExpiry  = By.cssSelector(".stat-block:nth-child(3) .stat-val");
    private By adjQty      = By.cssSelector(".adj-qty");
    private By decreaseBtn = By.cssSelector("button.btn-adj:nth-child(1)");
    private By increaseBtn = By.cssSelector("button.btn-adj:nth-child(3)");
    private By editBtn     = By.cssSelector("button.btn-edit");
    private By deleteBtn   = By.cssSelector("button.btn-del");

    // ── Toast ─────────────────────────────────────────────────────
    private By toastSuccess = By.cssSelector(".pc-toast.pc-toast-success");
    private By toastError   = By.cssSelector(".toast-error");

    public SellerInventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ── Navigation ────────────────────────────────────────────────
    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/seller/inventory");
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    // ── Add Medicine Form ─────────────────────────────────────────
    public void clickAddMedicineToggle() {
        wait.until(ExpectedConditions.elementToBeClickable(addMedicineToggle)).click();
    }

    public boolean isAddFormVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(addFormCard))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void selectMedicine(String visibleText) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(medicineSelect));
        new Select(driver.findElement(medicineSelect)).selectByVisibleText(visibleText);
    }

    public void selectMedicineByIndex(int index) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(medicineSelect));
        Thread.sleep(1000);
        new Select(driver.findElement(medicineSelect)).selectByIndex(index);
    }

    public int getMedicineOptionsCount() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(medicineSelect));
        Thread.sleep(1000);
        return new Select(driver.findElement(medicineSelect)).getOptions().size();
    }

    public void enterQuantity(String qty) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(quantityInput));
        input.clear();
        input.sendKeys(qty);
    }

    public String getErrorMessage(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    public void clickMedicineSelect(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(medicineSelect)).click();
    }

    public void enterPrice(String price) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(priceInput));
        input.clear();
        input.sendKeys(price);
    }

    public void enterManufacturingDate(String date) {
        // date format: yyyy-MM-dd
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(mfgDateInput));
        input.sendKeys(date);
    }

    public void enterExpiryDate(String date) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(expiryDateInput));
        input.sendKeys(date);
    }

    public void clickAddToInventory() {
        wait.until(ExpectedConditions.elementToBeClickable(addToInventoryBtn)).click();
    }

    public boolean isAddToInventoryButtonEnabled() {
        return driver.findElement(addToInventoryBtn).isEnabled();
    }

    // ── Full add form helper ──────────────────────────────────────
    public void addMedicine(int medicineIndex, String qty, String price,
                            String mfgDate, String expiryDate) throws InterruptedException {
        clickAddMedicineToggle();
        wait.until(ExpectedConditions.visibilityOfElementLocated(addFormCard));
        selectMedicineByIndex(medicineIndex);
        enterQuantity(qty);
        enterPrice(price);
        enterManufacturingDate(mfgDate);
        enterExpiryDate(expiryDate);
        clickAddToInventory();
    }

    // ── Inventory Cards ───────────────────────────────────────────
    private List<WebElement> getFreshCards() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(allCards));
    }

    public int getInventoryCardCount() {
        return driver.findElements(allCards).size();
    }

    public boolean isEmptyStateVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(emptyState))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getMedicineNameAt(int index) {
        return getFreshCards().get(index).findElement(invName).getText();
    }

    public String getQuantityAt(int index) {
        return getFreshCards().get(index).findElement(adjQty).getText().trim();
    }

    public String getPriceAt(int index) {
        return getFreshCards().get(index).findElement(statPrice).getText().trim();
    }

    public String getExpiryAt(int index) {
        return getFreshCards().get(index).findElement(statExpiry).getText().trim();
    }

    public String getStockBadgeAt(int index) {
        try {
            return getFreshCards().get(index).findElement(stockBadge).getText().trim();
        } catch (Exception e) {
            return "Normal";
        }
    }

    // ── Adjust Quantity (+/-) ─────────────────────────────────────
    public void clickIncreaseQtyAt(int index) {
        getFreshCards().get(index).findElement(increaseBtn).click();
    }

    public void clickDecreaseQtyAt(int index) {
        getFreshCards().get(index).findElement(decreaseBtn).click();
    }

    // ── Edit ──────────────────────────────────────────────────────
    public void clickEditAt(int index) {
        getFreshCards().get(index).findElement(editBtn).click();
    }

    public boolean isEditFormVisibleAt(int index) {
        try {
            return getFreshCards().get(index).findElement(editInput).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickSaveAt(int index) {
        getFreshCards().get(index).findElement(saveBtn).click();
    }

    public void clickCancelEditAt(int index) {
        getFreshCards().get(index).findElement(cancelBtn).click();
    }

    // ── Delete ────────────────────────────────────────────────────
    public void clickDeleteAt(int index) {
        getFreshCards().get(index).findElement(deleteBtn).click();
    }

    // ── Search & Sort ─────────────────────────────────────────────
    public void searchMedicine(String name) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(name);
    }

    public void clearSearch() {
        driver.findElement(searchInput).clear();
    }

    public void sortBy(String value) {
        new Select(wait.until(
                ExpectedConditions.visibilityOfElementLocated(sortDropdown)))
                .selectByValue(value);
    }

    // ── Bulk Upload ───────────────────────────────────────────────
    public void clickShowFormat() {
        wait.until(ExpectedConditions.elementToBeClickable(showFormatBtn)).click();
    }

    public boolean isBulkHelpVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(bulkHelp))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void uploadBulkCsv(String filePath) {
        driver.findElement(bulkFileInput).sendKeys(filePath);
    }

    public boolean isBulkUploadButtonEnabled() {
        return driver.findElement(bulkUploadBtn).isEnabled();
    }

    // ── Toast ─────────────────────────────────────────────────────
    public String getToastSuccessMessage() {
        try {
            // ✅ Handle alert if present before looking for toast
            try {
                wait.withTimeout(Duration.ofSeconds(2))
                        .until(ExpectedConditions.alertIsPresent());
                driver.switchTo().alert().accept();
            } catch (TimeoutException ignored) {
                // No alert — continue normally
            }

            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastSuccess))
                    .getText();
        } catch (TimeoutException e) {
            return "No success toast";
        }
    }

    public String getToastErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastError))
                    .getText();
        } catch (TimeoutException e) {
            return "No error toast";
        }
    }

    public boolean isRedirected(String url) {
        try {
            return wait.until(ExpectedConditions.urlContains(url));
        } catch (TimeoutException e) {
            return false;
        }
    }
    // ── Alert Handling ────────────────────────────────────────────
    public boolean isAlertPresent() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getAlertText() {
        try {
            return wait.until(ExpectedConditions.alertIsPresent()).getText();
        } catch (TimeoutException e) {
            return "No alert";
        }
    }

    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    public void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
    }

    // Call after clickAddToInventory() to handle either success or duplicate alert
    public void handlePostAddAction() {
        try {
            wait.until(ExpectedConditions.alertIsPresent()).dismiss(); // dismiss duplicate
        } catch (TimeoutException e) {
            // No alert — normal success, do nothing
        }
    }
}