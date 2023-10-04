package interpreter.program;

import interpreter.syntax.expressions.Expression;

public class Definition {
    private final String identifier;
    private final DefinitionType type;

    public String getIdentifier() {
        return identifier;
    }

    public DefinitionType getType() {
        return type;
    }

    public Expression getExpression() {
        return expression;
    }

    private Expression expression;

    public Definition(String identifier, Expression expression, DefinitionType type){
        this.identifier = identifier;
        this.type = type;
        this.expression = expression;
    }

    public void setExpression(Expression expression){
        this.expression = expression;
    }

}
