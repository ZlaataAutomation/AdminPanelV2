package objectRepo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;

public abstract class ClassificationCategory_ObjRepo extends BasePage {
	
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
    
    @FindBy(xpath = "(//li[normalize-space()='Classifications'])[1]")
    protected WebElement classificationSubMenu;
    
    @FindBy(xpath = "(//span[normalize-space()='Create Category'])[1]")
    protected WebElement createCaotegoryBtn;
    
    @FindBy(xpath = "(//input[@id='category_name'])[1]")
    protected WebElement categoryName;
    
    @FindBy(xpath = "(//input[@id='custom_delivery_date'])[1]")
    protected WebElement customDeliveryDate;
    
    @FindBy(xpath = "(//span[normalize-space()='Select Brand Type...'])[1]")
    protected WebElement brandTypeDropdown;
    
    @FindBy(xpath = "//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a")
    protected List<WebElement> brandDropdownOptions;
    
    @FindBy(xpath = "//div[@id='products_select']//button[@type='button']")
    protected WebElement productDropdown;
    
    @FindBy(xpath = "//ul[@class='select-options-menu']//li[@class='option-item']")
    protected List<WebElement> productOptions;
    
    @FindBy(xpath = "(//input[@id='sort_by'])[1]")
    protected WebElement sortBy;
    
    @FindBy(xpath = "(//textarea[@id='style'])[1]")
    protected WebElement style;
    
    @FindBy(xpath = "(//input[@id='desktopFileInput'])[1]")
    protected WebElement desktopBannerfileInput;
    
    @FindBy(xpath = "(//input[@id='mobiletopFileInput'])[1]")
    protected WebElement mobileBannerFileInput;
    
    @FindBy(xpath = "(//button[@type='button'])[9]")
    protected WebElement googleCategoryDropdwon;
    
    @FindBy(xpath = "(//input[@id='seometatitle'])[1]")
    protected WebElement seoMetaTitle;
    
    @FindBy(xpath = "(//input[@id='seoaltimagetag'])[1]")
    protected WebElement seoALtImageTag;
    
    @FindBy(xpath = "(//input[@id='seometakey'])[1]")
    protected WebElement seoMetaKey;
    
    @FindBy(xpath = "(//input[@id='seotag'])[1]")
    protected WebElement seoTag;
    
    @FindBy(xpath = "(//textarea[@id='Meta-description-first'])[1]")
    protected WebElement seoMetaDescription;
    
    @FindBy(xpath = "(//button[normalize-space()='Save'])[1]")
    protected WebElement saveBtn;
    
    @FindBy(xpath = "(//div[@class='dropdown actions-dropdown-wrapper'])[1]")
    protected WebElement threedotBtn;
    
