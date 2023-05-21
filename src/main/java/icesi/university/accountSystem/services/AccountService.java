package icesi.university.accountSystem.services;

import icesi.university.accountSystem.dto.RequestAccountDTO;
import icesi.university.accountSystem.dto.ResponseAccountDTO;
import icesi.university.accountSystem.dto.TransactionOperationDTO;
import icesi.university.accountSystem.dto.TransactionResultDTO;
import icesi.university.accountSystem.enums.TypeAccount;
import icesi.university.accountSystem.mapper.IcesiAccountMapper;
import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.repository.IcesiAccountRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private  IcesiAccountRepository icesiAccountRepository;
    private IcesiAccountMapper icesiAccountMapper;

    private IcesiUserRepository icesiUserRepository;

    public ResponseAccountDTO save(RequestAccountDTO accountDTO){
        var user = icesiUserRepository.findByEmail(accountDTO.getUser()).orElseThrow(()-> new RuntimeException("User doesn't exists"));
        IcesiAccount account = icesiAccountMapper.fromIcesiAccountDTO(accountDTO);
        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(validateAccountNumber(getRandomAccountNumber()));
        account.setActive(true);
        account.setUser(user);
        return icesiAccountMapper.fromAccountToSendAccountDTO(icesiAccountRepository.save(account));
    }
@Transactional
    public String activateAccount(String accountNumber){
        var account = icesiAccountRepository.findByAccountNumber(accountNumber, false)
                .orElseThrow(() -> new RuntimeException("The account: " + accountNumber + " can't be enabled"));
        account.setActive(true);
        icesiAccountRepository.save(account);
        return "The account is enabled";
    }

    @Transactional
    public String deactivateAccount(String accountNumber){
        var account = icesiAccountRepository.findByAccountNumber(accountNumber, true)
                .orElseThrow(() -> new RuntimeException("The account: " + accountNumber + " can't be disabled"));
        account.setActive(false);
        icesiAccountRepository.save(account);
        return "The account was disabled";
    }
    @Transactional
    public TransactionResultDTO withdrawal(TransactionOperationDTO transaction){
        IcesiAccount account = getAccountByAccountNumber(transaction.getAccountFrom());
        validateAccountBalance(account, transaction.getAmount());
        account.setBalance( account.getBalance() - transaction.getAmount() );
        icesiAccountRepository.save(account);
        return icesiAccountMapper.fromTransactionOperation(transaction, "The withdrawal was successful");
    }
@Transactional
    public TransactionResultDTO deposit(TransactionOperationDTO transaction){
        IcesiAccount account = getAccountByAccountNumber(transaction.getAccountTo());
        account.setBalance(account.getBalance() + transaction.getAmount());
        icesiAccountRepository.save(account);
        return icesiAccountMapper.fromTransactionOperation(transaction, "The deposit was successful");
    }
@Transactional
    public TransactionResultDTO transfer(TransactionOperationDTO transaction){
    IcesiAccount accountOrigin = getAccountByAccountNumber(transaction.getAccountFrom());
    IcesiAccount accountDestination = getAccountByAccountNumber(transaction.getAccountTo());
    validateAccountType(accountOrigin);
    validateAccountType(accountDestination);
    validateAccountBalance(accountOrigin, transaction.getAmount());

    accountOrigin.setBalance( accountOrigin.getBalance() - transaction.getAmount());
    accountDestination.setBalance( accountDestination.getBalance() + transaction.getAmount());

    icesiAccountRepository.save(accountOrigin);
    icesiAccountRepository.save(accountDestination);
    return icesiAccountMapper.fromTransactionOperation(transaction, "The transfer was successful");

    }

    private void validateAccountType(IcesiAccount account){
        if(account.getType() == TypeAccount.DEPOSIT_ONLY){
            throw new RuntimeException("Account: " + account.getAccountNumber() + " is deposit only");
        }
    }

    private void validateAccountBalance(IcesiAccount account, long amount){
        if (account.getBalance() < amount) {
            throw new RuntimeException("Low balance: " + account.getBalance());
        }
    }

    private String validateAccountNumber(String accountNumber) {
        if (icesiAccountRepository.existsByAccountNumber(accountNumber)) {
            return validateAccountNumber(getRandomAccountNumber());
        }
        return accountNumber;
    }

    public IcesiAccount getAccountByAccountNumber(String accountNumber) {
        return icesiAccountRepository.findByAccountNumber(accountNumber, true)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    private String getRandomAccountNumber(){
        String accountNumber = "xxx-xxxxxx-xx";
        String toReplace = "x";
        String output;
        for (int i = 0;i<accountNumber.length();i++){
                String number = Integer.toString((int)(Math.random()*10));
                accountNumber = accountNumber.replaceFirst(toReplace,number);
        }

        if(icesiAccountRepository.findByAccountNumber(accountNumber,true).isEmpty()){
            return accountNumber;
        }else{
            return getRandomAccountNumber();
        }
    }
}
