package com.pharmaconnect.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.pharmaconnect.automation.base.BaseTest;
import com.pharmaconnect.automation.base.StatefulBaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {

    ExtentReports extentReports = ExtentManager.getExtentReports();

    // 1. ThreadLocal prevents parallel tests from overwriting each other's reports
    private ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result){
        String testName = result.getMethod().getMethodName();
        ExtentTest extentTest = extentReports.createTest(testName);
        test.set(extentTest); // Save to the current thread

        test.get().log(Status.INFO,"Test Execution Started");
    }

    @Override
    public void onTestSuccess(ITestResult result){
        String testDescription = result.getMethod().getDescription();
        test.get().log(Status.PASS,"Testcase Passed: <br/>" + testDescription);
    }

    @Override
    public void onTestFailure(ITestResult result){
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();

        // Safety Catch: If @BeforeClass failed, onTestStart is skipped. We must create the test here!
        if(test.get() == null) {
            test.set(extentReports.createTest(testName));
        }

        test.get().log(Status.FAIL,"Test: " + testName + " \nfor: " + testDescription + " failed");
        test.get().log(Status.FAIL, result.getThrowable());

        try {
            Object testClassInstance = result.getInstance();
            WebDriver driver = null;

            // 2. Safely check WHICH BaseTest the failed class is using!
            if (testClassInstance instanceof BaseTest) {
                driver = ((BaseTest) testClassInstance).getWebDriver();
            } else if (testClassInstance instanceof StatefulBaseTest) {
                driver = ((StatefulBaseTest) testClassInstance).getWebDriver();
            }

            if (driver != null) {
                String base64 = ScreenshotUtility.getBase64Screenshot(driver);
                ScreenshotUtility.saveScreenShot(testName, driver);
                test.get().addScreenCaptureFromBase64String(base64, "Screenshot for failed step");
            }

        } catch (Exception e) {
            test.get().log(Status.INFO, "Failed to attach screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result){
        String testName = result.getMethod().getMethodName();

        // Safety Catch for Skipped tests
        if(test.get() == null) {
            test.set(extentReports.createTest(testName));
        }

        test.get().log(Status.SKIP,"Skipped this testcase");

        if (result.getThrowable() != null) {
            test.get().log(Status.SKIP, result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context){
        extentReports.flush();
        // Clear the thread to prevent memory leaks after the suite finishes
        test.remove();
    }
}