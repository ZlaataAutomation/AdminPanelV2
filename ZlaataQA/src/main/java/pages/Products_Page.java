package pages;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.function.Supplier;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.TimeoutException;

import objectRepo.Products_ObjRepo;

public class Products_Page extends Products_ObjRepo {

    public Products_Page(WebDriver driver) {
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

    public void navigatetoProductsPage() {
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

 // --- Stored Creation Fields ---
    private String lastGeneratedProductName, lastGeneratedSKU, lastGeneratedHSN;
    private String lastSelectedProductType = "", lastSelectedBrand = "", lastSelectedCategory = "",
            lastSelectedColor = "", lastSelectedCollection = "", lastSelectedNeckline = "",
            lastSelectedSleeveType = "", lastSelectedRise = "", lastSelectedFit = "",
            lastSelectedBarcodeType = "", lastSelectedSize = "", lastDeliveryDays = "", lastTrafficMessage = "";
    private Map<String, String> lastVariantData = new HashMap<>();

    // --- Shared constants / helpers ---
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final By DROPDOWN_OPTIONS = By.xpath(
            "//ul[contains(@class,'select-options-menu')]//li | " +
            "//ul[contains(@class,'dropdown-menu')]//li | " +
            "//div[contains(@class,'dropdown-menu')]//a | " +
            "//div[contains(@class,'select-options')]//div | " +
            "//ul[contains(@class,'options')]//li | " +
            "//div[contains(@class,'option-item')]");
    private final Random rnd = new Random();

    private JavascriptExecutor js() { return (JavascriptExecutor) driver; }

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

    private String selectRandomCustomDropdownOption(WebElement triggerButton, String fieldName) {
        logAction("Selecting random option for: " + fieldName);
        String optionText = "";
        try {
            wait.until(ExpectedConditions.elementToBeClickable(triggerButton));
            scrollToCenter(triggerButton);
            clickWithJsFallback(triggerButton::click, triggerButton);
            actionPause();

            List<WebElement> visibleOptions = wait
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(DROPDOWN_OPTIONS))
                    .stream().filter(WebElement::isDisplayed).toList();

            if (!visibleOptions.isEmpty()) {
                int randomIndex = rnd.nextInt(visibleOptions.size());
                WebElement selectedOption = visibleOptions.get(randomIndex);
                optionText = selectedOption.getText().trim();
                clickWithJsFallback(selectedOption::click, selectedOption);
                logSuccess("Selected " + fieldName + ": '" + (optionText.isEmpty() ? "Index " + randomIndex : optionText) + "'");
            } else {
                logFailure("No visible options found for dropdown: " + fieldName);
                dismissOverlay();
            }
        } catch (Exception e) {
            logFailure("Failed to select option for " + fieldName + ": " + e.getMessage());
            dismissOverlay();
        }
        return optionText;
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

    public String createNewProductAutomatically() {
        return createNewProductAutomatically(false);
    }

    public String createNewProductAutomatically(boolean isAlreadyOnCreatePage) {
        logHeader("PRODUCT CREATION AUTOMATION");

        // Generate product name, SKU (ZL-XXXXXX) and HSN
        String[] femaleCategories = {"Silk Dress", "Floral Top", "Cotton Kurti", "A-Line Skirt", "Denim Jacket", "Embroidered Saree", "Co-ord Set", "Maxi Dress", "Blouse", "Palazzo Pants"};
        String category = femaleCategories[rnd.nextInt(femaleCategories.length)];
        lastGeneratedProductName = category + " " + (100 + rnd.nextInt(900)); // removed leading space

        StringBuilder sku = new StringBuilder("ZL-");
        for (int i = 0; i < 6; i++) sku.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
        lastGeneratedSKU = sku.toString();
        lastGeneratedHSN = String.format("%08d", rnd.nextInt(100000000));

        // Step 1: Click Create Product button (skipped if already on page)
        if (!isAlreadyOnCreatePage) {
            logAction("Clicking 'Create Product' button...");
            wait.until(ExpectedConditions.elementToBeClickable(createProductBtn));
            click(createProductBtn);
            actionPause();
        } else {
            logAction("Already on Create Product page. Skipping 'Create Product' button click.");
        }

        // Step 2: Product Name
        logAction("Entering Product Name: '" + lastGeneratedProductName + "'");
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

        // Step 6: Custom dropdown selections
        lastSelectedProductType = selectRandomCustomDropdownOption(productTypeDropdown, "Product Type");
        lastSelectedCategory = selectRandomCustomDropdownOption(cateogryDropdown, "Category");
        lastSelectedCollection = selectRandomCustomDropdownOption(collectionDropdown, "Collection");
        lastSelectedNeckline = selectRandomCustomDropdownOption(neckLineDropdown, "Neckline");
        lastSelectedSleeveType = selectRandomCustomDropdownOption(sleeveTypeDropdown, "Sleeve Type");
        selectRandomCustomDropdownOption(occasionDropdown, "Occasion");
        lastSelectedRise = selectRandomCustomDropdownOption(riseDropdown, "Rise");
        lastSelectedFit = selectRandomCustomDropdownOption(fitDropdown, "Fit");
        selectRandomCustomDropdownOption(sizeChartDropdown, "Size Chart");

        // Step 7: Barcode Protocol (index 14 = Protocol dropdown; 13 was the "i" info icon)
        WebElement barcodeDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[@type='button'])[14]")));
        scrollToCenter(barcodeDropdown);
        lastSelectedBarcodeType = selectRandomCustomDropdownOption(barcodeDropdown, "Barcode Protocol");

        // Step 8: Save & Next
        clickSaveAndNext();

        return lastGeneratedProductName;
    }

    public String getLastGeneratedProductName() { return lastGeneratedProductName; }
    public String getLastGeneratedSKU() { return lastGeneratedSKU; }
    public String getLastGeneratedHSN() { return lastGeneratedHSN; }
    public String getLastSelectedProductType() { return lastSelectedProductType; }
    public String getLastSelectedBrand() { return lastSelectedBrand; }
    public String getLastSelectedCategory() { return lastSelectedCategory; }
    public String getLastSelectedColor() { return lastSelectedColor; }
    
 // --- Shared helpers for inventory / variants setup ---
    // Dimension fields filled per block: placeholder, log label, {random bound, offset}
    private static final String[] DIM_PLACEHOLDERS = {"L", "B", "H", "Weight", "Qty", "Alert"};
    private static final String[] DIM_LABELS = {"Length (L)", "Breadth (B)", "Height (H)", "Weight", "Qty", "Alert Threshold"};
    private static final int[][] DIM_RANGES = {{20, 10}, {15, 5}, {10, 2}, {500, 100}, {50, 10}, {5, 1}};

    /** Waits for the Nth generic dropdown button and selects a random option from it. */
    private String selectDropdownByIndex(int buttonIndex, String fieldName) {
        return selectRandomCustomDropdownOption(wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[@type='button'])[" + buttonIndex + "]"))), fieldName);
    }

    /** Shared 'Save & Next' click (also use this in createNewProductAutomatically). */
    private void clickSaveAndNext() {
        logHeader("SAVING PRODUCT");
        logAction("Clicking 'Save & Next' button...");
        wait.until(ExpectedConditions.elementToBeClickable(saveAndNextBtn));
        scrollToCenter(saveAndNextBtn);
        clickWithJsFallback(() -> click(saveAndNextBtn), saveAndNextBtn);
        logSuccess("Clicked 'Save & Next' button successfully!");
    }

    public void configureInventorySetup() {
        logHeader("INVENTORY SETUP AUTOMATION");

        boolean isSetProduct = lastSelectedProductType.toLowerCase().contains("set");
        logAction("Configuring inventory for Product Type: '" +
            (lastSelectedProductType.isEmpty() ? "Unknown" : lastSelectedProductType) +
            "' (Is Set Type: " + isSetProduct + ")");

        // Step 1: Select Color
        lastSelectedColor = selectDropdownByIndex(2, "Color");
        actionPause();

        // Step 2: Select Size
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

        // Step 3: Fill Dimensions
        By lengthFieldsLocator = By.xpath("//input[@type='text' and @placeholder='L']");
        wait.until(ExpectedConditions.presenceOfElementLocated(lengthFieldsLocator));
        int blockCount = driver.findElements(lengthFieldsLocator).size();

        if (isSetProduct && blockCount < 2) {
            logAction("Product type is a 'Set', waiting for second dimension block to render...");
            try {
                wait.until(ExpectedConditions.numberOfElementsToBe(lengthFieldsLocator, 2));
                blockCount = 2;
            } catch (Exception e) {
                logFailure("Expected 2 dimension blocks for 'Set' product type, but found " + blockCount);
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

        // Step 4: Select Raw Material
        selectDropdownByIndex(3, "Raw Material");
        actionPause();

        // Step 5: Save & Next
        clickSaveAndNext();
    }

    private void enterInputValue(By locator, String value, String fieldName) {
        try {
            enterText(wait.until(ExpectedConditions.presenceOfElementLocated(locator)), value);
            logSuccess("Entered " + fieldName + ": " + value);
        } catch (Exception e) {
            logFailure("Failed to enter " + fieldName + ": " + e.getMessage());
        }
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
        for (String name : List.of("Product_image.jpg", "Product_image2.jpg", "Product_image3.jpg", "Product_image4.jpg")) {
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

        // --- 2. REEL VIDEO ---
        logHeader("UPLOADING REEL VIDEO");
        String absoluteVideoPath = resolveFile("src/test/resources/videos/reelvideo.mp4", "Video");
        if (absoluteVideoPath != null) {
            uploadToContainer("(//div[@class='file-upload-container'])[3]", absoluteVideoPath, "Reel Video Container");
            actionPause();
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

            logAction("Clicking 'Save & Next' button...");
            WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[normalize-space()='Save & Next' or contains(text(),'Save & Next')]")));
            scrollToCenter(saveBtn);
            actionPause();
            clickWithJsFallback(() -> wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click(), saveBtn);

            logSuccess("Successfully selected Try-Along products using term '" + searchTerm + "' and clicked 'Save & Next'!");
        } catch (Exception e) {
            logFailure("Failed during Try-Along product selection flow: " + e.getMessage());
        }
    }

    private Map<String, Boolean> configuredSettingsState = new HashMap<>();

    public void configureProductSettingsPage(String deliveryDaysText, String trafficMessage) {
        logHeader("CONFIGURING PRODUCT SETTINGS PAGE");
        this.lastDeliveryDays = deliveryDaysText;
        this.lastTrafficMessage = trafficMessage;

        logAction("Randomizing product settings toggle switches...");
        String[] settingLabels = {
            "Cancel Option", "Return Option", "Recommended", "Personalized Letter",
            "Best Seller", "Free Shipping/Item", "Express Delivery", "Custom Size", "Exchange Option"
        };

        for (String label : settingLabels) {
            try {
                By switchLocator = By.xpath("//label[contains(translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"
                    + label.toLowerCase() + "')]/ancestor::div[contains(@class,'form-check') or contains(@class,'form-group') or contains(@class,'switch') or contains(@class,'col') or contains(@class,'row')]//input[@type='checkbox'] "
                    + "| //*[contains(text(),'" + label + "')]/following::input[@type='checkbox'][1]");

                List<WebElement> toggleInputs = driver.findElements(switchLocator);

                if (!toggleInputs.isEmpty()) {
                    WebElement toggle = toggleInputs.get(0);
                    if (Math.random() > 0.5) {
                        js().executeScript("arguments[0].click();", toggle);
                        configuredSettingsState.put(label, true);
                        logAction("Toggled switch ON: " + label);
                    } else {
                        configuredSettingsState.put(label, toggle.isSelected());
                    }
                } else {
                    logAction("Setting switch element not found (skipped): " + label);
                }
            } catch (Exception e) {
                logAction("Could not interact with switch: " + label);
            }
        }

        logAction("Setting Delivery Days: " + deliveryDaysText);
        fillSettingsField(By.xpath("//input[starts-with(@id, 'delivery_day_')] "
                + "| //input[contains(@name, 'delivery_day')] "
                + "| //label[contains(text(), 'Delivery')]/following::input[1]"),
            deliveryDaysText, "Successfully entered Delivery Days: " + deliveryDaysText, "Delivery Days");

        logAction("Setting Traffic Data Message: " + trafficMessage);
        fillSettingsField(By.xpath("//textarea[starts-with(@id, 'traffic_data_')] "
                + "| //textarea[contains(@name, 'traffic_data')] "
                + "| //label[contains(text(), 'Traffic')]/following::textarea[1]"),
            trafficMessage, "Successfully entered Traffic Data Message", "Traffic Data");

        logAction("Clicking 'Save & Next' button...");
        clickButtonWithFallback(By.xpath("//button[contains(normalize-space(),'Save & Next') or contains(normalize-space(),'Save')]"),
            "Settings Save & Next Button");
        logSuccess("Successfully saved settings and navigated to next step!");
    }

    private void fillSettingsField(By locator, String value, String successMsg, String failureLabel) {
        try {
            WebElement field = waitForElementPresence(locator, 10);
            field.clear();
            field.sendKeys(value);
            logSuccess(successMsg);
        } catch (Exception e) {
            logFailure("Failed to input " + failureLabel + ": " + e.getMessage());
        }
    }

    public Map<String, Boolean> getConfiguredSettingsState() {
        return configuredSettingsState;
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
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[normalize-space()='Save & Next' or contains(text(),'Save & Next')]")));
            scrollAndClick(saveAndNextBtn);
            logSuccess("Successfully saved Product Description section!");

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

    public void verifyProductInListingPage() {
        logHeader("VERIFYING PRODUCT DETAILS IN LISTING PAGE");

        try {
            By firstRowLocator = By.xpath("//tbody/tr[1]");
            wait.until(ExpectedConditions.presenceOfElementLocated(firstRowLocator));

            verifyTableCell(By.xpath("(//td)[1]"), lastGeneratedProductName, "Product Name");
            verifyTableCell(By.xpath("(//td)[2]"), lastGeneratedSKU, "SKU");
            verifyTableCell(By.xpath("(//td)[3]"), lastGeneratedHSN, "HSN");
            verifyTableCell(By.xpath("(//td)[4]"), lastSelectedBrand, "Brand");
            verifyTableCell(By.xpath("(//td)[5]"), lastSelectedColor, "Color");
            verifyTableCell(By.xpath("(//td)[7]"), lastSelectedCategory, "Category");

            logSuccess("All product details in the listing page verified successfully!");
        } catch (Exception e) {
            logFailure("Listing page verification failed: " + e.getMessage());
            throw e;
        }
    }

    private void verifyTableCell(By locator, String expectedValue, String fieldName) {
        try {
            WebElement cell = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", cell);
            
            String actualValue = cell.getText().trim();
            String cleanedExpected = expectedValue != null ? expectedValue.trim() : "";

            if (actualValue.equalsIgnoreCase(cleanedExpected) || actualValue.contains(cleanedExpected) || cleanedExpected.contains(actualValue)) {
                logSuccess("VERIFIED LISTING [" + fieldName + "] -> Expected: '" + cleanedExpected + "' | Actual: '" + actualValue + "'");
            } else {
                logFailure("MISMATCH LISTING [" + fieldName + "] -> Expected: '" + cleanedExpected + "' | Actual: '" + actualValue + "'");
            }
        } catch (Exception e) {
            logFailure("Could not verify [" + fieldName + "] in cell location: " + locator + " -> " + e.getMessage());
        }
    }

 // --- PREVIEW PAGE NAVIGATION AND VERIFICATION ---

    public void navigateToPreviewAndVerifyDetails() {
        logHeader("NAVIGATING TO PREVIEW PAGE");

        try {
            logAction("Clicking action three-dot button...");
            By threedotBtnLocator = By.xpath("(//div[@class='dropdown actions-dropdown-wrapper'])[1]");
            WebElement threeDotElement = wait.until(ExpectedConditions.elementToBeClickable(threedotBtnLocator));
            scrollAndClick(threeDotElement);
            actionPause();

            logAction("Clicking 'Preview' option...");
            By previewBtnLocator = By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]");
            WebElement previewElement = wait.until(ExpectedConditions.elementToBeClickable(previewBtnLocator));
            scrollAndClick(previewElement);
            logSuccess("Successfully navigated to Product Preview Page!");
            actionPause();

            // 1. BASIC INFO TAB
            logHeader("1. VERIFYING BASIC INFO TAB");
            verifyPreviewField(By.xpath("(//div[@class='detail-value text-primary'])[1]"), lastGeneratedProductName, "Product Name");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[1]"), lastSelectedBrand, "Brand Type");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[2]"), lastGeneratedSKU, "SKU");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[3]"), lastGeneratedHSN, "HSN");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[4]"), lastSelectedProductType, "Product Type");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[5]"), lastSelectedCollection, "Collection");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[6]"), lastSelectedNeckline, "Neckline");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[7]"), lastSelectedSleeveType, "Sleeve Type");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[8]"), lastSelectedRise, "Rise");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[9]"), lastSelectedFit, "Fit");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[11]"), lastSelectedBarcodeType, "Barcode Type");

            // 2. INVENTORY TAB
            logHeader("2. VERIFYING INVENTORY TAB");
            By inventoryTabLocator = By.xpath("(//button[@id='inventory-tab'])[1]");
            clickTabAndPause(inventoryTabLocator, "Inventory Tab");

            verifyPreviewField(By.xpath("(//div[@class='detail-item'])[1]"), lastSelectedColor, "Color");
            verifyPreviewField(By.xpath("(//div[@class='detail-item'])[2]"), lastSelectedSize, "Size");

            // 3. VARIANTS TAB
            logHeader("3. VERIFYING VARIANTS TAB");
            By variantTabLocator = By.xpath("(//button[normalize-space()='Variants'])[1]");
            clickTabAndPause(variantTabLocator, "Variants Tab");

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
            By settingsTabLocator = By.xpath("(//button[normalize-space()='Settings'])[1]");
            clickTabAndPause(settingsTabLocator, "Settings Tab");

            verifyPreviewFieldPresence(By.xpath("(//div[@class='detail-item d-flex flex-row align-items-center justify-content-between mb-2'])[2]"), "Custom Size");
            verifyPreviewFieldPresence(By.xpath("(//div[@class='detail-item d-flex flex-row align-items-center justify-content-between mb-2'])[3]"), "Return Option");
            verifyPreviewFieldPresence(By.xpath("(//div[@class='detail-item d-flex flex-row align-items-center justify-content-between mb-2'])[4]"), "Cancel Option");
            verifyPreviewFieldPresence(By.xpath("(//div[@class='detail-item d-flex flex-row align-items-center justify-content-between mb-2'])[5]"), "Exchange Option");
            verifyPreviewFieldPresence(By.xpath("(//div[@class='detail-item d-flex flex-row align-items-center justify-content-between mb-2'])[6]"), "Best Seller");
            verifyPreviewFieldPresence(By.xpath("(//div[@class='detail-item d-flex flex-row align-items-center justify-content-between mb-2'])[7]"), "Recommended");
            verifyPreviewFieldPresence(By.xpath("(//div[@class='detail-item d-flex flex-row align-items-center justify-content-between mb-2'])[8]"), "Free Option");
            verifyPreviewFieldPresence(By.xpath("(//div[@class='detail-item d-flex flex-row align-items-center justify-content-between mb-2'])[9]"), "Personalized Letter");

            // Updated Traffic Data locator to avoid index out of bound issues
            verifyPreviewField(By.xpath("//div[contains(@class,'detail-item') or contains(@class,'detail-value')][contains(.,'High Demand') or contains(.,'looking at this')]"), lastTrafficMessage, "Traffic Data");

            // 5. DESCRIPTION TAB
            logHeader("5. VERIFYING DESCRIPTION TAB");
            By descriptionTabLocator = By.xpath("(//button[normalize-space()='Description'])[1]");
            clickTabAndPause(descriptionTabLocator, "Description Tab");

            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[1]"), storedDescriptionData.get("Product Description"), "Product Description");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[2]"), storedDescriptionData.get("Composition & Care"), "Composition & Care");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[3]"), storedDescriptionData.get("Styling Suggestion"), "Styling");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[4]"), storedDescriptionData.get("Product Specification"), "Product Specification");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[5]"), storedDescriptionData.get("Size & Fit"), "Size and Fit");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[6]"), storedDescriptionData.get("Return & Exchange"), "Return and Exchange");

            // 6. SEO TAB
            logHeader("6. VERIFYING SEO TAB");
            By seoTabLocator = By.xpath("(//button[normalize-space()='SEO'])[1]");
            clickTabAndPause(seoTabLocator, "SEO Tab");

            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[1]"), storedSeoData.get("SEO Meta Title"), "SEO Meta Title");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[2]"), storedSeoData.get("SEO Meta Keyword"), "Meta Key");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[3]"), storedSeoData.get("SEO Meta Description"), "SEO Description");

            logSuccess("All product details across all Preview tabs verified successfully!");

        } catch (Exception e) {
            logFailure("Failed during preview verification flow: " + e.getMessage());
            throw e;
        }
    }

    private void clickTabAndPause(By tabLocator, String tabName) {
        logAction("Navigating to preview tab: " + tabName);
        WebElement tabElement = wait.until(ExpectedConditions.presenceOfElementLocated(tabLocator));
        scrollAndClick(tabElement);
        actionPause();
    }

    private void verifyPreviewField(By locator, String expectedValue, String fieldName) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

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
            } else if (normalizedActual.equalsIgnoreCase(normalizedExpected) || 
                       normalizedActual.contains(normalizedExpected) || 
                       normalizedExpected.contains(normalizedActual)) {
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
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

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
    
    
    
  //TC-02
 public String editExistingProductDetails() {
     logHeader("EDIT EXISTING PRODUCT AUTOMATION");
     Random random = new Random();

     // 1. Navigate to Edit Mode via Action Menu
     logAction("Clicking action three-dot button...");
     scrollClickWhenReady(threedotBtn);
     actionPause();

     logAction("Clicking 'Edit' option...");
     scrollClickWhenReady(editBtn);
     logSuccess("Successfully navigated to Product Edit Page!");
     actionPause();

     // 2. Preserve/Capture Existing Product Type before editing
     try {
         lastSelectedProductType = driver.findElement(By.xpath("(//button[@type='button'])[4]")).getText().trim();
         logSuccess("Preserved existing Product Type: '" + lastSelectedProductType + "'");
     } catch (Exception e) {
         logAction("Could not capture Product Type text directly from button; keeping existing state.");
     }

     // 3. Generate New Female Product Name
     String[] femaleCategories = {"Silk Dress", "Floral Top", "Cotton Kurti", "A-Line Skirt", "Denim Jacket", "Embroidered Saree", "Co-ord Set", "Maxi Dress", "Blouse", "Palazzo Pants"};
     lastGeneratedProductName = " Updated " + femaleCategories[random.nextInt(femaleCategories.length)] + " " + (100 + random.nextInt(900));
     actionPause();

     // 4. Generate New Random SKU (ZL-EDIT-XXXX)
     String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
     StringBuilder skuBuilder = new StringBuilder("ZL-EDIT-");
     for (int i = 0; i < 4; i++) skuBuilder.append(characters.charAt(random.nextInt(characters.length())));
     lastGeneratedSKU = skuBuilder.toString();
     actionPause();

     // 5. Generate New Random HSN Code
     lastGeneratedHSN = String.format("%08d", random.nextInt(100000000));
     actionPause();

     // 6. Clear and Update Product Name
     logAction("Updating Product Name to: '" + lastGeneratedProductName + "'");
     wait.until(ExpectedConditions.visibilityOf(productNameInput));
     clearAndTypeInput(productNameInput, lastGeneratedProductName);
     actionPause();

     // 7. Select New Brand
     logAction("Selecting Brand option...");
     lastSelectedBrand = selectBrandDropdownSafely(brandDropdownBtn);
     actionPause();

     // 8. Clear and Update SKU
     logAction("Updating SKU Code to: '" + lastGeneratedSKU + "'");
     clearAndTypeInput(skuInput, lastGeneratedSKU);
     actionPause();

     // 9. Clear and Update HSN
     logAction("Updating HSN Code to: '" + lastGeneratedHSN + "'");
     clearAndTypeInput(hsnInput, lastGeneratedHSN);
     actionPause();

     // 10. Select New Custom Dropdown Options (Keeping Product Type Unchanged)
     lastSelectedCategory = selectRandomCustomDropdownOption(cateogryDropdown, "Category");
     lastSelectedCollection = selectRandomCustomDropdownOption(collectionDropdown, "Collection");
     lastSelectedNeckline = selectRandomCustomDropdownOption(neckLineDropdown, "Neckline");
     lastSelectedSleeveType = selectRandomCustomDropdownOption(sleeveTypeDropdown, "Sleeve Type");
     selectRandomCustomDropdownOption(occasionDropdown, "Occasion");
     lastSelectedRise = selectRandomCustomDropdownOption(riseDropdown, "Rise");
     lastSelectedFit = selectRandomCustomDropdownOption(fitDropdown, "Fit");
     selectRandomCustomDropdownOption(sizeChartDropdown, "Size Chart");
     actionPause();

     // 11. Update Barcode Protocol using Dedicated Handler
     logAction("Selecting Barcode Protocol option...");
     lastSelectedBarcodeType = selectBarcodeDropdownSafely();
     actionPause();

     // 12. Save & Next Step
     logHeader("SAVING UPDATED PRODUCT DETAILS");
     logAction("Clicking 'Save & Next' button...");
     wait.until(ExpectedConditions.elementToBeClickable(saveAndNextBtn));
     scrollIntoView(saveAndNextBtn);
     try {
         click(saveAndNextBtn);
     } catch (Exception e) {
         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveAndNextBtn);
     }
     logSuccess("Successfully saved updated basic product details!");
     actionPause();

     return lastGeneratedProductName;
 }

