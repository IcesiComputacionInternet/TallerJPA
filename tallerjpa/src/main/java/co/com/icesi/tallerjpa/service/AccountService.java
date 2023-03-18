package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.Enum.AccountType;
import co.com.icesi.tallerjpa.dto.AccountCreateDTO;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.model.IcesiAccount;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
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
    private final UserRepository userRepository;

    public IcesiAccount save(AccountCreateDTO account){
        String accountNumber = accountNumbersGenerated();

        if(account.getBalance() < 0){
            throw new RuntimeException("Account balance can't be below 0");
        }
        checkUser(account);
        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        IcesiUser icesiUser = userRepository.findByEmail(account.getIcesiUserDto().getEmail()).orElseThrow(()-> new RuntimeException("User is not created"));
        icesiAccount.setAccountNumber(accountNumber);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setUser(icesiUser);
        return accountRepository.save(icesiAccount);
    }

    public void checkUser(AccountCreateDTO account){
        if(account.getIcesiUserDto() == null){
            throw new RuntimeException("There is no user for the account");
        }
    }

    public String accountNumbersGenerated(){
        StringBuilder accountNumbers = new StringBuilder();
        int[] stringSizes = {3, 6, 2};
        for (int i = 0; i < stringSizes.length; i++) {
            int numLength = stringSizes[i];
            accountNumbers.append(numberGenerator(numLength));
            if (i < stringSizes.length - 1) {
                accountNumbers.append("-");
            }
        }
        System.out.print(accountNumbers);
        return accountNumbers.toString();
    }
    public String numberGenerator(int length) {
        Random random = new Random();
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < length; i++) {
            num.append(random.nextInt(10));
        }
        return num.toString();
    }

    // Requirements
    // A function in AccountService to enable/disable the account.
    public String accountState(String accountNumber) {
        IcesiAccount account = accountRepository.findByAccountNumber(accountNumber).get();
        if (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            if(accountRepository.findByAccountNumber(accountNumber).get().getBalance() == 0 && accountRepository.findByAccountNumber(accountNumber).get().isActive()){
                accountRepository.findByAccountNumber(accountNumber).get().setActive(false);
                return ("Account has been disabled");
            } else if(!accountRepository.findByAccountNumber(accountNumber).get().isActive()) {
                accountRepository.findByAccountNumber(accountNumber).get().setActive(true);
                return ("Account has been enabled");
            }
            accountRepository.save(account);
        }
        throw new RuntimeException("State can't be changed");
    }

    // A function in AccountService to withdrawal money.
    public String withdrawal(String accountNumber, long withdrawalMoney) {
        IcesiAccount account = accountRepository.findByAccountNumber(accountNumber).get();
        if (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            if(accountRepository.findByAccountNumber(accountNumber).get().getBalance() >= withdrawalMoney && accountRepository.findByAccountNumber(accountNumber).get().isActive()){
                accountRepository.findByAccountNumber(accountNumber).get().setBalance(accountRepository.findByAccountNumber(accountNumber).get().getBalance() - withdrawalMoney);
                accountRepository.save(account);
                return ("Successful withdrawal");
            } else if(accountRepository.findByAccountNumber(accountNumber).get().getBalance() == 0){
                throw new RuntimeException("Not enough balance");
            } else if(!accountRepository.findByAccountNumber(accountNumber).get().isActive()){
                throw new RuntimeException("Account is not active");
            }
        }
        throw new RuntimeException("Can't withdrawal");
    }

    // A function in AccountService to deposit money.
    public String deposit(String accountNumber, long depositMoney) {
        IcesiAccount account = accountRepository.findByAccountNumber(accountNumber).get();
        if (accountRepository.findByAccountNumber(accountNumber).isPresent()){
            if(accountRepository.findByAccountNumber(accountNumber).get().isActive()){
                accountRepository.findByAccountNumber(accountNumber).get().setBalance(accountRepository.findByAccountNumber(accountNumber).get().getBalance() + depositMoney);
                accountRepository.save(account);
                return ("Successful deposit");
            }
        }
        return ("Unsuccessful deposit");
    }

    // A function in AccountService to transfer money to another account.
    public String transfer(String senderAccount, String receiverAccount, long moneyTransfer) {
        IcesiAccount accountFrom = accountRepository.findByAccountNumber(senderAccount).get();
        IcesiAccount accountTo = accountRepository.findByAccountNumber(receiverAccount).get();
        if (accountRepository.findByAccountNumber(senderAccount).isPresent() && accountRepository.findByAccountNumber(receiverAccount).isPresent()){
            if (accountFrom.isActive() && accountTo.isActive()){
                if(accountFrom.getType().equals(AccountType.DEPOSIT_ONLY.name()) || accountTo.getType().equals(AccountType.DEPOSIT_ONLY.name())){
                    throw new RuntimeException("Account is Deposit type");
                }
                if(accountFrom.getBalance() >= moneyTransfer){
                    accountRepository.findByAccountNumber(senderAccount).get().setBalance(accountRepository.findByAccountNumber(senderAccount).get().getBalance() - moneyTransfer);
                    accountRepository.findByAccountNumber(receiverAccount).get().setBalance(accountRepository.findByAccountNumber(receiverAccount).get().getBalance() + moneyTransfer);
                    return ("Successful transfer");
                }
            }
        }
        return ("Unsuccessful transfer");
    }
}
