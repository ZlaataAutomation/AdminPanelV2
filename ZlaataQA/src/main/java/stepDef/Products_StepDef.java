package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import pages.Products_Page;

public class Products_StepDef {
	
	TestContext testContext;
    Products_Page products;

    public Products_StepDef(TestContext context) {
        testContext = context;
        products = testContext.getPageObjectManager().getProducts_Page();
    }
    
    
    @Given("admin navigates to the Product module")
    public void admin_navigates_to_the_product_module() {
    	products.adminLogin();
    	products.navigatetoProductsPage();

    }
    
    //TC-PM-01
    @When("admin creates a new Product with valid details and credentials")
    public void admin_creates_a_new_product_with_valid_details_and_credentials() {
    	products.createProductFlow();
    }

    @When("admin saves the Product successfully")
    public void admin_saves_the_product_successfully() {

    }

    @Then("the newly created Product should be displayed correctly on the Product Listing page")
    public void the_newly_created_product_should_be_displayed_correctly_on_the_product_listing_page() {

    }
    
    //TC-PM-02
    @When("admin edits an existing Product with valid details")
    public void admin_edits_an_existing_product_with_valid_details() {
    	products.editProductFlow();
    }

    @Then("the updated Product details should be displayed correctly on the Product Listing page")
    public void the_updated_product_details_should_be_displayed_correctly_on_the_product_listing_page() {

    }

    @And("the updated Product details should be displayed correctly on the Product Preview page")
    public void the_updated_product_details_should_be_displayed_correctly_on_the_product_preview_page() {
    	
    }
    
    
    //TC-PM-03
    @When("admin adjusts the stock of an existing Product to zero from the Preview page")
    public void admin_adjusts_the_stock_of_an_existing_product_to_zero_from_the_preview_page() {
        products.adjustStockFlow();
    }

    @Then("the Product should be displayed as Out of Stock with the updated stock details on Stock History, Details and Listing pages")
    public void the_product_should_be_displayed_as_out_of_stock_with_the_updated_stock_details_on_stock_history_details_and_listing_pages() {
        
    }
    
    
    //TC-PM-04
    @When("admin adds stock to an existing Product from the Preview page")
    public void admin_adds_stock_to_an_existing_product_from_the_preview_page() {
        products.addStockFlow();
    }

    @Then("the Product should be displayed as In Stock with the updated stock details on Stock History, Details and Listing pages")
    public void the_product_should_be_displayed_as_in_stock_with_the_updated_stock_details_on_stock_history_details_and_listing_pages() {
        
    }
    
    //TC-PM-05

    @When("admin clicks the Save button without entering any Product details")
    public void admin_clicks_the_save_button_without_entering_any_product_details() {
    	products.mandatoryFieldValidationFlow();
    }

    @Then("the mandatory field validation messages should be displayed")
    public void the_mandatory_field_validation_messages_should_be_displayed() {

    }
    
    //TC-PM-06
    @When("admin partially creates a Product and saves it")
    public void admin_partially_creates_a_product_and_saves_it() {
        products.verfiySaveAsDraftFlow();
    }

    @Then("the Product should be displayed with Draft status and inactive status toggle on the Product Listing page")
    public void the_product_should_be_displayed_with_draft_status_and_inactive_status_toggle_on_the_product_listing_page() {
        
    }
    
    
    //TC-PM-07
    @When("admin adds multiple variants with product details through the Inventory Setup module")
    public void admin_adds_multiple_variants_with_product_details_through_the_inventory_setup_module() {
       products.verfiyMultipleVariantsAndProductFlow();
    }

    @Then("all added product variants and their details should be displayed correctly on the Product Preview page")
    public void all_added_product_variants_and_their_details_should_be_displayed_correctly_on_the_product_preview_page() {

    }
    

}
