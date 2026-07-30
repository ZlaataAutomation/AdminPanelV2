package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import junit.framework.Assert;
import pages.ProductStock_Page;
import pages.Supplier_Page;

public class Supplier_StepDef {
	
	
	TestContext testContext;
    Supplier_Page supplier;

    public Supplier_StepDef(TestContext context) {
        testContext = context;
        supplier = testContext.getPageObjectManager().getSupplier_Page();
    }  
	
	
	//TC-SM-01
	@Given("admin navigates to the Supplier Listing page")
	public void admin_navigates_to_the_supplier_listing_page() throws InterruptedException {
		supplier.adminLogin();
        supplier.navigatetoSupplierPage();
		
	}


	@When("admin creates a new supplier with valid supplier details")
	public void admin_creates_a_new_supplier_with_valid_supplier_details() {
		supplier.validateCreatenewsupplier();
	    
	}
	@Then("the newly created supplier should be displayed successfully on the Supplier Listing page")
	public void the_newly_created_supplier_should_be_displayed_successfully_on_the_supplier_listing_page() {
		boolean isVerified = supplier.verifySupplierDetailsInListingTable();
        Assert.assertTrue("Newly created supplier details did not match the listing page!", isVerified);
        boolean isPreviewVerified = supplier.verifySupplierDetailsInPreviewPage();
        Assert.assertTrue("Supplier details verification failed on Preview Page!", isPreviewVerified);
	}

	
	//TC-SM-02
	@Given("admin views existing supplier details from the preview page")
    public void admin_views_existing_supplier_details_from_the_preview_page() {
		supplier.validateeditSupplier();
    }

    @When("admin edits an existing supplier with valid updated details")
    public void admin_edits_an_existing_supplier_with_valid_updated_details() {
        
    }

    @Then("the updated supplier details should be displayed successfully on the Supplier Listing page")
    public void the_updated_supplier_details_should_be_displayed_successfully_on_the_supplier_listing_page() {
        boolean isVerified = supplier.verifySupplierDetailsInListingTable();
        Assert.assertTrue("Edited supplier details did not match the listing page!", isVerified);
        
        boolean isPreviewVerified = supplier.verifySupplierDetailsInPreviewPage();
        Assert.assertTrue("Supplier details verification failed on Preview Page!", isPreviewVerified);
    }
     
    
    //TC-SM-03
    @Given("admin navigates to the Supplier Preview page")
    public void admin_navigates_to_the_supplier_preview_page() {
    	
    }

    @When("admin adds valid contact details and bank details from the Preview page")
    public void admin_adds_valid_contact_details_and_bank_details_from_the_preview_page() {
       supplier.validateAddContactAndBank();
    }

    @Then("the contact details and bank details should be saved successfully")
    public void the_contact_details_and_bank_details_should_be_saved_successfully() {
        boolean isVerified = supplier.verifyAddedContactAndBankDetailsInPreview();
        Assert.assertTrue("Contact details and Bank details added from Preview were not saved correctly!", isVerified);
    }
    
    
    
  //TC-SM-04
    @When("admin marks an active supplier as Inactive")
    public void admin_marks_an_active_supplier_as_inactive() throws Exception {
    	supplier.validateactiveandInactivestatus();
    }

    @Then("the supplier status should be updated to Inactive successfully")
    public void the_supplier_status_should_be_updated_to_inactive_successfully() {
    	boolean isVerified = supplier.verifySupplierStatusToggle();
        Assert.assertTrue("Supplier status toggle verification failed on Preview page!", isVerified);
    }
    
    
  //TC-SM-05
    @When("admin deletes an existing supplier")
    public void admin_deletes_an_existing_supplier() throws Exception {
    	supplier.validateDeleteSupplier();

    }

    @Then("the deleted supplier should no longer be displayed on the Supplier Listing page")
    public void the_deleted_supplier_should_no_longer_be_displayed_on_the_supplier_listing_page() {
    	
    	boolean isDeleted = supplier.verifySupplierDeletion();
        Assert.assertTrue("Deleted supplier is still displayed on the Supplier Listing page!", isDeleted);

    }
    
    
    //TC-SM-06
    @When("admin clicks on the Save and Next button without filling the mandatory fields")
    public void admin_clicks_on_the_save_and_next_button_without_filling_the_mandatory_fields() throws Exception {
        supplier.validateMandatoryFieldsValidation();
    }

    @Then("validation messages should be displayed for all mandatory fields")
    public void validation_messages_should_be_displayed_for_all_mandatory_fields() {

        boolean isStep1Validated = supplier.verifyMandatoryFieldValidationMessages();
        Assert.assertTrue("Step 1 mandatory validation failed!", isStep1Validated);

        supplier.fillStep1MandatoryFieldsAndSave();

        boolean isStep2Validated = supplier.verifyAndFillStep2AddressDetails();
        Assert.assertTrue("Step 2 address validation failed!", isStep2Validated);

       
        boolean isStep3Validated = supplier.verifyAndFillStep3BankDetails();
        Assert.assertTrue("Step 3 bank details validation failed!", isStep3Validated);
    }
    
    
  //TC-SM-07
    @When("admin creates a new supplier with multiple contact details and bank account details")
    public void admin_creates_a_new_supplier_with_multiple_contact_details_and_bank_account_details() {
       supplier.validateMultipleContactAndBankAccount();
    }

    
    @Then("the supplier should be created successfully with all contact and bank account details saved")
    public void the_supplier_should_be_created_successfully_with_all_contact_and_bank_account_details_saved() {
       
        boolean isVerified = supplier.verifyAddedMultipleContactAndBankDetailsInPreview();
        Assert.assertTrue("Validation Failed: One or more dynamic contact/bank details missing on Preview page!", isVerified);
    }
}
