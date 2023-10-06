package interpreter.syntax.statements;

import interpreter.program.ExpressionFormatter;
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
    public void format(ExpressionFormatter formatter) {
        formatter.getBuilder().append("print");
    }

    @Override
    public Expression takeInput(Expression expression) {
        System.out.println(expression.evaluate());
        return Void.instance;
    }
}
