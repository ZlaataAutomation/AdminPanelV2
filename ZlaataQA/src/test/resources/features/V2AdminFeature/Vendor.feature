Feature: Vendor  Management

 @Regression
  @TC_UI_Zlaata_VM_01
  Scenario Outline: TC_UI_Zlaata_VM_01 |Verify that a newly created vendor is displayed on the vendor Listing page.|"<TD_ID>"
     
    Given admin creates a new vendor with valid vendor details
    Then the newly created vendor should be displayed successfully on the vendor Listing page
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_VM_01 |
      
      @Regression
  @TC_UI_Zlaata_VM_02
  Scenario Outline: TC_UI_Zlaata_VM_02 |Verify that changes made after editing a vendor are updated correctly and displayed on the vendor Listing page.|"<TD_ID>"
     
    Given admin views existing vendor details from the preview page
    When admin edits an existing vendor with valid updated details
	 Then the updated vendor details should be displayed successfully on the vendor Listing page
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_VM_02 |
      
       @Regression
  @TC_UI_Zlaata_VM_03
  Scenario Outline: TC_UI_Zlaata_VM_03 |Verify that vendor contact details and bank details added from the Preview page are saved successfully.|"<TD_ID>"
     
    Given admin adds valid contact details and bank details from the vendor Preview page
	  Then tthe vendor contact details and bank details should be saved successfully 
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_VM_03 |
      
           @Regression
  @TC_UI_Zlaata_VM_04
  Scenario Outline: TC_UI_Zlaata_VM_04 |Verify that the vendor status changes to Inactive when the vendor is marked as inactive.|"<TD_ID>"
     
    Given admin marks an active vendor as Inactive
	  Then the vendor status should be updated to Inactive successfully
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_VM_04 |
      
       @Regression
  @TC_UI_Zlaata_VM_05
  Scenario Outline: TC_UI_Zlaata_VM_05 |Verify that a deleted vendor is no longer displayed on the vendor Listing page.|"<TD_ID>"
     
    Given admin deletes an existing vendor
	  Then the deleted vendor should no longer be displayed on the vendor Listing page
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_VM_05 |
      
