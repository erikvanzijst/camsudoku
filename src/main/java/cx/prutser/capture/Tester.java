package cx.prutser.capture;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tester application for the OCR engine. Takes a single image of a digit and
 * tries to recognize its value.
 *
 * @author Erik van Zijst
 */
public class Tester {

    private String config = "config.net";
    private String filename = null;

    public Tester(String... args) {
        parseArgs(args);
    }

    private void run() {

        final File configFile = new File(config);
        try {
            SudokuDigitRecognizer ocr = new SudokuDigitRecognizer(configFile);
            InputStream in = null;
            try {
                in = filename == null ? System.in : new FileInputStream(filename);

                final double[] pixels = Util.pixelsToPattern(Util.getPixels(ImageIO.read(in)));
                final double[] result = ocr.test(pixels);
                final int digit = ocr.testAndClassify(pixels);

                final StringBuilder buf = new StringBuilder(digit > 0 ? String.valueOf(digit) : "Not recognized");
                buf.append("\n")
                    .append("[");
                String sep = "";
                for (double d : result) {
                    buf.append(sep)
                        .append(String.format("%.3f", d));
                    sep = ", ";
                }
                buf.append("]");
                System.out.println(buf.toString());

            } catch (IOException e) {
                System.err.println("Error reading image: " + e.getMessage());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {}
                }
            }
        } catch (IOException e) {
            System.err.println(String.format("Error reading network configuration (%s): %s",
                    configFile.getAbsolutePath(), e.getMessage()));
        }
    }

    public static void main(String... args) {
        new Tester(args).run();
    }

    private void parseArgs(String... args) {

        final String usage = "Usage: java " + getClass().getName() + " [OPTIONS]\n" +
                "\n" +
                "Tester application for the OCR engine. Takes a single image of a digit and tries\n" +
                "to recognize it.\n" +
                "Images must be 8-bit gray scale in 16x16 resolution and png format.\n" +
                "\n" +
                "OPTIONS\n" +
                "   -n, --net   network configuration file (defaults to config.net)\n" +
                "   -f, --file  the image of a single digit (reads from stdin when omitted)\n" +
                "   -h, --help  print this help message and exit.";

        boolean exit = false;
        try {
            for (int i = 0; !exit && i < args.length; i++) {
                if ("-f".equals(args[i]) || "--file".equals(args[i])) {
                    filename = args[++i];
                } else if("-n".equals(args[i]) || "--net".equals(args[i])) {
                    config = args[++i];
                } else if("-h".equals(args[i]) || "--help".equals(args[i])) {
                    exit = true;
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            exit = true;
        }

        if (exit) {
            System.err.println(usage);
            System.exit(1);
        }
    }
}
