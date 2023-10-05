import interpreter.program.Program;
import interpreter.TokenStack;
import util.StopWatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        String code = null;
        try {
            code = Files.readString(Path.of("example_programs/fibonacci.lc"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TokenStack tokenStack = new TokenStack(code);

        Program program = new Program(tokenStack);

        StopWatch stopWatch = new StopWatch();

        if (program.parse()) {
            stopWatch.printTime();
            program.evaluate();
        }

        stopWatch.printTime();
    }
}