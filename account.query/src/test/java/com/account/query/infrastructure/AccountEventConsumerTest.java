package com.account.query.infrastructure;

import com.account.query.events.AccountOpenedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.Acknowledgment;

import java.math.BigDecimal;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountEventConsumerTest {

    @Mock
    private EventHandler eventHandler;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private AccountEventConsumer accountEventConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsume_Success() throws JsonProcessingException {
        
        String eventInput = "{\"accountId\":\"123\",\"accountHolder\":\"John Doe\",\"openingBalance\":1000.0}";
        AccountOpenedEvent event = new AccountOpenedEvent();
        event.setId("123");
        event.setAccountHolder("John Doe");
        event.setOpeningBalance(BigDecimal.valueOf(1000.0));

        when(objectMapper.readValue(eventInput, AccountOpenedEvent.class)).thenReturn(event);

      
        accountEventConsumer.consume(eventInput, acknowledgment);

      
        verify(eventHandler, times(1)).on(event);
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void testConsume_InvalidEventInput() throws JsonProcessingException {
        
        String invalidEventInput = "{\"invalidKey\":\"invalidValue\"}";

        when(objectMapper.readValue(invalidEventInput, AccountOpenedEvent.class)).thenThrow(new JsonProcessingException("Invalid input") {});

        try {
            accountEventConsumer.consume(invalidEventInput, acknowledgment);
        } catch (IllegalArgumentException e) {
            verify(eventHandler, never()).on(any());
            verify(acknowledgment, never()).acknowledge();
        }
    }
}
