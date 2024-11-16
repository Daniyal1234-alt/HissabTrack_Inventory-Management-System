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
                admin = system.addAdmin(inputScanner);
            }
        }

        //prompt for login details
        system.login(inputScanner);

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
                        system.addManager(adminID, inputScanner); 
                    } else if (choice1 == 2) {
                        system.removeManager(adminID, inputScanner);
                    } else if (choice1 == 3) {
                        system.updateManager(adminID, inputScanner); 
                    } else if (choice1 == 4) {
                        system.addSupplier(adminID, inputScanner);
                    } else if (choice1 == 5) {
                        system.removeSupplier(adminID, inputScanner);
                    } else if (choice1 == 6) {
                        system.updateSupplier(adminID, inputScanner);
                    } else if (choice1 == 7) {
                        system.generateReport(); //requires implementation
                    } else if (choice1 == 8) {
                        admin.updateProfile(); //requires implementation
                    } else {
                        System.out.println("Invalid option. Please select a valid option (1-9).");
                    }
                    
                    break;
                case 2:
                    int supplierID = supplier.getSupplierID();
                    //load admin details from DB

                    System.out.println("HisaabTrack Supplier");
                    System.out.println("\t1- View Pending Orders\n\t2- Send Order\n\t3- Request Payment");
                    System.err.println("Select an option: ");
                    choice1 = inputScanner.nextInt();
                    inputScanner.nextLine(); 
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
