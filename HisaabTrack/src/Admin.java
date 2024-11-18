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
    public InventoryManager addInventoryManager(int mID, String Name, String cnic, String Address, Store s) {
        InventoryManager Manager = new InventoryManager(mID, Name, cnic, Address);
        Manager.setManagingStore(s);            
        myManagers.add(Manager);
        return Manager; 
    }
    

    public boolean removeInventoryManager(int managerID) {
        // search for this ID in admin's manager list and remove
        return false; // Placeholder for implementation
    }

    public boolean updateInventoryManager(int managerID, String Name, String cnic, String Address) {
        // search for this ID in admin's manager list and update non empty strings
        return false;
    }

    public Supplier addSupplier(int sID, String company, String location, int regNo) {
            Supplier newSupplier = new Supplier(sID, company, location, regNo);
            return newSupplier; 
    }

    public boolean removeSupplier(int supplierID, List<Supplier> s) {
        //Search for this ID in system'supplier list and remove
        return false; // Placeholder for implementation
    }

    public boolean updateSupplier(List<Supplier> s, int supplierID, String company, String location, int regNo) {
        //if string values non-empty then update
        //if regNo != -1 then update
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