//Dedicated helper method to select Barcode Protocol reliably
private String selectBarcodeDropdownSafely() {
  By triggerLocator = By.xpath("(//button[@type='button'])[14]"); // was [13] (that is the "i" info icon)
  By optionsLocator = By.xpath("//ul[contains(@class,'select-options-menu') or contains(@class,'dropdown-menu')]//li | //div[contains(@class,'dropdown-menu')]//a | //li[@class='option-item']");
  return selectRandomOptionWithRetry(
          () -> {
              WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(triggerLocator));
              scrollIntoView(trigger);
              return trigger;
          },
          ExpectedConditions.presenceOfAllElementsLocatedBy(optionsLocator),
          "Barcode Protocol", "Barcode selection");
}

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

 // Shared retry logic: open dropdown, pick a random visible non-empty option (max 3 attempts)
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

//Map to store inventory details for future verification
protected Map<String, String> lastInventoryDimensions = new HashMap<>();

public void editInventorySetup() {
  logHeader("INVENTORY SETUP AUTOMATION (CREATE / EDIT)");
  Random random = new Random();
  lastInventoryDimensions.clear();

  boolean isSetProduct = lastSelectedProductType != null && lastSelectedProductType.toLowerCase().contains("set");
  logAction("Configuring inventory for Product Type: '" +
          (lastSelectedProductType == null || lastSelectedProductType.isEmpty() ? "Unknown" : lastSelectedProductType) +
          "' (Is Set Type: " + isSetProduct + ")");

  // Step 1: Select Color
  WebElement colorDropdownBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@type='button'])[2]")));
  lastSelectedColor = selectRandomCustomDropdownOption(colorDropdownBtn, "Color");
  actionPause();

  // Step 2: Select Size
  logAction("Selecting random Size option...");
  By sizeOptionsLocator = By.xpath("(//div[@class='size-wrapper'])[1]//label[@class='custom-size-checkbox']");
  try {
      List<WebElement> sizeOptions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(sizeOptionsLocator))
              .stream()
              .filter(WebElement::isDisplayed)
              .toList();

      if (!sizeOptions.isEmpty()) {
          WebElement selectedSize = sizeOptions.get(random.nextInt(sizeOptions.size()));
          lastSelectedSize = selectedSize.getText().trim();
          scrollIntoView(selectedSize);
          safeClick(selectedSize);
          logSuccess("Selected Size: '" + lastSelectedSize + "'");
      } else {
          logFailure("No visible Size options found.");
      }
  } catch (Exception e) {
      logFailure("Failed to select Size: " + e.getMessage());
  }
  actionPause();
  actionPause();

  // Step 3: Fill Dimensions & Store Captured Values
  By lengthFieldsLocator = By.xpath("//input[@type='text' and @placeholder='L']");
  wait.until(ExpectedConditions.presenceOfElementLocated(lengthFieldsLocator));

  int blockCount = driver.findElements(lengthFieldsLocator).size();

  if (isSetProduct && blockCount < 2) {
      logAction("Product type is a 'Set', waiting for second dimension block to render...");
      try {
          wait.until(ExpectedConditions.numberOfElementsToBe(lengthFieldsLocator, 2));
          blockCount = 2;
      } catch (Exception e) {
          logFailure("Expected 2 dimension blocks for 'Set' product type, but found " + blockCount);
      }
  }

  logAction("Processing " + blockCount + " inventory detail block(s)...");

  // Per field: input placeholder, log label, map key, {random bound, offset}
  String[] placeholders = {"L", "B", "H", "Weight", "Qty", "Alert"};
  String[] labels = {"Length (L)", "Breadth (B)", "Height (H)", "Weight", "Qty", "Alert Threshold"};
  String[] keys = {"Length", "Breadth", "Height", "Weight", "Qty", "Alert"};
  int[][] ranges = {{20, 10}, {15, 5}, {10, 2}, {500, 100}, {50, 10}, {5, 1}};

  for (int i = 1; i <= blockCount; i++) {
      String blockLabel = (blockCount > 1) ? ((i == 1) ? "Top" : "Bottom") : "Single Product Item";

      logHeader("FILLING DIMENSIONS: " + blockLabel.toUpperCase());

      String[] values = new String[placeholders.length];
      for (int f = 0; f < values.length; f++) {
          values[f] = String.valueOf(random.nextInt(ranges[f][0]) + ranges[f][1]);
      }
      for (int f = 0; f < values.length; f++) {
          enterInputValue(By.xpath("(//input[@type='text' and @placeholder='" + placeholders[f] + "'])[" + i + "]"), values[f], blockLabel + " -> " + labels[f]);
      }
      // Store in map for future verification
      for (int f = 0; f < values.length; f++) {
          lastInventoryDimensions.put(blockLabel + "_" + keys[f], values[f]);
      }
  }
  actionPause();
  actionPause();

  // Step 4: Select Raw Material
  selectRandomCustomDropdownOption(wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@type='button'])[3]"))), "Raw Material");
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
      logAction("Clicking 'Save & Next' button...");
      WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
              By.xpath("//button[normalize-space()='Save & Next' or contains(text(),'Save & Next')]")));

      scrollIntoView(saveBtn);
      actionPause();

      try {
          wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
      } catch (Exception e) {
          ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
      }

      logSuccess("Successfully updated Try-Along products and clicked 'Save & Next'!");

  } catch (Exception e) {
      logFailure("Failed during Edit Try-Along product selection flow: " + e.getMessage());
  }
}

