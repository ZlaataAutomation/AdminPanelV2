package pages;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import objectRepo.ClassificationCategory_ObjRepo;

public class ClassificationCollection_Page extends ClassificationCategory_ObjRepo {

	public ClassificationCollection_Page(WebDriver driver) {
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

	private void actionPause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void clickWithJsFallback(WebElement element) {
		try {
			element.click();
		} catch (Exception e) {
			jsClick(element);
		}
	}

	private void jsClick(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	private void scrollIntoView(WebElement element, String block) {
		((JavascriptExecutor) driver)
				.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: '" + block + "'});", element);
	}

	private void scrollToCenter(WebElement element, int pauseMs) {
		scrollIntoView(element, "center");
		actionPause(pauseMs);
	}

	private static final int ACTION_DELAY = 700;

	private void actionPause() {
		pause(ACTION_DELAY);
	}

	private void logAction(String message) {
		System.out.println(PURPLE + BOLD + "[ACTION] " + message + RESET);
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

	public void navigatetoProductsCollectionPage() {
		logHeader("NAVIGATION");
		logAction("Hovering over 'Product' menu...");
		wait.until(ExpectedConditions.visibilityOf(productMenu));
		new Actions(driver).moveToElement(productMenu).perform();
		actionPause();

		logAction("Clicking 'Classification' sub-menu...");
		wait.until(ExpectedConditions.visibilityOf(classificationSubMenu));
		click(classificationSubMenu);
		logSuccess("Navigated to Classification page");

		logAction("Clicking 'Classification' Collection sub-menu...");
		wait.until(ExpectedConditions.visibilityOf(collectionSubMenu));
		click(collectionSubMenu);
		logSuccess("Navigated to Classification Collection page");
	}

	public void navigatetoProductsPageAndProructSubMenu() {
		logHeader("NAVIGATION");
		logAction("Hovering over 'Product' menu...");
		wait.until(ExpectedConditions.visibilityOf(productMenu));
		new Actions(driver).moveToElement(productMenu).perform();
		actionPause();

		logAction("Clicking 'Products' sub-menu...");
		wait.until(ExpectedConditions.visibilityOf(productsSubMenu));
		click(productsSubMenu);
		logSuccess("Navigated to Products page");
	}

	public static String savedCollectionName;
	public static String savedCollectionBrand;
	public static String savedCollectionSlug;
	public static int savedCollectionDeliveryDays;
	public static String savedCollectionSectionType;
	public static int savedCollectionProductCount;
	public static String savedCollectionSortBy;
	public static String savedCollectionStyle;
	public static String savedSaleCollection;
	public static String savedSaleType;
	public static String savedOfferPriceText;
	public static String savedMetaTitle;
	public static String savedAltTag;
	public static String savedMetaKey;
	public static String savedSeoTag;
	public static String savedMetaDesc;
	public static boolean savedCollectionStatusActive = false;
	public static boolean savedIsAccessoriesActive = false;
	public static boolean savedIsGiftActive = false;
	public static boolean savedIsSaleActive = false;
	public static boolean savedIsReturnableActive = false;
	public static boolean savedIsExchangeableActive = false;
	public static boolean savedIsCancelLabelActive = false;

	// TC-01
	public void fillCreateCollectionForm() {
		Random random = new Random();
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement createBtn = wait.until(ExpectedConditions.elementToBeClickable(createCollectionBtn));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", createBtn);
		actionPause(600);

		try {
			createBtn.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", createBtn);
		}
		System.out.println(GREEN + "[INFO] Clicked on 'Create Collection' button. Opening form..." + RESET);
		actionPause(1000);

		WebElement nameField = wait.until(ExpectedConditions.visibilityOf(collectionName));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", nameField);
		actionPause(500);

		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder("Collection");
		for (int i = 0; i < 6; i++) {
			sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
		}
		savedCollectionName = sb.toString();
		nameField.clear();
		nameField.sendKeys(savedCollectionName);
		System.out.println(
				CYAN + "[DATA] Generated & Entered Collection Name (Letters Only): " + savedCollectionName + RESET);

		WebElement brandDD = wait.until(ExpectedConditions.elementToBeClickable(collectionBrandTypeDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", brandDD);
		actionPause(500);
		brandDD.click();
		System.out.println(GREEN + "[INFO] Opened Brand Type Dropdown." + RESET);
		actionPause(800);

		List<WebElement> brandOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
				"//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a | //ul[@class='select-options-menu']//li[@class='option-item']")));

		if (brandOpts != null && !brandOpts.isEmpty()) {
			int randomBrandIdx = random.nextInt(brandOpts.size());
			WebElement selectedBrand = brandOpts.get(randomBrandIdx);
			savedCollectionBrand = selectedBrand.getText().trim();

			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'nearest'});", selectedBrand);
			actionPause(400);
			js.executeScript("arguments[0].click();", selectedBrand);
			System.out.println(PURPLE + "[SELECTED] Brand Type Option: " + savedCollectionBrand + RESET);
		}
		actionPause(600);

		WebElement dateField = wait.until(ExpectedConditions.visibilityOf(customDeliveryDate));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", dateField);
		actionPause(500);

		savedCollectionDeliveryDays = random.nextInt(14) + 1;
		dateField.clear();
		dateField.sendKeys(String.valueOf(savedCollectionDeliveryDays));
		System.out.println(CYAN + "[DATA] Entered Custom Delivery Days: " + savedCollectionDeliveryDays + RESET);

		WebElement sectionTypeDD = wait.until(ExpectedConditions.elementToBeClickable(collectionSectionTypeDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", sectionTypeDD);
		actionPause(500);
		sectionTypeDD.click();
		System.out.println(GREEN + "[INFO] Opened Collection Section Type Dropdown." + RESET);
		actionPause(800);

		List<WebElement> sectionOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
				"//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a | //ul[@class='select-options-menu']//li[@class='option-item']")));

		if (sectionOpts != null && !sectionOpts.isEmpty()) {
			int randomSectionIdx = random.nextInt(sectionOpts.size());
			WebElement selectedSection = sectionOpts.get(randomSectionIdx);
			savedCollectionSectionType = selectedSection.getText().trim();

			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'nearest'});", selectedSection);
			actionPause(400);
			js.executeScript("arguments[0].click();", selectedSection);
			System.out.println(PURPLE + "[SELECTED] Section Type: " + savedCollectionSectionType + RESET);
		}
		actionPause(600);

		WebElement productDD = wait.until(ExpectedConditions.elementToBeClickable(collectionProductDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", productDD);
		actionPause(500);
		productDD.click();
		System.out.println(GREEN + "[INFO] Opened Collection Product Dropdown." + RESET);
		actionPause(800);

		List<WebElement> productOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
				"//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a | //ul[@class='select-options-menu']//li[@class='option-item']")));

		if (productOpts != null && !productOpts.isEmpty()) {
			savedCollectionProductCount = Math.min(random.nextInt(3) + 1, productOpts.size());
			System.out.println(CYAN + "[INFO] Selecting " + savedCollectionProductCount
					+ " random product(s) for collection..." + RESET);

			for (int i = 0; i < savedCollectionProductCount; i++) {
				int randomIndex = random.nextInt(productOpts.size());
				WebElement prodOption = productOpts.get(randomIndex);
				String prodText = prodOption.getText().trim();
				actionPause(600);

				js.executeScript("arguments[0].click();", prodOption);
				System.out.println(PURPLE + "[SELECTED] Collection Product [" + (i + 1) + "]: " + prodText + RESET);
			}
		}
		try {
			productDD.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", productDD);
		}
		actionPause(500);

		savedCollectionSortBy = "1";
		WebElement sortField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@id='sort_by'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", sortField);
		actionPause(500);

		try {
			sortField.clear();
			sortField.sendKeys(savedCollectionSortBy);
		} catch (Exception e) {
			js.executeScript(
					"arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('input')); arguments[0].dispatchEvent(new Event('change'));",
					sortField, savedCollectionSortBy);
		}
		System.out.println(CYAN + "[DATA] Entered Sort By value: " + savedCollectionSortBy + RESET);

		savedCollectionStyle = "Modern Collection Style Layout - Automated Test";
		WebElement styleField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//textarea[@id='style'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", styleField);
		actionPause(500);

		try {
			styleField.clear();
			styleField.sendKeys(savedCollectionStyle);
		} catch (Exception e) {
			js.executeScript(
					"arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('input')); arguments[0].dispatchEvent(new Event('change'));",
					styleField, savedCollectionStyle);
		}
		System.out.println(CYAN + "[DATA] Entered Style input: " + savedCollectionStyle + RESET);

		WebElement[] allToggles = { collectionstautsToggle, isAccessoriesToggle, isGiftToggle, isSaleToggle,
				isReturnableToggle, isExchangeableToggle, isCancelLabelToggle };

		String[] toggleNames = { "Collection Status", "Is Accessories", "Is Gift", "Is Sale", "Is Returnable",
				"Is Exchangeable", "Is Cancel Label" };

		int[] domIndices = { 1, 2, 3, 4, 5, 6, 7 };

		List<Integer> indices = new ArrayList<>();
		for (int i = 0; i < allToggles.length; i++) {
			indices.add(i);
		}
		java.util.Collections.shuffle(indices, random);
		int firstChoice = indices.get(0);
		int secondChoice = indices.get(1);

		System.out.println(CYAN + "[INFO] Randomly chosen toggles to enable: [" + toggleNames[firstChoice] + "] and ["
				+ toggleNames[secondChoice] + "]" + RESET);

		for (int k = 0; k < allToggles.length; k++) {
			WebElement toggle = allToggles[k];
			boolean shouldBeOn = (k == firstChoice || k == secondChoice);

			if (toggle != null) {
				try {
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", toggle);
					actionPause(300);

					boolean isCurrentlySelected = toggle.isSelected();

					if (shouldBeOn && !isCurrentlySelected) {
						js.executeScript("arguments[0].click();", toggle);
						System.out.println(GREEN + "[INFO] Toggled ON: " + toggleNames[k] + RESET);
					} else if (!shouldBeOn && isCurrentlySelected) {
						js.executeScript("arguments[0].click();", toggle);
						System.out.println(GREEN + "[INFO] Toggled OFF: " + toggleNames[k] + RESET);
					}
				} catch (Exception e) {
					try {
						int domIndex = domIndices[k];
						WebElement parentLabel = driver.findElement(By
								.xpath("(//div[@class='custom-control custom-switch ms-3'])[" + domIndex + "]//label"));
						js.executeScript("arguments[0].click();", parentLabel);
						System.out.println(
								GREEN + "[SUCCESS] Toggled switch [" + toggleNames[k] + "] via parent label." + RESET);
					} catch (Exception ignored) {
						System.out.println(
								PURPLE + "[WARNING] Could not toggle switch [" + toggleNames[k] + "]." + RESET);
					}
				}
			}

			if (k == 0)
				savedCollectionStatusActive = shouldBeOn;
			else if (k == 1)
				savedIsAccessoriesActive = shouldBeOn;
			else if (k == 2)
				savedIsGiftActive = shouldBeOn;
			else if (k == 3)
				savedIsSaleActive = shouldBeOn;
			else if (k == 4)
				savedIsReturnableActive = shouldBeOn;
			else if (k == 5)
				savedIsExchangeableActive = shouldBeOn;
			else if (k == 6)
				savedIsCancelLabelActive = shouldBeOn;
		}
		actionPause(800);
	}

	public void uploadCollectionBanners() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		List<String> absoluteImagePaths = new ArrayList<>();

		for (String name : List.of("CollectionBanner.png")) {
			File file = new File("src/test/resources/images/" + name);
			if (file.exists()) {
				absoluteImagePaths.add(file.getAbsolutePath());
			} else {
				File fallbackFile = new File(
						"C:\\Users\\Abdul\\git\\AdminPanelV2local\\ZlaataQA\\src\\test\\resources\\images\\" + name);
				if (fallbackFile.exists()) {
					absoluteImagePaths.add(fallbackFile.getAbsolutePath());
				}
			}
		}

		if (!absoluteImagePaths.isEmpty()) {
			String joinedFilePaths = String.join("\n", absoluteImagePaths);

			try {
				WebElement desktopInput = driver.findElement(By.xpath("(//input[@id='desktopFileInput'])[1]"));
				js.executeScript("arguments[0].style.display='block'; arguments[0].style.opacity=1;", desktopInput);
				desktopInput.sendKeys(joinedFilePaths);
				System.out.println(PURPLE + "[UPLOAD] Collection Desktop Banner uploaded successfully using path(s):\n"
						+ joinedFilePaths + RESET);
			} catch (Exception e) {
				System.out.println(
						GREEN + "[WARNING] Failed to upload Collection Desktop Banner: " + e.getMessage() + RESET);
			}

			actionPause(1000);

			try {
				WebElement mobileInput = driver.findElement(By.xpath("(//input[@id='mobiletopFileInput'])[1]"));
				js.executeScript("arguments[0].style.display='block'; arguments[0].style.opacity=1;", mobileInput);
				actionPause(500);

				mobileInput.sendKeys(joinedFilePaths);
				System.out.println(PURPLE + "[UPLOAD] Collection Mobile Banner uploaded successfully using path(s):\n"
						+ joinedFilePaths + RESET);
			} catch (Exception e) {
				try {
					WebElement fallbackMobile = driver.findElement(By.xpath("(//input[@type='file'])[last()]"));
					js.executeScript("arguments[0].style.display='block'; arguments[0].style.opacity=1;",
							fallbackMobile);
					fallbackMobile.sendKeys(joinedFilePaths);
					System.out.println(
							PURPLE + "[UPLOAD] Collection Mobile Banner uploaded via fallback file input." + RESET);
				} catch (Exception ex) {
					System.out.println(
							GREEN + "[WARNING] Failed to upload Collection Mobile Banner: " + ex.getMessage() + RESET);
				}
			}

		} else {
			System.out.println(GREEN
					+ "[WARNING] No valid 'CollectionBanner.png' file found in src/test/resources/images/. Skipping upload."
					+ RESET);
		}
		actionPause(1500);
	}

	public void fillSaleInfoSection() {
		Random random = new Random();
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement saleColDD = wait.until(ExpectedConditions.elementToBeClickable(saleCollectionDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", saleColDD);
		actionPause(500);
		saleColDD.click();
		System.out.println(GREEN + "[INFO] Opened Sale Collection Dropdown." + RESET);
		actionPause(800);

		List<WebElement> saleColOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
				"//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a | //ul[@class='select-options-menu']//li[@class='option-item']")));

		if (saleColOpts != null && !saleColOpts.isEmpty()) {
			int randomIdx = random.nextInt(saleColOpts.size());
			WebElement selectedOpt = saleColOpts.get(randomIdx);
			savedSaleCollection = selectedOpt.getText().trim();

			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'nearest'});", selectedOpt);
			actionPause(400);
			js.executeScript("arguments[0].click();", selectedOpt);
			System.out.println(PURPLE + "[SELECTED] Sale Collection Option: " + savedSaleCollection + RESET);
		}
		actionPause(600);

		WebElement saleTypeDD = wait.until(ExpectedConditions.elementToBeClickable(saleTypeDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", saleTypeDD);
		actionPause(500);
		saleTypeDD.click();
		System.out.println(GREEN + "[INFO] Opened Sale Type Dropdown." + RESET);
		actionPause(800);

		List<WebElement> saleTypeOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
				"//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a | //ul[@class='select-options-menu']//li[@class='option-item']")));

		if (saleTypeOpts != null && !saleTypeOpts.isEmpty()) {
			int randomIdx = random.nextInt(saleTypeOpts.size());
			WebElement selectedOpt = saleTypeOpts.get(randomIdx);
			savedSaleType = selectedOpt.getText().trim();

			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'nearest'});", selectedOpt);
			actionPause(400);
			js.executeScript("arguments[0].click();", selectedOpt);
			System.out.println(PURPLE + "[SELECTED] Sale Type Option: " + savedSaleType + RESET);
		}
		actionPause(600);

		savedOfferPriceText = "Special Promo Offer " + (random.nextInt(90) + 10) + "% OFF";
		WebElement offerField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//textarea[@id='offer_price'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", offerField);
		actionPause(500);

		try {
			offerField.clear();
			offerField.sendKeys(savedOfferPriceText);
		} catch (Exception e) {
			js.executeScript(
					"arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('input')); arguments[0].dispatchEvent(new Event('change'));",
					offerField, savedOfferPriceText);
		}
		System.out.println(CYAN + "[DATA] Entered Offer Price/Type text: " + savedOfferPriceText + RESET);
		actionPause(500);
	}

	public void fillSeoDetails() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Random random = new Random();

		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
		}
		String seoAlphaId = sb.toString();

		savedMetaTitle = "Meta Title " + seoAlphaId;
		savedAltTag = "Alt Tag " + seoAlphaId;
		savedMetaKey = "meta keyword " + seoAlphaId;
		savedSeoTag = "Tag " + seoAlphaId;
		savedMetaDesc = "Automated seo description test for collection creation without numbers " + seoAlphaId;

		WebElement metaTitleField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@id='seometatitle'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", metaTitleField);
		actionPause(600);
		safeSendKeys(metaTitleField, savedMetaTitle);
		System.out.println(CYAN + "[DATA] Entered SEO Meta Title: " + savedMetaTitle + RESET);

		WebElement altField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@id='seoaltimagetag'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", altField);
		actionPause(600);
		safeSendKeys(altField, savedAltTag);
		System.out.println(CYAN + "[DATA] Entered SEO Alt Image Tag: " + savedAltTag + RESET);

		WebElement metaKeyField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@id='seometakey'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", metaKeyField);
		actionPause(600);
		safeSendKeys(metaKeyField, savedMetaKey);
		System.out.println(CYAN + "[DATA] Entered SEO Meta Key: " + savedMetaKey + RESET);

		WebElement tagField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@id='seotag'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", tagField);
		actionPause(600);
		safeSendKeys(tagField, savedSeoTag);
		System.out.println(CYAN + "[DATA] Entered SEO Tag: " + savedSeoTag + RESET);

		WebElement metaDescField = wait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("(//textarea[@id='Meta-description-first'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", metaDescField);
		actionPause(600);
		safeSendKeys(metaDescField, savedMetaDesc);
		System.out.println(CYAN + "[DATA] Entered SEO Meta Description: " + savedMetaDesc + RESET);

		clickSaveButton();
	}

	private void safeSendKeys(WebElement element, String value) {
		try {
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value = arguments[1]; "
					+ "arguments[0].dispatchEvent(new Event('input', { bubbles: true })); "
					+ "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element, value);
		}
	}

	public void clickSaveButton() {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", saveButton);
		actionPause(800); // Visible pause before clicking save

		try {
			saveButton.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", saveButton);
		}

		System.out.println(GREEN + "[SUCCESS] Clicked on 'Save' button. Category form submitted." + RESET);

		actionPause(2000);
	}

	private void logCompare(String label, Object expected, Object actual) {
		String exp = String.valueOf(expected);
		String act = String.valueOf(actual);
		String color = exp.equals(act) ? BOLD_GREEN : BOLD_YELLOW;
		System.out.println(color + String.format("%-16s -> Expected: [%s] | Actual: [%s]", label, exp, act) + RESET);
	}

	public void verifyCollectionOnListingPage() {
		org.testng.asserts.SoftAssert softAssert = new org.testng.asserts.SoftAssert();

		WebElement collectionNameCell = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//td)[1]")));

		String actualCollectionName = collectionNameCell.getText().trim();
		String actualBrandType = driver.findElement(By.xpath("(//td)[2]")).getText().trim();
		String actualProductCount = driver.findElement(By.xpath("(//td)[3]")).getText().trim();
		String actualSelectionType = driver.findElement(By.xpath("(//td)[4]")).getText().trim();

		boolean actualStatusToggleState = false;
		try {
			WebElement statusInput = driver.findElement(By.xpath("(//td)[6]//input[@type='checkbox']"));
			actualStatusToggleState = statusInput.isSelected();
		} catch (Exception e) {
			String statusText = driver.findElement(By.xpath("(//td)[6]")).getText().trim().toLowerCase();
			actualStatusToggleState = statusText.contains("enabled") || statusText.contains("active")
					|| statusText.contains("true") || statusText.contains("yes");
		}

		System.out.println(CYAN + "--- [VERIFY: COLLECTION LISTING PAGE (SOFT ASSERTIONS)] ---" + RESET);
		logCompare("Collection Name", savedCollectionName, actualCollectionName);
		logCompare("Brand Type", savedCollectionBrand, actualBrandType);
		logCompare("Product Count", savedCollectionProductCount, actualProductCount);
		logCompare("Selection Type", savedCollectionSectionType, actualSelectionType);

		boolean expectedStatusState = savedCollectionStatusActive;
		logCompare("Status Toggle", expectedStatusState, actualStatusToggleState);

		softAssert.assertEquals(actualCollectionName, savedCollectionName, "Collection Name mismatch on Listing page!");
		softAssert.assertEquals(actualBrandType, savedCollectionBrand, "Brand Type mismatch on Listing page!");
		softAssert.assertEquals(actualProductCount, String.valueOf(savedCollectionProductCount),
				"Product Count mismatch on Listing page!");
		softAssert.assertEquals(actualSelectionType, savedCollectionSectionType,
				"Selection Type mismatch on Listing page!");
		softAssert.assertEquals(actualStatusToggleState, expectedStatusState,
				"Status Toggle state mismatch on Listing page!");

		softAssert.assertAll();
		System.out.println(BOLD_GREEN
				+ "[SUCCESS] All Collection Listing page details verified successfully with Soft Assertions!" + RESET);
	}

	public void verifyCollectionOnPreviewPage() {
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
		System.out.println(BOLD_GREEN + "[INFO] Clicked on Preview button. Opening collection detail modal..." + RESET);
		actionPause(1200); // Wait for modal animation to render completely

		String modalCollectionName = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//p[@class='modal_para_dark'])[1]")))
				.getText().trim();
		String modalBrandType = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[2]")).getText().trim();
		String modalDeliveryDate = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[4]")).getText().trim();
		String modalSelectionType = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[5]")).getText().trim();
		String modalProductCount = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[6]")).getText().trim();
		String modalSortBy = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[7]")).getText().trim();
		String modalStyle = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[9]")).getText().trim();

		String modalStatus = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[10]")).getText().trim();
		String modalAccessories = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[11]")).getText().trim();
		String modalGift = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[12]")).getText().trim();
		String modalReturn = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[14]")).getText().trim();
		String modalExchange = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[15]")).getText().trim();
		String modalCancel = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[16]")).getText().trim();

		String modalSaleCollection = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[17]")).getText()
				.trim();
		String modalSaleType = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[18]")).getText().trim();
		String modalOfferPrice = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[19]")).getText().trim();

		String modalSeoMetaTitle = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[20]")).getText().trim();
		String modalSeoAltImage = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[21]")).getText().trim();
		String modalSeoMetaKey = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[22]")).getText().trim();
		String modalSeoH1Tag = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[23]")).getText().trim();
		String modalSeoDescription = driver.findElement(By.xpath("(//p[@class='modal_para_dark'])[24]")).getText()
				.trim();

		String expectedDeliveryDisplay = savedCollectionDeliveryDays
				+ (savedCollectionDeliveryDays == 1 ? " Day" : " Days");
		String expectedProductCountDisplay = savedCollectionProductCount
				+ (savedCollectionProductCount == 1 ? " Item Selected" : " Items Selected");

		System.out.println(CYAN + "--- [VERIFY: COLLECTION PREVIEW MODAL (SOFT ASSERTIONS)] ---" + RESET);
		logCompare("Collection Name", savedCollectionName, modalCollectionName);
		logCompare("Brand Type", savedCollectionBrand, modalBrandType);
		logCompare("Delivery Date", expectedDeliveryDisplay, modalDeliveryDate);
		logCompare("Selection Type", savedCollectionSectionType, modalSelectionType);
		logCompare("Product Count", expectedProductCountDisplay, modalProductCount);
		logCompare("Sort By", savedCollectionSortBy, modalSortBy);
		logCompare("Style", savedCollectionStyle, modalStyle);

		String expectedStatus = savedCollectionStatusActive ? "Active" : "Inactive";
		logCompare("Status", expectedStatus, modalStatus);
		logCompare("Is Accessories", savedIsAccessoriesActive ? "Yes" : "No", modalAccessories);
		logCompare("Is Gift", savedIsGiftActive ? "Yes" : "No", modalGift);
		logCompare("Is Returnable", savedIsReturnableActive ? "Yes" : "No", modalReturn);
		logCompare("Is Exchangeable", savedIsExchangeableActive ? "Yes" : "No", modalExchange);
		logCompare("Is Cancel", savedIsCancelLabelActive ? "Yes" : "No", modalCancel);

		logCompare("Sale Collection", savedSaleCollection, modalSaleCollection);
		logCompare("Sale Type", savedSaleType, modalSaleType);
		logCompare("Offer Price", savedOfferPriceText, modalOfferPrice);

		logCompare("SEO Meta Title", savedMetaTitle, modalSeoMetaTitle);
		logCompare("SEO Alt Image", savedAltTag, modalSeoAltImage);
		logCompare("SEO Meta Key", savedMetaKey, modalSeoMetaKey);
		logCompare("SEO H1 Tag", savedSeoTag, modalSeoH1Tag);
		logCompare("SEO Meta Desc", savedMetaDesc, modalSeoDescription);

		softAssert.assertEquals(modalCollectionName, savedCollectionName, "Preview Modal Collection Name mismatch!");
		softAssert.assertEquals(modalBrandType, savedCollectionBrand, "Preview Modal Brand Type mismatch!");
		softAssert.assertEquals(modalDeliveryDate, expectedDeliveryDisplay, "Preview Modal Delivery Date mismatch!");
		softAssert.assertEquals(modalSelectionType, savedCollectionSectionType,
				"Preview Modal Selection Type mismatch!");
		softAssert.assertEquals(modalProductCount, expectedProductCountDisplay,
				"Preview Modal Product Count mismatch!");
		softAssert.assertEquals(modalSortBy, savedCollectionSortBy, "Preview Modal Sort By mismatch!");
		softAssert.assertEquals(modalStyle, savedCollectionStyle, "Preview Modal Style mismatch!");

		softAssert.assertTrue(modalStatus.toLowerCase().contains(expectedStatus.toLowerCase()),
				"Preview Modal Status mismatch!");
		softAssert.assertEquals(modalAccessories.toLowerCase(), savedIsAccessoriesActive ? "yes" : "no",
				"Preview Modal Accessories toggle mismatch!");
		softAssert.assertEquals(modalGift.toLowerCase(), savedIsGiftActive ? "yes" : "no",
				"Preview Modal Gift toggle mismatch!");
		softAssert.assertEquals(modalReturn.toLowerCase(), savedIsReturnableActive ? "yes" : "no",
				"Preview Modal Return toggle mismatch!");
		softAssert.assertEquals(modalExchange.toLowerCase(), savedIsExchangeableActive ? "yes" : "no",
				"Preview Modal Exchange toggle mismatch!");
		softAssert.assertEquals(modalCancel.toLowerCase(), savedIsCancelLabelActive ? "yes" : "no",
				"Preview Modal Cancel toggle mismatch!");

		softAssert.assertEquals(modalSaleCollection, savedSaleCollection, "Preview Modal Sale Collection mismatch!");
		softAssert.assertEquals(modalSaleType, savedSaleType, "Preview Modal Sale Type mismatch!");
		softAssert.assertEquals(modalOfferPrice, savedOfferPriceText, "Preview Modal Offer Price mismatch!");

		softAssert.assertEquals(modalSeoMetaTitle, savedMetaTitle, "Preview Modal SEO Meta Title mismatch!");
		softAssert.assertEquals(modalSeoAltImage, savedAltTag, "Preview Modal SEO Alt Image Tag mismatch!");
		softAssert.assertEquals(modalSeoMetaKey, savedMetaKey, "Preview Modal SEO Meta Key mismatch!");
		softAssert.assertEquals(modalSeoH1Tag, savedSeoTag, "Preview Modal SEO H1 Tag mismatch!");
		softAssert.assertEquals(modalSeoDescription, savedMetaDesc, "Preview Modal SEO Description mismatch!");

		WebElement desktopImg = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img[@alt='Desktop Banner'])[1]")));
		WebElement mobileImg = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img[@alt='Mobile Banner'])[1]")));

		boolean isDesktopImgLoaded = (Boolean) js.executeScript(
				"return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0;",
				desktopImg);
		boolean isMobileImgLoaded = (Boolean) js.executeScript(
				"return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0;",
				mobileImg);

		softAssert.assertTrue(isDesktopImgLoaded, "Collection Desktop banner image failed to load in preview modal!");
		softAssert.assertTrue(isMobileImgLoaded, "Collection Mobile banner image failed to load in preview modal!");
		System.out.println((isDesktopImgLoaded && isMobileImgLoaded ? BOLD_GREEN : BOLD_YELLOW)
				+ "[VERIFY: BANNERS] Desktop loaded: " + isDesktopImgLoaded + " | Mobile loaded: " + isMobileImgLoaded
				+ RESET);

		softAssert.assertAll();
		System.out.println(BOLD_GREEN
				+ "[SUCCESS] All Collection Preview Modal details, toggles, sale info, and SEO fields verified successfully!"
				+ RESET);
	}

	// TC-02
	public void clickEditFromActionMenu() {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement threeDot = wait.until(ExpectedConditions.elementToBeClickable(threedotBtn));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", threeDot);
		actionPause(600);

		try {
			threeDot.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", threeDot);
		}
		System.out.println(GREEN + "[INFO] Clicked on Three-Dot Actions menu for editing." + RESET);
		actionPause(500);

		WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(editBtn));
		try {
			editOption.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", editOption);
		}
		System.out.println(GREEN + "[INFO] Clicked on Edit button. Opening edit form..." + RESET);
		actionPause(1000); // Wait for edit form to render completely
	}

	public void editCollectionForm() {
		Random random = new Random();
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement nameField = wait.until(ExpectedConditions.visibilityOf(collectionName));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", nameField);
		actionPause(500);

		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder("UpdatedCol");
		for (int i = 0; i < 6; i++) {
			sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
		}
		savedCollectionName = sb.toString();
		nameField.clear();
		nameField.sendKeys(savedCollectionName);
		System.out.println(CYAN + "[DATA] Updated Collection Name (Letters Only): " + savedCollectionName + RESET);

		WebElement brandDD = wait.until(ExpectedConditions.elementToBeClickable(editBrandTypeDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", brandDD);
		actionPause(500);
		js.executeScript("arguments[0].click();", brandDD);
		System.out.println(GREEN + "[INFO] Opened Brand Type Dropdown for update." + RESET);
		actionPause(800);

		List<WebElement> brandOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
				"//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a | //ul[@class='select-options-menu']//li[@class='option-item']")));

		if (brandOpts != null && !brandOpts.isEmpty()) {
			int randomBrandIdx = random.nextInt(brandOpts.size());
			WebElement selectedBrand = brandOpts.get(randomBrandIdx);
			savedCollectionBrand = selectedBrand.getText().trim();
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'nearest'});", selectedBrand);
			actionPause(400);
			js.executeScript("arguments[0].click();", selectedBrand);
			System.out.println(PURPLE + "[SELECTED] Updated Brand Type Option: " + savedCollectionBrand + RESET);
		}
		actionPause(600);

		WebElement dateField = wait.until(ExpectedConditions.visibilityOf(customDeliveryDate));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", dateField);
		actionPause(500);

		savedCollectionDeliveryDays = random.nextInt(14) + 1;
		dateField.clear();
		dateField.sendKeys(String.valueOf(savedCollectionDeliveryDays));
		System.out.println(CYAN + "[DATA] Updated Custom Delivery Days: " + savedCollectionDeliveryDays + RESET);

		WebElement sectionTypeDD = wait
				.until(ExpectedConditions.elementToBeClickable(editCollectionSectionTypeDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", sectionTypeDD);
		actionPause(500);
		js.executeScript("arguments[0].click();", sectionTypeDD);
		System.out.println(GREEN + "[INFO] Opened Collection Section Type Dropdown for update." + RESET);
		actionPause(800);

		List<WebElement> sectionOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
				"//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a | //ul[@class='select-options-menu']//li[@class='option-item']")));

		if (sectionOpts != null && !sectionOpts.isEmpty()) {
			int randomSectionIdx = random.nextInt(sectionOpts.size());
			WebElement selectedSection = sectionOpts.get(randomSectionIdx);
			savedCollectionSectionType = selectedSection.getText().trim();
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'nearest'});", selectedSection);
			actionPause(400);
			js.executeScript("arguments[0].click();", selectedSection);
			System.out.println(PURPLE + "[SELECTED] Updated Section Type: " + savedCollectionSectionType + RESET);
		}
		actionPause(600);

		WebElement productDD = wait.until(ExpectedConditions.elementToBeClickable(editCollectionProductDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", productDD);
		actionPause(500);
		js.executeScript("arguments[0].click();", productDD);
		System.out.println(GREEN + "[INFO] Opened Collection Product Dropdown for update." + RESET);
		actionPause(800);

		List<WebElement> productOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
				"//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a | //ul[@class='select-options-menu']//li[@class='option-item']")));

		if (productOpts != null && !productOpts.isEmpty()) {
			savedCollectionProductCount = Math.min(random.nextInt(3) + 1, productOpts.size());
			System.out.println(CYAN + "[INFO] Selecting " + savedCollectionProductCount
					+ " random product(s) for updated collection..." + RESET);

			for (int i = 0; i < savedCollectionProductCount; i++) {
				int randomIndex = random.nextInt(productOpts.size());
				WebElement prodOption = productOpts.get(randomIndex);
				String prodText = prodOption.getText().trim();
				actionPause(600);
				js.executeScript("arguments[0].click();", prodOption);
				System.out.println(
						PURPLE + "[SELECTED] Updated Collection Product [" + (i + 1) + "]: " + prodText + RESET);
			}
		}
		try {
			js.executeScript("arguments[0].click();", productDD);
		} catch (Exception e) {
			productDD.click();
		}
		actionPause(500);

		savedCollectionSortBy = "2";
		WebElement sortField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@id='sort_by'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", sortField);
		actionPause(500);

		try {
			sortField.clear();
			sortField.sendKeys(savedCollectionSortBy);
		} catch (Exception e) {
			js.executeScript(
					"arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('input')); arguments[0].dispatchEvent(new Event('change'));",
					sortField, savedCollectionSortBy);
		}
		System.out.println(CYAN + "[DATA] Updated Sort By value: " + savedCollectionSortBy + RESET);

		savedCollectionStyle = "Updated Modern Collection Style Layout - Automated Test";
		WebElement styleField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//textarea[@id='style'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", styleField);
		actionPause(500);

		try {
			styleField.clear();
			styleField.sendKeys(savedCollectionStyle);
		} catch (Exception e) {
			js.executeScript(
					"arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('input')); arguments[0].dispatchEvent(new Event('change'));",
					styleField, savedCollectionStyle);
		}
		System.out.println(CYAN + "[DATA] Updated Style input: " + savedCollectionStyle + RESET);

		WebElement[] allToggles = { collectionstautsToggle, isAccessoriesToggle, isGiftToggle, isSaleToggle,
				isReturnableToggle, isExchangeableToggle, isCancelLabelToggle };

		String[] toggleNames = { "Collection Status", "Is Accessories", "Is Gift", "Is Sale", "Is Returnable",
				"Is Exchangeable", "Is Cancel Label" };

		int[] domIndices = { 1, 2, 3, 4, 5, 6, 7 };

		List<Integer> indices = new ArrayList<>();
		for (int i = 0; i < allToggles.length; i++) {
			indices.add(i);
		}
		java.util.Collections.shuffle(indices, random);
		int firstChoice = indices.get(0);
		int secondChoice = indices.get(1);

		System.out.println(CYAN + "[INFO] Randomly chosen updated toggles to enable: [" + toggleNames[firstChoice]
				+ "] and [" + toggleNames[secondChoice] + "]" + RESET);

		for (int k = 0; k < allToggles.length; k++) {
			WebElement toggle = allToggles[k];
			boolean shouldBeOn = (k == firstChoice || k == secondChoice);

			if (toggle != null) {
				try {
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", toggle);
					actionPause(300);

					boolean isCurrentlySelected = toggle.isSelected();
					if (shouldBeOn && !isCurrentlySelected) {
						js.executeScript("arguments[0].click();", toggle);
					} else if (!shouldBeOn && isCurrentlySelected) {
						js.executeScript("arguments[0].click();", toggle);
					}
				} catch (Exception e) {
					try {
						int domIndex = domIndices[k];
						WebElement parentLabel = driver.findElement(By
								.xpath("(//div[@class='custom-control custom-switch ms-3'])[" + domIndex + "]//label"));
						js.executeScript("arguments[0].click();", parentLabel);
					} catch (Exception ignored) {
					}
				}
			}

			if (k == 0)
				savedCollectionStatusActive = shouldBeOn;
			else if (k == 1)
				savedIsAccessoriesActive = shouldBeOn;
			else if (k == 2)
				savedIsGiftActive = shouldBeOn;
			else if (k == 3)
				savedIsSaleActive = shouldBeOn;
			else if (k == 4)
				savedIsReturnableActive = shouldBeOn;
			else if (k == 5)
				savedIsExchangeableActive = shouldBeOn;
			else if (k == 6)
				savedIsCancelLabelActive = shouldBeOn;
		}
		actionPause(800);

		WebElement saleColDD = wait.until(ExpectedConditions.elementToBeClickable(editSaleCollectionDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", saleColDD);
		actionPause(500);
		js.executeScript("arguments[0].click();", saleColDD);
		actionPause(800);

		List<WebElement> saleColOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
				"//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a | //ul[@class='select-options-menu']//li[@class='option-item']")));

		if (saleColOpts != null && !saleColOpts.isEmpty()) {
			WebElement selectedOpt = saleColOpts.get(random.nextInt(saleColOpts.size()));
			savedSaleCollection = selectedOpt.getText().trim();
			js.executeScript("arguments[0].click();", selectedOpt);
		}
		actionPause(600);

		WebElement saleTypeDD = wait.until(ExpectedConditions.elementToBeClickable(editSaleTypeDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", saleTypeDD);
		actionPause(500);
		js.executeScript("arguments[0].click();", saleTypeDD);
		actionPause(800);

		List<WebElement> saleTypeOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
				"//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a | //ul[@class='select-options-menu']//li[@class='option-item']")));

		if (saleTypeOpts != null && !saleTypeOpts.isEmpty()) {
			WebElement selectedOpt = saleTypeOpts.get(random.nextInt(saleTypeOpts.size()));
			savedSaleType = selectedOpt.getText().trim();
			js.executeScript("arguments[0].click();", selectedOpt);
		}
		actionPause(600);

		savedOfferPriceText = "Updated Promo Offer " + (random.nextInt(90) + 10) + "% OFF";
		WebElement offerField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//textarea[@id='offer_price'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", offerField);
		actionPause(500);

		try {
			offerField.clear();
			offerField.sendKeys(savedOfferPriceText);
		} catch (Exception e) {
			js.executeScript(
					"arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('input')); arguments[0].dispatchEvent(new Event('change'));",
					offerField, savedOfferPriceText);
		}
		System.out.println(CYAN + "[DATA] Updated Offer Price/Type text: " + savedOfferPriceText + RESET);
		actionPause(500);

		String alphabetSeo = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder seoSb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			seoSb.append(alphabetSeo.charAt(random.nextInt(alphabetSeo.length())));
		}
		String seoAlphaId = seoSb.toString();

		savedMetaTitle = "Updated Meta Title " + seoAlphaId;
		savedAltTag = "Updated Alt Tag " + seoAlphaId;
		savedMetaKey = "updated meta keyword " + seoAlphaId;
		savedSeoTag = "UpdatedTag " + seoAlphaId;
		savedMetaDesc = "Updated automated seo description test for collection update without numbers " + seoAlphaId;

		WebElement metaTitleField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@id='seometatitle'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", metaTitleField);
		actionPause(600);
		safeSendKeys(metaTitleField, savedMetaTitle);

		WebElement altField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@id='seoaltimagetag'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", altField);
		actionPause(600);
		safeSendKeys(altField, savedAltTag);

		WebElement metaKeyField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@id='seometakey'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", metaKeyField);
		actionPause(600);
		safeSendKeys(metaKeyField, savedMetaKey);

		WebElement tagField = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@id='seotag'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", tagField);
		actionPause(600);
		safeSendKeys(tagField, savedSeoTag);

		WebElement metaDescField = wait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("(//textarea[@id='Meta-description-first'])[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", metaDescField);
		actionPause(600);
		safeSendKeys(metaDescField, savedMetaDesc);
		System.out.println(CYAN + "[DATA] All SEO fields updated successfully." + RESET);

		clickSaveButton();
	}

	// TC-03
	public void createCollectionAsDraft() {
		Random random = new Random();
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement createBtn = wait.until(ExpectedConditions.elementToBeClickable(createCollectionBtn));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", createBtn);
		actionPause(600);

		try {
			createBtn.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", createBtn);
		}
		System.out.println(GREEN + "[INFO] Clicked on 'Create Collection' button. Opening form..." + RESET);
		actionPause(1000);

		WebElement nameField = wait.until(ExpectedConditions.visibilityOf(collectionName));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", nameField);
		actionPause(500);

		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder("DraftCol");
		for (int i = 0; i < 6; i++) {
			sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
		}
		savedCollectionName = sb.toString();
		nameField.clear();
		nameField.sendKeys(savedCollectionName);
		System.out.println(CYAN + "[DATA] Generated & Entered Draft Collection Name: " + savedCollectionName + RESET);

		WebElement brandDD = wait.until(ExpectedConditions.elementToBeClickable(collectionBrandTypeDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", brandDD);
		actionPause(500);
		js.executeScript("arguments[0].click();", brandDD);
		System.out.println(GREEN + "[INFO] Opened Brand Type Dropdown." + RESET);
		actionPause(800);

		List<WebElement> brandOpts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
				"//ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a | //ul[@class='select-options-menu']//li[@class='option-item']")));

		if (brandOpts != null && !brandOpts.isEmpty()) {
			int randomBrandIdx = random.nextInt(brandOpts.size());
			WebElement selectedBrand = brandOpts.get(randomBrandIdx);
			savedCollectionBrand = selectedBrand.getText().trim();

			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'nearest'});", selectedBrand);
			actionPause(400);
			js.executeScript("arguments[0].click();", selectedBrand);
			System.out.println(PURPLE + BOLD + "[SELECTED] Brand Type Option: " + savedCollectionBrand + RESET);
		}
		actionPause(600);

		WebElement dateField = wait.until(ExpectedConditions.visibilityOf(customDeliveryDate));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", dateField);
		actionPause(500);

		savedCollectionDeliveryDays = random.nextInt(14) + 1;
		dateField.clear();
		dateField.sendKeys(String.valueOf(savedCollectionDeliveryDays));
		System.out.println(CYAN + "[DATA] Entered Custom Delivery Days: " + savedCollectionDeliveryDays + RESET);
		actionPause(600);

		WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(backBtn));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", backButton);
		actionPause(600);

		try {
			backButton.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", backButton);
		}
		System.out.println(GREEN + "[INFO] Clicked on Back button to prompt draft save." + RESET);
		actionPause(1000);

		WebElement draftButton = wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftBtn));
		try {
			draftButton.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", draftButton);
		}
		System.out.println(GREEN + "[SUCCESS] Clicked on 'Save Draft' button. Collection saved as draft." + RESET);
		actionPause(2000);
	}

	public void verifyDraftCollectionOnListingPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		org.testng.asserts.SoftAssert softAssert = new org.testng.asserts.SoftAssert();

		actionPause(2000); // Allow listing table to render

		WebElement collectionNameCell = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//td)[1]")));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", collectionNameCell);
		actionPause(500);

		String actualCellText = collectionNameCell.getText().trim();

		System.out.println(CYAN + BOLD + "--- [VERIFY: DRAFT COLLECTION LISTING PAGE] ---" + RESET);
		System.out.println(PURPLE + BOLD + "Expected Collection Name -> [" + savedCollectionName + "]" + RESET);
		System.out.println(PURPLE + BOLD + "Expected Status          -> [Draft]" + RESET);
		System.out.println(PURPLE + BOLD + "Actual Cell Text         -> [" + actualCellText + "]" + RESET);

		softAssert.assertTrue(actualCellText.contains(savedCollectionName),
				"Draft Collection Name mismatch! Expected to find '" + savedCollectionName + "' inside cell text: '"
						+ actualCellText + "'");
		softAssert.assertTrue(actualCellText.toLowerCase().contains("draft"),
				"Collection status 'Draft' not found inside cell text: '" + actualCellText + "'");

		softAssert.assertAll();
		System.out.println(
				GREEN + "[SUCCESS] Draft collection name and status verified successfully from (//td)[1]!" + RESET);
	}

	public void deleteDraftCollection() {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement threeDot = wait.until(ExpectedConditions.elementToBeClickable(threedotBtn));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", threeDot);
		actionPause(600);

		try {
			threeDot.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", threeDot);
		}
		System.out.println(GREEN + "[INFO] Clicked on Three-Dot Actions menu for deletion." + RESET);
		actionPause(500);

		WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
		try {
			deleteOption.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", deleteOption);
		}
		System.out.println(GREEN + "[INFO] Clicked on Delete option. Opening confirmation popup..." + RESET);
		actionPause(800);

		WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteBtn));
		try {
			confirmBtn.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", confirmBtn);
		}
		System.out.println(GREEN + "[SUCCESS] Clicked Confirm Delete button. Category deleted." + RESET);
		actionPause(2000); // Wait for table refresh after deletion
	}

	public void verifyCollectionDeletedFromListingPage() {
		actionPause(2000); // Allow table to update

		List<WebElement> nameCells = driver.findElements(By.xpath("(//td)[1]"));
		boolean isCollectionStillPresent = false;

		for (WebElement cell : nameCells) {
			if (cell.getText().trim().contains(savedCollectionName)) {
				isCollectionStillPresent = true;
				break;
			}
		}

		System.out.println(CYAN + BOLD + "--- [VERIFY: COLLECTION DELETION CHECK] ---" + RESET);
		System.out.println(PURPLE + BOLD + "Checking if deleted collection '" + savedCollectionName
				+ "' is absent from listing..." + RESET);

		Assert.assertFalse(isCollectionStillPresent,
				"Deleted collection '" + savedCollectionName + "' is still displayed on the listing page!");
		System.out.println(GREEN + BOLD
				+ "[SUCCESS] Verified that deleted collection is no longer displayed on the listing page!" + RESET);
	}

	// TC-04
	public void verifyCollectionOnProductCreationPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		org.testng.asserts.SoftAssert softAssert = new org.testng.asserts.SoftAssert();

		actionPause(1000);
		try {
			WebElement statusSwitch = wait.until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("(//div[@class='custom-control custom-switch'])[1]")));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", statusSwitch);
			actionPause(500);

			boolean isChecked = false;
			try {
				WebElement inputCheckbox = driver.findElement(
						By.xpath("(//div[@class='custom-control custom-switch'])[1]//input[@type='checkbox']"));
				isChecked = inputCheckbox.isSelected();
			} catch (Exception e) {
				String classAttr = statusSwitch.getAttribute("class");
				isChecked = classAttr.contains("checked") || classAttr.contains("active");
			}

			if (!isChecked) {
				try {
					WebElement toggleLabel = driver
							.findElement(By.xpath("(//div[@class='custom-control custom-switch'])[1]//label"));
					js.executeScript("arguments[0].click();", toggleLabel);
				} catch (Exception e) {
					js.executeScript("arguments[0].click();", statusSwitch);
				}
				System.out.println(GREEN + "[INFO] Collection status was Inactive. Toggled ON to Active." + RESET);
				actionPause(1000); // Wait for confirmation popup to appear

				try {
					WebElement confirmActivateBtn = wait.until(
							ExpectedConditions.elementToBeClickable(By.xpath("(//button[@id='activate_btn'])[1]")));
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
							confirmActivateBtn);
					actionPause(500);
					js.executeScript("arguments[0].click();", confirmActivateBtn);
					System.out.println(GREEN + "[SUCCESS] Clicked confirmation on activation popup." + RESET);
				} catch (Exception ex) {
					System.out.println(
							PURPLE + "[WARNING] Activation confirmation popup button not found or already dismissed: "
									+ ex.getMessage() + RESET);
				}

				actionPause(1500); // Wait for status update to save
			} else {
				System.out.println(GREEN + "[INFO] Collection status is already Active." + RESET);
			}
		} catch (Exception e) {
			System.out.println(
					PURPLE + "[WARNING] Could not verify/toggle collection status switch: " + e.getMessage() + RESET);
		}

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
				By.xpath("//ul[@class='select-options-menu']//li[@class='option-item']")));

		boolean brandFound = false;
		for (WebElement option : brandOpts) {
			String brandText = option.getText().trim();
			if (brandText.equalsIgnoreCase(savedCollectionBrand)) {
				js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'nearest'});", option);
				actionPause(400);
				js.executeScript("arguments[0].click();", option);
				brandFound = true;
				System.out.println(PURPLE + "[SELECTED] Brand Type matched and selected: " + brandText + RESET);
				break;
			}
		}
		softAssert.assertTrue(brandFound, "Could not find matching brand type '" + savedCollectionBrand
				+ "' in Product Creation brand dropdown!");
		actionPause(1000);

		WebElement colDD = wait.until(ExpectedConditions.elementToBeClickable(collectionDropdown));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", colDD);
		actionPause(600);
		colDD.click();
		System.out.println(
				GREEN + "[INFO] Opened Collection Dropdown to verify presence of: " + savedCollectionName + RESET);
		actionPause(800);

		List<WebElement> collectionDropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
				.xpath("//ul[@class='select-options-menu']//li[@class='option-item'] | //ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a")));

		boolean collectionFound = false;
		String actualFoundText = "Not Found";
		for (WebElement colOption : collectionDropdownOptions) {
			String optionText = colOption.getText().trim();
			if (optionText.contains(savedCollectionName)) {
				collectionFound = true;
				actualFoundText = optionText;
				System.out.println(
						GREEN + "[FOUND] Collection successfully reflected in dropdown: " + optionText + RESET);
				break;
			}
		}

		System.out.println(CYAN + "--- [VERIFY: PRODUCT CREATION PAGE - COLLECTION REFLECTION] ---" + RESET);
		System.out.println(PURPLE + "Target Collection Name -> Expected: [" + savedCollectionName
				+ "] | Actual Found: [" + actualFoundText + "]" + RESET);

		softAssert.assertTrue(collectionFound, "Newly created collection '" + savedCollectionName
				+ "' is NOT reflected in the Product Creation collection dropdown!");

		softAssert.assertAll();
		System.out.println(GREEN
				+ "[SUCCESS] Collection successfully verified on the Product Creation page! Test Case Passed." + RESET);
	}

	// TC-05
	private By bulkCheckbox(int index) {
		return By.xpath("(//label[@class='crud_bulk_actions_checkbox custom-checkbox'])[" + index + "]");
	}

	protected boolean savedExpectedBulkStatus;

	private final By noActionNeededBtn = By.xpath("(//button[normalize-space()='No Action Needed'])[1]");
	private final By activateConfirmBtn = By.xpath("(//button[@id='confirmCollectionActivateBtn'])[1]");
	private final By deactivateConfirmBtn = By.xpath("(//button[@id='confirmCollectionDeactivateBtn'])[1]");

	private void clickElement(By locator, int scrollOffset, String logMessage) {
		clickElement(wait.until(ExpectedConditions.elementToBeClickable(locator)), scrollOffset, logMessage);
	}

	private void clickElement(WebElement element, int scrollOffset, String logMessage) {
		WebElement clickable = wait.until(ExpectedConditions.elementToBeClickable(element));
		scrollToCenter(clickable, scrollOffset);
		clickWithJsFallback(clickable);
		System.out.println(GREEN + logMessage + RESET);
	}

	private void openBulkActionAndSelect(String optionText) {
		clickElement(bulkActionBtn, 600, "[INFO] Clicked on Bulk Action button.");
		actionPause(800);

		By optionXpath = By.xpath(
				"(//div[contains(@class,'dropdown-menu dropdown-menu-right shadow-sm show')])[1]//a[normalize-space()='"
						+ optionText + "']");
		clickElement(optionXpath, 500, "[SUCCESS] Clicked on '" + optionText + "' option. Popup opened.");
		actionPause(1000);
	}

	public void selectMultipleCollectionsAndOpenSettings() {
		actionPause(2000);

		for (int i = 3; i <= 4; i++) {
			clickElement(bulkCheckbox(i), 400, "[INFO] Selected collection checkbox option index: " + (i - 2));
		}
		actionPause(1000);

		boolean pickActive = new java.util.Random().nextBoolean();
		String firstAction = pickActive ? "Set as Active" : "Set as Inactive";
		By firstConfirmBtn = pickActive ? activateConfirmBtn : deactivateConfirmBtn;
		System.out.println(CYAN + "[INFO] Randomly selected first action: " + firstAction + RESET);

		openBulkActionAndSelect(firstAction);

		wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(firstConfirmBtn),
				ExpectedConditions.visibilityOfElementLocated(noActionNeededBtn)));

		boolean noActionShown = !driver.findElements(noActionNeededBtn).isEmpty()
				&& driver.findElement(noActionNeededBtn).isDisplayed();

		if (!noActionShown) {
			clickElement(firstConfirmBtn, 500, "[SUCCESS] Clicked confirmation button for '" + firstAction + "'.");
			savedExpectedBulkStatus = pickActive;
		} else {
			clickElement(noActionNeededBtn, 500,
					"[INFO] '" + firstAction + "' showed 'No Action Needed'. Closing popup...");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(noActionNeededBtn));
			actionPause(500);

			boolean oppositeActive = !pickActive;
			String oppositeAction = oppositeActive ? "Set as Active" : "Set as Inactive";
			By oppositeConfirmBtn = oppositeActive ? activateConfirmBtn : deactivateConfirmBtn;

			openBulkActionAndSelect(oppositeAction);
			clickElement(oppositeConfirmBtn, 500,
					"[SUCCESS] Clicked confirmation button for '" + oppositeAction + "'.");
			savedExpectedBulkStatus = oppositeActive;
		}

		System.out.println(
				PURPLE + "[INFO] Expected final status: " + (savedExpectedBulkStatus ? "Active" : "Inactive") + RESET);
		actionPause(2500);
	}

	private boolean isCollectionToggleActive(int tdIndex) {
		By cell = By.xpath("(//td)[" + tdIndex + "]");
		try {
			return driver.findElement(By.xpath("(//td)[" + tdIndex + "]//input[@type='checkbox']")).isSelected();
		} catch (Exception e) {
			String text = driver.findElement(cell).getText().trim().toLowerCase();
			return text.contains("active") || text.contains("yes") || text.contains("true");
		}
	}

	public void verifyCollectionBulkStatusOnListingPage() {
		org.testng.asserts.SoftAssert softAssert = new org.testng.asserts.SoftAssert();

		actionPause(2000);

		int statusTdIndex = 6; // Adjust according to your listing table column index for status
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//td)[" + statusTdIndex + "]")));

		boolean isStatusActive = isCollectionToggleActive(statusTdIndex);

		System.out.println(CYAN + "--- [VERIFY: COLLECTION BULK STATUS UPDATE] ---" + RESET);
		System.out.println(PURPLE + "Status Active -> Expected: [" + savedExpectedBulkStatus + "] | Actual: ["
				+ isStatusActive + "]" + RESET);

		softAssert.assertEquals(isStatusActive, savedExpectedBulkStatus,
				"Collection bulk status mismatch on Listing page!");

		softAssert.assertAll();
		System.out.println(GREEN + "[SUCCESS] Collection bulk status verified successfully on listing page!" + RESET);
	}

	//TC-01
	public void createCollectionFlow() {
		fillCreateCollectionForm();
		uploadCollectionBanners();
		fillSaleInfoSection();
		fillSeoDetails();
		verifyCollectionOnListingPage();
		verifyCollectionOnPreviewPage();
	}

	//TC-02
	public void editCollectionFlow() {
		clickEditFromActionMenu();
		editCollectionForm();
		verifyCollectionOnListingPage();
		verifyCollectionOnPreviewPage();
	}

	//TC-03
	public void draftAndDeleteCollectionFlow() {
		createCollectionAsDraft();
		verifyDraftCollectionOnListingPage();
		deleteDraftCollection();
		verifyCollectionDeletedFromListingPage();
	}

	//TC-04
	public void activeCollectionFlow() {
		fillCreateCollectionForm();
		uploadCollectionBanners();
		fillSaleInfoSection();
		fillSeoDetails();
		verifyCollectionOnProductCreationPage();
	}

	//TC-05
	public void bulkUpdateCategorySettingsFlow() {
		selectMultipleCollectionsAndOpenSettings();
		verifyCollectionBulkStatusOnListingPage();
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
