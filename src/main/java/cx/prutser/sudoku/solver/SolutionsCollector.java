package cx.prutser.sudoku.solver;

/**
 * @author Erik van Zijst
 */
public interface SolutionsCollector<T> {

    /**
     * Invoked by the {@link cx.prutser.sudoku.solver.Solver} when a new solution is
     * found. This callback is invoked synchronously during the search process.
     * The supplied {@link cx.prutser.sudoku.solver.SolverContext} instance provides
     * the user with information about the number of evaluations spent so far
     * and allows the user to terminate the search to prevent the solver from
     * searching for additional solutions.
     *
     * @param solution
     * @param ctx
     */
    void newSolution(T[] solution, SolverContext ctx);

    /**
     * Called by the {@link cx.prutser.sudoku.solver.Solver} when the entire puzzle
     * has been searched and all solutions have been reported to
     * {@link #newSolution(Object[], SolverContext)}.
     *
     * @param evaluations   the total number of evaluations spent on the puzzle.
     */
    void searchComplete(long evaluations);

    /**
     * Called by the {@link cx.prutser.sudoku.solver.Solver} when the puzzle
     * could not be solved within the timeout.
     *
     * @param millis    the timeout in milliseconds that was exceeded.
     */
    void timeoutExceeded(long millis);
}
