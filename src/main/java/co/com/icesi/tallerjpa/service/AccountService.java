package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.Enum.AccountType;
import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.model.IcesiAccount;
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

    public ResponseAccountDTO save(RequestAccountDTO accountDTO){
        var checkUser = userRepository.findByEmail(accountDTO.getUser()).
                orElseThrow(() -> new RuntimeException("There is no user for the account"));

        checkBalanceOverZero(accountDTO);

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(accountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(checkNumGenerated(accountNumbersGenerated()));
        icesiAccount.setActive(true);
        icesiAccount.setUser(checkUser);
        accountRepository.save(icesiAccount);
        return accountMapper.fromIcesiAccountToResUserDTO(icesiAccount);
    }

    public IcesiAccount checkAccountNumber(String accNumber){
        return accountRepository.findByAccountNumber(accNumber)
                .orElseThrow(() -> new RuntimeException("Account number not found"));
    }

    public void checkBalanceOverZero(RequestAccountDTO accountDTO){
        if(accountDTO.getBalance() < 0){
            throw new RuntimeException("Account balance can't be below 0");
        }
    }

    public void checkBalance(IcesiAccount account, long amount){
        if(account.getBalance() < amount) {
            throw new RuntimeException("The amount of money in Balance is less than the amount needed");
        }

    }
    public void checkAccountType(IcesiAccount account){
        if(account.getType().equals(AccountType.DEPOSIT_ONLY.toString())){
            throw new RuntimeException("The account's type is DEPOSIT_ONLY");
        }
    }
    public String checkNumGenerated(String accNum){
        //Optional<IcesiAccount> checkNumGenerated = accountRepository.findByAccountNumber(accNum);
        //String accountNumber = accountNumbersGenerated();;

        /*do{
            accountNumber = accountNumbersGenerated();
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());

        return accountNumber;*/

        if(accountRepository.findByAccountNumber(accNum).isPresent()){
            return checkNumGenerated(accountNumbersGenerated());
        }
        return accNum;
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
    // A function in AccountService to enable the account.
    public ResponseAccountDTO enableAccount(String accNumber){
        var account = accountRepository.findByAccountNumber(accNumber).orElseThrow(()->new RuntimeException("The account does not exist"));

        if (!account.isActive()) {
            account.setActive(true);
            accountRepository.save(account);
            return accountMapper.fromIcesiAccountToResUserDTO(account);
        }
        throw new RuntimeException("Account can't be enabled");
    }

    // A function in AccountService to disable the account.
    public ResponseAccountDTO disableAccount(String accNumber){
        var account = accountRepository.findByAccountNumber(accNumber).orElseThrow(()->new RuntimeException("The account does not exist"));

        if (account.getBalance() == 0) {
            account.setActive(false);
            accountRepository.save(account);
            return accountMapper.fromIcesiAccountToResUserDTO(account);
        }
        throw new RuntimeException("Account can't be disabled");
    }

    // A function in AccountService to withdrawal money.
    public TransactionDTO withdrawal(TransactionDTO transactionDTO) {
        IcesiAccount account = checkAccountNumber(transactionDTO.getAccountFrom());
        if(account.isActive()){
            checkBalance(account, transactionDTO.getAmount());
            account.setBalance(account.getBalance() - transactionDTO.getAmount());
            transactionDTO.setResult("Successful withdrawal");
            accountRepository.save(account);
            return transactionDTO;
        }
        throw new RuntimeException("Unsuccessful withdrawal");
    }

    // A function in AccountService to deposit money.
    public TransactionDTO deposit(TransactionDTO transactionDTO) {
        IcesiAccount account = checkAccountNumber(transactionDTO.getAccountFrom());
        if(account.isActive()){
            account.setBalance(account.getBalance() + transactionDTO.getAmount());
            transactionDTO.setResult("Successful deposit");
            accountRepository.save(account);
            return transactionDTO;
        }
        throw new RuntimeException("Unsuccessful deposit");
    }

    // A function in AccountService to transfer money to another account.
    public TransactionDTO transfer(TransactionDTO transactionDTO) {
        IcesiAccount accountFrom = checkAccountNumber(transactionDTO.getAccountFrom());
        IcesiAccount accountTo = checkAccountNumber(transactionDTO.getAccountTo());

        if(accountFrom.isActive() & accountTo.isActive()){
            checkAccountType(accountFrom);
            checkAccountType(accountTo);
            checkBalance(accountFrom,transactionDTO.getAmount());

            accountFrom.setBalance(accountFrom.getBalance() - transactionDTO.getAmount());
            accountTo.setBalance(transactionDTO.getAmount() + accountTo.getBalance());

            transactionDTO.setResult("Successful transfer");

            accountRepository.save(accountFrom);
            accountRepository.save(accountTo);
            return transactionDTO;
        }
        throw new RuntimeException("Unsuccessful transfer");
    }
}
