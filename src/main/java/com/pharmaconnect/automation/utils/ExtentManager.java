package com.pharmaconnect.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;

public class ExtentManager {

    private static ExtentReports extentReports;

    public static ExtentReports getExtentReports(){

        if(extentReports == null){

            String rootPath = System.getProperty("user.dir");
            String folderPath = rootPath + "/src/test-output/reports";
            String reportPath = folderPath + "/AutomationReport.html";

            File folder = new File(folderPath);
            if(!folder.exists()){
                folder.mkdirs();
            }

            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
            reporter.config().setDocumentTitle(ConfigReader.getProperty("extentReportTitle"));
            reporter.config().setReportName(ConfigReader.getProperty("extentReportName"));

            extentReports = new ExtentReports();
            extentReports.attachReporter(reporter);

        }
        return extentReports;
    }
}
