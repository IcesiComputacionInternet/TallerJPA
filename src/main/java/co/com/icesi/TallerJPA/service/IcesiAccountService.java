package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiAccountCreateDTO;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.repository.IcesiAccountRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountRepository accountRepository;
    private final IcesiUserRepository userRepository;
    private final IcesiAccountMapper accountMapper;

    public boolean isDepositOnlyAccount(String accountNumber) {
        Optional<IcesiAccount> optionalAccount = accountRepository.findAccountByAccountNumber(accountNumber);

        if (optionalAccount.isPresent()) {
            IcesiAccount account = optionalAccount.get();
            return account.getType().equals("deposit") && !account.isActive();
        } else {
            throw new NoSuchElementException("Account not found");
        }
    }

    public boolean hasNonNegativeBalance(IcesiAccount account) {
        return account.getBalance() >= 0;
    }

    public IcesiAccountCreateDTO createAccount(IcesiAccountCreateDTO icesiAccountCreateDTO) {
        String accountNumber = generateAccountNumber();
        //icesiAccountCreateDTO.setAccountNumber(accountNumber);
        icesiAccountCreateDTO.setActive(true);
        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountCreateDTO(icesiAccountCreateDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setUser(userRepository.findByEmail(icesiAccountCreateDTO.getIcesiUser().getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found")));
        accountRepository.save(icesiAccount);
        return icesiAccountCreateDTO;
    }

    public void disableAccount(String accountNumber) {
        Optional<IcesiAccount> optionalAccount = accountRepository.findAccountByAccountNumber(accountNumber);

        if (optionalAccount.isPresent()) {
            IcesiAccount account = optionalAccount.get();
            if (account.getBalance() == 0) {
                account.setActive(false);
                accountRepository.save(account);
            } else {
                throw new IllegalStateException("Account balance must be zero to disable account");
            }
        } else {
            throw new NoSuchElementException("Account not found");
        }
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        accountNumber.append(String.format("%03d", random.nextInt(1000)));
        accountNumber.append("-");
        accountNumber.append(String.format("%06d", random.nextInt(1000000)));
        accountNumber.append("-");
        accountNumber.append(String.format("%02d", random.nextInt(100)));
        return accountNumber.toString();
    }

    // Update the balance of an account with a deposit
    public void deposit(String accountNumber, Long amount) {
        Optional<IcesiAccount> optionalAccount = accountRepository.findAccountByAccountNumber(accountNumber);

        if (optionalAccount.isPresent()) {
            IcesiAccount account = optionalAccount.get();
            if (hasNonNegativeBalance(account)) {
                account.setBalance(account.getBalance() + amount);
                accountRepository.save(account);
            } else {
                throw new IllegalArgumentException("Account balance cannot be negative");
            }
        } else {
            throw new NoSuchElementException("Account not found");
        }
    }

    // Update the balance of an account with a withdrawal
    @Transactional
    public void withdraw(String accountNumber, Long amount) {
        Optional<IcesiAccount> optionalAccount = accountRepository.findAccountByAccountNumber(accountNumber);

        if (optionalAccount.isPresent()) {
            IcesiAccount account = optionalAccount.get();
            if (hasNonNegativeBalance(account)) {
                Long newBalance = account.getBalance() - amount;
                if (newBalance >= 0) {
                    account.setBalance(newBalance);
                    accountRepository.save(account);
                } else {
                    throw new IllegalArgumentException("Account balance cannot be negative");
                }
            } else {
                throw new IllegalArgumentException("Account balance cannot be negative");
            }
            if (!account.isActive() && account.getBalance() == 0) {
                account.setActive(false);
                accountRepository.save(account);
            }
        } else {
            throw new NoSuchElementException("Account not found");
        }
    }
}




