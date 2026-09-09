package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import pages.PurchaseOrder_Page;

public class PurchaseOrder_StepDef {
	
	TestContext testContext;
    PurchaseOrder_Page purchaseOrder;

    public PurchaseOrder_StepDef(TestContext context) {
        testContext = context;
        purchaseOrder = testContext.getPageObjectManager().getPurchaseOrder_Page();
    }  
    
    @Given("admin navigates to the Purchase Order module")
    public void admin_navigates_to_the_purchase_order_module() throws Exception {
    	purchaseOrder.adminLogin();
    	purchaseOrder.navigatetoPurchaseOrderPage();
    }
    
  //TC-PO-01
    @When("admin clicks on Add Purchase Order and enters all mandatory details")
    public void admin_clicks_on_add_purchase_order_and_enters_all_mandatory_details() throws Exception {
    	purchaseOrder.createPurchaseOrderFlow();
    }

    @When("admin verifies that the entered mandatory details are reflected correctly on the Purchase Order creation page")
    public void admin_verifies_that_the_entered_mandatory_details_are_reflected_correctly_on_the_purchase_order_creation_page() throws Exception {

    }

    @When("admin saves the Purchase Order as Draft")
    public void admin_saves_the_purchase_order_as_draft() throws Exception {

    }

    @Then("the newly created Purchase Order should be displayed with Draft status on the Purchase Order Listing page")
    public void the_newly_created_purchase_order_should_be_displayed_with_draft_status_on_the_purchase_order_listing_page() {

    }

    @When("admin edits the created Purchase Order and saves the changes")
    public void admin_edits_the_created_purchase_order_and_saves_the_changes() throws Exception {

    }

    @Then("the updated Purchase Order details should be reflected correctly on the Purchase Order Listing page")
    public void the_updated_purchase_order_details_should_be_reflected_correctly_on_the_purchase_order_listing_page() {

    }
    
  //TC-PO-02
    @When("admin creates a Purchase Order with valid quantity and price per unit")
    public void admin_creates_a_purchase_order_with_valid_quantity_and_price_per_unit() throws Exception {
    	purchaseOrder.verifyQtyAndPricePerUnit();
    }

    @Then("the Quantity, Price per Unit, and Total Amount should be displayed and calculated correctly")
    public void the_quantity_price_per_unit_and_total_amount_should_be_displayed_and_calculated_correctly() {

    }
    
  //TC-PO-03
    @When("admin edits an existing Purchase Order with valid updated details")
    public void admin_edits_an_existing_purchase_order_with_valid_updated_details() throws Exception {
    	purchaseOrder.editPurchaseOrderFlow();
    }

    @When("admin saves the edited Purchase Order")
    public void admin_saves_the_edited_purchase_order() throws Exception {

    }

    @Then("the updated Purchase Order details should be saved successfully and reflected correctly")
    public void the_updated_purchase_order_details_should_be_saved_successfully_and_reflected_correctly() {

    }
    
  //TC-PO-04
    @When("admin cancels the raw material items in an existing Purchase Order")
    public void admin_cancels_the_raw_material_items_in_an_existing_purchase_order() throws Exception {
    	purchaseOrder.cancelRawMaterialItemsInPreview();
    }

    @Then("the Purchase Order status should be updated to {string} and reflected correctly on the Listing page and Detail page")
    public void the_purchase_order_status_should_be_updated_to_and_reflected_correctly_on_the_listing_page_and_detail_page(String status){
    	purchaseOrder.verifyCancelledStatus(status);
    }
    
    
  //TC-PO-05
    @When("admin clicks on Save without filling the mandatory fields")
    public void admin_clicks_on_save_without_filling_the_mandatory_fields() throws Exception {

    }
    @Then("validation messages should be displayed for all mandatory Purchase Order fields")
    public void validation_messages_should_be_displayed_for_all_mandatory_purchase_order_fields() {
    	purchaseOrder.verifyMandatoryAndFillDetails();
    }
    
  //TC-PO-06
    @When("admin adds multiple raw materials with valid quantities")
    public void admin_adds_multiple_raw_materials_with_valid_quantities() throws Exception {
    	purchaseOrder.addMultipleRawMaterialsAndVerifyCalcualtions();
    }

    @Then("the available quantity and calculated amount should be displayed correctly for all added raw materials")
    public void the_available_quantity_and_calculated_amount_should_be_displayed_correctly_for_all_added_raw_materials() {

    }
}
