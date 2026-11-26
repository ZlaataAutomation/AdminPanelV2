package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import pages.ClassificationCategory_Page;

public class ClassificationCategory_StepDef {
	
	TestContext testContext;
    ClassificationCategory_Page category;
	
	public ClassificationCategory_StepDef(TestContext context) {
        testContext = context;
        category = testContext.getPageObjectManager().getClassificationCategory_Page();
    }
	
	//TC-UI-Zlaata_Category-01
	@Given("admin navigates to the Category module")
	public void admin_navigates_to_the_category_module() {
		category.adminLogin();
		category.navigatetoProductsPage();
	}

	@When("admin creates a new category with valid details and saves it")
	public void admin_creates_a_new_category_with_valid_details_and_saves_it() {
		category.createCategoryFlow();
	}

	@Then("the newly created category details should be displayed correctly on the Category Listing page")
	public void the_newly_created_category_details_should_be_displayed_correctly_on_the_category_listing_page() {

	}

	@Then("the same category details should be reflected correctly on the Category Detail page")
	public void the_same_category_details_should_be_reflected_correctly_on_the_category_detail_page() {
		
	}

	//TC-UI-Zlaata_Category-02
	@When("admin selects an existing category, updates its details and saves the changes")
	public void admin_selects_an_existing_category_updates_its_details_and_saves_the_changes() {
		category.editCategoryFlow();
	}

	@Then("the updated category details should be displayed correctly on the Category Listing page")
	public void the_updated_category_details_should_be_displayed_correctly_on_the_category_listing_page() {
	    
	}

	@Then("the updated details should also be reflected correctly on the Category Detail page")
	public void the_updated_details_should_also_be_reflected_correctly_on_the_category_detail_page() {
	    
	}
	
	//TC-UI-Zlaata_Category-03
	@When("admin creates a new category with valid details and saves it as Draft")
	public void admin_creates_a_new_category_with_valid_details_and_saves_it_as_draft() {
		category.createDraftCategoryFlow();
	}

	@Then("the category should be displayed with Draft status on the Category Listing page")
	public void the_category_should_be_displayed_with_draft_status_on_the_category_listing_page() {

	}

	@When("admin deletes the Draft category")
	public void admin_deletes_the_draft_category() {

	}

	@Then("the deleted category should no longer be displayed on the Category Listing page")
	public void the_deleted_category_should_no_longer_be_displayed_on_the_category_listing_page() {
	    
	}
	
	//TC-UI-Zlaata_Category-04
	@When("admin activates the newly created category")
	public void admin_activates_the_newly_created_category() {
		category.activateCategoryFlow();
	}

	@Then("the category status should be displayed as Active on the Category Listing page")
	public void the_category_status_should_be_displayed_as_active_on_the_category_listing_page() {

	}

	@Then("the newly created category should be displayed on the Product Category page")
	public void the_newly_created_category_should_be_displayed_on_the_product_category_page() {
		
	}
	
	//TC-UI-Zlaata_Category-05
	@When("admin selects multiple categories and opens the Category Settings popup")
	public void admin_selects_multiple_categories_and_opens_the_category_settings_popup() {
		category.bulkUpdateCategorySettingsFlow();
	}

	@Then("the popup should display Trending, Status, and Custom Delivery Day options")
	public void the_popup_should_display_trending_status_and_custom_delivery_day_options() {

	}

	@When("admin updates the required settings and clicks Save")
	public void admin_updates_the_required_settings_and_clicks_save() {
	    
	}

	@Then("the selected settings should be applied successfully to all selected categories")
	public void the_selected_settings_should_be_applied_successfully_to_all_selected_categories() {
	    
	}
	
	//TC-UI-Zlaata_Category-06
	@When("admin imports a category using a valid import file")
	public void admin_imports_a_category_using_a_valid_import_file() throws Exception {
		category.verifyImportCategoryFlow();
	}

	@Then("the imported category should be displayed with the correct details on the Category Listing page")
	public void the_imported_category_should_be_displayed_with_the_correct_details_on_the_category_listing_page() {

	}
	
	//TC-UI-Zlaata_Category-07
	@When("admin exports the category data to an Excel file")
	public void admin_exports_the_category_data_to_an_excel_file() {
		category.verifyExportCategoryFlow();
	}

	@Then("the exported Excel file should contain all category details with the correct column values")
	public void the_exported_excel_file_should_contain_all_category_details_with_the_correct_column_values() {
	  
	}

}
