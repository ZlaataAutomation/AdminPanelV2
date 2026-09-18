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

import objectRepo.ManufactureReceive_ObjRepo;
import utils.Common;

public class ManufactureReceive_Page extends ManufactureReceive_ObjRepo {
	
	public ManufactureReceive_Page(WebDriver driver) 
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(this.driver, this);
	}
	
	public void adminLogin() {
	
	AdminLogin_Page login= new AdminLogin_Page(driver);
	login.adminLoginApp();
	
	}
	
	
	 public void navigatetoManufactureOrderPage() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        wait.until(ExpectedConditions.visibilityOf(inventory));
	        new Actions(driver).moveToElement(inventory).perform();

	        wait.until(ExpectedConditions.visibilityOf(manufactureOrder));
	        click(manufactureOrder);
	    }
	 
	 public void navigatetoManufactureReceivePage() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        wait.until(ExpectedConditions.visibilityOf(inventory));
	        new Actions(driver).moveToElement(inventory).perform();

	        wait.until(ExpectedConditions.visibilityOf(manufactureReceive));
	        click(manufactureReceive);
	    }
	 
	 String moId ;
	 public void openManufactureReceiveAfterMarkAsIssued() {

		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    Common.waitForElement(2);
		    // 1. Select Status -> Open
		    WebElement statusDropdown = wait.until(
		            ExpectedConditions.elementToBeClickable(By.id("filter_status"))
		    );

		    Select statusSelect = new Select(statusDropdown);
		    statusSelect.selectByVisibleText("Open");

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
		    
		   

		    // 1. Click "Mark as Issued"
		    WebElement markAsIssuedButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//button[@data-target='#markAsIssuedModal' "
		                            + "and normalize-space()='Mark as Issued']")
		            )
		    );

		    markAsIssuedButton.click();

		    Common.waitForElement(2);

		    // 2. Click "Yes, Issued" confirmation button
		    WebElement yesIssuedButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("btn_confirm_mark_issued")
		            )
		    );

		    yesIssuedButton.click();

		    Common.waitForElement(3);

		    System.out.println("Manufacture Order marked as Issued successfully.");
		    
		    WebElement moIdElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("p.card_coupon_code.modal_para_dark")
		            )
		    );

		     moId = moIdElement.getText().trim();

		    System.out.println("MO ID: " + moId);
		    

		    // 3. Click "Receive"
		    WebElement receiveButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//a[contains(@href,'/admin/manufacture-receive/create') "
		                            + "and normalize-space()='Receive']")
		            )
		    );

		    receiveButton.click();

		    Common.waitForElement(2);

		    System.out.println("Receive button clicked.");

		    // 4. Verify Create Manufacture Receive heading
		    WebElement createManufactureReceiveHeading = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//h4[contains(@class,'page_heading') "
		                            + "and normalize-space()='Create Manufacture Receive']")
		            )
		    );

		    Assert.assertTrue(
		            "Create Manufacture Receive page is not displayed",
		            createManufactureReceiveHeading.isDisplayed()
		    );

		    String actualHeading = createManufactureReceiveHeading.getText().trim();

		    Assert.assertEquals(
		            "Create Manufacture Receive heading is incorrect",
		            "Create Manufacture Receive",
		            actualHeading
		    );

		    System.out.println("Create Manufacture Receive page is displayed successfully.");
		    
		    
		    WebElement receiveMoIdElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("div.select-box")
		            )
		    );

		    String receiveMoId = receiveMoIdElement.getText().trim();

		    System.out.println("Copied MO ID: " + moId);
		    System.out.println("Receive Page MO ID: " + receiveMoId);

		    Assert.assertEquals(
		            "MO ID is not matching between Manufacture Order and Manufacture Receive",
		            moId,
		            receiveMoId
		    );

		    System.out.println("MO ID matched successfully: " + moId);
		}
	 String vendorName;
	 String note;
	 public void enterReceivedQuantityAndSave() {

		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    Common.waitForElement(2);

		    // 1. Get selected Vendor Name
		    WebElement vendorDropdown = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.id("vendorSelect")
		            )
		    );

		    Select vendorSelect = new Select(vendorDropdown);

		    WebElement selectedVendor = vendorSelect.getFirstSelectedOption();

		    vendorName = selectedVendor.getText().trim();

		    Assert.assertFalse(
		            "Vendor is not selected",
		            vendorName.isEmpty()
		    );

		    System.out.println("Selected Vendor Name: " + vendorName);
		    
		    
		 // Enter today's date using Flatpickr
		    WebElement visibleDateInput = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("div.date_input_wrapper input.form-control.input")
		            )
		    );

		    WebElement hiddenDateInput = wait.until(
		            ExpectedConditions.presenceOfElementLocated(
		                    By.id("receivedDateInput")
		            )
		    );

		    String todayDate = LocalDate.now()
		            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		    JavascriptExecutor js = (JavascriptExecutor) driver;

		    // Set today's date in Flatpickr and hidden input
		    js.executeScript(
		            "var visibleInput = arguments[0];" +
		            "var hiddenInput = arguments[1];" +
		            "var dateValue = arguments[2];" +

		            "if (visibleInput._flatpickr) {" +
		            "    visibleInput._flatpickr.setDate(dateValue, true, 'Y-m-d');" +
		            "} else if (hiddenInput._flatpickr) {" +
		            "    hiddenInput._flatpickr.setDate(dateValue, true, 'Y-m-d');" +
		            "}" +

		            "hiddenInput.value = dateValue;" +

		            "visibleInput.dispatchEvent(new Event('change', { bubbles: true }));" +
		            "hiddenInput.dispatchEvent(new Event('change', { bubbles: true }));" +
		            "hiddenInput.dispatchEvent(new Event('input', { bubbles: true }));",
		            
		            visibleDateInput,
		            hiddenDateInput,
		            todayDate
		    );

		    Common.waitForElement(1);

		    // Verify hidden input
		    String actualDate = hiddenDateInput.getAttribute("value");

		    Assert.assertEquals(
		            "Received date is not set correctly",
		            todayDate,
		            actualDate
		    );

		    System.out.println("Expected Received Date: " + todayDate);
		    System.out.println("Actual Received Date: " + actualDate);
		    System.out.println("Today's date entered successfully.");
		    
		    WebElement receiveQuantity = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//p[contains(@class,'fs-6') and normalize-space()='Receive quantity']")
		            )
		    );

		    receiveQuantity.click();

		    Common.waitForElement(1);

		    System.out.println("Receive quantity section clicked.");
		    

		    // 2. Find all received quantity input boxes
		    List<WebElement> quantityFields = wait.until(
		            ExpectedConditions.visibilityOfAllElementsLocatedBy(
		                    By.cssSelector("input.item-size-input")
		            )
		    );

		    int quantityCount = quantityFields.size();

		    Assert.assertTrue(
		            "No received quantity fields are displayed",
		            quantityCount > 0
		    );

		    System.out.println("Total quantity fields found: " + quantityCount);

		    // 3. Enter 1 in every quantity field
		    for (int i = 0; i < quantityFields.size(); i++) {

		        WebElement quantityField = quantityFields.get(i);

		        wait.until(ExpectedConditions.visibilityOf(quantityField));

		        quantityField.clear();
		        quantityField.sendKeys("1");

		        System.out.println(
		                "Quantity entered as 1 in row " + (i + 1)
		        );
		    }

		    // 4. Verify all quantity fields contain 1
		    for (int i = 0; i < quantityFields.size(); i++) {

		        String enteredQuantity = quantityFields.get(i)
		                .getAttribute("value")
		                .trim();

		        Assert.assertEquals(
		                "Received quantity is not 1 in row " + (i + 1),
		                "1",
		                enteredQuantity
		        );
		    }

		    System.out.println("Received quantity 1 entered for all rows.");
		    
		    // 6. Enter Notes
		    WebElement notesField = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.name("notes")
		            )
		    );

		     note = "Manufacture Receive created successfully";

		    notesField.clear();
		    notesField.sendKeys(note);

		    System.out.println("Notes entered: " + note);
		    Common.waitForElement(2);

		 // 5. Click Save
		    WebElement saveButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("saveBtn")
		            )
		    );

		    saveButton.click();

		    Common.waitForElement(2);

		    // 6. Click "Yes, Save" confirmation
		    WebElement confirmSaveButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("confirmSave")
		            )
		    );

		    confirmSaveButton.click();

		    Common.waitForElement(3);

		    System.out.println("Manufacture Receive saved successfully.");
		}
	 public void validateManufactureReceiveListing() {

		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		    // 1. Get first row
		    WebElement firstRow = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("tbody tr:first-child")
		            )
		    );

		    // 2. Verify Vendor Name - 2nd column
		    WebElement vendorElement = firstRow.findElement(
		            By.xpath("./td[2]//span")
		    );

		    String actualVendorName = vendorElement.getText().trim();

		    System.out.println("Expected Vendor Name: " + vendorName);
		    System.out.println("Actual Vendor Name: " + actualVendorName);

		    Assert.assertEquals(
		            "Vendor Name is not matching",
		            vendorName,
		            actualVendorName
		    );

		    // 3. Verify MO ID - 3rd column
		    WebElement moElement = firstRow.findElement(
		            By.xpath("./td[3]//span")
		    );

		    String actualMoId = moElement.getText().trim();

		    System.out.println("Expected MO ID: " + moId);
		    System.out.println("Actual MO ID: " + actualMoId);

		    Assert.assertEquals(
		            "Manufacture Order ID is not matching",
		            moId,
		            actualMoId
		    );

		    // 4. Verify Received Date - 4th column
		    WebElement receivedDateElement = firstRow.findElement(
		            By.xpath("./td[4]//span")
		    );

		    String actualReceivedDate = receivedDateElement.getText().trim();

		    String expectedReceivedDate = LocalDate.now()
		            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		    System.out.println("Expected Received Date: " + expectedReceivedDate);
		    System.out.println("Actual Received Date: " + actualReceivedDate);

		    Assert.assertEquals(
		            "Received Date is not matching today's date",
		            expectedReceivedDate,
		            actualReceivedDate
		    );

		    // 5. Verify Status - 6th column
		    WebElement statusElement = firstRow.findElement(
		            By.xpath("./td[6]//p")
		    );

		    String actualStatus = statusElement.getText().trim();

		    System.out.println("Expected Status: In Transit");
		    System.out.println("Actual Status: " + actualStatus);

		    Assert.assertEquals(
		            "Manufacture Receive status is not In Transit",
		            "In Transit",
		            actualStatus
		    );

		    System.out.println("Manufacture Receive listing validation completed successfully.");
		}
	 
	 
	 public void validateManufactureReceivePreview() {

		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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

		    // 1. Verify Manufacture Receive Status
		    WebElement statusElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//p[normalize-space()='In Transit']")
		            )
		    );

		    String actualStatus = statusElement.getText().trim();

		    Assert.assertEquals(
		            "Manufacture Receive status is not In Transit",
		            "In Transit",
		            actualStatus
		    );

		    System.out.println("Manufacture Receive Status: " + actualStatus);

		    // 2. Verify Vendor Name
		    WebElement vendorElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("(//p[contains(@class,'card_coupon_code') and contains(@class,'modal_para_dark')])[2]")
		            )
		    );

		    String actualVendorName = vendorElement.getText().trim();

		    System.out.println("Expected Vendor Name: " + vendorName);
		    System.out.println("Actual Vendor Name: " + actualVendorName);

		    Assert.assertEquals(
		            "Vendor Name is not matching",
		            vendorName,
		            actualVendorName
		    );

		    // 3. Verify Received Date
		    List<WebElement> cardValues = wait.until(
		            ExpectedConditions.visibilityOfAllElementsLocatedBy(
		                    By.xpath("(//p[contains(@class,'card_coupon_code') and contains(@class,'modal_para_dark')])[3]")
		            )
		    );

		    String expectedReceivedDate = LocalDate.now()
		            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		    boolean dateFound = false;

		    for (WebElement element : cardValues) {

		        String value = element.getText().trim();

		        if (value.equals(expectedReceivedDate)) {
		            dateFound = true;
		            System.out.println("Received Date: " + value);
		            break;
		        }
		    }

		    Assert.assertTrue(
		            "Today's received date is not displayed in Manufacture Receive Preview",
		            dateFound
		    );

		    WebElement manufactureOrder = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//p[contains(@class,'form_title') and normalize-space()='Manufacture Order (1)']")
		            )
		    );

		    manufactureOrder.click();

		    Common.waitForElement(1);

		    System.out.println("Manufacture Order (1) clicked successfully.");
		    
		    // 4. Click Manufacture Order link
		    WebElement manufactureOrderLink = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//div[contains(@class,'adrs_tag')]"
		                            + "//a[contains(@href,'/admin/manufacture-order/')"
		                            + " and normalize-space()='" + moId + "']")
		            )
		    );

		    String displayedMoId = manufactureOrderLink.getText().trim();

		    System.out.println("Expected MO ID: " + moId);
		    System.out.println("MO ID displayed in Preview: " + displayedMoId);

		    Assert.assertEquals(
		            "Manufacture Order ID is not matching",
		            moId,
		            displayedMoId
		    );

	//	    manufactureOrderLink.click();

		    Common.waitForElement(2);

		    // 5. Verify Manufacture Order status is Issued
		    WebElement moStatusElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//p[contains(@class,'m-0') and normalize-space()='Issued']")
		            )
		    );

		    String actualMoStatus = moStatusElement.getText().trim();

		    Assert.assertEquals(
		            "Manufacture Order status is not Issued",
		            "Issued",
		            actualMoStatus
		    );

		    System.out.println("Manufacture Order Status: " + actualMoStatus);

		    // 6. Verify Notes
		    WebElement notesElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//div[contains(@class,'field_wrapper')]"
		                            + "//p[contains(@class,'modal_para')"
		                            + " and normalize-space()='Notes']/following-sibling::*[1]")
		            )
		    );

		    String actualNotes = notesElement.getText().trim();

		    System.out.println("Expected Notes: " + note);
		    System.out.println("Actual Notes: " + actualNotes);

		    Assert.assertEquals(
		            "Manufacture Receive Notes are not matching",
		            note,
		            actualNotes
		    );

		    System.out.println("Manufacture Receive Preview validation completed successfully.");
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
		    WebElement threeDotMenu = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.cssSelector(".material_action_dropdown .field_wrapper > label.label")
		            )
		    );

		    threeDotMenu.click();

		  

		    System.out.println("Three-dot menu clicked successfully.");

		    Common.waitForElement(1);


		    // 5. Click Edit
		    WebElement editButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//li[contains(@class,'tag_item')]//button[normalize-space()='Edit']")
		            )
		    );

		    editButton.click();

		    Common.waitForElement(2);

		    System.out.println("Manufacture Receive Edit page opened.");


		    // 6. Click Mark as In-Transit dropdown
		    WebElement markTransitDropdown = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//span[contains(@class,'selected-text') and normalize-space()='Save as In-Transit']")
		            )
		    );

		    markTransitDropdown.click();

		    Common.waitForElement(1);


		    WebElement saveAsReceived = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.cssSelector("button.js-option[data-value='Save as Received']")
		            )
		    );

		    saveAsReceived.click();

		    

		    System.out.println("Save as Received clicked successfully.");
		    Common.waitForElement(1);
		    WebElement confirmReceived = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("confirmReceived")
		            )
		    );

		    confirmReceived.click();

		    Common.waitForElement(2);

		    System.out.println("Yes, Confirm clicked successfully.");
		    Common.waitForElement(1);


		 // 8. Verify error message
		    WebElement errorMessage = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("span.qty_validation_error")
		            )
		    );

		    String actualErrorMessage = errorMessage.getText().trim();

		    String expectedErrorMessage =
		            "Receive all remaining quantities to save as Received";

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
	 
	 
	 public void updateManufactureReceiveToReceived() throws InterruptedException {

		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		    WebElement receiveQuantity = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//p[contains(@class,'fs-6') and normalize-space()='Receive quantity']")
		            )
		    );

		    receiveQuantity.click();

		    Common.waitForElement(1);

		    System.out.println("Receive quantity section clicked.");
		    

		 // 1. Get all item rows
		    List<WebElement> rows = wait.until(
		            ExpectedConditions.visibilityOfAllElementsLocatedBy(
		                    By.xpath("(//table[contains(@class,'modal_table_wrapper')])[2]//tbody/tr")
		            )
		    );

		    int rowCount = rows.size();

		    Assert.assertTrue(
		            "No Manufacture Receive item rows are displayed",
		            rowCount > 0
		    );

		    System.out.println("Total Item Rows: " + rowCount);

		    // 2. Process each row
		    for (int i = 0; i < rowCount; i++) {

		        WebElement row = rows.get(i);

		        // Item name
		        String itemName = row.findElement(
		                By.xpath("./td[1]")
		        ).getText().trim();

		        // Ordered Quantity
		        int orderedQuantity = Integer.parseInt(
		                row.findElement(By.xpath("./td[2]"))
		                        .getText().trim()
		        );

		        // Already Received Quantity
		        int alreadyReceivedQuantity = Integer.parseInt(
		                row.findElement(By.xpath("./td[3]"))
		                        .getText().trim()
		        );

		        // Calculate remaining quantity
		        int remainingQuantity =
		                orderedQuantity - alreadyReceivedQuantity;

		        System.out.println(
		                "Row " + (i + 1)
		                        + " | Item: " + itemName
		                        + " | Ordered: " + orderedQuantity
		                        + " | Already Received: " + alreadyReceivedQuantity
		                        + " | Remaining: " + remainingQuantity
		        );

		        // 3. If remaining quantity is 0, skip the row
		        if (remainingQuantity == 0) {

		            System.out.println(
		                    "Row " + (i + 1)
		                            + " | Item: " + itemName
		                            + " | Remaining quantity is 0 - Skipping."
		            );

		            continue;
		        }

		        // 4. Find Receive Quantity input
		        WebElement receiveQuantityField = row.findElement(
		                By.cssSelector("input.item-size-input")
		        );

		        wait.until(
		                ExpectedConditions.visibilityOf(receiveQuantityField)
		        );

		        // 5. Enter remaining quantity
		        receiveQuantityField.clear();
		        receiveQuantityField.sendKeys(
		                String.valueOf(remainingQuantity)
		        );

		        System.out.println(
		                "Row " + (i + 1)
		                        + " | Item: " + itemName
		                        + " | Receive Quantity entered: "
		                        + remainingQuantity
		        );

		        // 6. Verify entered quantity
		        String actualQuantity =
		                receiveQuantityField.getAttribute("value").trim();

		        Assert.assertEquals(
		                "Receive quantity is incorrect for item: " + itemName,
		                String.valueOf(remainingQuantity),
		                actualQuantity
		        );
		    }

		    System.out.println(
		            "Remaining quantities entered successfully."
		    );
		    Common.waitForElement(2);
		    JavascriptExecutor js = (JavascriptExecutor) driver;

		    js.executeScript("window.scrollTo({ top: 0, behavior: 'smooth' });");
		    Common.waitForElement(1);

		    WebElement saveAsReceivedDropdown = wait.until(
		            ExpectedConditions.presenceOfElementLocated(
		                    By.xpath("//span[contains(@class,'js-selected-text') " +
		                             "and normalize-space()='Save as Received']")
		            )
		    );

		    // Scroll the dropdown into view

		    js.executeScript(
		            "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
		            saveAsReceivedDropdown
		    );

		    Common.waitForElement(1);

		    // Now click
		    wait.until(ExpectedConditions.elementToBeClickable(saveAsReceivedDropdown)).click();

		    Common.waitForElement(1);

		    System.out.println("Save as Received dropdown clicked.");

		    // Click Save as Received option
		    WebElement saveAsReceived = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.cssSelector("button.js-option[data-value='Save as Received']")
		            )
		    );

		    saveAsReceived.click();

		    Common.waitForElement(1);

		    System.out.println("Save as Received option clicked.");
		    System.out.println("Save as Received option clicked.");

		    // 6. Click Yes, Confirm
		    WebElement confirmReceived = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("confirmReceived")
		            )
		    );

		    Assert.assertTrue(
		            "Yes, Confirm button is not displayed",
		            confirmReceived.isDisplayed()
		    );

		    confirmReceived.click();

		    Common.waitForElement(3);

		    System.out.println(
		            "Manufacture Receive confirmed successfully."
		    );

		    // 7. Verify redirected to Manufacture Receives listing page
		    WebElement manufactureReceivesHeading = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath(
		                            "//h1[contains(@class,'page_heading') and normalize-space()='Manufacture Receives']"
		                    )
		            )
		    );

		    Assert.assertTrue(
		            "Manufacture Receives listing page is not displayed",
		            manufactureReceivesHeading.isDisplayed()
		    );

		    String actualHeading =
		            manufactureReceivesHeading.getText().trim();

		    Assert.assertTrue(
		            "Manufacture Receives page heading is incorrect",
		            actualHeading.equalsIgnoreCase("manufacture receives")
		    );

		    System.out.println(
		            "Successfully saved Manufacture Receive as Received."
		    );

		    System.out.println(
		            "Manufacture Receives listing page is displayed successfully."
		    );
		}
	 public void verifyManufactureReceiveReceivedStatus() {

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

		    System.out.println("Manufacture Receive Listing Status: " + listingStatus);

		    Assert.assertEquals(
		            "Manufacture Receive status is not Received in listing",
		            "Received",
		            listingStatus
		    );

		    System.out.println(
		            "First Manufacture Receive row status verified successfully: Received"
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
		                            "//p[normalize-space()='Received']"
		                    )
		            )
		    );

		    String previewStatusText = previewStatus.getText().trim();

		    System.out.println(
		            "Manufacture Receive Preview Status: " + previewStatusText
		    );

		    Assert.assertEquals(
		            "Manufacture Receive status is not Received in Preview",
		            "Received",
		            previewStatusText
		    );

		    System.out.println(
		            "Manufacture Receive Preview status verified successfully: Received"
		    );
		}
	 
	 
	 public void validateManufactureReceiveMandatoryFields() {
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		    // 1. Click Add manufacture-receive
		    WebElement addManufactureReceive = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//a[@bp-button='create' and .//span[normalize-space()='Add manufacture-receive']]")
		            )
		    );

		    addManufactureReceive.click();
		    Common.waitForElement(2);

		    System.out.println("Add manufacture-receive page opened.");

		    // 2. Click Save without selecting Vendor and Manufacture Order
		    WebElement saveButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("saveBtn")
		            )
		    );

		    saveButton.click();
		    Common.waitForElement(1);

		    System.out.println("Save button clicked.");
		    Common.waitForElement(2);
		    // 3. Validate Vendor error message
		    WebElement vendorError = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("span.vendor_error")
		            )
		    );

		    String actualVendorError = vendorError.getText().trim();
		    String expectedVendorError = "Please select vendor";

		    Assert.assertEquals(
		            "Vendor validation message is incorrect",
		            expectedVendorError,
		            actualVendorError
		    );

		    System.out.println("Vendor validation message verified: " + actualVendorError);

		    // 4. Validate Manufacture Order error message
		    WebElement manufactureOrderError = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("span.mo_error")
		            )
		    );

		    String actualManufactureOrderError = manufactureOrderError.getText().trim();
		    String expectedManufactureOrderError =
		            "Please select manufacture order ID";

		    Assert.assertEquals(
		            "Manufacture Order validation message is incorrect",
		            expectedManufactureOrderError,
		            actualManufactureOrderError
		    );

		    System.out.println(
		            "Manufacture Order validation message verified: "
		                    + actualManufactureOrderError
		    );
		}
	 
	 public void validateManufactureReceiveQuantityMandatory() {
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		    // Click Save button
		    WebElement saveButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("saveBtn")
		            )
		    );

		    saveButton.click();
		    Common.waitForElement(1);

		    System.out.println("Save button clicked.");

		    // Validate Receive Quantity error message
		    WebElement receiveQtyError = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("span.receive_qty_error")
		            )
		    );

		    String actualErrorMessage = receiveQtyError.getText().trim();
		    String expectedErrorMessage = "Please Receive Minimum 1 Quantity";

		    Assert.assertTrue(
		            "Receive Quantity error message is not displayed",
		            receiveQtyError.isDisplayed()
		    );

		    Assert.assertEquals(
		            "Receive Quantity validation message is incorrect",
		            expectedErrorMessage,
		            actualErrorMessage
		    );

		    System.out.println(
		            "Receive Quantity validation message verified: "
		                    + actualErrorMessage
		    );
		}
	 
	 
	 public void validateEditDeleteAndDeleteManufactureReceive() {
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
		 
		    // 1. Get the first row
		    WebElement firstRow = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("tbody tr:first-child")
		            )
		    );

		    // 2. Copy first row details
		    String mrId = firstRow.findElement(By.xpath("./td[1]//span"))
		            .getText().trim();

		    String vendorName = firstRow.findElement(By.xpath("./td[2]//span"))
		            .getText().trim();

		    String moId = firstRow.findElement(By.xpath("./td[3]//span"))
		            .getText().trim();

		    String receivedDate = firstRow.findElement(By.xpath("./td[4]//span"))
		            .getText().trim();

		    String receivedQuantity = firstRow.findElement(By.xpath("./td[5]//span"))
		            .getText().trim();

		    String status = firstRow.findElement(By.xpath("./td[6]//p"))
		            .getText().trim();

		    System.out.println("MR ID: " + mrId);
		    System.out.println("Vendor: " + vendorName);
		    System.out.println("MO ID: " + moId);
		    System.out.println("Received Date: " + receivedDate);
		    System.out.println("Received Quantity: " + receivedQuantity);
		    System.out.println("Status: " + status);

		    // 3. Click three-dot menu of first row
		    WebElement threeDotMenu = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    firstRow.findElement(
		                            By.cssSelector("a.actions-buttons-column")
		                    )
		            )
		    );

		    threeDotMenu.click();
		    Common.waitForElement(1);

		    System.out.println("Three-dot menu clicked.");

		    // 4. Verify Preview option
		    WebElement previewOption = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath(
		                            "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]"
		                    )
		            )
		    );

		    Assert.assertTrue(
		            "Preview option is not displayed",
		            previewOption.isDisplayed()
		    );

		    // 5. Verify Edit option
		    WebElement editOption = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath(
		                            "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[2]"
		                    )
		            )
		    );

		    Assert.assertTrue(
		            "Edit option is not displayed",
		            editOption.isDisplayed()
		    );

		    // 6. Verify Delete option
		    WebElement deleteOption = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath(
		                            "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[3]"
		                    )
		            )
		    );

		    Assert.assertTrue(
		            "Delete option is not displayed",
		            deleteOption.isDisplayed()
		    );

		    System.out.println("Preview, Edit and Delete options are available.");

		    // 7. Click Delete
		    deleteOption.click();
		    Common.waitForElement(1);

		    System.out.println("Delete option clicked.");

		    // 8. Verify Delete confirmation button
		    WebElement confirmDeleteButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("DeleteConfirmBtn")
		            )
		    );

		    Assert.assertTrue(
		            "Delete confirmation button is not displayed",
		            confirmDeleteButton.isDisplayed()
		    );

		    // 9. Click Confirm
		    confirmDeleteButton.click();
		    Common.waitForElement(3);

		    System.out.println("Delete confirmation clicked.");

		    // 10. Verify deleted MR is no longer available
		    List<WebElement> mrRows = driver.findElements(
		            By.xpath(
		                    "//tbody/tr/td[1]//span[normalize-space()='" + mrId + "']"
		            )
		    );

		    Assert.assertTrue(
		            "Manufacture Receive " + mrId + " was not deleted successfully",
		            mrRows.isEmpty()
		    );

		    System.out.println(
		            "Manufacture Receive " + mrId +
		            " was deleted successfully."
		    );
		}
	 //TC-01
	 public void validateInTransitStatusPurchaseReceive() {
		 
		 adminLogin();
		 
		 navigatetoManufactureOrderPage();	 
	 
		 openManufactureReceiveAfterMarkAsIssued();
		 
		 enterReceivedQuantityAndSave();
		 
		 validateManufactureReceiveListing();
		 
		 validateManufactureReceivePreview();
	 
	 }
	 
	//TC-02	
		 public void validateReceivedStatus() throws InterruptedException {
			 
			 adminLogin();
			 
			 navigatetoManufactureReceivePage();	
			 
			 edittheIntransittoReceived();
			 
			 updateManufactureReceiveToReceived();
			 
			 verifyManufactureReceiveReceivedStatus();
		 }
	//TC-03 
		 public void varifyValidationErrorMessageCreatePage() { 
		 
			 adminLogin();
			 
			 navigatetoManufactureReceivePage();
			 
			 validateManufactureReceiveMandatoryFields();
		 }
	 
			//TC-04 
		 public void varifyValidationErrorMessage() { 
		 
			 adminLogin();
			 
			 navigatetoManufactureOrderPage();	 
		 
			 openManufactureReceiveAfterMarkAsIssued();
			 
			 validateManufactureReceiveQuantityMandatory();
		 }	 
	 
		//TC-05
		 public void varifyDeleteFunctionalty() { 
		 
			 adminLogin();
			 
			 navigatetoManufactureReceivePage();	 
			 
			 validateEditDeleteAndDeleteManufactureReceive();
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
