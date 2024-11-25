import java.util.ArrayList;
import java.util.List;

public class Supplier {
    // Attributes
    private int supplierID;
    private String company;
    private String location;
    private int regNo;
    private double balance;
    private ProductCatalog products;
    private List<Invoice> recievedOrders;
    private List<Invoice> sentOrders;
    public Invoice getRecievedOrdersByID(int ID) {
        for(Invoice invoice : recievedOrders){
            if(invoice.getInvoiceID() == ID){
                return invoice;
            }
        }
        return null;
    }
    public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public List<Invoice> getSentOrders() {
		return sentOrders;
	}
	public void setSentOrders(List<Invoice> sentOrders) {
		this.sentOrders = sentOrders;
	}
	public String getPasswordString() {
		return passwordString;
	}
	public void setPasswordString(String passwordString) {
		this.passwordString = passwordString;
	}
	public void setRecievedOrders(List<Invoice> recievedOrders) {
		this.recievedOrders = recievedOrders;
	}
	public void addDeliveredOrder(Invoice i) {
		this.sentOrders.add(i);
	}
	private String passwordString;
    // Constructor
    public Supplier(int supplierID, String company, String location, int regNo, String password) {
        this.supplierID = supplierID;
        this.company = company;
        this.location = location;
        this.regNo = regNo;
        products = new ProductCatalog();
        recievedOrders = new ArrayList<>();
        sentOrders = new ArrayList<>();
        this.passwordString = password;
        balance = 0;

    }
    // Method Signatures
    public int getSupplierID() {
        return supplierID;
    }

    public List<Invoice> getRecievedOrders() {
        return recievedOrders;
    }

    public List<Invoice> getCompletedOrders() {
        return sentOrders;
    }
    
    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRegNo() {
        return regNo;
    }

    public void setRegNo(int regNo) {
        this.regNo = regNo;
    }

    public ProductCatalog getProducts() {
        return products;
    }

    public void setProducts(ProductCatalog products) {
        this.products = products;
    }

    public boolean addIncomingOrder (Invoice order) {
    	if(checkStock(order)) {
    		recievedOrders.add(order);
    		return true;
    	}
    	return false;
    }

    public void receivePayment(int invoiceID, double payment) {
        balance += payment;
        for(Invoice e : sentOrders) {
            if(e.getInvoiceID() == invoiceID) {
                e.setPaymentStatus(true);
            }
        }
    }

    public void addProduct(Product p, int amount) {
        products.addProduct(p, amount);
    }

    public void removeProduct(int productID) {
        products.removeProduct(productID);
    }

    public boolean checkStock(Invoice order) {
        //check whether supplier has the facilities to complete an order
        for(int i = 0; i < order.getProducts().size(); ++i) {
            if(products.getProduct().contains(order.getProducts().get(i))) { //check if supplier catalog contains ordered products
                for(int j=0;j<products.getProduct().size();++j) {
                    if (products.getProduct().get(j) == order.getProducts().get(i)) {
                        if(products.getAmount().get(j) < order.getAmount().get(i)) {
                            break; 
                            //if supplier does not have the amount of an ordered product
                            //Order not possible
                        }
                    }
                }
                return true;
            }else {
                break; 
                //if one of the products ordered not in supplier catalog
                //Order not possible
            }
        }
        return false;
    }   

    public int sendOrder (int invoiceID) {
        int managerID = 0; //manager of store that issued invoice
        for(int i=0;i<recievedOrders.size();++i){
            if(recievedOrders.get(i).getInvoiceID() == invoiceID) {
                managerID = recievedOrders.get(i).getCreatedBy();
                if(recievedOrders.get(i).isPaidFor()) { //if paid for then deliver
                    //delivery status true then manager gets notification or product delivery
                    recievedOrders.get(i).setDeliveryStatus(true);
                    sentOrders.add(recievedOrders.get(i));
                    recievedOrders.remove(i);
                }
            }
        }
        return managerID;
    }
}
