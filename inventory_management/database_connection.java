package Inventory_Management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database_connection {
    private static final String URL = "jdbc:mysql://mysql-ae35f14-project0132.c.aivencloud.com:23579/defaultdb"
            + "?ssl=true"
            + "&sslMode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "pass here";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}