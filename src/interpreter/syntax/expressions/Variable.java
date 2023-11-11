package interpreter.syntax.expressions;

import interpreter.program.ExpressionFormatter;

import java.util.Objects;

public class Variable extends Expression{
    private final Lambda lambda;

    public Variable(Lambda lambda){
        this.lambda = lambda;

        lambda.registerVariable(this);
    }

    public Expression evaluate(){
        Expression substitution = lambda.getSubstitution();

        if(substitution == null)
            return this;

        return substitution.createCopy();
    }

    @Override
    public Expression simplify() {
        Expression substitution = lambda.getSubstitution();

        if(substitution == null)
            return this;

        return substitution.createCopy().simplify();
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
        if(lambda.getSubstitution() == null)
            formatter.getBuilder().append(formatter.getPrime(lambda));
        else
            lambda.getSubstitution().format(formatter);
    }
}
