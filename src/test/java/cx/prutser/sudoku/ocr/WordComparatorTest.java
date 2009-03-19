package cx.prutser.sudoku.ocr;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author  Erik van Zijst
 */
public class WordComparatorTest {

    @Test
    public void compare() {

        SortedSet<String> set = new TreeSet<String>(new WordComparator());

        set.add("ABCD");
        set.add("B");
        set.add("ABC");
        set.add("ABCDE");
        set.add("HTSGQ");

        final String[] words = set.toArray(new String[set.size()]);

        assertEquals("ABCDE", words[0]);
        assertEquals("HTSGQ", words[1]);
        assertEquals("ABCD", words[2]);
        assertEquals("ABC", words[3]);
        assertEquals("B", words[4]);
    }
}
