package interpreter.syntax.expressions;

import interpreter.program.Program;
import interpreter.syntax.Token;

public abstract class Expression {
    public abstract Expression evaluate();

    public abstract ExpressionType getType();

    public abstract Expression createCopy();

    public abstract Expression substituteAndEvaluate(Identifier identifier, Expression expression);

    public abstract Expression substitute(Identifier identifier, Expression expression);

    public abstract void buildString(StringBuilder builder);

    public static Expression parse(Program program){

        Token first = program.getTokenStack().peek();

        return switch (first.getType()){
            case OPEN_BRACKET -> Sequence.parseBracket(program);
            case LAMBDA -> Lambda.parse(program);
            case IDENTIFIER -> Identifier.parse(program);
            default -> throw new RuntimeException("Not a valid expression!");
        };
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        buildString(builder);
        return builder.toString();
    }
}
