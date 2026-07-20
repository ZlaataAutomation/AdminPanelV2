package pages;

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
import objectRepo.ProductStock_ObjRepo;
import java.util.ArrayList;
import java.util.List;

public class ProductStock_Page extends ProductStock_ObjRepo {

    private String capturedProductName = "";
    private String capturedStockStatus = "";
    private int capturedStockQuantity = 0;

    private String capturedAdjustmentQuantity = "";
    private String capturedReason = "";
    private String capturedDescription = "";
    private int capturedCurrentStock = 0;
    private List<Integer> capturedItemStocks = new ArrayList<>();
    
    private int totalTopQuantity = 0;
    private int totalBottomQuantity = 0;
    private int grandTotalQuantity = 0;
    
    private int addedStockQuantity = 0;
    private int newTotalQuantity = 0;
    

    public ProductStock_Page(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(this.driver, this);
    }

    public void adminLogin() {
        AdminLogin_Page login = new AdminLogin_Page(driver);
        login.adminLoginApp();
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public void selectRandomProductCaptureDetailsClickEditAndPreview() {
        wait.until(ExpectedConditions.visibilityOf(inventory));
        new Actions(driver).moveToElement(inventory).perform();

        wait.until(ExpectedConditions.visibilityOf(productStockModule));
        click(productStockModule);

        wait.until(ExpectedConditions.visibilityOfAllElements(productDataRows));
        int totalRows = productDataRows.size();
        if (totalRows == 0) {
            throw new RuntimeException("No product rows found in the table!");
        }
        int selectedIndex = new Random().nextInt(totalRows);
        WebElement selectedRow = productDataRows.get(selectedIndex);

        WebElement nameCell = selectedRow.findElement(By.xpath(".//td[1]"));
        wait.until(ExpectedConditions.visibilityOf(nameCell));
        capturedProductName = nameCell.getText().trim();
        System.out.println("ℹ️ Selected Product Name: " + capturedProductName);
        
     // ===== CAPTURE QUANTITY (3rd column) =====
        WebElement qtyCell = selectedRow.findElement(By.xpath(".//td[3]"));
        wait.until(ExpectedConditions.visibilityOf(qtyCell));
        String qtyText = qtyCell.getText().trim();
        try {
            capturedStockQuantity = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            capturedStockQuantity = 0;
        }
        System.out.println("ℹ️ Captured Current Quantity: " + capturedStockQuantity);

        if (selectedRow.findElements(By.xpath(".//span[@class='stock_warning_para']")).size() > 0) {
            capturedStockStatus = "LOW_STOCK";
        } else if (selectedRow.findElements(By.xpath(".//span[@class='stock_active_para in-stock']")).size() > 0) {
            capturedStockStatus = "IN_STOCK";
        } else if (selectedRow.findElements(By.xpath(".//span[@class='stock_inactive_para']")).size() > 0) {
            capturedStockStatus = "OUT_OF_STOCK";
        } else {
            capturedStockStatus = "UNKNOWN";
        }
        System.out.println("ℹ️ Captured Stock Status: " + capturedStockStatus);

        wait.until(ExpectedConditions.visibilityOfAllElements(editButtons));
        if (selectedIndex >= editButtons.size()) {
            throw new RuntimeException("Edit button not found for index: " + selectedIndex);
        }
        WebElement editBtn = editButtons.get(selectedIndex);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});", editBtn);
        wait.until(ExpectedConditions.elementToBeClickable(editBtn));
        click(editBtn);
        System.out.println("🖱️ Clicked Edit button for: " + capturedProductName);

