package interpreter.syntax.expressions;

import interpreter.program.Definition;
import interpreter.program.ExpressionFormatter;
import interpreter.program.Program;
import interpreter.syntax.Token;
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
    public void format(ExpressionFormatter formatter) {
        formatter.getBuilder().append(name);
    }

    public Identifier(String name){
        this.name = name;
    }

    public static Expression parse(Program program){
        Token identifier = program.getTokenStack().expect(TokenType.IDENTIFIER);

        Definition definition = program.getDefinitionStack().find(identifier);

        return switch (definition.getType()){
            case LAMBDA -> new Variable((Lambda) definition.getExpression());
            case ASSIGNMENT -> new DefinitionIdentifier(definition);
        };

        /*return switch (identifier){
            case "print" -> PrintExp.instance;
            default -> new Identifier(identifier);
        };*/
    }
}
