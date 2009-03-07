package cx.prutser.capture;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Erik van Zijst
 */
public class CaptureUtils {

    public static BufferedImage createBufferedImage(Image imageIn) {

        BufferedImage bufferedImageOut = new BufferedImage(imageIn
                .getWidth(null), imageIn.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImageOut.getGraphics();
        g.drawImage(imageIn, 0, 0, null);

        return bufferedImageOut;
    }
}
