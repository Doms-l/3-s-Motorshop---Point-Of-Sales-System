package pkg3.smotorshop;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReceiptPDF {

    private static final DecimalFormat MONEY =
            new DecimalFormat("#,##0.00");

    public static File generate(
            String transactionNo,
            String customer,
            List<String> items,
            double total,
            double paid,
            double change) throws Exception {

        String date =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date());

        // =====================================================
        // RECEIPT FOLDER
        // =====================================================

        File folder = new File("receipts");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File pdfFile = new File(
                folder,
                transactionNo + ".pdf"
        );

        // =====================================================
        // THERMAL RECEIPT SIZE
        // =====================================================
        //
        // 80 mm = approximately 226.77 points
        //
        // Width  = 80 mm
        // Height = 300 mm
        //
        // This is much narrower than A4/A5.
        // The receipt will look like a supermarket/POS receipt.
        //

        float receiptWidth = 226.77f;
        float receiptHeight = 850f;

        com.itextpdf.text.Rectangle receiptSize =
                new com.itextpdf.text.Rectangle(
                        receiptWidth,
                        receiptHeight
                );

        Document document =
                new Document(
                        receiptSize,
                        18,
                        18,
                        18,
                        18
                );

        PdfWriter writer =
                PdfWriter.getInstance(
                        document,
                        new FileOutputStream(pdfFile)
                );

        document.open();

        // =====================================================
        // FONTS
        // =====================================================

        Font shopFont =
                new Font(
                        Font.FontFamily.HELVETICA,
                        16,
                        Font.BOLD
                );

        Font titleFont =
                new Font(
                        Font.FontFamily.HELVETICA,
                        9,
                        Font.BOLD
                );

        Font boldFont =
                new Font(
                        Font.FontFamily.HELVETICA,
                        8,
                        Font.BOLD
                );

        Font normalFont =
                new Font(
                        Font.FontFamily.HELVETICA,
                        8,
                        Font.NORMAL
                );

        Font smallFont =
                new Font(
                        Font.FontFamily.HELVETICA,
                        7,
                        Font.NORMAL
                );

        // =====================================================
        // LOGO
        // =====================================================

        try {

            java.net.URL logoURL =
                    ReceiptPDF.class.getResource(
                            "/image/Logoo.png"
                    );

            if (logoURL != null) {

                Image logo =
                        Image.getInstance(logoURL);

                // Keep logo proportional
                logo.scaleToFit(
                        75,
                        75
                );

                logo.setAlignment(
                        Element.ALIGN_CENTER
                );

                document.add(logo);
            }

        } catch (Exception e) {

            // If logo is unavailable,
            // continue generating the receipt.
        }

        // =====================================================
        // SHOP NAME
        // =====================================================

        Paragraph shopName =
                new Paragraph(
                        "3'S MOTORSHOP",
                        shopFont
                );

        shopName.setAlignment(
                Element.ALIGN_CENTER
        );

        shopName.setSpacingAfter(2);

        document.add(shopName);

        // =====================================================
        // OFFICIAL RECEIPT
        // =====================================================

        Paragraph receiptTitle =
                new Paragraph(
                        "OFFICIAL SALES RECEIPT",
                        titleFont
                );

        receiptTitle.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(receiptTitle);

        document.add(
                new Paragraph(" ", smallFont)
        );

        // =====================================================
        // TRANSACTION INFORMATION
        // =====================================================

        Paragraph transaction =
                new Paragraph(
                        "Transaction #: " + transactionNo,
                        normalFont
                );

        document.add(transaction);

        Paragraph dateParagraph =
                new Paragraph(
                        "Date: " + date,
                        normalFont
                );

        document.add(dateParagraph);

        String customerName =
                customer == null ||
                customer.trim().isEmpty()
                        ? "Walk-in Customer"
                        : customer;

        Paragraph customerParagraph =
                new Paragraph(
                        "Customer: " + customerName,
                        normalFont
                );

        document.add(customerParagraph);

        document.add(
                new Paragraph(" ", smallFont)
        );

        // =====================================================
        // LINE
        // =====================================================

        document.add(
                new Paragraph(
                        "--------------------------------",
                        smallFont
                )
        );

        // =====================================================
        // ITEMS
        // =====================================================

        Paragraph itemsTitle =
                new Paragraph(
                        "ITEMS",
                        boldFont
                );

        document.add(itemsTitle);

        document.add(
                new Paragraph(
                        "--------------------------------",
                        smallFont
                )
        );

        if (items != null && !items.isEmpty()) {

            for (String item : items) {

                Paragraph itemParagraph =
                        new Paragraph(
                                item,
                                normalFont
                        );

                itemParagraph.setLeading(10);

                document.add(itemParagraph);
            }

        } else {

            document.add(
                    new Paragraph(
                            "No items",
                            normalFont
                    )
            );
        }

        // =====================================================
        // LINE
        // =====================================================

        document.add(
                new Paragraph(
                        "--------------------------------",
                        smallFont
                )
        );

        // =====================================================
        // TOTAL
        // =====================================================

        Paragraph totalParagraph =
                new Paragraph(
                        "TOTAL: PHP "
                        + MONEY.format(total),
                        boldFont
                );

        totalParagraph.setAlignment(
                Element.ALIGN_RIGHT
        );

        document.add(totalParagraph);

        // =====================================================
        // CASH
        // =====================================================

        Paragraph paidParagraph =
                new Paragraph(
                        "CASH: PHP "
                        + MONEY.format(paid),
                        normalFont
                );

        paidParagraph.setAlignment(
                Element.ALIGN_RIGHT
        );

        document.add(paidParagraph);

        // =====================================================
        // CHANGE
        // =====================================================

        Paragraph changeParagraph =
                new Paragraph(
                        "CHANGE: PHP "
                        + MONEY.format(change),
                        normalFont
                );

        changeParagraph.setAlignment(
                Element.ALIGN_RIGHT
        );

        document.add(changeParagraph);

        document.add(
                new Paragraph(" ", smallFont)
        );

        // =====================================================
        // BARCODE
        // =====================================================

        Barcode128 barcode =
                new Barcode128();

        /*
         * The barcode contains ONLY
         * the transaction number.
         */

        barcode.setCode(transactionNo);

        barcode.setCodeType(
                Barcode128.CODE128
        );

        barcode.setBarHeight(35);

        barcode.setX(1.0f);

        barcode.setFont(null);

        Image barcodeImage =
                barcode.createImageWithBarcode(
                        writer.getDirectContent(),
                        null,
                        null
                );

        barcodeImage.scalePercent(
                75,
                75
        );

        barcodeImage.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(barcodeImage);

        // =====================================================
        // BARCODE TEXT
        // =====================================================

        Paragraph barcodeText =
                new Paragraph(
                        transactionNo,
                        smallFont
                );

        barcodeText.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(barcodeText);

        document.add(
                new Paragraph(" ", smallFont)
        );

        // =====================================================
        // THANK YOU
        // =====================================================

        Paragraph thankYou =
                new Paragraph(
                        "Thank you for choosing",
                        normalFont
                );

        thankYou.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(thankYou);

        Paragraph shopThankYou =
                new Paragraph(
                        "3'S MOTORSHOP!",
                        boldFont
                );

        shopThankYou.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(shopThankYou);

        document.add(
                new Paragraph(" ", smallFont)
        );

        Paragraph system =
                new Paragraph(
                        "Please keep this receipt for your records.",
                        smallFont
                );

        system.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(system);

        // =====================================================
        // CLOSE PDF
        // =====================================================

        document.close();

        return pdfFile;
    }

    // =========================================================
    // OPEN PDF
    // =========================================================

    public static void open(File pdfFile)
            throws Exception {

        if (pdfFile == null ||
                !pdfFile.exists()) {

            throw new Exception(
                    "Receipt PDF could not be found."
            );
        }

        if (!Desktop.isDesktopSupported()) {

            throw new Exception(
                    "Desktop PDF opening is not supported."
            );
        }

        Desktop.getDesktop().open(pdfFile);
    }
}