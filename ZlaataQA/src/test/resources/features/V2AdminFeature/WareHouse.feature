Feature: Ware House  Management

@Ware
  @TC_UI_Zlaata_WH_01
  Scenario Outline: TC_UI_Zlaata_WH_01 |Verify that a newly created WareHouse  is displayed on the WareHouse Listing page.|"<TD_ID>"
     
    Given admin creates a new WareHouse with valid WareHouse details
    Then the newly created WareHouse should be displayed successfully on the WareHouse Listing page
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_WH_01 |
      
       @TC_UI_Zlaata_WH_02
  Scenario Outline: TC_UI_Zlaata_WH_02 |Verify that changes made after editing a WareHouse are updated correctly and displayed on the WareHouse Listing page.|"<TD_ID>"
     
    Given admin views existing WareHouse details from the preview page
    When admin edits an existing WareHouse with valid updated details
	 Then the updated WareHouse details should be displayed successfully on the WareHouse Listing page
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_WH_02 |
      
       @TC_UI_Zlaata_WH_03
  Scenario Outline: TC_UI_Zlaata_WH_03 |Verify that Set as main Branch functionalty.|"<TD_ID>"
     
Given admin views  WareHouse details from the preview page
    Then the Set as main Branch  should be displayed successfully on the WareHouse Listing page
    

    Examples: 
      | TD_ID                  |
      | TD_UI_Zlaata_WH_03 |