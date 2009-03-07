package cx.prutser.ocr;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Erik van Zijst
 */
public class Trainer {

    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    private String dir = ".";
    private String filename = "config.net";

    private long evals = 0L;
    private long start = System.currentTimeMillis();
    private SudokuDigitRecognizer engine;

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
                this.input = OCRUtils.pixelsToPattern(pixels);
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
        File f = new File(filename);
        if (f.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(f);
                engine = new SudokuDigitRecognizer(in);
            } catch (IOException e) {
                System.err.println(String.format(
                        "Error reading initial network configuration (%s): %s",
                        f.getAbsolutePath(), e.getMessage()));
                System.exit(1);
            } finally {
                try {
                    in.close();
                } catch(IOException e) {}
            }
        }
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
                        testValues.add(new TestValue(Integer.parseInt(dir), OCRUtils.getPixels(ImageIO.read(file)), file));
                    } catch(IllegalArgumentException iae) {
                        System.err.println("Error processing: " + file.getPath() + ": " + iae.getMessage());
                    } catch(IOException ioe) {
                        System.err.println("Error reading " + file.getPath() + ": " + ioe.getMessage());
                    }
                }
                System.out.println(String.format("Loaded %d images of \"%s\"", files.length, dir));
            }

            if (testValues.isEmpty()) {
                System.out.println("No training images found.");

            } else {
                System.out.println(String.format(
                        "Training with learning rate %.3f and momentum %.3f.",
                        engine.getLearningRate(), engine.getMomentum()));

                boolean exit = false;
                final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                while (!exit) {
                    try {
                        if (stdin.ready()) {
                            stdin.readLine();
                            while (true) {
                                System.out.print(
                                    "Interrupt program and save current state?\n" +
                                    "Y (save and stop), K (stop), S (save and resume), enter (resume) : ");
                                String str = stdin.readLine();

                                if ("Y".equalsIgnoreCase(str)) {
                                    doSave();
                                    exit = true;
                                    break;
                                } else if ("K".equalsIgnoreCase(str)) {
                                    exit = true;
                                    break;
                                } else if ("S".equalsIgnoreCase(str)) {
                                    doSave();
                                    break;
                                } else if (str.isEmpty()) {
                                    break;
                                }
                            }
                        } else {
                            final int success = doRun(testValues);
                            if (success == testValues.size()) {
                                doLog(testValues, success);
                                doSave();
                                exit = true;
                            } else {

                                long now = System.currentTimeMillis();
                                if (now - start > 1000L) {
                                    doLog(testValues, success);
                                    start = now;
                                }
                            }
                        }
                    } catch(IOException e) {
                    }
                }
            }
        }
    }

    private int doRun(List<TestValue> testValues) {
        int success = 0;
        for (TestValue testValue : testValues) {
            if (engine.trainAndClassifyResult(testValue.getExpectedDigit(), testValue.getInput())) {
                success++;
                testValue.successCount++;
            }
            evals++;
        }
        return success;
    }

    private void doSave() {

        final File f = new File(filename);
        OutputStream out = null;
        try {
            engine.save(out = new FileOutputStream(f));
            System.out.println("Network configuration saved to " + f.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Unable to save the network configuration to " + f.getAbsolutePath());
        } finally {
            try {
                out.close();
            } catch (IOException e) {}
        }
    }

    private void doLog(List<TestValue> testValues, int success) {

        System.out.println(String.format("Recognized %d of %d images (%.2f%%, %d evals). Hardest image: %s",
                success, testValues.size(), (success / (float)testValues.size()) * 100,
                evals, getHardestImages(testValues, 1).get(0).getPath()));
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
                "gray scale in 16x16 resolution and png format.\n" +
                "\n" +
                "OPTIONS\n" +
                "   -d, --dir   directory containing the tile images (defaults to .)\n" +
                "   -f, --file  save learned network state to file (defaults to config.net)\n" +
                "               if this file exists at startup, it is used at initialization.\n" +
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
