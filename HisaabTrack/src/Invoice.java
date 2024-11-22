import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Invoice {
    // Attributes
    private int invoiceID;
    private int createdBy; //inventory manager ID
    private int supplierID;
    private Date createdOn;
    private List<Product> products;
    private List<Integer> amount;
    private boolean deliveryStatus;
    private boolean paymentStatus;
    private String creatorType;
    public String getCreatorType() {
		return creatorType;
	}

	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
	}

	// Constructor
    public Invoice() {
        products = new ArrayList<>();
        amount = new ArrayList<>();
        deliveryStatus = false;
        paymentStatus = false;
        createdOn = new Date();
        creatorType = "\0";
    }
    //Parameterized Constructor
    public Invoice(int invoiceID, int createdBy, Date createdOn, boolean deliveryStatus, boolean paymentStatus, String creatorType) {
        this.invoiceID = invoiceID;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.deliveryStatus = deliveryStatus;
        this.paymentStatus = paymentStatus;
        this.creatorType = creatorType;
    }
    // Method Signatures
    public Invoice generateInvoice(List<Product> P) {
        return null; // Placeholder for implementation
    }
    // Adding a product to the invoice
    public void addProduct(Product p, int amount) {
    	this.products.add(p);
    	this.amount.add(amount);
    }
    public List<Integer> getAmount() {
        return amount;
    }
    
    double getTotalAmount() {
        double total = 0;
        int i = 0;
         for(Product p : products) {
            total += (p.getPrice() * amount.get(i++));
        }
        return total;
    }
    
    void addItem(Product p, int quantity) {
        products.add(p);
        amount.add(quantity);
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }
    
    public int getSupplierID() {
        return supplierID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }


    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Integer> getQuantity() {
        return amount;
    }

    public void setQuantity(List<Integer> quantity) {
        this.amount = quantity;
    }

    public boolean isDelivered() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(boolean deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public boolean isPaidFor() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
