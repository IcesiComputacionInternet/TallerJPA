package co.edu.icesi.tallerjpa.runableartefact.service;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.TransactionInformationDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.response.TransactionInformationResponseDTO;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.InsufficientBalance;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.OperationNotAvailable;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.ParameterRequired;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiAccount;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class IcesiAccountService {

    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiUserRepository icesiUserRepository;

    private final IcesiAccountMapper icesiAccountMapper;

    @Transactional
    public String saveNewAccount(IcesiAccountDTO icesiAccountDTO) {
        isAValidBalance(icesiAccountDTO.getBalance());
        IcesiAccount icesiAccount = icesiAccountMapper.toIcesiAccount(icesiAccountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(generateAccountNumberThatDontExist());
        setIcesiUserToIcesiAccount(icesiAccountDTO, icesiAccount);
        icesiAccountRepository.save(icesiAccount);
        return "Account saved";
    }

    private void isAValidBalance(Long balance){
        if (balance < 0){
            throw new ParameterRequired("Balance can't be negative");
        }
    }

    private void setIcesiUserToIcesiAccount(IcesiAccountDTO icesiAccountDTO, IcesiAccount icesiAccount) {
        if(icesiUserRepository.findByEmail(icesiAccountDTO.getIcesiUserEmail()).isEmpty()){
            throw new ParameterRequired("User not found");
        }
        icesiAccount.setUser(icesiUserRepository.findByEmail(icesiAccount.getUser().getEmail()).get());
    }

    @Transactional
    public String activateAccount(String accountNumber) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent()) {
            icesiAccount.get().setActive(true);
            icesiAccountRepository.save(icesiAccount.get());
            return "Account activated";
        }
        return "Account not activated";
    }

    @Transactional
    public String deactivateAccount(String accountNumber){
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent() && validateAccountBalanceToDeactivate(icesiAccount.get())) {
            icesiAccount.get().setActive(false);
            icesiAccountRepository.save(icesiAccount.get());
            return "Account deactivated";
        }
        return "Account not deactivated";
    }

    @Transactional
    public TransactionInformationResponseDTO withdrawal(TransactionInformationDTO transactionInformationDTO) {
        IcesiAccount icesiAccount = getIcesiAccountByAccountNumber(transactionInformationDTO.getAccountNumberOrigin());

        validateAccountBalanceToWithdrawal(icesiAccount);
        validateAccountEnoughBalanceToWithdrawal(icesiAccount.getBalance(), transactionInformationDTO.getAmount());

        icesiAccount.setBalance(icesiAccount.getBalance() - transactionInformationDTO.getAmount());
        icesiAccountRepository.save(icesiAccount);

        return TransactionInformationResponseDTO.builder()
                .accountNumberOrigin(transactionInformationDTO.getAccountNumberOrigin())
                .message("Withdrawal successful")
                .amount(transactionInformationDTO.getAmount())
                .build();
    }
    private void validateAccountEnoughBalanceToWithdrawal(Long accountBalance, Long amount){
        if (accountBalance < amount){
            throw new InsufficientBalance("Can't withdrawal more than the account balance");
        }
    }

    @Transactional
    public TransactionInformationResponseDTO transfer(TransactionInformationDTO transactionInformationDTO) {
        IcesiAccount icesiAccountOrigin = getIcesiAccountByAccountNumber(transactionInformationDTO.getAccountNumberOrigin());
        IcesiAccount icesiAccountDestination = getIcesiAccountByAccountNumber(transactionInformationDTO.getAccountNumberDestination());
        Long amount = transactionInformationDTO.getAmount();

        canTransfer(icesiAccountOrigin);
        canTransfer(icesiAccountDestination);
        validateAccountBalanceToWithdrawal(icesiAccountOrigin);
        enoughBalanceToWithdrawal(icesiAccountOrigin.getBalance(), amount);

        icesiAccountOrigin.setBalance(icesiAccountOrigin.getBalance()-amount);
        icesiAccountDestination.setBalance(icesiAccountDestination.getBalance()+amount);

        icesiAccountRepository.save(icesiAccountOrigin);
        icesiAccountRepository.save(icesiAccountDestination);

        return TransactionInformationResponseDTO.builder()
                .accountNumberOrigin(transactionInformationDTO.getAccountNumberOrigin())
                .accountNumberDestination(transactionInformationDTO.getAccountNumberDestination())
                .amount(amount)
                .message("Transfer successful")
                .build();
    }

    private IcesiAccount getIcesiAccountByAccountNumber(String accountNumber){
        return icesiAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ParameterRequired("Account not found"));
    }

    private void canTransfer(IcesiAccount icesiAccount){
        if (icesiAccount.getType().equals("deposit")){
            throw new OperationNotAvailable("Can't transfer from/to a deposit account");
        }
    }


    @Transactional
    public String deposit(String accountNumber, Long amount) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent() && icesiAccount.get().isActive()) {
            icesiAccount.get().setBalance(icesiAccount.get().getBalance() + amount);
            icesiAccountRepository.save(icesiAccount.get());
            return "Deposit successful";
        }
        return "Deposit not successful";
    }

    private void enoughBalanceToWithdrawal(Long accountBalance, Long amount){
        if (accountBalance < amount){
            throw new InsufficientBalance("Can't withdrawal more than the account balance");
        }
    }
    private boolean validateAccountBalanceToDeactivate(IcesiAccount icesiAccount) {
        return icesiAccount.getBalance() == 0;
    }
    private void validateAccountBalanceToWithdrawal(IcesiAccount icesiAccount) {
        if(icesiAccount.getBalance() > 0) throw new InsufficientBalance("Can't withdrawal more than the account balance");
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
