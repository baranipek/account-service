package com.account.query.mapper;

import com.account.query.entity.BankAccount;
import com.account.query.entity.Payment;
import com.account.query.response.BankAccountResponse;
import com.account.query.response.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BankAccountMapperTest {

    @Mock
    private Payment payment1;

    @Mock
    private Payment payment2;

    @InjectMocks
    private BankAccountMapper bankAccountMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMap() {
        BankAccount account1 = new BankAccount();
        account1.setId("123");
        account1.setAccountHolder("John Doe");
        account1.setCreationDate(new Date());
        account1.setBalance(new BigDecimal("1000.00"));
        account1.setCreditLines(new BigDecimal("500.00"));

        List<Payment> payments = new ArrayList<>();
        payments.add(payment1);
        payments.add(payment2);

        account1.setPayments(payments);

        when(payment1.getId()).thenReturn("paymentId1");
        when(payment1.getAmount()).thenReturn(new BigDecimal("200.00"));
        when(payment1.getValueDate()).thenReturn(new Date());
        when(payment1.getProcessType()).thenReturn("Process1");
        when(payment1.getPaymentType()).thenReturn("Payment1");

        when(payment2.getId()).thenReturn("paymentId2");
        when(payment2.getAmount()).thenReturn(new BigDecimal("300.00"));
        when(payment2.getValueDate()).thenReturn(new Date());
        when(payment2.getProcessType()).thenReturn("Process2");
        when(payment2.getPaymentType()).thenReturn("Payment2");

        List<BankAccountResponse> accountResponses = bankAccountMapper.map(List.of(account1));

        assertEquals(1, accountResponses.size());

        BankAccountResponse response = accountResponses.get(0);
        assertEquals("123", response.getId());
        assertEquals("John Doe", response.getAccountHolder());
        assertEquals(account1.getCreationDate(), response.getCreationDate());
        assertEquals(new BigDecimal("1000.00"), response.getBalance());
        assertEquals(new BigDecimal("500.00"), response.getCreditLines());

        assertEquals(2, response.getPayments().size());
        PaymentResponse paymentResponse1 = response.getPayments().get(0);
        assertEquals("paymentId1", paymentResponse1.getId());
        assertEquals(new BigDecimal("200.00"), paymentResponse1.getAmount());
        assertEquals(payment1.getValueDate(), paymentResponse1.getValueDate());
        assertEquals("Process1", paymentResponse1.getProcessType());
        assertEquals("Payment1", paymentResponse1.getPaymentType());

        PaymentResponse paymentResponse2 = response.getPayments().get(1);
        assertEquals("paymentId2", paymentResponse2.getId());
        assertEquals(new BigDecimal("300.00"), paymentResponse2.getAmount());
        assertEquals(payment2.getValueDate(), paymentResponse2.getValueDate());
        assertEquals("Process2", paymentResponse2.getProcessType());
        assertEquals("Payment2", paymentResponse2.getPaymentType());
    }
}
