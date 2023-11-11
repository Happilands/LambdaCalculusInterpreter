package interpreter.program;

import interpreter.syntax.expressions.*;

public class Evaluator {
    private final Sequence root;

    public Evaluator(Expression expression) {
        root = new Sequence();
        root.addExpression(expression);
    }

    public Expression evaluate() {
        while (doStep()) {
            //System.out.println(getFirst(root));
        }

        return getFirst(root);
    }

    private Expression getFirst(Expression parent){
        if(parent.getType() == ExpressionType.LAMBDA)
            return ((Lambda)parent).getBody();
        else
            return ((Sequence)parent).getExpressions().get(0);
    }

    private int getSize(Expression parent){
        if(parent.getType() == ExpressionType.LAMBDA)
            return 1;
        else
            return ((Sequence)parent).getExpressions().size();
    }

    private void replaceFirst(Expression parent, Expression replacement){
        if(parent.getType() == ExpressionType.LAMBDA)
            ((Lambda)parent).setBody(replacement);
        else
            ((Sequence)parent).replaceExpression(0, replacement);
    }

    private boolean doStep() {
        Expression parent = root;

        while (true) {
            Expression current = getFirst(parent);

            if (current.getType() == ExpressionType.LAMBDA) {
                if (getSize(parent) > 1) {
                    Sequence parentAsSequence = (Sequence)parent;
                    Lambda lambda = (Lambda) current;

                    Expression right = parentAsSequence.getExpressions().get(1);
                    parentAsSequence.replaceExpression(0, lambda.takeInputEvaluate(right));
                    parentAsSequence.removeExpression(1);
                    //System.out.printf("Substituted '%s' with '%s'%n", lambda.getName(), right);
                    return true;
                } else {
                    parent = current;
                    continue;
                }
            }

            if(current.getType() == ExpressionType.SEQUENCE){
                Sequence sequence = (Sequence)current;

                if(sequence.getExpressions().size() == 1) {
                    replaceFirst(parent, sequence.getExpressions().get(0));
                    continue;
                }

                parent = current;
                continue;
            }

            if (current.getType() == ExpressionType.IDENTIFIER) {
                Expression replacement = ((Identifier) current).evaluate();
                replaceFirst(parent, replacement);
                //System.out.printf("Replaced identifier '%s' with '%s' %n", current, replacement);
                continue;
            }

            if (current.getType() == ExpressionType.VARIABLE) {
                Expression replacement = ((Variable) current).evaluate();
                replaceFirst(parent, replacement);
                //System.out.printf("Replaced variable '%s' with '%s' %n", current, replacement);
                if(current != replacement)
                    continue;
            }

            if(parent.getType() == ExpressionType.SEQUENCE) {
                // PARENT MUST BE SEQUENCE
                // CURRENT MUST BE VARIABLE

                Sequence parentAsSequence = (Sequence) parent;

                for (int i = 1; i < parentAsSequence.getExpressions().size(); i++) {
                    Evaluator evaluator = new Evaluator(parentAsSequence.getExpressions().get(i));
                    parentAsSequence.replaceExpression(i, evaluator.evaluate());
                }
            }

            return false;
        }
    }
}
