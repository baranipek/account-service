package com.payment.query.infrastructure;


import com.payment.query.entity.Payment;
import com.payment.query.enumeration.PaymentProcessType;
import com.payment.query.enumeration.PaymentType;
import com.payment.query.event.PaymentCreditedEvent;
import com.payment.query.event.PaymentDebitedEvent;
import com.payment.query.repository.AccountRepository;
import com.payment.query.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentEventHandler implements EventHandler {

    private final PaymentRepository repository;

    private final AccountRepository accountRepository;

    @Override
    public void on(PaymentDebitedEvent event) {
        boolean isOverLimit = false;
        var account = accountRepository.
                findById(event.getAccountId()).orElseThrow(() ->
                        new EntityNotFoundException("Bank account not found with id " + event.getAccountId()));

        if (account.getBalance().add(account.getCreditLines()).subtract(event.getAmount()).compareTo(BigDecimal.ZERO) < 0) isOverLimit=true;

        var payment =
                    Payment.builder()
                            .id(event.getId())
                            .bankAccount(account)
                            .processType(!isOverLimit ? PaymentProcessType.SUCCESSFUL.toString() : PaymentProcessType.PENDING.toString() )
                            .amount(event.getAmount())
                            .paymentType(PaymentType.DEBIT.toString())
                            .valueDate(event.getValueDate())
                            .build();
            if (!isOverLimit) account.setBalance(account.getBalance().subtract(event.getAmount()));

            payment.setBankAccount(account);
            account.getPayments().add(payment);
            repository.save(payment);
            accountRepository.save(account);


    }

    @Override
    public void on(PaymentCreditedEvent event) {
        var account = accountRepository.
                findById(event.getAccountId()).orElseThrow(() ->
                        new EntityNotFoundException("Bank account not found with id " + event.getAccountId()));

        account.setBalance(account.getBalance().add(event.getAmount()));
        var payment =
                Payment.builder()
                        .id(event.getId())
                        .bankAccount(account)
                        .processType(PaymentProcessType.SUCCESSFUL.toString())
                        .amount(event.getAmount())
                        .paymentType(PaymentType.CREDIT.toString())
                        .valueDate(event.getValueDate())
                        .build();

        payment.setBankAccount(account);
        account.getPayments().add(payment);

        repository.save(payment);
        accountRepository.save(account);

    }

}
