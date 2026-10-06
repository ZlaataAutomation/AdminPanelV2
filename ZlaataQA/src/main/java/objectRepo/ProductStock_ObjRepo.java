package objectRepo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;

public abstract class ProductStock_ObjRepo extends BasePage {

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

 	@FindBy(xpath= "(//span[normalize-space()='Inventory'])[1]")
 	protected WebElement inventory;
 	
 	@FindBy(xpath = "(//li[normalize-space()='Product Stocks'])[1]")
 	protected WebElement productStockModule;
 	
 	@FindBy(xpath = "(//button[@onclick=\"openUpdateStockModal('top')\"])[1]")
 	protected WebElement updatetopStockButton;
 	
 	@FindBy(xpath="(//button[normalize-space()='Update Stock (Bottom)'])[1]")
 	protected WebElement updatebottomStockButton;
 	
 	@FindBy(xpath = "//label[@class='label']//*[name()='svg']")
 	protected WebElement previewEditbutton;
 	
 	@FindBy(xpath = "(//li[@onclick=\"openLowStockModal('top')\"])[1]")
 	protected WebElement setLowstockButton;
 	
 	@FindBy(xpath = "(//li[normalize-space()='Set Low Stock Alert (Bottom)'])[1]")
 	protected WebElement setLowstockBottomButton;
 	
 	@FindBy(xpath = "(//li[normalize-space()='Stock History'])[1]")
 	protected WebElement stockHistoryButton;
     
     @FindBy(xpath = "//table//tbody//tr[contains(@class,'even') or contains(@class,'odd')]")
     protected List<WebElement> productDataRows;
     
     @FindBy(xpath = "//i[contains(@class,'bi-three-dots-vertical')]")
     protected List<WebElement> editButtons;
     
     @FindBy(xpath = "//span[contains(normalize-space(),'Preview')]")
     protected List<WebElement> previewIcons;
     
     
     
     @FindBy(xpath = "(//button[@class='import-export-btn js-toggle'])[1]")
     protected WebElement importExportBtnDropdown;
     
     @FindBy(xpath = "(//button[@class='js-option'])[1]")
     protected WebElement exportBtn;
     
     @FindBy(xpath = "(//label[@class='section-toggle'])[1]")
     protected WebElement includeCheckBox;
     
     @FindBy(xpath ="(//button[normalize-space()='Download'])[1]")
     protected WebElement downloadBtn;
     
     @FindBy(xpath = "(//div[@class='noty_body'])[1]")
     protected WebElement generatingMsg;
     
     @FindBy(xpath = "(//span[normalize-space()='Export History'])[1]")
     protected WebElement exportHistoryPage;
     
     @FindBy(xpath = "//tbody/tr[1]/td[2]")
     protected WebElement fileName;
     
     @FindBy(xpath = "(//td)[7]")
     protected WebElement status;
     
     @FindBy(xpath = "(//div[@class='dropdown actions-dropdown-wrapper'])[1]")
     protected WebElement threeDot;

     @FindBy(xpath = "//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
     protected WebElement exportDownloadBtn;
 		
}
