package objectRepo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;

public abstract class Products_ObjRepo extends BasePage {
	
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
    
    
    @FindBy(xpath = "(//span[normalize-space()='Product'])[1]")
    protected WebElement productMenu;
    
    @FindBy(xpath = "(//li[normalize-space()='Products'])[1]")
    protected WebElement productsSubMenu;
    
    @FindBy(xpath = "(//a[@class='btn btn-primary'])[1]")
    protected WebElement createProductBtn;
    
    @FindBy(xpath = "(//a[normalize-space()='Accessories'])[1]")
    protected WebElement accessoriesTab;
    
    @FindBy(xpath = "(//input[@id='detailed_name'])[1]")
    protected WebElement productNameInput;
    
 // Update XPath to target the button trigger instead of <select>
    @FindBy(xpath = "(//button[@type='button'])[2]")
    protected WebElement brandDropdownBtn;   

    // Generic locator for dropdown option elements
    @FindBy(xpath = "//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a")
    protected List<WebElement> brandDropdownOptions;
    
    @FindBy(xpath = "(//input[@id='sku'])[1]")
    protected WebElement skuInput;
    
    @FindBy(xpath = "(//input[@id='hsn'])[1]")
    protected WebElement hsnInput;
    
    @FindBy(xpath = "(//button[@type='button'])[4]")
    protected WebElement productTypeDropdown;
    
    @FindBy(xpath = "(//button[@type='button'])[5]")
    protected WebElement cateogryDropdown;
    
    @FindBy(xpath = "(//button[@type='button'])[6]")
    protected WebElement collectionDropdown;
    
    @FindBy(xpath = "(//button[@type='button'])[7]")
    protected WebElement neckLineDropdown;
    
    @FindBy(xpath = "(//button[@type='button'])[8]")
    protected WebElement sleeveTypeDropdown;
    
    @FindBy(xpath = "(//button[@type='button'])[9]")
    protected WebElement occasionDropdown;
    
    @FindBy(xpath = "(//button[@type='button'])[10]")
    protected WebElement riseDropdown;
    
    @FindBy(xpath = "(//button[@type='button'])[11]")
    protected WebElement fitDropdown;
    
    @FindBy(xpath = "(//button[@type='button'])[12]")
    protected WebElement sizeChartDropdown;
    
    @FindBy(xpath = "(//button[@type='button'])[14]//span[@class=\"selected-text-display text-placeholder\"]")
    protected WebElement barCodeProtocolDropdown;
    
    @FindBy(xpath = "(//button[normalize-space()='Save & Next'])[1]")
    protected WebElement saveAndNextBtn;
    
    @FindBy(xpath = "(//label[@for='custom_size_1925'])[1]")
    protected WebElement customSizeCheckbox;
    
    @FindBy(xpath = "(//label[@for='return_option_1925'])[1]")
    protected WebElement returnOptionCheckbox;
    
    @FindBy(xpath = "(//label[@for='cancel_option_1925'])[1]")
    protected WebElement cancelOptionCheckbox;
    
    @FindBy(xpath = "(//label[@for='exchange_option_1925'])[1]")
    protected WebElement exchangeOptionCheckbox;
    
    @FindBy(xpath = "(//label[@for='best_seller_1925'])[1]")
    protected WebElement bestSellerCheckbox;
    
    @FindBy(xpath = "(//label[@for='recommended_1925'])[1]")
    protected WebElement recommendedCheckbox;
    
    @FindBy(xpath = "(//label[@for='free_1925'])[1]")
    protected WebElement freeCheckbox;
    
    @FindBy(xpath = "(//label[@for='personalized_letter_1925'])[1]")
    protected WebElement personalizedLetterCheckbox;
    
    @FindBy(xpath = "(//label[@for='express_delivery_1925'])[1]")
    protected WebElement expressDeliveryCheckbox;
    
    @FindBy(xpath = "(//input[@id='delivery_day_1925'])[1]")
    protected WebElement deliveryDaysInput;
    
    @FindBy(xpath = "(//textarea[@id='traffic_data_1925'])[1]")
    protected WebElement trafficDataInput;
    
    
    
    @FindBy(xpath = "(//span[@class='text_button'])[1]")
    protected WebElement aiSuggestionBtn;

    @FindBy(xpath = "//div[@class='ai_suggestion_item']")
    protected List<WebElement> aiSuggestionOptions;

    @FindBy(xpath = "(//button[normalize-space()='Proceed'])[1]")
    protected WebElement aiProceedBtn;

    @FindBy(xpath = "(//textarea[@id='product_description_0'])[1]")
    protected WebElement productDescriptionInput;

    @FindBy(xpath = "(//textarea[@id='composition_care_0'])[1]")
    protected WebElement compositionCareInput;

    @FindBy(xpath = "(//textarea[@id='product_specification_0'])[1]")
    protected WebElement productSpecificationInput;

    @FindBy(xpath = "(//textarea[@id='styling_suggestion_0'])[1]")
    protected WebElement stylingSuggestionInput;

    @FindBy(xpath = "(//textarea[@id='size_fit_0'])[1]")
    protected WebElement sizeFitInput;

    @FindBy(xpath = "(//textarea[@id='return_and_exchange_0'])[1]")
    protected WebElement returnAndExchangeInput;
    
    @FindBy(xpath = "(//span[@class='text_button'])[1]")
    protected WebElement seoAiSuggestionBtn;

