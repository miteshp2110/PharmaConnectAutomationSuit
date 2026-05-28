package com.pharmaconnect.automation.stepdefs;

import com.pharmaconnect.automation.manager.PageObjectManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CucumberBaseTest {

    private static WebDriver driver;
    private static PageObjectManager pageObjectManager;

    public static void initDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        pageObjectManager = new PageObjectManager(driver);
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            pageObjectManager = null;
        }
    }

    public static WebDriver getWebDriver() {
        return driver;
    }

    public static PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }
}