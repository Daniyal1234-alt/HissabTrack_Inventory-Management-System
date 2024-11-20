import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Factory {
    // System
    private HisaabTrack system;

    // Actors
    private Admin admin;
    private Supplier supplier;
    private InventoryManager manager;

    private int login(Scanner inputScanner) {
        System.out.println("=== HisaabTrack Login ===");
        System.out.println("1. Admin");
        System.out.println("2. Supplier");
        System.out.println("3. Inventory Manager");
        System.out.print("Select an option: ");
        int choice = inputScanner.nextInt();
        inputScanner.nextLine(); // Clear the buffer

        switch (choice) {
            case 1:
                System.out.println("1. Existing Admin");
                System.out.println("2. New Admin");
                System.out.print("Select an option: ");
                int adminChoice = inputScanner.nextInt();
                inputScanner.nextLine(); // Clear the buffer

                if (adminChoice == 2) {
                    System.out.println("Enter Admin Details:");
                    System.out.print("Name: ");
                    String name = inputScanner.nextLine();
                    System.out.print("CNIC: ");
                    String cnic = inputScanner.nextLine();
                    System.out.print("Address: ");
                    String address = inputScanner.nextLine();

                    admin = system.addAdmin(name, cnic, address, false);
                }

                // Prompt for login details
                system.login();
                break;

            case 2:
                // Supplier login logic
                System.out.println("Enter Supplier ID: ");
                int supplierID = inputScanner.nextInt();
                supplier = system.getSupplierByID(supplierID);
                break;

            case 3:
                // Inventory Manager login logic
                System.out.println("Enter Manager ID: ");
                int managerID = inputScanner.nextInt();
                manager = system.getManagerByID(managerID);
                break;

            default:
                System.out.println("Invalid Option. Try Again!");
        }
        return choice;
    }

    private void adminMenu(Scanner inputScanner) {
        boolean adminFlag = true;
        int adminID = admin.getAdminID();
        while (adminFlag) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add Inventory Manager");
            System.out.println("2. Remove Inventory Manager");
            System.out.println("3. Update Inventory Manager");
            System.out.println("4. Add Supplier");
            System.out.println("5. Remove Supplier");
            System.out.println("6. Update Supplier");
            System.out.println("7. Generate Report");
            System.out.println("8. Update Profile");
            System.out.println("9. Logout");
            System.out.print("Select an option: ");
            int choice = inputScanner.nextInt();
            inputScanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter Manager Details:");
                    System.out.print("Name: ");
                    String name = inputScanner.nextLine();
                    System.out.print("CNIC: ");
                    String cnic = inputScanner.nextLine();
                    System.out.print("Address: ");
                    String address = inputScanner.nextLine();
                    List<Store> storeList = system.getStores();
                    Store store = null; // Assume store selection is done
                    system.addManager(admin.getAdminID(), name, cnic, address, store, false);
                    break;

                case 2:
                    List<InventoryManager> managerList = system.getAdminsInventoryManagers(adminID);
                    // Check if there are managers to display
                    if (managerList.isEmpty()) {
                        System.out.println("No Inventory Managers found under this admin.");
                    } else {
                        System.out.println("\n=== Inventory Managers List ===");
                        for (InventoryManager manager : managerList) {
                            System.out.println("Manager ID: " + manager.getManagerID());
                            System.out.println("Name: " + manager.getName());
                            System.out.println("CNIC: " + manager.getCNIC());
                            System.out.println("Address: " + manager.getAddress());
                            System.out.println("-------------------------");
                        }

                        // Prompt user to select a manager to remove
                        System.out.print("Enter the Manager ID to remove: ");
                        int managerID = inputScanner.nextInt();

                        // Attempt to remove the manager
                        boolean isRemoved = system.removeManager(adminID, managerID);
                        if (isRemoved) {
                            System.out.println("Manager with ID " + managerID + " has been successfully removed.");
                        } else {
                            System.out.println("Failed to remove manager. Please ensure the ID is correct.");
                        }
                    }
                    break;

                case 3:
                    // Update Inventory Manager details
                    managerList = system.getAdminsInventoryManagers(adminID);

                    // Check if there are managers to display
                    if (managerList.isEmpty()) {
                        System.out.println("No Inventory Managers found under this admin.");
                    } else {
                        System.out.println("\n=== Inventory Managers List ===");
                        for (InventoryManager manager : managerList) {
                            System.out.println("Manager ID: " + manager.getManagerID());
                            System.out.println("Name: " + manager.getName());
                            System.out.println("CNIC: " + manager.getCNIC());
                            System.out.println("Address: " + manager.getAddress());
                            System.out.println("-------------------------");
                        }

                        // Prompt user to select a manager to update
                        System.out.print("Enter the Manager ID to update: ");
                        int managerID = inputScanner.nextInt();
                        inputScanner.nextLine(); // Clear the buffer

                        // Fetch manager details to confirm selection
                        InventoryManager selectedManager = null;
                        for (InventoryManager manager : managerList) {
                            if (manager.getManagerID() == managerID) {
                                selectedManager = manager;
                                break;
                            }
                        }

                        if (selectedManager == null) {
                            System.out.println("Invalid Manager ID. Please try again.");
                            return;
                        }

                        // Prompt for updated fields
                        System.out.println("\nUpdating Manager Details (Leave blank to keep current value):");

                        System.out.print("Current Name: " + selectedManager.getName() + "\nEnter New Name: ");
                        String newName = inputScanner.nextLine();

                        System.out.print("Current CNIC: " + selectedManager.getCNIC() + "\nEnter New CNIC: ");
                        String newCnic = inputScanner.nextLine();

                        System.out.print("Current Address: " + selectedManager.getAddress() + "\nEnter New Address: ");
                        String newAddress = inputScanner.nextLine();

                        // Send updated data to the system (blank fields will be ignored by the system logic)
                        String nameToUpdate = newName.isBlank() ? "" : newName;
                        String cnicToUpdate = newCnic.isBlank() ? "" : newCnic;
                        String addressToUpdate = newAddress.isBlank() ? "" : newAddress;

                        boolean isUpdated = system.updateManager(adminID, managerID, nameToUpdate, cnicToUpdate, addressToUpdate);
                        if (isUpdated) {
                            System.out.println("Manager details updated successfully.");
                        } else {
                            System.out.println("Failed to update manager. Please check the details and try again.");
                        }
                    }

                    break;

                case 4:
                    System.out.println("Enter Supplier Details:");
                    System.out.print("Company: ");
                    String company = inputScanner.nextLine();
                    System.out.print("Location: ");
                    String location = inputScanner.nextLine();
                    System.out.print("Registration No: ");
                    int regNo = inputScanner.nextInt();
                    system.addSupplier(admin.getAdminID(), company, location, regNo, false);
                    break;

                case 5:
                    // Remove supplier logic
                    List<Supplier> supplierList = system.getSuppliers();

                    // Check if there are suppliers to display
                    if (supplierList.isEmpty()) {
                        System.out.println("No Suppliers found in the system.");
                    } else {
                        System.out.println("\n=== Supplier List ===");
                        for (Supplier supplier : supplierList) {
                            System.out.println("Supplier ID: " + supplier.getSupplierID());
                            System.out.println("Company: " + supplier.getCompany());
                            System.out.println("Location: " + supplier.getLocation());
                            System.out.println("-------------------------");
                        }

                        // Prompt user to select a supplier to remove
                        System.out.print("Enter the Supplier ID to remove: ");
                        int supplierID = inputScanner.nextInt();
                        inputScanner.nextLine(); // Clear the buffer

                        // Validate the supplierID
                        boolean supplierExists = false;
                        for (Supplier supplier : supplierList) {
                            if (supplier.getSupplierID() == supplierID) {
                                supplierExists = true;
                                break;
                            }
                        }

                        if (!supplierExists) {
                            System.out.println("Invalid Supplier ID. Please try again.");
                        } else {
                            // Attempt to remove the supplier
                            boolean isRemoved = system.removeSupplier(adminID, supplierID);
                            if (isRemoved) {
                                System.out.println("Supplier with ID " + supplierID + " has been successfully removed.");
                            } else {
                                System.out.println("Failed to remove supplier. Please check the details and try again.");
                            }
                        }
                    }

                    break;

                case 6:
                    // Update supplier logic
                    supplierList = system.getSuppliers();

                    // Check if there are suppliers to display
                    if (supplierList.isEmpty()) {
                        System.out.println("No Suppliers found in the system.");
                    } else {
                        System.out.println("\n=== Supplier List ===");
                        for (Supplier supplier : supplierList) {
                            System.out.println("Supplier ID: " + supplier.getSupplierID());
                            System.out.println("Company: " + supplier.getCompany());
                            System.out.println("Location: " + supplier.getLocation());
                            System.out.println("Registration No: " + supplier.getRegNo());
                            System.out.println("-------------------------");
                        }

                        // Prompt user to select a supplier to update
                        System.out.print("Enter the Supplier ID to update: ");
                        int supplierID = inputScanner.nextInt();
                        inputScanner.nextLine(); // Clear the buffer

                        // Validate the supplierID
                        Supplier selectedSupplier = null;
                        for (Supplier supplier : supplierList) {
                            if (supplier.getSupplierID() == supplierID) {
                                selectedSupplier = supplier;
                                break;
                            }
                        }

                        if (selectedSupplier == null) {
                            System.out.println("Invalid Supplier ID. Please try again.");
                            return;
                        }

                        // Prompt for updated details
                        System.out.println("\nUpdating Supplier Details (Leave blank to keep current value):");

                        System.out.print("Current Company: " + selectedSupplier.getCompany() + "\nEnter New Company: ");
                        String newCompany = inputScanner.nextLine();

                        System.out.print("Current Location: " + selectedSupplier.getLocation() + "\nEnter New Location: ");
                        String newLocation = inputScanner.nextLine();

                        System.out.print("Current Registration No: " + selectedSupplier.getRegNo() + "\nEnter New Registration No (-1 to keep current): ");
                        int newRegNo = inputScanner.nextInt();
                        inputScanner.nextLine(); // Clear the buffer

                        // Set new values or retain old ones for fields left blank
                        String companyToUpdate = newCompany.isBlank() ? "" : newCompany;
                        String locationToUpdate = newLocation.isBlank() ? "" : newLocation;
                        int regNoToUpdate = newRegNo == -1 ? -1 : newRegNo;

                        // Call the system's updateSupplier method
                        boolean isUpdated = system.updateSupplier(adminID, supplierID, companyToUpdate, locationToUpdate, regNoToUpdate);

                        if (isUpdated) {
                            System.out.println("Supplier details updated successfully.");
                        } else {
                            System.out.println("Failed to update supplier. Please check the details and try again.");
                        }
                    }

                    break;

                case 7:
                    System.out.println("Generating Report...");
                    // Generate report logic
                    break;

                case 8:
                    System.out.println("Updating Profile...");
                    // Update profile logic
                    break;

                case 9:
                    adminFlag = false;
                    break;

                default:
                    System.out.println("Invalid Option. Try Again!");
            }
        }
    }

    private void supplierMenu(Scanner inputScanner) {
        boolean supplierFlag = true;
        int supplierID = supplier.getSupplierID();
        while (supplierFlag) {
            System.out.println("\n=== Supplier Menu ===");
            System.out.println("1. Send Order");
            System.out.println("2. Request Payment");
            System.out.println("3. Add Product");
            System.out.println("4. Remove Product");
            System.out.println("5. Update Product Quantity");
            System.out.println("6. Display Catalog");
            System.out.println("7. Logout");
            System.out.print("Select an option: ");
            int choice = inputScanner.nextInt();
            inputScanner.nextLine();

            switch (choice) {
                case 1:
                    // Send order logic
                    List<Invoice> orders = system.viewRecievedOrders(supplierID);

                    // Check if there are any received orders
                    if (orders.isEmpty()) {
                        System.out.println("No received orders found.");
                    } else {
                        System.out.println("\n=== Received Orders ===");

                        // Display the list of orders
                        for (Invoice order : orders) {
                            System.out.println("Invoice ID: " + order.getInvoiceID());
                            System.out.println("Customer(Manager) ID: " + order.getCreatedBy());
                            System.out.println("Order Date: " + order.getCreatedOn());
                            System.out.println("Total Amount: " + order.getTotalAmount());
                            System.out.println("Product List: ");
                            int i = 0;
                            for(Product p : order.getProducts()) {
                                System.err.println("Name: " + p.getName() + "\tAmount: " + order.getAmount().get(i++));
                            }
                            System.out.println("-------------------------");
                        }

                        // Prompt the user to select an order by ID
                        System.out.print("Enter the Invoice ID to send products: ");
                        int invoiceID = inputScanner.nextInt();
                        inputScanner.nextLine(); // Clear the buffer

                        // Validate the entered invoice ID
                        boolean orderExists = false;
                        for (Invoice order : orders) {
                            if (order.getInvoiceID() == invoiceID) {
                                orderExists = true;
                                break;
                            }
                        }

                        if (!orderExists) {
                            System.out.println("Invalid Invoice ID. Please try again.");
                        } else {
                            // Attempt to send the products
                            system.sendProducts(supplierID, invoiceID);
                            System.out.println("Products for Invoice ID " + invoiceID + " have been successfully sent.");
                        }
                    }

                    break;

                case 2:
                    // Request payment logic
                    orders = system.viewCompletedOrders(supplierID);

                    // Check if there are any completed orders
                    if (orders.isEmpty()) {
                        System.out.println("No completed orders found.");
                    } else {
                        System.out.println("\n=== Completed Orders ===");

                        // Display the list of completed orders
                        for (Invoice order : orders) {
                            System.out.println("Invoice ID: " + order.getInvoiceID());
                            System.out.println("Customer(Manager) ID: " + order.getCreatedBy());
                            System.out.println("Order Date: " + order.getCreatedOn());
                            System.out.println("Total Amount: " + order.getTotalAmount());
                            System.out.println("Product List: ");
                            int i = 0;
                            for(Product p : order.getProducts()) {
                                System.err.println("Name: " + p.getName() + "\tAmount: " + order.getAmount().get(i++));
                            }
                            System.out.println("-------------------------");
                        }
                        // Prompt the user to select an invoice by ID
                        System.out.print("Enter the Invoice ID to request payment: ");
                        int invoiceID = inputScanner.nextInt();
                        inputScanner.nextLine(); // Clear the buffer

                        // Validate the entered invoice ID
                        boolean orderExists = false;
                        for (Invoice order : orders) {
                            if (order.getInvoiceID() == invoiceID) {
                                orderExists = true;
                                break;
                            }
                        }

                        if (!orderExists) {
                            System.out.println("Invalid Invoice ID. Please try again.");
                        } else {
                            // Attempt to request payment
                            system.requestPayment(supplierID, invoiceID);
                            System.out.println("Payment for Invoice ID " + invoiceID + " has been successfully requested.");
                    }

                    break;
                }

                case 3:
                    System.out.println("Enter Product Details:");
                    System.out.print("Name: ");
                    String name = inputScanner.nextLine();
                    System.out.print("Description: ");
                    String description = inputScanner.nextLine();
                    System.out.print("Price: ");
                    double price = inputScanner.nextDouble();
                    System.out.print("Quantity: ");
                    int quantity = inputScanner.nextInt();
                    system.addItem(supplier.getSupplierID(), name, description, price, new Date(), new Date(), quantity,false);
                    break;

                case 4:
                    // Remove product logic
                    ProductCatalog p = system.getProductCatalog(supplierID);
                    //display the catalog and get the product ID you want to remove
                    int productID = 0;
                    system.removeItem(supplierID, productID);
                    break;

                case 5:
                    // Update product quantity logic
                    p = system.getProductCatalog(supplierID);
                    //display and get ID of product to update
                    productID = 0; int amount = 0;
                    system.updateItem(supplierID, productID, amount);
                    break;

                case 6:
                    // Display catalog logic
                    p = system.getProductCatalog(supplierID);
                    if(p.getProduct().isEmpty()) {
                        System.out.println("Empty Product Catalog.");
                    } else {
                        System.out.println("\n=== Completed Orders ==="); 
                        int i = 0;
                        for(Product product : p.getProduct()) {
                            System.err.println("Name: " + product.getName()  + "\nDescription: " + product.getDescription() + "\nAmount: " + p.getAmount().get(i++));
                        }
                    }
                    break;

                case 7:
                    supplierFlag = false;
                    break;

                default:
                    System.out.println("Invalid Option. Try Again!");
            }
        }
    }

    public void start() {
        Scanner inputScanner = new Scanner(System.in);
        system = new HisaabTrack(); // Initialize the system
        int userRole = this.login(inputScanner);

        switch (userRole) {
            case 1:
                adminMenu(inputScanner);
                break;
            case 2:
                supplierMenu(inputScanner);
                break;
            case 3:
                // Inventory Manager menu logic
                break;
            default:
                System.out.println("Invalid Role!");
        }

        inputScanner.close();
    }
}
