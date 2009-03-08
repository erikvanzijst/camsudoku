package cx.prutser.sudoku.ocr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 */
public class MassTester {
    private List<String> files = null;

    public MassTester(String... args) {
        parseArgs(args);
    }

    private void run() {
        for (String filename : files) {
            try {
                BufferedImage bi = ImageIO.read(new File(filename));
                Pair<Integer, Integer> pos = OCRUtils.centerOfMass(OCRUtils.getPixels(bi), bi.getWidth(), bi.getHeight(), true);

                System.out.println(filename + ": " + pos);
            } catch (IOException e) {
                System.err.println("Error reading file " + filename + ": " + e.getMessage());
            }
        }
    }

    public static void main(String... args) {
        new MassTester(args).run();
    }

    private void parseArgs(String... args) {

        final String usage = "Usage: java " + getClass().getName() + " [OPTIONS] file1 file2...\n" +
                "\n" +
                "Tester application for the OCR engine. Takes a list of images and tries\n" +
                "to recognize their digit value.\n" +
                "Images must be 8-bit gray scale in 16x16 resolution and png format.\n" +
                "\n" +
                "OPTIONS\n" +
                "   -h, --help  print this help message and exit.";

        boolean exit = false;
        try {
            for (int i = 0; !exit && i < args.length; i++) {
                if("-h".equals(args[i]) || "--help".equals(args[i])) {
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
