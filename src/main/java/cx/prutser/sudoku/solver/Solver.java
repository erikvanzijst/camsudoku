package cx.prutser.sudoku.solver;

/**
 * @author Erik van Zijst
 */
public interface Solver<T> {

    public void solve(SolutionsCollector<T> solutionsCollector);
}
