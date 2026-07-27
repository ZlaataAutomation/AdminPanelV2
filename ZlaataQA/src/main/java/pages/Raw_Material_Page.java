package pages;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import objectRepo.Raw_Material_ObjRepo;
import utils.Common;

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
	    Select category = new Select(wait.until(
	            ExpectedConditions.elementToBeClickable(By.name("category_id"))));
	    category.selectByIndex(1);

	    // Material Type
	    Select materialType = new Select(wait.until(
	            ExpectedConditions.elementToBeClickable(By.name("type_id"))));
	    materialType.selectByIndex(1);

	    // Unit Measurement
	    Select measurement = new Select(wait.until(
	            ExpectedConditions.elementToBeClickable(By.name("measurement_id"))));
	    measurement.selectByIndex(1);

	    // Supplier (Select2)
	    wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//span[contains(@class,'select2-selection--multiple')]"))).click();

	    WebElement supplierSearch = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//input[@class='select2-search__field']")));

	    supplierSearch.sendKeys(Keys.ENTER);

	    Common.waitForElement(1);

	    // Get Selected Supplier Name
	    String supplierName = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//li[contains(@class,'select2-selection__choice')]")))
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
	    expectedCategory = category.getFirstSelectedOption().getText();
	    expectedMaterialType = materialType.getFirstSelectedOption().getText();
	    expectedMeasurement = measurement.getFirstSelectedOption().getText();
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
