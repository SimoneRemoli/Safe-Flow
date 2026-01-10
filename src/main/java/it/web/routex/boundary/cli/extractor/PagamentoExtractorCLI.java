package it.web.routex.boundary.cli.extractor;
import it.web.routex.boundary.cli.view.ConfermaPagamentoCLI;
import it.web.routex.extractor.PaymentValidator;
import it.web.routex.record.PaymentRecord;

import it.web.routex.exception.InvalidPaymentInputExceptionRemoli;

public final class PagamentoExtractorCLI {

    private PagamentoExtractorCLI()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static PaymentRecord from() throws InvalidPaymentInputExceptionRemoli {

        return PaymentValidator.validateAndBuild(
                ConfermaPagamentoCLI.getCity(),
                ConfermaPagamentoCLI.getQuantity(),
                String.valueOf(ConfermaPagamentoCLI.getPrezzoTotale()),
                ConfermaPagamentoCLI.getMetodoPagamento(),
                ConfermaPagamentoCLI.getPersistenza()
        );
    }

}
