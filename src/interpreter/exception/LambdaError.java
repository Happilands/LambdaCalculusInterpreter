package interpreter.exception;

public abstract class LambdaError extends RuntimeException{
    public LambdaError(String format) {
        super(format);
    }
}
