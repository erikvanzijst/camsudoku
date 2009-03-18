package cx.prutser.sudoku.ocr;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Tester application for the OCR engine. Takes a list of images and
 * tries to recognize their digit value.
 *
 * @author Erik van Zijst
 */
public class Tester {

    private String config = "config.net";
    private List<String> files = null;

    private Tester(String... args) {
        parseArgs(args);
    }

    private void run() {

        final File configFile = new File(config);
        try {
            SudokuDigitRecognizer ocr = new SudokuDigitRecognizer(new FileInputStream(configFile));

            int success = 0;
            for (String filename : files) {
                InputStream in = null;
                try {
                    in = new FileInputStream(filename);

                    final double[] pixels = OCRUtils.pixelsToPattern(OCRUtils.getPixels(ImageIO.read(in)));
                    final double[] result = ocr.test(pixels);
                    final int digit = ocr.testAndClassify(pixels);
                    final boolean recognized = digit >= 0;

                    if (recognized) {
                        success++;
                    }

                    final StringBuilder buf = new StringBuilder();
                    buf.append(String.format("%-20s ", filename))
                        .append(recognized ? String.valueOf(digit) : "Not recognized")
                        .append(" [");
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
            }
            if (!files.isEmpty()) {
                System.out.println(String.format("%.2f%% recognized (%d/%d)",
                        (success / (float)files.size()) * 100, success, files.size()));
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

        final String usage = "Usage: java " + getClass().getName() + " [OPTIONS] file1 file2...\n" +
                "\n" +
                "Tester application for the OCR engine. Takes a list of images and tries\n" +
                "to recognize their digit value.\n" +
                "Images must be 8-bit gray scale in 16x16 resolution and png format.\n" +
                "\n" +
                "OPTIONS\n" +
                "   -n, --net   network configuration file (defaults to config.net)\n" +
                "   -h, --help  print this help message and exit.";

        boolean exit = false;
        try {
            for (int i = 0; !exit && i < args.length; i++) {
                if("-n".equals(args[i]) || "--net".equals(args[i])) {
                    config = args[++i];
                } else if("-h".equals(args[i]) || "--help".equals(args[i])) {
                    exit = true;
                } else {
                    files = Arrays.asList(args).subList(i, args.length);
                    break;
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
