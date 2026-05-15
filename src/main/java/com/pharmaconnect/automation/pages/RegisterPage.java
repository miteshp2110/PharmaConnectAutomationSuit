package com.pharmaconnect.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    private By fullNameInput = By.name("name");
    private By emailInput = By.name("email");
    private By phoneInput = By.name("phone");
    private By cityInput = By.name("city");
    private By addressInput = By.name("address");
    private By pincodeInput = By.name("pincode");
    private By passwordInput = By.name("password");
    private By confirmPasswordInput = By.name("confirmPassword");

    private By termsCheckbox = By.id("terms");
    private By createAccountBtn = By.cssSelector("button.pc-auth-submit");

    private By signInLink = By.cssSelector("a[href='/login']");
    private By backToHomeLink = By.cssSelector("a.pc-auth-back");
    private By errorAlert = By.cssSelector(".pc-alert-error");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    public void enterFullName(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(fullNameInput)).sendKeys(name);
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
    }

    public void enterPhone(String phone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput)).sendKeys(phone);
    }

    public void enterCity(String city) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cityInput)).sendKeys(city);
    }

    public void enterAddress(String address) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput)).sendKeys(address);
    }

    public void enterPincode(String pincode) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pincodeInput)).sendKeys(pincode);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordInput)).sendKeys(confirmPassword);
    }

    public void clickTermsCheckbox() {
        wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox)).click();
    }

    public void clickCreateAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(createAccountBtn)).click();
    }

    public void clickSignInLink() {
        wait.until(ExpectedConditions.elementToBeClickable(signInLink)).click();
    }

    public void fillRequiredRegistrationFields(String name, String email, String phone, String password) {
        enterFullName(name);
        enterEmail(email);
        enterPhone(phone);
        enterPassword(password);
        enterConfirmPassword(password); // Assuming we want it to match
    }

    public void fillFullRegistrationForm(String name, String email, String phone, String city, String address, String pincode, String password) {
        fillRequiredRegistrationFields(name, email, phone, password);
        enterCity(city);
        enterAddress(address);
        enterPincode(pincode);
    }


    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorAlert)).getText();
        } catch (TimeoutException e) {
            return "No error message displayed";
        }
    }

    public boolean isTermsCheckboxSelected() {
        return driver.findElement(termsCheckbox).isSelected();
    }
    public boolean isErrorShownBeforeRedirect(String registerUrl) {
        try {
            // Wait until EITHER the error appears OR the URL changes
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(errorAlert),
                    ExpectedConditions.not(ExpectedConditions.urlToBe(registerUrl))
            ));

            // Now check what actually happened
            return driver.getCurrentUrl().equals(registerUrl)
                    && driver.findElements(errorAlert).size() > 0;

        } catch (TimeoutException e) {
            return false;
        }
    }
    public void scrollToTop(){
        actions.scrollByAmount(0,1000);
    }
    public boolean isRedirected(String url){
        try{
            return wait.until(ExpectedConditions.urlContains(url));
        } catch (TimeoutException e) {
            return false;
        }
    }
}