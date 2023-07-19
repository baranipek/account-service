package com.account.cmd.domain.aggregate;

import com.account.cmd.commands.OpenAccountCommand;
import com.account.cmd.event.AccountOpenedEvent;
import com.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private BigDecimal balance;
    private BigDecimal openCreditLine;

    public BigDecimal getOpenCreditLine() {return openCreditLine;}

    public BigDecimal getBalance() {
        return this.balance;
    }

    public AccountAggregate(OpenAccountCommand command) {
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .createdDate(new Date())
                .openingBalance(command.getOpeningBalance())
                .openCreditLine(command.getOpenCreditLine())
                .build());
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.balance = event.getOpeningBalance();
        this.openCreditLine = event.getOpenCreditLine();
    }

}