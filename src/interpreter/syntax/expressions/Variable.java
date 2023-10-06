package interpreter.syntax.expressions;

import interpreter.program.ExpressionFormatter;

public class Variable extends Expression{
    private final String name;

    public Variable(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public Expression evaluate() {
        return this;
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.VARIABLE;
    }

    @Override
    public Expression createCopy() {
        return null;
    }

    @Override
    public Expression substitute(Identifier identifier, Expression expression) {
        return null;
    }

    @Override
    public void format(ExpressionFormatter formatter) {

    }
}
