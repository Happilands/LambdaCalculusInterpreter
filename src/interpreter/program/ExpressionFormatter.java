package interpreter.program;

import interpreter.syntax.expressions.Lambda;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class ExpressionFormatter {
    private final Map<String, Integer> name2primeCount;
    private final Map<Lambda, Integer> lambda2primeCount;
    private final StringBuilder builder;

    public ExpressionFormatter(){
        name2primeCount = new HashMap<>();
        lambda2primeCount = new HashMap<>();
        builder = new StringBuilder();
    }

    public StringBuilder getBuilder(){
        return builder;
    }

    public void push(Lambda lambda){
        int primeCount = name2primeCount.merge(lambda.getName(), 1, Integer::sum);
        lambda2primeCount.put(lambda, primeCount);
    }

    public String getPrime(Lambda lambda){
        if(lambda2primeCount.get(lambda) == null)
            return lambda.getName();
        int count = Objects.requireNonNull(lambda2primeCount.get(lambda));
        return lambda.getName() + "'".repeat(count - 1);
    }

    public void pop(Lambda lambda){
        name2primeCount.merge(lambda.getName(), -1, Integer::sum);
        Objects.requireNonNull(lambda2primeCount.remove(lambda));
    }
}
