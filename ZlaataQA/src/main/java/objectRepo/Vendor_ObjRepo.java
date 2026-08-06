package objectRepo;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;

public abstract class Vendor_ObjRepo extends BasePage {
	public void waitFor(WebElement el) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(ExpectedConditions.elementToBeClickable(el));
    }
    public void type(WebElement el, String value) {
        waitFor(el); el.clear(); el.sendKeys(value);
    }
    public void click(WebElement el) {
        waitFor(el); el.click();
    }
    
    
    @FindBy(name = "email")
 	protected WebElement adminEmail;
 	
 	@FindBy(id = "password")
 	protected WebElement adminPassword;
 	
 	@FindBy(xpath = "//button[@type='submit']")
 	protected WebElement adminLogin;
	
	
	
    @FindBy(xpath = "(//span[normalize-space()='Inventory'])[1]")
    protected WebElement inventory;
    
    @FindBy(xpath = "(//li[normalize-space()='Supplier'])[1]")
    protected WebElement Supplier;
    
    @FindBy(xpath ="//a[@class='btn btn-primary'][normalize-space()='Add vendor']")
    protected WebElement createvendorbtn;
    
    @FindBy(xpath = "(//input[@id='company_name'])[1]")
    protected WebElement companyName;
    
    @FindBy(xpath = "(//select[@id='supplier_type'])[1]")
    protected WebElement supplierTypeDropdwon;
    
    @FindBy(xpath = "(//input[@id='email_address'])[1]")
    protected WebElement emailId;
    
    @FindBy(xpath = "(//input[@id='phone_number'])[1]")
    protected WebElement companyNumber;
    
    @FindBy(xpath = "(//div[@class='tag_input js-tag-toggle'])[1]")
    protected WebElement tagDropdwon;
    
    @FindBy(xpath = "//span[normalize-space()='button']")
    protected WebElement tagOption;
    
    @FindBy(xpath = "(//input[@placeholder='Enter Name'])[1]")
    protected WebElement contactName; 
    
    @FindBy(xpath = "(//input[@name='email[]'])[1]")
    protected WebElement contactemailId;
    
    @FindBy(xpath = "(//input[@name='phone[]'])[1]")
    protected WebElement phoneNumber; 
    
    @FindBy(xpath = "(//button[normalize-space()='Add Contact'])[1]")
    protected WebElement addContactbtn;  
    
    @FindBy(xpath = "(//textarea[@id='reg_adrs_1'])[1]")
    protected WebElement AddressLine1;   
    
    @FindBy(xpath = "//textarea[@id='reg_adrs_2']")
    protected WebElement AddressLine2;   
    
    @FindBy(xpath = "(//select[@id='reg_adrs_country'])[1]")
    protected WebElement countrydropdown;
    
    @FindBy(xpath = "(//input[@id='reg_adrs_pincode'])[1]")
    protected WebElement pincode;
    
    @FindBy(xpath = "(//input[@id='confirm_address'])[1]")
    protected WebElement sameAddressCheckbox;
    
    @FindBy(xpath = "(//input[@placeholder='Enter Account Holder Name'])[1]")
    protected WebElement accountHolderName;
    
    @FindBy(xpath = "(//input[@placeholder='Search bank name...'])[1]")
    protected WebElement bankNamedropdown;
    
    @FindBy(xpath = "//li[normalize-space()='State Bank of India']")
    protected WebElement stateBankOfIndiaOption;
    
    @FindBy(xpath = "(//input[@name='account_number[]'])[1]")
    protected WebElement accountNumber;
    
    @FindBy(xpath = "(//input[@name='re_account_number[]'])[1]")
    protected WebElement reEnterAccNumber;
    
    @FindBy(xpath = "(//input[@placeholder='Enter your IFSC Code'])[1]")
    protected WebElement ifscCode;
    
    @FindBy(xpath = "(//button[normalize-space()='Add bank account'])[1]")
    protected WebElement addBankAccountbtn;
    
    @FindBy(xpath = "(//button[normalize-space()='Save & Next'])[1]")
    protected WebElement saveandNextbtn;
    
    @FindBy(xpath = "(//input[@id='pan_number'])[1]")
    protected WebElement panCardNumber;
    
    @FindBy(xpath = "(//button[normalize-space()='Save'])[1]")
    protected WebElement saveBtn;
 
    @FindBy(xpath = "(//i[@class='las la-ellipsis-v'])[1]")
    protected WebElement threeBotbtn;
    
    @FindBy(xpath = "(//span[contains(text(),'Preview')])[1]")
    protected WebElement previewbtn;

    @FindBy(xpath = "//body/div[@class='main_wrapper']/main[@class='main_content']/div[@class='container-fluid']/div[@class='create_wizard_wrapper supplier_details']/div[@id='pills-tabContent']/div[@id='basic-info-home-tab']/div[@class='row']/div[@class='col-md-12 col-lg-5 supplier_contact_details']/div[@class='form_content_wrapper']/div/dl/div[1]//dd")
    protected WebElement previewContactName;

    @FindBy(xpath = "//body[1]/div[1]/main[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/dl[1]/div[2]/dd[1]/div[1]")
    protected WebElement previewContactEmail;

    @FindBy(xpath = "//body[1]/div[1]/main[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/dl[1]/div[3]/dd[1]/div[1]")
    protected WebElement previewPhoneNumber;

    @FindBy(xpath = "//div[@class='row mt-4']//div[@class='col-md-12 col-lg-7']//dl//div[1]")
    protected WebElement previewAddress;

    @FindBy(xpath = "//div[@class='row mt-4']//div[@class='supplier_info_grid']//div[1]//dd")
    protected WebElement previewBankName; // Index 1 is Bank Name

    @FindBy(xpath = "//div[@class='row mt-4']//div[@class='supplier_info_grid']//div[2]//dd")
    protected WebElement previewAccountHolder; // Index 2 is Account Holder

    @FindBy(xpath = "//div[@class='row mt-4']//div[@class='supplier_info_grid']//div[3]//dd")
    protected WebElement previewAccountNumber; // Index 3 is Account Number

    @FindBy(xpath = "//div[@class='row mt-4']//div[@class='supplier_info_grid']//div[4]//dd")
    protected WebElement previewIfscCode; // Index 4 remains IFSC Code
	
    @FindBy(xpath = "((//a)[@bp-button='update'])[1]")
    protected WebElement editBtn;
    
    @FindBy(xpath = "(//*[name()='svg'][@class='back_icon'])[1]")
    protected WebElement previewBackBtn;
    
    @FindBy(xpath = "(//label[@class='tag_option'])[2]")
    protected WebElement tagOption2;
    
    @FindBy(xpath = "(//button[normalize-space()='Update & Next'])[1]")
    protected WebElement updateAndNext;
    
    @FindBy(xpath = "(//button[normalize-space()='Update'])[1]")
    protected WebElement updateBtn;
    
    
    @FindBy(xpath = "(//button[normalize-space()='Add Contact'])[1]")
    protected WebElement previewaddContactbtn;
    
    @FindBy(xpath = "(//button[normalize-space()='Add Bank'])[1]")
    protected WebElement previewaddBankbtn;
    
    @FindBy(xpath = "(//button[normalize-space()='Add Contact'])[1]")
    protected WebElement addContactBtn;

    @FindBy(xpath = "(//input[@id='contactNameInput'])[1]")
    protected WebElement popupContactNameInput;

    @FindBy(xpath = "(//input[@id='contactEmailInput'])[1]")
    protected WebElement popupContactEmailInput;

    @FindBy(xpath = "(//input[@id='contactPhoneInput'])[1]")
    protected WebElement popupContactPhoneInput;

    @FindBy(xpath = "(//button[@type='submit'][normalize-space()='Save'])[1]")
    protected WebElement contactPopupSaveBtn;

    // Add Bank Locators
    @FindBy(xpath = "(//button[normalize-space()='Add Bank'])[1]")
    protected WebElement addBankBtn;

    @FindBy(xpath = "(//input[@placeholder='Enter Account Holder Name'])[1]")
    protected WebElement popupAccountHolderInput;

    @FindBy(xpath = "(//input[@placeholder='Search bank name...'])[1]")
    protected WebElement popupBankSearchInput;

    @FindBy(xpath = "//li[contains(text(),'ICICI Bank') or contains(span/text(),'ICICI Bank')] | //div[contains(@class,'option') and contains(text(),'ICICI')]")
    protected WebElement iciciBankOption;

    @FindBy(xpath = "(//input[@name='account_number[]'])[1]")
    protected WebElement popupAccountNumberInput;

    @FindBy(xpath = "(//input[@name='re_account_number[]'])[1]")
    protected WebElement popupReAccountNumberInput;

    @FindBy(xpath = "(//input[@placeholder='Enter your IFSC Code'])[1]")
    protected WebElement popupIfscCodeInput;

    @FindBy(xpath = "(//button[@type='submit'][normalize-space()='Save'])[2]")
    protected WebElement bankPopupSaveBtn;
    
 // ==========================================
    // DYNAMIC CONTACT DISPLAY LOCATORS (Fetches Latest/Last Appended Contact)
    // ==========================================
    
    // Selects the last contact card container dynamically
    @FindBy(xpath = "(//main//dl[contains(@class,'') or ancestor::div[contains(@class,'')]])[last()]")
    protected WebElement lastContactCardContainer;

    // Contact details inside the LAST contact card block
    @FindBy(xpath = "(//main//div[2]//dl[last()]//dd)[1] | (//main//dl[last()]//div[1]/dd)[1]")
    protected WebElement previewDisplayedContactName;

    @FindBy(xpath = "(//main//div[2]//dl[last()]//dd)[2] | (//main//dl[last()]//div[2]/dd)[1]")
    protected WebElement previewDisplayedContactEmail;

    @FindBy(xpath = "(//main//div[2]//dl[last()]//dd)[3] | (//main//dl[last()]//div[3]/dd)[1]")
    protected WebElement previewDisplayedContactPhone;

    // ==========================================
    // DYNAMIC BANK DISPLAY LOCATORS (Fetches Latest/Last Appended Bank)
    // ==========================================

    @FindBy(xpath = "(//main//div[2]/div[2]//dl[last()]//div[1]/dd)[1] | /html/body/div[1]/main/div/div[2]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/dl[last()]/div[1]/dd")
    protected WebElement previewDisplayedBankName;

    @FindBy(xpath = "(//main//div[2]/div[2]//dl[last()]//div[2]/dd)[1] | /html/body/div[1]/main/div/div[2]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/dl[last()]/div[2]/dd")
    protected WebElement previewDisplayedAccountHolder;

    @FindBy(xpath = "(//main//div[2]/div[2]//dl[last()]//div[3]/dd)[1] | /html/body/div[1]/main/div/div[2]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/dl[last()]/div[3]/dd")
    protected WebElement previewDisplayedAccountNumber;

    @FindBy(xpath = "(//main//div[2]/div[2]//dl[last()]//div[4]/dd)[1] | /html/body/div[1]/main/div/div[2]/div[1]/div[1]/div[2]/div[2]/div[1]/div[2]/dl[last()]/div[4]/dd")
    protected WebElement previewDisplayedIfscCode;
    
    
    @FindBy(xpath = "(//i[@class='la la-ellipsis-v'])[1]")
    protected WebElement previewThreedot;

    @FindBy(xpath = "(//a[normalize-space()='Mark as Inactive'])[1] | (//a[contains(text(),'Inactive') or contains(text(),'Active')])[1]")
    protected WebElement markAsInactivebtn;

    @FindBy(xpath = "(//p[@class='font_14 status-text'])[1]")
    protected WebElement supplierStatusText;
    
    
    
    
    
    @FindBy(xpath = "//tbody/tr[1]/td[1]")
    protected WebElement firstRowSupplierId;

    @FindBy(xpath = "//tbody/tr[1]/td[2]")
    protected WebElement firstRowSupplierName;

    @FindBy(xpath = "(//a[@class='dropdown-item'][normalize-space()='Delete'])[2]")
    protected WebElement deletebtn;

    @FindBy(xpath = "(//button[@id='vendorDeleteConfirmBtn'])[1]")
    protected WebElement deletePopUpconfirmbtn;

    @FindBy(xpath = "(//button[@class='btn_with_outline'][normalize-space()='Cancel'])[last()]")
    protected WebElement deletePopupCancelBtn;
	
	
	
	
	

}
