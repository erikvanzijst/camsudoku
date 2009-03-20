package cx.prutser.sudoku.ocr;

import cx.prutser.anagram.SolutionsCollector;
import cx.prutser.anagram.Solver;
import cx.prutser.anagram.SolverContext;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * OCR-based anagram solver.
 * 
 * @author Erik van Zijst
 */
public class GraphicalSolver {

    private final static String CONFIG_FILENAME = "config.net";
    private final CharacterRecognizer ocr;
    private String target = "";

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
                ocr = new CharacterRecognizer(in);
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
     * Returns the characters as recognized from the puzzle, in their original
     * order.
     *
     * @return
     */
    public String getTarget() {
        return target;
    }

    /**
     * The supplied image must exactly contain the puzzle and be square in size.
     * TODO: return a list of Pairs that include the definition of the returned words
     *
     * @param image
     * @throws IllegalArgumentException when the image is not squared.
     */
    public List<String> solve(BufferedImage image) throws IllegalArgumentException {

        if (image.getWidth() != image.getHeight()) {
            throw new IllegalArgumentException(String.format(
                    "The supplied image must be a square; not %dx%d",
                    image.getWidth(), image.getHeight()));

        } else {
            return findWords(readCharacters(image));
        }
    }

    private String readCharacters(BufferedImage image) {

        final List<BufferedImage> tiles =
                    new LoggingTileExtractor(
                            new WordTileExtractor(), "snapshots")
                .extractTiles(image);

        final StringBuilder buf = new StringBuilder();
        final StringBuilder t = new StringBuilder();

        for (int index = 0; index < tiles.size(); index++) {
            char c = ocr.testAndClassify(OCRUtils.pixelsToPattern(OCRUtils.getPixels(tiles.get(index))));

            if (CharacterRecognizer.UNRECOGNIZED == c) {
                System.err.println(String.format("Warning: tile %d not recognized!", index));

            } else if (index == 4) {
                // put the middle tile at the head
                buf.insert(0, c);
                t.append(c);

            } else {
                buf.append(c);
                t.append(c);
            }
        }

        target = t.toString();
        return buf.toString();
    }

    /**
     * Returns a sorted list of solutions, sorted according to relevance. The
     * longest words at the head.
     *
     * @param word  the given characters, with the required character at index 0.
     */
    private List<String> findWords(String word) {

        final List<String> words = new ArrayList<String>();

        new Solver(word, 0).solve(new SolutionsCollector() {
            public void newSolution(String word, SolverContext ctx) {
                words.add(word);
            }
            public void searchComplete(long evaluations) {}
            public void timeoutExceeded(long millis) {}
        });

        Collections.sort(words, new WordComparator());
        return words;
    }

//    /**
//     * Solves the sudoku using a brute-force algorithm. Only returns the first
//     * solution found. Returns <code>null</code> if no solution could be found.
//     *
//     * @param board
//     * @return
//     */
//    private Integer[] solve(Integer[] board, long timeout) {
//
//        final Integer[] solution = new Integer[81];
//        final AtomicBoolean unsolvable = new AtomicBoolean(false);  // the glass is half full
//
//        new ClassicSolver(board, timeout).solve(new SolutionsCollector<Integer>() {
//            public void newSolution(Integer[] sol, SolverContext ctx) {
//
//                System.arraycopy(sol, 0, solution, 0, sol.length);
//                ctx.cancel();
//            }
//
//            public void searchComplete(long evaluations) {
//                unsolvable.set(true);
//            }
//
//            public void timeoutExceeded(long millis) {
//                System.out.println("Could not solve within " + millis + "ms");
//                unsolvable.set(true);
//            }
//        });
//
//        return unsolvable.get() ? null : solution;
//    }
//
//    /**
//     * This method modifies its parameter.
//     *
//     * @param image
//     * @return
//     */
//    private BufferedImage burnSolution(BufferedImage image, Integer[] solution) {
//
//        final int TILE_SIZE = image.getWidth() / 9;
//        final int FONT_HEIGHT = (int)(TILE_SIZE * 0.8);
//        final int FONT_WIDTH = (int)(FONT_HEIGHT * 0.75); // approximate
//
//        Graphics2D g2 = image.createGraphics();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setFont(new Font("lucida sans unicode", Font.PLAIN, FONT_HEIGHT));
//        g2.setColor(Color.BLUE);
//
//        for (int i = 0; i < solution.length; i++) {
//            if (solution[i] != null) {
//                int x = TILE_SIZE * (i % 9) + (TILE_SIZE - FONT_WIDTH) / 2;
//                int y = TILE_SIZE * (i / 9) + (TILE_SIZE - FONT_HEIGHT) / 2 + FONT_HEIGHT;
//
//                g2.drawString(String.valueOf(solution[i]), x, y);
//            }
//        }
//        g2.dispose();
//
//        return image;
//    }
}
