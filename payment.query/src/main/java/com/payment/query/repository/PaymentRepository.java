package com.payment.query.repository;

import com.cqrs.core.domain.BaseEntity;
import com.payment.query.entity.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {

    List<BaseEntity> findPaymentByProcessType (String processType);

    List<BaseEntity> findByBankAccount_IdAndValueDateGreaterThan(String accountId, Date date);


}
