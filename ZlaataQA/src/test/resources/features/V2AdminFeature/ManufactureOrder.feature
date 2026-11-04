Feature: Manufacture Order Management Verification

@MO
@TC_UI_Zlaata_MO_01
Scenario Outline: TC_UI_Zlaata_MO_01 |Verify that a newly created Manufacture Order is displayed on the Manufacture Order Listing page| "<TD_ID>"

Given admin navigates to the Manufacture Order module
When admin creates a new Manufacture Order with valid details
And admin saves the Manufacture Order
Then the newly created Manufacture Order should be displayed on the Manufacture Order Listing page

Examples:
| TD_ID |
| TD_UI_Zlaata_MO_01 |

@MO
@TC_UI_Zlaata_MO_02
Scenario Outline: TC_UI_Zlaata_MO_02 |Verify that the user is able to edit the Manufacture Order and the edited Manufacture Order details are reflected correctly| "<TD_ID>"

Given admin navigates to the Manufacture Order module
When admin edits an existing Manufacture Order with valid updated details
And admin saves the edited Manufacture Order
Then the updated Manufacture Order details should be reflected correctly on the Manufacture Order Listing page

Examples:
| TD_ID               |
| TD_UI_Zlaata_MO_02  |

@MO
@TC_UI_Zlaata_MO_03
Scenario Outline: TC_UI_Zlaata_MO_03 |Verify that when a product is saved as Draft, the status is displayed as Draft and the Draft product can be deleted| "<TD_ID>"

Given admin navigates to the Manufacture Order module
When admin creates a new product with valid details and saves it as Draft
Then the product status should be displayed as Draft
When admin deletes the Draft product
Then the Draft product should be deleted successfully

Examples:
| TD_ID               |
| TD_UI_Zlaata_MO_03  |

@MO
@TC_UI_Zlaata_MO_04
Scenario Outline: TC_UI_Zlaata_MO_04 |Verify that the "Cancel Items" option is available and that the order status changes to "Cancelled" after cancelling the items| "<TD_ID>"

Given admin navigates to the Manufacture Order module
When admin opens an existing Manufacture Order
Then the "Cancel Items" option should be displayed
When admin cancels the items in the Manufacture Order
Then the Manufacture Order status should be changed to "Cancelled"

Examples:
| TD_ID               |
| TD_UI_Zlaata_MO_04  |

@MO
@TC_UI_Zlaata_MO_05
Scenario Outline: TC_UI_Zlaata_MO_05 |Verify that when an Open Manufacture Order is marked as Issued, the status is changed to Issued on both the Listing page and Detail page| "<TD_ID>"

Given admin navigates to the Manufacture Order module
When admin marks an Open Manufacture Order as Issued
Then the Manufacture Order status should be changed to "Issued" and reflected correctly on both the Listing page and Detail page

Examples:
| TD_ID               |
| TD_UI_Zlaata_MO_05  |


@MO
@TC_UI_Zlaata_MO_06
Scenario Outline: TC_UI_Zlaata_MO_06 |Verify that when the user tries to save a Manufacture Order without entering the mandatory details, the mandatory field validation messages are displayed| "<TD_ID>"

Given admin navigates to the Manufacture Order module
When admin clicks on Save without entering the mandatory details
Then mandatory field validation messages should be displayed for all required fields

Examples:
| TD_ID               |
| TD_UI_Zlaata_MO_06  |


@MO
@TC_UI_Zlaata_MO_07
Scenario Outline: TC_UI_Zlaata_MO_07 |Verify that multiple products and raw materials can be added while creating a new Manufacture Order| "<TD_ID>"

Given admin navigates to the Manufacture Order module
When admin creates a new Manufacture Order
And admin adds multiple products and raw materials to the Manufacture Order
Then multiple products and raw materials should be added successfully to the Manufacture Order

Examples:
| TD_ID               |
| TD_UI_Zlaata_MO_07  |


@MO
@TC_UI_Zlaata_MO_08
Scenario Outline: TC_UI_Zlaata_MO_08 |Verify that the Manufacture Order export functionality works correctly and the corresponding columns are exported accurately in the Excel file| "<TD_ID>"