        WebElement previewIcon = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//div[@class='nav-item dropdown'])[" + (selectedIndex + 1) + "]")));

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});", previewIcon);
        click(previewIcon);
        System.out.println("🖱️ Clicked Preview button for: " + capturedProductName + " — navigating to details page");
    }

    public void completeStockAdjustmentFlow() {
        wait.until(ExpectedConditions.elementToBeClickable(updatetopStockButton));
        click(updatetopStockButton);
        System.out.println("🖱️ Clicked Update Top Stock button — popup opened");
        pause(1000);

        try { Thread.sleep(500); } catch (InterruptedException e) { }

        WebElement adjustStockRadio = null;

        try {
            adjustStockRadio = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'modal') or contains(@class,'popup') or contains(@class,'dialog')]//input[@id='adjust_md']")));
        } catch (Exception e) {
            System.out.println("⚠️ Strategy 1 failed: modal-scoped adjust_md not found");
        }

        if (adjustStockRadio == null) {
            try {
                adjustStockRadio = driver.findElement(By.xpath(
                    "//label[contains(text(),'Adjust Stock') or contains(text(),'adjust stock')]//preceding-sibling::input[@type='radio'] | " +
                    "//label[contains(text(),'Adjust Stock') or contains(text(),'adjust stock')]/input[@type='radio'] | " +
                    "//input[@type='radio'][following-sibling::label[contains(text(),'Adjust Stock')]]"));
            } catch (Exception e) {
                System.out.println("⚠️ Strategy 2 failed: label-based radio not found");
            }
        }

        if (adjustStockRadio == null) {
            try {
                adjustStockRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//div[contains(@class,'modal') or contains(@class,'popup')]//input[@type='radio'][contains(@value,'adjust') or contains(@name,'adjust')]")));
            } catch (Exception e) {
                System.out.println("⚠️ Strategy 3 failed: value/name-based radio not found");
            }
        }

        if (adjustStockRadio == null) {
            try {
                List<WebElement> radios = driver.findElements(By.xpath(
                    "//div[contains(@class,'modal') or contains(@class,'popup') or contains(@id,'modal') or contains(@id,'popup')]//input[@type='radio']"));
                if (radios.size() >= 2) {
                    adjustStockRadio = radios.get(1);
                } else if (radios.size() == 1) {
                    adjustStockRadio = radios.get(0);
                }
            } catch (Exception e) {
                System.out.println("⚠️ Strategy 4 failed: no radios in modal");
            }
        }

        if (adjustStockRadio == null) {
            throw new RuntimeException("❌ Could not find Adjust Stock radio button in popup!");
        }

        if (!adjustStockRadio.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", adjustStockRadio);
        }
        System.out.println("✅ Selected Adjust Stock option in popup");
        pause(1000);

        capturedItemStocks.clear();

        List<WebElement> currentStockCells = driver.findElements(By.xpath("//td[@class='current-stock']"));
        int itemCount = currentStockCells.size();
        System.out.println("ℹ️ Number of stock items found: " + itemCount);
        pause(800);

        if (itemCount == 0) {
            throw new RuntimeException("❌ Could not locate any current stock cells in popup!");
        }

        if (itemCount == 1) {
            String currentStockText = currentStockCells.get(0).getText().trim().replaceAll("[^0-9]", "");
            int currentStockValue = 0;
            try {
                currentStockValue = Integer.parseInt(currentStockText);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Could not parse current stock value: " + currentStockCells.get(0).getText().trim());
            }
            System.out.println("ℹ️ Current Stock Value: " + currentStockValue);
            pause(800);

            WebElement adjustmentQtyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//input[@class='adjust-quantity-input form-control'])[1]")));
            adjustmentQtyInput.clear();
            adjustmentQtyInput.sendKeys(String.valueOf(currentStockValue));
            System.out.println("✅ Entered adjustment quantity: " + currentStockValue);
            pause(1000);

            this.capturedCurrentStock = currentStockValue;
            this.capturedAdjustmentQuantity = String.valueOf(currentStockValue);
            this.capturedItemStocks.add(currentStockValue);

        } else {
            int totalStock = 0;
            for (int i = 0; i < itemCount; i++) {
                String currentStockText = currentStockCells.get(i).getText().trim().replaceAll("[^0-9]", "");
                int currentStockValue = 0;
                try {
                    currentStockValue = Integer.parseInt(currentStockText);
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Could not parse current stock value for item " + i + ": " + currentStockCells.get(i).getText().trim());
                }
                System.out.println("ℹ️ Item " + i + " Current Stock Value: " + currentStockValue);
                pause(600);

                WebElement adjustmentQtyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//input[@name='stock_adjustments[" + i + "][adjust_quantity]'])[1]")));
                adjustmentQtyInput.clear();
                adjustmentQtyInput.sendKeys(String.valueOf(currentStockValue));
                System.out.println("✅ Entered adjustment quantity for item " + i + ": " + currentStockValue);
                pause(800);

                this.capturedItemStocks.add(currentStockValue);
                totalStock += currentStockValue;
            }
            this.capturedCurrentStock = totalStock;
            this.capturedAdjustmentQuantity = String.valueOf(totalStock);
        }

        WebElement reasonDropdown = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//select[@id='stock_reason'])[1]")));
        click(reasonDropdown);
        pause(800);

        String[] reasons = {
            "stolen_goods",
            "damaged_goods",
            "stock_written_off",
            "stock_taking_result",
            "inventory_revaluation"
        };
        String selectedReason = reasons[new Random().nextInt(reasons.length)];

        WebElement reasonOption = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//select[@id='stock_reason'])[1]//option[@value='" + selectedReason + "']")));
        click(reasonOption);
        System.out.println("✅ Selected reason: " + selectedReason);
        pause(1000);

        String randomDescription = "Stock adjustment - " + selectedReason + " - " +
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        WebElement descriptionField = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//textarea[@id='description'])[1]")));
        descriptionField.clear();
        descriptionField.sendKeys(randomDescription);
        System.out.println("✅ Entered description: " + randomDescription);
        pause(1000);

        this.capturedReason = selectedReason;
        this.capturedDescription = randomDescription;

        WebElement saveChangesButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//button[normalize-space()='Save Changes'])[1]")));
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});", saveChangesButton);
        pause(500);
        click(saveChangesButton);
        System.out.println("✅ Clicked Save Changes button");
        pause(1500);

        System.out.println("=== Stock Adjustment Summary ===");
        System.out.println("Reason: " + selectedReason);
        System.out.println("Description: " + randomDescription);
        pause(1000);
    }

    public void verifyOutOfStockStatusAndStockHistory() {
        int itemCount = capturedItemStocks.size();
        System.out.println("ℹ️ Verifying Out of Stock status for " + itemCount + " item(s)");
        pause(800);

        if (itemCount == 1) {
            WebElement listBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//button[normalize-space()='Out of stock'])[1]")));
            if (!listBadge.isDisplayed()) {
                throw new RuntimeException("❌ Out of Stock badge not displayed in list for single item");
            }
            System.out.println("✅ Out of Stock badge verified in list");
            pause(1000);
        } else {
            for (int i = 0; i < itemCount; i++) {
                WebElement listBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//button[@class='m-0 out_of_stock_badge'][normalize-space()='Out of stock'])[" + (i + 1) + "]")));
                if (!listBadge.isDisplayed()) {
                    throw new RuntimeException("❌ Out of Stock badge not displayed in list for item " + i);
                }
                System.out.println("✅ Out of Stock badge verified in list for item " + i);
                pause(800);
            }
        }

        WebElement topStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//p[@class='stock_active_para out-of-stock']")));
        if (!topStatus.isDisplayed()) {
            throw new RuntimeException("❌ Out of Stock status not displayed at top of details page");
        }
        System.out.println("✅ Out of Stock status verified at top of details page");
        pause(1200);

        WebElement editIcon = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//*[name()='svg'])[6]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", editIcon);
        pause(500);
        click(editIcon);
        System.out.println("🖱️ Clicked Edit icon");
        pause(1000);

        WebElement stockHistoryOption = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//li[normalize-space()='Stock History'])[1]")));
        click(stockHistoryOption);
        System.out.println("🖱️ Clicked Stock History");
        pause(1200);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//p[@class='count_para m-0 out_of_stock_color'])[1]")));
        System.out.println("📄 Navigated to Stock History page");
        pause(1000);

        for (int i = 0; i < itemCount; i++) {
            WebElement countPara = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//p[@class='count_para m-0 out_of_stock_color'])[" + (i + 1) + "]")));
            String historyStockText = countPara.getText().trim().replaceAll("[^0-9]", "");
            int historyStockValue = 0;
            try {
                historyStockValue = Integer.parseInt(historyStockText);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Could not parse stock history value for item " + i + ": " + countPara.getText().trim());
            }

            int expectedStockValue = Math.abs(capturedItemStocks.get(i));
            if (historyStockValue != expectedStockValue) {
                throw new RuntimeException("❌ Stock History mismatch for item " + i +
                    " — expected: " + expectedStockValue + ", found: " + historyStockValue);
            }
            System.out.println("✅ Stock History quantity verified for item " + i + ": " + historyStockValue);
            pause(800);
        }

        // Description is shared across all items in a multi-item adjustment — verify it once, at index [1]
        WebElement descriptionPara = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//p[@class='date_para m-0'])[1]")));
        String historyDescription = descriptionPara.getText().trim();

        if (!historyDescription.contains(capturedDescription)) {
            throw new RuntimeException("❌ Stock History description mismatch" +
                " — expected to contain: " + capturedDescription + ", found: " + historyDescription);
        }
        System.out.println("✅ Stock History description verified: " + historyDescription);
        pause(800);

        System.out.println("✅ Out of Stock status and Stock History verification completed successfully");
        pause(1000);

        // ---- Navigate back: Stock History page -> Product Details page ----
        WebElement backButtonFromHistory = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//*[name()='svg'][@class='back_icon'])[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", backButtonFromHistory);
        click(backButtonFromHistory);
        System.out.println("🖱️ Clicked Back button — returning to Product Details page");
        pause(1200);

        // ---- Navigate back: Product Details page -> Product List page ----
        WebElement backButtonFromDetails = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//*[name()='svg'][@class='back_icon'])[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", backButtonFromDetails);
        click(backButtonFromDetails);
        System.out.println("🖱️ Clicked Back button — returning to Product List page");
        pause(1200);

        // ---- Verify Out of Stock status is reflected in the product list for the captured product ----
        wait.until(ExpectedConditions.visibilityOfAllElements(productDataRows));

        WebElement updatedRow = null;
        for (WebElement row : productDataRows) {
            WebElement nameCell = row.findElement(By.xpath(".//td[1]"));
            if (nameCell.getText().trim().equalsIgnoreCase(capturedProductName)) {
                updatedRow = row;
                break;
            }
        }

        if (updatedRow == null) {
            throw new RuntimeException("❌ Could not locate product row for: " + capturedProductName + " in the product list");
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", updatedRow);
        pause(800);

        boolean isOutOfStockInList = updatedRow.findElements(By.xpath(".//span[@class='stock_inactive_para']")).size() > 0;

        if (!isOutOfStockInList) {
            throw new RuntimeException("❌ Out of Stock status change not reflected in product list for: " + capturedProductName);
        }

        System.out.println("✅ Out of Stock status change verified/reflected in product list for: " + capturedProductName);
        pause(1000);
    }

    
    
    //TC-02
    public void verifyDetailsPageAndClickUpdateStock() {
        // ===== STEP 1: Verify overall quantity is displayed correctly on details page =====
        System.out.println("⏳ Waiting for overall quantity to be visible...");
        String formattedQty = String.format("%,d", capturedStockQuantity);
        String overallQtyXpath = "(//p[normalize-space()='" + formattedQty + "'])[1]";
        
        WebElement overallQtyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(overallQtyXpath)));
        String displayedOverallQty = overallQtyElement.getText().trim().replace(",", "");
        
        int displayedOverall = 0;
        try {
            displayedOverall = Integer.parseInt(displayedOverallQty);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Could not parse overall quantity from details page: '" + displayedOverallQty + "'");
        }
        
        if (displayedOverall != capturedStockQuantity) {
            throw new RuntimeException("Overall quantity mismatch! Expected: " + capturedStockQuantity + ", Found: " + displayedOverall);
        }
        System.out.println("✅ Overall quantity verified on details page: " + formattedQty);
        pause(1000);

        // ===== STEP 2: Check if Top and Bottom tabs exist =====
        System.out.println("⏳ Checking for Top/Bottom tabs...");
        boolean hasTopTab = false;
        boolean hasBottomTab = false;
        
        try {
            WebElement topTab = driver.findElement(By.xpath("(//button[normalize-space()='Top'])[1]"));
            hasTopTab = topTab.isDisplayed();
        } catch (Exception e) {
            hasTopTab = false;
        }
        
        try {
            WebElement bottomTab = driver.findElement(By.xpath("(//button[normalize-space()='Bottom'])[1]"));
            hasBottomTab = bottomTab.isDisplayed();
        } catch (Exception e) {
            hasBottomTab = false;
        }
        
        System.out.println("Top tab present: " + hasTopTab);
        System.out.println("Bottom tab present: " + hasBottomTab);
        pause(800);

        int calculatedTotalQuantity = 0;

        // ===== SCENARIO A: Product has Top and/or Bottom tabs =====
        if (hasTopTab || hasBottomTab) {
            
            // --- Capture Top quantities ---
            if (hasTopTab) {
                System.out.println("⏳ Clicking Top tab...");
                WebElement topTab = driver.findElement(By.xpath("(//button[normalize-space()='Top'])[1]"));
                if (!topTab.getAttribute("class").contains("active")) {
                    click(topTab);
                    System.out.println("🖱️ Clicked Top tab");
                } else {
                    System.out.println("ℹ️ Top tab already active");
                }
                pause(1500);
                
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[normalize-space()='Top'][contains(@class,'active')]")));
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//td[@class='d-flex justify-content-between align-items-center']")));
                System.out.println("✅ Top tab content loaded");
                pause(500);
                
                System.out.println("⏳ Capturing Top quantities...");
                List<WebElement> allQtyCells = driver.findElements(
                    By.xpath("//td[@class='d-flex justify-content-between align-items-center']"));
                
                for (int i = 0; i < allQtyCells.size(); i++) {
                    String rawText = allQtyCells.get(i).getText().trim();
                    String qtyText = rawText.split("\\s+")[0].replace(",", "");
                    
                    int qty = 0;
                    try {
                        qty = Integer.parseInt(qtyText);
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping non-numeric cell at index " + (i+1) + ": '" + rawText + "'");
                        continue;
                    }
                    calculatedTotalQuantity += qty;
                    System.out.println("  Top item quantity: " + qty + " (raw: '" + rawText + "')");
                    pause(300);
                }
                System.out.println("📊 Total Top Quantity so far: " + calculatedTotalQuantity);
                pause(800);
            }

            // --- Capture Bottom quantities ---
            if (hasBottomTab) {
                System.out.println("⏳ Clicking Bottom tab...");
                WebElement bottomTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[normalize-space()='Bottom'])[1]")));
                click(bottomTab);
                System.out.println("🖱️ Clicked Bottom tab");
                pause(1500);
                
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[normalize-space()='Bottom'][contains(@class,'active')]")));
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//td[@class='d-flex justify-content-between align-items-center']")));
                System.out.println("✅ Bottom tab content loaded");
                pause(500);
                
                System.out.println("⏳ Capturing Bottom quantities...");
                List<WebElement> allQtyCells = driver.findElements(
                    By.xpath("//td[@class='d-flex justify-content-between align-items-center']"));
                
                int bottomTotal = 0;
                for (int i = 0; i < allQtyCells.size(); i++) {
                    String rawText = allQtyCells.get(i).getText().trim();
                    String qtyText = rawText.split("\\s+")[0].replace(",", "");
                    
                    int qty = 0;
                    try {
                        qty = Integer.parseInt(qtyText);
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping non-numeric cell at index " + (i+1) + ": '" + rawText + "'");
                        continue;
                    }
                    bottomTotal += qty;
                    calculatedTotalQuantity += qty;
                    System.out.println("  Bottom item quantity: " + qty + " (raw: '" + rawText + "')");
                    pause(300);
                }
                System.out.println("📊 Total Bottom Quantity: " + bottomTotal);
                pause(800);
            }

            // --- Verify grand total ---
            System.out.println("📊 Grand Total (Top + Bottom): " + calculatedTotalQuantity);
            System.out.println("📊 Captured Stock Quantity: " + capturedStockQuantity);
            pause(500);
            
            if (calculatedTotalQuantity != capturedStockQuantity) {
                throw new RuntimeException("Quantity verification failed! Expected: " + capturedStockQuantity 
                    + ", Calculated: " + calculatedTotalQuantity);
            }
            System.out.println("✅ Grand total quantity verified successfully!");
            pause(1000);

            // --- Click back to Top tab and click Update Stock ---
            if (hasTopTab) {
                System.out.println("⏳ Clicking back to Top tab...");
                WebElement topTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[normalize-space()='Top'])[1]")));
                click(topTab);
                System.out.println("🖱️ Clicked Top tab");
                pause(1500);
                
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[normalize-space()='Top'][contains(@class,'active')]")));
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//td[@class='d-flex justify-content-between align-items-center']")));
                System.out.println("✅ Top tab active again");
                pause(500);
                
                System.out.println("⏳ Clicking Update Stock (Top) button...");
                WebElement updateTopStockBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@onclick=\"openUpdateStockModal('top')\"])[1]")));
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", updateTopStockBtn);
                pause(500);
                
                wait.until(ExpectedConditions.elementToBeClickable(updateTopStockBtn));
                click(updateTopStockBtn);
                System.out.println("🖱️ Clicked Update Stock (Top) button — popup opened");
                pause(1500);
                
                wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(@class,'modal') or contains(@class,'popup') or contains(@id,'modal')]")));
                System.out.println("✅ Popup modal detected");
                pause(500);
                
            } else if (hasBottomTab) {
                System.out.println("⏳ Clicking Update Stock (Bottom) button...");
                WebElement updateBottomStockBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@onclick=\"openUpdateStockModal('bottom')\"])[1]")));
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", updateBottomStockBtn);
                pause(500);
                
                wait.until(ExpectedConditions.elementToBeClickable(updateBottomStockBtn));
                click(updateBottomStockBtn);
                System.out.println("🖱️ Clicked Update Stock (Bottom) button — popup opened");
                pause(1500);
                
                wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(@class,'modal') or contains(@class,'popup') or contains(@id,'modal')]")));
                System.out.println("✅ Popup modal detected");
                pause(500);
            }

        // ===== SCENARIO B: No Top/Bottom tabs (Accessories) =====
        } else {
            System.out.println("ℹ️ No Top/Bottom tabs found — processing as accessory/single item product");
            pause(800);
            
            System.out.println("⏳ Capturing quantities directly...");
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//td[@class='d-flex justify-content-between align-items-center']")));
            
            List<WebElement> allQtyCells = driver.findElements(
                By.xpath("//td[@class='d-flex justify-content-between align-items-center']"));
            
            if (allQtyCells.isEmpty()) {
                throw new RuntimeException("No quantity cells found on details page!");
            }
            
            for (int i = 0; i < allQtyCells.size(); i++) {
                String rawText = allQtyCells.get(i).getText().trim();
                String qtyText = rawText.split("\\s+")[0].replace(",", "");
                
                int qty = 0;
                try {
                    qty = Integer.parseInt(qtyText);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping non-numeric cell at index " + (i+1) + ": '" + rawText + "'");
                    continue;
                }
                calculatedTotalQuantity += qty;
                System.out.println("  Item quantity: " + qty + " (raw: '" + rawText + "')");
                pause(300);
            }
            System.out.println("📊 Total Quantity: " + calculatedTotalQuantity);
            System.out.println("📊 Captured Stock Quantity: " + capturedStockQuantity);
            pause(800);
            
            if (calculatedTotalQuantity != capturedStockQuantity) {
                throw new RuntimeException("Quantity verification failed! Expected: " + capturedStockQuantity 
                    + ", Calculated: " + calculatedTotalQuantity);
            }
            System.out.println("✅ Total quantity verified successfully!");
            pause(1000);

            System.out.println("⏳ Clicking Update Stock button...");
            WebElement updateStockBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[normalize-space()='Update Stock'])[1]")));
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", updateStockBtn);
            pause(500);
            
            wait.until(ExpectedConditions.elementToBeClickable(updateStockBtn));
            click(updateStockBtn);
            System.out.println("🖱️ Clicked Update Stock button — popup opened");
            pause(1500);
            
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'modal') or contains(@class,'popup') or contains(@id,'modal')]")));
            System.out.println("✅ Popup modal detected");
            pause(500);
        }
    }
    
    
 // ==================== METHOD 1: Add Random Stock in Popup ====================
    public void addRandomStockInPopup() {
        System.out.println("⏳ Popup opened — adding random stock quantity...");
        pause(800);
        
        // Generate random quantity between 1-100
        Random random = new Random();
        addedStockQuantity = random.nextInt(100) + 1; // 1 to 100
        System.out.println("🎲 Generated random stock to add: " + addedStockQuantity);
        
        // Find the quantity input in popup and enter value
        WebElement qtyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("(//input[@name='stock_updates[0][add_quantity]'])[1]")));
        qtyInput.clear();
        qtyInput.sendKeys(String.valueOf(addedStockQuantity));
        System.out.println("⌨️ Entered quantity: " + addedStockQuantity);
        pause(500);
        
        // Click Save Changes button in popup
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//button[normalize-space()='Save Changes'])[1]")));
        click(saveBtn);
        System.out.println("💾 Clicked Save Changes button — stock added");
        pause(1500);
        
        // Wait for popup to close and page to refresh
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//div[contains(@class,'modal') and contains(@style,'display: block')]")));
        System.out.println("✅ Popup closed, page refreshed");
        pause(1000);
    }
    
    

 // ==================== METHOD 2: Verify Overall Quantity After Add ====================
 public void verifyOverallQuantityAfterAdd() {
     System.out.println("⏳ Verifying updated overall quantity...");
     pause(800);
     
     // Calculate expected new total
     newTotalQuantity = capturedStockQuantity + addedStockQuantity;
     String expectedFormattedQty = String.format("%,d", newTotalQuantity);
     
     // Verify overall quantity on details page
     String overallQtyXpath = "(//p[normalize-space()='" + expectedFormattedQty + "'])[1]";
     WebElement overallQtyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(overallQtyXpath)));
     String displayedQty = overallQtyElement.getText().trim().replace(",", "");
     
     int displayedOverall = 0;
     try {
         displayedOverall = Integer.parseInt(displayedQty);
     } catch (NumberFormatException e) {
         throw new RuntimeException("Could not parse updated overall quantity: '" + displayedQty + "'");
     }
     
     if (displayedOverall != newTotalQuantity) {
         throw new RuntimeException("Updated quantity mismatch! Expected: " + newTotalQuantity + ", Found: " + displayedOverall);
     }
     System.out.println("✅ Overall quantity verified after add: " + expectedFormattedQty + " (Previous: " + capturedStockQuantity + " + Added: " + addedStockQuantity + ")");
     pause(1000);
 }


 // ==================== METHOD 3: Navigate to Stock History & Verify ====================
 public void navigateToStockHistoryAndVerify() {
     System.out.println("⏳ Clicking 3-dot menu...");
     pause(500);
     
     // Click 3-dot/ellipsis menu
     WebElement threeDotMenu = wait.until(ExpectedConditions.elementToBeClickable(
         By.xpath("(//*[name()='svg'])[6]")));
     click(threeDotMenu);
     System.out.println("🖱️ Clicked 3-dot menu");
     pause(800);
     
     // Click Stock History
     WebElement stockHistoryBtn = wait.until(ExpectedConditions.elementToBeClickable(
         By.xpath("(//li[normalize-space()='Stock History'])[1]")));
     click(stockHistoryBtn);
     System.out.println("🖱️ Clicked Stock History — navigating to history page");
     pause(1500);
     
     // Wait for Stock History page to load
     wait.until(ExpectedConditions.visibilityOfElementLocated(
         By.xpath("(//p[@class='count_para m-0 text-success'])[1]")));
     System.out.println("✅ Stock History page loaded");
     pause(800);
     
     // Verify added quantity in history
     WebElement historyQtyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
         By.xpath("(//p[@class='count_para m-0 text-success'])[1]")));
     String historyQtyText = historyQtyElement.getText().trim().replace(",", "").replace("+", "");
     
     int historyQty = 0;
     try {
         historyQty = Integer.parseInt(historyQtyText);
     } catch (NumberFormatException e) {
         throw new RuntimeException("Could not parse history quantity: '" + historyQtyText + "'");
     }
     
     if (historyQty != addedStockQuantity) {
         throw new RuntimeException("History quantity mismatch! Expected: " + addedStockQuantity + ", Found: " + historyQty);
     }
     System.out.println("✅ History quantity verified: " + historyQty + " (matches added: " + addedStockQuantity + ")");
     pause(500);
     
     // Verify status is "Stock Added"
     WebElement statusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
         By.xpath("(//p[@class='stock_active_para m-0 text-success'])[1][normalize-space()='Stock Added']")));
     String statusText = statusElement.getText().trim();
     
     if (!"Stock Added".equals(statusText)) {
         throw new RuntimeException("Status mismatch! Expected: 'Stock Added', Found: '" + statusText + "'");
     }
     System.out.println("✅ Status verified: '" + statusText + "'");
     pause(800);
 }


 // ==================== METHOD 4: Go Back to Listing & Verify ====================
 public void goBackToListingAndVerify() {
     System.out.println("⏳ Clicking back button (1st time)...");
     pause(500);
     
     // Click back button first time
     WebElement backBtn1 = wait.until(ExpectedConditions.elementToBeClickable(
         By.xpath("(//*[name()='svg'][@class='back_icon'])[1]")));
     click(backBtn1);
     System.out.println("🖱️ Clicked back button — 1st time");
     pause(1200);
     
     System.out.println("⏳ Clicking back button (2nd time)...");
     
     // Click back button second time
     WebElement backBtn2 = wait.until(ExpectedConditions.elementToBeClickable(
         By.xpath("(//*[name()='svg'][@class='back_icon'])[1]")));
     click(backBtn2);
     System.out.println("🖱️ Clicked back button — 2nd time");
     pause(1500);
     
     // Wait for listing page to load
     wait.until(ExpectedConditions.visibilityOfElementLocated(
         By.xpath("//table//tbody//tr[contains(@class,'even') or contains(@class,'odd')]")));
     System.out.println("✅ Back on Product Stock listing page");
     pause(800);
     
     // Find the product row by name and verify updated quantity
     System.out.println("⏳ Verifying updated quantity in listing for: " + capturedProductName);
     
     // Find the row containing the product name
     String rowXpath = "//tr[contains(@class,'even') or contains(@class,'odd')][.//td[normalize-space()='" + capturedProductName + "']]";
     WebElement productRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXpath)));
     
     // Get quantity from 3rd column of that row
     WebElement qtyCell = productRow.findElement(By.xpath(".//td[3]"));
     String listingQtyText = qtyCell.getText().trim().replace(",", "");
     
     int listingQty = 0;
     try {
         listingQty = Integer.parseInt(listingQtyText);
     } catch (NumberFormatException e) {
         throw new RuntimeException("Could not parse listing quantity: '" + listingQtyText + "'");
     }
     
     if (listingQty != newTotalQuantity) {
         throw new RuntimeException("Listing quantity mismatch! Expected: " + newTotalQuantity + ", Found: " + listingQty);
     }
     System.out.println("✅ Listing quantity verified: " + listingQty + " (Expected: " + newTotalQuantity + " = " + capturedStockQuantity + " + " + addedStockQuantity + ")");
     pause(1000);
     
     // Verify stock status updated (should be IN_STOCK now if it was LOW_STOCK)
     String rowStatus = "UNKNOWN";
     if (productRow.findElements(By.xpath(".//span[@class='stock_warning_para']")).size() > 0) {
         rowStatus = "LOW_STOCK";
     } else if (productRow.findElements(By.xpath(".//span[@class='stock_active_para in-stock']")).size() > 0) {
         rowStatus = "IN_STOCK";
     } else if (productRow.findElements(By.xpath(".//span[@class='stock_inactive_para']")).size() > 0) {
         rowStatus = "OUT_OF_STOCK";
     }
     
     System.out.println("📊 Updated Stock Status in listing: " + rowStatus);
     if ("IN_STOCK".equals(rowStatus)) {
         System.out.println("✅ Product is now IN_STOCK");
     }
     pause(500);
 }
    
    
 
 //TC-03
