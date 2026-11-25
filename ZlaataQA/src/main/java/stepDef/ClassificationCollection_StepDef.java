package stepDef;

import context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ClassificationCategory_Page;
import pages.ClassificationCollection_Page;

public class ClassificationCollection_StepDef {
	
	
	TestContext testContext;
    ClassificationCollection_Page collection;
	
	public ClassificationCollection_StepDef(TestContext context) {
        testContext = context;
        collection = testContext.getPageObjectManager().getClassificationCollection_Page();
    }
	
	
	//TC-UI-Zlaata_Collection-01
	@Given("admin navigates to the Collection module")
	public void admin_navigates_to_the_collection_module() {
		collection.adminLogin();
		collection.navigatetoProductsCollectionPage();
	    
	}

	@When("admin creates a new collection with valid details and saves it")
	public void admin_creates_a_new_collection_with_valid_details_and_saves_it() {
	    collection.createCollectionFlow();
	}

	@Then("the newly created collection details should be displayed correctly on the Collection Listing page")
	public void the_newly_created_collection_details_should_be_displayed_correctly_on_the_collection_listing_page() {
	    
	}

	@Then("the same collection details should be reflected correctly on the Collection Detail page")
	public void the_same_collection_details_should_be_reflected_correctly_on_the_collection_detail_page() {
	    
	}
	
	
	//TC-UI-Zlaata_Collection-02
	@When("admin selects an existing collection, updates its details and saves the changes")
	public void admin_selects_an_existing_collection_updates_its_details_and_saves_the_changes() {
		collection.editCollectionFlow();
	}

	@Then("the updated collection details should be displayed correctly on the Collection Listing page")
	public void the_updated_collection_details_should_be_displayed_correctly_on_the_collection_listing_page() {

	}

	@Then("the updated details should also be reflected correctly on the Collection Detail page")
	public void the_updated_details_should_also_be_reflected_correctly_on_the_collection_detail_page() {
	    
	}
	
	//TC-UI-Zlaata_Collection-03
	@When("admin creates a new collection with valid details and saves it as Draft")
	public void admin_creates_a_new_collection_with_valid_details_and_saves_it_as_draft() {
	    collection.draftAndDeleteCollectionFlow();
	}

	@Then("the collection should be displayed with Draft status on the Collection Listing page")
	public void the_collection_should_be_displayed_with_draft_status_on_the_collection_listing_page() {
	    
	}

	@When("admin deletes the Draft collection")
	public void admin_deletes_the_draft_collection() {
	    
	}

	@Then("the deleted collection should no longer be displayed on the Collection Listing page")
	public void the_deleted_collection_should_no_longer_be_displayed_on_the_collection_listing_page() {
	    
	}
	
	//TC-UI-Zlaata_Collection-04
	@Given("admin activates the newly created collection")
	public void admin_activates_the_newly_created_collection() {
	    collection.activeCollectionFlow();
	}

	@Then("the collection status should be displayed as Active on the Collection Listing page")
	public void the_collection_status_should_be_displayed_as_active_on_the_collection_listing_page() {
	    
	}

	@Then("the newly created collection should be displayed on the Product Collection page")
	public void the_newly_created_collection_should_be_displayed_on_the_product_collection_page() {
	    
	}
	
	//TC-UI-Zlaata_Collection-05
	@When("admin selects multiple collections and opens the Collection Settings popup")
	public void admin_selects_multiple_collections_and_opens_the_collection_settings_popup() {
	    collection.bulkUpdateCategorySettingsFlow();
	}
	
	@Then("the selected settings should be applied successfully to all selected collections")
	public void the_selected_settings_should_be_applied_successfully_to_all_selected_collections() {
	    
	}

}
