package cx.prutser.capture;

import com.digiburo.backprop1.Pattern;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * @author Erik van Zijst
 */
public class Trainer {

    private static final int COLOR_DEPTH = 8;
    private static final int WIDTH = 48;
    private static final int HEIGHT = 48;
    private String dir = ".";

    public Trainer(String... args) {
        parseArgs(args);
    }

    private void train() {

        final File baseDir = new File(dir);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.err.println(dir + " is not a directory.");

        } else {
            final String[] dirs = baseDir.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return new File(dir, name).isDirectory() &&
                            name.length() == 1 &&
                            "0123456789".contains(name);
                }
            });
            for (String dir : dirs) {
                final File[] files = new File(baseDir, dir).listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".png");
                    }
                });
                for (File file : files) {
                    try {
                        Pattern pattern = createPattern(ImageIO.read(file));
                    } catch(IllegalArgumentException iae) {
                        System.err.println(file.getPath() + ": " + iae.getMessage());
                    } catch(IOException ioe) {
                        System.err.println("Error reading " + file.getPath() + ": " + ioe.getMessage());
                    }
                }
            }
        }
    }

    private Pattern createPattern(BufferedImage image) {

        if (image.getWidth() != WIDTH || image.getHeight() != HEIGHT ||
                image.getColorModel().getPixelSize() != COLOR_DEPTH) {
            throw new IllegalArgumentException(String.format(
                    "Invalid image. Must be %dx%d resolution in %d bit gray scale",
                    WIDTH, HEIGHT, COLOR_DEPTH));
        } else {

        }
        return null;
    }

    public static void main(String... args) {
        new Trainer(args).train();
    }

    private void parseArgs(String... args) {

        final String usage = "Usage: java " + getClass().getName() + " [OPTIONS]\n" +
                "\n" +
                "Trains the neural network to recognize the sudoku digits 1-9 and the blank tile\n" +
                "using the tile images from a specified directory. The tile images must be 8-bit\n" +
                "gray scale in 48x48 resolution and png format.\n" +
                "\n" +
                "OPTIONS\n" +
                "   -d, --dir   directory containing the tile images (defaults to .)\n" +
                "   -h, --help  print this help message and exit.";

        boolean exit = false;
        try {
            for (int i = 0; !exit && i < args.length; i++) {
                if ("-d".equals(args[i]) || "--dir".equals(args[i])) {
                    dir = args[++i];
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
