Feature: Product Archive Management verification

  @PA
  @TC_UI_Zlaata_PA_01
  Scenario Outline: TC_UI_Zlaata_PA_01 |Verify that product is successfully archived and displayed in the Product Archive section.| "<TD_ID>"

    Given admin archives a product successfully
    Then the archived product should be displayed successfully in the Product Archive section

    Examples:
      | TD_ID               |
      | TD_UI_Zlaata_PA_01  |
      
       @PA
  @TC_UI_Zlaata_PA_02
  Scenario Outline: TC_UI_Zlaata_PA_02 |Verify that an archived product is successfully restored and displayed in the Product Listing section.| "<TD_ID>"

    Given admin restores an archived product successfully
    Then the restored product should be displayed successfully in the Product Listing section

    Examples:
      | TD_ID               |
      | TD_UI_Zlaata_PA_02  |
      
            @PA
  @TC_UI_Zlaata_PA_03
  Scenario Outline: TC_UI_Zlaata_PA_03 |Verify that archived products are successfully restored using the Bulk Restore option.| "<TD_ID>"

    Given admin selects multiple archived products and clicks the Bulk Restore option
    Then the selected products should be successfully restored and displayed in the Product Listing section.

    Examples:
      | TD_ID               |
      | TD_UI_Zlaata_PA_03  |