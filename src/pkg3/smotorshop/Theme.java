package pkg3.smotorshop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import javax.swing.table.JTableHeader;

/**
 * 3'S Motor Shop theme: PURE BLACK and RED (with white text).
 * One place for every color so the whole system looks the same.
 */
public final class Theme {

    private Theme() { }

    public static final Color BLACK     = new Color(0, 0, 0);
    public static final Color RED       = new Color(229, 9, 20);
    public static final Color RED_DARK  = new Color(160, 0, 8);
    public static final Color RED_SOFT  = new Color(90, 0, 4);   // selected rows
    public static final Color WHITE     = Color.WHITE;
    public static final Color MUTED     = new Color(200, 200, 200);   // light text on black
    public static final Color GRAY_TEXT = new Color(90, 95, 100);     // secondary text on white
    public static final Color GRID      = new Color(220, 223, 227);
    public static final Color SEL_ROW   = new Color(255, 224, 224);   // selected table row

    public static final String FONT = "Segoe UI";

    public static Border redLine() {
        return BorderFactory.createLineBorder(RED);
    }

    public static Border redLine(int pad) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RED),
                BorderFactory.createEmptyBorder(pad, pad + 4, pad, pad + 4));
    }

    /** Call BEFORE creating any window (and before setLookAndFeel). */
    public static void install() {
        try {
            UIManager.put("control", WHITE);
            UIManager.put("nimbusBase", BLACK);
            UIManager.put("nimbusBlueGrey", new Color(40, 0, 0));
            UIManager.put("nimbusLightBackground", WHITE);
            UIManager.put("nimbusFocus", RED);
            UIManager.put("nimbusSelectionBackground", RED);
            UIManager.put("nimbusSelectedText", WHITE);
            UIManager.put("nimbusOrange", RED);
            UIManager.put("text", BLACK);
            UIManager.put("info", WHITE);
            UIManager.put("infoText", BLACK);
            UIManager.put("menu", WHITE);
            UIManager.put("menuText", BLACK);
            UIManager.put("textForeground", BLACK);
            UIManager.put("textBackground", WHITE);
            UIManager.put("textHighlight", RED);
            UIManager.put("textHighlightText", WHITE);
            UIManager.put("TabbedPane.selected", RED);
            UIManager.put("Table.selectionBackground", SEL_ROW);
            UIManager.put("Table.selectionForeground", BLACK);
            UIManager.put("Table.alternateRowColor", WHITE);
            UIManager.put("OptionPane.background", WHITE);
            UIManager.put("Panel.background", WHITE);
            UIManager.put("OptionPane.messageForeground", BLACK);
            UIManager.put("ComboBox.selectionBackground", RED);
            UIManager.put("ComboBox.selectionForeground", WHITE);
            UIManager.put("nimbusBase", BLACK);
        } catch (Exception ignored) {
            // theme is cosmetic; never block the app
        }
    }

    /** Paint a whole window tree black/red. */
    public static void apply(Component c) {
        if (c instanceof JPanel) {
            Color old = c.getBackground();
            boolean wasBlack = old != null && old.getRed() < 40 && old.getGreen() < 40 && old.getBlue() < 40;
            c.setBackground(wasBlack ? BLACK : WHITE);
        }
        if (c instanceof JLabel) {
            JLabel l = (JLabel) c;
            Color old = l.getForeground();
            boolean wasRed = old != null && old.getRed() > 150 && old.getGreen() < 90 && old.getBlue() < 90;
            Color parentBg = l.getParent() != null ? l.getParent().getBackground() : WHITE;
            boolean onBlack = parentBg != null && parentBg.getRed() < 100 && parentBg.getGreen() < 100;
            l.setForeground(wasRed ? RED : (onBlack ? WHITE : BLACK));
        }
        if (c instanceof JButton) {
            JButton b = (JButton) c;
            b.setBackground(RED);
            b.setForeground(WHITE);
            b.setFont(new Font(FONT, Font.BOLD, 12));
            b.setFocusPainted(false);
            b.setOpaque(true);
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(RED),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        } else if (c instanceof JCheckBox || c instanceof JRadioButton) {
            c.setBackground(c.getParent() != null ? c.getParent().getBackground() : WHITE);
            Color pb = c.getBackground();
            c.setForeground(pb != null && pb.getRed() < 100 ? WHITE : BLACK);
        } else if (c instanceof JTextComponent) {
            JTextComponent t = (JTextComponent) c;
            t.setBackground(WHITE);
            t.setForeground(BLACK);
            t.setCaretColor(BLACK);
            t.setSelectionColor(RED);
            t.setSelectedTextColor(WHITE);
            if (c instanceof JTextArea) {
                t.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            } else {
                t.setBorder(redLine(3));
            }
        } else if (c instanceof JComboBox) {
            c.setBackground(WHITE);
            c.setForeground(BLACK);
        } else if (c instanceof JSpinner) {
            c.setBackground(WHITE);
            c.setForeground(BLACK);
        } else if (c instanceof JTable) {
            JTable t = (JTable) c;
            t.setBackground(WHITE);
            t.setForeground(BLACK);
            t.setGridColor(GRID);
            t.setSelectionBackground(SEL_ROW);
            t.setSelectionForeground(BLACK);
            t.setRowHeight(26);
            t.setShowGrid(true);
            JTableHeader h = t.getTableHeader();
            if (h != null) {
                h.setBackground(RED);
                h.setForeground(WHITE);
                h.setFont(new Font(FONT, Font.BOLD, 12));
                h.setOpaque(true);
            }
        } else if (c instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) c;
            sp.setBackground(WHITE);
            sp.getViewport().setBackground(WHITE);
            sp.setBorder(BorderFactory.createLineBorder(RED));
        } else if (c instanceof JTabbedPane) {
            JTabbedPane tp = (JTabbedPane) c;
            tp.setBackground(WHITE);
            tp.setForeground(WHITE);
            tp.setFont(new Font(FONT, Font.BOLD, 13));
            tp.setOpaque(true);
            tp.setUI(new BlackRedTabsUI());
        }
        if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) {
                apply(child);
            }
        }
    }

    /** Flat tabs: red when selected, pure black otherwise, thin red outline. */
    private static final class BlackRedTabsUI extends javax.swing.plaf.basic.BasicTabbedPaneUI {
        @Override protected void installDefaults() {
            super.installDefaults();
            tabAreaInsets = new java.awt.Insets(4, 2, 0, 2);
            contentBorderInsets = new java.awt.Insets(2, 0, 0, 0);
            selectedTabPadInsets = new java.awt.Insets(0, 0, 0, 0);
        }
        @Override protected void paintTabBackground(java.awt.Graphics g, int tabPlacement, int tabIndex,
                int x, int y, int w, int h, boolean isSelected) {
            g.setColor(isSelected ? RED : BLACK);
            g.fillRect(x, y, w, h);
        }
        @Override protected void paintTabBorder(java.awt.Graphics g, int tabPlacement, int tabIndex,
                int x, int y, int w, int h, boolean isSelected) {
            g.setColor(RED);
            g.drawRect(x, y, w - 1, h - 1);
        }
        @Override protected void paintFocusIndicator(java.awt.Graphics g, int tabPlacement,
                java.awt.Rectangle[] rects, int tabIndex, java.awt.Rectangle iconRect,
                java.awt.Rectangle textRect, boolean isSelected) { }
        @Override protected void paintContentBorder(java.awt.Graphics g, int tabPlacement, int selectedIndex) {
            java.awt.Insets i = tabPane.getInsets();
            int y = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight) + i.top;
            g.setColor(RED);
            g.fillRect(i.left, y, tabPane.getWidth() - i.left - i.right, 2);
        }
        @Override protected java.awt.Insets getTabInsets(int tabPlacement, int tabIndex) {
            return new java.awt.Insets(6, 14, 6, 14);
        }
    }
}
