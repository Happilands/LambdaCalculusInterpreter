import interpreter.program.Program;
import util.StopWatch;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Program program = new Program(Path.of("binary/"));

        StopWatch stopWatch = new StopWatch();

        program.importFile(Path.of("program.lc"));

        program.evaluate();

        System.out.print("Evaluation ");
        stopWatch.printTime();
    }
}