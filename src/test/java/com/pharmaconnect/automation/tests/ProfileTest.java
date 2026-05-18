package com.pharmaconnect.automation.tests;

import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.pages.ProfilePage;
import com.pharmaconnect.automation.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ProfileTest extends BaseTest {

    private ProfilePage profilePage;

    // ── Keep browser alive across all profile tests ───────────────
    @Override
    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser) {
        if (webDriver == null) {
            super.setup(browser);
            loginOnce();
        }
        profilePage = pageObjectManager.getProfilePage();
        profilePage.navigateTo(ConfigReader.getProperty("base.url"));
    }

    @Override
    @org.testng.annotations.AfterMethod(alwaysRun = true)
    public void cleanUp() {
        // Keep browser alive between tests
    }

    @org.testng.annotations.AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    private void loginOnce() {
        webDriver.get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = pageObjectManager.getLoginPage();
        loginPage.enterEmail(ConfigReader.getProperty("test.user.email"));
        loginPage.enterPassword(ConfigReader.getProperty("test.user.password"));
        loginPage.clickLoginButton();
        loginPage.isRedirected("/search");
    }

    // ── TC1 — Page Loads ──────────────────────────────────────────
    @Test(priority = 1,
            testName = "Profile page loads correctly",
            description = "Navigate to the profile page and verify it loads with the user's name visible")
    public void profilePageLoadsCorrectly() {
        Assert.assertTrue(profilePage.isProfileLoaded(),
                "Profile page did not load — avatar name not visible");
    }

    // ── TC2 — Page Title ──────────────────────────────────────────
    @Test(priority = 2,
            testName = "Profile page title is correct",
            description = "Navigate to profile page and verify the page title shows My Profile")
    public void profilePageTitleIsCorrect() {
        Assert.assertEquals(profilePage.getPageTitle(), "My Profile",
                "Page title mismatch");
    }

    // ── TC3 — Page Subtitle ───────────────────────────────────────
    @Test(priority = 3,
            testName = "Profile page subtitle is correct",
            description = "Navigate to profile page and verify the subtitle shows Your account information")
    public void profilePageSubtitleIsCorrect() {
        Assert.assertEquals(profilePage.getPageSubtitle(), "Your account information",
                "Page subtitle mismatch");
    }

    // ── TC4 — Full Name ───────────────────────────────────────────
    @Test(priority = 4,
            testName = "Profile shows correct full name",
            description = "Verify the full name displayed on the profile matches the logged in user's name from config")
    public void profileShowsCorrectFullName() {
        Assert.assertEquals(
                profilePage.getFullName(),
                ConfigReader.getProperty("test.user.name"),
                "Full name mismatch on profile"
        );
    }

    // ── TC5 — Email ───────────────────────────────────────────────
    @Test(priority = 5,
            testName = "Profile shows correct email address",
            description = "Verify the email displayed on the profile matches the logged in user's email from config")
    public void profileShowsCorrectEmail() {
        Assert.assertEquals(
                profilePage.getEmail(),
                ConfigReader.getProperty("test.user.email"),
                "Email mismatch on profile"
        );
    }

    // ── TC6 — Phone ───────────────────────────────────────────────
    @Test(priority = 6,
            testName = "Profile shows correct phone number",
            description = "Verify the phone number displayed on the profile matches the logged in user's phone from config")
    public void profileShowsCorrectPhone() {
        Assert.assertEquals(
                profilePage.getPhone(),
                ConfigReader.getProperty("test.user.phone"),
                "Phone number mismatch on profile"
        );
    }

    // ── TC7 — Role Chip ───────────────────────────────────────────
    @Test(priority = 7,
            testName = "Profile role chip shows Patient",
            description = "Verify the account role chip on the profile card displays Patient")
    public void profileRoleChipShowsPatient() {
        Assert.assertEquals(profilePage.getRoleChipText(), "Patient",
                "Role chip does not show Patient");
    }

    // ── TC8 — Avatar Initial ──────────────────────────────────────
    @Test(priority = 8,
            testName = "Avatar initial matches first letter of name",
            description = "Verify the green avatar circle shows the first letter of the user's full name")
    public void avatarInitialMatchesName() {
        String fullName = profilePage.getFullName();
        Assert.assertTrue(profilePage.isAvatarInitialCorrect(fullName),
                "Avatar initial does not match first letter of name: " + fullName);
    }

    // ── TC9 — Avatar Name ─────────────────────────────────────────
    @Test(priority = 9,
            testName = "Avatar section shows correct name",
            description = "Verify the name displayed next to the avatar circle matches the user's full name")
    public void avatarNameMatchesFullName() {
        Assert.assertEquals(
                profilePage.getAvatarName(),
                ConfigReader.getProperty("test.user.name"),
                "Avatar name does not match full name"
        );
    }

    // ── TC10 — Avatar Role ────────────────────────────────────────
    @Test(priority = 10,
            testName = "Avatar section shows Patient Account role",
            description = "Verify the role text displayed next to the avatar shows Patient Account")
    public void avatarRoleShowsPatientAccount() {
        Assert.assertEquals(profilePage.getAvatarRole(), "Patient Account",
                "Avatar role text mismatch");
    }

    // ── TC11 — Account ID UUID ────────────────────────────────────
    @Test(priority = 11,
            testName = "Account ID is a valid UUID format",
            description = "Verify the Account ID shown on the profile is in valid UUID format")
    public void accountIdIsValidUUID() {
        Assert.assertTrue(profilePage.isAccountIdValidUUID(),
                "Account ID is not a valid UUID: " + profilePage.getAccountId());
    }

    // ── TC12 — Member Since Not Empty ─────────────────────────────
    @Test(priority = 12,
            testName = "Member Since date is displayed",
            description = "Verify that the Member Since field on the profile is not empty")
    public void memberSinceDateIsDisplayed() {
        Assert.assertFalse(profilePage.getMemberSince().isEmpty(),
                "Member Since date is empty");
    }

    // ── TC13 — Search Medicines Button ────────────────────────────
    @Test(priority = 13,
            testName = "Search Medicines button navigates to search page",
            description = "Click the Search Medicines button on the profile page and verify navigation to the search page")
    public void searchMedicinesButtonNavigatesToSearch() {
        profilePage.clickSearchMedicines();
        Assert.assertTrue(profilePage.isRedirected("/search"),
                "Search Medicines button did not navigate to search. URL: " + webDriver.getCurrentUrl());
    }

    // ── TC14 — My Reservations Button ─────────────────────────────
    @Test(priority = 14,
            testName = "My Reservations button navigates to reservations page",
            description = "Click the My Reservations button on the profile page and verify navigation to the reservations page")
    public void myReservationsButtonNavigatesToReservations() {
        profilePage.clickMyReservations();
        Assert.assertTrue(profilePage.isRedirected("/my-reservations"),
                "My Reservations button did not navigate correctly. URL: " + webDriver.getCurrentUrl());
    }

    // ── TC15 — Logo Navigation ────────────────────────────────────
    @Test(priority = 15,
            testName = "Logo navigates to search page",
            description = "Click the PharmaConnect logo on the profile page and verify navigation to the search page")
    public void logoNavigatesToSearch() {
        profilePage.clickLogo();
        Assert.assertTrue(profilePage.isRedirected("/search"),
                "Logo did not navigate to search. URL: " + webDriver.getCurrentUrl());
    }

    // ── TC16 — Logout ─────────────────────────────────────────────
    // ⚠️ Always last — this logs out and breaks session for all tests above
    @Test(priority = 16,
            testName = "Logout redirects to login page",
            description = "Click Logout on the profile page and verify the user is redirected to the login page")
    public void logoutRedirectsToLogin() {
        profilePage.clickLogout();
        Assert.assertTrue(profilePage.isRedirected("/"),
                "Logout did not redirect to Home. URL: " + webDriver.getCurrentUrl());
    }
}