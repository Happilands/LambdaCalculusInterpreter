package interpreter.syntax;

public enum TokenType {
    ASSIGNMENT,
    IDENTIFIER,
    STRING,
    LAMBDA,
    DOT,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    TERMINATOR;

    public String getRepresentation(){
        return switch (this){
            case ASSIGNMENT -> ":=";
            case STRING -> "string";
            case IDENTIFIER -> "identifier";
            case LAMBDA -> "λ";
            case DOT -> ".";
            case OPEN_BRACKET -> "(";
            case CLOSE_BRACKET -> ")";
            case TERMINATOR -> ";";
        };
    }

    public static TokenType getTypeFromString(String s) {
        return switch (s) {
            case ":=" -> ASSIGNMENT;
            case "λ" -> LAMBDA;
            case "." -> DOT;
            case ";" -> TERMINATOR;
            case "(" -> OPEN_BRACKET;
            case ")" -> CLOSE_BRACKET;
            default -> IDENTIFIER;
        };
    }
}
