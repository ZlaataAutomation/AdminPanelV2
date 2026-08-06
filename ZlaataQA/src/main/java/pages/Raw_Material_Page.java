package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import objectRepo.Raw_Material_ObjRepo;
import utils.Common;
import utils.ExcelXLSReader;
import utils.ExportValidator;

public class Raw_Material_Page extends Raw_Material_ObjRepo {
	
	public Raw_Material_Page(WebDriver driver) 
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(this.driver, this);
	}
	
	public void adminLogin() {
	
	AdminLogin_Page login= new AdminLogin_Page(driver);
	login.adminLoginApp();
	
	}
	
	// Declare these variables at class level
	public static String expectedMaterialName;
	public static String expectedCategory;
	public static String expectedMaterialType;
	public static String expectedMeasurement;
	public static String expectedSupplier;
	public static String expectedQuantity;
	public static String expectedSku;

	public void fillRawMaterialDetails() throws InterruptedException {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    Actions actions = new Actions(driver);
	    Random random = new Random();

	    final String RESET = "\u001B[0m";
	    final String GREEN = "\u001B[32m";
	    final String CYAN = "\u001B[36m";
	    final String YELLOW = "\u001B[33m";

	    // Hover on Inventory
	    WebElement inventory = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//button[contains(@class,'sidebar_menu_btn')]//span[normalize-space()='Inventory']")));
	    actions.moveToElement(inventory).perform();

	    // Click Raw Material Stocks
	    WebElement rawMaterial = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[normalize-space()='Raw Material Stocks']")));
	    rawMaterial.click();

	    Common.waitForElement(2);

	    // Click Add Raw Material
	    wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//a[@bp-button='create' and .//span[normalize-space()='Add raw-material']]")))
	            .click();

	    Common.waitForElement(2);

	    // Generate Random Data
	    String materialName = "automationraw" + (1000 + random.nextInt(9000));
	    String sku = "AUTO" + (100 + random.nextInt(900));

	    // Material Name
	    WebElement material = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
	    material.clear();
	    material.sendKeys(materialName);

	    // Category
	 // Click the Category dropdown

	    WebElement categoryDropdown = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("(//button[contains(@class,'select-trigger-btn')])[1]")));
	    categoryDropdown.click();

	    try {
	        WebElement buttonOption = wait.until(ExpectedConditions.elementToBeClickable(
	                By.xpath("//li[@data-label='Button']")));
	        buttonOption.click();
	        System.out.println("Button selected.");

	    } catch (Exception e) {

	        System.out.println("Button not found. Selecting Zipper.");

	        WebElement zipperOption = wait.until(ExpectedConditions.elementToBeClickable(
	                By.xpath("//li[@data-label='Zipper']")));
	        zipperOption.click();
	    }
	    Common.waitForElement(1);
	    // Material Type
	 // Click the Material Type dropdown (2nd dropdown on the page)
	 WebElement materialTypeDropdown = wait.until(
	         ExpectedConditions.elementToBeClickable(
	                 By.xpath("(//button[contains(@class,'select-trigger-btn')])[2]")));
	 materialTypeDropdown.click();

	 try {
	     // Try selecting Cotton
	     wait.until(ExpectedConditions.elementToBeClickable(
	             By.xpath("//li[@data-label='Cotton']"))).click();
	     System.out.println("Cotton selected.");

	 } catch (Exception e) {

	     System.out.println("Cotton not found. Selecting Polyester.");

	     // If Cotton is not available, select Polyester
	     wait.until(ExpectedConditions.elementToBeClickable(
	             By.xpath("//li[@data-label='Polyester']"))).click();
	 }
	  Common.waitForElement(1);
	    // Unit Measurement
	// Click the Unit Measurement dropdown (3rd dropdown)
	WebElement measurementDropdown = wait.until(
	        ExpectedConditions.elementToBeClickable(
	                By.xpath("(//button[contains(@class,'select-trigger-btn')])[3]")));
	measurementDropdown.click();

	try {
	    // Try selecting Mm
	    wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[@data-label='Mm']"))).click();
	    System.out.println("Mm selected.");

	} catch (Exception e) {

	    System.out.println("Mm not found. Selecting Cm.");

	    // If M is not available, select Cm
	    wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[@data-label='Cm']"))).click();
	}
	  Common.waitForElement(1);
	    // Supplier (Select2)
	// Click Supplier dropdown
	wait.until(ExpectedConditions.elementToBeClickable(
	        By.xpath("(//button[contains(@class,'select-trigger-btn')])[4]"))).click();

	try {
	    // Try selecting ABC dddTextiles Pvt Ltd
	    wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[@data-label='ABC dddTextiles Pvt Ltd']"))).click();

	    System.out.println("ABC dddTextiles Pvt Ltd selected.");
	    Common.waitForElement(1);
	   

	} catch (Exception e) {

	    System.out.println("ABC supplier not found. Selecting PGR Sun Production.");

	    wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[@data-label='PGR Sun Production']"))).click();
	    Common.waitForElement(1);
	   
	}
	Common.waitForElement(2);
	WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
	        By.xpath("//input[contains(@class,'select-search-input')]")));

	// Click the search box
	searchBox.click();
	searchBox.sendKeys(Keys.ENTER);

	    

	    // Get Selected Supplier Name
	    String supplierName = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//span[@class='chip-label']")))
	            .getText()
	            .replace("×", "")
	            .replace("\n", "")
	            .trim();

	    // Quantity
	    WebElement quantity = driver.findElement(By.name("quantity"));
	    quantity.clear();
	    quantity.sendKeys("100");

	    // SKU
	    WebElement skuField = driver.findElement(By.name("sku"));
	    skuField.clear();
	    skuField.sendKeys(sku);
	    Common.waitForElement(2);
	    // Store values for verification
	    expectedMaterialName = materialName;
	    expectedCategory = driver.findElement(
	            By.xpath("(//span[@class='selected-text-display'])[1]"))
	            .getText()
	            .trim();

	    System.out.println("Selected Category: " + expectedCategory);
	    
	    expectedMaterialType = driver.findElement(
	            By.xpath("(//span[@class='selected-text-display'])[2]"))
	            .getText()
	            .trim();

	    System.out.println("Selected Material: " + expectedMaterialType);
	    expectedMeasurement =driver.findElement(
	            By.xpath("(//span[@class='selected-text-display'])[3]"))
	            .getText()
	            .trim();

	    System.out.println("Selected Measurement: " + expectedMeasurement);
	    expectedSupplier = supplierName;
	    expectedQuantity = "100";
	    expectedSku = sku;

	    // Console Output
	    System.out.println(GREEN + "\n===============================================");
	    System.out.println("        RAW MATERIAL FILLED DATA");
	    System.out.println("===============================================" + RESET);

	    System.out.println(CYAN + "Material Name      : " + RESET + expectedMaterialName);
	    System.out.println(CYAN + "Category           : " + RESET + expectedCategory);
	    System.out.println(CYAN + "Material Type      : " + RESET + expectedMaterialType);
	    System.out.println(CYAN + "Unit Measurement   : " + RESET + expectedMeasurement);
	    System.out.println(CYAN + "Supplier           : " + RESET + expectedSupplier);
	    System.out.println(CYAN + "Quantity           : " + RESET + expectedQuantity);
	    System.out.println(CYAN + "SKU                : " + RESET + expectedSku);

	    System.out.println(YELLOW + "===============================================\n" + RESET);

	    Common.waitForElement(2);
	    wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[@type='submit' and @form='crudForm' and normalize-space()='Save']")))
	        .click();
	    Common.waitForElement(2);
	}
	
	
	public void verifyRawMaterialFirstRow() {

	    Common.waitForElement(2);

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    // ANSI Colors
	    final String RESET = "\u001B[0m";
	    final String GREEN = "\u001B[32m";
	    final String RED = "\u001B[31m";
	    final String CYAN = "\u001B[36m";
	    final String YELLOW = "\u001B[33m";

	    // Wait for first row
	    WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//table//tbody/tr[1]")));

	    String actualSku = firstRow.findElement(By.xpath("./td[1]")).getText().trim();
	    String actualMaterialName = firstRow.findElement(By.xpath("./td[2]")).getText().trim();
	    String actualCategory = firstRow.findElement(By.xpath("./td[3]")).getText().trim();
	    String actualMaterialType = firstRow.findElement(By.xpath("./td[4]")).getText().trim();
	    String actualQuantity = firstRow.findElement(By.xpath("./td[6]")).getText().trim();
	    String actualSupplier = firstRow.findElement(By.xpath("./td[7]")).getText().trim();
	    
	    System.out.println("=========== DISPLAYED FIRST ROW DATA ==========="); 
	    System.out.println("SKU : " + actualSku); 
	    System.out.println("Material Name : " + actualMaterialName); 
	    System.out.println("Category : " + actualCategory); 
	    System.out.println("Material Type : " + actualMaterialType); 
	    System.out.println("Quantity : " + actualQuantity); 
	    System.out.println("Supplier : " + actualSupplier); 
	    System.out.println("========================================");

	    System.out.println(GREEN + "\n==================================================");
	    System.out.println("          RAW MATERIAL VERIFICATION");
	    System.out.println("==================================================" + RESET);

	    validateField("SKU", expectedSku, actualSku, CYAN, GREEN, RED, RESET);
	    validateField("Material Name", expectedMaterialName, actualMaterialName, CYAN, GREEN, RED, RESET);
	    validateField("Category", expectedCategory, actualCategory, CYAN, GREEN, RED, RESET);
	    validateField("Material Type", expectedMaterialType, actualMaterialType, CYAN, GREEN, RED, RESET);
	    validateField("Quantity", expectedQuantity, actualQuantity, CYAN, GREEN, RED, RESET);
	    validateField("Supplier", expectedSupplier, actualSupplier, CYAN, GREEN, RED, RESET);

	    System.out.println(YELLOW + "==================================================" + RESET);

	    Assert.assertEquals(expectedSku, actualSku);
	    Assert.assertEquals(expectedMaterialName, actualMaterialName);
	    Assert.assertEquals(expectedCategory, actualCategory);
	    Assert.assertEquals(expectedMaterialType, actualMaterialType);
	    Assert.assertEquals(expectedQuantity, actualQuantity);
	    Assert.assertEquals(expectedSupplier, actualSupplier);

	    System.out.println(GREEN + "✅ ALL RAW MATERIAL DETAILS VERIFIED SUCCESSFULLY." + RESET);

	    Common.waitForElement(2);
	}

	private void validateField(String fieldName, String expected, String actual,
	                           String CYAN, String GREEN, String RED, String RESET) {

	    System.out.println(CYAN + fieldName + RESET);
	    System.out.println("Expected : " + expected);
	    System.out.println("Actual   : " + actual);

	    if (expected.equals(actual)) {
	        System.out.println(GREEN + "✅ MATCHED" + RESET);
	    } else {
	        System.out.println(RED + "❌ NOT MATCHED" + RESET);
	    }

	    System.out.println("--------------------------------------------");
	}
	
	String capturedProductName;
	int capturedStockQuantity;
	String capturedStockStatus;
	String caturedSKU;
	
	
	public void selectRandomProductonRawMaterialSection() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    Actions actions = new Actions(driver);
	    
	    // Hover on Inventory
	    WebElement inventory = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//button[contains(@class,'sidebar_menu_btn')]//span[normalize-space()='Inventory']")));
	    actions.moveToElement(inventory).perform();

	    // Click Raw Material Stocks
	    WebElement rawMaterial = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[normalize-space()='Raw Material Stocks']")));
	    rawMaterial.click();

	    Common.waitForElement(2);
	    
	    wait.until(ExpectedConditions.visibilityOfAllElements(productDataRows));
        int totalRows = productDataRows.size();
        if (totalRows == 0) {
            throw new RuntimeException("No product rows found in the table!");
        }
        int selectedIndex = new Random().nextInt(totalRows);
        WebElement selectedRow = productDataRows.get(selectedIndex);

        WebElement nameCell = selectedRow.findElement(By.xpath(".//td[2]"));
        wait.until(ExpectedConditions.visibilityOf(nameCell));
        capturedProductName = nameCell.getText().trim();
        WebElement skuCell = selectedRow.findElement(By.xpath(".//td[1]"));
        wait.until(ExpectedConditions.visibilityOf(skuCell));
        caturedSKU = skuCell.getText().trim();
         System.out.println(":information_source: Selected Raw Material Name: " + capturedProductName);
        System.out.println(":information_source: Selected Raw Material SKU: " + caturedSKU);

        
        WebElement searchBar = driver.findElement(By.id("text-filter-sku"));
        searchBar.click();
        searchBar.clear();
        searchBar.sendKeys(caturedSKU); 
	    Common.waitForElement(2);

        
	 // ===== Capture Quantity (3rd column) =====
	    WebElement qtyCell = driver.findElement(By.xpath("(//tbody/tr/td[6])[1]"));
	    wait.until(ExpectedConditions.visibilityOf(qtyCell));

	    String qtyText = qtyCell.getText().trim();
	    capturedStockQuantity = Integer.parseInt(qtyText);

	    System.out.println("Captured Quantity: " + capturedStockQuantity);

	    // ===== Capture Stock Status (4th column) =====
	    WebElement stockStatusElement = driver.findElement(
	            By.xpath("(//tbody/tr/td[5])[1]"));
	    wait.until(ExpectedConditions.visibilityOf(stockStatusElement));

	    capturedStockStatus = stockStatusElement.getText().trim();

	    System.out.println("Captured Stock Status: " + capturedStockStatus);

        wait.until(ExpectedConditions.visibilityOfAllElements(editButtons));
     // Click the three-dot menu
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@class,'actions-buttons-column')]")));
        menu.click();
        Common.waitForElement(1);
        // Click the Edit option
        WebElement previewBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@bp-button='show']")));
        previewBtn.click();
        System.out.println(":three_button_mouse: Clicked preview  button for: " + capturedProductName + " — navigating to preview page");
        Common.waitForElement(2);
	}
	
	
	public void completeStockAdjustmentFlow() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    // Click Update Stock button
	    WebElement updateStockBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[@data-target='#UpdateStockModal']")));
	    updateStockBtn.click();
	    System.out.println("🖱️ Clicked Update Stock button");
	    Common.waitForElement(2);

	    // Select "Adjust Stock" radio button
	    WebElement adjustRadio = wait.until(ExpectedConditions.presenceOfElementLocated(
	            By.id("adjust")));

	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", adjustRadio);

	    System.out.println("✅ Selected Adjust Stock");
	    Common.waitForElement(1);
	    // Enter reason
	    WebElement reasonTextArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.id("reason")));
	    reasonTextArea.clear();
	    reasonTextArea.sendKeys("Stock adjustment for automation testing.");
	    System.out.println("✅ Entered adjustment reason");

	    // Enter captured stock quantity
	    WebElement quantityField = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//input[@name='add_quantity']")));
	    quantityField.clear();
	    quantityField.sendKeys(String.valueOf(capturedStockQuantity));
	    System.out.println("✅ Entered Quantity: " + capturedStockQuantity);

	    // Click Save Changes
	    WebElement saveChangesBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[normalize-space()='Save Changes']")));
	    saveChangesBtn.click();
	    System.out.println("💾 Clicked Save Changes");

	    Common.waitForElement(3);
	}
	
	
	public void verifyOutOfStockStatusAndStockHistory() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    // ===== Verify Out of Stock status =====
	    WebElement stockStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//p[contains(@class,'stock_inactive_para')]")));

	    String actualStatus = stockStatus.getText().trim();

	    Assert.assertEquals("Stock status is incorrect.",
	            "Out of Stock",
	            actualStatus);

	    System.out.println("✅ Stock Status : " + actualStatus);

	    // ===== Verify table quantity = 0 =====
	    WebElement tableQty = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//tbody//tr[1]//td[2])[1]")));

	    String actualQty = tableQty.getText().trim();

	    Assert.assertEquals("Table quantity is not zero.",
	            "0",
	            actualQty);

	    System.out.println("✅ Table Quantity : " + actualQty);

	    // ===== Verify preview quantity = 0 =====
	    WebElement previewQty = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//div[p[normalize-space()='Quantity']]/p[@class='modal_para_dark']")));

	    String actualPreviewQty = previewQty.getText().trim();

	    Assert.assertEquals("Preview quantity is not zero.",
	            "0",
	            actualPreviewQty);

	    System.out.println("✅ Preview Quantity : " + actualPreviewQty);

	    // ===== Click three-dot menu =====
	    WebElement threeDot = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//div[contains(@class,'material_action_dropdown')]//label")));
	    threeDot.click();

	    // ===== Click Stock History =====
	    WebElement stockHistory = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[normalize-space()='Stock History']")));
	    stockHistory.click();
	    System.out.println("✅ Opened Stock History");
	    Common.waitForElement(3);

	    // ===== Verify adjusted quantity =====
	    WebElement adjustedQty = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//p[contains(@class,'count_para')])[1]")));

	    String actualQty1 = adjustedQty.getText().trim();
	    String expectedQty = "-" + capturedStockQuantity;

	    Assert.assertEquals("Adjusted quantity mismatch.",
	            expectedQty,
	            actualQty1);

	    System.out.println("✅ Adjusted Quantity : " + actualQty1);

	    // ===== Verify Stocks Adjusted =====
	    WebElement stockAdjusted = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//p[normalize-space()='Stocks Adjusted'])[1]")));

	    String actualStatus1 = stockAdjusted.getText().trim();
	    String expectedStatus = "Stocks Adjusted";

	    Assert.assertEquals("Stock adjustment status mismatch.",
	            expectedStatus,
	            actualStatus1);

	    System.out.println("✅ Stock Adjustment Status Verified : " + actualStatus1);
	    // ===== Verify Reason =====
	    WebElement reason = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//div[p[normalize-space()='Reason']]/p[@class='date_para m-0'])[1]")));

	    String actualReason = reason.getText().trim();
	    String expectedReason = "Stock adjustment for automation testing.";

	    Assert.assertEquals("Reason mismatch.",
	            expectedReason,
	            actualReason);

	    System.out.println("✅ Reason Verified : " + actualReason);
	}
	String currentStockQty;
	public void completeStockAddFlow() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    // Click Update Stock button
	    WebElement updateStockBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[@data-target='#UpdateStockModal']")));
	    updateStockBtn.click();
	    System.out.println("🖱️ Clicked Update Stock button");
	    Common.waitForElement(2);

	    WebElement currentStock = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//tbody/tr[1]/td[2])[2]")));

	     currentStockQty = currentStock.getText().trim();

	    System.out.println("✅ Current Stock Quantity : " + currentStockQty);
	    Common.waitForElement(1);
	    // Enter captured stock quantity
	    WebElement quantityField = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//input[@name='add_quantity']")));
	    quantityField.clear();
	    quantityField.sendKeys("2000");
	    System.out.println("✅ Entered Quantity: 2000" );

	    // Click Save Changes
	    WebElement saveChangesBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[normalize-space()='Save Changes']")));
	    saveChangesBtn.click();
	    System.out.println("💾 Clicked Save Changes");

	    Common.waitForElement(3);
	    
	}
	
	public void verifyInStockStatusAndStockHistory() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	 // ===== Verify In Stock status =====
	    WebElement stockStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//p[contains(@class,'stock_active_para') and contains(@class,'in-stock')]")));

	    String actualStatus = stockStatus.getText().trim();

	    Assert.assertEquals("Stock status is incorrect.",
	            "In Stock",
	            actualStatus);

	    System.out.println("✅ Stock Status : " + actualStatus);

	 // ===== Verify updated table quantity =====
	    WebElement tableQty = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//tbody//tr[1]//td[2])[1]")));

	    String actualQty = tableQty.getText().trim();

	    // Calculate expected quantity
	    int expectedQty = Integer.parseInt(currentStockQty) + 2000;
	    
	    Assert.assertEquals("Updated table quantity is incorrect.",
	            String.valueOf(expectedQty),
	            actualQty);

	    System.out.println("✅ Expected Quantity : " + expectedQty);
	    System.out.println("✅ Actual Quantity   : " + actualQty);

	    // ===== Verify preview quantity = 0 =====
	    WebElement previewQty = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//div[p[normalize-space()='Quantity']]/p[@class='modal_para_dark']")));

	    String actualPreviewQty = previewQty.getText().trim();

	 // Calculate expected quantity
	    int  expectedPreviewQty = Integer.parseInt(currentStockQty) + 2000;

	    Assert.assertEquals("Updated table quantity is incorrect.",
	            String.valueOf(expectedPreviewQty),
	            actualPreviewQty);

	    System.out.println("✅ Expected Quantity : " + expectedPreviewQty);
	    System.out.println("✅ Actual Quantity   : " + actualPreviewQty);
	    Common.waitForElement(2);
	    // ===== Click three-dot menu =====
	    WebElement threeDot = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//div[contains(@class,'material_action_dropdown')]//label")));
	    threeDot.click();

	    // ===== Click Stock History =====
	    WebElement stockHistory = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[normalize-space()='Stock History']")));
	    stockHistory.click();
	    System.out.println("✅ Opened Stock History");
	    Common.waitForElement(3);

	    // ===== Verify adjusted quantity =====
	    WebElement adjustedQty = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//p[contains(@class,'count_para')])[1]")));

	    String actualQty1 = adjustedQty.getText().trim();
	    String expectedHistoryQty = "+" + 2000;

	    Assert.assertEquals("Adjusted quantity mismatch.",
	    		expectedHistoryQty,
	            actualQty1);

	    System.out.println("✅ Adjusted Quantity : " + actualQty1);

	    // ===== Verify Stocks Adjusted =====
	    WebElement stockAdjusted = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//p[normalize-space()='Stock Added'])[1]")));

	    String actualStatus1 = stockAdjusted.getText().trim();
	    String expectedStatus = "Stock Added";

	    Assert.assertEquals("Stock Added status mismatch.",
	            expectedStatus,
	            actualStatus1);

	    System.out.println("✅ Stock Added Status Verified : " + actualStatus1);
	    
	}
	String currentStockQty1;
	public void completeLowAlertFlow() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    WebElement currentStock = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//tbody/tr[1]/td[2])[1]")));

	     currentStockQty1 = currentStock.getText().trim();

	    System.out.println("✅ Current Stock Quantity : " + currentStockQty1);
	    Common.waitForElement(1);
	    
	    
	    // Click three-dot menu
	    WebElement threeDots = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//div[contains(@class,'material_action_dropdown')]//label")));
	    threeDots.click();
	    System.out.println("🖱️ Clicked Three Dots");
	    Common.waitForElement(1);
	    // Click Set Low Stock Alert
	    WebElement lowStockAlert = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[normalize-space()='Set Low Stock Alert']")));
	    lowStockAlert.click();
	    System.out.println("🖱️ Clicked Set Low Stock Alert");
	    Common.waitForElement(1);
	    // Wait for modal
	    wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.cssSelector(".modal.show")));

	    // Enter captured quantity
	    WebElement qtyField = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//input[@name='low_alert_level']")));   // <-- verify name attribute

	    qtyField.clear();
	    qtyField.sendKeys(String.valueOf(currentStockQty1));

	    System.out.println("✅ Entered Low Stock Quantity : " + currentStockQty1);

	    // Click Save Alert
	    WebElement saveAlertBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[normalize-space()='Save Alert']")));
	    saveAlertBtn.click();

	    System.out.println("💾 Clicked Save Alert");

	    Common.waitForElement(3);
	    
	}
	
	
	public void verifyLowStockStatus() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	 // ===== Verify Low Stock status =====
	    WebElement stockStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//p[contains(@class,'stock_warning_para')]")));

	    String actualStatus = stockStatus.getText().trim();

	    Assert.assertEquals("Stock status is incorrect.",
	            "Low Stock",
	            actualStatus);

	    System.out.println("✅ Stock Status : " + actualStatus);

	 // ===== Verify updated table quantity =====
	    WebElement tableQty = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(//tbody//tr[1]//td[3])[1]")));

	    String actualQty = tableQty.getText().trim();

	    // Calculate expected quantity
	    int expectedQty = Integer.parseInt(currentStockQty1);
	    
	    Assert.assertEquals("Updated table quantity is incorrect.",
	            String.valueOf(expectedQty),
	            actualQty);

	    System.out.println("✅ Expected Quantity : " + expectedQty);
	    System.out.println("✅ Actual Quantity   : " + actualQty);

	    
	}
	
	
	String uploadedFileName;
	public void verifyRawmaterialImportFlow() throws Exception {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    Actions actions = new Actions(driver);

	    // Hover on Inventory
	    WebElement inventory = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//button[contains(@class,'sidebar_menu_btn')]//span[normalize-space()='Inventory']")));
	    actions.moveToElement(inventory).perform();

	    // Click Raw Material Stocks
	    WebElement rawMaterial = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[normalize-space()='Raw Material Stocks']")));
	    rawMaterial.click();

	    Common.waitForElement(2);

	    // Click Import/Export button
	    WebElement importExportBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[contains(@class,'import-export-btn')]")));
	    importExportBtn.click();

	    System.out.println("✅ Clicked Import/Export button");

	    // Click Import option
	    WebElement importOption = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[contains(@class,'js-custom-import-trigger') and normalize-space()='Import']")));
	    importOption.click();

	    System.out.println("✅ Clicked Import option");
	    
	    updateRawMaterialExcel();

	    Common.waitForElement(4);

	    // Upload Excel file
	    String excelFilePath = System.getProperty("user.dir") + "/src/test/resources/ImportFile/RawMaterial.xlsx";

	    WebElement uploadInput = wait.until(ExpectedConditions.presenceOfElementLocated(
	            By.id("simFileInput")));

	    uploadInput.sendKeys(excelFilePath);

	    System.out.println("✅ Excel file selected");

	 // Verify upload successful
	    WebElement uploadedFile = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//div[@id='simDropzone' and contains(@class,'uploaded')]")));

	    Assert.assertTrue("Excel file upload failed.", uploadedFile.isDisplayed());

	    System.out.println(GREEN + "✅ Excel file uploaded successfully." + RESET);

	     uploadedFileName = driver.findElement(
	            By.xpath("//div[@id='simDropzone']//div[contains(@class,'drop-title')]"))
	            .getText().trim();

	    System.out.println(CYAN + "📄 Uploaded File : " + uploadedFileName + RESET);
	    
	}
	
	
	// Class variables
	public String expectedExcelSku;
	public String expectedExcelMaterialName;
	public String expectedExcelCategory;
	public String expectedExcelQuantity;
	public String expectedExcelLowAlertLevel;
	public String expectedExcelSupplier;
	public String expectedExcelUnitMeasurement;
	public String expectedExcelMaterialType;
	// ANSI Colors
	final String RESET  = "\u001B[0m";
	final String GREEN  = "\u001B[32m";
	final String CYAN   = "\u001B[36m";
	final String YELLOW = "\u001B[33m";
	public void updateRawMaterialExcel() throws Exception {
		
		String excelFilePath = System.getProperty("user.dir") + "/src/test/resources/ImportFile/RawMaterial.xlsx";
	    FileInputStream fis = new FileInputStream(excelFilePath);
	    Workbook workbook = new XSSFWorkbook(fis);
	    Sheet sheet = workbook.getSheetAt(0);

	    // Row 0 = Main Heading
	    // Row 1 = Column Names
	    // Row 2 = First Data Row
	    Row row = sheet.getRow(2);

	    Random random = new Random();

	    expectedExcelSku = "Test" + (100 + random.nextInt(900));
	    expectedExcelMaterialName = "Auto Material " + (100 + random.nextInt(900));

	    row.getCell(0).setCellValue(expectedExcelSku);           // SKU
	    row.getCell(1).setCellValue(expectedExcelMaterialName);  // Name

	    // Copy all values after modification
	    expectedExcelCategory = row.getCell(2).getStringCellValue();
	    expectedExcelQuantity = String.valueOf((int) row.getCell(3).getNumericCellValue());
	    expectedExcelLowAlertLevel = String.valueOf((int) row.getCell(4).getNumericCellValue());
	    expectedExcelSupplier = row.getCell(5).getStringCellValue();
	    expectedExcelUnitMeasurement = row.getCell(6).getStringCellValue();
	    expectedExcelMaterialType = row.getCell(7).getStringCellValue();

	    fis.close();

	    FileOutputStream fos = new FileOutputStream(excelFilePath);
	    workbook.write(fos);

	    fos.close();
	    workbook.close();
	    System.out.println(GREEN + "========== UPDATED EXCEL DATA ==========" + RESET);

	    System.out.println(CYAN + "SKU              : " + RESET + expectedExcelSku);
	    System.out.println(CYAN + "Name             : " + RESET + expectedExcelMaterialName);
	    System.out.println(CYAN + "Category         : " + RESET + expectedExcelCategory);
	    System.out.println(CYAN + "Quantity         : " + RESET + expectedExcelQuantity);
	    System.out.println(CYAN + "Low Alert Level  : " + RESET + expectedExcelLowAlertLevel);
	    System.out.println(CYAN + "Supplier         : " + RESET + expectedExcelSupplier);
	    System.out.println(CYAN + "Unit Measurement : " + RESET + expectedExcelUnitMeasurement);
	    System.out.println(CYAN + "Material Type    : " + RESET + expectedExcelMaterialType);

	    System.out.println(YELLOW + "========================================" + RESET);
	}
	
	
	public void verifyImportPreviewAndStartImport() {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    // ANSI Colors
	    final String RESET  = "\u001B[0m";
	    final String GREEN  = "\u001B[32m";
	    final String CYAN   = "\u001B[36m";
	    final String YELLOW = "\u001B[33m";

	    // ==============================
	    // Click Next (Upload Screen)
	    // ==============================
	    WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.id("simNextBtn")));
	    nextBtn.click();

	    System.out.println(GREEN + "✅ Clicked First Next Button" + RESET);

	    Common.waitForElement(2);

	    // ==============================
	    // Verify Uploaded File Name
	    // ==============================
	    WebElement fileName = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.id("simFileName")));

	    Assert.assertEquals(uploadedFileName,
	            fileName.getText().trim());

	    System.out.println(CYAN + "Uploaded File Matched : "
	            + fileName.getText().trim() + RESET);

	    // ==============================
	    // Click Next (Preview Screen)
	    // ==============================
	    nextBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.id("simNextBtn")));
	    nextBtn.click();

	    System.out.println(GREEN + "✅ Clicked Second Next Button" + RESET);

	    Common.waitForElement(3);

	    // ==============================
	    // Verify Preview Data
	    // ==============================

	    WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//tbody[@id='simPreviewBody']/tr")));

	    String actualSku =
	            row.findElement(By.xpath("./td[1]")).getText().trim();

	    String actualName =
	            row.findElement(By.xpath("./td[2]")).getText().trim();

	    String actualCategory =
	            row.findElement(By.xpath("./td[3]")).getText().trim();

	    String actualQuantity =
	            row.findElement(By.xpath("./td[4]")).getText().trim();

	    String actualLowAlert =
	            row.findElement(By.xpath("./td[5]")).getText().trim();

	    String actualUnit =
	            row.findElement(By.xpath("./td[6]")).getText().trim();

	    String actualSupplier =
	            row.findElement(By.xpath("./td[7]")).getText().trim();

	    String actualMaterialType =
	            row.findElement(By.xpath("./td[8]")).getText().trim();

	    System.out.println(YELLOW + "\n========== IMPORT PREVIEW DATA ==========" + RESET);

	    System.out.println(CYAN + "SKU              : " + actualSku + RESET);
	    System.out.println(CYAN + "Name             : " + actualName + RESET);
	    System.out.println(CYAN + "Category         : " + actualCategory + RESET);
	    System.out.println(CYAN + "Quantity         : " + actualQuantity + RESET);
	    System.out.println(CYAN + "Low Alert Level  : " + actualLowAlert + RESET);
	    System.out.println(CYAN + "Unit Measurement : " + actualUnit + RESET);
	    System.out.println(CYAN + "Supplier         : " + actualSupplier + RESET);
	    System.out.println(CYAN + "Material Type    : " + actualMaterialType + RESET);

	    Assert.assertEquals(expectedExcelSku, actualSku);
	    Assert.assertEquals(expectedExcelMaterialName, actualName);
	    Assert.assertEquals(expectedExcelCategory, actualCategory);
	    Assert.assertEquals(expectedExcelQuantity, actualQuantity);
	    Assert.assertEquals(expectedExcelLowAlertLevel, actualLowAlert);
	    Assert.assertEquals(expectedExcelUnitMeasurement, actualUnit);
	    Assert.assertEquals(expectedExcelSupplier, actualSupplier);
	    Assert.assertEquals(expectedExcelMaterialType, actualMaterialType);

	    System.out.println(GREEN + "✅ Import Preview Data Verified Successfully." + RESET);

	    // ==============================
	    // Select Overwrite
	    // ==============================
	    WebElement overwrite = wait.until(ExpectedConditions.elementToBeClickable(
	            By.id("previewOverwrite")));
	    overwrite.click();

	    System.out.println(GREEN + "✅ Selected Overwrite Option" + RESET);

	    // ==============================
	    // Click Start Import
	    // ==============================
	    WebElement startImport = wait.until(ExpectedConditions.elementToBeClickable(
	            By.id("simStartImportBtn")));
	    startImport.click();

	    System.out.println(GREEN + "✅ Clicked Start Import Button" + RESET);
	    
	    Common.waitForElement(4);

	 // ==============================
	 // Verify Import Successful
	 // ==============================
	 WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
	         By.id("simResultTitle")));

	 String actualMessage = successMsg.getText().trim();

	 Assert.assertEquals("Import success message mismatch.",
	         "Import Successful!",
	         actualMessage);

	 System.out.println(GREEN + "✅ Import Status : " + actualMessage + RESET);

	 // ==============================
	 // Click Close Button
	 // ==============================
	 WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
	         By.xpath("//button[normalize-space()='Close']")));

	 closeBtn.click();

	 System.out.println(GREEN + "✅ Clicked Close Button" + RESET);
	}
	
	
	public void validateImportedRawMaterialData() {
		Common.waitForElement(2);
		driver.navigate().refresh();
		Common.waitForElement(2);
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    // ANSI Colors
	    final String RESET  = "\u001B[0m";
	    final String GREEN  = "\u001B[32m";
	    final String RED    = "\u001B[31m";
	    final String CYAN   = "\u001B[36m";
	    final String YELLOW = "\u001B[33m";

	    // Wait for first row
	    WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//table//tbody/tr[1]")));

	    String actualSku          = firstRow.findElement(By.xpath("./td[1]")).getText().trim();
	    String actualName         = firstRow.findElement(By.xpath("./td[2]")).getText().trim();
	    String actualCategory     = firstRow.findElement(By.xpath("./td[3]")).getText().trim();
	    String actualMaterialType = firstRow.findElement(By.xpath("./td[4]")).getText().trim();
	    String actualQuantity     = firstRow.findElement(By.xpath("./td[6]")).getText().trim();
	    String actualSupplier     = firstRow.findElement(By.xpath("./td[7]")).getText().trim();

	    // Supplier Mapping
	    String expectedSupplierName = expectedExcelSupplier;
	    if ("S10".equalsIgnoreCase(expectedExcelSupplier)) {
	        expectedSupplierName = "Supplier-02";
	    }

	    System.out.println(YELLOW + "\n=========== IMPORTED RAW MATERIAL VERIFICATION ===========" + RESET);

	    validateField("SKU", expectedExcelSku, actualSku, CYAN, GREEN, RED, RESET);
	    validateField("Material Name", expectedExcelMaterialName, actualName, CYAN, GREEN, RED, RESET);
	    validateField("Category", expectedExcelCategory, actualCategory, CYAN, GREEN, RED, RESET);
	    validateField("Material Type", expectedExcelMaterialType, actualMaterialType, CYAN, GREEN, RED, RESET);
	    validateField("Quantity", expectedExcelQuantity, actualQuantity, CYAN, GREEN, RED, RESET);
	    validateField("Supplier", expectedSupplierName, actualSupplier, CYAN, GREEN, RED, RESET);

	    System.out.println(YELLOW + "==========================================================" + RESET);

	    Assert.assertEquals("SKU mismatch.", expectedExcelSku, actualSku);
	    Assert.assertEquals("Material Name mismatch.", expectedExcelMaterialName, actualName);
	    Assert.assertEquals("Category mismatch.", expectedExcelCategory, actualCategory);
	    Assert.assertEquals("Material Type mismatch.", expectedExcelMaterialType, actualMaterialType);
	    Assert.assertEquals("Quantity mismatch.", expectedExcelQuantity, actualQuantity);
	    Assert.assertEquals("Supplier mismatch.", expectedSupplierName, actualSupplier);

	    System.out.println(GREEN + "✅ Imported Raw Material verified successfully." + RESET);
	}
	
	
	public static List<Map<String, Object>> readProductsWithMultiple(String filePath) throws IOException {

	    List<Map<String, Object>> data = new ArrayList<>();

	    FileInputStream fis = new FileInputStream(filePath);
	    Workbook workbook = WorkbookFactory.create(fis);
	    Sheet sheet = workbook.getSheetAt(0);

	    // Header Row (Row 0)
	    Row headerRow = sheet.getRow(0);
	    

	    for (int i = 1; i <= sheet.getLastRowNum(); i++) {

	        Row row = sheet.getRow(i);

	        if (row == null)
	            continue;

	        Map<String, Object> rowData = new LinkedHashMap<>();

	        for (int j = 0; j < headerRow.getLastCellNum(); j++) {

	            Cell headerCell = headerRow.getCell(j);
	            Cell cell = row.getCell(j);

	            String columnName = headerCell.getStringCellValue().trim();

	            Object value = "";

	            if (cell != null) {

	                switch (cell.getCellType()) {

	                case STRING:
	                    value = cell.getStringCellValue().trim();
	                    break;

	                case NUMERIC:
	                    if (DateUtil.isCellDateFormatted(cell)) {
	                        value = cell.getDateCellValue();
	                    } else {

	                        double num = cell.getNumericCellValue();

	                        if (num == (long) num)
	                            value = String.valueOf((long) num);
	                        else
	                            value = String.valueOf(num);
	                    }
	                    break;

	                case BOOLEAN:
	                    value = cell.getBooleanCellValue();
	                    break;

	                case FORMULA:
	                    value = cell.toString().trim();
	                    break;

	                case BLANK:
	                    value = "";
	                    break;

	                default:
	                    value = cell.toString().trim();
	                }
	            }

	            rowData.put(columnName, value);
	        }

	        data.add(rowData);
	    }

	    workbook.close();
	    fis.close();

	    return data;
	}
	
	public void exportRawMaterialLast7Days() {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    Actions actions = new Actions(driver);

	    // Hover on Inventory
	    WebElement inventory = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//button[contains(@class,'sidebar_menu_btn')]//span[normalize-space()='Inventory']")));
	    actions.moveToElement(inventory).perform();

	    // Click Raw Material Stocks
	    WebElement rawMaterial = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[normalize-space()='Raw Material Stocks']")));
	    rawMaterial.click();

	    Common.waitForElement(2);
	    
	    // Click Import/Export button
	    WebElement importExportBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[contains(@class,'import-export-btn')]")));
	    importExportBtn.click();

	    System.out.println("✅ Clicked Import/Export button");

	    // Click Export option
	    WebElement exportOption = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[@class='js-option' and normalize-space()='Export']")));
	    exportOption.click();
	    Common.waitForElement(2);
	    System.out.println("✅ Clicked Export option");

	    // ANSI Colors
	    final String RESET = "\u001B[0m";
	    final String GREEN = "\u001B[32m";

	    // ==============================
	    // Select Updated At -> Last 7 Days
	    // ==============================
	    WebElement updatedAt = wait.until(ExpectedConditions.elementToBeClickable(
	            By.name("export_updated_at_range")));

	    Select updatedSelect = new Select(updatedAt);
	    updatedSelect.selectByValue("7");

	    System.out.println(GREEN + "✅ Updated At : Last 7 Days selected" + RESET);

	    // ==============================
	    // Select Created At -> Last 7 Days
	    // ==============================
	    WebElement createdAt = wait.until(ExpectedConditions.elementToBeClickable(
	            By.name("export_created_at_range")));

	    Select createdSelect = new Select(createdAt);
	    createdSelect.selectByValue("7");

	    System.out.println(GREEN + "✅ Created At : Last 7 Days selected" + RESET);

	    Common.waitForElement(1);
	    WebElement xlsxOption = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//label[@for='xlsx']")));
	    xlsxOption.click();

	    System.out.println("✅ Selected XLSX file format");
	    Common.waitForElement(2);
	    // ==============================
	    // Click Export Button
	    // ==============================
	    WebElement exportBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[@type='submit' and normalize-space()='Export']")));

	    exportBtn.click();

	    System.out.println(GREEN + "✅ Clicked Export Button" + RESET);
	    Common.waitForElement(2);
	}
	
	 private ExportValidator validator = new ExportValidator();
	    private String downloadDir ="C:\\Users\\Sarojkumar\\Downloads\\";
	public void downloadExportHistory() throws InterruptedException {
		// ✅ Go to Export Histories
        Common.waitForElement(2);
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));
	    Actions actions = new Actions(driver);

	    // Hover on Inventory
	    WebElement exportHistory = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[contains(@class,'sidebar_menu_btn')]//span[normalize-space()='Export History']")));
	    exportHistory.click();
        System.out.println("✅ Opened Export Histories page");

        // ✅ Wait until export = Success
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofMinutes(10))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        // Wait until first row status becomes Success
        wait.until(driver -> {

            driver.navigate().refresh();
            Common.waitForElement(2);

            WebElement status = driver.findElement(
                    By.xpath("//tbody/tr[1]/td[7]//span[@class='d-inline-flex']"));

            String currentStatus = status.getText().trim();
            System.out.println("📊 Current Status : " + currentStatus);

            return currentStatus.equalsIgnoreCase("Success");
        });

        System.out.println("✅ Export completed successfully.");

        // Click first row three-dot button
        WebElement threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tbody/tr[1]/td[last()]//a[contains(@class,'actions-buttons-column')]")));
        threeDotBtn.click();
	    Common.waitForElement(2);


        System.out.println("✅ Clicked Three Dot");

        // Click Download
        WebElement downloadBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//a[normalize-space()='Download'])[1]")));
        downloadBtn.click();

        System.out.println("✅ Export download started.");
        int randomNum = new Random().nextInt(1000);
        fileName = "RawMaterialExport_"  + randomNum + ".xlsx";

        Thread.sleep(10000);
        File file = validator.waitForDownload(downloadDir, fileName, 30);
        System.out.println("✅  Export saved: " + file.getAbsolutePath());
	}
	
	public String dateRange;
    String fileName;
	public void storeLast7DaysDateRange() {

	    final String RESET = "\u001B[0m";
	    final String GREEN = "\u001B[32m";

	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	    Calendar calendar = Calendar.getInstance();

	    // End Date = Today
	    String endDate = sdf.format(calendar.getTime());

	    // Start Date = Today - 7 Days
	    calendar.add(Calendar.DAY_OF_MONTH, -7);
	    String startDate = sdf.format(calendar.getTime());

	    dateRange = startDate + " - " + endDate;

	    System.out.println(GREEN + "✅ Selected Date Range : " + dateRange + RESET);
	}
	
	public void verifyExportedRawMaterialDates() throws Exception {
		
		storeLast7DaysDateRange();

	    String[] parts = dateRange.split(" - ");

	    String startDateStr = parts[0].trim();
	    String endDateStr = parts[1].trim();

	    String excelPath = downloadDir + fileName;

	    List<Map<String, Object>> exportedData =readProductsWithMultiple(excelPath);
	    

	    SimpleDateFormat excelFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    SimpleDateFormat rangeFormat = new SimpleDateFormat("dd-MM-yyyy");
	    Date startDate = rangeFormat.parse(startDateStr);
	    Date endDate = rangeFormat.parse(endDateStr);

	    Calendar cal = Calendar.getInstance();
	    cal.setTime(endDate);
	    cal.add(Calendar.DAY_OF_MONTH, 1);
	    cal.add(Calendar.SECOND, -1);

	    Date inclusiveEndDate = cal.getTime();

	    boolean invalidFound = false;

	    System.out.println("=================================================");
	    System.out.println("Checking Date Range : " + dateRange);
	    System.out.println("=================================================");

	    for (Map<String, Object> row : exportedData) {

	        // Verify Last Updated
	    	Object updatedObj = row.get("Last Updated");

	    	if (updatedObj == null) {
	    	    System.out.println("Available Columns : " + row.keySet());
	    	    throw new RuntimeException("Column 'Last Updated' not found in Excel.");
	    	}

	    	String updated = updatedObj.toString().trim();

	        Date updatedDate = excelFormat.parse(updated);

	        if (updatedDate.before(startDate) ||
	                updatedDate.after(inclusiveEndDate)) {

	            System.out.println("❌ Last Updated Out of Range : " + updated);
	            invalidFound = true;
	        }

	        // Verify Created At
	        Object createdObj = row.get("Created At");

	        if (createdObj == null) {
	            System.out.println("Available Columns : " + row.keySet());
	            throw new RuntimeException("Column 'Created At' not found in Excel.");
	        }

	        String created = createdObj.toString().trim();

	        Date createdDate = excelFormat.parse(created);

	        if (createdDate.before(startDate) ||
	                createdDate.after(inclusiveEndDate)) {

	            System.out.println("❌ Created At Out of Range : " + created);
	            invalidFound = true;
	        }
	    }

	    if (invalidFound) {

	        Assert.fail("❌ Export contains dates outside selected range : "
	                + dateRange);

	    } else {

	        System.out.println("✅ All Last Updated dates are within range.");
	        System.out.println("✅ All Created At dates are within range.");
	    }
	}
	
	
