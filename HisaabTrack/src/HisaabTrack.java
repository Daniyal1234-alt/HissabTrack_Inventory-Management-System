import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class HisaabTrack {
    // Attributes
    private List<Admin> admins;
    private List<Supplier> suppliers;
    private List<InventoryManager> managers;
    private List<Store> stores;
    private ITService IT;

    // Constructor
    public HisaabTrack() {
        admins = new ArrayList<>();
        suppliers = new ArrayList<>();
        managers = new ArrayList<>();
        stores = new ArrayList<>();
        IT = new ITService();
    }

    public boolean login(Scanner inputScanner) {
        return true;
    }

    public boolean signUp(Scanner inputScanner) {
        return true;
    }

    // Assuming the 'stores' list is already populated and inputScanner is defined
    public Store displayStoreMenu(Scanner inputScanner) {
        if (stores.isEmpty()) {
            System.out.println("No stores available.");
            return null;
        }

        System.out.println("Select a store from the list below:");
        
        for (int i = 0; i < stores.size(); i++) {
            Store store = stores.get(i);
            System.out.println((i + 1) + "- Store ID: " + store.getStoreID() + ", Location: " + store.getLocation());
        }

        System.out.print("Enter your choice (1-" + stores.size() + "): ");
        int choice;
        
        try {
            choice = inputScanner.nextInt();
            inputScanner.nextLine(); 

            if (choice < 1 || choice > stores.size()) {
                System.out.println("Invalid selection. Please select a valid store.");
            } else {
                Store selectedStore = stores.get(choice - 1);
                System.out.println("You selected Store ID: " + selectedStore.getStoreID() + " at " + selectedStore.getLocation());
                return selectedStore;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            inputScanner.nextLine();
        }
        return null;
    }

    // Method signatures
    public void addSupplier(int adminID, Scanner inputScanner) {
        Supplier e = null;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                e = admins.get(i).addSupplier(suppliers.size()+1,inputScanner);
                break;
            }
        }
        suppliers.add(e);
        //add new supplier to db
    }
    public void removeSupplier(int adminID, Scanner inputScanner) {
        boolean flag;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                flag = admins.get(i).removeSupplier(suppliers, inputScanner);
                if (flag) {
                    //UI displays successful removal
                    //update DB
                } else {
                    //not removed
                }
                break;
            }
        }
    }
    public void updateSupplier(int adminID, Scanner inputScanner) {
        boolean flag;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                flag = admins.get(i).updateSupplier(suppliers, inputScanner);
                if (flag) {
                    //UI displays successful update
                    //Update DB
                } else {
                    //not removed
                }
                break;
            }
        }
    }

    public Admin addAdmin(Scanner inputScanner) {
        try {
            // Prompt user to enter admin details
            int adminID = admins.size() + 1;
    
            System.out.print("Enter Admin Name: ");
            String name = inputScanner.nextLine();
    
            System.out.print("Enter CNIC: ");
            String CNIC = inputScanner.nextLine();
    
            System.out.print("Enter Address: ");
            String address = inputScanner.nextLine();
    
            // Create a new Admin object with the provided details
            Admin newAdmin = new Admin(adminID, name, CNIC, address);
    
            // Add the new admin to the list
            admins.add(newAdmin);
            System.out.println("Admin added successfully!");
            return newAdmin;
    
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter the correct data types.");
            inputScanner.nextLine(); // Clear the buffer in case of invalid input
        }
        return null;
    }
    
   // public void removeAdmin() {}

    public void addManager(int adminID, Scanner inputScanner) {
        InventoryManager e = null;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                Store s = this.displayStoreMenu(inputScanner);
                e = admins.get(i).addInventoryManager(managers.size()+1, s, inputScanner);
                break;
            }
        }
        managers.add(e);
        //add new manager to db
    }
    public void removeManager(int adminID, Scanner inputScanner) {
        boolean flag;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                flag = admins.get(i).removeInventoryManager(inputScanner);
                if (flag) {
                    //UI displays successful removal
                    //update DB
                } else {
                    //not removed
                }
                break;
            }
        }
    }
    public void updateManager(int adminID, Scanner inputScanner) {
        boolean flag;
        for(int i = 0; i < admins.size(); ++i) {
            if(adminID == admins.get(i).getAdminID()) {
                flag = admins.get(i).updateInventoryManager(inputScanner);
                if (flag) {
                    //UI displays successful update
                    //Update DB
                } else {
                    //not removed
                }
                break;
            }
        }
    }

    public void generateReport() {}

    public void addItem() {}
    public void removeItem() {}

    public void generateOrder() {}
    public void removeOrder() {}

    public void payInvoice() {}

    //supplier system functions
    public void sendProducts(int ID) {
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == ID) {
                suppliers.get(i).sendOrder();
            }
        }
    }
    public void viewRecievedOrders(int ID) {  //new
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == ID) {
                //Display recieved orders on UI
            }
        }
    } 
    public void requestPayment(int ID) { //new
        for(int i=0;i<suppliers.size();++i){
            if(suppliers.get(i).getSupplierID() == ID) {
                suppliers.get(i).requestPayment();
            }
        }
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
