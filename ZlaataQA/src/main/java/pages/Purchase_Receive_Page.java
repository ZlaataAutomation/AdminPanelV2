package pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import objectRepo.Purchase_Receive_ObjRepo;
import utils.Common;

public class Purchase_Receive_Page extends Purchase_Receive_ObjRepo {
	
	public Purchase_Receive_Page(WebDriver driver) 
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
	        wait.until(ExpectedConditions.visibilityOf(inventory));
	        new Actions(driver).moveToElement(inventory).perform();

	        wait.until(ExpectedConditions.visibilityOf(purchaseOrder));
	        click(purchaseOrder);
	    }
	 
	 public void navigatetoPurchaseReceivePage() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        wait.until(ExpectedConditions.visibilityOf(inventory));
	        new Actions(driver).moveToElement(inventory).perform();

	        wait.until(ExpectedConditions.visibilityOf(purchaseReceive));
	        click(purchaseReceive);
	    }
	
	 public void verifyNewPurchaseReceive() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		 Common.waitForElement(2);
		    // 1. Select Status -> Open
		    WebElement statusDropdown = wait.until(
		            ExpectedConditions.elementToBeClickable(By.id("filter_status"))
		    );

		    Select statusSelect = new Select(statusDropdown);
		    statusSelect.selectByVisibleText("Open");

		    Common.waitForElement(2);

		    // 2. Select Purchase Receive Status -> Not Received
		    WebElement purchaseReceiveStatusDropdown = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("filter_purchase_receive_status")
		            )
		    );

		    Select purchaseReceiveSelect = new Select(purchaseReceiveStatusDropdown);
		    purchaseReceiveSelect.selectByVisibleText("Not Received");

		    Common.waitForElement(2);

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

		    // 5. Verify "New Purchase Receive" button is displayed
		    WebElement newPurchaseReceiveButton = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//button[contains(@class,'add_row_btn') and contains(normalize-space(.),'New Purchase Receive')]")
		            )
		    );

		    Assert.assertTrue(
		            "New Purchase Receive button is not displayed",
		            newPurchaseReceiveButton.isDisplayed()
		    );

		    System.out.println("New Purchase Receive button is displayed successfully.");


		    // 6. Click New Purchase Receive
		    newPurchaseReceiveButton.click();

		    Common.waitForElement(1);

		    // 7. Click Yes, Confirm in popup
		    WebElement confirmButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("btn_confirm_move_to_receive")
		            )
		    );

		    Assert.assertTrue(
		            "Yes, Confirm button is not displayed in the popup",
		            confirmButton.isDisplayed()
		    );

		    confirmButton.click();
		    System.out.println("Successfully clicked Yes, Confirm.");
		    Common.waitForElement(2);
		 // 8. Verify redirected to Purchase Receive section
		    WebElement createPurchaseReceiveHeading = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//h4[contains(@class,'page_heading') and normalize-space()='Create Purchase Receive']")
		            )
		    );

		    Assert.assertTrue(
		            "Failed to redirect to Create Purchase Receive section",
		            createPurchaseReceiveHeading.isDisplayed()
		    );

		    Assert.assertEquals(
		            "Purchase Receive page heading is incorrect",
		            "Create Purchase Receive",
		            createPurchaseReceiveHeading.getText().trim()
		    );

		    System.out.println("Successfully redirected to Create Purchase Receive section.");
		    
		}	
	
	 String supplierName;
	 String poCode;
	 String note;
	 public void verifyPurchaseReceiveStatus() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		    // 1. Get Supplier Name
		    WebElement supplierElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("#supplier_select_wrapper .select-box p[data-id]")
		            )
		    );

		     supplierName = supplierElement.getText().trim();

		    System.out.println("Supplier Name: " + supplierName);

		    Assert.assertFalse(
		            "Supplier name is not displayed",
		            supplierName.isEmpty()
		    );


		    // 2. Get Purchase Order Code
		    WebElement poElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("#po_select_wrapper .select-box p[data-id]")
		            )
		    );

		     poCode = poElement.getText().trim();

		    System.out.println("Purchase Order: " + poCode);

		    Assert.assertFalse(
		            "Purchase Order code is not displayed",
		            poCode.isEmpty()
		    );


		 // 3. Enter today's date using Flatpickr

		    WebElement receivedDate = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.id("received_date")
		            )
		    );

		    String todayDate = LocalDate.now()
		            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		    JavascriptExecutor js = (JavascriptExecutor) driver;

		    // Set the date through Flatpickr API
		    js.executeScript(
		            "var fp = arguments[0]._flatpickr;" +
		            "fp.setDate(arguments[1], true, 'd-m-Y');",
		            receivedDate,
		            todayDate
		    );

		    System.out.println("Received Date: " + todayDate);

		    Common.waitForElement(1);

		    // Close Flatpickr calendar
		    js.executeScript(
		            "var fp = arguments[0]._flatpickr;" +
		            "if (fp) { fp.close(); }",
		            receivedDate
		    );

		    Common.waitForElement(1);

		    System.out.println("Received date entered and calendar closed.");
		    
		    Common.waitForElement(2);
		    // 4. Count how many PR item rows are displayed
		    List<WebElement> prRows = wait.until(
		            ExpectedConditions.visibilityOfAllElementsLocatedBy(
		                    By.cssSelector("#pr_items_tbody tr")
		            )
		    );

		    int rowCount = prRows.size();

		    System.out.println("Total PR Item Rows: " + rowCount);

		    Assert.assertTrue(
		            "No PR item rows are displayed",
		            rowCount > 0
		    );


		    // 5. Enter quantity = 1 for every row
		    List<WebElement> quantityFields = driver.findElements(
		            By.cssSelector("#pr_items_tbody tr input.input-group-field")
		    );

		    Assert.assertEquals(
		            "PR row count and quantity field count are not matching",
		            rowCount,
		            quantityFields.size()
		    );

		    for (int i = 0; i < quantityFields.size(); i++) {

		        WebElement quantityField = quantityFields.get(i);

		        wait.until(ExpectedConditions.visibilityOf(quantityField));

		        quantityField.clear();
		        quantityField.sendKeys("1");

		        System.out.println(
		                "Row " + (i + 1) + " quantity entered: 1"
		        );
		    }


		    // 6. Enter Notes
		    WebElement notesField = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.name("notes")
		            )
		    );

		     note = "Purchase Receive created successfully";

		    notesField.clear();
		    notesField.sendKeys(note);

		    System.out.println("Notes entered: " + note);


		    // 7. Click the status dropdown/text - Mark as In-Transit
		    WebElement statusText = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//span[contains(@class,'js-selected-text') and normalize-space()='Mark as In-Transit']")
		            )
		    );

		    statusText.click();

		    Common.waitForElement(1);


		    // 8. Click Mark as In-Transit from dropdown
		    WebElement markAsTransit = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("btn_save_transit")
		            )
		    );

		    markAsTransit.click();

		    Common.waitForElement(1);


		    // 9. Verify confirmation popup
		    WebElement confirmTransitButton = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.id("confirm_transit")
		            )
		    );

		    Assert.assertTrue(
		            "Mark as In-Transit confirmation popup is not displayed",
		            confirmTransitButton.isDisplayed()
		    );

		    System.out.println(
		            "Mark as In-Transit confirmation popup is displayed."
		    );


		    // 10. Click Yes, Confirm
		    confirmTransitButton.click();

		    System.out.println(
		            "Successfully clicked Yes, Confirm for Mark as In-Transit."
		    		
		    );
		    Common.waitForElement(2);
			 // 8. Verify redirected to Purchase Receive section
			    WebElement createPurchaseReceiveHeading = wait.until(
			            ExpectedConditions.visibilityOfElementLocated(
			                    By.xpath("//h1[contains(@class,'page_heading') and normalize-space()='purchase receives']")
			            )
			    );

			    

			    Assert.assertEquals(
			            "Purchase Receive page heading is incorrect",
			            "Purchase Receives",
			            createPurchaseReceiveHeading.getText().trim()
			    );

			    System.out.println("Successfully  Created Purchase Receive.");
		}
	
	
	 public void verifyPurchaseReceiveInListing() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		    // Expected values captured from Create Purchase Receive page
		    String expectedSupplier = supplierName;
		    String expectedPoCode = poCode;
		    String expectedStatus = "In Transit";

		    System.out.println("Expected Supplier: " + expectedSupplier);
		    System.out.println("Expected PO: " + expectedPoCode);
		    System.out.println("Expected Status: " + expectedStatus);

		    // Wait for first row in Purchase Receive listing
		    WebElement firstRow = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("tbody tr:first-child")
		            )
		    );

		    // 1. Verify Supplier Name
		    WebElement supplierElement = firstRow.findElement(
		            By.xpath(".//td[2]//span[contains(@class,'d-inline-flex')]")
		    );

		    String actualSupplier = supplierElement.getText().trim();

		    System.out.println("Actual Supplier: " + actualSupplier);

		    Assert.assertEquals(
		            "Supplier name is not matching",
		            expectedSupplier,
		            actualSupplier
		    );


		    // 2. Verify PO Code
		    WebElement poElement = firstRow.findElement(
		            By.xpath(".//td[3]//span")
		    );

		    String actualPoCode = poElement.getText().trim();

		    System.out.println("Actual PO: " + actualPoCode);

		    Assert.assertEquals(
		            "Purchase Order code is not matching",
		            expectedPoCode,
		            actualPoCode
		    );


		    // 3. Verify Status -> In Transit
		    WebElement statusElement = firstRow.findElement(
		            By.xpath(".//td[6]//p[normalize-space()='In Transit']")
		    );

		    String actualStatus = statusElement.getText().trim();

		    System.out.println("Actual Status: " + actualStatus);

		    Assert.assertEquals(
		            "Purchase Receive status is not In Transit",
		            expectedStatus,
		            actualStatus
		    );


		    System.out.println(
		            "Purchase Receive listing validation passed successfully."
		    );
		}
	String prCode;
	 public void verifyPurchaseReceivePreview() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    // 3. Click the first three-dots menu
		    WebElement firstThreeDots = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("(//i[contains(@class,'bi-three-dots-vertical')])[1]")
		            )
		    );

		    firstThreeDots.click();

		    Common.waitForElement(1);

		    // 4. Click Preview
		    WebElement preview = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
		            )
		    );

		    preview.click();

		    Common.waitForElement(2);

		    // 1. Verify Supplier Name
		    WebElement supplierElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//p[contains(@class,'modal_para_dark') and normalize-space()='"
		                            + supplierName + "']")
		            )
		    );

		    String actualSupplier = supplierElement.getText().trim();

		    System.out.println("Expected Supplier: " + supplierName);
		    System.out.println("Actual Supplier: " + actualSupplier);

		    Assert.assertEquals(
		            "Supplier name is not matching in PR Preview",
		            supplierName,
		            actualSupplier
		    );


		    // 2. Verify PO Code in Purchase Receive details
		    WebElement poElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//p[contains(@class,'modal_para_dark') and normalize-space()='"
		                            + poCode + "']")
		            )
		    );

		    String actualPoCode = poElement.getText().trim();

		    System.out.println("Expected PO: " + poCode);
		    System.out.println("Actual PO: " + actualPoCode);

		    Assert.assertEquals(
		            "Purchase Order code is not matching in PR Preview",
		            poCode,
		            actualPoCode
		    );


		    // 3. Verify PO Code in the Purchase Order table/link
		    WebElement poTableElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//a[contains(@class,'purchase_para') and normalize-space()='"
		                            + poCode + "']")
		            )
		    );

		    String actualPoTable = poTableElement.getText().trim();

		    System.out.println("Expected PO in table: " + poCode);
		    System.out.println("Actual PO in table: " + actualPoTable);

		    Assert.assertEquals(
		            "Purchase Order code is not matching in PR table",
		            poCode,
		            actualPoTable
		    );


		    // 4. Verify Status -> In Transit
		    WebElement statusElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//p[contains(@class,'m-0') and normalize-space()='In Transit']")
		            )
		    );

		    String actualStatus = statusElement.getText().trim();

		    System.out.println("Expected Status: In Transit");
		    System.out.println("Actual Status: " + actualStatus);

		    Assert.assertEquals(
		            "Purchase Receive status is not In Transit",
		            "In Transit",
		            actualStatus
		    );


		    // 5. Copy/Get PR Code
		    WebElement prCodeElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("p.modal_para_dark.order_id")
		            )
		    );

		    prCode = prCodeElement.getText().trim();

		    System.out.println("Purchase Receive Code: " + prCode);

		    Assert.assertFalse(
		            "Purchase Receive Code is not displayed",
		            prCode.isEmpty()
		    );

		    System.out.println(
		            "Purchase Receive Preview validation completed successfully."
		    );
		}
	
	
	 public void edittheIntransittoReceived() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		 Common.waitForElement(2);
		// Select Status -> In Transit
		 WebElement statusDropdown = wait.until(
		         ExpectedConditions.elementToBeClickable(
		                 By.id("filter_status")
		         )
		 );

		 Select statusSelect = new Select(statusDropdown);
		 statusSelect.selectByVisibleText("In Transit");

		 Common.waitForElement(2);

		 System.out.println("Status selected: In Transit");

		    Common.waitForElement(2);

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

		    System.out.println("Purchase Receive Edit page opened.");


		    // 6. Click Mark as In-Transit dropdown
		    WebElement markTransitDropdown = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//span[contains(@class,'js-selected-text') and normalize-space()='Mark as In-Transit']")
		            )
		    );

		    markTransitDropdown.click();

		    Common.waitForElement(1);


		    // 7. Click Save as Received
		    WebElement saveAsReceived = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("btn_save_received")
		            )
		    );

		    saveAsReceived.click();

		    Common.waitForElement(1);


		    // 8. Verify error message
		    WebElement errorMessage = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.id("err_pr_items_tbody")
		            )
		    );

		    String actualErrorMessage = errorMessage.getText().trim();

		    String expectedErrorMessage =
		            "All quantities must be fully received before saving as Received Use Mrk as In-Transit.";

		    System.out.println("Expected Error: " + expectedErrorMessage);
		    System.out.println("Actual Error: " + actualErrorMessage);

		    Assert.assertEquals(
		            "Incorrect error message displayed while saving as Received",
		            expectedErrorMessage,
		            actualErrorMessage
		    );

		    System.out.println(
		            "Correct error message displayed successfully: "
		                    + actualErrorMessage
		    );
		}
	
	 public void updatePurchaseReceiveToReceived() {

		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		    // 1. Count how many PR item rows are displayed
		    List<WebElement> prRows = wait.until(
		            ExpectedConditions.visibilityOfAllElementsLocatedBy(
		                    By.cssSelector("#pr_items_tbody tr")
		            )
		    );

		    int rowCount = prRows.size();

		    System.out.println("Total PR Item Rows: " + rowCount);

		    Assert.assertTrue(
		            "No PR item rows are displayed",
		            rowCount > 0
		    );


		    // 2. Enter quantity = 10000 for every row
		    List<WebElement> quantityFields = driver.findElements(
		            By.cssSelector("#pr_items_tbody tr input.input-group-field")
		    );

		    Assert.assertEquals(
		            "PR row count and quantity field count are not matching",
		            rowCount,
		            quantityFields.size()
		    );

		    for (int i = 0; i < quantityFields.size(); i++) {

		        WebElement quantityField = quantityFields.get(i);

		        wait.until(ExpectedConditions.visibilityOf(quantityField));

		        quantityField.clear();
		        quantityField.sendKeys("10000");

		        System.out.println(
		                "Row " + (i + 1) + " quantity entered: 10000"
		        );
		    }


		    // 3. Click Mark as In-Transit dropdown
//		    WebElement statusDropdown = wait.until(
//		            ExpectedConditions.elementToBeClickable(
//		                    By.xpath("//span[contains(@class,'js-selected-text') and normalize-space()='Mark as In-Transit']")
//		            )
//		    );
//
//		    statusDropdown.click();

		    Common.waitForElement(1);


		    // 4. Click Save as Received
		    WebElement saveAsReceived = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("btn_save_received")
		            )
		    );

		    saveAsReceived.click();

		    Common.waitForElement(1);

		    System.out.println("Save as Received clicked.");


		    // 5. Click Yes, Confirm
		    WebElement confirmReceived = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("confirm_received")
		            )
		    );

		    Assert.assertTrue(
		            "Yes, Confirm button is not displayed",
		            confirmReceived.isDisplayed()
		    );

		    confirmReceived.click();

		    Common.waitForElement(3);

		    System.out.println("Purchase Receive confirmed successfully.");


		    // 6. Verify redirected to Purchase Receives listing page
		    WebElement purchaseReceivesHeading = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//h1[contains(@class,'page_heading') and normalize-space()='purchase receives']")
		            )
		    );

		    Assert.assertTrue(
		            "Purchase Receives listing page is not displayed",
		            purchaseReceivesHeading.isDisplayed()
		    );

		    String actualHeading = purchaseReceivesHeading.getText().trim();

		    Assert.assertTrue(
		            "Purchase Receives page heading is incorrect",
		            actualHeading.equalsIgnoreCase("purchase receives")
		    );

		    System.out.println(
		            "Successfully saved Purchase Receive as Received."
		    );
		    System.out.println(
		            "Purchase Receives listing page is displayed successfully."
		    );
		}
	 
	 
	 public void verifyPurchaseReceiveReceivedStatus() {

		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		    // 1. Get the first row
		    WebElement firstRow = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("tbody tr:first-child")
		            )
		    );

		    // 2. Verify 6th column status = Received
		    WebElement statusElement = firstRow.findElement(
		            By.xpath(".//td[6]//p[normalize-space()='Received']")
		    );

		    String listingStatus = statusElement.getText().trim();

		    System.out.println("Purchase Receive Listing Status: " + listingStatus);

		    Assert.assertEquals(
		            "Purchase Receive status is not Received in listing",
		            "Received",
		            listingStatus
		    );

		    System.out.println(
		            "First Purchase Receive row status verified successfully: Received"
		    );

		    Common.waitForElement(2);
		    // 3. Click three-dots in the first row
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


		    // 5. Verify Received status in Preview
		    WebElement previewStatus = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath(
		                            "//p[contains(@class,'m-0') and normalize-space()='Received']"
		                    )
		            )
		    );

		    String previewStatusText = previewStatus.getText().trim();

		    System.out.println(
		            "Purchase Receive Preview Status: " + previewStatusText
		    );

		    Assert.assertEquals(
		            "Purchase Receive status is not Received in Preview",
		            "Received",
		            previewStatusText
		    );

		    System.out.println(
		            "Purchase Receive Preview status verified successfully: Received"
		    );
		}
	
	
	 public void verfyConvertBillOption() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		 Common.waitForElement(2);
		// Select Status -> In Transit
		 WebElement statusDropdown = wait.until(
		         ExpectedConditions.elementToBeClickable(
		                 By.id("filter_status")
		         )
		 );

		 Select statusSelect = new Select(statusDropdown);
		 statusSelect.selectByVisibleText("Received");

		 Common.waitForElement(2);

		 System.out.println("Status selected: Received");

		   
		 // Select Bill Status -> Not Billed
		    WebElement billStatusDropdown = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("filter_bill_status")
		            )
		    );

		    Select billStatusSelect = new Select(billStatusDropdown);
		    billStatusSelect.selectByVisibleText("Not Billed");

		    Common.waitForElement(2);

		    System.out.println("Bill Status selected: Not Billed");
		    
		    

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
		 // 4. Click three-dots menu on Purchase Receive Preview page
		    WebElement previewThreeDots = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//button[@data-toggle='dropdown' and .//i[contains(@class,'bi-three-dots-vertical')]]")
		            )
		    );

		    previewThreeDots.click();
		    Common.waitForElement(1);


		    // 6. Verify Convert to Bill option is displayed
		    WebElement convertToBill = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath(
		                            "//a[contains(@class,'dropdown-item') "
		                                    + "and contains(@onclick,'convertToBill()') "
		                                    + "and normalize-space()='Convert to Bill']"
		                    )
		            )
		    );

		    Assert.assertTrue(
		            "Convert to Bill option is not displayed",
		            convertToBill.isDisplayed()
		    );

		    System.out.println("Convert to Bill option is displayed.");


		    // 7. Click Convert to Bill
		    wait.until(
		            ExpectedConditions.elementToBeClickable(convertToBill)
		    ).click();

		    Common.waitForElement(2);


		    // 8. Click Yes, Convert
		    WebElement yesConvertButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("convert_bill_btn")
		            )
		    );

		    Assert.assertTrue(
		            "Yes, Convert button is not displayed",
		            yesConvertButton.isDisplayed()
		    );

		    yesConvertButton.click();

		    Common.waitForElement(3);

		    System.out.println("Yes, Convert clicked successfully.");


		 // 9. Verify redirect to Create Purchase Bill page
		    WebElement createPurchaseBillHeading = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath(
		                            "//h4[contains(@class,'page_heading') "
		                                    + "and normalize-space()='Create Purchase Bill']"
		                    )
		            )
		    );

		    Assert.assertTrue(
		            "Create Purchase Bill page is not displayed after converting Purchase Receive to Bill",
		            createPurchaseBillHeading.isDisplayed()
		    );

		    String actualHeading = createPurchaseBillHeading.getText().trim();

		    Assert.assertEquals(
		            "Create Purchase Bill page heading is incorrect",
		            "Create Purchase Bill",
		            actualHeading
		    );

		    System.out.println(
		            "Successfully redirected to Create Purchase Bill page."
		    );
	
	 }
	
	 public void verifyPurchaseReceiveErrorMessages() {

		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    Common.waitForElement(3);
		    // =========================================================
		    // 1. Click Mark as In-Transit
		    // =========================================================

		    WebElement statusDropdown = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//span[contains(@class,'js-selected-text') and normalize-space()='Mark as In-Transit']")
		            )
		    );

		    statusDropdown.click();

		    Common.waitForElement(2);


		    // 2. Click Mark as In-Transit from dropdown
		    WebElement markAsTransit = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("btn_save_transit")
		            )
		    );

		    markAsTransit.click();

		    Common.waitForElement(3);


		    // =========================================================
		    // 3. Verify Received Date error message
		    // =========================================================

		    WebElement receivedDateError = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.id("err_received_date")
		            )
		    );

		    String actualDateError = receivedDateError.getText().trim();

		    String expectedDateError =
		            "Please select a received date.";

		    System.out.println("Received Date Error: " + actualDateError);

		    Assert.assertEquals(
		            "Received date error message is incorrect",
		            expectedDateError,
		            actualDateError
		    );


		    // =========================================================
		    // 4. Verify Quantity error message
		    // =========================================================

		    WebElement quantityError = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.id("err_pr_items_tbody")
		            )
		    );

		    String actualQuantityError = quantityError.getText().trim();

		    String expectedQuantityError =
		            "Please enter at least one quantity to receive.";

		    System.out.println("Quantity Error: " + actualQuantityError);

		    Assert.assertEquals(
		            "Quantity error message is incorrect",
		            expectedQuantityError,
		            actualQuantityError
		    );


		    System.out.println(
		            "Both validation error messages are displayed correctly."
		    );


		    // =========================================================
		    // 5. Enter today's received date
		    // =========================================================


		    WebElement receivedDate = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.id("received_date")
		            )
		    );

		    String todayDate = LocalDate.now()
		            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		    JavascriptExecutor js = (JavascriptExecutor) driver;

		    // Set the date through Flatpickr API
		    js.executeScript(
		            "var fp = arguments[0]._flatpickr;" +
		            "fp.setDate(arguments[1], true, 'd-m-Y');",
		            receivedDate,
		            todayDate
		    );

		    System.out.println("Received Date: " + todayDate);

		    Common.waitForElement(1);

		    // Close Flatpickr calendar
		    js.executeScript(
		            "var fp = arguments[0]._flatpickr;" +
		            "if (fp) { fp.close(); }",
		            receivedDate
		    );

		    Common.waitForElement(1);

		    System.out.println("Received date entered and calendar closed.");

		    Common.waitForElement(1);


		    // =========================================================
		    // 6. Enter quantity for every PR item
		    // =========================================================

		    List<WebElement> prRows = wait.until(
		            ExpectedConditions.visibilityOfAllElementsLocatedBy(
		                    By.cssSelector("#pr_items_tbody tr")
		            )
		    );

		    int rowCount = prRows.size();

		    Assert.assertTrue(
		            "No PR item rows are displayed",
		            rowCount > 0
		    );

		    List<WebElement> quantityFields = driver.findElements(
		            By.cssSelector(
		                    "#pr_items_tbody tr input.input-group-field"
		            )
		    );

		    Assert.assertEquals(
		            "PR row count and quantity field count are not matching",
		            rowCount,
		            quantityFields.size()
		    );

		    for (int i = 0; i < quantityFields.size(); i++) {

		        WebElement quantityField = quantityFields.get(i);

		        wait.until(
		                ExpectedConditions.visibilityOf(quantityField)
		        );

		        quantityField.clear();
		        quantityField.sendKeys("10000");

		        System.out.println(
		                "Row " + (i + 1) + " quantity entered: 10000"
		        );
		    }


		    // =========================================================
		    // 7. Click Mark as In-Transit again
		    // =========================================================

