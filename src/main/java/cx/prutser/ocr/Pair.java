package cx.prutser.ocr;

/**
 * A pair of objects - used to create composite keys for maps
 */
public class Pair<T1, T2> {
    private final T1 comp1;
    private final T2 comp2;

    public Pair(T1 comp1, T2 comp2) {
        this.comp1 = comp1;
        this.comp2 = comp2;
    }

    public static <T1,T2> Pair<T1,T2> newInstance(T1 comp1, T2 comp2) {
        return new Pair<T1, T2>(comp1, comp2);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;

        final Pair pair = (Pair) o;

        if (comp1 != null ? !comp1.equals(pair.comp1) : pair.comp1 != null) return false;
        if (comp2 != null ? !comp2.equals(pair.comp2) : pair.comp2 != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (comp1 != null ? comp1.hashCode() : 0);
        result = 29 * result + (comp2 != null ? comp2.hashCode() : 0);
        return result;
    }

    public T1 getFirst() {
        return comp1;
    }

    public T2 getSecond() {
        return comp2;
    }

    public String toString() {
        return "(" + comp1 + ", " + comp2 + ")";
    }
}
