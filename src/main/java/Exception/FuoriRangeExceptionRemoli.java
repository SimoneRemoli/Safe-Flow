package Exception;

import java.time.LocalDateTime;

/**
 * Eccezione lanciata quando un indice (ID stazione, nodo, ecc.)
 * è fuori dal range della matrice di adiacenza.
 */
public class FuoriRangeExceptionRemoli extends RuntimeException {

    private final String errorCode;
    private final String userMessage;
    private final String technicalMessage;
    private final LocalDateTime timestamp;
    private final Severity severity;

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public FuoriRangeExceptionRemoli(String technicalMessage) {
        super(technicalMessage);
        this.errorCode = "ERR-RANGE-REMOLI";
        this.userMessage = "Parametro fuori dal range consentito. Verifica la stazione selezionata.";
        this.technicalMessage = technicalMessage;
        this.timestamp = LocalDateTime.now();
        this.severity = Severity.HIGH;
    }

    public String getErrorCode() { return errorCode; }
    public String getUserMessage() { return userMessage; }
    public String getTechnicalMessage() { return technicalMessage; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Severity getSeverity() { return severity; }

    @Override
    public String toString() {
        return "FuoriRangeExceptionRemoli {" +
                "errorCode='" + errorCode + '\'' +
                ", userMessage='" + userMessage + '\'' +
                ", technicalMessage='" + technicalMessage + '\'' +
                ", severity=" + severity +
                ", timestamp=" + timestamp +
                '}';
    }
}
