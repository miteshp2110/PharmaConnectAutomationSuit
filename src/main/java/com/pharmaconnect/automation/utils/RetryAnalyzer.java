package com.pharmaconnect.automation.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static int MAX_COUNT = 2;

    private int count = 0;

    @Override
    public boolean retry(ITestResult result){
        if(count<MAX_COUNT){
            count++;
            return true;
        }
        return false;
    }


}
