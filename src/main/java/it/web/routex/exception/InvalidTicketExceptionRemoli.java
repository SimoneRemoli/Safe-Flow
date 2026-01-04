package it.web.routex.exception;

public class InvalidTicketExceptionRemoli extends Exception {

    private final String codiceTicket;

    public InvalidTicketExceptionRemoli(String userMessage,
                                        String details,
                                        String codiceTicket) {
        super(userMessage + " | " + details);
        this.codiceTicket = codiceTicket;
    }

    public String getCodiceTicket() {
        return codiceTicket;
    }
}
