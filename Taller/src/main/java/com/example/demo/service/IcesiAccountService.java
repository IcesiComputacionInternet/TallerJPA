package com.example.demo.service;

import com.example.demo.DTO.IcesiAccountDTO;
import com.example.demo.Mapper.IcesiAccountMapper;
import com.example.demo.Repository.IcesiAccountRepository;
import com.example.demo.model.IcesiAccount;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@AllArgsConstructor
@Service
public class IcesiAccountService {

    @Autowired
    private final IcesiAccountRepository accountRepository;

    private final IcesiAccountMapper accountMapper;

    public final IcesiAccount createIcesiAccount(IcesiAccountDTO accountDTO){

        // Account verifier
        String accountNumber = generateAccountNumber();
        if (accountRepository.findByAccountNumber(accountNumber) != null) {
            throw new DuplicateKeyException("Account number already exists");
        }
        //Balance verifier
        if(accountDTO.getBalance() < 0){
            throw new IllegalArgumentException("invalid funds");
        }
        accountDTO.setAccountNumber(accountNumber);
        accountDTO.setAccountId(UUID.randomUUID());
        return accountRepository.save(accountMapper.fromIcesiAccountDTO(accountDTO));
    }

    private String generateAccountNumber() {
        return IntStream.range(0, 3)
                .mapToObj(i -> String.valueOf((int) (Math.random() * 10)))
                .collect(Collectors.joining())
                + "-"
                + IntStream.range(0, 6)
                .mapToObj(i -> String.valueOf((int) (Math.random() * 10)))
                .collect(Collectors.joining())
                + "-"
                + IntStream.range(0, 2)
                .mapToObj(i -> String.valueOf((int) (Math.random() * 10)))
                .collect(Collectors.joining());
    }

    public IcesiAccount setAccountEnabledStatus(UUID accountId, boolean enabled) throws ChangeSetPersister.NotFoundException {
        Optional<IcesiAccount> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            IcesiAccount account = optionalAccount.get();
            if(account.getBalance() == 0 && !enabled) {
                account.setActive(false);
            } else {
                account.setActive(true);
            }
            return accountRepository.save(account);
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }
}
