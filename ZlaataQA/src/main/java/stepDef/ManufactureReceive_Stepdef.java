package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import pages.ManufactureReceive_Page;

public class ManufactureReceive_Stepdef {
	TestContext testContext;
	ManufactureReceive_Page manufactureReceive;
	


	public ManufactureReceive_Stepdef(TestContext context) {
		testContext = context;
		manufactureReceive = testContext.getPageObjectManager().getManufactureReceive_Page();
	}

		@Given("the newly created Manufacture Receive should be displayed with Draft status on the Manufacture Receive Listing page")
		public void the_newly_created_manufacture_receive_should_be_displayed_with_draft_status_on_the_manufacture_receive_listing_page() {
			manufactureReceive.validateInTransitStatusPurchaseReceive();
		}
		@Then("the updated Manufacture Receive details should be reflected correctly on the Manufacture Receive Listing page")
		public void the_updated_manufacture_receive_details_should_be_reflected_correctly_on_the_manufacture_receive_listing_page() {
	
		}

			@Given("admin navigates to the Manufacture Receive module")
			public void admin_navigates_to_the_manufacture_receive_module() throws InterruptedException {
				manufactureReceive.validateReceivedStatus();
			}
			@When("admin creates a Manufacture Receive with valid quantity and price per unit")
			public void admin_creates_a_manufacture_receive_with_valid_quantity_and_price_per_unit() {
			 
			}

	
				@Given("admin  navigates to Manufacture Receive Module.")
				public void admin_navigates_to_manufacture_receive_module() {
					manufactureReceive.varifyValidationErrorMessageCreatePage();
				}
				@Then("Verify  Manufacture Receive  Validation Error message on Manufacture Receive Create Page.")
				public void verify_manufacture_receive_validation_error_message_on_manufacture_receive_create_page() {
				  
				}


					@Given("admin navigate to Manufacture Receive Module.")
					public void admin_navigate_to_manufacture_receive_module() {
						manufactureReceive.varifyValidationErrorMessage();
					}
						@Then("Verify  Manufacture Receive  Validation Error message on Manufacture Receive Create Pages.")
						public void verify_manufacture_receive_validation_error_message_on_manufacture_receive_create_pages() {
						   
						}



							@Given("the admin delete Manufacture Receive  details")
							public void the_admin_delete_manufacture_receive_details() {
								manufactureReceive.varifyDeleteFunctionalty();
							}
							@Then("the Manufacture Receive should be added successfully and removed correctly in the Manufacture Receive List page")
							public void the_manufacture_receive_should_be_added_successfully_and_removed_correctly_in_the_manufacture_receive_list_page() {
							    
							}


			





	
	
	
	
}
