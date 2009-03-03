package cx.prutser.capture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * @author Erik van Zijst
 */
public class Util {

    public static BufferedImage createBufferedImage(Image imageIn) {

        BufferedImage bufferedImageOut = new BufferedImage(imageIn
                .getWidth(null), imageIn.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImageOut.getGraphics();
        g.drawImage(imageIn, 0, 0, null);

        return bufferedImageOut;
    }

    public static byte[] getPixels(BufferedImage image) {

        if (image.getColorModel().getPixelSize() != 8) {
            throw new IllegalArgumentException("Color must be 8 bit gray scale.");
        } else {
            DataBufferByte buffer = (DataBufferByte)image.getRaster().getDataBuffer();
            return buffer.getData();
        }
    }

    public static BufferedImage toGrayScale(BufferedImage image) {
        BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = gray.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return gray;
    }
}
