package com.pharmaconnect.automation.utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

//public class ExcelUtil {
//
//    public static Object [][] getExcelData(String sheetName){
//        String projectRoot = System.getProperty("user.dir");
//        String excelPath = projectRoot + "/src/test/resources/testData/"+ConfigReader.getProperty("testDataFileName");
//        File file = new File(excelPath);
//        Object[][] data = null;
//        try(FileInputStream fis = new FileInputStream(file)){
//            XSSFWorkbook workbook = new XSSFWorkbook(fis);
//            XSSFSheet sheet = workbook.getSheet(sheetName);
//            int rowCount = sheet.getPhysicalNumberOfRows();
//            int colCount = sheet.getRow(0).getLastCellNum();
//            data = new Object[rowCount-1][colCount];
//
//            DataFormatter dataFormatter = new DataFormatter();
//
//            for(int i=1;i<rowCount;i++){
//                for(int j=0;j<colCount;j++){
//
//                    data[i-1][j]=dataFormatter.formatCellValue(sheet.getRow(i).getCell(j));
//                }
//            }
//
//        }
//        catch (Exception e){
//            throw new RuntimeException("Failed to read Excel file at path: " + excelPath, e);
//        }
//        return data;
//    }
//}

public class ExcelUtil {

    public static Object[][] getExcelData(String sheetName) {
        String projectRoot = System.getProperty("user.dir");
        String excelPath = projectRoot + "/src/test/resources/testData/"
                + ConfigReader.getProperty("testDataFileName");

        File file = new File(excelPath);
        Object[][] data = null;

        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in Excel file.");
            }

            int rowCount = sheet.getLastRowNum() + 1;  // ✅ includes all rows
            int colCount = sheet.getRow(0).getLastCellNum();
            DataFormatter dataFormatter = new DataFormatter();

            data = new Object[rowCount - 1][colCount];

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;             // ✅ skip empty rows
                for (int j = 0; j < colCount; j++) {
                    data[i - 1][j] = dataFormatter.formatCellValue(row.getCell(j));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to read Excel file at path: " + excelPath, e);
        }

        return data;
    }
}
