package pkg3.smotorshop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AdminLogin extends JFrame {

    private final JTextField username = new JTextField("admin");
    private final JPasswordField password = new JPasswordField();

    // =========================================================
    // COLORS
    // =========================================================

    private final Color DARK = Theme.BLACK;
    private final Color RED = Theme.RED;
    private final Color LIGHT = Theme.WHITE;
    private final Color TEXT = Theme.BLACK;

    public AdminLogin() {

        setTitle("3'S Motor Shop - Administrator Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);

        buildUI();

        // Window size
        setSize(520, 540);

        setLocationRelativeTo(null);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    }

    private void buildUI() {

        // =====================================================
        // MAIN PANEL
        // =====================================================

        JPanel main = new JPanel(new BorderLayout());

        main.setBackground(LIGHT);

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header = new JPanel(new BorderLayout());

        // PURE BLACK HEADER
        header.setBackground(DARK);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        28,
                        20,
                        28
                )
        );

        // =====================================================
        // LOGO
        // =====================================================

        JLabel logoLabel = new JLabel();

        try {

            ImageIcon originalIcon =
                    new ImageIcon(
                            getClass().getResource(
                                    "/image/Logoo.png"
                            )
                    );

            Image originalImage =
                    originalIcon.getImage();

            Image resizedImage =
                    originalImage.getScaledInstance(
                            65,
                            65,
                            Image.SCALE_SMOOTH
                    );

            logoLabel.setIcon(
                    new ImageIcon(resizedImage)
            );

        } catch (Exception e) {

            // Logo will simply be hidden if the file
            // cannot be found.
            logoLabel.setText("");
        }

        header.add(
                logoLabel,
                BorderLayout.WEST
        );

        // =====================================================
        // SHOP NAME
        // =====================================================

        JLabel shopName =
                new JLabel("3'S MOTOR SHOP");

        shopName.setForeground(Color.WHITE);

        shopName.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        // =====================================================
        // ADMINISTRATION
        // =====================================================

        JLabel adminLabel =
                new JLabel("ADMINISTRATION");

        adminLabel.setForeground(
                Theme.MUTED
        );

        adminLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        // =====================================================
        // TITLE PANEL
        // =====================================================

        JPanel titlePanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                3
                        )
                );

        titlePanel.setOpaque(false);

        titlePanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        15,
                        5,
                        0
                )
        );

        titlePanel.add(
                shopName,
                BorderLayout.NORTH
        );

        titlePanel.add(
                adminLabel,
                BorderLayout.SOUTH
        );

        header.add(
                titlePanel,
                BorderLayout.CENTER
        );

        main.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // LOGIN FORM
        // =====================================================

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        formPanel.setBackground(LIGHT);

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        70,
                        25,
                        70
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;

        gbc.gridwidth = 2;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // =====================================================
        // LOGIN TITLE
        // =====================================================

        JLabel title =
                new JLabel(
                        "Administrator Login"
                );

        title.setForeground(TEXT);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        gbc.gridy = 0;

        gbc.insets =
                new Insets(
                        0,
                        0,
                        5,
                        0
                );

        formPanel.add(
                title,
                gbc
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        JLabel subtitle =
                new JLabel(
                        "Sign in to manage the motor shop system"
                );

        subtitle.setForeground(
                Theme.GRAY_TEXT
        );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        gbc.gridy = 1;

        gbc.insets =
                new Insets(
                        0,
                        0,
                        20,
                        0
                );

        formPanel.add(
                subtitle,
                gbc
        );

        // =====================================================
        // USERNAME LABEL
        // =====================================================

        JLabel usernameLabel =
                new JLabel("USERNAME");

        usernameLabel.setForeground(TEXT);

        usernameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        gbc.gridy = 2;

        gbc.insets =
                new Insets(
                        0,
                        0,
                        5,
                        0
                );

        formPanel.add(
                usernameLabel,
                gbc
        );

        // =====================================================
        // USERNAME FIELD
        // =====================================================

        username.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        username.setPreferredSize(
                new Dimension(
                        300,
                        38
                )
        );

        username.setBackground(Color.WHITE);
        username.setForeground(Theme.BLACK);
        username.setCaretColor(Theme.BLACK);
        username.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.RED
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        gbc.gridy = 3;

        gbc.insets =
                new Insets(
                        0,
                        0,
                        15,
                        0
                );

        formPanel.add(
                username,
                gbc
        );

        // =====================================================
        // PASSWORD LABEL
        // =====================================================

        JLabel passwordLabel =
                new JLabel("PASSWORD");

        passwordLabel.setForeground(TEXT);

        passwordLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        gbc.gridy = 4;

        gbc.insets =
                new Insets(
                        0,
                        0,
                        5,
                        0
                );

        formPanel.add(
                passwordLabel,
                gbc
        );

        // =====================================================
        // PASSWORD FIELD
        // =====================================================

        password.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        password.setPreferredSize(
                new Dimension(
                        300,
                        38
                )
        );

        password.setBackground(Color.WHITE);
        password.setForeground(Theme.BLACK);
        password.setCaretColor(Theme.BLACK);
        password.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.RED
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        gbc.gridy = 5;

        gbc.insets =
                new Insets(
                        0,
                        0,
                        8,
                        0
                );

        formPanel.add(
                password,
                gbc
        );

        // =====================================================
        // SHOW PASSWORD
        // =====================================================

        JCheckBox showPassword =
                new JCheckBox(
                        "Show password"
                );

        showPassword.setBackground(LIGHT);

        showPassword.setForeground(
                Theme.GRAY_TEXT
        );

        showPassword.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        showPassword.addActionListener(
                e -> {

                    if (showPassword.isSelected()) {

                        password.setEchoChar(
                                (char) 0
                        );

                    } else {

                        password.setEchoChar(
                                '•'
                        );
                    }
                }
        );

        gbc.gridy = 6;

        gbc.insets =
                new Insets(
                        0,
                        0,
                        20,
                        0
                );

        formPanel.add(
                showPassword,
                gbc
        );

        // =====================================================
        // LOGIN BUTTON
        // =====================================================

        JButton login =
                new JButton("LOGIN");

        login.setPreferredSize(
                new Dimension(
                        300,
                        45
                )
        );

        login.setBackground(RED);

        login.setForeground(Color.WHITE);

        login.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        login.setFocusPainted(false);

        login.setBorder(
                BorderFactory.createEmptyBorder()
        );

        login.addActionListener(
                e -> doLogin()
        );

        password.addActionListener(
                e -> doLogin()
        );

        gbc.gridy = 7;

        gbc.insets =
                new Insets(
                        0,
                        0,
                        0,
                        0
                );

        formPanel.add(
                login,
                gbc
        );

        main.add(
                formPanel,
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
                        10,
                        20,
                        10,
                        20
                )
        );

        JLabel footerText =
                new JLabel(
                        "3'S Motor Shop Management System",
                        JLabel.CENTER
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
                BorderLayout.CENTER
        );

        main.add(
                footer,
                BorderLayout.SOUTH
        );

        // =====================================================
        // SET MAIN CONTENT
        // =====================================================

        setContentPane(main);
    }

    // =========================================================
    // LOGIN FUNCTION
    // =========================================================

    private void doLogin() {

        String u =
                username.getText().trim();

        String p =
                new String(
                        password.getPassword()
                );

        if (u.isEmpty() || p.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your username and password.",
                    "Login Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            AdminAuth.ensureDefaultAdmin();

            String fullName =
                    AdminAuth.login(
                            u,
                            p
                    );

            if (fullName == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid administrator credentials.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                password.setText("");

                password.requestFocus();

                return;
            }

            new AdminDashboard(
                    fullName,
                    this
            ).setVisible(true);

            setVisible(false);

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database connection failed:\n"
                    + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {
        Theme.install();

        SwingUtilities.invokeLater(
                () -> new AdminLogin().setVisible(true)
        );
    }
}