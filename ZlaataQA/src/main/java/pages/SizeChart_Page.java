package pages;

import java.time.Duration;
import java.util.Random;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import objectRepo.Product_Archive_ObjRepo;
import objectRepo.SizeChart_ObjRepo;
import utils.Common;

public class SizeChart_Page extends SizeChart_ObjRepo {
	
	public SizeChart_Page(WebDriver driver) 
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(this.driver, this);
	}
	
	public void adminLogin() {
	
	AdminLogin_Page login= new AdminLogin_Page(driver);
	login.adminLoginApp();
	
	}
	
	
	 public void navigatetoSizeChartsection() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        wait.until(ExpectedConditions.visibilityOf(product));
	        new Actions(driver).moveToElement(product).perform();

	        wait.until(ExpectedConditions.visibilityOf(sizeChart));
	        click(sizeChart);
	    }
	 
	 public void navigatetoProductsection() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        wait.until(ExpectedConditions.visibilityOf(product));
	        new Actions(driver).moveToElement(product).perform();

	        wait.until(ExpectedConditions.visibilityOf(productsection));
	        click(productsection);
	    } 

	 String sizeChartName;
	 public void createSizeChart() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    Common.waitForElement(2);
		    // Click Add Size Chart
		    wait.until(ExpectedConditions.elementToBeClickable(
		            By.cssSelector("a[bp-button='create']"))).click();
		    Common.waitForElement(2);
		    // Generate a random 3-digit number and create the size chart name
		     sizeChartName = "Automation" + (100 + new Random().nextInt(900));

		    // Enter Size Chart Name
		    WebElement nameField = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(By.id("name")));
		    nameField.clear();
		    nameField.sendKeys(sizeChartName);

		    // Click Top
		    wait.until(ExpectedConditions.elementToBeClickable(
		            By.cssSelector("button.top_button[data-type='top']"))).click();
		    Common.waitForElement(2);
		    // Select Size S
		    wait.until(ExpectedConditions.elementToBeClickable(
		            By.cssSelector("button.size_chip[data-size-name='S']"))).click();
		    Common.waitForElement(2);
		    // Enter Header Name
		    WebElement headerField = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("input.header_name_input[name='top_headers[]']")));
		    headerField.clear();
		    headerField.sendKeys("auto size");

		    // Enter Size Value: 45
		    WebElement valueField = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("input.row_value_input[name='top_values[inch][29][]']")));
		    valueField.clear();
		    valueField.sendKeys("45");
		    Common.waitForElement(3);
		    // Click Save
		    wait.until(ExpectedConditions.elementToBeClickable(
		            By.id("saveBtn"))).click();

		    System.out.println("Size Chart created: " + sizeChartName);
		} 
	 
	 
	 
	 public void validateSizeChartAndPreview() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    Common.waitForElement(2);
		    // Validate the first row name in the Size Chart listing
		    WebElement firstRow = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("tbody tr:first-child")
		            )
		    );

		    WebElement firstRowName = firstRow.findElement(
		            By.cssSelector("td:nth-child(1) span")
		    );

		    String actualName = firstRowName.getText().trim();

		    Assert.assertEquals(
		            "Size Chart name mismatch in the first row!",
		            sizeChartName,
		            actualName
		    );

		    System.out.println("PASS: Size Chart name matches in listing: " + actualName);

		    // Click the Preview action in the first row
		    Common.waitForElement(2);

		    // 3. Click the first three-dots menu
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
		 // Validate the Size Chart name on the Preview page
		    WebElement previewName = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath("//div[contains(@class,'material_wrapper')]" +
		                            "[.//p[normalize-space()='Size Chart Name']]" +
		                            "//p[contains(@class,'modal_para_dark')]")
		            )
		    );

		    String actualPreviewName = previewName.getText().trim();

		    Assert.assertEquals(
		            "Size Chart name mismatch on Preview page!",
		            sizeChartName,
		            actualPreviewName
		    );

		    System.out.println("PASS: Preview name matches: " + actualPreviewName);
		}
	 String sizeChartName1;
	 public void assignProductToSizeChart() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    Common.waitForElement(2);
		    // Copy the first row Size Chart name
		    WebElement firstRow = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("tbody tr:first-child")
		            )
		    );

		     sizeChartName1 = firstRow.findElement(
		            By.cssSelector("td:nth-child(1) span")
		    ).getText().trim();

		    System.out.println("Size Chart Name: " + sizeChartName1);

		    // Click the Edit option in the first row
		    Common.waitForElement(2);
		    // 3. Click the first three-dots menu
		    WebElement firstThreeDots = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("(//i[contains(@class,'bi-three-dots-vertical')])[1]")
		            )
		    );

		    firstThreeDots.click();

		    Common.waitForElement(2);

		    // 4. Click Edit
		    WebElement edit = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[2]")
		            )
		    );

		    edit.click();

		    Common.waitForElement(2);

		    // Search for the existing product
		    WebElement productSearch = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("addproducts")
		            )
		    );

		    productSearch.clear();
		    productSearch.sendKeys("Test by Auto");
		    Common.waitForElement(2);
		    // Select the product by pressing Enter
		    productSearch.sendKeys(Keys.ENTER);
		    
		    Common.waitForElement(2);
		    JavascriptExecutor js = (JavascriptExecutor) driver;
		    js.executeScript("window.scrollTo({top: 0, behavior: 'smooth'});");
		    Common.waitForElement(2);
		    WebElement saveButton = wait.until(
		            ExpectedConditions.presenceOfElementLocated(By.id("saveBtn"))
		    );

		    js.executeScript(
		            "arguments[0].scrollIntoView({block: 'center'});",
		            saveButton
		    );

		    wait.until(ExpectedConditions.visibilityOf(saveButton));
		    js.executeScript("arguments[0].click();", saveButton);

		    System.out.println(
		            "Product assignment submitted for Size Chart: " + sizeChartName
		    );
		} 
	 
	 public void validateAssignedSizeChart() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    Common.waitForElement(2);
		    // Search for the product
		    WebElement searchField = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.id("text-filter-name")
		            )
		    );

		    searchField.clear();
		    searchField.sendKeys("Test by Auto");
		    Common.waitForElement(4);
		    // Wait for the product row to appear
		    wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//tbody//tr[.//td//*[normalize-space()='Test by Auto']]")
		    ));
		    Common.waitForElement(2);
		    // 3. Click the first three-dots menu
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
		    
		    // Verify the assigned Size Chart name
		    WebElement sizeChartNameElement = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.xpath(
		                            "//div[contains(@class,'detail-item')]" +
		                            "[.//div[normalize-space()='Size Chart']]" +
		                            "//span[contains(@class,'badge-text')]"
		                    )
		            )
		    );

		    String actualSizeChartName = sizeChartNameElement.getText().trim();

		    Assert.assertEquals(
		            "Assigned Size Chart name mismatch!",
		            sizeChartName1,
		            actualSizeChartName
		    );

		    System.out.println(
		            "PASS: Product 'Test by Auto' has Size Chart: " + actualSizeChartName
		    );
		}
	 
	 public void deleteSizeChartAndValidate() {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		    Common.waitForElement(2);
		    // Copy the first row Size Chart name
		    WebElement firstRow = wait.until(
		            ExpectedConditions.visibilityOfElementLocated(
		                    By.cssSelector("tbody tr:first-child")
		            )
		    );

		    String deletedSizeChartName = firstRow.findElement(
		            By.cssSelector("td:nth-child(1) span")
		    ).getText().trim();

		    System.out.println("Size Chart to delete: " + deletedSizeChartName);

		    Common.waitForElement(2);
		    // 3. Click the first three-dots menu
		    WebElement firstThreeDots = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("(//i[contains(@class,'bi-three-dots-vertical')])[1]")
		            )
		    );

		    firstThreeDots.click();

		    Common.waitForElement(2);

		    // 4. Click Delete
		    WebElement delete = wait.until(
		            ExpectedConditions.elementToBeClickable(
		                    By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[3]")
		            )
		    );

		    delete.click();

		    Common.waitForElement(2);

		    // Click Confirm in the delete confirmation modal
		    wait.until(ExpectedConditions.elementToBeClickable(
		            By.id("DeleteConfirmBtn")
		    )).click();
		    Common.waitForElement(2);
		    // Wait until the deleted Size Chart disappears from the listing
		    wait.until(ExpectedConditions.invisibilityOfElementLocated(
		            By.xpath("//tbody//tr[td[1]//span[normalize-space()='" 
		                    + deletedSizeChartName + "']]")
		    ));

		    System.out.println(
		            "PASS: Size Chart deleted successfully: " + deletedSizeChartName
		    );
		}
	 
//TC-01	 
	 public void validatesizeChartCreation() {
		 
		 adminLogin();
		 
		 navigatetoSizeChartsection();
		 
		 createSizeChart();
		 
		 validateSizeChartAndPreview();
	 }
	 
//TC-02	 
	 public void assignproductInSizecghart() {
		 
		 adminLogin();
		 
		 navigatetoSizeChartsection();
		 
		 assignProductToSizeChart();
		 
		 navigatetoProductsection();
		 
		 validateAssignedSizeChart();
		 
	 }
	 
	 
//Tc-03	 
	 public void validatedeleteSizeChart() {

		 adminLogin();
		 
		 navigatetoSizeChartsection();
		 
		 deleteSizeChartAndValidate();
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
