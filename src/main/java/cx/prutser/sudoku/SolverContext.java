package cx.prutser.sudoku;

/**
 * Passed to the user by the {@link cx.prutser.sudoku.Solver} when a new
 * solution has been found.
 *
 * @see cx.prutser.sudoku.SolutionsCollector#newSolution(Object[], SolverContext)
 * @author Erik van Zijst
 */
public interface SolverContext {

    /**
     * Breaks the search for more solutions.
     */
    void cancel();

    /**
     * Returns the number of evaluations that have been made during while
     * solving the puzzle.
     *
     * @return  the number of evaluations required to reach this point.
     */
    long evaluations();
}
