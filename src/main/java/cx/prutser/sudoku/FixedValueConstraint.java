package cx.prutser.sudoku;

/**
 * @author Erik van Zijst
 */
class FixedValueConstraint<T> implements Constraint {

    private final T value;
    private final Tile<T> tile;

    public FixedValueConstraint(Tile<T> tile, T value) {
        if (tile == null || value == null) {
            throw new IllegalArgumentException("null");
        } else {
            this.tile = tile;
            this.value = value;
        }
    }

    public boolean isSatisfied() {
        return value.equals(tile.getValue());
    }
}
