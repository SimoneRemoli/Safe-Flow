package it.web.routex.model;
/**
 * Rappresenta il modello di dominio per il pagamento tramite PayPal.
 *
 * Questa classe incapsula i dati e le regole di business associate a una
 * transazione PayPal, occupandosi della validazione delle credenziali
 * di pagamento e dell’identificazione del metodo di pagamento utilizzato.
 *
 * In quanto Model di dominio, la classe non gestisce aspetti di
 * presentazione né di persistenza, ma definisce esclusivamente
 * il comportamento e i vincoli legati al pagamento PayPal.
 */

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
