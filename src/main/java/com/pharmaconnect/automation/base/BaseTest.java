package com.pharmaconnect.automation.base;

import com.pharmaconnect.automation.manager.PageObjectManager;
import com.pharmaconnect.automation.utils.ConfigReader;
import com.pharmaconnect.automation.utils.TestContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    protected WebDriver webDriver;
    protected PageObjectManager pageObjectManager;

    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            webDriver = new ChromeDriver(getChromeOptions());
        } else if (browser.equalsIgnoreCase("firefox")) {
            webDriver = new FirefoxDriver(getFirefoxOptions());
        } else if (browser.equalsIgnoreCase("edge")) {
            webDriver = new EdgeDriver(getEdgeOptions());
        } else {
            webDriver = new ChromeDriver(getChromeOptions());
        }
        pageObjectManager = new PageObjectManager(webDriver);
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(Integer.parseInt(ConfigReader.getProperty("implicitWait")))
        );
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        // ✅ Disable password save and change password popups
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        options.setExperimentalOption("prefs", prefs);

        // ✅ Suppress Chrome-level popups
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        return options;
    }

    private FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        // Firefox equivalent — disable password manager
        options.addPreference("signon.rememberSignons", false);
        options.addPreference("signon.autofillForms", false);
        return options;
    }

    private EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();

        Map<String, Object> prefs = new HashMap<>();
        // ✅ Same prefs work for Edge (Chromium-based)
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-notifications");

        return options;
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        TestContext.clear();
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }
}