package interpreter.program;

import interpreter.TokenStack;
import interpreter.syntax.statements.Statement;

public class ProgramSegment {
    private final TokenStack tokenStack;

    public ProgramSegment(String code){
        tokenStack = new TokenStack(code);
    }

    public TokenStack getTokenStack(){
        return tokenStack;
    }

    public boolean parse(Program program) {
        try {
            while (!tokenStack.hasEnded())
                program.addStatement(Statement.parseStatement(program));
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