//TC-01	
	public void validateRawMaterialCreation() throws InterruptedException {
		
		adminLogin();
		
		fillRawMaterialDetails();
		
		verifyRawMaterialFirstRow();
		
	}
	
//TC-02
	public void validateRawMaterialStockAdjust() {
		
		adminLogin();
		
		selectRandomProductonRawMaterialSection();
		
		completeStockAdjustmentFlow();
		
		verifyOutOfStockStatusAndStockHistory();

	}
	
//TC-03
	public void validateRawMaterialStockAdd() {

		adminLogin();
		
		selectRandomProductonRawMaterialSection();
		
		completeStockAddFlow();
		
		verifyInStockStatusAndStockHistory();
		
		
	}
	
	
//TC-04
	public void validateRawMaterialLowAlert() {
		
		adminLogin();
		
		selectRandomProductonRawMaterialSection();	
		
		completeLowAlertFlow();
		
		verifyLowStockStatus();
		
	}
	
//TC-05
	public void validateImportFunctionalty() throws Exception {
		
		adminLogin();
		
		verifyRawmaterialImportFlow();
		
		verifyImportPreviewAndStartImport();
		
		validateImportedRawMaterialData();
		
	}
	
	
//TC-06
	
	public void validateExportFunctionalty() throws Exception {
		
		adminLogin();

		exportRawMaterialLast7Days();
		
		downloadExportHistory();
		
		verifyExportedRawMaterialDates();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean verifyExactText(WebElement ele, String expectedText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WebDriver gmail(String browserName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isAt() {
		// TODO Auto-generated method stub
		return false;
	}
}
