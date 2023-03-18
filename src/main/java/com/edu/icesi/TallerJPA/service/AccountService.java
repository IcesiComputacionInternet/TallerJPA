package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
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

    public IcesiAccount save(AccountCreateDTO accountCreateDTO) {

        accountCreateDTO.setAccountNumber(sendToGenerateAccountNumbers());

        if (accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber()).isPresent()) {
            throw new RuntimeException("Account already exists, try again");
        }

        if (validateBalance(accountCreateDTO.getBalance())) {
            throw new RuntimeException("Balance can't be below 0");
        }

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(accountCreateDTO);
        icesiAccount.setAccountId(UUID.randomUUID());

        return accountRepository.save(icesiAccount);
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

    public boolean validateBalance(long balance) {

        return balance < 0;
    }

    public void setStateAccount(AccountCreateDTO account, String accountNumber) {

        if (accountRepository.findByAccountNumber(accountNumber).isPresent()){
            if (account.getBalance() == 0 && account.isActive()){
                account.setActive(false);

            } else if (!account.isActive()) {
                account.setActive(true);

            }else{
                throw new RuntimeException("Account can't change status");
            }
        }else {
            throw new RuntimeException("Account not found");
        }

    }

    public void withdrawals(AccountCreateDTO account, String accountNumber, int moneyToWithdraw) {

        if (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            long balance = account.getBalance();

            if (balance > moneyToWithdraw) {
                account.setBalance(balance - moneyToWithdraw);

            } else {
                throw new RuntimeException("Insufficient money");
            }
        } else {
            throw new RuntimeException("Account not found");
        }

    }

    public void depositMoney(AccountCreateDTO account, String accountNumber,int moneyToDeposit) {

        if (accountRepository.findByAccountNumber(accountNumber).isPresent() && moneyToDeposit > 0){
            account.setBalance(account.getBalance() + moneyToDeposit);

        }else if (moneyToDeposit <= 0){
            throw new RuntimeException("Invalid value");
        }
        else {
            throw new RuntimeException("Account not found");
        }
    }

    public void transferMoney(AccountCreateDTO sourceAccount, AccountCreateDTO destinationAccount, int moneyToTransfer) {

        if (accountRepository.findByAccountNumber(sourceAccount.getAccountNumber()).isPresent() &&
                accountRepository.findByAccountNumber(destinationAccount.getAccountNumber()).isPresent() &&
                validateAccountType(sourceAccount, destinationAccount)){

            if (sourceAccount.getBalance() >= moneyToTransfer ){
                sourceAccount.setBalance(sourceAccount.getBalance() - moneyToTransfer);
                destinationAccount.setBalance(destinationAccount.getBalance() + moneyToTransfer);

            } else if (moneyToTransfer < 0) {
                throw new RuntimeException("Invalid value");

            } else{
                throw new RuntimeException("Insufficient money to make a transfer");
            }
        }else {
            validateAccountType(sourceAccount, destinationAccount);
        }
    }

    private boolean validateAccountType(AccountCreateDTO accountCreateDTO, AccountCreateDTO accountCreateDTO1) {

        boolean verificationOfType = true;

        if (accountCreateDTO == null || accountCreateDTO1 == null) {
            throw new RuntimeException("It is not possible to make the transfer. At least one account not exists");
        }
        else if(accountCreateDTO.getType().equalsIgnoreCase("Deposit Only") || accountCreateDTO1.getType().equalsIgnoreCase("Deposit Only")){
            throw new RuntimeException("It is not possible to make the transfer. At least one account is deposit only");
        }

        return verificationOfType;
    }

}
