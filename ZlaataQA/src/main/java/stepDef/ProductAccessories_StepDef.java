package stepDef;

import context.TestContext;
import pages.ProductAccessories_Page;
import io.cucumber.java.en.*;

public class ProductAccessories_StepDef {
	
	TestContext testContext;
    ProductAccessories_Page accessories;

    public  ProductAccessories_StepDef(TestContext context) {
        testContext = context;
        accessories = testContext.getPageObjectManager().getProductAccessories_Page();
    }
    
    @Given("admin navigates to the Accessories module")
    public void admin_navigates_to_the_accessories_module() {
    	accessories.adminLogin();
    	accessories.navigateToAccessoriesPage();
    }

    @When("admin creates a new accessory with valid details and credentials")
    public void admin_creates_a_new_accessory_with_valid_details_and_credentials() {
    	accessories.createAccessoriesFlow();
    }

    @Then("the newly created accessory should be displayed correctly on the Accessories Listing page")
    public void the_newly_created_accessory_should_be_displayed_correctly_on_the_accessories_listing_page() {

    }
    
    //TC-AM-02

	@When("admin edits an existing accessory with valid details")
	public void admin_edits_an_existing_accessory_with_valid_details() {
		accessories.editAccessoriesFlow();
	}
	
	@Then("the updated accessory details should be displayed correctly on the Accessories Listing page")
	public void the_updated_accessory_details_should_be_displayed_correctly_on_the_accessories_listing_page() {
	
	}
	
	//TC_AM_03
	@When("admin adjusts the stock of an existing accessory to zero from the Preview page")
	public void admin_adjusts_the_stock_of_an_existing_accessory_to_zero_from_the_preview_page() {
		accessories.adjustStockFlow();
	}

	@Then("the accessory should be displayed as Out of Stock with the updated stock details on Stock History, Details and Listing pages")
	public void the_accessory_should_be_displayed_as_out_of_stock_with_the_updated_stock_details_on_stock_history_details_and_listing_pages() {

	}
	
	//TC_AM_04
	@When("admin adds stock to an existing accessory from the Preview page")
	public void admin_adds_stock_to_an_existing_accessory_from_the_preview_page() {
	    accessories.addStockFlow();
	}

	@Then("the accessory should be displayed as In Stock with the updated stock details on Stock History, Details and Listing pages")
	public void the_accessory_should_be_displayed_as_in_stock_with_the_updated_stock_details_on_stock_history_details_and_listing_pages() {

	}
	
	
	//TC_AM_05
	@When("admin clicks the Save button without entering any accessory details")
	public void admin_clicks_the_save_button_without_entering_any_accessory_details() {
		accessories.mandatoryfieldsValidationFlow();
	}

	@Then("the mandatory field validation messages should be displayed for Accessories")
	public void the_mandatory_field_validation_messages_should_be_displayed_for_accessories() {

	}
	
	// TC-AM-06
	@When("admin partially creates an accessory and saves it")
	public void admin_partially_creates_an_accessory_and_saves_it() {
		accessories.verfiySaveAsDraftFlow();
	}

	@Then("the accessory should be displayed with Draft status and inactive status toggle on the Accessories Listing page")
	public void the_accessory_should_be_displayed_with_draft_status_and_inactive_status_toggle_on_the_accessories_listing_page() {
	    // Add validation logic here
	}
	
	

}
