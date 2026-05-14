package com.pharmaconnect.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public LoginPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(15));
    }

    private By emailInput = By.id("email");
    private By passwordInput = By.id("password");
    private By rememberCheckbox = By.cssSelector("input[type='checkbox']");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By pharmacyLoginButton = By.xpath("//button[text()=('Pharmacy')]");
    private By adminLoginButton = By.xpath("//button[text()=('Admin')]");
    private By authTitle = By.cssSelector("h1.pc-auth-title");
    private By alertText = By.cssSelector("div.pc-alert");

    public void enterEmail(String email){
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
    }
    public void enterPassword(String password){
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).sendKeys(password);
    }
    public void clickRememberCheckbox(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(rememberCheckbox)).click();
    }
    public void clickLoginButton(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).click();
    }
    public void switchToPharmacyLogin(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(pharmacyLoginButton)).click();
    }
    public void switchToAdminLogin(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(adminLoginButton)).click();
    }

    public String getAuthTitleText(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(authTitle)).getText();
    }
    public String getAlertText(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(alertText)).getText();
    }

    public void loginAsUser(String email , String password){
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }
    public void loginAsPharmacy(String email , String password){
        switchToPharmacyLogin();
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }
    public void loginAsAdmin(String email , String password){
        switchToAdminLogin();
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isAlertVisible(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(alertText)).isDisplayed();
    }

    public boolean isRedirected(String url){
       try{
           return wait.until(ExpectedConditions.urlContains(url));
       } catch (TimeoutException e) {
           return false;
       }
    }




}
