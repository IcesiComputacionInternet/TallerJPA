package com.example.TallerJPA.service;

import com.example.TallerJPA.dto.*;
import com.example.TallerJPA.mapper.AccountMapper;
import com.example.TallerJPA.model.IcesiAccount;
import com.example.TallerJPA.model.IcesiUser;
import com.example.TallerJPA.repository.AccountRepository;
import com.example.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    public AccountResponseDTO save(AccountCreateDTO accountCreateDTO) {
        Optional<IcesiUser> userFound = userRepository.findByEmail(accountCreateDTO.getUserEmail());
        if (userFound.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        String uuid = generateAccountNumber();
        IcesiAccount icesiAccount = accountMapper.fromAccountCreateDTO(accountCreateDTO);
        icesiAccount.setAccountNumber(uuid);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setUser(userFound.get());
        icesiAccount.setBalance(0);
        icesiAccount.setActive(true);
        accountRepository.save(icesiAccount);
        return accountMapper.fromAccountToResponse(icesiAccount);
    }

    @Transactional
    public AccountResponseDTO disableAccount(String accountNumber) {
        IcesiAccount account = getAccountByNumber(accountNumber);
        account.setActive(false);
        accountRepository.save(account);
        return accountMapper.fromAccountToResponse(account);
    }

    @Transactional
    public AccountResponseDTO enableAccount(String accountNumber) {
        IcesiAccount account = getAccountByNumber(accountNumber);
        account.setActive(true);
        accountRepository.save(account);
        return accountMapper.fromAccountToResponse(account);
    }
    @Transactional
    public AccountResponseDTO withdraw(TransactionAccountDTO transactionInformation){
        IcesiAccount account = getAccountByNumber(transactionInformation.getAccountNumber());
        if(account.getBalance() < transactionInformation.getAmount()) {
            throw new RuntimeException("Account doesn't have enough balance");
        }
        account.setBalance(account.getBalance() - transactionInformation.getAmount());
        accountRepository.save(account);
        return accountMapper.fromAccountToResponse(account);
    }
    @Transactional
    public AccountResponseDTO deposit(TransactionAccountDTO transactionInformation){
        IcesiAccount account = getAccountByNumber(transactionInformation.getAccountNumber());
        account.setBalance(account.getBalance() + transactionInformation.getAmount());
        accountRepository.save(account);
        return accountMapper.fromAccountToResponse(account);
    }
    @Transactional
    public TransferResponseDTO transfer(TransferRequestDTO requestDTO){
        IcesiAccount originAccount = getTransferableAccountByNumber(requestDTO.getOriginAccountNumber());
        IcesiAccount destinationAccount = getTransferableAccountByNumber(requestDTO.getDestinationAccountNumber());
        if(originAccount.getBalance() < requestDTO.getAmount()){
            throw new RuntimeException("Origin account doesn't have enough balance");
        }
        originAccount.setBalance(originAccount.getBalance() - requestDTO.getAmount());
        destinationAccount.setBalance(destinationAccount.getBalance() + requestDTO.getAmount());
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);
        return accountMapper.fromRequestToResponse(originAccount, destinationAccount, requestDTO.getAmount(), originAccount.getBalance());
    }
    private String generateAccountNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        boolean unique = false;
        while (!unique){
            for (int i = 0; i < 3; i++) {
                int num = random.nextInt(10);
                sb.append(num);
            }
            sb.append("-");
            for (int i = 0; i < 6; i++) {
                int num = random.nextInt(10);
                sb.append(num);
            }
            sb.append("-");
            for (int i = 0; i < 2; i++) {
                int num = random.nextInt(10);
                sb.append(num);
            }
            Optional<IcesiAccount> accountFound = accountRepository.findByAccountNumber(sb.toString());
            if(accountFound.isEmpty() && checkFormat(sb.toString())){
                unique = true;
            }

        }
        return sb.toString();
    }
    private boolean checkFormat(String accountNumber){
        String[] parts = accountNumber.split("-");
        if(parts.length != 3){
            return false;
        }
        if(parts[0].length() != 3 || parts[1].length() != 6 || parts[2].length() != 2){
            return false;
        }
        return true;
    }
    private IcesiAccount getTransferableAccountByNumber(String accountNumber){
        Optional<IcesiAccount> accountFound = accountRepository.findByAccountNumber(accountNumber);
        if(accountFound.isEmpty()){
            throw new RuntimeException("Account not found");
        }
        if(!accountFound.get().getActive()){
            throw new RuntimeException("Account is not active");
        }
        if(accountFound.get().getType().equals("Deposito")){
            throw new RuntimeException("Account of type deposit can't transfer money");
        }
        return accountFound.get();
    }
    private IcesiAccount getAccountByNumber(String accountNumber){
        Optional<IcesiAccount> accountFound = accountRepository.findByAccountNumber(accountNumber);
        return accountFound.orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
