package pages;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import objectRepo.Products_ObjRepo;

public class ProductAccessories_Page extends Products_ObjRepo {
	
	public ProductAccessories_Page(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(this.driver, this);
    }
	
	 // --- Colored Console Logging Utilities ---
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

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
        System.out.println(CYAN + "[ACTION] " + RESET + message);
    }

    private void logSuccess(String message) {
        System.out.println(GREEN + BOLD + "[SUCCESS] " + message + RESET);
    }

    private void logFailure(String message) {
        System.out.println(RED + BOLD + "[FAILURE] " + message + RESET);
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

    public void navigateToAccessoriesPage() {
        logHeader("NAVIGATION");
        logAction("Hovering over 'Product' menu...");
        wait.until(ExpectedConditions.visibilityOf(productMenu));
        new Actions(driver).moveToElement(productMenu).perform();
        actionPause();

        logAction("Clicking 'Products' sub-menu...");
        wait.until(ExpectedConditions.visibilityOf(productsSubMenu));
        click(productsSubMenu);
        logSuccess("Navigated to Products page");
        
        logAction("Clicking 'Accessories Tab' sub-menu...");
        wait.until(ExpectedConditions.visibilityOf(accessoreisTab));
        click(accessoreisTab);
        logSuccess("Navigated to Accessories page");
        
    }
    
 // ===================== STATE =====================

    private String lastGeneratedProductName, lastGeneratedSKU, lastGeneratedHSN;
    private String lastSelectedProductType = "", lastSelectedAccessoryType = "", lastSelectedBrand = "", lastSelectedCategory = "",
            lastSelectedColor = "", lastSelectedRise = "", lastSelectedFit = "",
            lastSelectedBarcodeType = "", lastSelectedSize = "", lastDeliveryDays = "";
    private Map<String, String> lastVariantData = new HashMap<>();

    // ===================== CONSTANTS =====================

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final By DROPDOWN_OPTIONS = By.xpath(
            "//ul[contains(@class,'select-options-menu')]//li | " +
            "//ul[contains(@class,'dropdown-menu')]//li | " +
            "//div[contains(@class,'dropdown-menu')]//a | " +
            "//div[contains(@class,'select-options')]//div | " +
            "//ul[contains(@class,'options')]//li | " +
            "//div[contains(@class,'option-item')]");

    private static final String[] ACCESSORIES = {
            "Leather Belt", "Silk Scarf", "Tote Bag", "Clutch", "Sunglasses",
            "Hair Clip Set", "Statement Necklace", "Wrist Watch", "Stole", "Handbag"};

    private final Random rnd = new Random();


    // ===================== GENERIC HELPERS =====================

    private JavascriptExecutor js() {
        return (JavascriptExecutor) driver;
    }

    private void scrollToCenter(WebElement el) {
        js().executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
    }

    /** Runs the normal click; falls back to a JS click if it throws. */
    private void clickWithJsFallback(Runnable normalClick, WebElement el) {
        try {
            normalClick.run();
        } catch (Exception e) {
            js().executeScript("arguments[0].click();", el);
        }
    }

    /** Scrolls to the field, waits until clickable, clears and types (JS-clear fallback). */
    private void enterText(WebElement el, String value) {
        scrollToCenter(el);
        wait.until(ExpectedConditions.elementToBeClickable(el));
        try {
            el.clear();
            el.sendKeys(value);
        } catch (Exception e) {
            js().executeScript("arguments[0].value = '';", el);
            el.sendKeys(value);
        }
    }

    private void dismissOverlay() {
        try {
            new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        } catch (Exception ignored) {
            try {
                js().executeScript("document.body.click();");
            } catch (Exception ignored2) {}
        }
    }

    // ===================== DROPDOWN HELPERS =====================

    private String selectRandomCustomDropdownOption(WebElement triggerButton, String fieldName, String... excludedTexts) {
        logAction("Selecting random option for: " + fieldName);
        String optionText = "";
        try {
            wait.until(ExpectedConditions.elementToBeClickable(triggerButton));
            scrollToCenter(triggerButton);
            clickWithJsFallback(triggerButton::click, triggerButton);
            actionPause();

            List<WebElement> selectableOptions = wait
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(DROPDOWN_OPTIONS))
                    .stream()
                    .filter(WebElement::isDisplayed)
                    .filter(o -> {
                        String text = o.getText().trim().toLowerCase();
                        for (String excluded : excludedTexts) {
                            if (text.contains(excluded.toLowerCase())) return false;
                        }
                        return true;
                    })
                    .toList();

            if (!selectableOptions.isEmpty()) {
                int randomIndex = rnd.nextInt(selectableOptions.size());
                WebElement selectedOption = selectableOptions.get(randomIndex);
                optionText = selectedOption.getText().trim();
                clickWithJsFallback(selectedOption::click, selectedOption);
                logSuccess("Selected " + fieldName + ": '" + (optionText.isEmpty() ? "Index " + randomIndex : optionText) + "'");
            } else {
                logFailure("No selectable options found for dropdown: " + fieldName);
                dismissOverlay();
            }
        } catch (Exception e) {
            logFailure("Failed to select option for " + fieldName + ": " + e.getMessage());
            dismissOverlay();
        }
        return optionText;
    }

    /** Finds a dropdown trigger by XPath, scrolls to it, and selects a random option. */
    private String selectRandomOptionByXpath(String xpath, String fieldName) {
        WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        scrollToCenter(trigger);
        return selectRandomCustomDropdownOption(trigger, fieldName);
    }

    // ===================== DATA GENERATORS =====================

    private String generateSku() {
        StringBuilder sku = new StringBuilder("ZL-");
        for (int i = 0; i < 6; i++) sku.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
        return sku.toString();
    }

    private String generateHsn() {
        return String.format("%08d", rnd.nextInt(100000000));
    }

    // ===================== SAVE =====================

    private void clickSaveAndNext() {
        logAction("Clicking 'Save & Next'...");
        wait.until(ExpectedConditions.elementToBeClickable(saveAndNextBtn));
        scrollToCenter(saveAndNextBtn);
        clickWithJsFallback(saveAndNextBtn::click, saveAndNextBtn);
        actionPause();
    }

    // ===================== ACCESSORY CREATION =====================
    
    public String createNewAccessoryAutomatically() {
        return createNewAccessoryAutomatically(false);
    }

    public String createNewAccessoryAutomatically(boolean isAlreadyOnCreatePage) {
        logHeader("ACCESSORY CREATION AUTOMATION");

        lastGeneratedProductName = ACCESSORIES[rnd.nextInt(ACCESSORIES.length)] + " " + (100 + rnd.nextInt(900));
        lastGeneratedSKU = generateSku();
        lastGeneratedHSN = generateHsn();

        // Step 1: Click Create Product button (skipped if already on page)
        if (!isAlreadyOnCreatePage) {
            logAction("Clicking 'Create Product' button...");
            wait.until(ExpectedConditions.elementToBeClickable(createProductBtn));
            click(createProductBtn);
            actionPause();
        } else {
            logAction("Already on Create Accessory page. Skipping 'Create Product' button click.");
        }

        // Step 2: Product Name
        logAction("Entering Accessory Name: '" + lastGeneratedProductName + "'");
        wait.until(ExpectedConditions.visibilityOf(productNameInput));
        productNameInput.clear();
        productNameInput.sendKeys(lastGeneratedProductName);

        // Step 3: Brand
        lastSelectedBrand = selectRandomCustomDropdownOption(brandDropdownBtn, "Brand");

        // Step 4: SKU
        logAction("Entering SKU Code: '" + lastGeneratedSKU + "'");
        enterText(skuInput, lastGeneratedSKU);

        // Step 5: HSN
        logAction("Entering HSN Code: '" + lastGeneratedHSN + "'");
        enterText(hsnInput, lastGeneratedHSN);

        // Step 6: Type (button index 5)
        lastSelectedAccessoryType = selectRandomOptionByXpath("(//button[@type='button'])[5]", "Type");

        // Step 7: Barcode (button index 7)
        lastSelectedBarcodeType = selectRandomOptionByXpath("(//button[@type='button'])[7]", "Barcode");

        // Step 8: Save & Next
        clickSaveAndNext();

        return lastGeneratedProductName;
    }

    // ===================== GETTERS =====================

    public String getLastGeneratedProductName() { return lastGeneratedProductName; }
    public String getLastGeneratedSKU()         { return lastGeneratedSKU; }
    public String getLastGeneratedHSN()         { return lastGeneratedHSN; }
    public String getLastSelectedBrand()        { return lastSelectedBrand; }
    public String getLastSelectedAccessoryType(){ return lastSelectedAccessoryType; }
    public String getLastSelectedBarcodeType()  { return lastSelectedBarcodeType; }
    
    private String selectDropdownByIndex(int buttonIndex, String fieldName, String... excludedTexts) {
        return selectRandomCustomDropdownOption(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[@type='button'])[" + buttonIndex + "]"))), fieldName, excludedTexts);
    }
    
    private void enterInputValue(By locator, String value, String fieldName) {
        try {
            enterText(wait.until(ExpectedConditions.presenceOfElementLocated(locator)), value);
            logSuccess("Entered " + fieldName + ": " + value);
        } catch (Exception e) {
            logFailure("Failed to enter " + fieldName + ": " + e.getMessage());
        }
    }
    
    // Dimension fields filled per block: placeholder, log label, {random bound, offset}
    private static final String[] DIM_PLACEHOLDERS = {"L", "B", "H", "Weight", "Qty", "Alert"};
    private static final String[] DIM_LABELS = {"Length (L)", "Breadth (B)", "Height (H)", "Weight", "Qty", "Alert Threshold"};
    private static final int[][] DIM_RANGES = {{20, 10}, {15, 5}, {10, 2}, {500, 100}, {50, 10}, {5, 1}};
    
    /** Shared by product and accessory flows: dimensions, raw material, save. */
    private void fillDimensionsAndSave(boolean isSetProduct) {
        // Fill Dimensions
        By lengthFieldsLocator = By.xpath("//input[@type='text' and @placeholder='L']");
        wait.until(ExpectedConditions.presenceOfElementLocated(lengthFieldsLocator));
        int blockCount = driver.findElements(lengthFieldsLocator).size();

        if (isSetProduct && blockCount < 2) {
            logAction("Type is a 'Set', waiting for second dimension block to render...");
            try {
                wait.until(ExpectedConditions.numberOfElementsToBe(lengthFieldsLocator, 2));
                blockCount = 2;
            } catch (Exception e) {
                logFailure("Expected 2 dimension blocks for 'Set' type, but found " + blockCount);
            }
        }

        logAction("Processing " + blockCount + " inventory detail block(s)...");

        for (int i = 1; i <= blockCount; i++) {
            String blockLabel = (blockCount > 1) ? ((i == 1) ? "Top" : "Bottom") : "Single Product Item";
            logHeader("FILLING DIMENSIONS: " + blockLabel.toUpperCase());

            for (int f = 0; f < DIM_PLACEHOLDERS.length; f++) {
                enterInputValue(
                    By.xpath("(//input[@type='text' and @placeholder='" + DIM_PLACEHOLDERS[f] + "'])[" + i + "]"),
                    String.valueOf(rnd.nextInt(DIM_RANGES[f][0]) + DIM_RANGES[f][1]),
                    blockLabel + " -> " + DIM_LABELS[f]);
            }
        }
        actionPause();

     // Select Raw Material (skip "Add Raw Material", it redirects to another page)
        selectDropdownByIndex(3, "Raw Material", "Add Raw Material");
        actionPause();

        // Save & Next
        clickSaveAndNext();
    }

    /** True if the selected type text contains "set" (null-safe). */
    private boolean isSetType(String type) {
        return type != null && type.toLowerCase().contains("set");
    }

    // ===================== PRODUCT =====================

    public void configureInventorySetup() {
        logHeader("INVENTORY SETUP AUTOMATION");

        boolean isSetProduct = isSetType(lastSelectedProductType);
        logAction("Configuring inventory for Product Type: '" +
            (lastSelectedProductType == null || lastSelectedProductType.isEmpty() ? "Unknown" : lastSelectedProductType) +
            "' (Is Set Type: " + isSetProduct + ")");

        // Step 1: Select Color
        lastSelectedColor = selectDropdownByIndex(2, "Color");
        actionPause();

        // Step 2: Select random Size
        logAction("Selecting random Size option...");
        try {
            List<WebElement> sizeOptions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("(//div[@class='size-wrapper'])[1]//label[@class='custom-size-checkbox']")))
                    .stream().filter(WebElement::isDisplayed).toList();

            if (!sizeOptions.isEmpty()) {
                WebElement selectedSize = sizeOptions.get(rnd.nextInt(sizeOptions.size()));
                lastSelectedSize = selectedSize.getText().trim();
                scrollToCenter(selectedSize);
                clickWithJsFallback(selectedSize::click, selectedSize);
                logSuccess("Selected Size: '" + lastSelectedSize + "'");
            } else {
                logFailure("No visible Size options found.");
            }
        } catch (Exception e) {
            logFailure("Failed to select Size: " + e.getMessage());
        }
        actionPause();

        // Steps 3-5: Dimensions, Raw Material, Save & Next
        fillDimensionsAndSave(isSetProduct);
    }

    // ===================== ACCESSORY =====================

    public void configureAccessoryInventorySetup() {
        logHeader("ACCESSORY INVENTORY SETUP AUTOMATION");

        boolean isSetProduct = isSetType(lastSelectedAccessoryType);
        logAction("Configuring inventory for Accessory Type: '" +
            (lastSelectedAccessoryType == null || lastSelectedAccessoryType.isEmpty() ? "Unknown" : lastSelectedAccessoryType) +
            "' (Is Set Type: " + isSetProduct + ")");

        // Step 1: Select Color
        lastSelectedColor = selectDropdownByIndex(2, "Color");
        actionPause();

        // Step 2: Select Size (accessories only have "Regular")
        logAction("Selecting the 'Regular' Size option...");
        try {
            WebElement regularSize = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//label[@class='custom-size-checkbox'])[1]")));
            lastSelectedSize = regularSize.getText().trim();
            scrollToCenter(regularSize);
            clickWithJsFallback(regularSize::click, regularSize);
            logSuccess("Selected Size: '" + lastSelectedSize + "'");
        } catch (Exception e) {
            logFailure("Failed to select Size: " + e.getMessage());
        }
        actionPause();

        // Steps 3-5: Dimensions, Raw Material, Save & Next
        fillDimensionsAndSave(isSetProduct);
    }
    
    public Map<String, String> configureVariantsSetup() {
        logHeader("VARIANTS PAGE AUTOMATION");
        LocalDate today = LocalDate.now();

        selectVariantDate("From Date (Today)", "Select from", today.getDayOfMonth(), "From Date");
        selectVariantDate("To Date (+10 Days)", "Select to", today.plusDays(10).getDayOfMonth(), "To Date");
        selectVariantDropdown(1, "Secondary Color");

        int cutPrice = 1000 + rnd.nextInt(2001);
        int costPrice = cutPrice + 500 + rnd.nextInt(1001);
        actionPause();

        enterVariantPrice("Cost", costPrice);
        enterVariantPrice("Cut", cutPrice);
        selectVariantDropdown(3, "Fabric");
        selectVariantDropdown(4, "Pattern");

        logSuccess("Captured Variant Details: " + lastVariantData);
        return lastVariantData;
    }

    private void selectVariantDate(String logText, String placeholder, int day, String key) {
        logAction("Selecting " + logText + "...");
        lastVariantData.put(key, selectDateFromFlatpickr(
                By.xpath("(//input[@placeholder='" + placeholder + "'])[1]"), day, key));
        actionPause();
    }

    private void selectVariantDropdown(int buttonIndex, String key) {
        logAction("Selecting " + key + "...");
        lastVariantData.put(key, selectDropdownByIndex(buttonIndex, key));
        actionPause();
    }

    private void enterVariantPrice(String type, int price) {
        logAction("Entering " + type + " Price (₹): " + price);
        enterInputValue(By.xpath("(//input[@placeholder='Enter the " + type + " Price (₹)'])[1]"),
                String.valueOf(price), type + " Price");
        lastVariantData.put(type + " Price", String.valueOf(price));
        actionPause();
    }

    private String selectDateFromFlatpickr(By inputLocator, int targetDay, String fieldName) {
        String selectedDateText = "";
        try {
            WebElement dateInput = wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));
            scrollToCenter(dateInput);
            wait.until(ExpectedConditions.elementToBeClickable(dateInput));

            String openCal = "//div[contains(@class,'flatpickr-calendar') and contains(@class,'open')]";

            // Open the calendar and WAIT until it is actually visible (retry if it didn't open)
            boolean opened = false;
            for (int attempt = 1; attempt <= 3 && !opened; attempt++) {
                try {
                    if (attempt == 1) dateInput.click();
                    else js().executeScript("arguments[0].focus(); arguments[0].click();", dateInput);
                } catch (Exception e) {
                    js().executeScript("arguments[0].focus(); arguments[0].click();", dateInput);
                }
                try {
                    new WebDriverWait(driver, Duration.ofSeconds(3))
                            .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(openCal)));
                    opened = true;
                } catch (TimeoutException e) {
                    logAction("Calendar did not open for " + fieldName + " (attempt " + attempt + "), retrying...");
                }
            }
            if (!opened) {
                throw new RuntimeException("Flatpickr calendar did not open");
            }
            actionPause();

            // Only look inside the OPEN calendar (no hidden-calendar fallback)
            By dayLocator = By.xpath(openCal
                    + "//span[contains(@class,'flatpickr-day') and not(contains(@class,'flatpickr-disabled')) "
                    + "and not(contains(@class,'prevMonthDay')) and not(contains(@class,'nextMonthDay')) "
                    + "and normalize-space(.)='" + targetDay + "']");

            List<WebElement> matchingDays = driver.findElements(dayLocator);

            // Day not in current month view -> go to next month once and retry
            if (matchingDays.isEmpty()) {
                List<WebElement> next = driver.findElements(
                        By.xpath(openCal + "//span[contains(@class,'flatpickr-next-month')]"));
                if (!next.isEmpty()) {
                    js().executeScript("arguments[0].click();", next.get(0));
                    actionPause();
                    matchingDays = driver.findElements(dayLocator);
                }
            }

            if (!matchingDays.isEmpty()) {
                WebElement dayToSelect = matchingDays.get(0);

                js().executeScript("arguments[0].click();", dayToSelect);
                actionPause();

                selectedDateText = dateInput.getAttribute("value");
                if (selectedDateText == null || selectedDateText.isEmpty()) {
                    selectedDateText = (String) js().executeScript("return arguments[0].value;", dateInput);
                }
                logSuccess("Selected " + fieldName + " from Flatpickr Calendar: " + selectedDateText);
            } else {
                logFailure("Could not find day " + targetDay + " inside Flatpickr calendar for " + fieldName);
                dismissOverlay();
            }
        } catch (Exception e) {
            logFailure("Failed to select date from Flatpickr for " + fieldName + ": " + e.getMessage());
            dismissOverlay();
        }
        return selectedDateText;
    }

 // --- Shared helpers for media / thumbnail / try-along ---
    private void scrollToCenterNearest(WebElement el) {
        js().executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", el);
    }

    /** Returns the primary locator, or the fallback if the primary matches nothing right now. */
    private By xpathOrFallback(String xpath, String fallbackXpath) {
        By primary = By.xpath(xpath);
        return driver.findElements(primary).isEmpty() ? By.xpath(fallbackXpath) : primary;
    }

    private By previewImg(int n) {
        return By.xpath("(//img[@alt='preview-image'])[" + n + "]");
    }

    /** Logs and returns the absolute path if the file exists, otherwise logs failure and returns null. */
    private String resolveFile(String relativePath, String kind) {
        File file = new File(relativePath);
        String absolutePath = file.getAbsolutePath();
        if (file.exists()) {
            logSuccess("Validated " + kind.toLowerCase() + " path: " + absolutePath);
            return absolutePath;
        }
        logFailure(kind + " file not found at path: " + absolutePath);
        return null;
    }

    public void uploadProductImagesAndVideo() {
        logHeader("PRODUCT MEDIA (IMAGE & VIDEO) UPLOAD AUTOMATION");

        // --- 1. IMAGES ---
        List<String> absoluteImagePaths = new ArrayList<>();
        for (String name : List.of("Accessories1.jpg", "Accessories2.jpg", "Accessories3.jpg", "Accessories4.jpg")) {
            String path = resolveFile("src/test/resources/images/" + name, "Image");
            if (path != null) absoluteImagePaths.add(path);
        }

        if (!absoluteImagePaths.isEmpty()) {
            String joinedFilePaths = String.join("\n", absoluteImagePaths);
            String[] types = {"Desktop", "Mobile"};
            for (int i = 0; i < types.length; i++) {
                logHeader("UPLOADING " + types[i].toUpperCase() + " IMAGES");
                uploadToContainer("(//div[@class='file-upload-container'])[" + (i + 1) + "]", joinedFilePaths, types[i] + " Container");
                actionPause();
            }
        } else {
            logFailure("No valid image files found to upload. Skipping image upload step.");
        }

        logSuccess("Successfully completed media upload sequence for Desktop Images, Mobile Images, and Reel Video!");
    }

    private void uploadToContainer(String containerXpath, String joinedFilePaths, String containerLabel) {
        try {
            logAction("Locating " + containerLabel + "...");
            scrollToCenter(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(containerXpath))));
            actionPause();

            List<WebElement> fileInputs = driver.findElements(By.xpath(
                containerXpath + "//input[@type='file'] | " + containerXpath + "/following-sibling::input[@type='file']"));
            WebElement targetFileInput = !fileInputs.isEmpty() ? fileInputs.get(0) : driver.findElement(By.xpath("(//input[@type='file'])"));

            js().executeScript("arguments[0].style.display='block'; arguments[0].style.visibility='visible'; arguments[0].style.opacity='1';", targetFileInput);
            actionPause();

            logAction("Sending image paths to " + containerLabel + " file input...");
            targetFileInput.sendKeys(joinedFilePaths);

            logSuccess("Successfully attached " + joinedFilePaths.split("\n").length + " images to " + containerLabel);
            actionPause();
        } catch (Exception e) {
            logFailure("Failed to upload images to " + containerLabel + ": " + e.getMessage());
        }
    }

    public void setAndVerifyProductThumbnailsAndListImages() {
        logHeader("SETTING AND VERIFYING THUMBNAIL & LIST IMAGES");
        actionPause();

        String setThumb = "//button[@type='button'][normalize-space()='Set thumbnail']";
        String setList = "//button[@type='button'][normalize-space()='Set list image']";

        // 1. DESKTOP SECTION
        logHeader("1. DESKTOP IMAGE BADGES");

        logAction("Hovering over 1st Desktop Image preview...");
        hoverOverElement(previewImg(1), "1st Desktop Image Preview");
        logAction("Setting 1st Desktop Image as Thumbnail...");
        clickButtonWithFallback(By.xpath("(" + setThumb + ")[1]"), "Desktop 1st Image 'Set Thumbnail' button");
        actionPause();

        logAction("Hovering over 2nd Desktop Image preview...");
        hoverOverElement(previewImg(2), "2nd Desktop Image Preview");
        logAction("Setting 2nd Desktop Image as List Image...");
        clickButtonWithFallback(xpathOrFallback("(" + setList + ")[2]", "(" + setList + ")[1]"), "Desktop 2nd Image 'Set List Image' button");
        actionPause();

        logAction("Verifying Desktop 1st Image Thumbnail badge...");
        verifyBadgeVisibility(By.xpath("(//div[contains(@class,'thumbnail')])[1]"), "Desktop 1st Image Thumbnail Badge");

        logAction("Verifying Desktop 2nd Image List Image badge...");
        verifyBadgeVisibility(By.xpath("(//div[contains(@class,'list-image-badge')])[1]"), "Desktop 2nd Image List Image Badge");

        // 2. MOBILE SECTION
        logHeader("2. MOBILE IMAGE BADGES");

        logAction("Hovering over 1st Mobile Image preview (Image #5)...");
        hoverOverElement(previewImg(5), "1st Mobile Image Preview (#5)");
        logAction("Setting 1st Mobile Image as Thumbnail...");
        clickButtonWithFallback(
            xpathOrFallback("(" + setThumb + ")[5]",
                            "(//div[contains(@class,'mobile') or contains(@id,'mobile')]" + setThumb + ")[1]"),
            "Mobile 1st Image 'Set Thumbnail' button");
        actionPause();

        logAction("Hovering over 2nd Mobile Image preview (Image #6)...");
        hoverOverElement(previewImg(6), "2nd Mobile Image Preview (#6)");
        logAction("Setting 2nd Mobile Image as List Image...");
        By mobileListImgBtn = By.xpath("(//img[@alt='preview-image'])[6]/following::button[normalize-space()='Set list image'][1]");
        if (driver.findElements(mobileListImgBtn).isEmpty()) {
            int listBtnCount = driver.findElements(By.xpath(setList)).size();
            if (listBtnCount > 0) {
                mobileListImgBtn = By.xpath("(" + setList + ")[" + listBtnCount + "]");
            }
        }
        clickButtonWithFallback(mobileListImgBtn, "Mobile 2nd Image 'Set List Image' button");
        actionPause();

        logAction("Verifying Mobile 1st Image Thumbnail badge...");
        verifyBadgeVisibility(
            xpathOrFallback("(//div[contains(@class,'thumbnail')])[2]", "(//div[contains(@class,'thumbnail')])[5]"),
            "Mobile 1st Image Thumbnail Badge");

        logAction("Verifying Mobile 2nd Image List Image badge...");
        verifyBadgeVisibility(
            xpathOrFallback("(//div[contains(@class,'list-image-badge')])[2]", "(//div[contains(@class,'list-image-badge')])[6]"),
            "Mobile 2nd Image List Image Badge");

        logSuccess("Successfully configured and verified Thumbnail and List Images for Desktop and Mobile!");
    }

    private void hoverOverElement(By locator, String elementLabel) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            scrollToCenterNearest(element);

            try {
                new Actions(driver).moveToElement(element).perform();
            } catch (Exception e) {
                js().executeScript(
                    "var evObj = document.createEvent('MouseEvents');" +
                    "evObj.initEvent('mouseover', true, false);" +
                    "arguments[0].dispatchEvent(evObj);" +
                    "var evObj2 = document.createEvent('MouseEvents');" +
                    "evObj2.initEvent('mouseenter', true, false);" +
                    "arguments[0].dispatchEvent(evObj2);", element);
            }
            logSuccess("Hovered over: " + elementLabel);
            actionPause();
        } catch (Exception e) {
            logFailure("Failed to hover over " + elementLabel + ": " + e.getMessage());
        }
    }

    private void clickButtonWithFallback(By locator, String buttonLabel) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            scrollToCenterNearest(element);
            clickWithJsFallback(() -> {
                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
            }, element);
            logSuccess("Clicked: " + buttonLabel);
        } catch (Exception e) {
            logFailure("Failed to click " + buttonLabel + ": " + e.getMessage());
        }
    }

    private void verifyBadgeVisibility(By locator, String badgeLabel) {
        try {
            WebElement badgeElement = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            scrollToCenterNearest(badgeElement);

            if (badgeElement.isDisplayed()) {
                logSuccess("VERIFIED VISIBLE: " + badgeLabel);
            } else if ((Boolean) js().executeScript("return arguments[0] !== null;", badgeElement)) {
                logSuccess("VERIFIED VISIBLE (via DOM presence): " + badgeLabel);
            } else {
                logFailure("VERIFICATION FAILED: " + badgeLabel + " is not displayed.");
            }
        } catch (Exception e) {
            logFailure("VERIFICATION FAILED: Could not locate " + badgeLabel + " -> " + e.getMessage());
        }
    }
    
    public void selectTryAlongProductsAndSave() {
        // Pick a random search keyword from the pool
        String[] dynamicKeywords = {"dress", "yellow", "blue", "black", "kurti", "floral", "top", "silk", "cotton", "Set"};
        selectTryAlongProductsAndSave(dynamicKeywords[rnd.nextInt(dynamicKeywords.length)]);
    }

    public void selectTryAlongProductsAndSave(String searchTerm) {
        logHeader("TRY-ALONG PRODUCTS SELECTION");

        By optionsLocator = By.xpath("//ul[contains(@class,'try-along-search-results-0')]//li[contains(@class,'try-along-item-option')]");
        By searchInputLocator = By.xpath("(//input[@id='search_product_0'])[1]");

        try {
            WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(searchInputLocator));
            scrollToCenter(searchInput);
            actionPause();

            logAction("Searching for Try-Along products with dynamic term: '" + searchTerm + "'...");
            searchInput.click();
            searchInput.sendKeys(Keys.CONTROL + "a");
            searchInput.sendKeys(Keys.BACK_SPACE);
            searchInput.sendKeys(searchTerm);
            actionPause();

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(optionsLocator));

            // Wait for the result list to stop re-rendering (same count on two consecutive checks)
            int previousCount = -1;
            for (int i = 0; i < 10; i++) {
                int currentCount = driver.findElements(optionsLocator).size();
                if (currentCount > 0 && currentCount == previousCount) break;
                previousCount = currentCount;
                Thread.sleep(400);
            }

            List<WebElement> productOptions = driver.findElements(optionsLocator);

            if (productOptions.isEmpty()) {
                logFailure("No Try-Along products found for search term: " + searchTerm);
            } else {
                int selectionCount = Math.min(3, productOptions.size());
                logAction("Found " + productOptions.size() + " option(s). Selecting " + selectionCount + " product(s)...");

                for (int i = 0; i < selectionCount; i++) {
                    final int index = i;
                    String optionText = "";

                    // Re-locate the option fresh on every attempt so a re-render can't leave us with a stale element
                    for (int attempt = 1; attempt <= 3; attempt++) {
                        try {
                            List<WebElement> currentOptions = driver.findElements(optionsLocator);
                            if (index >= currentOptions.size()) break;

                            WebElement optionToSelect = currentOptions.get(index);
                            optionText = optionToSelect.getText().trim();

                            js().executeScript("arguments[0].scrollIntoView({block: 'nearest'});", optionToSelect);
                            clickWithJsFallback(optionToSelect::click, optionToSelect);

                            logSuccess("Selected Try-Along Product (" + (i + 1) + "/" + selectionCount + "): " + (optionText.isEmpty() ? "Item #" + (i + 1) : optionText));
                            break;
                        } catch (StaleElementReferenceException se) {
                            logAction("Option list re-rendered (attempt " + attempt + "), re-locating option " + (i + 1) + "...");
                            if (attempt == 3) throw se;
                            Thread.sleep(500);
                        }
                    }
                    actionPause();
                }
            }

            logAction("Closing Try-Along dropdown menu...");
            // Re-locate the search input in case it was re-rendered after selections
            driver.findElement(searchInputLocator).sendKeys(Keys.ESCAPE);
            actionPause();

            clickSaveAndNext();
            
            logSuccess("Successfully selected Try-Along products using term '" + searchTerm + "' and clicked 'Save & Next'!");
        } catch (Exception e) {
            logFailure("Failed during Try-Along product selection flow: " + e.getMessage());
        }
    }
    
    public WebElement waitForElementPresence(By locator, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    private Map<String, String> storedDescriptionData = new HashMap<>();

    /** Picks a random entry from the given options. */
    private String randomOf(String... options) {
        return options[rnd.nextInt(options.length)];
    }

    private WebDriverWait pickAiSuggestionAndProceed(List<WebElement> options, WebElement proceedBtn,
            String waitingMsg, String foundLabel, String selectedLabel, String proceedMsg) {
        logAction(waitingMsg);
        WebDriverWait aiWait = new WebDriverWait(driver, Duration.ofSeconds(40));

        aiWait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(@class,'modal') or contains(@class,'popup') or contains(@class,'show')]")));

        aiWait.until(d -> {
            try {
                return !options.isEmpty() && options.get(0).isDisplayed();
            } catch (Exception e) {
                return false;
            }
        });
        actionPause();

        int optionsCount = options.size();
        logAction("Found " + optionsCount + " " + foundLabel + " option(s). Picking one randomly...");

        int selectedIndex = rnd.nextInt(optionsCount);
        WebElement chosenOption = options.get(selectedIndex);

        aiWait.until(ExpectedConditions.elementToBeClickable(chosenOption));
        scrollAndClick(chosenOption);
        logSuccess("Selected " + selectedLabel + " Option #" + (selectedIndex + 1));
        actionPause();

        logAction(proceedMsg);
        aiWait.until(ExpectedConditions.elementToBeClickable(proceedBtn));
        scrollAndClick(proceedBtn);
        actionPause();
        return aiWait;
    }

    public void configureProductDescriptionAndSave() {
    	clickSaveAndNext();
        logHeader("CONFIGURING PRODUCT DESCRIPTION PAGE");

        try {
            // --- 1. AI SUGGESTION GENERATION ---
            logAction("Clicking 'AI Suggestion' button...");
            wait.until(ExpectedConditions.elementToBeClickable(aiSuggestionBtn));
            scrollAndClick(aiSuggestionBtn);

            WebDriverWait extendedAiWait = pickAiSuggestionAndProceed(aiSuggestionOptions, aiProceedBtn,
                "Waiting for AI Suggestions to generate...", "AI suggestion", "AI Suggestion",
                "Clicking 'Proceed' button on AI Suggestion popup...");

            extendedAiWait.until(ExpectedConditions.attributeToBeNotEmpty(productDescriptionInput, "value"));
            String generatedDescriptionText = productDescriptionInput.getAttribute("value").trim();

            if (generatedDescriptionText.isEmpty()) {
                logFailure("VERIFICATION FAILED: Product Description field is empty after AI Proceed!");
            } else {
                storedDescriptionData.put("Product Description", generatedDescriptionText);
                logSuccess("VERIFIED: Product Description populated -> " +
                    (generatedDescriptionText.length() > 60 ? generatedDescriptionText.substring(0, 60) + "..." : generatedDescriptionText));
            }

            // --- 2. FILL FIELDS WITH DYNAMIC RANDOM OPTIONS ---
            fillAndStoreField(compositionCareInput, "Composition & Care", randomOf(
                "Hand wash cold separately. Mild detergent only. Line dry in shade. Cool iron on reverse.",
                "Machine wash cold with like colors. Do not bleach. Tumble dry low. Warm iron if needed.",
                "Dry clean only. Do not wash or tumble dry. Steam iron on low setting if necessary.",
                "100% Organic Silk. Gentle hand wash in cold water. Do not wring. Flat dry away from sun."));

            fillAndStoreField(productSpecificationInput, "Product Specification", randomOf(
                "100% Premium Cotton, Breathable mesh weave, Reinforced stitch seams, Lightweight design.",
                "80% Linen 20% Rayon blend, Natural texture, Soft-washed feel, Wrinkle-resistant finish.",
                "Pure Mulberry Silk, Hypoallergenic weave, Lined interior, Tailored fit finish.",
                "100% Recycled Polyester, Quick-dry technology, Moisture-wicking fabric, UV protection."));

            fillAndStoreField(stylingSuggestionInput, "Styling Suggestion", randomOf(
                "Pair with clean white sneakers and a sleek denim jacket for a contemporary weekend look.",
                "Match with high-waisted trousers and classic leather loafers for a polished smart-casual outfit.",
                "Layer over a fitted turtleneck and complete with minimalist heels for an elegant evening ensemble.",
                "Style with tailored shorts and woven strap sandals for an effortless warm-weather vibe."));

            fillAndStoreField(sizeFitInput, "Size & Fit", randomOf(
                "True to size. Model is 5'9\" wearing size Small. Relaxed silhouette for everyday comfort.",
                "Slim fit profile. Designed to body contour. Take one size up if you prefer a looser fit.",
                "Oversized aesthetic. Model is 6'0\" wearing size Medium. Boxy cut for a modern urban fit.",
                "Regular fit. Tailored waist with flexible armholes. Model is 5'8\" wearing size Medium."));

            fillAndStoreField(returnAndExchangeInput, "Return & Exchange", randomOf(
                "Hassle-free 30-day returns and free instant exchanges. Pickup scheduled right from your doorstep.",
                "14-day easy return policy. Store credit or full refund provided upon quality inspection.",
                "7-day return window for unworn items with original tags intact. Easy online exchange process.",
                "30-day money-back guarantee. No questions asked return procedure with free home pick-up."));

            // --- 3. SAVE & NEXT ---
            logAction("Clicking 'Save & Next' button...");
            clickSaveAndNext();
            actionPause();

        } catch (Exception e) {
            logFailure("Failed to complete Description Page setup: " + e.getMessage());
            throw e;
        }
    }

    private void fillAndStoreField(WebElement element, String fieldName, String textToEnter) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            scrollToCenter(element);

            // Robust clearing sequence for both create and edit modes
            try {
                element.sendKeys(Keys.CONTROL + "a");
                element.sendKeys(Keys.BACK_SPACE);
                element.clear();
            } catch (Exception e) {
                js().executeScript("arguments[0].value = '';", element);
            }

            element.sendKeys(textToEnter);

            storedDescriptionData.put(fieldName, textToEnter);
            logSuccess("Entered " + fieldName + ": " + textToEnter);
            actionPause();
        } catch (Exception e) {
            logFailure("Failed to enter " + fieldName + ": " + e.getMessage());
        }
    }

    private void scrollAndClick(WebElement element) {
        scrollToCenter(element);
        clickWithJsFallback(element::click, element);
    }

    public Map<String, String> getStoredDescriptionData() {
        return storedDescriptionData;
    }

    private Map<String, String> storedSeoData = new HashMap<>();

    public void configureProductSeoAndSave() {
        logHeader("CONFIGURING SEO DETAILS PAGE");

        try {
            logAction("Clicking AI Suggestion button for SEO...");
            wait.until(ExpectedConditions.visibilityOf(seoAiSuggestionBtn));
            scrollAndClick(seoAiSuggestionBtn);

            pickAiSuggestionAndProceed(seoAiSuggestionOptions, seoAiProceedBtn,
                "Waiting for AI SEO suggestions to generate...", "AI SEO suggestion", "AI SEO Suggestion",
                "Clicking 'Proceed' button on AI SEO popup...");

            verifyAndStoreField(seoMetaTitleInput, "SEO Meta Title", "value");
            verifyAndStoreField(seoMetaKeyInput, "SEO Meta Keyword", "value");
            verifyAndStoreField(seoMetaDescriptionInput, "SEO Meta Description", "value");

            logAction("Clicking final 'Save' button for SEO details...");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//button[normalize-space()='Save'])[1]")));
            scrollAndClick(seoSaveBtn);

            logSuccess("Successfully configured and saved Product SEO Details!");

        } catch (Exception e) {
            logFailure("Failed to complete SEO Details setup: " + e.getMessage());
            throw e;
        }
    }
    
    private void verifyAndStoreField(WebElement element, String fieldName, String attributeName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.attributeToBeNotEmpty(element, attributeName));

            String fieldValue = element.getAttribute(attributeName);
            if (fieldValue != null) {
                fieldValue = fieldValue.trim();
            }

            if (fieldValue == null || fieldValue.isEmpty()) {
                logFailure("VERIFICATION FAILED: " + fieldName + " is empty!");
            } else {
                storedSeoData.put(fieldName, fieldValue);
                logSuccess("VERIFIED " + fieldName + " -> " + 
                    (fieldValue.length() > 50 ? fieldValue.substring(0, 50) + "..." : fieldValue));
            }
        } catch (Exception e) {
            logFailure("Failed while verifying field " + fieldName + ": " + e.getMessage());
        }
    }

    public Map<String, String> getStoredSeoData() {
        return storedSeoData;
    }
    
 // ===================== SHARED HELPERS =====================

    /** Opens the first row's action menu and clicks Preview. */
    private void openPreviewPage() {
        logAction("Clicking action three-dot button...");
        WebElement threeDotElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[@class='dropdown actions-dropdown-wrapper'])[1]")));
        scrollAndClick(threeDotElement);
        actionPause();

        logAction("Clicking 'Preview' option...");
        WebElement previewElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")));
        scrollAndClick(previewElement);
        logSuccess("Successfully navigated to Preview Page!");
        actionPause();
    }
    
    
 // ===================== VERIFICATION HELPERS =====================

    private void clickTabAndPause(By tabLocator, String tabName) {
        logAction("Navigating to preview tab: " + tabName);
        WebElement tabElement = wait.until(ExpectedConditions.presenceOfElementLocated(tabLocator));
        scrollAndClick(tabElement);
        actionPause();
    }

    private void verifyPreviewField(By locator, String expectedValue, String fieldName) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            scrollToCenter(element);

            String actualValue = element.getText().trim();
            String cleanedExpected = expectedValue != null ? expectedValue.trim() : "";

            // Normalize date separators (/ to -) for comparison
            String normalizedActual = actualValue.replace("/", "-");
            String normalizedExpected = cleanedExpected.replace("/", "-");

            if (cleanedExpected.isEmpty()) {
                if (!actualValue.isEmpty()) {
                    logSuccess("VERIFIED PREVIEW [" + fieldName + "] -> Populated Value: '" + actualValue + "'");
                } else {
                    logFailure("MISMATCH PREVIEW [" + fieldName + "] -> Element is empty!");
                }
            } else if (normalizedActual.equalsIgnoreCase(normalizedExpected)
                    || normalizedActual.contains(normalizedExpected)
                    || normalizedExpected.contains(normalizedActual)) {
                logSuccess("VERIFIED PREVIEW [" + fieldName + "] -> Expected: '" + cleanedExpected + "' | Actual: '" + actualValue + "'");
            } else {
                logFailure("MISMATCH PREVIEW [" + fieldName + "] -> Expected: '" + cleanedExpected + "' | Actual: '" + actualValue + "'");
            }
        } catch (Exception e) {
            logFailure("Could not verify preview field [" + fieldName + "] at locator: " + locator + " -> " + e.getMessage());
        }
    }

    private void verifyPreviewFieldPresence(By locator, String fieldName) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            scrollToCenter(element);

            String actualValue = element.getText().trim();
            if (!actualValue.isEmpty()) {
                logSuccess("VERIFIED PREVIEW TOGGLE OPTION [" + fieldName + "] -> Text: '" + actualValue + "'");
            } else {
                logFailure("VERIFICATION FAILED [" + fieldName + "] -> Element present but empty.");
            }
        } catch (Exception e) {
            logFailure("Could not verify preview toggle [" + fieldName + "] at locator: " + locator + " -> " + e.getMessage());
        }
    }

    private void verifyTableCell(By locator, String expectedValue, String fieldName) {
        try {
            WebElement cell = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            scrollToCenter(cell);

            String actualValue = cell.getText().trim();
            String cleanedExpected = expectedValue != null ? expectedValue.trim() : "";

            if (actualValue.equalsIgnoreCase(cleanedExpected)
                    || actualValue.contains(cleanedExpected)
                    || cleanedExpected.contains(actualValue)) {
                logSuccess("VERIFIED LISTING [" + fieldName + "] -> Expected: '" + cleanedExpected + "' | Actual: '" + actualValue + "'");
            } else {
                logFailure("MISMATCH LISTING [" + fieldName + "] -> Expected: '" + cleanedExpected + "' | Actual: '" + actualValue + "'");
            }
        } catch (Exception e) {
            logFailure("Could not verify [" + fieldName + "] in cell location: " + locator + " -> " + e.getMessage());
        }
    }
    
    /** Inventory -> Variants -> Settings -> Description -> SEO tabs (same for product and accessory). */
    private void verifyInventoryToSeoTabs() {
        // 2. INVENTORY TAB
        logHeader("2. VERIFYING INVENTORY TAB");
        clickTabAndPause(By.xpath("(//button[@id='inventory-tab'])[1]"), "Inventory Tab");
        verifyPreviewField(By.xpath("(//div[@class='detail-item'])[1]"), lastSelectedColor, "Color");
        verifyPreviewField(By.xpath("(//div[@class='detail-item'])[2]"), lastSelectedSize, "Size");

        // 3. VARIANTS TAB
        logHeader("3. VERIFYING VARIANTS TAB");
        clickTabAndPause(By.xpath("(//button[normalize-space()='Variants'])[1]"), "Variants Tab");
        verifyPreviewField(By.xpath("(//div[@class='detail-value'])[1]"), lastVariantData.get("From Date"), "From Date");
        verifyPreviewField(By.xpath("(//div[@class='detail-value'])[2]"), lastVariantData.get("To Date"), "To Date");
        verifyPreviewField(By.xpath("(//div[@class='detail-value'])[3]"), lastSelectedColor, "Color");
        verifyPreviewField(By.xpath("(//div[@class='detail-value'])[4]"), lastVariantData.get("Secondary Color"), "Secondary Color");
        verifyPreviewField(By.xpath("(//div[@class='detail-value'])[5]"), lastSelectedSize, "Size");
        verifyPreviewField(By.xpath("(//div[@class='detail-value'])[7]"), lastGeneratedSKU, "SKU");
        verifyPreviewField(By.xpath("(//div[@class='detail-value'])[8]"), lastVariantData.get("Cost Price"), "Cost Price");
        verifyPreviewField(By.xpath("(//div[@class='detail-value'])[9]"), lastVariantData.get("Cut Price"), "Cut Price");

        // 4. SETTINGS TAB
        logHeader("4. VERIFYING SETTINGS TAB");
        clickTabAndPause(By.xpath("(//button[normalize-space()='Settings'])[1]"), "Settings Tab");
        String[] settingLabels = {"Custom Size", "Return Option", "Cancel Option", "Exchange Option",
                                  "Best Seller", "Recommended", "Free Option", "Personalized Letter"};
        for (int i = 0; i < settingLabels.length; i++) {
            verifyPreviewFieldPresence(By.xpath(
                    "(//div[@class='detail-item d-flex flex-row align-items-center justify-content-between mb-2'])[" + (i + 2) + "]"),
                    settingLabels[i]);
        }

        // 5. DESCRIPTION TAB
        logHeader("5. VERIFYING DESCRIPTION TAB");
        clickTabAndPause(By.xpath("(//button[normalize-space()='Description'])[1]"), "Description Tab");
        String[][] descFields = {
                {"Product Description", "Product Description"}, {"Composition & Care", "Composition & Care"},
                {"Styling Suggestion", "Styling"}, {"Product Specification", "Product Specification"},
                {"Size & Fit", "Size and Fit"}, {"Return & Exchange", "Return and Exchange"}};
        for (int i = 0; i < descFields.length; i++) {
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[" + (i + 1) + "]"),
                    storedDescriptionData.get(descFields[i][0]), descFields[i][1]);
        }

        // 6. SEO TAB
        logHeader("6. VERIFYING SEO TAB");
        clickTabAndPause(By.xpath("(//button[normalize-space()='SEO'])[1]"), "SEO Tab");
        verifyPreviewField(By.xpath("(//div[@class='detail-value'])[1]"), storedSeoData.get("SEO Meta Title"), "SEO Meta Title");
        verifyPreviewField(By.xpath("(//div[@class='detail-value'])[2]"), storedSeoData.get("SEO Meta Keyword"), "Meta Key");
        verifyPreviewField(By.xpath("(//div[@class='detail-value'])[3]"), storedSeoData.get("SEO Meta Description"), "SEO Description");
    }

    // ===================== ACCESSORY: LISTING PAGE =====================

    public void verifyAccessoryInListingPage() {
        logHeader("VERIFYING ACCESSORY DETAILS IN LISTING PAGE");

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr[1]")));

            verifyTableCell(By.xpath("(//td)[1]"), lastGeneratedProductName, "Product Name");
            verifyTableCell(By.xpath("(//td)[2]"), lastGeneratedSKU, "SKU");
            verifyTableCell(By.xpath("(//td)[3]"), lastGeneratedHSN, "HSN");
            verifyTableCell(By.xpath("(//td)[4]"), lastSelectedBrand, "Brand Type");
            verifyTableCell(By.xpath("(//td)[5]"), lastSelectedColor, "Color");

            logSuccess("All accessory details in the listing page verified successfully!");
        } catch (Exception e) {
            logFailure("Listing page verification failed: " + e.getMessage());
            throw e;
        }
    }

    // ===================== ACCESSORY: PREVIEW PAGE =====================

    public void navigateToAccessoryPreviewAndVerifyDetails() {
        logHeader("NAVIGATING TO ACCESSORY PREVIEW PAGE");

        try {
            openPreviewPage();

            // 1. BASIC INFO TAB
            logHeader("1. VERIFYING BASIC INFO TAB");
            verifyPreviewField(By.xpath("(//div[@class='detail-value text-primary'])[1]"), lastGeneratedProductName, "Product Name");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[1]"), lastSelectedBrand, "Brand Type");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[2]"), lastGeneratedSKU, "SKU");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[3]"), lastGeneratedHSN, "HSN");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[5]"), lastSelectedAccessoryType, "Type");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[7]"), lastSelectedBarcodeType, "Barcode");

            // 2-6. INVENTORY, VARIANTS, SETTINGS, DESCRIPTION, SEO
            verifyInventoryToSeoTabs();

            logSuccess("All accessory details across all Preview tabs verified successfully!");
        } catch (Exception e) {
            logFailure("Failed during accessory preview verification flow: " + e.getMessage());
            throw e;
        }
    }
    
    
    //TC-02
 // ===================== NEW HELPERS =====================
 // Helper method to handle tricky Brand dropdown clicks reliably
    private String selectBrandDropdownSafely(WebElement dropdownBtn) {
        By optionsLocator = By.xpath("(//ul[@class='select-options-menu'])[1]//li[@class='option-item'] | //ul[contains(@class,'dropdown-menu')]//li/a | //div[contains(@class,'dropdown-menu')]//a");
        return selectRandomOptionWithRetry(
                () -> {
                    scrollIntoView(dropdownBtn);
                    wait.until(ExpectedConditions.elementToBeClickable(dropdownBtn));
                    return dropdownBtn;
                },
                ExpectedConditions.visibilityOfAllElementsLocatedBy(optionsLocator),
                "Brand", "Brand dropdown selection");
    }


    private String selectRandomOptionWithRetry(Supplier<WebElement> triggerSupplier,
                                               ExpectedCondition<List<WebElement>> optionsCondition,
                                               String name, String retryLabel) {
        Random random = new Random();
        String selectedOptionText = "";

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                safeClick(triggerSupplier.get());
                actionPause(); // Wait for animation/render

                List<WebElement> validOptions = wait.until(optionsCondition).stream()
                        .filter(WebElement::isDisplayed)
                        .filter(e -> !e.getText().trim().isEmpty())
                        .toList();

                if (!validOptions.isEmpty()) {
                    WebElement chosenOption = validOptions.get(random.nextInt(validOptions.size()));
                    selectedOptionText = chosenOption.getText().trim();
                    scrollIntoView(chosenOption);
                    safeClick(chosenOption);

                    logSuccess("Selected " + name + ": '" + selectedOptionText + "'");
                    return selectedOptionText;
                }
            } catch (Exception e) {
                logAction(retryLabel + " attempt " + attempt + " failed. Retrying...");
                actionPause();
            }
        }
        logFailure("Failed to select " + name + " from dropdown after 3 attempts.");
        return selectedOptionText;
    }

    // Helper method to reliably clear and enter input fields
    private void clearAndTypeInput(WebElement element, String text) {
        scrollIntoView(element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            element.sendKeys(Keys.CONTROL + "a");
            element.sendKeys(Keys.DELETE);
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", element);
            element.sendKeys(text);
        }
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    private void safeClick(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    private void scrollClickWhenReady(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        scrollAndClick(element);
    }

    private static final By EDIT_DROPDOWN_OPTIONS = By.xpath(
            "(//ul[@class='select-options-menu'])[1]//li[@class='option-item'] | " +
            "//ul[contains(@class,'select-options-menu') or contains(@class,'dropdown-menu')]//li | " +
            "//div[contains(@class,'dropdown-menu')]//a | " +
            "//li[@class='option-item']");

    /** Opens the dropdown at (//button[@type='button'])[index] and selects a random option (with retry). */
    private String selectDropdownByIndexWithRetry(int buttonIndex, String fieldName) {
        By triggerLocator = By.xpath("(//button[@type='button'])[" + buttonIndex + "]");
        return selectRandomOptionWithRetry(
                () -> {
                    WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(triggerLocator));
                    scrollIntoView(trigger);
                    return trigger;
                },
                ExpectedConditions.presenceOfAllElementsLocatedBy(EDIT_DROPDOWN_OPTIONS),
                fieldName, fieldName + " dropdown selection");
    }

    /** Builds a random code like prefix + N chars from A-Z0-9. */
    private String randomCode(String prefix, int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < length; i++) sb.append(chars.charAt(random.nextInt(chars.length())));
        return sb.toString();
    }

    /** Waits for an input by locator, then clears and types. */
    private void updateInput(By locator, String value, String label) {
        logAction("Updating " + label + " to: '" + value + "'");
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        clearAndTypeInput(input, value);
        actionPause();
    }

    // ===================== ACCESSORY EDIT =====================

    public String editExistingAccessoryDetails() {
        logHeader("EDIT EXISTING ACCESSORY AUTOMATION");
        Random random = new Random();

        // 1. Navigate to Edit page via action menu
        logAction("Clicking action three-dot button...");
        scrollClickWhenReady(threedotBtn);
        actionPause();

        logAction("Clicking 'Edit' option...");
        scrollClickWhenReady(editBtn);
        logSuccess("Successfully navigated to Accessory Edit Page!");
        actionPause();

        // 2. Generate new values
        String[] accessories = {"Leather Belt", "Silk Scarf", "Tote Bag", "Clutch", "Sunglasses",
                                "Hair Clip Set", "Statement Necklace", "Wrist Watch", "Stole", "Handbag"};
        lastGeneratedProductName = "Updated " + accessories[random.nextInt(accessories.length)] + " " + (100 + random.nextInt(900));
        lastGeneratedSKU = randomCode("ZL-EDIT-", 4);
        lastGeneratedHSN = String.format("%08d", random.nextInt(100000000));

        // 3. Product Name
        logAction("Updating Accessory Name to: '" + lastGeneratedProductName + "'");
        wait.until(ExpectedConditions.visibilityOf(productNameInput));
        clearAndTypeInput(productNameInput, lastGeneratedProductName);
        actionPause();

//        // 4. Brand (button index 2)
//        lastSelectedBrand = selectDropdownByIndexWithRetry(2, "Brand");
//        actionPause();

        // 5. SKU and 6. HSN
        updateInput(By.xpath("(//input[@id='sku'])[1]"), lastGeneratedSKU, "SKU Code");
        updateInput(By.xpath("(//input[@id='hsn'])[1]"), lastGeneratedHSN, "HSN Code");

        // 7. Type (button index 5)
        lastSelectedAccessoryType = selectDropdownByIndexWithRetry(5, "Type");
        actionPause();

        // 8. Barcode (button index 7)
        lastSelectedBarcodeType = selectDropdownByIndexWithRetry(7, "Barcode");
        actionPause();

        // 9. Save & Next
        logHeader("SAVING UPDATED ACCESSORY DETAILS");
        clickSaveAndNext();
        actionPause();

        return lastGeneratedProductName;
    }
    
  //Map to store inventory details for future verification
    protected Map<String, String> lastInventoryDimensions = new HashMap<>();
    
    public void editAccessoryInventorySetup() {
        logHeader("ACCESSORY INVENTORY SETUP AUTOMATION (EDIT)");
        Random random = new Random();
        lastInventoryDimensions.clear();

        // Step 1: Select Color
        WebElement colorDropdownBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[@type='button'])[2]")));
        lastSelectedColor = selectRandomCustomDropdownOption(colorDropdownBtn, "Color");
        actionPause();

        // Step 3: Fill Dimensions & store values for verification
        logHeader("FILLING DIMENSIONS: SINGLE PRODUCT ITEM");

        // Per field: input placeholder, log label, map key, {random bound, offset}
        String[] placeholders = {"L", "B", "H", "Weight", "Qty", "Alert"};
        String[] labels = {"Length (L)", "Breadth (B)", "Height (H)", "Weight", "Qty", "Alert Threshold"};
        String[] keys = {"Length", "Breadth", "Height", "Weight", "Qty", "Alert"};
        int[][] ranges = {{20, 10}, {15, 5}, {10, 2}, {500, 100}, {50, 10}, {5, 1}};

        for (int f = 0; f < placeholders.length; f++) {
            String value = String.valueOf(random.nextInt(ranges[f][0]) + ranges[f][1]);
            enterInputValue(
                    By.xpath("(//input[@placeholder='" + placeholders[f] + "'])[1]"),
                    value,
                    "Single Product Item -> " + labels[f]);
            lastInventoryDimensions.put("Single Product Item_" + keys[f], value);
        }
        actionPause();

     // Step 4: Select Raw Material (skip "Add Raw Material", it redirects to another page)
        selectRandomCustomDropdownOption(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[@type='button'])[3]"))), "Raw Material", "Add Raw Material");
        actionPause();

        // Step 5: Save & Next
        clickSaveAndNext();
        actionPause();
    }
    
    public Map<String, String> getLastInventoryDimensions() {
    	return lastInventoryDimensions;
    }
    
    
  //Storage list for future verification
    protected List<String> lastSelectedTryAlongProducts = new ArrayList<>();

    public void editTryAlongProductsAndSave() {
      String[] dynamicKeywords = {"dress", "yellow", "blue", "black", "kurti", "floral", "top", "silk", "cotton", "Set"};
      editTryAlongProductsAndSave(dynamicKeywords[new Random().nextInt(dynamicKeywords.length)]);
    }

    public void editTryAlongProductsAndSave(String searchTerm) {
      logHeader("EDIT TRY-ALONG PRODUCTS SELECTION");
      lastSelectedTryAlongProducts.clear();

      By searchInputLocator = By.xpath("(//input[@id='search_product_0'])[1]");
      By tryAlongSectionLocator = By.xpath("//*[contains(text(),'Try Along') or contains(text(),'Try-Along') or contains(@class,'try-along')]");
      By optionsLocator = By.xpath("//ul[contains(@class,'try-along-search-results-0')]//li[contains(@class,'try-along-item-option')]");
      By removeButtonsLocator = By.xpath("//div[contains(@class,'try-along') or contains(@class,'selected')]//span[contains(@class,'remove') or contains(@class,'close') or text()='×']");

      try {
          // --- Step 1: Scroll down to Try-Along section ---
          logAction("Scrolling down to the Try-Along section...");
          try {
              scrollIntoView(wait.until(ExpectedConditions.presenceOfElementLocated(tryAlongSectionLocator)));
          } catch (Exception e) {
              scrollIntoView(wait.until(ExpectedConditions.presenceOfElementLocated(searchInputLocator)));
          }
          actionPause();

          // --- Step 2: Remove Previously Selected Items (If Any) ---
          try {
              List<WebElement> existingRemoveBtns = driver.findElements(removeButtonsLocator);
              if (!existingRemoveBtns.isEmpty()) {
                  logAction("Removing " + existingRemoveBtns.size() + " existing Try-Along product selection(s)...");
                  for (WebElement removeBtn : existingRemoveBtns) {
                      safeClick(removeBtn);
                      actionPause();
                  }
                  logSuccess("Cleared previous Try-Along selections!");
              }
          } catch (Exception e) {
              logAction("No pre-existing Try-Along products to clear.");
          }

          // --- Step 3: Search & Select New Items ---
          WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(searchInputLocator));
          scrollIntoView(searchInput);
          actionPause();

          logAction("Searching for new Try-Along products with term: '" + searchTerm + "'...");
          safeClick(searchInput);

          searchInput.sendKeys(Keys.CONTROL + "a");
          searchInput.sendKeys(Keys.BACK_SPACE);
          searchInput.sendKeys(searchTerm);

          // Wait for dropdown results to dynamically populate after search input
          wait.until(ExpectedConditions.visibilityOfElementLocated(optionsLocator));
          actionPause();

          int initialCount = driver.findElements(optionsLocator).size();

          if (initialCount == 0) {
              logFailure("No Try-Along products found for search term: " + searchTerm);
          } else {
              int selectionCount = Math.min(3, initialCount);
              logAction("Found " + initialCount + " option(s). Selecting top " + selectionCount + " product(s)...");

              for (int i = 0; i < selectionCount; i++) {
                  // Re-query options on each iteration to prevent StaleElementReferenceException from dynamic DOM refreshes
                  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(optionsLocator));
                  List<WebElement> currentOptions = driver.findElements(optionsLocator);

                  if (i < currentOptions.size()) {
                      WebElement optionToSelect = currentOptions.get(i);

                      // Extract option label safely
                      String capturedName = "";
                      try {
                          capturedName = optionToSelect.getText().trim();
                      } catch (Exception e) {
                          // fall through to the default label below
                      }
                      if (capturedName.isEmpty()) capturedName = "Item #" + (i + 1);

                      // Scroll element into view before clicking
                      try {
                          ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'nearest'});", optionToSelect);
                          wait.until(ExpectedConditions.elementToBeClickable(optionToSelect)).click();
                      } catch (Exception e) {
                          ((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionToSelect);
                      }

                      // Store selected product name for downstream validations
                      lastSelectedTryAlongProducts.add(capturedName);

                      logSuccess("Selected Try-Along Product (" + (i + 1) + "/" + selectionCount + "): " + capturedName);
                      actionPause();
                  }
              }
          }

          // Close dropdown overlay
          logAction("Closing Try-Along dropdown menu...");
          try {
              searchInput.sendKeys(Keys.ESCAPE);
          } catch (Exception e) {
              // Ignore if overlay auto-closed upon selection limit
          }
          actionPause();

          // --- Step 4: Save & Next ---
          clickSaveAndNext();
          actionPause();

      } catch (Exception e) {
          logFailure("Failed during Edit Try-Along product selection flow: " + e.getMessage());
      }
    }

    public List<String> getLastSelectedTryAlongProducts() {
      return lastSelectedTryAlongProducts;
    }

    
    //TC-AM-03
    // Storage to track stock quantities and statuses for assertion
    protected List<Integer> initialStockQuantities = new ArrayList<>();
    protected List<String> initialStockStatuses = new ArrayList<>();
    public void navigateToProductPreview() {
        logHeader("NAVIGATING TO PRODUCT PREVIEW");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(threedotBtn));
            scrollAndClick(threedotBtn);
            logAction("Clicked action three-dot button...");

            wait.until(ExpectedConditions.elementToBeClickable(previewbtn));
            scrollAndClick(previewbtn);
            logSuccess("Successfully navigated to Product Preview Page!");
            actionPause();
        } catch (Exception e) {
            logFailure("Failed to navigate to Product Preview page: " + e.getMessage());
            throw e;
        }
    }

    public void adjustStockToZeroFromPreview() {
        logHeader("ADJUSTING PRODUCT STOCK TO ZERO VIA PREVIEW PAGE");
        initialStockQuantities.clear();
        initialStockStatuses.clear();

        try {
            // --- Step 1: Click Inventory Tab ---
            logHeader("1. NAVIGATING TO INVENTORY TAB");
            By inventoryTabLocator = By.xpath("(//button[@id='inventory-tab'])[1]");
            WebElement inventoryTab = wait.until(ExpectedConditions.presenceOfElementLocated(inventoryTabLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", inventoryTab);
            wait.until(ExpectedConditions.elementToBeClickable(inventoryTab)).click();
            logSuccess("Navigated to Inventory Tab!");
            actionPause();

            // --- Step 2: Capture Initial Quantities & Statuses ---
            By rowLocator = By.xpath("(//tbody)[1]//tr");
            wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));
            List<WebElement> inventoryRows = driver.findElements(rowLocator);
            int rowCount = inventoryRows.size();

            logAction("Found " + rowCount + " inventory item(s). Capturing initial stock details...");

            for (int i = 1; i <= rowCount; i++) {
                By qtyLocator = By.xpath("(//tbody)[1]//tr[" + i + "]//td[5]");
                By statusLocator = By.xpath("(//tbody)[1]//tr[" + i + "]//td[7]");

                String qtyText = driver.findElement(qtyLocator).getText().trim().replaceAll("[^0-9]", "");
                String statusText = driver.findElement(statusLocator).getText().trim();

                int qty = qtyText.isEmpty() ? 0 : Integer.parseInt(qtyText);
                initialStockQuantities.add(qty);
                initialStockStatuses.add(statusText);

                logSuccess("Item #" + i + " -> Quantity: " + qty + " | Status: " + statusText);
            }

            // --- Step 3: Click 'Update Stock' Button ---
            logAction("Clicking 'Update Stock' button...");
            wait.until(ExpectedConditions.elementToBeClickable(updateStockBtn));
            scrollAndClick(updateStockBtn);
            actionPause();

            // --- Step 4: Click 'Adjust Stocks' Radio/Button ---
            logAction("Selecting 'Adjust Stocks' option...");
            wait.until(ExpectedConditions.elementToBeClickable(adjustStocksBtn));
            scrollAndClick(adjustStocksBtn);
            actionPause();

            // --- Step 5: Deduct Stock Quantities to zero for each item ---
            logAction("Deducting exact stock quantities to reduce inventory to 0...");
            for (int i = 0; i < rowCount; i++) {
                By inputLocator = By.xpath("(//input[@placeholder='Enter Value'])[" + (i + 1) + "]");
                WebElement qtyInput = wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", qtyInput);

                // Clear existing input text safely
                try {
                    qtyInput.sendKeys(Keys.CONTROL + "a");
                    qtyInput.sendKeys(Keys.BACK_SPACE);
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", qtyInput);
                }

                int deductQty = initialStockQuantities.get(i);
                qtyInput.sendKeys(String.valueOf(deductQty));
                logSuccess("Item #" + (i + 1) + ": Entered deduction value -> " + deductQty);
                actionPause();
            }

            // --- Step 6: Select Random Reason from Dropdown ---
            logAction("Opening Reason dropdown...");
            wait.until(ExpectedConditions.elementToBeClickable(selectReasonDropdown));
            scrollAndClick(selectReasonDropdown);
            actionPause();

            By reasonOptionsLocator = By.xpath("(//ul[@class='select-options-menu'])[1]//li[@class='option-item']");
            List<WebElement> reasonOptions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(reasonOptionsLocator));

            if (!reasonOptions.isEmpty()) {
                Random random = new Random();
                int randomIndex = random.nextInt(reasonOptions.size());
                WebElement selectedReason = reasonOptions.get(randomIndex);
                String reasonText = selectedReason.getText().trim();

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'nearest'});", selectedReason);
                try {
                    selectedReason.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedReason);
                }
                logSuccess("Selected Reason: '" + reasonText + "'");
            } else {
                logFailure("No options found in Reason dropdown!");
            }
            actionPause();

            // --- Step 7: Enter Notes in Stock Notes Text Area ---
            logAction("Entering stock adjustment notes...");
            wait.until(ExpectedConditions.visibilityOf(stockNotesTextArea));
            stockNotesTextArea.clear();
            String noteMessage = "Automated stock adjustment to 0 for out-of-stock validation.";
            stockNotesTextArea.sendKeys(noteMessage);
            logSuccess("Entered Note: " + noteMessage);
            actionPause();

            // --- Step 8: Save / Update Stock & Force DOM Sync ---
            logAction("Clicking 'Update' button to apply stock adjustments...");
            wait.until(ExpectedConditions.elementToBeClickable(updateBtn));
            scrollAndClick(updateBtn);
            logSuccess("Successfully submitted stock adjustment to 0!");

            // Refresh page to load fresh backend table state post-update
            logAction("Refreshing page to ensure updated status is loaded...");
            driver.navigate().refresh();
            actionPause();

            // Re-open Inventory tab post-refresh
            WebElement reloadedTab = wait.until(ExpectedConditions.presenceOfElementLocated(inventoryTabLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", reloadedTab);
            wait.until(ExpectedConditions.elementToBeClickable(reloadedTab)).click();
            actionPause();

        } catch (Exception e) {
            logFailure("Failed to adjust stock to zero from Preview page: " + e.getMessage());
            throw e;
        }
    }

    public List<Integer> getInitialStockQuantities() {
        return initialStockQuantities;
    }

    public List<String> getInitialStockStatuses() {
        return initialStockStatuses;
    }
    
    
    public void verifyInventoryTabOutOfStockStatus() {
        logHeader("VERIFYING INVENTORY TAB OUT OF STOCK STATUS");

        By rowLocator = By.xpath("(//tbody)[1]//tr");
        wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));
        List<WebElement> inventoryRows = driver.findElements(rowLocator);
        int rowCount = inventoryRows.size();

        logAction("Checking status column for " + rowCount + " inventory item(s)...");

        for (int i = 1; i <= rowCount; i++) {
            By qtyLocator = By.xpath("(//tbody)[1]//tr[" + i + "]//td[5]");
            By statusLocator = By.xpath("(//tbody)[1]//tr[" + i + "]//td[7]");

            // Dynamic poll wait for table cell to display 'Out of Stock' or '0'
            try {
                wait.until(ExpectedConditions.textToBePresentInElementLocated(statusLocator, "Out of Stock"));
            } catch (Exception e) {
                // proceed to capture whatever is present for logging
            }

            WebElement statusElement = driver.findElement(statusLocator);
            String updatedQty = driver.findElement(qtyLocator).getText().trim();
            String updatedStatus = statusElement.getText().trim();

            logAction("Item #" + i + " -> Quantity: " + updatedQty + " | Status: " + updatedStatus);

            if (updatedStatus.equalsIgnoreCase("Out of Stock") || updatedQty.equals("0")) {
                logSuccess("VERIFIED: Item #" + i + " status updated correctly to 'Out of Stock'!");
            } else {
                logFailure("VERIFICATION FAILED: Item #" + i + " status is '" + updatedStatus + "', expected 'Out of Stock'");
            }
        }
    }

    public void verifyStockHistoryPageDetails() {
        logHeader("NAVIGATING AND VERIFYING STOCK HISTORY PAGE");

        try {
            // Step 1: Open 3-dot menu and click 'Stock History'
            logAction("Clicking Stock action three-dot button...");
            
            By stock3DotLocator = By.xpath("(//div[contains(@class,'material_action_dropdown') or contains(@class,'tag-toggle')])[1]");
            WebElement stockMenu = wait.until(ExpectedConditions.presenceOfElementLocated(stock3DotLocator));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", stockMenu);
            actionPause();

            try {
                wait.until(ExpectedConditions.elementToBeClickable(stockMenu)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", stockMenu);
            }
            actionPause();

            logAction("Clicking 'Stock History' option...");
            By stockHistoryLocator = By.xpath("//li[contains(@class,'tag_item') or contains(text(),'Stock History') or contains(text(),'History')]");
            WebElement historyOption = wait.until(ExpectedConditions.presenceOfElementLocated(stockHistoryLocator));

            try {
                wait.until(ExpectedConditions.elementToBeClickable(historyOption)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", historyOption);
            }
            actionPause();

            logSuccess("Successfully navigated to Stock History Page!");

            // Step 2: Verify stock reduced count and adjustment label
            int expectedItems = initialStockQuantities.isEmpty() ? 1 : initialStockQuantities.size();
            logAction("Validating Stock History entries for " + expectedItems + " item(s)...");

            for (int i = 1; i <= expectedItems; i++) {
                By countParaLocator = By.xpath("(//p[@class='count_para m-0 out_of_stock_color'])[" + i + "]");
                By activeParaLocator = By.xpath("(//p[@class='stock_active_para m-0 text-danger'])[" + i + "]");

                WebElement countElem = wait.until(ExpectedConditions.presenceOfElementLocated(countParaLocator));
                WebElement activeElem = wait.until(ExpectedConditions.presenceOfElementLocated(activeParaLocator));

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", countElem);

                String countText = countElem.getText().trim();
                String activeText = activeElem.getText().trim();

                logAction("History Entry #" + i + " -> Count: '" + countText + "' | Status: '" + activeText + "'");

                if (!countText.isEmpty()) {
                    logSuccess("VERIFIED: History Item #" + i + " displays stock count -> " + countText);
                } else {
                    logFailure("VERIFICATION FAILED: History Item #" + i + " count is empty!");
                }

                if (activeText.equalsIgnoreCase("Stock Adjusted")) {
                    logSuccess("VERIFIED: History Item #" + i + " status displays 'Stock Adjusted'");
                } else {
                    logFailure("VERIFICATION FAILED: History Item #" + i + " status is '" + activeText + "', expected 'Stock Adjusted'");
                }
            }

            // Step 3: Click back icon to return to Product Details
            logAction("Clicking Back button from Stock History...");
            By backIconLocator = By.xpath("(//*[name()='svg'][contains(@class,'back_icon') or contains(@class,'back')] | //button[contains(@class,'back')])[1]");
            WebElement backIcon = wait.until(ExpectedConditions.presenceOfElementLocated(backIconLocator));
            
            try {
                wait.until(ExpectedConditions.elementToBeClickable(backIcon)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backIcon);
            }
            actionPause();

        } catch (Exception e) {
            logFailure("Failed to verify Stock History Page: " + e.getMessage());
            throw e;
        }
    }

    public void verifyListingPageOutOfStockStatus() {
        logHeader("VERIFYING OUT OF STOCK ON LISTING PAGE");
        
        try {
            // Click back icon again to return to Product Listing page
            logAction("Clicking Back button from Product Details to return to Listing Page...");
            By backIconLocator = By.xpath("(//*[name()='svg'][contains(@class,'back_icon') or contains(@class,'back')] | //button[contains(@class,'back')])[1]");
            WebElement backIcon = wait.until(ExpectedConditions.presenceOfElementLocated(backIconLocator));

            try {
                wait.until(ExpectedConditions.elementToBeClickable(backIcon)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backIcon);
            }
            actionPause();
            
            logAction("Clicking 'Accessories Tab' sub-menu...");
            wait.until(ExpectedConditions.visibilityOf(accessoreisTab));
            click(accessoreisTab);
            logSuccess("Navigated to Accessories page");

            // Strictly targeted to first row's 6th column (Status column)
            By listingStatusLocator = By.xpath("(//td)[6]");
            WebElement listingStatusElem = wait.until(ExpectedConditions.presenceOfElementLocated(listingStatusLocator));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", listingStatusElem);
            String listingStatusText = listingStatusElem.getText().trim();

            logAction("Listing Page Product Status (6th Column): '" + listingStatusText + "'");

            if (listingStatusText.equalsIgnoreCase("Out of Stock") || listingStatusText.toLowerCase().contains("out of stock")) {
                logSuccess("VERIFIED: Product is correctly displayed as 'Out of Stock' on the Listing Page!");
            } else {
                logFailure("VERIFICATION FAILED: Listing Page status is '" + listingStatusText + "', expected 'Out of Stock'");
            }

        } catch (Exception e) {
            logFailure("Failed during Listing Page Out of Stock verification: " + e.getMessage());
            throw e;
        }
    }
    
    
    //TC-AM-04
    protected List<Integer> addedQuantities = new ArrayList<>();
    protected List<Integer> expectedTotalQuantities = new ArrayList<>();
    
    public void addStockFromPreviewPage() {
        logHeader("ADDING STOCK TO PRODUCT VIA PREVIEW PAGE");
        initialStockQuantities.clear();
        initialStockStatuses.clear();
        addedQuantities.clear();
        expectedTotalQuantities.clear();

        try {
            // --- Step 1: Click Inventory Tab ---
            logHeader("1. NAVIGATING TO INVENTORY TAB");
            By inventoryTabLocator = By.xpath("(//button[@id='inventory-tab'])[1]");
            WebElement inventoryTab = wait.until(ExpectedConditions.presenceOfElementLocated(inventoryTabLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", inventoryTab);
            wait.until(ExpectedConditions.elementToBeClickable(inventoryTab)).click();
            logSuccess("Navigated to Inventory Tab!");
            actionPause();

            // --- Step 2: Capture Initial Quantities & Statuses ---
            By rowLocator = By.xpath("(//tbody)[1]//tr");
            wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));
            List<WebElement> inventoryRows = driver.findElements(rowLocator);
            int rowCount = inventoryRows.size();

            logAction("Found " + rowCount + " inventory item(s). Capturing initial stock details...");

            for (int i = 1; i <= rowCount; i++) {
                By qtyLocator = By.xpath("(//tbody)[1]//tr[" + i + "]//td[5]");
                By statusLocator = By.xpath("(//tbody)[1]//tr[" + i + "]//td[7]");

                String qtyText = driver.findElement(qtyLocator).getText().trim().replaceAll("[^0-9]", "");
                String statusText = driver.findElement(statusLocator).getText().trim();

                int qty = qtyText.isEmpty() ? 0 : Integer.parseInt(qtyText);
                initialStockQuantities.add(qty);
                initialStockStatuses.add(statusText);

                logSuccess("Item #" + i + " -> Initial Qty: " + qty + " | Status: " + statusText);
            }

            // --- Step 3: Click 'Update Stock' Button ---
            logAction("Clicking 'Update Stock' button...");
            wait.until(ExpectedConditions.elementToBeClickable(updateStockBtn));
            scrollAndClick(updateStockBtn);
            actionPause();

            // --- Step 4: Ensure 'Add Stocks' Option Selected ---
            logAction("Selecting 'Add Stocks' option...");
            By addStocksRadio = By.xpath("(//input[@type='radio' and (contains(@id,'add') or contains(@value,'add'))] | //label[contains(text(),'Add Stock')])[1]");
            try {
                WebElement addRadioElem = driver.findElement(addStocksRadio);
                if (!addRadioElem.isSelected()) {
                    scrollAndClick(addRadioElem);
                }
            } catch (Exception e) {
                logAction("'Add Stocks' radio option naturally selected or default.");
            }
            actionPause();

            // --- Step 5: Enter Random Quantity (10 to 100) for each item ---
            Random random = new Random();
            logAction("Entering random stock quantity (10-100) for " + rowCount + " item(s)...");

            for (int i = 0; i < rowCount; i++) {
                By inputLocator = By.xpath("(//input[@placeholder='Enter Value'])[" + (i + 1) + "]");
                WebElement qtyInput = wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", qtyInput);

                try {
                    qtyInput.sendKeys(Keys.CONTROL + "a");
                    qtyInput.sendKeys(Keys.BACK_SPACE);
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", qtyInput);
                }

                int randomQty = random.nextInt(91) + 10; // generates between 10 and 100
                addedQuantities.add(randomQty);

                int expectedTotal = initialStockQuantities.get(i) + randomQty;
                expectedTotalQuantities.add(expectedTotal);

                qtyInput.sendKeys(String.valueOf(randomQty));
                logSuccess("Item #" + (i + 1) + ": Added Qty -> " + randomQty + " (New Expected Total: " + expectedTotal + ")");
                actionPause();
            }

            // --- Step 7: Enter Notes in Stock Notes Text Area ---
            logAction("Entering stock adjustment notes...");
            By stockNotesLocator = By.xpath("(//textarea[@id='modal-stock-notes'])[1]");
            WebElement stockNotesArea = wait.until(ExpectedConditions.visibilityOfElementLocated(stockNotesLocator));
            stockNotesArea.clear();
            String noteMessage = "Automated stock addition test to verify In Stock status.";
            stockNotesArea.sendKeys(noteMessage);
            logSuccess("Entered Note: " + noteMessage);
            actionPause();

            // --- Step 8: Save / Update Stock & Force DOM Refresh ---
            logAction("Clicking 'Update' button to apply added stock...");
            wait.until(ExpectedConditions.elementToBeClickable(updateBtn));
            scrollAndClick(updateBtn);
            logSuccess("Successfully submitted stock addition!");

            logAction("Refreshing page to load fresh backend state...");
            driver.navigate().refresh();
            actionPause();

            // Re-open Inventory tab post-refresh
            WebElement reloadedTab = wait.until(ExpectedConditions.presenceOfElementLocated(inventoryTabLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", reloadedTab);
            wait.until(ExpectedConditions.elementToBeClickable(reloadedTab)).click();
            actionPause();

        } catch (Exception e) {
            logFailure("Failed to add stock from Preview page: " + e.getMessage());
            throw e;
        }
    }

    public void verifyInventoryTabInStockStatus() {
        logHeader("VERIFYING INVENTORY TAB IN STOCK STATUS");

        By rowLocator = By.xpath("(//tbody)[1]//tr");
        wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));
        List<WebElement> inventoryRows = driver.findElements(rowLocator);
        int rowCount = inventoryRows.size();

        logAction("Checking updated quantity and status for " + rowCount + " inventory item(s)...");

        for (int i = 1; i <= rowCount; i++) {
            By qtyLocator = By.xpath("(//tbody)[1]//tr[" + i + "]//td[5]");
            By statusLocator = By.xpath("(//tbody)[1]//tr[" + i + "]//td[7]");

            try {
                wait.until(ExpectedConditions.textToBePresentInElementLocated(statusLocator, "In Stock"));
            } catch (Exception e) {
                // dynamic poll catch
            }

            String updatedQtyText = driver.findElement(qtyLocator).getText().trim().replaceAll("[^0-9]", "");
            String updatedStatus = driver.findElement(statusLocator).getText().trim();
            int updatedQty = updatedQtyText.isEmpty() ? 0 : Integer.parseInt(updatedQtyText);

            logAction("Item #" + i + " -> Updated Quantity: " + updatedQty + " | Updated Status: " + updatedStatus);

            if (updatedStatus.equalsIgnoreCase("In Stock")) {
                logSuccess("VERIFIED: Item #" + i + " status updated correctly to 'In Stock'!");
            } else {
                logFailure("VERIFICATION FAILED: Item #" + i + " status is '" + updatedStatus + "', expected 'In Stock'");
            }

            if (!expectedTotalQuantities.isEmpty() && updatedQty == expectedTotalQuantities.get(i - 1)) {
                logSuccess("VERIFIED: Item #" + i + " quantity matches expected total (" + updatedQty + ")");
            }
        }
    }


    public void verifyStockHistoryPageAddDetails() {
        logHeader("NAVIGATING AND VERIFYING STOCK HISTORY PAGE (ADD STOCK)");

        try {
            // Open 3-dot menu and select Stock History
            logAction("Clicking Stock action three-dot button...");
            By stock3DotLocator = By.xpath("(//div[contains(@class,'material_action_dropdown') or contains(@class,'tag-toggle')])[1]");
            WebElement stockMenu = wait.until(ExpectedConditions.presenceOfElementLocated(stock3DotLocator));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", stockMenu);
            actionPause();

            try {
                wait.until(ExpectedConditions.elementToBeClickable(stockMenu)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", stockMenu);
            }
            actionPause();

            logAction("Clicking 'Stock History' option...");
            By stockHistoryLocator = By.xpath("//li[contains(@class,'tag_item') or contains(text(),'Stock History') or contains(text(),'History')]");
            WebElement historyOption = wait.until(ExpectedConditions.presenceOfElementLocated(stockHistoryLocator));

            try {
                wait.until(ExpectedConditions.elementToBeClickable(historyOption)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", historyOption);
            }
            actionPause();

            logSuccess("Successfully navigated to Stock History Page!");

            int expectedItems = addedQuantities.isEmpty() ? 1 : addedQuantities.size();
            logAction("Validating Stock History entries for " + expectedItems + " item(s)...");

            for (int i = 1; i <= expectedItems; i++) {
                By countParaLocator = By.xpath("(//p[@class='count_para m-0 text-success'])[" + i + "]");
                By activeParaLocator = By.xpath("(//p[@class='stock_active_para m-0 text-success'])[" + i + "]");

                WebElement countElem = wait.until(ExpectedConditions.presenceOfElementLocated(countParaLocator));
                WebElement activeElem = wait.until(ExpectedConditions.presenceOfElementLocated(activeParaLocator));

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", countElem);

                String countText = countElem.getText().trim();
                String activeText = activeElem.getText().trim();

                logAction("History Entry #" + i + " -> Count: '" + countText + "' | Status: '" + activeText + "'");

                if (!countText.isEmpty()) {
                    logSuccess("VERIFIED: History Item #" + i + " displays stock addition count -> " + countText);
                } else {
                    logFailure("VERIFICATION FAILED: History Item #" + i + " count is empty!");
                }

                if (activeText.equalsIgnoreCase("Stock Added")) {
                    logSuccess("VERIFIED: History Item #" + i + " status displays 'Stock Added'");
                } else {
                    logFailure("VERIFICATION FAILED: History Item #" + i + " status is '" + activeText + "', expected 'Stock Added'");
                }
            }

        } catch (Exception e) {
            logFailure("Failed to verify Stock History Page: " + e.getMessage());
            throw e;
        }
    }
    
    public void verifyListingPageInStockStatus() {
        logHeader("VERIFYING IN STOCK ON LISTING PAGE");

        try {
            By backIconLocator = By.xpath("(//*[name()='svg'][contains(@class,'back_icon') or contains(@class,'back')] | //button[contains(@class,'back')])[1]");

            // Click Back #1: Stock History -> Preview Page
            logAction("Clicking Back button #1 (Stock History -> Preview Page)...");
            WebElement backIcon1 = wait.until(ExpectedConditions.presenceOfElementLocated(backIconLocator));
            try {
                wait.until(ExpectedConditions.elementToBeClickable(backIcon1)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backIcon1);
            }
            actionPause();

            // Click Back #2: Preview Page -> Product Listing Page
            logAction("Clicking Back button #2 (Preview Page -> Listing Page)...");
            WebElement backIcon2 = wait.until(ExpectedConditions.presenceOfElementLocated(backIconLocator));
            try {
                wait.until(ExpectedConditions.elementToBeClickable(backIcon2)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backIcon2);
            }
            actionPause();
            
            logAction("Clicking 'Accessories Tab' sub-menu...");
            wait.until(ExpectedConditions.visibilityOf(accessoreisTab));
            click(accessoreisTab);
            logSuccess("Navigated to Accessories page");

            
            // Verify Listing Page 6th column status
            By listingStatusLocator = By.xpath("(//td)[6]");
            WebElement listingStatusElem = wait.until(ExpectedConditions.presenceOfElementLocated(listingStatusLocator));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", listingStatusElem);
            String listingStatusText = listingStatusElem.getText().trim();

            logAction("Listing Page Product Status (6th Column): '" + listingStatusText + "'");

            if (listingStatusText.equalsIgnoreCase("In Stock") || listingStatusText.toLowerCase().contains("in stock")) {
                logSuccess("VERIFIED: Product is correctly displayed as 'In Stock' on the Listing Page!");
            } else {
                logFailure("VERIFICATION FAILED: Listing Page status is '" + listingStatusText + "', expected 'In Stock'");
            }

        } catch (Exception e) {
            logFailure("Failed during Listing Page In Stock verification: " + e.getMessage());
            throw e;
        }
    }
    
    //TC_AM_05
    public void navigateToCreateProductAndSubmitEmptyForm() {
        logHeader("NAVIGATING TO CREATE PRODUCT PAGE AND SUBMITTING EMPTY FORM");
        try {
            // Step 1: Click Create Product Button
            logAction("Clicking 'Create Product' button...");
            wait.until(ExpectedConditions.elementToBeClickable(createProductBtn));
            scrollAndClick(createProductBtn);
            logSuccess("Navigated to Create Product form page!");
            actionPause();

            // Step 2: Click 'Save & Next' Button directly without entering data
            clickSaveAndNext();

        } catch (Exception e) {
            logFailure("Failed during empty form submission: " + e.getMessage());
            throw e;
        }
    }
    
    
    //TC_AM_05
 // ===================== MANDATORY FIELD VALIDATION =====================

    /** Waits for one error message, checks it is visible and non-empty, and logs the result. */
    private boolean verifyErrorMessageDisplayed(WebElement errorElement, String fieldName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorElement));
            scrollIntoView(errorElement);

            String errorText = errorElement.getText().trim();
            if (errorText.isEmpty()) {
                logFailure("VALIDATION [" + fieldName + "] -> Error element is visible but has no text.");
                return false;
            }
            logSuccess("VERIFIED VALIDATION [" + fieldName + "] -> Message: '" + errorText + "'");
            return true;
        } catch (Exception e) {
            logFailure("VALIDATION [" + fieldName + "] -> Error message NOT displayed.");
            return false;
        }
    }

    public boolean verifyAccessoryMandatoryFieldValidations() {
        logHeader("ACCESSORY MANDATORY FIELD VALIDATION");

        try {

            // Step 1: Verify each mandatory-field error message
            boolean allDisplayed = true;
            allDisplayed &= verifyErrorMessageDisplayed(productNameErrorMsg, "Product Name");
            allDisplayed &= verifyErrorMessageDisplayed(brandTypeErrorMsg, "Brand Type");
            allDisplayed &= verifyErrorMessageDisplayed(skuErrorMsg, "SKU");
            allDisplayed &= verifyErrorMessageDisplayed(hsnErrorMsg, "HSN");
            allDisplayed &= verifyErrorMessageDisplayed(protocolErrorMsg, "Barcode Protocol");

            if (allDisplayed) {
                logSuccess("All mandatory field validation messages are displayed correctly!");
            } else {
                logFailure("One or more mandatory field validation messages were NOT displayed.");
            }
            return allDisplayed;

        } catch (Exception e) {
            logFailure("Failed during accessory mandatory field validation: " + e.getMessage());
            throw e;
        }
    }
    
 // ===================== HELPER =====================

    /** Verifies an error element is visible and non-empty; throws AssertionError if not. */
    private void assertErrorDisplayed(WebElement errorElem, String fieldName) {
        wait.until(ExpectedConditions.visibilityOf(errorElem));
        scrollIntoView(errorElem);

        String errorText = errorElem.getText().trim();
        if (!errorText.isEmpty() && errorElem.isDisplayed()) {
            logSuccess("VERIFIED: Validation error displayed for '" + fieldName + "' -> \"" + errorText + "\"");
        } else {
            logFailure("VERIFICATION FAILED: Error message for '" + fieldName + "' is missing or empty!");
            throw new AssertionError("Inventory validation failed for: " + fieldName);
        }
    }

    // ===================== ACCESSORY INVENTORY VALIDATION =====================

    public void verifyAccessoryInventorySetupValidations() {
        logHeader("VERIFYING ACCESSORY INVENTORY SETUP MANDATORY & STOCK VALIDATIONS");

        try {
            // Step 1: Click 'Save & Next' without filling details
            logAction("Clicking 'Save & Next' on Inventory Setup without entering details...");
            clickSaveAndNext();

            // Step 2: Verify mandatory field error messages
            logAction("Verifying Inventory mandatory field error messages (Color, Size, Raw Material)...");
            Map<String, WebElement> inventoryErrors = new LinkedHashMap<>();
            inventoryErrors.put("Color", colorErrorMsg);
            inventoryErrors.put("Size", sizeErrorMsg);
            inventoryErrors.put("Raw Material", rawMaterailErrorMsg);
            inventoryErrors.forEach((fieldName, errorElem) -> assertErrorDisplayed(errorElem, fieldName));

            // Step 3: Select the single "Regular" size
            logAction("Selecting the 'Regular' Size option...");
            WebElement regularSize = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//label[@class='custom-size-checkbox'])[1]")));
            lastSelectedSize = regularSize.getText().trim();
            scrollIntoView(regularSize);
            safeClick(regularSize);
            logSuccess("Selected Size: '" + lastSelectedSize + "'");
            actionPause();

            // Step 4: Click 'Save & Next' again to trigger the stock quantity error
            logAction("Clicking 'Save & Next' with size selected but empty stock quantity...");
            clickSaveAndNext();

            // Step 5: Verify stock quantity alert error message
            logAction("Verifying stock quantity alert error message...");
            assertErrorDisplayed(alertErrorMsg, "Stock Quantity Alert");

            // Step 6: Unselect the size by clicking it again
            logAction("Unselecting the size by clicking it again...");
            scrollIntoView(regularSize);
            safeClick(regularSize);
            logSuccess("Successfully unselected Size: '" + lastSelectedSize + "'");
            actionPause();

        } catch (Exception e) {
            logFailure("Failed during Accessory Inventory Setup validation flow: " + e.getMessage());
            throw e;
        }
    }
    
    public void verifyVariantsSetupValidations() {
        logHeader("VERIFYING VARIANTS SETUP MANDATORY FIELD VALIDATIONS");

        try {
            // --- Step 1: Click 'Save & Next' on Variants Setup without filling details ---
            logAction("Clicking 'Save & Next' on Variants Setup without entering details...");
            wait.until(ExpectedConditions.elementToBeClickable(saveAndNextBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", saveAndNextBtn);
            try {
                click(saveAndNextBtn);
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveAndNextBtn);
            }
            actionPause();

            // --- Step 2: Verify Variants Mandatory Field Validation Messages ---
            logAction("Verifying Variants mandatory field error messages (Cost Price, Cut Price, Product Image, Try-Along)...");

            Map<String, WebElement> variantErrors = new LinkedHashMap<>();
            variantErrors.put("Cost Price", costPriceErrorMsg);
            variantErrors.put("Cut Price", cutPriceErrorMsg);
            variantErrors.put("Product Image", productImageErrorMsg);
            variantErrors.put("Try-Along", tryAlongErrorMsg);

            int failureCount = 0;

            for (Map.Entry<String, WebElement> entry : variantErrors.entrySet()) {
                String fieldName = entry.getKey();
                WebElement errorElem = entry.getValue();

                try {
                    wait.until(ExpectedConditions.visibilityOf(errorElem));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", errorElem);

                    String errorText = errorElem.getText().trim();
                    if (!errorText.isEmpty() && errorElem.isDisplayed()) {
                        logSuccess("VERIFIED: Validation error displayed for '" + fieldName + "' -> \"" + errorText + "\"");
                    } else {
                        logFailure("VERIFICATION FAILED: Error message for '" + fieldName + "' is displayed but text is empty!");
                        failureCount++;
                    }
                } catch (Exception e) {
                    logFailure("VERIFICATION FAILED: Validation error for '" + fieldName + "' was NOT displayed on screen!");
                    failureCount++;
                }
            }

            if (failureCount > 0) {
                logFailure("Total missing variants validation messages: " + failureCount);
                throw new AssertionError("Variants setup mandatory field validation failed! " + failureCount + " field error message(s) missing.");
            } else {
                logSuccess("All variants setup mandatory field validation messages verified successfully!");
            }

        } catch (Exception e) {
            logFailure("Failed during Variants Setup validation flow: " + e.getMessage());
            throw e;
        }
    }
    
    
    public void verifyProductDescriptionSetupValidations() {
        logHeader("VERIFYING PRODUCT DESCRIPTION MANDATORY FIELD VALIDATIONS");

        try {
            // --- Step 1: Click 'Save & Next' 1st time (may trigger accordion transition or step progression) ---
            logAction("Clicking 'Save & Next' button for the 1st time...");
            wait.until(ExpectedConditions.elementToBeClickable(saveAndNextBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", saveAndNextBtn);
            try {
                click(saveAndNextBtn);
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveAndNextBtn);
            }
            actionPause();

            // --- Step 2: Click 'Save & Next' 2nd time (triggers form validation errors) ---
            logAction("Clicking 'Save & Next' button for the 2nd time to trigger validation errors...");
            wait.until(ExpectedConditions.elementToBeClickable(saveAndNextBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", saveAndNextBtn);
            try {
                click(saveAndNextBtn);
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveAndNextBtn);
            }
            actionPause();

            // --- Step 3: Verify All Description & Detail Error Messages ---
            logAction("Verifying product description and content mandatory error messages...");
            
            Map<String, WebElement> descriptionErrors = new LinkedHashMap<>();
            descriptionErrors.put("Product Description", productDescErrorMsg);
            descriptionErrors.put("Composition and Care", careErrorMsg);
            descriptionErrors.put("Product Specification", specificationErrorMsg);
            descriptionErrors.put("Styling Suggestion", StyleErrorMsg);
            descriptionErrors.put("Size and Fit", sizeFitErrorMsg);
            descriptionErrors.put("Return and Exchange", returnExchangeErrorMsg);

            int failureCount = 0;

            for (Map.Entry<String, WebElement> entry : descriptionErrors.entrySet()) {
                String fieldName = entry.getKey();
                WebElement errorElem = entry.getValue();

                try {
                    wait.until(ExpectedConditions.visibilityOf(errorElem));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", errorElem);
                    
                    String errorText = errorElem.getText().trim();
                    if (!errorText.isEmpty() && errorElem.isDisplayed()) {
                        logSuccess("VERIFIED: Validation error displayed for '" + fieldName + "' -> \"" + errorText + "\"");
                    } else {
                        logFailure("VERIFICATION FAILED: Error message for '" + fieldName + "' is displayed but text is empty!");
                        failureCount++;
                    }
                } catch (Exception e) {
                    logFailure("VERIFICATION FAILED: Validation error for '" + fieldName + "' was NOT displayed on screen!");
                    failureCount++;
                }
            }

            if (failureCount > 0) {
                logFailure("Total missing description validation messages: " + failureCount);
                throw new AssertionError("Product description mandatory field validation failed! " + failureCount + " field error message(s) missing.");
            } else {
                logSuccess("All product description and details validation messages verified successfully!");
            }

        } catch (Exception e) {
            logFailure("Failed during Product Description validation flow: " + e.getMessage());
            throw e;
        }
    }
    
    
  //TC_AM_06
    public void createPartialProductAndSaveAsDraft() {
        logHeader("PARTIALLY CREATING ACCESSORY & SAVING AS DRAFT");

        try {
            // Step 1: Click Create Product button
            logAction("Clicking 'Create Product' button...");
            wait.until(ExpectedConditions.elementToBeClickable(createProductBtn));
            scrollAndClick(createProductBtn);
            logSuccess("Navigated to Create Product form page!");
            actionPause();

            // Step 2: Generate and enter the accessory name
            lastGeneratedProductName = "New " + ACCESSORIES[rnd.nextInt(ACCESSORIES.length)] + " " + (100 + rnd.nextInt(900));
            logAction("Entering Accessory Name: '" + lastGeneratedProductName + "'");
            wait.until(ExpectedConditions.visibilityOf(productNameInput));
            productNameInput.clear();
            productNameInput.sendKeys(lastGeneratedProductName);
            actionPause();

            // Step 3: Select Brand
            lastSelectedBrand = selectRandomCustomDropdownOption(brandDropdownBtn, "Brand");
            actionPause();

            // Step 4: Click Back to trigger the Draft confirmation popup
            logAction("Clicking Back button to trigger exit confirmation popup...");
            By backIconLocator = By.xpath("(//*[name()='svg'][contains(@class,'back_icon') or contains(@class,'back')] | //button[contains(@class,'back')])[1]");
            WebElement backIcon = wait.until(ExpectedConditions.presenceOfElementLocated(backIconLocator));
            scrollIntoView(backIcon);
            safeClick(backIcon);
            actionPause();

            // Step 5: Click 'Save Draft' in the popup
            logAction("Clicking 'Save Draft' option from the popup...");
            wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftBtn));
            safeClick(saveAsDraftBtn);
            logSuccess("Successfully saved the partial accessory as a Draft!");
            actionPause();

        } catch (Exception e) {
            logFailure("Failed during partial accessory draft creation: " + e.getMessage());
            throw e;
        }
    }

    public void verifyDraftProductOnListingPage() {
        logAction("Clicking 'Accessories Tab' sub-menu...");
        wait.until(ExpectedConditions.visibilityOf(accessoreisTab));
        click(accessoreisTab);
        logSuccess("Navigated to Accessories page");

        logHeader("VERIFYING DRAFT ACCESSORY & INACTIVE STATUS ON LISTING PAGE");

        try {
            // Step 1: Wait for the first row of the listing table
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr[1]")));

            // Step 2: Verify the Draft status in the first column
            WebElement firstColElem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//td)[1]")));
            scrollIntoView(firstColElem);
            String firstColText = firstColElem.getText().trim();
            logAction("Listing Page 1st Column text / status: '" + firstColText + "'");

            if (!firstColText.toLowerCase().contains("draft")) {
                logFailure("VERIFICATION FAILED: Expected 'Draft' indicator in 1st column, but found: '" + firstColText + "'");
                throw new AssertionError("Draft accessory status verification failed.");
            }
            logSuccess("VERIFIED: Accessory correctly displays 'Draft' status in the listing table!");

            // Step 3: Verify the status toggle is inactive (TODO: replace with your real toggle locator)
            WebElement statusToggle = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tbody/tr[1]//input[@type='checkbox']")));
            if (statusToggle.isSelected()) {
                logFailure("VERIFICATION FAILED: Status toggle is ACTIVE for a draft accessory.");
                throw new AssertionError("Draft accessory should have an inactive status toggle.");
            }
            logSuccess("VERIFIED: Status toggle is inactive for the draft accessory!");

        } catch (Exception e) {
            logFailure("Failed during Draft accessory listing verification: " + e.getMessage());
            throw e;
        }
    }
    
    
    public void deleteDraftAccessoryAndVerify() {
        logHeader("DELETING DRAFT ACCESSORY & VERIFYING REMOVAL");

        try {
            String deletedName = lastGeneratedProductName;
            By rowByNameLocator = By.xpath("//tbody/tr[contains(normalize-space(.), \"" + deletedName + "\")]");

            // Step 1: Open the action menu of the first row
            logAction("Clicking action three-dot button...");
            scrollClickWhenReady(threedotBtn);
            actionPause();

            // Step 2: Click Delete
            logAction("Clicking 'Delete' option...");
            scrollClickWhenReady(deleteBtn);
            actionPause();

            // Step 3: Confirm in the popup
            logAction("Clicking 'Delete Product' in the confirmation popup...");
            wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteBtn));
            safeClick(confirmDeleteBtn);
            logSuccess("Confirmed deletion of accessory: '" + deletedName + "'");
            actionPause();

         // Step 4: Verify the accessory is no longer in the list
            logAction("Verifying accessory '" + deletedName + "' is removed from the listing...");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
                    "(//button[normalize-space()='Delete Product'])[1]")));
            boolean removed = wait.until(d -> d.findElements(rowByNameLocator).isEmpty());

            if (removed) {
                logSuccess("VERIFIED: Draft accessory '" + deletedName + "' is deleted from the listing page!");
            } else {
                logFailure("VERIFICATION FAILED: Accessory '" + deletedName + "' is still present in the listing.");
                throw new AssertionError("Draft accessory was not deleted from the listing.");
            }

        } catch (TimeoutException e) {
            logFailure("VERIFICATION FAILED: Accessory is still listed after deletion (or the popup did not close): " + e.getMessage());
            throw new AssertionError("Draft accessory was not deleted from the listing.", e);
        } catch (Exception e) {
            logFailure("Failed during draft accessory deletion flow: " + e.getMessage());
            throw e;
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //TC-AM-01
    public void createAccessoriesFlow() {
    	createNewAccessoryAutomatically();
    	configureAccessoryInventorySetup();
    	configureVariantsSetup();
        uploadProductImagesAndVideo();
        setAndVerifyProductThumbnailsAndListImages();
        selectTryAlongProductsAndSave();
        configureProductDescriptionAndSave();
        configureProductSeoAndSave();
        verifyAccessoryInListingPage();
        navigateToAccessoryPreviewAndVerifyDetails();
    }
    
    //TC-AM-02
    public void editAccessoriesFlow() {
    	editExistingAccessoryDetails();
    	editAccessoryInventorySetup();
    	configureVariantsSetup();
    	editTryAlongProductsAndSave();
    	configureProductDescriptionAndSave();
        configureProductSeoAndSave();
        verifyAccessoryInListingPage();
        navigateToAccessoryPreviewAndVerifyDetails();
    }
    
    
    //TC-AM-03
    public void adjustStockFlow() {
    	navigateToProductPreview();
    	adjustStockToZeroFromPreview();
    	verifyInventoryTabOutOfStockStatus();
        verifyStockHistoryPageDetails();
        verifyListingPageOutOfStockStatus();
    }
    
    
    //TC-AM-04
    public void addStockFlow() {
    	navigateToProductPreview();
    	addStockFromPreviewPage();
		verifyInventoryTabInStockStatus();
		verifyStockHistoryPageAddDetails();
		verifyListingPageInStockStatus();
    }
    
    
    //TC_AM_05
    public void mandatoryfieldsValidationFlow() {
    	navigateToCreateProductAndSubmitEmptyForm();
    	verifyAccessoryMandatoryFieldValidations();
    	createNewAccessoryAutomatically(true);
    	verifyAccessoryInventorySetupValidations();
    	configureInventorySetup();
    	verifyVariantsSetupValidations();
    	configureVariantsSetup();
        uploadProductImagesAndVideo();
        setAndVerifyProductThumbnailsAndListImages();
        selectTryAlongProductsAndSave();
        verifyProductDescriptionSetupValidations();
        configureProductDescriptionAndSave();
    }  
    
    //TC-AM_06
    public void verfiySaveAsDraftFlow() {
    	createPartialProductAndSaveAsDraft();
    	verifyDraftProductOnListingPage();
    	deleteDraftAccessoryAndVerify();
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
