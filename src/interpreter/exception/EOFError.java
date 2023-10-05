package interpreter.exception;

public class EOFError extends RuntimeException{
    public EOFError() {
        super("SyntaxError: Unexpected end of file reached");
    }
}
