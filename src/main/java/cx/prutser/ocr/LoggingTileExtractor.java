package cx.prutser.ocr;

import cx.prutser.capture.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Erik van Zijst
 */
public class LoggingTileExtractor implements TileExtractor {

    private final TileExtractor tileExtractor;

    public LoggingTileExtractor(TileExtractor tileExtractor, String path) {
        this.tileExtractor = tileExtractor;
        long sequence = System.currentTimeMillis();
        
        for (BufferedImage image : getTiles()) {
            File file = new File(String.format("%s/%d.png",path, sequence++));
            try {
                byte[] pixels = Util.getPixels(image);
//                ColorModel cm = image.getColorModel();
//                int ps = cm.getPixelSize();
//                int r = cm.getRed(pixels[0]);
//                int g = cm.getGreen(pixels[0]);
//                int b = cm.getBlue(pixels[0]);
                ImageIO.write(image, "png", file);
                System.out.println("Writing tile " + file.getPath() + "...");
            } catch(IOException e) {
                System.err.println("Error writing tile " + file.getPath() + ": " + e.getMessage());
            }
        }
    }

    public int getNumberOfTiles() {
        return tileExtractor.getNumberOfTiles();
    }

    public List<BufferedImage> getTiles() {
        return tileExtractor.getTiles();
    }
}
