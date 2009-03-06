package cx.prutser.sudoku;

/**
 * @author Erik van Zijst
 */
public interface Solver<T> {

    public void solve(SolutionsCollector<T> solutionsCollector);
}
