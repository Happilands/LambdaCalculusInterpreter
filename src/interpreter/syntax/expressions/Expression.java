package interpreter.syntax.expressions;

import interpreter.exception.LambdaError;
import interpreter.exception.SyntaxError;
import interpreter.program.ExpressionFormatter;
import interpreter.program.Program;
import interpreter.syntax.Token;

public abstract class Expression {
    public static boolean callByNameMode(){
        return true;
    }

    /**
     * Simplifies this expression.
     * @return the simplest form
     */
    public abstract Expression simplify();

    public abstract ExpressionType getType();

    public abstract Expression createCopy();

    public abstract void format(ExpressionFormatter formatter);

    public static Expression parse(Program program) throws LambdaError {

        Token first = program.getTokenStack().peek();

        return switch (first.getType()){
            case OPEN_BRACKET -> Sequence.parseBracket(program);
            case LAMBDA -> Lambda.parse(program);
            case IDENTIFIER -> Identifier.parse(program);
            default -> throw new SyntaxError(String.format("Invalid expression, found '%s'", first.getString()),
                    first.getLine(), first.getCharacter());
        };
    }

    @Override
    public String toString() {
        ExpressionFormatter formatter = new ExpressionFormatter();
        format(formatter);
        return formatter.getBuilder().toString();
    }
}
