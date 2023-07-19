package com.payment.query.query;


import com.cqrs.core.domain.BaseEntity;
import com.payment.query.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentQueryHandler implements QueryHandler {
    private final PaymentRepository repository;

    @Override
    public List<BaseEntity> handle(FindPaymentProcessQuery query) {
        var payment = repository.findPaymentByProcessType(query.getProcessType().toString());

        if (payment.isEmpty())
            return null;

        return payment;
    }

    @Override
    public List<BaseEntity> handle(FindPaymentByAccountIdAndDateQuery query) {
        var payment = repository.findByBankAccount_IdAndValueDateGreaterThan(query.getAccountId(),query.getFromDate());

        if (payment.isEmpty())
            return null;

        return payment;
    }


}