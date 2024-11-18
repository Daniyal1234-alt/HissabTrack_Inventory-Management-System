import java.nio.channels.IllegalBlockingModeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mysql.cj.QueryReturnType;

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

    public boolean login(Scanner inputScanner) {
        return true;
    }

    public boolean signUp(Scanner inputScanner) {
        return true;
    }

    // Method signatures
    public void addSupplier(int adminID,String company, String location, int regNo) {
        Supplier e = null;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                e = admins.get(i).addSupplier(suppliers.size() + 1,company,location,regNo);
                break;
            }
        }
        suppliers.add(e);
        //add new supplier to db
        DB.addSupplier(e);
    }
    public void removeSupplier(int adminID, int supplierID) {
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
    }
    public boolean updateSupplier(int adminID, int supplierID, String company, String location, int regNo) {
        boolean flag = false;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                flag = admins.get(i).updateSupplier(suppliers, supplierID, company, location, regNo);
                if (flag) {
                    //UI displays successful update
                    //Update DB
                	DB.updateSupplier();
                } else {
                    //not removed
                }
                break;
            }
        }
        return flag;
    }

    public Admin addAdmin(String Name, String cnic, String Address) {
        int adminID = admins.size() + 1;
        Admin newAdmin = new Admin(adminID, Name, cnic, Address);
        // Add the new admin to the list
        admins.add(newAdmin);
        DB.addAdmin(newAdmin);
        return newAdmin;
    }
    
   // public void removeAdmin() {}

    public void addManager(int adminID, String Name, String cnic, String address, Store s) {
        InventoryManager e = null;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                e = admins.get(i).addInventoryManager(managers.size() + 1, Name, cnic, address, s);
                break;
            }
        }
        managers.add(e);
        //add new manager to db
        DB.addInventoryManager(e);
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

    public void removeManager(int adminID, int managerID) {
        boolean flag = false;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                flag = admins.get(i).removeInventoryManager(managerID);
                if (flag) {
                    //UI displays successful removal
                    //update DB
                	DB.removeInventoryManager(findManagerByID(managerID));
                	
                } else {
                    //not removed
                }
                break;
            }
        }
    }
    public boolean updateManager(int adminID, int managerID, String Name, String cnic, String Address) {
        boolean flag = false;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                flag = admins.get(i).updateInventoryManager(managerID, Name, cnic, Address);
                if (flag) {
                    //UI displays successful update
                    //Update DB
                	DB.updateInventoryManager();
                	
                } else {
                    //not removed
                }
                break;
            }
        }
        return flag;
    }

    public void generateReport() {}

    public void addItem() {}
    public void removeItem() {}

    public void generateOrder() {}
    public void removeOrder() {}

    public void payInvoice() {}

    //supplier system functions
    public void sendProducts(int ID, int invoiceID) {
        int iManagerID = 0;
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == ID) {
                iManagerID = suppliers.get(i).sendOrder(invoiceID);
            }
        }
        //notify manager with corresponding ID
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
    public void requestPayment(int ID, int invoiceID) { //new
        int iManagerID = 0;
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == ID) {
                iManagerID = suppliers.get(i).requestPayment(invoiceID);
            }
        }
        //notify manager with corresponding ID
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
