package uz.md.shopapp.exceptions;

public class InvalidUserNameOrPasswordException extends RuntimeException {
    public InvalidUserNameOrPasswordException(String message) {
        super(message);
    }
}
