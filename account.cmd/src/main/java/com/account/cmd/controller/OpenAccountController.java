package com.account.cmd.controller;

import com.account.cmd.commands.OpenAccountCommand;
import com.account.cmd.domain.response.BaseResponse;
import com.account.cmd.domain.response.OpenAccountResponse;
import com.cqrs.core.infra.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/account")
@Slf4j
@RequiredArgsConstructor
public class OpenAccountController {
    private final CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command) {
        var id = UUID.randomUUID().toString();
        command.setId(id);
        try {
            commandDispatcher.send(command);
            return new ResponseEntity<>(new OpenAccountResponse("Bank Account created successfully", id), HttpStatus.CREATED);
        } catch (IllegalStateException ex) {
            log.error(MessageFormat.format("Client made a bad request -{0}.", ex.toString()));
            return new ResponseEntity<>(new BaseResponse(ex.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            var safeErrorMessage = MessageFormat.format("Error while opening new bank account for id - {0}.", id);
            log.error(safeErrorMessage, ex);
            return new ResponseEntity<>(new OpenAccountResponse(safeErrorMessage, id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
