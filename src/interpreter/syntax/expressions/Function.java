package interpreter.syntax.expressions;

public abstract class Function extends Expression {
    public abstract Expression takeInput(Expression expression);

    @Override
    public ExpressionType getType() {
        return ExpressionType.FUNCTION;
    }
}
