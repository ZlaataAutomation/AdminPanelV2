package objectRepo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;

public abstract class PurchaseOrder_ObjRepo extends BasePage {
	
	
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
    
    @FindBy(xpath = "(//li[normalize-space()='Purchase Order'])[1]")
    protected WebElement purchaseOrder;
    
    @FindBy(xpath= "(//a[@class='btn btn-primary'])[1]")
    protected WebElement addPurchaseOrder;
    
    @FindBy(xpath = "(//div[@id='supplierSelectBox'])[1]")
    protected WebElement supplierDropdown;
    
    @FindBy(xpath = "//div[@class='w-100 d-flex justify-content-start align-items-center']")
    protected List<WebElement> supplierOptionsList;

    @FindBy(xpath = "(//p[@class='card_coupon_code modal_para_dark info_dt mb-0'])[1]")
    protected WebElement billingAddressBox;

    @FindBy(xpath = "(//p[@class='card_coupon_code modal_para_dark info_dt mb-0'])[2]")
    protected WebElement shippingAddressBox;

    // Assuming the warehouse options are child elements (like divs or spans) inside the warehouseOptionsList container
    @FindBy(xpath = "//div[@id='warehouseOptionsList']//div[@class='option']")
    protected List<WebElement> warehouseOptions;

    @FindBy(xpath = "(//p[@class='card_coupon_code modal_para_dark info_dt mb-0'])[3]")
    protected WebElement deliveryFullAddressText;
    
    @FindBy(xpath = "(//div[@id='deliveryAddressSelectBox'])[1]")
    protected WebElement deliveryAddressDropdown;
    
    @FindBy(xpath = "(//input[@id='expect_delivery_date'])[1]")
    protected WebElement expectedDeilveryDate;
    
    @FindBy(xpath = "(//div[@id='paymentTermSelectBox'])[1]")
    protected WebElement paymentTermsDropdown;
    
    @FindBy(xpath = "(//button[normalize-space()='Add Item'])[1]")
    protected WebElement addItemBtn;
    
    @FindBy(xpath = "//div[@id='rawMaterialModal']//label[1]")
    protected WebElement rawmateraildropdwon;
    
    @FindBy(xpath= "(//button[normalize-space()='Yes, Confirm'])[1]")
    protected WebElement confirmBtn;
    
    @FindBy(xpath = "(//textarea[@placeholder='Enter Supplier Notes'])[1]")
    protected WebElement supplierNotes;
    
    @FindBy(xpath = "(//textarea[@placeholder='Add Description'])[1]")
    protected WebElement termsAndCondition;
    
    @FindBy(xpath = "(//button[@class='btn btn-secondary mr-1'][normalize-space()='Cancel'])[1]")
    protected WebElement cancelBtn;
    
    @FindBy(xpath = "(//button[normalize-space()='Save as Draft'])[1]")
    protected WebElement saveAsDraftBtn;
    
    @FindBy(xpath = "(//button[normalize-space()='Save'])[1]")
    protected WebElement saveBtn;
    
    @FindBy(xpath = "(//div[@class='dropdown actions-dropdown-wrapper'])[1]")
    protected WebElement threedotBtn;
    
    @FindBy(xpath = "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
    protected WebElement previewbtn;
    
    @FindBy(xpath= "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[2]")
    protected WebElement editBtn;
    
    @FindBy(xpath= "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[3]")
    protected WebElement deleteBtn;
    
    
    //TC-04
    
    @FindBy(xpath = "(//button[@class='btn btn-link p-0'])[1]")
    protected WebElement previewThreeDot;
    
    @FindBy(xpath = "(//a[normalize-space()='Cancel Items'])[1]")
    protected WebElement cancelItemsBtn;
    
    @FindBy(xpath= "(//input[@name='raw_material_ids[]'])[1]")
    protected WebElement selectItemCheckBox;
    
    @FindBy(xpath = "(//textarea[@id='cancel_reason'])[1]")
    protected WebElement cancelReasonTextArea;
    
    @FindBy(xpath = "(//button[@id='btn_confirm_cancel_items'])[1]")
    protected WebElement confirmCancelItemsBtn;
    
    @FindBy(xpath= "(//a[normalize-space()='Cancel Order'])[1]")
    protected WebElement cancelOrderBtn;
    
    @FindBy(xpath = "(//textarea[@id='order_cancel_reason'])[1]")
    protected WebElement cancelOrderreasonTextArea;
    
    @FindBy(xpath = "(//button[normalize-space()='Yes, Cancel'])[1]")
    protected WebElement confirmCancelOrderBtn;
    
    @FindBy(xpath = "(//p[@class='m-0'][normalize-space()='Cancelled'])[1]")
    protected WebElement cancelledStatus1;
    
    @FindBy(xpath = "(//p[@class='m-0'][normalize-space()='Cancelled'])[2]")
    protected WebElement cancelledStatus2;
    
    
    //TC-06
    
    @FindBy(xpath = "(//div[normalize-space()='Please select a supplier'])[1]")
    protected WebElement supplierValidationMsg;
    
    @FindBy(xpath = "(//div[normalize-space()='Please select a delivery address'])[1]")
    protected WebElement deliveryAddressValidationMsg;
    
    @FindBy(xpath = "(//div[normalize-space()='Please select expected delivery date'])[1]")
    protected WebElement expectedDeliveryDateValidationMsg;
    
    @FindBy(xpath = "(//div[normalize-space()='Please select the payment terms'])[1]")
    protected WebElement paymentTermsValidationMsg;
    
    @FindBy(xpath = "(//div[@class='noty_body'])[1]")
    protected WebElement notyBodyMessage;
    
    
    
    
    
    
    
    
    
    
    
}
