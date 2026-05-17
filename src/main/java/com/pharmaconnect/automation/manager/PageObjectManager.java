package com.pharmaconnect.automation.manager;

import com.pharmaconnect.automation.pages.*;
import org.openqa.selenium.WebDriver;

public class PageObjectManager {

    private WebDriver driver;

    private LoginPage loginPage;
    private RegisterPage registerPage;
    private RegisterPharmacyPage registerPharmacyPage;
    private SearchPage searchPage;
    private MyReservationsPage myReservationsPage;
    private ProfilePage profilePage;

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

    public RegisterPharmacyPage getRegisterPharmacyPage() {
        if(registerPharmacyPage==null){
            registerPharmacyPage = new RegisterPharmacyPage(driver);
        }
        return registerPharmacyPage;
    }

    public MyReservationsPage getMyReservationsPage() {
        if(myReservationsPage==null){
            myReservationsPage = new MyReservationsPage(driver);
        }
        return myReservationsPage;
    }
    public SearchPage getSearchPage(){
        if(searchPage==null){
            searchPage = new SearchPage(driver);
        }
        return searchPage;
    }
    public ProfilePage getProfilePage(){
        if(profilePage == null){
            profilePage = new ProfilePage(driver);
        }
        return profilePage;
    }
}
