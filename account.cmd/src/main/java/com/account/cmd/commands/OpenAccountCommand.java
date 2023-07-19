package com.account.cmd.commands;

import com.cqrs.core.commands.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OpenAccountCommand extends BaseCommand {
    @NonNull
    private String accountHolder;
    @NonNull
    private BigDecimal openingBalance;
    @NonNull
    private BigDecimal openCreditLine;
}
