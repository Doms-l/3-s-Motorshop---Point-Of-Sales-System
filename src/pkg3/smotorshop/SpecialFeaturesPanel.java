package pkg3.smotorshop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel;

/** Customer-focused modules for the 3'S Motor Shop POS. */
public class SpecialFeaturesPanel extends JPanel {
    private final DefaultTableModel loyaltyModel = model("ID", "Customer", "Phone", "Points");
    private final DefaultTableModel returnsModel = model("Return ID", "Sale ID", "Product ID", "Qty", "Reason", "Warranty End", "Status", "Date");
    private final DefaultTableModel reservationsModel = model("Reservation ID", "Customer", "Phone", "Product ID", "Product", "Qty", "Pickup Date", "Status");
    private JTable loyaltyTable, returnsTable, reservationsTable;

    public SpecialFeaturesPanel() {
        super(new BorderLayout());
        ensureTables();
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Customer Loyalty", buildLoyaltyPanel());
        tabs.addTab("Returns & Warranty", buildReturnsPanel());
        tabs.addTab("Product Reservations", buildReservationsPanel());
        add(tabs, BorderLayout.CENTER);
        applyFeatureTheme(this);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshAll();
    }

    private void applyFeatureTheme(Component component) {
        Theme.apply(component);
    }

    private static DefaultTableModel model(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
    }

