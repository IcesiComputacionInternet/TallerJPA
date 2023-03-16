package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.model.Account;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ResponseAccountDTO save(RequestAccountDTO accountDTO) {
        Account account = accountMapper.fromAccountDTO(accountDTO);

        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(validateAccountNumber(generateAccountNumber()));
        account.setActive(true);
        account.setUser(userRepository.findByEmail(accountDTO.getUser())
                .orElseThrow(() -> new RuntimeException("User not found")));

        return accountMapper.fromAccountToSendAccountDTO(accountRepository.save(account));
    }

    public Account getAccountByAccountNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional
    public String withdraw(Long amount, String accountNumber) {
        Account account = getAccountByAccountNumber(accountNumber);

        account.getType().getStrategy().withdraw(amount, account);

        accountRepository.updateBalance(account.getBalance(), accountNumber);
        return "The withdrawal was successful";
    }

    @Transactional
    public String deposit(Long amount, String accountNumber) {
        Account account = getAccountByAccountNumber(accountNumber);

        account.getType().getStrategy().deposit(amount, account);

        accountRepository.updateBalance(account.getBalance(), accountNumber);
        enableAccount(accountNumber);
        return "The deposit was successful";
    }

    @Transactional
    public String transfer(Long amount, String accountNumberOrigin, String accountNumberDestination) {
        Account accountOrigin = getAccountByAccountNumber(accountNumberOrigin);
        Account accountDestination = getAccountByAccountNumber(accountNumberDestination);

        boolean isReceiverAccountValid = accountDestination.getType().getStrategy().isReceiverAccountValid();
        accountOrigin.getType().getStrategy().transfer(amount, accountOrigin, accountDestination, isReceiverAccountValid);

        accountRepository.updateBalance(accountOrigin.getBalance(), accountNumberOrigin);
        accountRepository.updateBalance(accountDestination.getBalance(), accountNumberDestination);
        enableAccount(accountNumberDestination);
        return "The transfer was successful";
    }

    @Transactional
    public String enableAccount(String accountNumber){
        accountRepository.enableAccount(accountNumber);
        return accountRepository.isActive(accountNumber) ? "The account was enabled" : "The account can't be enabled";
    }

    @Transactional
    public String disableAccount(String accountNumber){
        accountRepository.disableAccount(accountNumber);
        return accountRepository.isActive(accountNumber) ? "The account can't be disabled" : "The account was disabled";
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
