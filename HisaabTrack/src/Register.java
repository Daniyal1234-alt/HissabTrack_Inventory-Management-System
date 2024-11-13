import java.util.List;

public class Register {
    // Attributes
    private int registerID;
    private List<Report> reports;
    private List<Invoice> invoices;

    // Constructor
    public Register() {}

    // Method Signatures
    public Report generateReport(Store s) {
        return null; // Placeholder for implementation
    }

    public Invoice generateInvoice(List<Product> p) {
        return null; // Placeholder for implementation
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
