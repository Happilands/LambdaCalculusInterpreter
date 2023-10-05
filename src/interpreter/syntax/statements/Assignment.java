package interpreter.syntax.statements;

import interpreter.program.Definition;
import interpreter.program.DefinitionType;
import interpreter.program.Program;
import interpreter.syntax.TokenType;
import interpreter.syntax.expressions.Expression;
import interpreter.syntax.expressions.Identifier;
import interpreter.syntax.expressions.Sequence;

public class Assignment extends Statement{
    private String identifier;
    private Expression body;
    private Definition definition;

    @Override
    public void run(Program program) {
        body = body.evaluate();

        program.getDefinitionStack().assign(definition, body);
    }

    public String getIdentifier(){
        return identifier;
    }

    public Expression getExpression(){
        return body;
    }

    public static Assignment parseAssignment(Program program) {
        Assignment assignment = new Assignment();

        assignment.identifier = program.getTokenStack().expect(TokenType.IDENTIFIER).getString();
        program.getTokenStack().expect(TokenType.ASSIGNMENT);

        assignment.body = Sequence.parseSemicolon(program);

        // Expect semicolon
        program.getTokenStack().expect(TokenType.TERMINATOR);

        assignment.definition = new Definition(
                assignment.identifier,
                null,
                DefinitionType.ASSIGNMENT
        );

        program.getDefinitionStack().push(assignment.definition);

        return assignment;
    }
}
