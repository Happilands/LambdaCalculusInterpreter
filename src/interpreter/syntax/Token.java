package interpreter.syntax;

public class Token{
    private final String string;
    private final int line;
    private final int character;
    private final TokenType type;

    public Token(String s, int line, int character){
        this.string = s;
        this.line = line;
        this.character = character;
        this.type = TokenType.getTypeFromString(s);
    }

    public Token(String s, int line, int character, TokenType type){
        this.string = s;
        this.line = line;
        this.character = character;
        this.type = type;
    }

    public String getString(){
        return string;
    }

    public TokenType getType(){
        return type;
    }

    public int getLine() {
        return line;
    }

    public int getCharacter() {
        return character;
    }
}