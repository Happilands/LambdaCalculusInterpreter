package interpreter.syntax;

public enum TokenType {
    NONE,
    ASSIGNMENT,
    IDENTIFIER,
    LAMBDA,
    DOT,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    TERMINATOR;

    public static TokenType getTypeFromString(String s) {
        return switch (s) {
            case ":=" -> ASSIGNMENT;
            case "λ" -> LAMBDA;
            case "l" -> LAMBDA;
            case "L" -> LAMBDA;
            case "." -> DOT;
            case ";" -> TERMINATOR;
            case "(" -> OPEN_BRACKET;
            case ")" -> CLOSE_BRACKET;
            default -> IDENTIFIER;
        };
    }
}
