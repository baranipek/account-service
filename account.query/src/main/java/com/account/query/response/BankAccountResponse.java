package com.account.query.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class BankAccountResponse {
    private String id;
    private String accountHolder;
    private Date creationDate;
    private BigDecimal balance;
    private BigDecimal creditLines;

    private List<PaymentResponse> payments;
}
