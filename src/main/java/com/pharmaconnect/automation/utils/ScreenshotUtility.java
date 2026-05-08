package com.pharmaconnect.automation.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtility {

    public static String getBase64Screenshot(WebDriver driver){
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    public static void saveScreenShot(String testName, WebDriver driver){
        File sourceFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName+"-"+timestamp+".png";
        String rootPath = System.getProperty("user.dir");
        String directoryPath = rootPath + "/src/test-output/screenshots";
        File destinationFile = new File(directoryPath,fileName);
        try{
            new File(directoryPath).mkdirs();
            Files.copy(sourceFile.toPath(),destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        }
        catch (Exception e){
            throw new RuntimeException("Failed to Save Screenshot");
        }
    }
}