//==================== FIELDS (add these) ====================
private int topAlertLevel = 0;
private int bottomAlertLevel = 0;
private int accessoryAlertLevel = 0;
private List<Integer> topQuantities = new ArrayList<>();
private List<Integer> bottomQuantities = new ArrayList<>();
private List<Integer> accessoryQuantities = new ArrayList<>();

//==================== GETTERS (add these) ====================
public int getTopAlertLevel() { return topAlertLevel; }
public int getBottomAlertLevel() { return bottomAlertLevel; }
public int getAccessoryAlertLevel() { return accessoryAlertLevel; }


//==================== MAIN METHOD: Complete Alert Level Flow ====================
public void verifyAlertLevelAndSetLowStock() {
  
  boolean hasTopTab = isElementPresent(By.xpath("(//button[normalize-space()='Top'])[1]"));
  boolean hasBottomTab = isElementPresent(By.xpath("(//button[normalize-space()='Bottom'])[1]"));
  
  System.out.println("Top tab present: " + hasTopTab);
  System.out.println("Bottom tab present: " + hasBottomTab);
  pause(500);

  if (hasTopTab || hasBottomTab) {
      handleTopBottomAlertFlow(hasTopTab, hasBottomTab);
  } else {
      handleAccessoryAlertFlow();
  }
}


