package com.payment.query.infrastructure;

import com.payment.query.entity.BankAccount;
import com.payment.query.event.PaymentCreditedEvent;
import com.payment.query.event.PaymentDebitedEvent;
import com.payment.query.repository.AccountRepository;
import com.payment.query.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentEventHandlerTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private PaymentEventHandler paymentEventHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnDebit_Successful() {
        
        String accountId = "account123";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        PaymentDebitedEvent event = new PaymentDebitedEvent();
        event.setId("payment123");
        event.setAccountId(accountId);
        event.setAmount(amount);

        BankAccount account = new BankAccount();
        account.setId(accountId);
        account.setBalance(BigDecimal.valueOf(500.0));
        account.setCreditLines(BigDecimal.valueOf(200.0));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        paymentEventHandler.on(event);

        verify(paymentRepository, times(1)).save(any());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void testOnDebit_Pending() {
        String accountId = "account123";
        BigDecimal amount = BigDecimal.valueOf(800.0);
        PaymentDebitedEvent event = new PaymentDebitedEvent();
        event.setId("payment123");
        event.setAccountId(accountId);
        event.setAmount(amount);

        BankAccount account = new BankAccount();
        account.setId(accountId);
        account.setBalance(BigDecimal.valueOf(500.0));
        account.setCreditLines(BigDecimal.valueOf(200.0));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

      
        paymentEventHandler.on(event);

      
        verify(paymentRepository, times(1)).save(any());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void testOnDebit_AccountNotFound() {

        String accountId = "nonExistentAccount";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        PaymentDebitedEvent event = new PaymentDebitedEvent();
        event.setId("payment123");
        event.setAccountId(accountId);
        event.setAmount(amount);

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentEventHandler.on(event));
        verify(paymentRepository, times(0)).save(any());
        verify(accountRepository, times(0)).save(any());
    }

    @Test
    void testOnCredit_Successful() {
        
        String accountId = "account123";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        PaymentCreditedEvent event = new PaymentCreditedEvent();
        event.setId("payment123");
        event.setAccountId(accountId);
        event.setAmount(amount);

        BankAccount account = new BankAccount();
        account.setId(accountId);
        account.setBalance(BigDecimal.valueOf(500.0));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

      
        paymentEventHandler.on(event);

      
        verify(paymentRepository, times(1)).save(any());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void testOnCredit_AccountNotFound() {
        
        String accountId = "nonExistentAccount";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        PaymentCreditedEvent event = new PaymentCreditedEvent();
        event.setId("payment123");
        event.setAccountId(accountId);
        event.setAmount(amount);

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentEventHandler.on(event));
        verify(paymentRepository, times(0)).save(any());
        verify(accountRepository, times(0)).save(any());
    }
}
