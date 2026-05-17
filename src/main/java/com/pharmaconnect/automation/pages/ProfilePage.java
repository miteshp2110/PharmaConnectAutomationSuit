package com.pharmaconnect.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Navigation ────────────────────────────────────────────────
    private By logo          = By.cssSelector("a.pc-logo");
    private By logoutBtn     = By.cssSelector("button.pc-logout");
    private By hamburgerMenu = By.cssSelector("button.pc-hamburger");

    // ── Page Header ───────────────────────────────────────────────
    private By pageTitle    = By.cssSelector(".pc-page-title");
    private By pageSubtitle = By.cssSelector(".pc-page-subtitle");

    // ── Avatar Section ────────────────────────────────────────────
    private By avatarInitial = By.cssSelector(".avatar");
    private By avatarName    = By.cssSelector("p.avatar-name");
    private By avatarRole    = By.cssSelector("p.avatar-role");

    // ── Info Fields ───────────────────────────────────────────────
    private By allInfoRows   = By.cssSelector(".info-row");
    private By allInfoLabels = By.cssSelector(".info-label");
    private By allInfoValues = By.cssSelector(".info-val");

    // Individual field locators using label+value pair structure
    private By fullNameLabel    = By.xpath("//span[@class='info-label' and text()='Full Name']/following-sibling::span[@class='info-val']");
    private By emailLabel       = By.xpath("//span[@class='info-label' and text()='Email Address']/following-sibling::span[@class='info-val']");
    private By phoneLabel       = By.xpath("//span[@class='info-label' and text()='Phone Number']/following-sibling::span[@class='info-val']");
    private By accountRoleLabel = By.xpath("//span[@class='info-label' and text()='Account Role']/following-sibling::span[@class='info-val']");
    private By memberSinceLabel = By.xpath("//span[@class='info-label' and text()='Member Since']/following-sibling::span[@class='info-val']");
    private By accountIdLabel   = By.xpath("//span[@class='info-label' and text()='Account ID']/following-sibling::span[contains(@class,'info-val')]");

    // ── Role Chip ─────────────────────────────────────────────────
    private By roleChip = By.cssSelector(".pc-chip.pc-chip-green");

    // ── Action Buttons ────────────────────────────────────────────
    private By searchMedicinesBtn  = By.cssSelector("a.pc-btn.pc-btn-primary");
    private By myReservationsBtn   = By.cssSelector("a.pc-btn.pc-btn-outline");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────
    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/profile");
    }

    public void clickLogo() {
        wait.until(ExpectedConditions.elementToBeClickable(logo)).click();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }

    public void clickSearchMedicines() {
        wait.until(ExpectedConditions.elementToBeClickable(searchMedicinesBtn)).click();
    }

    public void clickMyReservations() {
        wait.until(ExpectedConditions.elementToBeClickable(myReservationsBtn)).click();
    }

    // ── Page Header ───────────────────────────────────────────────
    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public String getPageSubtitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageSubtitle)).getText();
    }

    // ── Avatar ────────────────────────────────────────────────────
    public String getAvatarInitial() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(avatarInitial))
                .getText().trim();
    }

    public String getAvatarName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(avatarName)).getText();
    }

    public String getAvatarRole() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(avatarRole)).getText();
    }

    // ── Info Field Getters ────────────────────────────────────────
    public String getFullName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(fullNameLabel)).getText();
    }

    public String getEmail() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emailLabel)).getText();
    }

    public String getPhone() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(phoneLabel)).getText();
    }

    public String getAccountRole() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(accountRoleLabel)).getText();
    }

    public String getMemberSince() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(memberSinceLabel)).getText();
    }

    public String getAccountId() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(accountIdLabel)).getText();
    }

    public String getRoleChipText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(roleChip)).getText();
    }

    // ── Verification Helpers ──────────────────────────────────────
    public boolean isAvatarInitialCorrect(String fullName) {
        String expected = String.valueOf(fullName.charAt(0)).toUpperCase();
        return getAvatarInitial().equals(expected);
    }

    public boolean isProfileLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(avatarName));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccountIdValid() {
        // UUID format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        String id = getAccountId();
        return id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    // ── Composite: Verify Full Profile ───────────────────────────
    public boolean verifyProfileDetails(String expectedName, String expectedEmail,
                                        String expectedPhone, String expectedRole) {
        return getFullName().equalsIgnoreCase(expectedName)
                && getEmail().equalsIgnoreCase(expectedEmail)
                && getPhone().equals(expectedPhone)
                && getAccountRole().equalsIgnoreCase(expectedRole);
    }
    public boolean isRedirected(String url){
        try{
            return wait.until(ExpectedConditions.urlContains(url));
        } catch (TimeoutException e) {
            return false;
        }
    }
}