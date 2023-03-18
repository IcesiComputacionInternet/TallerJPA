package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.Enum.AccountType;
import com.edu.icesi.demojpa.dto.AccountCreateDTO;
import com.edu.icesi.demojpa.dto.UserCreateDTO;
import com.edu.icesi.demojpa.mapper.AccountMapper;
import com.edu.icesi.demojpa.model.IcesiAccount;
import com.edu.icesi.demojpa.model.IcesiUser;
import com.edu.icesi.demojpa.repository.AccountRepository;
import com.edu.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final AccountMapper accountMapper;

    private final AccountType depositOnly = AccountType.DEPOSIT_ONLY;

    public IcesiAccount save(AccountCreateDTO account){
        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        icesiAccount.setAccountId(idGenerator());
        icesiAccount.setAccountNumber(accountNumberGenerator());
        icesiAccount.setUser(findUser(account.getIcesiUserId()));
        return accountRepository.save(icesiAccount);
    }

    public String enableAccount(String accountNumber){
        IcesiAccount icesiAccount = findAccount(accountNumber);
        icesiAccount.setActive(true);
        accountRepository.save(icesiAccount);
        return "The account has been activated";
    }

    public String disableAccount(String accountNumber){
        IcesiAccount icesiAccount = findAccount(accountNumber);
        long balance = icesiAccount.getBalance();

        if(hasNoFunds().test(balance)){
            icesiAccount.setActive(false);
            accountRepository.save(icesiAccount);
            return "The account has been deactivated";
        }

        return "The account couldn't be deactivated because it is funded";
    }

    public String withdrawal(String accountNumber, long amountToWithdrawal){
        IcesiAccount account = findAccount(accountNumber);

        if(!account.isActive()){
            accountNotEnableException(accountNumber);
        }

        long accountBalance = account.getBalance();

        if(sufficientFunds().test(accountBalance, amountToWithdrawal)){
            accountNotHaveSufficientFundsException(accountNumber);
        }

        long newBalance = accountBalance - amountToWithdrawal;
        account.setBalance(newBalance);
        accountRepository.save(account);
        return  "The withdrawal was successfully carried out";
    }

    public String depositMoney(String accountNumber, long amountToDeposit){
        IcesiAccount account = findAccount(accountNumber);

        if(!account.isActive()){
            accountNotEnableException(accountNumber);
        }

        long accountBalance = account.getBalance();
        long newAccountBalance = accountBalance + amountToDeposit;
        account.setBalance(newAccountBalance);
        accountRepository.save(account);
        return "The deposit was successfully carried out";
    }

    public String transferMoneyToAnotherAccount(String accountNumberToWithdrawal, String accountNumberToDeposit, long amountToDeposit){
        IcesiAccount accountToWithdrawal = findAccount(accountNumberToWithdrawal);
        IcesiAccount accountToDeposit = findAccount(accountNumberToDeposit);

        if(isDepositOnly().test(accountToWithdrawal.getType())){
            accountDepositOnlyException(accountNumberToWithdrawal);
        }

        if(isDepositOnly().test(accountToDeposit.getType())){
            accountDepositOnlyException(accountNumberToDeposit);
        }

        withdrawal(accountNumberToWithdrawal, amountToDeposit);
        depositMoney(accountNumberToDeposit, amountToDeposit);
        accountRepository.save(accountToWithdrawal);
        accountRepository.save(accountToDeposit);
        return "The transaction was successfully completed";
    }


    public IcesiAccount findAccount(String accountNumber){
        Optional<IcesiAccount> icesiAccount = accountRepository.findAccountByAccountNumber(accountNumber);
        return icesiAccount.orElseThrow(() -> new RuntimeException(accountNumber+" Invalid account number"));
    }

    public IcesiUser findUser(String id){
        Optional<IcesiUser> icesiUser = userRepository.findUserById(id);
        return icesiUser.orElseThrow(() -> new RuntimeException(id+" Invalid id"));
    }


    public UUID idGenerator(){
        return UUID.randomUUID();
    }

    public String accountNumberGenerator(){
        Random random = new Random();
        String accountNumberGenerated = "";

        do {
            int threeDigitNumber = random.nextInt(1000);
            int sixDigitNumber = random.nextInt(1000000);
            int twoDigitNumber = random.nextInt(100);
            accountNumberGenerated = String.format("%03d-%06d-%02d", threeDigitNumber, sixDigitNumber, twoDigitNumber);

        }while (accountNumberInUse().test(accountNumberGenerated));

        return accountNumberGenerated;
    }

    public Predicate<Long> hasNoFunds(){
        return (accountBalance) -> accountBalance == 0;
    }

    public Predicate<String> isDepositOnly(){
        return (type) -> type.equals(depositOnly.getType());
    }

    public BiPredicate<Long, Long> sufficientFunds(){
        return (accountBalance, amountToWithdrawal) -> accountBalance - amountToWithdrawal < 0;
    }

    public Predicate<String> accountNumberInUse(){
        return (accountNumber) -> accountRepository.findAccountByAccountNumber(accountNumber).isPresent();
    }

    private void accountNotEnableException(String accountNumber) {
        throw new RuntimeException("The account with account number "+accountNumber+" isn't enabled");
    }

    private void accountNotHaveSufficientFundsException(String accountNumber) {
        throw new RuntimeException("The account with account number "+accountNumber+" doesn't have sufficient funds");
    }

    private void accountDepositOnlyException(String accountNumber){
        throw new RuntimeException("The account with number account "+accountNumber+" can't transfer or be transferred money");
    }
}
