package cx.prutser.capture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.*;

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
        final int width = image.getWidth();
        final int height = image.getHeight();

        DataBufferByte buffer = (DataBufferByte)image.getRaster().getDataBuffer();
        return buffer.getData();
//        final int[] pixels = new int[width * height];
//        for(int row = 0; row < height; row++) {
//            for (int col = 0; col < width; col++) {
//                final int index = row * width + col;
//                pixels[index] = image.getRGB(row, col);
//            }
//        }
//        return pixels;
    }

    public static BufferedImage toGrayScale(BufferedImage image) {
        BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = gray.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return gray;
    }
}
