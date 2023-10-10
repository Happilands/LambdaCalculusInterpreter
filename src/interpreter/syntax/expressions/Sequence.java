package interpreter.syntax.expressions;

import interpreter.exception.SyntaxError;
import interpreter.program.ExpressionFormatter;
import interpreter.program.Program;
import interpreter.syntax.Token;
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

            Expression left = expressions.get(0);
            Expression right = expressions.get(1);

            if (left.getType() == ExpressionType.LAMBDA) {
                replaceExpression(0, ((Lambda) left).takeInput(right));
                expressions.remove(1);
                continue;
            }

            // if the first expression in the sequence isn't a lambda, simplify the rest and return
            for (int i = 1; i < expressions.size(); i++) {
                replaceExpression(i, expressions.get(i).evaluate());
            }
            return this;
        }

        return expressions.get(0).evaluate();
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

    private static Sequence parse(Program program, List<TokenType> closeTypes){
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

    public static Sequence parseAny(Program program){
        return parse(program, List.of(TokenType.TERMINATOR, TokenType.CLOSE_BRACKET));
    }

    public static Sequence parseSemicolon(Program program){
        return parse(program, List.of(TokenType.TERMINATOR));
    }

    public static Sequence parseBracket(Program program){
        program.getTokenStack().expect(TokenType.OPEN_BRACKET);
        Sequence sequence = parse(program, List.of(TokenType.CLOSE_BRACKET));
        program.getTokenStack().expect(TokenType.CLOSE_BRACKET);
        return sequence;
    }
}
