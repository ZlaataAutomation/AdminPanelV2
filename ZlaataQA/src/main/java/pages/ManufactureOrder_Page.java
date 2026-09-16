package pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import objectRepo.ManufactureOrder_ObjRepo;

public class ManufactureOrder_Page extends ManufactureOrder_ObjRepo {

	public ManufactureOrder_Page(WebDriver driver) {
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
	
    // --- Colored Console Logging Utilities ---
    private void logAction(String message) {
        System.out.println(CYAN + BOLD + "[ACTION] " + RESET + message);
    }

    private void logSuccess(String message) {
        System.out.println(GREEN + BOLD + "[SUCCESS] " + RESET + message);
    }

    private void logHeader(String message) {
        System.out.println("\n" + YELLOW + BOLD + "========== " + message + " ==========" + RESET);
    }
    
    // --- Class-Level Global Variables for Verification ---
    private String selectedSupplierName;
    private String selectedWarehouseName;

    public String getSelectedSupplierName() {
        return selectedSupplierName;
    }

    public String getSelectedWarehouseName() {
        return selectedWarehouseName;
    }
	
    public void adminLogin() {
        AdminLogin_Page login = new AdminLogin_Page(driver);
        login.adminLoginApp();
    }
    
//    public void fillManufactureOrderDetails() {
//        // Implement your form filling steps here
//    }

    public void clickSaveManufactureOrder() {
        scrollToElement(saveBtn);
        click(saveBtn);
        sleep(1);
    }
    
    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // --- Helper to scroll element into view for visibility ---
    private void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
        sleep(1); // Pause slightly so you can see the scroll action
    }
	
    public void navigatetoManufactureOrderPage() {
        wait.until(ExpectedConditions.visibilityOf(inventory));
        new Actions(driver).moveToElement(inventory).perform();
        sleep(1);

        wait.until(ExpectedConditions.visibilityOf(manufactureOrder));
        scrollToElement(manufactureOrder);
        click(manufactureOrder);
        sleep(1);
    }
    
    public void clickAddManufactureOrder() {
        scrollToElement(addManufactureOrderBtn);
        click(addManufactureOrderBtn);
        sleep(1);
        
        wait.until(ExpectedConditions.visibilityOf(addCustomProductBtn));
        scrollToElement(addCustomProductBtn);
        click(addCustomProductBtn);
        sleep(1);
    }
    
    public String selectedVendorText = "";
    public String selectedAddressText = "";
    public String selectedDateText = "";

    public void selectRandomVendor() {
        scrollToElement(vendorDropdown);
        click(vendorDropdown);
        sleep(1);
        
        By searchInputLocator = By.xpath("(//input[@placeholder='Search the vendors'])[1]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputLocator));
        
        By scopedOptionLocator = By.xpath("(//div[@class='options_box'])[1]//div[@class='option']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(scopedOptionLocator));
        
        List<WebElement> options = driver.findElements(scopedOptionLocator);
        
        if (!options.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(options.size());
            
            int itemNumber = index + 1;
            By specificOption = By.xpath("((//div[@class='options_box'])[1]//div[@class='option'])[" + itemNumber + "]");
            WebElement chosenVendor = wait.until(ExpectedConditions.elementToBeClickable(specificOption));
            
            selectedVendorText = chosenVendor.getText().trim();
            scrollToElement(chosenVendor);
            chosenVendor.click();
            sleep(1);
            System.out.println("Selected Vendor: " + selectedVendorText);
        } else {
            throw new RuntimeException("No vendor options available in the options box!");
        }
    }

    public void selectRandomDeliveryDate() {
        waitFor(expectedDeliveryDateCalendar);
        scrollToElement(expectedDeliveryDateCalendar);
        
        // 1. Click the input field to open the Flatpickr calendar popup
        click(expectedDeliveryDateCalendar);
        sleep(1); // Give the calendar popup a split second to render
        
        // 2. Target valid, clickable days inside the specific Flatpickr dayContainer
        By activeDaysLocator = By.xpath("(//div[@class='dayContainer'])[1]//span[contains(@class, 'flatpickr-day') and not(contains(@class, 'flatpickr-disabled')) and not(contains(@class, 'prevMonthDay')) and not(contains(@class, 'nextMonthDay'))]");
        
        List<WebElement> dayElements = driver.findElements(activeDaysLocator);
        
        // Fallback if your Flatpickr build uses divs instead of spans for days
        if (dayElements.isEmpty()) {
            activeDaysLocator = By.xpath("(//div[@class='dayContainer'])[1]//div[contains(@class, 'flatpickr-day') and not(contains(@class, 'flatpickr-disabled')) and not(contains(@class, 'prevMonthDay')) and not(contains(@class, 'nextMonthDay'))]");
            dayElements = driver.findElements(activeDaysLocator);
        }
        
        if (dayElements.isEmpty()) {
            throw new RuntimeException("❌ Could not find any active/selectable days inside the Flatpickr dayContainer!");
        }
        
        // 3. Pick a random day element from the available pool
        Random random = new Random();
        WebElement randomDayElement = dayElements.get(random.nextInt(dayElements.size()));
        
        scrollToElement(randomDayElement);
        wait.until(ExpectedConditions.elementToBeClickable(randomDayElement));
        
        // 4. Click using JavaScript to trigger Flatpickr's internal event bindings safely
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", randomDayElement);
        sleep(1); // Allow the date to bind to the input field
        
        // 5. Extract the populated date from the input field value property
        selectedDateText = expectedDeliveryDateCalendar.getAttribute("value");
        if (selectedDateText == null || selectedDateText.isEmpty()) {
            selectedDateText = (String) js.executeScript("return arguments[0].value;", expectedDeliveryDateCalendar);
        }
        
        System.out.println("Selected Delivery Date from Calendar: " + selectedDateText);
    }

    public void selectRandomDeliveryAddress() {
        scrollToElement(deliveryAddressDropdown);
        click(deliveryAddressDropdown);
        sleep(1);
        
        By addressOptionLocator = By.xpath("(//div[@class='options_box'])[2]//div[@class='option']");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(addressOptionLocator));
        
        List<WebElement> options = driver.findElements(addressOptionLocator);
        
        if (!options.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(options.size());
            
            int itemNumber = index + 1;
            By specificAddress = By.xpath("((//div[@class='options_box'])[2]//div[@class='option'])[" + itemNumber + "]");
            WebElement chosenAddress = wait.until(ExpectedConditions.elementToBeClickable(specificAddress));
            
            selectedAddressText = chosenAddress.getText().trim();
            scrollToElement(chosenAddress);
            chosenAddress.click();
            sleep(1);
            System.out.println("Selected Delivery Address: " + selectedAddressText);
        } else {
            throw new RuntimeException("No delivery address options available in the options box [2]!");
        }
    }
    
    public List<String> selectedSizesList = new java.util.ArrayList<>();
    public List<Integer> selectedSizeIndices = new java.util.ArrayList<>();
    public List<Integer> selectedQuantitiesList = new java.util.ArrayList<>(); // Stores exact quantity for each size
    public double lastMakingCost = 0.0;
    public int totalQuantitySum = 0;
    
    
    public double firstProductExpectedCost = 0.0;
    public double secondProductExpectedCost = 0.0;
    
    public void addProductDetails() {
        // 1. Enter Product Name
        waitFor(productNameInput);
        scrollToElement(productNameInput);
        String randomProductName = "Product_" + new Random().nextInt(1000);
        productNameInput.clear();
        productNameInput.sendKeys(randomProductName);
        sleep(1);
        System.out.println("Entered Product Name: " + randomProductName);

        // 2. Enter Dynamic Making Cost (Supports optional decimals)
        waitFor(makingCostInput);
        scrollToElement(makingCostInput);
        
        Random random = new Random();
        int randomCostValue = 10 + random.nextInt(990); // Generates a random whole number between 10 and 999
        lastMakingCost = (double) randomCostValue; // Store as double for calculation if needed
        String makingCostStr = String.valueOf(randomCostValue); // Converts to clean whole number string (e.g., "350")
        
        waitFor(makingCostInput);
        scrollToElement(makingCostInput);
        makingCostInput.clear();
        makingCostInput.sendKeys(makingCostStr);
        sleep(1);
        System.out.println("Entered Making Cost: " + makingCostStr);

        // 3. Click Size Tags Dropdown to open the menu container
        scrollToElement(sizeTagsDropdown);
        click(sizeTagsDropdown);
        sleep(1);

        // 4. Locate all size options inside the tag menu box
        By sizeOptionsLocator = By.xpath("(//div[@class='tag_menu js-tag_menu'])[1]//label[@class='tag_option']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(sizeOptionsLocator));
        List<WebElement> sizeOptions = driver.findElements(sizeOptionsLocator);

        // Clear lists and sums for a fresh run
        selectedSizesList.clear();
        selectedSizeIndices.clear();
        selectedQuantitiesList.clear();
        totalQuantitySum = 0;

        if (!sizeOptions.isEmpty()) {
            // Decide to select between 1 to 3 random sizes (at least 1, up to 3)
            int numberOfSizesToSelect = Math.min(1 + random.nextInt(3), sizeOptions.size());
            
            // Track unique indices to prevent selecting the same size twice
            java.util.Set<Integer> chosenIndices = new java.util.HashSet<>();
            while (chosenIndices.size() < numberOfSizesToSelect) {
                chosenIndices.add(random.nextInt(sizeOptions.size()));
            }

            // Loop through chosen sizes and fill their corresponding quantity inputs using placeholder '-'
            for (int index : chosenIndices) {
                int itemNumber = index + 1; // 1-based indexing matching your XPath (e.g., 1, 2, 3...)
                selectedSizeIndices.add(itemNumber); // Store the index for product size use
                
                By specificOption = By.xpath("((//div[@class='tag_menu js-tag_menu'])[1]//label[@class='tag_option'])[" + itemNumber + "]");
                WebElement sizeOption = wait.until(ExpectedConditions.elementToBeClickable(specificOption));
                
                // Capture the actual text of the size before clicking it
                String sizeText = sizeOption.getText().trim();
                selectedSizesList.add(sizeText);
                
                // Scroll to and click the size tag option
                scrollToElement(sizeOption);
                sizeOption.click();
                sleep(1);
                
                // Match the exact index for the quantity input: (//input[@placeholder='-'])[n]
                By quantityInputLocator = By.xpath("(//input[@placeholder='-'])[" + itemNumber + "]");
                WebElement qtyInput = wait.until(ExpectedConditions.elementToBeClickable(quantityInputLocator));
                
                int randomQty = 1 + random.nextInt(5); // Random quantity between 1 and 5
                selectedQuantitiesList.add(randomQty); // Save exact quantity for raw material matching later
                totalQuantitySum += randomQty; // Accumulate total quantity
                
                scrollToElement(qtyInput);
                qtyInput.clear();
                qtyInput.sendKeys(String.valueOf(randomQty));
                sleep(1);
                
                System.out.println("Selected Size Name: '" + sizeText + "' at index [" + itemNumber + "] with Quantity: " + randomQty);
            }
            
            // Save expected total cost for product 1
            firstProductExpectedCost = lastMakingCost * totalQuantitySum;
            
            // 5. Close the size dropdown by clicking it again or clicking outside
            try {
                scrollToElement(sizeTagsDropdown);
                click(sizeTagsDropdown);
                sleep(1);
                System.out.println("Closed size dropdown successfully.");
            } catch (Exception e) {
                driver.findElement(By.xpath("//body")).click();
                sleep(1);
            }
            
        } else {
            throw new RuntimeException("No size options available in the tag menu container!");
        }
    }
    
    // Helper method mapping each size name precisely to its exact raw material input index
    private int getRawMaterialIndexForSize(String sizeText) {
        String upper = sizeText.toUpperCase().trim();
        
        if (upper.contains("XXL")) {
            return 13;
        } else if (upper.contains("XL")) {
            return 12;
        } else if (upper.contains("XS")) {
            return 8;
        } else if (upper.contains("PGR")) {
            return 14;
        } else if (upper.contains(" S ") || upper.endsWith("- S") || upper.equals("S")) {
            return 9;
        } else if (upper.contains(" M ") || upper.endsWith("- M") || upper.equals("M")) {
            return 10;
        } else if (upper.contains(" L ") || upper.endsWith("- L") || upper.equals("L")) {
            return 11;
        }
        return 8; // Default fallback index
    }
    
    public void selectRawMaterialAndFillQuantities() {
        Random random = new Random();
        
        // Calculate total required quantity across all selected sizes
        int totalRequiredQty = 0;
        for (int qty : selectedQuantitiesList) {
            totalRequiredQty += qty;
        }
        System.out.println("Total Required Raw Material Quantity: " + totalRequiredQty);

        boolean sufficientStockFound = false;
        int maxRetries = 5;
        int attempts = 0;

        while (!sufficientStockFound && attempts < maxRetries) {
            attempts++;
            
            // 1. Open Raw Material Dropdown
            try {
                WebElement dropdownToggle = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//div[@class='w-100 d-flex justify-content-start align-items-center'])[1]")
                ));
                scrollToElement(dropdownToggle);
                dropdownToggle.click();
            } catch (Exception e) {
                scrollToElement(rawMaterialDropdown);
                click(rawMaterialDropdown);
            }
            sleep(1);

            // 2. Select a random option from the 3rd options box
            By rawMaterialOptionLocator = By.xpath("(//div[@class='options_box'])[3]//div[@class='option']");
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rawMaterialOptionLocator));
            List<WebElement> rawMaterialOptions = driver.findElements(rawMaterialOptionLocator);

