package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import pages.SizeChart_Page;

public class SizeChart_StepDef {
	TestContext testContext;
	SizeChart_Page size;



public SizeChart_StepDef(TestContext context) {
	testContext = context;
	size = testContext.getPageObjectManager().getSizeChart_Page();
}

@Given("admin creates a new size chart with valid details")
public void admin_creates_a_new_size_chart_with_valid_details() {
	size.validatesizeChartCreation();
}
@Then("the newly created size chart should be displayed successfully on the Size Chart Listing page.")
public void the_newly_created_size_chart_should_be_displayed_successfully_on_the_size_chart_listing_page() {

}

@Given("admin assigns a size chart to a product and navigates to that product page")
public void admin_assigns_a_size_chart_to_a_product_and_navigates_to_that_product_page() {
	size.assignproductInSizecghart(); 
}

@Then("the assigned size chart should be displayed successfully on the product details page.")
public void the_assigned_size_chart_should_be_displayed_successfully_on_the_product_details_page() {
  
}

@Given("admin deletes an existing size chart")
public void admin_deletes_an_existing_size_chart() {
	size.validatedeleteSizeChart();
}
@Then("the selected size chart should be successfully deleted and should no longer be displayed on the Size Chart Listing page.")
public void the_selected_size_chart_should_be_successfully_deleted_and_should_no_longer_be_displayed_on_the_size_chart_listing_page() {
    
}









}
