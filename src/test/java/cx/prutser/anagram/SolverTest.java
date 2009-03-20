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
        // center character is 'C':
        Solver solver = new Solver("ABCDE", 2);

        assertTrue(solver.fits("C"));
        assertTrue(solver.fits("ABCD"));
        assertTrue(solver.fits("DCBA"));
        assertTrue(solver.fits("ABCDE"));

        assertFalse(solver.fits("B"));
        assertFalse(solver.fits("ABDE"));
        assertFalse(solver.fits("BAB"));
        assertFalse(solver.fits("ABAE"));
        assertFalse(solver.fits("CEFGH"));
        assertFalse(solver.fits("ABCDEF"));
    }

    @Test
    public void fitsDuplicateChars() {

        // center character is 'C':
        Solver solver = new Solver("ABCDED", 2);

        assertTrue(solver.fits("C"));
        assertTrue(solver.fits("DDC"));
        assertTrue(solver.fits("DC"));
        assertTrue(solver.fits("ABDCDE"));
        assertTrue(solver.fits("C"));

        assertFalse(solver.fits("EFDDC"));
        assertFalse(solver.fits("EFDDD"));
        assertFalse(solver.fits("EFGH"));
        assertFalse(solver.fits("AECDE"));
    }
}
