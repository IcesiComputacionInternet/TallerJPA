package com.example.demo.service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.List;
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

        existingIcesiAccount.ifPresent(u -> {throw IcesiExceptionBuilder.createIcesiException(
                "This account number is already in use",
                HttpStatus.BAD_REQUEST, 
                new DetailBuilder(ErrorCode.ERR_400, "account", "the number", account.getAccountNumber())
            ).get();});
            
        if(account.getBalance() < 0) {
            throw IcesiExceptionBuilder.createIcesiException(
                "The account balance cannot be negative",
                HttpStatus.BAD_REQUEST, 
                new DetailBuilder(ErrorCode.ERR_400, "account", "balance", account.getAccountNumber())
            ).get();
        }

        IcesiUser icesiUser = icesiUserRepository.findByEmail(account.getIcesiUser().getEmail())
            .orElseThrow(() -> IcesiExceptionBuilder.createIcesiException(
                "This icesi user is not present in the database",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "icesiUser", "email", account.getIcesiUser().getEmail())
            ).get());

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
            throw IcesiExceptionBuilder.createIcesiException(
                "This account cannot be disabled, its balance is not 0",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "account", "balance", String.valueOf(account.getBalance()))
            ).get();
        }

        return icesiAccountMapper.fromIcesiAccountCreateDTOToResponseIcesiAccountDTO(account);
    }

    public ResponseTransactionDTO withdrawalMoney(TransactionCreateDTO transactionCreateDTO) {
        IcesiAccount account = findIcesiAccountByAccountNumber(transactionCreateDTO.getSenderAccountNumber());

        if(!account.isActive()) {
            throw IcesiExceptionBuilder.createIcesiException(
                "Account is disabled, it is not possible to withdraw from it",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "account", "status", "disabled")
            ).get();
        } 

        if((account.getBalance() - transactionCreateDTO.getAmount()) < 0) {
            throw IcesiExceptionBuilder.createIcesiException(
                "This account does not have enough funds",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "account", "funds", String.valueOf(account.getBalance()))
            ).get();
        }

        account.setBalance(account.getBalance() - transactionCreateDTO.getAmount());

        return icesiAccountMapper.fromTransactionCrateDTO(transactionCreateDTO);
    }

    public ResponseTransactionDTO depositMoney(TransactionCreateDTO transactionCreateDTO) {
        IcesiAccount account = findIcesiAccountByAccountNumber(transactionCreateDTO.getSenderAccountNumber());

        if(!account.isActive()) {
            throw IcesiExceptionBuilder.createIcesiException(
                "Account is disabled, it is not possible to deposit money to it",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "account", "status", "disabled")
            ).get();
        }

        account.setBalance(account.getBalance() + transactionCreateDTO.getAmount());

        return icesiAccountMapper.fromTransactionCrateDTO(transactionCreateDTO);
    }

    public ResponseTransactionDTO transferMoneyToAnotherAccount(TransactionCreateDTO transactionCreateDTO) {
        IcesiAccount originAccount = findIcesiAccountByAccountNumber(transactionCreateDTO.getSenderAccountNumber());
        IcesiAccount destinationAccount = findIcesiAccountByAccountNumber(transactionCreateDTO.getReceiverAccountNumber());

        if(!originAccount.isActive()) {
            throw IcesiExceptionBuilder.createIcesiException(
                "The origin account is disabled",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "account", "status", "disabled")
            ).get();
        }

        if(!destinationAccount.isActive()) {
            throw IcesiExceptionBuilder.createIcesiException(
                "The destination account is disabled",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "account", "status", "disabled")
            ).get();
        }

        if (originAccount.getType().name() == "deposit") {
            throw IcesiExceptionBuilder.createIcesiException(
                "The origin account is not allowed to transfer money",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "account", "type", "deposit")
            ).get();
        }
    
        if(destinationAccount.getType().name() == "deposit") {
            throw IcesiExceptionBuilder.createIcesiException(
                "The destination account is not allowed to receive transferred money",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "account", "type", "deposit")
            ).get();
        }
        
        if((originAccount.getBalance() - transactionCreateDTO.getAmount()) < 0) {
            throw IcesiExceptionBuilder.createIcesiException(
                "The origin account does not have enough funds",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "account", "balance", "insufficient")
            ).get();
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

    public List<ResponseIcesiAccountDTO> findAccountsOwnedByAUser(String userId) {
        return findUserById(userId).getAccounts().stream()
            .map(icesiAccountMapper::fromIcesiAccountToResponseIcesiAccountDTO)
            .collect(Collectors.toList());
    }

    public IcesiUser findUserById(String userId) {
        return icesiUserRepository.findById(UUID.fromString(userId))
        .orElseThrow(IcesiExceptionBuilder.createIcesiException(
                "The user with id " + userId + " is not present in the database",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "user", "id", userId)
        ));
    }


}
