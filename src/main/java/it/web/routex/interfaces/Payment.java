package it.web.routex.interfaces;

import it.web.routex.enumerator.PaymentMethod;
import it.web.routex.exception.PaymentValidationExceptionRemoli;

public interface Payment {

    PaymentMethod getMethod();

    boolean isValid();

    void validate() throws PaymentValidationExceptionRemoli;
}
