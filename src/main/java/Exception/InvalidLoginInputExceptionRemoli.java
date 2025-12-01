package Exception;

import java.time.LocalDateTime;

public class InvalidLoginInputExceptionRemoli extends Exception {

    private final String errorCode;
    private final String userMessage;
    private final String technicalMessage;
    private final Severity severity;
    private final LocalDateTime timestamp;

    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }

    public InvalidLoginInputExceptionRemoli(String userMessage,
                                            String technicalMessage,
                                            Severity severity) {
        super(technicalMessage);
        this.errorCode = "ERR-LOGIN-INPUT";
        this.userMessage = userMessage;
        this.technicalMessage = technicalMessage;
        this.severity = severity;
        this.timestamp = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return "InvalidLoginInputExceptionRemoli {" +
                "errorCode='" + errorCode + '\'' +
                ", userMessage='" + userMessage + '\'' +
                ", technicalMessage='" + technicalMessage + '\'' +
                ", severity=" + severity +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getTechnicalMessage() {
        return technicalMessage;
    }
}
