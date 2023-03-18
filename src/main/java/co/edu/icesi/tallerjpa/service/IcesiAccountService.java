package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.exception.*;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transaction;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private static final long MAX_DEPOSIT_AMOUNT = 100000000;
    private final IcesiAccountMapper icesiAccountMapper;
    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiUserService icesiUserService;
    private final IcesiUserRepository icesiUserRepository;

    private final IcesiUserMapper icesiUserMapper;

    public IcesiAccount createAccount(IcesiAccountDTO icesiAccountDTO) throws UserNotFoundException, NegativeBalanceException, DuplicateDataException, MissingParameterException {
        UUID userId = UUID.fromString(icesiAccountDTO.getUserId());
        if (!icesiUserRepository.existsById(userId)) {
            throw new UserNotFoundException("There is no user with this ID");
        } else if (icesiAccountDTO.getBalance() < 0) {
            throw new NegativeBalanceException("The balance of the account is less than 0");
        }

        IcesiUser accountOwner = icesiUserRepository.findById(userId).get();
        IcesiAccount icesiAccount = icesiAccountMapper.toIcesiAccount(icesiAccountDTO);
        icesiAccount.setAccountOwner(accountOwner);
        icesiAccount.setAccountNumber(generateNumber());
        icesiUserService.saveNewUser(icesiUserMapper.fromModel(accountOwner));
        icesiAccount.setAccountId(UUID.randomUUID());
        return icesiAccountRepository.save(icesiAccount);
    }

    public String generateNumber() {
        Random rand = new Random();
        int firstGroup = rand.nextInt(1000);
        int secondGroup = rand.nextInt(1000000);
        int thirdGroup = rand.nextInt(100);

        return String.format("%03d-%06d-%02d", firstGroup, secondGroup, thirdGroup);
    }

    public String disableAccount(String accountNumber) throws AccountBalanceNotZeroException, AccountNotFoundException {
        Optional<IcesiAccount> optionalAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        optionalAccount.ifPresent(account -> {
            if (account.getBalance() != 0) {
                try {
                    throw new AccountBalanceNotZeroException("Cannot disable account with non-zero balance");
                } catch (AccountBalanceNotZeroException e) {
                    throw new RuntimeException(e);
                }
            }
            account.setActive(false);
            icesiAccountRepository.save(account);
        });
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException("No account found with number: " + accountNumber);
        }
        return accountNumber;
    }

    public String enableAccount(String accountNumber) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent()) {
            IcesiAccount account = icesiAccount.get();
            if (account.isActive()) {
                throw new RuntimeException("The account is already active.");
            }
            account.setActive(true);
            icesiAccountRepository.save(account);
        } else {
            throw new RuntimeException("There is no account with that number");
        }
        return accountNumber;
    }

    public String withdrawMoney(String accountNumber, long amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("The amount must be greater than zero");
        }
        Optional<IcesiAccount> icesiAccountOptional = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccountOptional.isEmpty()) {
            throw new IllegalArgumentException("There is no account with that number");
        }
        IcesiAccount icesiAccount = icesiAccountOptional.get();
        if (!icesiAccount.isActive()) {
            throw new IllegalStateException("The account is not active");
        }
        long balance = icesiAccount.getBalance();
        if (balance < amount) {
            throw new IllegalArgumentException("There is not enough money to withdraw from the account.");
        }
        icesiAccount.setBalance(balance - amount);
        icesiAccountRepository.save(icesiAccount);
        return accountNumber;
    }

    public String depositMoney(String accountNumber, long amount) {
        if (amount <= 0) {
            throw new RuntimeException("The value to be deposited must be greater than 0");
        }

        Optional<IcesiAccount> icesiAccountOptional = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccountOptional.isPresent()){
            IcesiAccount icesiAccount = icesiAccountOptional.get();
            if (icesiAccount.isActive()) {
                if (amount > MAX_DEPOSIT_AMOUNT) {
                    throw new RuntimeException("The amount to deposit exceeds the allowed limit");
                }

                icesiAccount.setBalance(icesiAccount.getBalance() + amount);
                icesiAccountRepository.save(icesiAccount);

                Transaction transaction;
                transaction = null;
                icesiAccount.addTransaction(transaction);
                icesiAccountRepository.save(icesiAccount);
            } else {
                throw new RuntimeException("The account is not active");
            }
        } else {
            throw new RuntimeException("There is no account with that number");
        }
        return accountNumber;
    }
}
