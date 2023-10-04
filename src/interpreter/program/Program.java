package interpreter.program;

import interpreter.TokenStack;
import interpreter.syntax.Token;
import interpreter.syntax.TokenType;
import interpreter.syntax.statements.Assignment;
import interpreter.syntax.statements.Print;
import interpreter.syntax.statements.Statement;

import java.util.ArrayList;
import java.util.List;

public class Program {
    private final List<Statement> statements;
    private final DefinitionStack definitionStack;
    private final TokenStack tokenStack;

    public Program(TokenStack tokenStack){
        statements = new ArrayList<>();
        definitionStack = new DefinitionStack();
        this.tokenStack = tokenStack;
    }

    public DefinitionStack getDefinitionStack(){
        return definitionStack;
    }

    public TokenStack getTokenStack(){
        return tokenStack;
    }

    public void parse() {
        while (!tokenStack.hasEnded()){
            Token next = tokenStack.peek();

            if(next.getName().equals("print") || next.getName().equals("println")){
                statements.add(Print.parsePrint(this));
            }
            else if(next.getType() == TokenType.IDENTIFIER)
                statements.add(Assignment.parseAssignment(this));
            else
                throw new RuntimeException("Not a valid start of a statement!");
        }
    }

    public void evaluate(){
        for(Statement statement : statements) {
            statement.run(this);
        }
    }
}
