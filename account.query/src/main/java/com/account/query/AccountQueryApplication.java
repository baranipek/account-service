package com.account.query;


import com.account.query.query.FindAccountByIdQuery;
import com.account.query.query.FindAccountWithBalanceQuery;
import com.account.query.query.QueryHandler;
import com.cqrs.core.infra.QueryDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class AccountQueryApplication {
    @Autowired
    private QueryDispatcher queryDispatcher;

    @Autowired
    private QueryHandler queryHandler;

    public static void main(String[] args) {
        SpringApplication.run(AccountQueryApplication.class, args);
    }

    @PostConstruct
    public void registerHandlers() {
        queryDispatcher.registerHandler(FindAccountByIdQuery.class, query -> queryHandler.handle(query));
        queryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, query -> queryHandler.handle(query));

    }
}