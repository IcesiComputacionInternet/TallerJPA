package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.*;
import co.edu.icesi.demo.enums.TypeAccount;
import co.edu.icesi.demo.error.DetailBuilder;
import co.edu.icesi.demo.error.ErrorCode;
import co.edu.icesi.demo.mapper.IcesiAccountMapper;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.IcesiAccountRepository;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import co.edu.icesi.demo.security.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static co.edu.icesi.demo.error.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountRepository accountRepository;
    private final IcesiAccountMapper accountMapper;
    private final IcesiUserRepository userRepository;

    public IcesiAccountDto save(IcesiAccountDto accountDTO) {
        var user = userRepository.findByEmail(accountDTO.getUser().getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        IcesiAccount account = accountMapper.fromIcesiAccountDto(accountDTO);
        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(validateAccountNumber(generateAccountNumber()));
        account.setActive(true);
        account.setUser(user);

        return accountMapper.fromIcesiAccount(accountRepository.save(account));
    }

    public IcesiAccount getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber, true)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }


    public void validateBalance(long amount, long balance){
        if(amount>balance){
            throw new RuntimeException("Account must have balance greater than the amount to transfer");
        }
    }

    public void validateStatus(IcesiAccount account){
        if(!account.isActive()){
            throw new RuntimeException("Account is disabled");
        }
    }

    public TransactionResultDto transfer(TransactionOperationDto transaction){

        IcesiAccount sourceAccount = getAccount(transaction.getAccountFrom());
        IcesiAccount targetAccount = getAccount(transaction.getAccountTo());
        long amount = transaction.getAmount();

        validateAccountType(sourceAccount);
        validateAccountType(targetAccount);
        validateBalance(amount, sourceAccount.getBalance());

        sourceAccount.setBalance(sourceAccount.getBalance()-amount);
        targetAccount.setBalance(targetAccount.getBalance()+amount);
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        return accountMapper.fromTransactionOperationDto(transaction, "Success");
    }


    public TransactionResultDto withdraw(TransactionOperationDto transaction){
        IcesiAccount account = getAccount(transaction.getAccountFrom());
        long amount = transaction.getAmount();

        validateStatus(account);
        validateBalance(amount, account.getBalance());

        if (amount < 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        return accountMapper.fromTransactionOperationDto(transaction, "Success");
    }

    private void validateAccountBalance(IcesiAccount account, long amount){
        if (account.getBalance() < amount) {
            throw new RuntimeException("Low balance: " + account.getBalance());
        }
    }

    public TransactionResultDto deposit(TransactionOperationDto transaction){
        IcesiAccount account = getAccount(transaction.getAccountFrom());
        long amount = transaction.getAmount();
        validateStatus(account);

        if (amount < 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        return accountMapper.fromTransactionOperationDto(transaction, "Success");
    }



    private void validateAccountType(IcesiAccount account){
        if(account.getTypeAccount() == TypeAccount.DEPOSIT_ONLY){
            throw new RuntimeException("Account: " + account.getAccountNumber() + " is deposit only");
        }
    }

    public String enableAccount(String accountNumber){
        IcesiAccount account = getAccount(accountNumber);
        account.setActive(true);
        accountRepository.save(account);
        return (accountNumber + " enabled");
    }

    public String disableAccount(String accountNumber){
        IcesiAccount account = getAccount(accountNumber);

        if(account.getBalance()>0){
            throw new RuntimeException("Account must have 0 balance to be disabled");
        }

        account.setActive(false);
        accountRepository.save(account);
        return (accountNumber + " disabled");
    }

    public IcesiAccount getAccount(String accountNumber) {
        return accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account " + accountNumber + " does not exist"));
    }


    private String validateAccountNumber(String accountNumber) {
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            return validateAccountNumber(generateAccountNumber());
        }
        return accountNumber;
    }

    private String generateAccountNumber() {
        IntStream intStream = new Random().ints(11, 0, 9);

        String randomNumbers = intStream
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());

        return String.format("%s-%s-%s",
                randomNumbers.substring(0, 3),
                randomNumbers.substring(3, 9),
                randomNumbers.substring(9, 11));
    }

    public AccountsUserDto getAccountsLoggedUser() {
        String userID = SecurityContext.getCurrentUserId();
        Optional<IcesiUser> userOptional = userRepository.findById(UUID.fromString(userID));
        validateUserFound(userOptional);
        IcesiUser user = userOptional.get();


        List<IcesiAccount> accounts = user.getIcesiAccounts();

        List<IcesiAccountDto> accountResponses = accounts.stream()
                .map(icesiAccount -> {
                    IcesiAccountDto accountResponse = new IcesiAccountDto();
                    accountResponse.setAccountNumber(icesiAccount.getAccountNumber());
                    accountResponse.setBalance(icesiAccount.getBalance());
                    accountResponse.setActive(icesiAccount.isActive());
                    accountResponse.setType(icesiAccount.getType());
                    return accountResponse;
                })
                .toList();
        return AccountsUserDto.builder().userAccounts(accountResponses).build();
        /*
        String userID = SecurityContext.getCurrentUserId();
        Optional<IcesiUser> userOptional = userRepository.findById(UUID.fromString(userID));
        validateUserFound(userOptional);
        IcesiUser user = userOptional.get();
        System.out.println(user+"; "+user.getUserId()+"; "+user.getFirstName());
        List<IcesiAccount> accounts = user.getIcesiAccounts();

        List<IcesiAccountDto> accountResponses = accounts.stream()
                .map(icesiAccount -> {
                    IcesiAccountDto accountResponse = new IcesiAccountDto();
                    accountResponse.setAccountNumber(icesiAccount.getAccountNumber());
                    accountResponse.setBalance(icesiAccount.getBalance());
                    accountResponse.setActive(icesiAccount.isActive());
                    accountResponse.setType(icesiAccount.getType());
                    return accountResponse;
                })
                .toList();
        System.out.println(accountResponses);
        System.out.println(accountResponses.isEmpty());
        */
        //return null;
    }

    private void validateUserFound( Optional<IcesiUser> user) {
        if(user.isEmpty()){
            throw createIcesiException(
                    "Id not found",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED,user)
            ).get();
        }
    }
}
