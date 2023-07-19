package com.account.query.infrastructure;


import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    void consume(@Payload String event, Acknowledgment ack);
}
