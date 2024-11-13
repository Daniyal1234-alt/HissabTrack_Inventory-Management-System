import java.util.List;
import java.util.Date;

public class Invoice {
    // Attributes
    private int invoiceID;
    private String createdBy;
    private Date createdOn;
    private List<Product> products;

    // Constructor
    public Invoice() {}

    // Method Signatures
    public Invoice generateInvoice(List<Product> P) {
        return null; // Placeholder for implementation
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
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
}
