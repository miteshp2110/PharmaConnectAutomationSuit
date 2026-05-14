package com.pharmaconnect.automation.tests;

import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import com.pharmaconnect.automation.utils.ExcelUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(testName = "Empty Fields Validation", description = "This test is to validate that the login functionality does not work it there are fields entered")
    public void validateEmptyFields(){
        webDriver.get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = pageObjectManager.getLoginPage();
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.isAlertVisible(),"The alert message was not displayed");
    }

    @Test(testName = "Auth title Validation", description = "Verify it displays correct Auth Title for different login roles")
    public void validateAuthTitleForUser(){
        webDriver.get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = pageObjectManager.getLoginPage();
        String authTitle = loginPage.getAuthTitleText();
        Assert.assertEquals(authTitle,"User Login","The Auth Title did not match the User Login Role");
    }
    @Test(testName = "Auth title Validation", description = "Verify it displays correct Auth Title for different login roles")
    public void validateAuthTitleForPharmacy(){
        webDriver.get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = pageObjectManager.getLoginPage();
        loginPage.switchToPharmacyLogin();
        String authTitle = loginPage.getAuthTitleText();
        Assert.assertEquals(authTitle,"Pharmacy Login","The Auth Title did not match the Pharmacy Login Role");
    }
    @Test(testName = "Auth title Validation", description = "Verify it displays correct Auth Title for different login roles")
    public void validateAuthTitleForAdmin(){
        webDriver.get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = pageObjectManager.getLoginPage();
        loginPage.switchToAdminLogin();
        String authTitle = loginPage.getAuthTitleText();
        Assert.assertEquals(authTitle,"Admin Login","The Auth Title did not match the Admin Login Role");
    }

    @DataProvider(name = "loginDataProvider")
    public Object [][] getLoginData(){
        return ExcelUtil.getExcelData("LoginData");
    }

    @Test(testName = "Login Credentials Validation", description = "The test validates the login functionality with different cases"
    ,dataProvider = "loginDataProvider")
    public void loginValidation(String role,String email,String password,String result){
        webDriver.get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = pageObjectManager.getLoginPage();
        if(role.equalsIgnoreCase("user")){
            loginPage.loginAsUser(email,password);
        }
        else if(role.equalsIgnoreCase("pharmacy")){
            loginPage.loginAsPharmacy(email,password);

        } else if (role.equalsIgnoreCase("admin")) {
            loginPage.loginAsAdmin(email,password);
        }
        else{
            Assert.assertTrue(false, "Invalid Role Specified ie: "+role);
        }

        if(result.startsWith("/")){
            Assert.assertTrue(loginPage.isRedirected(result),"Using Valid Credentials email: "+email+" password: "+password+" but failed to login or redirect");
        }
        else{
            String alertText = loginPage.getAlertText();
            Assert.assertEquals(alertText,result,"The alert message is not displayed");
        }
    }

}
