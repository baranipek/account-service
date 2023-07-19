package com.account.query.events;

import com.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AccountOpenedEvent extends BaseEvent {
    private String accountHolder;
    private Date createdDate;
    private BigDecimal openingBalance;
    private BigDecimal openCreditLine;
}
