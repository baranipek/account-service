package com.account.query.mapper;

import com.account.query.entity.BankAccount;
import com.account.query.entity.Payment;
import com.account.query.response.BankAccountResponse;
import com.account.query.response.PaymentResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankAccountMapper {
    public List<BankAccountResponse> map(List<BankAccount> accounts) {
        List<BankAccountResponse> responses = accounts.stream().map(this::map).toList();
        return responses;

    }

    private BankAccountResponse map(BankAccount account) {
        BankAccountResponse response = new BankAccountResponse();
        response.setId(account.getId());
        response.setAccountHolder(account.getAccountHolder());
        response.setCreationDate(account.getCreationDate());
        response.setCreditLines(account.getCreditLines());
        response.setBalance(account.getBalance());

        List<PaymentResponse> paymentResponses = account.getPayments().stream().map(this::map).toList();
        response.setPayments(paymentResponses);
        return response;
    }

    private PaymentResponse map(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setAmount(payment.getAmount());
        response.setValueDate(payment.getValueDate());
        response.setProcessType(payment.getProcessType());
        response.setPaymentType(payment.getPaymentType());
        response.setValueDate(payment.getValueDate());
        return response;
    }
}
