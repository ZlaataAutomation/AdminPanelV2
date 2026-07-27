Feature: Product Stock Management


   @PS
   @TC_UI_Zlaata_PS_01
   Scenario Outline: TC_UI_Zlaata_PS_01 |Verify that stock changes are reflected and saved successfully after adjusting stock.| "<TD_ID>"

   Given the admin navigates to the Product Stock module
   When the admin adjusts the stock of an existing product with valid stock details
   Then the adjusted stock quantity should be updated successfully and reflected correctly in the Product Stock list

   Examples:
      | TD_ID              |
      | TD_UI_Zlaata_PS_01 |

  @PS
  @TC_UI_Zlaata_PS_02
  Scenario Outline: TC_UI_Zlaata_PS_02 |Verify that stock changes are reflected and saved successfully after adding stock.| "<TD_ID>"

    Given the admin navigates to the Product Stock module and click on product modules
    When the admin adds stock to an existing product with valid stock details
    Then the stock quantity should be updated successfully and reflected correctly in the Product Stock list

    Examples:
      | TD_ID              |
      | TD_UI_Zlaata_PS_02 |
      
  @PS
  @TC_UI_Zlaata_PS_03
  Scenario Outline: TC_UI_Zlaata_PS_03 |Verify that the low stock alert is saved and reflected successfully for the product.| "<TD_ID>"

  	Given the admin navigates to the Product Stock module and click on product module
  	When the admin sets a low stock alert for an existing product with valid details
  	Then the low stock alert should be saved successfully and reflected correctly in the Product Stock list

    Examples:
      | TD_ID              |
      | TD_UI_Zlaata_PS_03 |
      
      