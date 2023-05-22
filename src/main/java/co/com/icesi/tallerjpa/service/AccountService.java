package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.Enum.AccountType;
import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import co.com.icesi.tallerjpa.error.util.IcesiExceptionBuilder;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.model.IcesiAccount;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import co.com.icesi.tallerjpa.security.IcesiSecurityContext;
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
    private final IcesiExceptionBuilder exceptionBuilder = new IcesiExceptionBuilder();

    public ResponseAccountDTO save(RequestAccountDTO accountDTO){


        var checkUser = userRepository.findByEmail(accountDTO.getUser())
                .orElseThrow(()-> exceptionBuilder.notFoundException("There is no user for the account", "User", "email", accountDTO.getUser()));

        //checkUpdateAccountPermission(checkUser.getUserId().toString());

        checkCreateAccountPermission(accountDTO.getUser());

        checkBalanceOverZero(accountDTO);

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(accountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(checkNumGenerated(accountNumbersGenerated()));
        icesiAccount.setActive(true);
        icesiAccount.setUser(checkUser);
        accountRepository.save(icesiAccount);
        return accountMapper.fromIcesiAccountToResUserDTO(icesiAccount);
    }

    public void transactionPermission(String user) {
        String emailToken = IcesiSecurityContext.getCurrentEmail();

        if (!emailToken.equalsIgnoreCase(user)) {
            throw exceptionBuilder.forbiddenException("You can't do a transaction");
        }
    }

    public void checkCreateAccountPermission(String userEmail) {
        String emailToken = IcesiSecurityContext.getCurrentEmail();
        String roleToken = IcesiSecurityContext.getCurrentRole();

        if(roleToken.equals("USER") && !emailToken.equalsIgnoreCase(userEmail) || roleToken.equals("BANK")) {
            throw exceptionBuilder.forbiddenException("You can't create the account");
        }
    }

    public IcesiAccount checkAccountNumber(String accNumber){
        return accountRepository.findByAccountNumber(accNumber)
                .orElseThrow(()-> exceptionBuilder.notFoundException("Account number not found", "Account", "number", accNumber));
    }

    public void checkBalanceOverZero(RequestAccountDTO accountDTO){
        if(accountDTO.getBalance() < 0){
            throw exceptionBuilder.generalException("Account balance can't be below 0", "Balance");
        }
    }

    public void checkBalance(IcesiAccount account, long amount){
        if(account.getBalance() < amount) {
            throw exceptionBuilder.generalException("The amount of money in Balance is less than the amount needed", "Balance");
        }

    }
    public void checkAccountType(IcesiAccount account){
        if(account.getType().equals(AccountType.DEPOSIT_ONLY.toString())){
            throw exceptionBuilder.generalException("The account's type is DEPOSIT_ONLY", "Account");
        }
    }
    public String checkNumGenerated(String accNum){
        //Optional<IcesiAccount> checkNumGenerated = accountRepository.findByAccountNumber(accNum);
        //String accountNumber = accountNumbersGenerated();;

        do{
            accNum = accountNumbersGenerated();
            } while (accountRepository.findByAccountNumber(accNum).isPresent());

            return accNum;


           /* if(accountRepository.findByAccountNumber(accNum).isPresent()){
                return checkNumGenerated(accountNumbersGenerated());
            }
            return accNum;*/
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
        var account = accountRepository.findByAccountNumber(accNumber).orElseThrow(()-> exceptionBuilder.notFoundException("The account does not exist", "Account", "number", accNumber));

        if (!account.isActive()) {
            account.setActive(true);
            accountRepository.save(account);
            return accountMapper.fromIcesiAccountToResUserDTO(account);
        }
        throw exceptionBuilder.generalException("Account can't be enabled", "Account");
    }

    // A function in AccountService to disable the account.
    public ResponseAccountDTO disableAccount(String accNumber){
        var account = accountRepository.findByAccountNumber(accNumber).orElseThrow(()-> exceptionBuilder.notFoundException("The account does not exist", "Account", "number", accNumber));

        if (account.getBalance() == 0) {
            account.setActive(false);
            accountRepository.save(account);
            return accountMapper.fromIcesiAccountToResUserDTO(account);
        }
        throw exceptionBuilder.generalException("Account can't be disabled", "Account");
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
        throw exceptionBuilder.generalException("Unsuccessful withdrawal", "Account");
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
        throw exceptionBuilder.generalException("Unsuccessful deposit", "Account");
    }

    // A function in AccountService to transfer money to another account.
    public TransactionDTO transfer(TransactionDTO transactionDTO) {

        IcesiAccount accountFrom = checkAccountNumber(transactionDTO.getAccountFrom());
        IcesiAccount accountTo = checkAccountNumber(transactionDTO.getAccountTo());
        transactionPermission(accountFrom.getUser().getEmail());


        if(accountFrom.isActive() && accountTo.isActive()){
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
        throw exceptionBuilder.generalException("Transaction","Unsuccessful transfer");
    }
}
