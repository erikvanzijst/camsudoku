package cx.prutser.sudoku.capture;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Erik van Zijst
 */
public class CaptureUtils {

    public static BufferedImage createBufferedImage(Image imageIn, int targetColor) {

        BufferedImage bufferedImageOut = new BufferedImage(imageIn.getWidth(null), imageIn.getHeight(null), targetColor);
        Graphics g = bufferedImageOut.getGraphics();
        g.drawImage(imageIn, 0, 0, null);
        g.dispose();
        return bufferedImageOut;
    }
}
