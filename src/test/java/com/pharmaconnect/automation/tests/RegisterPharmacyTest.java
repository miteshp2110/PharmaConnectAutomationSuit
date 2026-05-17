package com.pharmaconnect.automation.tests;

import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.pages.RegisterPharmacyPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import com.pharmaconnect.automation.utils.ExcelUtil;
import com.pharmaconnect.automation.utils.RandomGenerator;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class RegisterPharmacyTest extends BaseTest {


    @Test(testName = "Empty Fields Validation", description = "This test is to validate that the register functionality does not work it there are empty fields")
    public void validateEmptyFields(){
        webDriver.get(ConfigReader.getProperty("pharmacy.register.url"));
        RegisterPharmacyPage registerPharmacyPage = pageObjectManager.getRegisterPharmacyPage();
        registerPharmacyPage.clickRegisterButton();
        Assert.assertTrue(registerPharmacyPage.getErrorMessage().contains("Please fill in all required fields."),"The alert message was not displayed or incorrect message was displayed");
    }

    @DataProvider(name = "registerPharmacyData")
    public Object[][] getRegisterPharmacyData(){
        return ExcelUtil.getExcelData("RegisterPharmacyData");
    }

    @Test(
            dataProvider = "registerPharmacyData"
    )
    public void validatePharmacyRegistration(
            String testName,
            String description,
            String pharmacyName,
            String ownerName,
            String licenseNumber,
            String gstNumber,
            String email,
            String phone,
            String address,
            String city,
            String pincode,
            String operatingHours,
            String open247,
            String latitude,
            String longitude,
            String password,
            String confirmPassword,
            String expectedResult
    ) {
        webDriver.get(ConfigReader.getProperty("pharmacy.register.url"));
        String registerUrl = ConfigReader.getProperty("pharmacy.register.url");

        // ── Handle dynamically generated fields ──────────────────────
        if (email.equalsIgnoreCase("generatedDynamically")) {
            email = RandomGenerator.generateRandomEmail(pharmacyName.isEmpty() ? "pharmacy" : pharmacyName);
        }
        if (phone.equalsIgnoreCase("generatedDynamically")) {
            phone = RandomGenerator.generateRandomPhone();
        }
        description = description + "<br><br><b>Given Data:</b><br>"
                + "<b>Pharmacy Name:</b> "    + (pharmacyName.isEmpty()    ? "N/A" : pharmacyName)    + "<br>"
                + "<b>Owner Name:</b> "       + (ownerName.isEmpty()       ? "N/A" : ownerName)       + "<br>"
                + "<b>License Number:</b> "   + (licenseNumber.isEmpty()   ? "N/A" : licenseNumber)   + "<br>"
                + "<b>GST Number:</b> "       + (gstNumber.isEmpty()       ? "N/A" : gstNumber)       + "<br>"
                + "<b>Email:</b> "            + (email.isEmpty()           ? "N/A" : email)           + "<br>"
                + "<b>Phone:</b> "            + (phone.isEmpty()           ? "N/A" : phone)           + "<br>"
                + "<b>Address:</b> "          + (address.isEmpty()         ? "N/A" : address)         + "<br>"
                + "<b>City:</b> "             + (city.isEmpty()            ? "N/A" : city)            + "<br>"
                + "<b>Pincode:</b> "          + (pincode.isEmpty()         ? "N/A" : pincode)         + "<br>"
                + "<b>Operating Hours:</b> "  + (operatingHours.isEmpty()  ? "N/A" : operatingHours)  + "<br>"
                + "<b>Open 24x7:</b> "        + (open247.isEmpty()         ? "NO"  : open247)         + "<br>"
                + "<b>Latitude:</b> "         + (latitude.isEmpty()        ? "N/A" : latitude)        + "<br>"
                + "<b>Longitude:</b> "        + (longitude.isEmpty()       ? "N/A" : longitude)       + "<br>"
                + "<b>Password:</b> "         + (password.isEmpty()        ? "N/A" : password)        + "<br>"
                + "<b>Confirm Password:</b> " + (confirmPassword.isEmpty() ? "N/A" : confirmPassword) + "<br>"
                + "<b>Expected Result:</b> "  + expectedResult;

        Reporter.getCurrentTestResult().getMethod().setDescription(description);
        Reporter.getCurrentTestResult().setTestName(testName);
        RegisterPharmacyPage page = pageObjectManager.getRegisterPharmacyPage();

        // ── Fill Pharmacy Information ─────────────────────────────────
        if (!pharmacyName.isEmpty())  page.enterPharmacyName(pharmacyName);
        if (!ownerName.isEmpty())     page.enterOwnerName(ownerName);
        if (!licenseNumber.isEmpty()) page.enterLicenseNumber(licenseNumber);
        if (!gstNumber.isEmpty())     page.enterGstNumber(gstNumber);

        // ── Fill Contact Information ──────────────────────────────────
        if (!email.isEmpty())         page.enterEmail(email);
        if (!phone.isEmpty())         page.enterPhone(phone);
        if (!address.isEmpty())       page.enterAddress(address);
        if (!city.isEmpty())          page.enterCity(city);
        if (!pincode.isEmpty())       page.enterPincode(pincode);
        if (!operatingHours.isEmpty()) page.enterOperatingHours(operatingHours);

        // ── Handle 24x7 checkbox ──────────────────────────────────────
        if (open247.equalsIgnoreCase("YES")) {
            page.clickOpen247Checkbox();
        }

        // ── Fill Location ─────────────────────────────────────────────
        if (!latitude.isEmpty())      page.enterLatitude(latitude);
        if (!longitude.isEmpty())     page.enterLongitude(longitude);

        // ── Fill Password ─────────────────────────────────────────────
        if (!password.isEmpty())        page.enterPassword(password);
        if (!confirmPassword.isEmpty()) page.enterConfirmPassword(confirmPassword);



        // ── Terms checkbox — skip for navigation-only rows ────────────
        page.clickTermsCheckbox();
        page.clickRegisterButton();

        // ── Assert: Success → expect URL redirect ─────────────────────
        if (expectedResult.startsWith("/")) {

            Assert.assertTrue(
                    page.isRedirected(expectedResult),
                    "Expected redirect to [" + expectedResult + "] but got: [" + webDriver.getCurrentUrl() + "]"
            );
        }
        // ── Assert: Failure → expect error message ────────────────────
        else {
            Assert.assertTrue(
                    page.isErrorShownBeforeRedirect(registerUrl),
                    "Form was accepted but should have failed — " + testName
            );
            Assert.assertTrue(
                    page.getErrorMessage().contains(expectedResult),
                    "Wrong error. Expected: [" + expectedResult + "] Actual: [" + page.getErrorMessage() + "]"
            );
        }
    }
}
