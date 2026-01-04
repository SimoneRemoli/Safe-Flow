package it.web.routex.model;

import it.web.routex.interfaces.Payment;
import it.web.routex.enumerator.PaymentMethod;
import it.web.routex.exception.PaymentValidationExceptionRemoli;

public class Paypal implements Payment
{
    String email;
    String codice;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getEmail() {
        return email;
    }

    public String getCodice() {
        return codice;
    }
    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.PAYPAL;
    }

    @Override
    public boolean isValid() {
        return email != null
                && email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
                && codice != null
                && codice.matches("^TXN-[A-Z0-9]{6,20}$");
    }

    @Override
    public void validate() throws PaymentValidationExceptionRemoli {
        if (!isValid()) {
            throw new PaymentValidationExceptionRemoli(
                    "Dati Paypal non validi",
                    PaymentMethod.PAYPAL,
                    "Validazione dominio Paypal"
            );
        }
    }
    public String maskedAccount() {
        if (email == null || !email.contains("@")) {
            return "PayPal (account sconosciuto)";
        }
        String user = email.substring(0, email.indexOf('@'));
        return "PayPal (" + user + "@***)";
    }
}
