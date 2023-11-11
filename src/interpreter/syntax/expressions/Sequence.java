package interpreter.syntax.expressions;

import interpreter.exception.LambdaError;
import interpreter.exception.SyntaxError;
import interpreter.program.ExpressionFormatter;
import interpreter.program.Program;
import interpreter.syntax.Token;
import interpreter.syntax.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends Expression{
    private final List<Expression> expressions;

    public List<Expression> getExpressions(){
        return expressions;
    }

    public void addExpression(Expression expression){
        expressions.add(expression);
    }

    public void replaceExpression(int index, Expression expression){
        expressions.set(index, expression);
    }

    public void removeExpression(int index){
        expressions.remove(index);
    }

    @Override
    public Expression simplify() {
        while (expressions.size() > 1) {
            replaceExpression(0, expressions.get(0).simplify());

            Expression left = expressions.get(0);

            if (left.getType() == ExpressionType.LAMBDA) {
                Expression right = expressions.get(1);
                replaceExpression(0, ((Lambda) left).takeInputSimplify(right));
                expressions.remove(1);
                continue;
            }

            // if the first expression in the sequence isn't a lambda, simplify the rest and return
            for (int i = 1; i < expressions.size(); i++) {
                replaceExpression(i, expressions.get(i).simplify());
            }
            return this;
        }

        return expressions.get(0).simplify();
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

    private static Sequence parse(Program program, List<TokenType> closeTypes) throws LambdaError{
        Sequence sequence = new Sequence();

        while (!closeTypes.contains(program.getTokenStack().peek().getType())){
            sequence.addExpression(Expression.parse(program));
        }

        if(sequence.expressions.isEmpty()){
            Token next = program.getTokenStack().peek();
            throw new SyntaxError("Illegal empty expression", next.getLine(), next.getCharacter());
        }

        return sequence;
    }

    public static Sequence parseAny(Program program) throws LambdaError{
        return parse(program, List.of(TokenType.TERMINATOR, TokenType.CLOSE_BRACKET));
    }

    public static Sequence parseSemicolon(Program program) throws LambdaError{
        return parse(program, List.of(TokenType.TERMINATOR));
    }

    public static Sequence parseBracket(Program program) throws LambdaError {
        program.getTokenStack().expect(TokenType.OPEN_BRACKET);
        Sequence sequence = parse(program, List.of(TokenType.CLOSE_BRACKET));
        program.getTokenStack().expect(TokenType.CLOSE_BRACKET);
        return sequence;
    }
}
