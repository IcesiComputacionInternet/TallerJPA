package co.com.icesi.icesiAccountSystem.service;

import co.com.icesi.icesiAccountSystem.dto.AccountDTO;
import co.com.icesi.icesiAccountSystem.mapper.AccountMapper;
import co.com.icesi.icesiAccountSystem.model.AccountType;
import co.com.icesi.icesiAccountSystem.model.IcesiAccount;
import co.com.icesi.icesiAccountSystem.repository.AccountRepository;
import co.com.icesi.icesiAccountSystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    public IcesiAccount saveAccount(AccountDTO accountDTO){
        boolean validAccNum = false;

        if(accountDTO.getBalance()<0){
            throw new RuntimeException("Account's balance can't be below 0.");
        }
        if(accountDTO.isActive()==false){
            throw new RuntimeException("The status of a new account cannot be disabled.");
        }

        do{
            if (accountRepository.findByAccountNumber(randomNumAccSupplier().get()).isPresent()==false){
                accountDTO.setAccountNumber(randomNumAccSupplier().get());
                validAccNum=true;
            }
        }while(validAccNum==false);

        assignAccountType(accountDTO);
        IcesiAccount icesiAccount = accountMapper.fromAccountDTO(accountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        assignUser(accountDTO, icesiAccount);

        return accountRepository.save(icesiAccount);
    }

    private void assignUser(AccountDTO accountDTO, IcesiAccount account) {
        if (accountDTO.getUserEmail().equals("")){
            throw new RuntimeException("It is not possible to create an account without user.");
        }
        if(userRepository.findByEmail(accountDTO.getUserEmail()).isPresent()){
            account.setUser(userRepository.findByEmail(accountDTO.getUserEmail()).get());
        } else{
            throw new RuntimeException("User does not exists.");
        }
    }

    private Supplier<String> randomNumAccSupplier(){
        return () -> generateRandomAccNum();
    }
    private String generateRandomAccNum() {
        String numbers = "0123456789";
        StringBuilder account = new StringBuilder(13);
        for (int i = 0; i < 13; i++) {
            int index = (int)(numbers.length() * Math.random());
            if (i==3 || i==10){
                account.append('-');
            }else {
                account.append(numbers.charAt(index));
            }
        }
        return account.toString();
    }

    private void assignAccountType(AccountDTO accountDTO){
        if(accountDTO.getType().equalsIgnoreCase("deposit only")){
            accountDTO.setType(AccountType.DEPOSIT_ONLY.name());
        } else if (accountDTO.getType().equalsIgnoreCase("normal")) {
            accountDTO.setType(AccountType.NORMAL.name());
        }
    }

    public String manageAccountState(String accountNumber, boolean newState){
        String message = "";
        boolean accountExists = accountRepository.findByAccountNumber(accountNumber).isPresent();
        if(accountExists){
            IcesiAccount account = accountRepository.findByAccountNumber(accountNumber).get();
            if (newState==false){
                if (account.isActive() && account.getBalance()==0){
                    account.setActive(newState);
                    message = "The account was disabled.";
                }else if(account.getBalance()!=0){
                    throw new RuntimeException("An account can only be disabled if the balance is 0.");
                }
            }else{
                account.setActive(newState);
                message = "The account was enabled.";
            }
            accountRepository.save(account);
        }else {
            throw new RuntimeException("There isn't an account with the entered number.");
        }
        return message;
    }

    public String withdrawalMoney(String accountNumber, long amount){
        String message = "";
        boolean accountExists = accountRepository.findByAccountNumber(accountNumber).isPresent();
        if(accountExists){
            IcesiAccount account = accountRepository.findByAccountNumber(accountNumber).get();
            if(account.isActive() && account.getBalance()>=amount){
                account.setBalance(account.getBalance()-amount);
                message="The withdrawal of money from the account was successful";
            } else if (account.isActive()==false) {
                throw new RuntimeException("The account from which you want to withdraw money is disabled.");
            } else if (account.getBalance()<amount) {
                throw new RuntimeException("Insufficient funds.");
            }
            accountRepository.save(account);
        }else {
            throw new RuntimeException("There isn't an account with the entered number.");
        }
        return message;
    }
    public String depositMoney(String accountNumber, long amount){
        String message = "";
        boolean accountExists = accountRepository.findByAccountNumber(accountNumber).isPresent();
        if(accountExists){
            IcesiAccount account = accountRepository.findByAccountNumber(accountNumber).get();
            if(account.isActive()){
                account.setBalance(account.getBalance()+amount);
                message="The deposit of money to the account was successful";
            } else{
                throw new RuntimeException("The account to which you want to deposit money is disabled.");
            }
            accountRepository.save(account);
        }else {
            throw new RuntimeException("There isn't an account with the entered number.");
        }
        return message;
    }
    public String transferMoney(String sourceAccountNumber, String DestinationAccountNumber, long amount){
        String message = "";
        boolean sourceAccExists = accountRepository.findByAccountNumber(sourceAccountNumber).isPresent();
        boolean destinationAccExists = accountRepository.findByAccountNumber(DestinationAccountNumber).isPresent();
        if(sourceAccExists && destinationAccExists){
            IcesiAccount sourceAcc = accountRepository.findByAccountNumber(sourceAccountNumber).get();
            IcesiAccount destinationAcc = accountRepository.findByAccountNumber(DestinationAccountNumber).get();
            boolean srcNormalTypeAcc=sourceAcc.getType().equals(AccountType.DEPOSIT_ONLY.name());
            boolean destNormalTypeAcc=destinationAcc.getType().equals(AccountType.DEPOSIT_ONLY.name());
            if(sourceAcc.getBalance()>=amount && sourceAcc.isActive() && destinationAcc.isActive() && !srcNormalTypeAcc && !destNormalTypeAcc){
                destinationAcc.setBalance(destinationAcc.getBalance()+amount);
                sourceAcc.setBalance(sourceAcc.getBalance()-amount);
                message="The transfer of money was successful";
            } else if (sourceAcc.getBalance()<amount) {
                throw new RuntimeException("Insufficient funds.");
            } else if (sourceAcc.isActive()==false || destinationAcc.isActive()==false){
                throw new RuntimeException("The source or destination account is disabled.");
            } else if (srcNormalTypeAcc==true || destNormalTypeAcc==true) {
                throw new RuntimeException("The source or destination account is marked as deposit only, it can't transfer or be transferred money, only withdrawal and deposit.");
            }
            accountRepository.save(sourceAcc);
            accountRepository.save(destinationAcc);
        }else {
            throw new RuntimeException("There isn't an account with the entered number.");
        }
        return message;
    }



}
