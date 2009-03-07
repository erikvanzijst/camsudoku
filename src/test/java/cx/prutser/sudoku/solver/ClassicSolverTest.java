package cx.prutser.sudoku.solver;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: erik
 * Date: 3/10/2008
 * Time: 23:49:01
 * To change this template use File | Settings | File Templates.
 */
public class ClassicSolverTest {

    private Integer solvedPuzzle[] = null;
    private Integer simplePuzzle[] = null;
    private Integer blankPuzzle[] = null;
    private Integer realPuzzle[] = null;

    @Before
    public void setup() {

        solvedPuzzle = new Integer[] {
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                4, 5, 6, 7, 8, 9, 1, 2, 3,
                7, 8, 9, 1, 2, 3, 4, 5, 6,
                2, 3, 4, 5, 6, 7, 8, 9, 1,
                5, 6, 7, 8, 9, 1, 2, 3, 4,
                8, 9, 1, 2, 3, 4, 5, 6, 7,
                9, 1, 2, 3, 4, 5, 6, 7, 8,
                3, 4, 5, 6, 7, 8, 9, 1, 2,
                6, 7, 8, 9, 1, 2, 3, 4, 5
            };
        simplePuzzle = new Integer[] {
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                4, 5, 6, 7, 8, 9, 1, 2, 3,
                7, 8, 9, 1, 2, 3, 4, 5, 6,
                2, 3, 4, 5, 6, 7, 8, 9, 1,
                5, 6, 7, 8, 9, 1, 2, 3, 4,
                8, 9, 1, 2, 3, 4, 5, 6, 7,
                9, 1, 2, 3, 4, 5, 6, 7, 8,
                3, 4, 5, 6, 7, 8, 9, 1, 2,
                6, 7, 8, 9, 1, 2, 3, 4, null
            };
        blankPuzzle = new Integer[81];
        realPuzzle = new Integer[] {
                null, null, 8, null, null, 2, null, null, 3,
                null, 6, null, null, 5, null, null, 8, null,
                9, null, null, 3, null, null, 4, null, null,
                null, null, null, 1, 2, null, null, 9, null,
                null, null, 5, null, null, null, 7, null, null,
                null, 8, null, null, 3, 6, null, null, null,
                null, null, 6, null, null, 1, null, null, 8,
                null, 5, null, null, 9, null, null, 6, null,
                3, null, null, 6, null, null, 9, null, null
            };
    }

    @Test
    public void solvedPuzzle() {

        @SuppressWarnings("unchecked")
        SolutionsCollector<Integer> collector = createStrictMock(SolutionsCollector.class);
        collector.newSolution(aryEq(solvedPuzzle), isA(SolverContext.class));
        collector.searchComplete(0L);
        replay(collector);

        ClassicSolver solver = new ClassicSolver(solvedPuzzle);
        solver.solve(collector);

        verify(collector);
    }

    @Test
    public void simplePuzzle() {

        // very simple puzzle: only a single tile missing
        {
            @SuppressWarnings("unchecked")
            SolutionsCollector<Integer> collector = createStrictMock(SolutionsCollector.class);
            collector.newSolution(aryEq(solvedPuzzle), isA(SolverContext.class));
            collector.searchComplete(11L);
            replay(collector);

            ClassicSolver solver = new ClassicSolver(simplePuzzle);
            solver.solve(collector);

            verify(collector);
        }
    }

    @Test
    public void illegalBoard() {

        // null reference
        {
            try {
                new ClassicSolver(null);
                fail();
            } catch(IllegalArgumentException iae) {}
        }

        // invalid number of tiles
        {
            try {
                new ClassicSolver(new Integer[80]);
                fail();
            } catch(IllegalArgumentException iae) {}
        }

        // illegal tile number
        {
            try {
                blankPuzzle[80] = 10;
                new ClassicSolver(blankPuzzle);
                fail();
            } catch(IllegalArgumentException iae) {}
        }
    }

    @Test
    public void cancel() {

        // cancel after the first solution
        {
            ClassicSolver solver = new ClassicSolver(blankPuzzle);
            solver.solve(new SolutionsCollector<Integer>() {
                public void newSolution(Integer[] solution, SolverContext ctx) {
                    System.out.println(Arrays.toString(solution));
                    System.out.println("Evaluations: " + ctx.evaluations());
                    ctx.cancel();
                }

                public void searchComplete(long evaluations) {
                    fail();
                }
            });
        }
    }

    @Test
    public void realPuzzle() {

        ClassicSolver solver = new ClassicSolver(realPuzzle);
        solver.solve(new SolutionsCollector<Integer>() {
            public void newSolution(Integer[] solution, SolverContext ctx) {
                System.out.println(Arrays.toString(solution));
                System.out.println("Evaluations: " + ctx.evaluations());
            }

            public void searchComplete(long evaluations) {
                System.out.println("Completed in " + evaluations
                        + " evaluations: ");
            }
        });
    }
}
