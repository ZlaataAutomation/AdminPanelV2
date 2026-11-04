package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import pages.ManufactureOrder_Page;

public class ManufactureOrder_StepDef {
	TestContext testContext;
    ManufactureOrder_Page manufactureOrder;

    public ManufactureOrder_StepDef(TestContext context) {
        testContext = context;
        manufactureOrder = testContext.getPageObjectManager().getManufacutreOrder_Page();
    } 

  //TC-MO-01
    @Given("admin navigates to the Manufacture Order module")
    public void admin_navigates_to_the_manufacture_order_module() throws Exception {
    	manufactureOrder.adminLogin();
    	manufactureOrder.navigatetoManufactureOrderPage();
    }

    @When("admin creates a new Manufacture Order with valid details")
    public void admin_creates_a_new_manufacture_order_with_valid_details() throws Exception {
    	manufactureOrder.createNewManufactureOrderFlow();
    }

    @When("admin saves the Manufacture Order")
    public void admin_saves_the_manufacture_order() throws Exception {

    }

    @Then("the newly created Manufacture Order should be displayed on the Manufacture Order Listing page")
    public void the_newly_created_manufacture_order_should_be_displayed_on_the_manufacture_order_listing_page() {

    }
    
  //TC-PO-02
    @When("admin edits an existing Manufacture Order with valid updated details")
    public void admin_edits_an_existing_manufacture_order_with_valid_updated_details() throws Exception {

    }

    @When("admin saves the edited Manufacture Order")
    public void admin_saves_the_edited_manufacture_order() throws Exception {
    	manufactureOrder.editManufactureOrderFlow();
    }

    @Then("the updated Manufacture Order details should be reflected correctly on the Manufacture Order Listing page")
    public void the_updated_manufacture_order_details_should_be_reflected_correctly_on_the_manufacture_order_listing_page() {

    }
    
    //TC-MO-03
    @When("admin creates a new product with valid details and saves it as Draft")
    public void admin_creates_a_new_product_with_valid_details_and_saves_it_as_draft() throws Exception {
        // Keep this strictly focused on creation and saving as draft
        manufactureOrder.createDraftManufactureOrderFlow();
    }

    @Then("the product status should be displayed as Draft")
    public void the_product_status_should_be_displayed_as_draft() {
        // Call your listing page verification here
        manufactureOrder.verifyDraftManufactureOrderOnListingPage();
    }

    @When("admin deletes the Draft product")
    public void admin_deletes_the_draft_product() throws Exception {
        // Call your deletion/cancellation workflow method here
        manufactureOrder.performDraftDeletionWorkflow();
    }

    @Then("the Draft product should be deleted successfully")
    public void the_draft_product_should_be_deleted_successfully() {
        System.out.println("✅ Draft product successfully verified and deleted.");
    }
    
    //TC-MO-04
    @When("admin opens an existing Manufacture Order")
    public void admin_opens_an_existing_manufacture_order() throws Exception {
    	manufactureOrder.cancelManufactureOrderFlow();
    }

    @Then("the {string} option should be displayed")
    public void the_option_should_be_displayed(String option) {

    }

    @When("admin cancels the items in the Manufacture Order")
    public void admin_cancels_the_items_in_the_manufacture_order() throws Exception {

    }

    @Then("the Manufacture Order status should be changed to {string}")
    public void the_manufacture_order_status_should_be_changed_to(String status) {

    }
    
  //TC-MO-05
    @When("admin marks an Open Manufacture Order as Issued")
    public void admin_marks_an_open_manufacture_order_as_issued() throws Exception {
    	manufactureOrder.markAsIssuedManufactureOrderFlow();
    }

    @Then("the Manufacture Order status should be changed to {string} and reflected correctly on both the Listing page and Detail page")
    public void the_manufacture_order_status_should_be_changed_to_and_reflected_correctly_on_both_the_listing_page_and_detail_page(String status) {

    }
    
    
    //TC-MO-06
    @When("admin clicks on Save without entering the mandatory details")
    public void admin_clicks_on_save_without_entering_the_mandatory_details() throws Exception {
    	manufactureOrder.clickAddManufactureOrderForCustomProduct();
    	manufactureOrder.verifyMandatoryFieldsValidationMessages();
    }

    @Then("mandatory field validation messages should be displayed for all required fields")
    public void mandatory_field_validation_messages_should_be_displayed_for_all_required_fields() {
    	
    }
    
  //TC-MO-07
    @When("admin creates a new Manufacture Order")
    public void admin_creates_a_new_manufacture_order() throws Exception {
    	manufactureOrder.verifyMultipleProductsAndTotalsFlow();    
    }

