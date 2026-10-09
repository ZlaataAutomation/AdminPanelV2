package pages;

import java.time.Duration;

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

import objectRepo.Product_Archive_ObjRepo;
import objectRepo.WareHouse_ObjRepo;
import utils.Common;

public class ProductArchive_Page extends Product_Archive_ObjRepo {
	
	public ProductArchive_Page(WebDriver driver) 
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(this.driver, this);
	}
	
	public void adminLogin() {
	
	AdminLogin_Page login= new AdminLogin_Page(driver);
	login.adminLoginApp();
	
	}
	
	
	 public void navigatetoProductsection() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        wait.until(ExpectedConditions.visibilityOf(product));
	        new Actions(driver).moveToElement(product).perform();

	        wait.until(ExpectedConditions.visibilityOf(Products));
	        click(Products);
	    }
	 
	 public void navigatetoProductArchivesection() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        wait.until(ExpectedConditions.visibilityOf(product));
	        new Actions(driver).moveToElement(product).perform();

	        wait.until(ExpectedConditions.visibilityOf(ProductArchive));
	        click(ProductArchive);
	    } 
	 
	 
	 String productName;
	 String sku;
	 String hsn;
	 String archiveReason;
	 public void moveFirstActiveProductToArchive() throws InterruptedException {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		    // Select Active status
		    WebElement statusDropdown = wait.until(
		            ExpectedConditions.elementToBeClickable(By.id("filter_status"))
		    );

		    Select selectStatus = new Select(statusDropdown);
		    selectStatus.selectByValue("1");
		    Common.waitForElement(2);
		    // Wait for the product listing to refresh
		    wait.until(ExpectedConditions.presenceOfElementLocated(
		            By.cssSelector("tbody tr")
		    ));

		    // Get the first product row
		    WebElement firstRow = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("tbody tr:first-child")
		            )
		    );

		    // Copy Product Name
		     productName = firstRow.findElement(
		            By.cssSelector("td:nth-child(1)")
		    ).getText().trim();

		    // Copy SKU
		     sku = firstRow.findElement(
		            By.cssSelector("td:nth-child(2)")
		    ).getText().trim();

		    // Copy HSN
		     hsn = firstRow.findElement(
		            By.cssSelector("td:nth-child(3)")
		    ).getText().trim();

		    System.out.println("Product Name: " + productName);
		    System.out.println("SKU: " + sku);
		    System.out.println("HSN: " + hsn);
		    WebElement searchName = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("text-filter-name")
		            )
		    );

		    searchName.click();
		    Common.waitForElement(2);
		 // Select first row checkbox
		    WebElement firstCheckbox = firstRow.findElement(
		            By.cssSelector("input.crud_bulk_actions_line_checkbox")
		    );
		    if (!firstCheckbox.isSelected()) {
		        ((JavascriptExecutor) driver).executeScript(
		                "arguments[0].click();", firstCheckbox
		        );
		    }
		    Common.waitForElement(2);
Thread.sleep(2000);
//Locate the bulk archive dropdown
WebElement archiveDropdown = wait.until(
     ExpectedConditions.presenceOfElementLocated(
             By.id("bulk_archive")
     )
);

//Print current state for debugging
System.out.println(
     "Disabled attribute: " + archiveDropdown.getAttribute("disabled")
);

//Click the dropdown
((JavascriptExecutor) driver).executeScript(
     "arguments[0].click();", archiveDropdown
);

//Wait for the Move to Archive option
WebElement moveToArchive = wait.until(
     ExpectedConditions.visibilityOfElementLocated(
             By.cssSelector("a[data-name='move_to_archive']")
     )
);

moveToArchive.click();
		    Common.waitForElement(2);

		 // Enter archive reason
		     archiveReason = "Product moved to archive for testing";

		    WebElement reasonField = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("textarea")
		            )
		    );

		    reasonField.clear();
		    reasonField.sendKeys(archiveReason);

		    // Click Confirm
		    WebElement confirmButton = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("(//button[contains(normalize-space(),'Confirm')])[2]")
		            )
		    );

		    confirmButton.click();
		    Common.waitForElement(4);
		    System.out.println("Product moved to archive successfully.");
		} 
	 
	 public void validateProductDisplayedInArchive() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		    // Get the first row from Product Archive
		    WebElement firstRow = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("tbody tr:first-child")
		            )
		    );

		    // Get actual product details
		    String actualProductName = firstRow.findElement(
		            By.cssSelector("td:nth-child(1) span[title]")
		    ).getText().trim();

		    String actualSku = firstRow.findElement(
		            By.cssSelector("td:nth-child(2)")
		    ).getText().trim();

		    String actualHsn = firstRow.findElement(
		            By.cssSelector("td:nth-child(3)")
		    ).getText().trim();

		    // Validate product details
		    Assert.assertEquals("Product Name mismatch", productName, actualProductName);
		    Assert.assertEquals("SKU mismatch", sku, actualSku);
		    Assert.assertEquals("HSN mismatch", hsn, actualHsn);

		    System.out.println("PASS: Product is displayed correctly in Product Archive.");
		}
	 

