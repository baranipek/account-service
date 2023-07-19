package com.payment.query.controller;

import com.cqrs.core.infra.QueryDispatcher;
import com.payment.query.entity.Payment;
import com.payment.query.enumeration.PaymentProcessType;
import com.payment.query.mapper.PaymentMapper;
import com.payment.query.query.FindPaymentByAccountIdAndDateQuery;
import com.payment.query.query.FindPaymentProcessQuery;
import com.payment.query.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final QueryDispatcher queryDispatcher;

    private final PaymentMapper mapper;

    @GetMapping(path = "/{processType}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByProcessType(@PathVariable(value = "processType") PaymentProcessType paymentProcessType) {
        List<Payment> payments;
        try {
            payments = queryDispatcher.send(new FindPaymentProcessQuery(paymentProcessType));
            if (payments == null || payments.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(mapper.map(payments), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/account/{accountId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByAccountIdAndFromDate(@PathVariable(value = "accountId") String accountId,
                                                                                   @RequestParam(value = "fromDate") String fromDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedStartDate = new Date(dateFormat.parse(fromDate).getTime());
        List<Payment> payments;
        try {
            payments = queryDispatcher.send(new FindPaymentByAccountIdAndDateQuery(accountId,parsedStartDate));
            if (payments == null || payments.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(mapper.map(payments), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
