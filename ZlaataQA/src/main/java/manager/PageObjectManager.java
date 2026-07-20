package manager;


import context.TestContext;
import pages.*;
import stepDef.Hooks;


import org.openqa.selenium.WebDriver;

public class PageObjectManager {

    private WebDriver driver;
    private Hooks hooks;
    private LoginPage login;
    private HomePage home;
    private AdminPanelPage admin;
    private AdminPanelExportPage adminExports;
   private Raw_Material_Page raw;
   private AdminLogin_Page adminLogin;
   private ProductStock_Page productstock;
   

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }
   
    
	public Hooks getHooks() {
        TestContext testContext = null;
		return (hooks == null) ? hooks = new Hooks(testContext) : hooks;
    }
  
    
	public LoginPage getLoginPage() {
        return (login == null) ? login = new LoginPage(driver) : login;
    
	}
	
	public HomePage getHomePage() {
        return (home == null) ? home = new HomePage(driver) : home;
    
	}

	public AdminPanelPage getAdminPanelPage() {
        return (admin == null) ? admin = new AdminPanelPage(driver) : admin;
    
	}
	
	
	public AdminPanelExportPage getAdminPanelExportPage() {
		return (adminExports == null) ? adminExports = new AdminPanelExportPage(driver) : adminExports;
	}
	
	public AdminLogin_Page getAdminLogin_Page() {
        return (adminLogin == null) ? adminLogin = new AdminLogin_Page(driver) : adminLogin;
    
	}
	
	public Raw_Material_Page getRaw_Material_Page() {
		return (raw == null) ? raw = new Raw_Material_Page(driver) : raw;
	}
	
	public ProductStock_Page getProductStock_Page() {
		return (raw == null) ? productstock = new ProductStock_Page(driver) : productstock;
	}
	
	
}