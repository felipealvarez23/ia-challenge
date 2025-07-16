package co.com.bancolombia.model.exception;

public class InvalidHashException extends RuntimeException  {
    public InvalidHashException(String message) {
        super(message);
    }
}
