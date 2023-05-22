package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.Enum.AccountType;
import co.com.icesi.TallerJPA.dto.AccountCreateDTO;
import co.com.icesi.TallerJPA.dto.TransactionOperationDTO;
import co.com.icesi.TallerJPA.dto.response.AccountResponseDTO;
import co.com.icesi.TallerJPA.error.enums.ErrorCode;
import co.com.icesi.TallerJPA.error.util.ArgumentsExceptionBuilder;
import co.com.icesi.TallerJPA.error.util.DetailBuilder;
import co.com.icesi.TallerJPA.mapper.AccountMapper;
import co.com.icesi.TallerJPA.mapper.responseMapper.AccountResponseMapper;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.AccountRepository;
import co.com.icesi.TallerJPA.repository.UserRepository;
import co.com.icesi.TallerJPA.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
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
        if (account.getBalance()<0){
            throw new RuntimeException("Balance can't be negative");
        }

        IcesiUser user = userRepository.findUserByEmail(account.getUser()).orElseThrow(() -> new RuntimeException("User not found"));

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(validateAccountNumber(generateAccountNumber()));
        icesiAccount.setActive(true);
        icesiAccount.setUser(user);

        return accountResponseMapper.fromIcesiAccount(accountRepository.save(icesiAccount));

    }
    private void validateRoleName(){
        var role = IcesiSecurityContext.getCurrentUserRole();
        if (role.equals("BANK")){
            throw ArgumentsExceptionBuilder.createArgumentsException(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            );
        }

    }

    private void validateIfUserIsOwner(String accountNumber){
        var userId = IcesiSecurityContext.getCurrentUserId();
        System.out.println("Hello "+ userId);
        boolean owner = accountRepository.findIfUserIsOwner(accountNumber,userRepository.findUserById(UUID.fromString(userId)).orElse(null));
        boolean admin = validateIfUserIsAdmin();
        if (!owner && !admin){
            throw ArgumentsExceptionBuilder.createArgumentsException(
                    "Unauthorized, you are not the owner of the account",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            );
        }
    }

    private boolean validateIfUserIsAdmin(){
        var role = IcesiSecurityContext.getCurrentUserRole();
        return role.equals("ADMIN");
    }

    public IcesiAccount getAccountByAccountNumber(String accountNumber){
        return accountRepository.findAccount(accountNumber)
                .orElseThrow(()-> new RuntimeException("Account not found"));
    }

    private void validateAccountBalance(IcesiAccount account, long amount){
        if (account.getBalance()<amount){
            throw new RuntimeException("Insufficient funds: "+account.getBalance());
        }
    }

    private void validateAccountState(IcesiAccount account){
        if (!account.isActive()){
            throw new RuntimeException("The account "+ account.getAccountNumber()+" is disabled");
        }
    }

    private void validaAccountType(IcesiAccount account){
        if (account.getType() == AccountType.DEPOSIT_ONLY){
            throw new RuntimeException("The account "+ account.getAccountNumber()+" is deposit only");
        }
    }
    @Transactional
    public String enableAccount(String accountNumber){
        validateRoleName();
        validateIfUserIsOwner(accountNumber);
        IcesiAccount account = getAccountByAccountNumber(accountNumber);
        account.setActive(true);
        accountRepository.updateState(accountNumber,true);
        return "The account is enabled";
    }

    @Transactional
    public String disableAccount(String accountNumber){
        validateRoleName();
        validateIfUserIsOwner(accountNumber);
        IcesiAccount account = getAccountByAccountNumber(accountNumber);
        account.setActive(false);
        accountRepository.updateState(accountNumber,false);
        return "The account was disabled";

    }

    @Transactional
    public TransactionOperationDTO withdraw(TransactionOperationDTO transaction){
        validateRoleName();
        validateIfUserIsOwner(transaction.getAccountFrom());
        IcesiAccount account = getAccountByAccountNumber(transaction.getAccountFrom());
        validateAccountBalance(account,transaction.getAmount());
        validateAccountState(account);
        validaAccountType(account);
        account.setBalance(account.getBalance()-transaction.getAmount());
        accountRepository.updateAccount(account.getAccountNumber(), account.getBalance());
        transaction.setResult("Withdraw successful");
        return accountResponseMapper.fromTransactionOperationDTO(transaction);
    }

    @Transactional
    public TransactionOperationDTO deposit(TransactionOperationDTO transaction){
        validateRoleName();
        validateIfUserIsOwner(transaction.getAccountTo());
        IcesiAccount account = getAccountByAccountNumber(transaction.getAccountTo());
        validateAccountState(account);
        validaAccountType(account);
        account.setBalance(account.getBalance() + transaction.getAmount());
        accountRepository.updateAccount(account.getAccountNumber(), account.getBalance());
        transaction.setResult("Deposit successful");
        return accountResponseMapper.fromTransactionOperationDTO(transaction);
    }

    @Transactional
    public TransactionOperationDTO transfer(TransactionOperationDTO transaction){
        validateRoleName();
        validateIfUserIsOwner(transaction.getAccountFrom());
        IcesiAccount accountOrigin = getAccountByAccountNumber(transaction.getAccountFrom());
        IcesiAccount accountDestination = getAccountByAccountNumber(transaction.getAccountTo());
        validaAccountType(accountOrigin);
        validaAccountType(accountDestination);
        validateAccountState(accountOrigin);
        validateAccountState(accountDestination);
        validateAccountBalance(accountOrigin,transaction.getAmount());

        accountOrigin.setBalance(accountOrigin.getBalance()-transaction.getAmount());
        accountDestination.setBalance(accountDestination.getBalance()+transaction.getAmount());

        accountRepository.updateAccount(accountOrigin.getAccountNumber(), accountOrigin.getBalance());
        accountRepository.updateAccount(accountDestination.getAccountNumber(), accountDestination.getBalance());

        transaction.setResult("Transfer successful");

        return accountResponseMapper.fromTransactionOperationDTO(transaction);
    }

    public AccountResponseDTO getAccountByNumber(String accountNumber){
        validateRoleName();
        return accountResponseMapper.fromIcesiAccount(getAccountByAccountNumber(accountNumber));
    }

    public List<AccountResponseDTO> getAllAccounts(){
        validateRoleName();
        List<IcesiAccount> accounts = accountRepository.findAll();
        return accounts.stream().map(accountResponseMapper::fromIcesiAccount).collect(Collectors.toList());
    }

    private String validateAccountNumber(String accountNumber){
        if(accountRepository.findByAccountNumber(accountNumber)){
            return validateAccountNumber(generateAccountNumber());
        }
        return accountNumber;
    }

    private String generateAccountNumber(){
        Random random = new Random();

        IntStream stream = random.ints(11,0, 10);

        String formattedStream = stream.mapToObj(String::valueOf).collect(Collectors.joining());
        return String.format("%s-%s-%s", formattedStream.substring(0, 3),
                formattedStream.substring(3, 9),
                formattedStream.substring(9, 11));
    }


}
