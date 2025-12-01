package Exception;

import java.time.LocalDateTime;

public class InvalidPriceCalculationExceptionRemoli extends Exception {

    private final String errorCode;
    private final String userMessage;
    private final String technicalMessage;
    private final Severity severity;
    private final LocalDateTime timestamp;

    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }

    public InvalidPriceCalculationExceptionRemoli(String userMessage,
                                                  String technicalMessage,
                                                  Severity severity) {
        super(technicalMessage);
        this.errorCode = "ERR-PRICE-CALCULATION";
        this.userMessage = userMessage;
        this.technicalMessage = technicalMessage;
        this.severity = severity;
        this.timestamp = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return "InvalidPriceCalculationExceptionRemoli {" +
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
}
