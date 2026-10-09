package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import pages.ProductArchive_Page;

public class ProductArchive_StepDef {
	TestContext testContext;
	ProductArchive_Page archive;
	


	public ProductArchive_StepDef(TestContext context) {
		testContext = context;
		archive = testContext.getPageObjectManager().getProductArchive_Page();
	}

		@Given("admin archives a product successfully")
		public void admin_archives_a_product_successfully() throws InterruptedException {
			archive.validateProductArchive();
		}

		@Then("the archived product should be displayed successfully in the Product Archive section")
		public void the_archived_product_should_be_displayed_successfully_in_the_product_archive_section() {
		 
		}

	
			@Given("admin restores an archived product successfully")
			public void admin_restores_an_archived_product_successfully() {
				archive.validateRestoreArchivePRoduct();
			}

			@Then("the restored product should be displayed successfully in the Product Listing section")
			public void the_restored_product_should_be_displayed_successfully_in_the_product_listing_section() {
			 
			}


				@Given("admin selects multiple archived products and clicks the Bulk Restore option")
				public void admin_selects_multiple_archived_products_and_clicks_the_bulk_restore_option() {
					archive.validateBulkRestoreArchiveProduct();
				}

				@Then("the selected products should be successfully restored and displayed in the Product Listing section.")
				public void the_selected_products_should_be_successfully_restored_and_displayed_in_the_product_listing_section() {
				
				}






}
