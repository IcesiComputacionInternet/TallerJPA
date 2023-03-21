package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionOperationDTO;
import co.com.icesi.tallerjpa.dto.TransactionResultDTO;
import co.com.icesi.tallerjpa.enums.TypeAccount;
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
        var user = userRepository.findByEmail(accountDTO.getUser())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Account account = accountMapper.fromAccountDTO(accountDTO);
        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(validateAccountNumber(generateAccountNumber()));
        account.setActive(true);
        account.setUser(user);

        return accountMapper.fromAccountToSendAccountDTO(accountRepository.save(account));
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber, true)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional
    public TransactionResultDTO withdraw(TransactionOperationDTO transaction) {
        Account account = getAccountByAccountNumber(transaction.getAccountFrom());
        validateAccountBalance(account, transaction.getAmount());
        account.setBalance( account.getBalance() - transaction.getAmount() );
        accountRepository.save(account);
        return accountMapper.fromTransactionOperation(transaction, "The withdrawal was successful");
    }

    private void validateAccountBalance(Account account, long amount){
        if (account.getBalance() < amount) {
            throw new RuntimeException("Low balance: " + account.getBalance());
        }
    }

    @Transactional
    public TransactionResultDTO deposit(TransactionOperationDTO transaction) {
        Account account = getAccountByAccountNumber(transaction.getAccountTo());
        account.setBalance(account.getBalance() + transaction.getAmount());
        accountRepository.save(account);
        return accountMapper.fromTransactionOperation(transaction, "The deposit was successful");
    }

    @Transactional
    public TransactionResultDTO transfer(TransactionOperationDTO transaction) {
        Account accountOrigin = getAccountByAccountNumber(transaction.getAccountFrom());
        Account accountDestination = getAccountByAccountNumber(transaction.getAccountTo());
        validateAccountType(accountOrigin);
        validateAccountType(accountDestination);
        validateAccountBalance(accountOrigin, transaction.getAmount());

        accountOrigin.setBalance( accountOrigin.getBalance() - transaction.getAmount());
        accountDestination.setBalance( accountDestination.getBalance() + transaction.getAmount());

        accountRepository.save(accountOrigin);
        accountRepository.save(accountDestination);
        return accountMapper.fromTransactionOperation(transaction, "The transfer was successful");
    }

    private void validateAccountType(Account account){
        if(account.getType() == TypeAccount.DEPOSIT_ONLY){
            throw new RuntimeException("Account: " + account.getAccountNumber() + " is deposit only");
        }
    }

    @Transactional
    public String enableAccount(String accountNumber) {
        var account = accountRepository.findByAccountNumber(accountNumber, false)
                .orElseThrow(() -> new RuntimeException("The account: " + accountNumber + " can't be enabled"));
        account.setActive(true);
        accountRepository.save(account);
        return "The account is enabled";
    }

    @Transactional
    public String disableAccount(String accountNumber) {
        var account = accountRepository.findByAccountNumber(accountNumber, true)
                .orElseThrow(() -> new RuntimeException("The account: " + accountNumber + " can't be disabled"));
        account.setActive(false);
        accountRepository.save(account);
        return "The account was disabled";
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
}
