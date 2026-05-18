package com.pharmaconnect.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SellerDashboardPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Navigation ────────────────────────────────────────────────
    private By logo        = By.cssSelector("a.pc-logo");
    private By logoutBtn   = By.cssSelector("button.pc-logout");
    private By hamburgerMenu = By.cssSelector("button.pc-hamburger");

    // ── Page Header ───────────────────────────────────────────────
    private By pageBadge    = By.cssSelector(".pc-page-badge");
    private By pageTitle    = By.cssSelector(".pc-page-title");
    private By pageSubtitle = By.cssSelector(".pc-page-subtitle");
    private By verifiedChip = By.cssSelector(".pc-chip.pc-chip-green");

    // ── Stats Cards ───────────────────────────────────────────────
    private By allStats         = By.cssSelector(".pc-stat");
    private By totalMedicinesStat = By.xpath("//span[@class='pc-stat-label' and text()='Total Medicines']/following-sibling::span[@class='pc-stat-val']");
    private By lowStockStat       = By.xpath("//span[@class='pc-stat-label' and text()='Low Stock Items']/following-sibling::span[contains(@class,'pc-stat-val')]");
    private By pendingOrdersStat  = By.xpath("//span[@class='pc-stat-label' and text()='Pending Orders']/following-sibling::span[contains(@class,'pc-stat-val')]");
    private By outOfStockStat     = By.xpath("//span[@class='pc-stat-label' and text()='Out of Stock']/following-sibling::span[contains(@class,'pc-stat-val')]");

    // ── Quick Action Cards ────────────────────────────────────────
    private By manageStockCard     = By.cssSelector("a[href='/seller/inventory']");
    private By viewReservationsCard = By.cssSelector("a[href='/seller/reservations']");
    private By analyticsCard       = By.cssSelector("a[href='/seller/documents']");

    public SellerDashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/seller/dashboard");
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }

    public void clickLogo() {
        wait.until(ExpectedConditions.elementToBeClickable(logo)).click();
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public String getPageBadge() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageBadge)).getText();
    }

    public String getVerifiedChipText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(verifiedChip)).getText().trim();
    }

    public boolean isVerifiedChipVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(verifiedChip)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getTotalMedicines() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(totalMedicinesStat)).getText().trim();
    }

    public String getLowStockCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(lowStockStat)).getText().trim();
    }

    public String getPendingOrdersCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pendingOrdersStat)).getText().trim();
    }

    public String getOutOfStockCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(outOfStockStat)).getText().trim();
    }

    public int getStatCardCount() {
        return driver.findElements(allStats).size();
    }

    public void clickManageStock() {
        wait.until(ExpectedConditions.elementToBeClickable(manageStockCard)).click();
    }

    public void clickViewReservations() {
        wait.until(ExpectedConditions.elementToBeClickable(viewReservationsCard)).click();
    }

    public void clickAnalytics() {
        wait.until(ExpectedConditions.elementToBeClickable(analyticsCard)).click();
    }

    public boolean isDashboardLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
            return true;
        } catch (TimeoutException e) {
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