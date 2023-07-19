package com.payment.query.query;

import com.cqrs.core.queries.BaseQuery;
import com.payment.query.enumeration.PaymentProcessType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FindPaymentProcessQuery extends BaseQuery {
    private PaymentProcessType processType;
}
