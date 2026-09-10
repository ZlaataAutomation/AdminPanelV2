package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import pages.WareHouse_Page;

public class WareHouse_StepDef {
	TestContext testContext;
	WareHouse_Page ware;
	


	public WareHouse_StepDef(TestContext context) {
		testContext = context;
		ware = testContext.getPageObjectManager().getWareHouse_Page();
	}
	

		@Given("admin creates a new WareHouse with valid WareHouse details")
		public void admin_creates_a_new_ware_house_with_valid_ware_house_details() {
		  
		}
		@Then("the newly created WareHouse should be displayed successfully on the WareHouse Listing page")
		public void the_newly_created_ware_house_should_be_displayed_successfully_on_the_ware_house_listing_page() {
			ware.validateNewCreatedWarehouse();
		}


			@Given("admin views existing WareHouse details from the preview page")
			public void admin_views_existing_ware_house_details_from_the_preview_page() {
			    
			}
			@When("admin edits an existing WareHouse with valid updated details")
			public void admin_edits_an_existing_ware_house_with_valid_updated_details() {
				ware.validateEditWareHouse();
				
			}
			@Then("the updated WareHouse details should be displayed successfully on the WareHouse Listing page")
			public void the_updated_ware_house_details_should_be_displayed_successfully_on_the_ware_house_listing_page() {
			  
			}

				@Given("admin views  WareHouse details from the preview page")
				public void admin_views_ware_house_details_from_the_preview_page() {
				    
				}

				@Then("the Set as main Branch  should be displayed successfully on the WareHouse Listing page")
				public void the_set_as_main_branch_should_be_displayed_successfully_on_the_ware_house_listing_page() {
					ware.validatesetAsMainFunctionalty();
				}




	
	
	
}
