package cx.prutser.sudoku.ocr;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * @author Erik van Zijst
 */
public class OCRUtils {

    public static BufferedImage toGrayScale(BufferedImage image) {
        BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = gray.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return gray;
    }

    /**
     * Normalizes the 8-bit pixel values to [0-1] doubles.
     *
     * @param pixels
     * @return
     */
    public static double[] pixelsToPattern(byte[] pixels) {

        final double[] pattern = new double[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            pattern[i] = (((int)pixels[i]) & 0xFF) / 256D;
        }
        return pattern;
    }

    public static String patternToString(double[] pattern) {
        StringBuilder buf = new StringBuilder();
        for (double p : pattern) {
            buf.append(String.format("%3d ", (int)(p * 100)));
        }
        return buf.toString();
    }

    public static String pixelsToString(byte[] pixels) {
        StringBuilder buf = new StringBuilder();
        for (byte b : pixels) {
            buf.append(b >= 0 ? "." : "#");
        }
        return buf.toString();
    }

    public static byte[] getPixels(BufferedImage image) {

        if (image.getColorModel().getPixelSize() != 8) {
            throw new IllegalArgumentException("Color must be 8 bit gray scale.");
        } else {
            DataBufferByte buffer = (DataBufferByte)image.getRaster().getDataBuffer();
            return buffer.getData();
        }
    }

    /**
     * Returns the "center of gravity" for the specified image.
     *
     * @param pixels
     * @param width
     * @param height
     * @param invert    whether or not to invert the unsigned grey scale values.
     * @return  the coordinates of the pixel closest to the center of gravity.
     */
    public static Pair<Integer, Integer> centerOfGravity(byte[] pixels, int width, int height, boolean invert) {

        assert width >= 0 && height >= 0 && pixels.length == width * height;

        // center of gravity:
        float x1 = 0F;
        float y1 = 0F;

        float w1 = 0F;
        for (int i = 0; i < pixels.length; i++) {
            float x2 = i % width;
            float y2 = i / width;
            int w2 = (invert ? invert(pixels[i]) : pixels[i]) & 0xFF;

            float dx = x2 - x1;
            float dy = y2 - y1;
            float ratio = (w1 + w2) == 0F ? 0F : w2 / (w1 + w2);

            x1 += dx * ratio;
            y1 += dy * ratio;
            w1 += w2;
        }
        return Pair.newInstance(Math.round(x1), Math.round(y1));
    }

    public static byte invert(byte b) {
        return (byte)~b;
    }
}
