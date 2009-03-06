package cx.prutser.sudoku;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Solver for standard 9x9 sudoku's, based on a board layout with the following
 * tile indexes:
 * <PRE>
 *     0  1  2  3  4  5  6  7  8
 *   +--+--+--+--+--+--+--+--+--+
 * 0 | 0  1  2| 3  4  5| 6  7  8|
 * 1 | 9 10 11|12 13 14|15 16 17|
 * 2 |18 19 20|21 22 23|24 25 26|
 *   +--+--+--+--+--+--+--+--+--+
 * 3 |27 28 29|30 31 32|33 34 35|
 * 4 |36 37 38|39 40 41|42 43 44|
 * 5 |45 46 47|48 49 50|51 52 53|
 *   +--+--+--+--+--+--+--+--+--+
 * 6 |54 55 56|57 58 59|60 61 62|
 * 7 |63 64 65|66 67 68|69 70 71|
 * 8 |72 73 74|75 76 77|78 79 80|
 *   +--+--+--+--+--+--+--+--+--+
 * </PRE>
 *
 * @author Erik van Zijst
 */
public class ClassicSolver implements Solver<Integer> {

    private static final int HORIZONTAL_INDEX[] = new int[] {
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 1, 1, 1,
            2, 2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3, 3, 3, 3,
            4, 4, 4, 4, 4, 4, 4, 4, 4,
            5, 5, 5, 5, 5, 5, 5, 5, 5,
            6, 6, 6, 6, 6, 6, 6, 6, 6,
            7, 7, 7, 7, 7, 7, 7, 7, 7,
            8, 8, 8, 8, 8, 8, 8, 8, 8
    };
    private static final int VERTICAL_INDEX[] = new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            0, 1, 2, 3, 4, 5, 6, 7, 8
    };
    private static final int AREA_INDEX[] = new int[] {
            0, 0, 0, 1, 1, 1, 2, 2, 2,
            0, 0, 0, 1, 1, 1, 2, 2, 2,
            0, 0, 0, 1, 1, 1, 2, 2, 2,
            3, 3, 3, 4, 4, 4, 5, 5, 5,
            3, 3, 3, 4, 4, 4, 5, 5, 5,
            3, 3, 3, 4, 4, 4, 5, 5, 5,
            6, 6, 6, 7, 7, 7, 8, 8, 8,
            6, 6, 6, 7, 7, 7, 8, 8, 8,
            6, 6, 6, 7, 7, 7, 8, 8, 8
    };

    private Tile<Integer> tiles[];
    private Constraint[][] constraintsByTile = new Constraint[81][];
    private long evals = 0L;

    public ClassicSolver(Integer[] board) {

        if (board == null || board.length != 81) {
            throw new IllegalArgumentException("Board must contain 81 tiles.");
        } else {
            tiles = new Tile[81];
            for (int i = 0; i < board.length; i++) {
                if (board[i] != null && (board[i] < 1 || board[i] > 9)) {
                    throw new IllegalArgumentException(
                            "Illegal value: " + board[i]);
                } else {
                    tiles[i] = new Tile<Integer>(board[i]);
                }
            }
        }

        // create constraints:
        final UniqueConstraint<Integer> horizontal[] = new UniqueConstraint[9];
        final UniqueConstraint<Integer> vertical[] = new UniqueConstraint[9];
        final UniqueConstraint<Integer> area[] = new UniqueConstraint[9];
        for (int i = 0; i < 9; i++) {
            horizontal[i] = new UniqueConstraint<Integer>();
            vertical[i] = new UniqueConstraint<Integer>();
            area[i] = new UniqueConstraint<Integer>();
        }

        for (int i = 0; i < 81; i++) {

            final Set<Constraint> constraints = new HashSet<Constraint>();
            if (tiles[i].getValue() != null) {
                constraints.add(new FixedValueConstraint<Integer>(
                        tiles[i], tiles[i].getValue()));
            }

            // every tile is part of 3 unique collections
            vertical[VERTICAL_INDEX[i]].addTile(tiles[i]);
            horizontal[HORIZONTAL_INDEX[i]].addTile(tiles[i]);
            area[AREA_INDEX[i]].addTile(tiles[i]);

            constraints.add(vertical[VERTICAL_INDEX[i]]);
            constraints.add(horizontal[HORIZONTAL_INDEX[i]]);
            constraints.add(area[AREA_INDEX[i]]);

            constraintsByTile[i] = constraints.toArray(
                    new Constraint[constraints.size()]);
        }
    }

    public void solve(SolutionsCollector<Integer> integerSolutionsCollector) {

        try {
            solve(0, integerSolutionsCollector);
            integerSolutionsCollector.searchComplete(evals);
        } catch(CanceledException ce) {
        }
    }

    /**
     * Recursive call that implements the search algorithm. Every solution that
     * is found is synchronously reported to the user through the specified
     * {@link cx.prutser.sudoku.SolutionsCollector} instance.
     *
     * @param index the index of the tile currently being evaluated.
     * @param collector
     * @throws CanceledException    when the user canceled the search.
     */
    private void solve(int index, SolutionsCollector<Integer> collector)
            throws CanceledException {

        if (index == tiles.length) {
            reportSolution(collector);
        } else {
            if (tiles[index].getValue() != null) {
                solve(index + 1, collector);
            } else {
                for (int i = 1; i <= 9; i++) {
                    tiles[index].setValue(i);
                    if(meetsConstraints(index)) {
                        solve(index + 1, collector);
                    }
                }
                tiles[index].setValue(null);
            }
        }
    }

    private boolean meetsConstraints(int tileNumber) {

        for (Constraint constraint : constraintsByTile[tileNumber]) {
            evals++;
            if (!constraint.isSatisfied()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Invoked by the search algoritm when a solution has been found. This
     * method makes a copy of the puzzle and delivers it to the user's
     * {@link cx.prutser.sudoku.SolutionsCollector#newSolution(Object[], SolverContext)}
     * callback.
     *
     * @param collector
     * @throws CanceledException    when the users canceled the search.
     */
    private void reportSolution(SolutionsCollector<Integer> collector)
            throws CanceledException {

        final AtomicBoolean cancel = new AtomicBoolean(false);
        final Integer[] solution = new Integer[81];
        final long evaluations = evals;

        for (int i = 0; i < 81; i++) {
            solution[i] = tiles[i].getValue();
        }
        collector.newSolution(solution, new SolverContext() {
            public void cancel() {
                cancel.set(true);
            }
            public long evaluations() {
                return evaluations;
            }
        });

        if(cancel.get()) {
            throw new CanceledException();
        }
    }

    /**
     * Used internally by this solver to signal that the search must stop
     * because the user called {@link cx.prutser.sudoku.SolverContext#cancel()}.
     */
    private class CanceledException extends Exception {

        private CanceledException() {
        }
    }
}
