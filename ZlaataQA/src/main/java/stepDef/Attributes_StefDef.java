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
public void the_admin_adds_a_new_attributes_with_valid_details() throws InterruptedException {
    atbs.validateAttributesCreateFlow();
}

@Then("the Attributes should be added successfully and displayed correctly in the Attributes List page")
public void the_attributes_should_be_added_successfully_and_displayed_correctly_in_the_attributes_list_page() {
   
}
//TC-02
@Given("the admin Edit a  Attributes with valid details")
public void the_admin_edit_a_attributes_with_valid_details() throws InterruptedException {
    atbs.validateAttributesEditFlow();
}
@Then("the editted Attributes should be added successfully and displayed correctly in the Attributes List page")
public void the_editted_attributes_should_be_added_successfully_and_displayed_correctly_in_the_attributes_list_page() {
   
}
//TC-03
@Given("the admin delete Attributes  details")
public void the_admin_delete_attributes_details() throws InterruptedException {
    atbs.valiateDeleteAttribute();

}
@Then("the Attributes should be added successfully and removed correctly in the Attributes List page")
public void the_attributes_should_be_added_successfully_and_removed_correctly_in_the_attributes_list_page() {
  
}













}
