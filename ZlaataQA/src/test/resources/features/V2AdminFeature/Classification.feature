Feature: Product Classification Verification

@Category
@TC_UI_Zlaata_Category_01

Scenario Outline: TC_UI_Zlaata_Category_01 |Verify that admin can create a new category and the category details are reflected correctly on both the Listing page and Detail page| "<TD_ID>"

  Given admin navigates to the Category module
  When admin creates a new category with valid details and saves it
  Then the newly created category details should be displayed correctly on the Category Listing page
  And the same category details should be reflected correctly on the Category Detail page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_Category_01 |

@Category
@TC_UI_Zlaata_Category_02

Scenario Outline: TC_UI_Zlaata_Category_02 |Verify that admin can edit an existing category and the updated category details are reflected correctly on both the Listing page and Detail page| "<TD_ID>"

  Given admin navigates to the Category module
  When admin selects an existing category, updates its details and saves the changes
  Then the updated category details should be displayed correctly on the Category Listing page
  And the updated details should also be reflected correctly on the Category Detail page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_Category_02 |
  

@Category
@TC_UI_Zlaata_Category_03

Scenario Outline: TC_UI_Zlaata_Category_03 |Verify that admin can save a category as Draft, the Draft status is displayed correctly on the Listing page, and the category can be deleted successfully| "<TD_ID>"

  Given admin navigates to the Category module
  When admin creates a new category with valid details and saves it as Draft
  Then the category should be displayed with Draft status on the Category Listing page
  When admin deletes the Draft category
  Then the deleted category should no longer be displayed on the Category Listing page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_Category_03 |
  
  
@Category
@TC_UI_Zlaata_Category_04

Scenario Outline: TC_UI_Zlaata_Category_04 |Verify that admin can create a new Category, activate it, and ensure that the active Category is displayed on the Product Category page| "<TD_ID>"

  Given admin navigates to the Category module
  And admin activates the newly created category
  Then the category status should be displayed as Active on the Category Listing page
  And the newly created category should be displayed on the Product Category page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_Category_04 |

@Category
@TC_UI_Zlaata_Category_05

Scenario Outline: TC_UI_Zlaata_Category_05 |Verify that admin can apply Category Settings to multiple selected categories and save the changes successfully| "<TD_ID>"

  Given admin navigates to the Category module
  When admin selects multiple categories and opens the Category Settings popup
  Then the popup should display Trending, Status, and Custom Delivery Day options
  When admin updates the required settings and clicks Save
  Then the selected settings should be applied successfully to all selected categories

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_Category_05 |
  
  
@Category

@TC_UI_Zlaata_Category_06

Scenario Outline: TC_UI_Zlaata_Category_06 |Verify that admin can import categories successfully and the imported category details are displayed correctly on the Category Listing page| "<TD_ID>"

  Given admin navigates to the Category module
  When admin imports a category using a valid import file
  Then the imported category should be displayed with the correct details on the Category Listing page

Examples:

  | TD_ID                    |
  | TD_UI_Zlaata_Category_06 |
  
  
@Category
@TC_UI_Zlaata_Category_07

Scenario Outline: TC_UI_Zlaata_Category_07 |Verify that admin can export category data successfully and all category details are exported correctly in the downloaded file| "<TD_ID>"

  Given admin navigates to the Category module
  When admin exports the category data to an Excel file
  Then the exported Excel file should contain all category details with the correct column values

Examples:

  | TD_ID                    |
  | TD_UI_Zlaata_Category_07 |

 

@Collection
@TC_UI_Zlaata_Collection_01

Scenario Outline: TC_UI_Zlaata_Collection_01 |Verify that admin can create a new collection and the collection details are reflected correctly on both the Listing page and Detail page| "<TD_ID>"

  Given admin navigates to the Collection module
  When admin creates a new collection with valid details and saves it
  Then the newly created collection details should be displayed correctly on the Collection Listing page
  And the same collection details should be reflected correctly on the Collection Detail page

Examples:
  | TD_ID              |
  | TD_UI_Zlaata_Collection_01 |
  
  
@Collection
@TC_UI_Zlaata_Collection_02

Scenario Outline: TC_UI_Zlaata_Collection_02 |Verify that admin can edit an existing collection and the updated collection details are reflected correctly on both the Listing page and Detail page| "<TD_ID>"

  Given admin navigates to the Collection module
  When admin selects an existing collection, updates its details and saves the changes
  Then the updated collection details should be displayed correctly on the Collection Listing page
  And the updated details should also be reflected correctly on the Collection Detail page

Examples:

  | TD_ID                    |
  | TD_UI_Zlaata_Collection_02 |
  
  
@Collection

@TC_UI_Zlaata_Collection_03

Scenario Outline: TC_UI_Zlaata_Collection_03 |Verify that admin can save a collection as Draft, the Draft status is displayed correctly on the Listing page, and the collection can be deleted successfully| "<TD_ID>"

  Given admin navigates to the Collection module
  When admin creates a new collection with valid details and saves it as Draft
  Then the collection should be displayed with Draft status on the Collection Listing page
  When admin deletes the Draft collection
  Then the deleted collection should no longer be displayed on the Collection Listing page

Examples:

  | TD_ID                      |
  | TD_UI_Zlaata_Collection_03 |
 
  
@Collection
@TC_UI_Zlaata_Collection_04

Scenario Outline: TC_UI_Zlaata_Collection_04 |Verify that admin can create a new Collection, activate it, and ensure that the active Collection is displayed on the Product Collection page| "<TD_ID>"

  Given admin navigates to the Collection module
  And admin activates the newly created collection
  Then the collection status should be displayed as Active on the Collection Listing page
  And the newly created collection should be displayed on the Product Collection page

Examples:

  | TD_ID                      |
  | TD_UI_Zlaata_Collection_04 |
  
  

@Collection

@TC_UI_Zlaata_Collection_05

Scenario Outline: TC_UI_Zlaata_Collection_05 |Verify that admin can apply Collection Settings to multiple selected collections and save the changes successfully| "<TD_ID>"

  Given admin navigates to the Collection module
  When admin selects multiple collections and opens the Collection Settings popup
  Then the selected settings should be applied successfully to all selected collections

Examples:

  | TD_ID                      |
  | TD_UI_Zlaata_Collection_05 |



  
  

