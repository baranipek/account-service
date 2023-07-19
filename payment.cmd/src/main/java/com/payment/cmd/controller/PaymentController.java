package com.payment.cmd.controller;


import com.cqrs.core.infra.CommandDispatcher;
import com.payment.cmd.command.CreditPaymentCommand;
import com.payment.cmd.command.DebitPaymentCommand;
import com.payment.cmd.domain.response.BaseResponse;
import com.payment.cmd.domain.response.PaymentResponse;
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
@RequestMapping(path = "/api/v1/payment")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final CommandDispatcher commandDispatcher;

    @PostMapping("/credit")
    public ResponseEntity<BaseResponse> creditPayment(@RequestBody CreditPaymentCommand command) {
        var id = UUID.randomUUID().toString();
        command.setId(id);
        try {
            commandDispatcher.send(command);
            return new ResponseEntity<>(new PaymentResponse("Payment credited successfully", id), HttpStatus.CREATED);
        } catch (IllegalStateException ex) {
            log.warn(MessageFormat.format("Client made a bad request -{0}.", ex.toString()));
            return new ResponseEntity<>(new BaseResponse(ex.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            var safeErrorMessage = MessageFormat.format("Error while credit payment for id - {0}.", id);
            log.error(safeErrorMessage, ex);
            return new ResponseEntity<>(new PaymentResponse(safeErrorMessage, id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/debit")
    public ResponseEntity<BaseResponse> debitPayment(@RequestBody DebitPaymentCommand command) {
        var id = UUID.randomUUID().toString();
        command.setId(id);
        try {
            commandDispatcher.send(command);
            return new ResponseEntity<>(new PaymentResponse("Payment debited successfully", id), HttpStatus.CREATED);
        } catch (IllegalStateException ex) {
            log.warn(MessageFormat.format("Client made a bad request -{0}.", ex.toString()));
            return new ResponseEntity<>(new BaseResponse(ex.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            var safeErrorMessage = MessageFormat.format("Error while debit payment for id - {0}.", id);
            log.error(safeErrorMessage, ex);
            return new ResponseEntity<>(new PaymentResponse(safeErrorMessage, id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
