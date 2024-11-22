import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.mysql.cj.QueryReturnType;

public class HisaabTrack {
    // Attributes
    private List<Admin> admins;
    private List<Supplier> suppliers;
    private List<InventoryManager> managers;
    private List<Store> stores;
    private ITService IT;
    private SQLDBHandler DB;

    // Constructor
    public HisaabTrack() {
        admins = new ArrayList<>();
        suppliers = new ArrayList<>();
        managers = new ArrayList<>();
        stores = new ArrayList<>();
        IT = new ITService();
        DB = new SQLDBHandler();
    }

    // Method signatures
    public boolean login() {
        return true;
    }
    public boolean signUp() {
        return true;
    }

    // Admin functions
    public void addSupplier(int adminID,String company, String location, int regNo, boolean DBCall) {
        Supplier e = null;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                e = admins.get(i).addSupplier(suppliers.size() + 1,company,location,regNo);
                break;
            }
        }
        suppliers.add(e);
        //add new supplier to db
        if(!DBCall)
            DB.addSupplier(e);
    }
    public boolean removeSupplier(int adminID, int supplierID) {
        boolean flag = false;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                flag = admins.get(i).removeSupplier(supplierID, suppliers);
                if (flag) {
                    //UI displays successful removal
                    //update DB
                	DB.removeSupplier(supplierID);
                } else {
                    //not removed
                }
                break;
            }
        }
        return flag;
    }
    public boolean updateSupplier(int adminID, int supplierID, String company, String location, int regNo) {
        boolean flag = false;
        Supplier e = null;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                e = admins.get(i).updateSupplier(suppliers, supplierID, company, location, regNo);
                //UI displays successful update
                //Update DB
                if(e!=null)
                	DB.updateSupplier(e);
            }
        }
        return flag;
    }
    public Admin addAdmin(String Name, String cnic, String Address,String password, boolean DBCall) {
        int adminID = admins.size() + 1;
        Admin newAdmin = new Admin(adminID, Name, cnic, Address, password);
        // Add the new admin to the list
        admins.add(newAdmin);
        if(!DBCall)
            DB.addAdmin(newAdmin);
        return newAdmin;
    }
    public Store getStore(int storeID) {
    	 for (Store s : this.stores) {
             // Check if the manager's ID matches the provided ID
             if (s.getStoreID() == storeID) {
                 // Return the manager object if a match is found
                 return s;
             }
         }
    	 return null;
    }
    public Supplier getSupplier(int supplierID) {
    	for (Supplier s : this.suppliers) {
            // Check if the manager's ID matches the provided ID
            if (s.getSupplierID() == supplierID) {
                // Return the manager object if a match is found
                return s;
            }
        }
   	 return null;
    }
    public void addManager(int adminID, String Name, String cnic, String address, String password, Store s, boolean DBCall) {
        InventoryManager e = null;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                e = admins.get(i).addInventoryManager(managers.size() + 1, Name, cnic, address,password,  s );
                break;
            }
        }
        managers.add(e);
        //add new manager to db
        if(!DBCall) {
            DB.addInventoryManager(e);
            DB.addAdminInventoryManager(adminID, e.getManagerID());
        }
    }
    public void addUnpaidInvoice(int adminID, Invoice invoice) {
    	for(Admin a: this.admins) {
    		if(a.getAdminID()==adminID) {
    			a.addunpaidinvoice(invoice);
    		}
    	}
    }
    public void addStore(Store s) {
    	this.stores.add(s);
    }
    public String findManagerByID(int ID) {
        // Loop through the list of managers
        for (InventoryManager manager : managers) {
            // Check if the manager's ID matches the provided ID
            if (manager.getManagerID() == ID) {
                // Return the manager object if a match is found
                return manager.getCNIC();
            }
        }
        // Return null if no manager with the given ID is found
        return null;
    }
    public InventoryManager getManagerByID(int ID) {
        for (InventoryManager manager : managers) {
            // Check if the manager's ID matches the provided ID
            if (manager.getManagerID() == ID) {
                // Return the manager object if a match is found
                return manager;
            }
        }
        return null;
    }
    public Supplier getSupplierByID(int ID) {
        for (Supplier s : suppliers) {
            // Check if the manager's ID matches the provided ID
            if (s.getSupplierID() == ID) {
                // Return the manager object if a match is found
                return s;
            }
        }
        return null;
    }

    public boolean removeManager(int adminID, int managerID) {
        boolean flag = false;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                flag = admins.get(i).removeInventoryManager(managerID);
                if (flag) {
                    //UI displays successful removal
                    //update DB
                	DB.removeInventoryManager(findManagerByID(managerID));
                	DB.removeAdminInventoryManager(adminID, managerID);
                } else {
                    //not removed
                }
                break;
            }
        }
        return flag;
    }
    public boolean updateManager(int adminID, int managerID, String Name, String cnic, String Address) {
        InventoryManager e = null;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                e = admins.get(i).updateInventoryManager(managerID, Name, cnic, Address);
                //UI displays successful update
                //Update DB
                if(e!=null)
                	DB.updateInventoryManager(e);	
            }
        }
        return true;
    }
    public List<InventoryManager> getAdminsInventoryManagers(int adminID) {
         for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
               return admins.get(i).getMyManagers();
            }
        }
        return null;
    }
    public List<Invoice> handleUnpaidInvoices(int adminID) {
        for(Admin obj : admins) {
            if(obj.getAdminID() == adminID) {
                return obj.getUnpaidInvoices();
            }
        }
        return null;
    }
    public void payInvoice(int adminID, int invoiceID) {
        Invoice e = null;
        for(Admin obj : admins) {
            if(obj.getAdminID() == adminID) {
                e = obj.payInvoice(invoiceID);
                for(Supplier s : suppliers) {
                    if(s.getSupplierID() == e.getSupplierID()) {
                        s.receivePayment(invoiceID, e.getTotalAmount());
                        break;
                    }
                }
                break;
            }
        }
    }

    // manager functions
    public void makeSale(int managerID, List<Product> p, List<Integer> q) {
        for(InventoryManager obj:managers) {
            if(obj.getManagerID() == managerID) {
                obj.makeSale(p,q);
            }
        }
    }
    public void placeOrder(int managerID, int supplierID, List<Product> p, List<Integer> q) {
        Invoice e = null;
        for(InventoryManager manager:managers) {
            if(manager.getManagerID() == managerID) {
                e = manager.placeOrder(p, q);
                e.setSupplierID(supplierID);
                e.setCreatedBy(managerID);
            }
        }
        for(Supplier s:suppliers) {
            if(s.getSupplierID() == supplierID) {
                s.addIncomingOrder(e);
            }
        }
    }
    public List<Stock> checkStock(int managerID) {
        return getStoreStock(managerID);
    }
    public void generateReport(int managerID) {}
    public List<Invoice> viewOrderStatus(int managerID) {
        return getManagerByID(managerID).getOrders();
    }
    public void updateProfile(int managerID) {}

    
    // Supplier functions
    public ProductCatalog getProductCatalog(int supplierID) {
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == supplierID) {
                return suppliers.get(i).getProducts();
            }
        }
        return null;
    }
    public void addItem(int supplierID, String name, String description, double price, Date MFG, Date EXP, int amount, boolean DBCall) {
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == supplierID) {
                Product p = new Product(-1, name, description, price, MFG, EXP);
                suppliers.get(i).addProduct(p, amount);
                ProductCatalog catalog = suppliers.get(i).getProducts();
                //update catalog in DB
                if(!DBCall) {
                    DB.addProduct(suppliers.get(i), p);
                    DB.addProductCatalog(supplierID, p.getProductID());
                }
            }
        }
    }
    public void removeItem(int supplierID, int productID) {
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == supplierID) {
                suppliers.get(i).removeProduct(productID);
                ProductCatalog catalog = suppliers.get(i).getProducts();
                //update catalog in DB
                DB.removeProductCatalog(productID, supplierID);
                DB.removeProduct(suppliers.get(i), productID);
                
            }
        }
    }
    public void updateItem(int supplierID, int productID, int addedAmount) {
        Product p = null;
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == supplierID) {
                for(int j = 0;j<suppliers.get(i).getProducts().getProduct().size();++j) {
                    if (suppliers.get(i).getProducts().getProduct().get(j).getProductID() == productID) {
                        p = suppliers.get(i).getProducts().getProduct().get(j);
                    }
                }
                suppliers.get(i).addProduct(p,addedAmount);  
                ProductCatalog catalog = suppliers.get(i).getProducts();
                //update catalog in DB        
                //DB.updateCatalog(supplierID, Catalog);
            }
        }
    }
    public void sendProducts(int ID, int invoiceID) {
        int iManagerID = 0;
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == ID) {
                iManagerID = suppliers.get(i).sendOrder(invoiceID);
            }
        }
    }
    public void requestPayment(int ID, int invoiceID) { //new
        int iManagerID = 0;
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == ID) {
                iManagerID = suppliers.get(i).requestPayment(invoiceID);
            }
        }
        //notify manager with corresponding ID
        Invoice obj = null;
        for(int i=0;i<managers.size();++i) {
            if(managers.get(i).getManagerID() == iManagerID) {
                obj = managers.get(i).findInvoiceByID(invoiceID);
            }
        }
        for (Admin admin : admins) {
            if (admin.isMyManager(iManagerID)) {
                admin.addInvoice(obj);
            }
        }

    }
    public List<Invoice> viewRecievedOrders(int ID) {  //new
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == ID) {
                //Display recieved orders on UI
                return suppliers.get(i).getRecievedOrders();
            }
        }
        return null;
    } 
    public List<Invoice> viewCompletedOrders(int ID) {  //new
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == ID) {
                //Display recieved orders on UI
                return suppliers.get(i).getCompletedOrders();
            }
        }
        return null;
    } 

    // Utility functions
    public String findManagerByID(int ID) {
        for (InventoryManager manager : managers) {
            if (manager.getManagerID() == ID) {
                return manager.getCNIC();
            }
        }
        return null;
    }
    public InventoryManager getManagerByID(int ID) {
        for (InventoryManager manager : managers) {
            if (manager.getManagerID() == ID) {
                return manager;
            }
        }
        return null;
    }
    public Supplier getSupplierByID(int ID) {
        for (Supplier s : suppliers) {
            if (s.getSupplierID() == ID) {
                return s;
            }
        }
        return null;
    }
    public void addStore (Store obj) {
        stores.add(obj);
    }
    public List<Stock> getStoreStock(int managerID) {
        for(Store s:stores) {
            if(s.getManagerID() == managerID) {
                return s.getStock();
            }
        }
        return null;
    }

    // Getters and Setters
    public List<Admin> getAdmins() {
        return admins;
    }
    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }
    public List<Supplier> getSuppliers() {
        return suppliers;
    }
    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }
    public List<InventoryManager> getManagers() {
        return managers;
    }
    public void setManagers(List<InventoryManager> managers) {
        this.managers = managers;
    }
    public List<Store> getStores() {
        return stores;
    }
    public void setStores(List<Store> stores) {
        this.stores = stores;
    }
    public ITService getIT() {
        return IT;
    }
    public void setIT(ITService IT) {
        this.IT = IT;
    }
}
