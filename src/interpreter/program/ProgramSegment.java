package interpreter.program;

import interpreter.TokenStack;
import interpreter.exception.LambdaError;
import interpreter.syntax.statements.Statement;

public class ProgramSegment {
    private final TokenStack tokenStack;

    public ProgramSegment(String code){
        tokenStack = new TokenStack(code);
    }

    public TokenStack getTokenStack(){
        return tokenStack;
    }

    public void parse(Program program) throws LambdaError {
        while (!tokenStack.hasEnded())
            program.addStatement(Statement.parseStatement(program));
    }
}
