package stepDef;

import context.TestContext;
import io.cucumber.java.en.*;
import pages.AttributesPage;

public class Attributes_StefDef {
	
	TestContext testContext;
	AttributesPage atbs;



public Attributes_StefDef(TestContext context) {
	testContext = context;
	atbs = testContext.getPageObjectManager().getAttributesPage();
}

//TC-01
@Given("the admin adds a new Attributes with valid details")
public void the_admin_adds_a_new_attributes_with_valid_details() {
    atbs.adminLogin();
}

@Then("the Attributes should be added successfully and displayed correctly in the Attributes List page")
public void the_attributes_should_be_added_successfully_and_displayed_correctly_in_the_attributes_list_page() {
   
}










}
