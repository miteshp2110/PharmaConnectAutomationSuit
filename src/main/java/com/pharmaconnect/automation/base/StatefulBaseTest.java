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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class StatefulBaseTest {

    // ThreadLocal ensures parallel execution safety
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<PageObjectManager> pageManagerThreadLocal = new ThreadLocal<>();

    // @BeforeClass ensures the browser opens ONCE for the entire test class
    @BeforeClass(alwaysRun = true)
    @Parameters("browser")
    public void setup(@Optional("chrome") String browser) {
        WebDriver driver;

        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver(getChromeOptions());
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver(getFirefoxOptions());
        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver(getEdgeOptions());
        } else {
            driver = new ChromeDriver(getChromeOptions());
        }

        // Save the driver to the current thread
        driverThreadLocal.set(driver);

        // Initialize the Page Object Manager for this specific thread
        pageManagerThreadLocal.set(new PageObjectManager(getWebDriver()));

        getWebDriver().manage().window().maximize();

        // Assuming your ConfigReader has implicitWait defined.
        // If not, just hardcode Duration.ofSeconds(10)
        int waitTime = Integer.parseInt(ConfigReader.getProperty("implicitWait"));
        getWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(waitTime));
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        // Disable password save and change password popups
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        // Suppress Chrome-level popups
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
        // Same prefs work for Edge (Chromium-based)
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-notifications");
        return options;
    }

    // @AfterClass ensures the browser stays open until all tests in the class finish
    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        // Clear your custom context
        TestContext.clear();

        if (getWebDriver() != null) {
            getWebDriver().quit();
        }

        // CRITICAL: Prevent memory leaks in parallel execution
        driverThreadLocal.remove();
        pageManagerThreadLocal.remove();
    }

    // Getters for the Test classes to use
    public WebDriver getWebDriver() {
        return driverThreadLocal.get();
    }

    public PageObjectManager getPageObjectManager() {
        return pageManagerThreadLocal.get();
    }
}