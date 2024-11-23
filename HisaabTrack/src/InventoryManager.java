import java.util.List;

public class InventoryManager {
    // Attributes
    private int managerID;
    private String name;
    private String CNIC;
    private String address;
    private Store managingStore;
    String password;
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private Register register;

    // Constructor
    public InventoryManager(int managerID, String name, String CNIC, String address, String password) {
        this.managerID = managerID;
        this.name = name;
        this.CNIC = CNIC;
        this.address = address;
        this.password = password;
    }
    
    // Method Signatures
    public Product getProduct(int ID) {
        for(Stock s:managingStore.getStock()) {
            if(s.getProduct().getProductID() == ID) {
                return s.getProduct();
            }
        }
        return null;
    }
    public Invoice findInvoiceByID(int ID) {
        for (Invoice i : register.getInvoices()) {
            if (i.getInvoiceID() == ID) {
                return i;
            }
        }
        return null;
    }
    public boolean addStock(Stock s) {
        this.managingStore.addStock(s);
        return true; // Placeholder for implementation
    }

    public boolean removeStock(int sID) {
        this.managingStore.removeStock(sID);
        return true; // Placeholder for implementation
    }

    public void updateStock() {
        for(Invoice e:register.getInvoices()) {
            if(e.isDelivered() && e.isPaidFor()) {
                managingStore.updateStock(e);
                register.removeInvoice(e);
            }
        }
    }

    public List<Invoice> getOrders() {
        return register.getInvoices();
    }

    public void makeSale(List<Product> p, List<Integer> q) {
        managingStore.makeSale(p, q);
    }

    public Invoice placeOrder(List<Product> p, List<Integer> q) {
        return register.generateInvoice(p, q);
    }

    public Report generateReport() {
        return null; // Placeholder for implementation
    }

    public void updateProfile() {
        // Placeholder for implementation
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
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

    public Store getManagingStore() {
        return managingStore;
    }

    public void setManagingStore(Store managingStore) {
        this.managingStore = managingStore;
        setRegister(managingStore.getRegister());;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }
}
