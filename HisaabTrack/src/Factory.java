import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Factory {
    //system
    private HisaabTrack system;

    //actors
    private Admin admin;
    private Supplier supplier;
    private InventoryManager manager;


    private int Login(Scanner inputScanner) {
        System.out.println("HisaabTrack Login");
        System.out.println("\t1- Admin\n\t2- Supplier\n\t3- Inventory Manager\n");
        System.err.println("Select an option: ");
        int choice = inputScanner.nextInt();
        inputScanner.nextLine();

        if(choice == 1) {
            //prompt for signup if new admin
            //prompt for login details if existing admin
            System.out.println("\t1- Existing Admin\n\t2- New Admin\n");
            System.err.println("Select an option: ");
            int choice1 = inputScanner.nextInt();
            inputScanner.nextLine();

            if(choice1==2) {
                String Name = "", cnic = "", Address = "";
                //Gte details for UI form
                admin = system.addAdmin(Name, cnic, Address);
            }
        }

        //prompt for login details
        system.login();

        return choice;
    }

    public void start() {
        Scanner inputScanner = new Scanner(System.in);
        system = new HisaabTrack();
        //load data onto system from DB

        int choice = this.Login(inputScanner);

        boolean flag = true;
        int choice1;
        while (flag) {
            switch(choice){
                case 1:
                    int adminID = admin.getAdminID();
                    //load admin details from DB

                    System.out.println("HisaabTrack Admin");
                    System.out.println("\t1- addInventoryManager\n\t2- removeInventoryManager\n\t3- updateInventoryManager\n\t4- addSupplier\n\t5- removeSupplier\n\t6- updateSupplier\n\t7- generateReport\n\t8- updateProfile");
                    System.err.println("Select an option: ");
                    choice1 = inputScanner.nextInt();
                    inputScanner.nextLine(); 
                    
                    if (choice1 == 1) {
                        String Name = "", cnic = "", Address = "";
                        //Use UI to get info
                        List<Store> storeList = system.getStores();
                        Store s = null;
                        //Display and select store for manager
                        system.addManager(adminID, Name, cnic, Address, s); 
                    } else if (choice1 == 2) {
                        List<InventoryManager> managerList = system.getAdminsInventoryManagers(adminID);
                        int managerID = 0;
                        //Display and select manager to remove
                        system.removeManager(adminID, managerID);
                    } else if (choice1 == 3) {
                        List<InventoryManager> managerList = system.getAdminsInventoryManagers(adminID);
                        int managerID=0; String Name = "", cnic = "", Address = "";
                        system.updateManager(adminID, managerID, Name, cnic, Address); 
                    } else if (choice1 == 4) {
                        String company = "", location = ""; int regNo = 0;
                        //use UI to get info
                        system.addSupplier(adminID, company, location, regNo);
                    } else if (choice1 == 5) {
                        List<Supplier> supplierList = system.getSuppliers();
                        int supplierID = 0;
                        system.removeSupplier(adminID, supplierID);
                    } else if (choice1 == 6) {
                        List<Supplier> supplierList = system.getSuppliers();
                        int supplierID = 0;
                        String company = "", location = ""; int regNo = -1;
                        system.updateSupplier(adminID, supplierID, company, location, regNo);
                    } else if (choice1 == 7) {
                        // system.generateReport(); //requires implementation
                    } else if (choice1 == 8) {
                        // admin.updateProfile(); //requires implementation
                    } else {
                        System.out.println("Invalid option. Please select a valid option (1-9).");
                    }
                    
                    break;
                case 2:
                    int supplierID = supplier.getSupplierID();
                    //load admin details from DB

                    System.out.println("HisaabTrack Supplier");
                    System.out.println("\t1- Send Order\n\t2- Request Payment\n\t3- Add Product\n\t4- Remove Product\n\t5- Update Product Quantity\n\t6- Display Catalog");
                    System.err.println("Select an option: ");
                    choice1 = inputScanner.nextInt();
                    inputScanner.nextLine(); 

                    if(choice1==1) {
                        List<Invoice> Orders = system.viewRecievedOrders(supplierID);
                        //display orders list
                        //pick invoice by id
                        int invoiceID = 0; //picked invoice
                        system.sendProducts(supplierID, invoiceID);
                    } else if (choice1 == 2) {
                        List<Invoice> Orders = system.viewCompletedOrders(supplierID);
                        //display orders list
                        //pick invoice by id
                        int invoiceID = 0; //picked invoice
                        system.requestPayment(supplierID, invoiceID);
                    } else if (choice1 == 3) {
                        String name = "", description = ""; double price = 0; Date MFG = new Date(), EXP = new Date(); int amount = 0;
                        boolean newProduct = true;
                        while(newProduct) {
                            system.addItem(supplierID, name, description, price, MFG, EXP, amount);
                        }
                    } else if (choice1 == 4) {
                        ProductCatalog p = system.getProductCatalog(supplierID);
                        //display the catalog and get the product ID you want to remove
                        int productID = 0;
                        system.removeItem(supplierID, productID);
                    } else if (choice1 == 5) {
                        ProductCatalog p = system.getProductCatalog(supplierID);
                        //display and get ID of product to update
                        int productID = 0, amount = 0;
                        system.updateItem(supplierID, productID, amount);
                    } else if (choice1 == 6) {

                    } else {
                        //invalid choice
                    }

                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid Option. Try again!");
            }
        }
        inputScanner.close();
    }

    public static void clearScreen() {  
        try {  
            // Delay for 2 seconds (2000 milliseconds)  
            Thread.sleep(2000);  
            
            // Clear the console  
            System.out.print("\033[H\033[2J");  
            System.out.flush();  
        } catch (InterruptedException e) {  
            // Handle the exception if the sleep is interrupted  
            Thread.currentThread().interrupt(); // Restore interrupted status  
            System.err.println("Thread was interrupted: " + e.getMessage());  
        }  
    } 

}
