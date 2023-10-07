package interpreter.syntax.expressions;

import interpreter.program.ExpressionFormatter;
import interpreter.program.Program;
import interpreter.syntax.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends Expression{
    private final List<Expression> expressions;

    private void addExpression(Expression expression){
        expressions.add(expression);
    }

    private void replaceExpression(int index, Expression expression){
        expressions.set(index, expression);
    }

    @Override
    public Expression evaluate() {
        while (expressions.size() > 1) {
            replaceExpression(0, expressions.get(0).evaluate());
            if (expressions.get(0).getType() == ExpressionType.VOID) {
                expressions.remove(0);
                continue;
            }

            Expression left = expressions.get(0);
            Expression right = expressions.get(1);

            if (right.getType() == ExpressionType.VOID) {
                expressions.remove(1);
                continue;
            }

            if (left.getType() == ExpressionType.FUNCTION) {
                replaceExpression(0, ((Function) left).takeInput(right));
                expressions.remove(1);
                continue;
            }

            // if the first expression in the sequence isn't a lambda, simplify the rest and return
            for (int i = 1; i < expressions.size(); i++) {
                replaceExpression(i, expressions.get(i).evaluate());
                if (expressions.get(i).getType() == ExpressionType.VOID) {
                    expressions.remove(i);
                    i--;
                }
            }
            return this;
        }

        if(expressions.size() == 1)
            return expressions.get(0).evaluate();
        else
            return Void.instance;
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.SEQUENCE;
    }

    @Override
    public Expression createCopy() {
        Sequence copy = new Sequence();
        for (Expression expression : expressions) {
            copy.addExpression(expression.createCopy());
        }
        return copy;
    }

    @Override
    public void format(ExpressionFormatter formatter) {
        formatter.getBuilder().append('(');
        for (int i = 0; i < expressions.size(); i++) {
            Expression expression = expressions.get(i);
            expression.format(formatter);
            if(i < expressions.size() - 1)
                formatter.getBuilder().append(' ');
        }
        formatter.getBuilder().append(')');
    }

    public Sequence(){
        expressions = new ArrayList<>();
    }

    public static Sequence parseAny(Program program){
        Sequence sequence = new Sequence();
        List<TokenType> closeTypes = List.of(TokenType.TERMINATOR, TokenType.CLOSE_BRACKET);

        while (!closeTypes.contains(program.getTokenStack().peek().getType())){
            sequence.addExpression(Expression.parse(program));
        }

        return sequence;
    }

    public static Sequence parseSemicolon(Program program){
        Sequence sequence = new Sequence();

        while (program.getTokenStack().peek().getType() != TokenType.TERMINATOR){
            sequence.addExpression(Expression.parse(program));
        }

        return sequence;
    }

    public static Sequence parseBracket(Program program){
        Sequence sequence = new Sequence();

        program.getTokenStack().expect(TokenType.OPEN_BRACKET);

        while (program.getTokenStack().peek().getType() != TokenType.CLOSE_BRACKET){
            sequence.addExpression(Expression.parse(program));
        }

        program.getTokenStack().expect(TokenType.CLOSE_BRACKET);

        return sequence;
    }
}
