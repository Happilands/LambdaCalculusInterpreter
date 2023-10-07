package interpreter.syntax.expressions;

import interpreter.program.ExpressionFormatter;

import java.util.Objects;

public class Variable extends Expression{
    private Expression substitution;
    private final Lambda lambda;

    public Variable(Lambda lambda){
        this.substitution = null;
        this.lambda = lambda;

        lambda.registerVariable(this);
    }

    public void setSubstitution(Expression expression){
        substitution = expression;
    }

    @Override
    public Expression evaluate() {
        return Objects.requireNonNullElse(substitution, this);
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.VARIABLE;
    }

    @Override
    public Expression createCopy() {
        return lambda.registerVariableCopy();
    }

    @Override
    public void format(ExpressionFormatter formatter) {
        formatter.getBuilder().append(formatter.getPrime(lambda));
    }
}
