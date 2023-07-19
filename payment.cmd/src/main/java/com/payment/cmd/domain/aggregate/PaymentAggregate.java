package com.payment.cmd.domain.aggregate;

import com.cqrs.core.domain.AggregateRoot;
import com.payment.cmd.command.CreditPaymentCommand;
import com.payment.cmd.command.DebitPaymentCommand;
import com.payment.cmd.event.PaymentCreditedEvent;
import com.payment.cmd.event.PaymentDebitedEvent;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class PaymentAggregate extends AggregateRoot {

    public PaymentAggregate(DebitPaymentCommand command) {
        raiseEvent(PaymentDebitedEvent.builder()
                .id(command.getId())
                .accountId(command.getAccountId())
                .amount(command.getAmount())
                .valueDate(new Date())
                .build());
    }

    public PaymentAggregate(CreditPaymentCommand command) {
        raiseEvent(PaymentCreditedEvent.builder()
                .id(command.getId())
                .accountId(command.getAccountId())
                .amount(command.getAmount())
                .valueDate(new Date())
                .build());
    }
}
