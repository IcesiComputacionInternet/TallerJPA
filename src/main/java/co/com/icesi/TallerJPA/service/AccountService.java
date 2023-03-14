package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.Enum.AccountType;
import co.com.icesi.TallerJPA.dto.AccountCreateDTO;
import co.com.icesi.TallerJPA.dto.response.AccountResponseDTO;
import co.com.icesi.TallerJPA.exception.ArgumentsException;
import co.com.icesi.TallerJPA.mapper.AccountMapper;
import co.com.icesi.TallerJPA.mapper.responseMapper.AccountResponseMapper;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.repository.AccountRepository;
import co.com.icesi.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final AccountResponseMapper accountResponseMapper;

    public AccountResponseDTO save(AccountCreateDTO account){
        String accountNumber = generateAccountNumber();
        if (account.getBalance()<0){
            throw new ArgumentsException("Balance can't be negative");
        }
        setAccountType(account);
        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        icesiAccount.setUser(userRepository.findUserByEmail(account.getUser()).orElseThrow(() -> new RuntimeException("User not found")));
        icesiAccount.setAccountNumber(accountNumber);
        icesiAccount.setAccountId(UUID.randomUUID());
        return accountResponseMapper.fromIcesiAccount(accountRepository.save(icesiAccount));

    }
    public String generateAccountNumber(){
        Random random = new Random();
        IntStream stream = random.ints(11,0, 10);
        String formattedStream = stream.mapToObj(String::valueOf).collect(Collectors.joining());
        String account = String.format("%s-%s-%s", formattedStream.substring(0, 3),
                formattedStream.substring(3, 9),
                formattedStream.substring(9, 11));

        boolean exist = accountRepository.findByAccountNumber(account);
        if (exist){
            return generateAccountNumber();
        }
        return account;
    }

    private void setAccountType(AccountCreateDTO account){
        if(account.getType().equals("deposit only")){
            account.setType(AccountType.DEPOSIT_ONLY.name());
        }
    }

    public IcesiAccount findAccountByAccountNumber(String accountNumber){
        return accountRepository.findAccount(accountNumber).orElseThrow(()-> new RuntimeException("Account not found"));
    }

    public String changeState(String accountNumber){
        IcesiAccount account = findAccountByAccountNumber(accountNumber);
        if (account.getBalance()==0){
            account.setActive(!account.isActive());
            accountRepository.save(account);
        }else{
            return "The account can't be deactivated because it has money";
        }
        return "Account state changed";
    }

    public String withdraw(String accountNumber, long amount){
        IcesiAccount account = findAccountByAccountNumber(accountNumber);
        if ( account.isActive() && account.getBalance()>=amount){
            account.setBalance(account.getBalance()-amount);
            accountRepository.save(account);
            return "Withdrawal successful";
        }else if (!account.isActive()){
            throw new ArgumentsException("Account must be active to withdraw money");

        }else{
            throw new ArgumentsException("Insufficient funds");
        }
    }

    public String deposit(String accountNumber, long amount){
        IcesiAccount account = findAccountByAccountNumber(accountNumber);
        if (account.isActive()){
            account.setBalance(account.getBalance()+amount);
            accountRepository.save(account);
        }else{
            throw new ArgumentsException("Account must be active to deposit money");
        }
        return "Deposit successful";
    }

    public String transferMoney(String accountNumberOrigin, String accountNumberDestination, long amount){
        IcesiAccount accountOrigin = findAccountByAccountNumber(accountNumberOrigin);
        IcesiAccount accountDestination = findAccountByAccountNumber(accountNumberDestination);

        if(accountOrigin.getType().equals(AccountType.DEPOSIT_ONLY.name()) || accountDestination.getType().equals(AccountType.DEPOSIT_ONLY.name())){
            throw new ArgumentsException("Accounts marked as deposit only can't transfer or be transferred money, only withdrawal and deposit");
        }
        if(!accountOrigin.isActive() || !accountDestination.isActive()){
            throw new ArgumentsException("Accounts must be active to transfer money");
        }


        if (accountOrigin.getBalance()>=amount){
            accountOrigin.setBalance(accountOrigin.getBalance()-amount);
            accountDestination.setBalance(accountDestination.getBalance()+amount);
            accountRepository.save(accountOrigin);
            accountRepository.save(accountDestination);
            return "Transfer successful";
        }else{
            throw new ArgumentsException("Insufficient funds");
        }
    }


}
