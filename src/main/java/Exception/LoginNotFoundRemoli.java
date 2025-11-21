package Exception;

public class LoginNotFoundRemoli extends Exception {

    private final String email;
    private final String maskedPassword;

    public LoginNotFoundRemoli(String message, String email, String password) {
        super(message);
        this.email = email;
        this.maskedPassword = mask(password);
    }

    private String mask(String pwd) {
        if (pwd == null) return "null";
        return "*".repeat(Math.max(1, pwd.length()));
    }

    public String getEmail() {
        return email;
    }

    public String getMaskedPassword() {
        return maskedPassword;
    }
}

