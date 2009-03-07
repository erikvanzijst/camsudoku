package cx.prutser.sudoku.solver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: erik
 * Date: 3/10/2008
 * Time: 22:07:30
 * To change this template use File | Settings | File Templates.
 */
public class UniqueConstraintTest {
    
    @Test
    public void unique() {

        // all unique
        {
            final UniqueConstraint<Integer> con = new UniqueConstraint<Integer>();
            assertTrue(con.isSatisfied());

            con.addTile(new Tile<Integer>(1));
            assertTrue(con.isSatisfied());
            con.addTile(new Tile<Integer>(null));
            assertTrue(con.isSatisfied());
            con.addTile(new Tile<Integer>(5));
            assertTrue(con.isSatisfied());
            con.addTile(new Tile<Integer>(3));
            assertTrue(con.isSatisfied());

            final Tile<Integer> tile = new Tile<Integer>();
            con.addTile(tile);
            assertTrue(con.isSatisfied());

            tile.setValue(9);
            assertTrue(con.isSatisfied());
            tile.setValue(8);
            assertTrue(con.isSatisfied());
        }
    }

    @Test
    public void notUnique() {

        final UniqueConstraint<Integer> con = new UniqueConstraint<Integer>();
        assertTrue(con.isSatisfied());
        con.addTile(new Tile<Integer>(1));
        con.addTile(new Tile<Integer>(null));
        con.addTile(new Tile<Integer>(5));
        con.addTile(new Tile<Integer>(3));
        assertTrue(con.isSatisfied());

        final Tile<Integer> tile = new Tile<Integer>(5);
        con.addTile(tile);
        assertFalse(con.isSatisfied());

        tile.setValue(9);
        assertTrue(con.isSatisfied());

        tile.setValue(1);
        assertFalse(con.isSatisfied());
    }
}
