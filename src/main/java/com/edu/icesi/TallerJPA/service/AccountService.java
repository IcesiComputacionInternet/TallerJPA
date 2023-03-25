package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.dto.TransactionDTO;
import com.edu.icesi.TallerJPA.mapper.AccountMapper;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.edu.icesi.TallerJPA.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    public AccountCreateDTO save(AccountCreateDTO accountCreateDTO) {

        accountCreateDTO.setAccountNumber(sendToGenerateAccountNumbers());

        if (accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber()).isPresent()) {
            throw new RuntimeException("Account already exists, try again");
        }

        validateBalance(accountCreateDTO.getBalance());
        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(accountCreateDTO);
        icesiAccount.setAccountId(UUID.randomUUID());

        return accountMapper.fromIcesiAccount(accountRepository.save(icesiAccount));
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

    public void validateBalance(long balance) {
        if (balance < 0){
            throw new RuntimeException("Balance can't be below 0");
        }
    }

    public TransactionDTO withdrawals(TransactionDTO transactionDTO) {

        AccountCreateDTO accountToWithdraw = findByAccountNumber(transactionDTO.getSourceAccount());

        long balance = transactionDTO.getAmountMoney();

        validateTransactionBalance(accountToWithdraw, balance);

        accountToWithdraw.setBalance(accountToWithdraw.getBalance() - balance);

        transactionDTO.setResult("Successful withdrawal");

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToWithdraw));

        return transactionDTO;
    }

    public AccountCreateDTO findByAccountNumber(String accountNumber){

        if (accountRepository.findByAccountNumber(accountNumber).isEmpty()){
            throw new RuntimeException("Account "+accountNumber+" not found");
        }

        return accountMapper.fromIcesiAccount(accountRepository.findByAccountNumber(accountNumber).get());
    }

    public void validateTransactionBalance(AccountCreateDTO accountToTransaction, long moneyToTransaction) {

        if (accountToTransaction.getBalance() < moneyToTransaction){
            throw new RuntimeException("The account "+accountToTransaction.getAccountNumber()+" cannot perform the transaction. Insufficient money");
        }
    }

    public TransactionDTO depositMoney(TransactionDTO transactionDTO) {

        AccountCreateDTO accountToDeposit = findByAccountNumber(transactionDTO.getDestinationAccount());

        long moneyToDeposit = transactionDTO.getAmountMoney();

        validateMoneyForTransaction(moneyToDeposit);

        accountToDeposit.setBalance(accountToDeposit.getBalance() + moneyToDeposit);

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToDeposit));

        transactionDTO.setResult("Successful deposit");

        return transactionDTO;
    }

    public void validateMoneyForTransaction(long moneyToTransaction){

        if (moneyToTransaction < 0){
            throw new RuntimeException("Invalid value for transaction. Value can't be less than zero");
        }
    }

    public TransactionDTO transferMoney(TransactionDTO transactionDTO) {

        AccountCreateDTO sourceAccountToTransfer = findByAccountNumber(transactionDTO.getSourceAccount());

        AccountCreateDTO destinationAccountToTransfer = findByAccountNumber(transactionDTO.getDestinationAccount());

        validateAccountType(sourceAccountToTransfer, destinationAccountToTransfer);

        long moneyToTransfer = transactionDTO.getAmountMoney();

        validateMoneyForTransaction(moneyToTransfer);

        validateTransactionBalance(sourceAccountToTransfer, moneyToTransfer);

        sourceAccountToTransfer.setBalance(sourceAccountToTransfer.getBalance() - moneyToTransfer);

        destinationAccountToTransfer.setBalance(destinationAccountToTransfer.getBalance() + moneyToTransfer);

        accountRepository.save(accountMapper.fromIcesiAccountDTO(sourceAccountToTransfer));
        accountRepository.save(accountMapper.fromIcesiAccountDTO(destinationAccountToTransfer));

        transactionDTO.setResult("Successful transfer");

        return transactionDTO;
    }

    public void validateAccountType(AccountCreateDTO sourceAccount, AccountCreateDTO destinationAccount){

        if (sourceAccount.getType().equals("Deposit only") || destinationAccount.getType().equals("Deposit only")){
            throw new RuntimeException("It is not possible to make the transfer. At least one account is deposit only");
        }
    }


    public AccountCreateDTO setToEnableState(String accountNumber) {

        AccountCreateDTO accountToEnable = findByAccountNumber(accountNumber);

        validateStatusOfAccount(accountToEnable, true);

        accountToEnable.setActive(true);

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToEnable));

        return accountToEnable;
    }

    public void validateStatusOfAccount(AccountCreateDTO accountCreateDTO, boolean state){
        if (accountCreateDTO.isActive() == state){
            throw new RuntimeException("The account is already in that status");
        }
    }

    public AccountCreateDTO setToDisableState(String accountNumber) {

        AccountCreateDTO accountToEnable = findByAccountNumber(accountNumber);

        validateStatusOfAccount(accountToEnable, false);

        validateBalanceForDisableAccount(accountToEnable.getBalance());

        accountToEnable.setActive(false);

        accountRepository.save(accountMapper.fromIcesiAccountDTO(accountToEnable));

        return accountToEnable;
    }

    public void validateBalanceForDisableAccount(long balance){
        if (balance != 0){
            throw new RuntimeException("The account balance is not zero");
        }
    }

}
