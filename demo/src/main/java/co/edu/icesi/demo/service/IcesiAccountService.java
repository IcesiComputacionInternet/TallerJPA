package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.*;
import co.edu.icesi.demo.enums.TypeAccount;
import co.edu.icesi.demo.mapper.IcesiAccountMapper;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.repository.IcesiAccountRepository;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Builder
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

    @Transactional
    public TransactionResultDto withdraw(TransactionOperationDto transaction) {
        IcesiAccount account = getAccountByAccountNumber(transaction.getAccountFrom());
        validateAccountBalance(account, transaction.getAmount());
        account.setBalance( account.getBalance() - transaction.getAmount() );
        accountRepository.save(account);
        return accountMapper.fromTransactionOperationDto(transaction, "The withdrawal was successful");
    }

    private void validateAccountBalance(IcesiAccount account, long amount){
        if (account.getBalance() < amount) {
            throw new RuntimeException("Low balance: " + account.getBalance());
        }
    }

    @Transactional
    public TransactionResultDto deposit(TransactionOperationDto transaction) {
        IcesiAccount account = getAccountByAccountNumber(transaction.getAccountTo());
        account.setBalance(account.getBalance() + transaction.getAmount());
        accountRepository.save(account);
        return accountMapper.fromTransactionOperationDto(transaction, "The deposit was successful");
    }

    @Transactional
    public TransactionResultDto transfer(TransactionOperationDto transaction) {
        IcesiAccount accountOrigin = getAccountByAccountNumber(transaction.getAccountFrom());
        IcesiAccount accountDestination = getAccountByAccountNumber(transaction.getAccountTo());
        validateAccountType(accountOrigin);
        validateAccountType(accountDestination);
        validateAccountBalance(accountOrigin, transaction.getAmount());

        accountOrigin.setBalance( accountOrigin.getBalance() - transaction.getAmount());
        accountDestination.setBalance( accountDestination.getBalance() + transaction.getAmount());

        accountRepository.save(accountOrigin);
        accountRepository.save(accountDestination);
        return accountMapper.fromTransactionOperationDto(transaction, "The transfer was successful");
    }

    private void validateAccountType(IcesiAccount account){
        if(account.getTypeAccount() == TypeAccount.DEPOSIT_ONLY){
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
