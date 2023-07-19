package com.payment.cmd.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentResponse extends BaseResponse {
    private String id;

    public PaymentResponse(String message, String id) {
        super(message);
        this.id = id;
    }
}
