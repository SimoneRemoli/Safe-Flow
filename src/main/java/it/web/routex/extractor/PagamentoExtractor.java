package it.web.routex.extractor;

import it.web.routex.record.PaymentRecord;

import javax.servlet.http.HttpServletRequest;

import it.web.routex.exception.InvalidPaymentInputExceptionRemoli;
import it.web.routex.validator.PaymentValidator;

public final class PagamentoExtractor {

    private PagamentoExtractor()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static PaymentRecord from(HttpServletRequest request)
            throws InvalidPaymentInputExceptionRemoli {

        return PaymentValidator.validateAndBuild(
                request.getParameter("city"),
                request.getParameter("quantity"),
                request.getParameter("totale"),
                request.getParameter("metodoPagamento"),
                request.getParameter("persistence")
        );
    }
}
