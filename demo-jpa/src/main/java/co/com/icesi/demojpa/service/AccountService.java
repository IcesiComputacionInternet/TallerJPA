package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.TransactionOperationDTO;
import co.com.icesi.demojpa.dto.TransactionResultDTO;
import co.com.icesi.demojpa.error.exception.DetailBuilder;
import co.com.icesi.demojpa.error.exception.ErrorCode;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static co.com.icesi.demojpa.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;


    //@Transaccional asegura que las consultas se realicen una sola vez
    public AccountCreateDTO save(AccountCreateDTO account){

        userRepository.findById(account.getUser().getUserId()).orElseThrow(
                createIcesiException (
                        "User does not exist",
                        HttpStatus.BAD_REQUEST,
                        new DetailBuilder(ErrorCode.ERR_400, "User", "Id", account.getUser().getUserId().toString())
                )
        );
        if(account.getBalance()<0){
            throw createIcesiException(
                    "Balance must be greater than 0",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Account", "Balance")
            ).get();}

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(generateAccountNumber());
        account.setActive(true);
        return accountMapper.fromIcesiAccount(accountRepository.save(icesiAccount));
    }

    public void disableAccount(String accountNumber){
        IcesiAccount account = getAccount(accountNumber);

        if(account.getBalance()>0){
            throw createIcesiException(
                    "Account must have 0 balance to be disabled",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Account", "Balance")
            ).get();
        }

        account.setActive(false);
        accountRepository.save(account);

    }

    public void enableAccount(String accountNumber){
        IcesiAccount account = getAccount(accountNumber);
        account.setActive(true);
        accountRepository.save(account);
    }

    public TransactionResultDTO transferMoney(TransactionOperationDTO transaction){

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

        return accountMapper.fromTransactionOperation(transaction, "Success");
    }

    public IcesiAccount getAccount(String accountNumber) {
        return accountRepository.findByNumber(accountNumber).orElseThrow(
                        createIcesiException(
                                "Account does not exist",
                                HttpStatus.BAD_REQUEST,
                                new DetailBuilder(ErrorCode.ERR_400, "Account", "Number", accountNumber)
                        ));
    }

    public void validateAccountType(IcesiAccount account){
        if(account.getType().equals("Deposit")){
            throw createIcesiException(
                    "You can't transfer money to this type of accounts",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Account", "Type", account.getType())
            ).get();
        }
    }

    public void validateBalance(long amount, long balance){
        if(amount>balance){
            throw createIcesiException(
                    "Account must have balance greater than the amount to transfer",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Account", "Balance", String.valueOf(balance))
            ).get();
        }
    }

    public void validateStatus(IcesiAccount account){
        if(!account.isActive()){
            throw createIcesiException(
                    "Account is disabled",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Account", "Status", String.valueOf(account.isActive()))
            ).get();
        }
    }

    public TransactionResultDTO withdraw(TransactionOperationDTO transaction){
        IcesiAccount account = getAccount(transaction.getAccountFrom());
        long amount = transaction.getAmount();

        validateStatus(account);
        validateBalance(amount, account.getBalance());

        if (amount < 0) {
            throw createIcesiException(
                    "Amount must be greater than 0",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Account", "Amount", String.valueOf(amount))
            ).get();
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        return accountMapper.fromTransactionOperation(transaction, "Success");
    }

    public TransactionResultDTO deposit(TransactionOperationDTO transaction){
        IcesiAccount account = getAccount(transaction.getAccountFrom());
        long amount = transaction.getAmount();
        validateStatus(account);

        if (amount < 0) {
            throw createIcesiException(
                    "Amount must be greater than 0",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Account", "Amount", String.valueOf(amount))
            ).get();
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        return accountMapper.fromTransactionOperation(transaction, "Success");
    }

    public String generateAccountNumber() {
        Random rand = new Random();
        String accountNumber = IntStream.generate(() -> rand.nextInt(10))
                .limit(11)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(""));
        accountNumber = accountNumber.replaceFirst("(\\d{3})(\\d{6})(\\d{2})", "$1-$2-$3");
        if(accountRepository.findByNumber(accountNumber).isPresent()){
            generateAccountNumber();
        }
        return accountNumber;
    }


}
