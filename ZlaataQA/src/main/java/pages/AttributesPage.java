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
import org.openqa.selenium.support.ui.WebDriverWait;

import objectRepo.Attributes_ObjRepo;
import utils.Common;

public class AttributesPage extends Attributes_ObjRepo {
	
	public AttributesPage(WebDriver driver) 
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(this.driver, this);
	}
	
	public void adminLogin() {
	
	AdminLogin_Page login= new AdminLogin_Page(driver);
	login.adminLoginApp();
	
	}
	
	String expectedAttributeName;
	String expectedAttributeValue;
	public void attributesCreateFlow() {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
 Actions actions = new Actions(driver);
	    
	    // Hover on Inventory
	    WebElement inventory = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//button[contains(@class,'sidebar_menu_btn')]//span[normalize-space()='Inventory']")));
	    actions.moveToElement(inventory).perform();

	    // Click Attributes Stocks
	    WebElement attributes = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//li[normalize-space()='Attribute']")));
	    attributes.click();

	    Common.waitForElement(2);

	    // Click Create Attribute button
	    WebElement createAttributeBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[@data-target='#AttributeCreateModal']")));
	    createAttributeBtn.click();
	    System.out.println("🖱️ Clicked Create Attribute");
	    Common.waitForElement(3);
	    // Wait for modal
	    wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.id("AttributeCreateModal")));

	    // Select Clothes category
	    WebElement clothesRadio = wait.until(ExpectedConditions.elementToBeClickable(
	            By.id("clothes")));
	    clothesRadio.click();
	    System.out.println("✅ Selected Clothes category");

	    // Enter Attribute Name
	    Random random = new Random();
	    expectedAttributeName = "Automation" + (1000 + random.nextInt(9000));

	    System.out.println("Generated Attribute Name: " + expectedAttributeName);
	    WebElement attributeName = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.id("new_attribute")));
	    attributeName.clear();
	    attributeName.sendKeys(expectedAttributeName);
	    attributeName.sendKeys(Keys.ENTER);
	    System.out.println("✅ Entered Attribute Name");
	    Common.waitForElement(3);
	    
	 // Store Attribute Values
	    String value1 = "Test";
	    String value2 = "Test1";
	    String value3 = "Test2";

	    // Store expected value for verification
	    expectedAttributeValue = value1 + ", " + value2 + ", " + value3;

	    // Enter Attribute Values
	    WebElement attributeValue = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.id("attribute_value")));

	    attributeValue.sendKeys(value1);
	    attributeValue.sendKeys(Keys.ENTER);
	    Common.waitForElement(2);

	    attributeValue.sendKeys(value2);
	    attributeValue.sendKeys(Keys.ENTER);
	    Common.waitForElement(2);

	    attributeValue.sendKeys(value3);
	    attributeValue.sendKeys(Keys.ENTER);
	    Common.waitForElement(2);

	    System.out.println("✅ Entered Attribute Values: " + expectedAttributeValue);

	    // Click Save Attributes
	    WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//button[contains(@class,'save-attribute-btn')]")));
	    saveBtn.click();

	    System.out.println("💾 Clicked Save Attributes");

	    Common.waitForElement(2);
	}
	
	public void verifyAttributeFirstRow() {

	    Common.waitForElement(2);

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    // Wait for first row
	    WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//table//tbody/tr[1]")));

	    // Capture first and second columns
	    String actualAttributeName = firstRow.findElement(By.xpath("./td[1]")).getText().trim();
	    String actualAttributeValue = firstRow.findElement(By.xpath("./td[2]")).getText().trim();

	    System.out.println("========== ATTRIBUTE VERIFICATION ==========");
	    System.out.println("Expected Attribute Name  : " + expectedAttributeName);
	    System.out.println("Actual Attribute Name    : " + actualAttributeName);
	    System.out.println();

	    System.out.println("Expected Attribute Value : " + expectedAttributeValue);
	    System.out.println("Actual Attribute Value   : " + actualAttributeValue);
	    System.out.println("============================================");

	    Assert.assertEquals("Attribute Name mismatch.",
	            expectedAttributeName.toLowerCase(),
	            actualAttributeName.toLowerCase());

	    Assert.assertEquals("Attribute Value mismatch.",
	            expectedAttributeValue,
	            actualAttributeValue);

	    System.out.println("✅ Attribute Name Verified");
	    System.out.println("✅ Attribute Value Verified");
	}
	
	
