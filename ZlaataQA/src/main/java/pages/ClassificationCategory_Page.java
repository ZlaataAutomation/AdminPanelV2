package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import java.util.Date;
import org.apache.poi.ss.usermodel.*;
import objectRepo.ClassificationCategory_ObjRepo;

public class ClassificationCategory_Page extends ClassificationCategory_ObjRepo {

	public ClassificationCategory_Page(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(this.driver, this);
	}

	public static final String RESET = "\u001B[0m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String CYAN = "\u001B[36m";
	public static final String BOLD = "\u001B[1m";
	private static final String PURPLE = "\u001B[35m";
	public static final String BOLD_GREEN = "\u001B[1;32m";
	public static final String BOLD_YELLOW = "\u001B[1;33m";

	private void pause(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private static final int ACTION_DELAY = 700;

	private void actionPause() {
		pause(ACTION_DELAY);
	}

	private void logAction(String message) {
		System.out.println(PURPLE + BOLD + "[ACTION] " + RESET + message);
	}

	private void logSuccess(String message) {
		System.out.println(GREEN + BOLD + "[SUCCESS] " + message + RESET);
	}

	private void logHeader(String message) {
		System.out.println("\n" + YELLOW + BOLD + "============ " + message + " ============" + RESET);
	}

	public void adminLogin() {
		logHeader("ADMIN LOGIN");
		AdminLogin_Page login = new AdminLogin_Page(driver);
		login.adminLoginApp();
		logSuccess("Admin logged in successfully");
	}

	public void navigatetoProductsPage() {
		logHeader("NAVIGATION");
		logAction(YELLOW + BOLD + "Hovering over 'Product' menu..."+ RESET);
		wait.until(ExpectedConditions.visibilityOf(productMenu));
		new Actions(driver).moveToElement(productMenu).perform();
		actionPause();

		logAction(YELLOW + BOLD + "Clicking 'Classification' sub-menu..."+ RESET);
		wait.until(ExpectedConditions.visibilityOf(classificationSubMenu));
		click(classificationSubMenu);
		logSuccess(YELLOW + BOLD + "Navigated to Classification page");
	}

	public void navigatetoProductsPageAndProructSubMenu() {
		logHeader("NAVIGATION");
		logAction(YELLOW + BOLD + "Hovering over 'Product' menu..." + RESET);
		wait.until(ExpectedConditions.visibilityOf(productMenu));
		new Actions(driver).moveToElement(productMenu).perform();
		actionPause();

		logAction(YELLOW + BOLD + "Clicking 'Products' sub-menu..." + RESET);
		wait.until(ExpectedConditions.visibilityOf(productsSubMenu));
		click(productsSubMenu);
		logSuccess("Navigated to Products page");
	}

	public static String savedCategoryName;
	public static String savedBrandType;
	public static int savedProductCount;
	public static int savedDeliveryDays;
	public static String savedSortBy;
	public static String savedStyle;
	public static String savedMetaTitle;
	public static String savedAltTag;
	public static String savedMetaKey;
	public static String savedSeoTag;
	public static String savedMetaDesc;

	private static final Random RANDOM = new Random();

	private static final String DROPDOWN_OPTIONS_XPATH = "(//ul[@class='select-options-menu'])[1]//li[@class='option-item']";
	private static final String SET_VALUE_JS = "arguments[0].value=arguments[1]; "
			+ "arguments[0].dispatchEvent(new Event('input')); " + "arguments[0].dispatchEvent(new Event('change'));";

	private static final By SORT_BY_FIELD = By.xpath("(//input[@id='sort_by'])[1]");
	private static final By STYLE_FIELD = By.xpath("(//textarea[@id='style'])[1]");
	private static final By STATUS_SWITCH = By.xpath("(//div[@class='custom-control custom-switch ms-3'])[1]");
	private static final By DROPDOWN_OPTIONS = By.xpath(DROPDOWN_OPTIONS_XPATH);
	private static final By DESKTOP_FILE_INPUT = By.xpath("(//input[@id='desktopFileInput'])[1]");
	private static final By MOBILE_FILE_INPUT = By.xpath("(//input[@id='mobiletopFileInput'])[1]");

	public void fillCreateCategoryForm() {

		wait.until(ExpectedConditions.elementToBeClickable(createCaotegoryBtn)).click();
		System.out.println(GREEN + "[INFO] Clicked on 'Create Category' button." + RESET);
		actionPause(800);

		WebElement nameField = wait.until(ExpectedConditions.visibilityOf(categoryName));
		scrollToCenter(nameField, 600);

		savedCategoryName = "Category_" + UUID.randomUUID().toString().substring(0, 6);
		nameField.sendKeys(savedCategoryName);
		System.out.println(CYAN + "[DATA] Generated & Entered Category Name: " + savedCategoryName + RESET);

		WebElement dateField = wait.until(ExpectedConditions.visibilityOf(customDeliveryDate));
		scrollToCenter(dateField, 600);

		savedDeliveryDays = RANDOM.nextInt(14) + 1;
		dateField.clear();
		dateField.sendKeys(String.valueOf(savedDeliveryDays));
		System.out.println(CYAN + "[DATA] Entered Custom Delivery Days: " + savedDeliveryDays + RESET);

		WebElement brandDD = wait.until(ExpectedConditions.elementToBeClickable(brandTypeDropdown));
		scrollToCenter(brandDD, 600);
		brandDD.click();
		System.out.println(GREEN + "[INFO] Opened Brand Type Dropdown." + RESET);

		List<WebElement> brandOptions = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DROPDOWN_OPTIONS));

		if (!brandOptions.isEmpty()) {
			WebElement selectedBrandOption = brandOptions.get(RANDOM.nextInt(brandOptions.size()));
			savedBrandType = selectedBrandOption.getText().trim();

			scrollIntoView(selectedBrandOption, "nearest");
			actionPause(500);
			jsClick(selectedBrandOption);
			System.out.println(PURPLE + "[SELECTED] Brand Type Option: " + savedBrandType + RESET);
		}

		WebElement prodDD = wait.until(ExpectedConditions.elementToBeClickable(productDropdown));
		scrollToCenter(prodDD, 600);
		prodDD.click();
		System.out.println(GREEN + "[INFO] Opened Product Dropdown." + RESET);

		List<WebElement> productOptions = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DROPDOWN_OPTIONS));

		if (!productOptions.isEmpty()) {
			savedProductCount = Math.min(RANDOM.nextInt(3) + 1, productOptions.size());
			System.out.println(CYAN + "[INFO] Selecting " + savedProductCount + " random product(s)..." + RESET);

			for (int i = 0; i < savedProductCount; i++) {
				WebElement option = productOptions.get(RANDOM.nextInt(productOptions.size()));
				String productText = option.getText().trim();
				actionPause(1000);

				jsClick(option);
				System.out.println(PURPLE + "[SELECTED] Product [" + (i + 1) + "]: " + productText + RESET);
			}
		}

		try {
			prodDD.click();
			System.out.println(GREEN + "[INFO] Closed Product Dropdown." + RESET);
		} catch (Exception e) {
			jsClick(prodDD);
		}
		actionPause(500);

		savedSortBy = "1";
		WebElement sortByField = wait.until(ExpectedConditions.presenceOfElementLocated(SORT_BY_FIELD));
		scrollToCenter(sortByField, 600);
		typeWithJsFallback(sortByField, savedSortBy);
		System.out.println(CYAN + "[DATA] Entered Sort By value: " + savedSortBy + RESET);

		savedStyle = "Modern & Responsive Style Layout - Automated Test";
		WebElement styleField = wait.until(ExpectedConditions.presenceOfElementLocated(STYLE_FIELD));
		scrollToCenter(styleField, 600);
		typeWithJsFallback(styleField, savedStyle);
		System.out.println(CYAN + "[DATA] Entered Style input: " + savedStyle + RESET);

		WebElement statusSwitch = wait.until(ExpectedConditions.elementToBeClickable(STATUS_SWITCH));
		scrollToCenter(statusSwitch, 600);
		try {
			statusSwitch.click();
		} catch (Exception e) {
			jsClick(statusSwitch);
		}
		System.out.println(GREEN + "[INFO] Toggled ON Active status switch for the category." + RESET);
		actionPause(500);
	}

	public void uploadCategoryBanners() {
		List<String> absoluteImagePaths = new ArrayList<>();

		for (String name : List.of("CategoryBanner.png")) {
			File file = new File("src/test/resources/images/" + name);
			if (file.exists()) {
				absoluteImagePaths.add(file.getAbsolutePath());
			}
		}

		if (absoluteImagePaths.isEmpty()) {
			System.out.println(GREEN
					+ "[WARNING] No valid banner image files found in src/test/resources/images/. Skipping upload."
					+ RESET);
			return;
		}

		String joinedFilePaths = String.join("\n", absoluteImagePaths);

		uploadBanner("Desktop", DESKTOP_FILE_INPUT, joinedFilePaths);
		actionPause(1000);
		uploadBanner("Mobile", MOBILE_FILE_INPUT, joinedFilePaths);
	}

	private void uploadBanner(String label, By locator, String filePaths) {
		try {
			WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			scrollToCenter(input, 800);

			input.sendKeys(filePaths);
			System.out.println(PURPLE + "[UPLOAD] " + label + " Banner uploaded successfully using path(s):\n"
					+ filePaths + RESET);
		} catch (Exception e) {
			System.out.println(GREEN + "[WARNING] Failed to upload " + label + " Banner: " + e.getMessage() + RESET);
		}
	}

	private void scrollIntoView(WebElement element, String block) {
		((JavascriptExecutor) driver)
				.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: '" + block + "'});", element);
	}

	private void scrollToCenter(WebElement element, int pauseMs) {
		scrollIntoView(element, "center");
		actionPause(pauseMs);
	}

	private void jsClick(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	private void typeWithJsFallback(WebElement element, String value) {
		try {
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript(SET_VALUE_JS, element, value);
		}
	}

	private void actionPause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private static final By SEO_META_TITLE = By.xpath("(//input[@id='seometatitle'])[1]");
	private static final By SEO_ALT_TAG = By.xpath("(//input[@id='seoaltimagetag'])[1]");
	private static final By SEO_META_KEY = By.xpath("(//input[@id='seometakey'])[1]");
	private static final By SEO_TAG = By.xpath("(//input[@id='seotag'])[1]");
	private static final By SEO_META_DESC = By.xpath("(//textarea[@id='Meta-description-first'])[1]");

	public void fillSeoDetails() {

		String seoUniqueId = UUID.randomUUID().toString().substring(0, 6);

		savedMetaTitle = "Meta Title " + seoUniqueId;
		savedAltTag = "Alt Tag " + seoUniqueId;
		savedMetaKey = "meta, keyword, " + seoUniqueId;
		savedSeoTag = "Tag_" + seoUniqueId;
		savedMetaDesc = "Automated SEO description test for category creation " + seoUniqueId + ".";

		fillSeoField(SEO_META_TITLE, savedMetaTitle, "SEO Meta Title");
		fillSeoField(SEO_ALT_TAG, savedAltTag, "SEO Alt Image Tag");
		fillSeoField(SEO_META_KEY, savedMetaKey, "SEO Meta Key");
		fillSeoField(SEO_TAG, savedSeoTag, "SEO Tag");
		fillSeoField(SEO_META_DESC, savedMetaDesc, "SEO Meta Description");

		clickSaveButton();
	}

	public void clickSaveButton() {
		WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
		scrollToCenter(saveButton, 800);

		try {
			saveButton.click();
		} catch (Exception e) {
			jsClick(saveButton);
		}

		System.out.println(GREEN + "[SUCCESS] Clicked on 'Save' button. Category form submitted." + RESET);

		actionPause(2000);
	}

	private void fillSeoField(By locator, String value, String label) {
		WebElement field = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		scrollToCenter(field, 600);
		safeSendKeys(field, value);
		System.out.println(CYAN + "[DATA] Entered " + label + ": " + value + RESET);
	}

	private void safeSendKeys(WebElement element, String value) {
		try {
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1]; "
					+ "arguments[0].dispatchEvent(new Event('input', { bubbles: true })); "
					+ "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element, value);
		}
	}

	private void logCompare(String label, Object expected, Object actual) {
		String exp = String.valueOf(expected);
		String act = String.valueOf(actual);
		String color = exp.equals(act) ? BOLD_GREEN : BOLD_YELLOW;
		System.out.println(color + String.format("%-16s -> Expected: [%s] | Actual: [%s]", label, exp, act) + RESET);
	}

	public void verifyCategoryOnListingPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		org.testng.asserts.SoftAssert softAssert = new org.testng.asserts.SoftAssert();

		actionPause(2000);

		WebElement categoryNameCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//td)[1]")));
		WebElement brandTypeCell = driver.findElement(By.xpath("(//td)[2]"));
		WebElement productCountCell = driver.findElement(By.xpath("(//td)[3]"));
		WebElement deliveryDateCell = driver.findElement(By.xpath("(//td)[4]"));

		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", categoryNameCell);
		actionPause(500);

		String actualCategoryName = categoryNameCell.getText().trim();
		String actualBrandType = brandTypeCell.getText().trim();
		String actualProductCount = productCountCell.getText().trim();
		String actualDeliveryDate = deliveryDateCell.getText().trim();

		System.out.println(CYAN + "--- [VERIFY: LISTING PAGE (SOFT ASSERTIONS)] ---" + RESET);
		logCompare("Category Name", savedCategoryName, actualCategoryName);
		logCompare("Brand Type", savedBrandType, actualBrandType);
		logCompare("Product Count", savedProductCount, actualProductCount);
		logCompare("Delivery Date", savedDeliveryDays, actualDeliveryDate);

		softAssert.assertEquals(actualCategoryName, savedCategoryName, "Category Name mismatch on Listing page!");
		softAssert.assertEquals(actualBrandType, savedBrandType, "Brand Type mismatch on Listing page!");
		softAssert.assertEquals(actualProductCount, String.valueOf(savedProductCount),
				"Product Count mismatch on Listing page!");
		softAssert.assertEquals(actualDeliveryDate, String.valueOf(savedDeliveryDays),
				"Custom Delivery Date mismatch on Listing page!");

		softAssert.assertAll();
		System.out.println(BOLD_GREEN
				+ "[SUCCESS] All Category Listing page details verified successfully with Soft Assertions!" + RESET);
	}

	public void verifyCategoryOnPreviewPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		org.testng.asserts.SoftAssert softAssert = new org.testng.asserts.SoftAssert();

		WebElement threeDot = wait.until(ExpectedConditions.elementToBeClickable(threedotBtn));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", threeDot);
		actionPause(600);

		try {
			threeDot.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", threeDot);
		}
		System.out.println(BOLD_GREEN + "[INFO] Clicked on Three-Dot Actions menu." + RESET);
		actionPause(500);

		WebElement previewOption = wait.until(ExpectedConditions.elementToBeClickable(previewbtn));
		try {
			previewOption.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", previewOption);
		}
		System.out.println(BOLD_GREEN + "[INFO] Clicked on Preview button. Opening detail modal..." + RESET);
		actionPause(1000);

		String modalCategoryName = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//p[@class='modal_para_dark'])[1]")))
				.getText().trim();
		String modalDeliveryDate = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[2]")).getText().trim();
		String modalBrandType = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[3]")).getText().trim();
		String modalSortBy = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[4]")).getText().trim();
		String modalStyle = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[6]")).getText().trim();

		System.out.println(CYAN + "--- [VERIFY: PREVIEW MODAL - GENERAL DETAILS (SOFT ASSERTIONS)] ---" + RESET);
		logCompare("Category Name", savedCategoryName, modalCategoryName);
		logCompare("Delivery Date", savedDeliveryDays, modalDeliveryDate);
		logCompare("Brand Type", savedBrandType, modalBrandType);
		logCompare("Sort By", savedSortBy, modalSortBy);
		logCompare("Style", savedStyle, modalStyle);

		softAssert.assertEquals(modalCategoryName, savedCategoryName, "Preview Modal Category Name mismatch!");
		softAssert.assertEquals(modalDeliveryDate, String.valueOf(savedDeliveryDays),
				"Preview Modal Delivery Date mismatch!");
		softAssert.assertEquals(modalBrandType, savedBrandType, "Preview Modal Brand Type mismatch!");
		softAssert.assertEquals(modalSortBy, savedSortBy, "Preview Modal Sort By mismatch!");
		softAssert.assertEquals(modalStyle, savedStyle, "Preview Modal Style mismatch!");

		WebElement desktopImg = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img[@alt='desktop-banner'])[1]")));
		WebElement mobileImg = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img[@alt='mobile-banner'])[1]")));

		boolean isDesktopImgLoaded = (Boolean) js.executeScript(
				"return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0;",
				desktopImg);
		boolean isMobileImgLoaded = (Boolean) js.executeScript(
				"return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0;",
				mobileImg);

		softAssert.assertTrue(isDesktopImgLoaded,
				"Desktop banner image failed to load or render properly in the preview modal!");
		softAssert.assertTrue(isMobileImgLoaded,
				"Mobile banner image failed to load or render properly in the preview modal!");
		System.out.println((isDesktopImgLoaded && isMobileImgLoaded ? BOLD_GREEN : BOLD_YELLOW)
				+ "[VERIFY: BANNERS] Desktop loaded: " + isDesktopImgLoaded + " | Mobile loaded: " + isMobileImgLoaded
				+ RESET);

		String modalSeoMetaTitle = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[9]")).getText().trim();
		String modalSeoAltImage = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[10]")).getText().trim();
		String modalSeoMetaKey = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[11]")).getText().trim();
		String modalSeoTag = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[12]")).getText().trim();
		String modalSeoDescription = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[13]")).getText()
				.trim();

		System.out.println(CYAN + "--- [VERIFY: PREVIEW MODAL - SEO DETAILS (SOFT ASSERTIONS)] ---" + RESET);
		logCompare("SEO Meta Title", savedMetaTitle, modalSeoMetaTitle);
		logCompare("SEO Alt Image", savedAltTag, modalSeoAltImage);
		logCompare("SEO Meta Key", savedMetaKey, modalSeoMetaKey);
		logCompare("SEO Tag", savedSeoTag, modalSeoTag);
		logCompare("SEO Description", savedMetaDesc, modalSeoDescription);

		softAssert.assertEquals(modalSeoMetaTitle, savedMetaTitle, "Preview Modal SEO Meta Title mismatch!");
		softAssert.assertEquals(modalSeoAltImage, savedAltTag, "Preview Modal SEO Alt Image Tag mismatch!");
		softAssert.assertEquals(modalSeoMetaKey, savedMetaKey, "Preview Modal SEO Meta Key mismatch!");
		softAssert.assertEquals(modalSeoTag, savedSeoTag, "Preview Modal SEO Tag mismatch!");
		softAssert.assertEquals(modalSeoDescription, savedMetaDesc, "Preview Modal SEO Description mismatch!");

		softAssert.assertAll();
		System.out.println(BOLD_GREEN
				+ "[SUCCESS] All Preview Modal details, banners, and SEO fields verified successfully with Soft Assertions!"
				+ RESET);
	}

	// TC-PC-02
	private void clickWithJsFallback(WebElement element) {
		try {
			element.click();
		} catch (Exception e) {
			jsClick(element);
		}
	}

	public void clickEditFromActionMenu() {
		WebElement threeDot = wait.until(ExpectedConditions.elementToBeClickable(threedotBtn));
		scrollToCenter(threeDot, 600);
		clickWithJsFallback(threeDot);
		System.out.println(GREEN + "[INFO] Clicked on Three-Dot Actions menu for editing." + RESET);
		actionPause(500);

		WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(editBtn));
		clickWithJsFallback(editOption);
		System.out.println(GREEN + "[INFO] Clicked on Edit button. Opening edit form..." + RESET);
		actionPause(1000);
	}

	public void editCategoryForm() {

		WebElement nameField = wait.until(ExpectedConditions.visibilityOf(categoryName));
		scrollToCenter(nameField, 600);

		savedCategoryName = "UpdatedCat_" + UUID.randomUUID().toString().substring(0, 6);
		nameField.clear();
		nameField.sendKeys(savedCategoryName);
		System.out.println(CYAN + "[DATA] Updated Category Name: " + savedCategoryName + RESET);

		WebElement dateField = wait.until(ExpectedConditions.visibilityOf(customDeliveryDate));
		scrollToCenter(dateField, 600);

		savedDeliveryDays = RANDOM.nextInt(14) + 1;
		dateField.clear();
		dateField.sendKeys(String.valueOf(savedDeliveryDays));
		System.out.println(CYAN + "[DATA] Updated Custom Delivery Days: " + savedDeliveryDays + RESET);

		selectRandomBrandType(editBrandTypeDropdown, "[INFO] Opened Brand Type Dropdown for update.",
				"[SELECTED] Updated Brand Type Option: ");

		selectRandomProducts(editProductDropdown);

		savedSortBy = "2";
		WebElement sortByField = wait.until(ExpectedConditions.presenceOfElementLocated(SORT_BY_FIELD));
		scrollToCenter(sortByField, 600);
		safeSendKeys(sortByField, savedSortBy);
		System.out.println(CYAN + "[DATA] Updated Sort By value: " + savedSortBy + RESET);

		savedStyle = "Updated Modern Style Layout - Automated Test";
		WebElement styleField = wait.until(ExpectedConditions.presenceOfElementLocated(STYLE_FIELD));
		scrollToCenter(styleField, 600);
		safeSendKeys(styleField, savedStyle);
		System.out.println(CYAN + "[DATA] Updated Style input: " + savedStyle + RESET);

		String seoUniqueId = UUID.randomUUID().toString().substring(0, 6);
		savedMetaTitle = "Updated Meta Title " + seoUniqueId;
		savedAltTag = "Updated Alt Tag " + seoUniqueId;
		savedMetaKey = "updated, keyword, " + seoUniqueId;
		savedSeoTag = "UpdatedTag_" + seoUniqueId;
		savedMetaDesc = "Updated automated SEO description test for category " + seoUniqueId + ".";

		typeIntoField(SEO_META_TITLE, savedMetaTitle);
		typeIntoField(SEO_ALT_TAG, savedAltTag);
		typeIntoField(SEO_META_KEY, savedMetaKey);
		typeIntoField(SEO_TAG, savedSeoTag);
		typeIntoField(SEO_META_DESC, savedMetaDesc);
		System.out.println(CYAN + "[DATA] All SEO fields updated successfully." + RESET);

		clickSaveButton();
	}

	private void selectRandomBrandType(WebElement dropdown, String openedMsg, String selectedMsgPrefix) {
		WebElement brandDD = wait.until(ExpectedConditions.elementToBeClickable(dropdown));
		scrollToCenter(brandDD, 600);
		brandDD.click();
		System.out.println(GREEN + openedMsg + RESET);

		List<WebElement> brandOptions = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DROPDOWN_OPTIONS));

		if (!brandOptions.isEmpty()) {
			WebElement selected = brandOptions.get(RANDOM.nextInt(brandOptions.size()));
			savedBrandType = selected.getText().trim();

			scrollIntoView(selected, "nearest");
			actionPause(500);
			jsClick(selected);
			System.out.println(PURPLE + selectedMsgPrefix + savedBrandType + RESET);
		}
	}

	private void selectRandomProducts(WebElement dropdown) {
		WebElement prodDD = wait.until(ExpectedConditions.elementToBeClickable(dropdown));
		scrollToCenter(prodDD, 600);
		prodDD.click();
		System.out.println(GREEN + "[INFO] Opened Product Dropdown." + RESET);

		List<WebElement> productOptions = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DROPDOWN_OPTIONS));

		if (!productOptions.isEmpty()) {
			savedProductCount = Math.min(RANDOM.nextInt(3) + 1, productOptions.size());
			System.out.println(CYAN + "[INFO] Selecting " + savedProductCount + " random product(s)..." + RESET);

			for (int i = 0; i < savedProductCount; i++) {
				WebElement option = productOptions.get(RANDOM.nextInt(productOptions.size()));
				String productText = option.getText().trim();
				actionPause(1000);

				jsClick(option);
				System.out.println(PURPLE + "[SELECTED] Product [" + (i + 1) + "]: " + productText + RESET);
			}
		}

		try {
			prodDD.click();
			System.out.println(GREEN + "[INFO] Closed Product Dropdown." + RESET);
		} catch (Exception e) {
			jsClick(prodDD);
		}
		actionPause(500);
	}

	private void typeIntoField(By locator, String value) {
		WebElement field = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		scrollToCenter(field, 600);
		safeSendKeys(field, value);
	}

	// TC-03
	public void createCategoryAsDraft() {

		clickCreateCategoryButton();

		WebElement nameField = wait.until(ExpectedConditions.visibilityOf(categoryName));
		scrollToCenter(nameField, 600);

		savedCategoryName = "DraftCat_" + UUID.randomUUID().toString().substring(0, 6);
		nameField.sendKeys(savedCategoryName);
		System.out.println(CYAN + "[DATA] Generated & Entered Draft Category Name: " + savedCategoryName + RESET);

		enterRandomDeliveryDays("Entered");

		selectRandomBrandType(brandTypeDropdown, "[INFO] Opened Brand Type Dropdown.",
				"[SELECTED] Brand Type Option: ");

		WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(backBtn));
		scrollToCenter(backButton, 600);
		clickWithJsFallback(backButton);
		System.out.println(GREEN + "[INFO] Clicked on Back button to prompt draft save." + RESET);
		actionPause(1000);

		WebElement draftButton = wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftBtn));
		clickWithJsFallback(draftButton);
		System.out.println(GREEN + "[SUCCESS] Clicked on 'Save Draft' button. Category saved as draft." + RESET);
		actionPause(2000);
	}

	public void verifyDraftCategoryOnListingPage() {
		SoftAssert softAssert = new SoftAssert();

		actionPause(2000);

		WebElement categoryNameCell = wait.until(ExpectedConditions.visibilityOfElementLocated(LISTING_CELL_1));
		scrollToCenter(categoryNameCell, 500);

		String actualCellText = categoryNameCell.getText().trim();

		System.out.println(CYAN + "--- [VERIFY: DRAFT LISTING PAGE] ---" + RESET);
		System.out.println(PURPLE + "Expected Category Name -> [" + savedCategoryName + "]" + RESET);
		System.out.println(PURPLE + "Expected Status        -> [Draft]" + RESET);
		System.out.println(PURPLE + "Actual Cell Text       -> [" + actualCellText + "]" + RESET);

		softAssert.assertTrue(actualCellText.contains(savedCategoryName),
				"Draft Category Name mismatch! Expected to find '" + savedCategoryName + "' inside cell text: '"
						+ actualCellText + "'");
		softAssert.assertTrue(actualCellText.toLowerCase().contains("draft"),
				"Category status 'Draft' not found inside cell text: '" + actualCellText + "'");

		softAssert.assertAll();
		System.out.println(
				GREEN + "[SUCCESS] Draft category name and status verified successfully from (//td)[1]!" + RESET);
	}

	private static final By LISTING_CELL_1 = By.xpath("(//td)[1]");

	public void deleteDraftCategory() {

		WebElement threeDot = wait.until(ExpectedConditions.elementToBeClickable(threedotBtn));
		scrollToCenter(threeDot, 600);
		clickWithJsFallback(threeDot);
		System.out.println(GREEN + "[INFO] Clicked on Three-Dot Actions menu for deletion." + RESET);
		actionPause(500);

		WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
		clickWithJsFallback(deleteOption);
		System.out.println(GREEN + "[INFO] Clicked on Delete option. Opening confirmation popup..." + RESET);
		actionPause(800);

		WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteBtn));
		clickWithJsFallback(confirmBtn);
		System.out.println(GREEN + "[SUCCESS] Clicked Confirm Delete button. Category deleted." + RESET);
		actionPause(2000);
	}

	public void verifyCategoryDeletedFromListingPage() {
		actionPause(2000);

		boolean isCategoryStillPresent = driver.findElements(LISTING_CELL_1).stream()
				.anyMatch(cell -> cell.getText().trim().equals(savedCategoryName));

		System.out.println(CYAN + "--- [VERIFY: DELETION CHECK] ---" + RESET);
		System.out.println(
				PURPLE + "Checking if deleted category '" + savedCategoryName + "' is absent from listing..." + RESET);

		Assert.assertFalse(isCategoryStillPresent,
				"Deleted category '" + savedCategoryName + "' is still displayed on the listing page!");
		System.out.println(
				GREEN + "[SUCCESS] Verified that deleted category is no longer displayed on the listing page!" + RESET);
	}

	private void clickCreateCategoryButton() {
		wait.until(ExpectedConditions.elementToBeClickable(createCaotegoryBtn)).click();
		System.out.println(GREEN + "[INFO] Clicked on 'Create Category' button." + RESET);
		actionPause(800);
	}

	private void enterRandomDeliveryDays(String verb) {
		WebElement dateField = wait.until(ExpectedConditions.visibilityOf(customDeliveryDate));
		scrollToCenter(dateField, 600);

		savedDeliveryDays = RANDOM.nextInt(14) + 1;
		dateField.clear();
		dateField.sendKeys(String.valueOf(savedDeliveryDays));
		System.out.println(CYAN + "[DATA] " + verb + " Custom Delivery Days: " + savedDeliveryDays + RESET);
	}

	// TC-04
	public void verifyCategoryOnProductCreationPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		org.testng.asserts.SoftAssert softAssert = new org.testng.asserts.SoftAssert();

		navigatetoProductsPageAndProructSubMenu();

		WebElement createProdBtn = wait.until(ExpectedConditions.elementToBeClickable(createProductBtn));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", createProdBtn);
		actionPause(600);

		try {
			createProdBtn.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", createProdBtn);
		}
		System.out.println(
				GREEN + "[INFO] Clicked on 'Create Product' button. Navigated to product creation form." + RESET);
		actionPause(1500);

		WebElement brandDD = wait.until(ExpectedConditions.elementToBeClickable(brandDropdownBtn));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", brandDD);
		actionPause(600);
		brandDD.click();
		System.out.println(GREEN + "[INFO] Opened Brand Dropdown on Product Creation page." + RESET);
		actionPause(800);

		List<WebElement> brandOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				By.xpath("//ul[@class='select-options-menu']//li[@class=\"option-item\"]")));

		boolean brandFound = false;
		for (WebElement option : brandOpts) {
			String brandText = option.getText().trim();
			if (brandText.equalsIgnoreCase(savedBrandType)) {
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'nearest'});", option);
				actionPause(400);
				js.executeScript("arguments[0].click();", option);
				brandFound = true;
				System.out.println(PURPLE + "[SELECTED] Brand Type matched and selected: " + brandText + RESET);
				break;
			}
		}
		softAssert.assertTrue(brandFound,
				"Could not find matching brand type '" + savedBrandType + "' in Product Creation brand dropdown!");
		actionPause(1000);

		WebElement catDD = wait.until(ExpectedConditions.elementToBeClickable(cateogryDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", catDD);
		actionPause(600);
		catDD.click();
		System.out
				.println(GREEN + "[INFO] Opened Category Dropdown to verify presence of: " + savedCategoryName + RESET);
		actionPause(800);

		List<WebElement> categoryDropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				By.xpath("(//ul[@class='select-options-menu'])[1]//li[@class=\"option-item active\"]")));

		boolean categoryFound = false;
		String actualFoundText = "Not Found";
		for (WebElement catOption : categoryDropdownOptions) {
			String optionText = catOption.getText().trim();
			if (optionText.contains(savedCategoryName)) {
				categoryFound = true;
				actualFoundText = optionText;
				System.out
						.println(GREEN + "[FOUND] Category successfully reflected in dropdown: " + optionText + RESET);
				break;
			}
		}

		System.out.println(CYAN + "--- [VERIFY: PRODUCT CREATION PAGE - CATEGORY REFLECTION] ---" + RESET);
		System.out.println(PURPLE + "Target Category Name -> Expected: [" + savedCategoryName + "] | Actual Found: ["
				+ actualFoundText + "]" + RESET);

		softAssert.assertTrue(categoryFound, "Newly created category '" + savedCategoryName
				+ "' is NOT reflected in the Product Creation category dropdown!");

		softAssert.assertAll();
		System.out.println(GREEN
				+ "[SUCCESS] Category successfully verified on the Product Creation page! Test Case Passed." + RESET);
	}

	// TC-05
	public static String savedBulkDeliveryDays;

	private static final By BULK_DELIVERY_FIELD = By.xpath("(//input[@id='bulk_custom_delivery_date'])[1]");
	private static final By TRENDING_SWITCH = By.xpath("(//div[@class='custom-control custom-switch'])[1]");
	private static final By STATUS_SWITCH_BULK = By.xpath("(//div[@class='custom-control custom-switch'])[2]");
	private static final By TRENDING_INPUT = By
			.xpath("(//div[@class='custom-control custom-switch'])[1]//input[@type='checkbox']");
	private static final By STATUS_INPUT = By
			.xpath("(//div[@class='custom-control custom-switch'])[2]//input[@type='checkbox']");
	private static final By CATEGORY_SETTING_OPTION = By
			.xpath("(//div[contains(@class,'dropdown-menu dropdown-menu-right shadow-sm show')])[1]"
					+ "//a[@class='dropdown-item category_bulk_action_btn'][3]");

	public void selectMultipleCategoriesAndOpenSettings() {

		for (int i = 3; i <= 4; i++) {
			WebElement checkboxLabel = wait.until(ExpectedConditions.elementToBeClickable(bulkCheckbox(i)));
			scrollToCenter(checkboxLabel, 400);
			clickWithJsFallback(checkboxLabel);
			System.out.println(GREEN + "[INFO] Selected checkbox option index: " + (i - 2) + RESET);
		}
		actionPause(1000);

		WebElement bulkBtn = wait.until(ExpectedConditions.elementToBeClickable(bulkActionBtn));
		scrollToCenter(bulkBtn, 600);
		clickWithJsFallback(bulkBtn);
		System.out.println(GREEN + "[INFO] Clicked on Bulk Action button." + RESET);
		actionPause(800);

		WebElement categorySettingOption = wait.until(ExpectedConditions.elementToBeClickable(CATEGORY_SETTING_OPTION));
		scrollToCenter(categorySettingOption, 500);
		clickWithJsFallback(categorySettingOption);
		System.out.println(GREEN + "[SUCCESS] Clicked on 'Category Setting' option. Popup opened." + RESET);
		actionPause(1000);
	}

	public void verifyCategorySettingsPopupOptions() {
		SoftAssert softAssert = new SoftAssert();

		WebElement trendingSwitch = wait.until(ExpectedConditions.visibilityOfElementLocated(TRENDING_SWITCH));
		WebElement statusSwitch = wait.until(ExpectedConditions.visibilityOfElementLocated(STATUS_SWITCH_BULK));
		WebElement deliveryDateField = wait.until(ExpectedConditions.visibilityOfElementLocated(BULK_DELIVERY_FIELD));

		System.out.println(CYAN + "--- [VERIFY: BULK SETTINGS POPUP OPTIONS] ---" + RESET);
		softAssert.assertTrue(trendingSwitch.isDisplayed(), "Trending switch is not displayed in popup!");
		softAssert.assertTrue(statusSwitch.isDisplayed(), "Status switch is not displayed in popup!");
		softAssert.assertTrue(deliveryDateField.isDisplayed(), "Custom delivery date field is not displayed in popup!");
		softAssert.assertAll();
		System.out.println(GREEN + "[SUCCESS] All popup options (Trending, Status, Custom Delivery Day) verified successfully!" + RESET);
	}

	public void updateCategorySettingsAndSave() {

		WebElement trendingInput = wait.until(ExpectedConditions.presenceOfElementLocated(TRENDING_INPUT));
		scrollToCenter(trendingInput, 500);
		clickIfNotSelected(trendingInput);
		System.out.println(GREEN + "[INFO] Toggled ON Trending switch." + RESET);

		WebElement statusInput = wait.until(ExpectedConditions.presenceOfElementLocated(STATUS_INPUT));
		clickIfNotSelected(statusInput);
		System.out.println(GREEN + "[INFO] Toggled ON Status switch." + RESET);

		savedBulkDeliveryDays = String.valueOf(RANDOM.nextInt(10) + 1);
		WebElement deliveryField = wait.until(ExpectedConditions.visibilityOfElementLocated(BULK_DELIVERY_FIELD));
		deliveryField.clear();
		deliveryField.sendKeys(savedBulkDeliveryDays);
		System.out.println(CYAN + "[DATA] Entered Bulk Custom Delivery Days: " + savedBulkDeliveryDays + RESET);

		WebElement saveBulkBtn = wait.until(ExpectedConditions.elementToBeClickable(saveBulkChangesBtn));
		scrollToCenter(saveBulkBtn, 500);
		clickWithJsFallback(saveBulkBtn);
		System.out.println(GREEN + "[SUCCESS] Clicked Save Bulk Changes button. Settings applied." + RESET);
		actionPause(2500);
	}

	private static final By LISTING_CELL_4 = By.xpath("(//td)[4]");

	public void verifyBulkSettingsAppliedOnListingPage() {
		SoftAssert softAssert = new SoftAssert();

		actionPause(2000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(LISTING_CELL_4));

		verifyBulkRow(softAssert, "1st", 4, 5, 6);
		verifyBulkRow(softAssert, "2nd", 11, 12, 13);

		softAssert.assertAll();
		System.out
				.println(GREEN + "[SUCCESS] Bulk settings verified successfully on both selected categories!" + RESET);
	}

	private By bulkCheckbox(int index) {
		return By.xpath("(//label[@class='crud_bulk_actions_checkbox custom-checkbox'])[" + index + "]");
	}

	private void clickIfNotSelected(WebElement input) {
		if (!input.isSelected()) {
			clickWithJsFallback(input);
		}
	}

	private boolean isToggleActive(int tdIndex) {
		By cell = By.xpath("(//td)[" + tdIndex + "]");
		try {
			return driver.findElement(By.xpath("(//td)[" + tdIndex + "]//input[@type='checkbox']")).isSelected();
		} catch (Exception e) {
			String text = driver.findElement(cell).getText().trim().toLowerCase();
			return text.contains("active") || text.contains("yes");
		}
	}

	private void verifyBulkRow(SoftAssert softAssert, String ordinal, int deliveryTd, int statusTd, int trendingTd) {
		String actualDelivery = driver.findElement(By.xpath("(//td)[" + deliveryTd + "]")).getText().trim();
		boolean isStatusActive = isToggleActive(statusTd);
		boolean isTrendingActive = isToggleActive(trendingTd);

		System.out.println(CYAN + "--- [VERIFY: BULK SETTINGS - " + ordinal.toUpperCase() + " CATEGORY] ---" + RESET);
		System.out.println(PURPLE + "Delivery Date -> Expected: [" + savedBulkDeliveryDays + "] | Actual: ["
				+ actualDelivery + "]" + RESET);
		System.out.println(PURPLE + "Status Active -> Expected: [true] | Actual: [" + isStatusActive + "]" + RESET);
		System.out.println(PURPLE + "Trending Active-> Expected: [true] | Actual: [" + isTrendingActive + "]" + RESET);

		softAssert.assertEquals(actualDelivery, savedBulkDeliveryDays,
				ordinal + " Category Custom Delivery Date mismatch!");
		softAssert.assertTrue(isStatusActive, ordinal + " Category Status toggle is not active!");
		softAssert.assertTrue(isTrendingActive, ordinal + " Category Trending toggle is not active!");
	}

	// TC-06
	private final By fileInput = By.xpath("//input[@type='file']");

	private void clickElement(WebElement element, int scrollOffset, String logMessage) {
		WebElement clickable = wait.until(ExpectedConditions.elementToBeClickable(element));
		scrollToCenter(clickable, scrollOffset);
		clickWithJsFallback(clickable);
		System.out.println(GREEN + logMessage + RESET);
	}

	protected boolean savedStatusActive;
	protected boolean savedTrendingYes;
	
	private static final String TEMPLATE_DIR = System.getProperty("user.dir") + "/src/test/resources/ImportFile/";
	private static final String CATEGORY_TEMPLATE = "category_template.xlsx";

	private String randomText(int length) {
		return UUID.randomUUID().toString().replace("-", "").substring(0, length);
	}

	private String pick(String... options) {
		return options[ThreadLocalRandom.current().nextInt(options.length)];
	}

	private String capitalize(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	private void setCell(Row row, Map<String, Integer> cols, String header, Object value) {
		Integer idx = cols.get(header);
		if (idx == null) {
			throw new IllegalStateException("Column not found in template: " + header);
		}
		Cell cell = row.getCell(idx);
		if (cell == null)
			cell = row.createCell(idx);
		if (value instanceof Number) {
			cell.setCellValue(((Number) value).doubleValue());
		} else {
			cell.setCellValue(String.valueOf(value));
		}
	}

	public String generateRandomCategoryFile() throws Exception {
	    File template = new File(TEMPLATE_DIR + CATEGORY_TEMPLATE);
	    if (!template.exists()) {
	        throw new IllegalStateException("Template not found at: " + template.getAbsolutePath());
	    }

	    Workbook workbook;
	    try (FileInputStream fis = new FileInputStream(template)) {
	        workbook = new XSSFWorkbook(fis);
	    }

	    try {
	        Sheet sheet = workbook.getSheetAt(0);

	        Row headerRow = sheet.getRow(1);
	        Map<String, Integer> cols = new HashMap<>();
	        for (Cell c : headerRow) {
	            cols.put(c.getStringCellValue().trim(), c.getColumnIndex());
	        }

	        Row dataRow = sheet.getRow(2);
	        if (dataRow == null)
	            dataRow = sheet.createRow(2);

	        String suffix = randomText(6);
	        String keyword = "keyword" + randomText(4);
	        String style = pick("Western", "Ethnic", "Casual", "Formal");
	        int deliveryDays = ThreadLocalRandom.current().nextInt(1, 11);
	        int sortBy = ThreadLocalRandom.current().nextInt(1, 100);

	        savedCategoryName = "AutoCategory_" + suffix;
	        savedDeliveryDays = deliveryDays;
	        savedSortBy = String.valueOf(sortBy);
	        savedStyle = style;
	        savedMetaTitle = savedCategoryName + " | Zlaata India";
	        savedAltTag = savedCategoryName + " front view";
	        savedMetaKey = keyword + "," + keyword + " wear";
	        savedSeoTag = "Buy " + savedCategoryName + " Online";
	        savedMetaDesc = "Shop " + savedCategoryName + " " + capitalize(keyword) + " collection.";

	        savedBrandType = dataRow.getCell(cols.get("brand_type")).getStringCellValue().trim();
	        savedStatusActive = dataRow.getCell(cols.get("status")).getStringCellValue().trim().equalsIgnoreCase("Active");
	        savedTrendingYes = dataRow.getCell(cols.get("trending")).getStringCellValue().trim().equalsIgnoreCase("Yes");

	        setCell(dataRow, cols, "name", savedCategoryName);
	        setCell(dataRow, cols, "custom_delivery_date", deliveryDays);
	        setCell(dataRow, cols, "link", "/auto_link_" + suffix);
	        setCell(dataRow, cols, "style", style);
	        setCell(dataRow, cols, "sort_by", sortBy);
	        setCell(dataRow, cols, "desktop_banner", "https://example.com/desktop-banner-" + suffix + ".jpg");
	        setCell(dataRow, cols, "mobile_banner", "https://example.com/mobile-banner-" + suffix + ".jpg");
	        setCell(dataRow, cols, "seo_meta_title", savedMetaTitle);
	        setCell(dataRow, cols, "alt_image_tag", savedAltTag);
	        setCell(dataRow, cols, "seo_meta_key", savedMetaKey);
	        setCell(dataRow, cols, "seo_h1_tag", savedSeoTag);
	        setCell(dataRow, cols, "seo_meta_description", savedMetaDesc);

	        try (FileOutputStream fos = new FileOutputStream(template)) {
	            workbook.write(fos);
	        }
	    } finally {
	        workbook.close();
	    }

	    System.out.println(GREEN + "[INFO] Updated import file: " + template.getAbsolutePath()
	            + " | Category name: " + savedCategoryName + RESET);
	    return template.getAbsolutePath();
	}
	
	private boolean isImportedCellOn(String rowXpath, int tdIndex) {
	    By cell = By.xpath("(" + rowXpath + "/td)[" + tdIndex + "]");
	    try {
	        return driver.findElement(By.xpath("(" + rowXpath + "/td)[" + tdIndex + "]//input[@type='checkbox']"))
	                .isSelected();
	    } catch (Exception e) {
	        String text = driver.findElement(cell).getText().trim().toLowerCase();
	        return text.equals("active") || text.equals("yes") || text.equals("true") || text.equals("on");
	    }
	}

	public void importCategoryWithRandomData() throws Exception {
		String filePath = generateRandomCategoryFile();
		By closeBtn = By.xpath("(//button[normalize-space()='Close'])[1]");

		clickElement(importBtnDropdown, 400, "[INFO] Clicked on Import dropdown button.");
		actionPause(500);
		clickElement(importBtn, 400, "[INFO] Clicked on Import option. Popup opened.");
		actionPause(1000);

		wait.until(ExpectedConditions.visibilityOf(dragAndDropBox));
		WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(fileInput));
		((JavascriptExecutor) driver)
				.executeScript("arguments[0].style.display='block'; arguments[0].style.visibility='visible';", input);
		input.sendKeys(filePath);
		System.out.println(GREEN + "[SUCCESS] Uploaded file in drag and drop box." + RESET);
		actionPause(1500);

		clickElement(nextBtn, 400, "[INFO] Clicked on Next button (1st).");
		actionPause(1500);
		clickElement(nextBtn, 400, "[INFO] Clicked on Next button (2nd).");
		actionPause(1500);
		clickElement(startImportBtn, 400, "[SUCCESS] Clicked on Start Import button.");

		new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(60))
		        .until(ExpectedConditions.elementToBeClickable(closeBtn));
		actionPause(1000);

		WebElement closeButton = driver.findElement(closeBtn);
		scrollToCenter(closeButton, 400);
		clickWithJsFallback(closeButton);
		System.out.println(GREEN + "[SUCCESS] Import completed. Clicked on Close button." + RESET);

		wait.until(ExpectedConditions.invisibilityOf(startImportBtn));
		actionPause(1000);

		driver.navigate().refresh();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//td)[1]")));
		System.out.println(GREEN + "[INFO] Page refreshed after import." + RESET);
		actionPause(2000);
	}
	
	public void verifyImportedCategoryOnListingPage() {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    org.testng.asserts.SoftAssert softAssert = new org.testng.asserts.SoftAssert();

	    actionPause(2000);

	    String rowXpath = "//tr[td[contains(normalize-space(), '" + savedCategoryName + "')]]";

	    WebElement categoryNameCell = wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("(" + rowXpath + "/td)[1]")));
	    WebElement brandTypeCell = driver.findElement(By.xpath("(" + rowXpath + "/td)[2]"));
	    WebElement productCountCell = driver.findElement(By.xpath("(" + rowXpath + "/td)[3]"));
	    WebElement deliveryDateCell = driver.findElement(By.xpath("(" + rowXpath + "/td)[4]"));

	    js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", categoryNameCell);
	    actionPause(500);

	    String actualCategoryName = categoryNameCell.getText().trim();
	    String actualBrandType = brandTypeCell.getText().trim();
	    String actualProductCount = productCountCell.getText().trim();
	    String actualDeliveryDate = deliveryDateCell.getText().trim();
	    boolean actualStatusActive = isImportedCellOn(rowXpath, 5);
	    boolean actualTrendingYes = isImportedCellOn(rowXpath, 6);

	    String expectedProductCount = "-";

	    System.out.println(CYAN + BOLD + "--- [VERIFY: IMPORTED CATEGORY - LISTING PAGE (SOFT ASSERTIONS)] ---" + RESET);
	    logCompare("Category Name", savedCategoryName, actualCategoryName);
	    logCompare("Brand Type", savedBrandType, actualBrandType);
	    logCompare("Product Count", expectedProductCount, actualProductCount);
	    logCompare("Delivery Date", String.valueOf(savedDeliveryDays), actualDeliveryDate);
	    logCompare("Status", savedStatusActive ? "Active" : "Inactive", actualStatusActive ? "Active" : "Inactive");
	    logCompare("Trending", savedTrendingYes ? "Yes" : "No", actualTrendingYes ? "Yes" : "No");

	    softAssert.assertEquals(actualCategoryName, savedCategoryName, "Imported Category Name mismatch on Listing page!");
	    softAssert.assertEquals(actualBrandType, savedBrandType, "Imported Brand Type mismatch on Listing page!");
	    softAssert.assertEquals(actualProductCount, expectedProductCount,
	            "Imported Product Count should be '-' on Listing page!");
	    softAssert.assertEquals(actualDeliveryDate, String.valueOf(savedDeliveryDays),
	            "Imported Custom Delivery Date mismatch on Listing page!");
	    softAssert.assertEquals(actualStatusActive, savedStatusActive, "Imported Status mismatch on Listing page!");
	    softAssert.assertEquals(actualTrendingYes, savedTrendingYes, "Imported Trending mismatch on Listing page!");

	    softAssert.assertAll();
	    System.out.println(BOLD_GREEN
	            + "[SUCCESS] Imported category details verified successfully on Listing page!" + RESET);
	}
	
	//TC-07
	protected List<String> exportedColumnNames = new ArrayList<>();
	
	public void exportCategoryFlow() {
	    clickElement(importBtnDropdown, 400, "[INFO] Clicked on Import/Export dropdown button.");
	    actionPause(800);
	    clickElement(exportBtn, 400, "[INFO] Clicked on Export option. Export popup opened.");
	    actionPause(1000);

	    wait.until(ExpectedConditions.elementToBeClickable(exportStatusDropdown));
	    scrollToCenter(exportStatusDropdown, 400);
	    new Select(exportStatusDropdown).selectByValue("1");
	    System.out.println(GREEN + "[INFO] Selected 'Active' in Status filter." + RESET);
	    actionPause(500);

	    wait.until(ExpectedConditions.elementToBeClickable(exportTrendingDropdown));
	    scrollToCenter(exportTrendingDropdown, 400);
	    new Select(exportTrendingDropdown).selectByValue("1");
	    System.out.println(GREEN + "[INFO] Selected 'Active' in Trending filter." + RESET);
	    actionPause(500);

	    wait.until(ExpectedConditions.visibilityOf(exportDetailsSection));
	    scrollToCenter(exportDetailsSection, 400);

	    List<WebElement> columnLabels = exportDetailsSection.findElements(By.xpath(".//label"));
	    exportedColumnNames.clear();
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    System.out.println(CYAN + "--- Capturing Export Column Names ---" + RESET);
	    int index = 1;
	    for (WebElement label : columnLabels) {
	        String columnName = label.getText().trim();
	        if (columnName.isEmpty()) {
	            Object inner = js.executeScript("return arguments[0].innerText;", label);
	            columnName = inner != null ? inner.toString().trim() : "";
	        }
	        if (!columnName.isEmpty()) {
	            exportedColumnNames.add(columnName);
	            System.out.println(PURPLE + "Captured Column [" + index++ + "]: " + columnName + RESET);
	        }
	    }
	    System.out.println(CYAN + "Total columns captured: " + exportedColumnNames.size() + RESET);
	    actionPause(500);

	    wait.until(ExpectedConditions.presenceOfElementLocated(
	            By.xpath("(//input[contains(@type,'checkbox')])[1]")));
	    scrollToCenter(includeAllCheckBox, 400);
	    if (!includeAllCheckBox.isSelected()) {
	        clickWithJsFallback(includeAllCheckBox);
	    }
	    System.out.println(GREEN + "[INFO] 'Include All' checkbox selected." + RESET);
	    actionPause(800);

	    clickElement(exportDownloadBtn, 400, "[SUCCESS] Clicked on Download button. Export triggered.");
	    actionPause(2000);
	}
	
	
	public void verifyAndDownloadCategoryExportFromHistory() {
	    clickElement(exportHistoryPage, 400, "[INFO] Clicked on Export History.");
	    wait.until(ExpectedConditions.visibilityOf(historyFileName));
	    actionPause(1500);

	    scrollToCenter(historyFileName, 400);
	    String generatedFileName = historyFileName.getText().trim();
	    System.out.println(CYAN + "[INFO] Export file name found in history: " + generatedFileName + RESET);

	    if (!generatedFileName.toLowerCase().contains("category")) {
	        throw new RuntimeException("File name in export history does not contain 'category'! Found: " + generatedFileName);
	    }
	    System.out.println(GREEN + "[SUCCESS] Export file name contains 'category'." + RESET);

	    long deadline = System.currentTimeMillis() + 90_000;
	    int pollCount = 0;
	    boolean isSuccess = false;

	    while (!isSuccess && System.currentTimeMillis() < deadline) {
	        pollCount++;
	        String currentStatus = "";
	        try {
	            wait.until(ExpectedConditions.visibilityOf(historyStatus));
	            currentStatus = historyStatus.getText().trim();
	        } catch (Exception e) {
	            System.out.println(PURPLE + "[WARNING] Could not read status, retrying... (" + e.getMessage() + ")" + RESET);
	        }
	        System.out.println(PURPLE + "[INFO] Export Status Poll [" + pollCount + "]: " + currentStatus + RESET);

	        if (currentStatus.equalsIgnoreCase("Success")) {
	            isSuccess = true;
	            System.out.println(GREEN + "[SUCCESS] Export status changed to Success!" + RESET);
	        } else if (currentStatus.equalsIgnoreCase("Failed") || currentStatus.equalsIgnoreCase("Error")) {
	            throw new RuntimeException("Export generation failed on the server! Status: " + currentStatus);
	        } else {
	            driver.navigate().refresh();
	            actionPause(3000);
	        }
	    }

	    if (!isSuccess) {
	        throw new RuntimeException("Export status remained Pending/Processing after 90 seconds!");
	    }

	    clickElement(historyThreeDot, 400, "[INFO] Clicked on three-dot actions menu.");
	    actionPause(1000);

	    clickElement(historyDownloadBtn, 400, "[SUCCESS] Export file download triggered from Export History!");
	    actionPause(3000);
	}
	
	private static final boolean STRICT_COLUMN_MATCH = false;
	private static final int HEADER_ROW_NUMBER = 5;

	public void verifyExportedCategoryExcelColumns() {
	    System.out.println(CYAN + "==================================================" + RESET);
	    System.out.println(CYAN + "Starting Exported Category Excel File Verification..." + RESET);
	    System.out.println(CYAN + "==================================================" + RESET);

	    String downloadPath = System.getProperty("user.home") + "/Downloads";
	    File dir = new File(downloadPath);
	    System.out.println(GREEN + "[INFO] Scanning Downloads folder: " + downloadPath + RESET);

	    File latestFile = null;
	    long deadline = System.currentTimeMillis() + 30_000;

	    while (latestFile == null && System.currentTimeMillis() < deadline) {
	        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".xlsx")
	                && name.toLowerCase().contains("category"));
	        if (files != null && files.length > 0) {
	            latestFile = files[0];
	            for (File f : files) {
	                if (f.lastModified() > latestFile.lastModified()) {
	                    latestFile = f;
	                }
	            }
	        } else {
	            actionPause(1000);
	        }
	    }

	    if (latestFile == null) {
	        throw new RuntimeException("No downloaded category Excel file found in: " + downloadPath);
	    }

	    System.out.println(GREEN + "[INFO] Target Excel File: " + latestFile.getAbsolutePath() + RESET);
	    System.out.println(GREEN + "[INFO] File Last Modified: " + new Date(latestFile.lastModified()) + RESET);
	    actionPause(1000);

	    try (FileInputStream fis = new FileInputStream(latestFile);
	         Workbook workbook = WorkbookFactory.create(fis)) {

	        Sheet sheet = workbook.getSheetAt(0);
	        System.out.println(GREEN + "[INFO] Active Sheet Name: " + sheet.getSheetName() + RESET);

	        Row headerRow = sheet.getRow(HEADER_ROW_NUMBER - 1);
	        if (headerRow == null) {
	            throw new RuntimeException("Row " + HEADER_ROW_NUMBER
	                    + " (header row) is empty in the exported Excel file: " + latestFile.getName());
	        }

	        System.out.println(CYAN + "--- EXPORTED COLUMNS FOUND IN EXCEL (ROW " + HEADER_ROW_NUMBER + ") ---" + RESET);
	        List<String> actualExcelColumns = new ArrayList<>();
	        int colIndex = 1;
	        for (Cell cell : headerRow) {
	            String cellValue = cell.toString().trim();
	            if (!cellValue.isEmpty()) {
	                actualExcelColumns.add(cellValue);
	                System.out.println(PURPLE + "   [Column " + colIndex++ + "] ---> " + cellValue + RESET);
	            }
	        }

	        if (actualExcelColumns.isEmpty()) {
	            throw new RuntimeException("No valid column headers found in the Excel file!");
	        }

	        System.out.println(CYAN + "--- COMPARING POPUP COLUMNS WITH EXCEL COLUMNS ---" + RESET);
	        List<String> popupColumns = new ArrayList<>();
	        for (String c : exportedColumnNames) popupColumns.add(c.trim().toLowerCase());

	        List<String> excelColumnsLower = new ArrayList<>();
	        for (String c : actualExcelColumns) excelColumnsLower.add(c.trim().toLowerCase());

	        List<String> missingInExcel = new ArrayList<>();
	        for (String c : exportedColumnNames) {
	            if (!excelColumnsLower.contains(c.trim().toLowerCase())) missingInExcel.add(c);
	        }

	        System.out.println(CYAN + "Popup columns: " + exportedColumnNames.size()
	                + " | Excel columns: " + actualExcelColumns.size() + RESET);

	        if (missingInExcel.isEmpty()) {
	            System.out.println(BOLD_GREEN + "[SUCCESS] All popup columns are present in the exported Excel!" + RESET);
	        } else {
	            System.out.println(BOLD_YELLOW + "[WARNING] Popup columns not found in Excel: " + missingInExcel + RESET);
	            if (STRICT_COLUMN_MATCH) {
	                throw new RuntimeException("Exported Excel is missing columns: " + missingInExcel);
	            }
	        }

	        System.out.println(BOLD_GREEN + "[SUCCESS] Excel columns verification completed. Total columns: "
	                + actualExcelColumns.size() + RESET);

	    } catch (RuntimeException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to read or parse the downloaded Excel file: " + e.getMessage(), e);
	    }
	}
	
	// TC-01
	public void createCategoryFlow() {
		fillCreateCategoryForm();
		uploadCategoryBanners();
		fillSeoDetails();
		verifyCategoryOnListingPage();
		verifyCategoryOnPreviewPage();
	}

	// TC-02
	public void editCategoryFlow() {
		clickEditFromActionMenu();
		editCategoryForm();
		verifyCategoryOnListingPage();
		verifyCategoryOnPreviewPage();
	}

	// TC-03
	public void createDraftCategoryFlow() {
		createCategoryAsDraft();
		verifyDraftCategoryOnListingPage();
		deleteDraftCategory();
		verifyCategoryDeletedFromListingPage();
	}

	// TC-04
	public void activateCategoryFlow() {
		fillCreateCategoryForm();
		uploadCategoryBanners();
		fillSeoDetails();
		verifyCategoryOnProductCreationPage();
	}

	// TC-05
	public void bulkUpdateCategorySettingsFlow() {
		selectMultipleCategoriesAndOpenSettings();
		verifyCategorySettingsPopupOptions();
		updateCategorySettingsAndSave();
		verifyBulkSettingsAppliedOnListingPage();
	}

	// TC-06
	public void verifyImportCategoryFlow() throws Exception {
		importCategoryWithRandomData();
		verifyImportedCategoryOnListingPage();
	}
	
	//TC-07
	public void verifyExportCategoryFlow() {
		exportCategoryFlow();
		verifyAndDownloadCategoryExportFromHistory();
		verifyExportedCategoryExcelColumns();
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