    @FindBy(xpath = "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
    protected WebElement previewbtn;
    
    @FindBy(xpath= "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[2]")
    protected WebElement editBtn;
    
    @FindBy(xpath = "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[3]")
    protected WebElement deleteBtn;
    
    
    @FindBy(xpath = "//div[@id='brand_type']//button[@type='button']")
    protected WebElement editBrandTypeDropdown;
    
    @FindBy(xpath = "//div[@id='products_select']//button[@type='button']	")
    protected WebElement editProductDropdown;
    
    @FindBy(xpath = "(//a[@id='cancelBackBtn'])[1]")
    protected WebElement backBtn;
    
    @FindBy(xpath = "(//button[normalize-space()='Save Draft'])[1]")
    protected WebElement saveAsDraftBtn;
    
    @FindBy(xpath = "(//button[@id='DeleteConfirmBtn'])[1]")
    protected WebElement confirmDeleteBtn;
    
    
    @FindBy(xpath = "(//li[normalize-space()='Products'])[1]")
    protected WebElement productsSubMenu;
    
    @FindBy(xpath = "(//a[@class='btn btn-primary'])[1]")
    protected WebElement createProductBtn;
    
    @FindBy(xpath = "//div[@id='brand_type']//button[@type='button']")
    protected WebElement brandDropdownBtn;  
    
    @FindBy(xpath = "//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a")
    protected List<WebElement> brandDropdownOption;
    
    @FindBy(xpath = "//div[@id='category']//button[@type='button']")
    protected WebElement cateogryDropdown;
    
    
    @FindBy(xpath = "(//button[normalize-space()='Bulk Action'])[1]")
    protected WebElement bulkActionBtn;
    
    @FindBy(xpath = "(//button[@id='saveCategorySettingBtn'])[1]")
    protected WebElement saveBulkChangesBtn;
    
    
    @FindBy(xpath = "(//button[@class='import-export-btn js-toggle'])[1]")
    protected WebElement importBtnDropdown;
    
    @FindBy(xpath = "(//button[@class='js-option js-custom-import-trigger'])[1]")
    protected WebElement importBtn;
    
    @FindBy(xpath = "(//div[@id='simDropzone'])[1]")
    protected WebElement dragAndDropBox;
    
    @FindBy(xpath = "(//button[normalize-space()='Next'])[1]")
    protected WebElement nextBtn;
    
    @FindBy(xpath = "(//button[normalize-space()='Start Import'])[1]")
    protected WebElement startImportBtn;
    
    @FindBy(xpath = "(//button[@class='js-option'])[1]")
    protected WebElement exportBtn;

    @FindBy(xpath = "(//select[@name='filter_status'])[1]")
    protected WebElement exportStatusDropdown;

    @FindBy(xpath = "(//select[@name='filter_trending'])[1]")
    protected WebElement exportTrendingDropdown;

    @FindBy(xpath = "(//div[@data-section='details'])[1]")
    protected WebElement exportDetailsSection;

    @FindBy(xpath = "(//input[contains(@type,'checkbox')])[1]")
    protected WebElement includeAllCheckBox;

    @FindBy(xpath = "(//button[@id='exportDownloadBtn'])[1]")
    protected WebElement exportDownloadBtn;
    
    @FindBy(xpath = "(//span[normalize-space()='Export History'])[1]")
    protected WebElement exportHistoryPage;

    @FindBy(xpath = "//tbody/tr[1]/td[2]")
    protected WebElement historyFileName;

    @FindBy(xpath = "//tbody/tr[1]/td[6]")
    protected WebElement historyStatus;

    @FindBy(xpath = "(//div[@class='dropdown actions-dropdown-wrapper'])[1]")
    protected WebElement historyThreeDot;

    @FindBy(xpath = "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
    protected WebElement historyDownloadBtn;
    
    
    //Collection Page Elements
    
    
    @FindBy(xpath = "(//a[normalize-space()='Collection'])[1]")
    protected WebElement collectionSubMenu;
        
    @FindBy(xpath = "(//a[@class='btn btn-primary'])[1]")
    protected WebElement createCollectionBtn;
    
    @FindBy(xpath = "(//input[@id='name'])[1]")
    protected WebElement collectionName;
    
    @FindBy(xpath = "(//span[normalize-space()='Select Brand Type'])[1]")
    protected WebElement collectionBrandTypeDropdown;
    
    @FindBy(xpath = "(//input[@id='collections'])[1]")
    protected WebElement collectionSlug;
    
    @FindBy(xpath = "//div[@id='selection_type']//button[@type='button']")
    protected WebElement collectionSectionTypeDropdown;
    
    @FindBy(xpath = "//div[@id='products_select']//button[@type='button']")
    protected WebElement collectionProductDropdown;
    
    @FindBy(xpath = "(//div[@class='custom-control custom-switch ms-3'])[1]//input[@id='toggle_status']")
    protected WebElement collectionstautsToggle;
    
    @FindBy(xpath="(//div[@class='custom-control custom-switch ms-3'])[2]//input[@id='toggle_is_accessories']")
    protected WebElement isAccessoriesToggle;
    
    @FindBy(xpath = "(//div[@class='custom-control custom-switch ms-3'])[3]//input[@id='toggle_is_gift']")
    protected WebElement isGiftToggle;
    
    @FindBy(xpath = "(//div[@class='custom-control custom-switch ms-3'])[4]//input[@id='toggle_is_sale']")
    protected WebElement isSaleToggle;
    
    @FindBy(xpath = "(//div[@class='custom-control custom-switch ms-3'])[5]//input[@id='toggle_is_return']")
    protected WebElement isReturnableToggle;
    
    @FindBy(xpath = "(//div[@class='custom-control custom-switch ms-3'])[6]//input[@id='toggle_is_exchange']")
    protected WebElement isExchangeableToggle;
    
    @FindBy(xpath = "(//div[@class='custom-control custom-switch ms-3'])[7]//input[@id='toggle_is_cancel']")
    protected WebElement isCancelLabelToggle;
    
    @FindBy(xpath = "//div[@id='sale_collection_select']//button[@type='button']")
    protected WebElement saleCollectionDropdown;
    
    @FindBy(xpath = "//div[@id='sale_type']//button[@type='button']")
    protected WebElement saleTypeDropdown;
    
    @FindBy(xpath = "(//textarea[@id='offer_price'])[1]")
    protected WebElement offerPrice;   
    
    @FindBy(xpath = "//div[@id='selection_type']//button[@type='button']")
    protected WebElement editCollectionSectionTypeDropdown;
    
    @FindBy(xpath = "//div[@id='products_select']//button[@type='button']")
    protected WebElement editCollectionProductDropdown;
    
    @FindBy(xpath = "//div[@id='sale_collection_select']//button[@type='button']")
    protected WebElement editSaleCollectionDropdown;
    
    @FindBy(xpath = "//div[@id='sale_type']//button[@type='button']")
    protected WebElement editSaleTypeDropdown;
    
    @FindBy(xpath = "//div[@id='collections']//button[@type='button']")
    protected WebElement collectionDropdown;
    
    
    
    
}
