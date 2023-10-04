package interpreter.syntax.expressions;

import interpreter.program.Definition;
import interpreter.program.Program;
import interpreter.syntax.TokenType;

public class Identifier extends Expression{
    private final String name;

    @Override
    public Expression evaluate() {
        return this;
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.IDENTIFIER;
    }

    public String getName(){
        return name;
    }

    @Override
    public Expression createCopy() {
        return this;
    }

    @Override
    public Expression substituteAndEvaluate(Identifier identifier, Expression expression) {
        if(this.equals(identifier))
            return expression.createCopy();
        else
            return this;
    }

    @Override
    public Expression substitute(Identifier identifier, Expression expression) {
        if(this.equals(identifier))
            return expression.createCopy();
        else
            return this;
    }

    @Override
    public void buildString(StringBuilder builder) {
        builder.append(name);
    }

    public Identifier(String name){
        this.name = name;
    }

    public static Expression parse(Program program){
        String identifier = program.getTokenStack().expect(TokenType.IDENTIFIER).getName();

        Definition definition = program.getDefinitionStack().find(identifier);

        return switch (definition.getType()){
            case LAMBDA -> definition.getExpression();
            case DEFINITION -> new DefinitionIdentifier(definition);
        };

        /*return switch (identifier){
            case "print" -> PrintExp.instance;
            default -> new Identifier(identifier);
        };*/
    }
}
