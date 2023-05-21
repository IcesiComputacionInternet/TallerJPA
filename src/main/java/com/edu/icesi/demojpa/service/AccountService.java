package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.Enum.AccountType;
import com.edu.icesi.demojpa.dto.request.RequestAccountDTO;
import com.edu.icesi.demojpa.dto.request.RequestTransactionDTO;
import com.edu.icesi.demojpa.dto.response.ResponseAccountDTO;
import com.edu.icesi.demojpa.dto.response.ResponseTransactionDTO;
import com.edu.icesi.demojpa.error.util.IcesiExceptionBuilder;
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

    private final IcesiExceptionBuilder icesiExceptionBuilder = new IcesiExceptionBuilder();
    private final AccountType depositOnly = AccountType.DEPOSIT_ONLY;

    public ResponseAccountDTO save(RequestAccountDTO account){
        IcesiUser icesiUser = userRepository.findUserById(account.getIcesiUserId())
                .orElseThrow(() -> icesiExceptionBuilder.notFoundException("User with id " + account.getIcesiUserId() + " was not found",
                        "User", "id", account.getIcesiUserId().toString()));

        if(account.getBalance() < 0){
           throw icesiExceptionBuilder.badRequestException("The balance can't be below 0", "balance");
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
        isOwnerAccount(accountDTO.getIcesiUserId(), accountDTO.getAccountNumber());
        IcesiAccount account = accountRepository.findAccountByAccountNumber(accountDTO.getAccountNumber(), true)
                .orElseThrow(() -> icesiExceptionBuilder.badRequestException("Account with number " + accountDTO.getAccountNumber() + " can't be enabled",
                        "account"));
        account.setActive(true);
        accountRepository.save(account);
        return accountMapper.fromAccountDTO(account, "The account has been activated");
    }

    public ResponseAccountDTO disableAccount(RequestAccountDTO accountDTO){
        IcesiAccount account = accountRepository.findAccountByAccountNumber(accountDTO.getAccountNumber(), true)
                .orElseThrow(() -> icesiExceptionBuilder.badRequestException("Account with number " + accountDTO.getAccountNumber() + " can't be disabled",
                        "account"));
        boolean hasFund = account.getBalance() > 0;

        if(hasFund){
            throw icesiExceptionBuilder.badRequestException("The account couldn't be deactivated because it is funded", "balance");
        }

        account.setActive(false);
        accountRepository.save(account);
        return accountMapper.fromAccountDTO(account, "The account has been disabled");
    }

    public ResponseTransactionDTO withdraw(RequestTransactionDTO transaction){
        IcesiAccount account = accountRepository.findAccountByAccountNumber(transaction.getAccountFrom(), true)
                .orElseThrow(() -> icesiExceptionBuilder.badRequestException("The withdrawal wasn't successful", "account"));

        if(hasNoFunds(account.getBalance(), transaction.getAmount())){
            throw icesiExceptionBuilder.badRequestException("The account with number " + transaction.getAccountFrom() + " doesn't have sufficient funds", "balance");
        }

        account.setBalance(account.getBalance() - transaction.getAmount());
        accountRepository.save(account);
        return  accountMapper.fromTransactionDTO(transaction, "The withdrawal was successfully carried out");
    }

    public ResponseTransactionDTO deposit(RequestTransactionDTO transaction){
        IcesiAccount account = accountRepository.findAccountByAccountNumber(transaction.getAccountFrom(), true)
                .orElseThrow(() -> icesiExceptionBuilder.badRequestException("The deposit wasn't successful", "account"));

        account.setBalance(account.getBalance() + transaction.getAmount());
        accountRepository.save(account);
        return accountMapper.fromTransactionDTO(transaction, "The deposit was successfully carried out");
    }

    @Transactional
    public ResponseTransactionDTO transfer(RequestTransactionDTO transaction){
        IcesiAccount accountToWithdrawal = accountRepository.findAccountByAccountNumber(transaction.getAccountFrom(), true)
                .orElseThrow(() -> icesiExceptionBuilder.badRequestException("Money couldn't be transferred. The account is disable", "account to withdrawal"));
        IcesiAccount accountToDeposit = accountRepository.findAccountByAccountNumber(transaction.getAccountTo(), true)
                .orElseThrow(() -> icesiExceptionBuilder.badRequestException("Money couldn't be transferred. The account is disable", "account to deposit"));

        if(accountToWithdrawal.getType().equals(depositOnly.getType())){
            throw icesiExceptionBuilder.badRequestException("The account with number " + accountToWithdrawal.getAccountNumber() + " can't transfer money",
                    "account to withdrawal. Deposit only.");
        }

        if(accountToDeposit.getType().equals(depositOnly.getType())){
            throw icesiExceptionBuilder.badRequestException("The account with number " + accountToDeposit.getAccountNumber() + " can't be transferred money",
                    "account to deposit. Deposit only.");
        }

        if(hasNoFunds(accountToWithdrawal.getBalance(), transaction.getAmount())){
            throw icesiExceptionBuilder.badRequestException("The account with number " + transaction.getAccountFrom() + " doesn't have sufficient funds",
                    "account to withdrawal. Has no funds.");
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
                        .orElseThrow(() -> icesiExceptionBuilder.notFoundException("The account with number " + accountNumber + " doesn't exists",
                                "Account", "number", accountNumber)));
    }

    public List<ResponseAccountDTO> getAllAccounts(){
        return accountRepository
                .findAll()
                .stream()
                .map(accountMapper::fromAccountToDTO)
                .collect(Collectors.toList());
    }

    public void isOwnerAccount(UUID userId, String accountNumber){
        if(!accountRepository.isOwnerAccount(userId, accountNumber)){
            throw icesiExceptionBuilder.noPermissionException("No permission to do that");
        }
    }
}
