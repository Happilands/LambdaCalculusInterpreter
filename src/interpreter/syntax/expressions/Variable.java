package interpreter.syntax.expressions;

import interpreter.program.ExpressionFormatter;

import java.util.Objects;

public class Variable extends Expression{
    private final String name;
    private Expression substitution;
    private final Lambda parent;

    public Variable(String name, Lambda parent){
        this.name = name;
        this.substitution = null;
        this.parent = parent;

        parent.registerVariable(this);
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
        return parent.registerVariableCopy();
    }

    @Override
    public void format(ExpressionFormatter formatter) {
        formatter.getBuilder().append(formatter.getPrime(parent));
    }
}
