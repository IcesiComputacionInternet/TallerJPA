package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.mapper.AccountMapper;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.edu.icesi.TallerJPA.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    public IcesiAccount save(AccountCreateDTO accountCreateDTO) {

        accountCreateDTO.setAccountNumber(generateNumberAccount());

        if (accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber()).isPresent()) {
            throw new RuntimeException("Account already exists, try again");
        }

        if (validateBalance(accountCreateDTO.getBalance())) {
            throw new RuntimeException("Balance can't be 0");
        }

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(accountCreateDTO);
        icesiAccount.setAccountId(UUID.randomUUID());

        return accountRepository.save(icesiAccount);
    }

    public String generateNumberAccount() {

        String first = generateNumbers(3);
        String middle = generateNumbers(6);
        String last = generateNumbers(2);

        return first + "-" + middle + "-" + last;
    }

    public String generateNumbers(int length) {

        String stringWithId = "";
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            stringWithId += random.nextInt(10);
        }
        return stringWithId;
    }

    public boolean validateBalance(long balance) {

        boolean verification = false;

        if (balance == 0) {
            verification = true;
        }
        return verification;
    }

    public IcesiAccount setStateAccount(String accountNumber) {

        Optional<IcesiAccount> account = accountRepository.findByAccountNumber(accountNumber);

        if (account.isPresent()){
            if (account.get().getBalance() == 0 && account.get().isActive()){
                account.get().setActive(false);

            } else if (account.get().getBalance() != 0 && !account.get().isActive()) {
                account.get().setActive(true);

            } else if (!account.get().isActive()) {
                account.get().setActive(true);

            }else{
                throw new RuntimeException("Account can't change status");
            }
        }

        return account.orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public IcesiAccount withdrawals(List<IcesiAccount> accounts, String accountNumber, int moneyToWithdraw) {

        List<IcesiAccount> icesiAccount = accounts.stream().filter(account -> account != null && account.getAccountNumber().equalsIgnoreCase(accountNumber)).toList();

        if (!icesiAccount.isEmpty()) {
            long balance = icesiAccount.get(0).getBalance();

            if (balance > moneyToWithdraw) {
                icesiAccount.get(0).setBalance(balance - moneyToWithdraw);

            } else {
                throw new RuntimeException("Insufficient money");
            }
        } else {
            throw new RuntimeException("Account not found");
        }

        return icesiAccount.get(0);
    }

    public IcesiAccount depositMoney(String destinationAccountNumber, int moneyToDeposit) {
        Optional<IcesiAccount> account = accountRepository.findByAccountNumber(destinationAccountNumber);

        account.ifPresent(icesiAccount -> icesiAccount.setBalance(icesiAccount.getBalance() + moneyToDeposit));

        return account.orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public IcesiAccount transferMoney(List<IcesiAccount> accounts, String sourceAccountNumber, String destinationAccountNumber, int moneyToTransfer) {

        List<IcesiAccount> sourceAccount = accounts.stream().filter(source -> source.getAccountNumber().equals(sourceAccountNumber)).toList();

        List<IcesiAccount> destinationAccount = accounts.stream().filter(destination -> destination.getAccountNumber().equals(destinationAccountNumber)).toList();

        if (!sourceAccount.isEmpty() && !destinationAccount.isEmpty() &&
                !sourceAccount.get(0).getType().equals("Deposit Only") && !destinationAccount.get(0).getType().equals("Deposit Only")) {

            if (sourceAccount.get(0).getBalance() >= moneyToTransfer){
                destinationAccount.get(0).setBalance(destinationAccount.get(0).getBalance() + moneyToTransfer);

            } else{
                throw new RuntimeException("Insufficient money to make a transfer");
            }
        }else {
            throw new RuntimeException("The transfer not was possible");
        }

        return destinationAccount.get(0);
    }


}
