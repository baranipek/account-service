package com.cqrs.core.infra;

import com.cqrs.core.domain.BaseEntity;
import com.cqrs.core.queries.BaseQuery;
import com.cqrs.core.queries.QueryHandlerMethod;

import java.util.List;

public interface QueryDispatcher {
    <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler);

    <U extends BaseEntity> List<U> send(BaseQuery query);
}