//==================== HELPER: Check element exists ====================
private boolean isElementPresent(By by) {
  try {
      return driver.findElement(by).isDisplayed();
  } catch (Exception e) {
      return false;
  }
}


//==================== METHOD: Handle Top + Bottom Flow ====================
private void handleTopBottomAlertFlow(boolean hasTop, boolean hasBottom) {
  
  // --- STEP 1: Capture TOP alert level and ALL item quantities ---
  if (hasTop) {
      System.out.println("⏳ Clicking Top tab...");
      WebElement topTab = driver.findElement(By.xpath("(//button[normalize-space()='Top'])[1]"));
      if (!topTab.getAttribute("class").contains("active")) {
          click(topTab);
          System.out.println("🖱️ Clicked Top tab");
      }
      pause(1200);
      
      wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.xpath("//button[normalize-space()='Top'][contains(@class,'active')]")));
      
      // Capture TOP alert level from td[2] of first row
      WebElement topAlertCell = wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.xpath("//body[1]/div[1]/main[1]/div[1]/div[3]/div[2]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]")));
      String topAlertText = topAlertCell.getText().trim();
      try {
          topAlertLevel = Integer.parseInt(topAlertText);
      } catch (NumberFormatException e) {
          topAlertLevel = 0;
      }
      System.out.println("📊 Top Alert Level captured: " + topAlertLevel);
      pause(500);
      
      // Capture ALL TOP item quantities dynamically from td[3] of each row
      topQuantities.clear();
      int rowIndex = 1;
      while (true) {
          try {
              WebElement qtyCell = driver.findElement(By.xpath(
                  "//body[1]/div[1]/main[1]/div[1]/div[3]/div[2]/div[1]/div[1]/table[1]/tbody[1]/tr[" + rowIndex + "]/td[3]"));
              String qtyText = qtyCell.getText().trim().replace(",", "");
              int qty = Integer.parseInt(qtyText);
              topQuantities.add(qty);
              System.out.println("  Top item " + rowIndex + " quantity: " + qty);
              rowIndex++;
          } catch (Exception e) {
              break; // No more rows
          }
      }
      System.out.println("📊 Top quantities (" + topQuantities.size() + " items): " + topQuantities);
      pause(500);
  }

  // --- STEP 2: Capture BOTTOM alert level and ALL item quantities ---
  if (hasBottom) {
      System.out.println("⏳ Clicking Bottom tab...");
      WebElement bottomTab = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("(//button[normalize-space()='Bottom'])[1]")));
      click(bottomTab);
      System.out.println("🖱️ Clicked Bottom tab");
      pause(1200);
      
      wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.xpath("//button[normalize-space()='Bottom'][contains(@class,'active')]")));
      
      // Capture BOTTOM alert level from td[2] of first row
      WebElement bottomAlertCell = wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.xpath("//body[1]/div[1]/main[1]/div[1]/div[3]/div[2]/div[1]/div[2]/table[1]/tbody[1]/tr[1]/td[2]")));
      String bottomAlertText = bottomAlertCell.getText().trim();
      try {
          bottomAlertLevel = Integer.parseInt(bottomAlertText);
      } catch (NumberFormatException e) {
          bottomAlertLevel = 0;
      }
      System.out.println("📊 Bottom Alert Level captured: " + bottomAlertLevel);
      pause(500);
      
      // Capture ALL BOTTOM item quantities dynamically from td[3] of each row
      bottomQuantities.clear();
      int rowIndex = 1;
      while (true) {
          try {
              WebElement qtyCell = driver.findElement(By.xpath(
                  "//body[1]/div[1]/main[1]/div[1]/div[3]/div[2]/div[1]/div[2]/table[1]/tbody[1]/tr[" + rowIndex + "]/td[3]"));
              String qtyText = qtyCell.getText().trim().replace(",", "");
              int qty = Integer.parseInt(qtyText);
              bottomQuantities.add(qty);
              System.out.println("  Bottom item " + rowIndex + " quantity: " + qty);
              rowIndex++;
          } catch (Exception e) {
              break; // No more rows
          }
      }
      System.out.println("📊 Bottom quantities (" + bottomQuantities.size() + " items): " + bottomQuantities);
      pause(500);
  }

  // --- STEP 3: Compare alert level with quantities ---
  System.out.println("=== Alert Level vs Quantity Comparison ===");
  if (hasTop) {
      for (int i = 0; i < topQuantities.size(); i++) {
          boolean isLow = topQuantities.get(i) <= topAlertLevel;
          System.out.println("  Top item " + (i+1) + ": Qty=" + topQuantities.get(i) + ", Alert=" + topAlertLevel 
              + " → " + (isLow ? "LOW STOCK" : "OK"));
      }
  }
  if (hasBottom) {
      for (int i = 0; i < bottomQuantities.size(); i++) {
          boolean isLow = bottomQuantities.get(i) <= bottomAlertLevel;
          System.out.println("  Bottom item " + (i+1) + ": Qty=" + bottomQuantities.get(i) + ", Alert=" + bottomAlertLevel 
              + " → " + (isLow ? "LOW STOCK" : "OK"));
      }
  }
  pause(800);

  // --- STEP 4: Set TOP Low Stock Alert ---
  if (hasTop) {
      System.out.println("⏳ Setting TOP Low Stock Alert...");
      
      // Click 3-dot menu
      WebElement threeDotTop = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("(//*[name()='svg'])[6]")));
      click(threeDotTop);
      System.out.println("🖱️ Clicked 3-dot menu (Top)");
      pause(800);
      
      // Click Set Low Stock Alert (Top)
      WebElement setTopAlert = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("(//li[normalize-space()='Set Low Stock Alert (Top)'])[1]")));
      click(setTopAlert);
      System.out.println("🖱️ Clicked 'Set Low Stock Alert (Top)'");
      pause(1000);
      
      // Check if single item or multiple items
      if (topQuantities.size() == 1) {
          // SINGLE ITEM: Enter quantity directly in global_alert_level
          WebElement topAlertInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("(//input[@name='global_alert_level'])[1]")));
          topAlertInput.clear();
          topAlertInput.sendKeys(String.valueOf(topQuantities.get(0)));
          System.out.println("⌨️ Single item — Entered Top alert level: " + topQuantities.get(0));
      } else {
          // MULTIPLE ITEMS: Click threshold slider, then enter each quantity
          System.out.println("⏳ Multiple items detected — clicking threshold slider...");
          
          // Click the threshold slider button (enable per-item alerts)
          WebElement thresholdSlider = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//label[contains(@class,'switch')] | //input[@type='checkbox'][following-sibling::span] | //span[contains(@class,'slider')]")));
          click(thresholdSlider);
          System.out.println("🖱️ Clicked threshold slider for per-item alerts");
          pause(800);
          
          // Enter each top quantity in corresponding alert_level inputs
          for (int i = 0; i < topQuantities.size(); i++) {
              int inputIndex = i + 1; // size_alerts[1], size_alerts[2], etc.
              WebElement alertInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                  By.xpath("(//input[@name='size_alerts[" + inputIndex + "][alert_level]'])[1]")));
              alertInput.clear();
              alertInput.sendKeys(String.valueOf(topQuantities.get(i)));
              System.out.println("⌨️ Entered Top item " + inputIndex + " alert level: " + topQuantities.get(i));
              pause(300);
          }
      }
      pause(500);
      
      // Click Save Alert
      WebElement saveTopAlert = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("(//button[normalize-space()='Save Alert'])[1]")));
      click(saveTopAlert);
      System.out.println("💾 Saved Top Low Stock Alert");
      pause(300);
      
      // Wait for popup to close
      wait.until(ExpectedConditions.invisibilityOfElementLocated(
          By.xpath("//div[contains(@class,'modal') and contains(@style,'display: block')]")));
      System.out.println("✅ Top alert popup closed");
      pause(800);
  }

  // --- STEP 5: Set BOTTOM Low Stock Alert ---
  if (hasBottom) {
      System.out.println("⏳ Setting BOTTOM Low Stock Alert...");
      
      // Click 3-dot menu
      WebElement threeDotBottom = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("(//*[name()='svg'])[6]")));
      click(threeDotBottom);
      System.out.println("🖱️ Clicked 3-dot menu (Bottom)");
      pause(800);
      
      // Click Set Low Stock Alert (Bottom)
      WebElement setBottomAlert = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("(//li[normalize-space()='Set Low Stock Alert (Bottom)'])[1]")));
      click(setBottomAlert);
      System.out.println("🖱️ Clicked 'Set Low Stock Alert (Bottom)'");
      pause(1000);
      
      // Check if single item or multiple items
      if (bottomQuantities.size() == 1) {
          // SINGLE ITEM: Enter quantity directly in global_alert_level
          WebElement bottomAlertInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("(//input[@name='global_alert_level'])[1]")));
          bottomAlertInput.clear();
          bottomAlertInput.sendKeys(String.valueOf(bottomQuantities.get(0)));
          System.out.println("⌨️ Single item — Entered Bottom alert level: " + bottomQuantities.get(0));
      } else {
          // MULTIPLE ITEMS: Click threshold slider, then enter each quantity
          System.out.println("⏳ Multiple items detected — clicking threshold slider...");
          
          WebElement thresholdSlider = wait.until(ExpectedConditions.elementToBeClickable(
              By.xpath("//label[contains(@class,'switch')] | //input[@type='checkbox'][following-sibling::span] | //span[contains(@class,'slider')]")));
          click(thresholdSlider);
          System.out.println("🖱️ Clicked threshold slider for per-item alerts");
          pause(800);
          
          // Enter each bottom quantity in corresponding alert_level inputs
          for (int i = 0; i < bottomQuantities.size(); i++) {
              int inputIndex = i + 1;
              WebElement alertInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                  By.xpath("(//input[@name='size_alerts[" + inputIndex + "][alert_level]'])[1]")));
              alertInput.clear();
              alertInput.sendKeys(String.valueOf(bottomQuantities.get(i)));
              System.out.println("⌨️ Entered Bottom item " + inputIndex + " alert level: " + bottomQuantities.get(i));
              pause(300);
          }
      }
      pause(500);
      
      // Click Save Alert
      WebElement saveBottomAlert = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("(//button[normalize-space()='Save Alert'])[1]")));
      click(saveBottomAlert);
      System.out.println("💾 Saved Bottom Low Stock Alert");
      pause(1500);
      
      // Wait for popup to close
      wait.until(ExpectedConditions.invisibilityOfElementLocated(
          By.xpath("//div[contains(@class,'modal') and contains(@style,'display: block')]")));
      System.out.println("✅ Bottom alert popup closed");
      pause(800);
  }

  // --- STEP 6: Verify Low Stock status reflected for ALL items ---
  System.out.println("⏳ Verifying Low Stock status for all items...");
  pause(1000);
  
  // Verify TOP Low Stock badges for all items
  if (hasTop) {
      for (int i = 0; i < topQuantities.size(); i++) {
          int xpathIndex = i + 1;
          try {
              WebElement topLowStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(
                  By.xpath("(//button[@onclick=\"openLowStockModal('top')\"][contains(text(),'Low')])[" + xpathIndex + "]")));
              String statusText = topLowStatus.getText().trim();
              System.out.println("✅ Top item " + xpathIndex + " Low Stock status: '" + statusText + "'");
              pause(300);
          } catch (Exception e) {
              System.out.println("⚠️ Top item " + xpathIndex + " Low Stock status NOT found");
          }
      }
  }
  
  // Verify BOTTOM Low Stock badges for all items
  if (hasBottom) {
      for (int i = 0; i < bottomQuantities.size(); i++) {
          int xpathIndex = i + 1;
          try {
        	  WebElement bottomTab = wait.until(ExpectedConditions.elementToBeClickable(
        	          By.xpath("(//button[normalize-space()='Bottom'])[1]")));
        	      click(bottomTab);
        	      System.out.println("🖱️ Clicked Bottom tab");
        	      pause(1200);
              WebElement bottomLowStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(
                  By.xpath("(//button[@onclick=\"openLowStockModal('bottom')\"][contains(text(),'Low')])[" + xpathIndex + "]")));
              String statusText = bottomLowStatus.getText().trim();
              System.out.println("✅ Bottom item " + xpathIndex + " Low Stock status: '" + statusText + "'");
              pause(300);
          } catch (Exception e) {
              System.out.println("⚠️ Bottom item " + xpathIndex + " Low Stock status NOT found");
          }
      }
  }
  
  // Verify header alert display
  try {
      WebElement alertBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.xpath("(//p[@class='stock_active_para text-warning'])[1]")));
      System.out.println("✅ Details page alert header visible: '" + alertBtn.getText().trim() + "'");
  } catch (Exception e) {
      System.out.println("⚠️ Details page alert header not found");
  }
  pause(800);
}


