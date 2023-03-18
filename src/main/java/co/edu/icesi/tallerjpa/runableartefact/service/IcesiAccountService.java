package co.edu.icesi.tallerjpa.runableartefact.service;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.InsufficientBalance;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.OperationNotAvailable;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.ParameterRequired;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiAccount;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class IcesiAccountService {

    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiUserRepository icesiUserRepository;

    private final IcesiAccountMapper icesiAccountMapper;

    public String saveNewAccount(IcesiAccountDTO icesiAccountDTO) {
        IcesiAccount icesiAccount = icesiAccountMapper.toIcesiAccount(icesiAccountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(generateAccountNumberThatDontExist());
        setIcesiUserToIcesiAccount(icesiAccountDTO, icesiAccount);
        icesiAccountRepository.save(icesiAccount);
        return "Account saved";
    }

    private void setIcesiUserToIcesiAccount(IcesiAccountDTO icesiAccountDTO, IcesiAccount icesiAccount) {
        if(icesiUserRepository.findByEmail(icesiAccountDTO.getIcesiUserEmail()).isEmpty()){
            throw new ParameterRequired("User not found");
        }
        icesiAccount.setUser(icesiUserRepository.findByEmail(icesiAccount.getUser().getEmail()).get());
    }

    public String activateAccount(String accountNumber) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent()) {
            icesiAccount.get().setActive(true);
            icesiAccountRepository.save(icesiAccount.get());
            return "Account activated";
        }
        return "Account not activated";
    }

    public String deactivateAccount(String accountNumber){
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent() && validateAccountBalanceToDeactivate(icesiAccount.get())) {
            icesiAccount.get().setActive(false);
            icesiAccountRepository.save(icesiAccount.get());
            return "Account deactivated";
        }
        return "Account not deactivated";
    }

    public String withdrawal(String accountNumber, Long amount) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent()
                && validateAccountBalanceToWithdrawal(icesiAccount.get())
                && validateAccountEnoughBalanceToWithdrawal(icesiAccount.get().getBalance(), amount)) {
            icesiAccount.get().setBalance(icesiAccount.get().getBalance() - amount);
            icesiAccountRepository.save(icesiAccount.get());
            return "Withdrawal successful";
        }
        return "Withdrawal not successful";
    }
    private boolean validateAccountEnoughBalanceToWithdrawal(Long accountBalance, Long amount){
        if (accountBalance < amount){
            throw new InsufficientBalance("Can't withdrawal more than the account balance");
        }
        return true;
    }

    public String transfer(String accountNumberOrigin, String accountNumberDestination, Long amount) {
        Optional<IcesiAccount> icesiAccountOrigin = icesiAccountRepository.findByAccountNumber(accountNumberOrigin);
        Optional<IcesiAccount> icesiAccountDestination = icesiAccountRepository.findByAccountNumber(accountNumberDestination);
        if (icesiAccountOrigin.isPresent()
                && icesiAccountDestination.isPresent()
                && validateAccountBalanceToWithdrawal(icesiAccountOrigin.get())
                && canTransfer(icesiAccountOrigin.get())
                && canTransfer(icesiAccountDestination.get())
                && icesiAccountOrigin.get().getBalance() >= amount) {
            withdrawal(accountNumberOrigin, amount);
            deposit(accountNumberDestination, amount);
            return "Transfer successful";
        }
        return "Transfer not successful";
    }

    private boolean canTransfer(IcesiAccount icesiAccount){
        if (icesiAccount.getType().equals("deposit")){
            throw new OperationNotAvailable("Can't transfer from/to a deposit account");
        }
        return true;
    }


    public String deposit(String accountNumber, Long amount) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent() && icesiAccount.get().isActive()) {
            icesiAccount.get().setBalance(icesiAccount.get().getBalance() + amount);
            icesiAccountRepository.save(icesiAccount.get());
            return "Deposit successful";
        }
        return "Deposit not successful";
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
