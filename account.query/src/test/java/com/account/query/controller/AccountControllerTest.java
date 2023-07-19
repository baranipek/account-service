package com.account.query.controller;

import com.account.query.entity.BankAccount;
import com.account.query.mapper.BankAccountMapper;
import com.account.query.query.FindAccountByIdQuery;
import com.account.query.response.AccountResponse;
import com.cqrs.core.domain.BaseEntity;
import com.cqrs.core.infra.QueryDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @Mock
    private QueryDispatcher queryDispatcher;

    @Mock
    private BankAccountMapper mapper;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccountById_Success() {
        String accountId = "account-id";
        BankAccount account = new BankAccount();
        account.setId(accountId);
        List<BaseEntity> accounts = new ArrayList<>();
        accounts.add(account);

        List<BankAccount> accounts1 = new ArrayList<>();
        accounts.add(account);

        when(queryDispatcher.send(any(FindAccountByIdQuery.class))).thenReturn(accounts);
        when(mapper.map(accounts1)).thenReturn(new ArrayList<>());

        ResponseEntity<AccountResponse> response = accountController.getAccountById(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully returned bank accounts.", response.getBody().getMessage());
    }

    @Test
    void testGetAccountById_NoAccountsFound() {
        String accountId = "non-existing-account-id";
        List<BankAccount> accounts = new ArrayList<>();

        when(queryDispatcher.send(any(FindAccountByIdQuery.class))).thenReturn(anyList());

        ResponseEntity<AccountResponse> response = accountController.getAccountById(accountId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


}
