package com.payment.cmd.command;

public interface CommandHandler {

    void handle(DebitPaymentCommand command);

    void handle(CreditPaymentCommand command);

}