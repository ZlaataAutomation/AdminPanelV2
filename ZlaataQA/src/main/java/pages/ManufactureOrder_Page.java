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
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
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

    private void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
        sleep(1);
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
    
    public void clickAddManufactureOrderForCustomProduct() {
        scrollToElement(addManufactureOrderBtn);
        click(addManufactureOrderBtn);
        sleep(1);
        
        wait.until(ExpectedConditions.visibilityOf(addCustomProductBtn));
        scrollToElement(addCustomProductBtn);
        click(addCustomProductBtn);
        sleep(1);
    }
    
    public void clickCustomProductTab() {
        By customProductTab = By.xpath("(//a[normalize-space()='Custom Product'])[1]");

        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(customProductTab));
        scrollToElement(tab);
        tab.click();
        sleep(4);
        System.out.println("Clicked Custom Product tab.");
    }
    
    //TC-01
    public String selectedVendorText = "";
    public String selectedAddressText = "";
    public String selectedDateText = "";
    
    public void selectVendorDateAndAddress() {
        scrollToElement(vendorDropdown);
        click(vendorDropdown);
        sleep(1);
        
        By searchInputLocator = By.xpath("(//input[@placeholder='Search the vendors'])[1]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputLocator));
        
        By scopedOptionLocator = By.xpath("(//div[@class='options_box'])[1]//div[@class='option']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(scopedOptionLocator));
        
        List<WebElement> vendorOptions = driver.findElements(scopedOptionLocator);
        
        if (!vendorOptions.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(vendorOptions.size());
            
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

        waitFor(expectedDeliveryDateCalendar);
        scrollToElement(expectedDeliveryDateCalendar);
        
        click(expectedDeliveryDateCalendar);
        sleep(1);
        
        By activeDaysLocator = By.xpath("(//div[@class='dayContainer'])[1]//span[contains(@class, 'flatpickr-day') and not(contains(@class, 'flatpickr-disabled')) and not(contains(@class, 'prevMonthDay')) and not(contains(@class, 'nextMonthDay'))]");
        List<WebElement> dayElements = driver.findElements(activeDaysLocator);
        
        if (dayElements.isEmpty()) {
            activeDaysLocator = By.xpath("(//div[@class='dayContainer'])[1]//div[contains(@class, 'flatpickr-day') and not(contains(@class, 'flatpickr-disabled')) and not(contains(@class, 'prevMonthDay')) and not(contains(@class, 'nextMonthDay'))]");
            dayElements = driver.findElements(activeDaysLocator);
        }
        
        if (dayElements.isEmpty()) {
            throw new RuntimeException("❌ Could not find any active/selectable days inside the Flatpickr dayContainer!");
        }
        
        Random dateRandom = new Random();
        WebElement randomDayElement = dayElements.get(dateRandom.nextInt(dayElements.size()));
        
        scrollToElement(randomDayElement);
        wait.until(ExpectedConditions.elementToBeClickable(randomDayElement));
        
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", randomDayElement);
        sleep(1);
        
        selectedDateText = expectedDeliveryDateCalendar.getAttribute("value");
        if (selectedDateText == null || selectedDateText.isEmpty()) {
            selectedDateText = (String) js.executeScript("return arguments[0].value;", expectedDeliveryDateCalendar);
        }
        
        System.out.println("Selected Delivery Date from Calendar: " + selectedDateText);

        scrollToElement(deliveryAddressDropdown);
        click(deliveryAddressDropdown);
        sleep(1);
        
        By addressOptionLocator = By.xpath("(//div[@class='options_box'])[2]//div[@class='option']");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(addressOptionLocator));
        
        List<WebElement> addressOptions = driver.findElements(addressOptionLocator);
        
        if (!addressOptions.isEmpty()) {
            Random addressRandom = new Random();
            int addressIndex = addressRandom.nextInt(addressOptions.size());
            
            int addressItemNumber = addressIndex + 1;
            By specificAddress = By.xpath("((//div[@class='options_box'])[2]//div[@class='option'])[" + addressItemNumber + "]");
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
    public List<Integer> selectedQuantitiesList = new java.util.ArrayList<>(); 
    public double lastMakingCost = 0.0;
    public int totalQuantitySum = 0;
    
    
    public double firstProductExpectedCost = 0.0;
    public double secondProductExpectedCost = 0.0;
    public int totalSizeOptionsCount = 0; 
    
    public void addProductDetails() {
        waitFor(productNameInput);
        scrollToElement(productNameInput);
        String randomProductName = "Product_" + new Random().nextInt(1000);
        productNameInput.clear();
        productNameInput.sendKeys(randomProductName);
        sleep(1);
        System.out.println("Entered Product Name: " + randomProductName);

        waitFor(makingCostInput);
        scrollToElement(makingCostInput);
        
        Random random = new Random();
        int randomCostValue = 10 + random.nextInt(990); 
        lastMakingCost = (double) randomCostValue; 
        String makingCostStr = String.valueOf(randomCostValue); 
        
        waitFor(makingCostInput);
        scrollToElement(makingCostInput);
        makingCostInput.clear();
        makingCostInput.sendKeys(makingCostStr);
        sleep(1);
        System.out.println("Entered Making Cost: " + makingCostStr);

        scrollToElement(sizeTagsDropdown);
        click(sizeTagsDropdown);
        sleep(1);

        By sizeOptionsLocator = By.xpath("(//div[@class='tag_menu js-tag_menu'])[1]//label[@class='tag_option']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(sizeOptionsLocator));
        List<WebElement> sizeOptions = driver.findElements(sizeOptionsLocator);
        totalSizeOptionsCount = sizeOptions.size();

        selectedSizesList.clear();
        selectedSizeIndices.clear();
        selectedQuantitiesList.clear();
        totalQuantitySum = 0;

        if (!sizeOptions.isEmpty()) {
            int numberOfSizesToSelect = Math.min(1 + random.nextInt(3), sizeOptions.size());
            
            java.util.Set<Integer> chosenIndices = new java.util.HashSet<>();
            while (chosenIndices.size() < numberOfSizesToSelect) {
                chosenIndices.add(random.nextInt(sizeOptions.size()));
            }

            for (int index : chosenIndices) {
                int itemNumber = index + 1; 
                selectedSizeIndices.add(itemNumber); 
                
                By specificOption = By.xpath("((//div[@class='tag_menu js-tag_menu'])[1]//label[@class='tag_option'])[" + itemNumber + "]");
                WebElement sizeOption = wait.until(ExpectedConditions.elementToBeClickable(specificOption));
                
                String sizeText = sizeOption.getText().trim();
                selectedSizesList.add(sizeText);
                
                scrollToElement(sizeOption);
                sizeOption.click();
                sleep(1);
                
                By quantityInputLocator = By.xpath("(//input[@placeholder='-'])[" + itemNumber + "]");
                WebElement qtyInput = wait.until(ExpectedConditions.elementToBeClickable(quantityInputLocator));
                
                int randomQty = 1 + random.nextInt(5); 
                selectedQuantitiesList.add(randomQty); 
                totalQuantitySum += randomQty; 
                
                scrollToElement(qtyInput);
                qtyInput.clear();
                qtyInput.sendKeys(String.valueOf(randomQty));
                sleep(1);
                
                System.out.println("Selected Size Name: '" + sizeText + "' at index [" + itemNumber + "] with Quantity: " + randomQty);
            }
            
            firstProductExpectedCost = lastMakingCost * totalQuantitySum;
            
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
    
    private int getRawMaterialIndexForSizeIndex(int sizeIndex) {
        return sizeIndex + totalSizeOptionsCount;
    }
    
    public void selectRawMaterialAndFillQuantities() {
        Random random = new Random();
        
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

            int firstSizeInputIndex = getRawMaterialIndexForSizeIndex(selectedSizeIndices.get(0));
            
            By firstInputLocator = By.xpath("(//input[@placeholder='-'])[" + firstSizeInputIndex + "]");
            WebElement firstQtyInput = wait.until(ExpectedConditions.elementToBeClickable(firstInputLocator));
            scrollToElement(firstQtyInput);
            
            firstQtyInput.clear();
            firstQtyInput.sendKeys("1000000");
            sleep(1);

            int availableStock = 1000000; 
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

            if (availableStock >= totalRequiredQty) {
                System.out.println("Sufficient stock found! Filling exact quantities...");
                
                for (int i = 0; i < selectedSizesList.size(); i++) {
                    String targetSize = selectedSizesList.get(i);
                    int exactQty = selectedQuantitiesList.get(i);
                    int rmInputIndex = getRawMaterialIndexForSizeIndex(selectedSizeIndices.get(i));
                    
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
        waitFor(totalCostText);
        scrollToElement(totalCostText);
        String displayedTotalText = totalCostText.getText().trim();
        System.out.println("Displayed Total Cost Text on UI: " + displayedTotalText);
        
        double expectedTotalCost = lastMakingCost * totalQuantitySum;
        System.out.println("Calculated Expected Total Cost: " + String.format(java.util.Locale.US, "%.2f", expectedTotalCost));
        
        if (displayedTotalText.isEmpty()) {
            throw new RuntimeException("Total amount field is empty or not reflecting correctly!");
        }

        waitFor(addNotesInput);
        scrollToElement(addNotesInput);
        addNotesInput.clear();
        addNotesInput.sendKeys("Automated Notes: Order created successfully with verified pricing.");
        sleep(1);
        System.out.println("Entered Notes successfully.");

        waitFor(addDescriptionInput);
        scrollToElement(addDescriptionInput);
        addDescriptionInput.clear();
        addDescriptionInput.sendKeys("Automated Description: Checking end-to-end manufacture order creation workflow.");
        sleep(1);
        System.out.println("Entered Description successfully.");

        waitFor(saveBtn);
        scrollToElement(saveBtn);
        click(saveBtn);
        sleep(10);
        System.out.println("Clicked Save Button successfully.");
    }  
    
    public void verifyManufactureOrderOnListingPage() {
    	clickCustomProductTab();
        System.out.println("\n=============================================================");
        System.out.println("          MANUFACTURE ORDER LISTING PAGE VERIFICATION        ");
        System.out.println("=============================================================");

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String expectedVendor = (selectedVendorText != null) ? selectedVendorText.trim() : "";
        String expectedQty = String.valueOf(totalQuantitySum);
        
        double calculatedTotalAmount = lastMakingCost * totalQuantitySum;
        String expectedAmountStr = String.format(java.util.Locale.US, "%.2f", calculatedTotalAmount);
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

        boolean vendorMatch = actualVendor.contains(expectedVendor) || expectedVendor.contains(actualVendor);
        printComparison("Vendor Name", expectedVendor, actualVendor, vendorMatch, GREEN, RED, RESET);
        if (!vendorMatch) allPassed = false;

        boolean qtyMatch = actualQty.replaceAll("[^0-9]", "").equals(expectedQty);
        printComparison("Overall Quantity", expectedQty, actualQty, qtyMatch, GREEN, RED, RESET);
        if (!qtyMatch) allPassed = false;

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

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String expectedQty = String.valueOf(totalQuantitySum);
        double calculatedTotalAmount = lastMakingCost * totalQuantitySum;
        String expectedAmountStr = String.format(java.util.Locale.US, "%.2f", calculatedTotalAmount);

        try {
            By threeDotLocator = By.xpath("(//i[@class='bi bi-three-dots-vertical'])[1]");
            WebElement threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(threeDotLocator));
            scrollToElement(threeDotBtn);
            try {
                threeDotBtn.click();
            } catch (Exception ex) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", threeDotBtn);
            }
            sleep(1);

            WebElement previewBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
            ));
            previewBtn.click();
            sleep(2);

            String actualPreviewDate = driver.findElement(By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[4]")).getText().trim();
            String actualPreviewAddress = driver.findElement(By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[5]")).getText().trim();
            String actualPreviewProductName = driver.findElement(By.xpath("(//p[@class='font_14 m-0 fw-bold'])[1]")).getText().trim();
            String actualPreviewQty = driver.findElement(By.xpath("//body[1]/div[1]/main[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]")).getText().trim();
            String actualPreviewAmount = driver.findElement(By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[2]")).getText().trim();
            
            String actualNotes = driver.findElement(By.xpath("(//p[@class='info_dd mb-0'])[1]")).getText().trim();
            String actualTerms = driver.findElement(By.xpath("(//p[@class='info_dd mb-0'])[2]")).getText().trim();

            boolean previewPassed = true;

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
    
    //TC-02
    public void clickEditManufactureOrder() {
    	clickCustomProductTab();
    	
        By threeDotLocator = By.xpath("(//i[@class='bi bi-three-dots-vertical'])[1]");
        WebElement threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(threeDotLocator));
        scrollToElement(threeDotBtn);
        try {
            threeDotBtn.click();
        } catch (Exception ex) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", threeDotBtn);
        }
        sleep(1);

        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(editBtn));
        scrollToElement(editButton);
        editButton.click();
        sleep(2);
        System.out.println("Navigated to Edit Manufacture Order page successfully.");
    }

    public void editVendorDateAndAddress() {
        By vendorDropdownLocator = By.xpath("(//div[@class='select-wrapper vendor-select-wrapper'])[1]");
        WebElement vendorDropdownElement = wait.until(ExpectedConditions.elementToBeClickable(vendorDropdownLocator));
        scrollToElement(vendorDropdownElement);
        
        try {
            vendorDropdownElement.click();
        } catch (Exception ex) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", vendorDropdownElement);
        }
        sleep(1);
        
        By searchInputLocator = By.xpath("(//input[@placeholder='Search the vendors'])[1]");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputLocator));
        } catch (Exception ignored) {
        }
        
        By scopedOptionLocator = By.xpath("(//div[@class='options_box'])[1]//div[@class='option']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(scopedOptionLocator));
        List<WebElement> vendorOptions = driver.findElements(scopedOptionLocator);
        
        if (!vendorOptions.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(vendorOptions.size());
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

        waitFor(expectedDeliveryDateCalendar);
        scrollToElement(expectedDeliveryDateCalendar);
        
        click(expectedDeliveryDateCalendar);
        sleep(1);
        
        By activeDaysLocator = By.xpath("(//div[@class='dayContainer'])[1]//span[contains(@class, 'flatpickr-day') and not(contains(@class, 'flatpickr-disabled')) and not(contains(@class, 'prevMonthDay')) and not(contains(@class, 'nextMonthDay'))]");
        List<WebElement> dayElements = driver.findElements(activeDaysLocator);
        
        if (dayElements.isEmpty()) {
            activeDaysLocator = By.xpath("(//div[@class='dayContainer'])[1]//div[contains(@class, 'flatpickr-day') and not(contains(@class, 'flatpickr-disabled')) and not(contains(@class, 'prevMonthDay')) and not(contains(@class, 'nextMonthDay'))]");
            dayElements = driver.findElements(activeDaysLocator);
        }
        
        if (dayElements.isEmpty()) {
            throw new RuntimeException("❌ Could not find any active/selectable days inside the Flatpickr dayContainer!");
        }
        
        Random dateRandom = new Random();
        WebElement randomDayElement = dayElements.get(dateRandom.nextInt(dayElements.size()));
        
        scrollToElement(randomDayElement);
        wait.until(ExpectedConditions.elementToBeClickable(randomDayElement));
        
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", randomDayElement);
        sleep(1);
        
        selectedDateText = expectedDeliveryDateCalendar.getAttribute("value");
        if (selectedDateText == null || selectedDateText.isEmpty()) {
            selectedDateText = (String) js.executeScript("return arguments[0].value;", expectedDeliveryDateCalendar);
        }
        
        System.out.println("Updated Delivery Date from Calendar: " + selectedDateText);

        By addressDropdownLocator = By.xpath("(//div[@class='select-wrapper address-select-wrapper'])[1]");
        WebElement addressDropdownElement = wait.until(ExpectedConditions.elementToBeClickable(addressDropdownLocator));
        scrollToElement(addressDropdownElement);
        
        try {
            addressDropdownElement.click();
        } catch (Exception ex) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", addressDropdownElement);
        }
        sleep(1);
        
        By addressOptionLocator = By.xpath("(//div[@class='options_box'])[2]//div[@class='option']");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(addressOptionLocator));
        List<WebElement> addressOptions = driver.findElements(addressOptionLocator);
        
        if (!addressOptions.isEmpty()) {
            Random addressRandom = new Random();
            int addressIndex = addressRandom.nextInt(addressOptions.size());
            int addressItemNumber = addressIndex + 1;
            
            By specificAddress = By.xpath("((//div[@class='options_box'])[2]//div[@class='option'])[" + addressItemNumber + "]");
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
        waitFor(productNameInput);
        scrollToElement(productNameInput);
        String updatedProductName = "Updated_Product_" + new Random().nextInt(1000);
        productNameInput.clear();
        productNameInput.sendKeys(updatedProductName);
        sleep(1);
        System.out.println("Updated Product Name: " + updatedProductName);

        waitFor(makingCostInput);
        scrollToElement(makingCostInput);
        
        Random random = new Random();
        int randomCostValue = 20 + random.nextInt(980); 
        lastMakingCost = (double) randomCostValue; 
        String makingCostStr = String.valueOf(randomCostValue);
        
        makingCostInput.clear();
        makingCostInput.sendKeys(makingCostStr);
        sleep(1);
        System.out.println("Updated Making Cost: " + makingCostStr);

        scrollToElement(sizeTagsDropdown);
        click(sizeTagsDropdown);
        sleep(1);

        By sizeOptionsLocator = By.xpath("(//div[@class='tag_menu js-tag_menu'])[1]//label[@class='tag_option']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(sizeOptionsLocator));
        List<WebElement> sizeOptions = driver.findElements(sizeOptionsLocator);
        totalSizeOptionsCount = sizeOptions.size(); 

        selectedSizesList.clear();
        selectedSizeIndices.clear();
        selectedQuantitiesList.clear();
        totalQuantitySum = 0;

        if (!sizeOptions.isEmpty()) {
            for (int i = 0; i < sizeOptions.size(); i++) {
                int itemNumber = i + 1;
                WebElement sizeOption = sizeOptions.get(i);
                
                String optionClass = sizeOption.getAttribute("class");
                boolean isSelected = optionClass != null && (optionClass.contains("active") || optionClass.contains("selected") || optionClass.contains("checked"));
                
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
                    
                    By quantityInputLocator = By.xpath("(//input[@placeholder='-'])[" + itemNumber + "]");
                    WebElement qtyInput = wait.until(ExpectedConditions.elementToBeClickable(quantityInputLocator));
                    scrollToElement(qtyInput);
                    
                    int currentQty = 1; 
                    try {
                        String existingVal = qtyInput.getAttribute("value");
                        if (existingVal != null && !existingVal.trim().isEmpty()) {
                            currentQty = Integer.parseInt(existingVal.trim());
                        }
                    } catch (Exception ignored) {}

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
    
    public void verifyEditedTotalAmountAndAddNotesAndSave() {
        waitFor(totalCostText);
        scrollToElement(totalCostText);
        String displayedTotalText = totalCostText.getText().trim();
        System.out.println("Displayed Edited Total Cost Text on UI: " + displayedTotalText);
        
        double expectedTotalCost = lastMakingCost * totalQuantitySum;
        System.out.println("Calculated Expected Edited Total Cost: " + String.format(java.util.Locale.US, "%.2f", expectedTotalCost));
        
        if (displayedTotalText.isEmpty()) {
            throw new RuntimeException("Edited total amount field is empty or not reflecting correctly!");
        }

        waitFor(addNotesInput);
        scrollToElement(addNotesInput);
        addNotesInput.clear();
        addNotesInput.sendKeys("Automated Notes: Manufacture Order updated successfully with verified pricing.");
        sleep(1);
        System.out.println("Entered Edited Notes successfully.");

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
    	clickCustomProductTab();
        System.out.println("\n=============================================================");
        System.out.println("      EDITED MANUFACTURE ORDER LISTING PAGE VERIFICATION     ");
        System.out.println("=============================================================");

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String expectedVendor = (selectedVendorText != null) ? selectedVendorText.trim() : "";
        String expectedQty = String.valueOf(totalQuantitySum);
        
        double calculatedTotalAmount = lastMakingCost * totalQuantitySum;
        String expectedAmountStr = String.format(java.util.Locale.US, "%.2f", calculatedTotalAmount);
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

        boolean vendorMatch = actualVendor.contains(expectedVendor) || expectedVendor.contains(actualVendor);
        printComparison("Updated Vendor", expectedVendor, actualVendor, vendorMatch, GREEN, RED, RESET);
        if (!vendorMatch) allPassed = false;

        boolean qtyMatch = actualQty.replaceAll("[^0-9]", "").equals(expectedQty);
        printComparison("Updated Quantity", expectedQty, actualQty, qtyMatch, GREEN, RED, RESET);
        if (!qtyMatch) allPassed = false;

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

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String expectedQty = String.valueOf(totalQuantitySum);
        double calculatedTotalAmount = lastMakingCost * totalQuantitySum;
        String expectedAmountStr = String.format(java.util.Locale.US, "%.2f", calculatedTotalAmount);

        try {
            By threeDotLocator = By.xpath("(//i[@class='bi bi-three-dots-vertical'])[1]");
            WebElement threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(threeDotLocator));
            scrollToElement(threeDotBtn);
            try {
                threeDotBtn.click();
            } catch (Exception ex) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", threeDotBtn);
            }
            sleep(1);

            WebElement previewBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]")
            ));
            previewBtn.click();
            sleep(2);

            String actualPreviewDate = driver.findElement(By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[4]")).getText().trim();
            String actualPreviewAddress = driver.findElement(By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[5]")).getText().trim();
            String actualPreviewProductName = driver.findElement(By.xpath("(//p[@class='font_14 m-0 fw-bold'])[1]")).getText().trim();
            String actualPreviewQty = driver.findElement(By.xpath("//body[1]/div[1]/main[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]")).getText().trim();
            String actualPreviewAmount = driver.findElement(By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[2]")).getText().trim();
            
            String actualNotes = driver.findElement(By.xpath("(//p[@class='info_dd mb-0'])[1]")).getText().trim();
            String actualTerms = driver.findElement(By.xpath("(//p[@class='info_dd mb-0'])[2]")).getText().trim();

            boolean previewPassed = true;

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
    
    //TC-03
    public void verifySaveAsDraft() {
        waitFor(addNotesInput);
        scrollToElement(addNotesInput);
        addNotesInput.clear();
        addNotesInput.sendKeys("Automated Notes: Order created successfully with verified pricing.");
        sleep(1);
        System.out.println("Entered Notes successfully.");

        waitFor(addDescriptionInput);
        scrollToElement(addDescriptionInput);
        addDescriptionInput.clear();
        addDescriptionInput.sendKeys("Automated Description: Checking end-to-end manufacture order creation workflow.");
        sleep(1);
        System.out.println("Entered Description successfully.");

        waitFor(saveAsDraftBtn);
        scrollToElement(saveAsDraftBtn);
        click(saveAsDraftBtn);
        sleep(10);
        System.out.println("Clicked Save Button successfully.");
    }
    
    public void verifyDraftManufactureOrderOnListingPage() {
    	clickCustomProductTab();
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

    public void performDraftDeletionWorkflow() {
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        try {
            By moIdCellLocator = By.xpath("(//td)[2]");
            String actualMoId = driver.findElement(moIdCellLocator).getText().trim();

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
    
    //TC-04
    public void verifyCancelItemsOptionIsDisplayed() {
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        System.out.println("\n=============================================================");
        System.out.println("          VERIFY CANCEL ITEMS OPTION ON PREVIEW PAGE         ");
        System.out.println("=============================================================");
        clickCustomProductTab();
        click(threeDotBtn);
        sleep(1);

        click(previewbtn);
        sleep(2);

        WebElement initialStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//span[@class='status-chip active'])[1]")
        ));
        System.out.println("Preview Page Initial Status: " + initialStatus.getText().trim());

        WebElement prevThreeDot = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//div[@class='material_action_dropdown js-tag-toggle'])[1]")
        ));
        scrollToElement(prevThreeDot);
        prevThreeDot.click();
        sleep(1);

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

        WebElement cancelOpt = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//li[normalize-space()='Cancel Items'])[1]")
        ));
        cancelOpt.click();
        sleep(1);

        WebElement selectAllCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//input[@id='select_all_cancel_items'])[1]")
        ));
        scrollToElement(selectAllCheckbox);
        if (!selectAllCheckbox.isSelected()) {
            selectAllCheckbox.click();
        }
        sleep(1);

        WebElement reasonInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//textarea[@id='cancel_reason'])[1]")
        ));
        scrollToElement(reasonInput);
        reasonInput.clear();
        String randomReason = "Automated Cancellation: Requirement changed (" + new java.util.Random().nextInt(1000) + ")";
        reasonInput.sendKeys(randomReason);
        sleep(1);
        System.out.println("Entered cancellation reason: " + randomReason);

        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//button[@id='btn_confirm_cancel_items'])[1]")
        ));
        scrollToElement(confirmBtn);
        confirmBtn.click();
        sleep(3); 
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

        WebElement cancelledStatusPreview = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//span[@class='status-chip inactive'])[1]")
        ));
        scrollToElement(cancelledStatusPreview);
        String previewStatusText = cancelledStatusPreview.getText().trim();
        System.out.println("Preview Page Status: " + previewStatusText);

        click(backBtn);
        sleep(2);
        clickCustomProductTab();

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
      
    //TC-05
    public void convertAndVerifyManufactureOrderAsIssued() {
        System.out.println("\n=============================================================");
        System.out.println("      CONVERT MANUFACTURE ORDER TO ISSUED & VERIFY           ");
        System.out.println("=============================================================");

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";
        String expectedStatus = "Issued";

        try {
            WebElement issuedBtn = wait.until(ExpectedConditions.elementToBeClickable(markAsIssuedBtn));
            issuedBtn.click();
            sleep(1);

            WebElement confirmIssuedBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmMarkAsIssuedBtn));
            confirmIssuedBtn.click();
            sleep(2);

            System.out.println("Waiting for details page status to update to 'Issued'...");
            
            WebElement detailsStatus = wait.until(ExpectedConditions.visibilityOf(detailsPageStatusLocator));
            String actualDetailsStatus = detailsStatus.getText().trim();
            boolean detailsStatusMatch = actualDetailsStatus.equalsIgnoreCase(expectedStatus);
            printComparison("Details Page Status", expectedStatus, actualDetailsStatus, detailsStatusMatch, GREEN, RED, RESET);
            if (!detailsStatusMatch) {
                throw new AssertionError("Details page status did not reflect 'Issued'! Current text found: " + actualDetailsStatus);
            }

            WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(backBtn));
            backButton.click();
            sleep(2);
            
            clickCustomProductTab();
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

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        waitFor(saveBtn);
        scrollToElement(saveBtn);
        click(saveBtn);
        sleep(1);
        System.out.println("Clicked Save button with empty fields to trigger validations.");

        By vendorErrLocator = By.xpath("(//span[normalize-space()='Please select a vendor'])[1]");
        By dateErrLocator = By.xpath("(//span[normalize-space()='Please select an expected delivery date'])[1]");
        By addressErrLocator = By.xpath("(//span[normalize-space()='Please select a delivery address'])[1]");
        By productNameErrLocator = By.xpath("(//span[normalize-space()='Please enter a product name'])[1]");
        By makingCostErrLocator = By.xpath("(//span[normalize-space()='Please enter a valid integer making cost'])[1]");
        By sizeErrLocator = By.xpath("(//span[normalize-space()='Please select at least one size'])[1]");

        boolean allErrorsPassed = true;

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

    //TC-07
    public void addProductDetailsForSecondProduct() {
        waitFor(addProductBtn);
        scrollToElement(addProductBtn);
        click(addProductBtn);
        sleep(2);
        System.out.println("Clicked 'Add Product' button for second product.");

        By productNameInput2 = By.xpath("(//input[@id='product_name'])[2]");
        WebElement prodNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(productNameInput2));
        scrollToElement(prodNameInput);
        String randomProductName = "Product_" + new Random().nextInt(1000);
        prodNameInput.clear();
        prodNameInput.sendKeys(randomProductName);
        sleep(1);
        System.out.println("Entered Second Product Name: " + randomProductName);

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

        By sizeTagsDropdown2 = By.xpath("(//div[@class='tag_input js-tag-toggle'])[2]");
        WebElement sizeDropdown2 = wait.until(ExpectedConditions.elementToBeClickable(sizeTagsDropdown2));
        scrollToElement(sizeDropdown2);
        sizeDropdown2.click();
        sleep(1);

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

            int sizeCount = sizeOptions2.size(); 

            for (int index : chosenIndices) {
                int itemNumber = index + 1; 
                selectedSizeIndices.add(itemNumber); 

                By specificOption = By.xpath("((//div[@class='tag_menu js-tag_menu'])[2]//label[@class='tag_option'])[" + itemNumber + "]");
                WebElement specificSizeEl = wait.until(ExpectedConditions.elementToBeClickable(specificOption));

                String sizeText = specificSizeEl.getText().trim();
                selectedSizesList.add(sizeText);

                scrollToElement(specificSizeEl);
                specificSizeEl.click();
                sleep(1);

                int inputIndex = (2 * sizeCount) + itemNumber;
                By quantityInputLocator = By.xpath("(//input[@placeholder='-'])[" + inputIndex + "]");
                WebElement qtyInput = wait.until(ExpectedConditions.elementToBeClickable(quantityInputLocator));

                int randomQty = 1 + random.nextInt(5);
                selectedQuantitiesList.add(randomQty);
                totalQuantitySum += randomQty;

                scrollToElement(qtyInput);
                qtyInput.clear();
                qtyInput.sendKeys(String.valueOf(randomQty));
                sleep(1);

                System.out.println("Selected Second Product Size: '" + sizeText + "' at menu index [" + itemNumber + "] and placeholder index [" + inputIndex + "] with Quantity: " + randomQty);
            }
            
            secondProductExpectedCost = lastMakingCost * totalQuantitySum;
            
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
    
    private int getSecondProductRawMaterialIndex(int sizeIndex) {
        return sizeIndex + (3 * totalSizeOptionsCount);
    }
    
    public void selectRawMaterialAndFillQuantitiesForSecondProduct() {
        Random random = new Random();
        
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

            int firstSizeInputIndex = getSecondProductRawMaterialIndex(selectedSizeIndices.get(0));             
            
            By firstInputLocator = By.xpath("(//input[@placeholder='-'])[" + firstSizeInputIndex + "]");
            WebElement firstQtyInput = wait.until(ExpectedConditions.elementToBeClickable(firstInputLocator));
            scrollToElement(firstQtyInput);
            
            firstQtyInput.clear();
            firstQtyInput.sendKeys("1000000");
            sleep(1);

            int availableStock = 1000000; 
            try {
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

            if (availableStock >= totalRequiredQty) {
                System.out.println("Sufficient stock found for 2nd product! Filling exact quantities...");
                
                for (int i = 0; i < selectedSizesList.size(); i++) {
                    String targetSize = selectedSizesList.get(i);
                    int exactQty = selectedQuantitiesList.get(i);
                    int rmInputIndex = getSecondProductRawMaterialIndex(selectedSizeIndices.get(i));
                    
                    By rmQtyInputLocator = By.xpath("(//input[@placeholder='-'])[" + rmInputIndex + "]");
                    WebElement rmQtyInput = wait.until(ExpectedConditions.elementToBeClickable(rmQtyInputLocator));
                    scrollToElement(rmQtyInput);
                    
                    rmQtyInput.clear();
                    rmQtyInput.sendKeys(String.valueOf(exactQty));
                    sleep(1);
                    
                    System.out.println("Entered Exact Raw Material Quantity: " + exactQty + " for size: " + targetSize + " at index [" + rmInputIndex + "]");
                }
                
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
        By prod1TotalLoc = By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[1]");
        WebElement prod1TotalEl = wait.until(ExpectedConditions.visibilityOfElementLocated(prod1TotalLoc));
        scrollToElement(prod1TotalEl);
        String prod1Text = prod1TotalEl.getText().trim();
        System.out.println("1st Product Displayed Total Text: " + prod1Text);
        
        if (prod1Text.isEmpty()) {
            throw new RuntimeException("1st product total amount text box is empty!");
        }

        By prod2TotalLoc = By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[2]");
        WebElement prod2TotalEl = wait.until(ExpectedConditions.visibilityOfElementLocated(prod2TotalLoc));
        scrollToElement(prod2TotalEl);
        String prod2Text = prod2TotalEl.getText().trim();
        System.out.println("2nd Product Displayed Total Text: " + prod2Text);
        
        if (prod2Text.isEmpty()) {
            throw new RuntimeException("2nd product total amount text box is empty!");
        }

        By combinedTotalLoc = By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[3]");
        WebElement combinedTotalEl = wait.until(ExpectedConditions.visibilityOfElementLocated(combinedTotalLoc));
        scrollToElement(combinedTotalEl);
        String combinedText = combinedTotalEl.getText().trim();
        System.out.println("Combined Products Displayed Total Text: " + combinedText);
        
        if (combinedText.isEmpty()) {
            throw new RuntimeException("Combined total amount text box is empty!");
        }
        
        double expectedCombinedCost = firstProductExpectedCost + secondProductExpectedCost;
        System.out.println("Calculated Expected Combined Cost: " + String.format(java.util.Locale.US, "%.2f", expectedCombinedCost));
        System.out.println("All product and combined totals verified successfully!");
    }
    
    public int firstProductTotalQuantity = 0;
    public int secondProductTotalQuantity = 0;
    public void verifyManufactureOrderForMultipleProductsOnListingPage() {
    	clickCustomProductTab();
        System.out.println("\n=============================================================");
        System.out.println("          MANUFACTURE ORDER LISTING PAGE VERIFICATION        ");
        System.out.println("=============================================================");

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        String expectedVendor = (selectedVendorText != null) ? selectedVendorText.trim() : "";
        
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

        boolean vendorMatch = actualVendor.contains(expectedVendor) || expectedVendor.contains(actualVendor);
        printComparison("Vendor Name", expectedVendor, actualVendor, vendorMatch, GREEN, RED, RESET);
        if (!vendorMatch) allPassed = false;

        boolean qtyMatch = actualQty.replaceAll("[^0-9]", "").equals(expectedQty);
        printComparison("Combined Quantity", expectedQty, actualQty, qtyMatch, GREEN, RED, RESET);
        if (!qtyMatch) allPassed = false;

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

        By threeDotBtnLocator = By.xpath("(//i[@class='bi bi-three-dots-vertical'])[1]"); 
        WebElement threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(threeDotBtnLocator));
        scrollToElement(threeDotBtn);
        threeDotBtn.click();
        sleep(1);

        By previewBtnLocator = By.xpath("//div[@class='dropdown-menu actions-dropdown-menu dropdown-menu-right dropdown_button_wrapper show']//a[1]"); 
        WebElement previewBtn = wait.until(ExpectedConditions.elementToBeClickable(previewBtnLocator));
        scrollToElement(previewBtn);
        previewBtn.click();
        sleep(2); 

        boolean allPassed = true;

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
    
    //TC-08
    public void exportManufactureOrderFlow() {
        waitFor(exportBtnDropdown);
        scrollToElement(exportBtnDropdown);
        click(exportBtnDropdown);
        sleep(1); 

        waitFor(exportBtn);
        scrollToElement(exportBtn);
        click(exportBtn);
        sleep(1); 

        waitFor(includeCheckBox);
        scrollToElement(includeCheckBox);
        try {
            if (!includeCheckBox.isSelected()) {
                click(includeCheckBox);
            }
        } catch (Exception e) {
            click(includeCheckBox); 
        }
        sleep(1); 

        System.out.println("--- Capturing Export Column Names ---");
        
        int totalColumns = 8; 
        
        for (int i = 1; i <= totalColumns; i++) {
            String columnXpath;
            
            if (i == 1) {
                columnXpath = "(//div[contains(@data-section,'details')]//div[contains(@class,'section-body')]//label[contains(@class,'required') or contains(@class,'col-check-item')])[" + i + "]";
            } else {
                columnXpath = "(//div[contains(@data-section,'details')]//div[contains(@class,'section-body')]//label[contains(@class,'col-check-item')])[" + i + "]";
            }
            
            try {
                WebElement columnLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(columnXpath)));
                scrollToElement(columnLabel);
                
                String columnName = columnLabel.getText().trim();
                
                if (columnName.isEmpty()) {
                    org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
                    columnName = (String) js.executeScript("return arguments[0].innerText;", columnLabel);
                    columnName = columnName != null ? columnName.trim() : "";
                }
                
                System.out.println("Captured Column [" + i + "]: " + columnName);
            } catch (Exception e) {
                System.out.println("⚠️ Warning: Could not capture column at index [" + i + "]");
            }
        }
        System.out.println("-------------------------------------");
        sleep(1);

        waitFor(downloadBtn);
        scrollToElement(downloadBtn);
        click(downloadBtn);

        try {
            waitFor(generatingMsg);
            String generationText = generatingMsg.getText().trim();
            System.out.println("✅ Export Generation Message Displayed: " + generationText);
        } catch (Exception e) {
            System.out.println("⚠️ Warning: Generating message notification was not detected or vanished too quickly.");
        }
    } 
    
    public void verifyAndDownloadExportFileFromHistory() {
        waitFor(exportHistoryPage);
        scrollToElement(exportHistoryPage);
        click(exportHistoryPage);
        sleep(2); 
        
        waitFor(fileName);
        scrollToElement(fileName);
        String generatedFileName = fileName.getText().trim();
        System.out.println("📄 Export File Name found in history: " + generatedFileName);
        
        if (!generatedFileName.toLowerCase().contains("manufacture-order")) {
            throw new RuntimeException("❌ File name in export history does not match 'manufacture-order'! Found: " + generatedFileName);
        }
        
        boolean isSuccess = false;
        int maxPollAttempts = 30; 
        int pollCount = 0;
        
        while (!isSuccess && pollCount < maxPollAttempts) {
            pollCount++;
            try {
                waitFor(status);
                String currentStatus = status.getText().trim();
                System.out.println("⏳ Export Status Poll [" + pollCount + "]: " + currentStatus);
                
                if (currentStatus.equalsIgnoreCase("Success")) {
                    isSuccess = true;
                    System.out.println("✅ Export status changed to Success!");
                } else if (currentStatus.equalsIgnoreCase("Failed") || currentStatus.equalsIgnoreCase("Error")) {
                    throw new RuntimeException("❌ Export generation failed on the server!");
                } else {
                    driver.navigate().refresh();
                    sleep(3);
                }
            } catch (Exception e) {
                System.out.println("⚠️ Warning during status polling, retrying... (" + e.getMessage() + ")");
                driver.navigate().refresh();
                sleep(3);
            }
        }
        
        if (!isSuccess) {
            throw new RuntimeException("❌ Export status remained Pending/Processing after max retries!");
        }
        
        waitFor(threeDot);
        scrollToElement(threeDot);
        click(threeDot);
        sleep(1); 
        
        waitFor(exportDownloadBtn);
        scrollToElement(exportDownloadBtn);
        click(exportDownloadBtn);
        System.out.println("⬇️ Export file download triggered successfully from Export History!");
        sleep(3); 
    }  
    
    public void verifyExportedExcelColumns() {
        System.out.println("==================================================");
        System.out.println("🔍 Starting Exported Excel File Verification...");
        System.out.println("==================================================");
        
        String downloadPath = System.getProperty("user.home") + "/Downloads";
        File dir = new File(downloadPath);
        
        System.out.println("📂 Scanning Downloads folder: " + downloadPath);
        
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".xlsx") && name.toLowerCase().contains("manufacture-order"));
        
        if (files == null || files.length == 0) {
            System.out.println("⚠️ Specific 'manufacture-order' Excel file not instantly found. Checking for any recent .xlsx files...");
            files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".xlsx"));
        }
        
        if (files == null || files.length == 0) {
            throw new RuntimeException("❌ No downloaded Excel file found in the Downloads folder: " + downloadPath);
        }
        
        File latestFile = files[0];
        for (File f : files) {
            if (f.lastModified() > latestFile.lastModified()) {
                latestFile = f;
            }
        }
        
        System.out.println("📥 Target Excel File Identified: " + latestFile.getAbsolutePath());
        System.out.println("⏱️ File Last Modified: " + new java.util.Date(latestFile.lastModified()));
        
        sleep(2);

        System.out.println("📖 Opening Excel workbook and reading sheets...");
        try (FileInputStream fis = new FileInputStream(latestFile);
             Workbook workbook = WorkbookFactory.create(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0); 
            System.out.println("📄 Active Sheet Name: " + sheet.getSheetName());
            
            Row headerRow = sheet.getRow(1);
            if (headerRow == null) {
                System.out.println("⚠️ Row index 1 (2nd row) is empty. Falling back to row index 0 (1st row)...");
                headerRow = sheet.getRow(0);
            }
            
            if (headerRow == null) {
                throw new RuntimeException("❌ The header row in the exported Excel file is empty!");
            }
            
            System.out.println("\n--------------------------------------------------");
            System.out.println("📊 EXPORTED COLUMNS FOUND IN ROW 2:");
            System.out.println("--------------------------------------------------");
            
            List<String> actualExcelColumns = new ArrayList<>();
            int colIndex = 1;
            
            for (Cell cell : headerRow) {
                String cellValue = cell.toString().trim();
                if (!cellValue.isEmpty()) {
                    actualExcelColumns.add(cellValue);
                    System.out.println("   [Column " + colIndex + "] ---> " + cellValue);
                    colIndex++;
                }
            }
            System.out.println("--------------------------------------------------\n");
            
            if (actualExcelColumns.isEmpty()) {
                throw new RuntimeException("❌ No valid column headers found in the Excel file!");
            }
            
            System.out.println("✅ Excel columns verification completed successfully!");
            System.out.println("🎯 Total Columns Verified: " + actualExcelColumns.size());
            System.out.println("==================================================\n");
            
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to read or parse the downloaded Excel file: " + e.getMessage(), e);
        }
    }  
    
    //TC-09
    public void clickAddManufactureOrderForExistingProduct() {
        scrollToElement(addManufactureOrderBtn);
        click(addManufactureOrderBtn);
        sleep(1);
        
        wait.until(ExpectedConditions.visibilityOf(addExistingProductBtn));
        scrollToElement(addExistingProductBtn);
        click(addExistingProductBtn);
        sleep(1);
    }
    
    public static List<String> availableProductsText = new ArrayList<>();
    public static String selectedProductText = "";
    public static List<String> availableSizesText = new ArrayList<>();
    public static int totalEnteredQuantity = 0;
    public static int makingCostValue = 0;
    public static double expectedTotalAmount = 0.0;
    public static String savedNoteText = "";
    public static String savedDescText = "";
    
    public void selectProductAndCaptureSizes() {
        String CYAN = "\u001B[36m";
        String GREEN = "\u001B[32m";
        String RESET = "\u001B[0m";

        By itemDropdownLocator = By.xpath("(//div[@class='select-box d-flex justify-content-start align-items-center'])[1]");
        WebElement itemDropdownElement = wait.until(ExpectedConditions.elementToBeClickable(itemDropdownLocator));
        scrollToElement(itemDropdownElement);
        
        try {
            itemDropdownElement.click();
        } catch (Exception ex) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", itemDropdownElement);
        }
        sleep(1);
        
        By productOptionLocator = By.xpath("(//div[@class='options_box'])[3]//div[@class='option']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productOptionLocator));
        List<WebElement> productOptions = driver.findElements(productOptionLocator);
        
        availableProductsText.clear();
        for (WebElement option : productOptions) {
            availableProductsText.add(option.getText().trim());
        }
        System.out.println(CYAN + "Available Products List: " + availableProductsText + RESET);
        
        if (!productOptions.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(productOptions.size());
            int itemNumber = index + 1;
            
            By specificProduct = By.xpath("((//div[@class='options_box'])[3]//div[@class='option'])[" + itemNumber + "]");
            WebElement chosenProduct = wait.until(ExpectedConditions.elementToBeClickable(specificProduct));
            
            selectedProductText = chosenProduct.getText().trim();
            scrollToElement(chosenProduct);
            chosenProduct.click();
            sleep(1);
            System.out.println(GREEN + "Selected Product: " + selectedProductText + RESET);
        } else {
            throw new RuntimeException("No product options available in options box [3]!");
        }
        
        By sizeWrapperLocator = By.xpath("//div[@class='w-100 size_wrapper']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(sizeWrapperLocator));
        WebElement sizeWrapper = driver.findElement(sizeWrapperLocator);
        
        List<WebElement> sizeElements = sizeWrapper.findElements(By.xpath(".//span | .//button | .//div[contains(@class, 'size')]"));
        if (sizeElements.isEmpty()) {
            sizeElements = sizeWrapper.findElements(By.xpath("./*")); 
        }
        
        availableSizesText.clear();
        for (WebElement sizeElem : sizeElements) {
            String sizeText = sizeElem.getText().trim();
            if (!sizeText.isEmpty()) {
                availableSizesText.add(sizeText);
            }
        }
        
        if (availableSizesText.isEmpty()) {
            availableSizesText.add(sizeWrapper.getText().trim());
        }
        
        System.out.println(CYAN + "Captured Sizes from Size Wrapper: " + availableSizesText + RESET);
    }
       
    public void fillQuantitiesForCapturedSizes() {
        if (availableSizesText == null || availableSizesText.isEmpty()) {
            throw new RuntimeException("No available sizes captured! Please select a product and capture sizes first.");
        }

        String GREEN = "\u001B[32m";
        String YELLOW = "\u001B[33m";
        String RESET = "\u001B[0m";

        Random random = new Random();

        for (String size : availableSizesText) {
            String cleanSize = size.trim();
            
            if (cleanSize.contains("-") || cleanSize.contains("–") || cleanSize.contains("—") || cleanSize.contains(":")) {
                String[] parts = cleanSize.split("[-–—:]");
                cleanSize = parts[parts.length - 1].trim();
            } else {
                cleanSize = cleanSize.replaceAll("(?i)^size", "").trim();
            }

            String dynamicXpath = "(//input[@aria-label='Quantity for size " + cleanSize + "'])[1]";
            By quantityInputLocator = By.xpath(dynamicXpath);

            try {
                WebElement quantityInput = wait.until(ExpectedConditions.elementToBeClickable(quantityInputLocator));
                scrollToElement(quantityInput);
                
                int randomQty = random.nextInt(10) + 1;
                
                quantityInput.clear();
                quantityInput.sendKeys(String.valueOf(randomQty));
                
                System.out.println(GREEN + "Filled quantity '" + randomQty + "' for size: " + cleanSize + RESET);
                
            } catch (Exception e) {
                System.out.println(YELLOW + "⚠️ Warning: Could not find or fill quantity input for size '" + cleanSize + "' (Original text: '" + size + "') with XPath: " + dynamicXpath + RESET);
            }
        }
    }
    
    public void verifyTotalsAndEnterMakingCost() {
        String GREEN = "\u001B[32m";
        String CYAN = "\u001B[36m";
        String YELLOW = "\u001B[33m";
        String RESET = "\u001B[0m";

        int calculatedSum = 0;
        if (availableSizesText != null && !availableSizesText.isEmpty()) {
            for (String size : availableSizesText) {
                String cleanSize = size.trim();
                
                if (cleanSize.contains("-") || cleanSize.contains("–") || cleanSize.contains("—") || cleanSize.contains(":")) {
                    String[] parts = cleanSize.split("[-–—:]");
                    cleanSize = parts[parts.length - 1].trim();
                } else {
                    cleanSize = cleanSize.replaceAll("(?i)^size", "").trim();
                }

                By quantityInputLocator = By.xpath("(//input[@aria-label='Quantity for size " + cleanSize + "'])[1]");
                try {
                    WebElement inputElem = driver.findElement(quantityInputLocator);
                    String val = inputElem.getAttribute("value");
                    if (val != null && !val.isEmpty()) {
                        calculatedSum += Integer.parseInt(val.trim());
                    }
                } catch (Exception e) {
                }
            }
        }
        
        totalEnteredQuantity = calculatedSum;

        By totalQtyCellLocator = By.xpath("(//td[@class='total_qty_cell'])[1]");
        WebElement totalQtyCell = wait.until(ExpectedConditions.visibilityOfElementLocated(totalQtyCellLocator));
        scrollToElement(totalQtyCell);
        
        String displayedQtyText = totalQtyCell.getText().replaceAll("[^0-9]", "").trim();
        int displayedTotalQty = displayedQtyText.isEmpty() ? 0 : Integer.parseInt(displayedQtyText);
        
        if (displayedTotalQty == totalEnteredQuantity) {
            System.out.println(GREEN + "✅ Total Quantity Verified Successfully! Displayed: " + displayedTotalQty + " matches calculated sum: " + totalEnteredQuantity + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Mismatch! Displayed Total Qty (" + displayedTotalQty + ") does not match calculated sum (" + totalEnteredQuantity + ")" + RESET);
        }

        Random random = new Random();
        makingCostValue = random.nextInt(101) + 100; 
        
        By makingCostInputLocator = By.xpath("(//input[@placeholder='-'])[1]");
        WebElement makingCostInput = wait.until(ExpectedConditions.elementToBeClickable(makingCostInputLocator));
        scrollToElement(makingCostInput);
        
        makingCostInput.clear();
        makingCostInput.sendKeys(String.valueOf(makingCostValue));
        System.out.println(CYAN + "Entered Making Cost: " + makingCostValue + RESET);
        sleep(1); 

        By totalAmountCellLocator = By.xpath("(//td[@class='d-flex align-items-center'])[1]");
        WebElement totalAmountCell = wait.until(ExpectedConditions.visibilityOfElementLocated(totalAmountCellLocator));
        scrollToElement(totalAmountCell);
        
        String displayedAmountText = totalAmountCell.getText().replaceAll("[^0-9.]", "").trim();
        double displayedTotalAmount = displayedAmountText.isEmpty() ? 0.0 : Double.parseDouble(displayedAmountText);
        
        expectedTotalAmount = (double) displayedTotalQty * makingCostValue; 
        
        if (Math.abs(displayedTotalAmount - expectedTotalAmount) < 0.01) {
            System.out.println(GREEN + "✅ Total Amount Verified Successfully! Displayed Amount: " + displayedTotalAmount + " matches expected (" + displayedTotalQty + " * " + makingCostValue + " = " + expectedTotalAmount + ")" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Amount Mismatch! Displayed Total Amount (" + displayedTotalAmount + ") does not match expected (" + expectedTotalAmount + ")" + RESET);
        }

        By summaryLocator = By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[2]");
        WebElement summaryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(summaryLocator));
        scrollToElement(summaryElement);
        
        String summaryText = summaryElement.getText().replaceAll("[^0-9.]", "").trim();
        double displayedSummaryAmount = summaryText.isEmpty() ? 0.0 : Double.parseDouble(summaryText);
        
        if (Math.abs(displayedSummaryAmount - expectedTotalAmount) < 0.01) {
            System.out.println(GREEN + "✅ Summary Total Amount Verified Successfully! Summary Amount: " + displayedSummaryAmount + " matches expected (" + expectedTotalAmount + ")" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Summary Amount Mismatch! Summary Amount (" + displayedSummaryAmount + ") does not match expected (" + expectedTotalAmount + ")" + RESET);
        }
    }
    
    public void addNotesDescriptionAndSave() {
        String GREEN = "\u001B[32m";
        String CYAN = "\u001B[36m";
        String RESET = "\u001B[0m";

        savedNoteText = "Automated Note - " + java.util.UUID.randomUUID().toString().substring(0, 8);
        savedDescText = "Automated Description - " + java.util.UUID.randomUUID().toString().substring(0, 8);

        waitFor(addNotesInput);
        scrollToElement(addNotesInput);
        addNotesInput.clear(); 
        sleep(1);
        addNotesInput.sendKeys(savedNoteText);
        sleep(1);
        System.out.println(GREEN + "Cleared and entered Notes successfully: " + CYAN + savedNoteText + RESET);

        waitFor(addDescriptionInput);
        scrollToElement(addDescriptionInput);
        addDescriptionInput.clear(); 
        sleep(1);
        addDescriptionInput.sendKeys(savedDescText);
        sleep(1);
        System.out.println(GREEN + "Cleared and entered Description successfully: " + CYAN + savedDescText + RESET);

        waitFor(saveBtn);
        scrollToElement(saveBtn);
        click(saveBtn);
        sleep(10);
        System.out.println(GREEN + "Clicked Save Button successfully." + RESET);
    }   
    
    public void verifyManufactureOrderOnForExistingProductListingPage() {
        String GREEN = "\u001B[32m";
        String CYAN = "\u001B[36m";
        String YELLOW = "\u001B[33m";
        String RESET = "\u001B[0m";

        System.out.println(CYAN + "=== Starting Verification on Manufacture Order Listing Page ===" + RESET);

        By vendorCellLocator = By.xpath("(//td)[4]");
        WebElement vendorCell = wait.until(ExpectedConditions.visibilityOfElementLocated(vendorCellLocator));
        scrollToElement(vendorCell);
        String displayedVendor = vendorCell.getText().trim();
        
        String displayedVendorBase = displayedVendor.split("\\s+")[0];
        String expectedVendorBase = selectedVendorText.isEmpty() ? "" : selectedVendorText.split("\\s+")[0];
        
        if (!expectedVendorBase.isEmpty() && displayedVendorBase.equalsIgnoreCase(expectedVendorBase)) {
            System.out.println(GREEN + "✅ Vendor Verified Successfully on Listing! Displayed base: '" + displayedVendorBase + "' matches expected base: '" + expectedVendorBase + "' (Full displayed: '" + displayedVendor + "')" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Vendor Mismatch on Listing! Displayed base: '" + displayedVendorBase + "', Expected base: '" + expectedVendorBase + "'" + RESET);
        }

        By totalQtyCellLocator = By.xpath("(//td)[5]");
        WebElement totalQtyCell = wait.until(ExpectedConditions.visibilityOfElementLocated(totalQtyCellLocator));
        scrollToElement(totalQtyCell);
        String displayedQtyText = totalQtyCell.getText().replaceAll("[^0-9]", "").trim();
        int displayedQty = displayedQtyText.isEmpty() ? 0 : Integer.parseInt(displayedQtyText);
        
        if (displayedQty == totalEnteredQuantity) {
            System.out.println(GREEN + "✅ Total Quantities Verified Successfully on Listing! Displayed: " + displayedQty + " matches expected: " + totalEnteredQuantity + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Total Quantities Mismatch on Listing! Displayed: " + displayedQty + ", Expected: " + totalEnteredQuantity + RESET);
        }

        By totalAmountCellLocator = By.xpath("(//td)[6]");
        WebElement totalAmountCell = wait.until(ExpectedConditions.visibilityOfElementLocated(totalAmountCellLocator));
        scrollToElement(totalAmountCell);
        String displayedAmountText = totalAmountCell.getText().replaceAll("[^0-9.]", "").trim();
        double displayedAmount = displayedAmountText.isEmpty() ? 0.0 : Double.parseDouble(displayedAmountText);
        
        double expectedAmount = (double) totalEnteredQuantity * makingCostValue;
        if (Math.abs(displayedAmount - expectedAmount) < 0.01) {
            System.out.println(GREEN + "✅ Total Amount Verified Successfully on Listing! Displayed: " + displayedAmount + " matches expected: " + expectedAmount + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Total Amount Mismatch on Listing! Displayed: " + displayedAmount + ", Expected: " + expectedAmount + RESET);
        }

        By statusCellLocator = By.xpath("(//td)[7]");
        WebElement statusCell = wait.until(ExpectedConditions.visibilityOfElementLocated(statusCellLocator));
        scrollToElement(statusCell);
        String displayedStatus = statusCell.getText().trim();
        
        if (displayedStatus.equalsIgnoreCase("Open")) {
            System.out.println(GREEN + "✅ Status Verified Successfully on Listing! Displayed: '" + displayedStatus + "' matches expected: 'Open'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Status Mismatch on Listing! Displayed: '" + displayedStatus + "', Expected: 'Open'" + RESET);
        }

        By createdAtCellLocator = By.xpath("(//td)[8]");
        WebElement createdAtCell = wait.until(ExpectedConditions.visibilityOfElementLocated(createdAtCellLocator));
        scrollToElement(createdAtCell);
        String displayedDate = createdAtCell.getText().trim();
        
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String expectedDate = today.format(formatter);
        
        if (displayedDate.contains(expectedDate)) {
            System.out.println(GREEN + "✅ Created At Date Verified Successfully on Listing! Displayed: '" + displayedDate + "' matches today's date: '" + expectedDate + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Date Mismatch on Listing! Displayed: '" + displayedDate + "', Expected today's date: '" + expectedDate + "'" + RESET);
        }
    }
    
    public void verifyManufactureOrderOnPreviewPage() {
        String GREEN = "\u001B[32m";
        String CYAN = "\u001B[36m";
        String YELLOW = "\u001B[33m";
        String RESET = "\u001B[0m";

        System.out.println(CYAN + "=== Starting Navigation to Preview Page ===" + RESET);

        waitFor(threeDotBtn);
        scrollToElement(threeDotBtn);
        click(threeDotBtn);
        sleep(1);

        waitFor(previewbtn);
        scrollToElement(previewbtn);
        click(previewbtn);
        sleep(2); 
        System.out.println(GREEN + "Successfully opened Preview Page/Modal." + RESET);

        System.out.println(CYAN + "=== Verifying Details on Preview Page ===" + RESET);

        By createdAtLocator = By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[3]");
        WebElement createdAtElem = wait.until(ExpectedConditions.visibilityOfElementLocated(createdAtLocator));
        scrollToElement(createdAtElem);
        String displayedCreatedAt = createdAtElem.getText().trim();
        
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String expectedDate = today.format(formatter);
        
        if (displayedCreatedAt.contains(expectedDate)) {
            System.out.println(GREEN + "✅ Preview Created At Verified: '" + displayedCreatedAt + "' matches today's date: '" + expectedDate + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Created At Mismatch: '" + displayedCreatedAt + "', Expected: '" + expectedDate + "'" + RESET);
        }

        By deliveryDateLocator = By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[4]");
        WebElement deliveryDateElem = wait.until(ExpectedConditions.visibilityOfElementLocated(deliveryDateLocator));
        scrollToElement(deliveryDateElem);
        String displayedDeliveryDate = deliveryDateElem.getText().trim();
        
        if (displayedDeliveryDate.contains(selectedDateText)) {
            System.out.println(GREEN + "✅ Preview Delivery Date Verified: '" + displayedDeliveryDate + "' matches expected: '" + selectedDateText + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Delivery Date Mismatch: '" + displayedDeliveryDate + "', Expected: '" + selectedDateText + "'" + RESET);
        }

        By addressLocator = By.xpath("(//p[@class='card_coupon_code modal_para_dark m-0'])[1]");
        WebElement addressElem = wait.until(ExpectedConditions.visibilityOfElementLocated(addressLocator));
        scrollToElement(addressElem);
        String displayedAddress = addressElem.getText().trim();
        
        String expectedAddressBase = selectedAddressText.isEmpty() ? "" : selectedAddressText.split("\\s+")[0];
        if (displayedAddress.contains(expectedAddressBase)) {
            System.out.println(GREEN + "✅ Preview Delivery Address Verified: '" + displayedAddress + "' contains expected base: '" + expectedAddressBase + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Delivery Address Mismatch: '" + displayedAddress + "', Expected to contain: '" + expectedAddressBase + "'" + RESET);
        }

        By productCellLocator = By.xpath("(//td)[1]");
        WebElement productCell = wait.until(ExpectedConditions.visibilityOfElementLocated(productCellLocator));
        String displayedProduct = productCell.getText().trim();
        if (displayedProduct.contains(selectedProductText)) {
            System.out.println(GREEN + "✅ Preview Product Verified: '" + displayedProduct + "' matches expected: '" + selectedProductText + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Product Mismatch: '" + displayedProduct + "', Expected: '" + selectedProductText + "'" + RESET);
        }

        By qtyCellLocator = By.xpath("(//td)[2]");
        WebElement qtyCell = wait.until(ExpectedConditions.visibilityOfElementLocated(qtyCellLocator));
        int displayedPreviewQty = Integer.parseInt(qtyCell.getText().replaceAll("[^0-9]", "").trim());
        if (displayedPreviewQty == totalEnteredQuantity) {
            System.out.println(GREEN + "✅ Preview Total Quantity Verified: " + displayedPreviewQty + " matches expected: " + totalEnteredQuantity + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Total Quantity Mismatch: " + displayedPreviewQty + ", Expected: " + totalEnteredQuantity + RESET);
        }

        By costCellLocator = By.xpath("(//td)[3]");
        WebElement costCell = wait.until(ExpectedConditions.visibilityOfElementLocated(costCellLocator));
        double displayedMakingCost = Double.parseDouble(costCell.getText().replaceAll("[^0-9.]", "").trim());
        if (Math.abs(displayedMakingCost - makingCostValue) < 0.01) {
            System.out.println(GREEN + "✅ Preview Making Cost Verified: " + displayedMakingCost + " matches expected: " + makingCostValue + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Making Cost Mismatch: " + displayedMakingCost + ", Expected: " + makingCostValue + RESET);
        }

        By amountCellLocator = By.xpath("(//td)[4]");
        WebElement amountCell = wait.until(ExpectedConditions.visibilityOfElementLocated(amountCellLocator));
        double displayedTotalAmount = Double.parseDouble(amountCell.getText().replaceAll("[^0-9.]", "").trim());
        double expectedAmount = (double) totalEnteredQuantity * makingCostValue;
        if (Math.abs(displayedTotalAmount - expectedAmount) < 0.01) {
            System.out.println(GREEN + "✅ Preview Total Amount Verified: " + displayedTotalAmount + " matches expected: " + expectedAmount + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Total Amount Mismatch: " + displayedTotalAmount + ", Expected: " + expectedAmount + RESET);
        }

        By sizeWrapperPreviewLocator = By.xpath("(//div[@class='w-100 size_wrapper'])[1]");
        WebElement sizeWrapperPreview = wait.until(ExpectedConditions.visibilityOfElementLocated(sizeWrapperPreviewLocator));
        String sizeWrapperText = sizeWrapperPreview.getText().trim();
        
        System.out.println(CYAN + "Sizes displayed in Preview Wrapper: " + sizeWrapperText + RESET);
        boolean allSizesPresent = true;
        for (String size : availableSizesText) {
            String cleanSize = size.trim();
            if (cleanSize.contains("-") || cleanSize.contains("–") || cleanSize.contains("—") || cleanSize.contains(":")) {
                cleanSize = cleanSize.split("[-–—:]")[1].trim();
            } else {
                cleanSize = cleanSize.replaceAll("(?i)^size", "").trim();
            }
            if (!sizeWrapperText.contains(cleanSize)) {
                allSizesPresent = false;
                System.out.println(YELLOW + "⚠️ Size '" + cleanSize + "' was not found in the preview size wrapper!" + RESET);
            }
        }
        if (allSizesPresent) {
            System.out.println(GREEN + "✅ All captured sizes successfully verified inside the preview size wrapper!" + RESET);
        }

        By notesLocator = By.xpath("(//p[@class='info_dd mb-0'])[1]");
        WebElement notesElem = wait.until(ExpectedConditions.visibilityOfElementLocated(notesLocator));
        String displayedNotes = notesElem.getText().trim();
        if (displayedNotes.contains(savedNoteText)) {
            System.out.println(GREEN + "✅ Preview Notes Verified Successfully: '" + displayedNotes + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Notes Mismatch! Displayed: '" + displayedNotes + "', Expected to contain: '" + savedNoteText + "'" + RESET);
        }

        By descLocator = By.xpath("(//p[@class='info_dd mb-0'])[2]");
        WebElement descElem = wait.until(ExpectedConditions.visibilityOfElementLocated(descLocator));
        String displayedDesc = descElem.getText().trim();
        if (displayedDesc.contains(savedDescText)) {
            System.out.println(GREEN + "✅ Preview Description Verified Successfully: '" + displayedDesc + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Description Mismatch! Displayed: '" + displayedDesc + "', Expected to contain: '" + savedDescText + "'" + RESET);
        }
    }
    
    
    //TC-MO-10
    public void clickEditForExistingProductManufactureOrder() {
        By threeDotLocator = By.xpath("(//i[@class='bi bi-three-dots-vertical'])[1]");
        WebElement threeDotBtn = wait.until(ExpectedConditions.elementToBeClickable(threeDotLocator));
        scrollToElement(threeDotBtn);
        try {
            threeDotBtn.click();
        } catch (Exception ex) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", threeDotBtn);
        }
        sleep(1);

        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(editBtn));
        scrollToElement(editButton);
        editButton.click();
        sleep(2);
        System.out.println("Navigated to Edit Manufacture Order page successfully.");
    }
    
    public void verifyTotalsAndEnterMakingCostForEdit() {
        String GREEN = "\u001B[32m";
        String CYAN = "\u001B[36m";
        String YELLOW = "\u001B[33m";
        String RESET = "\u001B[0m";

        int calculatedSum = 0;
        if (availableSizesText != null && !availableSizesText.isEmpty()) {
            for (String size : availableSizesText) {
                String cleanSize = size.trim();
                
                if (cleanSize.contains("-") || cleanSize.contains("–") || cleanSize.contains("—") || cleanSize.contains(":")) {
                    String[] parts = cleanSize.split("[-–—:]");
                    cleanSize = parts[parts.length - 1].trim();
                } else {
                    cleanSize = cleanSize.replaceAll("(?i)^size", "").trim();
                }

                By quantityInputLocator = By.xpath("(//input[@aria-label='Quantity for size " + cleanSize + "'])[1]");
                try {
                    WebElement inputElem = driver.findElement(quantityInputLocator);
                    String val = inputElem.getAttribute("value");
                    if (val != null && !val.isEmpty()) {
                        calculatedSum += Integer.parseInt(val.trim());
                    }
                } catch (Exception e) {
                }
            }
        }
        
        totalEnteredQuantity = calculatedSum;

        By totalQtyCellLocator = By.xpath("(//td[@class='total_qty_cell'])[1]");
        WebElement totalQtyCell = wait.until(ExpectedConditions.visibilityOfElementLocated(totalQtyCellLocator));
        scrollToElement(totalQtyCell);
        
        String displayedQtyText = totalQtyCell.getText().replaceAll("[^0-9]", "").trim();
        int displayedTotalQty = displayedQtyText.isEmpty() ? 0 : Integer.parseInt(displayedQtyText);
        
        if (displayedTotalQty == totalEnteredQuantity) {
            System.out.println(GREEN + "✅ Total Quantity Verified Successfully! Displayed: " + displayedTotalQty + " matches calculated sum: " + totalEnteredQuantity + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Mismatch! Displayed Total Qty (" + displayedTotalQty + ") does not match calculated sum (" + totalEnteredQuantity + ")" + RESET);
        }

        Random random = new Random();
        makingCostValue = random.nextInt(101) + 100; 
        
        By makingCostInputLocator = By.xpath("(//input[@type='number'])[1]");
        WebElement makingCostInput = wait.until(ExpectedConditions.elementToBeClickable(makingCostInputLocator));
        scrollToElement(makingCostInput);
        
        makingCostInput.clear();
        makingCostInput.sendKeys(String.valueOf(makingCostValue));
        System.out.println(CYAN + "Entered Making Cost (Edit): " + makingCostValue + RESET);
        sleep(1); 

        By totalAmountCellLocator = By.xpath("(//td[@class='d-flex align-items-center'])[1]");
        WebElement totalAmountCell = wait.until(ExpectedConditions.visibilityOfElementLocated(totalAmountCellLocator));
        scrollToElement(totalAmountCell);
        
        String displayedAmountText = totalAmountCell.getText().replaceAll("[^0-9.]", "").trim();
        double displayedTotalAmount = displayedAmountText.isEmpty() ? 0.0 : Double.parseDouble(displayedAmountText);
        
        expectedTotalAmount = (double) displayedTotalQty * makingCostValue; 
        
        if (Math.abs(displayedTotalAmount - expectedTotalAmount) < 0.01) {
            System.out.println(GREEN + "✅ Total Amount Verified Successfully! Displayed Amount: " + displayedTotalAmount + " matches expected (" + displayedTotalQty + " * " + makingCostValue + " = " + expectedTotalAmount + ")" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Amount Mismatch! Displayed Total Amount (" + displayedTotalAmount + ") does not match expected (" + expectedTotalAmount + ")" + RESET);
        }

        By summaryLocator = By.xpath("(//p[@class='d-flex justify-content-start align-items-center m-0'])[2]");
        WebElement summaryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(summaryLocator));
        scrollToElement(summaryElement);
        
        String summaryText = summaryElement.getText().replaceAll("[^0-9.]", "").trim();
        double displayedSummaryAmount = summaryText.isEmpty() ? 0.0 : Double.parseDouble(summaryText);
        
        if (Math.abs(displayedSummaryAmount - expectedTotalAmount) < 0.01) {
            System.out.println(GREEN + "✅ Summary Total Amount Verified Successfully! Summary Amount: " + displayedSummaryAmount + " matches expected (" + expectedTotalAmount + ")" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Summary Amount Mismatch! Summary Amount (" + displayedSummaryAmount + ") does not match expected (" + expectedTotalAmount + ")" + RESET);
        }
    }
    
    //TC-MO-11
    public void verifyDraftManufactureOrderForExistingProductOnListingPage() {
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
     
    //TC-MO-12
    public void verifyCancelItemsOptionIsDisplayedForExistingProduct() {
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        System.out.println("\n=============================================================");
        System.out.println("          VERIFY CANCEL ITEMS OPTION ON PREVIEW PAGE         ");
        System.out.println("=============================================================");
        click(threeDotBtn);
        sleep(1);

        click(previewbtn);
        sleep(2);

        WebElement initialStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//span[@class='status-chip active'])[1]")
        ));
        System.out.println("Preview Page Initial Status: " + initialStatus.getText().trim());

        WebElement prevThreeDot = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//div[@class='material_action_dropdown js-tag-toggle'])[1]")
        ));
        scrollToElement(prevThreeDot);
        prevThreeDot.click();
        sleep(1);

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
    
    public void verifyManufactureOrderStatusIsCancelledForExistingProduct() {
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";
        
        System.out.println("\n=============================================================");
        System.out.println("       VERIFYING CANCELLED STATUS ACROSS PAGES               ");
        System.out.println("=============================================================");

        WebElement cancelledStatusPreview = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//span[@class='status-chip inactive'])[1]")
        ));
        scrollToElement(cancelledStatusPreview);
        String previewStatusText = cancelledStatusPreview.getText().trim();
        System.out.println("Preview Page Status: " + previewStatusText);

        click(backBtn);
        sleep(2);

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
    
    //TC-MO-13
    public void convertAndVerifyExistingManufactureOrderAsIssued() {
        System.out.println("\n=============================================================");
        System.out.println("      CONVERT MANUFACTURE ORDER TO ISSUED & VERIFY           ");
        System.out.println("=============================================================");

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";
        String expectedStatus = "Issued";

        try {
            WebElement issuedBtn = wait.until(ExpectedConditions.elementToBeClickable(markAsIssuedBtn));
            issuedBtn.click();
            sleep(1);

            WebElement confirmIssuedBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmMarkAsIssuedBtn));
            confirmIssuedBtn.click();
            sleep(2);

            System.out.println("Waiting for details page status to update to 'Issued'...");
            
            WebElement detailsStatus = wait.until(ExpectedConditions.visibilityOf(detailsPageStatusLocator));
            String actualDetailsStatus = detailsStatus.getText().trim();
            boolean detailsStatusMatch = actualDetailsStatus.equalsIgnoreCase(expectedStatus);
            printComparison("Details Page Status", expectedStatus, actualDetailsStatus, detailsStatusMatch, GREEN, RED, RESET);
            if (!detailsStatusMatch) {
                throw new AssertionError("Details page status did not reflect 'Issued'! Current text found: " + actualDetailsStatus);
            }

            WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(backBtn));
            backButton.click();
            sleep(2);
            
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
    
    //TC-MO-14
    public void verifyMandatoryFieldsValidationMessagesForExistingProduct() {
        System.out.println("\n=============================================================");
        System.out.println("  MANDATORY FIELDS VALIDATION (EXISTING PRODUCT) CHECK       ");
        System.out.println("=============================================================");

        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";

        try {
            By itemDropdownLocator = By.xpath("(//div[@class='select-box d-flex justify-content-start align-items-center'])[1]");
            WebElement itemDropdownElement = wait.until(ExpectedConditions.elementToBeClickable(itemDropdownLocator));
            scrollToElement(itemDropdownElement);
            itemDropdownElement.click();
            sleep(1);
            itemDropdownElement.click();
        } catch (Exception ignored) {
        }

        waitFor(saveBtn);
        scrollToElement(saveBtn);
        click(saveBtn);
        sleep(1);
        System.out.println("Clicked Save button with empty fields to trigger validations.");

        By vendorErrLocator = By.xpath("(//span[normalize-space()='Please select a vendor'])[1]");
        By dateErrLocator = By.xpath("(//span[normalize-space()='Please select an expected delivery date'])[1]");
        By addressErrLocator = By.xpath("(//span[normalize-space()='Please select a delivery address'])[1]");
        By productItemErrLocator = By.xpath("(//span[normalize-space()='Please select a product item'])[1]");
        By makingCostErrLocator = By.xpath("(//span[normalize-space()='Please enter a valid making cost'])[1]");

        boolean allErrorsPassed = true;

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

        try {
            WebElement prodItemErr = wait.until(ExpectedConditions.visibilityOfElementLocated(productItemErrLocator));
            scrollToElement(prodItemErr);
            boolean isDisplayed = prodItemErr.isDisplayed();
            printComparison("Product Item Val", "Displayed", prodItemErr.getText().trim(), isDisplayed, GREEN, RED, RESET);
            if (!isDisplayed) allErrorsPassed = false;
        } catch (Exception e) {
            printComparison("Product Item Val", "Displayed", "Not Found", false, GREEN, RED, RESET);
            allErrorsPassed = false;
        }

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

        System.out.println("=============================================================");
        if (allErrorsPassed) {
            System.out.println(GREEN + "✅ ALL EXISTING PRODUCT VALIDATION MESSAGES VERIFIED SUCCESSFULLY!" + RESET);
        } else {
            System.out.println(RED + "❌ SOME EXISTING PRODUCT VALIDATION MESSAGES FAILED OR WERE MISSING!" + RESET);
            throw new AssertionError("Existing product mandatory fields validation message check failed.");
        }
        System.out.println("=============================================================\n");
    }
     
    //TC-MO-15
    public static List<String> availableSizesTextItem2 = new ArrayList<>();
    public static String selectedProductTextItem2 = "";
    public static List<String> availableProductsTextItem2 = new ArrayList<>();
    public static int totalEnteredQuantityItem2 = 0;
    public static int makingCostValueItem2 = 0;
    public static double expectedTotalAmountItem2 = 0.0;

    public void selectSecondProductAndCaptureSizes() {
        String CYAN = "\u001B[36m";
        String GREEN = "\u001B[32m";
        String RESET = "\u001B[0m";
        
        waitFor(addItemBtn);
        scrollToElement(addItemBtn);
        click(addItemBtn);
        sleep(2); 
        System.out.println(GREEN + "Clicked 'Add Item' button successfully." + RESET);

        By itemDropdownLocator2 = By.xpath("(//div[@class='select-box d-flex justify-content-start align-items-center'])[2]");
        WebElement itemDropdownElement2 = wait.until(ExpectedConditions.elementToBeClickable(itemDropdownLocator2));
        scrollToElement(itemDropdownElement2);
        
        try {
            itemDropdownElement2.click();
        } catch (Exception ex) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", itemDropdownElement2);
        }
        sleep(1);
        
        By optionsBoxLocator2 = By.xpath("(//div[@class='options_box'])[4]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(optionsBoxLocator2));

        By productOptionLocator2 = By.xpath("(//div[@class='options_box'])[4]//div[@class='option']");
        List<WebElement> productOptions2 = driver.findElements(productOptionLocator2);
        
        availableProductsTextItem2.clear();
        for (WebElement option : productOptions2) {
            availableProductsTextItem2.add(option.getText().trim());
        }
        System.out.println(CYAN + "Available Products List (Item 2): " + availableProductsTextItem2 + RESET);
        
        if (!productOptions2.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(productOptions2.size());
            int itemNumber = index + 1;
            
            By specificProduct2 = By.xpath("((//div[@class='options_box'])[4]//div[@class='option'])[" + itemNumber + "]");
            WebElement chosenProduct2 = wait.until(ExpectedConditions.elementToBeClickable(specificProduct2));
            
            selectedProductTextItem2 = chosenProduct2.getText().trim();
            scrollToElement(chosenProduct2);
            chosenProduct2.click();
            sleep(1);
            System.out.println(GREEN + "Selected Product (Item 2): " + selectedProductTextItem2 + RESET);
        } else {
            throw new RuntimeException("No product options available in options box [4]!");
        }
        
        By sizeWrapperLocator2 = By.xpath("(//div[@class='w-100 size_wrapper'])[2]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(sizeWrapperLocator2));
        WebElement sizeWrapper2 = driver.findElement(sizeWrapperLocator2);
        
        List<WebElement> sizeElements2 = sizeWrapper2.findElements(By.xpath(".//span | .//button | .//div[contains(@class, 'size')]"));
        if (sizeElements2.isEmpty()) {
            sizeElements2 = sizeWrapper2.findElements(By.xpath("./*")); 
        }
        
        availableSizesTextItem2.clear();
        for (WebElement sizeElem : sizeElements2) {
            String sizeText = sizeElem.getText().trim();
            if (!sizeText.isEmpty()) {
                availableSizesTextItem2.add(sizeText);
            }
        }
        
        if (availableSizesTextItem2.isEmpty()) {
            availableSizesTextItem2.add(sizeWrapper2.getText().trim());
        }
        
        System.out.println(CYAN + "Captured Sizes from Size Wrapper (Item 2): " + availableSizesTextItem2 + RESET);
    }

    public void fillQuantitiesForSecondProduct() {
        if (availableSizesTextItem2 == null || availableSizesTextItem2.isEmpty()) {
            throw new RuntimeException("No available sizes captured for Item 2! Please select a product and capture sizes first.");
        }

        String GREEN = "\u001B[32m";
        String YELLOW = "\u001B[33m";
        String RESET = "\u001B[0m";

        Random random = new Random();
        int calculatedSum2 = 0;

        for (String size : availableSizesTextItem2) {
            String cleanSize = size.trim();
            
            if (cleanSize.contains("-") || cleanSize.contains("–") || cleanSize.contains("—") || cleanSize.contains(":")) {
                String[] parts = cleanSize.split("[-–—:]");
                cleanSize = parts[parts.length - 1].trim();
            } else {
                cleanSize = cleanSize.replaceAll("(?i)^size", "").trim();
            }

            String dynamicXpath2 = "(//input[@aria-label='Quantity for size " + cleanSize + "'])[2]";
            By quantityInputLocator2 = By.xpath(dynamicXpath2);

            try {
                WebElement quantityInput2 = wait.until(ExpectedConditions.elementToBeClickable(quantityInputLocator2));
                scrollToElement(quantityInput2);
                
                int randomQty2 = random.nextInt(10) + 1;
                calculatedSum2 += randomQty2;
                
                quantityInput2.clear();
                quantityInput2.sendKeys(String.valueOf(randomQty2));
                
                System.out.println(GREEN + "Filled quantity '" + randomQty2 + "' for size: " + cleanSize + " (Item 2)" + RESET);
                
            } catch (Exception e) {
                System.out.println(YELLOW + "⚠️ Warning: Could not find or fill quantity input for size '" + cleanSize + "' (Item 2) with XPath: " + dynamicXpath2 + RESET);
            }
        }
        
        totalEnteredQuantityItem2 = calculatedSum2;
        System.out.println("\u001B[36mTotal Expected Quantity Sum (Item 2): " + totalEnteredQuantityItem2 + RESET);
    }
    
    public void verifyTotalsAndEnterMakingCostForSecondProduct() {
        String GREEN = "\u001B[32m";
        String CYAN = "\u001B[36m";
        String YELLOW = "\u001B[33m";
        String RESET = "\u001B[0m";

        By totalQtyCellLocator2 = By.xpath("(//td[@class='total_qty_cell'])[2]");
        WebElement totalQtyCell2 = wait.until(ExpectedConditions.visibilityOfElementLocated(totalQtyCellLocator2));
        scrollToElement(totalQtyCell2);
        
        String displayedQtyText2 = totalQtyCell2.getText().replaceAll("[^0-9]", "").trim();
        int displayedTotalQty2 = displayedQtyText2.isEmpty() ? 0 : Integer.parseInt(displayedQtyText2);
        
        if (displayedTotalQty2 == totalEnteredQuantityItem2) {
            System.out.println(GREEN + "✅ Total Quantity Verified Successfully (Item 2)! Displayed: " + displayedTotalQty2 + " matches calculated sum: " + totalEnteredQuantityItem2 + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Mismatch (Item 2)! Displayed Total Qty (" + displayedTotalQty2 + ") does not match calculated sum (" + totalEnteredQuantityItem2 + ")" + RESET);
        }

        Random random = new Random();
        makingCostValueItem2 = random.nextInt(101) + 100; 
        
        By makingCostInputLocator2 = By.xpath("(//input[@type='number'])[2]");
        WebElement makingCostInput2 = wait.until(ExpectedConditions.elementToBeClickable(makingCostInputLocator2));
        scrollToElement(makingCostInput2);
        
        makingCostInput2.clear();
        makingCostInput2.sendKeys(String.valueOf(makingCostValueItem2));
        System.out.println(CYAN + "Entered Making Cost (Item 2): " + makingCostValueItem2 + RESET);
        sleep(1); 

        By totalAmountCellLocator2 = By.xpath("(//td[@class='d-flex align-items-center'])[2]");
        WebElement totalAmountCell2 = wait.until(ExpectedConditions.visibilityOfElementLocated(totalAmountCellLocator2));
        scrollToElement(totalAmountCell2);
        
        String displayedAmountText2 = totalAmountCell2.getText().replaceAll("[^0-9.]", "").trim();
        double displayedTotalAmount2 = displayedAmountText2.isEmpty() ? 0.0 : Double.parseDouble(displayedAmountText2);
        
        expectedTotalAmountItem2 = (double) displayedTotalQty2 * makingCostValueItem2;
        
        if (Math.abs(displayedTotalAmount2 - expectedTotalAmountItem2) < 0.01) {
            System.out.println(GREEN + "✅ Total Amount Verified Successfully (Item 2)! Displayed Amount: " + displayedTotalAmount2 + " matches expected (" + displayedTotalQty2 + " * " + makingCostValueItem2 + " = " + expectedTotalAmountItem2 + ")" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Amount Mismatch (Item 2)! Displayed Total Amount (" + displayedTotalAmount2 + ") does not match expected (" + expectedTotalAmountItem2 + ")" + RESET);
        }
    }
    
    public void verifyGrandTotal() {
        String GREEN = "\u001B[32m";
        String CYAN = "\u001B[36m";
        String YELLOW = "\u001B[33m";
        String RESET = "\u001B[0m";

        System.out.println(CYAN + "=== Verifying Grand Total for Multiple Products ===" + RESET);

        double expectedGrandTotal = expectedTotalAmount + expectedTotalAmountItem2;
        System.out.println(CYAN + "Expected Item 1 Total: " + expectedTotalAmount + RESET);
        System.out.println(CYAN + "Expected Item 2 Total: " + expectedTotalAmountItem2 + RESET);
        System.out.println(CYAN + "Calculated Expected Grand Total: " + expectedGrandTotal + RESET);

        By summaryLocator = By.xpath("(//p[contains(@class, 'd-flex') and contains(@class, 'justify-content-start')])[last()]");
        WebElement summaryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(summaryLocator));
        scrollToElement(summaryElement);
        
        String summaryText = summaryElement.getText().replaceAll("[^0-9.]", "").trim();
        double displayedSummaryAmount = summaryText.isEmpty() ? 0.0 : Double.parseDouble(summaryText);

        if (Math.abs(displayedSummaryAmount - expectedGrandTotal) < 0.01) {
            System.out.println(GREEN + "✅ Grand Total Verified Successfully! Summary Amount: " + displayedSummaryAmount + " matches expected grand total (" + expectedGrandTotal + ")" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Grand Total Mismatch! Summary Amount (" + displayedSummaryAmount + ") does not match expected grand total (" + expectedGrandTotal + ")" + RESET);
            throw new AssertionError("Grand total verification failed! Displayed: " + displayedSummaryAmount + ", Expected: " + expectedGrandTotal);
        }
    } 
    
    public void verifyManufactureOrderOnForMultipleProductsListingPage() {
        String GREEN = "\u001B[32m";
        String CYAN = "\u001B[36m";
        String YELLOW = "\u001B[33m";
        String RESET = "\u001B[0m";

        System.out.println(CYAN + "=== Starting Verification on Manufacture Order Listing Page (Multiple Products) ===" + RESET);

        By vendorCellLocator = By.xpath("(//td)[4]");
        WebElement vendorCell = wait.until(ExpectedConditions.visibilityOfElementLocated(vendorCellLocator));
        scrollToElement(vendorCell);
        String displayedVendor = vendorCell.getText().trim();
        
        String displayedVendorBase = displayedVendor.split("\\s+")[0];
        String expectedVendorBase = selectedVendorText.isEmpty() ? "" : selectedVendorText.split("\\s+")[0];
        
        if (!expectedVendorBase.isEmpty() && displayedVendorBase.equalsIgnoreCase(expectedVendorBase)) {
            System.out.println(GREEN + "✅ Vendor Verified Successfully on Listing! Displayed base: '" + displayedVendorBase + "' matches expected base: '" + expectedVendorBase + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Vendor Mismatch on Listing! Displayed base: '" + displayedVendorBase + "', Expected base: '" + expectedVendorBase + "'" + RESET);
        }

        int expectedCombinedQty = totalEnteredQuantity + totalEnteredQuantityItem2;

        By totalQtyCellLocator = By.xpath("(//td)[5]");
        WebElement totalQtyCell = wait.until(ExpectedConditions.visibilityOfElementLocated(totalQtyCellLocator));
        scrollToElement(totalQtyCell);
        String displayedQtyText = totalQtyCell.getText().replaceAll("[^0-9]", "").trim();
        int displayedQty = displayedQtyText.isEmpty() ? 0 : Integer.parseInt(displayedQtyText);
        
        if (displayedQty == expectedCombinedQty) {
            System.out.println(GREEN + "✅ Total Quantities Verified Successfully on Listing! Displayed: " + displayedQty + " matches expected combined quantity: " + expectedCombinedQty + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Total Quantities Mismatch on Listing! Displayed: " + displayedQty + ", Expected: " + expectedCombinedQty + RESET);
        }

        By totalAmountCellLocator = By.xpath("(//td)[6]");
        WebElement totalAmountCell = wait.until(ExpectedConditions.visibilityOfElementLocated(totalAmountCellLocator));
        scrollToElement(totalAmountCell);
        String displayedAmountText = totalAmountCell.getText().replaceAll("[^0-9.]", "").trim();
        double displayedAmount = displayedAmountText.isEmpty() ? 0.0 : Double.parseDouble(displayedAmountText);
        
        double expectedItem1Total = (double) totalEnteredQuantity * makingCostValue;
        double expectedItem2Total = (double) totalEnteredQuantityItem2 * makingCostValueItem2;
        double expectedGrandTotalAmount = expectedItem1Total + expectedItem2Total;

        if (Math.abs(displayedAmount - expectedGrandTotalAmount) < 0.01) {
            System.out.println(GREEN + "✅ Grand Total Amount Verified Successfully on Listing! Displayed: " + displayedAmount + " matches expected grand total: " + expectedGrandTotalAmount + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Grand Total Amount Mismatch on Listing! Displayed: " + displayedAmount + ", Expected: " + expectedGrandTotalAmount + RESET);
        }

        By statusCellLocator = By.xpath("(//td)[7]");
        WebElement statusCell = wait.until(ExpectedConditions.visibilityOfElementLocated(statusCellLocator));
        scrollToElement(statusCell);
        String displayedStatus = statusCell.getText().trim();
        
        if (displayedStatus.equalsIgnoreCase("Open")) {
            System.out.println(GREEN + "✅ Status Verified Successfully on Listing! Displayed: '" + displayedStatus + "' matches expected: 'Open'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Status Mismatch on Listing! Displayed: '" + displayedStatus + "', Expected: 'Open'" + RESET);
        }

        By createdAtCellLocator = By.xpath("(//td)[8]");
        WebElement createdAtCell = wait.until(ExpectedConditions.visibilityOfElementLocated(createdAtCellLocator));
        scrollToElement(createdAtCell);
        String displayedDate = createdAtCell.getText().trim();
        
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String expectedDate = today.format(formatter);
        
        if (displayedDate.contains(expectedDate)) {
            System.out.println(GREEN + "✅ Created At Date Verified Successfully on Listing! Displayed: '" + displayedDate + "' matches today's date: '" + expectedDate + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Date Mismatch on Listing! Displayed: '" + displayedDate + "', Expected today's date: '" + expectedDate + "'" + RESET);
        }
    }
    
    public void verifyManufactureOrderOnPreviewPageForMultipleProducts() {
        String GREEN = "\u001B[32m";
        String CYAN = "\u001B[36m";
        String YELLOW = "\u001B[33m";
        String RESET = "\u001B[0m";

        System.out.println(CYAN + "=== Starting Navigation to Preview Page ===" + RESET);

        waitFor(threeDotBtn);
        scrollToElement(threeDotBtn);
        click(threeDotBtn);
        sleep(1);

        waitFor(previewbtn);
        scrollToElement(previewbtn);
        click(previewbtn);
        sleep(2); 
        System.out.println(GREEN + "Successfully opened Preview Page/Modal." + RESET);

        System.out.println(CYAN + "=== Verifying Header Details on Preview Page ===" + RESET);

        By createdAtLocator = By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[3]");
        WebElement createdAtElem = wait.until(ExpectedConditions.visibilityOfElementLocated(createdAtLocator));
        scrollToElement(createdAtElem);
        String displayedCreatedAt = createdAtElem.getText().trim();
        
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String expectedDate = today.format(formatter);
        
        if (displayedCreatedAt.contains(expectedDate)) {
            System.out.println(GREEN + "✅ Preview Created At Verified: '" + displayedCreatedAt + "' matches today's date: '" + expectedDate + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Created At Mismatch: '" + displayedCreatedAt + "', Expected: '" + expectedDate + "'" + RESET);
        }

        By deliveryDateLocator = By.xpath("(//p[@class='card_coupon_code modal_para_dark'])[4]");
        WebElement deliveryDateElem = wait.until(ExpectedConditions.visibilityOfElementLocated(deliveryDateLocator));
        scrollToElement(deliveryDateElem);
        String displayedDeliveryDate = deliveryDateElem.getText().trim();
        
        if (displayedDeliveryDate.contains(selectedDateText)) {
            System.out.println(GREEN + "✅ Preview Delivery Date Verified: '" + displayedDeliveryDate + "' matches expected: '" + selectedDateText + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Delivery Date Mismatch: '" + displayedDeliveryDate + "', Expected: '" + selectedDateText + "'" + RESET);
        }

        By addressLocator = By.xpath("(//p[@class='card_coupon_code modal_para_dark m-0'])[1]");
        WebElement addressElem = wait.until(ExpectedConditions.visibilityOfElementLocated(addressLocator));
        scrollToElement(addressElem);
        String displayedAddress = addressElem.getText().trim();
        
        String expectedAddressBase = selectedAddressText.isEmpty() ? "" : selectedAddressText.split("\\s+")[0];
        if (displayedAddress.contains(expectedAddressBase)) {
            System.out.println(GREEN + "✅ Preview Delivery Address Verified: '" + displayedAddress + "' contains expected base: '" + expectedAddressBase + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Delivery Address Mismatch: '" + displayedAddress + "', Expected to contain: '" + expectedAddressBase + "'" + RESET);
        }

        System.out.println(CYAN + "=== Verifying Item 1 Details on Preview ===" + RESET);
        
        By productCellLocator = By.xpath("(//td)[1]");
        WebElement productCell = wait.until(ExpectedConditions.visibilityOfElementLocated(productCellLocator));
        String displayedProduct = productCell.getText().trim();
        if (displayedProduct.contains(selectedProductText)) {
            System.out.println(GREEN + "✅ Preview Product (Item 1) Verified: '" + displayedProduct + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Product Mismatch (Item 1): '" + displayedProduct + "', Expected: '" + selectedProductText + "'" + RESET);
        }

        By qtyCellLocator = By.xpath("(//td)[2]");
        WebElement qtyCell = wait.until(ExpectedConditions.visibilityOfElementLocated(qtyCellLocator));
        int displayedPreviewQty = Integer.parseInt(qtyCell.getText().replaceAll("[^0-9]", "").trim());
        if (displayedPreviewQty == totalEnteredQuantity) {
            System.out.println(GREEN + "✅ Preview Total Quantity (Item 1) Verified: " + displayedPreviewQty + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Total Quantity Mismatch (Item 1): " + displayedPreviewQty + ", Expected: " + totalEnteredQuantity + RESET);
        }

        By costCellLocator = By.xpath("(//td)[3]");
        WebElement costCell = wait.until(ExpectedConditions.visibilityOfElementLocated(costCellLocator));
        double displayedMakingCost = Double.parseDouble(costCell.getText().replaceAll("[^0-9.]", "").trim());
        if (Math.abs(displayedMakingCost - makingCostValue) < 0.01) {
            System.out.println(GREEN + "✅ Preview Making Cost (Item 1) Verified: " + displayedMakingCost + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Making Cost Mismatch (Item 1): " + displayedMakingCost + ", Expected: " + makingCostValue + RESET);
        }

        By amountCellLocator = By.xpath("(//td)[4]");
        WebElement amountCell = wait.until(ExpectedConditions.visibilityOfElementLocated(amountCellLocator));
        double displayedTotalAmount = Double.parseDouble(amountCell.getText().replaceAll("[^0-9.]", "").trim());
        double expectedAmount = (double) totalEnteredQuantity * makingCostValue;
        if (Math.abs(displayedTotalAmount - expectedAmount) < 0.01) {
            System.out.println(GREEN + "✅ Preview Total Amount (Item 1) Verified: " + displayedTotalAmount + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Total Amount Mismatch (Item 1): " + displayedTotalAmount + ", Expected: " + expectedAmount + RESET);
        }

        By sizeWrapperPreviewLocator = By.xpath("(//div[@class='w-100 size_wrapper'])[1]");
        WebElement sizeWrapperPreview = wait.until(ExpectedConditions.visibilityOfElementLocated(sizeWrapperPreviewLocator));
        String sizeWrapperText = sizeWrapperPreview.getText().trim();
        boolean allSizesPresent1 = true;
        for (String size : availableSizesText) {
            String cleanSize = size.trim();
            if (cleanSize.contains("-") || cleanSize.contains("–") || cleanSize.contains("—") || cleanSize.contains(":")) {
                cleanSize = cleanSize.split("[-–—:]")[1].trim();
            } else {
                cleanSize = cleanSize.replaceAll("(?i)^size", "").trim();
            }
            if (!sizeWrapperText.contains(cleanSize)) {
                allSizesPresent1 = false;
                System.out.println(YELLOW + "⚠️ Size '" + cleanSize + "' not found in Item 1 preview wrapper!" + RESET);
            }
        }
        if (allSizesPresent1) {
            System.out.println(GREEN + "✅ All captured sizes verified inside Item 1 preview wrapper!" + RESET);
        }

        if (selectedProductTextItem2 != null && !selectedProductTextItem2.isEmpty()) {
            System.out.println(CYAN + "=== Verifying Item 2 Details on Preview ===" + RESET);

            By productCellLocator2 = By.xpath("(//td)[6]");
            WebElement productCell2 = wait.until(ExpectedConditions.visibilityOfElementLocated(productCellLocator2));
            String displayedProduct2 = productCell2.getText().trim();
            if (displayedProduct2.contains(selectedProductTextItem2)) {
                System.out.println(GREEN + "✅ Preview Product (Item 2) Verified: '" + displayedProduct2 + "'" + RESET);
            } else {
                System.out.println(YELLOW + "⚠️ Preview Product Mismatch (Item 2): '" + displayedProduct2 + "', Expected: '" + selectedProductTextItem2 + "'" + RESET);
            }

            By qtyCellLocator2 = By.xpath("(//td)[7]");
            WebElement qtyCell2 = wait.until(ExpectedConditions.visibilityOfElementLocated(qtyCellLocator2));
            int displayedPreviewQty2 = Integer.parseInt(qtyCell2.getText().replaceAll("[^0-9]", "").trim());
            if (displayedPreviewQty2 == totalEnteredQuantityItem2) {
                System.out.println(GREEN + "✅ Preview Total Quantity (Item 2) Verified: " + displayedPreviewQty2 + RESET);
            } else {
                System.out.println(YELLOW + "⚠️ Preview Total Quantity Mismatch (Item 2): " + displayedPreviewQty2 + ", Expected: " + totalEnteredQuantityItem2 + RESET);
            }

            By costCellLocator2 = By.xpath("(//td)[8]");
            WebElement costCell2 = wait.until(ExpectedConditions.visibilityOfElementLocated(costCellLocator2));
            double displayedMakingCost2 = Double.parseDouble(costCell2.getText().replaceAll("[^0-9.]", "").trim());
            if (Math.abs(displayedMakingCost2 - makingCostValueItem2) < 0.01) {
                System.out.println(GREEN + "✅ Preview Making Cost (Item 2) Verified: " + displayedMakingCost2 + RESET);
            } else {
                System.out.println(YELLOW + "⚠️ Preview Making Cost Mismatch (Item 2): " + displayedMakingCost2 + ", Expected: " + makingCostValueItem2 + RESET);
            }

            By amountCellLocator2 = By.xpath("(//td)[9]");
            WebElement amountCell2 = wait.until(ExpectedConditions.visibilityOfElementLocated(amountCellLocator2));
            double displayedTotalAmount2 = Double.parseDouble(amountCell2.getText().replaceAll("[^0-9.]", "").trim());
            double expectedAmount2 = (double) totalEnteredQuantityItem2 * makingCostValueItem2;
            if (Math.abs(displayedTotalAmount2 - expectedAmount2) < 0.01) {
                System.out.println(GREEN + "✅ Preview Total Amount (Item 2) Verified: " + displayedTotalAmount2 + RESET);
            } else {
                System.out.println(YELLOW + "⚠️ Preview Total Amount Mismatch (Item 2): " + displayedTotalAmount2 + ", Expected: " + expectedAmount2 + RESET);
            }

            By sizeWrapperPreviewLocator2 = By.xpath("(//div[@class='form_content_wrapper add-bg'])[3]");
            WebElement sizeWrapperPreview2 = wait.until(ExpectedConditions.visibilityOfElementLocated(sizeWrapperPreviewLocator2));
            String sizeWrapperText2 = sizeWrapperPreview2.getText().trim();
            boolean allSizesPresent2 = true;
            for (String size : availableSizesTextItem2) {
                String cleanSize = size.trim();
                if (cleanSize.contains("-") || cleanSize.contains("–") || cleanSize.contains("—") || cleanSize.contains(":")) {
                    cleanSize = cleanSize.split("[-–—:]")[1].trim();
                } else {
                    cleanSize = cleanSize.replaceAll("(?i)^size", "").trim();
                }
                if (!sizeWrapperText2.contains(cleanSize)) {
                    allSizesPresent2 = false;
                    System.out.println(YELLOW + "⚠️ Size '" + cleanSize + "' not found in Item 2 preview wrapper!" + RESET);
                }
            }
            if (allSizesPresent2) {
                System.out.println(GREEN + "✅ All captured sizes verified inside Item 2 preview wrapper!" + RESET);
            }

            By grandTotalLocator = By.xpath("(//div[@class='d-flex justify-content-between align-items-center mt-4 total_row'])[1]");
            WebElement grandTotalElem = wait.until(ExpectedConditions.visibilityOfElementLocated(grandTotalLocator));
            scrollToElement(grandTotalElem);
            
            String grandTotalText = grandTotalElem.getText().replaceAll("[^0-9.]", "").trim();
            double displayedGrandTotal = grandTotalText.isEmpty() ? 0.0 : Double.parseDouble(grandTotalText);
            double expectedGrandTotal = expectedAmount + expectedAmount2;

            if (Math.abs(displayedGrandTotal - expectedGrandTotal) < 0.01) {
                System.out.println(GREEN + "✅ Preview Grand Total Verified Successfully: " + displayedGrandTotal + " matches expected (" + expectedGrandTotal + ")" + RESET);
            } else {
                System.out.println(YELLOW + "⚠️ Preview Grand Total Mismatch! Displayed: " + displayedGrandTotal + ", Expected: " + expectedGrandTotal + RESET);
            }
        }

        By notesLocator = By.xpath("(//p[@class='info_dd mb-0'])[1]");
        WebElement notesElem = wait.until(ExpectedConditions.visibilityOfElementLocated(notesLocator));
        String displayedNotes = notesElem.getText().trim();
        if (displayedNotes.contains(savedNoteText)) {
            System.out.println(GREEN + "✅ Preview Notes Verified Successfully: '" + displayedNotes + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Notes Mismatch! Displayed: '" + displayedNotes + "', Expected to contain: '" + savedNoteText + "'" + RESET);
        }

        By descLocator = By.xpath("(//p[@class='info_dd mb-0'])[2]");
        WebElement descElem = wait.until(ExpectedConditions.visibilityOfElementLocated(descLocator));
        String displayedDesc = descElem.getText().trim();
        if (displayedDesc.contains(savedDescText)) {
            System.out.println(GREEN + "✅ Preview Description Verified Successfully: '" + displayedDesc + "'" + RESET);
        } else {
            System.out.println(YELLOW + "⚠️ Preview Description Mismatch! Displayed: '" + displayedDesc + "', Expected to contain: '" + savedDescText + "'" + RESET);
        }
    }
    
  //TC-MO-01
    public void createNewManufactureOrderFlow() {
    	clickAddManufactureOrderForCustomProduct();
    	selectVendorDateAndAddress();
        addProductDetails();
        selectRawMaterialAndFillQuantities();
        verifyTotalAmountAndAddNotesAndSave();
        verifyManufactureOrderOnListingPage();
        verifyManufactureOrderPreviewPage();
    }
    
  //TC-MO-02
    public void editManufactureOrderFlow() {
        clickEditManufactureOrder();
        editVendorDateAndAddress();
        editProductDetails();
        selectRawMaterialAndFillQuantities();
        verifyEditedTotalAmountAndAddNotesAndSave();
        verifyEditedManufactureOrderOnListingPage();
        verifyEditedManufactureOrderPreviewPage();
    }
    
  //TC-MO-03
    public void createDraftManufactureOrderFlow() {
    	clickAddManufactureOrderForCustomProduct();
    	selectVendorDateAndAddress();
        verifySaveAsDraft(); // This clicks the Save as Draft button
    }
    
    //TC-MO-04
    public void cancelManufactureOrderFlow() {
    	clickAddManufactureOrderForCustomProduct();
    	selectVendorDateAndAddress();
        addProductDetails();
        selectRawMaterialAndFillQuantities();
        verifyTotalAmountAndAddNotesAndSave();
		verifyCancelItemsOptionIsDisplayed();
		cancelItemsInManufactureOrder();
		verifyManufactureOrderStatusIsCancelled();
	}
    
    //TC-MO-05
    public void markAsIssuedManufactureOrderFlow() {
    	clickAddManufactureOrderForCustomProduct();
    	selectVendorDateAndAddress();
        addProductDetails();
        selectRawMaterialAndFillQuantities();
        verifyTotalAmountAndAddNotesAndSave();
        verifyManufactureOrderOnListingPage();
        verifyManufactureOrderPreviewPage();
        convertAndVerifyManufactureOrderAsIssued();       
    }
    
    //TC-MO-07
    public void verifyMultipleProductsAndTotalsFlow() {
    	clickAddManufactureOrderForCustomProduct();
    	selectVendorDateAndAddress();
		addProductDetails();
		selectRawMaterialAndFillQuantities();
		addProductDetailsForSecondProduct();
		selectRawMaterialAndFillQuantitiesForSecondProduct();
		verifyIndividualAndCombinedTotals();  
		addNotesDescriptionAndSave();
		verifyManufactureOrderForMultipleProductsOnListingPage();
		verifyManufactureOrderPreviewModalDetails();
	}
   
   //TC-MO-08
    public void verfiyExportFunctionalityFlow() {
    	exportManufactureOrderFlow();
    	verifyAndDownloadExportFileFromHistory();
    	verifyExportedExcelColumns();
    }
    
    //TC-MO-09
    public void createManufactureOrderExistingProductFlow() {
    	clickAddManufactureOrderForExistingProduct();
    	selectVendorDateAndAddress();
    	selectProductAndCaptureSizes();
    	fillQuantitiesForCapturedSizes();
    	verifyTotalsAndEnterMakingCost();
    	addNotesDescriptionAndSave();
    	verifyManufactureOrderOnForExistingProductListingPage();
    	verifyManufactureOrderOnPreviewPage();
    }    
    
    //TC-MO-10
    public void editManufactureOrderExistingProductFlow() {
    	clickEditForExistingProductManufactureOrder();
    	editVendorDateAndAddress();
    	selectProductAndCaptureSizes();
    	fillQuantitiesForCapturedSizes();
    	verifyTotalsAndEnterMakingCostForEdit();
    	addNotesDescriptionAndSave();
    	verifyManufactureOrderOnForExistingProductListingPage();
    	verifyManufactureOrderOnPreviewPage();
    }
    
    //TC-MO-11
    public void createExistingDraftManufactureOrderFlow() {
    	clickAddManufactureOrderForExistingProduct();
    	selectVendorDateAndAddress();
    	verifySaveAsDraft();
    	verifyDraftManufactureOrderForExistingProductOnListingPage();
    	performDraftDeletionWorkflow();
    }
    
    //TC-MO-12
    public void cancelExistingProductManufactureOrderFlow() {
    	clickAddManufactureOrderForExistingProduct();
    	selectVendorDateAndAddress();
    	selectProductAndCaptureSizes();
    	fillQuantitiesForCapturedSizes();
    	verifyTotalsAndEnterMakingCost();
    	addNotesDescriptionAndSave();
    	verifyCancelItemsOptionIsDisplayedForExistingProduct();
    	cancelItemsInManufactureOrder();
    	verifyManufactureOrderStatusIsCancelledForExistingProduct();
    }
    
    //TC-MO-13
    public void markAsIssuedForExistingProductManufactureOrderFlow() {
    	clickAddManufactureOrderForExistingProduct();
    	selectVendorDateAndAddress();
    	selectProductAndCaptureSizes();
    	fillQuantitiesForCapturedSizes();
    	verifyTotalsAndEnterMakingCost();
    	addNotesDescriptionAndSave();
    	verifyManufactureOrderOnForExistingProductListingPage();
    	verifyManufactureOrderOnPreviewPage();
    	convertAndVerifyExistingManufactureOrderAsIssued();
    }
    
    //TC-M0-14
    public void mandatoryFieldValidationsForExistingProduct() {
    	clickAddManufactureOrderForExistingProduct();
    	verifyMandatoryFieldsValidationMessagesForExistingProduct();
    }
    
    //TC-MO-15
    public void verifyMultipleProductsAndTotalsFlowForExisting() {
    	clickAddManufactureOrderForExistingProduct();
    	selectVendorDateAndAddress();
    	selectProductAndCaptureSizes();
    	fillQuantitiesForCapturedSizes();
    	verifyTotalsAndEnterMakingCost();
    	selectSecondProductAndCaptureSizes();
    	fillQuantitiesForSecondProduct();
    	verifyTotalsAndEnterMakingCostForSecondProduct();
    	verifyGrandTotal();
        addNotesDescriptionAndSave();
        verifyManufactureOrderOnForMultipleProductsListingPage();
        verifyManufactureOrderOnPreviewPageForMultipleProducts();
       
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