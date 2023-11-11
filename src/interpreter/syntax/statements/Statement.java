package interpreter.syntax.statements;

import interpreter.exception.LambdaError;
import interpreter.exception.SyntaxError;
import interpreter.program.Program;
import interpreter.syntax.Token;
import interpreter.syntax.TokenType;

public abstract class Statement {
    public abstract void run(Program program);

    public static Statement parseStatement(Program program) throws LambdaError {
        Token next = program.getTokenStack().peek();

        if (next.getString().equals("print") || next.getString().equals("println"))
            return Print.parsePrint(program);
        else if(next.getString().equals("import"))
            return Import.parseImport(program);
        else if (next.getType() == TokenType.IDENTIFIER)
            return Assignment.parseAssignment(program);


        throw new SyntaxError("Unexpected token: '" + next.getString() + "'",
                next.getLine(), next.getCharacter());
    }
}
