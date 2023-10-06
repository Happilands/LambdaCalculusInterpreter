package interpreter.program;

import interpreter.TokenStack;
import interpreter.syntax.statements.Statement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Program {
    private final List<Statement> statements;
    private final DefinitionStack definitionStack;
    private final Stack<ProgramSegment> segments;
    private final Path workingDirectory;

    public Program(Path workingDirectory){
        this.workingDirectory = workingDirectory;

        statements = new ArrayList<>();
        definitionStack = new DefinitionStack();
        segments = new Stack<>();
    }

    public void addStatement(Statement statement){
        statements.add(statement);
    }

    public Path resolveFile(Path file){
        return workingDirectory.resolve(file);
    }

    public void importFile(Path file){
        Path path = workingDirectory.resolve(file);

        String code = null;
        try {
            code = Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public Path getWorkingDirectory(){
        return workingDirectory;
    }

    public boolean evaluate(){
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
