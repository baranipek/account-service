package com.payment.cmd.infrastructure;


import com.cqrs.core.handlers.EventSourcingHandler;
import com.payment.cmd.command.CommandHandler;
import com.payment.cmd.command.CreditPaymentCommand;
import com.payment.cmd.command.DebitPaymentCommand;
import com.payment.cmd.domain.aggregate.PaymentAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentCommandHandler implements CommandHandler {

    @Autowired
    private EventSourcingHandler<PaymentAggregate> eventSourcingHandler;

    @Override
    public void handle(DebitPaymentCommand command) {
        var aggregate = new PaymentAggregate(command);
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(CreditPaymentCommand command) {
        var aggregate = new PaymentAggregate(command);
        eventSourcingHandler.save(aggregate);
    }

}
