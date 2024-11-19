import java.util.List;

public class ProductCatalog {
    // Attributes
    private List<Product> product;
    private List<Integer> amount;

    // Constructor
    public ProductCatalog() {}

    // Method Signatures

    public void addProduct(Product p) {
        product.add(p);
    }

    public boolean removeProduct(int productID) {
        return false; // Placeholder for implementation
    }

    public boolean updateQuantity(int productID, int addedStock) {
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