package co.edu.icesi.tallerjpa.runableartefact.service;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiAccount;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class IcesiAccountService {

    private final IcesiAccountRepository icesiAccountRepository;

    private final IcesiAccountMapper icesiAccountMapper;

    public String saveNewAccount(IcesiAccountDTO icesiAccountDTO) {
        IcesiAccount icesiAccount = icesiAccountMapper.toIcesiAccount(icesiAccountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(generateAccountNumberThatDontExist());
        return "Account saved";
    }

    public String activateAccount(String accountNumber) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent()) {
            icesiAccount.get().setActive(true);
            return "Account activated";
        }
        return "Account not found";
    }

    public String deactivateAccount(String accountNumber){
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent() && validateAccountBalanceToDeactivate(icesiAccount.get())) {
            icesiAccount.get().setActive(false);
            return "Account deactivated";
        }
        return "Account not found";
    }

    public String withdrawal(String accountNumber, Long amount) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent()
                && validateAccountBalanceToWithdrawal(icesiAccount.get())
                && icesiAccount.get().getBalance() >= amount) {
            icesiAccount.get().setBalance(icesiAccount.get().getBalance() - amount);
            return "Withdrawal successful";
        }
        return "Account not found";
    }

    public String transfer(String accountNumberOrigin, String accountNumberDestination, Long amount) {
        Optional<IcesiAccount> icesiAccountOrigin = icesiAccountRepository.findByAccountNumber(accountNumberOrigin);
        Optional<IcesiAccount> icesiAccountDestination = icesiAccountRepository.findByAccountNumber(accountNumberDestination);
        if (icesiAccountOrigin.isPresent()
                && icesiAccountDestination.isPresent()
                && validateAccountBalanceToWithdrawal(icesiAccountOrigin.get())
                && icesiAccountOrigin.get().getBalance() >= amount) {
            icesiAccountOrigin.get().setBalance(icesiAccountOrigin.get().getBalance() - amount);
            icesiAccountDestination.get().setBalance(icesiAccountDestination.get().getBalance() + amount);
            return "Transfer successful";
        }
        return "Account not found";
    }


    public String deposit(String accountNumber, Long amount) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent()) {
            icesiAccount.get().setBalance(icesiAccount.get().getBalance() + amount);
            return "Deposit successful";
        }
        return "Account not found";
    }

    private boolean validateAccountBalanceToDeactivate(IcesiAccount icesiAccount) {
        return icesiAccount.getBalance() == 0;
    }
    private boolean validateAccountBalanceToWithdrawal(IcesiAccount icesiAccount) {
        return icesiAccount.getBalance() > 0;
    }

    private String generateAccountNumberThatDontExist() {
        boolean accountNumberExists = true;
        String accountNumber = null;

        while (accountNumberExists) {
            List<String> randomNumbers = new Random()
                    .ints(1, 1000000, 9999999)
                    .mapToObj(num -> String.format("%02d-%06d-%02d", num / 1000000, num % 1000000, num % 100))
                    .toList();
            accountNumber = randomNumbers.get(0);
            accountNumberExists = validateAccountNumber(accountNumber);
        }
        return accountNumber;
    }

    private boolean validateAccountNumber(String accountNumber) {
        return icesiAccountRepository.existsByAccountNumber(accountNumber);
    }
}
