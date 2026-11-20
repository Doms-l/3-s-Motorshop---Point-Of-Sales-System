package pkg3.smotorshop;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/** Plain Java receipt printer; works without iText/JasperReports dependencies. */
public final class ReceiptPrinter {
    private ReceiptPrinter() {}

    public static boolean print(String transactionNo, String customer, List<String> lines,
                                double total, double paid, double change) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("3'S Motor Shop Receipt - " + transactionNo);
        job.setPrintable(new ReceiptPrintable(transactionNo, customer, lines, total, paid, change));
        if (!job.printDialog()) return false;
        try { job.print(); return true; }
        catch (PrinterException ex) { throw new IllegalStateException("Unable to print receipt: " + ex.getMessage(), ex); }
    }

    private static final class ReceiptPrintable implements Printable {
        private final String transactionNo, customer;
        private final List<String> lines;
        private final double total, paid, change;
        ReceiptPrintable(String transactionNo, String customer, List<String> lines, double total, double paid, double change) {
            this.transactionNo = transactionNo; this.customer = customer; this.lines = lines;
            this.total = total; this.paid = paid; this.change = change;
        }
        @Override public int print(Graphics graphics, PageFormat pf, int pageIndex) {
            if (pageIndex > 0) return NO_SUCH_PAGE;
            Graphics2D g = (Graphics2D) graphics;
            g.translate(pf.getImageableX(), pf.getImageableY());
            g.setFont(new Font("Monospaced", Font.BOLD, 12));
            int y = 18;
            g.drawString("3'S MOTOR SHOP", 10, y); y += 16;
            g.setFont(new Font("Monospaced", Font.PLAIN, 9));
            g.drawString("OFFICIAL SALES RECEIPT", 10, y); y += 14;
            g.drawString("Transaction: " + transactionNo, 10, y); y += 14;
            g.drawString("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), 10, y); y += 14;
            g.drawString("Customer: " + (customer == null || customer.isEmpty() ? "Walk-in" : customer), 10, y); y += 18;
            for (String line : lines) { g.drawString(line, 10, y); y += 13; }
            y += 5;
            g.drawString("--------------------------------", 10, y); y += 14;
            g.setFont(new Font("Monospaced", Font.BOLD, 10));
            g.drawString(String.format("TOTAL : %10.2f", total), 10, y); y += 14;
            g.setFont(new Font("Monospaced", Font.PLAIN, 9));
            g.drawString(String.format("PAID  : %10.2f", paid), 10, y); y += 14;
            g.drawString(String.format("CHANGE: %10.2f", change), 10, y); y += 22;
            g.drawString("Thank you for your purchase!", 10, y);
            return PAGE_EXISTS;
        }
    }
}
