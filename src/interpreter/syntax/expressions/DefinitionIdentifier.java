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
        // Definitions should already be simplified, so no need to call evaluate() on the copy
        return definition.getExpression().createCopy();
    }
}
