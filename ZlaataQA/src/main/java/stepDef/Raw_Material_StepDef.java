package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import pages.Raw_Material_Page;

public class Raw_Material_StepDef {
	TestContext testContext;
	Raw_Material_Page raw;
	


	public Raw_Material_StepDef(TestContext context) {
		testContext = context;
		raw = testContext.getPageObjectManager().getRaw_Material_Page();
	}

	//TC-01
		@Given("the admin adds a new raw material with valid details")
		public void the_admin_adds_a_new_raw_material_with_valid_details() {
		   raw.adminLogin();
		}
		@Then("the raw material should be added successfully and displayed correctly in the Raw Material List page")
		public void the_raw_material_should_be_added_successfully_and_displayed_correctly_in_the_raw_material_list_page() {
		    
		}



	
	
	
	
	
	
	
	
}
