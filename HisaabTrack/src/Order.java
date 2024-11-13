import java.util.List;

public class Order {
    // Attributes
    private String receiver;
    private String sender;
    private List<Stock> stock;

    // Constructor
    public Order() {}

    // Getters and Setters
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<Stock> getStock() {
        return stock;
    }

    public void setStock(List<Stock> stock) {
        this.stock = stock;
    }
}
