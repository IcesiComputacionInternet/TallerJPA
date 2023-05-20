package co.com.icesi.icesiAccountSystem.service;

import co.com.icesi.icesiAccountSystem.dto.RequestAccountDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseAccountDTO;
import co.com.icesi.icesiAccountSystem.dto.TransactionOperationDTO;
import co.com.icesi.icesiAccountSystem.mapper.AccountMapper;
import co.com.icesi.icesiAccountSystem.enums.AccountType;
import co.com.icesi.icesiAccountSystem.model.IcesiAccount;
import co.com.icesi.icesiAccountSystem.repository.AccountRepository;
import co.com.icesi.icesiAccountSystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    public ResponseAccountDTO saveAccount(RequestAccountDTO requestAccountDTO){
        var user = userRepository.findByEmail(requestAccountDTO.getUserEmail())
                .orElseThrow(() -> new RuntimeException("The email of the user was not specified or user does not exists yet, it is not possible to create an account without a user."));
        if(requestAccountDTO.getBalance()<0){
            throw new RuntimeException("Account's balance can not be below 0.");
        }
        AccountType accountType = getAccountType(requestAccountDTO);
        IcesiAccount icesiAccount = accountMapper.fromAccountDTO(requestAccountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setUser(user);
        icesiAccount.setAccountNumber(getAccountNumber());
        icesiAccount.setType(accountType);
        icesiAccount.setActive(true);
        accountRepository.save(icesiAccount);
        return accountMapper.fromAccountToResponseAccountDTO(icesiAccount);
    }

    private String getAccountNumber(){
        String accountNum = getRandomAccNumberSupplier().get();
        while(accountRepository.findByAccountNumber(accountNum).isPresent()){
            accountNum = getRandomAccNumberSupplier().get();
        }
        return accountNum;
    }

    private Supplier<String> getRandomAccNumberSupplier(){
        return () -> generateRandomAccountNumber();
    }

    private String generateRandomAccountNumber() {
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

    private AccountType getAccountType(RequestAccountDTO requestAccountDTO){
        AccountType type = AccountType.NORMAL;
        if (requestAccountDTO.getType().equals("")){
            throw new RuntimeException("Account's type can not be empty.");
        }
        if (!requestAccountDTO.getType().equalsIgnoreCase("deposit only") && !requestAccountDTO.getType().equalsIgnoreCase("normal")){
            throw new RuntimeException("Account's type has to be deposit only or normal.");
        }
        if(requestAccountDTO.getType().equalsIgnoreCase("deposit only") ){
            type= AccountType.DEPOSIT_ONLY;
        }
        return type;
    }

    private IcesiAccount validateAccountNumber(String accNum){
        return accountRepository.findByAccountNumber(accNum).
                orElseThrow(() -> new RuntimeException("There is not an account with the entered number."));
    }

    public ResponseAccountDTO enableAccount(String accountNumber){
        var account = validateAccountNumber(accountNumber);
        account.setActive(true);
        accountRepository.save(account);
        return accountMapper.fromAccountToResponseAccountDTO(account);
    }

    public ResponseAccountDTO disableAccount(String accountNumber){
        var account = validateAccountNumber(accountNumber);
        if(account.getBalance()>0){
            throw new RuntimeException("An account can only be disabled if the balance is 0.");
        }
        account.setActive(false);
        accountRepository.save(account);
        return accountMapper.fromAccountToResponseAccountDTO(account);
    }

    private void validateAccountBalance(IcesiAccount account, long amount) {
        if (amount>account.getBalance()) {
            throw new RuntimeException("Insufficient funds.");
        }
    }

    private void checkIfAnAccountIsActive(IcesiAccount account){
        if(!account.isActive()){
            throw new RuntimeException("The account to/from which you want to make a transaction is disabled.");
        }
    }

    public TransactionOperationDTO withdrawMoney(TransactionOperationDTO transaction){
        var account = validateAccountNumber(transaction.getAccountFrom());
        checkIfAnAccountIsActive(account);
        validateAccountBalance(account, transaction.getAmount());
        account.setBalance(account.getBalance() - transaction.getAmount());
        accountRepository.save(account);
        transaction.setResult("The withdrawal of money from the account was successful");
        return transaction;
    }

    public TransactionOperationDTO depositMoney(TransactionOperationDTO transaction){
        var account = validateAccountNumber(transaction.getAccountTo());
        checkIfAnAccountIsActive(account);
        account.setBalance(account.getBalance() + transaction.getAmount());
        accountRepository.save(account);
        transaction.setResult("The deposit of money to the account was successful.");
        return transaction;
    }

    public TransactionOperationDTO transferMoney(TransactionOperationDTO transaction){
        var accountFrom = validateAccountNumber(transaction.getAccountFrom());
        var accountTo = validateAccountNumber(transaction.getAccountTo());
        validateAccountBalance(accountFrom, transaction.getAmount());
        checkIfAnAccountIsActive(accountFrom);
        checkIfAnAccountIsActive(accountTo);
        if (accountFrom.getType()==AccountType.DEPOSIT_ONLY || accountTo.getType()==AccountType.DEPOSIT_ONLY){
            throw new RuntimeException("The source or destination account is marked as deposit only, it can't transfer or be transferred money, only withdrawal and deposit.");
        }
        accountTo.setBalance(accountTo.getBalance() + transaction.getAmount());
        accountFrom.setBalance(accountFrom.getBalance() - transaction.getAmount());
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        transaction.setResult("The transfer of money was successful.");
        return transaction;
    }

    public ResponseAccountDTO getAccount(String accountNumber) {
        return accountMapper.fromAccountToResponseAccountDTO(validateAccountNumber(accountNumber));
    }

    public List<ResponseAccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(accountMapper::fromAccountToResponseAccountDTO).collect(Collectors.toList());
    }
}
