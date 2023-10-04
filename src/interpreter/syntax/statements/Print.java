package interpreter.syntax.statements;

import interpreter.program.Program;
import interpreter.syntax.Token;
import interpreter.syntax.TokenType;
import interpreter.syntax.expressions.Expression;
import interpreter.syntax.expressions.Identifier;
import interpreter.syntax.expressions.Sequence;

public class Print extends Statement{
    private Expression body;
    private boolean newline;

    @Override
    public void replace(Identifier identifier, Expression expression) {
        body = body.substitute(identifier, expression);
    }

    @Override
    public void run(Program program) {
        body = body.evaluate();

        StringBuilder builder = new StringBuilder();
        body.buildString(builder);

        if(newline) System.out.println(builder);
        else System.out.print(builder);
    }

    public static Print parsePrint(Program program){
        Print print = new Print();

        // Expect print keyword
        Token keyword = program.getTokenStack().expect(TokenType.IDENTIFIER);

        if(!keyword.getName().equals("print") && !keyword.getName().equals("println"))
            throw new RuntimeException("Expected print keyword!");

        print.body = Sequence.parseSemicolon(program);
        print.newline = keyword.getName().equals("println");

        // Expect semicolon
        program.getTokenStack().expect(TokenType.TERMINATOR);
        return print;
    }
}