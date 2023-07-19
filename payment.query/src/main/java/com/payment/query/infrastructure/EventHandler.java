package com.payment.query.infrastructure;


import com.payment.query.event.PaymentCreditedEvent;
import com.payment.query.event.PaymentDebitedEvent;

public interface EventHandler {
    void on(PaymentDebitedEvent event);

    void on(PaymentCreditedEvent event);
}
