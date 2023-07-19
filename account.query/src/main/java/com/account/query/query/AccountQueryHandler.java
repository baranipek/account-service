package com.account.query.query;

import com.account.query.enumeration.EqualityType;
import com.account.query.repository.AccountRepository;
import com.cqrs.core.domain.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountQueryHandler implements QueryHandler {
    private final AccountRepository accountRepository;

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        var bankAccount = accountRepository.findById(query.getId());

        if (bankAccount.isEmpty())
            return null;

        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());

        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
        return query.getEqualityType() == EqualityType.LESS_THAN
                ? accountRepository.findByBalanceLessThan(query.getBalance())
                : accountRepository.findByBalanceGreaterThan(query.getBalance());
    }


}