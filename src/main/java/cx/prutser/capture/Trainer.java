package cx.prutser.capture;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Erik van Zijst
 */
public class Trainer {

    private static final int WIDTH = 48;
    private static final int HEIGHT = 48;
    private String dir = ".";
    private String filename = "config.net";

    static class TestValue {

        private final double[] input;
        private final int expectedDigit;
        private final File file;
        private long successCount = 0L;

        public TestValue(int expectedDigit, byte[] pixels, File file) {

            if (expectedDigit < 0 || expectedDigit > 9 || pixels == null || pixels.length != WIDTH * HEIGHT) {
                throw new IllegalArgumentException("Pixel data out of range.");

            } else {
                this.expectedDigit = expectedDigit;
                this.file = file;
                this.input = Util.pixelsToPattern(pixels);
            }
        }

        public int getExpectedDigit() {
            return expectedDigit;
        }

        public double[] getInput() {
            return input;
        }

        public File getFile() {
            return file;
        }
    }

    public Trainer(String... args) {
        parseArgs(args);
    }

    private void train() {

        final File baseDir = new File(dir);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.err.println(dir + " is not a directory.");

        } else {
            final List<TestValue> testValues = new ArrayList<TestValue>();

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
                        testValues.add(new TestValue(Integer.parseInt(dir), Util.getPixels(ImageIO.read(file)), file));
                    } catch(IllegalArgumentException iae) {
                        System.err.println(file.getPath() + ": " + iae.getMessage());
                    } catch(IOException ioe) {
                        System.err.println("Error reading " + file.getPath() + ": " + ioe.getMessage());
                    }
                }
                System.out.println(String.format("Loaded %d images of \"%s\"", files.length, dir));
            }

            final SudokuDigitRecognizer engine = new SudokuDigitRecognizer();

            long evals = 0L;
            long start = System.currentTimeMillis();
            int success;
            do {
                success = 0;
                for (TestValue testValue : testValues) {
                    if (engine.trainAndClassifyResult(testValue.getExpectedDigit(), testValue.getInput())) {
                        success++;
                        testValue.successCount++;
                    }
                    evals++;
                }
                long now = System.currentTimeMillis();
                if (now - start > 1000L) {
                    System.out.println(String.format("Recognized %d of %d images (%2.2f%%, %d evals). Hardest image: %s",
                            success, testValues.size(), (success / (float)testValues.size()) * 100,
                            evals, getHardestImages(testValues, 1).get(0).getPath()));
                    start = now;
                }
            } while (success < testValues.size());

            final File f = new File(filename);
            try {
                engine.save(f);
                System.out.println("Network configuration saved to " + f.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Unable to save the network configuration to " + f.getAbsolutePath());
            }
        }
    }

    private List<File> getHardestImages(List<TestValue> testValues, int count) {
        List<TestValue> copy = new ArrayList<TestValue>(testValues);
        Collections.sort(copy, new Comparator<TestValue>() {
            public int compare(TestValue o1, TestValue o2) {
                return (int)(o1.successCount - o2.successCount);
            }
        });
        copy = copy.subList(0, Math.min(count, copy.size()));
        final List<File> files = new ArrayList<File>();

        for (TestValue testValue : copy) {
            files.add(testValue.getFile());
        }
        return files;
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
                "   -f, --file  save learned network state to file (defaults to config.net)\n" +
                "   -h, --help  print this help message and exit.";

        boolean exit = false;
        try {
            for (int i = 0; !exit && i < args.length; i++) {
                if ("-d".equals(args[i]) || "--dir".equals(args[i])) {
                    dir = args[++i];
                } else if("-f".equals(args[i]) || "--file".equals(args[i])) {
                    filename = args[++i];
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
