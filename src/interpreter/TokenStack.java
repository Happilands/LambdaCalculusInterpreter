package interpreter;

import interpreter.exception.EOFError;
import interpreter.exception.SyntaxError;
import interpreter.syntax.Token;
import interpreter.syntax.TokenType;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenStack {
    private final List<Token> tokens;
    private int index = 0;
    private int currentLine = 1;
    private int currentChar = 0;
    private int lastNewLineChar = 0;
    private final String code;

    public TokenStack(String code){
        tokens = new ArrayList<>();
        this.code = code;

        tokenize();
    }

    public void print(){
        tokens.forEach(token -> System.out.println(token.getString()));
    }

    private void updateLineCharacter(int toChar){
        for(; currentChar < toChar; currentChar++) {
            if (code.charAt(currentChar) == '\n') {
                currentLine++;
                lastNewLineChar = currentChar;
            }
        }
    }

    public Token peek(){
        if(hasEnded())
            throw new EOFError();

        return tokens.get(index);
    }

    public boolean hasEnded(){
        return index >= tokens.size();
    }

    public Token expect(TokenType type) {
        if(hasEnded())
            throw new RuntimeException("Expected " + type.toString().toLowerCase() + " but reached end of file");

        Token token = tokens.get(index);
        index++;

        if(token.getType() == type)
            return token;

        throw new SyntaxError(
                "Expected '" + type.getRepresentation() + "' but found '" + token.getString() + "'",
                token.getLine(), token.getCharacter()
        );
    }

    private void tokenize(){
        Matcher m = Pattern.compile("//.*|(?s)/\\*.*?\\*/|([λlL.;()]|:=|[a-zA-Z0-9_]+|\"[^\"]*\")|\\s")
                .matcher(code);

        int lastEnd = 0;

        while (m.find()) {
            if(m.start() != lastEnd){
                updateLineCharacter(m.start());
                throw new SyntaxError("Unexpected character '" + code.charAt(lastEnd) + "'",
                        currentLine, m.start() - lastNewLineChar - 2);
            }
            lastEnd = m.end();

            // Strip whitespace
            if(m.group().length() == 1 && m.group().trim().isEmpty()){
                continue;
            }
            // Strip comments
            if(m.group().charAt(0) == '/')
                continue;

            updateLineCharacter(m.start());

            int tokenLineNumber = currentLine;
            int tokenCharNumber = m.start() - lastNewLineChar - 1;

            if(m.group().charAt(0) == '\"'){
                String s = m.group().substring(1, m.group().length() - 1);
                tokens.add(new Token(s, tokenLineNumber, tokenCharNumber, TokenType.IDENTIFIER));
                continue;
            }

            tokens.add(new Token(m.group(), tokenLineNumber, tokenCharNumber));
        }
    }
}
