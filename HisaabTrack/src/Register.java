import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Register {
    // Attributes
    private int registerID;
    private List<Report> reports;
    private List<Invoice> invoices;

    // Constructor
    public Register(int ID) {
        registerID = ID;
        reports = new ArrayList<>();
        invoices = new ArrayList<>();
    }

    // Method Signatures
    public Report generateReport(Store s) {
        return null; // Placeholder for implementation
    }

    public Invoice generateInvoice(List<Product> p, List<Integer> q) {
        Invoice obj = new Invoice();
        int ID = 1;
        if(!invoices.isEmpty()) {
            ID = invoices.getLast().getInvoiceID() + 1;
        }
        obj.setInvoiceID(ID);
        obj.setProducts(p);
        obj.setQuantity(q);
        obj.setCreatedOn(new Date());
        invoices.add(obj);
        return obj; // Placeholder for implementation
    }

    public void removeInvoice(Invoice e) {
        invoices.remove(e);
    }

    // Getters and Setters
    public int getRegisterID() {
        return registerID;
    }

    public void setRegisterID(int registerID) {
        this.registerID = registerID;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
