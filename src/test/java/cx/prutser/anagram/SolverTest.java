package cx.prutser.anagram;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author  Erik van Zijst
 */
public class SolverTest {

    @Test
    public void fits() {
        Solver solver = new Solver("ABCD");

        assertTrue(solver.fits("A"));
        assertTrue(solver.fits("ABCD"));
        assertTrue(solver.fits("DCBA"));
        assertTrue(solver.fits("B"));

        assertFalse(solver.fits("BAB"));
        assertFalse(solver.fits("ABAE"));
        assertFalse(solver.fits("ABCDE"));
        assertFalse(solver.fits("EFGH"));
    }
}