public void validateArchivedProductPreview() {
	 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	// 2. Click Three Dots
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

	    System.out.println("Product Archive  Preview clicked.");

	 // Wait for the Archived Reason
	    WebElement archivedReason = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath(
	                            "//p[normalize-space()='Archived Reason']" +
	                            "/following::p[contains(@class,'modal_para_dark')][1]"
	                    )
	            )
	    );

    // Get the actual archived reason
    String actualArchiveReason = archivedReason.getText().trim();

    System.out.println("Expected Archive Reason: " + archiveReason);
    System.out.println("Actual Archive Reason: " + actualArchiveReason);

    // Validate archived reason
    Assert.assertEquals(
            "Archived reason mismatch!",
            archiveReason,
            actualArchiveReason
    );

    System.out.println("PASS: Archived reason matches successfully.");
}

String archivedProductName;
public void restoreArchivedProduct() {
	 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		// 2. Click Three Dots
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

	    System.out.println("Product Archive  Preview clicked.");
    // Copy Product Name
    WebElement productNameElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(
                            "//div[contains(@class,'material_wrapper')]" +
                            "[.//p[normalize-space()='Product Name']]" +
                            "//p[contains(@class,'modal_para_dark')]"
                    )
            )
    );

     archivedProductName = productNameElement.getText().trim();

    System.out.println("Archived Product Name: " + archivedProductName);

    // Verify Status is Archived
    WebElement statusElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(
                            "//div[contains(@class,'material_wrapper')]" +
                            "[.//p[normalize-space()='Status']]" +
                            "//p[contains(@class,'modal_para_dark')]"
                    )
            )
    );

    String actualStatus = statusElement.getText().trim();

    Assert.assertEquals(
            "Product status is not Archived!",
            "Archived",
            actualStatus
    );

    System.out.println("PASS: Product status is Archived.");

    // Click Restore Product
    WebElement restoreButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Restore Product']")
            )
    );

    restoreButton.click();
    Common.waitForElement(2);

    // Click Confirm in the restore confirmation modal
    WebElement confirmButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.xpath(
                            "//button[@type='submit' " +
                            "and normalize-space()='Confirm']"
                    )
            )
    );

    confirmButton.click();
    Common.waitForElement(2);

    // Verify Product Archive page heading is displayed
    WebElement pageHeading = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(
                            "//h1[@bp-section='page-heading' " +
                            "and normalize-space()='Product archive']"
                    )
            )
    );

    Assert.assertTrue(
            "Product Archive page heading is not displayed!",
            pageHeading.isDisplayed()
    );

    System.out.println("PASS: Product restored successfully.");
    System.out.println("Product Name: " + archivedProductName);
    System.out.println("Product Archive page heading is displayed.");
}
public void validateArchivedProductInSearch() {
	 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    // Search for the archived product
    WebElement searchName = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.id("text-filter-name")
            )
    );

    searchName.clear();
    searchName.sendKeys(archivedProductName);

    // Verify the exact full product name
    WebElement productNameElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(
                            "//tbody//span[@title='" + archivedProductName + "']"
                    )
            )
    );

    String actualProductName = productNameElement.getText().trim();

    Assert.assertEquals(
            "Product name mismatch in Product Archive!",
            archivedProductName,
            actualProductName
    );

    System.out.println(
            "PASS: Exact product name found: " + actualProductName
    );
}
//TC-01	 
	 public void validateProductArchive() throws InterruptedException {
		 
		 adminLogin();
		 
		 navigatetoProductsection();
		 
		 moveFirstActiveProductToArchive();
		 
		 navigatetoProductArchivesection();
		 
		 validateProductDisplayedInArchive();
		 
		 validateArchivedProductPreview();
		 
	 }
	 
	 
//TC-02	 
	 public void validateRestoreArchivePRoduct() {
		 
		 adminLogin();

		 navigatetoProductArchivesection();

		 restoreArchivedProduct();
		 
		 navigatetoProductsection();
		 
		 validateArchivedProductInSearch();
		 
	 }
	 
//TC-03	 
	 public void validateBulkRestoreArchiveProduct() {
		 adminLogin();

		 navigatetoProductArchivesection();
		 
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
