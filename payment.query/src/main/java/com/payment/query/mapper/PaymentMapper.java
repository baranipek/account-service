package com.payment.query.mapper;

import com.payment.query.entity.Payment;
import com.payment.query.response.PaymentResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMapper {
    public List<PaymentResponse> map(List<Payment> payments) {

        return payments.stream().map(this::map).toList();
    }

    public PaymentResponse map(Payment payment) {

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentType(payment.getPaymentType());
        paymentResponse.setId(payment.getId());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setValueDate(payment.getValueDate());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setProcessType(payment.getProcessType());
        return paymentResponse;
    }
}
