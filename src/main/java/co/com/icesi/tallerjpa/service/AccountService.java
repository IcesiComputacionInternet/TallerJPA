package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import co.com.icesi.tallerjpa.error.exception.CustomException;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.model.Account;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
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

    public ResponseAccountDTO save(RequestAccountDTO accountDTO) {
        Account account = accountMapper.fromAccountDTO(accountDTO);

        account.setUser(userRepository.findByEmail(accountDTO.getUser())
                .orElseThrow(() -> new RuntimeException("User not found")));
        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(validateAccountNumber(generateAccountNumber()));
        account.setActive(true);

        return accountMapper.fromAccountToSendAccountDTO(accountRepository.save(account));
    }

    public Account getAccountByAccountNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional
    public TransactionDTO withdraw(TransactionDTO transactionDTO) {
        Account account = getAccountByAccountNumber(transactionDTO.getAccountNumberOrigin());
        account.getType().getStrategy().withdraw(transactionDTO.getAmount(), account);
        accountRepository.updateBalance(account.getBalance(), transactionDTO.getAccountNumberOrigin());
        transactionDTO.setMessage("The withdrawal was successful");
        return transactionDTO;
    }

    @Transactional
    public TransactionDTO deposit(TransactionDTO transactionDTO) {
        Account account = getAccountByAccountNumber(transactionDTO.getAccountNumberOrigin());
        account.getType().getStrategy().deposit(transactionDTO.getAmount(), account);
        accountRepository.updateBalance(account.getBalance(), transactionDTO.getAccountNumberOrigin());
        transactionDTO.setMessage("The deposit was successful");
        return transactionDTO;
    }

    @Transactional
    public TransactionDTO transfer(TransactionDTO transactionDTO) {
        Account accountOrigin = getAccountByAccountNumber(transactionDTO.getAccountNumberOrigin());
        Account accountDestination = getAccountByAccountNumber(transactionDTO.getAccountNumberDestination());

        accountOrigin.getType().getStrategy().transfer(transactionDTO.getAmount(), accountOrigin, accountDestination);

        accountRepository.updateBalance(accountOrigin.getBalance(), transactionDTO.getAccountNumberOrigin());
        accountRepository.updateBalance(accountDestination.getBalance(), transactionDTO.getAccountNumberDestination());
        transactionDTO.setMessage("The transfer was successful");
        return transactionDTO;
    }

    @Transactional
    public TransactionDTO enableAccount(String accountNumber, UUID user){
        isAccountOwner(accountNumber, user);
        accountRepository.enableAccount(accountNumber);
        return TransactionDTO.builder()
                .accountNumberOrigin(accountNumber)
                .message(accountRepository.isActive(accountNumber) ? "The account was enabled" : "The account can't be enabled")
                .build();
    }

    @Transactional
    public TransactionDTO disableAccount(String accountNumber, UUID user){
        isAccountOwner(accountNumber, user);
        accountRepository.disableAccount(accountNumber);
        return TransactionDTO.builder()
                .accountNumberOrigin(accountNumber)
                .message(accountRepository.isActive(accountNumber) ? "The account can't be disabled" : "The account was disabled")
                .build();
    }

    @Transactional
    public List<ResponseAccountDTO> allAccountsByUser(UUID userId){
        return accountRepository.findAllById(userId)
                .stream()
                .map(accountMapper::fromAccountToSendAccountDTO)
                .collect(Collectors.toList());
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

    private void isAccountOwner(String accountNumber, UUID userId){
        if (!accountRepository.isAccountOwner(userId, accountNumber)) {
            throw new CustomException("You are not the owner of this account");
        }
    }
}
