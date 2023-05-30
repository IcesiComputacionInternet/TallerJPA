package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.Enums.Scopes;
import com.edu.icesi.TallerJPA.dto.IcesiAccountDTO;
import com.edu.icesi.TallerJPA.dto.IcesiAccountGetDTO;
import com.edu.icesi.TallerJPA.dto.IcesiTransactionDTO;
import com.edu.icesi.TallerJPA.error.exception.DetailBuilder;
import com.edu.icesi.TallerJPA.error.exception.ErrorCode;
import com.edu.icesi.TallerJPA.mapper.AccountMapper;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.edu.icesi.TallerJPA.repository.AccountRepository;
import com.edu.icesi.TallerJPA.repository.UserRepository;
import com.edu.icesi.TallerJPA.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.edu.icesi.TallerJPA.error.util.IcesiExceptionBuilder.createIcesiException;


@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final UserRepository userRepository;

    public IcesiAccountDTO save(IcesiAccountDTO accountCreateDTO) {

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), accountCreateDTO.getIcesiUser().getUserId());

        accountCreateDTO.setAccountNumber(sendToGenerateAccountNumbers());

        accountCreateDTO = verifyDuplicatedAccount(accountCreateDTO);
        validateBalanceCreate(accountCreateDTO.getBalance());
        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(accountCreateDTO);
        icesiAccount.setAccountId(UUID.randomUUID());

        return accountMapper.fromIcesiAccount(accountRepository.save(icesiAccount));
    }

    public void validateBalanceCreate(long balance) {
        if (balance < 0) {
            throw createIcesiException(
                    "BALANCED INVALID",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, " ", " ", " ")
            ).get();
        }
    }

    public IcesiAccountDTO verifyDuplicatedAccount(IcesiAccountDTO accountCreateDTO) {

        if (accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber()).isPresent()) {
            throw createIcesiException(
                    "ACCOUNT DUPLICATED",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "ACCOUNT", "ACCOUNT NUMBER", accountCreateDTO.getAccountNumber())
            ).get();

        }


        return accountCreateDTO;
    }

    public String sendToGenerateAccountNumbers() {

        String accountNumber = "";
        accountNumber += generateAccountNumber(3).get();
        accountNumber += "-";
        accountNumber += generateAccountNumber(6).get();
        accountNumber += "-";
        accountNumber += generateAccountNumber(2).get();

        return accountNumber;
    }

    public Supplier<String> generateAccountNumber(int length) {
        return () -> generateNumbers(length);
    }

    public String generateNumbers(int length) {

        String stringWithId = "";
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            stringWithId += random.nextInt(10);
        }

        return stringWithId;
    }

    public IcesiTransactionDTO withdrawals(IcesiTransactionDTO icesiTransactionDTO) {

        IcesiAccountDTO accountToWithdraw = findByAccountNumber(icesiTransactionDTO.getSourceAccount());

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), accountToWithdraw.getIcesiUser().getUserId());

        long balance = icesiTransactionDTO.getAmountMoney();

        validateTransactionBalance(accountToWithdraw, balance);

        accountToWithdraw.setBalance(accountToWithdraw.getBalance() - balance);

        icesiTransactionDTO.setResult("Successful withdrawal");

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToWithdraw));

        return icesiTransactionDTO;
    }

    public IcesiAccountDTO findByAccountNumber(String accountNumber) {

        if (accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
            throw createIcesiException(
                    "ACCOUNT NOT FOUND",
                    HttpStatus.NOT_FOUND,
                    new DetailBuilder(ErrorCode.ERR_404, "Account", "accountNumber", accountNumber)
            ).get();


        }

        return accountMapper.fromIcesiAccount(accountRepository.findByAccountNumber(accountNumber).get());
    }

    public void validateTransactionBalance(IcesiAccountDTO accountToTransaction, long moneyToTransaction) {

        if (accountToTransaction.getBalance() < moneyToTransaction || moneyToTransaction <= 0) {

            throw createIcesiException(
                    "INSUFFICIENT MONEY",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "VALUE " + moneyToTransaction, "", "")
            ).get();


        }
    }

    public IcesiTransactionDTO depositMoney(IcesiTransactionDTO icesiTransactionDTO) {

        IcesiAccountDTO accountToDeposit = findByAccountNumber(icesiTransactionDTO.getDestinationAccount());

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), accountToDeposit.getIcesiUser().getUserId());

        long moneyToDeposit = icesiTransactionDTO.getAmountMoney();
        validateTransactionBalance(accountToDeposit, moneyToDeposit);
        accountToDeposit.setBalance(accountToDeposit.getBalance() + moneyToDeposit);

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToDeposit));

        icesiTransactionDTO.setResult("Successful deposit");

        return icesiTransactionDTO;
    }

    public IcesiTransactionDTO transferMoney(IcesiTransactionDTO icesiTransactionDTO) {

        IcesiAccountDTO sourceAccountToTransfer = findByAccountNumber(icesiTransactionDTO.getSourceAccount());

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), sourceAccountToTransfer.getIcesiUser().getUserId());

        IcesiAccountDTO destinationAccountToTransfer = findByAccountNumber(icesiTransactionDTO.getDestinationAccount());

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), destinationAccountToTransfer.getIcesiUser().getUserId());

        validateAccountType(sourceAccountToTransfer, destinationAccountToTransfer);

        long moneyToTransfer = icesiTransactionDTO.getAmountMoney();

        validateTransactionBalance(sourceAccountToTransfer, moneyToTransfer);

        sourceAccountToTransfer.setBalance(sourceAccountToTransfer.getBalance() - moneyToTransfer);

        destinationAccountToTransfer.setBalance(destinationAccountToTransfer.getBalance() + moneyToTransfer);

        icesiTransactionDTO.setFinalBalanceSourceAccount(sourceAccountToTransfer.getBalance());
        icesiTransactionDTO.setFinalBalanceDestinationAccount(destinationAccountToTransfer.getBalance());

        icesiTransactionDTO.setResult("SUCCESSFUL TRANSFER");

        return icesiTransactionDTO;
    }

    public void validateAccountType(IcesiAccountDTO sourceAccount, IcesiAccountDTO destinationAccount) {

        if (sourceAccount.getType().equals("Deposit only") || destinationAccount.getType().equals("Deposit only")) {
            throw createIcesiException(
                    "INVALID ACCOUNT",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "", "", "")
            ).get();


        }
    }


    public IcesiAccountDTO setToEnableState(String accountNumber) {

        IcesiAccountDTO accountToEnable = findByAccountNumber(accountNumber);

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), accountToEnable.getIcesiUser().getUserId());

        validateStatusOfAccount(accountToEnable, true);

        accountToEnable.setActive(true);

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToEnable));

        return accountToEnable;
    }

    public void validateStatusOfAccount(IcesiAccountDTO accountCreateDTO, boolean state) {
        if (accountCreateDTO.isActive() == state) {
            throw createIcesiException(
                    "INVALID CHANGE",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "ACCOUNT " + accountCreateDTO.getAccountNumber(), "", "")
            ).get();


        }

    }
    private void verifyUserRole(String roleActualUser) {

        if (roleActualUser.equalsIgnoreCase(String.valueOf(Scopes.BANK))) {
            throw createIcesiException(
                    "UNAUTHORIZED",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401, "UNAUTHORIZED " +"", "", "")
            ).get();


        }
    }
    public void validateBalanceForDisableAccount(long balance) {
        if (balance != 0) {

            throw createIcesiException(

                    "INVALID VALUES",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "VALUES " + balance, "", "")
            ).get();


        }
    }
    public IcesiAccountDTO setToDisableState(String accountNumber) {

        IcesiAccountDTO accountToDisable = findByAccountNumber(accountNumber);

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), accountToDisable.getIcesiUser().getUserId());

        validateStatusOfAccount(accountToDisable, false);

        validateBalanceForDisableAccount(accountToDisable.getBalance());

        accountToDisable.setActive(false);

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToDisable));

        return accountToDisable;
    }

    public void verifyUserRole(String idActualUser, String roleActualUser, UUID idUserOfAccount) {

        searchUserById(idActualUser);

        if (roleActualUser.equalsIgnoreCase(String.valueOf(Scopes.USER)) && !idActualUser.equalsIgnoreCase(String.valueOf(idUserOfAccount))) {
            throw createIcesiException(
                    "UNAUTHORIZED",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401, "UNAUTHORIZED " +"", "", "")
            ).get();


        }

        verifyUserRole(roleActualUser);
    }

    public List<IcesiAccountGetDTO> findByAccounts(UUID idUser) {
        return accountRepository.findByAccounts(idUser).stream().map(accountMapper::fromIcesiAccountGet).collect(Collectors.toList());
    }

    public void searchUserById(String id) {

        if (userRepository.findById(UUID.fromString(id)).isEmpty()) {

            throw createIcesiException(
                    "USER NOT FOUND",
                    HttpStatus.NOT_FOUND,
                    new DetailBuilder(ErrorCode.ERR_404, "USER ", "", "")
            ).get();


        }
    }




}
