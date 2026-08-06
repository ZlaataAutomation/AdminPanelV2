Feature: Raw Material Management

  
  @Regression
  @TC_UI_Zlaata_Raw_01
  Scenario Outline: TC_UI_Zlaata_Raw_01 |Verify admin can add a new raw material successfully.|"<TD_ID>"
     
    Given the admin adds a new raw material with valid details
  Then the raw material should be added successfully and displayed correctly in the Raw Material List page
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_Raw_01 |
    
      @Regression
  @TC_UI_Zlaata_Raw_02
  Scenario Outline: TC_UI_Zlaata_Raw_02 |Verify that stock changes are reflected and saved successfully after adjusting stock on Raw Material Section.|"<TD_ID>"
     
      Given the admin navigates to the Raw Material module for stock adjust
   When the admin adjusts the stock of an existing product with valid stock details on Raw Material
   Then the adjusted stock quantity should be updated successfully and reflected correctly in the Raw Material list, History, and Preview Page.
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_Raw_02 |
      
  @Regression
  @TC_UI_Zlaata_Raw_03
  Scenario Outline: TC_UI_Zlaata_Raw_03 |Verify that stock changes are reflected and saved successfully after adding stock on Raw Material Section.| "<TD_ID>"

    Given the admin navigates to the Raw Material module for stock add.
    When the admin adds stock to an existing product with valid stock details on Raw Material
    Then the stock quantity should be updated successfully and reflected correctly in the Raw Material list, History, and Preview Page

    Examples:
      | TD_ID              |
      | TD_UI_Zlaata_Raw_03 |     
      
  @Regression
  @TC_UI_Zlaata_Raw_04
  Scenario Outline: TC_UI_Zlaata_Raw_04 |Verify that the low stock alert is saved and reflected successfully on Raw Material Section.| "<TD_ID>"

  	Given the admin navigates to the Raw Material module and click for set low stock
  	When the admin sets a low stock alert for an existing product with valid details on Raw Material module
  	Then the low stock alert should be saved successfully and reflected correctly Raw Material module.

    Examples:
      | TD_ID              |
      | TD_UI_Zlaata_Raw_04 |    
      
       @Regression
  @TC_UI_Zlaata_Raw_05
  Scenario Outline: TC_UI_Zlaata_Raw_05 |Verify admin can import Raw Material successfully.| "<TD_ID>"

  	Given the admin imports the Raw Material file successfully
    Then the imported Raw Material should be displayed in the list

    Examples:
      | TD_ID              |
      | TD_UI_Zlaata_Raw_05 |  
      
            @Regression
  @TC_UI_Zlaata_Raw_06
  Scenario Outline: TC_UI_Zlaata_Raw_06 |Verify admin can Export Last 7 Days Raw Material.| "<TD_ID>"

  	Given the admin exports the Raw Material for the last 7 days successfully
    Then the exported Raw Material data should be validated

    Examples:
      | TD_ID              |
      | TD_UI_Zlaata_Raw_06 |  