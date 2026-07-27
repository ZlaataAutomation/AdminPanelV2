Feature: Attributes Management

  
  @Regression
  @TC_UI_Zlaata_Atbs_01
  Scenario Outline: TC_UI_Zlaata_Atbs_01 |Verify admin can add a new Attributes successfully.|"<TD_ID>"
     
    Given the admin adds a new Attributes with valid details
  Then the Attributes should be added successfully and displayed correctly in the Attributes List page
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_Atbs_01 |