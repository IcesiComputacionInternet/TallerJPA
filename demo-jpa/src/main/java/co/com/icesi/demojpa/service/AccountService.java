package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.TransactionOperationDTO;
import co.com.icesi.demojpa.dto.TransactionResultDTO;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;


    //@Transaccional asegura que las consultas se realicen una sola vez
    public AccountCreateDTO save(AccountCreateDTO account){

        userRepository.findById(account.getUser().getUserId()).orElseThrow(
                () -> new RuntimeException("User does not exist"));

        if(account.getBalance()<0){
            throw new RuntimeException("Balance must be greater than 0");
        }

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(generateAccountNumber());
        account.setActive(true);
        return accountMapper.fromIcesiAccount(accountRepository.save(icesiAccount));
    }

    public void disableAccount(String accountNumber){
        IcesiAccount account = getAccount(accountNumber);

        if(account.getBalance()>0){
            throw new RuntimeException("Account must have 0 balance to be disabled");
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
        return accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account " + accountNumber + " does not exist"));
    }

    public void validateAccountType(IcesiAccount account){
        if(account.getType().equals("Deposit")){
            throw new RuntimeException("You can't transfer money to this type of accounts");
        }
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

    public TransactionResultDTO withdraw(TransactionOperationDTO transaction){
        IcesiAccount account = getAccount(transaction.getAccountFrom());
        long amount = transaction.getAmount();

        validateStatus(account);
        validateBalance(amount, account.getBalance());

        if (amount < 0) {
            throw new RuntimeException("Amount must be greater than 0");
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
            throw new RuntimeException("Amount must be greater than 0");
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
        accountNumber.replaceFirst("(\\d{3})(\\d{6})(\\d{2})", "$1-$2-$3");
        /*if(accountRepository.findByNumber(accountNumber).isPresent()){
            generateAccountNumber();
        }*/
        return accountNumber;
    }


}
