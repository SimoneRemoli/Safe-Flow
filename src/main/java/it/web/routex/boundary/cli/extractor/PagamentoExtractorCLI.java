package it.web.routex.boundary.cli.extractor;
import it.web.routex.boundary.cli.view.ConfermaPagamentoCLI;
import it.web.routex.enumerator.TypesOfPersistenceLayer;
import it.web.routex.record.PaymentRecord;

import it.web.routex.exception.InvalidPaymentInputExceptionRemoli;

public final class PagamentoExtractorCLI {

    private PagamentoExtractorCLI()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static PaymentRecord from() throws InvalidPaymentInputExceptionRemoli {

        String city = ConfermaPagamentoCLI.getCity();
        String quantityParam = ConfermaPagamentoCLI.getQuantity();
        String totaleParam = String.valueOf(ConfermaPagamentoCLI.getPrezzoTotale());
        String metodo = ConfermaPagamentoCLI.getMetodoPagamento();
        String persistence = ConfermaPagamentoCLI.getPersistenza();

        if (city == null)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Campo città mancante.",
                    "Parametro 'city' è null.",
                    InvalidPaymentInputExceptionRemoli.Severity.LOW
            );

        if (quantityParam == null)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Campo quantità mancante.",
                    "Parametro 'quantity' è null.",
                    InvalidPaymentInputExceptionRemoli.Severity.LOW
            );

        if (totaleParam == null)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Campo totale mancante.",
                    "Parametro 'totale' è null.",
                    InvalidPaymentInputExceptionRemoli.Severity.LOW
            );

        if (metodo == null)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Metodo di pagamento mancante.",
                    "Parametro 'metodoPagamento' è null.",
                    InvalidPaymentInputExceptionRemoli.Severity.LOW
            );

        if (persistence == null)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Parametro del livello di persistenza mancante.",
                    "Parametro 'persistence' è null.",
                    InvalidPaymentInputExceptionRemoli.Severity.LOW
            );

        int quantity;
        try {
            quantity = Integer.parseInt(quantityParam);
        } catch (NumberFormatException e) {
            throw new InvalidPaymentInputExceptionRemoli(
                    "La quantità inserita non è valida.",
                    "NumberFormatException per quantity='" + quantityParam + "'",
                    InvalidPaymentInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (quantity <= 0)
            throw new InvalidPaymentInputExceptionRemoli(
                    "La quantità deve essere maggiore di zero.",
                    "Quantity <= 0: " + quantity,
                    InvalidPaymentInputExceptionRemoli.Severity.MEDIUM
            );

        double totale;
        try {
            totale = Double.parseDouble(totaleParam);
        } catch (NumberFormatException e) {
            throw new InvalidPaymentInputExceptionRemoli(
                    "Il totale inserito non è valido.",
                    "NumberFormatException per totale='" + totaleParam + "'",
                    InvalidPaymentInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (totale < 0)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Il totale non può essere negativo.",
                    "Totale negativo: " + totale,
                    InvalidPaymentInputExceptionRemoli.Severity.MEDIUM
            );

        if (metodo.isBlank())
            throw new InvalidPaymentInputExceptionRemoli(
                    "Metodo di pagamento vuoto.",
                    "'metodoPagamento' è blank.",
                    InvalidPaymentInputExceptionRemoli.Severity.MEDIUM
            );

        TypesOfPersistenceLayer persistenceLayer;
        if (persistence.equals("JDBC")) {
            persistenceLayer = TypesOfPersistenceLayer.JDBC;
        } else if (persistence.equals("FileSystem")) {
            persistenceLayer = TypesOfPersistenceLayer.FILE_SYSTEM;
        } else {
            throw new InvalidPaymentInputExceptionRemoli(
                    "Tipo di persistenza non valido.",
                    "Parametro persistence='" + persistence + "' non riconosciuto.",
                    InvalidPaymentInputExceptionRemoli.Severity.HIGH
            );
        }
        return new PaymentRecord(
                city,
                quantity,
                totale,
                metodo,
                persistenceLayer
        );
    }
}
