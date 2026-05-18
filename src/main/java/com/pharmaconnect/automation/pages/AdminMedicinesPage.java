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

public class AdminMedicinesPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Navigation ────────────────────────────────────────────────
    private By logoutBtn = By.cssSelector("button.pc-logout");

    // ── Page Header ───────────────────────────────────────────────
    private By pageTitle    = By.cssSelector(".page-header h1");
    private By pageSubtitle = By.cssSelector(".page-header p");

    // ── Header Toggle Buttons ─────────────────────────────────────
    // Default: "+ Add Medicine" | When open: "✕ Cancel"
    private By addMedicineToggleBtn = By.cssSelector(".header-actions button.btn-primary");
    // Default: "🔗 Link Alternatives" | When open: "✕ Cancel"
    private By linkAlternativesToggleBtn = By.cssSelector(".header-actions button.btn-secondary");

    // ── Add Medicine Panel ────────────────────────────────────────
    private By addMedicinePanel      = By.xpath("//div[contains(@class,'panel') and .//h3[text()='Add New Medicine']]");
    private By nameInput             = By.cssSelector("input[placeholder='e.g. Paracetamol']");
    private By genericInput          = By.cssSelector("input[placeholder='e.g. Acetaminophen']");
    private By categoryInput         = By.cssSelector("input[placeholder='e.g. Analgesic']");
    private By manufacturerInput     = By.cssSelector("input[placeholder='e.g. Sun Pharma']");
    private By dosageFormInput       = By.cssSelector("input[placeholder='e.g. Tablet']");
    private By strengthInput         = By.cssSelector("input[placeholder='e.g. 500mg']");
    private By addMedicineSubmitBtn  = By.xpath(
            "//div[contains(@class,'panel') and .//h3[text()='Add New Medicine']]"
                    + "//button[normalize-space()='Add Medicine']");
    private By addFormInlineError    = By.xpath(
            "//div[contains(@class,'panel') and .//h3[text()='Add New Medicine']]"
                    + "//div[contains(@class,'inline-error')]");

    // ── Link Generic Alternatives Panel ──────────────────────────
    private By linkAlternativesPanel    = By.xpath(
            "//div[contains(@class,'panel') and .//h3[text()='Link Generic Alternatives']]");
    private By brandMedicineSelect      = By.xpath(
            "//label[text()='Brand Medicine']/following-sibling::select");
    private By genericAlternativeSelect = By.xpath(
            "//label[text()='Generic Alternative']/following-sibling::select");
    private By equivalenceNoteInput     = By.cssSelector(
            "input[placeholder='e.g. Same active ingredient, lower cost']");
    private By linkAlternativesSubmitBtn = By.xpath(
            "//div[contains(@class,'panel') and .//h3[text()='Link Generic Alternatives']]"
                    + "//button[normalize-space()='Link Alternatives']");
    private By linkFormInlineError      = By.xpath(
            "//div[contains(@class,'panel') and .//h3[text()='Link Generic Alternatives']]"
                    + "//div[contains(@class,'inline-error')]");

    // ── Search ────────────────────────────────────────────────────
    private By searchInput = By.cssSelector("input.search-input");
    private By countLabel  = By.cssSelector("span.count-label");

    // ── Table ─────────────────────────────────────────────────────
    private By tableHeaders = By.cssSelector("table.data-table thead th");
    private By tableRows    = By.cssSelector("table.data-table tbody tr");

    // ── Toast ─────────────────────────────────────────────────────
    private By toastSuccess = By.cssSelector(".pc-toast.pc-toast-success");
    private By toastError   = By.cssSelector(".toast-error");
    private By inlineSuccessMsg = By.cssSelector("div.inline-success");
    private By inlineErrorMsg = By.cssSelector("div.inline-error");

    public AdminMedicinesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ── Navigation ────────────────────────────────────────────────
    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/admin/medicines");
    }

    public String getInlineSuccessMessage(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(inlineSuccessMsg)).getText();
    }
    public String getInlineErrorMessage(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(inlineErrorMsg)).getText();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }

    // ── Page Header ───────────────────────────────────────────────
    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle))
                .getText().trim();
    }

    public String getPageSubtitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageSubtitle))
                .getText().trim();
    }

    public int getTotalMedicineCountFromSubtitle() {
        try {
            return Integer.parseInt(getPageSubtitle().split(" ")[0]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ── Header Toggle Buttons ─────────────────────────────────────
    public void clickAddMedicineToggle() {
        wait.until(ExpectedConditions.elementToBeClickable(addMedicineToggleBtn)).click();
    }

    public void clickLinkAlternativesToggle() {
        wait.until(ExpectedConditions.elementToBeClickable(linkAlternativesToggleBtn)).click();
    }

    public String getAddMedicineToggleBtnText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addMedicineToggleBtn))
                .getText().trim();
    }

    public String getLinkAlternativesToggleBtnText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(linkAlternativesToggleBtn))
                .getText().trim();
    }

    // ── Panel Visibility ──────────────────────────────────────────
    public boolean isAddMedicinePanelVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(addMedicinePanel))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isLinkAlternativesPanelVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(linkAlternativesPanel))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ── Add Medicine Form Fields ──────────────────────────────────
    public void enterName(String name) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        el.clear();
        el.sendKeys(name);
    }

    public void enterGenericName(String generic) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(genericInput));
        el.clear();
        el.sendKeys(generic);
    }

    public void enterCategory(String category) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(categoryInput));
        el.clear();
        el.sendKeys(category);
    }

    public void enterManufacturer(String manufacturer) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(manufacturerInput));
        el.clear();
        el.sendKeys(manufacturer);
    }

    public void enterDosageForm(String form) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(dosageFormInput));
        el.clear();
        el.sendKeys(form);
    }

    public void enterStrength(String strength) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(strengthInput));
        el.clear();
        el.sendKeys(strength);
    }

    public void clickAddMedicine() {
        wait.until(ExpectedConditions.elementToBeClickable(addMedicineSubmitBtn)).click();
    }

    public boolean isAddFormErrorVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(addFormInlineError))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getAddFormError() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(addFormInlineError))
                    .getText().trim();
        } catch (TimeoutException e) {
            return "No error";
        }
    }

    // ── Link Generic Alternatives Form Fields ─────────────────────
    public void selectBrandMedicine(int index) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(brandMedicineSelect));
        new Select(driver.findElement(brandMedicineSelect)).selectByIndex(index);
    }

    public void selectGenericAlternative(int index) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(genericAlternativeSelect));
        new Select(driver.findElement(genericAlternativeSelect)).selectByIndex(index);
    }

    public void enterEquivalenceNote(String note) {
        WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(equivalenceNoteInput));
        el.clear();
        el.sendKeys(note);
    }

    public void clickLinkAlternatives() {
        wait.until(ExpectedConditions.elementToBeClickable(linkAlternativesSubmitBtn)).click();
    }

    public boolean isLinkFormErrorVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(linkFormInlineError))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getLinkFormError() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(linkFormInlineError))
                    .getText().trim();
        } catch (TimeoutException e) {
            return "No error";
        }
    }

    public int getBrandMedicineOptionsCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(brandMedicineSelect));
        return new Select(driver.findElement(brandMedicineSelect)).getOptions().size();
    }

    // ── Search ────────────────────────────────────────────────────
    public void searchMedicine(String query) {
        WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchInput));
        el.clear();
        el.sendKeys(query);
    }

    public void clearSearch() {
        driver.findElement(searchInput).clear();
    }

    public String getCountLabel() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(countLabel))
                .getText().trim();
    }

    public int getCountFromLabel() {
        try {
            return Integer.parseInt(getCountLabel().split(" ")[0]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ── Table ─────────────────────────────────────────────────────
    public int getTableHeaderCount() {
        return driver.findElements(tableHeaders).size();
    }

    public int getTableRowCount() {
        return driver.findElements(tableRows).size();
    }

    public String getMedicineNameAt(int rowIndex) {
        return driver.findElements(tableRows)
                .get(rowIndex)
                .findElement(By.cssSelector("td.med-name"))
                .getText().trim();
    }

    public String getGenericNameAt(int rowIndex) {
        List<WebElement> cells = driver.findElements(tableRows)
                .get(rowIndex)
                .findElements(By.cssSelector("td"));
        return cells.size() > 1 ? cells.get(1).getText().trim() : "";
    }

    public String getCategoryAt(int rowIndex) {
        List<WebElement> cells = driver.findElements(tableRows)
                .get(rowIndex)
                .findElements(By.cssSelector("td"));
        try {
            return cells.size() > 2
                    ? cells.get(2).findElement(By.cssSelector(".cat-chip")).getText().trim()
                    : "";
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isMedicineInTable(String medicineName) {
        for (WebElement row : driver.findElements(tableRows)) {
            if (row.findElement(By.cssSelector("td.med-name"))
                    .getText().trim()
                    .equalsIgnoreCase(medicineName)) {
                return true;
            }
        }
        return false;
    }

    // ── Toast ─────────────────────────────────────────────────────
    public String getToastSuccessMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastSuccess))
                    .getText().trim();
        } catch (TimeoutException e) {
            return "No success toast";
        }
    }

    public String getToastErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastError))
                    .getText().trim();
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
}