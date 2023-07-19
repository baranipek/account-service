package com.account.cmd.controller;

import com.account.cmd.commands.OpenAccountCommand;
import com.account.cmd.domain.response.BaseResponse;
import com.cqrs.core.infra.CommandDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class OpenAccountControllerTest {

    @Mock
    private CommandDispatcher commandDispatcher;

    @InjectMocks
    private OpenAccountController openAccountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOpenAccount_Success() {
        OpenAccountCommand command = mock(OpenAccountCommand.class);

        ResponseEntity<BaseResponse> responseEntity = openAccountController.openAccount(command);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        verify(commandDispatcher, times(1)).send(command);
    }

    @Test
    void testOpenAccount_InternalServerError() {
        OpenAccountCommand command = mock(OpenAccountCommand.class);

        doThrow(new RuntimeException("Some error")).when(commandDispatcher).send(command);

        ResponseEntity<BaseResponse> responseEntity = openAccountController.openAccount(command);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        verify(commandDispatcher, times(1)).send(command);
    }
}
