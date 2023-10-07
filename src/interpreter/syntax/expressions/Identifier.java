package interpreter.syntax.expressions;

import interpreter.program.Definition;
import interpreter.program.ExpressionFormatter;
import interpreter.program.Program;
import interpreter.syntax.Token;
import interpreter.syntax.TokenType;

public class Identifier extends Expression{
    private final Definition definition;

    public Identifier(Definition definition){
        this.definition = definition;
    }

    @Override
    public Expression evaluate(){
        // Definitions should already be simplified, so no need to call evaluate() on the copy
        return definition.getExpression().createCopy();
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.IDENTIFIER;
    }

    @Override
    public Expression createCopy() {
        return this;
    }

    @Override
    public void format(ExpressionFormatter formatter) {
        formatter.getBuilder().append(definition.getIdentifier());
    }

    public static Expression parse(Program program){
        Token identifier = program.getTokenStack().expect(TokenType.IDENTIFIER);

        Definition definition = program.getDefinitionStack().find(identifier);

        return switch (definition.getType()){
            case LAMBDA -> new Variable((Lambda) definition.getExpression());
            case ASSIGNMENT -> new Identifier(definition);
        };
    }
}
