package com.account.query.infrastructure;


import com.account.query.entity.BankAccount;
import com.account.query.events.AccountOpenedEvent;
import com.account.query.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        var bankAccount =
                BankAccount.builder()
                        .id(event.getId())
                        .creditLines(event.getOpenCreditLine())
                        .accountHolder(event.getAccountHolder())
                        .creationDate(event.getCreatedDate())
                        .balance(event.getOpeningBalance())
                        .build();

        accountRepository.save(bankAccount);

    }

}
