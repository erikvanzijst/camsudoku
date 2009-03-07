package cx.prutser.sudoku.solver;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: erik
 * Date: 3/10/2008
 * Time: 20:53:34
 * To change this template use File | Settings | File Templates.
 */
public class FixedValueConstraintTest {

    private Tile<Integer> tile = null;

    @Before
    public void setup() {
        tile = new Tile<Integer>();
    }

    @Test
    public void test() {

        final FixedValueConstraint<Integer> con = new FixedValueConstraint<Integer>(tile, 1);

        tile.setValue(1);
        assertTrue(con.isSatisfied());
        tile.setValue(0);
        assertFalse(con.isSatisfied());
        tile.setValue(null);
        assertFalse(con.isSatisfied());
    }

    @Test
    public void validation() {

        // tile == null
        {
            try {
                new FixedValueConstraint<Integer>(null, 1);
                fail();
            } catch(IllegalArgumentException iae) {}
        }
        
        // value == null
        {
            try {
                new FixedValueConstraint<Integer>(new Tile<Integer>(), null);
                fail();
            } catch(IllegalArgumentException iae) {}
        }
    }
}
