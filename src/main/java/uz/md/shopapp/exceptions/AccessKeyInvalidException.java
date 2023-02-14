package uz.md.shopapp.exceptions;

public class AccessKeyInvalidException extends RuntimeException {
    public AccessKeyInvalidException(String message) {
        super(message);
    }
}
