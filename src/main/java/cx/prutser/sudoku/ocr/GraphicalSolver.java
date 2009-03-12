package cx.prutser.sudoku.ocr;

import cx.prutser.sudoku.solver.ClassicSolver;
import cx.prutser.sudoku.solver.ClassicSudokuUtils;
import cx.prutser.sudoku.solver.SolutionsCollector;
import cx.prutser.sudoku.solver.SolverContext;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
                    "The supplied image must be a square; not %dx%d",
                    image.getWidth(), image.getHeight()));
        } else {
            final Integer[] board = readTiles(image);
            System.out.println(ClassicSudokuUtils.format(board));

            final Integer[] solution = solve(board);
            if (solution == null) {
                System.out.println("Puzzle cannot be solved.");

            } else {
                System.out.println(String.format(
                        "Solution found :\n%s", ClassicSudokuUtils.format(solution)));

                // cut out the numbers that were in the puzzle, so we only burn the missing ones
                for (int i = 0; i < board.length; i++) {
                    if (board[i] != null) {
                        solution[i] = null;
                    }
                }
                image = burnSolution(image, solution);
            }

            return image;
        }
    }

    /**
     * Reads the puzzle's photo, extracts the numerical values and returns them
     * as a board array. Tiles that are not recognized properly are left as
     * blank tiles.
     *
     * @param image
     * @return
     */
    private Integer[] readTiles(BufferedImage image) {

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
        return board;
    }

    /**
     * Solves the sudoku using a brute-force algorithm. Only returns the first
     * solution found. Returns <code>null</code> if no solution could be found.
     *
     * @param board
     * @return
     */
    private Integer[] solve(Integer[] board) {

        final Integer[] solution = new Integer[81];
        final AtomicBoolean unsolvable = new AtomicBoolean(false);  // the glass is half full

        new ClassicSolver(board).solve(new SolutionsCollector<Integer>() {
            public void newSolution(Integer[] sol, SolverContext ctx) {

                System.arraycopy(sol, 0, solution, 0, sol.length);
                ctx.cancel();
            }

            public void searchComplete(long evaluations) {
                unsolvable.set(true);
            }
        });

        return unsolvable.get() ? null : solution;
    }

    /**
     * This method modifies its parameter.
     *
     * @param image
     * @return
     */
    private BufferedImage burnSolution(BufferedImage image, Integer[] solution) {

        final int TILE_SIZE = image.getWidth() / 9;
        final int FONT_HEIGHT = (int)(TILE_SIZE * 0.8);
        final int FONT_WIDTH = (int)(FONT_HEIGHT * 0.75); // approximate

        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("lucida sans unicode", Font.PLAIN, FONT_HEIGHT));
        g2.setColor(Color.BLUE);

        for (int i = 0; i < solution.length; i++) {
            if (solution[i] != null) {
                int x = TILE_SIZE * (i % 9) + (TILE_SIZE - FONT_WIDTH) / 2;// + FONT_WIDTH;
                int y = TILE_SIZE * (i / 9) + (TILE_SIZE - FONT_HEIGHT) / 2 + FONT_HEIGHT;

                g2.drawString(String.valueOf(solution[i]), x, y);
            }
        }
        g2.dispose();

        return image;
    }
}
