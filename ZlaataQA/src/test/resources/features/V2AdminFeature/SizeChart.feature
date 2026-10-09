Feature: Size Chart Management verification

  @SC
  @TC_UI_Zlaata_SC_01
  Scenario Outline: TC_UI_Zlaata_SC_01 |Verify that a new size chart is successfully created and displayed on the Size Chart Listing page.| "<TD_ID>"

    Given admin creates a new size chart with valid details
    Then the newly created size chart should be displayed successfully on the Size Chart Listing page.
    Examples:
      | TD_ID               |
      | TD_UI_Zlaata_SC_01  |
       @SC
  @TC_UI_Zlaata_SC_02
  Scenario Outline: TC_UI_Zlaata_SC_02 |Verify that the assigned size chart is displayed in the product details of the associated product.| "<TD_ID>"

    Given admin assigns a size chart to a product and navigates to that product page
    Then the assigned size chart should be displayed successfully on the product details page.
    Examples:
      | TD_ID               |
      | TD_UI_Zlaata_SC_02  |
      
             @SC
  @TC_UI_Zlaata_SC_03
  Scenario Outline: TC_UI_Zlaata_SC_03 |Verify that a size chart can be successfully deleted from the Size Chart Listing page.| "<TD_ID>"

    Given admin deletes an existing size chart
    Then the selected size chart should be successfully deleted and should no longer be displayed on the Size Chart Listing page.
    Examples:
      | TD_ID               |
      | TD_UI_Zlaata_SC_03  |