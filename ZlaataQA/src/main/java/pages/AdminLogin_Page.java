package pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import manager.FileReaderManager;
import objectRepo.AdminLogin_ObjRepo;
import objectRepo.Raw_Material_ObjRepo;
import utils.Common;

public class AdminLogin_Page extends Raw_Material_ObjRepo {

    public AdminLogin_Page(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(this.driver, this);
    }

    public void adminLoginApp() {

    	driver.get(FileReaderManager.getInstance().getConfigReader().getApplicationAdminUrl());
        type(adminEmail, FileReaderManager.getInstance().getJsonReader().getValueFromJson("AdminName"));
        type(adminPassword, FileReaderManager.getInstance().getJsonReader().getValueFromJson("AdminPassword"));
        click(adminLogin);
        System.out.println("✅ Admin Login Successful");
        
        Common.waitForElement(2);

	}

    public void closeDebugBarIfPresent() {
        try {
            List<WebElement> debugCloseBtn = driver.findElements(
                    By.xpath("//a[contains(@class,'phpdebugbar-close-btn')]")
            );
            if (!debugCloseBtn.isEmpty() && debugCloseBtn.get(0).isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", debugCloseBtn.get(0));
                System.out.println("🛑 PHP Debugbar closed");
            }
        } catch (Exception e) {
            // Intentionally ignored – debugbar not present
        }
    }

    @Override
    public boolean verifyExactText(WebElement ele, String expectedText) {
        return false;
    }

    @Override
    public WebDriver gmail(String browserName) {
        return null;
    }

    @Override
    protected boolean isAt() {
        return false;
    }
}