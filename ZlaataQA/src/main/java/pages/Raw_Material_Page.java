package pages;

import java.time.Duration;
import java.util.Random;

import org.junit.Assert;
import org.openqa.selenium.By;
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
	
	
	
	
	
	
//TC-01	
	public void validateRawMaterialCreation() throws InterruptedException {
		
		adminLogin();
		
		fillRawMaterialDetails();
		
		verifyRawMaterialFirstRow();
		
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
