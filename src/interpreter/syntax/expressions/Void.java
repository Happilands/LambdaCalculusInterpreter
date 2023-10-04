package interpreter.syntax.expressions;

public class Void extends Expression{
    public static Void instance = new Void();

    @Override
    public Expression evaluate() {
        return this;
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.VOID;
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
        builder.append("void");
    }
}
