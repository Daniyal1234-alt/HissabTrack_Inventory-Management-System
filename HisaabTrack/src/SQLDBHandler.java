import java.io.IOException;
import java.sql.*;
import java.util.regex.PatternSyntaxException;

import com.mysql.cj.QueryReturnType;
// System PARHH DEY GAYEEEEEEEEE
import com.mysql.cj.callback.UsernameCallback;
public class SQLDBHandler {
	private String connection;
	//private String className;
	private String userName;
	private String password;
	private static SQLDBHandler instanceHandler = null;
	
	private SQLDBHandler() {
        this.connection = "jdbc:mysql://localhost:3306/HissabTrackDB";
        this.userName = "root";
        this.password = "dani";
    }

    // Public method to get the singleton instance
    public static synchronized SQLDBHandler getInstance() {
        if (instanceHandler == null) {
            instanceHandler = new SQLDBHandler();
        }
        return instanceHandler;
    }
	// login
	public String Login(String username, String password) {
	    String queryAdmin = "SELECT * FROM Admin WHERE name = ? AND password = ?";
	    String queryManager = "SELECT * FROM InventoryManager WHERE name = ? AND password = ?";
	    String querySupplier = "SELECT * FROM Supplier WHERE companyName = ? AND password  = ?";
	    
	    try (Connection conn = DriverManager.getConnection(connection, username, password)) {
	        // Check Admin
	        try (PreparedStatement stmt = conn.prepareStatement(queryAdmin)) {
	            stmt.setString(1, username);
	            stmt.setString(2, password);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return "Admin";
	                }
	            }
	        }

