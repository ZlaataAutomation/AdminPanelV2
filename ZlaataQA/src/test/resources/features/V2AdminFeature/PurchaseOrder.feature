Feature: Purchase Order Management Verification

@PO
@TC_UI_Zlaata_PO_01
Scenario Outline: TC_UI_Zlaata_PO_01 | Verify that a newly created Purchase Order is displayed on the Purchase Order Listing page| "<TD_ID>"

  Given admin navigates to the Purchase Order module
  When admin clicks on Add Purchase Order and enters all mandatory details
  And admin verifies that the entered mandatory details are reflected correctly on the Purchase Order creation page
  And admin saves the Purchase Order as Draft
  Then the newly created Purchase Order should be displayed with Draft status on the Purchase Order Listing page
  When admin edits the created Purchase Order and saves the changes
  Then the updated Purchase Order details should be reflected correctly on the Purchase Order Listing page

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PO_01  |
  
  
  @PO
@TC_UI_Zlaata_PO_02
Scenario Outline: TC_UI_Zlaata_PO_02 |Verify that the Quantity, Price per Unit, and Total Amount are calculated and displayed correctly based on the entered quantity and price per unit| "<TD_ID>"

  Given admin navigates to the Purchase Order module
  When admin creates a Purchase Order with valid quantity and price per unit
  Then the Quantity, Price per Unit, and Total Amount should be displayed and calculated correctly

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PO_02  |
  
@PO
@TC_UI_Zlaata_PO_03
Scenario Outline: TC_UI_Zlaata_PO_03 |Verify that the edited Purchase Order details are saved successfully after editing the Purchase Order| "<TD_ID>"

  Given admin navigates to the Purchase Order module
  When admin edits an existing Purchase Order with valid updated details
  And admin saves the edited Purchase Order
  Then the updated Purchase Order details should be saved successfully and reflected correctly

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PO_03  |
  
  
  @PO
@TC_UI_Zlaata_PO_04
Scenario Outline: TC_UI_Zlaata_PO_04 |Verify that after cancelling the raw material items, the Purchase Order status changes to "Cancelled" and is reflected correctly on both the Listing page and Detail page| "<TD_ID>"

  Given admin navigates to the Purchase Order module
  When admin cancels the raw material items in an existing Purchase Order
  Then the Purchase Order status should be updated to "Cancelled" and reflected correctly on the Listing page and Detail page

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PO_04  |
  
  
  @PO
@TC_UI_Zlaata_PO_05
Scenario Outline: TC_UI_Zlaata_PO_05 |Verify that the number of due days is displayed correctly on the Listing page| "<TD_ID>"

  Given admin navigates to the Purchase Order module
  When admin views an overdue Purchase Order on the Listing page
  Then the number of due days should be displayed correctly

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PO_05  |