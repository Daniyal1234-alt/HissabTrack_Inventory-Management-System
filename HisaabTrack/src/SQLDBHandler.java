import java.awt.im.InputMethodHighlight;
import java.io.CharConversionException;
import java.lang.foreign.AddressLayout;
import java.sql.*;

import com.mysql.cj.callback.UsernameCallback;
public class SQLDBHandler {
	private String connection;
	private String className;
	private String userName;
	private String password;
	
	SQLDBHandler(){
		className = "com.mysql.cj.jdbc.Driver";
		connection = "jdbc:mysql://localhost:3306/HisaabTrack";
		userName = "root";
		password = "dani";
	}
	// Adding an Admin to the SQL DB
	public boolean addAdmin(Admin admin) {
	    try (Connection conn =  DriverManager.getConnection(connection,userName, password)) {
	        String sql = "INSERT INTO Admin (name, CNIC, address, active) VALUES (?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, admin.getName());
	        pstmt.setString(2, admin.getCNIC());
	        pstmt.setString(3, admin.getAddress());
	        pstmt.setBoolean(4, admin.isActive());
	        return pstmt.executeUpdate() > 0;
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
	        String sql = "INSERT INTO InventoryManager (name, CNIC, address, storeID) VALUES (?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, manager.getName());
	        pstmt.setString(2, manager.getCNIC());
	        pstmt.setString(3, manager.getAddress());
	        if (manager.getManagingStore() != null) {
	            pstmt.setInt(4, manager.getManagingStore().getStoreID());
	        } else {
	            pstmt.setNull(4, java.sql.Types.INTEGER);
	        }
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
	        String sql = "INSERT INTO Supplier (companyName, location, registrationNum) VALUES (?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, supplier.getCompany());
	        pstmt.setString(2, supplier.getLocation());
	        pstmt.setInt(3, supplier.getRegNo());
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
	        String sql = "INSERT INTO Invoice (createdByID, createdOn, userType, paid, deliverd) VALUES (?, ?, ?, ?, ?)";
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

}
