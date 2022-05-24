package co.com.bancolombia.binstash;

public class InvalidKeyException extends RuntimeException{
    public InvalidKeyException(String message) {
        super(message);
    }
}
