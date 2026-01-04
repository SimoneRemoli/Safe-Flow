package it.web.routex.enumerator;

public enum PaymentMethod {

    PAYPAL("PayPal"),
    MASTERCARD("Mastercard");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
