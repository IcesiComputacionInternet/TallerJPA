package co.com.icesi.demojpa.service;
import co.com.icesi.demojpa.dto.request.AccountCreateDTO;
import co.com.icesi.demojpa.dto.response.AccountResponseDTO;
import co.com.icesi.demojpa.enums.IcesiAccountType;
import co.com.icesi.demojpa.error.util.IcesiExceptionBuilder;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final UserRepository userRespository;

    private final IcesiExceptionBuilder eb = new IcesiExceptionBuilder();

    public AccountResponseDTO save(AccountCreateDTO icesiAccountDTO) {
        if(icesiAccountDTO.getBalance() <= 0 ){throw eb.exceptionDontValue("Balance can't be below 0", "Balance");}
        return createAccount(icesiAccountDTO);
    }

    public AccountResponseDTO createAccount(AccountCreateDTO icesiAccountDTO){

        IcesiAccount icesiAccount = accountMapper.fromAccountCreateDTO(icesiAccountDTO);
        icesiAccount.setUser(userRespository.findByEmail(icesiAccountDTO.getEmail())
                .orElseThrow(()->{throw eb.exceptionNotFound("User not found", "email");}));
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(setAccountNumberGenerate(generateAccountNumber()));
        setTypeAccount(icesiAccountDTO.getType().getValue(), icesiAccount);

        return accountMapper.toResponse(accountRepository.save(icesiAccount));
    }

    public String generateAccountNumber(){
        Random random = new Random();
        return String.format("%03d-%06d-%02d", random.nextInt(1000),
                random.nextInt(1000000), random.nextInt(100));
    }

    public String setAccountNumberGenerate(String number){
        if(accountRepository.existsByAccountNumber(generateAccountNumber())){
            return setAccountNumberGenerate(number);
        } else {
            return number;
        }
    }

    public void setTypeAccount(String type, IcesiAccount icesiAccount){
        try {
            icesiAccount.setType(IcesiAccountType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Account type does not exist");
        }
    }

    @Transactional
    public String activeAccount(String accountNumber){
        getUserLogged(accountNumber);
        accountRepository.activeAccount(accountNumber);
        return "The account " + accountNumber + " was active";
    }

    @Transactional
    public String disableAccount(String accountNumber){
        getUserLogged(accountNumber);
        accountRepository.inactiveAccount(accountNumber);
        if(!accountRepository.IcesiAccountByActive(accountNumber)){
            return "The account " + accountNumber + " was inactive";
        } else {
            throw eb.exceptionDontDisable("The account " + accountNumber + " can't be disabled", "accountNumber");
        }
    }

    @Transactional
    public String withdrawal(String accountNumber, Long value){
        try{
            accountRepository.withdrawalAccount(accountNumber, value);
            return "The withdrawal was successful";
        } catch (DataIntegrityViolationException e){
            throw eb.exceptionDontValue("The withdrawal can't be done", "Money");
        }
    }

    @Transactional
    public String deposit(String accountNumber, Long value){
        if(value>0){
            accountRepository.depositAccount(accountNumber, value);
            return "Deposit was successful";
        } else {
            throw eb.exceptionDontValue("The value don't be negative", "Balance");
        }
    }

    @Transactional
    public String transfer(String accountNumberOrigin, String accountNumberDestination, Long value){

        accountRepository.getTypeofAccount(
                accountNumberOrigin).orElseThrow(()-> {throw eb.exceptionAccountInactive("Deposit Origin Account inactive", accountNumberDestination);});
        accountRepository.getTypeofAccount(
                accountNumberDestination).orElseThrow(()->{throw eb.exceptionAccountInactive("Deposit Destination Account inactive", accountNumberDestination);});

        withdrawal(accountNumberOrigin, value);
        deposit(accountNumberDestination, value);

        return "The transaction was successful";
    }

    public void getUserLogged(String accountNumber){
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRespository.findByEmail(username).orElseThrow(()->{throw eb.exceptionNotFound("User not found", "email");});

        System.out.println(user.getUserId());

        if(!accountRepository.accountOwner(accountNumber, user.getUserId())){
            throw eb.exceptionNotFound("This account is not yours", "account Number in your accounts");
        }
    }
}