package interpreter.syntax.expressions;

import interpreter.exception.SyntaxError;
import interpreter.program.ExpressionFormatter;
import interpreter.program.Program;
import interpreter.syntax.Token;

public abstract class Expression {
    public abstract Expression evaluate();

    public abstract ExpressionType getType();

    public abstract Expression createCopy();

    public abstract Expression substituteAndEvaluate(Identifier identifier, Expression expression);

    public abstract Expression substitute(Identifier identifier, Expression expression);

    public abstract void format(ExpressionFormatter formatter);

    public static Expression parse(Program program){

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
