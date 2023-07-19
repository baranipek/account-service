package com.account.query.query;

import com.account.query.enumeration.EqualityType;
import com.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FindAccountWithBalanceQuery extends BaseQuery {
    private EqualityType equalityType;
    private BigDecimal balance;
}
