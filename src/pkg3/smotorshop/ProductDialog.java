package pkg3.smotorshop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Product add/edit dialog using the same column names as the POS database.
 */
public class ProductDialog extends JDialog {

    // =========================================================
    // COLORS - 3'S MOTOR SHOP THEME
    // =========================================================

    private final Color BLACK =
            new Color(0, 0, 0);

    private final Color RED =
            Theme.RED;

    private final Color WHITE =
            Color.WHITE;

    private final Color LIGHT =
            Theme.WHITE;

    private final Color TEXT =
            Theme.BLACK;

    private final Color LABEL =
            new Color(70, 75, 80);

    private final Color BORDER =
            Theme.RED;

    // =========================================================
    // FIELDS
    // =========================================================

    private final JTextField brandField =
            new JTextField();

    private final JTextField nameField =
            new JTextField();

    private final JTextField priceField =
            new JTextField();

    private final JTextField stockField =
            new JTextField();

    private final JTextField barcodeField =
            new JTextField();

    private final JLabel barcodePreview =
            new JLabel(
                    "No barcode generated",
                    JLabel.CENTER
            );

    private final Integer productId;

    private boolean saved = false;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ProductDialog(
            Frame owner,
            boolean modal,
            Integer productId,
            String brand,
            String name,
            String price,
            String stock) {

        super(owner, modal);

        this.productId = productId;

        setTitle(
                productId == null
                        ? "Add Product"
                        : "Edit Product"
        );

        setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        buildUI();

        // =====================================================
        // LOAD EXISTING PRODUCT DATA
        // =====================================================

        if (brand != null) {
            brandField.setText(brand);
        }

        if (name != null) {
            nameField.setText(name);
        }

        if (price != null) {
            priceField.setText(price);
        }

        if (stock != null) {
            stockField.setText(stock);
        }

        if (productId != null) {
            loadBarcode(productId);
        }

        setSize(560, 650);
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(LIGHT);

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(BLACK);

        header.setPreferredSize(
                new Dimension(
                        0,
                        82
                )
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        20,
                        8,
                        20
                )
        );

        // =====================================================
        // LOGO + TITLE
        // =====================================================

