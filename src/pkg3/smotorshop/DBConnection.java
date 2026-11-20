package pkg3.smotorshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Centralized MySQL connection settings for the desktop application. */
public final class DBConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/threes_motorshop_pos"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Manila&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Change this if your MySQL root account has a password.

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException modern) {
            try { Class.forName("com.mysql.jdbc.Driver"); }
            catch (ClassNotFoundException legacy) {
                throw new SQLException("MySQL JDBC driver not found. Add mysql-connector-java to NetBeans Libraries.", legacy);
            }
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
