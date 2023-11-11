package interpreter;

import interpreter.exception.EOFError;
import interpreter.exception.LambdaError;
import interpreter.exception.SyntaxError;
import interpreter.syntax.Token;
import interpreter.syntax.TokenType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenStack {
    private final Queue<Token> tokens;
    private int currentLine = 1;
    private int currentChar = 0;
    private int lastNewLineChar = 0;

    public TokenStack(String code) {
        tokens = new ArrayDeque<>();

        tokenize(code);
    }

    public void print() {
        tokens.forEach(token -> System.out.println(token.getString()));
    }

    private void updateLineCharacter(String code, int toChar) {
        for (; currentChar < toChar; currentChar++) {
            if (code.charAt(currentChar) == '\n') {
                currentLine++;
                lastNewLineChar = currentChar;
            }
        }
    }

    public Token peek() {
        if (hasEnded())
            throw new EOFError();

        return tokens.peek();
    }

    public boolean hasEnded() {
        return tokens.isEmpty();
    }

    public Token expect(TokenType type) throws LambdaError {
        if (hasEnded())
            throw new EOFError(type.toString().toLowerCase());

        Token token = tokens.poll();

        if (token.getType() == type)
            return token;

        throw new SyntaxError(
                "Expected '" + type.getRepresentation() + "' but found '" + token.getString() + "'",
                token.getLine(), token.getCharacter()
        );
    }

    private void tokenize(String code) {
        currentLine = 1;
        currentChar = 0;
        lastNewLineChar = 0;

        Matcher m = Pattern.compile("//.*|(?s)/\\*.*?\\*/|([λ.;()]|:=|[a-zA-Z0-9_]+|\"[^\"]*\")|\\s")
                .matcher(code);

        int lastEnd = 0;

        while (m.find()) {
            if (m.start() != lastEnd) {
                updateLineCharacter(code, m.start());
                throw new SyntaxError("Unexpected character '" + code.charAt(lastEnd) + "'",
                        currentLine, m.start() - lastNewLineChar - 2);
            }
            lastEnd = m.end();

            // Strip whitespace
            if (m.group().length() == 1 && m.group().trim().isEmpty()) {
                continue;
            }
            // Strip comments
            if (m.group().charAt(0) == '/')
                continue;

            updateLineCharacter(code, m.start());

            int tokenLineNumber = currentLine;
            int tokenCharNumber = m.start() - lastNewLineChar - 1;

            if (m.group().charAt(0) == '\"') {
                String s = m.group().substring(1, m.group().length() - 1);
                tokens.add(new Token(s, tokenLineNumber, tokenCharNumber, TokenType.STRING));
                continue;
            }

            tokens.add(new Token(m.group(), tokenLineNumber, tokenCharNumber));
        }
    }
}