package pkg3.smotorshop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AdminDashboard extends JFrame {

    private final String adminName;
    private final AdminLogin loginWindow;

    private final JLabel products =
            new JLabel("0");

    private final JLabel stock =
            new JLabel("0");

    private final JLabel todaySales =
            new JLabel("0.00");

    private final JLabel transactions =
            new JLabel("0");

    // =========================================================
    // COLORS
    // =========================================================

    // PURE BLACK
    private final Color DARK =
            Theme.BLACK;

    private final Color RED =
            Theme.RED;

    private final Color LIGHT =
            Theme.WHITE;

    private final Color TEXT =
            Theme.BLACK;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AdminDashboard(
            String adminName,
            AdminLogin loginWindow) {

        this.adminName = adminName;
        this.loginWindow = loginWindow;

        setTitle(
                "3'S Motor Shop - Admin Dashboard"
        );

        setDefaultCloseOperation(
                JFrame.DO_NOTHING_ON_CLOSE
        );

        setSize(1050, 650);
        setLocationRelativeTo(null);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

        buildUI();
        loadStats();
    }

    // =========================================================
    // BUILD USER INTERFACE
    // =========================================================

    private void buildUI() {

        JPanel root =
                new JPanel(new BorderLayout());

        root.setBackground(LIGHT);

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(new BorderLayout());

        // PURE BLACK HEADER
        header.setBackground(DARK);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 25, 8, 25
                )
        );

        // =====================================================
        // LOGO + SHOP NAME
        // =====================================================

        JPanel brandPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                0
                        )
                );

        brandPanel.setOpaque(false);

        // LOAD LOGO
        ImageIcon logoIcon =
                new ImageIcon(
                        getClass().getResource(
                                "/image/Logoo.png"
                        )
                );

        Image logoImage =
                logoIcon.getImage().getScaledInstance(
                        52,
                        52,
                        Image.SCALE_SMOOTH
                );

        JLabel logoLabel =
                new JLabel(
                        new ImageIcon(logoImage)
                );

        brandPanel.add(logoLabel);

        // SHOP NAME
        JLabel shopName =
                new JLabel(
                        "3'S MOTOR SHOP"
                );

        shopName.setForeground(
                Color.WHITE
        );

        shopName.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        brandPanel.add(shopName);

        header.add(
                brandPanel,
                BorderLayout.WEST
        );

        // =====================================================
        // ADMIN INFORMATION
        // =====================================================

        JPanel userPanel =
                new JPanel(
                        new BorderLayout(10, 2)
                );

        userPanel.setOpaque(false);

        JLabel user =
                new JLabel(
                        "Administrator: "
                        + adminName
                );

        user.setForeground(
                Color.WHITE
        );

        user.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        JLabel system =
                new JLabel(
                        "SYSTEM ADMINISTRATION"
                );

        system.setForeground(
                Theme.MUTED
        );

        system.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        userPanel.add(
                user,
                BorderLayout.NORTH
        );

        userPanel.add(
                system,
                BorderLayout.SOUTH
        );

        header.add(
                userPanel,
                BorderLayout.EAST
        );

        root.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // CONTENT
        // =====================================================

        JPanel content =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        content.setBackground(LIGHT);

        content.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        25,
                        20,
                        25
                )
        );

        // =====================================================
        // DASHBOARD TITLE
        // =====================================================

        JLabel welcome =
                new JLabel(
                        "Dashboard Overview"
                );

        welcome.setForeground(TEXT);

        welcome.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        content.add(
                welcome,
                BorderLayout.NORTH
        );

        // =====================================================
        // STAT CARDS
        // =====================================================

        JPanel cards =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                15,
                                15
                        )
                );

        cards.setBackground(LIGHT);

        // PRODUCTS
        addCard(
                cards,
                "PRODUCTS",
                products,
                Theme.RED
        );

        // TOTAL STOCK
        addCard(
                cards,
                "TOTAL STOCK",
                stock,
                Theme.RED
        );

        // TODAY'S SALES
        addCard(
                cards,
                "TODAY'S SALES",
                todaySales,
                Theme.RED
        );

        // TODAY'S TRANSACTIONS
        addCard(
                cards,
                "TODAY'S TRANSACTIONS",
                transactions,
                RED
        );

        content.add(
                cards,
                BorderLayout.CENTER
        );

        // =====================================================
        // QUICK ACTIONS PANEL
        // =====================================================

        JPanel info =
                new JPanel(
                        new BorderLayout()
                );

        info.setBackground(Color.WHITE);

        info.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.RED
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                18,
                                15,
                                18
                        )
                )
        );

        JLabel infoTitle =
                new JLabel(
                        "Quick Actions"
                );

        infoTitle.setForeground(TEXT);

        infoTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        info.add(
                infoTitle,
                BorderLayout.NORTH
        );

        // =====================================================
        // ACTION BUTTONS
        // =====================================================

        JPanel actions =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                12,
                                12
                        )
                );

        actions.setBackground(Color.WHITE);

        JButton pos =
                createButton(
                        "OPEN POS",
                        RED
                );

        JButton refresh =
                createButton(
                        "REFRESH",
                        Theme.RED
                );

        JButton logout =
                createButton(
                        "LOGOUT",
                        Theme.RED_DARK
                );

        JButton exit =
                createButton(
                        "EXIT",
                        Theme.RED_DARK
                );

        // OPEN POS
        pos.addActionListener(
                e -> openPOS()
        );

        // REFRESH
        refresh.addActionListener(
                e -> loadStats()
        );

        // LOGOUT
        logout.addActionListener(
                e -> logout()
        );

        // EXIT
        exit.addActionListener(
                e -> System.exit(0)
        );

        actions.add(pos);
        actions.add(refresh);
        actions.add(logout);
        actions.add(exit);

        info.add(
                actions,
                BorderLayout.CENTER
        );

        content.add(
                info,
                BorderLayout.SOUTH
        );

        root.add(
                content,
                BorderLayout.CENTER
        );

        // =====================================================
        // FOOTER
        // =====================================================

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        // PURE BLACK FOOTER
        footer.setBackground(DARK);

        footer.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        20,
                        8,
                        20
                )
        );

        JLabel footerText =
                new JLabel(
                        "3'S Motor Shop Management System"
                );

        footerText.setForeground(
                Theme.MUTED
        );

        footerText.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        footer.add(
                footerText,
                BorderLayout.WEST
        );

        JLabel status =
                new JLabel(
                        "SYSTEM ONLINE"
                );

        status.setForeground(
                Theme.WHITE
        );

        status.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        footer.add(
                status,
                BorderLayout.EAST
        );

        root.add(
                footer,
                BorderLayout.SOUTH
        );

        // =====================================================
        // SET CONTENT
        // =====================================================

        setContentPane(root);
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private void addCard(
            JPanel parent,
            String title,
            JLabel value,
            Color accent) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.RED
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        // CARD TITLE
        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setForeground(
                Theme.GRAY_TEXT
        );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        card.add(
                titleLabel,
                BorderLayout.NORTH
        );

        // CARD VALUE
        value.setHorizontalAlignment(
                JLabel.CENTER
        );

        value.setForeground(TEXT);

        value.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        card.add(
                value,
                BorderLayout.CENTER
        );

        // ACCENT BAR
        JPanel accentBar =
                new JPanel();

        accentBar.setBackground(
                accent
        );

        accentBar.setPreferredSize(
                new Dimension(
                        0,
                        5
                )
        );

        card.add(
                accentBar,
                BorderLayout.SOUTH
        );

        parent.add(card);
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color background) {

        JButton button =
                new JButton(text);

        button.setBackground(
                background
        );

        button.setForeground(
                Color.WHITE
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        10,
                        12,
                        10
                )
        );

        return button;
    }

    // =========================================================
    // LOAD DASHBOARD STATISTICS
    // =========================================================

    private void loadStats() {

        String[] sqls = {

            // PRODUCTS
            "SELECT COUNT(*) "
            + "FROM products "
            + "WHERE active=1",

            // TOTAL STOCK
            "SELECT COALESCE(SUM(stock_quantity),0) "
            + "FROM products "
            + "WHERE active=1",

            // TODAY'S SALES
            "SELECT COALESCE(SUM(total_amount),0) "
            + "FROM sales_transactions "
            + "WHERE DATE(transaction_date)=CURDATE()",

            // TODAY'S TRANSACTIONS
            "SELECT COUNT(*) "
            + "FROM sales_transactions "
            + "WHERE DATE(transaction_date)=CURDATE()"
        };

        try (
                Connection con =
                        DBConnection.getConnection()
        ) {

            JLabel[] labels = {

                products,
                stock,
                todaySales,
                transactions
            };

            for (
                    int i = 0;
                    i < sqls.length;
                    i++
            ) {

                try (
                        PreparedStatement ps =
                                con.prepareStatement(
                                        sqls[i]
                                );

                        ResultSet rs =
                                ps.executeQuery()
                ) {

                    rs.next();

                    // SALES AMOUNT
                    if (i == 2) {

                        labels[i].setText(
                                String.format(
                                        "₱ %,.2f",
                                        rs.getDouble(1)
                                )
                        );

                    } else {

                        labels[i].setText(
                                String.valueOf(
                                        rs.getInt(1)
                                )
                        );
                    }
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load dashboard statistics:\n"
                    + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // OPEN POS
    // =========================================================

    private void openPOS() {

        new S3Motorshop().setVisible(true);
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );

        if (answer ==
                JOptionPane.YES_OPTION) {

            dispose();

            loginWindow.setVisible(true);
        }
    }
}