//copy
	
	
	// Class variables
	 String capturedAttributeName;
	 String capturedAttributeValue;
	 String editedAttributeName;
	 String editedAttributeValue;
	public void verifyattributesEditFlow() {
		
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		 Actions actions = new Actions(driver);
			    
			    // Hover on Inventory
			    WebElement inventory = wait.until(ExpectedConditions.visibilityOfElementLocated(
			            By.xpath("//button[contains(@class,'sidebar_menu_btn')]//span[normalize-space()='Inventory']")));
			    actions.moveToElement(inventory).perform();

			    // Click Attributes Stocks
			    WebElement attributes = wait.until(ExpectedConditions.elementToBeClickable(
			            By.xpath("//li[normalize-space()='Attribute']")));
			    attributes.click();

			    Common.waitForElement(2);
			    
			    // ===== Capture first row =====
			        WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
			                By.xpath("//table//tbody/tr[1]")));

			        capturedAttributeName = firstRow.findElement(By.xpath("./td[1]")).getText().trim();
			        capturedAttributeValue = firstRow.findElement(By.xpath("./td[2]")).getText().trim();

			        System.out.println("Captured Attribute Name : " + capturedAttributeName);
			        System.out.println("Captured Attribute Value: " + capturedAttributeValue);

			        // ===== Click first row three dots =====
			        WebElement threeDots = firstRow.findElement(
			                By.xpath("./td[last()]//a[contains(@class,'actions-buttons-column')]"));

			        wait.until(ExpectedConditions.elementToBeClickable(threeDots)).click();
			        Common.waitForElement(2);
			        // ===== Click Edit =====
			        WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(
			                By.xpath("(//a[@bp-button='update'])[1]")));
			        editBtn.click();
			        Common.waitForElement(3);
			        // ===== Generate random attribute name =====
			        editedAttributeName = "AutomationTest" + (1000 + new Random().nextInt(9000));

			        WebElement editName = wait.until(ExpectedConditions.visibilityOfElementLocated(
			                By.id("edit_new_attribute")));

			        editName.clear();
			        editName.sendKeys(editedAttributeName);
			        editName.sendKeys(Keys.ENTER);

			        System.out.println("Edited Attribute Name : " + editedAttributeName);
			        Common.waitForElement(2);
			        // ===== Add new value =====
			        editedAttributeValue = "Auto" + (100 + new Random().nextInt(900));

			        WebElement editValue = wait.until(ExpectedConditions.visibilityOfElementLocated(
			                By.id("edit_attribute_value")));

			        editValue.sendKeys(editedAttributeValue);
			        editValue.sendKeys(Keys.ENTER);

			        System.out.println("Added Attribute Value : " + editedAttributeValue);
			        Common.waitForElement(2);
			        // ===== Save =====
			        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
			                By.xpath("//button[contains(@class,'edit-save-attribute-btn')]")));
			        saveBtn.click();

			        System.out.println("✅ Attribute updated successfully.");
			        Common.waitForElement(2);
	}
	
	public void verifyEditedAttribute() throws InterruptedException {
		Thread.sleep(5000);

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    // Wait for first row
	    WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//table//tbody/tr[1]")));

	    // Capture updated values
	    String actualAttributeName = firstRow.findElement(By.xpath("./td[1]")).getText().trim();
	    String actualAttributeValue = firstRow.findElement(By.xpath("./td[2]")).getText().trim();

	    // Expected values
	    String expectedAttributeName = editedAttributeName;
	    String expectedAttributeValue = capturedAttributeValue + ", " + editedAttributeValue;

	    System.out.println("========== EDIT ATTRIBUTE VERIFICATION ==========");
	    System.out.println("Expected Name   : " + expectedAttributeName);
	    System.out.println("Actual Name     : " + actualAttributeName);
	    System.out.println();
	    System.out.println("Expected Values : " + expectedAttributeValue);
	    System.out.println("Actual Values   : " + actualAttributeValue);
	    System.out.println("=================================================");
	            


	    Assert.assertEquals("Edited Attribute Name mismatch.",
	    		expectedAttributeName.toLowerCase(),
	            actualAttributeName.toLowerCase());

	    Assert.assertEquals("Edited Attribute Values mismatch.",
	            expectedAttributeValue,
	            actualAttributeValue);

	    System.out.println("✅ Edited Attribute Name Verified");
	    System.out.println("✅ Edited Attribute Values Verified");
	}
	
	
	
	public void deleteAttributeAndVerify() throws InterruptedException {

		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		 Actions actions = new Actions(driver);
			    
			    // Hover on Inventory
			    WebElement inventory = wait.until(ExpectedConditions.visibilityOfElementLocated(
			            By.xpath("//button[contains(@class,'sidebar_menu_btn')]//span[normalize-space()='Inventory']")));
			    actions.moveToElement(inventory).perform();

			    // Click Attributes Stocks
			    WebElement attributes = wait.until(ExpectedConditions.elementToBeClickable(
			            By.xpath("//li[normalize-space()='Attribute']")));
			    attributes.click();

			    Common.waitForElement(2);

	    // ===== Capture first row attribute name =====
	    WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//table//tbody/tr[1]")));

	    String deletedAttributeName = firstRow.findElement(By.xpath("./td[1]")).getText().trim();

	    System.out.println("📝 Attribute to Delete : " + deletedAttributeName);

	    // ===== Click first row three dots =====
	    WebElement threeDots = firstRow.findElement(
	            By.xpath("./td[last()]//a[contains(@class,'actions-buttons-column')]"));

	    wait.until(ExpectedConditions.elementToBeClickable(threeDots)).click();
        Common.waitForElement(2);


	    // ===== Click Delete =====
	    WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("(//a[@bp-button='delete'])[1]")));
	    deleteBtn.click();

	    System.out.println("🗑️ Clicked Delete");

        Common.waitForElement(2);

	    // ===== Click Confirm =====
	    WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
	            By.id("DeleteConfirmBtn")));
	    confirmBtn.click();
        Common.waitForElement(2);

	    System.out.println("✅ Clicked Confirm");

	    // Wait until deleted row disappears
	    wait.until(ExpectedConditions.invisibilityOfElementWithText(
	            By.xpath("//table//tbody"),
	            deletedAttributeName));

	    Thread.sleep(3000);

	    // ===== Verify first row is different =====
	    WebElement newFirstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//table//tbody/tr[1]/td[1]")));

	    String currentFirstRowName = newFirstRow.getText().trim();

	    System.out.println("Deleted Attribute : " + deletedAttributeName);
	    System.out.println("Current First Row : " + currentFirstRowName);

	    Assert.assertNotEquals(
	            "Deleted attribute is still displayed in the first row.",
	            deletedAttributeName,
	            currentFirstRowName);

	    System.out.println("✅ Attribute deleted successfully and removed from the first row.");
	}
	
	
	
//TC-01	
	public void validateAttributesCreateFlow() {
		
		adminLogin();
		
		attributesCreateFlow();
		
		verifyAttributeFirstRow();
	}
	
	
//TC-02	
	public void validateAttributesEditFlow() throws InterruptedException {
		
		adminLogin();
		
		verifyattributesEditFlow();
		
		verifyEditedAttribute();
	}
	
//TC-03
	public void valiateDeleteAttribute() throws InterruptedException {
		
		adminLogin();
		
		deleteAttributeAndVerify();
		
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
