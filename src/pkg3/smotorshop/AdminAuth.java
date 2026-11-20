package pkg3.smotorshop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Handles administrator login and first-run account creation. */
public final class AdminAuth {
    private AdminAuth() {}

    public static void ensureDefaultAdmin() throws SQLException {
        String countSql = "SELECT COUNT(*) FROM admin_users";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(countSql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            if (rs.getInt(1) > 0) return;
        }
        String salt = PasswordUtil.newSalt();
        String hash = PasswordUtil.hash("admin123", salt);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO admin_users (username,password_hash,password_salt,full_name) VALUES (?,?,?,?)")) {
            ps.setString(1, "admin"); ps.setString(2, hash); ps.setString(3, salt); ps.setString(4, "System Administrator");
            ps.executeUpdate();
        }
    }

    public static String login(String username, String password) throws SQLException {
        String sql = "SELECT full_name,password_hash,password_salt FROM admin_users WHERE username=? AND active=1 LIMIT 1";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return PasswordUtil.verify(password, rs.getString("password_salt"), rs.getString("password_hash"))
                        ? rs.getString("full_name") : null;
            }
        }
    }
}
