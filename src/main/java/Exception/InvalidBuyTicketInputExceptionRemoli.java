package Exception;

import java.time.LocalDateTime;

public class InvalidBuyTicketInputExceptionRemoli extends Exception {

  private final String errorCode;
  private final String userMessage;
  private final String technicalMessage;
  private final Severity severity;
  private final LocalDateTime timestamp;

  public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }

  public InvalidBuyTicketInputExceptionRemoli(String userMessage,
                                              String technicalMessage,
                                              Severity severity) {
    super(technicalMessage);
    this.errorCode = "ERR-BUY-TICKET-INPUT";
    this.userMessage = userMessage;
    this.technicalMessage = technicalMessage;
    this.severity = severity;
    this.timestamp = LocalDateTime.now();
  }

  public String getUserMessage() {
    return userMessage;
  }

  @Override
  public String toString() {
    return "InvalidBuyTicketInputExceptionRemoli {" +
            "errorCode='" + errorCode + '\'' +
            ", userMessage='" + userMessage + '\'' +
            ", technicalMessage='" + technicalMessage + '\'' +
            ", severity=" + severity +
            ", timestamp=" + timestamp +
            '}';
  }
}
