package interpreter.program;

import interpreter.exception.SyntaxError;
import interpreter.syntax.Token;
import interpreter.syntax.expressions.Expression;

import java.util.*;

public class DefinitionStack {
    private final Map<String, Stack<Definition>> id2definition;
    private final Stack<String> definitionStack;
    private final Map<String, Stack<Definition>> format2definition;
    private final Map<Definition, String> def2format;

    public DefinitionStack(){
        id2definition = new HashMap<>();
        definitionStack = new Stack<>();
        format2definition = new HashMap<>();
        def2format = new HashMap<>();
    }

    public void push(Definition definition){
        definitionStack.push(definition.getIdentifier());

        Stack<Definition> idStack = id2definition.computeIfAbsent(definition.getIdentifier(), (id) -> new Stack<>());

        idStack.push(definition);
    }

    public void assign(Definition definition, Expression expression){
        definition.setExpression(expression);

        String format = expression.toString();
        def2format.put(definition, format);

        Stack<Definition> defStack = format2definition.computeIfAbsent(format, (__) -> new Stack<>());
        defStack.push(definition);
    }

    public String formatExpression(Expression expression){
        String format = expression.toString();

        Stack<Definition> defStack = format2definition.get(format);

        if(defStack == null)
            return format;
        else
            return format + "\n\tequivalent to: " + defStack.peek().getIdentifier();
    }

    public void pop(){
        String id = definitionStack.pop();
        Stack<Definition> idStack = id2definition.get(id);
        Definition def = idStack.pop();

        // if(idStack.isEmpty()) definitions.remove(id);
    }

    public Definition find(Token identifier){

        Stack<Definition> idStack = id2definition.get(identifier.getString());

        if(idStack.isEmpty())
            throw new SyntaxError("Found undefined identifier: '" + identifier.getString() + "'",
                    identifier.getLine(), identifier.getCharacter());

        return idStack.peek();
    }
}
