package interpreter;

import interpreter.syntax.Token;
import interpreter.syntax.TokenType;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenStack {
    private final List<Token> tokens;
    private int index = 0;

    public TokenStack(String program){
        tokens = new ArrayList<>();

        tokenize(program);
    }

    public void print(){
        tokens.forEach(token -> System.out.println(token.getName()));
    }

    public Token peek(){
        return tokens.get(index);
    }

    public Token poll(){
        if(hasEnded())
            throw new RuntimeException("Unexpected end of file!");

        Token result = tokens.get(index);

        index++;
        return result;
    }

    public boolean hasEnded(){
        return index >= tokens.size();
    }

    public Token expect(TokenType type){
        Token token = tokens.get(index);
        index++;

        if(token.getType() == type)
            return token;

        throw new RuntimeException("Expected " + type + "!");
    }

    private void tokenize(String program){

        Matcher m = Pattern.compile("//.*|(?s)/\\*.*?\\*/|([λlL.;()]|:=|[a-zA-Z0-9_]+|\"[^\"]*\")")
                .matcher(program);

        while (m.find()) {
            // Strip comments
            if(m.group().charAt(0) == '<' || m.group().charAt(0) == '/')
                continue;

            if(m.group().charAt(0) == '\"'){
                String s = m.group().substring(1, m.group().length() - 1);
                tokens.add(new Token(s, TokenType.IDENTIFIER));
                continue;
            }

            tokens.add(new Token(m.group()));
        }
    }
}