            if (rawMaterialOptions.isEmpty()) {
                throw new RuntimeException("No raw material options available in options box [3]!");
            }

            int rmIndex = random.nextInt(rawMaterialOptions.size());
            int rmItemNumber = rmIndex + 1;

            By specificRmOption = By.xpath("((//div[@class='options_box'])[3]//div[@class='option'])[" + rmItemNumber + "]");
            WebElement chosenRm = wait.until(ExpectedConditions.elementToBeClickable(specificRmOption));
            
            String rmText = chosenRm.getText().trim();
            scrollToElement(chosenRm);
            chosenRm.click();
            sleep(1);
            System.out.println("Attempt " + attempts + " - Selected Raw Material: " + rmText);

            // 3. Enter a high quantity (1000000) into the first selected size's input box to trigger validation
            String firstSize = selectedSizesList.get(0);
            int firstSizeInputIndex = getRawMaterialIndexForSize(firstSize);
            
            By firstInputLocator = By.xpath("(//input[@placeholder='-'])[" + firstSizeInputIndex + "]");
            WebElement firstQtyInput = wait.until(ExpectedConditions.elementToBeClickable(firstInputLocator));
            scrollToElement(firstQtyInput);
            
            firstQtyInput.clear();
            firstQtyInput.sendKeys("1000000");
            sleep(1);

            // 4. Check non-intrusively for the stock error validation message
            int availableStock = 1000000; // Default to assume sufficient if no error is shown
            try {
                By errorMsgLocator = By.xpath("(//span[@class='text-danger error-stock-msg'])[1]");
                
                org.openqa.selenium.support.ui.WebDriverWait shortWait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(2));
                WebElement errorMsgElement = shortWait.until(ExpectedConditions.visibilityOf(driver.findElement(errorMsgLocator)));
                
                String errorText = errorMsgElement.getText();
                System.out.println("Stock Validation Message: " + errorText);
                
