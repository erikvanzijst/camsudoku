package cx.prutser.sudoku.ocr;

import com.jhlabs.image.CropFilter;
import com.jhlabs.image.InvertFilter;
import cx.prutser.sudoku.capture.CaptureUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

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
     * Inverts the colours of the supplied image by subtracting every colour
     * component from 255.
     *
     * @param image
     * @return
     */
    public static BufferedImage invert(BufferedImage image, int targetColor) {
        InvertFilter filter = new InvertFilter();
        BufferedImage dest = new BufferedImage(image.getWidth(), image.getHeight(), targetColor);
        return filter.filter(image, dest);
    }

    /**
     * Crops the supplied image and returns the cropped fragment.
     *
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage crop(BufferedImage image, int targetColor, int x, int y, int width, int height) {
        CropFilter filter = new CropFilter(x, y, width, height);
        BufferedImage dest = new BufferedImage(image.getWidth(), image.getHeight(), targetColor);
        return filter.filter(image, null);
    }

    /**
     * Applies adaptive thresholding to the supplied image. This filter may not
     * produce correct results on non-gray scale images.
     *
     * @param image
     * @param size
     * @param constant
     * @return
     */
    public static BufferedImage threshold(BufferedImage image, int targetColor, int size, int constant) {

        AdapThresh filter = new AdapThresh();
        BufferedImage dest = new BufferedImage(image.getWidth(), image.getHeight(), targetColor);

        int[] pixels = new int[image.getWidth() * image.getHeight()];

        PixelGrabber grabber = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        try {
            grabber.grabPixels();
            pixels = filter.mean_thresh(pixels, image.getWidth(), image.getHeight(), size, constant);

            Toolkit toolkit = Toolkit.getDefaultToolkit();
            return CaptureUtils.createBufferedImage(
                    toolkit.createImage(
                            new MemoryImageSource(image.getWidth(), image.getHeight(),
                                    pixels, 0, image.getWidth())), targetColor);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while grabbing pixels.");
        }
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
     * Returns the "center of mass" for the specified image.
     *
     * @param pixels
     * @param width
     * @param height
     * @param invert    whether or not to invert the unsigned grey scale values.
     * @return  the coordinates of the pixel closest to the center of mass.
     */
    public static Pair<Integer, Integer> centerOfMass(
            byte[] pixels, int width, int height, boolean invert) {

        assert width >= 0 && height >= 0 && pixels.length == width * height;

        // center of mass:
        double x1 = 0D;
        double y1 = 0D;

        double w1 = 0D;
        for (int i = 0; i < pixels.length; i++) {
            double x2 = i % width;
            double y2 = i / width;
            int w2 = (invert ? invert(pixels[i]) : pixels[i]) & 0xFF;

            double dx = x2 - x1;
            double dy = y2 - y1;
            double ratio = (w1 + w2) == 0D ? 0D : (w2 / (w1 + w2));

            x1 += dx * ratio;
            y1 += dy * ratio;
            w1 += w2;
        }
        assert x1 <= width && y1 <= height :
                String.format("Center of mass out of bounds: (%d, %d)", x1, y1);
        return Pair.newInstance(Math.round((float)x1), Math.round((float)y1));
    }

    public static byte invert(byte b) {
        return (byte)~b;
    }
}