    private void ensureTables() {
        String[] sql = {
            "CREATE TABLE IF NOT EXISTS loyalty_customers (loyalty_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, full_name VARCHAR(150) NOT NULL, phone VARCHAR(30) NOT NULL UNIQUE, points INT NOT NULL DEFAULT 0, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)",
            "CREATE TABLE IF NOT EXISTS loyalty_point_history (history_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, loyalty_id INT NOT NULL, action_type VARCHAR(20) NOT NULL, points INT NOT NULL, note VARCHAR(255), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, INDEX (loyalty_id))",
            "CREATE TABLE IF NOT EXISTS product_reservations (reservation_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, customer_name VARCHAR(150) NOT NULL, phone VARCHAR(30) NOT NULL, product_id INT NOT NULL, quantity INT NOT NULL, pickup_date DATE NOT NULL, status VARCHAR(20) NOT NULL DEFAULT 'Reserved', created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, INDEX (product_id), INDEX (status))",
            "CREATE TABLE IF NOT EXISTS sales_returns (return_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, transaction_id INT NOT NULL, product_id INT NOT NULL, quantity INT NOT NULL, reason VARCHAR(255) NOT NULL, warranty_end DATE NULL, status VARCHAR(20) NOT NULL DEFAULT 'Recorded', restocked TINYINT(1) NOT NULL DEFAULT 0, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, INDEX (transaction_id), INDEX (product_id))"
        };
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement()) {
            for (String statement : sql) st.executeUpdate(statement);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "The special-feature tables could not be prepared. Check that MySQL is running and the POS database is available.\n\n" + ex.getMessage(),
                "Database Setup", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel buildLoyaltyPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(new JLabel("Register customers, track loyalty points, and record points earned or redeemed."), BorderLayout.NORTH);
        loyaltyTable = new JTable(loyaltyModel);
        p.add(new JScrollPane(loyaltyTable), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Register Customer");
        JButton adjust = new JButton("Earn / Redeem Points");
        JButton refresh = new JButton("Refresh");
        add.addActionListener(e -> registerLoyaltyCustomer());
        adjust.addActionListener(e -> adjustPoints());
        refresh.addActionListener(e -> loadLoyalty());
        buttons.add(add); buttons.add(adjust); buttons.add(refresh);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildReturnsPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(new JLabel("Record returned or defective parts. Restocking is optional and requires staff confirmation."), BorderLayout.NORTH);
        returnsTable = new JTable(returnsModel);
        p.add(new JScrollPane(returnsTable), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Record Return");
        JButton restock = new JButton("Mark Restocked");
        JButton refresh = new JButton("Refresh");
        add.addActionListener(e -> recordReturn());
        restock.addActionListener(e -> restockSelectedReturn());
        refresh.addActionListener(e -> loadReturns());
        buttons.add(add); buttons.add(restock); buttons.add(refresh);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildReservationsPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(new JLabel("Reserve available parts for customer pickup. Active reservations are counted against available stock."), BorderLayout.NORTH);
        reservationsTable = new JTable(reservationsModel);
        p.add(new JScrollPane(reservationsTable), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("New Reservation");
        JButton complete = new JButton("Mark Picked Up");
        JButton cancel = new JButton("Cancel Reservation");
        JButton refresh = new JButton("Refresh");
        add.addActionListener(e -> createReservation());
        complete.addActionListener(e -> updateReservationStatus("Picked Up"));
        cancel.addActionListener(e -> updateReservationStatus("Cancelled"));
        refresh.addActionListener(e -> loadReservations());
        buttons.add(add); buttons.add(complete); buttons.add(cancel); buttons.add(refresh);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    private void registerLoyaltyCustomer() {
        JTextField name = new JTextField();
        JTextField phone = new JTextField();
        JPanel form = form(new String[]{"Customer name:", "Phone number:"}, new JTextField[]{name, phone});
        if (JOptionPane.showConfirmDialog(this, form, "Register Loyalty Customer", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        if (name.getText().trim().isEmpty() || phone.getText().trim().isEmpty()) { error("Enter both customer name and phone number."); return; }
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO loyalty_customers (full_name, phone) VALUES (?, ?)")) {
            ps.setString(1, name.getText().trim()); ps.setString(2, phone.getText().trim()); ps.executeUpdate();
            loadLoyalty(); info("Customer registered successfully.");
        } catch (SQLException ex) { error("Could not register customer. The phone number may already be registered.\n" + ex.getMessage()); }
    }

    private void adjustPoints() {
        JTextField phone = new JTextField();
        JComboBox<String> action = new JComboBox<String>(new String[]{"Earn", "Redeem"});
        JTextField points = new JTextField("1");
        JTextField note = new JTextField("Purchase / redemption");
        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Customer phone:")); form.add(phone);
        form.add(new JLabel("Action:")); form.add(action);
        form.add(new JLabel("Points:")); form.add(points);
        form.add(new JLabel("Note / sale reference:")); form.add(note);
        if (JOptionPane.showConfirmDialog(this, form, "Earn or Redeem Points", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        int amount;
        try { amount = Integer.parseInt(points.getText().trim()); if (amount <= 0) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { error("Points must be a positive whole number."); return; }
        boolean earn = "Earn".equals(action.getSelectedItem());
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            int id, balance;
            try (PreparedStatement ps = con.prepareStatement("SELECT loyalty_id, points FROM loyalty_customers WHERE phone = ? FOR UPDATE")) {
                ps.setString(1, phone.getText().trim());
                try (ResultSet rs = ps.executeQuery()) { if (!rs.next()) { con.rollback(); error("No loyalty customer found for that phone number."); return; } id = rs.getInt(1); balance = rs.getInt(2); }
            }
            if (!earn && amount > balance) { con.rollback(); error("Customer only has " + balance + " points."); return; }
            int newBalance = earn ? balance + amount : balance - amount;
            try (PreparedStatement ps = con.prepareStatement("UPDATE loyalty_customers SET points = ? WHERE loyalty_id = ?")) { ps.setInt(1, newBalance); ps.setInt(2, id); ps.executeUpdate(); }
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO loyalty_point_history (loyalty_id, action_type, points, note) VALUES (?, ?, ?, ?)")) {
                ps.setInt(1, id); ps.setString(2, earn ? "Earn" : "Redeem"); ps.setInt(3, amount); ps.setString(4, note.getText().trim()); ps.executeUpdate();
            }
            con.commit(); loadLoyalty(); info("Points updated. New balance: " + newBalance);
        } catch (SQLException ex) { error("Could not update points.\n" + ex.getMessage()); }
    }

    private void recordReturn() {
        JTextField saleId = new JTextField();
        JTextField productId = new JTextField();
        JTextField quantity = new JTextField("1");
        JTextField reason = new JTextField();
        JTextField warrantyEnd = new JTextField();
        JCheckBox restock = new JCheckBox("Return item to stock now (after inspection)");
        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        addField(form, "Original sale / transaction ID:", saleId); addField(form, "Product ID:", productId);
        addField(form, "Quantity returned:", quantity); addField(form, "Reason:", reason);
        addField(form, "Warranty end (YYYY-MM-DD, optional):", warrantyEnd);
        form.add(new JLabel("Inventory:")); form.add(restock);
        if (JOptionPane.showConfirmDialog(this, form, "Record Sales Return / Warranty Claim", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        int tid, pid, qty; Date warranty = null;
        try { tid = Integer.parseInt(saleId.getText().trim()); pid = Integer.parseInt(productId.getText().trim()); qty = Integer.parseInt(quantity.getText().trim()); if (tid <= 0 || pid <= 0 || qty <= 0 || reason.getText().trim().isEmpty()) throw new IllegalArgumentException(); if (!warrantyEnd.getText().trim().isEmpty()) warranty = Date.valueOf(LocalDate.parse(warrantyEnd.getText().trim())); }
        catch (Exception ex) { error("Enter valid sale ID, product ID, quantity, reason, and optional warranty date in YYYY-MM-DD format."); return; }
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement check = con.prepareStatement(
                    "SELECT i.quantity - COALESCE((SELECT SUM(r.quantity) FROM sales_returns r WHERE r.transaction_id = i.transaction_id AND r.product_id = i.product_id), 0) AS remaining " +
                    "FROM sales_transaction_items i WHERE i.transaction_id = ? AND i.product_id = ? FOR UPDATE")) {
                check.setInt(1, tid); check.setInt(2, pid);
                try (ResultSet rs = check.executeQuery()) {
                    if (!rs.next() || qty > rs.getInt("remaining")) {
                        con.rollback(); error("The sale item was not found, or this return exceeds the quantity still eligible to be returned."); return;
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO sales_returns (transaction_id, product_id, quantity, reason, warranty_end, restocked, status) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, tid); ps.setInt(2, pid); ps.setInt(3, qty); ps.setString(4, reason.getText().trim());
                if (warranty == null) ps.setNull(5, java.sql.Types.DATE); else ps.setDate(5, warranty);
                ps.setBoolean(6, restock.isSelected()); ps.setString(7, restock.isSelected() ? "Restocked" : "Recorded"); ps.executeUpdate();
            }
            if (restock.isSelected()) try (PreparedStatement ps = con.prepareStatement("UPDATE products SET stock_quantity = stock_quantity + ? WHERE product_id = ?")) { ps.setInt(1, qty); ps.setInt(2, pid); if (ps.executeUpdate() == 0) throw new SQLException("Product ID was not found."); }
            con.commit(); loadReturns(); info("Return recorded successfully.");
        } catch (SQLException ex) { error("Could not record return.\n" + ex.getMessage()); }
    }

    private void restockSelectedReturn() {
        int row = returnsTable.getSelectedRow();
        if (row < 0) { error("Select a return record first."); return; }
        int returnId = Integer.parseInt(returnsModel.getValueAt(row, 0).toString());
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            int pid, qty; boolean already;
            try (PreparedStatement ps = con.prepareStatement("SELECT product_id, quantity, restocked FROM sales_returns WHERE return_id = ? FOR UPDATE")) {
                ps.setInt(1, returnId); try (ResultSet rs = ps.executeQuery()) { if (!rs.next()) { con.rollback(); error("Return record not found."); return; } pid = rs.getInt(1); qty = rs.getInt(2); already = rs.getBoolean(3); }
            }
            if (already) { con.rollback(); error("This return has already been restocked."); return; }
            try (PreparedStatement ps = con.prepareStatement("UPDATE products SET stock_quantity = stock_quantity + ? WHERE product_id = ?")) { ps.setInt(1, qty); ps.setInt(2, pid); if (ps.executeUpdate() == 0) throw new SQLException("Product not found."); }
            try (PreparedStatement ps = con.prepareStatement("UPDATE sales_returns SET restocked = 1, status = 'Restocked' WHERE return_id = ?")) { ps.setInt(1, returnId); ps.executeUpdate(); }
            con.commit(); loadReturns(); info("Returned quantity added back to stock.");
        } catch (SQLException ex) { error("Could not restock return.\n" + ex.getMessage()); }
    }

    private void createReservation() {
        JTextField name = new JTextField(); JTextField phone = new JTextField(); JTextField product = new JTextField();
        JTextField quantity = new JTextField("1"); JTextField date = new JTextField(LocalDate.now().plusDays(1).toString());
        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        addField(form, "Customer name:", name); addField(form, "Phone number:", phone); addField(form, "Product ID:", product);
        addField(form, "Quantity:", quantity); addField(form, "Pickup date (YYYY-MM-DD):", date);
        if (JOptionPane.showConfirmDialog(this, form, "New Product Reservation", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        int pid, qty; Date pickup;
        try { pid = Integer.parseInt(product.getText().trim()); qty = Integer.parseInt(quantity.getText().trim()); pickup = Date.valueOf(LocalDate.parse(date.getText().trim())); if (pid <= 0 || qty <= 0 || name.getText().trim().isEmpty() || phone.getText().trim().isEmpty() || pickup.toLocalDate().isBefore(LocalDate.now())) throw new IllegalArgumentException(); }
        catch (Exception ex) { error("Complete all fields and enter a valid future/current pickup date in YYYY-MM-DD format."); return; }
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            int stock;
            try (PreparedStatement ps = con.prepareStatement("SELECT stock_quantity FROM products WHERE product_id = ? FOR UPDATE")) { ps.setInt(1, pid); try (ResultSet rs = ps.executeQuery()) { if (!rs.next()) { con.rollback(); error("Product ID not found."); return; } stock = rs.getInt(1); } }
            int reserved = 0;
            try (PreparedStatement ps = con.prepareStatement("SELECT COALESCE(SUM(quantity),0) FROM product_reservations WHERE product_id = ? AND status = 'Reserved'")) { ps.setInt(1, pid); try (ResultSet rs = ps.executeQuery()) { if (rs.next()) reserved = rs.getInt(1); } }
            if (qty > stock - reserved) { con.rollback(); error("Not enough unreserved stock. Physical stock: " + stock + "; already reserved: " + reserved + "."); return; }
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO product_reservations (customer_name, phone, product_id, quantity, pickup_date, status) VALUES (?, ?, ?, ?, ?, 'Reserved')")) {
                ps.setString(1, name.getText().trim()); ps.setString(2, phone.getText().trim()); ps.setInt(3, pid); ps.setInt(4, qty); ps.setDate(5, pickup); ps.executeUpdate();
            }
            con.commit(); loadReservations(); info("Product reserved successfully.");
        } catch (SQLException ex) { error("Could not create reservation.\n" + ex.getMessage()); }
    }

    private void updateReservationStatus(String status) {
        int row = reservationsTable.getSelectedRow();
        if (row < 0) { error("Select a reservation first."); return; }
        int id = Integer.parseInt(reservationsModel.getValueAt(row, 0).toString());
        String current = reservationsModel.getValueAt(row, 7).toString();
        if (!"Reserved".equals(current)) { error("Only active reservations can be updated."); return; }
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE product_reservations SET status = ? WHERE reservation_id = ? AND status = 'Reserved'")) {
            ps.setString(1, status); ps.setInt(2, id); ps.executeUpdate(); loadReservations(); info("Reservation marked as " + status + ".");
        } catch (SQLException ex) { error("Could not update reservation.\n" + ex.getMessage()); }
    }

    private void refreshAll() { loadLoyalty(); loadReturns(); loadReservations(); }
    private void loadLoyalty() {
        if (loyaltyTable == null) return; loyaltyModel.setRowCount(0);
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT loyalty_id, full_name, phone, points FROM loyalty_customers ORDER BY full_name")) {
            while (rs.next()) loyaltyModel.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)});
        } catch (SQLException ex) { /* setup error is shown during initialization */ }
    }
    private void loadReturns() {
        if (returnsTable == null) return; returnsModel.setRowCount(0);
        String sql = "SELECT return_id, transaction_id, product_id, quantity, reason, warranty_end, status, created_at FROM sales_returns ORDER BY return_id DESC";
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) returnsModel.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getDate(6) == null ? "" : rs.getDate(6).toString(), rs.getString(7), rs.getTimestamp(8)});
        } catch (SQLException ex) { }
    }
    private void loadReservations() {
        if (reservationsTable == null) return; reservationsModel.setRowCount(0);
        String sql = "SELECT r.reservation_id, r.customer_name, r.phone, r.product_id, COALESCE(p.product_name, '[deleted product]'), r.quantity, r.pickup_date, r.status FROM product_reservations r LEFT JOIN products p ON p.product_id = r.product_id ORDER BY r.reservation_id DESC";
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) reservationsModel.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getInt(6), rs.getDate(7), rs.getString(8)});
        } catch (SQLException ex) { }
    }
    private static JPanel form(String[] labels, JTextField[] fields) {
        JPanel p = new JPanel(new GridLayout(labels.length, 2, 6, 6));
        for (int i = 0; i < labels.length; i++) { p.add(new JLabel(labels[i])); p.add(fields[i]); }
        return p;
    }
    private static void addField(JPanel panel, String label, JTextField field) { panel.add(new JLabel(label)); panel.add(field); }
    private void error(String message) { JOptionPane.showMessageDialog(this, message, "3'S Motor Shop POS", JOptionPane.ERROR_MESSAGE); }
    private void info(String message) { JOptionPane.showMessageDialog(this, message, "3'S Motor Shop POS", JOptionPane.INFORMATION_MESSAGE); }
}
