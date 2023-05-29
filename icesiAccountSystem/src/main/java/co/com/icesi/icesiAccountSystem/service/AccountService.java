package co.com.icesi.icesiAccountSystem.service;

import co.com.icesi.icesiAccountSystem.dto.RequestAccountDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseAccountDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseUserDTO;
import co.com.icesi.icesiAccountSystem.dto.TransactionOperationDTO;
import co.com.icesi.icesiAccountSystem.enums.ErrorCode;
import co.com.icesi.icesiAccountSystem.error.exception.DetailBuilder;
import co.com.icesi.icesiAccountSystem.mapper.AccountMapper;
import co.com.icesi.icesiAccountSystem.enums.AccountType;
import co.com.icesi.icesiAccountSystem.mapper.RoleMapper;
import co.com.icesi.icesiAccountSystem.mapper.UserMapper;
import co.com.icesi.icesiAccountSystem.model.IcesiAccount;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import co.com.icesi.icesiAccountSystem.repository.AccountRepository;
import co.com.icesi.icesiAccountSystem.repository.UserRepository;
import co.com.icesi.icesiAccountSystem.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static co.com.icesi.icesiAccountSystem.error.util.AccountSystemExceptionBuilder.createAccountSystemException;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public ResponseAccountDTO saveAccount(RequestAccountDTO requestAccountDTO){
        List<DetailBuilder> errors = new ArrayList<>();
        checkPermissionsToCreate(requestAccountDTO.getUserEmail());
        IcesiUser user = userRepository.findByEmail(requestAccountDTO.getUserEmail()).orElseThrow(
                createAccountSystemException(
                        "User does not exist.",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "User", "email", requestAccountDTO.getUserEmail())
                )
        );
        var type = checkAccountType(requestAccountDTO.getType(),errors);
        if (!errors.isEmpty()){
            throw createAccountSystemException(
                    "Some fields of the new account had errors",
                    HttpStatus.BAD_REQUEST,
                    errors.stream().toArray(DetailBuilder[]::new)
            ).get();
        }
        IcesiAccount icesiAccount = accountMapper.fromAccountDTO(requestAccountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setUser(user);
        icesiAccount.setAccountNumber(getAccountNumber());
        icesiAccount.setType(type);
        icesiAccount.setActive(true);
        accountRepository.save(icesiAccount);
        ResponseAccountDTO responseAccDTO = accountMapper.fromAccountToResponseAccountDTO(icesiAccount);
        ResponseUserDTO responseUserDTO = userMapper.fromUserToResponseUserDTO(user);
        responseUserDTO.setRole(roleMapper.fromRoleToRoleDTO(user.getRole()));
        responseAccDTO.setUser(responseUserDTO);
        return responseAccDTO;
    }

    private AccountType checkAccountType(String type, List<DetailBuilder> errors){
        var accType=AccountType.NORMAL;
        if (!type.equalsIgnoreCase("deposit only") && !type.equalsIgnoreCase("normal")){
            errors.add(new DetailBuilder(ErrorCode.ERR_ACCOUNT_TYPE));
        }
        if(type.equalsIgnoreCase("deposit only") ){
            accType= AccountType.DEPOSIT_ONLY;
        }
        return accType;
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

    private IcesiAccount validateAccountNumber(String accNum){
        var account = accountRepository.findByAccountNumber(accNum)
                        .orElseThrow(
                                createAccountSystemException(
                                        "There is not an account with the entered number.",
                                        HttpStatus.NOT_FOUND,
                                        new DetailBuilder(ErrorCode.ERR_404, "Account", "number", accNum)
                                )
                        );
        return account;
    }

    public void checkPermissionsToCreate(String accUserEmail) {
        if((IcesiSecurityContext.getCurrentUserRole().equals("BANK_USER"))||(IcesiSecurityContext.getCurrentUserRole().equals("USER")&&!IcesiSecurityContext.getCurrentUserEmail().equals(accUserEmail))){
            throw createAccountSystemException(
                    "Only an ADMIN user can create new accounts for any user, and a normal USER only can create accounts for himself.",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403)
            ).get();
        }
    }

    public void checkPermissionsToUpdate(String accUserId) {
        if((IcesiSecurityContext.getCurrentUserRole().equals("BANK_USER"))||(IcesiSecurityContext.getCurrentUserRole().equals("USER")&&!IcesiSecurityContext.getCurrentUserId().equals(accUserId))){
            throw createAccountSystemException(
                    "Only an ADMIN user or the owner or the account can update it.",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403)
            ).get();
        }
    }

    public ResponseAccountDTO enableAccount(String accountNumber){
        var account = validateAccountNumber(accountNumber);
        checkPermissionsToUpdate(account.getUser().getUserId().toString());
        account.setActive(true);
        accountRepository.save(account);
        ResponseAccountDTO responseAccountDTO = accountMapper.fromAccountToResponseAccountDTO(account);
        responseAccountDTO.setUser(userMapper.fromUserToResponseUserDTO(account.getUser()));
        return responseAccountDTO;
    }

    private void checkBalanceIsZero(long balance) {
        if(balance>0){
            throw createAccountSystemException(
                    "An account can only be disabled if the balance is 0.",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_DISABLE_ACCOUNT, balance)
            ).get();
        }
    }

    public ResponseAccountDTO disableAccount(String accountNumber){
        var account = validateAccountNumber(accountNumber);
        checkPermissionsToUpdate(account.getUser().getUserId().toString());
        checkBalanceIsZero(account.getBalance());
        account.setActive(false);
        accountRepository.save(account);
        ResponseAccountDTO responseAccountDTO = accountMapper.fromAccountToResponseAccountDTO(account);
        responseAccountDTO.setUser(userMapper.fromUserToResponseUserDTO(account.getUser()));
        return responseAccountDTO;
    }

    private void validateAccountBalance(IcesiAccount account, long amount) {
        if (amount>account.getBalance()) {
            throw createAccountSystemException(
                    "Insufficient funds.",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "amount is greater than balance", account.getBalance())
            ).get();
        }
    }

    private void checkIfAnAccountIsActive(IcesiAccount account){
        if(!account.isActive()){
            throw createAccountSystemException(
                    "The account to/from which you want to make a transaction is inactive.",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "active of "+account.getAccountNumber(), "is false")
            ).get();
        }
    }

    public TransactionOperationDTO withdrawMoney(TransactionOperationDTO transaction){
        var account = validateAccountNumber(transaction.getAccountFrom());
        checkPermissionsToUpdate(account.getUser().getUserId().toString());
        checkIfAnAccountIsActive(account);
        validateAccountBalance(account, transaction.getAmount());
        account.setBalance(account.getBalance() - transaction.getAmount());
        accountRepository.save(account);
        transaction.setResult("The withdrawal of money from the account was successful");
        return transaction;
    }

    public TransactionOperationDTO depositMoney(TransactionOperationDTO transaction){
        var account = validateAccountNumber(transaction.getAccountTo());
        checkPermissionsToUpdate(account.getUser().getUserId().toString());
        checkIfAnAccountIsActive(account);
        account.setBalance(account.getBalance() + transaction.getAmount());
        accountRepository.save(account);
        transaction.setResult("The deposit of money to the account was successful.");
        return transaction;
    }

    public TransactionOperationDTO transferMoney(TransactionOperationDTO transaction){
        var accountFrom = validateAccountNumber(transaction.getAccountFrom());
        var accountTo = validateAccountNumber(transaction.getAccountTo());
        checkPermissionsToUpdate(accountFrom.getUser().getUserId().toString());
        validateAccountBalance(accountFrom, transaction.getAmount());
        checkIfAnAccountIsActive(accountFrom);
        checkIfAnAccountIsActive(accountTo);
        if (accountFrom.getType()==AccountType.DEPOSIT_ONLY || accountTo.getType()==AccountType.DEPOSIT_ONLY){
            throw createAccountSystemException(
                    "The source or destination account is marked as deposit only, it can't transfer or be transferred money, only withdrawal and deposit.",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "account from type is "+accountFrom.getType()+" and account to is", accountTo.getType())
            ).get();
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
        return accountRepository.findAll().stream()
                .filter(x -> x.getUser().getEmail().equals(IcesiSecurityContext.getCurrentUserEmail()))
                .map(accountMapper::fromAccountToResponseAccountDTO).collect(Collectors.toList());
    }
}
