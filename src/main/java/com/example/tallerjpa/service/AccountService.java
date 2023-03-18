package com.example.tallerjpa.service;

import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.mapper.AccountMapper;

import com.example.tallerjpa.model.IcesiAccount;
import com.example.tallerjpa.repository.AccountRepository;
import com.example.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;


    public IcesiAccount saveAccount(AccountDTO accountDTO){
        IcesiAccount icesiAccount = createAccount(accountDTO);
        return accountRepository.save(icesiAccount);
    }

    public IcesiAccount createAccount(AccountDTO accountDTO){
        IcesiAccount icesiAccount = accountMapper.fromAccountDTO(accountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(generateAccountNumber());
        setTypeAccount(accountDTO.getType(), icesiAccount);

        icesiAccount.setIcesiUser(userRepository.searchByEmail(accountDTO.getEmailUser()).orElseThrow(()-> new RuntimeException("User doesn't exists")));
        return icesiAccount;

    }

    public void setTypeAccount(String type, IcesiAccount icesiAccount){
        try {
            icesiAccount.setType(type.toUpperCase());
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Account type does not exist");
        }
    }

    private String generateAccountNumber() {
        Random rand = new Random();
        int firstSet = rand.nextInt(1000);
        int secondSet = rand.nextInt(1000000);
        int thirdSet = rand.nextInt(100);
        String accountNumber;
        accountNumber = String.format("%03d-%06d-%02d", firstSet, secondSet, thirdSet);
        if(accountRepository.existsByAccountNumber(accountNumber)){
            generateAccountNumber();
        }
        return accountNumber;
    }

    @Transactional
    public String activateAccount(String accountNumber){
        accountRepository.activateAccount(accountNumber);
        return "The account was activated";
    }

    @Transactional
    public String deactivateAccount(String accountNumber){
        Long balance = accountRepository.getBalance(accountNumber);
        if(balance>0){
            throw new RuntimeException("The account can't be deactivated because the balance is not 0");
        }else{
            accountRepository.deactivateAccount(accountNumber);
            return "The account is now deactivated";
        }
    }

    @Transactional
    public String withdrawMoney(String accountNumber, Long amount){
        boolean active = accountRepository.isActive(accountNumber);
        Long balance = accountRepository.getBalance(accountNumber);
        if (balance<amount){
            throw new RuntimeException("The account doesn't have enough money");
        }else {
            if(active){
                    accountRepository.withdrawMoney(accountNumber, amount);
                    return "The withdrawal was done successful";
            }else {
                throw new RuntimeException("This account is not active, so can't be done transactions");
            }
        }

    }

    @Transactional
    public String depositMoney(String accountNumber, Long amount){
        boolean active = accountRepository.isActive(accountNumber);
        if(active){
            if(amount>0){
                accountRepository.depositMoney(accountNumber, amount);
                return "Deposit was successful";
            } else {
                throw new RuntimeException("You can't use negative amounts, please verify");
            }
        }else {
            throw new RuntimeException("This account is not active, so can't be done transactions");
        }

    }

    @Transactional
    public String transferMoney(String accountOrigin, String accountDestination, Long amount){
        accountRepository.getStateOfAccount(accountOrigin).orElseThrow(()-> new RuntimeException("The origin account is not active"));
        accountRepository.getStateOfAccount(accountDestination).orElseThrow(()->new RuntimeException("The destination account is not active"));
        String typeOrigin = accountRepository.getType(accountOrigin);
        String typeDestination = accountRepository.getType(accountDestination);
        if(typeOrigin.equals("DEPOSIT")){
            throw new RuntimeException("The origin account is only for deposits, so can't transfer");
        } else if (typeDestination.equals("DEPOSIT")) {
            throw new RuntimeException("The destination account is only for deposits, so can't transfer");
        } else {
            Long balance = accountRepository.getBalance(accountOrigin);
            if(balance<amount){
                throw new RuntimeException("The account doesn't have enough money for do this transfer");
            }else {
                withdrawMoney(accountOrigin, amount);
                depositMoney(accountDestination, amount);
                return "The transfer was done successfully";
            }


        }
    }

}
