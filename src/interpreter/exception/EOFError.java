package interpreter.exception;

public class EOFError extends LambdaError{
    public EOFError() {
        super("SyntaxError: Unexpected end of file reached");
    }

    public EOFError(String expectedToken){
        super(String.format("SyntaxError: Expected '%s' but reached end of file", expectedToken));
    }
}
