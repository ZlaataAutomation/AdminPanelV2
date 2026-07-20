package stepDef;

import context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.ProductStock_Page;

public class ProductStock_StepDef {

    TestContext testContext;
    ProductStock_Page productstock;

    public ProductStock_StepDef(TestContext context) {
        testContext = context;
        productstock = testContext.getPageObjectManager().getProductStock_Page();
    }
    
    
    //TC-01
    @Given("the admin navigates to the Product Stock module")
    public void the_admin_navigates_to_the_product_stock_module() throws InterruptedException {
    	productstock.validateProductAdjustStockPage();
    }

    @When("the admin adjusts the stock of an existing product with valid stock details")
    public void the_admin_adjusts_the_stock_of_an_existing_product_with_valid_stock_details() {
        
    }

    @Then("the adjusted stock quantity should be updated successfully and reflected correctly in the Product Stock list")
    public void the_adjusted_stock_quantity_should_be_updated_successfully_and_reflected_correctly_in_the_product_stock_list() {
      
    }
    
    

    //TC-02
    
    @Given("the admin navigates to the Product Stock module and click on product modules")
    public void the_admin_navigates_to_the_product_stock_modul() throws InterruptedException {
    	productstock.validateProductAddStockPage();
    }
    
    @When("the admin adds stock to an existing product with valid stock details")
    public void the_admin_adds_stock_to_an_existing_product_with_valid_stock_details() throws InterruptedException {
    	
    }

    @Then("the stock quantity should be updated successfully and reflected correctly in the Product Stock list")
    public void the_stock_quantity_should_be_updated_successfully_and_reflected_correctly_in_the_product_stock_list() {

    }
    
    //TC-03
    @Given("the admin navigates to the Product Stock module and click on product module")
    public void the_admin_navigates_to_the_product_stock_module_and_click_on_product_module() throws InterruptedException {
        productstock.setLowStockAlertPage();
    }

    @When("the admin sets a low stock alert for an existing product with valid details")
    public void the_admin_sets_a_low_stock_alert_for_an_existing_product_with_valid_details() {
    	productstock.verifyAlertLevelAndSetLowStock();
    }

    @Then("the low stock alert should be saved successfully and reflected correctly in the Product Stock list")
    public void the_low_stock_alert_should_be_saved_successfully_and_reflected_correctly_in_the_product_stock_list() {
    	System.out.println("✅ Low stock alert set for: " + productstock.getCapturedProductName());
    }
    
    
    
    
    
    
}