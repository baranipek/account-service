package com.payment.query.infrastructure;


import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    void consumeCredit(@Payload String event, Acknowledgment ack);

    void consumeDebit(@Payload String event, Acknowledgment ack);

}
