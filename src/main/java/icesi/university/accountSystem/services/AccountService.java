package icesi.university.accountSystem.services;

import icesi.university.accountSystem.dto.RequestAccountDTO;
import icesi.university.accountSystem.dto.ResponseAccountDTO;
import icesi.university.accountSystem.dto.TransactionOperationDTO;
import icesi.university.accountSystem.dto.TransactionResultDTO;
import icesi.university.accountSystem.enums.TypeAccount;
import icesi.university.accountSystem.mapper.IcesiAccountMapper;
import icesi.university.accountSystem.mapper.IcesiUserMapper;
import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.model.IcesiUser;
import icesi.university.accountSystem.repository.IcesiAccountRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import icesi.university.accountSystem.security.IcesiSecurityContext;
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

    private IcesiUserMapper icesiUserMapper;

    public ResponseAccountDTO save(RequestAccountDTO accountDTO){
        var user = icesiUserRepository.findByEmail(accountDTO.getUser()).orElseThrow(()-> new RuntimeException("User doesn't exists"));
        validateAccountHimself(user.getUserId().toString());
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
        validateAccountUser(account.getUser().getUserId().toString());
        account.setActive(true);
        icesiAccountRepository.save(account);
        return "The account is enabled";
    }

    @Transactional
    public String deactivateAccount(String accountNumber){
        var account = icesiAccountRepository.findByAccountNumber(accountNumber, true)
                .orElseThrow(() -> new RuntimeException("The account: " + accountNumber + " can't be disabled"));
        validateAccountUser(account.getUser().getUserId().toString());
        account.setActive(false);
        icesiAccountRepository.save(account);
        return "The account was disabled";
    }
    @Transactional
    public TransactionResultDTO withdrawal(TransactionOperationDTO transaction){
        IcesiAccount account = getAccountByAccountNumber(transaction.getAccountFrom());
        validateAccountUser(account.getUser().getUserId().toString());
        validateAccountBalance(account, transaction.getAmount());
        account.setBalance( account.getBalance() - transaction.getAmount() );
        icesiAccountRepository.save(account);
        return icesiAccountMapper.fromTransactionOperation(transaction, "The withdrawal was successful");
    }
@Transactional
    public TransactionResultDTO deposit(TransactionOperationDTO transaction){
        IcesiAccount account = getAccountByAccountNumber(transaction.getAccountTo());
        validateAccountUser(account.getUser().getUserId().toString());
        account.setBalance(account.getBalance() + transaction.getAmount());
        icesiAccountRepository.save(account);
        return icesiAccountMapper.fromTransactionOperation(transaction, "The deposit was successful");
    }
@Transactional
    public TransactionResultDTO transfer(TransactionOperationDTO transaction){
    IcesiAccount accountOrigin = getAccountByAccountNumber(transaction.getAccountFrom());
    IcesiAccount accountDestination = getAccountByAccountNumber(transaction.getAccountTo());
    validateAccountUser(accountOrigin.getUser().getUserId().toString());
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

    private void validateAccountHimself(String icesiUserId){
        boolean creatingAccountToAnotherUser = !IcesiSecurityContext.getCurrentUserId().equals(icesiUserId);
        IcesiUser loggedIcesiUser = icesiUserRepository.findById(UUID.fromString(IcesiSecurityContext.getCurrentUserId()))
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean isNotAnAdminRole = !IcesiSecurityContext.getCurrentUserRole().equals("ADMIN");
        if(isNotAnAdminRole && creatingAccountToAnotherUser){
            throw new RuntimeException("You can't create an account to another user");
        }
    }

    private void validateAccountUser(String icesiUserId){
        IcesiUser icesiUser = icesiUserRepository.findById(UUID.fromString(icesiUserId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean isNotAnAdminRole = !IcesiSecurityContext.getCurrentUserRole().equals("ADMIN");
        boolean theAccountDoesNotBelongToTheUser = !IcesiSecurityContext.getCurrentUserId().equals(icesiUserId);
        if (isNotAnAdminRole && theAccountDoesNotBelongToTheUser){
            throw new RuntimeException("You can't see the account of another user");
        }
    }
}