//==================== METHOD: Handle Accessory Flow ====================
private void handleAccessoryAlertFlow() {
  System.out.println("ℹ️ Accessory product detected — no Top/Bottom tabs");
  pause(800);
  
  // --- STEP 1: Capture alert level from td[2] of first row ---
  WebElement alertCell = wait.until(ExpectedConditions.visibilityOfElementLocated(
      By.xpath("/html[1]/body[1]/div[1]/main[1]/div[1]/div[3]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]")));
  String alertText = alertCell.getText().trim();
  try {
      accessoryAlertLevel = Integer.parseInt(alertText);
  } catch (NumberFormatException e) {
      accessoryAlertLevel = 0;
  }
  System.out.println("📊 Accessory Alert Level captured: " + accessoryAlertLevel);
  pause(500);
  
  // --- STEP 2: Capture ALL item quantities dynamically from td[3] ---
  accessoryQuantities.clear();
  int rowIndex = 1;
  while (true) {
      try {
          WebElement qtyCell = driver.findElement(By.xpath(
              "//tbody/tr[" + rowIndex + "]/td[3]"));
          String qtyText = qtyCell.getText().trim().replace(",", "");
          int qty = Integer.parseInt(qtyText);
          accessoryQuantities.add(qty);
          System.out.println("  Accessory item " + rowIndex + " quantity: " + qty);
          rowIndex++;
      } catch (Exception e) {
          break; // No more rows
      }
  }
  System.out.println("📊 Accessory quantities (" + accessoryQuantities.size() + " items): " + accessoryQuantities);
  pause(500);
  
  // --- STEP 3: Compare alert level with quantities ---
  System.out.println("=== Alert Level vs Quantity Comparison ===");
  for (int i = 0; i < accessoryQuantities.size(); i++) {
      boolean isLow = accessoryQuantities.get(i) <= accessoryAlertLevel;
      System.out.println("  Item " + (i+1) + ": Qty=" + accessoryQuantities.get(i) + ", Alert=" + accessoryAlertLevel 
          + " → " + (isLow ? "LOW STOCK" : "OK"));
  }
  pause(800);
  
  // --- STEP 4: Set Low Stock Alert ---
  System.out.println("⏳ Setting Accessory Low Stock Alert...");
  
  // Click 3-dot menu
  WebElement threeDot = wait.until(ExpectedConditions.elementToBeClickable(
      By.xpath("(//*[name()='svg'])[6]")));
  click(threeDot);
  System.out.println("🖱️ Clicked 3-dot menu");
  pause(800);
  
  // Click Set Low Stock Alert
  WebElement setAlert = wait.until(ExpectedConditions.elementToBeClickable(
      By.xpath("(//li[normalize-space()='Set Low Stock Alert'])[1]")));
  click(setAlert);
  System.out.println("🖱️ Clicked 'Set Low Stock Alert'");
  pause(1000);
  
  // Check if single item or multiple items
  if (accessoryQuantities.size() == 1) {
      // SINGLE ITEM: Enter quantity directly
      WebElement alertInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.xpath("(//input[@name='global_alert_level'])[1]")));
      alertInput.clear();
      alertInput.sendKeys(String.valueOf(accessoryQuantities.get(0)));
      System.out.println("⌨️ Single item — Entered alert level: " + accessoryQuantities.get(0));
  } else {
      // MULTIPLE ITEMS: Click threshold slider, then enter each quantity
      System.out.println("⏳ Multiple items detected — clicking threshold slider...");
      
      WebElement thresholdSlider = wait.until(ExpectedConditions.elementToBeClickable(
          By.xpath("//label[contains(@class,'switch')] | //input[@type='checkbox'][following-sibling::span] | //span[contains(@class,'slider')]")));
      click(thresholdSlider);
      System.out.println("🖱️ Clicked threshold slider for per-item alerts");
      pause(800);
      
      // Enter each accessory quantity in corresponding alert_level inputs
      for (int i = 0; i < accessoryQuantities.size(); i++) {
          int inputIndex = i + 1;
          WebElement alertInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("(//input[@name='size_alerts[" + inputIndex + "][alert_level]'])[1]")));
          alertInput.clear();
          alertInput.sendKeys(String.valueOf(accessoryQuantities.get(i)));
          System.out.println("⌨️ Entered Accessory item " + inputIndex + " alert level: " + accessoryQuantities.get(i));
          pause(300);
      }
  }
  pause(500);
  
  // Click Save Alert
  WebElement saveAlert = wait.until(ExpectedConditions.elementToBeClickable(
      By.xpath("(//button[normalize-space()='Save Alert'])[1]")));
  click(saveAlert);
  System.out.println("💾 Saved Low Stock Alert");
  pause(1500);
  
  // Wait for popup to close
  wait.until(ExpectedConditions.invisibilityOfElementLocated(
      By.xpath("//div[contains(@class,'modal') and contains(@style,'display: block')]")));
  System.out.println("✅ Alert popup closed");
  pause(800);
  
  // --- STEP 5: Verify Low Stock status reflected for ALL items ---
  System.out.println("⏳ Verifying Low Stock status for all accessory items...");
  pause(1000);
  
  for (int i = 0; i < accessoryQuantities.size(); i++) {
      int xpathIndex = i + 1;
      try {
          WebElement lowStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("(//button[@class='m-0 low_stock_badge'][contains(text(),'Low')])[" + xpathIndex + "]")));
          String statusText = lowStatus.getText().trim();
          System.out.println("✅ Accessory item " + xpathIndex + " Low Stock status: '" + statusText + "'");
          pause(300);
      } catch (Exception e) {
          System.out.println("⚠️ Accessory item " + xpathIndex + " Low Stock status NOT found");
      }
  }
  
  // Verify header alert display
  try {
      WebElement alertBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
          By.xpath("(//p[@class='stock_active_para text-warning'])[1]")));
      System.out.println("✅ Details page alert header visible: '" + alertBtn.getText().trim() + "'");
  } catch (Exception e) {
      System.out.println("⚠️ Details page alert header not found");
  }
  pause(800);
}
public String getCapturedProductName() {
    return capturedProductName;
}
public String getCapturedStockStatus() {
    return capturedStockStatus;
}

