package cx.prutser.sudoku.ocr;

import com.digiburo.backprop1.BackProp;
import com.digiburo.backprop1.Pattern;

import java.util.Arrays;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * OCR engine for recognizing the uppercase letters [A-Z].
 *
 * @author Erik van Zijst
 */
public class CharacterRecognizer {

    public static final char UNRECOGNIZED = '?';
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final char[] CHARACTERS = ALPHABET.toCharArray();

    private static final double one = 0.9999999999D;
    private static final double zero = 0.0000000001D;

    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    private static final int hiddenLayer = 128;
    private static final float learningRate = 0.25F;    // was 0.25
    private static final float momentum = 0.30F;    // was 0.75
    private static final double[][] outputPattern = new double[26][26];

    private final BackProp backProp;

    static {
        for (int i = 0; i < outputPattern.length; i++) {
            Arrays.fill(outputPattern[i], zero);
            outputPattern[i][i] = one;
        }
    }

    /**
     * Creates a new, unconfigured letter recognizer that has to be trained first.
     */
    public CharacterRecognizer() {
        backProp = new BackProp(WIDTH * HEIGHT, hiddenLayer, 26, learningRate, momentum);
    }

    /**
     * Creates a new instance of this letter recognizer, initialized with the
     * network configuration stored in the specified file.
     *
     * @param in
     * @throws java.io.IOException  when the network configuration could not be read.
     */
    public CharacterRecognizer(InputStream in) throws IOException {
        try {
            backProp = new BackProp(in);
        } catch(ClassNotFoundException e) {
            throw new IOException("The network configuration file could not parsed.", e);
        }
    }

    public float getLearningRate() {
        return learningRate;
    }

    public float getMomentum() {
        return momentum;
    }

    /**
     *
     * @param pixels
     * @return  the successfully recognized character ([A-Z]), or '?'
     * when the image could not be recognized.
     */
    public char testAndClassify(double[] pixels) {
        return classifyResult(test(pixels));
    }

    public double[] test(double[] pixels) {

        if (pixels.length != WIDTH * HEIGHT) {
            throw new IllegalArgumentException("Unsupported tile size: " + pixels.length);

        } else {
            backProp.setInputPattern(pixels);
            backProp.runNetwork();
            return backProp.getOutputPattern();
        }
    }

    protected char classifyResult(double[] result) {

        if (result.length != ALPHABET.length()) {
            throw new IllegalArgumentException("Invalid array length: " + result.length);

        } else {
            for (int i = 0; i < outputPattern.length; i++) {

                boolean recognized = true;
                for (int j = 0; j < result.length; j++) {
                    if (round2(result[j]) != round2(outputPattern[i][j])) {
                        recognized = false;
                        break;
                    }
                }
                if (recognized) {
                    return CHARACTERS[i];
                }
            }
            return UNRECOGNIZED;
        }
    }

    public boolean trainAndClassifyResult(char expectedChar, double[] pixels)
            throws IllegalArgumentException {

        final double[] expectedPattern = outputPattern[ALPHABET.indexOf(expectedChar)];
        double[] result = train(expectedChar, pixels);
        for (int i = 0; i < result.length; i++) {
            if (round1(result[i]) != round2(expectedPattern[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Trains the network on one tile image.
     *
     * @param pixels    the 8-bit gray scale pixel data (must be between 0 and 1).
     * @param expectedChar the expected outcome ([A-Z]).
     * @throws IllegalArgumentException when the pixel data is not 16x16 wide,
     * or the expectedChar is invalid.
     * @return <code>true</code> if the image was successfully recognized,
     * <code>false</code> if not.
     */
    public double[] train(char expectedChar, double[] pixels) throws IllegalArgumentException {

        if (ALPHABET.indexOf(expectedChar) < 0 || pixels == null || pixels.length != WIDTH * HEIGHT) {
            throw new IllegalArgumentException("Input out of range.");

        } else {
            backProp.setInputPattern(pixels);
            backProp.runNetwork();
            backProp.trainNetwork(new Pattern(pixels, outputPattern[ALPHABET.indexOf(expectedChar)]));
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
        if (candidate > 0.85D) {
            return 1;
        } else if (candidate < 0.15D) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Map a truth value to a value suitable for comparison
     *
     * @param candidate value from truth pattern
     * @return value for comparison w/truth
     */
    private int round2(double candidate) {
        if (candidate >= 0.5D) {
            return 1;
        }

        return 0;
    }

    /**
     * Writes the current network configuration to the specified stream.
     *
     * @param out
     * @throws IOException
     */
    public void save(OutputStream out) throws IOException {
        backProp.writer(out);
    }
}
