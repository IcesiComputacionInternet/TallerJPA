package com.example.demo.service;

import java.lang.StackWalker.Option;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;
import com.example.demo.mapper.IcesiAccountMapper;
import com.example.demo.model.IcesiAccount;
import com.example.demo.model.IcesiUser;
import com.example.demo.model.TypeIcesiAccount;
import com.example.demo.repository.IcesiAccountRepository;
import com.example.demo.repository.IcesiUserRepository;

import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice.Return;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    
    private final IcesiAccountRepository icesiAccountRepository;

    private final IcesiAccountMapper IcesiAccountMapper;

    private final IcesiUserRepository icesiUserRepository;

    public ResponseIcesiAccountDTO create(IcesiAccountCreateDTO account) {
        Optional<IcesiAccount> existingIcesiAccount = icesiAccountRepository.findByAccountNumber(account.getAccountNumber());

        existingIcesiAccount.ifPresent(u -> {throw new RuntimeException("This account number is already in use");});
        if(account.getBalance() < 0) {throw new RuntimeException("The account balance cannot be negative");}
        IcesiUser icesiUser = icesiUserRepository.findByEmail(account.getIcesiUser().getEmail())
            .orElseThrow(() -> new RuntimeException("This icesi user is not present in the database "));

        IcesiAccount icesiAccount = IcesiAccountMapper.fromIcesiAccountCreateDTO(account);

        icesiAccount.setIcesiUser(icesiUser);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(checkAccountNumber(generateAccountNumber()));
        icesiAccount.setActive(true);

        return IcesiAccountMapper.fromIcesiAcountToIcesiAccountCreateDTO(icesiAccountRepository.save(icesiAccount));
    }

    /*The following method is used to generate a random account number whose format is XXX-XXXXXX-XX
    where X is a random digit between 0 and 9*/
    
    public static String generateAccountNumber() {
        
        //The following line creates a IntStream of 11 digits with values between 0(inclusive) and 10(exclusive)
        IntStream intStream = new Random().ints(11,0,10); 

        String digits = intStream.mapToObj(Integer::toString).collect(Collectors.joining());

        //Each %a, %b, %c corresponds to each one of the following substrings
        String accountNumber = String.format("%a-%b-%c", 
            digits.substring(0, 3),
            digits.substring(3, 9),
            digits.substring(9, 11));

        return accountNumber;
    }

    //This method checks if the generated account number is actually unique
    private String checkAccountNumber(String accountNumber) {
        Optional<IcesiAccount> existingIcesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        
        if(existingIcesiAccount.isPresent()) {
            return checkAccountNumber(generateAccountNumber());
        }
        
        return accountNumber;
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
        
        if((account.getBalance() - amountToWithdraw) < 0) {
            throw new RuntimeException("This account does not have enough funds");
        }
  
        account.setBalance(account.getBalance() - amountToWithdraw);
    }

    public void depositMoney(long amountToDeposit, IcesiAccountCreateDTO account) {
        if(!account.isActive()) {
            throw new RuntimeException("Account is disabled, it is not possible to deposit money to it");
        }

        account.setBalance(account.getBalance() + amountToDeposit);
    }

    public void transferMoneyToAnotherAccount(long amountToTransfer, IcesiAccountCreateDTO originAccount, IcesiAccountCreateDTO destinationAccount) {
        if(!originAccount.isActive()) {
            throw new RuntimeException("The origin account is disabled");
        }

        if(!destinationAccount.isActive()) {
            throw new RuntimeException("The destination account is disabled");
        }

        if(originAccount.getType() == TypeIcesiAccount.deposit.name()) {
            throw new RuntimeException("The origin account is not allowed to be transfer money");
        }
    
        if(destinationAccount.getType() == TypeIcesiAccount.deposit.name()) {
            throw new RuntimeException("The destination account is not allowed to be transferred money");
        }
        
        if((originAccount.getBalance() - amountToTransfer) < 0) {
            throw new RuntimeException("The origin account does not have enough funds");
        }
    
        originAccount.setBalance(originAccount.getBalance() - amountToTransfer);
        destinationAccount.setBalance(destinationAccount.getBalance() + amountToTransfer);
    }
}
