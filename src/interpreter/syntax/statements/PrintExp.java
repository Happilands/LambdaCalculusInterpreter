package interpreter.syntax.statements;

import interpreter.syntax.expressions.Expression;
import interpreter.syntax.expressions.Function;
import interpreter.syntax.expressions.Identifier;
import interpreter.syntax.expressions.Void;

public class PrintExp extends Function {
    public static PrintExp instance = new PrintExp();

    @Override
    public Expression evaluate() {
        return this;
    }

    @Override
    public Expression createCopy() {
        return this;
    }

    @Override
    public Expression substituteAndEvaluate(Identifier identifier, Expression expression) {
        return this;
    }

    @Override
    public Expression substitute(Identifier identifier, Expression expression) {
        return this;
    }

    @Override
    public void buildString(StringBuilder builder) {
        builder.append("print");
    }

    @Override
    public Expression takeInput(Expression expression) {
        System.out.println(expression.evaluate());
        return Void.instance;
    }
}
