package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import pages.Purchase_Receive_Page;

public class Purchase_Receive_StepDef {
	TestContext testContext;
	Purchase_Receive_Page purchaseReceive;
	


	public Purchase_Receive_StepDef(TestContext context) {
		testContext = context;
		purchaseReceive = testContext.getPageObjectManager().getPurchase_Receive_Page();
	}
	
		@Given("the newly created Purchase Receive should be displayed with Draft status on the Purchase Receive Listing page")
		public void the_newly_created_purchase_receive_should_be_displayed_with_draft_status_on_the_purchase_receive_listing_page() {
			purchaseReceive.validateInTransitStatusPurchaseReceive();
		}

		@Then("the updated Purchase Receive details should be reflected correctly on the Purchase Receive Listing page")
		public void the_updated_purchase_receive_details_should_be_reflected_correctly_on_the_purchase_receive_listing_page() {
		 
		}

			@Given("admin navigates to the Purchase Receive module")
			public void admin_navigates_to_the_purchase_receive_module() {
			    
			}
			@When("admin creates a Purchase Receive with valid quantity and price per unit")
			public void admin_creates_a_purchase_receive_with_valid_quantity_and_price_per_unit() {
				purchaseReceive.validateReceivedStatus();  
			}
	
				@Given("admin navigates to the Purchase Receive module for convert bill")
				public void admin_navigates_to_the_purchase_receive_module_for_convert_bill() {
					purchaseReceive.validateConvertBill();
				}
				@Then("that the convert bill option should redirect to bill module")
				public void that_the_convert_bill_option_should_redirect_to_bill_module() {
				  
				}

					@Given("admin navigate to Purchase Receive Module.")
					public void admin_navigate_to_purchase_receive_module() {
					  
					}
					@Then("t=Verify  Purchase Receive  Validation Error message on Purchase Receive Create Page")
					public void t_verify_purchase_receive_validation_error_message_on_purchase_receive_create_page() {
						purchaseReceive.varifyValidationErrorMessage();
					}





	
	
	
	

}
