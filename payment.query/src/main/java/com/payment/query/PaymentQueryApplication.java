package com.payment.query;

import com.cqrs.core.infra.QueryDispatcher;
import com.payment.query.query.FindPaymentByAccountIdAndDateQuery;
import com.payment.query.query.FindPaymentProcessQuery;
import com.payment.query.query.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class PaymentQueryApplication {

    @Autowired
    private QueryDispatcher queryDispatcher;

    @Autowired
    private QueryHandler queryHandler;

    public static void main(String[] args) {
        SpringApplication.run(PaymentQueryApplication.class, args);
    }


    @PostConstruct
    public void registerHandlers() {
        queryDispatcher.registerHandler(FindPaymentProcessQuery.class, query -> queryHandler.handle(query));
        queryDispatcher.registerHandler(FindPaymentByAccountIdAndDateQuery.class, query -> queryHandler.handle(query));
    }

}
