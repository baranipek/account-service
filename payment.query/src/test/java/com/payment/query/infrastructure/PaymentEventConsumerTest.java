package com.payment.query.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.query.event.PaymentCreditedEvent;
import com.payment.query.event.PaymentDebitedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.Acknowledgment;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentEventConsumerTest {

    @Mock
    private EventHandler eventHandler;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private PaymentEventConsumer paymentEventConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumeDebit_Successful() throws JsonProcessingException {
        String eventInput = "DebitEventInput";
        PaymentDebitedEvent event = new PaymentDebitedEvent();
        when(objectMapper.readValue(eventInput, PaymentDebitedEvent.class)).thenReturn(event);

        paymentEventConsumer.consumeDebit(eventInput, acknowledgment);

        verify(eventHandler, times(1)).on(event);
        verify(acknowledgment, times(1)).acknowledge();
    }



    @Test
    void testConsumeCredit_Successful() throws JsonProcessingException {
        String eventInput = "CreditEventInput";
        PaymentCreditedEvent event = new PaymentCreditedEvent();
        when(objectMapper.readValue(eventInput, PaymentCreditedEvent.class)).thenReturn(event);

        paymentEventConsumer.consumeCredit(eventInput, acknowledgment);

        verify(eventHandler, times(1)).on(event);
        verify(acknowledgment, times(1)).acknowledge();
    }


}