//		    statusDropdown = wait.until(
//		            ExpectedConditions.elementToBeClickable(
//		                    By.xpath("//span[contains(@class,'js-selected-text') and normalize-space()='Mark as In-Transit']")
//		            )
//		    );
//
//		    statusDropdown.click();

		    Common.waitForElement(2);


		    // 8. Click Mark as In-Transit
		    markAsTransit = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("btn_save_transit")
		            )
		    );

		    markAsTransit.click();

		    Common.waitForElement(3);


		    // =========================================================
		    // 9. Verify Fully Received error message
		    // =========================================================

		    quantityError = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.id("err_pr_items_tbody")
		            )
		    );

		    String actualFullyReceivedError =
		            quantityError.getText().trim();

		    String expectedFullyReceivedError =
		            "All quantities are fully received — please save as Received instead.";

		    System.out.println(
		            "Fully Received Error: " + actualFullyReceivedError
		    );

		    Assert.assertEquals(
		            "Fully received validation error message is incorrect",
		            expectedFullyReceivedError,
		            actualFullyReceivedError
		    );

		    System.out.println(
		            "Fully received validation message verified successfully."
		    );
		}
	
	
	//TC-01
	 public void validateInTransitStatusPurchaseReceive() {
		 
		 adminLogin();
		 
		 navigatetoPurchaseOrderPage();
		 
		 verifyNewPurchaseReceive();
		 
		 verifyPurchaseReceiveStatus();
		 
		 verifyPurchaseReceiveInListing();
		 
		 verifyPurchaseReceivePreview();
		 
	 }
	
//TC-02	
	 public void validateReceivedStatus() {
		 
		 adminLogin();
		 
		 navigatetoPurchaseReceivePage();
		 
		 edittheIntransittoReceived();
		 
		 updatePurchaseReceiveToReceived();
		 
		 verifyPurchaseReceiveReceivedStatus();
	 }
	//TC-03
	 
	 public void validateConvertBill() {
		 
		 adminLogin();
		 
		 navigatetoPurchaseReceivePage();
		 
		 verfyConvertBillOption();
		 
	 }
	
//TC-04
	 public void varifyValidationErrorMessage() {
		 
		 adminLogin();
		 
		 navigatetoPurchaseOrderPage();
		 
		 verifyNewPurchaseReceive();		 
		 
		 verifyPurchaseReceiveErrorMessages();
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
