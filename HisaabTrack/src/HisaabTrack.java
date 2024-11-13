import java.util.List;

public class HisaabTrack {
    // Attributes
    private List<Admin> admins;
    private List<Supplier> suppliers;
    private List<InventoryManager> managers;
    private List<Store> stores;
    private ITService IT;

    // Constructor
    public HisaabTrack() {}

    // Method signatures
    public void addSupplier() {}
    public void removeSupplier() {}
    public void updateSupplier() {}

    public void addAdmin() {}
    public void removeAdmin() {}

    public void addManager() {}
    public void removeManager() {}
    public void updateManager() {}

    public void generateReport() {}

    public void addItem() {}
    public void removeItem() {}

    public void generateOrder() {}
    public void removeOrder() {}

    public void payInvoice() {}
    public void sendProducts() {}

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
