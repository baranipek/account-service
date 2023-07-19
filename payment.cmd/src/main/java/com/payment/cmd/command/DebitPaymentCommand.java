package com.payment.cmd.command;

import com.cqrs.core.commands.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class DebitPaymentCommand extends BaseCommand {
    private String accountId;
    private BigDecimal amount;
}