    @FindBy(xpath = "(//div[@id='aiSuggestionSeo-options'])[1]//div[@class='ai_suggestion_item']")
    protected List<WebElement> seoAiSuggestionOptions;

     @FindBy(xpath = "(//button[normalize-space()='Proceed'])[1]")
     protected WebElement seoAiProceedBtn;

     @FindBy(xpath = "(//input[@id='seo_meta_title_0'])[1]")
     protected WebElement seoMetaTitleInput;

     @FindBy(xpath = "(//input[@id='seo_meta_key_0'])[1]")
     protected WebElement seoMetaKeyInput;

     @FindBy(xpath = "(//textarea[@id='seo_meta_description_0'])[1]")
     protected WebElement seoMetaDescriptionInput;

     @FindBy(xpath = "(//button[normalize-space()='Save'])[1]")
     protected WebElement seoSaveBtn;
     
     @FindBy(xpath = "(//div[@class='dropdown actions-dropdown-wrapper'])[1]")
     protected WebElement threedotBtn;
     
     @FindBy(xpath = "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
     protected WebElement previewbtn;
     
     @FindBy(xpath= "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[2]")
     protected WebElement editBtn;
     
     @FindBy(xpath = "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[3]")
     protected WebElement deleteBtn;
     
     @FindBy(xpath = "(//button[normalize-space()='Delete Product'])[1]")
     protected WebElement confirmDeleteBtn;
     
     @FindBy(xpath = "(//button[@type='button'])[13]")
     protected WebElement editProtocolDropdown;
     
     
     //TC-PM-03
     
     @FindBy(xpath = "(//button[normalize-space()='Update Stock'])[1]")
     protected WebElement updateStockBtn;
     
     @FindBy(xpath = "(//div[@class='material_action_dropdown js-tag-toggle'])[1]")
     protected WebElement stockThreeDotBtn;
     
     @FindBy(xpath = "(//li[@class='tag_item'])[1]")
     protected WebElement stockHistoryBtn;
     
     @FindBy(xpath = "(//label[normalize-space()='Adjust Stocks'])[1]")
     protected WebElement adjustStocksBtn;
     
     @FindBy(xpath = "(//button[contains(@class,'select-trigger-btn')])[1]")
     protected WebElement selectReasonDropdown;
     
     @FindBy(xpath = "(//textarea[@id='modal-stock-notes'])[1]")
     protected WebElement stockNotesTextArea;
     
     @FindBy(xpath = "(//button[normalize-space()='Update'])[1]")
     protected WebElement updateBtn;
     
     @FindBy(xpath = "(//*[name()='svg'][@class='back_icon'])[1]")
     protected WebElement backBtn;
        
    //TC-05
     @FindBy(xpath = "//span[@class=\"text-danger error_msg detailed_name_error\"]")
     protected WebElement productNameErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg brand_type_error'])[1]")
     protected WebElement brandTypeErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg sku_error'])[1]")
     protected WebElement skuErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg hsn_error'])[1]")
     protected WebElement hsnErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg product_type_error'])[1]")
     protected WebElement productTypeErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg category_error'])[1]")
     protected WebElement categoryErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg protocol_error'])[1]")
     protected WebElement protocolErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg color_error'])[1]")
     protected WebElement colorErrorMsg;
     
     @FindBy(xpath ="(//span[@class='text-danger error_msg size_error'])[1]")
     protected WebElement sizeErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg stock_qty_error mb-3'])[1]")
     protected WebElement alertErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg raw_material_error'])[1]")
     protected WebElement rawMaterailErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg cost_price_error'])[1]")
     protected WebElement costPriceErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg cut_price_error'])[1]")
     protected WebElement cutPriceErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg product_image_error'])[1]")
     protected WebElement productImageErrorMsg;
     
     @FindBy(xpath = "(//span[@class='text-danger error_msg try_along_error mt-2'])[1]")
     protected WebElement tryAlongErrorMsg;
     
     @FindBy(xpath = "(//span[normalize-space()='Product description is required.'])[1]")
     protected WebElement productDescErrorMsg;
     
     @FindBy(xpath = "(//span[normalize-space()='Composition and care is required.'])[1]")
     protected WebElement careErrorMsg;
     
     @FindBy(xpath = "(//span[normalize-space()='Product specification is required.'])[1]")
     protected WebElement specificationErrorMsg;
     
     @FindBy(xpath = "(//span[normalize-space()='Styling suggestion is required.'])[1]")
     protected WebElement StyleErrorMsg;
     
     @FindBy(xpath = "(//span[normalize-space()='Size and fit is required.'])[1]")
     protected WebElement sizeFitErrorMsg;
     
     @FindBy(xpath = "(//span[normalize-space()='Return and Exchange is required.'])[1]")
     protected WebElement returnExchangeErrorMsg;
     
     //TC-PM-06
     @FindBy(xpath = "(//button[normalize-space()='Save Draft'])[1]")
     protected WebElement saveAsDraftBtn;
     
     @FindBy(xpath = "(//button[normalize-space()='Add Variant'])[1]")
     protected WebElement addVariantBtn;
     
     @FindBy(xpath = "//div[@class='arrow-icon cursor-pointer collapsed']//*[name()='svg']")
     protected WebElement secondItemCursor;
     
     
     //Accessories
     
     @FindBy(xpath = "(//a[normalize-space()='Accessories'])[1]")
     protected WebElement accessoreisTab;
    
    
    
}
