package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.Enums.Scopes;
import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.dto.TransactionDTO;
import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.edu.icesi.TallerJPA.error.exception.DetailBuilder;
import com.edu.icesi.TallerJPA.error.exception.ErrorCode;
import com.edu.icesi.TallerJPA.mapper.AccountMapper;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.AccountRepository;
import com.edu.icesi.TallerJPA.repository.UserRepository;
import com.edu.icesi.TallerJPA.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

import static com.edu.icesi.TallerJPA.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final UserRepository userRepository;

    public AccountCreateDTO save(AccountCreateDTO accountCreateDTO) {

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), accountCreateDTO.getIcesiUser().getUserId());

        accountCreateDTO.setAccountNumber(sendToGenerateAccountNumbers());

        accountCreateDTO = verifyDuplicatedAccount(accountCreateDTO);

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(accountCreateDTO);
        icesiAccount.setAccountId(UUID.randomUUID());

        return accountMapper.fromIcesiAccount(accountRepository.save(icesiAccount));
    }

    public AccountCreateDTO verifyDuplicatedAccount(AccountCreateDTO accountCreateDTO){

        if (accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber()).isPresent()) {
            throw createIcesiException(
                    "Duplicated account",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "account","accountNumber", accountCreateDTO.getAccountNumber())
            ).get();
        }
        return accountCreateDTO;
    }

    public String sendToGenerateAccountNumbers(){

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

    public TransactionDTO withdrawals(TransactionDTO transactionDTO) {

        AccountCreateDTO accountToWithdraw = findByAccountNumber(transactionDTO.getSourceAccount());

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), accountToWithdraw.getIcesiUser().getUserId());

        long balance = transactionDTO.getAmountMoney();

        validateTransactionBalance(accountToWithdraw, balance);

        accountToWithdraw.setBalance(accountToWithdraw.getBalance() - balance);

        transactionDTO.setResult("Successful withdrawal");

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToWithdraw));

        return transactionDTO;
    }

    public AccountCreateDTO findByAccountNumber(String accountNumber){

        if (accountRepository.findByAccountNumber(accountNumber).isEmpty()){
            throw createIcesiException(
                    "Account not found",
                    HttpStatus.NOT_FOUND,
                    new DetailBuilder(ErrorCode.ERR_404, "Account", "accountNumber", accountNumber)
            ).get();
        }

        return accountMapper.fromIcesiAccount(accountRepository.findByAccountNumber(accountNumber).get());
    }

    public void validateTransactionBalance(AccountCreateDTO accountToTransaction, long moneyToTransaction) {

        if (accountToTransaction.getBalance() < moneyToTransaction){
            throw createIcesiException(
                    "Insufficient money",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "value "+moneyToTransaction,"transaction", "Insufficient money")
            ).get();
        }
    }

    public TransactionDTO depositMoney(TransactionDTO transactionDTO) {

        AccountCreateDTO accountToDeposit = findByAccountNumber(transactionDTO.getDestinationAccount());

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), accountToDeposit.getIcesiUser().getUserId());

        long moneyToDeposit = transactionDTO.getAmountMoney();

        accountToDeposit.setBalance(accountToDeposit.getBalance() + moneyToDeposit);

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToDeposit));

        transactionDTO.setResult("Successful deposit");

        return transactionDTO;
    }

    public TransactionDTO transferMoney(TransactionDTO transactionDTO) {

        AccountCreateDTO sourceAccountToTransfer = findByAccountNumber(transactionDTO.getSourceAccount());

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), sourceAccountToTransfer.getIcesiUser().getUserId());

        AccountCreateDTO destinationAccountToTransfer = findByAccountNumber(transactionDTO.getDestinationAccount());

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), destinationAccountToTransfer.getIcesiUser().getUserId());

        validateAccountType(sourceAccountToTransfer, destinationAccountToTransfer);

        long moneyToTransfer = transactionDTO.getAmountMoney();

        validateTransactionBalance(sourceAccountToTransfer, moneyToTransfer);

        sourceAccountToTransfer.setBalance(sourceAccountToTransfer.getBalance() - moneyToTransfer);

        destinationAccountToTransfer.setBalance(destinationAccountToTransfer.getBalance() + moneyToTransfer);

        transactionDTO.setFinalBalanceSourceAccount(sourceAccountToTransfer.getBalance());
        transactionDTO.setFinalBalanceDestinationAccount(destinationAccountToTransfer.getBalance());

        transactionDTO.setResult("Successful transfer");

        return transactionDTO;
    }

    public void validateAccountType(AccountCreateDTO sourceAccount, AccountCreateDTO destinationAccount){

        if (sourceAccount.getType().equals("Deposit only") || destinationAccount.getType().equals("Deposit only")){
            throw createIcesiException(
                    "Invalid account",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "At least one account","transaction", "Is deposit only")
            ).get();
        }
    }


    public AccountCreateDTO setToEnableState(String accountNumber) {

        AccountCreateDTO accountToEnable = findByAccountNumber(accountNumber);

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), accountToEnable.getIcesiUser().getUserId());

        validateStatusOfAccount(accountToEnable, true);

        accountToEnable.setActive(true);

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToEnable));

        return accountToEnable;
    }

    public void validateStatusOfAccount(AccountCreateDTO accountCreateDTO, boolean state){
        if (accountCreateDTO.isActive() == state){
            throw createIcesiException(
                    "Invalid change",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "account "+accountCreateDTO.getAccountNumber(),"change status", "Is already in that status")
            ).get();
        }
    }

    public AccountCreateDTO setToDisableState(String accountNumber) {

        AccountCreateDTO accountToDisable = findByAccountNumber(accountNumber);

        verifyUserRole(IcesiSecurityContext.getCurrentUserId(), IcesiSecurityContext.getCurrentRol(), accountToDisable.getIcesiUser().getUserId());

        validateStatusOfAccount(accountToDisable, false);

        validateBalanceForDisableAccount(accountToDisable.getBalance());

        accountToDisable.setActive(false);

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToDisable));

        return accountToDisable;
    }

    public void verifyUserRole(String idActualUser, String roleActualUser,UUID idUserOfAccount){

        searchUserById(idActualUser);

        if (roleActualUser.equalsIgnoreCase(String.valueOf(Scopes.USER)) && !idActualUser.equalsIgnoreCase(String.valueOf(idUserOfAccount))){
            throw createIcesiException(
                    "User unauthorized",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();
        }

        verifyUserRole(roleActualUser);
    }

    private void verifyUserRole(String roleActualUser){

        if (roleActualUser.equalsIgnoreCase(String.valueOf(Scopes.BANK))){
            throw createIcesiException(
                    "User unauthorized",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();
        }
    }

    public void searchUserById(String id){

        if (userRepository.findById(UUID.fromString(id)).isEmpty()){
            throw createIcesiException(
                    "User not found",
                    HttpStatus.NOT_FOUND,
                    new DetailBuilder(ErrorCode.ERR_404, "User ", "id", id)
            ).get();
        }
    }

    public void validateBalanceForDisableAccount(long balance){
        if (balance != 0){
            throw createIcesiException(
                    "Invalid value",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "value "+balance, "disable account", "The account balance is not zero")
            ).get();
        }
    }

}
