Feature: Product Management Verification

@PM
@TC_UI_Zlaata_PM_01
Scenario Outline: TC_UI_Zlaata_PM_01 |Verify that admin can create a product with valid details and credentials and it is reflected correctly on the Product Listing page| "<TD_ID>"

  Given admin navigates to the Product module
  When admin creates a new Product with valid details and credentials
  And admin saves the Product successfully
  Then the newly created Product should be displayed correctly on the Product Listing page

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PM_01  |
  
  
@PM
@TC_UI_Zlaata_PM_02
Scenario Outline: TC_UI_Zlaata_PM_02 |Verify that admin can edit an existing product with all the details and the updated product details are displayed correctly on the Listing and Preview pages| "<TD_ID>"

  Given admin navigates to the Product module
  When admin edits an existing Product with valid details
  Then the updated Product details should be displayed correctly on the Product Listing page
  And the updated Product details should be displayed correctly on the Product Preview page

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PM_02  |  
  
  
  

@PM
@TC_UI_Zlaata_PM_03
Scenario Outline: TC_UI_Zlaata_PM_03 |Verify that adjusting product stock from Preview page reflects the correct stock status and details across the application| "<TD_ID>"

  Given admin navigates to the Product module
  When admin adjusts the stock of an existing Product to zero from the Preview page
  Then the Product should be displayed as Out of Stock with the updated stock details on Stock History, Details and Listing pages

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PM_03  |
  

@PM
@TC_UI_Zlaata_PM_04
Scenario Outline: TC_UI_Zlaata_PM_04 |Verify that adding product stock from Preview page reflects the correct stock status and details across the application| "<TD_ID>"

  Given admin navigates to the Product module
  When admin adds stock to an existing Product from the Preview page
  Then the Product should be displayed as In Stock with the updated stock details on Stock History, Details and Listing pages

Examples:
  | TD_ID               |
  | TD_UI_Zlaata_PM_04  |



@PM
@TC_UI_Zlaata_PM_05
Scenario Outline: TC_UI_Zlaata_PM_05 |Verify mandatory field validation when admin saves the Product without entering required details| "<TD_ID>"

  Given admin navigates to the Product module
  When admin clicks the Save button without entering any Product details
  Then the mandatory field validation messages should be displayed

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_PM_05 |
  
  
  @PM
@TC_UI_Zlaata_PM_06
Scenario Outline: TC_UI_Zlaata_PM_06 |Verify that a partially created Product is displayed as Draft with inactive status on the Product Listing page| "<TD_ID>"

  Given admin navigates to the Product module
  When admin partially creates a Product and saves it
  Then the Product should be displayed with Draft status and inactive status toggle on the Product Listing page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_PM_06 |
  
  
  
  @PM
@TC_UI_Zlaata_PM_07
Scenario Outline: TC_UI_Zlaata_PM_07 |Verify that admin can add multiple product variants with details through the Inventory Setup module and verify them on the Preview page| "<TD_ID>"

  Given admin navigates to the Product module
  When admin adds multiple variants with product details through the Inventory Setup module
  Then all added product variants and their details should be displayed correctly on the Product Preview page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_PM_07 |
  
  

@AM
@TC_UI_Zlaata_AM_01
Scenario Outline: TC_UI_Zlaata_AM_01 |Verify that admin can create an accessory with valid details and it is reflected correctly on the Accessories Listing page| "<TD_ID>"

  Given admin navigates to the Accessories module
  When admin creates a new accessory with valid details and credentials
  Then the newly created accessory should be displayed correctly on the Accessories Listing page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_AM_01 |



@AM
@TC_UI_Zlaata_AM_02
Scenario Outline: TC_UI_Zlaata_AM_02 |Verify that admin can edit an existing accessory and the updated details are reflected correctly on the Accessories Listing page| "<TD_ID>"

  Given admin navigates to the Accessories module
  When admin edits an existing accessory with valid details
  Then the updated accessory details should be displayed correctly on the Accessories Listing page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_AM_02 |
  
  
@AM
@TC_UI_Zlaata_AM_03
Scenario Outline: TC_UI_Zlaata_AM_03 |Verify that adjusting accessory stock from Preview page reflects the correct stock status and details across the application| "<TD_ID>"

  Given admin navigates to the Accessories module
  When admin adjusts the stock of an existing accessory to zero from the Preview page
  Then the accessory should be displayed as Out of Stock with the updated stock details on Stock History, Details and Listing pages

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_AM_03 |


@AM
@TC_UI_Zlaata_AM_04
Scenario Outline: TC_UI_Zlaata_AM_04 |Verify that adding accessory stock from Preview page reflects the correct stock status and details across the application| "<TD_ID>"

  Given admin navigates to the Accessories module
  When admin adds stock to an existing accessory from the Preview page
  Then the accessory should be displayed as In Stock with the updated stock details on Stock History, Details and Listing pages

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_AM_04 |
  
  
  
@AM
@TC_UI_Zlaata_AM_05
Scenario Outline: TC_UI_Zlaata_AM_05 |Verify mandatory field validation when admin saves the accessory without entering required details| "<TD_ID>"

  Given admin navigates to the Accessories module
  When admin clicks the Save button without entering any accessory details
  Then the mandatory field validation messages should be displayed for Accessories

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_AM_05 |
  
  
@AM
@TC_UI_Zlaata_AM_06
Scenario Outline: TC_UI_Zlaata_AM_06 |Verify that a partially created accessory is displayed as Draft with inactive status on the Accessories Listing page| "<TD_ID>"

  Given admin navigates to the Accessories module
  When admin partially creates an accessory and saves it
  Then the accessory should be displayed with Draft status and inactive status toggle on the Accessories Listing page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_AM_06 |


  
  