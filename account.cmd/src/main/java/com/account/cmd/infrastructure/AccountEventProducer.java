package com.account.cmd.infrastructure;

import com.cqrs.core.events.BaseEvent;
import com.cqrs.core.producers.EventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountEventProducer implements EventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper;

    @Override
    public void produce(String topic, BaseEvent event)  {
        try {
            this.kafkaTemplate.send(topic, mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            var safeErrorMessage = MessageFormat.format("Error while producing message to topic - {0}.", topic);
            log.error(safeErrorMessage, e);
            throw new IllegalArgumentException(e);
        }
    }
}
