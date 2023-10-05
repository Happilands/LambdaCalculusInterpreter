package interpreter.syntax.statements;

import interpreter.exception.SyntaxError;
import interpreter.program.Program;
import interpreter.syntax.Token;
import interpreter.syntax.TokenType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Import extends Statement{
    @Override
    public void run(Program program) {

    }

    public static Statement parseImport(Program program){
        Import statement = new Import();

        Token keyword = program.getTokenStack().expect(TokenType.IDENTIFIER);
        Token string = program.getTokenStack().expect(TokenType.STRING);
        program.getTokenStack().expect(TokenType.TERMINATOR);

        if(!keyword.getString().equals("import"))
            throw new SyntaxError(String.format("Expected import keyword, found '%s'", keyword.getString()),
                    keyword.getLine(), keyword.getCharacter());

        Path path = Path.of(string.getString());

        if(!Files.exists(path)) {
            throw new SyntaxError(String.format("Import: file not found '%s'", string.getString())
                    , string.getLine(), string.getCharacter());
        }

        program.importFile(path);

        return statement;
    }
}
