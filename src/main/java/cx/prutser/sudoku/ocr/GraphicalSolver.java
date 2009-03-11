package cx.prutser.sudoku.ocr;

import cx.prutser.sudoku.solver.ClassicSolver;
import cx.prutser.sudoku.solver.ClassicSudokuUtils;
import cx.prutser.sudoku.solver.SolutionsCollector;
import cx.prutser.sudoku.solver.SolverContext;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Erik van Zijst
 */
public class GraphicalSolver {

    private final static String CONFIG_FILENAME = "config.net";
    private final SudokuDigitRecognizer ocr;

    public GraphicalSolver() {

        InputStream in = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILENAME);

        if (in == null) {
            throw new RuntimeException("Could not find neural network " +
                    "configuration in classpath (" + CONFIG_FILENAME + ").");

        } else {
            try {
                ocr = new SudokuDigitRecognizer(in);
            } catch (IOException e) {
                throw new RuntimeException("Error reading neural network: " + e.getMessage(), e);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {}
            }
        }
    }

    /**
     * The supplied image must exactly contain the puzzle and be square in size.
     *
     * @param image
     * @return  the same image, with the missing digits superimposed on the empty
     * tiles.
     * @throws IllegalArgumentException when the image is not squared.
     */
    public BufferedImage solve(BufferedImage image) throws IllegalArgumentException {

        if (image.getWidth() != image.getHeight()) {
            throw new IllegalArgumentException(String.format(
                    "The supplied image must be a square; not %dx%d", image.getWidth(), image.getHeight()));
        } else {
            final Integer[] board = new Integer[81];
            final List<BufferedImage> tiles =
                    new AdaptiveThresholdingExtractor(
                            new LoggingTileExtractor(
                                    new SimpleTileExtractor(), "snapshots"))
                    .extractTiles(image);

            for (int index = 0; index < tiles.size(); index++) {

                int digit = ocr.testAndClassify(OCRUtils.pixelsToPattern(OCRUtils.getPixels(tiles.get(index))));
                board[index] = digit <= 0 ? null : digit;
                if (digit < 0) {
                    System.err.println(String.format("Warning: tile %d not recognized!", index));
                }
            }

            System.out.println(ClassicSudokuUtils.format(board));

            final ClassicSolver solver = new ClassicSolver(board);
            solver.solve(new SolutionsCollector<Integer>() {
                public void newSolution(Integer[] solution, SolverContext ctx) {

                    System.out.println(String.format(
                            "Solution found in %d evaluations:\n%s", ctx.evaluations(), ClassicSudokuUtils.format(solution)));
                    ctx.cancel();
                }

                public void searchComplete(long evaluations) {}
            });

            return image;
        }
    }
}
