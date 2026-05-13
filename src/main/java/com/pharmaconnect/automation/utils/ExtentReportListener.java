package com.pharmaconnect.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.pharmaconnect.automation.base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {

    ExtentReports extentReports = ExtentManager.getExtentReports();

    ExtentTest test;

    @Override
    public void onTestStart(ITestResult result){
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        test=extentReports.createTest(testName,testDescription);
        test.log(Status.INFO,"Test Execution Started");
    }

    @Override
    public void onTestSuccess(ITestResult result){
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        test = extentReports.createTest(testName,testDescription);
        test.log(Status.PASS,"Testcase Passed");
    }

    @Override
    public void onTestFailure(ITestResult result){
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        test = extentReports.createTest(testName,testDescription);
        test.log(Status.FAIL,"Test: "+testName+" \nfor: "+testDescription+" failed");
        test.log(Status.FAIL,result.getThrowable());

        try{
            Object testClassInstance = result.getInstance();
            WebDriver driver = ((BaseTest)testClassInstance).getWebDriver();
            String base64 = ScreenshotUtility.getBase64Screenshot(driver);
            ScreenshotUtility.saveScreenShot(testName,driver);
            test.addScreenCaptureFromBase64String(base64,"Screenshot for failed step");
        } catch (Exception e) {
            test.log(Status.INFO,"Failed to attach screenshot");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result){
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        test = extentReports.createTest(testName,testDescription);
        test.log(Status.SKIP,"Skipped this testcase");
    }

    @Override
    public void onFinish(ITestContext context){
        extentReports.flush();
    }
}
