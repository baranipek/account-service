package com.account.cmd.infrastructure;

import com.account.cmd.commands.OpenAccountCommand;
import com.account.cmd.domain.aggregate.AccountAggregate;
import com.cqrs.core.handlers.EventSourcingHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AccountCommandHandlerTest {

    @Mock
    private EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @InjectMocks
    private AccountCommandHandler accountCommandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle_Success() {
        OpenAccountCommand command =  new OpenAccountCommand();
        command.setAccountHolder("John Doe");
        command.setOpeningBalance(BigDecimal.valueOf(1000));
        command.setOpenCreditLine(BigDecimal.valueOf(500));

        accountCommandHandler.handle(command);

        verify(eventSourcingHandler, times(1)).save(any(AccountAggregate.class));
    }
}
