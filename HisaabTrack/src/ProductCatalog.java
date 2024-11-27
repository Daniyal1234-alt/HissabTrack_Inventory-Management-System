import java.util.ArrayList;
import java.util.List;

public class ProductCatalog {
    // Attributes
    private List<Product> product;
    private List<Integer> amount;

    // Constructor
    public ProductCatalog() {
        this.product = new ArrayList<>(); // Initialize product list
        this.amount = new ArrayList<>();  // Initialize amount list
    }

    // Method to add a product to the catalog
    public void addProduct(Product p, int amount) {
        // Check if the product already exists
        if(p.getProductID() != -1) {
            for (int i = 0; i < product.size(); i++) {
                if (product.get(i).getProductID()==p.getProductID()) {
                    // Product exists, update the amount
                    this.amount.set(i, this.amount.get(i) + amount);
                    return; // Exit after updating
                }
            }
        }

        // Product does not exist, add it to the list
        int ID = 1;
        if(p.getProductID() == -1){  //not DB call
            if(!product.isEmpty())
                ID = product.getLast().getProductID() + 1;
            p.setProductID(ID);
        }
        this.product.add(p);
        this.amount.add(amount);
    }

    public boolean removeProduct(int productID) {
        return false; // Placeholder for implementation
    }

    Product getProductByID(int pID) {
        for(Product p : product) {
            if(p.getProductID() == pID)
                return p;
        }
        return null;
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