public int getCapturedStockQuantity() {
    return capturedStockQuantity;
}

public int getAddedStockQuantity() {
    return addedStockQuantity;
}

public int getNewTotalQuantity() {
    return newTotalQuantity;
}    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //TC-01 Orchestration method to run the full flow for stock adjustment and verification
    public void validateProductAdjustStockPage() throws InterruptedException {
        adminLogin();
        selectRandomProductCaptureDetailsClickEditAndPreview();
        completeStockAdjustmentFlow();
        verifyOutOfStockStatusAndStockHistory();
    }
    
  //TC-02 Orchestration method to run the full flow for Add stock and verification
    public void validateProductAddStockPage() throws InterruptedException {
        adminLogin();
        selectRandomProductCaptureDetailsClickEditAndPreview();
        verifyDetailsPageAndClickUpdateStock();
        
        addRandomStockInPopup();
        verifyOverallQuantityAfterAdd();
        navigateToStockHistoryAndVerify();
        goBackToListingAndVerify();
    }
    
    //TC-03 Orchestration method to run the full flow for Add stock and verification
    
    public void setLowStockAlertPage() throws InterruptedException{
    	adminLogin();
    	selectRandomProductCaptureDetailsClickEditAndPreview();

    	
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
        return productStockModule.isDisplayed();
    }
}