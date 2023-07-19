package com.payment.query.repository;

import com.payment.query.entity.BankAccount;
import org.springframework.data.repository.CrudRepository;


public interface AccountRepository extends CrudRepository<BankAccount, String> {

}
