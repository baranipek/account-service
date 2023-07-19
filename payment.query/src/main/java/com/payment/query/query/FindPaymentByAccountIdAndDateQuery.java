package com.payment.query.query;

import com.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FindPaymentByAccountIdAndDateQuery extends BaseQuery {
    private String accountId;
    private Date fromDate;
}
