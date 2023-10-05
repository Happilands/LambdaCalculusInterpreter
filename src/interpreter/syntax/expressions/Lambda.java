package interpreter.syntax.expressions;

import interpreter.program.Definition;
import interpreter.program.DefinitionType;
import interpreter.program.ExpressionFormatter;
import interpreter.program.Program;
import interpreter.syntax.Token;
import interpreter.syntax.TokenType;

public class Lambda extends Function{
    private final Identifier identifier;
    private Expression body;

    public Lambda(Identifier identifier, Expression body){
        this.identifier = identifier;
        this.body = body;
    }

    @Override
    public Expression evaluate() {
        body = body.evaluate();
        return this;
    }

    @Override
    public Expression takeInput(Expression expression){
        body = body.substitute(identifier, expression);

        return body.evaluate();
    }

    @Override
    public Expression createCopy() {
        return new Lambda(this.identifier, body.createCopy());
    }

    @Override
    public Expression substituteAndEvaluate(Identifier identifier, Expression expression) {
        if(identifier.equals(this.identifier))
            return this;

        body = body.substituteAndEvaluate(identifier, expression);
        return this.evaluate();
    }

    @Override
    public Expression substitute(Identifier identifier, Expression expression) {
        if(identifier.equals(this.identifier))
            return this;

        body = body.substitute(identifier, expression);
        return this;
    }

    @Override
    public void format(ExpressionFormatter formatter) {
        formatter.push(identifier);

        formatter.getBuilder().append('(').append('λ').append(formatter.getPrime(identifier)).append('.');
        body.format(formatter);
        formatter.getBuilder().append(')');

        formatter.pop(identifier);
    }

    public static Lambda parse(Program program) {
        Token lambda = program.getTokenStack().expect(TokenType.LAMBDA);
        Token varName = program.getTokenStack().expect(TokenType.IDENTIFIER);
        Token dot = program.getTokenStack().expect(TokenType.DOT);

        String idName = varName.getString();
        Identifier identifier = new Identifier(varName.getString());

        // PUSH
        program.getDefinitionStack().push(new Definition(idName, identifier, DefinitionType.LAMBDA));
        Lambda function = new Lambda(identifier, Sequence.parseAny(program));
        program.getDefinitionStack().pop();
        // POP

        return function;
    }
}
