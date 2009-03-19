package cx.prutser.anagram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Brute-force solver algorithm for finding solutions for an anagram puzzle. The
 * dictionary file is loaded from the classpath.
 *
 * @author  Erik van Zijst
 */
public class Solver {

    private final String DICT_FILENAME = "words";
    private final String target;
    private long evals;
    private final Set<Character> buf = new HashSet<Character>();

    /**
     *
     * @param word
     * @throws IllegalArgumentException when the supplied word contained
     * non-unique characters.
     */
    public Solver(String word) throws IllegalArgumentException {
        this.target = word;
    }

    public void solve(SolutionsCollector collector) {

        evals = 0L;    // number of words evaluated
        InputStream in = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(DICT_FILENAME);

        if (in == null) {
            throw new RuntimeException("Could not find neural network " +
                    "configuration in classpath (" + DICT_FILENAME + ").");

        } else {
            final AtomicBoolean canceled = new AtomicBoolean(false);

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String word;
            try {
                while (!canceled.get() && (word = reader.readLine()) != null) {

                    if (word.length() > 0 && fits(word)) {
                        collector.newSolution(word, new SolverContext() {
                            public void cancel() {
                                canceled.set(true);
                            }

                            public long evaluations() {
                                return evals;
                            }
                        });
                    }
                }

                if (!canceled.get()) {
                    collector.searchComplete(evals);
                }
            } catch (IOException e) {
                throw new RuntimeException(
                        "Error reading dictionary from classpath: " + e.getMessage());
            } finally {
                try {
                    reader.close();
                } catch(Exception e) {}
            }
        }
    }

    /**
     * Returns <code>true</code> if the supplied word can be constructed from
     * the target characters, <code>false</code> otherwise. Words must always
     * be in the same case. No conversion is applied.
     *
     * @param word
     * @return
     */
    protected boolean fits(String word) {

        if (word.length() <= target.length()) {
            evals++;
            try {
                for (char c : word.toCharArray()) {
                    if (target.indexOf(c) < 0) {
                        return false;
                    } else {
                        buf.add(c);
                    }
                }
                return buf.size() == word.length();
            } finally {
                buf.clear();
            }
        }
        return false;
    }

    public static void main(String... args) {

        final String usage = "Usage: java " + Solver.class.getName() + " [OPTIONS] target\n" +
                "\n" +
                "Brute-force anagram solver.\n" +
                "\n" +
                "OPTIONS\n" +
                "   -h, --help  print this help message and exit.";

        String word = null;
        boolean exit = false;
        try {
            for (int i = 0; !exit && i < args.length; i++) {
                if("-h".equals(args[i]) || "--help".equals(args[i])) {
                    exit = true;
                } else {
                    word = args[i];
                    break;
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            exit = true;
        }

        if (exit || word == null || word.length() == 0) {
            System.err.println(usage);
            System.exit(1);

        } else {
            Solver solver = new Solver(word);
            solver.solve(new SolutionsCollector() {
                public void newSolution(String word, SolverContext ctx) {
                    System.out.println(word);
                }

                public void searchComplete(long evaluations) {
                    System.out.println("Search completed in " + evaluations + " evaluations.");
                }

                public void timeoutExceeded(long millis) {
                    System.out.println("Search time exceeded (" + millis + "ms)");
                }
            });
        }
    }
}
