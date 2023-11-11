package interpreter.syntax.expressions;

import interpreter.exception.LambdaError;
import interpreter.program.Definition;
import interpreter.program.DefinitionType;
import interpreter.program.ExpressionFormatter;
import interpreter.program.Program;
import interpreter.syntax.Token;
import interpreter.syntax.TokenType;

import java.util.ArrayDeque;
import java.util.Queue;

public class Lambda extends Sequence{
    private final String varName;
    private Expression body;
    private final Queue<Variable> variables;
    private Lambda copyInProgress;
    private Expression substitution;

    @Override
    public ExpressionType getType() {
        return ExpressionType.LAMBDA;
    }

    public Expression getSubstitution(){
        return substitution;
    }

    public Lambda(String varName){
        this.varName = varName;
        this.body = null;
        this.variables = new ArrayDeque<>();
    }

    public String getName() {
        return varName;
    }

    public Expression getBody(){
        return body;
    }

    public void setBody(Expression expression){
        body = expression;
    }

    @Override
    public Expression simplify() {
        body = body.simplify();
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
        return new Variable(parent);
    }

    public Expression takeInputEvaluate(Expression expression){
        /*if(!variables.isEmpty()) {
            while (variables.size() > 1) {
                variables.poll().setSubstitution(expression.createCopy());
            }
            variables.poll().setSubstitution(expression);
        }*/
        substitution = expression;

        return body;
    }

    public Expression takeInputSimplify(Expression expression){
        if(variables.isEmpty())
            return body.simplify();

        substitution = expression.simplify();

        /*if(!variables.isEmpty()) {
            while (variables.size() > 1) {
                variables.poll().setSubstitution(evaluated.createCopy());
            }
            variables.poll().setSubstitution(evaluated);
        }*/

        return body.simplify();
    }

    @Override
    public void format(ExpressionFormatter formatter) {
        formatter.push(this);

        formatter.getBuilder().append('(').append('λ').append(formatter.getPrime(this)).append('.');
        body.format(formatter);
        formatter.getBuilder().append(')');

        formatter.pop(this);
    }

    public static Lambda parse(Program program) throws LambdaError {
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
