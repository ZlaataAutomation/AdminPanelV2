Feature: Supplier Management verification

  @SM
  @TC_UI_Zlaata_SM_01
  Scenario Outline: TC_UI_Zlaata_SM_01 |Verify that a newly created supplier is displayed on the Supplier Listing page| "<TD_ID>"

    Given admin navigates to the Supplier Listing page
    When admin creates a new supplier with valid supplier details
    Then the newly created supplier should be displayed successfully on the Supplier Listing page

    Examples:
      | TD_ID               |
      | TD_UI_Zlaata_SM_01  |
      
   @SM
   @TC_UI_Zlaata_SM_02
   Scenario Outline: TC_UI_Zlaata_SM_02 |Verify that changes made after editing a supplier are updated correctly and displayed on the Supplier Listing page| "<TD_ID>"
	
	 Given admin navigates to the Supplier Listing page
	 And admin views existing supplier details from the preview page
	 When admin edits an existing supplier with valid updated details
	 Then the updated supplier details should be displayed successfully on the Supplier Listing page
	
	Examples:
	  | TD_ID               |
	  | TD_UI_Zlaata_SM_02  |
	  
   @SM
	@TC_UI_Zlaata_SM_03
	Scenario Outline: TC_UI_Zlaata_SM_03 |Verify that contact details and bank details added from the Preview page are saved successfully| "<TD_ID>"
	
	  Given admin navigates to the Supplier Preview page
	  When admin adds valid contact details and bank details from the Preview page
	  Then the contact details and bank details should be saved successfully
	
	Examples:
	  | TD_ID               |
	  | TD_UI_Zlaata_SM_03  |
	  
	  
	@SM
	@TC_UI_Zlaata_SM_04
	Scenario Outline: TC_UI_Zlaata_SM_04 |Verify that the supplier status changes to Inactive when the supplier is marked as inactive| "<TD_ID>"
	
	  When admin marks an active supplier as Inactive
	  Then the supplier status should be updated to Inactive successfully
	
	Examples:
	  | TD_ID               |
	  | TD_UI_Zlaata_SM_04  |
	  
	  
	@SM
	@TC_UI_Zlaata_SM_05
	Scenario Outline: TC_UI_Zlaata_SM_05 |Verify that a deleted supplier is no longer displayed on the Supplier Listing page| "<TD_ID>"
	
	  When admin deletes an existing supplier
	  Then the deleted supplier should no longer be displayed on the Supplier Listing page
	
	Examples:
	  | TD_ID               |
	  | TD_UI_Zlaata_SM_05  |
	  
	  
	@SM
	@TC_UI_Zlaata_SM_06
	Scenario Outline: TC_UI_Zlaata_SM_06 |Verify validation messages are displayed when mandatory fields are left blank while creating a supplier| "<TD_ID>"
	
	  When admin clicks on the Save and Next button without filling the mandatory fields
	  Then validation messages should be displayed for all mandatory fields
	
	Examples:
	  | TD_ID               |
	  | TD_UI_Zlaata_SM_06  |
	  
	@SM
	@TC_UI_Zlaata_SM_07
	Scenario Outline: TC_UI_Zlaata_SM_07 |Verify that multiple contact and bank account details are saved successfully while creating a new supplier| "<TD_ID>"
	
	  When admin creates a new supplier with multiple contact details and bank account details
	  Then the supplier should be created successfully with all contact and bank account details saved
	
	Examples:
	  | TD_ID               |
	  | TD_UI_Zlaata_SM_07  |