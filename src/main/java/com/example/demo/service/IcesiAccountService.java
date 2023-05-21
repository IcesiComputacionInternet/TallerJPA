package com.example.demo.service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;
import com.example.demo.DTO.ResponseTransactionDTO;
import com.example.demo.DTO.TransactionCreateDTO;
import com.example.demo.error.exception.DetailBuilder;
import com.example.demo.error.exception.ErrorCode;
import com.example.demo.mapper.IcesiAccountMapper;
import com.example.demo.model.IcesiAccount;
import com.example.demo.model.IcesiUser;
import com.example.demo.repository.IcesiAccountRepository;
import com.example.demo.repository.IcesiUserRepository;
import com.example.demo.error.util.IcesiExceptionBuilder;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    
    private final IcesiAccountRepository icesiAccountRepository;

    private final IcesiAccountMapper icesiAccountMapper;

    private final IcesiUserRepository icesiUserRepository;

    public ResponseIcesiAccountDTO create(IcesiAccountCreateDTO account) {
        Optional<IcesiAccount> existingIcesiAccount = icesiAccountRepository.findByAccountNumber(account.getAccountNumber());

        existingIcesiAccount.ifPresent(u -> {throw new RuntimeException("This account number is already in use");});
        if(account.getBalance() < 0) {throw new RuntimeException("The account balance cannot be negative");}
        IcesiUser icesiUser = icesiUserRepository.findByEmail(account.getIcesiUser().getEmail())
            .orElseThrow(() -> new RuntimeException("This icesi user is not present in the database "));

        IcesiAccount icesiAccount = icesiAccountMapper.fromIcesiAccountCreateDTO(account);

        icesiAccount.setIcesiUser(icesiUser);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(checkAccountNumber(generateAccountNumber()));
        icesiAccount.setActive(true);

        return icesiAccountMapper.fromIcesiAccountToResponseIcesiAccountDTO(icesiAccountRepository.save(icesiAccount));
    }

    /*The following method is used to generate a random account number whose format is XXX-XXXXXX-XX
    where X is a random digit between 0 and 9*/
    public static String generateAccountNumber() {
     
        //The following line creates a IntStream of 11 digits with values between 0(inclusive) and 10(exclusive)
        IntStream intStream = new Random().ints(11,0,10); 

        String digits = intStream.mapToObj(Integer::toString).collect(Collectors.joining());

        return  String.format("%s-%s-%s", 
            digits.substring(0, 3),
            digits.substring(3, 9),
            digits.substring(9, 11));
    }

    //This method checks if the generated account number is actually unique
    private String checkAccountNumber(String accountNumber) {
        Optional<IcesiAccount> existingIcesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        
        if(existingIcesiAccount.isPresent()) {
            return checkAccountNumber(generateAccountNumber());
        }
        
        return accountNumber;
    }

    public ResponseIcesiAccountDTO enableAccount(IcesiAccountCreateDTO account) {
        account.setActive(true);
        return icesiAccountMapper.fromIcesiAccountCreateDTOToResponseIcesiAccountDTO(account);
    }

    public ResponseIcesiAccountDTO disableAccount(IcesiAccountCreateDTO account) {
        if(account.getBalance() == 0) {
            account.setActive(false);
        }
        else {
            throw new RuntimeException("This accout cannot be disabled, its balances is not 0");
        }

        return icesiAccountMapper.fromIcesiAccountCreateDTOToResponseIcesiAccountDTO(account);
    }

    public ResponseTransactionDTO withdrawalMoney(TransactionCreateDTO transactionCreateDTO) {
        IcesiAccount account = findIcesiAccountByAccountNumber(transactionCreateDTO.getSenderAccountNumber());

        if(!account.isActive()) {
            throw new RuntimeException("Account is disabled, it is not possible to withdraw form it");
        } 

        if((account.getBalance() - transactionCreateDTO.getAmount()) < 0) {
            throw new RuntimeException("This account does not have enough funds");
        }
        
        account.setBalance(account.getBalance() - transactionCreateDTO.getAmount());

        return icesiAccountMapper.fromTransactionCrateDTO(transactionCreateDTO);
    }

    public ResponseTransactionDTO depositMoney(TransactionCreateDTO transactionCreateDTO) {
        IcesiAccount account = findIcesiAccountByAccountNumber(transactionCreateDTO.getReceiverAccountNumber());

        if(!account.isActive()) {
            throw new RuntimeException("Account is disabled, it is not possible to deposit money to it");
        }

        account.setBalance(account.getBalance() + transactionCreateDTO.getAmount());

        return icesiAccountMapper.fromTransactionCrateDTO(transactionCreateDTO);
    }

    public ResponseTransactionDTO transferMoneyToAnotherAccount(TransactionCreateDTO transactionCreateDTO) {
        IcesiAccount originAccount = findIcesiAccountByAccountNumber(transactionCreateDTO.getSenderAccountNumber());
        IcesiAccount destinationAccount = findIcesiAccountByAccountNumber(transactionCreateDTO.getReceiverAccountNumber());

        if(!originAccount.isActive()) {
            throw new RuntimeException("The origin account is disabled");
        }

        if(!destinationAccount.isActive()) {
            throw new RuntimeException("The destination account is disabled");
        }

        if (originAccount.getType() == "deposit") {
            throw new RuntimeException("The origin account is not allowed to be transfer money");
        }
    
        if(destinationAccount.getType() == "deposit") {
            throw new RuntimeException("The destination account is not allowed to be transferred money");
        }
        
        if((originAccount.getBalance() - transactionCreateDTO.getAmount()) < 0) {
            throw new RuntimeException("The origin account does not have enough funds");
        }
    
        originAccount.setBalance(originAccount.getBalance() - transactionCreateDTO.getAmount());
        destinationAccount.setBalance(destinationAccount.getBalance() + transactionCreateDTO.getAmount());

        return icesiAccountMapper.fromTransactionCrateDTO(transactionCreateDTO);
    }

    private IcesiAccount findIcesiAccountByAccountNumber(String accountNumber) {
        return icesiAccountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(IcesiExceptionBuilder.createIcesiException(
                "The account number" + accountNumber  + "is not present in the database",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "account", "the number", accountNumber)
        ));
    }
}
