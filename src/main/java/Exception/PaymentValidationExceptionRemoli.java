package Exception;

import Model.Domain.PaymentMethod;

public class PaymentValidationExceptionRemoli extends Exception
{
    private final PaymentMethod method;
    private final String detail;

    public PaymentValidationExceptionRemoli(String message, PaymentMethod method, String detail) {
        super(message);
        this.method = method;
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public PaymentMethod getMethod() {
        return method;
    }
}
