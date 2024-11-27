import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
public class Invoice {
    // Attributes
    private int invoiceID;
    private int createdBy; //inventory manager ID
    private int supplierID;
    private Date createdOn;
    private List<Product> products;
    private List<Integer> amount;
    private Boolean deliveryStatus;
    private Boolean paymentStatus;
    private String creatorType;
    public String getCreatorType() {
		return creatorType;
	}

	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
	}

	// Constructor
    public Invoice() {
        products = new ArrayList<Product>();
        amount = new ArrayList<Integer>();
        deliveryStatus = false;
        paymentStatus = false;
        createdOn = new Date();
        creatorType = "\0";
    }
    //Parameterized Constructor
    public Invoice(int invoiceID, int createdBy, Date createdOn, boolean deliveryStatus, boolean paymentStatus, String creatorType) {
    	products = new ArrayList<Product>();
        amount = new ArrayList<Integer>();
         deliveryStatus = false;
         paymentStatus = false;
         createdOn = new Date();
         creatorType = "\0";
    	this.invoiceID = invoiceID;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.deliveryStatus = deliveryStatus;
        this.paymentStatus = paymentStatus;
        this.creatorType = "Manager";
    }
    

    // Method Signatures
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice ID: ").append(invoiceID).append("\n")
          .append("Created By (Manager ID): ").append(createdBy).append("\n")
          .append("Supplier ID: ").append(supplierID).append("\n")
          .append("Created On: ").append(createdOn).append("\n")
          .append("Creator Type: ").append(creatorType).append("\n")
          .append("Delivery Status: ").append(deliveryStatus ? "Delivered" : "Pending").append("\n")
          .append("Payment Status: ").append(paymentStatus ? "Paid" : "Unpaid").append("\n")
          .append("Products Ordered:\n");
    
        // Loop through products and amounts
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            sb.append("   - Product ID: ").append(product.getProductID()).append("\n")
              .append("     Name: ").append(product.getName()).append("\n")
              .append("     Description: ").append(product.getDescription()).append("\n")
              .append("     Price: $").append(product.getPrice()).append("\n")
              .append("     Amount: ").append(amount.get(i)).append("\n");
        }
    
        sb.append("-----------------------------------------");
        return sb.toString();
    }    

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
