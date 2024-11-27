import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Store {
    // Attributes
    private int storeID;
    private String location;
    private NotificationManager notifications;
    private List<Stock> stock;
    private Register register;
    private int managerID;

    // Constructor
    public Store(int ID, String loc) {
        storeID = ID;
        location = loc;
        notifications = new NotificationManager();
        stock = new ArrayList<>();
        register = new Register(storeID);
        managerID = -1;
    }

    // Method Signatures
    public void printStock() {
        // Placeholder for implementation
    }

    public boolean updateStock(Invoice e) {
        boolean flag = false;
        int i = 0;
        for(Product p: e.getProducts()) {
            flag = false;
            for(Stock s1:stock) {
                if(s1.getProduct().getName().equals(p.getName())) {      	
                    s1.setQuantity(s1.getQuantity() + e.getAmount().get(i));
                    s1.setArrivalDate(new Date());
                    flag = true;
                    break;
                } 
            }
            if(!flag) {
                Stock s = new Stock();
                s.setProduct(p);
                s.setQuantity(e.getAmount().get(i));
                s.setTotalCost(p.getPrice() * s.getQuantity());
                stock.add(s);
            }
            i++;

        }
        return true; // Placeholder for implementation
    }

    public void makeSale(List<Product> p, List<Integer> q) {
        int i = 0;
        for(Product product:p) {
            for(Stock s:stock) {
                if(s.getProduct().getName().equals(product.getName())) {
                    s.setQuantity(s.getQuantity() - q.get(i));
                }
            }
            i++;
        }
    }

    public boolean addStock(Stock s) {
    	this.stock.add(s);
        return false; // Placeholder for implementation
    }

    public boolean removeStock(int ID) {
        for(Stock s: stock) {
            if(s.getStockID() == ID) {
                stock.remove(s);
            }
        }
        return false; // Placeholder for implementation
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }
    
    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeIIDD) {
        this.storeID = storeID;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int ID) {
        this.managerID = ID;
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
