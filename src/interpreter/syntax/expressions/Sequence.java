package interpreter.syntax.expressions;

import interpreter.program.ExpressionFormatter;
import interpreter.program.Program;
import interpreter.syntax.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends Expression{
    private final List<Expression> expressions;

    @Override
    public Expression evaluate() {
        while (expressions.size() > 1){
            expressions.set(0, expressions.get(0).evaluate());
            if(expressions.get(0).getType() == ExpressionType.VOID) {
                expressions.remove(0);
                continue;
            }

            Expression left = expressions.get(0);
            Expression right = expressions.get(1);

            if(left.getType() == ExpressionType.FUNCTION){
                expressions.set(0, ((Function)left).takeInput(right));
                expressions.remove(1);
            }
            else {
                for (int i = 1; i < expressions.size(); i++) {
                    expressions.set(i, expressions.get(i).evaluate());
                    if(expressions.get(i).getType() == ExpressionType.VOID) {
                        expressions.remove(i);
                        i--;
                    }
                }
                return this;
            }
        }

        if(expressions.size() == 1)
            return expressions.get(0).evaluate();
        else
            return Void.instance;

        /*if(expressions.size() == 1)
            return expressions.get(0).evaluate();

        int i = 0;
        while (i < expressions.size() - 1){

            expressions.set(i, expressions.get(i).evaluate());
            Expression left = expressions.get(i);
            Expression right = expressions.get(i + 1);

            if(left.getType() == ExpressionType.LAMBDA){
                Expression result = ((Lambda)left).substitute(right);
                expressions.set(i, result);
                expressions.remove(i + 1);
            }
            else i++;
        }

        if(expressions.size() == 1)
            return expressions.get(0).evaluate();

        return this;*/
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.SEQUENCE;
    }

    @Override
    public Expression createCopy() {
        Sequence copy = new Sequence();

        for (int i = 0; i < expressions.size(); i++) {
            copy.expressions.add(expressions.get(i).createCopy());
        }
        return copy;
    }



    @Override
    public Expression substituteAndEvaluate(Identifier identifier, Expression expression) {
        for (int i = 0; i < expressions.size(); i++) {
            expressions.set(i, expressions.get(i).substituteAndEvaluate(identifier, expression));
        }

        return this;
    }

    @Override
    public Expression substitute(Identifier identifier, Expression expression) {
        for (int i = 0; i < expressions.size(); i++) {
            expressions.set(i, expressions.get(i).substitute(identifier, expression));
        }

        return this;
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
            sequence.expressions.add(Expression.parse(program));
        }

        return sequence;
    }

    public static Sequence parseSemicolon(Program program){
        Sequence sequence = new Sequence();

        while (program.getTokenStack().peek().getType() != TokenType.TERMINATOR){
            sequence.expressions.add(Expression.parse(program));
        }

        return sequence;
    }

    public static Sequence parseBracket(Program program){
        Sequence sequence = new Sequence();

        program.getTokenStack().expect(TokenType.OPEN_BRACKET);

        while (program.getTokenStack().peek().getType() != TokenType.CLOSE_BRACKET){
            sequence.expressions.add(Expression.parse(program));
        }

        program.getTokenStack().expect(TokenType.CLOSE_BRACKET);

        return sequence;
    }
}
