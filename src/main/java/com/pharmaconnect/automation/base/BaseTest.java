package com.pharmaconnect.automation.base;

import com.pharmaconnect.automation.manager.PageObjectManager;
import com.pharmaconnect.automation.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.time.Duration;

public class BaseTest {

    protected WebDriver webDriver;
    protected PageObjectManager pageObjectManager;

    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser){
        if(browser.equalsIgnoreCase("chrome")){
            webDriver = new ChromeDriver();
        }
        else if(browser.equalsIgnoreCase("firefox")){
            webDriver = new FirefoxDriver();
        }
        else if(browser.equalsIgnoreCase("edge")){
            webDriver = new EdgeDriver();
        }
        else{
            webDriver = new ChromeDriver();
        }
        pageObjectManager = new PageObjectManager(webDriver);
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(ConfigReader.getProperty("implicitWait"))));
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp(){
        if(webDriver!=null){
            webDriver.quit();
        }
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }
}
