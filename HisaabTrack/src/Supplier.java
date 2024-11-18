import com.sun.net.httpserver.Request;
import java.util.List;
import java.util.ArrayList;

public class Supplier {
    // Attributes
    private int supplierID;
    private String company;
    private String location;
    private int regNo;
    private ProductCatalog products;
    private List<Invoice> recievedOrders;
    private List<Invoice> sentOrders;

    // Constructor
    public Supplier(int supplierID, String company, String location, int regNo) {
        this.supplierID = supplierID;
        this.company = company;
        this.location = location;
        this.regNo = regNo;
        products = new ProductCatalog();
        recievedOrders = new ArrayList<>();
        sentOrders = new ArrayList<>();
    }
    // Method Signatures
    public int getSupplierID() {
        return supplierID;
    }

    public List<Invoice> getRecievedOrders() {
        return recievedOrders;
    }

    public List<Invoice> getCompletedOrders() {
        return sentOrders;
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

    public void addIncomingOrder (Invoice order) {
        recievedOrders.add(order);
    }

    public boolean checkStock(Invoice order) {
        //check whether supplier has the facilities to complete an order
        for(int i = 0; i < order.getProducts().size(); ++i) {
            if(products.getProduct().contains(order.getProducts().get(i))) { //check if supplier catalog contains ordered products
                for(int j=0;j<products.getProduct().size();++j) {
                    if (products.getProduct().get(j) == order.getProducts().get(i)) {
                        if(products.getAmount().get(j) < order.getAmount().get(i)) {
                            break; 
                            //if supplier does not have the amount of an ordered product
                            //Order not possible
                        }
                    }
                }
                return true;
            }else {
                break; 
                //if one of the products ordered not in supplier catalog
                //Order not possible
            }
        }
        return false;
    }   

    public int sendOrder (int invoiceID) {
        //check product catalog to see if order can be completed. call checkStock(chosenOrder)
        //if yes, change delivery status and move from recieved list to sent list
        //if no, remove order from system let system know
        int managerID = 0; //manager of store that issued invoice
        for(int i=0;i<recievedOrders.size();++i){
            if(recievedOrders.get(i).getInvoiceID() == invoiceID) {
                managerID = recievedOrders.get(i).getCreatedBy();
                if(checkStock(recievedOrders.get(i))) {
                    //delivery status true then manager gets notification or product delivery
                    recievedOrders.get(i).setDeliveryStatus(true);
                    sentOrders.add(recievedOrders.get(i));
                    recievedOrders.remove(i);
                }
                //else manager gets notification indicating order not possible
            }
        }
        return managerID;
    }

    public int requestPayment(int invoiceID) {
        //sent a request to system for payment from respective managers
        int managerID = 0; //manager of store that issued invoice
           for(int i=0;i<sentOrders.size();++i){
            if(sentOrders.get(i).getInvoiceID() == invoiceID && !sentOrders.get(i).isPaidFor()) {
                managerID = sentOrders.get(i).getCreatedBy();
            }
        }
        return managerID;
    }
}
