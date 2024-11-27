import java.util.Date;

public class Product {
    // Attributes
	private static int productcounter;
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
    public Product(int productID, String name, String description, double price, Date MFG, Date EXP, Boolean DBCall) {
    	if(DBCall == true)	
    		productcounter = productID;
    		
    	else {
    		productcounter  = productcounter + 1;
    	}
    	this.productID = productcounter;
        this.name = name;
        this.description = description;
        this.price = price;
        this.MFG = MFG;
        this.EXP = EXP;
    }
 // Copy constructor
    public Product(Product other) {
        this.productID = other.productID;
        this.name = other.name;
        this.description = other.description;
        this.price = other.price;
        this.MFG = new Date(other.MFG.getTime()); // Defensive copy
        this.EXP = new Date(other.EXP.getTime()); // Defensive copy
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
