package com.account.query.infrastructure;


import com.account.query.events.AccountOpenedEvent;

public interface EventHandler {
    void on(AccountOpenedEvent event);
}
