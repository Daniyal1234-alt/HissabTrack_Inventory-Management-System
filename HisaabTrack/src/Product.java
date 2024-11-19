import java.util.Date;

public class Product {
    // Attributes
    private int productID;
    private String name;
    private String description;
    private double price;
    private Date MFG; // Manufacturing date
    private Date EXP; // Expiry date

    // Constructor
    // Default constructor
    public Product() {}

    // Parameterized constructor
    public Product(int productID, String name, String description, double price, Date MFG, Date EXP) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.MFG = MFG;
        this.EXP = EXP;
    }
    // Method Signatures
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getMFG() {
        return MFG;
    }

    public void setMFG(Date MFG) {
        this.MFG = MFG;
    }

    public Date getEXP() {
        return EXP;
    }

    public void setEXP(Date EXP) {
        this.EXP = EXP;
    }
}
