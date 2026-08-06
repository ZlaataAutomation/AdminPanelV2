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
		public void the_admin_adds_a_new_raw_material_with_valid_details() throws InterruptedException {
		   raw.validateRawMaterialCreation();
		}
		@Then("the raw material should be added successfully and displayed correctly in the Raw Material List page")
		public void the_raw_material_should_be_added_successfully_and_displayed_correctly_in_the_raw_material_list_page() {
		    
		}
		//TC-02
			@Given("the admin navigates to the Raw Material module for stock adjust")
			public void the_admin_navigates_to_the_raw_material_module_for_stock_adjust() {
				raw.validateRawMaterialStockAdjust();
			}
			@When("the admin adjusts the stock of an existing product with valid stock details on Raw Material")
			public void the_admin_adjusts_the_stock_of_an_existing_product_with_valid_stock_details_on_raw_material() {
			   
			}
			@Then("the adjusted stock quantity should be updated successfully and reflected correctly in the Raw Material list, History, and Preview Page.")
			public void the_adjusted_stock_quantity_should_be_updated_successfully_and_reflected_correctly_in_the_raw_material_list_history_and_preview_page() {
			    
			}

//TC-03

				@Given("the admin navigates to the Raw Material module for stock add.")
				public void the_admin_navigates_to_the_raw_material_module_for_stock_add() {
				   raw.validateRawMaterialStockAdd();
				}
				@When("the admin adds stock to an existing product with valid stock details on Raw Material")
				public void the_admin_adds_stock_to_an_existing_product_with_valid_stock_details_on_raw_material() {
			
				}
				@Then("the stock quantity should be updated successfully and reflected correctly in the Raw Material list, History, and Preview Page")
				public void the_stock_quantity_should_be_updated_successfully_and_reflected_correctly_in_the_raw_material_list_history_and_preview_page() {
				   
				}

//TC-04
		
					@Given("the admin navigates to the Raw Material module and click for set low stock")
					public void the_admin_navigates_to_the_raw_material_module_and_click_for_set_low_stock() {
					 raw.validateRawMaterialLowAlert();
					}
					@When("the admin sets a low stock alert for an existing product with valid details on Raw Material module")
					public void the_admin_sets_a_low_stock_alert_for_an_existing_product_with_valid_details_on_raw_material_module() {
					   
					}
					@Then("the low stock alert should be saved successfully and reflected correctly Raw Material module.")
					public void the_low_stock_alert_should_be_saved_successfully_and_reflected_correctly_raw_material_module() {
					
					}


//TC-5

						@Given("the admin imports the Raw Material file successfully")
						public void the_admin_imports_the_raw_material_file_successfully() throws Exception {
							raw.validateImportFunctionalty();
						}
						@Then("the imported Raw Material should be displayed in the list")
						public void the_imported_raw_material_should_be_displayed_in_the_list() {
		
						}



//TC-06

							@Given("the admin exports the Raw Material for the last {int} days successfully")
							public void the_admin_exports_the_raw_material_for_the_last_days_successfully(Integer int1) throws Exception {
								raw.validateExportFunctionalty();
							}
							@Then("the exported Raw Material data should be validated")
							public void the_exported_raw_material_data_should_be_validated() {
							 
							}






	
	
	
	
	
	
	
	
}
