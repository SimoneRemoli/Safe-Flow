package Exception;

public class LoginNotFound extends RuntimeException {
    public LoginNotFound(String message) {
        super(message);
    }
}
