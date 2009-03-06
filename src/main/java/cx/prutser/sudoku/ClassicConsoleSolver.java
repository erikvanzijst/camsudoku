package cx.prutser.sudoku;

import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: erik
 * Date: 4/10/2008
 * Time: 14:09:43
 * To change this template use File | Settings | File Templates.
 */
public class ClassicConsoleSolver {

    private String filename = null;
    private int maxSolutions = 0;

    private ClassicConsoleSolver(String... args) {
        parseArgs(args);
    }

    private void run() throws Exception {

        final ClassicSolver solver = new ClassicSolver(
                ClassicSudokuUtils.parseRaw(filename == null ?
                        new InputStreamReader(System.in) : new FileReader(filename)));

        final long start = System.currentTimeMillis();
        solver.solve(new SolutionsCollector<Integer>() {
            int count = 0;
            public void newSolution(Integer[] solution, SolverContext ctx) {
                System.out.println(String.format(
                        "%s\nFound in %d ms and %d evaluations.",
                        ClassicSudokuUtils.format(solution),
                        System.currentTimeMillis() - start, ctx.evaluations()));
                if (++count >= maxSolutions && maxSolutions > 0) {
                    ctx.cancel();
                }
            }

            public void searchComplete(long evaluations) {
                System.out.println(String .format(
                        "All %d solutions found in %d ms and %d evaluations.",
                        count, System.currentTimeMillis() - start, evaluations));
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new ClassicConsoleSolver(args).run();
    }

    private void parseArgs(String... args) {

        final String usage = "Usage:\n" +
                "   sudoku [OPTIONS]\n" +
                "\n" +
                "Solves a classic 9x9 sudoku puzzle from file or stdin.\n" +
                "\n" +
                "OPTIONS:\n" +
                "   -i, --input     input file, use stdin when omitted.\n" +
                "   -m, --max       maximum number of solutions to search for\n" +
                "                   (default is unlimited).\n" +
                "   -h, --help      print this help message and exit.\n";

        boolean exit = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-i") || args[i].equals("--input")) {
                filename = args[++i];
            } else if (args[i].equals("-m") || args[i].equals("--max")) {
                maxSolutions = Integer.parseInt(args[++i]);
            } else if (args[i].equals("-h") || args[i].equals("--help")) {
                exit = true;
                break;
            }
        }

        if (exit) {
            System.out.println(usage);
            System.exit(0);
        }
    }
}
