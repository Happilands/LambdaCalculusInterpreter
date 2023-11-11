package interpreter.syntax.expressions;

import interpreter.exception.LambdaError;
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

    public Expression evaluate() {
        return definition.getExpression().createCopy();
    }

    @Override
    public Expression simplify(){
        return definition.getExpression().createCopy().simplify();
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

    public static Expression parse(Program program) throws LambdaError {
        Token identifier = program.getTokenStack().expect(TokenType.IDENTIFIER);

        Definition definition = program.getDefinitionStack().find(identifier);

        return switch (definition.getType()){
            case LAMBDA -> new Variable((Lambda) definition.getExpression());
            case ASSIGNMENT -> new Identifier(definition);
        };
    }
}
