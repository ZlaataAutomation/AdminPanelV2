package pages;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
import objectRepo.Vendor_ObjRepo;
import utils.Common;

public class Vendor_Page extends Vendor_ObjRepo {
	
	public Vendor_Page(WebDriver driver) 
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(this.driver, this);
	}
	
	public void adminLogin() {
	
	AdminLogin_Page login= new AdminLogin_Page(driver);
	login.adminLoginApp();
	
	}

	// --- ANSI Color Constants for Console Output ---
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    public String generatedCompanyName;
    public String generatedCompanyEmail;
    public String generatedcompanyNumber;
    public String generatedContactName;
    public String generatedContactEmail;
    public String generatedPhoneNumber;
    public String generatedAccountHolderName;
    public String generatedAccountNumber;
    public String generatedBankName;
    public String generatedIfscCode;
    public String generatedPanCardNumber;
    public String selectedSupplierTypeText;
    public String selectedTagText;


    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static final int ACTION_DELAY = 700;

    private void actionPause() {
        pause(ACTION_DELAY);
    }

    private void logAction(String message) {
        System.out.println(CYAN + " " + RESET + message);
    }

    private void logSuccess(String message) {
        System.out.println(GREEN + BOLD + "[SUCCESS] " + message + RESET);
    }

    private void logFailure(String message) {
        System.out.println(RED + BOLD + "[FAILURE] " + message + RESET);
    }

    private void logHeader(String message) {
        System.out.println("\n" + YELLOW + BOLD + "============ " + message + " ============" + RESET);
    }


    public void verifyCreatedNewVendor() {
    	
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Actions actions = new Actions(driver);
       	    
       	    // Hover on Inventory
       	    WebElement inventory = wait.until(ExpectedConditions.visibilityOfElementLocated(
       	            By.xpath("//button[contains(@class,'sidebar_menu_btn')]//span[normalize-space()='Inventory']")));
       	    actions.moveToElement(inventory).perform();

       	    // Click supplier 
       	    WebElement supplier = wait.until(ExpectedConditions.elementToBeClickable(
       	            By.xpath("(//li[normalize-space()='Vendor'])[1]")));
       	 supplier.click();

       	    Common.waitForElement(2);
        Random random = new Random();

        // Generate Random Data
        generatedCompanyName = "Vendor_" + UUID.randomUUID().toString().substring(0, 8);
        generatedCompanyEmail = "company" + random.nextInt(100000) + "@gmail.com";
        generatedcompanyNumber = "9" + (100000000 + random.nextInt(900000000));
        String[] firstNames = {"Alice", "Bob", "Charlie", "Diana", "Ethan", "Fiona", "George", "Hannah"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Taylor", "Miller", "Davis", "Wilson"};
        generatedContactName = firstNames[random.nextInt(firstNames.length)] + 
                lastNames[random.nextInt(lastNames.length)];
        generatedContactEmail = "contact" + random.nextInt(100000) + "@gmail.com";
        generatedPhoneNumber = "9" + (100000000 + random.nextInt(900000000));

        logHeader("Generated Vendor Test Data");
        logAction("Company Name  : " + BLUE + generatedCompanyName + RESET);
        logAction("Company Email : " + BLUE + generatedCompanyEmail + RESET);
        logAction("Company Phone Number  : " + BLUE + generatedcompanyNumber + RESET);
        logAction("Contact Name  : " + BLUE + generatedContactName + RESET);
        logAction("Contact Email : " + BLUE + generatedContactEmail + RESET);
        logAction("Phone Number  : " + BLUE + generatedPhoneNumber + RESET);

        // Click Create Vendor
        wait.until(ExpectedConditions.elementToBeClickable(createvendorbtn));
        click(createvendorbtn);
        logAction("Clicked on Create Vendor button");
        actionPause();

        // Company Name
        wait.until(ExpectedConditions.visibilityOf(companyName));
        type(companyName, generatedCompanyName);
        logAction("Entered Company Name");
        actionPause();
        

        // Vendor Type
        wait.until(ExpectedConditions.elementToBeClickable(supplierTypeDropdwon));
        Select supplierType = new Select(supplierTypeDropdwon);
        supplierType.selectByIndex(1);
        selectedSupplierTypeText = supplierType.getFirstSelectedOption().getText().trim();
        logAction("Selected Vendor Type : " + selectedSupplierTypeText);
        actionPause();

        // Company Email
        type(emailId, generatedCompanyEmail);
        logAction("Entered Company Email");
        actionPause();
        
        type(companyNumber, generatedcompanyNumber);
        logAction("Entered Company Phone Number");
        actionPause();

     // Tag
        click(tagDropdwon);
        logAction("Clicked Tag Dropdown");
        actionPause();

        if (!tagOptions.isEmpty()) {
            Random random1 = new Random();
            int randomIndex = random1.nextInt(tagOptions.size());
            
            WebElement randomTagOption = tagOptions.get(randomIndex);
            
            click(randomTagOption);
            selectedTagText = randomTagOption.getText().trim();
            logAction("Selected Random Tag: " + selectedTagText);
            actionPause();
        } else {
            logAction("No tag options found in the dropdown!");
        }

        // Contact Details
        type(contactName, generatedContactName);
        logAction("Entered Contact Name");
        actionPause();

        type(contactemailId, generatedContactEmail);
        logAction("Entered Contact Email");
        actionPause();

        type(phoneNumber, generatedPhoneNumber);
        logAction("Entered Phone Number");
        actionPause();

        // Save
        click(saveandNextbtn);
        logAction("Clicked Save & Next button");
    }

    public void addVendorAddressDetails() {
        type(AddressLine1, "Riverview Street");
        logAction("Entered Address Line 1 : Riverview Street");
        actionPause();

        type(AddressLine2, "Karapakkam");
        logAction("Entered Address Line 2 : Karapakkam");
        actionPause();

        Select country = new Select(countrydropdown);
        country.selectByVisibleText("India");
        logAction("Selected Country : India");
        actionPause();

        type(pincode, "600009");
        logAction("Entered Pincode : 600009");
        actionPause();

        if (!sameAddressCheckbox.isSelected()) {
            click(sameAddressCheckbox);
            logAction("Selected Same Address Checkbox");
        }

        actionPause();

        click(saveandNextbtn);
        logAction("Clicked Save & Next button from Address Page");
        actionPause();
    }

    public void addBankDetails() {
        Random random = new Random();

        generatedAccountHolderName = "AccountHolder_" + UUID.randomUUID().toString().substring(0, 6);
        generatedAccountNumber = String.valueOf(1000000000000000L + (long)(random.nextDouble() * 9000000000000000L));
        generatedBankName = "State Bank of India";
        generatedIfscCode = "SBIN0008888";

        logHeader("Generated Bank Details");
        logAction("Account Holder : " + BLUE + generatedAccountHolderName + RESET);
        logAction("Account Number : " + BLUE + generatedAccountNumber + RESET);
        logAction("Bank Name      : " + BLUE + generatedBankName + RESET);
        logAction("IFSC Code      : " + BLUE + generatedIfscCode + RESET);

        // 1. Account Holder
        type(accountHolderName, generatedAccountHolderName);
        logAction("Entered Account Holder Name");
        actionPause();

        // 2. Click Dropdown trigger
        wait.until(ExpectedConditions.elementToBeClickable(bankNamedropdown)).click();
        logAction("Clicked Bank Dropdown");
        actionPause();

        // 3. Type into search field
        type(bankNamedropdown, generatedBankName);
        actionPause();

        // 4. Select Option with fallbacks
        try {
            wait.until(ExpectedConditions.elementToBeClickable(stateBankOfIndiaOption)).click();
        } catch (Exception e) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", stateBankOfIndiaOption);
            } catch (Exception ex) {
                bankNamedropdown.sendKeys(Keys.ENTER);
            }
        }
        logAction("Selected Bank from Dropdown");
        actionPause();

        // 5. Account Number details
        type(accountNumber, generatedAccountNumber);
        logAction("Entered Account Number");
        actionPause();

        type(reEnterAccNumber, generatedAccountNumber);
        logAction("Re-entered Account Number");
        actionPause();

        type(ifscCode, generatedIfscCode);
        logAction("Entered IFSC Code");
        actionPause();

        // 6. Navigation buttons
        click(saveandNextbtn);
        logAction("Clicked Save & Next button from Bank Page");
        actionPause();
    }

    public void addotherdetails() {
        generatedPanCardNumber = "GHFRD" + UUID.randomUUID().toString().substring(0, 5);

        type(panCardNumber, generatedPanCardNumber);
        logAction("Entered the Pan Card number: " + generatedPanCardNumber);
        actionPause();

        click(saveBtn);
        logAction("Clicked Save Button");
        actionPause();
    }

    
    public boolean verifyVendorDetailsInListingTable() {
        logHeader("STARTING Vendor LISTING TABLE VERIFICATION");

        // Wait explicitly for table row to render
        By firstRowXPath = By.xpath("//tbody/tr[1]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstRowXPath));

        pause(2000);
        boolean allMatched = true;

        // 1. Verify Company Name (Column 2)
        WebElement companyNameEle = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"));
        String actualCompanyName = companyNameEle.getText().trim();
        pause(1000);
        if (actualCompanyName.equalsIgnoreCase(generatedCompanyName)) {
            logSuccess("Company Name Matched  --> Expected: [" + generatedCompanyName + "] | Actual: [" + actualCompanyName + "]");
        } else {
            logFailure("Company Name Mismatch --> Expected: [" + generatedCompanyName + "] | Actual: [" + actualCompanyName + "]");
            allMatched = false;
        }

     // 2. Verify Supplier Type (Column 3)
        WebElement supplierTypeEle = driver.findElement(By.xpath("//tbody/tr[1]/td[3]"));
        String actualSupplierType = supplierTypeEle.getText().trim().toLowerCase();
        pause(1000);

        boolean isSupplierTypeMatching = false;
        
        // Fallback to listingSupplierType if selectedSupplierTypeText wasn't updated
        String expectedTypeToCompare = (selectedSupplierTypeText != null && !selectedSupplierTypeText.isEmpty()) 
                                        ? selectedSupplierTypeText 
                                        : listingSupplierType;

        if (expectedTypeToCompare != null && !expectedTypeToCompare.isEmpty()) {
            String expectedClean = expectedTypeToCompare.toLowerCase();
            
            if (actualSupplierType.contains(expectedClean) || expectedClean.contains(actualSupplierType)) {
                isSupplierTypeMatching = true;
            } else {
                String[] keywords = expectedClean.split("[\\s/]+");
                for (String word : keywords) {
                    if (!word.isBlank() && actualSupplierType.contains(word)) {
                        isSupplierTypeMatching = true;
                        break;
                    }
                }
            }
        }

        if (isSupplierTypeMatching) {
            logSuccess("Vendor Type Matched --> Expected: [" + expectedTypeToCompare + "] | Actual: [" + actualSupplierType + "]");
        } else {
            logFailure("Vendor Type Mismatch --> Expected: [" + expectedTypeToCompare + "] | Actual: [" + actualSupplierType + "]");
            allMatched = false;
        }

        // 3. Verify Email (Column 4)
        WebElement emailEle = driver.findElement(By.xpath("//tbody/tr[1]/td[4]"));
        String actualEmail = emailEle.getText().trim();
        pause(1000);
        if (actualEmail.equalsIgnoreCase(generatedCompanyEmail)) {
            logSuccess("Company Email Matched --> Expected: [" + generatedCompanyEmail + "] | Actual: [" + actualEmail + "]");
        } else {
            logFailure("Company Email Mismatch --> Expected: [" + generatedCompanyEmail + "] | Actual: [" + actualEmail + "]");
            allMatched = false;
        }

        // 4. Verify Tag (Column 5)
        WebElement tagEle = driver.findElement(By.xpath("//tbody/tr[1]/td[5]"));
        String actualTag = tagEle.getText().trim();
        pause(1000);
        if (selectedTagText != null && actualTag.toLowerCase().contains(selectedTagText.toLowerCase())) {
            logSuccess("Tag Matched           --> Expected: [" + selectedTagText + "] | Actual: [" + actualTag + "]");
        } else {
            logFailure("Tag Mismatch          --> Expected: [" + selectedTagText + "] | Actual: [" + actualTag + "]");
            allMatched = false;
        }
        
        // 5. Verify Phone Number (Column 6)
        WebElement phoneEle = driver.findElement(By.xpath("//tbody/tr[1]/td[6]"));
        String actualPhone = phoneEle.getText().trim();
        pause(1000);
        if (actualPhone.equalsIgnoreCase(generatedcompanyNumber)) {
            logSuccess("Company Number Matched --> Expected: [" + generatedcompanyNumber + "] | Actual: [" + actualPhone + "]");
        } else {
            logFailure("Company Number Mismatch --> Expected: [" + generatedcompanyNumber + "] | Actual: [" + actualPhone + "]");
            allMatched = false;
        }

        // 6. Verify Contact Name (Column 8)
        WebElement contactNameEle = driver.findElement(By.xpath("//tbody/tr[1]/td[8]"));
        String actualContactName = contactNameEle.getText().trim();
        pause(1000);
        if (actualContactName.equalsIgnoreCase(generatedContactName)) {
            logSuccess("Contact Name Matched  --> Expected: [" + generatedContactName + "] | Actual: [" + actualContactName + "]");
        } else {
            logFailure("Contact Name Mismatch --> Expected: [" + generatedContactName + "] | Actual: [" + actualContactName + "]");
            allMatched = false;
        }

        pause(1500);

        if (allMatched) {
            logSuccess("ALL Vendor DETAILS VERIFIED SUCCESSFULLY ON THE LISTING PAGE!");
        } else {
            logFailure("❌ ONE OR MORE Vendor DETAILS DID NOT MATCH THE LISTING PAGE!");
        }

        return allMatched;
    }
    
    
    public boolean verifyVendorDetailsInPreviewPage() {
        logHeader("NAVIGATING TO PREVIEW PAGE");

        wait.until(ExpectedConditions.elementToBeClickable(threeBotbtn));
        click(threeBotbtn);
        logAction("Clicked 3-Dots Action Button");
        actionPause();

        wait.until(ExpectedConditions.elementToBeClickable(previewbtn));
        click(previewbtn);
        logAction("Clicked Preview Option");
        pause(2000);

        logHeader("STARTING PREVIEW PAGE VERIFICATION");

        boolean allMatched = true;

        // 1. Contact Name
        wait.until(ExpectedConditions.visibilityOf(previewContactName));
        String actualContactName = previewContactName.getText().trim();
        pause(500);
        if (actualContactName.equalsIgnoreCase(generatedContactName)) {
            logSuccess("Preview Contact Name Matched  --> Expected: [" + generatedContactName + "] | Actual: [" + actualContactName + "]");
        } else {
            logFailure("Preview Contact Name Mismatch --> Expected: [" + generatedContactName + "] | Actual: [" + actualContactName + "]");
            allMatched = false;
        }

        // 2. Contact Email
        String actualContactEmail = previewContactEmail.getText().trim();
        pause(500);
        if (actualContactEmail.equalsIgnoreCase(generatedContactEmail)) {
            logSuccess("Preview Contact Email Matched --> Expected: [" + generatedContactEmail + "] | Actual: [" + actualContactEmail + "]");
        } else {
            logFailure("Preview Contact Email Mismatch --> Expected: [" + generatedContactEmail + "] | Actual: [" + actualContactEmail + "]");
            allMatched = false;
        }

        // 3. Phone Number
        String actualPhoneNumber = previewPhoneNumber.getText().trim();
        pause(500);
        if (actualPhoneNumber.contains(generatedPhoneNumber)) {
            logSuccess("Preview Phone Number Matched  --> Expected: [" + generatedPhoneNumber + "] | Actual: [" + actualPhoneNumber + "]");
        } else {
            logFailure("Preview Phone Number Mismatch --> Expected: [" + generatedPhoneNumber + "] | Actual: [" + actualPhoneNumber + "]");
            allMatched = false;
        }

        // 4. Entire Address
//        String actualAddress = previewAddress.getText().trim();
//        pause(500);
//        if (actualAddress.contains("Riverview Street") && actualAddress.contains("Karapakkam") && actualAddress.contains("600009")) {
//            logSuccess("Preview Address Details Matched --> Actual: [" + actualAddress.replace("\n", " ") + "]");
//        } else {
//            logFailure("Preview Address Details Mismatch --> Actual: [" + actualAddress + "]");
//            allMatched = false;
//        }

        // 5. Bank Name
        String actualBankName = previewBankName.getText().trim();
        pause(500);
        if (actualBankName.equalsIgnoreCase(generatedBankName)) {
            logSuccess("Preview Bank Name Matched      --> Expected: [" + generatedBankName + "] | Actual: [" + actualBankName + "]");
        } else {
            logFailure("Preview Bank Name Mismatch      --> Expected: [" + generatedBankName + "] | Actual: [" + actualBankName + "]");
            allMatched = false;
        }

        // 6. Account Holder
        String actualAccountHolder = previewAccountHolder.getText().trim();
        pause(500);
        if (actualAccountHolder.equalsIgnoreCase(generatedAccountHolderName)) {
            logSuccess("Preview Account Holder Matched --> Expected: [" + generatedAccountHolderName + "] | Actual: [" + actualAccountHolder + "]");
        } else {
            logFailure("Preview Account Holder Mismatch --> Expected: [" + generatedAccountHolderName + "] | Actual: [" + actualAccountHolder + "]");
            allMatched = false;
        }

        // 7. Account Number
        String actualAccountNumber = previewAccountNumber.getText().trim();
        pause(500);
        if (actualAccountNumber.contains(generatedAccountNumber)) {
            logSuccess("Preview Account Number Matched --> Expected: [" + generatedAccountNumber + "] | Actual: [" + actualAccountNumber + "]");
        } else {
            logFailure("Preview Account Number Mismatch --> Expected: [" + generatedAccountNumber + "] | Actual: [" + actualAccountNumber + "]");
            allMatched = false;
        }

        // 8. IFSC Code
        String actualIfscCode = previewIfscCode.getText().trim();
        pause(500);
        if (actualIfscCode.equalsIgnoreCase(generatedIfscCode)) {
            logSuccess("Preview IFSC Code Matched      --> Expected: [" + generatedIfscCode + "] | Actual: [" + actualIfscCode + "]");
        } else {
            logFailure("Preview IFSC Code Mismatch      --> Expected: [" + generatedIfscCode + "] | Actual: [" + actualIfscCode + "]");
            allMatched = false;
        }

        pause(1500);

        if (allMatched) {
            logSuccess("🎉 ALL VENDOR DETAILS VERIFIED SUCCESSFULLY ON PREVIEW PAGE!");
        } else {
            logFailure("❌ ONE OR MORE VENDOR DETAILS DID NOT MATCH ON PREVIEW PAGE!");
        }
		return allMatched;
    }  

    private String listingCompanyName;
    private String listingSupplierType;
    private String listingCompanyEmail;
    private String listingTag;
    private String listingPhoneNumber;
    private String listingContactName;

    public String updatedCompanyName;
    public String updatedCompanyEmail;
    public String updatedContactName;
    public String updatedContactEmail;
    public String updatedPhoneNumber;

    // ==========================================
    // TC-02 EDIT FLOW IMPLEMENTATION
    // ==========================================

    private void clearAndSendKeys(WebElement element, String text) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));

            element.clear();
            element.sendKeys(Keys.CONTROL + "a");
            element.sendKeys(Keys.BACK_SPACE);

            element.sendKeys(text);
            actionPause();
        } catch (Exception e) {
            logAction("Failed to clear and send keys to element: " + e.getMessage());
            throw e;
        }
    }

    private boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public void captureListingDetailsAndGoToPreview() {
    	
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Actions actions = new Actions(driver);
       	    
       	    // Hover on Inventory
       	    WebElement inventory = wait.until(ExpectedConditions.visibilityOfElementLocated(
       	            By.xpath("//button[contains(@class,'sidebar_menu_btn')]//span[normalize-space()='Inventory']")));
       	    actions.moveToElement(inventory).perform();

       	    // Click supplier 
       	    WebElement supplier = wait.until(ExpectedConditions.elementToBeClickable(
       	            By.xpath("(//li[normalize-space()='Vendor'])[1]")));
       	 supplier.click();

       	    Common.waitForElement(2);
    	
        logHeader("CAPTURING Vendor DETAILS FROM LISTING TABLE BEFORE PREVIEW");

        By firstRowXPath = By.xpath("//tbody/tr[1]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstRowXPath));
        pause(1000);

        listingCompanyName  = driver.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText().trim();
        listingSupplierType = driver.findElement(By.xpath("//tbody/tr[1]/td[3]")).getText().trim();
        listingCompanyEmail = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText().trim();
        listingTag          = driver.findElement(By.xpath("//tbody/tr[1]/td[5]")).getText().trim();
        listingPhoneNumber  = driver.findElement(By.xpath("//tbody/tr[1]/td[6]")).getText().trim();
        listingContactName  = driver.findElement(By.xpath("//tbody/tr[1]/td[8]")).getText().trim();

        // FIX: Assign existing table supplier type so verification won't compare against null
        selectedSupplierTypeText = listingSupplierType;

        logAction("Captured Listing Company Name  : " + BLUE + listingCompanyName + RESET);
        logAction("Captured Listing Vendor Type : " + BLUE + listingSupplierType + RESET);
        logAction("Captured Listing Company Email : " + BLUE + listingCompanyEmail + RESET);
        logAction("Captured Listing Tag           : " + BLUE + listingTag + RESET);
        logAction("Captured Listing Phone Number  : " + BLUE + listingPhoneNumber + RESET);
        logAction("Captured Listing Contact Name  : " + BLUE + listingContactName + RESET);

        wait.until(ExpectedConditions.elementToBeClickable(threeBotbtn));
        click(threeBotbtn);
        logAction("Clicked 3-Dots Action Button");
        actionPause();

        wait.until(ExpectedConditions.elementToBeClickable(previewbtn));
        click(previewbtn);
        logAction("Clicked Preview Option");
        actionPause();

        fetchExistingSupplierDetailsFromPreview();
    }

    public void fetchExistingSupplierDetailsFromPreview() {
        logHeader("READING EXISTING VENDOR DETAILS FROM PREVIEW");

        wait.until(ExpectedConditions.visibilityOf(previewContactName));

        generatedContactName       = previewContactName.getText().trim();
        generatedContactEmail      = previewContactEmail.getText().trim();
        generatedPhoneNumber       = previewPhoneNumber.getText().trim();
        generatedBankName          = previewBankName.getText().trim();
        generatedAccountHolderName = previewAccountHolder.getText().trim();
        generatedAccountNumber    = previewAccountNumber.getText().trim();
        generatedIfscCode          = previewIfscCode.getText().trim();

        logAction("Captured Contact Name  : " + BLUE + generatedContactName + RESET);
        logAction("Captured Contact Email : " + BLUE + generatedContactEmail + RESET);
        logAction("Captured Phone Number  : " + BLUE + generatedPhoneNumber + RESET);
        logAction("Captured Bank Name      : " + BLUE + generatedBankName + RESET);
        logAction("Captured Account Holder : " + BLUE + generatedAccountHolderName + RESET);
        logAction("Captured Account No.    : " + BLUE + generatedAccountNumber + RESET);
        logAction("Captured IFSC Code      : " + BLUE + generatedIfscCode + RESET);
    }

    public void readSupplierDetailsFromPreview() {
        logHeader("NAVIGATING TO PREVIEW TO COPY EXISTING DETAILS");

        wait.until(ExpectedConditions.elementToBeClickable(threeBotbtn));
        click(threeBotbtn);
        logAction("Clicked 3-Dots Action Button");
        actionPause();

        wait.until(ExpectedConditions.elementToBeClickable(previewbtn));
        click(previewbtn);
        logAction("Clicked Preview Option");
        actionPause();

        fetchExistingSupplierDetailsFromPreview();
    }

    public void clickPreviewBack() {
        wait.until(ExpectedConditions.elementToBeClickable(previewBackBtn));
        click(previewBackBtn);
        logAction("Clicked Back Button from Preview Page");
        actionPause();
    }

    public void openEditVendorForm() {
        wait.until(ExpectedConditions.elementToBeClickable(threeBotbtn));
        click(threeBotbtn);
        logAction("Clicked 3-Dots Action Button for Edit");
        actionPause();

        wait.until(ExpectedConditions.elementToBeClickable(editBtn));
        click(editBtn);
        logAction("Clicked Edit Option");
        actionPause();
    }

    public void updateBasicVendorDetails() {
        Random random = new Random();

        // Generate data values
        updatedCompanyName   = "Vendor_Edit_" + UUID.randomUUID().toString().substring(0, 8);
        updatedCompanyEmail  = "company_edit" + random.nextInt(100000) + "@gmail.com";
        generatedcompanyNumber = "9" + (100000000 + random.nextInt(900000000));
        String[] firstNames = {"Alice", "Bob", "Charlie", "Diana", "Ethan", "Fiona", "George", "Hannah"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Taylor", "Miller", "Davis", "Wilson"};
        // Pick a random index for both arrays
        updatedContactName = firstNames[random.nextInt(firstNames.length)] + 
                                      lastNames[random.nextInt(lastNames.length)];
        updatedContactEmail  = "contact_edit" + random.nextInt(100000) + "@gmail.com";
        updatedPhoneNumber   = "9" + (100000000 + random.nextInt(900000000));

        // Assign to master instance variables used by assertions
        generatedCompanyName  = updatedCompanyName;
        generatedCompanyEmail = updatedCompanyEmail;
        generatedContactName  = updatedContactName;
        generatedContactEmail = updatedContactEmail;
        generatedPhoneNumber  = updatedPhoneNumber;

        logHeader("GENERATING & ENTERING UPDATED BASIC VENDOR DETAILS");

        // Edit Company Name
        wait.until(ExpectedConditions.visibilityOf(companyName));
        clearAndSendKeys(companyName, updatedCompanyName);
        logAction("Entered Updated Company Name  : " + BLUE + companyName.getAttribute("value") + RESET);

        // Edit Company Email
        clearAndSendKeys(emailId, updatedCompanyEmail);
        logAction("Entered Updated Company Email : " + BLUE + emailId.getAttribute("value") + RESET);

        // Edit Company Phone Number
        if (isElementPresent(companyNumber)) {
            clearAndSendKeys(companyNumber, generatedcompanyNumber);
            logAction("Entered Updated Company Phone : " + BLUE + companyNumber.getAttribute("value") + RESET);
        }

        // Edit Tag Selection
        if (isElementPresent(tagDropdwon)) {
        	if (isElementPresent(tagDropdwon)) {
                click(tagDropdwon);
                logAction("Clicked Tag Dropdown");
                actionPause();

                // Check if the list of options is loaded and not empty
                if (tagOptions != null && !tagOptions.isEmpty()) {
                    Random random2 = new Random();
                    int randomIndex = random2.nextInt(tagOptions.size());
                    
                    WebElement randomTagOption = tagOptions.get(randomIndex);
                    
                    click(randomTagOption);
                    selectedTagText = randomTagOption.getText().trim();
                    logAction("Selected Updated Tag        : " + BLUE + selectedTagText + RESET);
                    actionPause();
                } else {
                    logAction("No tag options found in the dropdown!");
                }
            }

        // Edit Contact Name
        clearAndSendKeys(contactName, updatedContactName);
        logAction("Entered Updated Contact Name  : " + BLUE + contactName.getAttribute("value") + RESET);

        // Edit Contact Email
        clearAndSendKeys(contactemailId, updatedContactEmail);
        logAction("Entered Updated Contact Email : " + BLUE + contactemailId.getAttribute("value") + RESET);

        // Edit Contact Phone Number
        if (isElementPresent(phoneNumber)) {
            clearAndSendKeys(phoneNumber, updatedPhoneNumber);
            logAction("Entered Updated Phone Number  : " + BLUE + phoneNumber.getAttribute("value") + RESET);
        }

        // Save & Next
        wait.until(ExpectedConditions.elementToBeClickable(updateAndNext));
        click(updateAndNext);
        logAction("Clicked Update & Next button from Basic Details Page");
        actionPause();
        }
    }

    public void updateAddressDetails() {
        logHeader("EDITING ADDRESS DETAILS");

        clearAndSendKeys(AddressLine1, "World Trade Centre");
        logAction("Entered Address Line 1 : " + BLUE + AddressLine1.getAttribute("value") + RESET);

        clearAndSendKeys(AddressLine2, "Perungudi");
        logAction("Entered Address Line 2 : " + BLUE + AddressLine2.getAttribute("value") + RESET);

        if (isElementPresent(countrydropdown)) {
            Select country = new Select(countrydropdown);
            country.selectByVisibleText("India");
            logAction("Selected Country       : " + BLUE + "India" + RESET);
            actionPause();
        }

        if (isElementPresent(pincode)) {
            clearAndSendKeys(pincode, "600096");
            logAction("Entered Pincode        : " + BLUE + pincode.getAttribute("value") + RESET);
        }

        if (isElementPresent(sameAddressCheckbox) && !sameAddressCheckbox.isSelected()) {
            click(sameAddressCheckbox);
            logAction("Selected Same Address Checkbox");
            actionPause();
        }

        wait.until(ExpectedConditions.elementToBeClickable(updateAndNext));
        click(updateAndNext);
        logAction("Clicked Update & Next button from Address Page");
        actionPause();
    }

    public void updateBankDetails() {
        Random random = new Random();

        generatedAccountHolderName = "EditHolder_" + UUID.randomUUID().toString().substring(0, 6);
        generatedAccountNumber    = String.valueOf(1000000000000000L + (long)(random.nextDouble() * 9000000000000000L));
        generatedBankName          = "State Bank of India";
        generatedIfscCode          = "SBIN0008888";

        logHeader("EDITING BANK DETAILS");

        // Account Holder
        clearAndSendKeys(accountHolderName, generatedAccountHolderName);
        logAction("Entered Account Holder Name : " + BLUE + accountHolderName.getAttribute("value") + RESET);

        // Bank Dropdown Selection
        if (isElementPresent(bankNamedropdown)) {
            wait.until(ExpectedConditions.elementToBeClickable(bankNamedropdown)).click();
            logAction("Clicked Bank Dropdown");
            actionPause();

            type(bankNamedropdown, generatedBankName);
            actionPause();

            try {
                wait.until(ExpectedConditions.elementToBeClickable(stateBankOfIndiaOption)).click();
            } catch (Exception e) {
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", stateBankOfIndiaOption);
                } catch (Exception ex) {
                    bankNamedropdown.sendKeys(Keys.ENTER);
                }
            }
            logAction("Selected Bank Name         : " + BLUE + generatedBankName + RESET);
            actionPause();
        }

        // Account Numbers & IFSC
        clearAndSendKeys(accountNumber, generatedAccountNumber);
        logAction("Entered Account Number      : " + BLUE + accountNumber.getAttribute("value") + RESET);

        clearAndSendKeys(reEnterAccNumber, generatedAccountNumber);
        logAction("Re-entered Account Number   : " + BLUE + reEnterAccNumber.getAttribute("value") + RESET);

        clearAndSendKeys(ifscCode, generatedIfscCode);
        logAction("Entered IFSC Code           : " + BLUE + ifscCode.getAttribute("value") + RESET);

        wait.until(ExpectedConditions.elementToBeClickable(updateAndNext));
        click(updateAndNext);
        logAction("Clicked Update & Next button from Bank Page");
        actionPause();
    }

    public void updatePanAndSave() {
        logHeader("EDITING PAN DETAILS AND SAVING");

        generatedPanCardNumber = "EDITP" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        clearAndSendKeys(panCardNumber, generatedPanCardNumber);
        logAction("Entered PAN Card Number     : " + BLUE + panCardNumber.getAttribute("value") + RESET);

        wait.until(ExpectedConditions.elementToBeClickable(updateBtn));
        click(updateBtn);
        logAction("Clicked Final Update Button");
        actionPause();
    }
   
 // Instance Variables to Store Generated Preview Pop-up Values
    private String popupGenContactName;
    private String popupGenContactEmail;
    private String popupGenContactPhone;

    private String popupGenAccountHolder;
    private String popupGenBankName;
    private String popupGenAccountNumber;
    private String popupGenIfscCode;

    // ==========================================
    // TC-03 BUSINESS LOGIC METHODS
    // ==========================================
    
    
    public void navigatetoVendorPage() {
        wait.until(ExpectedConditions.visibilityOf(inventory));
        new Actions(driver).moveToElement(inventory).perform();

        wait.until(ExpectedConditions.visibilityOf(Vendors));
        click(Vendors);
    	
        logHeader("NAVIGATING TO VENDOR PREVIEW PAGE");
    }

    public void navigateToVendorPreviewPage() {
//    	
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//        Actions actions = new Actions(driver);
//       	    
//       	    // Hover on Inventory
//       	    WebElement inventory = wait.until(ExpectedConditions.visibilityOfElementLocated(
//       	            By.xpath("//button[contains(@class,'sidebar_menu_btn')]//span[normalize-space()='Inventory']")));
//       	    actions.moveToElement(inventory).perform();
//
//       	    // Click supplier 
//       	    WebElement supplier = wait.until(ExpectedConditions.elementToBeClickable(
//       	            By.xpath("(//li[normalize-space()='Vendor'])[1]")));
//       	 supplier.click();
//
//       	    Common.waitForElement(2);
//       	    
//        logHeader("NAVIGATING TO VENDOR PREVIEW PAGE");

        // 1. Ensure table row is present and visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr[1]")));
        pause(1000);

        //JavascriptExecutor js = (JavascriptExecutor) driver;

        // 2. Force click the 3-dots action button using JS to prevent click interception
//        wait.until(ExpectedConditions.elementToBeClickable(threeBotbtn));
//        js.executeScript("arguments[0].click();", threeBotbtn);
//        logAction("Clicked 3-Dots Action Button");
//
//        // 3. Wait specifically for the Preview link using your exact XPath
//        By previewOptionXpath = By.xpath("//a[@class='dropdown-item'])[46]");
//        WebElement previewElement = wait.until(ExpectedConditions.visibilityOfElementLocated(previewOptionXpath));
//        actionPause();
        
        wait.until(ExpectedConditions.elementToBeClickable(threeBotbtn));
        click(threeBotbtn);
        logAction("Clicked 3-Dots Action Button");
        actionPause();

        wait.until(ExpectedConditions.elementToBeClickable(previewbtn));
        click(previewbtn);
        logAction("Clicked Preview Option");
        pause(2000);
        
//        // 4. Force click the Preview option via JavaScript
//        js.executeScript("arguments[0].click();", previewElement);
//        logAction("Clicked Preview Option");
//        actionPause();
    }
    
    private String generateRandomAlphabetic(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    public void addContactDetailsFromPreview() {
        logHeader("ADDING NEW CONTACT DETAILS FROM PREVIEW POPUP");

        Random random = new Random();
        String randomLetters = generateRandomAlphabetic(5); // e.g. "useyv"
        
        // ⚠️ DO NOT USE UNDERSCORES as the application UI strips them upon save
        popupGenContactName  = "PreviewContact" + randomLetters; // e.g. "PreviewContactuseyv"
        popupGenContactEmail = "preview_contact" + random.nextInt(100000) + "@gmail.com";
        popupGenContactPhone = "9" + (100000000 + random.nextInt(900000000));

        wait.until(ExpectedConditions.elementToBeClickable(addContactBtn));
        click(addContactBtn);
        logAction("Clicked 'Add Contact' Button");
        actionPause();

        clearAndSendKeys(popupContactNameInput, popupGenContactName);
        logAction("Entered Contact Name  : " + BLUE + popupGenContactName + RESET);

        clearAndSendKeys(popupContactEmailInput, popupGenContactEmail);
        logAction("Entered Contact Email : " + BLUE + popupGenContactEmail + RESET);

        clearAndSendKeys(popupContactPhoneInput, popupGenContactPhone);
        logAction("Entered Contact Phone : " + BLUE + popupGenContactPhone + RESET);

        wait.until(ExpectedConditions.elementToBeClickable(contactPopupSaveBtn));
        click(contactPopupSaveBtn);
        logAction("Clicked Save Button in Add Contact Popup");
        actionPause();
    }


    public void addBankDetailsFromPreview() {
        logHeader("ADDING NEW BANK DETAILS FROM PREVIEW POPUP");

        Random random = new Random();
        String randomLetters = generateRandomAlphabetic(5);
        popupGenAccountHolder = "PreviewHoldername" + randomLetters;
        popupGenBankName      = "ICICI Bank Ltd";
        popupGenAccountNumber = String.valueOf(1000000000000000L + (long) (random.nextDouble() * 9000000000000000L));
        popupGenIfscCode      = "ICIC0000001";

        wait.until(ExpectedConditions.elementToBeClickable(addBankBtn));
        click(addBankBtn);
        logAction("Clicked 'Add Bank' Button");
        actionPause();

        clearAndSendKeys(popupAccountHolderInput, popupGenAccountHolder);
        logAction("Entered Account Holder Name : " + BLUE + popupGenAccountHolder + RESET);

        // Bank Dropdown Selection
        click(popupBankSearchInput);
        popupBankSearchInput.sendKeys(popupGenBankName);
        actionPause();

        try {
            wait.until(ExpectedConditions.elementToBeClickable(iciciBankOption)).click();
        } catch (Exception e) {
            popupBankSearchInput.sendKeys(Keys.ENTER);
        }
        logAction("Selected Bank Name         : " + BLUE + popupGenBankName + RESET);

        clearAndSendKeys(popupAccountNumberInput, popupGenAccountNumber);
        logAction("Entered Account Number      : " + BLUE + popupGenAccountNumber + RESET);

        clearAndSendKeys(popupReAccountNumberInput, popupGenAccountNumber);
        logAction("Re-entered Account Number   : " + BLUE + popupGenAccountNumber + RESET);

        clearAndSendKeys(popupIfscCodeInput, popupGenIfscCode);
        logAction("Entered IFSC Code           : " + BLUE + popupGenIfscCode + RESET);

        wait.until(ExpectedConditions.elementToBeClickable(bankPopupSaveBtn));
        click(bankPopupSaveBtn);
        logAction("Clicked Save Button in Add Bank Popup");
        actionPause();
    }

    public boolean verifyAddedContactAndBankDetailsInPreview() {
        logHeader("VERIFYING ADDED CONTACT AND BANK DETAILS ON PREVIEW PAGE");

        boolean allMatched = true;
        pause(1500); // Allow DOM to re-render after popup close

        // =========================================================================
        // 1. DYNAMIC CONTACT DETAILS VERIFICATION
        // =========================================================================
        allMatched &= verifyDynamicValue("Preview Contact Name", popupGenContactName);
        allMatched &= verifyDynamicValue("Preview Contact Email", popupGenContactEmail);
        allMatched &= verifyDynamicValue("Preview Contact Phone", popupGenContactPhone);

        // =========================================================================
        // 2. DYNAMIC BANK DETAILS VERIFICATION
        // =========================================================================
        allMatched &= verifyDynamicValue("Preview Bank Name", popupGenBankName);
        allMatched &= verifyDynamicValue("Preview Account Holder", popupGenAccountHolder);
        allMatched &= verifyDynamicValue("Preview Account Number", popupGenAccountNumber);
        allMatched &= verifyDynamicValue("Preview IFSC Code", popupGenIfscCode);

        if (!allMatched) {
            logFailure("❌ ONE OR MORE CONTACT/BANK DETAILS DID NOT MATCH ON PREVIEW PAGE!");
        }

        return allMatched;
    }


   
    private boolean verifyDynamicValue(String fieldName, String expectedValue) {
        if (expectedValue == null || expectedValue.trim().isEmpty()) {
            logFailure(fieldName + " Skipped --> Expected value is empty/null");
            return false;
        }

        String cleanExpected = expectedValue.trim();

        // Locates any element containing the expected text
        String dynamicXpath = String.format(
            "//*[contains(normalize-space(text()), '%s')]", cleanExpected
        );

        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // Wait for element to be present and displayed in DOM
            WebElement foundElement = shortWait.until(d -> {
                List<WebElement> matchingElements = d.findElements(By.xpath(dynamicXpath));
                return matchingElements.stream()
                    .filter(e -> e.isDisplayed() 
                        && !e.getTagName().equalsIgnoreCase("script") 
                        && !e.getTagName().equalsIgnoreCase("style"))
                    .findFirst()
                    .orElse(null);
            });

            if (foundElement != null) {
                // Retrieve actual text found in the DOM node
                String actualText = foundElement.getText().trim();
                
                // Side-by-side formatted log output
                logSuccess(String.format("%-28s Matched --> Expected: [%s] | Found on Page: [%s]", 
                        fieldName, cleanExpected, actualText));
                return true;
            } else {
                logFailure(String.format("%-28s Mismatch --> Expected: [%s] | NOT FOUND ON PAGE", 
                        fieldName, cleanExpected));
                return false;
            }
        } catch (Exception e) {
            logFailure(String.format("%-28s Exception --> Expected: [%s] | NOT FOUND ON PAGE (Timeout)", 
                    fieldName, cleanExpected));
            return false;
        }
    }

    // Helper method for fallback extraction
    private String getElementTextSafely(By primary, By fallback) {
        try {
            List<WebElement> elements = driver.findElements(primary);
            if (!elements.isEmpty() && !elements.get(0).getText().trim().isEmpty()) {
                return elements.get(0).getText().trim();
            }
            return driver.findElement(fallback).getText().trim();
        } catch (Exception e) {
            return driver.findElement(fallback).getText().trim();
        }
    }
	
    // TC-04
    // Stores status strings for comparison and logging
    private String statusAfterFirstToggle;
    private String statusAfterSecondToggle;
    public void toggleSupplierStatusFromPreview() {
        // 1. Navigate to Preview Page
        navigateToVendorPreviewPage();
        
        // 2. Click Preview 3-Dots Menu & Click "Mark as Inactive"
        clickPreviewThreeDotsAndOption();
        actionPause();
        Common.waitForElement(2);
        // 3. Capture Status after 1st toggle
        wait.until(ExpectedConditions.visibilityOf(supplierStatusText));
        statusAfterFirstToggle = supplierStatusText.getText().trim();
        logAction("Captured Status after First Toggle : " + BLUE + statusAfterFirstToggle + RESET);

        // 4. Click Preview 3-Dots Menu again & Click the toggle option back
        clickPreviewThreeDotsAndOption();
        actionPause();

        // 5. Capture Status after 2nd toggle
        wait.until(ExpectedConditions.visibilityOf(supplierStatusText));
        statusAfterSecondToggle = supplierStatusText.getText().trim();
        logAction("Captured Status after Second Toggle: " + BLUE + statusAfterSecondToggle + RESET);
    }


    private void clickPreviewThreeDotsAndOption() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        wait.until(ExpectedConditions.elementToBeClickable(previewThreedot));
        js.executeScript("arguments[0].click();", previewThreedot);
        logAction("Clicked Preview 3-Dots Action Button");
        Common.waitForElement(2);
        
        wait.until(ExpectedConditions.elementToBeClickable(markAsInactivebtn));
        js.executeScript("arguments[0].click();", markAsInactivebtn);
        logAction("Clicked Status Action Button ('Mark as Inactive / Active')");
        Common.waitForElement(2);
    }

    public boolean verifySupplierStatusToggle() {
        logHeader("VERIFYING VENDOR STATUS TOGGLE ON PREVIEW PAGE");

        boolean isFirstToggleSuccess = statusAfterFirstToggle.equalsIgnoreCase("Inactive");
        
        if (isFirstToggleSuccess) {
            logSuccess(String.format("%-28s Matched --> Expected: [Inactive] | Found on Page: [%s]", 
                    "Status After 1st Toggle", statusAfterFirstToggle));
        } else {
            logFailure(String.format("%-28s Mismatch --> Expected: [Inactive] | Found on Page: [%s]", 
                    "Status After 1st Toggle", statusAfterFirstToggle));
        }

        logSuccess(String.format("%-28s Reflected --> Status reset to: [%s]", 
                "Status After 2nd Toggle", statusAfterSecondToggle));

        return isFirstToggleSuccess;
    }
    
    
    private String deletedSupplierId;
    private String deletedSupplierName;
    private boolean isSupplierPresentAfterCancel;

    // ==========================================
    // TC-05 BUSINESS LOGIC METHODS
    // ==========================================
    public void deleteSupplierWithCancelAndConfirm() {
    	
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Actions actions = new Actions(driver);
       	    

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // 1. Capture Supplier ID and Name from the first row
        wait.until(ExpectedConditions.visibilityOf(firstRowSupplierId));
        deletedSupplierId = firstRowSupplierId.getText().trim();
        deletedSupplierName = firstRowSupplierName.getText().trim();

        logAction("Target Supplier ID   : " + BLUE + deletedSupplierId + RESET);
        logAction("Target Supplier Name : " + BLUE + deletedSupplierName + RESET);

        // =========================================================================
        // STEP 1: CANCEL DELETION VERIFICATION
        // =========================================================================
        // Open 3-dots action menu
        click(threeBotbtn);
        logAction("Clicked 3-Dots Action Button");
        actionPause();

        // Click 'Delete' option
        wait.until(ExpectedConditions.elementToBeClickable(deletebtn));
        js.executeScript("arguments[0].click();", deletebtn);
        logAction("Clicked 'Delete' Option from Dropdown");
        actionPause();

     // Click 'Cancel' button in popup
        wait.until(ExpectedConditions.elementToBeClickable(deletePopupCancelBtn));
        js.executeScript("arguments[0].click();", deletePopupCancelBtn);
        logAction("Clicked 'Cancel' Button in Delete Confirmation Popup");
        actionPause();

        // Verify supplier is still present on the page after cancel
        isSupplierPresentAfterCancel = isTextPresentOnPage(deletedSupplierId);
        if (isSupplierPresentAfterCancel) {
            logSuccess(String.format("%-28s Matched --> Expected: [%s] Still Visible on Page", 
                    "Cancel Deletion Check", deletedSupplierId));
        } else {
            logFailure(String.format("%-28s Mismatch --> Expected: [%s] missing after cancel", 
                    "Cancel Deletion Check", deletedSupplierId));
        }

        // =========================================================================
        // STEP 2: CONFIRM DELETION
        // =========================================================================
        // Open 3-dots action menu again
        wait.until(ExpectedConditions.elementToBeClickable(threeBotbtn));
        click(threeBotbtn);
        logAction("Clicked 3-Dots Action Button");
        actionPause();

        // Click 'Delete' option again
        wait.until(ExpectedConditions.elementToBeClickable(deletebtn));
        js.executeScript("arguments[0].click();", deletebtn);
        logAction("Clicked 'Delete' Option from Dropdown");
        actionPause();

        // Click 'Confirm' button in popup
        wait.until(ExpectedConditions.elementToBeClickable(deletePopUpconfirmbtn));
        js.executeScript("arguments[0].click();", deletePopUpconfirmbtn);
        logAction("Clicked 'Confirm Delete' Button in Popup");
        Common.waitForElement(3);
        pause(2000); // Wait for DOM table to refresh post-deletion
    }

    public boolean verifySupplierDeletion() {
        logHeader("VERIFYING VENDOR DELETION ON LISTING PAGE");

        if (!isSupplierPresentAfterCancel) {
            logFailure("Cancel check failed previously: Vendor was unexpectedly removed when clicking Cancel!");
        }

        boolean isSupplierStillPresent = isTextPresentOnPage(deletedSupplierId);

        if (!isSupplierStillPresent) {
            logSuccess(String.format("%-28s Matched --> Vendor ID: [%s] | Found on Page: [NO LONGER DISPLAYED]", 
                    "Verify Deletion", deletedSupplierId));
            return true;
        } else {
            logFailure(String.format("%-28s Mismatch --> Vendor ID: [%s] | Found on Page: [STILL DISPLAYED]", 
                    "Verify Deletion", deletedSupplierId));
            return false;
        }
    }


    private boolean isTextPresentOnPage(String targetText) {
        String xpath = String.format("//*[contains(normalize-space(text()), '%s')]", targetText);
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        return elements.stream().anyMatch(e -> e.isDisplayed() 
                && !e.getTagName().equalsIgnoreCase("script") 
                && !e.getTagName().equalsIgnoreCase("style"));
    }
    
    
    //TC-01
    public void validateVendorCreationPage() {
    	
    	adminLogin();
    	
    	verifyCreatedNewVendor();
    	
    	addVendorAddressDetails();
    	
    	addBankDetails();
    	
    	addotherdetails();
    	    	
    }
	
//TC-02
    
	public void validateEditVendorDetails() {
		
		adminLogin();
		
		captureListingDetailsAndGoToPreview();
		
		clickPreviewBack();
		
		openEditVendorForm();
		
		updateBasicVendorDetails();
		
		updateAddressDetails();
		
        updateBankDetails();
        
        updatePanAndSave();
		
	}
	
	  //TC-03 AddContact - Bank in Preview Page
    public void validateAddContactAndBank() {
    	adminLogin();
    	
    	navigatetoVendorPage();
    	
    	navigateToVendorPreviewPage();
        
        addContactDetailsFromPreview();
        
        addBankDetailsFromPreview();
    }	
	
    //TC-04
    public void validateactiveandInactivestatus() {
    	adminLogin();
    	
    	navigatetoVendorPage();
    	        
    	toggleSupplierStatusFromPreview();
    }	
	
	
    //TC-05
    public void validateDeleteVendor() {
    	adminLogin();
    	
    	navigatetoVendorPage();
    	
        deleteSupplierWithCancelAndConfirm();
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
