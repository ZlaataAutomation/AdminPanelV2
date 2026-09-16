package objectRepo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;

public abstract class ManufactureOrder_ObjRepo extends BasePage{
	
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
    
    @FindBy(xpath = "(//span[normalize-space()='Inventory'])[1]")
    protected WebElement inventory;
    
    @FindBy(xpath = "(//li[normalize-space()='Manufacture Order'])[1]")
    protected WebElement manufactureOrder;
    
    @FindBy(xpath = "(//a[@class='btn btn-primary'])[1]")
    protected WebElement addManufactureOrderBtn;
    
    @FindBy(xpath = "(//a[@class='custom_product'])[1]")
    protected WebElement addCustomProductBtn;
    
    @FindBy(xpath = "(//div[normalize-space()='Search the vendors'])[1]")
    protected WebElement vendorDropdown;
    
    @FindBy(xpath = "//div[@class='option']")
    protected List<WebElement> vendorOptionsList;
    
    @FindBy(xpath = "(//input[@placeholder='Select date'])[1]")
    protected WebElement expectedDeliveryDateCalendar;
    
    @FindBy(xpath = "(//div[normalize-space()='Search the Address'])[1]")
    protected WebElement deliveryAddressDropdown;
    
    @FindBy(xpath = "(//div[@class='options_box'])[2]//div[@class='option']")
    protected List<WebElement> deliveryAddressOptionsList;
    
    @FindBy(xpath = "(//input[@id='product_name'])[1]")
    protected WebElement productNameInput;
    
    @FindBy(xpath = "(//input[@id='making_cost'])[1]")
    protected WebElement makingCostInput;
    
    @FindBy(xpath = "(//div[@class='tag_input js-tag-toggle'])[1]")
    protected WebElement sizeTagsDropdown;
    
    @FindBy(xpath = "(//div[@class='text-left pl-2'])[1]")
    protected WebElement rawMaterialDropdown;
    
    @FindBy(xpath = "(//button[normalize-space()='Add Raw Material'])[1]")
    protected WebElement addRawMaterialBtn;
    
    @FindBy(xpath = "(//button[normalize-space()='Add Product'])[1]")
    protected WebElement addProductBtn;
    
    @FindBy(xpath = "//p[@class='d-flex justify-content-start align-items-center m-0']")
    protected WebElement totalCostText;
    
    @FindBy(xpath = "(//textarea[@placeholder='Add Notes'])[1]")
    protected WebElement addNotesInput;
    
    @FindBy(xpath = "(//textarea[@placeholder='Add Description'])[1]")
    protected WebElement addDescriptionInput;
    
    @FindBy(xpath = "(//a[normalize-space()='Cancel'])[1]")
    protected WebElement cacelBtn;
    
    @FindBy(xpath = "(//button[normalize-space()='Save as Draft'])[1]")
    protected WebElement saveAsDraftBtn;
    
    @FindBy(xpath = "(//button[normalize-space()='Save'])[1]")
    protected WebElement saveBtn;
    
    @FindBy(xpath = "(//i[@class='bi bi-three-dots-vertical'])[1]")
    protected WebElement threeDotBtn;
    
    @FindBy(xpath = "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
    protected WebElement previewbtn;
    
    @FindBy(xpath= "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[2]")
    protected WebElement editBtn;
    
    @FindBy(xpath= "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[3]")
    protected WebElement deleteBtn;
    
    @FindBy(xpath = "(//button[normalize-space()='Mark as Issued'])[1]")
    protected WebElement markAsIssuedBtn;

    @FindBy(xpath = "(//button[normalize-space()='Yes, Issued'])[1]")
    protected WebElement confirmMarkAsIssuedBtn;

    @FindBy(xpath = "(//p[@class='m-0'])[1]")
    protected WebElement detailsPageStatusLocator;

    @FindBy(xpath = "(//a[@class='back_icon text-dark mr-2'])[1]")
    protected WebElement backBtn;

    @FindBy(xpath = "(//td)[7]")
    protected WebElement listingPageStatusLocator;
    
    @FindBy(xpath = "(//button[normalize-space()='Save Changes'])[1]")
    protected WebElement saveChangesBtn;
    
    @FindBy(xpath = "(//div[@class='material_action_dropdown js-tag-toggle'])[1]")
    protected WebElement previewThreeDotBtn;
   
    @FindBy(xpath = "(//li[normalize-space()='Cancel Items'])[1]")
    protected WebElement cancelItemsBtn;
    
    @FindBy(xpath = "(//input[@id='select_all_cancel_items'])[1]")
    protected WebElement allItemsDetailsCheckBox;

    @FindBy(xpath = "(//textarea[@id='cancel_reason'])[1]")
    protected WebElement cancelReasonInput;
    
    @FindBy(xpath = "(//button[@id='btn_confirm_cancel_items'])[1]")
    protected WebElement yesConfirmBtn;
    
    @FindBy(xpath = "(//span[@class='status-chip inactive'])[1]")
    protected WebElement previewCancelledStatus; 
    		
    
    
    
    
}
