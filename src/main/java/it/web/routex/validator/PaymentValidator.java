package it.web.routex.validator;

import it.web.routex.exception.InvalidPaymentInputExceptionRemoli;
import it.web.routex.record.PaymentRecord;

public final class PaymentValidator {

    private PaymentValidator() {}

    public static PaymentRecord validateAndBuild(
            String city,
            String quantityParam,
            String totaleParam,
            String metodo
    ) throws InvalidPaymentInputExceptionRemoli {

        checkNotNull(city, "city");
        checkNotNull(quantityParam, "quantity");
        checkNotNull(totaleParam, "totale");
        checkNotNull(metodo, "metodoPagamento");

        int quantity = parseQuantity(quantityParam);
        double totale = parseTotale(totaleParam);

        if (metodo.isBlank()) {
            error("Metodo di pagamento vuoto.");
        }

        return new PaymentRecord(
                city,
                quantity,
                totale,
                metodo
        );
    }


    private static void checkNotNull(String value, String name)
            throws InvalidPaymentInputExceptionRemoli {

        if (value == null) {
            throw new InvalidPaymentInputExceptionRemoli(
                    "Campo mancante: " + name + ".",
                    "Parametro '" + name + "' è null.",
                    InvalidPaymentInputExceptionRemoli.Severity.LOW
            );
        }
    }

    private static int parseQuantity(String quantityParam)
            throws InvalidPaymentInputExceptionRemoli {

        int quantity;
        try {
            quantity = Integer.parseInt(quantityParam);
        } catch (NumberFormatException e) {
            error("La quantità inserita non è valida.");
            return 0; // unreachable
        }

        if (quantity <= 0) {
            error("La quantità deve essere maggiore di zero.");
        }

        return quantity;
    }

    private static double parseTotale(String totaleParam)
            throws InvalidPaymentInputExceptionRemoli {

        double totale;
        try {
            totale = Double.parseDouble(totaleParam);
        } catch (NumberFormatException e) {
            error("Il totale inserito non è valido.");
            return 0; // unreachable
        }

        if (totale < 0) {
            error("Il totale non può essere negativo.");
        }

        return totale;
    }

    private static void error(String msg)
            throws InvalidPaymentInputExceptionRemoli {

        throw new InvalidPaymentInputExceptionRemoli(
                msg,
                msg,
                InvalidPaymentInputExceptionRemoli.Severity.MEDIUM
        );
    }
}
