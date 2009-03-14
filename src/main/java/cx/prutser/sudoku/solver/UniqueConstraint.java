package cx.prutser.sudoku.solver;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO: verify if using the fastutils data structures makes sense performance wise.
 *
 * @author Erik van Zijst
 */
class UniqueConstraint<T> implements Constraint {

    private final Set<T> set = new HashSet<T>();
    private Tile<T> tiles[] = new Tile[0];

    public void addTile(Tile<T> tile) {

        Tile<T> buf[] = new Tile[tiles.length + 1];
        for (int i = 0; i < tiles.length; i++) {
            buf[i] = tiles[i];
        }
        buf[tiles.length] = tile;
        tiles = buf;
    }

    /**
     * Returns <code>true</code> if all {@link Tile}s contain unique values,
     * <code>false</code> otherwise. Note that a <code>null</code> value
     * indicates the tile does not have a value set.
     *
     * @return <code>true</code> if all tiles contain unique values.
     */
    public boolean isSatisfied() {

        try {
            for (Tile<T> tile : tiles) {
                if (tile.getValue() != null && !set.add(tile.getValue())) {
                    return false;
                }
            }
            return true;
        } finally {
            set.clear();
        }
    }
}
