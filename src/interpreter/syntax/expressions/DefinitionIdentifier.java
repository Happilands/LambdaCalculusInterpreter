package interpreter.syntax.expressions;

import interpreter.program.Definition;

public class DefinitionIdentifier extends Identifier{
    private final Definition definition;
    public DefinitionIdentifier(Definition definition) {
        super(definition.getIdentifier());
        this.definition = definition;
    }

    @Override
    public Expression evaluate(){
        return definition.getExpression().createCopy();
    }
}
