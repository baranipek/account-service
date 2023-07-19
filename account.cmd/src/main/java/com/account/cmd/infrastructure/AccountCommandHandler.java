package com.account.cmd.infrastructure;

import com.account.cmd.commands.CommandHandler;
import com.account.cmd.commands.OpenAccountCommand;
import com.account.cmd.domain.aggregate.AccountAggregate;
import com.cqrs.core.handlers.EventSourcingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountCommandHandler implements CommandHandler {

    private final EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand command) {
        var aggregate = new AccountAggregate(command);
        eventSourcingHandler.save(aggregate);
    }

}