Given admin navigates to the Manufacture Order module
When admin exports the Manufacture Order data to an Excel file
Then the corresponding Manufacture Order columns should be exported correctly in the Excel file

Examples:
| TD_ID               |
| TD_UI_Zlaata_MO_08  |


@MO
@TC_UI_Zlaata_MO_09

Scenario Outline: TC_UI_Zlaata_MO_09 |Verify that admin can create a Manufacture Order using an existing Product and it is displayed on the Manufacture Order Listing page| "<TD_ID>"

Given admin navigates to the Manufacture Order module
When admin creates a new Manufacture Order using an existing Product saves the Manufacture Order
Then the newly created Manufacture Order should be displayed on the Manufacture Order Listing page

Examples:

| TD_ID                  |
| TD_UI_Zlaata_MO_09     |


@MO
@TC_UI_Zlaata_MO_10

Scenario Outline: TC_UI_Zlaata_MO_10 |Verify that the user is able to edit an existing Manufacture Order and the updated details are reflected correctly on the Manufacture Order Listing page| "<TD_ID>"

  Given admin navigates to the Manufacture Order module
  When admin edits an existing Manufacture Order with valid updated details and saves it
  Then the updated Manufacture Order details should be displayed correctly on the Manufacture Order Listing page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_MO_10 |
  
  
  

@MO
@TC_UI_Zlaata_MO_11

Scenario Outline: TC_UI_Zlaata_MO_11 |Verify that a newly created Manufacture Order can be saved as Draft, the Draft status is displayed correctly, and the Draft Manufacture Order can be deleted| "<TD_ID>"

  Given admin navigates to the Manufacture Order module
  When admin creates a new Manufacture Order with valid details and saves it as Draft
  Then the Manufacture Order status should be displayed as Draft
  When admin deletes the Draft Manufacture Order
  Then the Draft Manufacture Order should be deleted successfully

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_MO_11 |
  
  
  

@MO
@TC_UI_Zlaata_MO_12

Scenario Outline: TC_UI_Zlaata_MO_12 |Verify that the Cancel Items option is available and the Manufacture Order status changes to Cancelled after cancelling the items| "<TD_ID>"

   Given admin navigates to the Manufacture Order module
  When admin selects an existing Manufacture Order from the listing page
  Then the "Cancel Items" option should be available for the selected Manufacture Order
  When admin selects the "Cancel Items" option and confirms the cancellation
  Then the Manufacture Order status should be updated to "Cancelled" on the listing page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_MO_12 |
  
  
  

@MO
@TC_UI_Zlaata_MO_13

Scenario Outline: TC_UI_Zlaata_MO_13 |Verify that an existing Open Manufacture Order can be marked as Issued and the updated status is reflected on both the Listing page and Detail page| "<TD_ID>"

  Given admin navigates to the Manufacture Order module
  When admin selects an existing Manufacture Order with Open status from the listing page
  And admin changes the Manufacture Order status to "Issued"
  Then the updated status should be displayed as "Issued" on the Manufacture Order Listing page
  And the status should also be displayed as "Issued" on the Manufacture Order Detail page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_MO_13 |
  
  
@MO
@TC_UI_Zlaata_MO_14

Scenario Outline: TC_UI_Zlaata_MO_14 |Verify that mandatory field validation messages are displayed when an existing Manufacture Order is saved without entering the required details| "<TD_ID>"

  Given admin navigates to the Manufacture Order module
  When admin opens an existing Manufacture Order and clicks Save without filling the mandatory fields
  Then mandatory field validation messages should be displayed for all required fields

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_MO_14 |
  
  
  
@MO
@TC_UI_Zlaata_MO_15

Scenario Outline: TC_UI_Zlaata_MO_15 |Verify that multiple products and raw materials can be added while creating a new Manufacture Order| "<TD_ID>"

  Given admin navigates to the Manufacture Order module
  And admin adds multiple product items and raw materials to the Manufacture Order
  Then all added products and raw materials should be displayed correctly in the Manufacture Order

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_MO_15 |








