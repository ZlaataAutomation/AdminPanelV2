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
   private AttributesPage atbs;
   private Supplier_Page supplier;
   private Vendor_Page vendor;
   private PurchaseOrder_Page purchaseOrder;
   private Purchase_Receive_Page purchaseReceive;
   private WareHouse_Page ware;
   private ManufactureOrder_Page maufactureOrder;
   private ManufactureReceive_Page manufactureReceive;

   
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
	
	public AttributesPage getAttributesPage() {
		return (atbs == null) ? atbs = new AttributesPage(driver) : atbs;
	}
	
	public Supplier_Page getSupplier_Page() {
		return (supplier == null) ? supplier = new Supplier_Page(driver) : supplier;
	}
	
	public Vendor_Page getVendor_Page() {
		return (vendor == null) ? vendor = new Vendor_Page(driver) : vendor;
	}
	
	public PurchaseOrder_Page getPurchaseOrder_Page() {
		return (purchaseOrder == null) ? purchaseOrder = new PurchaseOrder_Page(driver) : purchaseOrder;
	}
	public Purchase_Receive_Page getPurchase_Receive_Page() {
		return (purchaseReceive == null) ? purchaseReceive = new Purchase_Receive_Page(driver) : purchaseReceive;
	}
	public WareHouse_Page getWareHouse_Page() {
		return (ware == null) ? ware = new WareHouse_Page(driver) : ware;
	}
	
	public ManufactureOrder_Page getManufacutreOrder_Page() {
		return (maufactureOrder == null) ? maufactureOrder = new ManufactureOrder_Page(driver) : maufactureOrder;
	}
	public ManufactureReceive_Page getManufactureReceive_Page() {
		return (manufactureReceive == null) ? manufactureReceive = new ManufactureReceive_Page(driver) : manufactureReceive;
	}
	
}