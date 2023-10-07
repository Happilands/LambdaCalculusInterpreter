package interpreter.syntax.expressions;

import interpreter.program.Definition;
import interpreter.program.DefinitionType;
import interpreter.program.ExpressionFormatter;
import interpreter.program.Program;
import interpreter.syntax.Token;
import interpreter.syntax.TokenType;

import java.util.ArrayDeque;
import java.util.Queue;

public class Lambda extends Function{
    private final String varName;
    private Expression body;
    private final Queue<Variable> variables;
    private Lambda copyInProgress;

    public Lambda(String varName){
        this.varName = varName;
        this.body = null;
        this.variables = new ArrayDeque<>();
    }

    public String getName() {
        return varName;
    }

    @Override
    public Expression evaluate() {
        body = body.evaluate();
        return this;
    }

    @Override
    public Expression createCopy() {
        copyInProgress = new Lambda(varName);

        copyInProgress.body = this.body.createCopy();

        Expression result = copyInProgress;
        copyInProgress = null;

        return result;
    }

    public void registerVariable(Variable variable){
        variables.add(variable);
    }

    public Expression registerVariableCopy(){
        Lambda parent = copyInProgress != null ? copyInProgress : this;
        return new Variable(varName, parent);
    }

    @Override
    public Expression takeInput(Expression expression){
        Expression evaluated = expression.evaluate();

        if(!variables.isEmpty()) {
            while (variables.size() > 1)
                variables.poll().setSubstitution(evaluated.createCopy());
            variables.poll().setSubstitution(evaluated);
        }

        return body.evaluate();
    }

    @Override
    public void format(ExpressionFormatter formatter) {
        formatter.push(this);

        formatter.getBuilder().append('(').append('λ').append(formatter.getPrime(this)).append('.');
        body.format(formatter);
        formatter.getBuilder().append(')');

        formatter.pop(this);
    }

    public static Lambda parse(Program program) {
        Token lambdaToken = program.getTokenStack().expect(TokenType.LAMBDA);
        Token varToken = program.getTokenStack().expect(TokenType.IDENTIFIER);
        Token dotToken = program.getTokenStack().expect(TokenType.DOT);

        String varName = varToken.getString();
        Lambda lambda = new Lambda(varName);

        // PUSH
        program.getDefinitionStack().push(new Definition(varName, lambda, DefinitionType.LAMBDA));
        lambda.body = Sequence.parseAny(program);
        program.getDefinitionStack().pop();
        // POP

        return lambda;
    }
}
