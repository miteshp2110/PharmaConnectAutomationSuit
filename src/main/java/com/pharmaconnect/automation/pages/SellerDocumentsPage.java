package com.pharmaconnect.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SellerDocumentsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Navigation ────────────────────────────────────────────────
    private By logo      = By.cssSelector("a.pc-logo");
    private By logoutBtn = By.cssSelector("button.pc-logout");

    // ── Page Header ───────────────────────────────────────────────
    private By pageTitle    = By.cssSelector(".page-header h1");
    private By pageSubtitle = By.cssSelector(".page-header p");

    // ── Info Banner ───────────────────────────────────────────────
    private By infoBanner      = By.cssSelector(".info-banner");
    private By infoBannerTitle = By.cssSelector(".info-body strong");

    // ── Upload Form ───────────────────────────────────────────────
    private By documentTypeSelect = By.cssSelector("select.ng-valid");
    private By fileInput          = By.cssSelector("input[type='file']#docFile");
    private By submitBtn          = By.cssSelector("button.btn-upload");

    // ── Steps ─────────────────────────────────────────────────────
    private By stepsCard = By.cssSelector(".steps-card");
    private By allSteps  = By.cssSelector(".step");

    public SellerDocumentsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/seller/documents");
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public String getPageSubtitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageSubtitle)).getText();
    }

    public boolean isInfoBannerVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(infoBanner)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getInfoBannerTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(infoBannerTitle)).getText();
    }

    public void selectDocumentType(String value) {
        new Select(wait.until(
                ExpectedConditions.visibilityOfElementLocated(documentTypeSelect)))
                .selectByValue(value);
    }

    public void uploadFile(String filePath) {
        driver.findElement(fileInput).sendKeys(filePath);
    }

    public boolean isSubmitButtonEnabled() {
        return driver.findElement(submitBtn).isEnabled();
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
    }

    public int getStepCount() {
        return driver.findElements(allSteps).size();
    }

    public boolean isStepsCardVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(stepsCard)).isDisplayed();
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