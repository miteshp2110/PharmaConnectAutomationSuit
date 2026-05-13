package com.pharmaconnect.automation.manager;

import com.pharmaconnect.automation.pages.LoginPage;
import org.openqa.selenium.WebDriver;

public class PageObjectManager {

    private WebDriver driver;

    private LoginPage loginPage;

    public PageObjectManager(WebDriver driver){
        this.driver=driver;
    }

    public LoginPage getLoginPage(){
        if(loginPage == null){
            loginPage = new LoginPage(driver);
        }
        return loginPage;
    }
}