        JPanel brandPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                2
                        )
                );

        brandPanel.setOpaque(false);

        // LOAD LOGO
        try {

            java.net.URL logoURL =
                    getClass().getResource(
                            "/image/Logoo.png"
                    );

            if (logoURL != null) {

                ImageIcon originalLogo =
                        new ImageIcon(logoURL);

                Image logoImage =
                        originalLogo.getImage()
                                .getScaledInstance(
                                        55,
                                        55,
                                        Image.SCALE_SMOOTH
                                );

                JLabel logoLabel =
                        new JLabel(
                                new ImageIcon(
                                        logoImage
                                )
                        );

                brandPanel.add(
                        logoLabel
                );
            }

        } catch (Exception ex) {
            // Continue without logo if unavailable
        }

        // =====================================================
        // SHOP NAME + PAGE TITLE
        // =====================================================

        JPanel titlePanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        titlePanel.setOpaque(false);

        JLabel shopName =
                new JLabel(
                        "3'S MOTOR SHOP"
                );

        shopName.setForeground(
                WHITE
        );

        shopName.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        JLabel pageTitle =
                new JLabel(
                        productId == null
                                ? "ADD NEW PRODUCT"
                                : "EDIT PRODUCT"
                );

        pageTitle.setForeground(
                Theme.RED
        );

        pageTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        titlePanel.add(shopName);
        titlePanel.add(pageTitle);

        brandPanel.add(
                titlePanel
        );

        header.add(
                brandPanel,
                BorderLayout.WEST
        );

        // =====================================================
        // RED ACCENT LINE
        // =====================================================

        JPanel redLine =
                new JPanel();

        redLine.setBackground(RED);

        redLine.setPreferredSize(
                new Dimension(
                        0,
                        4
                )
        );

        // Put header + red line together
        JPanel headerContainer =
                new JPanel(
                        new BorderLayout()
                );

        headerContainer.setBackground(
                BLACK
        );

        headerContainer.add(
                header,
                BorderLayout.CENTER
        );

        headerContainer.add(
                redLine,
                BorderLayout.SOUTH
        );

        root.add(
                headerContainer,
                BorderLayout.NORTH
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        JPanel content =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        content.setBackground(
                LIGHT
        );

        content.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        25,
                        10,
                        25
                )
        );

        // =====================================================
        // FORM
        // =====================================================

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setBackground(
                LIGHT
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        5,
                        5,
                        5,
                        5
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.WEST;

        gbc.weightx = 0;

        // =====================================================
        // BRAND
        // =====================================================

        addFormRow(
                form,
                gbc,
                0,
                "Brand:",
                brandField
        );

        // =====================================================
        // PRODUCT NAME
        // =====================================================

        addFormRow(
                form,
                gbc,
                1,
                "Product Name:",
                nameField
        );

        // =====================================================
        // UNIT PRICE
        // =====================================================

        addFormRow(
                form,
                gbc,
                2,
                "Unit Price:",
                priceField
        );

        // =====================================================
        // STOCK QUANTITY
        // =====================================================

        addFormRow(
                form,
                gbc,
                3,
                "Stock Quantity:",
                stockField
        );

        // =====================================================
        // BARCODE
        // =====================================================

        addFormRow(
                form,
                gbc,
                4,
                "Barcode:",
                barcodeField
        );

        content.add(
                form,
                BorderLayout.NORTH
        );

        // =====================================================
        // BARCODE PREVIEW PANEL
        // =====================================================

        JPanel previewPanel =
                new JPanel(
                        new BorderLayout()
                );

        previewPanel.setBackground(
                Color.WHITE
        );

        previewPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        JLabel previewTitle =
                new JLabel(
                        "BARCODE PREVIEW",
                        JLabel.CENTER
                );

        previewTitle.setForeground(
                TEXT
        );

        previewTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        previewPanel.add(
                previewTitle,
                BorderLayout.NORTH
        );

        barcodePreview.setForeground(
                Theme.GRAY_TEXT
        );

        barcodePreview.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        barcodePreview.setPreferredSize(
                new Dimension(
                        0,
                        150
                )
        );

        previewPanel.add(
                barcodePreview,
                BorderLayout.CENTER
        );

        content.add(
                previewPanel,
                BorderLayout.CENTER
        );

        root.add(
                content,
                BorderLayout.CENTER
        );

        // =====================================================
        // BUTTON PANEL
        // =====================================================

        JPanel bottom =
                new JPanel(
                        new BorderLayout()
                );

        bottom.setBackground(
                LIGHT
        );

        bottom.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        25,
                        20,
                        25
                )
        );

        // =====================================================
        // GENERATE BARCODE
        // =====================================================

        JButton generateBtn =
                createButton(
                        "GENERATE BARCODE",
                        Theme.RED_DARK,
                        WHITE
                );

        generateBtn.addActionListener(
                e -> generatePreview()
        );

        // =====================================================
        // SAVE / CANCEL
        // =====================================================

        JPanel rightButtons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        rightButtons.setOpaque(false);

        JButton saveBtn =
                createButton(
                        "SAVE",
                        RED,
                        WHITE
                );

        JButton cancelBtn =
                createButton(
                        "CANCEL",
                        Theme.RED_DARK,
                        WHITE
                );

        saveBtn.addActionListener(
                e -> onSave()
        );

        cancelBtn.addActionListener(
                e -> dispose()
        );

        rightButtons.add(saveBtn);
        rightButtons.add(cancelBtn);

        bottom.add(
                generateBtn,
                BorderLayout.WEST
        );

        bottom.add(
                rightButtons,
                BorderLayout.EAST
        );

        root.add(
                bottom,
                BorderLayout.SOUTH
        );

        // =====================================================
        // SET CONTENT
        // =====================================================

        setContentPane(root);
    }

    // =========================================================
    // FORM ROW
    // =========================================================

    private void addFormRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String labelText,
            JTextField field) {

        gbc.gridy = row;

        // LABEL
        gbc.gridx = 0;
        gbc.weightx = 0;

        JLabel label =
                new JLabel(
                        labelText
                );

        label.setForeground(
                LABEL
        );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        panel.add(
                label,
                gbc
        );

        // FIELD
        gbc.gridx = 1;
        gbc.weightx = 1.0;

        field.setPreferredSize(
                new Dimension(
                        250,
                        32
                )
        );

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        field.setBackground(
                Color.WHITE
        );

        field.setForeground(
                TEXT
        );
        field.setCaretColor(Color.BLACK);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.RED
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                8,
                                5,
                                8
                        )
                )
        );

        panel.add(
                field,
                gbc
        );
    }

    // =========================================================
    // BUTTON CREATOR
    // =========================================================

    private JButton createButton(
            String text,
            Color background,
            Color foreground) {

        JButton button =
                new JButton(text);

        button.setBackground(
                background
        );

        button.setForeground(
                foreground
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        16,
                        10,
                        16
                )
        );

        button.setOpaque(true);

        return button;
    }

    // =========================================================
    // LOAD BARCODE
    // =========================================================

    private void loadBarcode(int id) {

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(
                                "SELECT barcode "
                                + "FROM products "
                                + "WHERE product_id=?"
                        )
        ) {

            ps.setInt(
                    1,
                    id
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    String barcode =
                            rs.getString(1);

                    barcodeField.setText(
                            barcode
                    );

                    if (barcode != null
                            && !barcode.trim().isEmpty()) {

                        showBarcode(
                                barcode
                        );
                    }
                }
            }

        } catch (SQLException ignored) {
        }
    }

    // =========================================================
    // GENERATE BARCODE
    // =========================================================

    private void generatePreview() {

        String value =
                barcodeField
                        .getText()
                        .trim();

        if (value.isEmpty()) {

            value =
                    "3S"
                    + System.currentTimeMillis();
        }

        barcodeField.setText(
                value
        );

        showBarcode(
                value
        );
    }

    // =========================================================
    // SHOW BARCODE
    // =========================================================

    private void showBarcode(
            String value) {

        try {

            File file =
                    BarcodeGenerator.save(
                            value
                    );

            ImageIcon icon =
                    new ImageIcon(
                            file.getAbsolutePath()
                    );

            Image scaled =
                    icon.getImage()
                            .getScaledInstance(
                                    400,
                                    120,
                                    Image.SCALE_SMOOTH
                            );

            barcodePreview.setIcon(
                    new ImageIcon(
                            scaled
                    )
            );

            barcodePreview.setText("");

        } catch (Exception ex) {

            barcodePreview.setIcon(null);

            barcodePreview.setText(
                    "Barcode preview unavailable: "
                    + ex.getMessage()
            );
        }
    }

    // =========================================================
    // SAVE PRODUCT
    // =========================================================

    private void onSave() {

        String brand =
                brandField
                        .getText()
                        .trim();

        String name =
                nameField
                        .getText()
                        .trim();

        String priceText =
                priceField
                        .getText()
                        .trim()
                        .replace(",", "");

        String stockText =
                stockField
                        .getText()
                        .trim();

        String barcode =
                barcodeField
                        .getText()
                        .trim()
                        .toUpperCase();

        // =====================================================
        // REQUIRED FIELDS
        // =====================================================

        if (brand.isEmpty()
                || name.isEmpty()
                || priceText.isEmpty()
                || stockText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in brand, product name, price, and stock.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // =====================================================
        // GENERATE DEFAULT BARCODE
        // =====================================================

        if (barcode.isEmpty()) {

            barcode =
                    productId == null
                            ? ""
                            : "3S"
                            + String.format(
                                    "%08d",
                                    productId
                            );
        }

        double price;
        int stock;

        // =====================================================
        // VALIDATE NUMBERS
        // =====================================================

        try {

            price =
                    Double.parseDouble(
                            priceText
                    );

            stock =
                    Integer.parseInt(
                            stockText
                    );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Price must be a number and stock must be a whole number.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // =====================================================
        // NEGATIVE VALUES
        // =====================================================

        if (price < 0
                || stock < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Price and stock cannot be negative.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // =====================================================
        // DATABASE SAVE
        // =====================================================

        try (
                Connection con =
                        DBConnection.getConnection()
        ) {

            // =================================================
            // ADD NEW PRODUCT
            // =================================================

            if (productId == null) {

                int newId;

                try (
                        PreparedStatement ps =
                                con.prepareStatement(
                                        "INSERT INTO products "
                                        + "(barcode, brand, product_name, "
                                        + "unit_price, stock_quantity) "
                                        + "VALUES (?,?,?,?,?)",
                                        java.sql.Statement.RETURN_GENERATED_KEYS
                                )
                ) {

                    // Temporary unique barcode
                    ps.setString(
                            1,
                            barcode.isEmpty()
                                    ? "TEMP"
                                    + System.nanoTime()
                                    : barcode
                    );

                    ps.setString(
                            2,
                            brand
                    );

                    ps.setString(
                            3,
                            name
                    );

                    ps.setDouble(
                            4,
                            price
                    );

                    ps.setInt(
                            5,
                            stock
                    );

                    ps.executeUpdate();

                    try (
                            ResultSet keys =
                                    ps.getGeneratedKeys()
                    ) {

                        keys.next();

                        newId =
                                keys.getInt(1);
                    }
                }

                // =============================================
                // CREATE PRODUCT-ID BARCODE
                // =============================================

                if (barcode.isEmpty()) {

                    barcode =
                            "3S"
                            + String.format(
                                    "%08d",
                                    newId
                            );

                    try (
                            PreparedStatement ps =
                                    con.prepareStatement(
                                            "UPDATE products "
                                            + "SET barcode=? "
                                            + "WHERE product_id=?"
                                    )
                    ) {

                        ps.setString(
                                1,
                                barcode
                        );

                        ps.setInt(
                                2,
                                newId
                        );

                        ps.executeUpdate();
                    }
                }

            } else {

                // =============================================
                // EDIT EXISTING PRODUCT
                // =============================================

                try (
                        PreparedStatement ps =
                                con.prepareStatement(
                                        "UPDATE products "
                                        + "SET barcode=?, "
                                        + "brand=?, "
                                        + "product_name=?, "
                                        + "unit_price=?, "
                                        + "stock_quantity=? "
                                        + "WHERE product_id=?"
                                )
                ) {

                    ps.setString(
                            1,
                            barcode
                    );

                    ps.setString(
                            2,
                            brand
                    );

                    ps.setString(
                            3,
                            name
                    );

                    ps.setDouble(
                            4,
                            price
                    );

                    ps.setInt(
                            5,
                            stock
                    );

                    ps.setInt(
                            6,
                            productId
                    );

                    ps.executeUpdate();
                }
            }

            // =================================================
            // SAVE BARCODE IMAGE
            // =================================================

            BarcodeGenerator.save(
                    barcode
            );

            saved = true;

            dispose();

        } catch (
                SQLException
                | IllegalArgumentException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not save product: "
                    + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (
                java.io.IOException ex
        ) {

            saved = true;

            dispose();
        }
    }

    // =========================================================
    // CHECK IF SAVED
    // =========================================================

    public boolean isSaved() {

        return saved;
    }
}