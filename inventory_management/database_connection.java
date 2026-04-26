package Inventory_Management;


import java.sql.Connection;
import java.sql.DriverManager;

public class database_connection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/inventory";
    private static final String USER = "root";
    private static final String PASSWORD = "pass here"; // your MySQL password here
    
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
