package it.web.routex.model;
/**
 * Rappresenta il modello di dominio per il pagamento tramite Mastercard.
 *
 * Questa classe incapsula i dati e la logica di business associati a una carta
 * di pagamento Mastercard, occupandosi della validazione dei dati inseriti
 * e dell’esposizione del metodo di pagamento utilizzato.
 *
 * In quanto Model, non contiene alcuna logica di presentazione né di accesso
 * ai dati persistenti, ma definisce esclusivamente il comportamento e le
 * regole di dominio legate al concetto di pagamento Mastercard.
 */

import it.web.routex.interfaces.Payment;
import it.web.routex.enumerator.PaymentMethod;
import it.web.routex.exception.PaymentValidationExceptionRemoli;

public class Mastercard implements Payment
{
    String numeroCarta;
    String dataScadenza;
    String cvv;

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public void setNumeroCarta(String numeroCarta) {
        this.numeroCarta = numeroCarta;
    }



    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.MASTERCARD;
    }

    @Override
    public boolean isValid() {
        return numeroCarta != null && numeroCarta.matches("\\d{16}")
                && cvv != null && cvv.matches("\\d{3}")
                && dataScadenza != null && dataScadenza.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    @Override
    public void validate() throws PaymentValidationExceptionRemoli {
        if (!isValid()) {
            throw new PaymentValidationExceptionRemoli(
                    "Dati Mastercard non validi",
                    PaymentMethod.MASTERCARD,
                    "Validazione dominio Mastercard"
            );
        }
    }

    public String maskedNumber() {
        return "**** **** **** " + numeroCarta.substring(12);
    }
}
