package cx.prutser.sudoku.capture;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;
import javax.media.format.RGBFormat;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Vector;

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

    public static CaptureDeviceInfo getDeviceInfo() {

        String locator = null;
        CaptureDeviceInfo device = null;

        Vector devices = CaptureDeviceManager.getDeviceList(null);

        for(Iterator it = devices.iterator(); it.hasNext();) {
            CaptureDeviceInfo di = (CaptureDeviceInfo)it.next();
            if (locator != null && locator.equals(di.getLocator().toString())) {
                device = di;
                break;
            } else {
                if (findMatchingFormat(di.getFormats(), new Dimension(640, 480)) != null) {
                    device = di;
                }
            }
        }
        return device;
    }

    /**
     * Returns the first {@link javax.media.format.RGBFormat} that matches the
     * given resolution.
     *
     * @param formats
     * @param size
     * @return
     */
    public static RGBFormat findMatchingFormat(Format[] formats, Dimension size) {

        for (Format f : formats) {
            if (f instanceof RGBFormat) {
                RGBFormat rgb = (RGBFormat)f;
                if (rgb.getSize().equals(new Dimension(640, 480))) {
                    return rgb;
                }
            }
        }
        return null;
    }
}
