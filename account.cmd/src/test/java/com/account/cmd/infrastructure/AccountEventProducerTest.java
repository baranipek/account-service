package com.account.cmd.infrastructure;


import com.account.cmd.event.AccountOpenedEvent;
import com.cqrs.core.events.BaseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountEventProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AccountEventProducer accountEventProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProduce_Success() throws JsonProcessingException {
        String topic = "test-topic";
        BaseEvent event = new AccountOpenedEvent();
        event.setId("123");

        when(objectMapper.writeValueAsString(event)).thenReturn("{\"eventId\":\"123\",\"eventName\":\"Test Event\"}");

        accountEventProducer.produce(topic, event);

        verify(kafkaTemplate, times(1)).send(topic, "{\"eventId\":\"123\",\"eventName\":\"Test Event\"}");
    }

    @Test
    void testProduce_JsonProcessingException() throws JsonProcessingException {
        String topic = "test-topic";
        BaseEvent event = new AccountOpenedEvent();
        event.setId("123");

        when(objectMapper.writeValueAsString(event)).thenThrow(new JsonProcessingException("Error") {});

        try {
            accountEventProducer.produce(topic, event);
        } catch (IllegalArgumentException e) {
            verify(kafkaTemplate, never()).send(any(), any());
        }
    }
}
