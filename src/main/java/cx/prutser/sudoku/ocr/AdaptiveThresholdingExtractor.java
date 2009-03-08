package cx.prutser.sudoku.ocr;

import cx.prutser.sudoku.capture.CaptureUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.List;

/**
 * Implementation that applies Adaptive Thresholding to the input image before
 * passing it on.
 *
 * @author Erik van Zijst
 */
public class AdaptiveThresholdingExtractor implements TileExtractor {

    private final static int SIZE = 7;
    private final static int CONSTANT = 7;

    private final AdapThresh thresholder;
    private final TileExtractor extractor;

    public AdaptiveThresholdingExtractor(TileExtractor extractor) {
        this.extractor = extractor;
        this.thresholder = new AdapThresh();
    }

    public List<BufferedImage> extractTiles(BufferedImage image) {

        int[] pixels = new int[image.getWidth() * image.getHeight()];

        PixelGrabber grabber = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        try {
            grabber.grabPixels();
            pixels = thresholder.mean_thresh(pixels, image.getWidth(), image.getHeight(), SIZE, CONSTANT);

            Toolkit toolkit = Toolkit.getDefaultToolkit();
            BufferedImage processed = CaptureUtils.createBufferedImage(
                    toolkit.createImage(
                            new MemoryImageSource(image.getWidth(), image.getHeight(),
                                    pixels, 0, image.getWidth())), BufferedImage.TYPE_INT_RGB);

            return extractor.extractTiles(processed);

        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while grabbing pixels.");
        }
    }
}
