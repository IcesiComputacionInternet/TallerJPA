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

    public IcesiAccount createIcesiAccount(IcesiAccountDTO accountDTO) {
        // Account verifier
        String accountNumber = generateAccountNumber();
        if (accountRepository.findByAccountNumber(accountNumber) != null) {
            throw new DuplicateKeyException("Account number already exists");
        }
        //Balance verifier
        if (accountDTO.getBalance() < 0) {
            throw new IllegalArgumentException("invalid balance");
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
            if (account.getBalance() == 0 && !enabled) {
                account.setActive(false);
            } else {
                account.setActive(true);
            }
            return accountRepository.save(account);
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    public final void transferMoney(UUID issuerAccount, UUID recipientAccount, Long amount) {
        IcesiAccount account1 = accountRepository.findById(issuerAccount).orElseThrow(
                () -> new IllegalArgumentException("Invalid issuer account Id:" + issuerAccount));
        IcesiAccount account2 = accountRepository.findById(issuerAccount).orElseThrow(
                () -> new IllegalArgumentException("Invalid recipient account Id:" + recipientAccount));

        if (!account1.isActive() && !account2.isActive()) {
            throw new IllegalArgumentException("Account is not active.");
        }
        if (account1.getType().equals("Deposit") || account2.getType().equals("Deposit")) {
            throw new IllegalArgumentException("One account is deposit only.");
        }

        if (account1.getBalance() < amount) {
            throw new IllegalArgumentException("Account balance is insufficient.");
        }

        account1.setBalance(account1.getBalance() - amount);
        account2.setBalance(account2.getBalance() + amount);
        accountRepository.save(account1);
        accountRepository.save(account2);
    }

    public final void withdrawMoney(UUID accountId, Long amount) {
        IcesiAccount account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalArgumentException("Invalid account Id:" + accountId));

        if (!account.isActive()) {
            throw new IllegalArgumentException("Account is not active.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to withdraw must be greater than zero.");
        }

        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Account balance is insufficient.");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    public final void depositMoney(UUID accountId, Long amount) {
        IcesiAccount account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalArgumentException("Invalid account Id:" + accountId));
        if (!account.isActive()) {
            throw new IllegalArgumentException("Account is not active.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to deposit must be greater than zero.");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }
}
