import java.util.List;

public class Admin {
    // Attributes
    private int adminID;
    private String name;
    private String CNIC;
    private String address;
    private List<InventoryManager> managersUnderAdmin;
    private List<Invoice> unpaidInvoices;
    private boolean active;

    // Constructor
    public Admin() {}

    // Method Signatures
    public InventoryManager addInventoryManager(Store s) {
        return null; // Placeholder for implementation
    }

    public boolean removeInventoryManager(Store s) {
        return false; // Placeholder for implementation
    }

    public void updateProfile() {
        // Placeholder for implementation
    }

    public Supplier addSupplier() {
        return null; // Placeholder for implementation
    }

    public boolean removeSupplier(List<Supplier> suppliers) {
        return false; // Placeholder for implementation
    }

    public boolean updateSupplier(Supplier s) {
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

    public List<InventoryManager> getManagersUnderAdmin() {
        return managersUnderAdmin;
    }

    public void setManagersUnderAdmin(List<InventoryManager> managersUnderAdmin) {
        this.managersUnderAdmin = managersUnderAdmin;
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
