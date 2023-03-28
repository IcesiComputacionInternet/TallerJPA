package com.example.demo.service;

import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;
import com.example.demo.mapper.IcesiAccountMapper;
import com.example.demo.model.IcesiAccount;
import com.example.demo.model.IcesiUser;
import com.example.demo.repository.IcesiAccountRepository;
import com.example.demo.repository.IcesiUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    
    private final IcesiAccountRepository icesiAccountRepository;

    private final IcesiAccountMapper IcesiAccountMapper;

    private final IcesiUserRepository icesiUserRepository;

    public ResponseIcesiAccountDTO create(IcesiAccountCreateDTO account) {
        if(account.getBalance() < 0) {
            throw new RuntimeException("The account balance cannot be negative");
        }
        IcesiAccount icesiAccount = IcesiAccountMapper.fromIcesiAccountDTO(account);
        if(icesiAccountRepository.findByAccountNumber(icesiAccount.getAccountNumber()).isPresent()){
            throw new RuntimeException("This account number is already in use");
        }
        IcesiUser icesiUser = icesiUserRepository.findByEmail(account.getIcesiUser().getEmail()).orElseThrow(() -> new RuntimeException("This icesi user is not present in the database "));
        icesiAccount.setIcesiUser(icesiUser);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(generateAccountNumber());
        return IcesiAccountMapper.fromIcesiAcountToIcesiAccountDTO(icesiAccountRepository.save(icesiAccount));
    }

    public static String generateAccountNumber() {
        // Use a Supplier lambda function to generate random digits
        Supplier<Integer> digitSupplier = () -> new Random().nextInt(10);

        // Use StringBuilder to build the account number
        StringBuilder accountNumberBuilder = new StringBuilder();
        accountNumberBuilder.append(generateDigits(digitSupplier, 3));
        accountNumberBuilder.append("-");
        accountNumberBuilder.append(generateDigits(digitSupplier, 6));
        accountNumberBuilder.append("-");
        accountNumberBuilder.append(generateDigits(digitSupplier, 2));

        return accountNumberBuilder.toString();
    }

    private static String generateDigits(Supplier<Integer> digitSupplier, int count) {
        StringBuilder digitsBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            digitsBuilder.append(digitSupplier.get());
        }
        return digitsBuilder.toString();
    }

    public void enableAccount(IcesiAccountCreateDTO account) {
        account.setActive(true);
    }

    public void disableAccount(IcesiAccountCreateDTO account) {
        if(account.getBalance() == 0) {
            account.setActive(false);
        }
        else {
            throw new RuntimeException("This accout cannot be disabled, its balances is not 0");
        }
    }

    public void withdrawalMoney(long amountToWithdraw, IcesiAccountCreateDTO account) {
        if(!account.isActive()) {
            throw new RuntimeException("Account is disabled, it is not possible to withdraw form it");
        } 
        else {
            if((account.getBalance() - amountToWithdraw) < 0) {
                throw new RuntimeException("This account does not have enough funds");
            }
            else {
                account.setBalance(account.getBalance()- amountToWithdraw);
            }
        }
    }

    public void depositMoney(long amountToDeposit, IcesiAccountCreateDTO account) {
        account.setBalance(account.getBalance() + amountToDeposit);
    }

    public void transferMoneyToAnotherAccount(long amountToTransfer, IcesiAccountCreateDTO originAccount, IcesiAccountCreateDTO destinationAccount) {
        if(!originAccount.isActive()) {
            throw new RuntimeException("The origin account is disabled");
        }
        else if(!destinationAccount.isActive()) {
            throw new RuntimeException("The destination account is disabled");
        }
        else {
            if(originAccount.getType().equals("deposit")) {
                throw new RuntimeException("The origin account is not allowed to be transfer money");
            }
            else {
                if(destinationAccount.getType().equals("deposit")) {
                    throw new RuntimeException("The destination account is not allowed to be transferred money");
                }
                else {
                    if((originAccount.getBalance() - amountToTransfer) < 0) {
                        throw new RuntimeException("The origin account does not have enough funds");
                    }
                    else {
                        originAccount.setBalance(originAccount.getBalance() - amountToTransfer);
                        destinationAccount.setBalance(destinationAccount.getBalance() + amountToTransfer);
                    }
                }
            }
        }
    }
}
