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

public class SearchPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Navigation ────────────────────────────────────────────────
    private By logo          = By.cssSelector("a.pc-logo");
    private By logoutBtn     = By.cssSelector("button.pc-logout");
    private By hamburgerMenu = By.cssSelector("button.pc-hamburger");

    private By noResultFoundMsg = By.xpath("//h3[text()='No medicines found']");

    // ── Search Modes ──────────────────────────────────────────────
    private By allModeBtns      = By.cssSelector("button.srch-mode-btn");
    private By keywordModeBtn   = By.cssSelector("button.srch-mode-btn:nth-child(1)");
    private By nearestModeBtn   = By.cssSelector("button.srch-mode-btn:nth-child(2)");
    private By emergencyModeBtn = By.cssSelector("button.srch-mode-btn:nth-child(3)");

    // ── Search Input ──────────────────────────────────────────────
    private By searchInput        = By.cssSelector("input.srch-input");
    private By searchBtn          = By.cssSelector("button.srch-btn");
    private By suggestionDropdown = By.cssSelector(".srch-suggestions");
    private By suggestionItems    = By.cssSelector(".srch-suggestion-item");

    // ── Emergency Banner ──────────────────────────────────────────
    private By emergencyBanner    = By.cssSelector(".emrg-banner");
    private By emergencyEnableBtn = By.cssSelector("button.emrg-btn");

    // ── Results Toolbar ───────────────────────────────────────────
    private By resultCount  = By.cssSelector(".srch-count");
    private By sortDropdown = By.cssSelector("select.pc-select");

    // ── Result Cards ──────────────────────────────────────────────
    private By allCards     = By.cssSelector(".srch-card");
    private By medicineName = By.cssSelector(".srch-med-name");
    private By genericName  = By.cssSelector(".srch-generic");
    private By pharmacyName = By.cssSelector(".srch-ph-name");
    private By pharmacyAddr = By.cssSelector(".srch-ph-addr");
    private By price        = By.cssSelector(".srch-price");
    private By distanceRow  = By.cssSelector(".srch-distance-row");
    private By stockInfo    = By.cssSelector(".srch-stock");
    private By badge247     = By.cssSelector(".srch-247-badge");

    // ── Card Action Buttons ───────────────────────────────────────
    private By comparePricesBtn = By.cssSelector("button.srch-action-btn:nth-child(1)");
    private By genericsBtn      = By.cssSelector("button.srch-action-btn:nth-child(2)");
    private By showInMapsBtn    = By.cssSelector("button.srch-maps-btn");

    // ── Reserve ───────────────────────────────────────────────────
    private By reserveNowBtn     = By.cssSelector("button.srch-reserve-btn");
    private By reserveForm       = By.cssSelector(".srch-reserve-form");
    private By qtyInput          = By.cssSelector("input.srch-qty-input");
    private By confirmReserveBtn = By.cssSelector("button.srch-confirm-btn");
    private By cancelReserveBtn  = By.cssSelector("button.srch-cancel-btn");
    private By outOfStockMsg     = By.cssSelector(".srch-oos");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ── Navigation ────────────────────────────────────────────────
    public void clickLogo() {
        wait.until(ExpectedConditions.elementToBeClickable(logo)).click();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }

    public void clickHamburgerMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(hamburgerMenu)).click();
    }

    // ── Search Modes ──────────────────────────────────────────────
    public void clickKeywordMode() {
        wait.until(ExpectedConditions.elementToBeClickable(keywordModeBtn)).click();
    }

    public void clickNearestMode() {
        wait.until(ExpectedConditions.elementToBeClickable(nearestModeBtn)).click();
    }

    public void clickEmergencyMode() {
        wait.until(ExpectedConditions.elementToBeClickable(emergencyModeBtn)).click();
    }

    public boolean isKeywordModeActive() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(keywordModeBtn))
                .getAttribute("class").contains("active");
    }

    public boolean isNearestModeActive() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(nearestModeBtn))
                .getAttribute("class").contains("active");
    }

    public boolean isEmergencyModeActive() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emergencyModeBtn))
                .getAttribute("class").contains("active");
    }

    public int getActiveModeCount() {
        List<WebElement> modes = driver.findElements(allModeBtns);
        return (int) modes.stream()
                .filter(m -> m.getAttribute("class").contains("active"))
                .count();
    }

    // ── Search Input ──────────────────────────────────────────────
    public void enterMedicineName(String medicineName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput)).clear();
        driver.findElement(searchInput).sendKeys(medicineName);
    }

    public void clickSearchButton() {
        wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
    }

    public void searchMedicine(String medicineName) {
        enterMedicineName(medicineName);
        clickSearchButton();
    }

    public boolean isSearchButtonEnabled() {
        return driver.findElement(searchBtn).isEnabled();
    }

    public boolean isSuggestionDropdownVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(suggestionDropdown))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public List<WebElement> getSuggestions() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(suggestionDropdown));
        return driver.findElements(suggestionItems);
    }

    public void clickSuggestion(int index) {
        List<WebElement> suggestions = getSuggestions();
        suggestions.get(index).click();
    }

    public String getSearchInputValue() {
        return driver.findElement(searchInput).getAttribute("value");
    }

    // ── Emergency Banner ──────────────────────────────────────────
    public boolean isEmergencyBannerVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(emergencyBanner))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickEnableEmergencyMode() {
        wait.until(ExpectedConditions.elementToBeClickable(emergencyEnableBtn)).click();
    }

    public boolean isEmergencyModeEnabled() {
        return driver.findElement(emergencyBanner)
                .getAttribute("class").contains("emrg-active");
    }

    // ── Results ───────────────────────────────────────────────────
    public void waitForResults() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(allCards));
        wait.until(ExpectedConditions.visibilityOfElementLocated(medicineName));
    }

    public int getResultCardCount() {
        return driver.findElements(allCards).size();
    }

    public int getResultCountFromToolbar() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(resultCount))
                .getText();
        return Integer.parseInt(text.split(" ")[0]);
    }

    public boolean isNoResultsMessageShown() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(noResultFoundMsg));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ── Card Helper — always fetches fresh list from DOM ──────────
    private List<WebElement> getFreshCards() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(allCards));
    }

    // ── Reserve Form Helper — waits for form to be open ──────────
    private void waitForReserveForm() {
        wait.until(ExpectedConditions.presenceOfElementLocated(reserveForm));
        wait.until(ExpectedConditions.visibilityOfElementLocated(reserveForm));
    }

    // ── Card Field Getters (by card index) ────────────────────────
    public String getMedicineNameAt(int index) {
        return getFreshCards().get(index).findElement(medicineName).getText();
    }

    public String getGenericNameAt(int index) {
        return getFreshCards().get(index).findElement(genericName).getText();
    }

    public String getPharmacyNameAt(int index) {
        return getFreshCards().get(index).findElement(pharmacyName).getText();
    }

    public String getPharmacyAddressAt(int index) {
        return getFreshCards().get(index).findElement(pharmacyAddr).getText();
    }

    public double getPriceAt(int index) {
        String priceText = getFreshCards().get(index).findElement(price).getText();
        return Double.parseDouble(priceText.replace("₹", "").trim());
    }

    public String getDistanceAt(int index) {
        return getFreshCards().get(index).findElement(distanceRow).getText();
    }

    public int getStockAt(int index) {
        String stockText = getFreshCards().get(index).findElement(stockInfo).getText().trim();
        return Integer.parseInt(stockText.split(" ")[0]);
    }

    public boolean is247BadgeShownAt(int index) {
        return !getFreshCards().get(index).findElements(badge247).isEmpty();
    }

    // ── Sort ──────────────────────────────────────────────────────
    public void sortBy(String value) {
        Select select = new Select(wait.until(
                ExpectedConditions.visibilityOfElementLocated(sortDropdown)));
        select.selectByValue(value);
    }

    // ── Reserve ───────────────────────────────────────────────────
    public void clickReserveNow(int cardIndex) {
        getFreshCards().get(cardIndex).findElement(reserveNowBtn).click();
    }

    public boolean isReserveFormVisible(int cardIndex) {
        try {
            waitForReserveForm();
            return getFreshCards().get(cardIndex).findElement(reserveForm).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getDefaultQuantity(int cardIndex) {
        waitForReserveForm();
        return getFreshCards().get(cardIndex).findElement(qtyInput).getAttribute("value");
    }

    public void enterQuantity(int cardIndex, String qty) {
        waitForReserveForm();
        WebElement input = getFreshCards().get(cardIndex).findElement(qtyInput);
        input.clear();
        input.sendKeys(qty);
    }

    public void clickConfirmReservation(int cardIndex) {
        waitForReserveForm();
        getFreshCards().get(cardIndex).findElement(confirmReserveBtn).click();
    }

    public void clickCancelReservation(int cardIndex) {
        waitForReserveForm();
        getFreshCards().get(cardIndex).findElement(cancelReserveBtn).click();
    }

    public boolean isConfirmButtonEnabled(int cardIndex) {
        waitForReserveForm();
        return getFreshCards().get(cardIndex).findElement(confirmReserveBtn).isEnabled();
    }

    public boolean isReservationConfirmed(int cardIndex) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(reserveForm));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isOutOfStockShownAt(int cardIndex) {
        return !getFreshCards().get(cardIndex).findElements(outOfStockMsg).isEmpty();
    }

    // ── Composite ─────────────────────────────────────────────────
    public void searchAndReserve(String medicine, int cardIndex, String qty) {
        searchMedicine(medicine);
        waitForResults();
        clickReserveNow(cardIndex);
        enterQuantity(cardIndex, qty);
        clickConfirmReservation(cardIndex);
    }

    public boolean isRedirected(String url) {
        try {
            return wait.until(ExpectedConditions.urlContains(url));
        } catch (TimeoutException e) {
            return false;
        }
    }
}