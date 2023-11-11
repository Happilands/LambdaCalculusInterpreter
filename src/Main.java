import interpreter.program.Program;
import util.StopWatch;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        for (String arg : args
             ) {
            run(Path.of(arg));
        }
    }

    private static void run(Path path){
        Path file = path.getFileName();
        Path directory = path.getParent();

        Program program = new Program(directory);

        program.run(file);

        program.evaluate();
    }

    private static void debugRun(Path path){
        Path file = path.getFileName();
        Path directory = path.getParent();

        Program program = new Program(directory);

        StopWatch stopWatch = new StopWatch();

        program.run(file);

        System.out.print("Parsing ");
        stopWatch.printTime();

        program.evaluate();

        System.out.print("Evaluation ");
        stopWatch.printTime();
    }
}