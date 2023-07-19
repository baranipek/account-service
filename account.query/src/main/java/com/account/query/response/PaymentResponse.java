package com.account.query.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentResponse {

    private String id;
    private Date valueDate;
    private BigDecimal amount;
    private String paymentType;
    private String processType;
}
