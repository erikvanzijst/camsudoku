package cx.prutser.sudoku.ocr;

import java.util.Comparator;

/**
 * Sorts the words found by the anagram solver according to their length and
 * alphabetical order.
 *
 * @author  Erik van Zijst
 */
public class WordComparator implements Comparator<String> {

    public int compare(String w1, String w2) {

        if (w1.length() == w2.length()) {
            return w1.compareTo(w2);
        } else {
            return w2.length() - w1.length();
        }
    }
}