	        // Check Inventory Manager
	        try (PreparedStatement stmt = conn.prepareStatement(queryManager)) {
	            stmt.setString(1, username);
	            stmt.setString(2, password);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return "Inventory Manager";
	                }
	            }
	        }

	        // Check Supplier
	        try (PreparedStatement stmt = conn.prepareStatement(querySupplier)) {
	            stmt.setString(1, username);
	            stmt.setString(2, password);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return "Supplier";
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null; // No match found
	}

	
	// Adding an Admin to the SQL DB
	public boolean addAdmin(Admin admin) {
	    try (Connection conn =  DriverManager.getConnection(connection, userName, password)) {
	        String sql = "INSERT INTO Admin (name, CNIC, address, active, password) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, admin.getName());
	        pstmt.setString(2, admin.getCNIC());
	        pstmt.setString(3, admin.getAddress());
	        pstmt.setBoolean(4, admin.isActive());
	        pstmt.setString(5, admin.getPassword());
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean loadFromDB(HisaabTrack system) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        // Adding Admins
	        String adminSql = "SELECT * FROM Admin";
	        try (PreparedStatement adminStmt = conn.prepareStatement(adminSql)) {
	            ResultSet adminRs = adminStmt.executeQuery();
	            while (adminRs.next()) {
	                system.addAdmin(
	                    adminRs.getString("name"),
	                    adminRs.getString("CNIC"),
	                    adminRs.getString("address"),
	                    adminRs.getString("password"),
	                    adminRs.getBoolean("active")
	                );
	            }
	        }
	     // Adding Inventory Managers
	        String managerSql = """
	            SELECT a.managerID, a.name, a.CNIC, a.address, a.password, ms.storeID
	            FROM InventoryManager a JOIN admininventorymanager ai ON a.managerID = ai.inventoryManagerID
	            LEFT JOIN ManagerStore ms ON a.managerID = ms.managerID
	        """;
	        try (PreparedStatement managerStmt = conn.prepareStatement(managerSql)) {
	            ResultSet managerRs = managerStmt.executeQuery();
	            while (managerRs.next()) {
	                InventoryManager manager = new InventoryManager(
	                    managerRs.getInt("managerID"),
	                    managerRs.getString("name"),
	                    managerRs.getString("CNIC"),
	                    managerRs.getString("address"),
	                    managerRs.getString("password")
	                   
	                );
	                system.addManager(managerRs.getInt("adminID"), managerRs.getString("name"), managerRs.getString("CNIC"), managerRs.getString("address"),
	                    managerRs.getString("password"), system.getStore(managerRs.getInt("storeID")), true );
	            }
	        }
	        String storeSqlString = "Select * FROM Store";
	        try (PreparedStatement storeStmt = conn.prepareStatement(storeSqlString)) {
	            ResultSet rs = storeStmt.executeQuery();
	            while (rs.next()) {
	                // Extract store data
	                int storeID = rs.getInt("storeID");
	                String location = rs.getString("location");

	                // Check if store already exists in the system
	                Store store = system.getStore(storeID);
	                if (store == null) {
	                    store = new Store(storeID, location);
	                    system.addStore(store);
	                }
	            }
	        }
	        // Adding Stores and Store Stocks
	        String storeQuery = """
	            SELECT st.storeID, st.location, st.managerID,
	                   s.stockID, s.productID, s.quantity, s.totalCost, s.arrivalDate,
	                   p.name AS productName, p.description AS productDescription,
	                   p.price AS productPrice, p.MFG AS productMFG, p.EXP AS productEXP
	            FROM Store st
	            LEFT JOIN StoreStock ss ON st.storeID = ss.storeID
	            JOIN Stock s ON s.stockID = ss.stockID
	            JOIN Product p ON s.productID = s.productID
	        """;
	        try (PreparedStatement storeStmt = conn.prepareStatement(storeQuery)) {
	            ResultSet rs = storeStmt.executeQuery();
	            while (rs.next()) {
	                // Extract store data
	                int storeID = rs.getInt("storeID");
	                String location = rs.getString("location");

	                // Check if store already exists in the system
	                Store store = system.getStore(storeID);
	                if (store == null) {
	                    store = new Store(storeID, location);
	                    system.addStore(store);
	                }

	                // Add manager to store if applicable
	                int managerID = rs.getInt("managerID");
	                if (!rs.wasNull()) {
	                    InventoryManager manager = system.getManagerByID(managerID);
	                    if (manager != null) {
	                        store.setManagerID(managerID);;
	                    }
	                }

	                // Add stock to the store if applicable
	                int stockID = rs.getInt("stockID");
	                if (!rs.wasNull()) {
	                    Product product = new Product(
	                        rs.getInt("productID"),
	                        rs.getString("productName"),
	                        rs.getString("productDescription"),
	                        rs.getDouble("productPrice"),
	                        rs.getDate("productMFG"),
	                        rs.getDate("productEXP")
	                    );

	                    Stock stock = new Stock(
	                        stockID,
	                        product,
	                        rs.getInt("quantity"),
	                        rs.getDouble("totalCost"),
	                        rs.getDate("arrivalDate")
	                    );
	                    store.addStock(stock);
	                }
	            }
	        }

	        // Adding Suppliers and Products
	        String supplierSql = """
	            SELECT s.supplierID, s.companyName, s.location, s.registrationNum, s.password,
	                   cp.productID, cp.quantity,
	                   p.name AS productName, p.description, p.price, p.MFG, p.EXP
	            FROM Supplier s
	            LEFT JOIN ProductCatalog pc ON s.supplierID = pc.supplierID
	            LEFT JOIN ProductCatalogProducts cp ON pc.productCatalogID = cp.catalogID
	            LEFT JOIN Product p ON cp.productID = p.productID
	        """;
	        try (PreparedStatement supplierStmt = conn.prepareStatement(supplierSql)) {
	            ResultSet supplierRs = supplierStmt.executeQuery();
	            while (supplierRs.next()) {
	                Supplier supplier = system.getSupplier(supplierRs.getInt("supplierID"));
	                if (supplier == null) {
	                    system.addSupplier(
	                    	1,
	                        supplierRs.getString("companyName"),
	                        supplierRs.getString("location"),
	                        supplierRs.getInt("registrationNum"),
	                        supplierRs.getString("password"),
	                        true
	                    );
	                }

	                // Add product to supplier if applicable
	                int productID = supplierRs.getInt("productID");
	                if (!supplierRs.wasNull()) {
	                    Product product = new Product(
	                        productID,
	                        supplierRs.getString("productName"),
	                        supplierRs.getString("description"),
	                        supplierRs.getDouble("price"),
	                        supplierRs.getDate("MFG"),
	                        supplierRs.getDate("EXP")
	                    );
	                    supplier.addProduct(product, supplierRs.getInt("quantity"));
	                }
	            }
	        }

	        
	        // Adding Unpaid Invoices
	        String unpaidInvoicesSql = """
	            SELECT i.invoiceID, i.createdByID, i.createdOn, i.userType, i.paid, i.delivered
	            FROM Invoice i
	            JOIN AdminUnpaidInvoices ai ON i.invoiceID = ai.invoiceID
	            WHERE i.paid = FALSE
	        """;
	        try (PreparedStatement unpaidStmt = conn.prepareStatement(unpaidInvoicesSql)) {
	            ResultSet rs = unpaidStmt.executeQuery();
	            while (rs.next()) {
	                Invoice invoice = new Invoice(
	                    rs.getInt("invoiceID"),
	                    rs.getInt("createdByID"),
	                    rs.getDate("createdOn"),
	                    rs.getBoolean("delivered"),
	                    rs.getBoolean("paid"),
	                    rs.getString("userType")
	                );
	                system.addUnpaidInvoice(rs.getInt("createdByID"), invoice);
	            }
	        }

	        return true;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Removing an Admin based on it's CNIC
	public boolean removeAdmin(String CNIC) {
	    try (Connection conn = DriverManager.getConnection(connection,userName, password)) {
	        String sql = "DELETE FROM Admin WHERE CNIC = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, CNIC);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Adding an Inventory Manager
	public boolean addInventoryManager(InventoryManager manager) {
	    try (Connection conn = DriverManager.getConnection(connection,userName, password)) {
	        String sql = "INSERT INTO InventoryManager (name, CNIC, address, storeID, password) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, manager.getName());
	        pstmt.setString(2, manager.getCNIC());
	        pstmt.setString(3, manager.getAddress());
	        if (manager.getManagingStore() != null) {
	            pstmt.setInt(4, manager.getManagingStore().getStoreID());
	        } else {
	            pstmt.setInt(4, -1);
	        }
	        pstmt.setString(5, manager.getPassword());
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Removing an Inventory Manager based on it's CNIC
	public boolean removeInventoryManager(String CNIC) {
	    try (Connection conn = DriverManager.getConnection(connection,userName, password)) {
	        String sql = "DELETE FROM InventoryManager WHERE CNIC = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, CNIC);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Adding a supplier to the DB
	public boolean addSupplier(Supplier supplier) {
	    try (Connection conn = DriverManager.getConnection(connection,userName, password)) {
	        String sql = "INSERT INTO Supplier (companyName, location, registrationNum, password) VALUES (?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, supplier.getCompany());
	        pstmt.setString(2, supplier.getLocation());
	        pstmt.setInt(3, supplier.getRegNo());
	        pstmt.setString(4, password);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Removing a supplier based on it's registration number
	public boolean removeSupplier(int supplierID) {
	    try (Connection conn = DriverManager.getConnection(connection,userName, password)) {
	        String sql = "DELETE FROM Supplier WHERE supplierID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, supplierID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Adding a generic invoice to invoice table 
	public boolean addInvoice(Invoice invoice) {
	    try (Connection conn = DriverManager.getConnection(connection,userName, password)) {
	        String sql = "INSERT INTO Invoice (createdByID, createdOn, userType, paid, delivered) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, invoice.getCreatedBy());
	        pstmt.setDate(2, (Date)invoice.getCreatedOn());
	        pstmt.setString(3, invoice.getCreatorType());
	        pstmt.setBoolean(4, invoice.isPaidFor());
	        pstmt.setBoolean(5, invoice.isDelivered());
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Removing an invoice based on invoiceID
	public boolean removeInvoice(int invoiceID) {
	    try (Connection conn = DriverManager.getConnection(connection,userName, password)) {
	        String sql = "DELETE FROM Invoice WHERE invoiceID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, invoiceID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Add to Stock of a specific store
	public boolean addProductStock(Stock stock, int StoreID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "INSERT INTO Stock (productID, storeID, quantity, totalCost, arrivalDate) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, stock.getProduct().getProductID());
	        pstmt.setInt(2, StoreID);
	        pstmt.setInt(3, stock.getQuantity());
	        pstmt.setDouble(4, stock.getTotalCost());
	        pstmt.setDate(5, (Date)stock.getArrivalDate());
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Removing a stock from a specific store
	public boolean removeProductStock(int stockID, int StoreID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "DELETE FROM Stock WHERE stockID = ? AND storeID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, stockID);
	        pstmt.setInt(2, StoreID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Adding into StoreStocks Relationship table
	public boolean addStoreStock(int storeID, int stockID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "INSERT INTO StoreStock (storeID, stockID) VALUES (?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, storeID);
	        pstmt.setInt(2, stockID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Removing from StoreStock Relationship table
	public boolean removeStoreStock(int storeID, int stockID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "DELETE FROM StoreStock WHERE storeID = ? AND stockID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, storeID);
	        pstmt.setInt(2, stockID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Adding to SupplierCatalogID
	public boolean addSupplierCatalog(int supplierID, int catalogID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "INSERT INTO SupplierCatalog (supplierID, catalogID) VALUES (?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, supplierID);
	        pstmt.setInt(2, catalogID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Removing Supplier CatalogID 
	public boolean removeSupplierCatalog(int supplierID, int catalogID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "DELETE FROM SupplierCatalog WHERE supplierID = ? AND catalogID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, supplierID);
	        pstmt.setInt(2, catalogID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Adding product to product catalog
	public boolean addProductCatalog(int productCatalogID, int productID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "INSERT INTO ProductCatalogProducts (catalogID, productID) VALUES (?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, productCatalogID);
	        pstmt.setInt(2, productID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Removing product from a product catalog ID
	public boolean removeProductCatalog(int productCatalogID, int productID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "DELETE FROM ProductCatalogProducts WHERE catalogID = ? AND productID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, productCatalogID);
	        pstmt.setInt(2, productID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Adding an invoice that admin will have to pay
	public boolean addAdminUnpaidInvoice(int adminID, int invoiceID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "INSERT INTO AdminUnpaidInvoices (adminID, invoiceID) VALUES (?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, adminID);
	        pstmt.setInt(2, invoiceID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Removing an invoice that has been paid
	public boolean removeAdminUnpaidInvoice(int adminID, int invoiceID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "DELETE FROM AdminUnpaidInvoices WHERE adminID = ? AND invoiceID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, adminID);
	        pstmt.setInt(2, invoiceID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Adding an inventory manager under Admin
	public boolean addAdminInventoryManager(int adminID, int inventoryManagerID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "INSERT INTO AdminInventoryManager (adminID, inventoryManagerID) VALUES (?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, adminID);
	        pstmt.setInt(2, inventoryManagerID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Removing an inventory manager from an admin
	public boolean removeAdminInventoryManager(int adminID, int inventoryManagerID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "DELETE FROM AdminInventoryManager WHERE adminID = ? AND inventoryManagerID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, adminID);
	        pstmt.setInt(2, inventoryManagerID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	//  Add a supplier pending order
	public boolean addSupplierPendingOrder(int supplierID, int invoiceID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "INSERT INTO SupplierPendingOrders (supplierID, invoiceID) VALUES (?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, supplierID);
	        pstmt.setInt(2, invoiceID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Removing a supplier pending order which has been fulfilled
	public boolean removeSupplierPendingOrder(int supplierID, int invoiceID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "DELETE FROM SupplierPendingOrders WHERE supplierID = ? AND invoiceID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, supplierID);
	        pstmt.setInt(2, invoiceID);
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	// Sql query for when the invoice is paid
	public boolean invoicePaid(int invoiceID) {
		 try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
		        String sql = "UPDATE Invoices SET paid = true  WHERE invoiceID = ?";
		        PreparedStatement pstmt = conn.prepareStatement(sql);
		        pstmt.setInt(1, invoiceID);
		        int rows = pstmt.executeUpdate();
		        if(rows>0) {
		        	sql = "DELETE FROM adminunpaidinvoices WHERE invoiceID = ?";
			        pstmt = conn.prepareStatement(sql);
			        pstmt.setInt(1, invoiceID);
			        rows = pstmt.executeUpdate();
			        if(rows>0) {
			        	return true;
			        }
			        else {
			        	return false;
			        }
		        }
		        else {
		        	return false;
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
	}
	// SQL Query for when an invoice is fulfilled 
	public boolean invoiceDelivered(int invoiceID) {
		 try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
		        String sql = "UPDATE Invoices SET delivered = true  WHERE invoiceID = ?";
		        PreparedStatement pstmt = conn.prepareStatement(sql);
		        pstmt.setInt(1, invoiceID);
		        int rows = pstmt.executeUpdate();
		        if(rows>0) {
		        	sql = "DELETE FROM supplierpendingorders WHERE InvoiceID = ?";
			        pstmt = conn.prepareStatement(sql);
			        pstmt.setInt(1, invoiceID);
			        rows = pstmt.executeUpdate();
			        if(rows>0) {
			        	return true;
			        }
			        else {
			        	return false;
			        }
		        }
		        else {
		        	return false;
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
	}
	// SQL Query for report generation 
	public Report generateReport(InventoryManager manager) {
		Report report = new Report();
	    StringBuilder reportContent = new StringBuilder();

	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        // Query to get product information for the store managed by the given InventoryManager
	        String sql = "SELECT p.productID, p.productName, s.quantity, s.arrivalDate " +
	                     "FROM Store st " +
	                     "JOIN Stock s ON st.storeID = s.storeID " +
	                     "JOIN Product p ON s.productID = p.productID " +
	                     "WHERE st.storeID = ?";
	        report.setCreatedBy("Manager: " + manager.getManagerID() );
	        Date date = new Date(0);
	        report.setCreatedOn(date);
	        report.setUserType("Manager");
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, manager.getManagingStore().getStoreID()); // Assuming InventoryManager has getStoreID() method

	        ResultSet rs = pstmt.executeQuery();

	        // Add a header to the report
	        reportContent.append("Inventory Report for Store ID: ").append(manager.getManagingStore().getStoreID()).append("\n");
	        reportContent.append("=========================================================\n");
	        reportContent.append(String.format("%-10s %-20s %-10s %-15s\n", 
	                                           "ProductID", "ProductName", "Quantity", "ArrivalDate"));
	        reportContent.append("---------------------------------------------------------\n");

	        // Process the result set and build the report
	        while (rs.next()) {
	            int productID = rs.getInt("productID");
	            String productName = rs.getString("productName");
	            int quantity = rs.getInt("quantity");
	            Date arrivalDate = rs.getDate("arrivalDate");

	            reportContent.append(String.format("%-10d %-20s %-10d %-15s\n", 
	                                               productID, productName, quantity, arrivalDate.toString()));
	        }

	        reportContent.append("=========================================================\n");

	        // Set the report content in the Report object
	        report.setReportData(reportContent.toString());
	     // 2. Insert report into the 'report' table
	        String insertQuery = "INSERT INTO report ( createdByID, createdOn, userType) VALUES (?, ?, ?, ?)";
	        PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
	        insertStmt.setInt(1, manager.getManagerID());
	        insertStmt.setDate(2, new Date(System.currentTimeMillis()));
	        insertStmt.setString(3, "Manager");

	        int affectedRows = insertStmt.executeUpdate();

	        // 3. Retrieve and set the generated report ID
	        if (affectedRows > 0) {
	            System.out.println("Inserted into Report Table");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        report.setReportData("Error generating report: " + e.getMessage());
	    }

	    return report;
	}
	// SQL Query for updating inventory manager
	public boolean updateInventoryManager(InventoryManager manager) {
		 try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
		        String sql = "UPDATE inventorymanager SET name = ?, CINC = ?, address = ?, storeID = ?  WHERE managerID = ?";
		        PreparedStatement pstmt = conn.prepareStatement(sql);
		        pstmt.setString(1, manager.getName());
		        pstmt.setString(2, manager.getCNIC());
		        pstmt.setString(3, manager.getAddress());
		        pstmt.setInt(4, manager.getManagingStore().getStoreID());
		        pstmt.setInt(5, manager.getManagerID());
		        int rows = pstmt.executeUpdate();
		        return rows > 0 ;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
	}
	//SQL Query for updating supplier
	public boolean updateSupplier(Supplier supplier) {
		try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "UPDATE supplier SET companyName = ?, location = ?, registrationNum = ? WHERE supplierID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, supplier.getCompany());
	        pstmt.setString(2, supplier.getLocation());
	        pstmt.setInt(3, supplier.getRegNo());
	        pstmt.setInt(4, supplier.getSupplierID());
	        int rows = pstmt.executeUpdate();
	        return rows > 0 ;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	//SQL Query for updating catalog
	public  void updateCatalog(int supplierID, int productID, int addedAmount) {
        String updateQuery = """
            UPDATE ProductCatalogProducts 
            SET quantity = quantity + ? 
            WHERE catalogID = (SELECT productCatalogID 
                               FROM ProductCatalog 
                               WHERE supplierID = ?)
              AND productID = ?;
        """;

        try (Connection conn = DriverManager.getConnection(connection, userName, password);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
             
            // Set parameters for the query
            stmt.setInt(1, addedAmount);
            stmt.setInt(2, supplierID);
            stmt.setInt(3, productID);
            
            // Execute the update
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Catalog updated successfully for SupplierID: " + supplierID + " and ProductID: " + productID);
            } else {
                System.out.println("No matching record found. Update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while updating the product catalog.");
        }
    }
	//Delete an invoice 
	public boolean deleteInovice(int InvoiceID) {
		try(Connection conn = DriverManager.getConnection(connection, userName, password)){
			String sql = "DELETE FROM Invoice WHERE invoiceID = ?";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, InvoiceID);
	        int rows = pstmt.executeUpdate();
			return rows > 0;
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	// Adding a product to a catalog and product table
	public boolean addProduct(Supplier s, Product p) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        // Insert Product into Product table
	        String productSql = "INSERT INTO Product (name, description, price, MFG, EXP) "
	                + "VALUES (?, ?, ?, ?, ?)";
	        try (PreparedStatement productPstmt = conn.prepareStatement(productSql)) {
	            productPstmt.setString(1, p.getName());
	            productPstmt.setDouble(2, p.getPrice());
	            productPstmt.setDouble(3, p.getPrice());
	            productPstmt.setDate(4, (Date)p.getMFG());
	            productPstmt.setDate(5, (Date)p.getEXP());
	            int rows = productPstmt.executeUpdate();
	            if(rows > 0 ) {
	            	String productcatalogproductString = "INSERT INTO productcatalogproducts (CatalogID, ProductID) VALUES (?, ?)";
	            	PreparedStatement pstmtPreparedStatement = conn.prepareStatement(productcatalogproductString);
	            	pstmtPreparedStatement.setInt(1, s.getSupplierID());
	            	pstmtPreparedStatement.setInt(2, p.getProductID());
	            	rows = pstmtPreparedStatement.executeUpdate();
	            	if(rows>0) {
	            		return true;
	            	}
	            	else {
	            		return false;
	            	}
	            }
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean removeProduct(Supplier s, int ProductID) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        // Delete the association between Product and Supplier from ProductCatalogProducts table
	        String removeAssociationSql = "DELETE FROM ProductCatalogProducts WHERE CatalogID = ? AND ProductID = ?";
	        try (PreparedStatement removeAssocStmt = conn.prepareStatement(removeAssociationSql)) {
	            removeAssocStmt.setInt(1, s.getSupplierID());
	            removeAssocStmt.setInt(2, ProductID);
	            int rows = removeAssocStmt.executeUpdate();
	            
	            if (rows > 0) {
	                // If association was successfully removed, delete the product from the Product table
	                String removeProductSql = "DELETE FROM Product WHERE productID = ?";
	                try (PreparedStatement removeProductStmt = conn.prepareStatement(removeProductSql)) {
	                    removeProductStmt.setInt(1, ProductID);
	                    rows = removeProductStmt.executeUpdate();
	                    
	                    if (rows > 0) {
	                        return true; // Product and its association removed successfully
	                    }
	                }
	            }
	        }
	        return false; // Failed to remove product or association
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Error occurred during operation
	    }
	}
	


}
