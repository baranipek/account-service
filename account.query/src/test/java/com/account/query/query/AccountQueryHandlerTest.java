package com.account.query.query;

import com.account.query.entity.BankAccount;
import com.account.query.enumeration.EqualityType;
import com.account.query.repository.AccountRepository;
import com.cqrs.core.domain.BaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AccountQueryHandlerTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountQueryHandler accountQueryHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandle_FindAccountByIdQuery_ExistingAccount() {
        
        String accountId = "123";
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(bankAccount));

        FindAccountByIdQuery query = new FindAccountByIdQuery(accountId);

      
        List<BaseEntity> result = accountQueryHandler.handle(query);

      
        assertEquals(1, result.size());
        assertEquals(bankAccount, result.get(0));
    }

    @Test
    void testHandle_FindAccountByIdQuery_NonExistingAccount() {
        String accountId = "456";

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        FindAccountByIdQuery query = new FindAccountByIdQuery(accountId);

        List<BaseEntity> result = accountQueryHandler.handle(query);

        assertEquals(null, result);
    }

    @Test
    void testHandle_FindAccountWithBalanceQuery_LessThan() {
        BigDecimal balance = new BigDecimal("1000.00");
        BankAccount bankAccount1 = new BankAccount();
        bankAccount1.setId("123");
        bankAccount1.setBalance(new BigDecimal("500.00"));

        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setId("456");
        bankAccount2.setBalance(new BigDecimal("800.00"));

        List<BaseEntity> accounts = List.of(bankAccount1, bankAccount2);

        when(accountRepository.findByBalanceLessThan(balance)).thenReturn(accounts);

        FindAccountWithBalanceQuery query = new FindAccountWithBalanceQuery(EqualityType.LESS_THAN, balance);

        List<BaseEntity> result = accountQueryHandler.handle(query);

        assertEquals(2, result.size());
        assertEquals(bankAccount1, result.get(0));
        assertEquals(bankAccount2, result.get(1));
    }

    @Test
    void testHandle_FindAccountWithBalanceQuery_GreaterThan() {
        BigDecimal balance = new BigDecimal("500.00");
        BankAccount bankAccount1 = new BankAccount();
        bankAccount1.setId("123");
        bankAccount1.setBalance(new BigDecimal("800.00"));

        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setId("456");
        bankAccount2.setBalance(new BigDecimal("1000.00"));

        List<BaseEntity> accounts = List.of(bankAccount1, bankAccount2);

        when(accountRepository.findByBalanceGreaterThan(balance)).thenReturn(accounts);

        FindAccountWithBalanceQuery query = new FindAccountWithBalanceQuery(EqualityType.GREATER_THAN, balance);

        List<BaseEntity> result = accountQueryHandler.handle(query);

        assertEquals(2, result.size());
        assertEquals(bankAccount1, result.get(0));
        assertEquals(bankAccount2, result.get(1));
    }

}
