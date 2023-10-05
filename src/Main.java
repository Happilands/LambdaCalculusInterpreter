import interpreter.program.Program;
import util.StopWatch;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Program program = new Program();

        StopWatch stopWatch = new StopWatch();

        program.importFile(Path.of("example_programs/fibonacci.lc"));

        program.evaluate();

        System.out.print("Evaluation ");
        stopWatch.printTime();
    }
}