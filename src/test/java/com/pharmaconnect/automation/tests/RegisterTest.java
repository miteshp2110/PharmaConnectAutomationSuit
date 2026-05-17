package com.pharmaconnect.automation.tests;

import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.pages.RegisterPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import com.pharmaconnect.automation.utils.ExcelUtil;
import com.pharmaconnect.automation.utils.RandomGenerator;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Objects;

public class RegisterTest extends BaseTest {



    @Test(testName = "Empty Fields Validation", description = "This test is to validate that the register functionality does not work it there are empty fields")
    public void validateEmptyFields(){
        webDriver.get(ConfigReader.getProperty("register.url"));
        RegisterPage registerPage = pageObjectManager.getRegisterPage();
        registerPage.clickCreateAccount();
        Assert.assertTrue(registerPage.getErrorMessage().contains("Please fill in all required fields."),"The alert message was not displayed or incorrect message was displayed");
    }

    @Test(priority = 1,testName = "Validate if checkBox is working", description = "This test is to validate if without checking the checkbox for terms and conditions can we proceed")
    public void validateTermsAndConditions(){
        webDriver.get(ConfigReader.getProperty("register.url"));
        RegisterPage registerPage = pageObjectManager.getRegisterPage();
        registerPage.enterFullName("autox23");
        registerPage.enterEmail(RandomGenerator.generateRandomEmail("autox23"));
        registerPage.enterPhone(RandomGenerator.generateRandomPhone());
        registerPage.enterPassword("password");
        registerPage.enterConfirmPassword("password");
        registerPage.clickCreateAccount();
        Assert.assertTrue(Objects.requireNonNull(webDriver.getCurrentUrl()).contains(ConfigReader.getProperty("register.url")),"Without accepting terms and condition the user was created");
        String errorMessage = registerPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("You must agree to the Terms of Service to continue."),"Wrong or no error message was displayed");

    }

    @Test(priority = 2,testName = "Validate Signin Link",description = "To validate if the signin link is working to go to signin/login page")
    public void validateSigInLink(){
        webDriver.get(ConfigReader.getProperty("register.url"));
        RegisterPage registerPage = pageObjectManager.getRegisterPage();
        registerPage.clickSignInLink();
        Assert.assertTrue(Objects.requireNonNull(webDriver.getCurrentUrl()).contains(ConfigReader.getProperty("login.url")),"The user was not redirected to the login page");
    }


    @DataProvider(name = "registerTestData")
    public Object [][] getRegisterTestData(){
        return ExcelUtil.getExcelData("RegisterData");
    }

    @Test(priority = 3,dataProvider = "registerTestData")
    public void validateRegistration(
            String testName,
            String description,
            String fullName,
            String email,
            String phone,
            String city,
            String address,
            String pincode,
            String password,
            String confirmPassword,
            String expectedResult
    ) {
        if(email.equalsIgnoreCase("generatedDynamically")){
            email = RandomGenerator.generateRandomEmail(fullName);
        }
        if(phone.equalsIgnoreCase("generatedDynamically")){
            phone = RandomGenerator.generateRandomPhone();
        }
        description = description + "<br><br><b>Given Data:</b><br>"
                + "<b>Full Name:</b> "       + fullName        + "<br>"
                + "<b>Email:</b> "           + email           + "<br>"
                + "<b>Phone:</b> "           + phone           + "<br>"
                + "<b>City:</b> "            + (city.isEmpty()    ? "N/A" : city)    + "<br>"
                + "<b>Address:</b> "         + (address.isEmpty() ? "N/A" : address) + "<br>"
                + "<b>Pincode:</b> "         + (pincode.isEmpty() ? "N/A" : pincode) + "<br>"
                + "<b>Password:</b> "        + password        + "<br>"
                + "<b>Confirm Password:</b> "+ confirmPassword + "<br>"
                + "<b>Expected Result:</b> " + expectedResult;
        Reporter.getCurrentTestResult().getMethod().setDescription(description);
        Reporter.getCurrentTestResult().setTestName(testName);

        webDriver.get(ConfigReader.getProperty("register.url"));
        RegisterPage registerPage = pageObjectManager.getRegisterPage();

        registerPage.enterFullName(fullName);
        registerPage.enterEmail(email);
        registerPage.enterPhone(phone);
        if(!city.isEmpty()) registerPage.enterCity(city);
        if(!address.isEmpty()) registerPage.enterAddress(address);
        if(!pincode.isEmpty()) registerPage.enterPincode(pincode);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.clickTermsCheckbox();
        registerPage.clickCreateAccount();

        if(expectedResult.startsWith("/")){
            Assert.assertTrue(registerPage.isRedirected(expectedResult),"Account was not created or wrong redirection");
        }
        else{
            Assert.assertTrue(registerPage.isErrorShownBeforeRedirect(ConfigReader.getProperty("register.url")),
                    "Invalid data was accepted and user was created or wrong redirection");
            String errorMessage = registerPage.getErrorMessage();
            Assert.assertEquals(expectedResult,errorMessage,"The error message does not match");
        }

    }
}
