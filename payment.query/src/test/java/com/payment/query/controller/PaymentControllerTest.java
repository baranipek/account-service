package com.payment.query.controller;

import com.cqrs.core.domain.BaseEntity;
import com.cqrs.core.infra.QueryDispatcher;
import com.payment.query.entity.Payment;
import com.payment.query.enumeration.PaymentProcessType;
import com.payment.query.mapper.PaymentMapper;
import com.payment.query.query.FindPaymentByAccountIdAndDateQuery;
import com.payment.query.query.FindPaymentProcessQuery;
import com.payment.query.response.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class PaymentControllerTest {

    @Mock
    private QueryDispatcher queryDispatcher;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPaymentsByProcessType_Successful() {
        
        List<Payment> payments1 = new ArrayList<>();
        List<BaseEntity> payments = new ArrayList<>();
        Payment payment1 = new Payment();
        payment1.setProcessType(PaymentProcessType.SUCCESSFUL.toString());
        Payment payment2 = new Payment();
        payment2.setProcessType(PaymentProcessType.SUCCESSFUL.toString());
        payments.add(payment1);
        payments.add(payment2);
        payments1.add(payment1);
        payments1.add(payment2);

        when(queryDispatcher.send(any(FindPaymentProcessQuery.class))).thenReturn(payments);
        when(paymentMapper.map(payments1)).thenReturn(List.of(new PaymentResponse(), new PaymentResponse()));

      
        ResponseEntity<List<PaymentResponse>> result = paymentController.getPaymentsByProcessType(PaymentProcessType.SUCCESSFUL);

      
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void testGetPaymentsByProcessType_NoContent() {
        
        when(queryDispatcher.send(any(FindPaymentProcessQuery.class))).thenReturn(null);

      
        ResponseEntity<List<PaymentResponse>> result = paymentController.getPaymentsByProcessType(PaymentProcessType.SUCCESSFUL);

      
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertEquals(null, result.getBody());
    }

    @Test
    void testGetPaymentsByAccountIdAndFromDate() throws ParseException {
        
        String accountId = "123";
        String fromDate = "2023-07-18";
        Date parsedStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);

        List<BaseEntity> payments = new ArrayList<>();
        List<Payment> payments1 = new ArrayList<>();
        Payment payment1 = new Payment();
        payment1.setProcessType(PaymentProcessType.SUCCESSFUL.toString());
        Payment payment2 = new Payment();
        payment2.setProcessType(PaymentProcessType.PENDING.toString());
        payments.add(payment1);
        payments.add(payment2);
        payments1.add(payment1);
        payments1.add(payment2);

        when(queryDispatcher.send(any(FindPaymentByAccountIdAndDateQuery.class))).thenReturn(payments);
        when(paymentMapper.map(payments1)).thenReturn(List.of(new PaymentResponse(), new PaymentResponse()));

      
        ResponseEntity<List<PaymentResponse>> result = paymentController.getPaymentsByAccountIdAndFromDate(accountId, fromDate);

      
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void testGetPaymentsByAccountIdAndFromDate_NoContent() throws ParseException {
        
        String accountId = "123";
        String fromDate = "2023-07-18";
        Date parsedStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);

        when(queryDispatcher.send(any(FindPaymentByAccountIdAndDateQuery.class))).thenReturn(null);

      
        ResponseEntity<List<PaymentResponse>> result = paymentController.getPaymentsByAccountIdAndFromDate(accountId, fromDate);

      
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertEquals(null, result.getBody());
    }
}
