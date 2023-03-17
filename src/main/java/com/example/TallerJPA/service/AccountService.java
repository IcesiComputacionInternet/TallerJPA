package com.example.TallerJPA.service;

import com.example.TallerJPA.dto.AccountCreateDTO;
import com.example.TallerJPA.mapper.AccountMapper;
import com.example.TallerJPA.model.IcesiAccount;
import com.example.TallerJPA.model.IcesiUser;
import com.example.TallerJPA.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserService userService;

    public IcesiAccount save(AccountCreateDTO accountCreateDTO) {
        String uuid = generateAccountNumber();
        Optional<IcesiUser> userFound = userService.findUserByEmail(accountCreateDTO.getUserEmail());
        if (userFound.isPresent()) {
            IcesiAccount icesiAccount = accountMapper.fromAccountCreateDTO(accountCreateDTO);
            icesiAccount.setAccountNumber(uuid);
            icesiAccount.setAccountId(UUID.randomUUID());
            icesiAccount.setUser(userFound.get());
            icesiAccount.setBalance(0);
            icesiAccount.setActive(true);
            return accountRepository.save(icesiAccount);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public IcesiAccount changeStatus(IcesiAccount account){
        if(account.getBalance() != 0){
            throw new RuntimeException("Account can't be closed because it has reamining balance");
        }else{
            account.setActive(false);
        }
        return accountRepository.save(account);
    }

    public void withdraw(IcesiAccount account, long amount){
        if(account.getBalance() < amount){
            throw new RuntimeException("Account doesn't have enough balance");
        }else{
            account.setBalance(account.getBalance() - amount);
        }
        accountRepository.save(account);
    }

    public void deposit(IcesiAccount account, long amount){
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    public void transfer(IcesiAccount account, IcesiAccount account2, long amount){
        if(account.getBalance() < amount){
            throw new RuntimeException("Account doesn't have enough balance");
        } else if (account.getType().equals("Deposito") || account2.getType().equals("Deposito")) {
            throw new RuntimeException("Account of type deposit can't transfer money");
        } else{
            account.setBalance(account.getBalance() - amount);
            account2.setBalance(account2.getBalance() + amount);
        }
        accountRepository.save(account);
        accountRepository.save(account2);
    }

    private String generateAccountNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
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
        return sb.toString();
    }
}
