package pages;

import java.time.Duration;
import java.util.Random;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import objectRepo.WareHouse_ObjRepo;
import utils.Common;

public class WareHouse_Page extends WareHouse_ObjRepo {
	
	public WareHouse_Page(WebDriver driver) 
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(this.driver, this);
	}
	
	public void adminLogin() {
	
	AdminLogin_Page login= new AdminLogin_Page(driver);
	login.adminLoginApp();
	
	}
	
	
	 public void navigatetoPurchaseOrderPage() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        wait.until(ExpectedConditions.visibilityOf(setting));
	        new Actions(driver).moveToElement(setting).perform();

	        wait.until(ExpectedConditions.visibilityOf(wareHouse));
	        click(wareHouse);
	    }
	 
	 
	// Warehouse Test Data
	 Random random = new Random();

	 String warehouseName = "Automation Warehouse "
	         + String.format("%03d", random.nextInt(1000));
	String legalLocation = "Bhubaneswar";
	String flatNumber = "House No 101";
	String roadArea = "Patia Main Road";
	String country = "India";
	String pincode = "751024";
	String contactNameValue = "Automation User";
	String contactEmailValue = "automation@test.com";
	String contactPhoneValue = "9876543210";


	public void createWarehouse() {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    Common.waitForElement(2);

	    // 1. Click Add New Warehouse
	    WebElement addWarehouse = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("//a[@bp-button='create' and .//span[normalize-space()='Add New Warehouse']]")
	            )
	    );
	    addWarehouse.click();
	    Common.waitForElement(2);
	    System.out.println("Add New Warehouse clicked.");

	    // 2. Enter Warehouse Name
	    WebElement warehouseNameField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("name")
	            )
	    );
	    warehouseNameField.clear();
	    warehouseNameField.sendKeys(warehouseName);
	    System.out.println("Warehouse Name entered: " + warehouseName);

	    // 3. Enter Legal Location
	    WebElement legalLocationField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("legal_location")
	            )
	    );
	    legalLocationField.clear();
	    legalLocationField.sendKeys(legalLocation);
	    System.out.println("Legal Location entered: " + legalLocation);

	    // 4. Enter House/Flat
	    WebElement flatNumberField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("flat_number")
	            )
	    );
	    flatNumberField.clear();
	    flatNumberField.sendKeys(flatNumber);
	    System.out.println("House/Flat entered: " + flatNumber);

	    // 5. Enter Apartment/Road Area
	    WebElement roadAreaField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("road_area")
	            )
	    );
	    roadAreaField.clear();
	    roadAreaField.sendKeys(roadArea);
	    System.out.println("Apartment/Road Area entered: " + roadArea);

	    // 6. Select Country
	    WebElement countryDropdown = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.id("country")
	            )
	    );
	    Select countrySelect = new Select(countryDropdown);
	    countrySelect.selectByVisibleText(country);
	    Common.waitForElement(2);
	    System.out.println("Country selected: " + country);

	    // 7. Enter PIN Code
	    WebElement pincodeField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.id("pincode")
	            )
	    );
	    pincodeField.clear();
	    pincodeField.sendKeys(pincode);
	    Common.waitForElement(2);
	    System.out.println("PIN Code entered: " + pincode);

	    // 8. Enter Contact Name
	    WebElement contactNameField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("contact_name[]")
	            )
	    );
	    contactNameField.clear();
	    contactNameField.sendKeys(contactNameValue);
	    System.out.println("Contact Name entered: " + contactNameValue);

	    // 9. Enter Contact Email
	    WebElement contactEmailField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("contact_email[]")
	            )
	    );
	    contactEmailField.clear();
	    contactEmailField.sendKeys(contactEmailValue);
	    System.out.println("Contact Email entered: " + contactEmailValue);

	    // 10. Enter Contact Phone
	    WebElement contactPhoneField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("contact_phone[]")
	            )
	    );
	    contactPhoneField.clear();
	    contactPhoneField.sendKeys(contactPhoneValue);
	    System.out.println("Contact Phone entered: " + contactPhoneValue);

	    // 11. Verify all entered data

	    Assert.assertEquals(
	            "Warehouse Name is not entered correctly",
	            warehouseName,
	            warehouseNameField.getAttribute("value")
	    );

	    Assert.assertEquals(
	            "Legal Location is not entered correctly",
	            legalLocation,
	            legalLocationField.getAttribute("value")
	    );

	    Assert.assertEquals(
	            "House/Flat is not entered correctly",
	            flatNumber,
	            flatNumberField.getAttribute("value")
	    );

	    Assert.assertEquals(
	            "Road Area is not entered correctly",
	            roadArea,
	            roadAreaField.getAttribute("value")
	    );

	    Assert.assertEquals(
	            "PIN Code is not entered correctly",
	            pincode,
	            pincodeField.getAttribute("value")
	    );

	    Assert.assertEquals(
	            "Contact Name is not entered correctly",
	            contactNameValue,
	            contactNameField.getAttribute("value")
	    );

	    Assert.assertEquals(
	            "Contact Email is not entered correctly",
	            contactEmailValue,
	            contactEmailField.getAttribute("value")
	    );

	    Assert.assertEquals(
	            "Contact Phone is not entered correctly",
	            contactPhoneValue,
	            contactPhoneField.getAttribute("value")
	    );

	    System.out.println("All warehouse details entered successfully.");

	    Common.waitForElement(3);

	    // 12. Click Save
	    WebElement saveButton = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("//button[@type='submit' and normalize-space()='Save']")
	            )
	    );
	    saveButton.click();

	    Common.waitForElement(3);
	    System.out.println("Warehouse details saved successfully.");

	    // 13. Verify Warehouses page heading
	    WebElement warehousesHeading = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath("//h1[@class='page_heading' and normalize-space()='warehouses']")
	            )
	    );

	    Assert.assertTrue(
	            "Warehouses page heading is not displayed after saving the warehouse",
	            warehousesHeading.isDisplayed()
	    );

	    String actualHeading = warehousesHeading.getText().trim();

	    Assert.assertEquals(
	            "Warehouses page heading is incorrect",
	            "Warehouses",
	            actualHeading
	    );

	    System.out.println(
	            "Warehouse created successfully and Warehouses page is displayed."
	    );
	}
	
	public void verifyCreatedWarehouse() {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    Common.waitForElement(2);
	    // 1. Verify created Warehouse Name in first row - 2nd column
	    WebElement firstRow = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.cssSelector("tbody tr:first-child")
	            )
	    );

	    WebElement warehouseNameInListing = firstRow.findElement(
	            By.xpath("./td[2]//span[normalize-space()='" + warehouseName + "']")
	    );

	    String actualWarehouseName = warehouseNameInListing.getText().trim();

	    Assert.assertEquals(
	            "Created Warehouse Name is not matching in the listing",
	            warehouseName,
	            actualWarehouseName
	    );

	    System.out.println(
	            "Warehouse Name verified in listing: " + actualWarehouseName
	    );

	    // 2. Click Three Dots
	    WebElement firstThreeDots = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("(//i[contains(@class,'bi-three-dots-vertical')])[1]")
	            )
	    );

	    firstThreeDots.click();

	    Common.waitForElement(2);

	    // 4. Click Preview
	    WebElement preview = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
	            )
	    );

	    preview.click();

	    Common.waitForElement(2);

	    System.out.println("Warehouse Preview clicked.");

	    // 4. Verify Warehouse Name in Preview
	    WebElement previewWarehouseName = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Warehouse Name']"
	                                    + "/following-sibling::dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualPreviewWarehouseName =
	            previewWarehouseName.getText().trim();

	    Assert.assertEquals(
	            "Warehouse Name is not matching in Preview",
	            warehouseName,
	            actualPreviewWarehouseName
	    );

	    System.out.println(
	            "Preview Warehouse Name verified: " + actualPreviewWarehouseName
	    );

	    // 5. Verify Legal Location
	    WebElement previewLegalLocation = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Legal Location']"
	                                    + "/following-sibling::dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualLegalLocation =
	            previewLegalLocation.getText().trim();

	    Assert.assertEquals(
	            "Legal Location is not matching in Preview",
	            legalLocation,
	            actualLegalLocation
	    );

	    System.out.println(
	            "Preview Legal Location verified: " + actualLegalLocation
	    );

	    // 6. Verify Address
	    WebElement previewAddress = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Address']"
	                                    + "/following-sibling::dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualAddress = previewAddress.getText().trim();

	    Assert.assertTrue(
	            "House/Flat is not displayed correctly in Preview Address",
	            actualAddress.contains(flatNumber)
	    );

	    Assert.assertTrue(
	            "Road Area is not displayed correctly in Preview Address",
	            actualAddress.contains(roadArea)
	    );

	    Assert.assertTrue(
	            "PIN Code is not displayed correctly in Preview Address",
	            actualAddress.contains(pincode)
	    );

	    System.out.println(
	            "Preview Address verified: " + actualAddress
	    );

	    // 7. Verify Contact Name
	    WebElement previewContactName = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Contact Name']"
	                                    + "/following-sibling::dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualContactName =
	            previewContactName.getText().trim();

	    Assert.assertEquals(
	            "Contact Name is not matching in Preview",
	            contactNameValue,
	            actualContactName
	    );

	    System.out.println(
	            "Preview Contact Name verified: " + actualContactName
	    );

	    // 8. Verify Email
	    WebElement previewEmail = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Email']"
	                                    + "/following-sibling::div//dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualEmail =
	            previewEmail.getText().trim();

	    Assert.assertEquals(
	            "Contact Email is not matching in Preview",
	            contactEmailValue,
	            actualEmail
	    );

	    System.out.println(
	            "Preview Email verified: " + actualEmail
	    );

	    // 9. Verify Phone
	    WebElement previewPhone = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Phone']"
	                                    + "/following-sibling::div//dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualPhone =
	            previewPhone.getText().trim();

	    Assert.assertEquals(
	            "Contact Phone is not matching in Preview",
	            contactPhoneValue,
	            actualPhone
	    );

	    System.out.println(
	            "Preview Phone verified: " + actualPhone
	    );

	    System.out.println(
	            "All created Warehouse details verified successfully in Preview."
	    );
	}
	

	

	 String editWarehouseName = "Edit Automation Warehouse "
	         + String.format("%03d", random.nextInt(1000));
	String editLegalLocation = "Bhubaneswar";
	String editFlatNumber = "House No 501";
	String editRoadArea = "Patia Main Road";
	String editContactNameValue = "Edit Automation User";
	String EeditContactEmailValue = "editautomation@test.com";
	String editContactPhoneValue = "9876543211";
	public void editExistingWarehouse() {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    Common.waitForElement(2);

	    // =========================================================
	    // 1. Get First Warehouse Row
	    // =========================================================

	    WebElement firstRow = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.cssSelector("tbody tr:first-child")
	            )
	    );

	    System.out.println("First warehouse row displayed.");

	    // =========================================================
	    // 2. Click Three Dots
	    // =========================================================

	    WebElement firstThreeDots = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("(//i[contains(@class,'bi-three-dots-vertical')])[1]")
	            )
	    );

	    firstThreeDots.click();

	    Common.waitForElement(2);

	    // 4. Click Preview
	    WebElement preview = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
	            )
	    );

	    preview.click();

	    Common.waitForElement(2);

	    System.out.println("Warehouse Preview opened.");

	    // =========================================================
	    // 4. Copy / Store Warehouse Details From Preview
	    // =========================================================

	 // Warehouse ID
	    WebElement warehouseIdElement = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Warehouse ID']"
	                            + "/following-sibling::dd[contains(@class,'view_value')]"
	                    )
	            )
	    );
	    String existingWarehouseId = warehouseIdElement.getText().trim();


	   	    Common.waitForElement(2);	
		 // 4. Click three-dots menu on Purchase Receive Preview page
		    WebElement previewThreeDots = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//button[@data-toggle='dropdown' and .//i[contains(@class,'bi-three-dots-vertical')]]")
		            )
		    );

		    previewThreeDots.click();

		    Common.waitForElement(1);


		    // 5. Click Edit
		    WebElement editButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//a[contains(@class,'dropdown-item') and normalize-space()='Edit']")
		            )
		    );

		    editButton.click();

		    Common.waitForElement(2);

	    System.out.println("Edit Warehouse page opened.");

	    // =========================================================
	    // 9. Fill Warehouse Name
	    // =========================================================

	    WebElement warehouseNameField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("name")
	            )
	    );

	    warehouseNameField.clear();
	    warehouseNameField.sendKeys(editWarehouseName);

	    // =========================================================
	    // 10. Fill Legal Location
	    // =========================================================

	    WebElement legalLocationField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("legal_location")
	            )
	    );

	    legalLocationField.clear();
	    legalLocationField.sendKeys(editLegalLocation);

	    // =========================================================
	    // 11. Fill Address Fields
	    // =========================================================

	    WebElement flatNumberField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("flat_number")
	            )
	    );

	    flatNumberField.clear();
	    flatNumberField.sendKeys(editFlatNumber);

	    WebElement roadAreaField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("road_area")
	            )
	    );

	    roadAreaField.clear();
	    roadAreaField.sendKeys(editRoadArea);

	    // =========================================================
	    // 12. Select Country
	    // =========================================================

	    WebElement countryDropdown = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.id("country")
	            )
	    );

	    Select countrySelect = new Select(countryDropdown);
	    countrySelect.selectByVisibleText(country);

	    Common.waitForElement(1);

	    // =========================================================
	    // 13. Fill PIN Code
	    // =========================================================

	    WebElement pincodeField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.id("pincode")
	            )
	    );

	    pincodeField.clear();
	    pincodeField.sendKeys(pincode);

	    Common.waitForElement(2);

	    // =========================================================
	    // 14. Fill Contact Name
	    // =========================================================

	    WebElement contactNameField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("contact_name[]")
	            )
	    );

	    contactNameField.clear();
	    contactNameField.sendKeys(editContactNameValue);

	    // =========================================================
	    // 15. Fill Contact Email
	    // =========================================================

	    WebElement contactEmailField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("contact_email[]")
	            )
	    );

	    contactEmailField.clear();
	    contactEmailField.sendKeys(EeditContactEmailValue);

	    // =========================================================
	    // 16. Fill Contact Phone
	    // =========================================================

	    WebElement contactPhoneField = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.name("contact_phone[]")
	            )
	    );

	    contactPhoneField.clear();
	    contactPhoneField.sendKeys(editContactPhoneValue);

	    System.out.println("All existing warehouse details filled in Edit page.");

	    // =========================================================
	    // 17. Verify Entered Details
	    // =========================================================

	    Assert.assertEquals(
	            "Warehouse Name is not entered correctly",
	            editWarehouseName,
	            warehouseNameField.getAttribute("value")
	    );

	    Assert.assertEquals(
	            "Legal Location is not entered correctly",
	            editLegalLocation,
	            legalLocationField.getAttribute("value")
	    );

	    Assert.assertEquals(
	            "Contact Name is not entered correctly",
	            editContactNameValue,
	            contactNameField.getAttribute("value")
	    );

	    Assert.assertEquals(
	            "Email is not entered correctly",
	            EeditContactEmailValue,
	            contactEmailField.getAttribute("value")
	    );

	    Assert.assertEquals(
	            "Phone is not entered correctly",
	            editContactPhoneValue,
	            contactPhoneField.getAttribute("value")
	    );

	    System.out.println("All warehouse details verified before update.");

	    Common.waitForElement(2);

	    // =========================================================
	    // 18. Click Update
	    // =========================================================

	    WebElement updateButton = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath(
	                            "//button[@type='submit' "
	                            + "and normalize-space()='Update']"
	                    )
	            )
	    );

	    updateButton.click();

	    Common.waitForElement(3);

	    System.out.println("Warehouse updated successfully.");

	    // =========================================================
	    // 19. Verify Warehouses Page Heading
	    // =========================================================

	    WebElement warehousesHeading = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//h1[@class='page_heading' "
	                            + "and normalize-space()='warehouses']"
	                    )
	            )
	    );

	    Assert.assertTrue(
	            "Warehouses page heading is not displayed after updating warehouse",
	            warehousesHeading.isDisplayed()
	    );

	    String actualHeading = warehousesHeading.getText().trim();

	    Assert.assertEquals(
	            "Warehouses page heading is incorrect",
	            "Warehouses",
	            actualHeading
	    );

	    System.out.println(
	            "Warehouse updated successfully and Warehouses page is displayed."
	    );
	}
	
	public void verifyEditedWarehouse() {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    Common.waitForElement(2);
	    // 1. Verify created Warehouse Name in first row - 2nd column
	    WebElement firstRow = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.cssSelector("tbody tr:first-child")
	            )
	    );

	    WebElement warehouseNameInListing = firstRow.findElement(
	            By.xpath("./td[2]//span[normalize-space()='" + editWarehouseName + "']")
	    );

	    String actualWarehouseName = warehouseNameInListing.getText().trim();

	    Assert.assertEquals(
	            "Created Warehouse Name is not matching in the listing",
	            editWarehouseName,
	            actualWarehouseName
	    );

	    System.out.println(
	            "Warehouse Name verified in listing: " + actualWarehouseName
	    );

	    // 2. Click Three Dots
	    WebElement firstThreeDots = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("(//i[contains(@class,'bi-three-dots-vertical')])[1]")
	            )
	    );

	    firstThreeDots.click();

	    Common.waitForElement(2);

	    // 4. Click Preview
	    WebElement preview = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
	            )
	    );

	    preview.click();

	    Common.waitForElement(2);

	    System.out.println("Warehouse Preview clicked.");

	    // 4. Verify Warehouse Name in Preview
	    WebElement previewWarehouseName = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Warehouse Name']"
	                                    + "/following-sibling::dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualPreviewWarehouseName =
	            previewWarehouseName.getText().trim();

	    Assert.assertEquals(
	            "Warehouse Name is not matching in Preview",
	            editWarehouseName,
	            actualPreviewWarehouseName
	    );

	    System.out.println(
	            "Preview Warehouse Name verified: " + actualPreviewWarehouseName
	    );

	    // 5. Verify Legal Location
	    WebElement previewLegalLocation = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Legal Location']"
	                                    + "/following-sibling::dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualLegalLocation =
	            previewLegalLocation.getText().trim();

	    Assert.assertEquals(
	            "Legal Location is not matching in Preview",
	            editLegalLocation,
	            actualLegalLocation
	    );

	    System.out.println(
	            "Preview Legal Location verified: " + actualLegalLocation
	    );

	    // 6. Verify Address
	    WebElement previewAddress = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Address']"
	                                    + "/following-sibling::dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualAddress = previewAddress.getText().trim();

	    Assert.assertTrue(
	            "House/Flat is not displayed correctly in Preview Address",
	            actualAddress.contains(editFlatNumber)
	    );

	    Assert.assertTrue(
	            "Road Area is not displayed correctly in Preview Address",
	            actualAddress.contains(editRoadArea)
	    );

	    Assert.assertTrue(
	            "PIN Code is not displayed correctly in Preview Address",
	            actualAddress.contains(pincode)
	    );

	    System.out.println(
	            "Preview Address verified: " + actualAddress
	    );

	    // 7. Verify Contact Name
	    WebElement previewContactName = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Contact Name']"
	                                    + "/following-sibling::dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualContactName =
	            previewContactName.getText().trim();

	    Assert.assertEquals(
	            "Contact Name is not matching in Preview",
	            editContactNameValue,
	            actualContactName
	    );

	    System.out.println(
	            "Preview Contact Name verified: " + actualContactName
	    );

	    // 8. Verify Email
	    WebElement previewEmail = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Email']"
	                                    + "/following-sibling::div//dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualEmail =
	            previewEmail.getText().trim();

	    Assert.assertEquals(
	            "Contact Email is not matching in Preview",
	            EeditContactEmailValue,
	            actualEmail
	    );

	    System.out.println(
	            "Preview Email verified: " + actualEmail
	    );

	    // 9. Verify Phone
	    WebElement previewPhone = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//dt[normalize-space()='Phone']"
	                                    + "/following-sibling::div//dd[contains(@class,'view_value')]"
	                    )
	            )
	    );

	    String actualPhone =
	            previewPhone.getText().trim();

	    Assert.assertEquals(
	            "Contact Phone is not matching in Preview",
	            editContactPhoneValue,
	            actualPhone
	    );

	    System.out.println(
	            "Preview Phone verified: " + actualPhone
	    );

	    System.out.println(
	            "All created Warehouse details verified successfully in Preview."
	    );
	}
	
	
	public void setWarehouseAsMainBranch() {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    Common.waitForElement(2);

	    // 1. Click first row three dots
	    WebElement firstRow = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.cssSelector("tbody tr:first-child")
	            )
	    );

	    // 3. Click the first three-dots menu
	    WebElement firstThreeDots = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("(//i[contains(@class,'bi-three-dots-vertical')])[1]")
	            )
	    );

	    firstThreeDots.click();

	    Common.waitForElement(2);

	    // 4. Click Preview
	    WebElement preview = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
	            )
	    );

	    preview.click();


	    Common.waitForElement(2);

	 // 4. Verify Set as Main Branch is initially "No"
	    WebElement mainBranchElement = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath("//dt[contains(normalize-space(), 'Set as Main')]/following-sibling::dd[contains(@class,'view_value')]")
	            )
	    );

	    String initialMainBranch = mainBranchElement.getText().trim();

	    Assert.assertEquals(
	            "Set as Main Branch should initially be No",
	            "No",
	            initialMainBranch
	    );

	    System.out.println("Initial Set as Main Branch status: " + initialMainBranch);

	   
	    Common.waitForElement(2);

	    // 5. Click first row three dots again
	    WebElement previewThreeDots = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("//button[@data-toggle='dropdown' and .//i[contains(@class,'bi-three-dots-vertical')]]")
	            )
	    );

	    previewThreeDots.click();

	    Common.waitForElement(2);

	    // 6. Click Set as Main Branch
	    WebElement setAsMainBranch = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.xpath("//a[contains(@class,'dropdown-item') and normalize-space()='Set as Main Branch']")
	            )
	    );

	    setAsMainBranch.click();

	    Common.waitForElement(2);

	    // 7. Click Yes, Confirm
	    WebElement confirmButton = wait.until(
	            ExpectedConditions.elementToBeClickable(
	                    By.id("setMainBranchConfirmBtn")
	            )
	    );

	    confirmButton.click();

	    Common.waitForElement(3);

	    System.out.println("Set as Main Branch confirmed.");

	    // 8. Verify Set as Main Branch is now "Yes"
	    WebElement updatedMainBranchElement = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath("//dt[contains(normalize-space(), 'Set as Main')]/following-sibling::dd[contains(@class,'view_value')]")
	            )
	    );

	    String updatedMainBranch = updatedMainBranchElement.getText().trim();

	    Assert.assertEquals(
	            "Set as Main Branch should be Yes after confirmation",
	            "Yes",
	            updatedMainBranch
	    );

	    System.out.println("Updated Set as Main Branch status: " + updatedMainBranch);

	    // 9. Navigate back to Warehouse listing
	    driver.navigate().back();
	    Common.waitForElement(2);

	    // 10. Verify first row, second column contains "Main"
	    WebElement firstRowSecondColumn = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.cssSelector("tbody tr:first-child td:nth-child(2)")
	            )
	    );

	    String warehouseColumnText = firstRowSecondColumn.getText().trim();

	    System.out.println("First row second column text: " + warehouseColumnText);

	    Assert.assertTrue(
	            "Main text is not displayed for the warehouse in the first row",
	            warehouseColumnText.contains("Main")
	    );
	    Common.waitForElement(3);
	    System.out.println("Main text is displayed successfully for the first-row warehouse.");
	}
	
	
	 
	//TC-01
	 public void validateNewCreatedWarehouse() {
		 
		 adminLogin();
		 
		 navigatetoPurchaseOrderPage();
		 
		 createWarehouse();
		 
		 verifyCreatedWarehouse();
		 
	 }
		
//TC-02	 
	 public void validateEditWareHouse() {	 

		 adminLogin();
		 
		 navigatetoPurchaseOrderPage();
		 
		 editExistingWarehouse();
		 
		 verifyEditedWarehouse();
	 }
//TC-03	 
	 public void validatesetAsMainFunctionalty() {
 
		 adminLogin();
		 
		 navigatetoPurchaseOrderPage();
		 
		 setWarehouseAsMainBranch();
		 
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
