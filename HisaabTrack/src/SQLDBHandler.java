import java.io.IOException;
import java.security.PublicKey;
import java.sql.*;
import java.util.regex.PatternSyntaxException;

import javax.sql.rowset.JoinRowSet;

import java.util.ArrayList;
import java.util.List;
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
	    
	    try (Connection conn = DriverManager.getConnection(connection, this.userName, this.password)) {
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
	public void addManagerStore(int managerID, int storeID) {
		try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
		    String sql = "INSERT INTO ManagerStore (managerID, storeID) VALUES (?, ?)";
		    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        pstmt.setInt(1, managerID);
		        pstmt.setInt(2, storeID);
		        pstmt.executeUpdate();
		        System.out.println("ManagerStore relationship added successfully.");
		    }
		}catch (SQLException e) {
	        System.err.println("Error adding ManagerStore relationship: " + e.getMessage());
	    }
	}

	public void removeManagerStore(int managerID, int storeID) {
		try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
		    String sql = "DELETE FROM ManagerStore WHERE managerID = ? AND storeID = ?";
		    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        pstmt.setInt(1, managerID);
		        pstmt.setInt(2, storeID);
		        int rowsAffected = pstmt.executeUpdate();
		        if (rowsAffected > 0) {
		            System.out.println("ManagerStore relationship removed successfully.");
		        } else {
		            System.out.println("No relationship found to remove.");
		        }
		    } 
	    }catch (SQLException e) {
	        System.err.println("Error removing ManagerStore relationship: " + e.getMessage());
	    }
	}
	
	// Loading from DB to HissabTrack
	public void loadFromDB(HisaabTrack system) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        
	        // Adding an Admin
	        String adminSql = "SELECT * FROM Admin";
	        try (PreparedStatement adminStmt = conn.prepareStatement(adminSql)) {
	            ResultSet adminRs = adminStmt.executeQuery();
	            while (adminRs.next()) {
	                system.addAdmin(
	                    adminRs.getString("name"),
	                    adminRs.getString("CNIC"),
	                    adminRs.getString("address"),
	                    adminRs.getString("password"),
	                    true
	                );
	            }
	        } catch (SQLException e) {
	            System.err.println("Error loading Admin data: " + e.getMessage());
	        }

	        // Adding a store
	        String storeSqlString = "SELECT * FROM Store";
	        try (PreparedStatement storeStmt = conn.prepareStatement(storeSqlString)) {
	            ResultSet rs = storeStmt.executeQuery();
	            while (rs.next()) {
	                int storeID = rs.getInt("storeID");
	                String location = rs.getString("location");
	                Store store = new Store(storeID, location);
	                system.addStore(store);
	            }
	        } catch (SQLException e) {
	            System.err.println("Error loading Store data: " + e.getMessage());
	        }

	        // Adding Inventory Managers
	        String managerSql = """
	            SELECT ai.adminID, a.managerID, a.name, a.CNIC, a.address, a.password, ms.storeID
	            FROM InventoryManager a
	            JOIN admininventorymanager ai ON a.managerID = ai.inventoryManagerID
	            JOIN ManagerStore ms ON a.managerID = ms.managerID
	        """;
	        try (PreparedStatement managerStmt = conn.prepareStatement(managerSql)) {
	            ResultSet managerRs = managerStmt.executeQuery();
	            while (managerRs.next()) {
	                system.addManager(
	                    managerRs.getInt("adminID"),
	                    managerRs.getString("name"),
	                    managerRs.getString("CNIC"),
	                    managerRs.getString("address"),
	                    managerRs.getString("password"),
	                    system.getStore(managerRs.getInt("storeID")),
	                    true
	                );
	            }
	        } catch (SQLException e) {
	            System.err.println("Error loading Inventory Manager data: " + e.getMessage());
	        }

	        // Adding a list of Products
	        List<Product> products = new ArrayList<>();
	        String query = "SELECT * FROM Product";
	        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                Product product = new Product(
	                    rs.getInt("productID"),
	                    rs.getString("name"),
	                    rs.getString("description"),
	                    rs.getFloat("price"),
	                    rs.getDate("MFG"),
	                    rs.getDate("EXP")
	                );
	                products.add(product);
	            }
	        } catch (SQLException e) {
	            System.err.println("Error loading Product data: " + e.getMessage());
	        }
	        for(Product product : products) {
	        	System.out.println("Product: " + product.getProductID() + "Product Name: " + product.getName());
	        }
	        // Adding stock
	        query = "SELECT st.*, s.*, m.managerID FROM Store st "
	              + "LEFT JOIN StoreStock ss ON st.storeID = ss.storeID "
	              + "LEFT JOIN ManagerStore m ON m.storeID = st.storeID "
	              + "JOIN Stock s ON s.stockID = ss.stockID";
	        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                int storeID = rs.getInt("storeID");
	                Store store = system.getStore(storeID);
	                store.setManagerID(rs.getInt("managerID"));
	                Stock stock = new Stock(
	                    rs.getInt("stockID"),
	                    getProduct(products, rs.getInt("productID")),
	                    rs.getInt("quantity"),
	                    rs.getDouble("totalCost"),
	                    rs.getDate("arrivalDate")
	                );
	                store.addStock(stock);
	            }
	        } catch (SQLException e) {
	            System.err.println("Error loading Stock data: " + e.getMessage());
	        }
	        System.out.println("Before adding supplier: ");
	        for(Product product : products) {
	        	System.out.println("Product: " + product.getProductID() + "Product Name: " + product.getName());
	        }
	        // Adding a supplier
	        query = "SELECT s.supplierID, s.companyName, s.location, s.registrationNum, s.password, "
	              + "cp.productID, cp.quantity FROM Supplier s "
	              + "JOIN ProductCatalogProducts cp ON cp.catalogID = s.supplierID "
	              + "JOIN Product p ON cp.productID = p.productID";
	        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                system.addSupplier(
	                    1, // You may need to adjust this field if needed
	                    rs.getString("companyName"),
	                    rs.getString("location"),
	                    rs.getInt("registrationNum"),
	                    rs.getString("password"),
	                    true
	                );
	                Supplier supplier = system.getSupplier(rs.getInt("s.supplierID"));
	                Product product = getProduct(products, rs.getInt("productID"));
	                System.out.println("Product :  " + product.getProductID() + "   " + product.getName());
	                supplier.addProduct(product, rs.getInt("quantity"));
	            }
	        } catch (SQLException e) {
	            System.err.println("Error loading Supplier data: " + e.getMessage());
	        }
	        System.out.println("Before adding invoices: ");
	        for(Product product : products) {
	        	System.out.println("Product: " + product.getProductID() + "Product Name: " + product.getName());
	        }
	        // Listing an invoice
	        List<Invoice> invoiceDetailsList = new ArrayList<>();
	        query = "SELECT i.invoiceID, i.createdByID, i.userType, i.paid, i.delivered, i.createdOn FROM Invoice i ";
	        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                Invoice details = new Invoice(
	                    rs.getInt("invoiceID"),
	                    rs.getInt("createdByID"),
	                    rs.getDate("createdOn"),
	                    rs.getBoolean("delivered"),
	                    rs.getBoolean("paid"),
	                    "\0"
	                );
	                //System.out.println(details.toString());
	                query = """
	                    SELECT i.invoiceID,
	                           ip.productID, p.name AS productName, p.description AS productDescription, 
	                           ip.quantity, p.price
	                    FROM Invoice i
	                    JOIN InvoiceProduct ip ON i.invoiceID = ip.invoiceID
	                    JOIN Product p ON ip.productID = p.productID
	                    WHERE i.invoiceID = ?
	                """;
	                
	                int invoiceID = details.getInvoiceID();
	                try (PreparedStatement stmt2 = conn.prepareStatement(query)) {
	                    stmt2.setInt(1, invoiceID);
	                    try (ResultSet rs2 = stmt2.executeQuery()) {
	                        while (rs2.next()) {
	                        	if(getProduct(products, rs2.getInt("productID"))!=null) {
	                        		Product product = getProduct(products, rs2.getInt("productID")); 
		                            details.addItem(product, rs2.getInt("quantity"));
	                        	}
	                        	else {
	                        		System.out.println("Product ID: " + rs2.getInt("productID") + "   NULL" );
	                        	}
	                        }
	                    }
	                }
	                details.toString();
	                invoiceDetailsList.add(details);
	            }
	        } catch (SQLException e) {
	            System.err.println("Error loading Invoice data: " + e.getMessage());
	        }
	        //for(Invoice invoice : invoiceDetailsList) {
	        //	invoice.toString();
	        //}
	        query = "SELECT "
	                + "    s.supplierID, "
	                + "    s.companyName, "
	                + "    s.location AS supplierLocation, "
	                + "    i.invoiceID, "
	                + "    ip.productID, "
	                + "    p.name AS productName, "
	                + "    p.description AS productDescription, "
	                + "    ip.quantity AS quantityDelivered, "
	                + "    i.createdOn AS deliveryDate "
	                + "FROM "
	                + "    Supplier s "
	                + "JOIN "
	                + "    SupplierPendingOrders sdo ON s.supplierID = sdo.supplierID "
	                + "JOIN "
	                + "    Invoice i ON sdo.invoiceID = i.invoiceID "
	                + "JOIN "
	                + "    InvoiceProduct ip ON i.invoiceID = ip.invoiceID "
	                + "JOIN "
	                + "    Product p ON ip.productID = p.productID "
	                + "WHERE "
	                + "    i.delivered = TRUE;";

			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			    while (rs.next()) {
			        system.addPendingOrder(rs.getInt("supplierID"), getInvoice(invoiceDetailsList, rs.getInt("invoiceID")));
			    }
			} catch (SQLException e) {
			    System.err.println("Error loading Supplier Pending Orders: " + e.getMessage());
			}


	        // Adding to supplier's sentOrders
	        query = "SELECT s.supplierID, s.companyName, s.location AS supplierLocation, i.invoiceID, "
	              + "ip.productID, "
	              + "ip.quantity AS quantityDelivered, i.createdOn AS deliveryDate "
	              + "FROM Supplier s "
	              + "JOIN SupplierDeliveredOrders sdo ON s.supplierID = sdo.supplierID "
	              + "JOIN Invoice i ON sdo.invoiceID = i.invoiceID "
	              + "JOIN InvoiceProduct ip ON i.invoiceID = ip.invoiceID "
	              + "WHERE i.delivered = TRUE;";
	        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                system.addDeliveredOrder(rs.getInt("s.supplierID"), getInvoice(invoiceDetailsList, rs.getInt("i.invoiceID")));
	            }
	        } catch (SQLException e) {
	            System.err.println("Error loading Supplier Delivered Orders: " + e.getMessage());
	        }

	        // Adding unpaidInvoices in Admin
	        query = "SELECT i.invoiceID, i.createdByID, i.userType, i.paid, i.delivered, i.createdOn, au.adminID "
	              + "FROM AdminUnpaidInvoices au "
	              + "JOIN Invoice i ON au.invoiceID = i.invoiceID "
	              + "WHERE  i.paid = FALSE;";
	        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                system.addUnpaidInvoice(rs.getInt("adminID"), getInvoice(invoiceDetailsList, rs.getInt("i.invoiceID")));
	            }
	        } catch (SQLException e) {
	            System.err.println("Error loading Unpaid Invoices: " + e.getMessage());
	        }

	    } catch (SQLException e) {
	        System.err.println("Error connecting to the database: " + e.getMessage());
	    }
	}

	// Get an invoice
	public Invoice getInvoice(List<Invoice> invoice, int invoiceID) {
		for(Invoice i : invoice) {
			if(i.getInvoiceID()==invoiceID) {
				return i;
			}
		}
		return null;
	}
	// Get a product 
	public Product getProduct(List<Product> products , int ProductID) {
		for(Product p: products) {
			if(p.getProductID() == ProductID) {
				return p;
			}
		}
		return null;
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
	        String sql = "INSERT INTO Supplier (companyName, location, registrationNum, balance, password) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, supplier.getCompany());
	        pstmt.setString(2, supplier.getLocation());
	        pstmt.setInt(3, supplier.getRegNo());
	        pstmt.setDouble(4, supplier.getBalance());
	        pstmt.setString(5, password);
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
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        // Step 1: Insert the Invoice into the Invoice table
	        String sql = "INSERT INTO Invoice (createdByID, createdOn, userType, paid, delivered) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  // Get generated keys
	        pstmt.setInt(1, invoice.getCreatedBy());
	        java.sql.Date sqlDate = new java.sql.Date(invoice.getCreatedOn().getTime());
	        pstmt.setDate(2, sqlDate);
	        pstmt.setString(3, invoice.getCreatorType());
	        pstmt.setBoolean(4, invoice.isPaidFor());
	        pstmt.setBoolean(5, invoice.isDelivered());

	        int affectedRows = pstmt.executeUpdate();
	        
	        // Check if the invoice was successfully inserted
	        if (affectedRows > 0) {
	            // Step 2: Get the generated invoiceID
	            ResultSet generatedKeys = pstmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int invoiceID = generatedKeys.getInt(1);  // Get the generated invoiceID

	                // Step 3: Insert each product related to this invoice into InvoiceProduct table
	                String invoiceProductSql = "INSERT INTO InvoiceProduct (invoiceID, productID, quantity) VALUES (?, ?, ?)";
	                try (PreparedStatement pstmtInvoiceProduct = conn.prepareStatement(invoiceProductSql)) {
	                	int idx = 0;
	                    for (Product  invoiceProduct: invoice.getProducts()) {  // Assuming invoice has a list of products
	                        pstmtInvoiceProduct.setInt(1, invoiceID);  // Use the generated invoiceID
	                        pstmtInvoiceProduct.setInt(2, invoiceProduct.getProductID());
	                        int amount = invoice.getQuantity().get(idx++);
	                        pstmtInvoiceProduct.setInt(3, amount);
	                        pstmtInvoiceProduct.addBatch();  // Add to batch
	                    }

	                    // Execute the batch insert
	                    pstmtInvoiceProduct.executeBatch();
	                }
	                return true;  // Successfully added the invoice and products
	            }
	        }

	        return false;  // Failed to insert the invoice

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
	        java.sql.Date sqlDate = new java.sql.Date(stock.getArrivalDate().getTime());
	        pstmt.setDate(5, sqlDate);
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
	public boolean addProductCatalog(int productCatalogID, int productID, int quantity) {
	    try (Connection conn = DriverManager.getConnection(connection, userName, password)) {
	        String sql = "INSERT INTO ProductCatalogProducts (catalogID, productID, quantity) VALUES (?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, productCatalogID);
	        pstmt.setInt(2, productID);
	        pstmt.setInt(3, quantity);
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
		        String sql = "UPDATE inventorymanager SET name = ?, CNIC = ?, address = ?, storeID = ?  WHERE managerID = ?";
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
	//SQL Query for updating admin
	public boolean updateAdmin(Admin a) {
	    String sql = "UPDATE Admin SET name = ?, CNIC = ?, address = ?, active = ? WHERE adminID = ?";
	    
	    try (Connection conn = DriverManager.getConnection(connection, userName, password);
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        // Set the parameters for the SQL statement
	        stmt.setString(1, a.getName());
	        stmt.setString(2, a.getCNIC());
	        stmt.setString(3, a.getAddress());
	        stmt.setBoolean(4, a.isActive());
	        stmt.setInt(5, a.getAdminID());  // Assuming `adminID` is an integer
	        
	        // Execute the update statement
	        int rowsAffected = stmt.executeUpdate();
	        
	        // Return true if the update was successful (i.e., at least one row was affected)
	        return rowsAffected > 0;
	        
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
            WHERE catalogID = ?
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
	            java.sql.Date mfgDate = new java.sql.Date(p.getMFG().getTime());
	            java.sql.Date expDate = new java.sql.Date(p.getEXP().getTime());
	            productPstmt.setDate(4, mfgDate);
	            productPstmt.setDate(5, expDate);
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
