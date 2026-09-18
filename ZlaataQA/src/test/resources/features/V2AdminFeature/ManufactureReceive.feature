Feature: Manufacture Receive  Management Verification


@MR
@TC_UI_Zlaata_MR_01
Scenario Outline: TC_UI_Zlaata_MR_01 | Verify that a newly created Manufacture Receive In Transit Status is displayed on the Manufacture Receive Listing page| "<TD_ID>"

  Given the newly created Manufacture Receive should be displayed with Draft status on the Manufacture Receive Listing page
  Then the updated Manufacture Receive details should be reflected correctly on the Manufacture Receive Listing page

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_MR_01  |
  
  
  @MR
@TC_UI_Zlaata_MR_02
Scenario Outline: TC_UI_Zlaata_MR_02 |Verify Manufacture Receive the Received Status is displayed correctly on Listing and Preview Page.| "<TD_ID>" 

  Given admin navigates to the Manufacture Receive module
  When admin creates a Manufacture Receive with valid quantity and price per unit

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_MR_02  |
  
@MR
@TC_UI_Zlaata_MR_03
Scenario Outline: TC_UI_Zlaata_MR_03 |Verify that validation messages are displayed when mandatory fields are left blank on create page and the user tries to save the Manufacture Receive| "<TD_ID>"

  Given admin  navigates to Manufacture Receive Module.
  Then  Verify  Manufacture Receive  Validation Error message on Manufacture Receive Create Page.

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_MR_03  |
  
  
  @MR
@TC_UI_Zlaata_MR_04
Scenario Outline: TC_UI_Zlaata_MR_04 |Verify that validation messages are displayed when mandatory fields are left blank and the user tries to save the Manufacture Receive| "<TD_ID>"

  Given admin navigate to Manufacture Receive Module.
  Then Verify  Manufacture Receive  Validation Error message on Manufacture Receive Create Pages.

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_MR_04  |
  
    @MR
@TC_UI_Zlaata_MR_05
Scenario Outline: TC_UI_Zlaata_MR_05 |Verify admin can delete Manufacture Receive successfully.| "<TD_ID>"

  Given the admin delete Manufacture Receive  details
  Then the Manufacture Receive should be added successfully and removed correctly in the Manufacture Receive List page

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_MR_05  |
  
