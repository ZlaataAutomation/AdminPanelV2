package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import junit.framework.Assert;
import pages.Vendor_Page;

public class Vendor_StepDef {
	
	TestContext testContext;
	Vendor_Page vendor;



public Vendor_StepDef(TestContext context) {
	testContext = context;
	vendor = testContext.getPageObjectManager().getVendor_Page();
}
	
	
	
	
	
	
	
//TC-01
		@Given("admin creates a new vendor with valid vendor details")
		public void admin_creates_a_new_vendor_with_valid_vendor_details() {
			vendor.validateVendorCreationPage();
		}
		@Then("the newly created vendor should be displayed successfully on the vendor Listing page")
		public void the_newly_created_vendor_should_be_displayed_successfully_on_the_vendor_listing_page() {
			boolean isVerified = vendor.verifyVendorDetailsInListingTable();
	        Assert.assertTrue("Newly created vendor details did not match the listing page!", isVerified);
	        boolean isPreviewVerified = vendor.verifyVendorDetailsInPreviewPage();
	        Assert.assertTrue("vendor details verification failed on Preview Page!", isPreviewVerified);
		}

//TC-02

			@Given("admin views existing vendor details from the preview page")
			public void admin_views_existing_vendor_details_from_the_preview_page() {
			vendor.validateEditVendorDetails();
			}
			@When("admin edits an existing vendor with valid updated details")
			public void admin_edits_an_existing_vendor_with_valid_updated_details() {
			    
			}
			@Then("the updated vendor details should be displayed successfully on the vendor Listing page")
			public void the_updated_vendor_details_should_be_displayed_successfully_on_the_vendor_listing_page() {
				boolean isVerified = vendor.verifyVendorDetailsInListingTable();
		        Assert.assertTrue("Edited supplier details did not match the listing page!", isVerified);
		        
		        boolean isPreviewVerified = vendor.verifyVendorDetailsInPreviewPage();
		        Assert.assertTrue("Supplier details verification failed on Preview Page!", isPreviewVerified);
			}
//TC-03
				@Given("admin adds valid contact details and bank details from the vendor Preview page")
				public void admin_adds_valid_contact_details_and_bank_details_from_the_vendor_preview_page() {
				  vendor.validateAddContactAndBank(); 
				}
				@Then("tthe vendor contact details and bank details should be saved successfully")
				public void tthe_vendor_contact_details_and_bank_details_should_be_saved_successfully() {
					 boolean isVerified = vendor.verifyAddedContactAndBankDetailsInPreview();
				        Assert.assertTrue("Contact details and Bank details added from Preview were not saved correctly!", isVerified);
				}

//TC-04
				
					@Given("admin marks an active vendor as Inactive")
					public void admin_marks_an_active_vendor_as_inactive() {
					 vendor.validateactiveandInactivestatus();
					}
					@Then("the vendor status should be updated to Inactive successfully")
					public void the_vendor_status_should_be_updated_to_inactive_successfully() {
						boolean isVerified = vendor.verifySupplierStatusToggle();
				        Assert.assertTrue("Vendor status toggle verification failed on Preview page!", isVerified);
					}

//TC-05
					
						@Given("admin deletes an existing vendor")
						public void admin_deletes_an_existing_vendor() {
						   vendor.validateDeleteVendor();
						}
						@Then("the deleted vendor should no longer be displayed on the vendor Listing page")
						public void the_deleted_vendor_should_no_longer_be_displayed_on_the_vendor_listing_page() {
							boolean isDeleted = vendor.verifySupplierDeletion();
					        Assert.assertTrue("Deleted supplier is still displayed on the Supplier Listing page!", isDeleted);
						}








}
