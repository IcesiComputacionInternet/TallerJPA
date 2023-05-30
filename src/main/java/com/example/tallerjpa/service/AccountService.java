package com.example.tallerjpa.service;

import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.dto.AccountResponseDTO;
import com.example.tallerjpa.dto.TransactionRequestDTO;
import com.example.tallerjpa.dto.TransactionResponseDTO;
import com.example.tallerjpa.error.exception.CustomException;
import com.example.tallerjpa.mapper.AccountMapper;
import com.example.tallerjpa.enums.AccountType;
import com.example.tallerjpa.model.IcesiAccount;
import com.example.tallerjpa.repository.AccountRepository;
import com.example.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;


    public IcesiAccount createAccount(AccountDTO accountDTO){
        if(accountDTO.getBalance() <= 0 ){throw new CustomException("Balance can't be below 0");}
        IcesiAccount icesiAccount = accountMapper.fromAccountDTO(accountDTO);
        icesiAccount.setIcesiUser(userRepository.searchByEmail(accountDTO.getEmailUser()).orElseThrow(()-> new RuntimeException("User doesn't exists")));
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(generateAccountNumber());
        setTypeAccount(accountDTO.getType().getValue(), icesiAccount);
        return accountRepository.save(icesiAccount);

    }

    public void setTypeAccount(String type, IcesiAccount icesiAccount){
        try {
            icesiAccount.setType(AccountType.valueOf(type.toUpperCase()));
            System.out.println(icesiAccount.getType());
        } catch (IllegalArgumentException e){
            throw new CustomException("Account type does not exist");
        }
    }

    public String generateAccountNumber() {
        String accountNumber;
        do {
            Random rand = new Random();
            int firstSet = rand.nextInt(1000);
            int secondSet = rand.nextInt(1000000);
            int thirdSet = rand.nextInt(100);
            accountNumber = String.format("%03d-%06d-%02d", firstSet, secondSet, thirdSet);
        }while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    @Transactional
    public String activateAccount(String accountNumber){
        IcesiAccount account = getAccount(accountNumber);
        if(account.isActive()){
            throw new CustomException("This account was enabled already");
        }
        account.setActive(true);
        accountRepository.save(account);
        return "The account was enabled";
    }

    @Transactional
    public String deactivateAccount(String accountNumber){
        IcesiAccount account = getAccount(accountNumber);
        if(account.getBalance()>0){
            throw new CustomException("The account can not be disabled");
        }
        account.setActive(false);
        accountRepository.save(account);
        return "The account was disabled";
    }

    @Transactional
    public TransactionResponseDTO withdrawMoney(TransactionRequestDTO transactionRequestDTO){
        IcesiAccount account = getAccount(transactionRequestDTO.getOriginAccountNumber());
        boolean active = account.isActive();
        long balance = account.getBalance();
        if(active) {
            if (balance<transactionRequestDTO.getAmount()){
                throw new CustomException("The account doesn't have enough money");
            }else {
                    account.setBalance(balance-transactionRequestDTO.getAmount());
                    accountRepository.save(account);
                    return TransactionResponseDTO.builder()
                            .oldBalance("Previous balance: "+balance)
                            .newBalance("New balance: "+account.getBalance())
                            .result("The withdrawal was done successfully")
                            .build();
                }
        }else {
            throw new CustomException("This account is not active, so can't be done transactions");
        }
    }

    @Transactional
    public TransactionResponseDTO depositMoney(TransactionRequestDTO transactionRequestDTO){
        IcesiAccount account = getAccount(transactionRequestDTO.getOriginAccountNumber());
        long balance = account.getBalance();
        boolean active = account.isActive();
        if(active){
            if(transactionRequestDTO.getAmount()>0){
                account.setBalance(balance+transactionRequestDTO.getAmount());
                accountRepository.save(account);
                return TransactionResponseDTO.builder()
                        .oldBalance("Previous balance: "+balance)
                        .newBalance("New balance: "+account.getBalance())
                        .result("The withdrawal was done successfully")
                        .build();
            } else {
                throw new CustomException("You can't use negative amounts, please verify");
            }
        }else {
            throw new CustomException("This account is not active, so can't be done transactions");
        }

    }

    @Transactional
    public TransactionResponseDTO transferMoney(TransactionRequestDTO transactionRequestDTO){
        IcesiAccount originAccount = getAccount(transactionRequestDTO.getOriginAccountNumber());
        IcesiAccount destinationAccount = getAccount(transactionRequestDTO.getDestinationAccountNumber());
        if(!originAccount.isActive()) {
            throw new CustomException("The origin account is not active");
        } else if (!destinationAccount.isActive()) {
            throw new CustomException("The destination account is not active");
        }
        String typeOrigin = String.valueOf(originAccount.getType());
        String typeDestination = String.valueOf(destinationAccount.getType());
        if(typeOrigin.equals("DEPOSIT")){
            throw new CustomException("The origin account is only for deposits, so can't transfer");
        } else if (typeDestination.equals("DEPOSIT")) {
            throw new CustomException("The destination account is only for deposits, so can't transfer");
        } else {
            long oldBalanceOriginAccount = originAccount.getBalance();
            long oldBalanceDestinationAccount = destinationAccount.getBalance();
            if(oldBalanceOriginAccount<transactionRequestDTO.getAmount()){
                throw new CustomException("The account doesn't have enough money for do this transfer");
            }else {
                originAccount.setBalance(oldBalanceOriginAccount-transactionRequestDTO.getAmount());
                destinationAccount.setBalance(oldBalanceDestinationAccount+transactionRequestDTO.getAmount());
                accountRepository.save(originAccount);
                accountRepository.save(destinationAccount);
                return TransactionResponseDTO.builder()
                        .oldBalance("Previous balance of origin account: "+originAccount.getAccountNumber()+"was: "+oldBalanceOriginAccount+"\n"+
                                    "Previous balance of destination account: "+destinationAccount.getAccountNumber()+"was: "+oldBalanceDestinationAccount+"\n")
                        .newBalance("New balance of origin account: "+originAccount.getBalance()+"\n"+
                                    "New balance of destination account: "+destinationAccount.getBalance()+"\n")
                        .result("The transfer was done successfully")
                        .build();
            }
        }
    }

    public IcesiAccount getAccount(String numberAccount){
        return accountRepository.getAccount(numberAccount)
                .orElseThrow(() -> new CustomException("The account was not found"));
    }

    @Transactional
    public List<AccountResponseDTO> getAllAccountsByUser(UUID userId){
        return accountRepository.getAllAccounts(userId)
                .stream()
                .map(accountMapper::fromAccountToResponseDTO)
                .collect(Collectors.toList());
    }

}
