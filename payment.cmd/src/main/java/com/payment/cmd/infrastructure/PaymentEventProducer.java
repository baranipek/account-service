package com.payment.cmd.infrastructure;

import com.cqrs.core.events.BaseEvent;
import com.cqrs.core.producers.EventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@Slf4j
public class PaymentEventProducer implements EventProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper mapper;

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
