package interpreter.syntax;

public class Token{
    private final String s;
    private final TokenType type;

    public Token(String s){
        this.s = s;
        this.type = TokenType.getTypeFromString(s);
    }

    public Token(String s, TokenType type){
        this.s = s;
        this.type = type;
    }

    public String getName(){
        return s;
    }

    public TokenType getType(){
        return type;
    }
}