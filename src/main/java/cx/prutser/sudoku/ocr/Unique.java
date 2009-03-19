package cx.prutser.sudoku.ocr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple temp app that reads words from stdin and on prints words of which
 * all letters are unique.
 */
public class Unique {

    public static void main(String... args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String word;

        while ((word = br.readLine()) != null) {
            Set<Character> chars = new HashSet<Character>();
            int size = 0;
            boolean drop = false;
            for (char c : word.toCharArray()) {
                chars.add(c);
                if (chars.size() == size) {
                    drop = true;
                    break;
                } else {
                    size = chars.size();
                }
            }
            if (!drop) {
                System.out.println(word);
            }
        }
    }
}
