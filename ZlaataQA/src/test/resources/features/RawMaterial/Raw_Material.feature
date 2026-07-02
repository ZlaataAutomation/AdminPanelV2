Feature: Raw Material Management

  
  @Regression
  @TC_UI_Zlaata_Raw_01
  Scenario Outline: TC_UI_Zlaata_Raw_01 |Verify admin can add a new raw material successfully.|"<TD_ID>"
     
    Given the admin adds a new raw material with valid details
  Then the raw material should be added successfully and displayed correctly in the Raw Material List page
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_Raw_01 |
    