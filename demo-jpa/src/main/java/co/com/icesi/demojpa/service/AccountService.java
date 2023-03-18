package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
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

    public IcesiAccount save(AccountCreateDTO account){

        Optional<IcesiUser> userOptional = userRepository.findById(account.getUser().getUserId());

        if(userOptional.isEmpty()){
            throw new RuntimeException("User does not exist");
        }
        if(account.getBalance()<0){
            throw new RuntimeException("Balance must be greater than 0");
        }

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(generateAccountNumber());
        //userOptional.get().getAccounts().add(icesiAccount);
        return accountRepository.save(icesiAccount);
    }

    public void disableAccount(String accountNumber){
        Optional<IcesiAccount> accountOptional = accountRepository.findByNumber(accountNumber);
        if(accountOptional.isEmpty()){
            throw new RuntimeException("Account does not exist");
        }
        if(accountOptional.get().getBalance()>0){
            throw new RuntimeException("Account must have 0 balance to be disabled");
        }else{
            accountOptional.get().setActive(false);
            accountRepository.save(accountOptional.get());
        }
    }

    public void enableAccount(String accountNumber){
        Optional<IcesiAccount> accountOptional = accountRepository.findByNumber(accountNumber);
        if(accountOptional.isEmpty()){
            throw new RuntimeException("Account does not exist");
        }else{
            accountOptional.get().setActive(true);
            accountRepository.save(accountOptional.get());
        }
    }

    public void transferMoney(String sourceAccountNumber, String targetAccountNumber, long amount){
        Optional<IcesiAccount> sourceAccountOptional = accountRepository.findByNumber(sourceAccountNumber);
        Optional<IcesiAccount> targetAccountOptional = accountRepository.findByNumber(targetAccountNumber);

        if(sourceAccountOptional.isEmpty() || targetAccountOptional.isEmpty()){
            throw new RuntimeException("One or both accounts do not exist");
        }
        if(sourceAccountOptional.get().getBalance()<=0){
            throw new RuntimeException("Source account must have balance greater than 0");
        }
        if(!sourceAccountOptional.get().isActive() || !targetAccountOptional.get().isActive()){
            throw new RuntimeException("One or both accounts are disabled");
        }
        if(sourceAccountOptional.get().getType().equals("Deposit") || targetAccountOptional.get().getType().equals("Deposit")){
            throw new RuntimeException("One or both accounts are deposit accounts, " +
                    "you can't transfer money to this type of accounts");
        }
        if(sourceAccountOptional.get().getBalance()<amount){
            throw new RuntimeException("Source account must have balance greater than the amount to transfer");
        }

        sourceAccountOptional.get().setBalance(sourceAccountOptional.get().getBalance()-amount);
        targetAccountOptional.get().setBalance(targetAccountOptional.get().getBalance()+amount);
        accountRepository.save(sourceAccountOptional.get());
        accountRepository.save(targetAccountOptional.get());
    }

    public void withdraw(String accountNumber, long amount){
        Optional<IcesiAccount> accountOptional = accountRepository.findByNumber(accountNumber);

        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Account does not exist");
        } else if (!accountOptional.get().isActive()) {
            throw new RuntimeException("Account is disabled");
        } else if (amount < 0) {
            throw new RuntimeException("Amount must be greater than 0");
        } else if (accountOptional.get().getBalance() < amount) {
            throw new RuntimeException("Account must have balance greater than the amount to withdraw");
        } else {
            IcesiAccount account = accountOptional.get();
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);
        }

    }

    public void deposit(String accountNumber, long amount){
        Optional<IcesiAccount> accountOptional = accountRepository.findByNumber(accountNumber);

        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Account does not exist");
        } else if (!accountOptional.get().isActive()) {
            throw new RuntimeException("Account is disabled");
        } else if (amount < 0) {
            throw new RuntimeException("Amount must be greater than 0");
        } else {
            IcesiAccount account = accountOptional.get();
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);
        }
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
