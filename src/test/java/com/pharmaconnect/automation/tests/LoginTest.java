package com.pharmaconnect.automation.tests;

import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.manager.PageObjectManager;
import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(testName = "Empty Fields Validation", description = "This test is to validate that the login functionality does not work it there are fields entered")
    public void testEmptyFields(){
        webDriver.get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = pageObjectManager.getLoginPage();
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.isAlertVisible(),"The alert message was not displayed");
    }
}
