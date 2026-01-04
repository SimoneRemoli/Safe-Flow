package it.web.routex.bean;

import java.util.List;

public class PaymentResultBean {

    private final String city;
    private final int quantity;
    private final double total;
    private final String paymentMethod;
    private final List<String> ticketCodes;

    public PaymentResultBean(
            String city,
            int quantity,
            double total,
            String paymentMethod,
            List<String> ticketCodes
    ) {
        this.city = city;
        this.quantity = quantity;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.ticketCodes = ticketCodes;
    }

    public String getCity() {
        return city;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public List<String> getTicketCodes() {
        return ticketCodes;
    }
}
