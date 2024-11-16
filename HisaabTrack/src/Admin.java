import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Admin {
    // Attributes
    private int adminID;
    private String name;
    private String CNIC;
    private String address;
    private List<InventoryManager> myManagers;
    private List<Invoice> unpaidInvoices;
    private boolean active;

    // Constructor
    public Admin(int adminID, String name, String CNIC, String address) {
        this.adminID = adminID;
        this.name = name;
        this.CNIC = CNIC;
        this.address = address;
        this.active = true;
    }

    public void updateProfile() {
        // Placeholder for implementation
    }

    public void displaySuppliers(List<Supplier> suppliers, Scanner inputScanner) {
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers available.");
            return;
        }
    
        System.out.println("Available Suppliers:");
        for (int i = 0; i < suppliers.size(); i++) {
            Supplier supplier = suppliers.get(i);
            System.out.println((i + 1) + "- Supplier ID: " + supplier.getSupplierID() + 
                               ", Company: " + supplier.getCompany() + 
                               ", Location: " + supplier.getLocation() + 
                               ", Registration No: " + supplier.getRegNo());
        }
    }

    public void displayManagers() {
        if (myManagers.isEmpty()) {
            System.out.println("You are currently have no managers.");
            return;
        }
    
        System.out.println("Manager List:");
        for (int i = 0; i < myManagers.size(); i++) {
            InventoryManager manager = myManagers.get(i);
            System.out.println((i + 1) + "- Manager ID: " + manager.getManagerID() + 
                               ", Manager Name: " + manager.getName() + 
                               ", Manger CNIC: " + manager.getCNIC() + 
                               ", Store Location: " + manager.getManagingStore().getLocation());
        }
    }
    

    // Method Signatures
    public InventoryManager addInventoryManager(int mID, Store s, Scanner inputScanner) {
        try {
            int managerID = mID;
    
            System.out.print("Enter Manager Name: ");
            String _name = inputScanner.nextLine();
    
            System.out.print("Enter CNIC: ");
            String _CNIC = inputScanner.nextLine();
    
            System.out.print("Enter Address: ");
            String _address = inputScanner.nextLine();
    
            InventoryManager newManager = new InventoryManager(managerID, _name, _CNIC, _address);
            newManager.setManagingStore(s);
            System.out.println("Inventory Manager added successfully for Store ID: " + s.getStoreID() + " at " + s.getLocation());
            
            myManagers.add(newManager);

            return newManager; 
    
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter the correct data types.");
            inputScanner.nextLine(); 
        }
    
        return null; 
    }
    

    public boolean removeInventoryManager(Scanner inputScanner) {
        displayManagers();
        System.out.print("Enter your choice (1-" + myManagers.size() + "): ");
        
        try {
            int choice = inputScanner.nextInt();
            inputScanner.nextLine(); 
    
            if (choice < 1 || choice > myManagers.size()) {
                System.out.println("Invalid selection. Please enter a valid number.");
            }
    
            InventoryManager selectedManager = myManagers.get(choice - 1);
            System.out.println("Selected Manager: " + selectedManager.getName() + 
                               " (ID: " + selectedManager.getManagerID() + ") Store Location: " + selectedManager.getManagingStore().getLocation());
            System.out.println("Confirm Removal? (y/n)");
            String option = inputScanner.nextLine();
            if(option == "y" || option =="Y") {
                myManagers.remove(choice - 1);
                return true;
            } else {
                return false;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            inputScanner.nextLine(); 
        }
        return false; // Placeholder for implementation
    }

    public boolean updateInventoryManager(Scanner inputScanner) {
        displayManagers();
        System.out.print("Enter your choice (1-" + myManagers.size() + "): ");
        
        try {
            int choice = inputScanner.nextInt();
            inputScanner.nextLine(); 
    
            if (choice < 1 || choice > myManagers.size()) {
                System.out.println("Invalid selection. Please enter a valid number.");
            }
    
            InventoryManager selectedManager = myManagers.get(choice - 1);
            System.out.println("Selected Manager: " + selectedManager.getName() + 
                               " (ID: " + selectedManager.getManagerID() + ") Store Location: " + selectedManager.getManagingStore().getLocation());

            //perform update

            System.out.println("Confirm update? (y/n)");
            String option = inputScanner.nextLine();
            if(option == "y" || option =="Y") {
                return true;
            } else {
                return false;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            inputScanner.nextLine(); 
        }
        return false;
    }

    public Supplier addSupplier(int sID, Scanner inputScanner) {
        try {
            int supplierID = sID; 

            System.out.print("Enter Supplier Company Name: ");
            String company = inputScanner.nextLine();

            System.out.print("Enter Location: ");
            String location = inputScanner.nextLine();

            System.out.print("Enter Registration Number: ");
            int regNo = inputScanner.nextInt();
            inputScanner.nextLine(); 

            Supplier newSupplier = new Supplier(supplierID, company, location, regNo);
            System.out.println("Supplier added successfully!");

            return newSupplier; 

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter the correct data types.");
            inputScanner.nextLine(); 
        }

        return null;
    }

    public boolean removeSupplier(List<Supplier> suppliers, Scanner inputScanner) {
        displaySuppliers(suppliers, inputScanner);
        System.out.print("Enter your choice (1-" + suppliers.size() + "): ");
        
        try {
            int choice = inputScanner.nextInt();
            inputScanner.nextLine(); 
    
            if (choice < 1 || choice > suppliers.size()) {
                System.out.println("Invalid selection. Please enter a valid number.");
            }
    
            Supplier selectedSupplier = suppliers.get(choice - 1);
            System.out.println("Selected Supplier: " + selectedSupplier.getCompany() + 
                               " (ID: " + selectedSupplier.getSupplierID() + ")");
            System.out.println("Confirm Removal? (y/n)");
            String option = inputScanner.nextLine();
            if(option == "y" || option =="Y") {
                suppliers.remove(choice - 1);
                return true;
            } else {
                return false;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            inputScanner.nextLine(); 
        }
        return false; // Placeholder for implementation
    }

    public boolean updateSupplier(List<Supplier> suppliers, Scanner inputScanner) {
        displaySuppliers(suppliers, inputScanner);
        System.out.print("Enter your choice (1-" + suppliers.size() + "): ");
        
        try {
            int choice = inputScanner.nextInt();
            inputScanner.nextLine(); 
    
            if (choice < 1 || choice > suppliers.size()) {
                System.out.println("Invalid selection. Please enter a valid number.");
            }
    
            Supplier selectedSupplier = suppliers.get(choice - 1);
            System.out.println("Selected Supplier: " + selectedSupplier.getCompany() + 
                               " (ID: " + selectedSupplier.getSupplierID() + ")");

            //Update Logic


            System.out.println("Confirm Update? (y/n)");
            String option = inputScanner.nextLine();
            if(option == "y" || option =="Y") {
                suppliers.remove(choice - 1);
                return true;
            } else {
                return false;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            inputScanner.nextLine(); 
        }
        return false; // Placeholder for implementation
    }

    public boolean payInvoice(Invoice invoice) {
        return false; // Placeholder for implementation
    }

    public boolean addInvoice(Invoice invoice) {
        return false; // Placeholder for implementation
    }

    public Invoice removeInvoice() {
        return null; // Placeholder for implementation
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<InventoryManager> getMyManagers() {
        return myManagers;
    }

    public void setMyManagers(List<InventoryManager> managersUnderAdmin) {
        this.myManagers = managersUnderAdmin;
    }

    public List<Invoice> getUnpaidInvoices() {
        return unpaidInvoices;
    }

    public void setUnpaidInvoices(List<Invoice> unpaidInvoices) {
        this.unpaidInvoices = unpaidInvoices;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
