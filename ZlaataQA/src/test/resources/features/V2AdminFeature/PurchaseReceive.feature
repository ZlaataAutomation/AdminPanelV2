Feature: Purchase Receive Management Verification

@PR
@TC_UI_Zlaata_PR_01
Scenario Outline: TC_UI_Zlaata_PR_01 | Verify that a newly created Purchase Receive In Transit Status is displayed on the Purchase Receive Listing page| "<TD_ID>"

  Given the newly created Purchase Receive should be displayed with Draft status on the Purchase Receive Listing page
  Then the updated Purchase Receive details should be reflected correctly on the Purchase Receive Listing page

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PR_01  |
  
  
  @PR
@TC_UI_Zlaata_PR_02
Scenario Outline: TC_UI_Zlaata_PR_02 |Verify Purchase Receive the Received Status is displayed correctly on Listing and Preview Page.| "<TD_ID>" 

  Given admin navigates to the Purchase Receive module
  When admin creates a Purchase Receive with valid quantity and price per unit

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PR_02  |
  
@PR
@TC_UI_Zlaata_PR_03
Scenario Outline: TC_UI_Zlaata_PR_03 |Verify that the convert bill option should redirect to bill module.| "<TD_ID>"

  Given admin navigates to the Purchase Receive module for convert bill
  Then  that the convert bill option should redirect to bill module

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PR_03  |
  
  
  @PR
@TC_UI_Zlaata_PR_04
Scenario Outline: TC_UI_Zlaata_PR_04 |Verify that validation messages are displayed when mandatory fields are left blank and the user tries to save the Purchase Receive| "<TD_ID>"

  Given admin navigate to Purchase Receive Module.
  Then t=Verify  Purchase Receive  Validation Error message on Purchase Receive Create Page

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PR_04  |
  
  
