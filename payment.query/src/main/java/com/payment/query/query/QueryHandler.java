package com.payment.query.query;


import com.cqrs.core.domain.BaseEntity;

import java.util.List;

public interface QueryHandler {

    List<BaseEntity> handle(FindPaymentProcessQuery query);
    List<BaseEntity> handle(FindPaymentByAccountIdAndDateQuery query);

}
