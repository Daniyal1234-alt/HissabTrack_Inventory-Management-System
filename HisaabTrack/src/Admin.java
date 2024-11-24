import java.util.ArrayList;
import java.util.List;

public class Admin {
    // Attributes
    private int adminID;
    private String name;
    private String CNIC;
    private String address;
    private String password;
    public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	private List<InventoryManager> myManagers;
    private List<Invoice> unpaidInvoices;
    private boolean active;

    // Constructor
    public Admin() {
    	  this.adminID = 0;
          this.name = "\0";
          this.CNIC = "\0";
          this.address = "\0";
          this.active = true;
          myManagers = new ArrayList<>();
          unpaidInvoices = new ArrayList<>();
    }
    public Admin(int adminID, String name, String CNIC, String address, String password) {
        this.adminID = adminID;
        this.name = name;
        this.CNIC = CNIC;
        this.address = address;
        this.active = true;
        this.password = password;
        myManagers = new ArrayList<>();
        unpaidInvoices = new ArrayList<>();
    }
    public void addunpaidinvoice(Invoice e) {
    	this.unpaidInvoices.add(e);
    }
    public void updateProfile(String Name, String cnic, String Address,String password) {
        // Placeholder for implementation
        this.name = Name;
        this.CNIC = cnic;
        this.address = Address;
        this.password = password;
    }

    // Method Signatures
    public boolean isMyManager(int ID) {
        for(int i=0;i<myManagers.size();++i) {
            if(myManagers.get(i).getManagerID() == ID) {
                return true;
            }
        }
        return false;
    }

    public double payInvoice(int invoiceID) {
        for(Invoice obj:unpaidInvoices ) {
            if(obj.getInvoiceID() == invoiceID) {
            	double amount = obj.getTotalAmount();
                unpaidInvoices.remove(this.getInvoiceByID(invoiceID));
                return amount;
            }
        }
        return 0;
    }
    
    public Invoice getInvoiceByID(int ID) {
    	for(Invoice e: unpaidInvoices) {
    		if(e.getInvoiceID() == ID) {
    			return e;
    		}
    	}
    	return null;
    }

    public InventoryManager addInventoryManager(int mID, String Name, String cnic, String Address, Store s, String password) {
        InventoryManager Manager = new InventoryManager(mID, Name, cnic, Address, password);
        Manager.setManagingStore(s);            
        myManagers.add(Manager);
        return Manager; 
    }
    

    public boolean removeInventoryManager(int managerID) {
        // search for this ID in admin's manager list and remove
        for(int i=0;i<myManagers.size();++i) {
            if(myManagers.get(i).getManagerID() == managerID) {
                myManagers.remove(i);
                return true;
            }
        }
        return false;
    }

    public InventoryManager updateInventoryManager(int managerID, String Name, String cnic, String Address) {
        // search for this ID in admin's manager list and update non empty strings
        for(int i=0;i<myManagers.size();++i) {
            if(myManagers.get(i).getManagerID() == managerID) {
                if(!Name.isBlank())
                    myManagers.get(i).setName(Name);
                if(!Address.isBlank())
                    myManagers.get(i).setAddress(Address);
                if(!cnic.isBlank())
                    myManagers.get(i).setCNIC(cnic);
                return myManagers.get(i);
            }
        }
        return null;
    }

    public Supplier addSupplier(int sID, String company, String location, int regNo, String password) {
            Supplier newSupplier = new Supplier(sID, company, location, regNo, password);
            return newSupplier; 
    }

    public boolean removeSupplier(int supplierID, List<Supplier> s) {
        //Search for this ID in system'supplier list and remove
        for(int i=0;i<s.size();++i) {
            if(s.get(i).getSupplierID() == supplierID) {
                s.remove(i);
                return true;
            }
        }
        return false; 
    }

    public Supplier updateSupplier(List<Supplier> s, int supplierID, String company, String location, int regNo) {
        //if string values non-empty then update
        //if regNo != -1 then update
        for(int i=0;i<s.size();++i) {
            if(s.get(i).getSupplierID() == supplierID) {
                if(!company.isBlank());
                    s.get(i).setCompany(company);
                if(!location.isBlank());
                    s.get(i).setLocation(location);
                if(regNo!=-1);       
                    s.get(i).setRegNo(regNo);         
                return s.get(i);
            }
        }
        return null; // Placeholder for implementation
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
