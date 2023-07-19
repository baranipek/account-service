package com.account.query.controller;

import com.account.query.entity.BankAccount;
import com.account.query.enumeration.EqualityType;
import com.account.query.mapper.BankAccountMapper;
import com.account.query.query.FindAccountByIdQuery;
import com.account.query.query.FindAccountWithBalanceQuery;
import com.account.query.response.AccountResponse;
import com.cqrs.core.infra.QueryDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final Logger logger = Logger.getLogger(AccountController.class.getName());

    private final QueryDispatcher queryDispatcher;
    private final BankAccountMapper mapper;


    @GetMapping(path = "/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable(value = "id") String id) {
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByIdQuery(id));
            if (accounts == null || accounts.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response
                    = AccountResponse.builder()
                    .accounts(mapper.map(accounts))
                    .message("Successfully returned bank accounts.")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            var safeErrorMessage = "Failed to fetch given accounts details!";
            logger.log(Level.SEVERE, safeErrorMessage, ex);
            return new ResponseEntity<>(new AccountResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/balance/{equalityType}/{balance}")
    public ResponseEntity<AccountResponse> getAccountsWithBalance(@PathVariable(value = "equalityType") EqualityType equalityType,
                                                                        @PathVariable(value = "balance") BigDecimal balance) {
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountWithBalanceQuery(equalityType,balance));
            if (accounts == null || accounts.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response
                    = AccountResponse.builder()
                    .accounts(mapper.map(accounts))
                    .message("Successfully returned bank accounts.")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            var safeErrorMessage = "Failed to fetch given accounts details!";
            logger.log(Level.SEVERE, safeErrorMessage, ex);
            return new ResponseEntity<>(new AccountResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
