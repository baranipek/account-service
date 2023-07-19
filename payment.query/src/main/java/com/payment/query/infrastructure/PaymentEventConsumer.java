package com.payment.query.infrastructure;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.query.event.PaymentCreditedEvent;
import com.payment.query.event.PaymentDebitedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;


@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentEventConsumer implements EventConsumer {

    private final EventHandler eventHandler;

    private final ObjectMapper  objectMapper;

    @KafkaListener(topicPartitions = @TopicPartition(topic = "PaymentDebitedEvent", partitions = {"0"}), groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consumeDebit(String eventInput, Acknowledgment ack) {
        PaymentDebitedEvent event;

        try {
            event = objectMapper.readValue(eventInput, PaymentDebitedEvent.class);

        } catch (JsonProcessingException e) {
            var errorMessage = MessageFormat.format("Error while parsing string message  - {0}.", eventInput);
            log.error(errorMessage, e);
            throw new IllegalArgumentException(e);
        }
        this.eventHandler.on(event);
        ack.acknowledge();

    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "PaymentCreditedEvent", partitions = {"0"}), groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consumeCredit(String eventInput, Acknowledgment ack) {
        PaymentCreditedEvent event;

        try {
            event = objectMapper.readValue(eventInput, PaymentCreditedEvent.class);

        } catch (JsonProcessingException e) {
            var errorMessage = MessageFormat.format("Error while parsing string message  - {0}.", eventInput);
            log.error(errorMessage, e);
            throw new IllegalArgumentException(e);
        }
        this.eventHandler.on(event);
        ack.acknowledge();

    }

}
