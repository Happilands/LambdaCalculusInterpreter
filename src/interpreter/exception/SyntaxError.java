package interpreter.exception;

public class SyntaxError extends RuntimeException{
    public SyntaxError(String errorMessage, int line, int character) {
        super(String.format("SyntaxError: %s\nat line:%d char:%d", errorMessage, line, character));
    }
}
