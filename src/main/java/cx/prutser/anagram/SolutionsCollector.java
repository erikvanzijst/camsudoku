package cx.prutser.anagram;

/**
 * @author  Erik van Zijst
 */
public interface SolutionsCollector {

    void newSolution(String word, SolverContext ctx);

    /**
     * Called by the {@link cx.prutser.anagram.Solver} when the entire puzzle
     * has been searched and all solutions have been reported to
     * {@link #newSolution(String, SolverContext)}.
     *
     * @param evaluations   the total number of evaluations spent on the puzzle.
     */
    void searchComplete(long evaluations);

    /**
     * Called by the {@link cx.prutser.anagram.Solver} when the puzzle
     * could not be solved within the timeout.
     *
     * @param millis    the timeout in milliseconds that was exceeded.
     */
    void timeoutExceeded(long millis);
}