    @When("admin adds multiple products and raw materials to the Manufacture Order")
    public void admin_adds_multiple_products_and_raw_materials_to_the_manufacture_order() throws Exception {

    }

    @Then("multiple products and raw materials should be added successfully to the Manufacture Order")
    public void multiple_products_and_raw_materials_should_be_added_successfully_to_the_manufacture_order() {

    }
    
    //TC-MO-08
    @When("admin exports the Manufacture Order data to an Excel file")
    public void admin_exports_the_manufacture_order_data_to_an_excel_file() throws Exception {
    	manufactureOrder.verfiyExportFunctionalityFlow();
    }

    @Then("the corresponding Manufacture Order columns should be exported correctly in the Excel file")
    public void the_corresponding_manufacture_order_columns_should_be_exported_correctly_in_the_excel_file() {

    }
    
    
    
    //TC-MO-09
    @When("admin creates a new Manufacture Order using an existing Product saves the Manufacture Order")
    public void admin_creates_a_new_manufacture_order_using_an_existing_product_saves_the_manufacture_order() {
    	manufactureOrder.createManufactureOrderExistingProductFlow();
    }
    
    
    //TC-MO-10
    @When("admin edits an existing Manufacture Order with valid updated details and saves it")
    public void admin_edits_an_existing_manufacture_order_with_valid_updated_details_and_saves_it() {
    	manufactureOrder.editManufactureOrderExistingProductFlow();
    }

    @Then("the updated Manufacture Order details should be displayed correctly on the Manufacture Order Listing page")
    public void the_updated_manufacture_order_details_should_be_displayed_correctly_on_the_manufacture_order_listing_page() {

    }
    
    
    //TC-MO-11
    @When("admin creates a new Manufacture Order with valid details and saves it as Draft")
    public void admin_creates_a_new_manufacture_order_with_valid_details_and_saves_it_as_draft() {
        manufactureOrder.createExistingDraftManufactureOrderFlow();
    }

    @Then("the Manufacture Order status should be displayed as Draft")
    public void the_manufacture_order_status_should_be_displayed_as_draft() {
        
    }

    @When("admin deletes the Draft Manufacture Order")
    public void admin_deletes_the_draft_manufacture_order() {
    	
    }

    @Then("the Draft Manufacture Order should be deleted successfully")
    public void the_draft_manufacture_order_should_be_deleted_successfully() {
        
    }
    
    //TC-MO-12
    @When("admin selects an existing Manufacture Order from the listing page")
    public void admin_selects_an_existing_manufacture_order_from_the_listing_page() {
        manufactureOrder.cancelExistingProductManufactureOrderFlow();
    }

    @Then("the {string} option should be available for the selected Manufacture Order")
    public void the_option_should_be_available_for_the_selected_manufacture_order(String option) {
       
    }

    @When("admin selects the {string} option and confirms the cancellation")
    public void admin_selects_the_option_and_confirms_the_cancellation(String option) {
     
    }

    @Then("the Manufacture Order status should be updated to {string} on the listing page")
    public void the_manufacture_order_status_should_be_updated_on_the_listing_page(String status) {
   
    }
    
    //TC-MO-13
    @When("admin selects an existing Manufacture Order with Open status from the listing page")
    public void admin_selects_an_existing_manufacture_order_with_open_status_from_the_listing_page() {
    	manufactureOrder.markAsIssuedForExistingProductManufactureOrderFlow();
    }

    @When("admin changes the Manufacture Order status to {string}")
    public void admin_changes_the_manufacture_order_status_to(String status) {

    }

    @Then("the updated status should be displayed as {string} on the Manufacture Order Listing page")
    public void the_updated_status_should_be_displayed_on_the_manufacture_order_listing_page(String status) {

    }

    @Then("the status should also be displayed as {string} on the Manufacture Order Detail page")
    public void the_status_should_also_be_displayed_on_the_manufacture_order_detail_page(String status) {

    }
    
    //TC-MO-14
    @When("admin opens an existing Manufacture Order and clicks Save without filling the mandatory fields")
    public void admin_opens_an_existing_manufacture_order_and_clicks_save_without_filling_the_mandatory_fields() {
        manufactureOrder.mandatoryFieldValidationsForExistingProduct();
    }
    
    //TC-MO-15
	@Given("admin adds multiple product items and raw materials to the Manufacture Order")
	public void admin_adds_multiple_product_items_and_raw_materials_to_the_manufacture_order() {
		manufactureOrder.verifyMultipleProductsAndTotalsFlowForExisting();
	}
	
	@Then("all added products and raw materials should be displayed correctly in the Manufacture Order")
	public void all_added_products_and_raw_materials_should_be_displayed_correctly_in_the_manufacture_order() {
	    // Verify that all added products and raw materials are displayed correctly
	}
    
        
}
