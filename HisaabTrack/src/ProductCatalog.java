import java.util.List;

public class ProductCatalog {
    // Attributes
    private List<Product> product;
    private List<Integer> amount;

    // Constructor
    public ProductCatalog() {}

    // Method Signatures
    public List<Product> getProducts() {
        return product;
    }

    public void addProduct(Product p) {
        // Placeholder for implementation
    }

    public boolean removeProduct() {
        return false; // Placeholder for implementation
    }

    public boolean updateQuantity(Product p) {
        return false; // Placeholder for implementation
    }

    // Getters and Setters
    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public List<Integer> getAmount() {
        return amount;
    }

    public void setAmount(List<Integer> amount) {
        this.amount = amount;
    }
}