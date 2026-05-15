package com.pharmaconnect.automation.manager;

import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.pages.RegisterPage;
import org.openqa.selenium.WebDriver;

public class PageObjectManager {

    private WebDriver driver;

    private LoginPage loginPage;
    private RegisterPage registerPage;

    public PageObjectManager(WebDriver driver){
        this.driver=driver;
    }

    public LoginPage getLoginPage(){
        if(loginPage == null){
            loginPage = new LoginPage(driver);
        }
        return loginPage;
    }

    public RegisterPage getRegisterPage() {
        if(registerPage==null){
            registerPage = new RegisterPage(driver);
        }
        return registerPage;
    }
}
