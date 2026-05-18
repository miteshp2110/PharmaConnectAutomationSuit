package com.pharmaconnect.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SellerReservationsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Navigation ────────────────────────────────────────────────
    private By logo      = By.cssSelector("a.pc-logo");
    private By logoutBtn = By.cssSelector("button.pc-logout");

    // ── Page Header ───────────────────────────────────────────────
    private By pageTitle   = By.cssSelector(".page-header h1");
    private By totalBadge  = By.cssSelector("span.total-badge");

    // ── Filter Buttons ────────────────────────────────────────────
    private By allFilters      = By.cssSelector(".filter-row button");
    private By filterAll       = By.cssSelector(".filter-row button:nth-child(1)");
    private By filterPending   = By.cssSelector(".filter-row button:nth-child(2)");
    private By filterClaimed   = By.cssSelector(".filter-row button:nth-child(3)");
    private By filterExpired   = By.cssSelector(".filter-row button:nth-child(4)");
    private By filterCancelled = By.cssSelector(".filter-row button:nth-child(5)");

    // ── Reservation Cards ─────────────────────────────────────────
    private By allCards      = By.cssSelector(".res-card");
    private By pendingCards  = By.cssSelector(".res-card.card-pending");
    private By claimedCards  = By.cssSelector(".res-card.card-claimed");
    private By expiredCards  = By.cssSelector(".res-card.card-expired");
    private By cancelledCards = By.cssSelector(".res-card.card-cancelled");
    private By emptyState    = By.cssSelector(".empty-state");

    // ── Card Inner Elements ───────────────────────────────────────
    private By medicineName = By.cssSelector(".med-name");
    private By qtyBadge     = By.cssSelector(".qty-badge");
    private By statusChip   = By.cssSelector(".status-chip");
    private By claimBtn     = By.cssSelector("button.btn-claim");

    public SellerReservationsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/seller/reservations");
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public int getTotalBadgeCount() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(totalBadge))
                .getText().trim();
        return Integer.parseInt(text.split(" ")[0]);
    }

    // ── Filters ───────────────────────────────────────────────────
    public void clickFilterAll() {
        wait.until(ExpectedConditions.elementToBeClickable(filterAll)).click();
    }

    public void clickFilterPending() {
        wait.until(ExpectedConditions.elementToBeClickable(filterPending)).click();
    }

    public void clickFilterClaimed() {
        wait.until(ExpectedConditions.elementToBeClickable(filterClaimed)).click();
    }

    public void clickFilterExpired() {
        wait.until(ExpectedConditions.elementToBeClickable(filterExpired)).click();
    }

    public void clickFilterCancelled() {
        wait.until(ExpectedConditions.elementToBeClickable(filterCancelled)).click();
    }

    public boolean isFilterActive(By filterBtn) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(filterBtn))
                .getAttribute("class").contains("active");
    }

    public boolean isFilterAllActive() { return isFilterActive(filterAll); }

    // ── Cards ─────────────────────────────────────────────────────
    public int getTotalCardCount() {
        return driver.findElements(allCards).size();
    }

    public int getPendingCardCount() {
        return driver.findElements(pendingCards).size();
    }

    public int getClaimedCardCount() {
        return driver.findElements(claimedCards).size();
    }

    public int getExpiredCardCount() {
        return driver.findElements(expiredCards).size();
    }

    public int getCancelledCardCount() {
        return driver.findElements(cancelledCards).size();
    }

    public boolean isEmptyStateVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(emptyState)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    private List<WebElement> getFreshCards() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(allCards));
    }

    public String getMedicineNameAt(int index) {
        return getFreshCards().get(index).findElement(medicineName).getText();
    }

    public String getStatusAt(int index) {
        return getFreshCards().get(index).findElement(statusChip).getText().trim();
    }

    public String getQuantityAt(int index) {
        return getFreshCards().get(index).findElement(qtyBadge).getText().trim();
    }

    public void clickClaimAt(int index) {
        getFreshCards().get(index).findElement(claimBtn).click();
    }

    public boolean isClaimButtonVisibleAt(int index) {
        try {
            return getFreshCards().get(index).findElement(claimBtn).isDisplayed();
        } catch (Exception e) {
            return false;
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