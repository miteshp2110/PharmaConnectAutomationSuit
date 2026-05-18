package com.pharmaconnect.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPharmacyPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Pharmacy Information ──────────────────────────────────────
    private By pharmacyNameInput   = By.name("pharmacyName");
    private By ownerNameInput      = By.name("ownerName");
    private By licenseNumberInput  = By.name("licenseNumber");
    private By gstNumberInput      = By.name("gstNumber");

    // ── Contact Information ───────────────────────────────────────
    private By emailInput          = By.name("email");
    private By phoneInput          = By.name("phone");
    private By addressInput        = By.name("address");
    private By cityInput           = By.name("city");
    private By pincodeInput        = By.name("pincode");
    private By operatingHoursInput = By.name("operatingHours");

    // ── Location ──────────────────────────────────────────────────
    private By latitudeInput       = By.name("locationLatitude");
    private By longitudeInput      = By.name("locationLongitude");
    private By useCurrentLocationBtn = By.cssSelector("button.pc-btn.pc-btn-outline");

    // ── Account Security ──────────────────────────────────────────
    private By passwordInput       = By.name("password");
    private By confirmPasswordInput = By.name("confirmPassword");

    // ── Checkboxes & Submit ───────────────────────────────────────
    private By open247Checkbox     = By.id("open247");
    private By termsCheckbox       = By.name("agreedToTerms");
    private By registerBtn         = By.cssSelector("button.pc-auth-submit");

    // ── Navigation ────────────────────────────────────────────────
    private By loginLink           = By.cssSelector("a[href='/login']");
    private By backToHomeLink      = By.cssSelector("a.pc-auth-back");

    // ── Alerts ────────────────────────────────────────────────────
    private By errorAlert          = By.cssSelector(".pc-alert-error");
    private By successAlert        = By.cssSelector(".pc-alert-success");

    public RegisterPharmacyPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ── Pharmacy Information ──────────────────────────────────────
    public void enterPharmacyName(String pharmacyName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pharmacyNameInput)).sendKeys(pharmacyName);
    }

    public void enterOwnerName(String ownerName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(ownerNameInput)).sendKeys(ownerName);
    }

    public void enterLicenseNumber(String licenseNumber) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(licenseNumberInput)).sendKeys(licenseNumber);
    }

    public void enterGstNumber(String gstNumber) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gstNumberInput)).sendKeys(gstNumber);
    }

    // ── Contact Information ───────────────────────────────────────
    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
    }

    public void enterPhone(String phone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput)).sendKeys(phone);
    }

    public void enterAddress(String address) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput)).sendKeys(address);
    }

    public void enterCity(String city) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cityInput)).sendKeys(city);
    }

    public void enterPincode(String pincode) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pincodeInput)).sendKeys(pincode);
    }

    public void enterOperatingHours(String hours) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(operatingHoursInput)).sendKeys(hours);
    }

    // ── Location ──────────────────────────────────────────────────
    public void enterLatitude(String latitude) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(latitudeInput)).sendKeys(latitude);
    }

    public void enterLongitude(String longitude) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(longitudeInput)).sendKeys(longitude);
    }

    public void clickUseCurrentLocation() {
        wait.until(ExpectedConditions.elementToBeClickable(useCurrentLocationBtn)).click();
    }

    // ── Account Security ──────────────────────────────────────────
    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordInput)).sendKeys(confirmPassword);
    }

    // ── Checkboxes ────────────────────────────────────────────────
    public void clickOpen247Checkbox() {
        wait.until(ExpectedConditions.elementToBeClickable(open247Checkbox)).click();
    }

    public void clickTermsCheckbox() {
        wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox)).click();
    }

    public boolean isOpen247CheckboxSelected() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(open247Checkbox)).isSelected();
    }

    public boolean isTermsCheckboxSelected() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(termsCheckbox)).isSelected();
    }

    // ── Submit & Navigation ───────────────────────────────────────
    public void clickRegisterButton() {
        wait.until(ExpectedConditions.elementToBeClickable(registerBtn)).click();
    }

    public void clickLoginLink() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }

    public void clickBackToHome() {
        wait.until(ExpectedConditions.elementToBeClickable(backToHomeLink)).click();
    }

    // ── Alerts ────────────────────────────────────────────────────
    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorAlert)).getText();
        } catch (TimeoutException e) {
            return "No error message displayed";
        }
    }

    public String getSuccessMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert)).getText();
        } catch (TimeoutException e) {
            return "No success message displayed";
        }
    }

    public boolean isErrorShownBeforeRedirect(String currentUrl) {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(errorAlert),
                    ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl))
            ));
            return driver.getCurrentUrl().equals(currentUrl)
                    && driver.findElements(errorAlert).size() > 0;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ── Composite Fill Methods ────────────────────────────────────
    public void fillRequiredFields(String pharmacyName, String ownerName, String email,
                                   String phone, String address, String password) {
        enterPharmacyName(pharmacyName);
        enterOwnerName(ownerName);
        enterEmail(email);
        enterPhone(phone);
        enterAddress(address);
        enterPassword(password);
        enterConfirmPassword(password);
    }

    public void fillFullForm(String pharmacyName, String ownerName, String licenseNumber,
                             String gstNumber, String email, String phone, String address,
                             String city, String pincode, String operatingHours,
                             String password) {
        enterPharmacyName(pharmacyName);
        enterOwnerName(ownerName);
        enterLicenseNumber(licenseNumber);
        enterGstNumber(gstNumber);
        enterEmail(email);
        enterPhone(phone);
        enterAddress(address);
        enterCity(city);
        enterPincode(pincode);
        enterOperatingHours(operatingHours);
        enterPassword(password);
        enterConfirmPassword(password);
    }
    public boolean isRedirected(String url){
        try{
            return wait.until(ExpectedConditions.urlContains(url));
        } catch (TimeoutException e) {
            return false;
        }
    }
}