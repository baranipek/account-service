package com.payment.cmd;


import com.cqrs.core.infra.CommandDispatcher;
import com.payment.cmd.command.CommandHandler;
import com.payment.cmd.command.CreditPaymentCommand;
import com.payment.cmd.command.DebitPaymentCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class PaymentCommandApplication {

    @Autowired
    private CommandDispatcher commandDispatcher;

    @Autowired
    private CommandHandler commandHandler;

    public static void main(String[] args) {
        SpringApplication.run(PaymentCommandApplication.class, args);
    }

    @PostConstruct
    public void registerHandlers() {
        commandDispatcher.registerHandler(DebitPaymentCommand.class, commandHandler::handle);
        commandDispatcher.registerHandler(CreditPaymentCommand.class, commandHandler::handle);
    }
}
