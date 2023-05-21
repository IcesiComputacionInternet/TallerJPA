package co.com.icesi.TallerJpa.service;

import co.com.icesi.TallerJpa.dto.IcesiAccountRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiAccountResponseDTO;
import co.com.icesi.TallerJpa.dto.TransactionDTO;
import co.com.icesi.TallerJpa.enums.AccountType;
import co.com.icesi.TallerJpa.error.exception.DetailBuilder;
import co.com.icesi.TallerJpa.error.exception.ErrorCode;
import co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions.*;
import co.com.icesi.TallerJpa.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJpa.model.IcesiAccount;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiAccountRepository;
import co.com.icesi.TallerJpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static co.com.icesi.TallerJpa.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiAccountMapper icesiAccountMapper;
    private final IcesiUserRepository icesiUserRepository;

    public IcesiAccountResponseDTO saveAccount(IcesiAccountRequestDTO accountDTO){
        IcesiUser icesiUser = icesiUserRepository.findByEmail(accountDTO.getUser())
                .orElseThrow(createIcesiException(
                        "User: "+accountDTO.getUser()+" not found",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404,"IcesiUser","Email",accountDTO.getUser())
                ));
        if(accountDTO.getBalance() < 0){
            throw createIcesiException(
                    "The balance can't be lower than 0",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Balance","lower than 0")
            ).get();
        }
        IcesiAccount icesiAccount = icesiAccountMapper.fromAccountDto(accountDTO);
        icesiAccount.setIcesiUser(icesiUser);
        icesiAccount.setActive(true);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(generateAccountNumber());

        return icesiAccountMapper.fromIcesiAccountToResponse(icesiAccountRepository.save(icesiAccount));
    }

    public IcesiAccountResponseDTO getAccountByAccountNumber(String accountNumber){
        return icesiAccountMapper.fromIcesiAccountToResponse(
                icesiAccountRepository.findByAccountNumber(accountNumber)
                        .orElseThrow(createIcesiException(
                                "Account not found",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404,"IcesiAccount","accountNumber",accountNumber)
                        ))
        );
    }


    public TransactionDTO enableAccount(String accountNumber, UUID userId){
        isAccountOwner(accountNumber, userId);
        icesiAccountRepository.enableByAccountNumber(accountNumber);
        return TransactionDTO.builder()
                .accountNumberOrigin(accountNumber)
                .message("Account enabled")
                .build();
    }

    public TransactionDTO disableAccount(String accountNumber, UUID userId){
        isAccountOwner(accountNumber, userId);
        icesiAccountRepository.disableByAccountNumber(accountNumber);
        return TransactionDTO.builder()
                .accountNumberOrigin(accountNumber)
                .message(icesiAccountRepository.isActiveByAccountNumber(accountNumber)
                        ?"Account can't be disable, balance higher than 0":"Account disabled")
                .build();
    }
    public TransactionDTO withdrawal(TransactionDTO transactionDTO, UUID userId) {
        IcesiAccountResponseDTO icesiAccount = getAccountByAccountNumber(transactionDTO.getAccountNumberOrigin());
        isAccountOwner(icesiAccount.getAccountNumber(), userId);
        isAccountDisabled(icesiAccount.getAccountNumber());
        enoughMoney(icesiAccount.getBalance(), transactionDTO.getAmount());

        long newBalance = icesiAccount.getBalance() - transactionDTO.getAmount();

        icesiAccountRepository.updateBalance(newBalance, icesiAccount.getAccountNumber());
        transactionDTO.setAmount(newBalance);
        transactionDTO.setMessage("The withdrawal was successful");
        return transactionDTO;
    }
    public TransactionDTO deposit(TransactionDTO transactionDTO, UUID userId){
        IcesiAccountResponseDTO icesiAccount = getAccountByAccountNumber(transactionDTO.getAccountNumberOrigin());
        isAccountOwner(icesiAccount.getAccountNumber(), userId);
        isAccountDisabled(icesiAccount.getAccountNumber());

        long newBalance = icesiAccount.getBalance() + transactionDTO.getAmount();

        icesiAccountRepository.updateBalance(newBalance, icesiAccount.getAccountNumber());
        transactionDTO.setAmount(newBalance);
        transactionDTO.setMessage("The deposit was successful");
        return transactionDTO;
    }
    public TransactionDTO transfer(TransactionDTO transactionDTO, UUID userId){
        IcesiAccountResponseDTO icesiAccountOrigin = getAccountByAccountNumber(transactionDTO.getAccountNumberOrigin());
        isAccountOwner(icesiAccountOrigin.getAccountNumber(), userId);
        isAccountDisabled(icesiAccountOrigin.getAccountNumber());
        isValidAccountType(icesiAccountOrigin.getType());
        enoughMoney(icesiAccountOrigin.getBalance(), transactionDTO.getAmount());

        IcesiAccountResponseDTO icesiAccountDestiny = getAccountByAccountNumber(transactionDTO.getAccountNumberDestiny());
        isAccountDisabled(icesiAccountDestiny.getAccountNumber());
        isValidAccountType(icesiAccountDestiny.getType());

        long originNewBalance = icesiAccountOrigin.getBalance() - transactionDTO.getAmount();
        long destinyNewBalance = icesiAccountDestiny.getBalance() + transactionDTO.getAmount();

        icesiAccountRepository.updateBalance(originNewBalance, icesiAccountOrigin.getAccountNumber());
        icesiAccountRepository.updateBalance(destinyNewBalance, icesiAccountDestiny.getAccountNumber());
        transactionDTO.setAmount(originNewBalance);
        transactionDTO.setMessage("The transfer was successful");
        return transactionDTO;
    }
    private String generateAccountNumber(){
        IntStream intStream = new Random().ints(11,0,9);
        String rn = intStream
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
        String result = String.format("%s-%s-%s",
                rn.substring(0,3),
                rn.substring(3,9),
                rn.substring(9,11));
        if (icesiAccountRepository.existsByAccountNumber(result)){
            return generateAccountNumber();
        }else return result;
    }
    private void isAccountOwner(String accountNumber, UUID userId){
        if(!icesiAccountRepository.isIcesiAccountOwner(userId, accountNumber)){
            throw createIcesiException(
                    "Can't modified account",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();
        }
    }
    private void isAccountDisabled(String accountNumber){
        if(!icesiAccountRepository.isActiveByAccountNumber(accountNumber)){
            throw createIcesiException(
                    "The account "+accountNumber+" is disabled",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Active", "in account: "+accountNumber+", is disabled")
            ).get();
        }
    }
    private void enoughMoney(Long accountBalance, Long amount){
        if(amount > accountBalance){
            throw createIcesiException(
                    "Theres not enough money",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Balance", "has not enough money")
            ).get();
        }
    }
    private void isValidAccountType(AccountType accountType){
        if(Objects.equals(accountType.getType(), AccountType.DEPOSIT_ONLY.getType())){
            throw createIcesiException(
                    "Account is deposit only",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "AccountType","is deposit only")
            ).get();
        }
    }
}