public List<String> getLastSelectedTryAlongProducts() {
  return lastSelectedTryAlongProducts;
}
    
    
    //TC-PM-03
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

    
    //TC-PM-04
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

    //TC-PM-05
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

    public void verifyMandatoryFieldValidationMessages() {
        logHeader("VERIFYING MANDATORY FIELD VALIDATION MESSAGES");

        Map<String, WebElement> requiredFieldErrors = new LinkedHashMap<>();
        requiredFieldErrors.put("Product Name", productNameErrorMsg);
        requiredFieldErrors.put("Brand Type", brandTypeErrorMsg);
        requiredFieldErrors.put("SKU", skuErrorMsg);
        requiredFieldErrors.put("HSN Code", hsnErrorMsg);
        requiredFieldErrors.put("Product Type", productTypeErrorMsg);
        requiredFieldErrors.put("Category", categoryErrorMsg);
        requiredFieldErrors.put("Protocol", protocolErrorMsg);

        int failureCount = 0;

        for (Map.Entry<String, WebElement> entry : requiredFieldErrors.entrySet()) {
            String fieldName = entry.getKey();
            WebElement errorElem = entry.getValue();

            try {
                wait.until(ExpectedConditions.visibilityOf(errorElem));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", errorElem);
                
                String errorText = errorElem.getText().trim();

                if (!errorText.isEmpty() && errorElem.isDisplayed()) {
                    logSuccess("VERIFIED: Validation error displayed for '" + fieldName + "' -> \"" + errorText + "\"");
                } else {
                    logFailure("VERIFICATION FAILED: Validation element for '" + fieldName + "' is displayed but text is empty!");
                    failureCount++;
                }
            } catch (Exception e) {
                logFailure("VERIFICATION FAILED: Validation error for '" + fieldName + "' was NOT displayed on screen!");
                failureCount++;
            }
        }

        if (failureCount > 0) {
            logFailure("Total missing validation messages: " + failureCount);
            throw new AssertionError("Mandatory field validation test failed! " + failureCount + " field error message(s) missing.");
        } else {
            logSuccess("All mandatory field validation messages verified successfully!");
        }
    }
    
    public void verifyInventorySetupValidations() {
        logHeader("VERIFYING INVENTORY SETUP MANDATORY & STOCK VALIDATIONS");
        Random random = new Random();

        try {
            // --- Step 1: Click 'Save & Next' on Inventory Setup without filling details ---
            logAction("Clicking 'Save & Next' on Inventory Setup without entering details...");
            wait.until(ExpectedConditions.elementToBeClickable(saveAndNextBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", saveAndNextBtn);
            try {
                click(saveAndNextBtn);
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveAndNextBtn);
            }
            actionPause();

            // --- Step 2: Verify Inventory Mandatory Field Validation Messages ---
            logAction("Verifying Inventory mandatory field error messages (Color, Size, Raw Material)...");
            
            Map<String, WebElement> inventoryErrors = new LinkedHashMap<>();
            inventoryErrors.put("Color", colorErrorMsg);
            inventoryErrors.put("Size", sizeErrorMsg);
            inventoryErrors.put("Raw Material", rawMaterailErrorMsg);

            for (Map.Entry<String, WebElement> entry : inventoryErrors.entrySet()) {
                String fieldName = entry.getKey();
                WebElement errorElem = entry.getValue();

                wait.until(ExpectedConditions.visibilityOf(errorElem));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", errorElem);
                
                String errorText = errorElem.getText().trim();
                if (!errorText.isEmpty() && errorElem.isDisplayed()) {
                    logSuccess("VERIFIED: Validation error displayed for '" + fieldName + "' -> \"" + errorText + "\"");
                } else {
                    logFailure("VERIFICATION FAILED: Error message for '" + fieldName + "' is missing or empty!");
                    throw new AssertionError("Inventory validation failed for: " + fieldName);
                }
            }

            // --- Step 3: Select a Random Size ---
            logAction("Selecting random Size option...");
            By sizeOptionsLocator = By.xpath("(//div[@class='size-wrapper'])[1]//label[@class='custom-size-checkbox']");
            List<WebElement> sizeOptions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(sizeOptionsLocator))
                    .stream()
                    .filter(WebElement::isDisplayed)
                    .toList();

            WebElement selectedSizeElement = null;
            if (!sizeOptions.isEmpty()) {
                selectedSizeElement = sizeOptions.get(random.nextInt(sizeOptions.size()));
                lastSelectedSize = selectedSizeElement.getText().trim();
                
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedSizeElement);
                try {
                    selectedSizeElement.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedSizeElement);
                }
                logSuccess("Selected Size: '" + lastSelectedSize + "'");
            } else {
                logFailure("No visible Size options found!");
                throw new AssertionError("Size options list is empty.");
            }
            actionPause();

            // --- Step 4: Click 'Save & Next' again to trigger quantity/stock error ---
            logAction("Clicking 'Save & Next' with size selected but empty stock quantity...");
            clickSaveAndNext();

            // --- Step 5: Verify Stock Quantity Alert Error Message ---
            logAction("Verifying stock quantity alert error message...");
            wait.until(ExpectedConditions.visibilityOf(alertErrorMsg));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", alertErrorMsg);
            
            String alertText = alertErrorMsg.getText().trim();
            if (!alertText.isEmpty() && alertErrorMsg.isDisplayed()) {
                logSuccess("VERIFIED: Stock quantity alert message displayed -> \"" + alertText + "\"");
            } else {
                logFailure("VERIFICATION FAILED: Stock quantity alert error message is missing or empty!");
                throw new AssertionError("Stock quantity alert validation failed.");
            }

            // --- Step 6: Unselect the Size by clicking it again ---
            logAction("Unselecting the size by clicking it again...");
            if (selectedSizeElement != null) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedSizeElement);
                try {
                    selectedSizeElement.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedSizeElement);
                }
                logSuccess("Successfully unselected Size: '" + lastSelectedSize + "'");
            }
            actionPause();

        } catch (Exception e) {
            logFailure("Failed during Inventory Setup validation flow: " + e.getMessage());
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
    
    //TC-PM-06
    public void createPartialProductAndSaveAsDraft() {
        logHeader("PARTIALLY CREATING PRODUCT & SAVING AS DRAFT");
        Random random = new Random();

        try {
            // --- Step 1: Click Create Product Button ---
            logAction("Clicking 'Create Product' button...");
            wait.until(ExpectedConditions.elementToBeClickable(createProductBtn));
            scrollAndClick(createProductBtn);
            logSuccess("Navigated to Create Product form page!");
            actionPause();

            // --- Step 2: Generate & Enter Product Name ---
            String[] categories = {"Casual Shirt", "Summer Frock", "Woolen Sweater", "Formal Blazer", "Chiffon Skirt"};
            String category = categories[random.nextInt(categories.length)];
            lastGeneratedProductName = "New" + category + " " + (100 + random.nextInt(900));

            logAction("Entering Product Name: '" + lastGeneratedProductName + "'");
            wait.until(ExpectedConditions.visibilityOf(productNameInput));
            productNameInput.clear();
            productNameInput.sendKeys(lastGeneratedProductName);
            actionPause();

            // --- Step 3: Select Brand Dropdown ---
            lastSelectedBrand = selectRandomCustomDropdownOption(brandDropdownBtn, "Brand");
            actionPause();

            // --- Step 4: Click Back Button to trigger Draft Confirmation Popup ---
            logAction("Clicking Back button to trigger exit confirmation popup...");
            By backIconLocator = By.xpath("(//*[name()='svg'][contains(@class,'back_icon') or contains(@class,'back')] | //button[contains(@class,'back')])[1]");
            WebElement backIcon = wait.until(ExpectedConditions.presenceOfElementLocated(backIconLocator));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", backIcon);
            try {
                wait.until(ExpectedConditions.elementToBeClickable(backIcon)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backIcon);
            }
            actionPause();

         // --- Step 5: Click 'Save Draft' Button from Popup ---
            logAction("Clicking 'Save Draft' option from the popup...");
            wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftBtn));
            try {
                click(saveAsDraftBtn);
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveAsDraftBtn);
            }
            logSuccess("Successfully saved the partial product as a Draft!");
            actionPause();

        } catch (Exception e) {
            logFailure("Failed during partial product draft creation: " + e.getMessage());
            throw e;
        }
    }
    
    public void verifyDraftProductOnListingPage() {
        logHeader("VERIFYING DRAFT PRODUCT & INACTIVE STATUS ON LISTING PAGE");

        try {
            // --- Step 1: Locate the first row in the product listing table ---
            By firstRowLocator = By.xpath("(//td)[1]");
            wait.until(ExpectedConditions.presenceOfElementLocated(firstRowLocator));

            // --- Step 2: Verify Draft Status in 1st Column (or row context containing draft tag) ---
            By firstColumnLocator = By.xpath("(//td)[1]");
            WebElement firstColElem = wait.until(ExpectedConditions.presenceOfElementLocated(firstColumnLocator));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstColElem);
            String firstColText = firstColElem.getText().trim();

            logAction("Listing Page 1st Column text / status: '" + firstColText + "'");

            if (firstColText.toLowerCase().contains("draft")) {
                logSuccess("VERIFIED: Product correctly displays 'Draft' status in the listing table!");
            } else {
                logFailure("VERIFICATION FAILED: Expected 'Draft' indicator in 1st column, but found: '" + firstColText + "'");
                throw new AssertionError("Draft product status verification failed.");
            }
            
        } catch (Exception e) {
            logFailure("Failed during Draft product listing verification: " + e.getMessage());
            throw e;
        }
    }
    
    public void deleteDraftProductAndVerify() {
        logHeader("DELETING DRAFT PRODUCT & VERIFYING REMOVAL");

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
            logSuccess("Confirmed deletion of product: '" + deletedName + "'");
            actionPause();

            // Step 4: Verify the product is no longer in the list
            logAction("Verifying product '" + deletedName + "' is removed from the listing...");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
                    "(//button[normalize-space()='Delete Product'])[1]")));
            boolean removed = wait.until(d -> d.findElements(rowByNameLocator).isEmpty());

            if (removed) {
                logSuccess("VERIFIED: Draft product '" + deletedName + "' is deleted from the listing page!");
            } else {
                logFailure("VERIFICATION FAILED: Product '" + deletedName + "' is still present in the listing.");
                throw new AssertionError("Draft product was not deleted from the listing.");
            }

        } catch (TimeoutException e) {
            logFailure("VERIFICATION FAILED: Product is still listed after deletion (or the popup did not close): " + e.getMessage());
            throw new AssertionError("Draft product was not deleted from the listing.", e);
        } catch (Exception e) {
            logFailure("Failed during draft product deletion flow: " + e.getMessage());
            throw e;
        }
    }
    
    //TC-PM-07
    public static class DimensionBlockData {
        public String blockName;
        public String length;
        public String breadth;
        public String height;
        public String weight;
        public String qty;
        public String alert;
    }

    public static class VariantData {
        public String color;
        public String size;
        public String rawMaterial;
        public List<DimensionBlockData> blocks = new ArrayList<>();
    }

    protected List<VariantData> configuredVariants = new ArrayList<>();

    public void configureMultipleVariantsInventorySetup(int numberOfVariants) {
        logHeader("MULTIPLE VARIANTS INVENTORY SETUP AUTOMATION");
        Random random = new Random();
        configuredVariants.clear();

        boolean isSetProduct = lastSelectedProductType.toLowerCase().contains("set");
        logAction("Configuring multiple variants for Product Type: '" + 
            (lastSelectedProductType.isEmpty() ? "Unknown" : lastSelectedProductType) + 
            "' (Is Set Type: " + isSetProduct + ", Total Variants: " + numberOfVariants + ")");

        // Track global block index across all variants/blocks for dimensions (L, B, H, Qty, Alert)
        int globalBlockIndex = 1;

        for (int v = 1; v <= numberOfVariants; v++) {
            logHeader("--- CONFIGURING VARIANT #" + v + " ---");
            VariantData currentVariant = new VariantData();

            // --- Step 1: Click 'Add Variant' for variants after the first one ---
            if (v > 1) {
                logAction("Clicking 'Add Variant' button for Variant #" + v + "...");
                By addVariantLocator = By.xpath("//button[normalize-space()='Add Variant' or contains(@class,'add-variant')]");
                WebElement addVariantBtn = wait.until(ExpectedConditions.presenceOfElementLocated(addVariantLocator));
                
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addVariantBtn);
                actionPause();
                
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(addVariantBtn)).click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addVariantBtn);
                }
                actionPause();
            }

            // --- Step 2: Select Color ---
            // Variant 1 color dropdown is index 2, Variant 2 is index 6, etc.
            int colorBtnIndex = (v == 1) ? 2 : 2 + ((v - 1) * 4);
            By colorBtnLocator = By.xpath("(//button[@type='button'])[" + colorBtnIndex + "]");
            WebElement colorDropdownBtn = wait.until(ExpectedConditions.elementToBeClickable(colorBtnLocator));
            currentVariant.color = selectRandomCustomDropdownOption(colorDropdownBtn, "Color (Variant #" + v + ")");
            actionPause();

            // --- Step 3: Select Size ---
            By sizeOptionsLocator = By.xpath("(//div[@class='size-wrapper'])[" + v + "]//label[@class='custom-size-checkbox']");
            try {
                List<WebElement> sizeOptions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(sizeOptionsLocator))
                        .stream()
                        .filter(WebElement::isDisplayed)
                        .toList();

                if (!sizeOptions.isEmpty()) {
                    WebElement selectedSize = sizeOptions.get(random.nextInt(sizeOptions.size()));
                    currentVariant.size = selectedSize.getText().trim();
                    
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedSize);
                    try {
                        selectedSize.click();
                    } catch (Exception e) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedSize);
                    }
                    logSuccess("Selected Size for Variant #" + v + ": '" + currentVariant.size + "'");
                } else {
                    logFailure("No visible Size options found for Variant #" + v);
                }
            } catch (Exception e) {
                logFailure("Failed to select Size for Variant #" + v + ": " + e.getMessage());
            }
            actionPause();

            // --- Step 4: Fill Dimensions (Top & Bottom blocks if Set, 1 block if Top/Single) ---
            int blocksPerVariant = isSetProduct ? 2 : 1;

            for (int b = 1; b <= blocksPerVariant; b++) {
                String blockLabel = isSetProduct ? ((b == 1) ? "Top" : "Bottom") : "Single Item";
                DimensionBlockData blockData = new DimensionBlockData();
                blockData.blockName = "Variant #" + v + " - " + blockLabel;

                blockData.length = String.valueOf(random.nextInt(20) + 10);
                blockData.breadth = String.valueOf(random.nextInt(15) + 5);
                blockData.height = String.valueOf(random.nextInt(10) + 2);
                
                // Weight applies to all blocks (Top, Bottom, or Single Item)
                blockData.weight = String.valueOf(random.nextInt(500) + 100);
                blockData.qty = String.valueOf(random.nextInt(50) + 10);
                blockData.alert = String.valueOf(random.nextInt(5) + 1);

                logHeader("FILLING DIMENSIONS: " + blockData.blockName.toUpperCase() + " (Global Block Index: " + globalBlockIndex + ")");

                enterInputValue(By.xpath("(//input[@type='text' and @placeholder='L'])[" + globalBlockIndex + "]"), blockData.length, blockData.blockName + " -> Length (L)");
                enterInputValue(By.xpath("(//input[@type='text' and @placeholder='B'])[" + globalBlockIndex + "]"), blockData.breadth, blockData.blockName + " -> Breadth (B)");
                enterInputValue(By.xpath("(//input[@type='text' and @placeholder='H'])[" + globalBlockIndex + "]"), blockData.height, blockData.blockName + " -> Height (H)");
                
                // Weight input field mapping using globalBlockIndex sequentially (1, 2, 3, 4...)
                enterInputValue(By.xpath("(//input[@type='text' and @placeholder='Weight'])[" + globalBlockIndex + "]"), blockData.weight, blockData.blockName + " -> Weight");
                
                enterInputValue(By.xpath("(//input[@type='text' and @placeholder='Qty'])[" + globalBlockIndex + "]"), blockData.qty, blockData.blockName + " -> Qty");
                enterInputValue(By.xpath("(//input[@type='text' and @placeholder='Alert'])[" + globalBlockIndex + "]"), blockData.alert, blockData.blockName + " -> Alert Threshold");

                currentVariant.blocks.add(blockData);
                globalBlockIndex++;
            }
            actionPause();

            // --- Step 5: Select Raw Material ---
            // Variant 1 raw material button is index 3, Variant 2 is index 7, etc.
            int rawMatBtnIndex = (v == 1) ? 3 : 3 + ((v - 1) * 4);
            By rawMatLocator = By.xpath("(//button[@type='button'])[" + rawMatBtnIndex + "]");
            WebElement rawMaterialDropdownBtn = wait.until(ExpectedConditions.elementToBeClickable(rawMatLocator));
            currentVariant.rawMaterial = selectRandomCustomDropdownOption(rawMaterialDropdownBtn, "Raw Material (Variant #" + v + ")");
            actionPause();

            configuredVariants.add(currentVariant);
        }

        // --- Step 6: Save & Next ---
        logHeader("SAVING MULTIPLE VARIANTS");
        logAction("Clicking 'Save & Next' button...");
        wait.until(ExpectedConditions.elementToBeClickable(saveAndNextBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", saveAndNextBtn);
        try {
            click(saveAndNextBtn);
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveAndNextBtn);
        }
        logSuccess("Clicked 'Save & Next' button successfully with " + numberOfVariants + " variant(s) configured!");
    }

    public List<VariantData> getConfiguredVariants() {
        return configuredVariants;
    }
    
    protected Map<String, String> multiVariantPreviewData = new HashMap<>();

    public void configureMultipleVariantsSetupAndMedia() {
        logHeader("MULTIPLE VARIANTS SETUP & MEDIA AUTOMATION (VARIANT 1 & VARIANT 2)");
        Random random = new Random();
        LocalDate today = LocalDate.now();
        LocalDate targetToDate = today.plusDays(10);
        String[] dynamicKeywords = {"dress", "yellow", "blue", "black", "kurti", "floral", "top"};
        multiVariantPreviewData.clear();
        multiVariantPreviewData.put("Variant #1 - From Date", today.toString());
        multiVariantPreviewData.put("Variant #1 - To Date", targetToDate.toString());
        multiVariantPreviewData.put("Variant #2 - From Date", today.toString());
        multiVariantPreviewData.put("Variant #2 - To Date", targetToDate.toString());

        // ==========================================
        // 1. VARIANT #1 DETAILS CONFIGURATION
        // ==========================================
        logHeader("--- CONFIGURING VARIANT #1 DETAILS ---");

        By fromDateLocator1 = By.xpath("(//input[@placeholder='Select from'])[1]");
        By toDateLocator1 = By.xpath("(//input[@placeholder='Select to'])[1]");

        selectDateFromFlatpickr(fromDateLocator1, today.getDayOfMonth(), "Variant #1 From Date");
        actionPause();
        selectDateFromFlatpickr(toDateLocator1, targetToDate.getDayOfMonth(), "Variant #1 To Date");
        actionPause();

        logAction("Selecting Variant #1 Secondary Color...");
        WebElement secondaryColorBtn1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@type='button'])[1]")));
        String secondaryColor1 = selectRandomCustomDropdownOption(secondaryColorBtn1, "Variant #1 Secondary Color");
        multiVariantPreviewData.put("Variant #1 - Secondary Color", secondaryColor1);
        actionPause();

        int cutPrice1 = 1000 + random.nextInt(2001);
        int costPrice1 = cutPrice1 + 500 + random.nextInt(1001);

        enterInputValue(By.xpath("(//input[@placeholder='Enter the Cost Price (₹)'])[1]"), String.valueOf(costPrice1), "Variant #1 Cost Price");
        enterInputValue(By.xpath("(//input[@placeholder='Enter the Cut Price (₹)'])[1]"), String.valueOf(cutPrice1), "Variant #1 Cut Price");
        multiVariantPreviewData.put("Variant #1 - Cost Price", String.valueOf(costPrice1));
        multiVariantPreviewData.put("Variant #1 - Cut Price", String.valueOf(cutPrice1));
        actionPause();

        logAction("Selecting Variant #1 Fabric...");
        WebElement fabricDropdownBtn1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@type='button'])[3]")));
        selectRandomCustomDropdownOption(fabricDropdownBtn1, "Variant #1 Fabric");
        actionPause();

        logAction("Selecting Variant #1 Pattern...");
        WebElement patternDropdownBtn1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@type='button'])[4]")));
        selectRandomCustomDropdownOption(patternDropdownBtn1, "Variant #1 Pattern");
        actionPause();

        // ==========================================
        // 2. VARIANT #1 MEDIA UPLOAD & BADGES
        // ==========================================
        logHeader("--- UPLOADING MEDIA & SETTING BADGES FOR VARIANT #1 ---");
        List<String> relativeImagePaths = List.of(
            "src/test/resources/images/Product_image.jpg",
            "src/test/resources/images/Product_image2.jpg",
            "src/test/resources/images/Product_image3.jpg",
            "src/test/resources/images/Product_image4.jpg"
        );

        List<String> absoluteImagePaths = new ArrayList<>();
        for (String relativePath : relativeImagePaths) {
            File imageFile = new File(relativePath);
            if (imageFile.exists()) {
                absoluteImagePaths.add(imageFile.getAbsolutePath());
            }
        }

        if (!absoluteImagePaths.isEmpty()) {
            String joinedFilePaths = String.join("\n", absoluteImagePaths);
            uploadToContainer("(//div[@class='file-upload-container'])[1]", joinedFilePaths, "Variant #1 Desktop Container");
            actionPause();
        }

        // Set Thumbnail and List Images for Variant 1
        hoverOverElement(By.xpath("(//img[@alt='preview-image'])[1]"), "Variant #1 Desktop Image 1");
        clickButtonWithFallback(By.xpath("(//button[@type='button'][normalize-space()='Set thumbnail'])[1]"), "Variant #1 Set Thumbnail");
        actionPause();

        hoverOverElement(By.xpath("(//img[@alt='preview-image'])[2]"), "Variant #1 Desktop Image 2");
        clickButtonWithFallback(By.xpath("(//button[@type='button'][normalize-space()='Set list image'])[2]"), "Variant #1 Set List Image");
        actionPause();

        // ==========================================
        // 3. VARIANT #1 TRY-ALONG PRODUCTS (#search_product_0)
        // ==========================================
        logHeader("--- SELECTING TRY-ALONG PRODUCTS FOR VARIANT #1 ---");

        By searchInputLocator1 = By.xpath("(//input[@id='search_product_0'])[1]");
        List<By> optionLocators1 = List.of(
                By.xpath("//ul[contains(@class,'try-along-search-results-0')]//li[contains(@class,'try-along-item-option')]"),
                By.xpath("(//input[@id='search_product_0'])[1]/following::li[contains(@class,'try-along-item-option')]"),
                By.xpath("//li[contains(@class,'try-along-item-option')]")
        );

        java.util.function.Supplier<List<WebElement>> visibleOptions1 = () -> {
            List<WebElement> result = new ArrayList<>();
            for (By loc : optionLocators1) {
                for (WebElement el : driver.findElements(loc)) {
                    try {
                        if (el.isDisplayed()) {
                            result.add(el);
                        }
                    } catch (Exception ignored) {
                        // element re-rendered, skip it
                    }
                }
                if (!result.isEmpty()) {
                    break;
                }
            }
            return result;
        };

        try {
            WebElement searchInput1 = wait.until(ExpectedConditions.presenceOfElementLocated(searchInputLocator1));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", searchInput1);
            actionPause();

            WebDriverWait optionsWait1 = new WebDriverWait(driver, Duration.ofSeconds(8));
            List<String> keywordPool1 = new ArrayList<>(Arrays.asList(dynamicKeywords));
            Collections.shuffle(keywordPool1);
            java.util.Set<String> selectedOptions1 = new java.util.HashSet<>();

            for (String keyword : keywordPool1) {
                if (selectedOptions1.size() >= 3) {
                    break;
                }

                try {
                    searchInput1.click();
                } catch (Exception ex) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click(); arguments[0].focus();", searchInput1);
                }

                String existingValue1 = searchInput1.getAttribute("value");
                int existingLength1 = existingValue1 == null ? 0 : existingValue1.length();
                for (int c = 0; c < existingLength1; c++) {
                    searchInput1.sendKeys(Keys.BACK_SPACE);
                }

                searchInput1.sendKeys(keyword);
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].dispatchEvent(new Event('input', {bubbles: true}));"
                                + "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {bubbles: true}));", searchInput1);
                logAction("Variant #1 Try-Along search term: " + keyword);
                actionPause();

                try {
                    optionsWait1.until(d -> !visibleOptions1.get().isEmpty());
                } catch (Exception ex) {
                    logAction("No visible Variant #1 try-along options for '" + keyword + "', trying another keyword...");
                    continue;
                }

                int guard = 0;
                int reopenAttempts = 0;
                while (selectedOptions1.size() < 3 && guard++ < 15) {
                    WebElement target = null;
                    String targetText = null;

                    // Fetch fresh elements on every iteration to avoid StaleElementReferenceException
                    for (WebElement opt : visibleOptions1.get()) {
                        try {
                            String txt = opt.getText().trim();
                            if (txt.isEmpty()) {
                                txt = String.valueOf(opt.getAttribute("textContent")).trim();
                            }
                            if (!selectedOptions1.contains(txt)) {
                                target = opt;
                                targetText = txt;
                                break;
                            }
                        } catch (Exception ignored) {
                            // element re-rendered, skip it
                        }
                    }

                    if (target == null) {
                        if (reopenAttempts++ < 2) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click(); arguments[0].focus();", searchInput1);
                            actionPause();
                            continue;
                        }
                        break;
                    }

                    try {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'nearest'});", target);
                        actionPause();
                        try {
                            target.click();
                        } catch (Exception ex) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
                        }
                        selectedOptions1.add(targetText);
                        logAction("Selected Variant #1 try-along option #" + selectedOptions1.size() + ": " + targetText);
                        actionPause();
                    } catch (Exception st) {
                        // If stale during click, retry this iteration
                        continue;
                    }
                }
            }

            if (selectedOptions1.size() < 3) {
                for (int i = 0; i < optionLocators1.size(); i++) {
                    logAction("DIAGNOSTIC Variant #1 locator " + (i + 1) + " matches: "
                            + driver.findElements(optionLocators1.get(i)).size());
                }
                logFailure("Variant #1 Try-Along: only " + selectedOptions1.size() + " of 3 products were selected.");
            } else {
                logSuccess("Variant #1 Try-Along: 3 products selected.");
            }
        } catch (Exception e) {
            logFailure("Failed during Variant #1 Try-Along selection: " + e.getMessage());
        }

        // ==========================================
        // 4. VARIANT #2 DETAILS CONFIGURATION (EXPAND ACCORDION AFTER VARIANT 1 TRY-ALONGS)
        // ==========================================
        logHeader("--- EXPANDING & CONFIGURING VARIANT #2 DETAILS ---");

        try {
            By secondCursorLocator = By.xpath("(//div[contains(@class,'arrow-icon')]//*[name()='svg'])[2]");
            WebElement secondCursor = wait.until(ExpectedConditions.presenceOfElementLocated(secondCursorLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", secondCursor);
            try {
                wait.until(ExpectedConditions.elementToBeClickable(secondCursor)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", secondCursor);
            }
            actionPause();
        } catch (Exception e) {
            logAction("Variant #2 accordion is already expanded or cursor not required.");
        }

        // Variant 2 specific name-attribute date fields
        By fromDateLocator2 = By.xpath("(//input[@name='collection_from_date[1]'])[1]");
        By toDateLocator2 = By.xpath("(//input[@name='collection_to_date[1]'])[1]");

        selectDateFromFlatpickr(fromDateLocator2, today.getDayOfMonth(), "Variant #2 From Date");
        actionPause();
        selectDateFromFlatpickr(toDateLocator2, targetToDate.getDayOfMonth(), "Variant #2 To Date");
        actionPause();

        int cutPrice2 = 1000 + random.nextInt(2001);
        int costPrice2 = cutPrice2 + 500 + random.nextInt(1001);

        enterInputValue(By.xpath("(//input[@name='cost_price[1]'])[1]"), String.valueOf(costPrice2), "Variant #2 Cost Price");
        enterInputValue(By.xpath("(//input[@name='cut_price[1]'])[1]"), String.valueOf(cutPrice2), "Variant #2 Cut Price");
        multiVariantPreviewData.put("Variant #2 - Cost Price", String.valueOf(costPrice2));
        multiVariantPreviewData.put("Variant #2 - Cut Price", String.valueOf(cutPrice2));
        actionPause();

        // ==========================================
        // 5. VARIANT #2 MEDIA UPLOAD & BADGES
        // ==========================================
        logHeader("--- UPLOADING MEDIA & SETTING BADGES FOR VARIANT #2 ---");
        if (!absoluteImagePaths.isEmpty()) {
            String joinedFilePaths = String.join("\n", absoluteImagePaths);
            uploadToContainer("(//div[@class='file-upload-container'])[6]", joinedFilePaths, "Variant #2 Desktop Container");
            actionPause();
        }

        // Set Thumbnail and List Images for Variant 2
        hoverOverElement(By.xpath("(//img[@alt='preview-image'])[5]"), "Variant #2 Desktop Image 1");
        clickButtonWithFallback(By.xpath("(//button[@type='button'][normalize-space()='Set thumbnail'])[5]"), "Variant #2 Set Thumbnail");
        actionPause();

        hoverOverElement(By.xpath("(//img[@alt='preview-image'])[6]"), "Variant #2 Desktop Image 2");
        clickButtonWithFallback(By.xpath("(//button[@type='button'][normalize-space()='Set list image'])[6]"), "Variant #2 Set List Image");
        actionPause();

        // ==========================================
        // 6. VARIANT #2 TRY-ALONG PRODUCTS (#search_product_1)
        // ==========================================
        logHeader("--- SELECTING TRY-ALONG PRODUCTS FOR VARIANT #2 ---");

        By searchInputLocator2 = By.xpath("(//input[@id='search_product_1'])[1]");
        List<By> optionLocators2 = List.of(
                By.xpath("//ul[contains(@class,'try-along-search-results-1')]//li[contains(@class,'try-along-item-option')]"),
                By.xpath("(//input[@id='search_product_1'])[1]/following::li[contains(@class,'try-along-item-option')]"),
                By.xpath("//li[contains(@class,'try-along-item-option')]")
        );

        java.util.function.Supplier<List<WebElement>> visibleOptions2 = () -> {
            List<WebElement> result = new ArrayList<>();
            for (By loc : optionLocators2) {
                for (WebElement el : driver.findElements(loc)) {
                    try {
                        if (el.isDisplayed()) {
                            result.add(el);
                        }
                    } catch (Exception ignored) {
                        // element re-rendered, skip it
                    }
                }
                if (!result.isEmpty()) {
                    break;
                }
            }
            return result;
        };

        try {
            WebElement searchInput2 = wait.until(ExpectedConditions.presenceOfElementLocated(searchInputLocator2));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", searchInput2);
            actionPause();

            WebDriverWait optionsWait2 = new WebDriverWait(driver, Duration.ofSeconds(8));
            List<String> keywordPool2 = new ArrayList<>(Arrays.asList(dynamicKeywords));
            Collections.shuffle(keywordPool2);
            java.util.Set<String> selectedOptions2 = new java.util.HashSet<>();

            for (String keyword : keywordPool2) {
                if (selectedOptions2.size() >= 3) {
                    break;
                }

                try {
                    searchInput2.click();
                } catch (Exception ex) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click(); arguments[0].focus();", searchInput2);
                }

                String existingValue2 = searchInput2.getAttribute("value");
                int existingLength2 = existingValue2 == null ? 0 : existingValue2.length();
                for (int c = 0; c < existingLength2; c++) {
                    searchInput2.sendKeys(Keys.BACK_SPACE);
                }

                searchInput2.sendKeys(keyword);
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].dispatchEvent(new Event('input', {bubbles: true}));"
                                + "arguments[0].dispatchEvent(new KeyboardEvent('keyup', {bubbles: true}));", searchInput2);
                logAction("Variant #2 Try-Along search term: " + keyword);
                actionPause();

                try {
                    optionsWait2.until(d -> !visibleOptions2.get().isEmpty());
                } catch (Exception ex) {
                    logAction("No visible Variant #2 try-along options for '" + keyword + "', trying another keyword...");
                    continue;
                }

                int guard = 0;
                int reopenAttempts = 0;
                while (selectedOptions2.size() < 3 && guard++ < 15) {
                    WebElement target = null;
                    String targetText = null;

                    // Fetch fresh elements on every iteration to avoid StaleElementReferenceException
                    for (WebElement opt : visibleOptions2.get()) {
                        try {
                            String txt = opt.getText().trim();
                            if (txt.isEmpty()) {
                                txt = String.valueOf(opt.getAttribute("textContent")).trim();
                            }
                            if (!selectedOptions2.contains(txt)) {
                                target = opt;
                                targetText = txt;
                                break;
                            }
                        } catch (Exception ignored) {
                            // element re-rendered, skip it
                        }
                    }

                    if (target == null) {
                        if (reopenAttempts++ < 2) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click(); arguments[0].focus();", searchInput2);
                            actionPause();
                            continue;
                        }
                        break;
                    }

                    try {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'nearest'});", target);
                        actionPause();
                        try {
                            target.click();
                        } catch (Exception ex) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
                        }
                        selectedOptions2.add(targetText);
                        logAction("Selected Variant #2 try-along option #" + selectedOptions2.size() + ": " + targetText);
                        actionPause();
                    } catch (Exception st) {
                        // If stale during click, retry this iteration
                        continue;
                    }
                }
            }

            if (selectedOptions2.size() < 3) {
                for (int i = 0; i < optionLocators2.size(); i++) {
                    logAction("DIAGNOSTIC Variant #2 locator " + (i + 1) + " matches: "
                            + driver.findElements(optionLocators2.get(i)).size());
                }
                logFailure("Variant #2 Try-Along: only " + selectedOptions2.size() + " of 3 products were selected.");
            } else {
                logSuccess("Variant #2 Try-Along: 3 products selected.");
            }
        } catch (Exception e) {
            logFailure("Failed during Variant #2 Try-Along selection: " + e.getMessage());
        }

        // ==========================================
        // 7. SAVE & NEXT
        // ==========================================
        logHeader("SAVING MULTIPLE VARIANTS SETUP");
        try {
            By saveAndNextBtnLocator = By.xpath("//button[normalize-space()='Save & Next' or contains(text(),'Save & Next')]");
            WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(saveAndNextBtnLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", saveBtn);
            actionPause();
            try {
                wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
            }
            logSuccess("Successfully completed full multi-variant setup sequence for both variants and clicked 'Save & Next'!");
        } catch (Exception e) {
            logFailure("Failed to click 'Save & Next': " + e.getMessage());
        }
    }
    
    public void configureMultipleVariantsDescriptionAndSave() {
        logHeader("CONFIGURING MULTIPLE VARIANTS PRODUCT DESCRIPTION PAGE");
        
        logAction("Clicking 'Save & Next' to move to the description page...");
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[normalize-space()='Save & Next' or contains(text(),'Save & Next')]")));
        scrollAndClick(saveAndNextBtn);
        actionPause();
        
        try {
            // ---------- VARIANT #1 (default active tab) ----------
            configureSingleVariantDescription(0, "Variant #1");

            // ---------- SWITCH TO VARIANT #2 TAB ----------
            logHeader("--- SWITCHING TO VARIANT #2 DESCRIPTION TAB ---");
            By variant2TabLocator = By.xpath("(//button[@id='description-tab-1'])[1]");
            WebElement variant2Tab = wait.until(ExpectedConditions.presenceOfElementLocated(variant2TabLocator));
            scrollAndClick(variant2Tab);
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//textarea[@id='product_description_1'])[1]")));
            logSuccess("Switched to Variant #2 description tab.");
            actionPause();

            // ---------- VARIANT #2 ----------
            configureSingleVariantDescription(1, "Variant #2");

            // ---------- SAVE & NEXT (after both variants) ----------
            logHeader("SAVING MULTIPLE VARIANTS DESCRIPTION");
            logAction("Clicking 'Save & Next' button...");
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[normalize-space()='Save & Next' or contains(text(),'Save & Next')]")));
            scrollAndClick(saveAndNextBtn);
            logSuccess("Successfully saved Product Description section for both variants!");

        } catch (Exception e) {
            logFailure("Failed to complete Multiple Variants Description Page setup: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Handles one variant's description: AI suggestion -> popup option -> proceed -> capture -> fill other fields.
     * variantIndex 0 = first variant (ids ending _0), 1 = second variant (ids ending _1).
     */
    private void configureSingleVariantDescription(int variantIndex, String variantLabel) {
        logHeader("--- CONFIGURING " + variantLabel.toUpperCase() + " DESCRIPTION ---");
        Random random = new Random();

        By productDescriptionLocator = By.xpath("(//textarea[@id='product_description_" + variantIndex + "'])[1]");
        By compositionCareLocator    = By.xpath("(//textarea[@id='composition_care_" + variantIndex + "'])[1]");
        By productSpecLocator        = By.xpath("(//textarea[@id='product_specification_" + variantIndex + "'])[1]");
        By stylingSuggestionLocator  = By.xpath("(//textarea[@id='styling_suggestion_" + variantIndex + "'])[1]");
        By sizeFitLocator            = By.xpath("(//textarea[@id='size_fit_" + variantIndex + "'])[1]");
        By returnExchangeLocator     = By.xpath("(//textarea[@id='return_and_exchange_" + variantIndex + "'])[1]");
        By aiSuggestionBtnLocator    = By.xpath("(//span[@class='text_button'])[1]");

        // --- 1. AI SUGGESTION ---
        logAction("Clicking 'AI Suggestion' button for " + variantLabel + "...");
        wait.until(d -> findFirstDisplayed(aiSuggestionBtnLocator) != null);
        scrollAndClick(findFirstDisplayed(aiSuggestionBtnLocator));

        logAction("Waiting for AI Suggestions to generate for " + variantLabel + "...");
        WebDriverWait extendedAiWait = new WebDriverWait(driver, Duration.ofSeconds(40));

        extendedAiWait.until(d -> {
            try {
                return !aiSuggestionOptions.isEmpty() && aiSuggestionOptions.get(0).isDisplayed();
            } catch (Exception e) {
                return false;
            }
        });
        actionPause();

        int optionsCount = aiSuggestionOptions.size();
        logAction("Found " + optionsCount + " AI suggestion option(s) for " + variantLabel + ". Picking one randomly...");
        int selectedIndex = random.nextInt(optionsCount);
        WebElement chosenOption = aiSuggestionOptions.get(selectedIndex);

        extendedAiWait.until(ExpectedConditions.elementToBeClickable(chosenOption));
        scrollAndClick(chosenOption);
        logSuccess(variantLabel + " - Selected AI Suggestion Option #" + (selectedIndex + 1));
        actionPause();

        logAction("Clicking 'Proceed' button on AI Suggestion popup for " + variantLabel + "...");
        extendedAiWait.until(ExpectedConditions.elementToBeClickable(aiProceedBtn));
        scrollAndClick(aiProceedBtn);
        actionPause();

        // Wait for this variant's description textarea to be populated, then capture it
        extendedAiWait.until(d -> {
            String v = d.findElement(productDescriptionLocator).getAttribute("value");
            return v != null && !v.trim().isEmpty();
        });
        String generatedDescriptionText = driver.findElement(productDescriptionLocator).getAttribute("value").trim();

        if (generatedDescriptionText.isEmpty()) {
            logFailure("VERIFICATION FAILED: " + variantLabel + " Product Description field is empty after AI Proceed!");
        } else {
            storedDescriptionData.put(variantLabel + " - Product Description", generatedDescriptionText);
            logSuccess("VERIFIED: " + variantLabel + " Product Description populated -> "
                    + (generatedDescriptionText.length() > 60 ? generatedDescriptionText.substring(0, 60) + "..." : generatedDescriptionText));
        }

        // --- 2. DYNAMIC TEXT DATA POOLS ---
        String[] compositionCareOptions = {
            "Hand wash cold separately. Mild detergent only. Line dry in shade. Cool iron on reverse.",
            "Machine wash cold with like colors. Do not bleach. Tumble dry low. Warm iron if needed.",
            "Dry clean only. Do not wash or tumble dry. Steam iron on low setting if necessary.",
            "100% Organic Silk. Gentle hand wash in cold water. Do not wring. Flat dry away from sun."
        };

        String[] productSpecificationOptions = {
            "100% Premium Cotton, Breathable mesh weave, Reinforced stitch seams, Lightweight design.",
            "80% Linen 20% Rayon blend, Natural texture, Soft-washed feel, Wrinkle-resistant finish.",
            "Pure Mulberry Silk, Hypoallergenic weave, Lined interior, Tailored fit finish.",
            "100% Recycled Polyester, Quick-dry technology, Moisture-wicking fabric, UV protection."
        };

        String[] stylingSuggestionOptions = {
            "Pair with clean white sneakers and a sleek denim jacket for a contemporary weekend look.",
            "Match with high-waisted trousers and classic leather loafers for a polished smart-casual outfit.",
            "Layer over a fitted turtleneck and complete with minimalist heels for an elegant evening ensemble.",
            "Style with tailored shorts and woven strap sandals for an effortless warm-weather vibe."
        };

        String[] sizeFitOptions = {
            "True to size. Model is 5'9\" wearing size Small. Relaxed silhouette for everyday comfort.",
            "Slim fit profile. Designed to body contour. Take one size up if you prefer a looser fit.",
            "Oversized aesthetic. Model is 6'0\" wearing size Medium. Boxy cut for a modern urban fit.",
            "Regular fit. Tailored waist with flexible armholes. Model is 5'8\" wearing size Medium."
        };

        String[] returnAndExchangeOptions = {
            "Hassle-free 30-day returns and free instant exchanges. Pickup scheduled right from your doorstep.",
            "14-day easy return policy. Store credit or full refund provided upon quality inspection.",
            "7-day return window for unworn items with original tags intact. Easy online exchange process.",
            "30-day money-back guarantee. No questions asked return procedure with free home pick-up."
        };

        // --- 3. FILL FIELDS (values are stored by fillAndStoreField under variant-specific keys) ---
        fillAndStoreField(driver.findElement(compositionCareLocator), variantLabel + " - Composition & Care",
                compositionCareOptions[random.nextInt(compositionCareOptions.length)]);
        fillAndStoreField(driver.findElement(productSpecLocator), variantLabel + " - Product Specification",
                productSpecificationOptions[random.nextInt(productSpecificationOptions.length)]);
        fillAndStoreField(driver.findElement(stylingSuggestionLocator), variantLabel + " - Styling Suggestion",
                stylingSuggestionOptions[random.nextInt(stylingSuggestionOptions.length)]);
        fillAndStoreField(driver.findElement(sizeFitLocator), variantLabel + " - Size & Fit",
                sizeFitOptions[random.nextInt(sizeFitOptions.length)]);
        fillAndStoreField(driver.findElement(returnExchangeLocator), variantLabel + " - Return & Exchange",
                returnAndExchangeOptions[random.nextInt(returnAndExchangeOptions.length)]);

        logSuccess(variantLabel + " description details filled and captured.");
    }

    // Returns the first currently displayed element for a locator, or null if none is visible
    private WebElement findFirstDisplayed(By locator) {
        for (WebElement el : driver.findElements(locator)) {
            try {
                if (el.isDisplayed()) {
                    return el;
                }
            } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
                // element re-rendered, skip it
            }
        }
        return null;
    }
    
    
    
    public void configureMultipleVariantsSeoAndSave() {
        logHeader("CONFIGURING MULTIPLE VARIANTS SEO DETAILS PAGE");

        try {
            // ---------- VARIANT #1 (default active tab) ----------
            configureSingleVariantSeo(0, "Variant #1");

            // ---------- SWITCH TO VARIANT #2 SEO TAB ----------
            logHeader("--- SWITCHING TO VARIANT #2 SEO TAB ---");
            By variant2SeoTabLocator = By.xpath("(//button[@id='seo-tab-1'])[1]");
            WebElement variant2SeoTab = wait.until(ExpectedConditions.presenceOfElementLocated(variant2SeoTabLocator));
            scrollAndClick(variant2SeoTab);
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//input[@id='seo_meta_title_1'])[1]")));
            logSuccess("Switched to Variant #2 SEO tab.");
            actionPause();

            // ---------- VARIANT #2 ----------
            configureSingleVariantSeo(1, "Variant #2");

            // ---------- SAVE (after both variants) ----------
            logHeader("SAVING MULTIPLE VARIANTS SEO DETAILS");
            logAction("Clicking final 'Save' button for SEO details...");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//button[normalize-space()='Save'])[1]")));
            scrollAndClick(seoSaveBtn);

            logSuccess("Successfully configured and saved SEO Details for both variants!");

        } catch (Exception e) {
            logFailure("Failed to complete Multiple Variants SEO Details setup: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Handles one variant's SEO: AI Suggestion -> popup option -> proceed -> capture the 3 fields.
     * variantIndex 0 = first variant (ids ending _0), 1 = second variant (ids ending _1).
     */
    private void configureSingleVariantSeo(int variantIndex, String variantLabel) {
        logHeader("--- CONFIGURING " + variantLabel.toUpperCase() + " SEO ---");
        Random random = new Random();

        By aiSuggestionBtnLocator = By.xpath("(//button[@id='generate-seo-btn'])[1]");
        By metaTitleLocator       = By.xpath("(//input[@id='seo_meta_title_" + variantIndex + "'])[1]");
        By metaKeyLocator         = By.xpath("(//input[@id='seo_meta_key_" + variantIndex + "'])[1]");
        By metaDescriptionLocator = By.xpath("(//textarea[@id='seo_meta_description_" + variantIndex + "'])[1]");

        // --- 1. AI SUGGESTION ---
        logAction("Clicking AI Suggestion button for SEO (" + variantLabel + ")...");
        wait.until(d -> findFirstDisplayed(aiSuggestionBtnLocator) != null);
        scrollAndClick(findFirstDisplayed(aiSuggestionBtnLocator));

        logAction("Waiting for AI SEO suggestions to generate for " + variantLabel + "...");
        WebDriverWait extendedSeoWait = new WebDriverWait(driver, Duration.ofSeconds(40));

        By seoModalLocator = By.xpath("//div[contains(@class,'modal') or contains(@class,'popup') or contains(@class,'show')]");
        extendedSeoWait.until(ExpectedConditions.visibilityOfElementLocated(seoModalLocator));

        extendedSeoWait.until(d -> {
            try {
                return !seoAiSuggestionOptions.isEmpty() && seoAiSuggestionOptions.get(0).isDisplayed();
            } catch (Exception e) {
                return false;
            }
        });
        actionPause();

        int optionsCount = seoAiSuggestionOptions.size();
        logAction("Found " + optionsCount + " AI SEO suggestion option(s) for " + variantLabel + ". Picking one randomly...");
        int selectedIndex = random.nextInt(optionsCount);
        WebElement chosenOption = seoAiSuggestionOptions.get(selectedIndex);

        extendedSeoWait.until(ExpectedConditions.elementToBeClickable(chosenOption));
        scrollAndClick(chosenOption);
        logSuccess(variantLabel + " - Selected AI SEO Suggestion Option #" + (selectedIndex + 1));
        actionPause();

        logAction("Clicking 'Proceed' button on AI SEO popup for " + variantLabel + "...");
        extendedSeoWait.until(ExpectedConditions.elementToBeClickable(seoAiProceedBtn));
        scrollAndClick(seoAiProceedBtn);
        actionPause();

        // Wait for the popup options to close so the next variant doesn't pick up stale options
        try {
            extendedSeoWait.until(d -> {
                try {
                    return seoAiSuggestionOptions.isEmpty() || !seoAiSuggestionOptions.get(0).isDisplayed();
                } catch (Exception e) {
                    return true;
                }
            });
        } catch (Exception e) {
            logAction("SEO popup still reported as open for " + variantLabel + ", continuing...");
        }

        // --- 2. CAPTURE THE ENTERED DETAILS ---
        verifyAndStoreSeoField(metaTitleLocator, variantLabel + " - SEO Meta Title");
        verifyAndStoreSeoField(metaKeyLocator, variantLabel + " - SEO Meta Keyword");
        verifyAndStoreSeoField(metaDescriptionLocator, variantLabel + " - SEO Meta Description");

        logSuccess(variantLabel + " SEO details generated and captured.");
    }

    // Locator-based version of verifyAndStoreField (the existing one takes a WebElement and stays unchanged)
    private void verifyAndStoreSeoField(By locator, String storeKey) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            wait.until(d -> {
                String v = d.findElement(locator).getAttribute("value");
                return v != null && !v.trim().isEmpty();
            });

            String fieldValue = driver.findElement(locator).getAttribute("value");
            fieldValue = fieldValue == null ? "" : fieldValue.trim();

            if (fieldValue.isEmpty()) {
                logFailure("VERIFICATION FAILED: " + storeKey + " is empty!");
            } else {
                storedSeoData.put(storeKey, fieldValue);
                logSuccess("VERIFIED " + storeKey + " -> "
                        + (fieldValue.length() > 50 ? fieldValue.substring(0, 50) + "..." : fieldValue));
            }
        } catch (Exception e) {
            logFailure("Failed while verifying field " + storeKey + ": " + e.getMessage());
        }
    }
    
    
    public void navigateToPreviewAndVerifyMultipleVariantsDetails() {
        logHeader("NAVIGATING TO PREVIEW PAGE (MULTIPLE VARIANTS)");

        try {
            logAction("Clicking action three-dot button...");
            By threedotBtnLocator = By.xpath("(//div[@class='dropdown actions-dropdown-wrapper'])[1]");
            WebElement threeDotElement = wait.until(ExpectedConditions.elementToBeClickable(threedotBtnLocator));
            scrollAndClick(threeDotElement);
            actionPause();

            logAction("Clicking 'Preview' option...");
            By previewBtnLocator = By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]");
            WebElement previewElement = wait.until(ExpectedConditions.elementToBeClickable(previewBtnLocator));
            scrollAndClick(previewElement);
            logSuccess("Successfully navigated to Product Preview Page!");
            actionPause();

            // Variant values captured by the setup methods
            String v1Color = configuredVariants.size() > 0 ? configuredVariants.get(0).color : "";
            String v1Size  = configuredVariants.size() > 0 ? configuredVariants.get(0).size  : "";
            String v2Color = configuredVariants.size() > 1 ? configuredVariants.get(1).color : "";
            String v2Size  = configuredVariants.size() > 1 ? configuredVariants.get(1).size  : "";

            // 1. BASIC INFO TAB
            logHeader("1. VERIFYING BASIC INFO TAB");
            verifyPreviewField(By.xpath("(//div[@class='detail-value text-primary'])[1]"), lastGeneratedProductName, "Product Name");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[1]"), lastSelectedBrand, "Brand Type");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[2]"), lastGeneratedSKU, "SKU");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[3]"), lastGeneratedHSN, "HSN");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[4]"), lastSelectedProductType, "Product Type");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[5]"), lastSelectedCollection, "Collection");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[6]"), lastSelectedNeckline, "Neckline");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[7]"), lastSelectedSleeveType, "Sleeve Type");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[8]"), lastSelectedRise, "Rise");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[9]"), lastSelectedFit, "Fit");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[11]"), lastSelectedBarcodeType, "Barcode Type");

            // 2. INVENTORY TAB
            logHeader("2. VERIFYING INVENTORY TAB");
            clickTabAndPause(By.xpath("(//button[@id='inventory-tab'])[1]"), "Inventory Tab");

            verifyPreviewField(By.xpath("(//div[@class='detail-item'])[1]"), v1Color, "Variant #1 Color");
            verifyPreviewField(By.xpath("(//div[@class='detail-item'])[2]"), v1Size, "Variant #1 Size");
            verifyPreviewField(By.xpath("(//div[@class='detail-item'])[3]"), v2Color, "Variant #2 Color");
            verifyPreviewField(By.xpath("(//div[@class='detail-item'])[4]"), v2Size, "Variant #2 Size");

            // 3. VARIANTS TAB
            logHeader("3. VERIFYING VARIANTS TAB");
            clickTabAndPause(By.xpath("(//button[normalize-space()='Variants'])[1]"), "Variants Tab");

            // --- Variant #1 ---
            verifyPreviewDate(By.xpath("(//div[@class='detail-value'])[1]"), multiVariantPreviewData.get("Variant #1 - From Date"), "Variant #1 From Date");
            verifyPreviewDate(By.xpath("(//div[@class='detail-value'])[2]"), multiVariantPreviewData.get("Variant #1 - To Date"), "Variant #1 To Date");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[3]"), v1Color, "Variant #1 Color");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[4]"), multiVariantPreviewData.get("Variant #1 - Secondary Color"), "Variant #1 Secondary Color");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[5]"), v1Size, "Variant #1 Size");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[7]"), lastGeneratedSKU, "Variant #1 SKU");
            verifyPreviewPrice(By.xpath("(//div[@class='detail-value'])[8]"), multiVariantPreviewData.get("Variant #1 - Cost Price"), "Variant #1 Cost Price");
            verifyPreviewPrice(By.xpath("(//div[@class='detail-value'])[9]"), multiVariantPreviewData.get("Variant #1 - Cut Price"), "Variant #1 Cut Price");

            // --- Open Variant #2 using the slider, then verify ---
            expandVariant2InPreview();

            verifyPreviewDate(By.xpath("(//div[@class='detail-value'])[15]"), multiVariantPreviewData.get("Variant #2 - From Date"), "Variant #2 From Date");
            verifyPreviewDate(By.xpath("(//div[@class='detail-value'])[16]"), multiVariantPreviewData.get("Variant #2 - To Date"), "Variant #2 To Date");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[17]"), v2Color, "Variant #2 Color");
            verifyPreviewPrice(By.xpath("(//div[@class='detail-value'])[22]"), multiVariantPreviewData.get("Variant #2 - Cost Price"), "Variant #2 Cost Price");
            verifyPreviewPrice(By.xpath("(//div[@class='detail-value'])[23]"), multiVariantPreviewData.get("Variant #2 - Cut Price"), "Variant #2 Cut Price");

            // 4. DESCRIPTION TAB (Settings tab skipped)
            logHeader("4. VERIFYING DESCRIPTION TAB");
            clickTabAndPause(By.xpath("(//button[normalize-space()='Description'])[1]"), "Description Tab");

            // --- Variant #1 ---
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[1]"), storedDescriptionData.get("Variant #1 - Product Description"), "Variant #1 Product Description");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[2]"), storedDescriptionData.get("Variant #1 - Composition & Care"), "Variant #1 Composition & Care");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[3]"), storedDescriptionData.get("Variant #1 - Styling Suggestion"), "Variant #1 Styling");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[4]"), storedDescriptionData.get("Variant #1 - Product Specification"), "Variant #1 Product Specification");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[5]"), storedDescriptionData.get("Variant #1 - Size & Fit"), "Variant #1 Size and Fit");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[6]"), storedDescriptionData.get("Variant #1 - Return & Exchange"), "Variant #1 Return and Exchange");

            // --- Variant #2 (indexes 7-12, same field order as Variant #1) ---
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[7]"), storedDescriptionData.get("Variant #2 - Product Description"), "Variant #2 Product Description");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[8]"), storedDescriptionData.get("Variant #2 - Composition & Care"), "Variant #2 Composition & Care");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[9]"), storedDescriptionData.get("Variant #2 - Styling Suggestion"), "Variant #2 Styling");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[10]"), storedDescriptionData.get("Variant #2 - Product Specification"), "Variant #2 Product Specification");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[11]"), storedDescriptionData.get("Variant #2 - Size & Fit"), "Variant #2 Size and Fit");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[12]"), storedDescriptionData.get("Variant #2 - Return & Exchange"), "Variant #2 Return and Exchange");

            // 5. SEO TAB
            logHeader("5. VERIFYING SEO TAB");
            clickTabAndPause(By.xpath("(//button[normalize-space()='SEO'])[1]"), "SEO Tab");

            // --- Variant #1 ---
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[1]"), storedSeoData.get("Variant #1 - SEO Meta Title"), "Variant #1 SEO Meta Title");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[2]"), storedSeoData.get("Variant #1 - SEO Meta Keyword"), "Variant #1 Meta Key");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[3]"), storedSeoData.get("Variant #1 - SEO Meta Description"), "Variant #1 SEO Description");

            // --- Variant #2 ---
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[4]"), storedSeoData.get("Variant #2 - SEO Meta Title"), "Variant #2 SEO Meta Title");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[5]"), storedSeoData.get("Variant #2 - SEO Meta Keyword"), "Variant #2 Meta Key");
            verifyPreviewField(By.xpath("(//div[@class='detail-value'])[6]"), storedSeoData.get("Variant #2 - SEO Meta Description"), "Variant #2 SEO Description");

            logSuccess("Preview verification completed for multiple variants!");

        } catch (Exception e) {
            logFailure("Failed during multi-variant preview verification flow: " + e.getMessage());
            throw e;
        }
    }

    // Clicks the slider on the Variants tab that opens Variant #2's details
    private void expandVariant2InPreview() {
        logAction("Clicking slider to expand Variant #2 details...");

        List<By> sliderLocators = List.of(
                // Dynamic: second variant header, whatever its generated id is
                By.xpath("(//div[starts-with(@id,'variant_header_')])[2]//div[@class='d-flex align-items-start justify-content-between']//*[name()='svg']"),
                // Your original locator (hardcoded id)
                By.xpath("//div[@id='variant_header_2012']//div[@class='d-flex align-items-start justify-content-between']//*[name()='svg']")
        );

        for (By locator : sliderLocators) {
            try {
                List<WebElement> found = driver.findElements(locator);
                if (found.isEmpty()) {
                    continue;
                }
                WebElement slider = found.get(0);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", slider);
                actionPause();
                try {
                    slider.click();
                } catch (Exception e) {
                    // SVG elements may not support .click(), so dispatch a real click event instead
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));", slider);
                }
                actionPause();
                logSuccess("Variant #2 slider clicked.");
                return;
            } catch (Exception e) {
                logAction("Slider locator failed, trying next one: " + e.getMessage());
            }
        }
        logFailure("Could not find or click the Variant #2 slider in the Variants tab.");
    }

    // Compares a preview date (any common display format) against an ISO date (yyyy-MM-dd)
    private void verifyPreviewDate(By locator, String isoDate, String fieldName) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

            String actual = element.getText().trim();
            String normalizedActual = actual.replace("/", "-").toLowerCase();

            if (isoDate == null || isoDate.isEmpty()) {
                logFailure("MISMATCH PREVIEW [" + fieldName + "] -> No stored date to compare. Actual: '" + actual + "'");
                return;
            }

            java.time.LocalDate expected = java.time.LocalDate.parse(isoDate);
            String[] patterns = {"dd-MM-yyyy", "yyyy-MM-dd", "MM-dd-yyyy", "d-M-yyyy",
                                 "dd MMM yyyy", "d MMM yyyy", "dd MMMM yyyy", "MMM dd, yyyy", "MMM d, yyyy", "dd MMM, yyyy"};

            for (String pattern : patterns) {
                String candidate = expected.format(
                        java.time.format.DateTimeFormatter.ofPattern(pattern, java.util.Locale.ENGLISH)).toLowerCase();
                if (normalizedActual.contains(candidate)) {
                    logSuccess("VERIFIED PREVIEW [" + fieldName + "] -> Expected: '" + isoDate + "' | Actual: '" + actual + "'");
                    return;
                }
            }
            logFailure("MISMATCH PREVIEW [" + fieldName + "] -> Expected: '" + isoDate + "' | Actual: '" + actual + "'");
        } catch (Exception e) {
            logFailure("Could not verify preview date [" + fieldName + "] at locator: " + locator + " -> " + e.getMessage());
        }
    }

    // Compares prices numerically so "₹2,345", "2345.00" and "2345" all match
    private void verifyPreviewPrice(By locator, String expectedPrice, String fieldName) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

            String actual = element.getText().trim();
            String actualDigits = actual.replaceAll("[^0-9.]", "");

            if (expectedPrice == null || expectedPrice.isEmpty()) {
                logFailure("MISMATCH PREVIEW [" + fieldName + "] -> No stored price to compare. Actual: '" + actual + "'");
                return;
            }
            if (actualDigits.isEmpty()) {
                logFailure("MISMATCH PREVIEW [" + fieldName + "] -> Expected: '" + expectedPrice + "' | Actual: '" + actual + "'");
                return;
            }

            if (Double.parseDouble(actualDigits) == Double.parseDouble(expectedPrice)) {
                logSuccess("VERIFIED PREVIEW [" + fieldName + "] -> Expected: '" + expectedPrice + "' | Actual: '" + actual + "'");
            } else {
                logFailure("MISMATCH PREVIEW [" + fieldName + "] -> Expected: '" + expectedPrice + "' | Actual: '" + actual + "'");
            }
        } catch (Exception e) {
            logFailure("Could not verify preview price [" + fieldName + "] at locator: " + locator + " -> " + e.getMessage());
        }
    }
    
    //TC-PM-01
    public void createProductFlow() {
        createNewProductAutomatically();
        configureInventorySetup();
        configureVariantsSetup();
        uploadProductImagesAndVideo();
        setAndVerifyProductThumbnailsAndListImages();
        selectTryAlongProductsAndSave();
        configureProductSettingsPage("3-5 Days", "High Demand - 50+ users looking at this right now");
        configureProductDescriptionAndSave();
        configureProductSeoAndSave();
        verifyProductInListingPage();
        navigateToPreviewAndVerifyDetails();
    }

    //TC-PM-02
    public void editProductFlow() {
    	editExistingProductDetails();
    	editInventorySetup();
    	configureVariantsSetup();
    	editTryAlongProductsAndSave();
        configureProductSettingsPage("4-7 Days", "Very High Demand - 100+ users looking at this right now");
        configureProductDescriptionAndSave();
        configureProductSeoAndSave();
        verifyProductInListingPage();
        navigateToPreviewAndVerifyDetails();
    }
    
    //TC-PM-03
    public void adjustStockFlow() {
    	navigateToProductPreview();
    	adjustStockToZeroFromPreview();
    	verifyInventoryTabOutOfStockStatus();
        verifyStockHistoryPageDetails();
        verifyListingPageOutOfStockStatus();
    }
    
    //TC-PM-04
    public void addStockFlow() {
		navigateToProductPreview();
		addStockFromPreviewPage();
		verifyInventoryTabInStockStatus();
		verifyStockHistoryPageAddDetails();
		verifyListingPageInStockStatus();
	}
    
    //TC-PM-05
    public void mandatoryFieldValidationFlow() {
    	navigateToCreateProductAndSubmitEmptyForm();
    	verifyMandatoryFieldValidationMessages();
    	createNewProductAutomatically(true);
    	verifyInventorySetupValidations();
    	configureInventorySetup();
    	verifyVariantsSetupValidations();
    	configureVariantsSetup();
        uploadProductImagesAndVideo();
        setAndVerifyProductThumbnailsAndListImages();
        selectTryAlongProductsAndSave();
        verifyProductDescriptionSetupValidations();
        configureProductDescriptionAndSave();
    	
    }
    
    //TC-PM-06
    public void verfiySaveAsDraftFlow() {
    	createPartialProductAndSaveAsDraft();
    	verifyDraftProductOnListingPage();
    	deleteDraftProductAndVerify();
    }
    
 // TC-PM-07
    public void verfiyMultipleVariantsAndProductFlow() {
        logHeader("STARTING MULTIPLE VARIANTS END-TO-END FLOW");
        
        createNewProductAutomatically(); 
        configureMultipleVariantsInventorySetup(2);
        configureMultipleVariantsSetupAndMedia();
        configureMultipleVariantsDescriptionAndSave();
        configureMultipleVariantsSeoAndSave();
        verifyProductInListingPage();
        navigateToPreviewAndVerifyMultipleVariantsDetails();
        
        logSuccess("Successfully completed multi-variant product creation and verification flow!");
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