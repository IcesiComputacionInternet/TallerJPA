package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.AccountDTO;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.model.Account;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import co.com.icesi.tallerjpa.strategy.accounts.interfaces.TypeAccountStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;
    private final List<TypeAccountStrategy> typeAccountStrategies;

    public void save(AccountDTO account) {
        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(validateAccountNumber(generateAccountNumber()));
        account.setActive(true);
        account.setUser(userRepository.findByEmail(account.getUser().getEmail())
                .orElseThrow(() -> new RuntimeException("User not found")));

        accountRepository.save(accountMapper.fromAccountDTO(account));
    }

    public Account getAccountByAccountNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional
    public String withdraw(Long amount, String accountNumber) {
        Account account = getAccountByAccountNumber(accountNumber);

        getTypeAccountStrategy(account).withdraw(amount, account);

        accountRepository.updateBalance(account.getBalance(), accountNumber);
        return "The withdrawal was successful";
    }

    @Transactional
    public String deposit(Long amount, String accountNumber) {
        Account account = getAccountByAccountNumber(accountNumber);

        getTypeAccountStrategy(account).deposit(amount, account);

        accountRepository.updateBalance(account.getBalance(), accountNumber);
        enableOrDisableAccount(accountNumber);
        return "The deposit was successful";
    }

    @Transactional
    public String transfer(Long amount, String accountNumberOrigin, String accountNumberDestination) {
        Account accountOrigin = getAccountByAccountNumber(accountNumberOrigin);
        Account accountDestination = getAccountByAccountNumber(accountNumberDestination);

        boolean isReceiverAccountValid = getTypeAccountStrategy(accountDestination).isReceiverAccountValid();
        getTypeAccountStrategy(accountOrigin).transfer(amount, accountOrigin, accountDestination, isReceiverAccountValid);

        accountRepository.updateBalance(accountOrigin.getBalance(), accountNumberOrigin);
        accountRepository.updateBalance(accountDestination.getBalance(), accountNumberDestination);
        enableOrDisableAccount(accountNumberDestination);
        return "The transfer was successful";
    }

    @Transactional
    public String enableOrDisableAccount(String accountNumber){
        accountRepository.enableOrDisableAccount(accountNumber);
        return accountRepository.isActive(accountNumber) ? "The account was enabled" : "The account was disabled";
    }

    private TypeAccountStrategy getTypeAccountStrategy(Account account){
        return typeAccountStrategies.stream()
                .filter(typeAccountStrategy -> typeAccountStrategy.getType().equals(account.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Account type not found"));
    }

    private String validateAccountNumber(String accountNumber){
        if(accountRepository.existsByAccountNumber(accountNumber)){
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
}
