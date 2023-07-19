package com.payment.cmd.infrastructure;

import com.cqrs.core.events.BaseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentEventProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PaymentEventProducer paymentEventProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProduce_Successful() throws JsonProcessingException {
        BaseEvent event = new TestEvent();
        String topic = "test-topic";
        String eventJson = "test-event-json";

        when(objectMapper.writeValueAsString(event)).thenReturn(eventJson);

        paymentEventProducer.produce(topic, event);

        verify(kafkaTemplate, times(1)).send(topic, eventJson);
    }

    @Test
    void testProduce_JsonProcessingException() throws JsonProcessingException {
        BaseEvent event = new TestEvent();
        String topic = "test-topic";

        when(objectMapper.writeValueAsString(event)).thenThrow(new JsonProcessingException("Error") {
        });


        assertThrows(IllegalArgumentException.class, () -> paymentEventProducer.produce(topic, event));
    }

    private static class TestEvent extends BaseEvent {
    }
}
