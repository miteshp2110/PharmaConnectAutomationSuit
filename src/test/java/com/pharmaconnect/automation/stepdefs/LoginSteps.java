package com.pharmaconnect.automation.stepdefs;

import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import io.cucumber.java.en.*;
import org.testng.Assert;

// ✅ Does NOT extend anything — this is the Cucumber rule
public class LoginSteps {

    private LoginPage loginPage;

    @Given("I navigate to the login page")
    public void iNavigateToLoginPage() {
        CucumberBaseTest.getWebDriver().get(ConfigReader.getProperty("login.url"));
        loginPage = CucumberBaseTest.getPageObjectManager().getLoginPage();
    }

    @When("I select the {string} login tab")
    public void iSelectLoginTab(String role) {
        if (role.equalsIgnoreCase("Pharmacy")) loginPage.switchToPharmacyLogin();
        else if (role.equalsIgnoreCase("Admin"))    loginPage.switchToAdminLogin();
    }

    @When("I enter email {string} and password {string}")
    public void iEnterCredentials(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
    }

    @When("I click the Login button")
    public void iClickLoginButton() {
        loginPage.clickLoginButton();
    }

    @When("I click the Login button without entering credentials")
    public void iClickLoginButtonEmpty() {
        loginPage.clickLoginButton();
    }

    @Then("I should be redirected to {string}")
    public void iShouldBeRedirectedTo(String url) {
        Assert.assertTrue(
                loginPage.isRedirected(url),
                "Expected redirect to " + url + " but got: "
                        + CucumberBaseTest.getWebDriver().getCurrentUrl()
        );
    }

    @Then("I should see the error {string}")
    public void iShouldSeeError(String expectedError) {
        Assert.assertEquals(loginPage.getAlertText(), expectedError);
    }

    @Then("an alert or validation message should be displayed")
    public void alertShouldBeDisplayed() {
        Assert.assertTrue(loginPage.isAlertVisible(),
                "No alert or validation message was displayed");
    }
}