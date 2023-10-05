package interpreter.program;

import interpreter.syntax.expressions.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ExpressionFormatter {
    private final Map<String, Integer> toPrimeCount;
    private final Map<Identifier, Stack<Integer>> toPrimeStack;
    private final Stack<Identifier> identifierStack;
    private final StringBuilder builder;

    public ExpressionFormatter(){
        toPrimeCount = new HashMap<>();
        toPrimeStack = new HashMap<>();
        identifierStack = new Stack<>();
        builder = new StringBuilder();
    }

    public StringBuilder getBuilder(){
        return builder;
    }

    public String push(Identifier identifier){
        identifierStack.push(identifier);

        int primeCount = toPrimeCount.merge(identifier.getName(), 1, Integer::sum);
        Stack<Integer> stack = toPrimeStack.computeIfAbsent(identifier, (id) -> new Stack<>());

        stack.push(primeCount);

        return getPrime(identifier);
    }

    public String getPrime(Identifier identifier){

        int count = toPrimeStack.get(identifier).peek() - 1;

        return identifier.getName() + "'".repeat(count);
    }

    public void pop(Identifier identifier){
        if(identifier != identifierStack.pop())
            throw new RuntimeException("Invalid identifier");

        toPrimeCount.merge(identifier.getName(), -1, Integer::sum);
        toPrimeStack.get(identifier).pop();
    }
}
