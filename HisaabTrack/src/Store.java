import java.util.List;

public class Store {
    // Attributes
    private int storeID;
    private String location;
    private NotificationManager notifications;
    private List<Stock> stock;

    // Constructor
    public Store() {}

    // Method Signatures
    public void printStock() {
        // Placeholder for implementation
    }

    public boolean updateStock() {
        return false; // Placeholder for implementation
    }

    public boolean addStock(Stock s) {
        return false; // Placeholder for implementation
    }

    public boolean removeStock() {
        return false; // Placeholder for implementation
    }

    public void updateProfile() {
        // Placeholder for implementation
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public NotificationManager getNotifications() {
        return notifications;
    }

    public void setNotifications(NotificationManager notifications) {
        this.notifications = notifications;
    }

    public List<Stock> getStock() {
        return stock;
    }

    public void setStock(List<Stock> stock) {
        this.stock = stock;
    }
}
