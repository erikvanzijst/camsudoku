package cx.prutser.capture;

import com.digiburo.backprop1.BackProp;
import com.digiburo.backprop1.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * OCR engine for recognizing the digits [1-9] and the blank tile in a sudoku.
 *
 * @author Erik van Zijst
 */
public class SudokuDigitRecognizer {

    private static final double one = 0.9999999999;
    private static final double zero = 0.0000000001;

    private static final int WIDTH = 48;
    private static final int HEIGHT = 48;
    private static final int hiddenLayer = 100;
    private static final float learningRate = 0.45F;
    private static final float momentum = 0.2F;
    private static final double[][] outputPattern = new double[10][10];

    private final BackProp backProp;

    static {
        for (int i = 0; i < outputPattern.length; i++) {
            Arrays.fill(outputPattern[i], zero);
            outputPattern[i][i] = one;
        }
    }

    /**
     * Creates a new, unconfigured digit recognizer that has to be trained first.
     */
    public SudokuDigitRecognizer() {
        backProp = new BackProp(WIDTH * HEIGHT, hiddenLayer, 10, learningRate, momentum);
    }

    /**
     * Creates a new instance of this digit recognizer, initialized with the
     * network configuration stored in the specified file.
     *
     * @param file
     * @throws IOException  when the network configuration could not be read.
     */
    public SudokuDigitRecognizer(File file) throws IOException {
        try {
            backProp = new BackProp(file);
        } catch(ClassNotFoundException e) {
            throw new IOException("The network configuration file could not parsed.", e);
        }
    }

    public boolean trainAndClassifyResult(int expectedDigit, double[] pixels) throws IllegalArgumentException {

        double[] result = train(expectedDigit, pixels);
        for (int i = 0; i < result.length; i++) {
            if (round1(result[i]) != round2(outputPattern[expectedDigit][i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Trains the network on one tile image.
     *
     * @param pixels    the 8-bit gray scale pixel data (must be between 0 and 1).
     * @param expectedDigit the expected outcome ([0-9]).
     * @throws IllegalArgumentException when the pixel data is not 48x48 wide,
     * or the expectedDigit is out of range.
     * @return <code>true</code> if the image was successfully recognized,
     * <code>false</code> if not.
     */
    public double[] train(int expectedDigit, double[] pixels) throws IllegalArgumentException {

        if (expectedDigit < 0 || expectedDigit > 9 || pixels == null || pixels.length != WIDTH * HEIGHT) {
            throw new IllegalArgumentException("Input out of range.");

        } else {
            backProp.setInputPattern(pixels);
            backProp.runNetwork();
            backProp.trainNetwork(new Pattern(pixels, outputPattern[expectedDigit]));
            return backProp.getOutputPattern();
        }
    }


    /**
     * Map an answer from the network to a value suitable for truth comparison
     *
     * @param candidate value from network
     * @return value for comparison w/truth
     */
    private int round1(double candidate) {
        if (candidate > 0.85) {
            return (1);
        } else if (candidate < 0.15) {
            return (0);
        }

        return (-1);
    }

    /**
     * Map a truth value to a value suitable for comparison
     *
     * @param candidate value from truth pattern
     * @return value for comparison w/truth
     */
    private int round2(double candidate) {
        if (candidate > 0.5) {
            return (1);
        }

        return (0);
    }

    /**
     * Writes the current network configuration to the specified file.
     *
     * @param file
     * @throws IOException
     */
    public void save(File file) throws IOException {
        backProp.writer(file);
    }
}
