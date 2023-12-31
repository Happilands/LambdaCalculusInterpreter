package interpreter.program;

import interpreter.TokenStack;
import interpreter.exception.LambdaError;
import interpreter.syntax.statements.Statement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Program {
    private final List<Statement> statements;
    private final DefinitionStack definitionStack;
    private final Stack<ProgramSegment> segments;
    private final Path workingDirectory;
    private final Set<Path> imports;

    public Program(Path workingDirectory){
        this.workingDirectory = workingDirectory;

        statements = new ArrayList<>();
        definitionStack = new DefinitionStack();
        segments = new Stack<>();
        imports = new HashSet<>();
    }

    public void addStatement(Statement statement){
        statements.add(statement);
    }

    public Path resolveFile(Path file){
        return workingDirectory.resolve(file);
    }

    public void run(Path file){
        Path path = workingDirectory.resolve(file);
        imports.add(path);
        ProgramSegment segment = new ProgramSegment(readFile(path));
        segments.push(segment);

        try {
            segment.parse(this);
        }
        catch (LambdaError le){
            System.out.println(le.getMessage());
        }
        segments.pop();
    }

    public static String readFile(Path path){
        String code;
        try {
            code = Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return code;
    }

    public void importFile(Path file) throws LambdaError{
        Path path = workingDirectory.resolve(file);

        if(imports.contains(path))
            return;

        String code = readFile(path);

        imports.add(path);

        ProgramSegment segment = new ProgramSegment(code);
        segments.push(segment);
        segment.parse(this);
        segments.pop();
    }

    public DefinitionStack getDefinitionStack(){
        return definitionStack;
    }

    public TokenStack getTokenStack(){
        return segments.peek().getTokenStack();
    }

    public boolean evaluate() {
        //try {
        for (Statement statement : statements) {
            statement.run(this);
        }
        /*}
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return false;
        }*/
        return true;
    }
}
