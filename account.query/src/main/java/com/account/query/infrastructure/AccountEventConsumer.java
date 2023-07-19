package com.account.query.infrastructure;


import com.account.query.events.AccountOpenedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;


@Service
@Slf4j
public class AccountEventConsumer implements EventConsumer {

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    ObjectMapper objectMapper;

    @KafkaListener(topicPartitions = @TopicPartition(topic = "AccountOpenedEvent", partitions = {"0"}), groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(String eventInput, Acknowledgment ack) {
        AccountOpenedEvent event;

        try {
            event = objectMapper.readValue(eventInput, AccountOpenedEvent.class);

        } catch (JsonProcessingException e) {
            var errorMessage = MessageFormat.format("Error while parsing string message  - {0}.", eventInput);
            log.error(errorMessage, e);
            throw new IllegalArgumentException(e);
        }
        this.eventHandler.on(event);
        ack.acknowledge();

    }

}
