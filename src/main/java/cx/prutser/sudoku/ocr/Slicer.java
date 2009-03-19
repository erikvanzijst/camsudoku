package cx.prutser.sudoku.ocr;

import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;

/**
 * Command line application that takes a square photo of a Target Word puzzle
 * and extract the 9 indidivual tiles and writes them to separate files.
 *
 * @author  Erik van Zijst
 */
public class Slicer {

    private String filename = null; // null for stdin
    private String dir = ".";

    public Slicer(String... args) {
        parseArgs(args);
    }

    private void run() {

        InputStream in = null;
        try {
            in = filename == null ? System.in : new FileInputStream(filename);
            final BufferedImage source = ImageIO.read(in);

            TileExtractor extractor = new LoggingTileExtractor(new WordTileExtractor(), dir);
            extractor.extractTiles(source);

        } catch (IOException ioe) {
            System.err.println("Error reading " + filename + ": " + ioe.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch(IOException e) {}
        }
    }

    public static void main(String... args) {
        new Slicer(args).run();
    }

    private void parseArgs(String... args) {

        final String usage = "Usage: java " + getClass().getName() + " [OPTIONS]\n" +
                "\n" +
                "Takes a square photo of a Target Word puzzle and extract the 9 indidivual\n" +
                "tiles and writes them to separate files.\n" +
                "\n" +
                "OPTIONS\n" +
                "   -f, --file  the image file to read (stdin when omitted)\n" +
                "   -d, --dir   destination directory for the tiles (. when omitted)\n" +
                "   -h, --help  print this help message and exit.";

        boolean exit = false;
        try {
            for (int i = 0; !exit && i < args.length; i++) {
                if("-f".equals(args[i]) || "--file".equals(args[i])) {
                    filename = args[++i];
                } else if("-d".equals(args[i]) || "--dir".equals(args[i])) {
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
