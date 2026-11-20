package pkg3.smotorshop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/** Lightweight Code 39 barcode generator; no external barcode library is required. */
public final class BarcodeGenerator {
    private static final String[] CHARS = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z", "-", ".", " ", "$", "/", "+", "%"
    };

    private static final String[] PATTERNS = {
        "nnnwwnwnn","wnnwnnnnw","nnwwnnnnw","wnwwnnnnn","nnnwwnnnw","wnnwwnnnn","nnwwwnnnn","nnnwnnwnw","wnnwnnwnn","nnwwnnwnn",
        "wnnnnwnnw","nnwnnwnnw","wnwnnwnnn","nnnnwwnnw","wnnnwwnnn","nnwnwwnnn","nnnnnwwnw","wnnnnwwnn","nnwnnwwnn","nnnnwwwnn",
        "wnnnnnnww","nnwnnnnww","wnwnnnnwn","nnnnwnnww","wnnnwnnwn","nnwnwnnwn","nnnnnnwww","wnnnnnwwn","nnwnnnwwn","nnnnwnwwn",
        "wwnnnnnnw","nwwnnnnnw","wwwnnnnnn","nwnnwnnnw","wwnnwnnnn","nwwnwnnnn","nwnnnnwnw","wwnnnnwnn","nwwnnnwnn","nwwnnnnwn",
        "nwnwnnnnw","wwnnnwnnn","nwwnwnnnn","nwnnwwnnn","wwnnwwnnn","nwwnwwnnn","nwnnnnwwn","wwnnnnwwn","nwwnnnwwn","nwnnwnwwn"
    };

    private BarcodeGenerator() {}

    public static BufferedImage create(String value, int moduleWidth, int height) {
        String data = normalize(value);
        String encoded = "*" + data + "*";
        int quiet = 10;
        int width = quiet * 2;
        for (int i = 0; i < encoded.length(); i++) {
            String p = patternFor(encoded.charAt(i));
            for (int j = 0; j < p.length(); j++) width += (p.charAt(j) == 'w' ? 3 : 1) * moduleWidth;
            if (i < encoded.length() - 1) width += moduleWidth;
        }
        width += 1;

        BufferedImage image = new BufferedImage(width, height + 28, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height + 28);
        int x = quiet;
        g.setColor(Color.BLACK);
        for (int i = 0; i < encoded.length(); i++) {
            String p = patternFor(encoded.charAt(i));
            boolean black = true;
            for (int j = 0; j < p.length(); j++) {
                int w = (p.charAt(j) == 'w' ? 3 : 1) * moduleWidth;
                if (black) g.fillRect(x, 5, w, height);
                x += w;
                black = !black;
            }
            if (i < encoded.length() - 1) x += moduleWidth;
        }
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(14f));
        int textWidth = g.getFontMetrics().stringWidth(data);
        g.drawString(data, Math.max(2, (width - textWidth) / 2), height + 20);
        g.dispose();
        return image;
    }

    public static File save(String value) throws IOException {
        File dir = new File("barcodes");
        if (!dir.exists() && !dir.mkdirs()) throw new IOException("Cannot create barcode folder.");
        File file = new File(dir, sanitize(value) + ".png");
        ImageIO.write(create(value, 2, 70), "png", file);
        return file;
    }

    private static String normalize(String value) {
        String s = value == null ? "" : value.trim().toUpperCase();
        if (s.isEmpty()) throw new IllegalArgumentException("Barcode cannot be empty.");
        for (int i = 0; i < s.length(); i++) {
            if ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%".indexOf(s.charAt(i)) < 0) {
                throw new IllegalArgumentException("Code 39 supports only A-Z, 0-9, -, ., space, $, /, + and %.");
            }
        }
        return s;
    }

    private static String patternFor(char c) {
        if (c == '*') return "nwnnwnwnn";
        String s = String.valueOf(c);
        for (int i = 0; i < CHARS.length; i++) if (CHARS[i].equals(s)) return PATTERNS[i];
        throw new IllegalArgumentException("Unsupported barcode character: " + c);
    }

    private static String sanitize(String value) { return value.replaceAll("[^A-Za-z0-9._-]", "_"); }
}
