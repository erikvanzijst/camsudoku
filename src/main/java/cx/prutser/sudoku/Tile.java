package cx.prutser.sudoku;

class Tile<T> {

    private T value = null;

    public Tile() {
        this(null);
    }

    public Tile(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
