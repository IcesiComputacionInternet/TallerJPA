package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.Enum.AccountType;
import com.edu.icesi.demojpa.dto.RequestAccountDTO;
import com.edu.icesi.demojpa.dto.RequestTransactionDTO;
import com.edu.icesi.demojpa.dto.ResponseAccountDTO;
import com.edu.icesi.demojpa.dto.ResponseTransactionDTO;
import com.edu.icesi.demojpa.mapper.AccountMapper;
import com.edu.icesi.demojpa.model.IcesiAccount;
import com.edu.icesi.demojpa.model.IcesiUser;
import com.edu.icesi.demojpa.repository.AccountRepository;
import com.edu.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final AccountMapper accountMapper;

    private final AccountType depositOnly = AccountType.DEPOSIT_ONLY;

    public ResponseAccountDTO save(RequestAccountDTO account){
        IcesiUser icesiUser = userRepository.findUserById(account.getIcesiUserId())
                .orElseThrow(() -> new RuntimeException("User with id " + account.getIcesiUserId() + " was not found"));

        if(account.getBalance() < 0){
            throw new RuntimeException("The balance can't be below 0");
        }

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        icesiAccount.setAccountId(UUID.randomUUID());
        String accountNumber = accountNumberGenerator();
        icesiAccount.setAccountNumber(accountNumber);
        icesiAccount.setUser(icesiUser);
        icesiAccount.setActive(true);
        accountRepository.save(icesiAccount);
        return accountMapper.fromAccountDTO(icesiAccount, "The user has been saved");
    }

    public String accountNumberGenerator(){
        Random random = new Random();
        String accountNumberGenerated = "";

        do {
            int threeDigitNumber = random.nextInt(1000);
            int sixDigitNumber = random.nextInt(1000000);
            int twoDigitNumber = random.nextInt(100);
            accountNumberGenerated = String.format("%03d-%06d-%02d", threeDigitNumber, sixDigitNumber, twoDigitNumber);
        }while (isAccountNumberInUse(accountNumberGenerated));

        return accountNumberGenerated;
    }

    public boolean isAccountNumberInUse(String accountNumber){
        return accountRepository.findAccountByAccountNumber(accountNumber, true).isPresent();
    }

    public ResponseAccountDTO enableAccount(RequestAccountDTO accountDTO){
        IcesiAccount account = accountRepository.findAccountByAccountNumber(accountDTO.getAccountNumber(), true)
                .orElseThrow(() -> new RuntimeException("Account with number " + accountDTO.getAccountNumber() + " can't be enabled"));
        account.setActive(true);
        accountRepository.save(account);
        return accountMapper.fromAccountDTO(account, "The account has been activated");
    }

    public ResponseAccountDTO disableAccount(RequestAccountDTO accountDTO){
        IcesiAccount account = accountRepository.findAccountByAccountNumber(accountDTO.getAccountNumber(), true)
                .orElseThrow(() -> new RuntimeException("Account with number " + accountDTO.getAccountNumber() + " can't be disabled"));
        boolean hasFund = account.getBalance() > 0;

        if(hasFund){
            throw new RuntimeException("The account couldn't be deactivated because it is funded");
        }

        account.setActive(false);
        accountRepository.save(account);
        return accountMapper.fromAccountDTO(account, "The account has been disabled");
    }

    public ResponseTransactionDTO withdraw(RequestTransactionDTO transaction){
        IcesiAccount account = accountRepository.findAccountByAccountNumber(transaction.getAccountFrom(), true)
                .orElseThrow(() -> new RuntimeException("The withdrawal wasn't successful"));

        if(hasNoFunds(account.getBalance(), transaction.getAmount())){
            throw new RuntimeException("The account with number " + transaction.getAccountFrom() + " doesn't have sufficient funds");
        }

        account.setBalance(account.getBalance() - transaction.getAmount());
        accountRepository.save(account);
        return  accountMapper.fromTransactionDTO(transaction, "The withdrawal was successfully carried out");
    }

    public ResponseTransactionDTO deposit(RequestTransactionDTO transaction){
        IcesiAccount account = accountRepository.findAccountByAccountNumber(transaction.getAccountFrom(), true)
                .orElseThrow(() -> new RuntimeException("The deposit wasn't successful"));

        account.setBalance(account.getBalance() + transaction.getAmount());
        accountRepository.save(account);
        return accountMapper.fromTransactionDTO(transaction, "The deposit was successfully carried out");
    }

    @Transactional
    public ResponseTransactionDTO transfer(RequestTransactionDTO transaction){
        IcesiAccount accountToWithdrawal = accountRepository.findAccountByAccountNumber(transaction.getAccountFrom(), true)
                .orElseThrow(() -> new RuntimeException("Money couldn't be transferred"));
        IcesiAccount accountToDeposit = accountRepository.findAccountByAccountNumber(transaction.getAccountTo(), true)
                .orElseThrow(() -> new RuntimeException("Money couldn't be transferred"));

        if(accountToWithdrawal.getType().equals(depositOnly.getType())){
            throw new RuntimeException("The account with number " + accountToWithdrawal.getAccountNumber() + " can't transfer money");
        }

        if(accountToDeposit.getType().equals(depositOnly.getType())){
            throw new RuntimeException("The account with number " + accountToDeposit.getAccountNumber() + " can't be transferred money");
        }

        if(hasNoFunds(accountToWithdrawal.getBalance(), transaction.getAmount())){
            throw new RuntimeException("The account with number " + transaction.getAccountFrom() + " doesn't have sufficient funds");
        }

        accountToWithdrawal.setBalance(accountToWithdrawal.getBalance() - transaction.getAmount());
        accountToDeposit.setBalance(accountToDeposit.getBalance() + transaction.getAmount());
        accountRepository.save(accountToWithdrawal);
        accountRepository.save(accountToDeposit);
        return accountMapper.fromTransactionDTO(transaction, "The transaction was successfully completed");
    }

    public boolean hasNoFunds(long accountBalance, long amountToWithdrawal){
        return accountBalance < amountToWithdrawal;
    }

    public ResponseAccountDTO getAccount(String accountNumber){
        return accountMapper.fromAccountToDTO(
                accountRepository.findAccountByAccountNumber(accountNumber, true)
                        .orElseThrow(() -> new RuntimeException("The account with number " + accountNumber + " doesn't exists")));
    }

    public List<ResponseAccountDTO> getAllAccounts(){
        return accountRepository
                .findAll()
                .stream()
                .map(accountMapper::fromAccountToDTO)
                .collect(Collectors.toList());
    }
}
