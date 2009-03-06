package cx.prutser.sudoku;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

/**
 * @author Erik van Zijst
 */
public class ClassicSudokuUtils {

    private ClassicSudokuUtils() {}

    public static Integer[] parseRaw(Reader reader) throws IOException {

        final char[] buf = new char[4096];
        final StringWriter out = new StringWriter();

        int len = 0;
        while ((len = reader.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        return parseRaw(out.toString());
    }

    /**
     * Parses a classic sudoku puzzle. The specified {@link java.io.Reader}
     * must supply data in the following format:
     * <PRE>
     * - - 8 - - 2 - - 3
     * - 6 - - 5 - - 8 -
     * 9 - - 3 - - 4 - -
     * - - - 1 2 - - 9 -
     * - - 5 - - - 7 - -
     * - 8 - - 3 6 - - -
     * - - 6 - - 1 - - 8
     * - 5 - - 9 - - 6 -
     * 3 - - 6 - - 9 - -
     * </PRE>
     * <P>
     * Where "-" indicates a blank tile. Note that "-" may be substituted by
     * "?". Newlines are optional and do not have to be column-aligned. One or
     * more space must separate each tile. Leading and trailing whitespace is
     * ignored.
     *
     * @param string
     * @return
     */
    public static Integer[] parseRaw(String string) {

        final Integer[] result = new Integer[81];
        final String[] tokens = string.split("\\s+");
        for (int i = 0; i < 81; i++) {
            result[i] = ("-?".indexOf(tokens[i]) == -1) ?
                    Integer.parseInt(tokens[i]) : null;
        }
        return result;
    }

    /**
     *
     * @param puzzle
     * @return
     */
    public static String format(Integer[] puzzle) {

        final String[] strings = new String[81];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = puzzle[i] == null ? "" : String.valueOf(puzzle[i]);
        }

        return String.format(
                "+--+--+--+--+--+--+--+--+--+\n" +
                "|%2s %2s %2s|%2s %2s %2s|%2s %2s %2s|\n" +
                "|%2s %2s %2s|%2s %2s %2s|%2s %2s %2s|\n" +
                "|%2s %2s %2s|%2s %2s %2s|%2s %2s %2s|\n" +
                "+--+--+--+--+--+--+--+--+--+\n" +
                "|%2s %2s %2s|%2s %2s %2s|%2s %2s %2s|\n" +
                "|%2s %2s %2s|%2s %2s %2s|%2s %2s %2s|\n" +
                "|%2s %2s %2s|%2s %2s %2s|%2s %2s %2s|\n" +
                "+--+--+--+--+--+--+--+--+--+\n" +
                "|%2s %2s %2s|%2s %2s %2s|%2s %2s %2s|\n" +
                "|%2s %2s %2s|%2s %2s %2s|%2s %2s %2s|\n" +
                "|%2s %2s %2s|%2s %2s %2s|%2s %2s %2s|\n" +
                "+--+--+--+--+--+--+--+--+--+",
                (Object[])strings);
    }
}
