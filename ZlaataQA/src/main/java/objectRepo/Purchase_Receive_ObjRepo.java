package objectRepo;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;

public abstract class Purchase_Receive_ObjRepo extends BasePage {
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
    
    @FindBy(xpath = "(//li[normalize-space()='Purchase Receive'])[1]")
    protected WebElement purchaseReceive;
}