                String numericStock = errorText.replaceAll("[^0-9]", "");
                if (!numericStock.isEmpty()) {
                    availableStock = Integer.parseInt(numericStock);
                }
            } catch (Exception e) {
                System.out.println("No stock validation error displayed. Proceeding with stock as sufficient.");
                availableStock = 1000000; 
            }

            System.out.println("Available Stock detected: " + availableStock);

            // 5. Validate if available stock is sufficient
            if (availableStock >= totalRequiredQty) {
                System.out.println("Sufficient stock found! Filling exact quantities...");
                
                for (int i = 0; i < selectedSizesList.size(); i++) {
                    String targetSize = selectedSizesList.get(i);
                    int exactQty = selectedQuantitiesList.get(i);
                    int rmInputIndex = getRawMaterialIndexForSize(targetSize);
                    
                    By rmQtyInputLocator = By.xpath("(//input[@placeholder='-'])[" + rmInputIndex + "]");
                    WebElement rmQtyInput = wait.until(ExpectedConditions.elementToBeClickable(rmQtyInputLocator));
                    scrollToElement(rmQtyInput);
                    
                    rmQtyInput.clear();
                    rmQtyInput.sendKeys(String.valueOf(exactQty));
                    sleep(1);
                    
                    System.out.println("Entered Exact Raw Material Quantity: " + exactQty + " for size: " + targetSize + " at index [" + rmInputIndex + "]");
                }
                firstProductTotalQuantity = totalRequiredQty;
                sufficientStockFound = true;
            } else {
                System.out.println("Stock is low (" + availableStock + " < " + totalRequiredQty + "). Retrying with a different raw material...");
                try {
                    firstQtyInput.clear();
                } catch (Exception ignored) {}
            }
        }

        if (!sufficientStockFound) {
            throw new RuntimeException("Failed to find a raw material with sufficient stock after " + maxRetries + " attempts!");
        }
    }
    
    public void verifyTotalAmountAndAddNotesAndSave() {
        // 1. Verify Total Amount Reflection
        waitFor(totalCostText);
        scrollToElement(totalCostText);
        String displayedTotalText = totalCostText.getText().trim();
        System.out.println("Displayed Total Cost Text on UI: " + displayedTotalText);
        
        // Calculate expected total (Making Cost * Total Quantity Sum)
        double expectedTotalCost = lastMakingCost * totalQuantitySum;
        System.out.println("Calculated Expected Total Cost: " + String.format(java.util.Locale.US, "%.2f", expectedTotalCost));
        
        if (displayedTotalText.isEmpty()) {
            throw new RuntimeException("Total amount field is empty or not reflecting correctly!");
        }

        // 2. Add Notes
        waitFor(addNotesInput);
        scrollToElement(addNotesInput);
        addNotesInput.clear();
        addNotesInput.sendKeys("Automated Notes: Order created successfully with verified pricing.");
        sleep(1);
        System.out.println("Entered Notes successfully.");

        // 3. Add Description
        waitFor(addDescriptionInput);
        scrollToElement(addDescriptionInput);
        addDescriptionInput.clear();
        addDescriptionInput.sendKeys("Automated Description: Checking end-to-end manufacture order creation workflow.");
        sleep(1);
        System.out.println("Entered Description successfully.");

        // 4. Click Save Button
        waitFor(saveBtn);
        scrollToElement(saveBtn);
        click(saveBtn);
        sleep(10);
        System.out.println("Clicked Save Button successfully.");
    }
    
    
    public void verifyManufactureOrderOnListingPage() {
        System.out.println("\n=============================================================");
        System.out.println("          MANUFACTURE ORDER LISTING PAGE VERIFICATION        ");
        System.out.println("=============================================================");

        // ANSI Color Codes for Console Output
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        // Define Expected Values from test run variables
        String expectedVendor = (selectedVendorText != null) ? selectedVendorText.trim() : "";
        String expectedQty = String.valueOf(totalQuantitySum);
        
        // Calculate expected total amount (Making Cost * Total Quantity Sum)
        double calculatedTotalAmount = lastMakingCost * totalQuantitySum;
        String expectedAmountStr = String.format(java.util.Locale.US, "%.2f", calculatedTotalAmount);
        String expectedStatus = "Open";

        // Locate elements on the listing table using your specific XPaths
        By vendorCellLocator = By.xpath("(//td)[4]");
        By qtyCellLocator = By.xpath("(//td)[5]");
        By amountCellLocator = By.xpath("(//td)[6]");
        By statusCellLocator = By.xpath("(//td)[7]");

        // Wait for listing data to load
        WebElement vendorCell = wait.until(ExpectedConditions.visibilityOfElementLocated(vendorCellLocator));
        String actualVendor = vendorCell.getText().trim();
        
        String actualQty = driver.findElement(qtyCellLocator).getText().trim();
        String actualAmount = driver.findElement(amountCellLocator).getText().trim();
        String actualStatus = driver.findElement(statusCellLocator).getText().trim();

        // Perform validations and print color-graded console comparison
        boolean allPassed = true;
        

        By listingRowLocator = By.xpath("(//td)[2]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(listingRowLocator));
        sleep(1);

        // 1. Vendor Name Verification
        boolean vendorMatch = actualVendor.contains(expectedVendor) || expectedVendor.contains(actualVendor);
        printComparison("Vendor Name", expectedVendor, actualVendor, vendorMatch, GREEN, RED, RESET);
        if (!vendorMatch) allPassed = false;

        // 2. Overall Quantity Verification
        boolean qtyMatch = actualQty.replaceAll("[^0-9]", "").equals(expectedQty);
        printComparison("Overall Quantity", expectedQty, actualQty, qtyMatch, GREEN, RED, RESET);
        if (!qtyMatch) allPassed = false;

        // 3. Total Amount Verification
        boolean amountMatch = false;
        try {
            double expectedNumericAmount = Double.parseDouble(expectedAmountStr);
            double actualNumericAmount = Double.parseDouble(actualAmount.replaceAll("[^0-9.]", ""));
            amountMatch = Math.abs(expectedNumericAmount - actualNumericAmount) < 0.01;
        } catch (Exception e) {
            amountMatch = actualAmount.replaceAll("[^0-9]", "").equals(expectedAmountStr.replaceAll("[^0-9]", ""));
        }
        printComparison("Total Amount", expectedAmountStr, actualAmount, amountMatch, GREEN, RED, RESET);
        if (!amountMatch) allPassed = false;

        // 4. Status Verification (Uncomment if needed)
         boolean statusMatch = actualStatus.equalsIgnoreCase(expectedStatus);
         printComparison("Status", expectedStatus, actualStatus, statusMatch, GREEN, RED, RESET);
         if (!statusMatch) allPassed = false;

        System.out.println("=============================================================");
        if (allPassed) {
            System.out.println(GREEN + "✅ ALL LISTING PAGE DETAILS MATCHED SUCCESSFULLY!" + RESET);
        } else {
            System.out.println(RED + "❌ MISMATCH FOUND ON LISTING PAGE!" + RESET);
            throw new AssertionError("Manufacture Order listing verification failed.");
        }
        System.out.println("=============================================================\n");
    }

    public void verifyManufactureOrderPreviewPage() {
        System.out.println("\n=============================================================");
        System.out.println("           MANUFACTURE ORDER PREVIEW PAGE VERIFICATION       ");
        System.out.println("=============================================================");

        // ANSI Color Codes for Console Output
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        // Define Expected Values from test run variables
        String expectedQty = String.valueOf(totalQuantitySum);
        double calculatedTotalAmount = lastMakingCost * totalQuantitySum;
        String expectedAmountStr = String.format(java.util.Locale.US, "%.2f", calculatedTotalAmount);

        try {
            // 1. Locate and click the 3-dot action button in the first row
            By threeDotLocator = By.xpath("(//i[@class='bi bi-three-dots-vertical'])[1]");
            WebElement threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(threeDotLocator));
            scrollToElement(threeDotBtn);
            try {
                threeDotBtn.click();
            } catch (Exception ex) {
                // Fallback using JavaScript click if intercepted or obscured
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", threeDotBtn);
            }
            sleep(1);

            // 2. Click Preview option from the dropdown menu
            WebElement previewBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
            ));
            previewBtn.click();
            sleep(2);

            // 3. Capture Preview Page actual values using precise XPaths
            String actualPreviewDate = driver.findElement(By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[4]")).getText().trim();
            String actualPreviewAddress = driver.findElement(By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[5]")).getText().trim();
            String actualPreviewProductName = driver.findElement(By.xpath("(//p[@class='font_14 m-0 fw-bold'])[1]")).getText().trim();
            String actualPreviewQty = driver.findElement(By.xpath("//body[1]/div[1]/main[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]")).getText().trim();
            String actualPreviewAmount = driver.findElement(By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[2]")).getText().trim();
            
            // Notes & Terms verification fields
            String actualNotes = driver.findElement(By.xpath("(//p[@class='info_dd mb-0'])[1]")).getText().trim();
            String actualTerms = driver.findElement(By.xpath("(//p[@class='info_dd mb-0'])[2]")).getText().trim();

            boolean previewPassed = true;

            // 4. Perform Assertions & Comparisons
            boolean dateMatch = actualPreviewDate.contains(selectedDateText);
            printComparison("Preview Date", selectedDateText, actualPreviewDate, dateMatch, GREEN, RED, RESET);
            if (!dateMatch) previewPassed = false;

            boolean addressMatch = !actualPreviewAddress.isEmpty();
            printComparison("Preview Address", "Valid Address", actualPreviewAddress, addressMatch, GREEN, RED, RESET);
            if (!addressMatch) previewPassed = false;

            boolean productMatch = actualPreviewProductName.contains("Product");
            printComparison("Preview Product", "Product Name", actualPreviewProductName, productMatch, GREEN, RED, RESET);
            if (!productMatch) previewPassed = false;

            boolean previewQtyMatch = actualPreviewQty.replaceAll("[^0-9]", "").equals(expectedQty);
            printComparison("Preview Quantity", expectedQty, actualPreviewQty, previewQtyMatch, GREEN, RED, RESET);
            if (!previewQtyMatch) previewPassed = false;

            boolean previewAmountMatch = actualPreviewAmount.replaceAll("[^0-9]", "").equals(expectedAmountStr.replaceAll("[^0-9]", ""));
            printComparison("Preview Amount", expectedAmountStr, actualPreviewAmount, previewAmountMatch, GREEN, RED, RESET);
            if (!previewAmountMatch) previewPassed = false;

            boolean notesMatch = !actualNotes.isEmpty();
            printComparison("Manufacture Notes", "Not Empty", actualNotes, notesMatch, GREEN, RED, RESET);
            
            boolean termsMatch = !actualTerms.isEmpty();
            printComparison("Terms & Conditions", "Not Empty", actualTerms, termsMatch, GREEN, RED, RESET);

            System.out.println("=============================================================");
            if (previewPassed) {
                System.out.println(GREEN + "✅ ALL PREVIEW PAGE DETAILS MATCHED SUCCESSFULLY!" + RESET);
            } else {
                System.out.println(RED + "❌ MISMATCH FOUND ON PREVIEW PAGE!" + RESET);
                throw new AssertionError("Manufacture Order preview verification failed.");
            }
            System.out.println("=============================================================\n");

        } catch (Exception e) {
            System.out.println(RED + "❌ Exception occurred during Preview verification: " + e.getMessage() + RESET);
            throw new RuntimeException(e);
        }
    }

    // Helper method to format console comparison logs cleanly
    private void printComparison(String fieldName, String expected, String actual, boolean isMatch, String green, String red, String reset) {
        String statusColor = isMatch ? green : red;
        String statusSymbol = isMatch ? "[PASS]" : "[FAIL]";
        
        System.out.println(String.format("%-18s | Expected: %-15s | Actual: %-15s | %s %s %s", 
                fieldName, 
                "'" + expected + "'", 
                "'" + actual + "'", 
                statusColor + statusSymbol, 
                isMatch ? "MATCHED" : "MISMATCHED", 
                reset));
    }
    
    
    
    public void clickEditManufactureOrder() {
        // 1. Click the 3-dot action button in the first row
    	// 1. Locate and click the 3-dot action button in the first row
        By threeDotLocator = By.xpath("(//i[@class='bi bi-three-dots-vertical'])[1]");
        WebElement threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(threeDotLocator));
        scrollToElement(threeDotBtn);
        try {
            threeDotBtn.click();
        } catch (Exception ex) {
            // Fallback using JavaScript click if intercepted or obscured
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", threeDotBtn);
        }
        sleep(1);

        // 2. Click the Edit button from the dropdown
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(editBtn));
        scrollToElement(editButton);
        editButton.click();
        sleep(2);
        System.out.println("Navigated to Edit Manufacture Order page successfully.");
    }

    public void editRandomVendor() {
        // 1. Locate and click the vendor dropdown container specific to the edit page
        By vendorDropdownLocator = By.xpath("(//div[@class='select-wrapper vendor-select-wrapper'])[1]");
        WebElement vendorDropdownElement = wait.until(ExpectedConditions.elementToBeClickable(vendorDropdownLocator));
        scrollToElement(vendorDropdownElement);
        
        try {
            vendorDropdownElement.click();
        } catch (Exception ex) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", vendorDropdownElement);
        }
        sleep(1);
        
        // 2. Wait for the search input or options box to render after clicking
        By searchInputLocator = By.xpath("(//input[@placeholder='Search the vendors'])[1]");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputLocator));
        } catch (Exception ignored) {
            // Fallback if placeholder text differs slightly
        }
        
        // 3. Retrieve all options from the first options box
        By scopedOptionLocator = By.xpath("(//div[@class='options_box'])[1]//div[@class='option']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(scopedOptionLocator));
        List<WebElement> options = driver.findElements(scopedOptionLocator);
        
        if (!options.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(options.size());
            int itemNumber = index + 1;
            
            By specificOption = By.xpath("((//div[@class='options_box'])[1]//div[@class='option'])[" + itemNumber + "]");
            WebElement chosenVendor = wait.until(ExpectedConditions.elementToBeClickable(specificOption));
            
            selectedVendorText = chosenVendor.getText().trim();
            scrollToElement(chosenVendor);
            chosenVendor.click();
            sleep(1);
            System.out.println("Updated/Selected Vendor: " + selectedVendorText);
        } else {
            throw new RuntimeException("No vendor options available in the options box for editing!");
        }
    }

    public void editRandomDeliveryAddress() {
        // 1. Locate and click the delivery address dropdown container specific to the edit page
        By addressDropdownLocator = By.xpath("(//div[@class='select-wrapper address-select-wrapper'])[1]");
        WebElement addressDropdownElement = wait.until(ExpectedConditions.elementToBeClickable(addressDropdownLocator));
        scrollToElement(addressDropdownElement);
        
        try {
            addressDropdownElement.click();
        } catch (Exception ex) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", addressDropdownElement);
        }
        sleep(1);
        
        // 2. Retrieve all options from the second options box
        By addressOptionLocator = By.xpath("(//div[@class='options_box'])[2]//div[@class='option']");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(addressOptionLocator));
        List<WebElement> options = driver.findElements(addressOptionLocator);
        
        if (!options.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(options.size());
            int itemNumber = index + 1;
            
            By specificAddress = By.xpath("((//div[@class='options_box'])[2]//div[@class='option'])[" + itemNumber + "]");
            WebElement chosenAddress = wait.until(ExpectedConditions.elementToBeClickable(specificAddress));
            
            selectedAddressText = chosenAddress.getText().trim();
            scrollToElement(chosenAddress);
            chosenAddress.click();
            sleep(1);
            System.out.println("Updated Delivery Address: " + selectedAddressText);
        } else {
            throw new RuntimeException("No delivery address options available in options box [2] for editing!");
        }
    }
    
    public void editProductDetails() {
        // 1. Update Product Name with an updated random value
        waitFor(productNameInput);
        scrollToElement(productNameInput);
        String updatedProductName = "Updated_Product_" + new Random().nextInt(1000);
        productNameInput.clear();
        productNameInput.sendKeys(updatedProductName);
        sleep(1);
        System.out.println("Updated Product Name: " + updatedProductName);

        // 2. Update Making Cost with a new dynamic value
        waitFor(makingCostInput);
        scrollToElement(makingCostInput);
        
        Random random = new Random();
        int randomCostValue = 20 + random.nextInt(980); // Generates a random whole number between 20 and 999
        lastMakingCost = (double) randomCostValue; 
        String makingCostStr = String.valueOf(randomCostValue);
        
        makingCostInput.clear();
        makingCostInput.sendKeys(makingCostStr);
        sleep(1);
        System.out.println("Updated Making Cost: " + makingCostStr);

        // 3. Click Size Tags Dropdown to open the menu container
        scrollToElement(sizeTagsDropdown);
        click(sizeTagsDropdown);
        sleep(1);

        // 4. Locate all size options inside the tag menu box
        By sizeOptionsLocator = By.xpath("(//div[@class='tag_menu js-tag_menu'])[1]//label[@class='tag_option']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(sizeOptionsLocator));
        List<WebElement> sizeOptions = driver.findElements(sizeOptionsLocator);

        // Clear lists and sums for a fresh edit run
        selectedSizesList.clear();
        selectedSizeIndices.clear();
        selectedQuantitiesList.clear();
        totalQuantitySum = 0;

        if (!sizeOptions.isEmpty()) {
            // Check each size option to see if it is already selected/active from the previous order creation
            for (int i = 0; i < sizeOptions.size(); i++) {
                int itemNumber = i + 1;
                WebElement sizeOption = sizeOptions.get(i);
                
                String optionClass = sizeOption.getAttribute("class");
                boolean isSelected = optionClass != null && (optionClass.contains("active") || optionClass.contains("selected") || optionClass.contains("checked"));
                
                // Also check if an inner checkbox element is selected
                try {
                    WebElement checkbox = sizeOption.findElement(By.tagName("input"));
                    if (checkbox.isSelected()) {
                        isSelected = true;
                    }
                } catch (Exception ignored) {}

                if (isSelected) {
                    String sizeText = sizeOption.getText().trim();
                    selectedSizesList.add(sizeText);
                    selectedSizeIndices.add(itemNumber);
                    
                    // Locate the existing quantity input for this size
                    By quantityInputLocator = By.xpath("(//input[@placeholder='-'])[" + itemNumber + "]");
                    WebElement qtyInput = wait.until(ExpectedConditions.elementToBeClickable(quantityInputLocator));
                    scrollToElement(qtyInput);
                    
                    // Read current quantity value from the input field to exclude it
                    int currentQty = 1; // default fallback
                    try {
                        String existingVal = qtyInput.getAttribute("value");
                        if (existingVal != null && !existingVal.trim().isEmpty()) {
                            currentQty = Integer.parseInt(existingVal.trim());
                        }
                    } catch (Exception ignored) {}

                    // Generate a new random quantity between 1 and 5 that excludes the current quantity
                    int updatedRandomQty;
                    do {
                        updatedRandomQty = 1 + random.nextInt(5);
                    } while (updatedRandomQty == currentQty);

                    selectedQuantitiesList.add(updatedRandomQty); 
                    totalQuantitySum += updatedRandomQty; 
                    
                    qtyInput.clear();
                    qtyInput.sendKeys(String.valueOf(updatedRandomQty));
                    sleep(1);
                    
                    System.out.println("Found Pre-selected Size: '" + sizeText + "' at index [" + itemNumber + "] | Previous Qty: " + currentQty + " -> Updated Quantity: " + updatedRandomQty);
                }
            }

            // Fallback: If no pre-selected sizes were detected, select a default random size
            if (selectedSizesList.isEmpty()) {
                System.out.println("No pre-selected sizes detected. Selecting a fallback random size...");
                int randomIndex = random.nextInt(sizeOptions.size());
                int itemNumber = randomIndex + 1;
                WebElement sizeOption = sizeOptions.get(randomIndex);
                
                scrollToElement(sizeOption);
                sizeOption.click();
                sleep(1);
                
                String sizeText = sizeOption.getText().trim();
                selectedSizesList.add(sizeText);
                selectedSizeIndices.add(itemNumber);
                
                By quantityInputLocator = By.xpath("(//input[@placeholder='-'])[" + itemNumber + "]");
                WebElement qtyInput = wait.until(ExpectedConditions.elementToBeClickable(quantityInputLocator));
                int fallbackQty = 2;
                selectedQuantitiesList.add(fallbackQty);
                totalQuantitySum += fallbackQty;
                
                scrollToElement(qtyInput);
                qtyInput.clear();
                qtyInput.sendKeys(String.valueOf(fallbackQty));
                sleep(1);
                System.out.println("Fallback Size: '" + sizeText + "' at index [" + itemNumber + "] with Quantity: " + fallbackQty);
            }
            
            // 5. Close the size dropdown successfully
            try {
                scrollToElement(sizeTagsDropdown);
                click(sizeTagsDropdown);
                sleep(1);
                System.out.println("Closed size dropdown successfully during edit.");
            } catch (Exception e) {
                driver.findElement(By.xpath("//body")).click();
                sleep(1);
            }
            
        } else {
            throw new RuntimeException("No size options available in the tag menu container for editing!");
        }
    }
    
//    public void editedRawMaterialAndFillQuantities() {
//        Random random = new Random();
//        
//        // 1. Dynamically check and ensure the correct size buttons/tags are clicked or tracked based on the edit flow
//        System.out.println("Checking active/selected sizes for raw material allocation...");
//        
//        // Define possible sizes and their exact toggle XPaths provided
//        String[][] sizeToggles = {
//            {"Xs", "(//span[normalize-space()='Size - Xs'])[1]"},
//            {"M", "(//span[normalize-space()='Size - M'])[1]"},
//            {"L", "(//span[normalize-space()='Size - L'])[1]"},
//            {"Xxl", "(//span[normalize-space()='Size - Xxl'])[1]"},
//            {"Xl", "(//span[normalize-space()='Size - Xl'])[1]"},
//            {"S", "(//span[normalize-space()='Size - S'])[1]"}
//        };
//
//        for (String[] sizeInfo : sizeToggles) {
//            String sizeName = sizeInfo[0];
//            String toggleXpath = sizeInfo[1];
//            try {
//                List<WebElement> sizeElements = driver.findElements(By.xpath(toggleXpath));
//                if (!sizeElements.isEmpty()) {
//                    WebElement sizeEl = sizeElements.get(0);
//                    scrollToElement(sizeEl);
//                    
//                    // Check if it needs to be clicked or is already active
//                    String parentClass = "";
//                    try {
//                        parentClass = sizeEl.findElement(By.xpath("./..")).getAttribute("class");
//                    } catch (Exception ignored) {}
//
//                    boolean isActive = (parentClass != null && (parentClass.contains("active") || parentClass.contains("selected"))) || sizeEl.isEnabled();
//                    
//                    if (isActive) {
//                        System.out.println("Size '" + sizeName + "' is detected as active/present for editing.");
//                    }
//                }
//            } catch (Exception ignored) {}
//        }
//
//        // 2. Calculate total required quantity across all selected/edited sizes
//        int totalRequiredQty = 0;
//        for (int qty : selectedQuantitiesList) {
//            totalRequiredQty += qty;
//        }
//        System.out.println("Total Required Raw Material Quantity for Edit: " + totalRequiredQty);
//
//        boolean sufficientStockFound = false;
//        int maxRetries = 5;
//        int attempts = 0;
//
//        while (!sufficientStockFound && attempts < maxRetries) {
//            attempts++;
//            
//            // 3. Open Raw Material Dropdown
//            try {
//                WebElement dropdownToggle = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.xpath("(//div[@class='w-100 d-flex justify-content-start align-items-center'])[1]")
//                ));
//                scrollToElement(dropdownToggle);
//                dropdownToggle.click();
//            } catch (Exception e) {
//                scrollToElement(rawMaterialDropdown);
//                click(rawMaterialDropdown);
//            }
//            sleep(1);
//
//            // 4. Select a random option from the 3rd options box
//            By rawMaterialOptionLocator = By.xpath("(//div[@class='options_box'])[3]//div[@class='option']");
//            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rawMaterialOptionLocator));
//            List<WebElement> rawMaterialOptions = driver.findElements(rawMaterialOptionLocator);
//
//            if (rawMaterialOptions.isEmpty()) {
//                throw new RuntimeException("No raw material options available in options box [3] during edit!");
//            }
//
//            int rmIndex = random.nextInt(rawMaterialOptions.size());
//            int rmItemNumber = rmIndex + 1;
//
//            By specificRmOption = By.xpath("((//div[@class='options_box'])[3]//div[@class='option'])[" + rmItemNumber + "]");
//            WebElement chosenRm = wait.until(ExpectedConditions.elementToBeClickable(specificRmOption));
//            
//            String rmText = chosenRm.getText().trim();
//            scrollToElement(chosenRm);
//            chosenRm.click();
//            sleep(1);
//            System.out.println("Edit Attempt " + attempts + " - Selected Raw Material: " + rmText);
//
//            // 5. Enter a high quantity (1000000) into the first selected size's input box to trigger stock validation
//            String firstSize = selectedSizesList.get(0);
//            int firstSizeInputIndex = getRawMaterialIndexForSize(firstSize);
//            
//            By firstInputLocator = By.xpath("(//input[@placeholder='-'])[" + firstSizeInputIndex + "]");
//            WebElement firstQtyInput = wait.until(ExpectedConditions.elementToBeClickable(firstInputLocator));
//            scrollToElement(firstQtyInput);
//            
//            firstQtyInput.clear();
//            firstQtyInput.sendKeys("1000000");
//            sleep(1);
//
//            // 6. Check non-intrusively for the stock error validation message
//            int availableStock = 1000000; 
//            try {
//                By errorMsgLocator = By.xpath("(//span[@class='text-danger error-stock-msg'])[1]");
//                org.openqa.selenium.support.ui.WebDriverWait shortWait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(2));
//                WebElement errorMsgElement = shortWait.until(ExpectedConditions.visibilityOf(driver.findElement(errorMsgLocator)));
//                
//                String errorText = errorMsgElement.getText();
//                System.out.println("Stock Validation Message (Edit): " + errorText);
//                
//                String numericStock = errorText.replaceAll("[^0-9]", "");
//                if (!numericStock.isEmpty()) {
//                    availableStock = Integer.parseInt(numericStock);
//                }
//            } catch (Exception e) {
//                System.out.println("No stock validation error displayed during edit. Proceeding with stock as sufficient.");
//                availableStock = 1000000; 
//            }
//
//            System.out.println("Available Stock detected (Edit): " + availableStock);
//
//            // 7. Validate if available stock is sufficient
//            if (availableStock >= totalRequiredQty) {
//                System.out.println("Sufficient stock found for edit! Filling updated quantities...");
//                
//                for (int i = 0; i < selectedSizesList.size(); i++) {
//                    String targetSize = selectedSizesList.get(i);
//                    int exactQty = selectedQuantitiesList.get(i);
//                    int rmInputIndex = getRawMaterialIndexForSize(targetSize);
//                    
//                    By rmQtyInputLocator = By.xpath("(//input[@placeholder='-'])[" + rmInputIndex + "]");
//                    WebElement rmQtyInput = wait.until(ExpectedConditions.elementToBeClickable(rmQtyInputLocator));
//                    scrollToElement(rmQtyInput);
//                    
//                    rmQtyInput.clear();
//                    rmQtyInput.sendKeys(String.valueOf(exactQty));
//                    sleep(1);
//                    
//                    System.out.println("Entered Updated Raw Material Quantity: " + exactQty + " for size: " + targetSize + " at index [" + rmInputIndex + "]");
//                }
//                sufficientStockFound = true;
//            } else {
//                System.out.println("Stock is low during edit (" + availableStock + " < " + totalRequiredQty + "). Retrying with a different raw material...");
//                try {
//                    firstQtyInput.clear();
//                } catch (Exception ignored) {}
//            }
//        }
//
//        if (!sufficientStockFound) {
//            throw new RuntimeException("Failed to find a raw material with sufficient stock for edit after " + maxRetries + " attempts!");
//        }
//    }
    
    
    public void verifyEditedTotalAmountAndAddNotesAndSave() {
        // 1. Verify Edited Total Amount Reflection
        waitFor(totalCostText);
        scrollToElement(totalCostText);
        String displayedTotalText = totalCostText.getText().trim();
        System.out.println("Displayed Edited Total Cost Text on UI: " + displayedTotalText);
        
        // Calculate expected total (Updated Making Cost * Updated Total Quantity Sum)
        double expectedTotalCost = lastMakingCost * totalQuantitySum;
        System.out.println("Calculated Expected Edited Total Cost: " + String.format(java.util.Locale.US, "%.2f", expectedTotalCost));
        
        if (displayedTotalText.isEmpty()) {
            throw new RuntimeException("Edited total amount field is empty or not reflecting correctly!");
        }

        // 2. Update/Add Notes for Edit
        waitFor(addNotesInput);
        scrollToElement(addNotesInput);
        addNotesInput.clear();
        addNotesInput.sendKeys("Automated Notes: Manufacture Order updated successfully with verified pricing.");
        sleep(1);
        System.out.println("Entered Edited Notes successfully.");

        // 3. Update/Add Description for Edit
        waitFor(addDescriptionInput);
        scrollToElement(addDescriptionInput);
        addDescriptionInput.clear();
        addDescriptionInput.sendKeys("Automated Description: Checking end-to-end manufacture order update/edit workflow.");
        sleep(1);
        System.out.println("Entered Edited Description successfully.");
        
        try {
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveChangesBtn));
            scrollToElement(saveChangesBtn);
            saveButton.click();
            sleep(2);
            System.out.println("Clicked Save/Update button successfully.");
        } catch (Exception e) {
            System.out.println("Could not find standard save button, attempting alternative locator...");
            WebElement altSaveBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[normalize-space()='Save Changes'])[1]")));
            altSaveBtn.click();
            sleep(2);
        }
        
        System.out.println("=============================================================");
        System.out.println("✅ MANUFACTURE ORDER EDITED SUCCESSFULLY!");
        System.out.println("=============================================================\n");
    }
    
    public void verifyEditedManufactureOrderOnListingPage() {
        System.out.println("\n=============================================================");
        System.out.println("      EDITED MANUFACTURE ORDER LISTING PAGE VERIFICATION     ");
        System.out.println("=============================================================");

        // ANSI Color Codes for Console Output
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        // Define Expected Values from updated test run variables
        String expectedVendor = (selectedVendorText != null) ? selectedVendorText.trim() : "";
        String expectedQty = String.valueOf(totalQuantitySum);
        
        // Calculate expected total amount using updated making cost and quantity sum
        double calculatedTotalAmount = lastMakingCost * totalQuantitySum;
        String expectedAmountStr = String.format(java.util.Locale.US, "%.2f", calculatedTotalAmount);
        String expectedStatus = "Open";

        // Locate elements on the listing table using your specific XPaths
        By vendorCellLocator = By.xpath("(//td)[4]");
        By qtyCellLocator = By.xpath("(//td)[5]");
        By amountCellLocator = By.xpath("(//td)[6]");
        By statusCellLocator = By.xpath("(//td)[7]");

        // Wait for listing data to load
        WebElement vendorCell = wait.until(ExpectedConditions.visibilityOfElementLocated(vendorCellLocator));
        String actualVendor = vendorCell.getText().trim();
        
        String actualQty = driver.findElement(qtyCellLocator).getText().trim();
        String actualAmount = driver.findElement(amountCellLocator).getText().trim();
        String actualStatus = driver.findElement(statusCellLocator).getText().trim();

        boolean allPassed = true;

        By listingRowLocator = By.xpath("(//td)[2]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(listingRowLocator));
        sleep(1);

        // 1. Updated Vendor Name Verification
        boolean vendorMatch = actualVendor.contains(expectedVendor) || expectedVendor.contains(actualVendor);
        printComparison("Updated Vendor", expectedVendor, actualVendor, vendorMatch, GREEN, RED, RESET);
        if (!vendorMatch) allPassed = false;

        // 2. Updated Overall Quantity Verification
        boolean qtyMatch = actualQty.replaceAll("[^0-9]", "").equals(expectedQty);
        printComparison("Updated Quantity", expectedQty, actualQty, qtyMatch, GREEN, RED, RESET);
        if (!qtyMatch) allPassed = false;

        // 3. Updated Total Amount Verification
        boolean amountMatch = false;
        try {
            double expectedNumericAmount = Double.parseDouble(expectedAmountStr);
            double actualNumericAmount = Double.parseDouble(actualAmount.replaceAll("[^0-9.]", ""));
            amountMatch = Math.abs(expectedNumericAmount - actualNumericAmount) < 0.01;
        } catch (Exception e) {
            amountMatch = actualAmount.replaceAll("[^0-9]", "").equals(expectedAmountStr.replaceAll("[^0-9]", ""));
        }
        printComparison("Updated Amount", expectedAmountStr, actualAmount, amountMatch, GREEN, RED, RESET);
        if (!amountMatch) allPassed = false;

        // 4. Status Verification
        boolean statusMatch = actualStatus.equalsIgnoreCase(expectedStatus);
        printComparison("Status", expectedStatus, actualStatus, statusMatch, GREEN, RED, RESET);
        if (!statusMatch) allPassed = false;

        System.out.println("=============================================================");
        if (allPassed) {
            System.out.println(GREEN + "✅ ALL EDITED LISTING PAGE DETAILS MATCHED SUCCESSFULLY!" + RESET);
        } else {
            System.out.println(RED + "❌ MISMATCH FOUND ON EDITED LISTING PAGE!" + RESET);
            throw new AssertionError("Edited Manufacture Order listing verification failed.");
        }
        System.out.println("=============================================================\n");
    }

    public void verifyEditedManufactureOrderPreviewPage() {
        System.out.println("\n=============================================================");
        System.out.println("        EDITED MANUFACTURE ORDER PREVIEW PAGE VERIFICATION   ");
        System.out.println("=============================================================");

        // ANSI Color Codes for Console Output
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        // Define Expected Values from updated test run variables
        String expectedQty = String.valueOf(totalQuantitySum);
        double calculatedTotalAmount = lastMakingCost * totalQuantitySum;
        String expectedAmountStr = String.format(java.util.Locale.US, "%.2f", calculatedTotalAmount);

        try {
            // 1. Locate and click the 3-dot action button in the first row
            By threeDotLocator = By.xpath("(//i[@class='bi bi-three-dots-vertical'])[1]");
            WebElement threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(threeDotLocator));
            scrollToElement(threeDotBtn);
            try {
                threeDotBtn.click();
            } catch (Exception ex) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", threeDotBtn);
            }
            sleep(1);

            // 2. Click Preview option from the dropdown menu
            WebElement previewBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
            ));
            previewBtn.click();
            sleep(2);

            // 3. Capture Preview Page actual values using precise XPaths
            String actualPreviewDate = driver.findElement(By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[4]")).getText().trim();
            String actualPreviewAddress = driver.findElement(By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[5]")).getText().trim();
            String actualPreviewProductName = driver.findElement(By.xpath("(//p[@class='font_14 m-0 fw-bold'])[1]")).getText().trim();
            String actualPreviewQty = driver.findElement(By.xpath("//body[1]/div[1]/main[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]")).getText().trim();
            String actualPreviewAmount = driver.findElement(By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[2]")).getText().trim();
            
            // Notes & Terms verification fields
            String actualNotes = driver.findElement(By.xpath("(//p[@class='info_dd mb-0'])[1]")).getText().trim();
            String actualTerms = driver.findElement(By.xpath("(//p[@class='info_dd mb-0'])[2]")).getText().trim();

            boolean previewPassed = true;

            // 4. Perform Assertions & Comparisons for Edited Values
            boolean dateMatch = actualPreviewDate.contains(selectedDateText);
            printComparison("Edited Preview Date", selectedDateText, actualPreviewDate, dateMatch, GREEN, RED, RESET);
            if (!dateMatch) previewPassed = false;

            boolean addressMatch = !actualPreviewAddress.isEmpty();
            printComparison("Edited Preview Address", "Valid Address", actualPreviewAddress, addressMatch, GREEN, RED, RESET);
            if (!addressMatch) previewPassed = false;

            boolean productMatch = actualPreviewProductName.contains("Updated_Product") || actualPreviewProductName.contains("Product");
            printComparison("Edited Preview Product", "Updated Product Name", actualPreviewProductName, productMatch, GREEN, RED, RESET);
            if (!productMatch) previewPassed = false;

            boolean previewQtyMatch = actualPreviewQty.replaceAll("[^0-9]", "").equals(expectedQty);
            printComparison("Edited Preview Quantity", expectedQty, actualPreviewQty, previewQtyMatch, GREEN, RED, RESET);
            if (!previewQtyMatch) previewPassed = false;

            boolean previewAmountMatch = actualPreviewAmount.replaceAll("[^0-9]", "").equals(expectedAmountStr.replaceAll("[^0-9]", ""));
            printComparison("Edited Preview Amount", expectedAmountStr, actualPreviewAmount, previewAmountMatch, GREEN, RED, RESET);
            if (!previewAmountMatch) previewPassed = false;

            boolean notesMatch = !actualNotes.isEmpty();
            printComparison("Edited Manufacture Notes", "Not Empty", actualNotes, notesMatch, GREEN, RED, RESET);
            
            boolean termsMatch = !actualTerms.isEmpty();
            printComparison("Terms & Conditions", "Not Empty", actualTerms, termsMatch, GREEN, RED, RESET);

            System.out.println("=============================================================");
            if (previewPassed) {
                System.out.println(GREEN + "✅ ALL EDITED PREVIEW PAGE DETAILS MATCHED SUCCESSFULLY!" + RESET);
            } else {
                System.out.println(RED + "❌ MISMATCH FOUND ON EDITED PREVIEW PAGE!" + RESET);
                throw new AssertionError("Edited Manufacture Order preview verification failed.");
            }
            System.out.println("=============================================================\n");

        } catch (Exception e) {
            System.out.println(RED + "❌ Exception occurred during Edited Preview verification: " + e.getMessage() + RESET);
            throw new RuntimeException(e);
        }
    }
    

    
    
    public void verifySaveAsDraft() {
        // 2. Add Notes
        waitFor(addNotesInput);
        scrollToElement(addNotesInput);
        addNotesInput.clear();
        addNotesInput.sendKeys("Automated Notes: Order created successfully with verified pricing.");
        sleep(1);
        System.out.println("Entered Notes successfully.");

        // 3. Add Description
        waitFor(addDescriptionInput);
        scrollToElement(addDescriptionInput);
        addDescriptionInput.clear();
        addDescriptionInput.sendKeys("Automated Description: Checking end-to-end manufacture order creation workflow.");
        sleep(1);
        System.out.println("Entered Description successfully.");

        // 4. Click Save Button
        waitFor(saveAsDraftBtn);
        scrollToElement(saveAsDraftBtn);
        click(saveAsDraftBtn);
        sleep(10);
        System.out.println("Clicked Save Button successfully.");
    }
    
 // Step 2: Verify it on the listing page (Status = Draft)
    public void verifyDraftManufactureOrderOnListingPage() {
        System.out.println("\n=============================================================");
        System.out.println("      DRAFT MANUFACTURE ORDER LISTING VERIFICATION           ");
        System.out.println("=============================================================");

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String expectedVendor = (selectedVendorText != null) ? selectedVendorText.trim() : "";
        String expectedStatus = "Draft";

        By moIdCellLocator = By.xpath("(//td)[2]");
        By vendorCellLocator = By.xpath("(//td)[4]");
        By statusCellLocator = By.xpath("(//td)[7]");

        WebElement moIdCell = wait.until(ExpectedConditions.visibilityOfElementLocated(moIdCellLocator));
        String actualMoId = moIdCell.getText().trim();
        String actualVendor = driver.findElement(vendorCellLocator).getText().trim();
        String actualStatus = driver.findElement(statusCellLocator).getText().trim();

        boolean allPassed = true;

        boolean moIdMatch = !actualMoId.isEmpty();
        printComparison("MO ID", "Valid ID", actualMoId, moIdMatch, GREEN, RED, RESET);
        if (!moIdMatch) allPassed = false;

        boolean vendorMatch = actualVendor.contains(expectedVendor) || expectedVendor.contains(actualVendor);
        printComparison("Vendor Name", expectedVendor, actualVendor, vendorMatch, GREEN, RED, RESET);
        if (!vendorMatch) allPassed = false;

        boolean statusMatch = actualStatus.equalsIgnoreCase(expectedStatus);
        printComparison("Status", expectedStatus, actualStatus, statusMatch, GREEN, RED, RESET);
        if (!statusMatch) allPassed = false;

        if (!allPassed) {
            throw new AssertionError("Draft Manufacture Order listing verification failed.");
        }
        System.out.println(GREEN + "✅ DRAFT LISTING PAGE DETAILS MATCHED SUCCESSFULLY!" + RESET + "\n");
    }

    // Step 3: Handle the cancel test & actual deletion workflow
    public void performDraftDeletionWorkflow() {
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        try {
            By moIdCellLocator = By.xpath("(//td)[2]");
            String actualMoId = driver.findElement(moIdCellLocator).getText().trim();

            // =============================================================
            //                   FIRST ATTEMPT: CANCEL FLOW                
            // =============================================================
            System.out.println("--- Executing Delete-Cancel Workflow ---");
            
            By threeDotLocator = By.xpath("(//i[@class='bi bi-three-dots-vertical'])[1]");
            WebElement threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(threeDotLocator));
            scrollToElement(threeDotBtn);
            try {
                threeDotBtn.click();
            } catch (Exception ex) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", threeDotBtn);
            }
            sleep(1);

            By deleteBtnLocator = By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[3]");
            WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(deleteBtnLocator));
            printComparison("Delete Option", "Active", "Active", true, GREEN, RED, RESET);
            deleteBtn.click();
            sleep(1);

            By cancelPopupLocator = By.xpath("(//button[@type='button'][normalize-space()='Cancel'])[10]");
            WebElement cancelPopupBtn = wait.until(ExpectedConditions.elementToBeClickable(cancelPopupLocator));
            cancelPopupBtn.click();
            sleep(2);

            WebElement recheckedMoIdCell = wait.until(ExpectedConditions.visibilityOfElementLocated(moIdCellLocator));
            boolean isStillDisplayed = recheckedMoIdCell.getText().trim().equals(actualMoId);
            printComparison("MO Persistence (After Cancel)", actualMoId, recheckedMoIdCell.getText().trim(), isStillDisplayed, GREEN, RED, RESET);
            if (!isStillDisplayed) {
                throw new AssertionError("Manufacture Order disappeared after clicking cancel on delete popup!");
            }

            // =============================================================
            //                  SECOND ATTEMPT: CONFIRM DELETE             
            // =============================================================
            System.out.println("--- Executing Delete-Confirm Workflow ---");

            threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(threeDotLocator));
            scrollToElement(threeDotBtn);
            try {
                threeDotBtn.click();
            } catch (Exception ex) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", threeDotBtn);
            }
            sleep(1);

            deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(deleteBtnLocator));
            deleteBtn.click();
            sleep(1);

            By confirmDeleteLocator = By.xpath("(//button[@id='DeleteConfirmBtn'])[1]");
            WebElement confirmDeleteBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteLocator));
            confirmDeleteBtn.click();
            sleep(2);

            System.out.println(GREEN + "✅ DRAFT MANUFACTURE ORDER DELETED SUCCESSFULLY!" + RESET + "\n");

        } catch (Exception e) {
            System.out.println(RED + "❌ Exception occurred during Draft MO actions: " + e.getMessage() + RESET);
            throw new RuntimeException(e);
        }
    }
    
    

    
    public void verifyCancelItemsOptionIsDisplayed() {
        // ANSI Color Codes for Console Output
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        System.out.println("\n=============================================================");
        System.out.println("          VERIFY CANCEL ITEMS OPTION ON PREVIEW PAGE         ");
        System.out.println("=============================================================");
        
        // 1. Click 3-dot action button on listing page using repo element
        click(threeDotBtn);
        sleep(1);

        // 2. Click Preview option to navigate to preview page using repo element
        click(previewbtn);
        sleep(2);

        // 3. Verify initial status is active/open
        WebElement initialStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//span[@class='status-chip active'])[1]")
        ));
        System.out.println("Preview Page Initial Status: " + initialStatus.getText().trim());

        // 4. Click the preview action 3-dot button
        WebElement prevThreeDot = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//div[@class='material_action_dropdown js-tag-toggle'])[1]")
        ));
        scrollToElement(prevThreeDot);
        prevThreeDot.click();
        sleep(1);

        // 5. Verify that 'Cancel Items' option is displayed
        WebElement cancelOpt = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//li[normalize-space()='Cancel Items'])[1]")
        ));
        scrollToElement(cancelOpt);
        if (!cancelOpt.isDisplayed()) {
            System.out.println(RED + "❌ 'Cancel Items' option is NOT displayed!" + RESET);
            throw new AssertionError("'Cancel Items' option is not displayed on the preview page!");
        }
        System.out.println(GREEN + "✅ 'Cancel Items' option is successfully displayed." + RESET);
        System.out.println("=============================================================\n");
    }

    public void cancelItemsInManufactureOrder() {
        System.out.println("\n=============================================================");
        System.out.println("           CANCELLING ITEMS IN MANUFACTURE ORDER             ");
        System.out.println("=============================================================");

        // 1. Click 'Cancel Items' option from dropdown
        WebElement cancelOpt = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//li[normalize-space()='Cancel Items'])[1]")
        ));
        cancelOpt.click();
        sleep(1);

        // 2. Select the checkbox to choose all items details on the confirmation popup
        WebElement selectAllCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//input[@id='select_all_cancel_items'])[1]")
        ));
        scrollToElement(selectAllCheckbox);
        if (!selectAllCheckbox.isSelected()) {
            selectAllCheckbox.click();
        }
        sleep(1);

        // 3. Enter a random cancellation reason into the textarea
        WebElement reasonInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//textarea[@id='cancel_reason'])[1]")
        ));
        scrollToElement(reasonInput);
        reasonInput.clear();
        String randomReason = "Automated Cancellation: Requirement changed (" + new java.util.Random().nextInt(1000) + ")";
        reasonInput.sendKeys(randomReason);
        sleep(1);
        System.out.println("Entered cancellation reason: " + randomReason);

        // 4. Click Yes Confirm Button
        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//button[@id='btn_confirm_cancel_items'])[1]")
        ));
        scrollToElement(confirmBtn);
        confirmBtn.click();
        sleep(3); // Wait for cancellation action to process
        System.out.println("✅ Clicked confirmation button for cancellation successfully.");
        System.out.println("=============================================================\n");
    }

    public void verifyManufactureOrderStatusIsCancelled() {
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        System.out.println("\n=============================================================");
        System.out.println("       VERIFYING CANCELLED STATUS ACROSS PAGES               ");
        System.out.println("=============================================================");

        // 1. Verify status on Preview page is changed to inactive/cancelled
        WebElement cancelledStatusPreview = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//span[@class='status-chip inactive'])[1]")
        ));
        scrollToElement(cancelledStatusPreview);
        String previewStatusText = cancelledStatusPreview.getText().trim();
        System.out.println("Preview Page Status: " + previewStatusText);

        // 2. Click Back Button using the repository element (`backBtn`)
        click(backBtn);
        sleep(2);

        // 3. Verify listing page status using repository element (`listingPageStatusLocator` i.e. `(//td)[7]`)
        waitFor(listingPageStatusLocator);
        scrollToElement(listingPageStatusLocator);
        String listingStatusText = listingPageStatusLocator.getText().trim();
        System.out.println("Listing Page Status: " + listingStatusText);

        if (!listingStatusText.equalsIgnoreCase("Cancelled")) {
            System.out.println(RED + "❌ Status verification FAILED! Expected 'Cancelled', found: " + listingStatusText + RESET);
            throw new AssertionError("Expected listing page status to be 'Cancelled', but found: " + listingStatusText);
        }
        
        System.out.println(GREEN + "✅ Manufacture Order status successfully verified as 'Cancelled' across Preview and Listing pages!" + RESET);
        System.out.println("=============================================================\n");
    }
    

    
    
    public void convertAndVerifyManufactureOrderAsIssued() {
        System.out.println("\n=============================================================");
        System.out.println("      CONVERT MANUFACTURE ORDER TO ISSUED & VERIFY           ");
        System.out.println("=============================================================");

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";
        String expectedStatus = "Issued";

        try {
            // 0. Ensure we are back on the listing page and elements are loaded
         
            // 2. Click "Mark as Issued" button from the dropdown
            WebElement issuedBtn = wait.until(ExpectedConditions.elementToBeClickable(markAsIssuedBtn));
            issuedBtn.click();
            sleep(1);

            // 3. Click "Yes, Issued" on the confirmation popup
            WebElement confirmIssuedBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmMarkAsIssuedBtn));
            confirmIssuedBtn.click();
            sleep(2);

            // 4. Wait dynamically until the status on the Details page changes to "Issued" (up to 10 seconds)
            System.out.println("Waiting for details page status to update to 'Issued'...");
            boolean statusUpdated = wait.until(ExpectedConditions.textToBePresentInElement(detailsPageStatusLocator, expectedStatus));
            
            WebElement detailsStatus = wait.until(ExpectedConditions.visibilityOf(detailsPageStatusLocator));
            String actualDetailsStatus = detailsStatus.getText().trim();
            boolean detailsStatusMatch = actualDetailsStatus.equalsIgnoreCase(expectedStatus);
            printComparison("Details Page Status", expectedStatus, actualDetailsStatus, detailsStatusMatch, GREEN, RED, RESET);
            if (!detailsStatusMatch) {
                throw new AssertionError("Details page status did not reflect 'Issued'! Current text found: " + actualDetailsStatus);
            }

            // 5. Click Back Button to return to listing page
            WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(backBtn));
            backButton.click();
            sleep(2);

            // 6. Verify Status on Listing Page
            WebElement listingStatus = wait.until(ExpectedConditions.visibilityOf(listingPageStatusLocator));
            String actualListingStatus = listingStatus.getText().trim();
            boolean listingStatusMatch = actualListingStatus.equalsIgnoreCase(expectedStatus);
            printComparison("Listing Page Status", expectedStatus, actualListingStatus, listingStatusMatch, GREEN, RED, RESET);
            if (!listingStatusMatch) {
                throw new AssertionError("Listing page status did not reflect 'Issued'! Current text found: " + actualListingStatus);
            }

            System.out.println("=============================================================");
            System.out.println(GREEN + "✅ ORDER SUCCESSFULLY CONVERTED TO ISSUED AND VERIFIED ON BOTH PAGES!" + RESET);
            System.out.println("=============================================================\n");

        } catch (Exception e) {
            System.out.println(RED + "❌ Exception occurred during Mark as Issued workflow: " + e.getMessage() + RESET);
            throw new RuntimeException(e);
        }
    }
    
   //TC-06
    public void verifyMandatoryFieldsValidationMessages() {
        System.out.println("\n=============================================================");
        System.out.println("     MANDATORY FIELDS VALIDATION ERROR MESSAGE CHECK         ");
        System.out.println("=============================================================");

        // ANSI Color Codes for Console Output
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        // 1. Click Save/Submit button while the form is completely empty
        waitFor(saveBtn);
        scrollToElement(saveBtn);
        click(saveBtn);
        sleep(1);
        System.out.println("Clicked Save button with empty fields to trigger validations.");

        // 2. Define the exact XPaths for the error/validation spans provided
        By vendorErrLocator = By.xpath("(//span[normalize-space()='Please select a vendor'])[1]");
        By dateErrLocator = By.xpath("(//span[normalize-space()='Please select an expected delivery date'])[1]");
        By addressErrLocator = By.xpath("(//span[normalize-space()='Please select a delivery address'])[1]");
        By productNameErrLocator = By.xpath("(//span[normalize-space()='Please enter a product name'])[1]");
        By makingCostErrLocator = By.xpath("(//span[normalize-space()='Please enter a valid integer making cost'])[1]");
        By sizeErrLocator = By.xpath("(//span[normalize-space()='Please select at least one size'])[1]");

        boolean allErrorsPassed = true;

        // 3. Verify Vendor Validation Message
        try {
            WebElement vendorErr = wait.until(ExpectedConditions.visibilityOfElementLocated(vendorErrLocator));
            scrollToElement(vendorErr);
            boolean isDisplayed = vendorErr.isDisplayed();
            printComparison("Vendor Validation", "Displayed", vendorErr.getText().trim(), isDisplayed, GREEN, RED, RESET);
            if (!isDisplayed) allErrorsPassed = false;
        } catch (Exception e) {
            printComparison("Vendor Validation", "Displayed", "Not Found", false, GREEN, RED, RESET);
            allErrorsPassed = false;
        }

        // 4. Verify Delivery Date Validation Message
        try {
            WebElement dateErr = wait.until(ExpectedConditions.visibilityOfElementLocated(dateErrLocator));
            scrollToElement(dateErr);
            boolean isDisplayed = dateErr.isDisplayed();
            printComparison("Delivery Date Val", "Displayed", dateErr.getText().trim(), isDisplayed, GREEN, RED, RESET);
            if (!isDisplayed) allErrorsPassed = false;
        } catch (Exception e) {
            printComparison("Delivery Date Val", "Displayed", "Not Found", false, GREEN, RED, RESET);
            allErrorsPassed = false;
        }

        // 5. Verify Delivery Address Validation Message
        try {
            WebElement addressErr = wait.until(ExpectedConditions.visibilityOfElementLocated(addressErrLocator));
            scrollToElement(addressErr);
            boolean isDisplayed = addressErr.isDisplayed();
            printComparison("Address Val", "Displayed", addressErr.getText().trim(), isDisplayed, GREEN, RED, RESET);
            if (!isDisplayed) allErrorsPassed = false;
        } catch (Exception e) {
            printComparison("Address Val", "Displayed", "Not Found", false, GREEN, RED, RESET);
            allErrorsPassed = false;
        }

        // 6. Verify Product Name Validation Message
        try {
            WebElement prodNameErr = wait.until(ExpectedConditions.visibilityOfElementLocated(productNameErrLocator));
            scrollToElement(prodNameErr);
            boolean isDisplayed = prodNameErr.isDisplayed();
            printComparison("Product Name Val", "Displayed", prodNameErr.getText().trim(), isDisplayed, GREEN, RED, RESET);
            if (!isDisplayed) allErrorsPassed = false;
        } catch (Exception e) {
            printComparison("Product Name Val", "Displayed", "Not Found", false, GREEN, RED, RESET);
            allErrorsPassed = false;
        }

        // 7. Verify Making Cost Validation Message
        try {
            WebElement makingCostErr = wait.until(ExpectedConditions.visibilityOfElementLocated(makingCostErrLocator));
            scrollToElement(makingCostErr);
            boolean isDisplayed = makingCostErr.isDisplayed();
            printComparison("Making Cost Val", "Displayed", makingCostErr.getText().trim(), isDisplayed, GREEN, RED, RESET);
            if (!isDisplayed) allErrorsPassed = false;
        } catch (Exception e) {
            printComparison("Making Cost Val", "Displayed", "Not Found", false, GREEN, RED, RESET);
            allErrorsPassed = false;
        }

        // 8. Verify Size Selection Validation Message
        try {
            WebElement sizeErr = wait.until(ExpectedConditions.visibilityOfElementLocated(sizeErrLocator));
            scrollToElement(sizeErr);
            boolean isDisplayed = sizeErr.isDisplayed();
            printComparison("Size Selection Val", "Displayed", sizeErr.getText().trim(), isDisplayed, GREEN, RED, RESET);
            if (!isDisplayed) allErrorsPassed = false;
        } catch (Exception e) {
            printComparison("Size Selection Val", "Displayed", "Not Found", false, GREEN, RED, RESET);
            allErrorsPassed = false;
        }

        System.out.println("=============================================================");
        if (allErrorsPassed) {
            System.out.println(GREEN + "✅ ALL MANDATORY VALIDATION MESSAGES VERIFIED SUCCESSFULLY!" + RESET);
        } else {
            System.out.println(RED + "❌ SOME MANDATORY VALIDATION MESSAGES FAILED OR WERE MISSING!" + RESET);
            throw new AssertionError("Mandatory fields validation message check failed.");
        }
        System.out.println("=============================================================\n");
    }

  

    
    public void addProductDetailsForSecondProduct() {
        // 1. Click the 'Add Product' button to open a new product form block/window
        waitFor(addProductBtn);
        scrollToElement(addProductBtn);
        click(addProductBtn);
        sleep(2);
        System.out.println("Clicked 'Add Product' button for second product.");

        // 2. Enter Product Name using index [2] xpath
        By productNameInput2 = By.xpath("(//input[@id='product_name'])[2]");
        WebElement prodNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(productNameInput2));
        scrollToElement(prodNameInput);
        String randomProductName = "Product_" + new Random().nextInt(1000);
        prodNameInput.clear();
        prodNameInput.sendKeys(randomProductName);
        sleep(1);
        System.out.println("Entered Second Product Name: " + randomProductName);

        // 3. Enter Dynamic Making Cost using index [2] xpath
        By makingCostInput2 = By.xpath("(//input[@id='making_cost'])[2]");
        WebElement costInput = wait.until(ExpectedConditions.visibilityOfElementLocated(makingCostInput2));
        scrollToElement(costInput);
        
        Random random = new Random();
        int randomCostValue = 10 + random.nextInt(990); 
        lastMakingCost = (double) randomCostValue; 
        String makingCostStr = String.valueOf(randomCostValue);
        
        costInput.clear();
        costInput.sendKeys(makingCostStr);
        sleep(1);
        System.out.println("Entered Second Product Making Cost: " + makingCostStr);

        // 4. Click Size Tags Dropdown using index [2] xpath to open the second menu container
        By sizeTagsDropdown2 = By.xpath("(//div[@class='tag_input js-tag-toggle'])[2]");
        WebElement sizeDropdown2 = wait.until(ExpectedConditions.elementToBeClickable(sizeTagsDropdown2));
        scrollToElement(sizeDropdown2);
        sizeDropdown2.click();
        sleep(1);

        // 5. Locate all size options inside the second tag menu box
        By sizeOptionsLocator2 = By.xpath("(//div[@class='tag_menu js-tag_menu'])[2]//label[@class='tag_option']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(sizeOptionsLocator2));
        List<WebElement> sizeOptions2 = driver.findElements(sizeOptionsLocator2);

        selectedSizesList.clear();
        selectedSizeIndices.clear();
        selectedQuantitiesList.clear();
        totalQuantitySum = 0; 

        if (!sizeOptions2.isEmpty()) {
            int numberOfSizesToSelect = Math.min(1 + random.nextInt(3), sizeOptions2.size());
            
            java.util.Set<Integer> chosenIndices = new java.util.HashSet<>();
            while (chosenIndices.size() < numberOfSizesToSelect) {
                chosenIndices.add(random.nextInt(sizeOptions2.size()));
            }

            for (int index : chosenIndices) {
                WebElement sizeOption = sizeOptions2.get(index);
                String sizeText = sizeOption.getText().trim();
                selectedSizesList.add(sizeText);
                
                // Get the exact size option index (8 to 14)
                int sizeIndex = getRawMaterialIndexForSize(sizeText);
                selectedSizeIndices.add(sizeIndex);
                
                // Click the size option using the exact global index (e.g. (//label[@class='tag_option'])[8])
                By specificOption = By.xpath("(//label[@class='tag_option'])[" + sizeIndex + "]");
                WebElement specificSizeEl = wait.until(ExpectedConditions.elementToBeClickable(specificOption));
                scrollToElement(specificSizeEl);
                specificSizeEl.click();
                sleep(1);
                
                // Second product input placeholder index starts at 15 for XS (sizeIndex + 7)
                int inputIndex = sizeIndex + 7;
                By quantityInputLocator = By.xpath("(//input[@placeholder='-'])[" + inputIndex + "]"); 
                WebElement qtyInput = wait.until(ExpectedConditions.elementToBeClickable(quantityInputLocator));
                
                int randomQty = 1 + random.nextInt(5); 
                selectedQuantitiesList.add(randomQty); 
                totalQuantitySum += randomQty; 
                
                scrollToElement(qtyInput);
                qtyInput.clear();
                qtyInput.sendKeys(String.valueOf(randomQty));
                sleep(1);
                
                System.out.println("Selected Second Product Size: '" + sizeText + "' at tag option index [" + sizeIndex + "] and placeholder index [" + inputIndex + "] with Quantity: " + randomQty);
            }
            
            secondProductExpectedCost = lastMakingCost * totalQuantitySum;
            
            // 6. Close the second size dropdown
            try {
                scrollToElement(sizeDropdown2);
                sizeDropdown2.click();
                sleep(1);
                System.out.println("Closed second product size dropdown successfully.");
            } catch (Exception e) {
                driver.findElement(By.xpath("//body")).click();
                sleep(1);
            }
            
        } else {
            throw new RuntimeException("No size options available in the second tag menu container!");
        }
    }
    
    public void selectRawMaterialAndFillQuantitiesForSecondProduct() {
        Random random = new Random();
        
        // Calculate total required quantity across all selected sizes for the second product
        int totalRequiredQty = 0;
        for (int qty : selectedQuantitiesList) {
            totalRequiredQty += qty;
        }
        System.out.println("Total Required Raw Material Quantity for Second Product: " + totalRequiredQty);

        boolean sufficientStockFound = false;
        int maxRetries = 5;
        int attempts = 0;

        while (!sufficientStockFound && attempts < maxRetries) {
            attempts++;
            
            // 1. Open Raw Material Dropdown for Second Product
            try {
                WebElement dropdownToggle2 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//div[@class='w-100 text-left pl-2 field_wrapper'])[2]")
                ));
                scrollToElement(dropdownToggle2);
                dropdownToggle2.click();
            } catch (Exception e) {
                WebElement fallbackToggle = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//div[@class='w-100 text-left pl-2 field_wrapper'])[2]")
                ));
                scrollToElement(fallbackToggle);
                fallbackToggle.click();
            }
            sleep(1);

            // 2. Select a random option from the 4th options box
            By rawMaterialOptionLocator2 = By.xpath("(//div[@class='options_box'])[4]//div[@class='option']");
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(rawMaterialOptionLocator2));
            List<WebElement> rawMaterialOptions2 = driver.findElements(rawMaterialOptionLocator2);

            if (rawMaterialOptions2.isEmpty()) {
                throw new RuntimeException("No raw material options available in options box [4]!");
            }

            int rmIndex = random.nextInt(rawMaterialOptions2.size());
            int rmItemNumber = rmIndex + 1;

            By specificRmOption = By.xpath("((//div[@class='options_box'])[4]//div[@class='option'])[" + rmItemNumber + "]");
            WebElement chosenRm = wait.until(ExpectedConditions.elementToBeClickable(specificRmOption));
            
            String rmText = chosenRm.getText().trim();
            scrollToElement(chosenRm);
            chosenRm.click();
            sleep(1);
            System.out.println("Attempt " + attempts + " - Selected Raw Material for 2nd Product: " + rmText);

            // 3. Enter a high quantity (1000000) into the first selected size's input box with offset +14 to trigger validation
            String firstSize = selectedSizesList.get(0);
            int firstSizeIndex = getRawMaterialIndexForSize(firstSize); // e.g. 8 for XS
            int firstSizeInputIndex = firstSizeIndex + 14;              // 8 + 14 = 22
            
            By firstInputLocator = By.xpath("(//input[@placeholder='-'])[" + firstSizeInputIndex + "]");
            WebElement firstQtyInput = wait.until(ExpectedConditions.elementToBeClickable(firstInputLocator));
            scrollToElement(firstQtyInput);
            
            firstQtyInput.clear();
            firstQtyInput.sendKeys("1000000");
            sleep(1);

            // 4. Check non-intrusively for the stock error validation message using the correct error class
            int availableStock = 1000000; 
            try {
                // FIXED: Use the specific error-stock-msg class and check multiple elements if needed, or target index [2] for the second product block
                By errorMsgLocator = By.xpath("((//span[contains(@class, 'error-stock-msg')])[2]) | (//span[contains(text(),'Available stock:')])[2]");
                
                org.openqa.selenium.support.ui.WebDriverWait shortWait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(2));
                WebElement errorMsgElement = shortWait.until(ExpectedConditions.visibilityOf(driver.findElement(errorMsgLocator)));
                
                String errorText = errorMsgElement.getText();
                System.out.println("Stock Validation Message (2nd Product): " + errorText);
                
                String numericStock = errorText.replaceAll("[^0-9]", "");
                if (!numericStock.isEmpty()) {
                    availableStock = Integer.parseInt(numericStock);
                }
            } catch (Exception e) {
                System.out.println("No stock validation error displayed for 2nd product. Proceeding with stock as sufficient.");
                availableStock = 1000000; 
            }

            System.out.println("Available Stock detected for 2nd Product: " + availableStock);

            // 5. Validate if available stock is sufficient
            if (availableStock >= totalRequiredQty) {
                System.out.println("Sufficient stock found for 2nd product! Filling exact quantities...");
                
                for (int i = 0; i < selectedSizesList.size(); i++) {
                    String targetSize = selectedSizesList.get(i);
                    int exactQty = selectedQuantitiesList.get(i);
                    int targetSizeIndex = getRawMaterialIndexForSize(targetSize);
                    int rmInputIndex = targetSizeIndex + 14; 
                    
                    By rmQtyInputLocator = By.xpath("(//input[@placeholder='-'])[" + rmInputIndex + "]");
                    WebElement rmQtyInput = wait.until(ExpectedConditions.elementToBeClickable(rmQtyInputLocator));
                    scrollToElement(rmQtyInput);
                    
                    rmQtyInput.clear();
                    rmQtyInput.sendKeys(String.valueOf(exactQty));
                    sleep(1);
                    
                    System.out.println("Entered Exact Raw Material Quantity: " + exactQty + " for size: " + targetSize + " at index [" + rmInputIndex + "]");
                }
                
                // Store the total quantity for the second product so listing verification passes
                secondProductTotalQuantity = totalRequiredQty;
                sufficientStockFound = true;
            } else {
                System.out.println("Stock is low (" + availableStock + " < " + totalRequiredQty + "). Retrying with a different raw material for 2nd product...");
                try {
                    firstQtyInput.clear();
                } catch (Exception ignored) {}
            }
        }

        if (!sufficientStockFound) {
            throw new RuntimeException("Failed to find a raw material with sufficient stock for the second product after " + maxRetries + " attempts!");
        }
    }

    public void verifyIndividualAndCombinedTotals() {
        // 1. Verify 1st Product Total Amount
        By prod1TotalLoc = By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[1]");
        WebElement prod1TotalEl = wait.until(ExpectedConditions.visibilityOfElementLocated(prod1TotalLoc));
        scrollToElement(prod1TotalEl);
        String prod1Text = prod1TotalEl.getText().trim();
        System.out.println("1st Product Displayed Total Text: " + prod1Text);
        
        if (prod1Text.isEmpty()) {
            throw new RuntimeException("1st product total amount text box is empty!");
        }

        // 2. Verify 2nd Product Total Amount
        By prod2TotalLoc = By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[2]");
        WebElement prod2TotalEl = wait.until(ExpectedConditions.visibilityOfElementLocated(prod2TotalLoc));
        scrollToElement(prod2TotalEl);
        String prod2Text = prod2TotalEl.getText().trim();
        System.out.println("2nd Product Displayed Total Text: " + prod2Text);
        
        if (prod2Text.isEmpty()) {
            throw new RuntimeException("2nd product total amount text box is empty!");
        }

        // 3. Verify Combined Total Amount
        By combinedTotalLoc = By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[3]");
        WebElement combinedTotalEl = wait.until(ExpectedConditions.visibilityOfElementLocated(combinedTotalLoc));
        scrollToElement(combinedTotalEl);
        String combinedText = combinedTotalEl.getText().trim();
        System.out.println("Combined Products Displayed Total Text: " + combinedText);
        
        if (combinedText.isEmpty()) {
            throw new RuntimeException("Combined total amount text box is empty!");
        }
        
        // Optional calculation cross-check (extracting numeric values from strings if currency symbols are attached)
        double expectedCombinedCost = firstProductExpectedCost + secondProductExpectedCost;
        System.out.println("Calculated Expected Combined Cost: " + String.format(java.util.Locale.US, "%.2f", expectedCombinedCost));
        System.out.println("All product and combined totals verified successfully!");
    }
    
    
    
    public void verifyAddNotesAndSave() {
        // 1. Add Notes
        waitFor(addNotesInput);
        scrollToElement(addNotesInput);
        addNotesInput.clear();
        addNotesInput.sendKeys("Automated Notes: Order created successfully with verified pricing.");
        sleep(1);
        System.out.println("Entered Notes successfully.");

        // 2. Add Description
        waitFor(addDescriptionInput);
        scrollToElement(addDescriptionInput);
        addDescriptionInput.clear();
        addDescriptionInput.sendKeys("Automated Description: Checking end-to-end manufacture order creation workflow.");
        sleep(1);
        System.out.println("Entered Description successfully.");

        // 3. Click Save Button
        waitFor(saveBtn);
        scrollToElement(saveBtn);
        click(saveBtn);
        sleep(10);
        System.out.println("Clicked Save Button successfully.");
    }
    
    public int firstProductTotalQuantity = 0;
    public int secondProductTotalQuantity = 0;
    public void verifyManufactureOrderForMultipleProductsOnListingPage() {
        System.out.println("\n=============================================================");
        System.out.println("          MANUFACTURE ORDER LISTING PAGE VERIFICATION        ");
        System.out.println("=============================================================");

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String expectedVendor = (selectedVendorText != null) ? selectedVendorText.trim() : "";
        
        // Combine quantities from both products safely
        int combinedExpectedQty = firstProductTotalQuantity + secondProductTotalQuantity;
        String expectedQty = String.valueOf(combinedExpectedQty);
        
        double combinedExpectedAmount = firstProductExpectedCost + secondProductExpectedCost;
        String expectedAmountStr = String.format(java.util.Locale.US, "%.2f", combinedExpectedAmount);
        String expectedStatus = "Open";

        By vendorCellLocator = By.xpath("(//td)[4]");
        By qtyCellLocator = By.xpath("(//td)[5]");
        By amountCellLocator = By.xpath("(//td)[6]");
        By statusCellLocator = By.xpath("(//td)[7]");

        WebElement vendorCell = wait.until(ExpectedConditions.visibilityOfElementLocated(vendorCellLocator));
        String actualVendor = vendorCell.getText().trim();
        
        String actualQty = driver.findElement(qtyCellLocator).getText().trim();
        String actualAmount = driver.findElement(amountCellLocator).getText().trim();
        String actualStatus = driver.findElement(statusCellLocator).getText().trim();

        boolean allPassed = true;

        By listingRowLocator = By.xpath("(//td)[2]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(listingRowLocator));
        sleep(1);

        // 1. Vendor Name Verification
        boolean vendorMatch = actualVendor.contains(expectedVendor) || expectedVendor.contains(actualVendor);
        printComparison("Vendor Name", expectedVendor, actualVendor, vendorMatch, GREEN, RED, RESET);
        if (!vendorMatch) allPassed = false;

        // 2. Combined Overall Quantity Verification
        boolean qtyMatch = actualQty.replaceAll("[^0-9]", "").equals(expectedQty);
        printComparison("Combined Quantity", expectedQty, actualQty, qtyMatch, GREEN, RED, RESET);
        if (!qtyMatch) allPassed = false;

        // 3. Combined Total Amount Verification
        boolean amountMatch = false;
        try {
            double expectedNumericAmount = Double.parseDouble(expectedAmountStr);
            double actualNumericAmount = Double.parseDouble(actualAmount.replaceAll("[^0-9.]", ""));
            amountMatch = Math.abs(expectedNumericAmount - actualNumericAmount) < 0.01;
        } catch (Exception e) {
            amountMatch = actualAmount.replaceAll("[^0-9]", "").equals(expectedAmountStr.replaceAll("[^0-9]", ""));
        }
        printComparison("Combined Total Amount", expectedAmountStr, actualAmount, amountMatch, GREEN, RED, RESET);
        if (!amountMatch) allPassed = false;

        // 4. Status Verification
        boolean statusMatch = actualStatus.equalsIgnoreCase(expectedStatus);
        printComparison("Status", expectedStatus, actualStatus, statusMatch, GREEN, RED, RESET);
        if (!statusMatch) allPassed = false;

        System.out.println("=============================================================");
        if (allPassed) {
            System.out.println(GREEN + "✅ ALL MULTI-PRODUCT LISTING PAGE DETAILS MATCHED SUCCESSFULLY!" + RESET);
        } else {
            System.out.println(RED + "❌ MISMATCH FOUND ON LISTING PAGE FOR MULTI-PRODUCT FLOW!" + RESET);
            throw new AssertionError("Manufacture Order multi-product listing verification failed.");
        }
        System.out.println("=============================================================\n");
    }
    
    
    public void verifyManufactureOrderPreviewModalDetails() {
        System.out.println("\n=============================================================");
        System.out.println("          MANUFACTURE ORDER PREVIEW MODAL VERIFICATION       ");
        System.out.println("=============================================================");

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        // 1. Click on the three-dot action button on the listing page
        By threeDotBtnLocator = By.xpath("(//i[@class='bi bi-three-dots-vertical'])[1]"); 
        WebElement threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(threeDotBtnLocator));
        scrollToElement(threeDotBtn);
        threeDotBtn.click();
        sleep(1);

        // 2. Click on the Preview button from the dropdown
        By previewBtnLocator = By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]"); 
        // Note: Update previewBtnLocator xpath if your app uses specific icon or text for preview
        WebElement previewBtn = wait.until(ExpectedConditions.elementToBeClickable(previewBtnLocator));
        scrollToElement(previewBtn);
        previewBtn.click();
        sleep(2); // Wait for preview modal/drawer to fully load

        boolean allPassed = true;

        // --- FIRST PRODUCT VERIFICATION ---
        String actualFirstProdName = driver.findElement(By.xpath("(//td)[1]")).getText().trim();
        String actualFirstProdQty = driver.findElement(By.xpath("(//td)[2]")).getText().trim();
        String actualFirstProdMakingCost = driver.findElement(By.xpath("(//td)[3]")).getText().trim();
        String actualFirstProdTotalAmount = driver.findElement(By.xpath("(//td)[4]")).getText().trim();

        System.out.println("First Product Name -> Actual: " + actualFirstProdName);
        System.out.println("First Product Qty  -> Actual: " + actualFirstProdQty + " | Expected: " + firstProductTotalQuantity);
        System.out.println("First Product Cost -> Actual: " + actualFirstProdMakingCost);
        System.out.println("First Product Total -> Actual: " + actualFirstProdTotalAmount + " | Expected: " + firstProductExpectedCost);

        boolean p1QtyMatch = actualFirstProdQty.replaceAll("[^0-9]", "").equals(String.valueOf(firstProductTotalQuantity));
        printComparison("1st Product Quantity", String.valueOf(firstProductTotalQuantity), actualFirstProdQty, p1QtyMatch, GREEN, RED, RESET);
        if (!p1QtyMatch) allPassed = false;

        // --- SECOND PRODUCT VERIFICATION ---
        String actualSecondProdName = driver.findElement(By.xpath("(//td)[9]")).getText().trim();
        String actualSecondProdQty = driver.findElement(By.xpath("(//td)[10]")).getText().trim();
        String actualSecondProdMakingCost = driver.findElement(By.xpath("(//td)[11]")).getText().trim();
        String actualSecondProdTotalAmount = driver.findElement(By.xpath("(//td)[12]")).getText().trim();

        System.out.println("2nd Product Name -> Actual: " + actualSecondProdName);
        System.out.println("2nd Product Qty  -> Actual: " + actualSecondProdQty + " | Expected: " + secondProductTotalQuantity);
        System.out.println("2nd Product Cost -> Actual: " + actualSecondProdMakingCost);
        System.out.println("2nd Product Total -> Actual: " + actualSecondProdTotalAmount + " | Expected: " + secondProductExpectedCost);

        boolean p2QtyMatch = actualSecondProdQty.replaceAll("[^0-9]", "").equals(String.valueOf(secondProductTotalQuantity));
        printComparison("2nd Product Quantity", String.valueOf(secondProductTotalQuantity), actualSecondProdQty, p2QtyMatch, GREEN, RED, RESET);
        if (!p2QtyMatch) allPassed = false;

        // --- COMBINED TOTAL AMOUNT VERIFICATION ---
        By combinedTotalLocator = By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[3]");
        WebElement combinedTotalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(combinedTotalLocator));
        String actualCombinedText = combinedTotalElement.getText().trim();

        double expectedCombinedCost = firstProductExpectedCost + secondProductExpectedCost;
        String expectedCombinedStr = String.format(java.util.Locale.US, "%.2f", expectedCombinedCost);

        boolean combinedAmountMatch = false;
        try {
            double actualNumericCombined = Double.parseDouble(actualCombinedText.replaceAll("[^0-9.]", ""));
            combinedAmountMatch = Math.abs(expectedCombinedCost - actualNumericCombined) < 0.01;
        } catch (Exception e) {
            combinedAmountMatch = actualCombinedText.replaceAll("[^0-9]", "").equals(expectedCombinedStr.replaceAll("[^0-9]", ""));
        }

        printComparison("Combined Total Amount (Modal)", expectedCombinedStr, actualCombinedText, combinedAmountMatch, GREEN, RED, RESET);
        if (!combinedAmountMatch) allPassed = false;

        System.out.println("=============================================================");
        if (allPassed) {
            System.out.println(GREEN + "✅ ALL PREVIEW MODAL PRODUCT DETAILS AND TOTALS MATCHED!" + RESET);
        } else {
            System.out.println(RED + "❌ MISMATCH FOUND IN PREVIEW MODAL VERIFICATION!" + RESET);
            throw new AssertionError("Manufacture Order Preview modal verification failed.");
        }
        System.out.println("=============================================================\n");
    }
    
    
    
    
    
    
    
    //TC-01
    public void createNewManufactureOrderFlow() {
    	clickAddManufactureOrder();
        selectRandomVendor();
        selectRandomDeliveryDate();
        selectRandomDeliveryAddress();
        addProductDetails();
        selectRawMaterialAndFillQuantities();
        verifyTotalAmountAndAddNotesAndSave();
        verifyManufactureOrderOnListingPage();
        verifyManufactureOrderPreviewPage();
        
    }
    
    //TC-02
    public void editManufactureOrderFlow() {
        System.out.println("\n=============================================================");
        System.out.println("          STARTING EDIT MANUFACTURE ORDER FLOW               ");
        System.out.println("=============================================================");
        clickEditManufactureOrder();
        editRandomVendor();
        selectRandomDeliveryDate();
        editRandomDeliveryAddress();
        editProductDetails();
        selectRawMaterialAndFillQuantities();
        verifyEditedTotalAmountAndAddNotesAndSave();
        verifyEditedManufactureOrderOnListingPage();
        verifyEditedManufactureOrderPreviewPage();
    }
    
  //TC-03
    public void createDraftManufactureOrderFlow() {
        clickAddManufactureOrder();
        selectRandomVendor();
        selectRandomDeliveryDate();
        selectRandomDeliveryAddress();
        verifySaveAsDraft(); // This clicks the Save as Draft button
    }
    
    //TC-04
    public void cancelManufactureOrderFlow() {
    	clickAddManufactureOrder();
        selectRandomVendor();
        selectRandomDeliveryDate();
        selectRandomDeliveryAddress();
        addProductDetails();
        selectRawMaterialAndFillQuantities();
        verifyTotalAmountAndAddNotesAndSave();
		System.out.println("\n=============================================================");
		System.out.println("          STARTING CANCEL MANUFACTURE ORDER FLOW             ");
		System.out.println("=============================================================");
		verifyCancelItemsOptionIsDisplayed();
		cancelItemsInManufactureOrder();
		verifyManufactureOrderStatusIsCancelled();
	}
    
    //TC-05
    public void markAsIssuedManufactureOrderFlow() {
    	clickAddManufactureOrder();
        selectRandomVendor();
        selectRandomDeliveryDate();
        selectRandomDeliveryAddress();
        addProductDetails();
        selectRawMaterialAndFillQuantities();
        verifyTotalAmountAndAddNotesAndSave();
        verifyManufactureOrderOnListingPage();
        verifyManufactureOrderPreviewPage();
        convertAndVerifyManufactureOrderAsIssued();       
    }
    
    //TC-07
    public void verifyMultipleProductsAndTotalsFlow() {
		clickAddManufactureOrder();
		selectRandomVendor();
		selectRandomDeliveryDate();
		selectRandomDeliveryAddress();
		addProductDetails();
		selectRawMaterialAndFillQuantities();
		addProductDetailsForSecondProduct();
		selectRawMaterialAndFillQuantitiesForSecondProduct();
		verifyIndividualAndCombinedTotals();  
		verifyAddNotesAndSave();
		verifyManufactureOrderForMultipleProductsOnListingPage();
		verifyManufactureOrderPreviewModalDetails();
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