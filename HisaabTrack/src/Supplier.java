import java.util.List;

public class Supplier {
    // Attributes
    private int supplierID;
    private String company;
    private String location;
    private int regNo;
    private ProductCatalog products;
    private List<Invoice> order;

    // Constructor
    public Supplier() {}

    // Method Signatures
    public int getSupplierID() {
        return supplierID;
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

    public List<Invoice> getOrder() {
        return order;
    }

    public void setOrder(List<Invoice> order) {
        this.order = order;
    }

    public List<Order> sendOrder() {
        return null; // Placeholder for implementation
    }
}
