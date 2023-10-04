package interpreter.program;

import interpreter.syntax.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

public class DefinitionStack {
    private final List<Definition> definitions;

    public DefinitionStack(){
        definitions = new ArrayList<>();
    }

    public void push(Definition definition){
        definitions.add(definition);
    }

    public void pop(){
        definitions.remove(definitions.size() - 1);
    }

    public Definition find(String identifier){
        for (int i = definitions.size() - 1; i >= 0; i--) {
            Definition definition = definitions.get(i);

            if(identifier.equals(definition.getIdentifier()))
                return definition;
        }
        throw new RuntimeException("found undefined identifier: '" + identifier + "'");
    }
}
