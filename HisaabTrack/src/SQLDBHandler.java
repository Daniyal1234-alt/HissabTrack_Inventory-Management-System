import java.io.CharConversionException;
import java.sql.*;
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
	public Boolean createDBTable() {
        try (Connection conn = DriverManager.getConnection(connection, userName, password);
    	Statement statement = conn.createStatement()) {
	        
        	System.out.println("Tables created successfully!");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
}
