package Exception;

public class CredentialsExceptionRemoli extends Exception {

    String details;

    public CredentialsExceptionRemoli(String message,String details) {
        super(message);
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}
