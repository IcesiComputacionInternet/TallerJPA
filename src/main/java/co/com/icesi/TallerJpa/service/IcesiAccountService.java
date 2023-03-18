package co.com.icesi.TallerJpa.service;

import co.com.icesi.TallerJpa.dto.IcesiAccountCreateDTO;
import co.com.icesi.TallerJpa.enums.AccountType;
import co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions.*;
import co.com.icesi.TallerJpa.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJpa.model.IcesiAccount;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiAccountMapper icesiAccountMapper;
    public IcesiAccount saveAccount(IcesiAccountCreateDTO account, IcesiUser icesiUser){
        if(account.getBalance()<0){
            throw new AccountBalanceNotValidException("No se puede crear una cuenta con balance menor a 0");
        }
        String accountNumber = generateAccountNumber();
        if(icesiAccountRepository.findByAccountNumber(accountNumber).isPresent()){
            throw new AccountNumberAlreadyInUseException();
        }
        if(!Arrays.stream(AccountType.values()).filter(x ->x.name().equals(account.getType().toUpperCase())).findFirst().isPresent()){
            throw new AccountTypeNotExistsException(account.getType());
        }
        if(icesiUser == null){
            throw new UserNotExistsException();
        }
        IcesiAccount icesiAccount = icesiAccountMapper.fromAccountDto(account);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(accountNumber);
        icesiAccount.setType(account.getType().toUpperCase());
        icesiAccount.setActive(true);
        icesiAccount.setIcesiUser(icesiUser);
        return icesiAccountRepository.save(icesiAccount);
    }
    public void enableAccount(IcesiAccount icesiAccount){
        icesiAccount.setActive(true);
    }
    public void disableAccount(IcesiAccount icesiAccount){
        if(icesiAccount.getBalance()>0){
            throw new AccountCantBeDisableException();
        }
        icesiAccount.setActive(false);
    }
    public IcesiAccount disableAccount(String accountNumber){
        Optional<IcesiAccount> account = icesiAccountRepository.findByAccountNumber(accountNumber);
        if(!account.isPresent()){
            throw new AccountNotExistsException("account number");
        }
        if(account.get().getBalance()==0){
            account.get().setActive(false);
        }else{
            throw new AccountCantBeDisableException();
        }
        return account.get();
    }
    public void withdrawal(IcesiAccount icesiAccount, long value) {
        if (!icesiAccount.isActive()) {
            throw new AccountDisabledException();
        }
        if (icesiAccount.getBalance() - value < 0) {
            throw new AccountUnableToWithdrawalException("You cant get that amount of money out of the account");
        }
        icesiAccount.setBalance(icesiAccount.getBalance() - value);
    }
    public void deposit(IcesiAccount icesiAccount, long value){
        if(!icesiAccount.isActive()){
            throw new AccountDisabledException();
        }
        icesiAccount.setBalance(icesiAccount.getBalance()+value);
    }
    public void transfer(IcesiAccount destiny, IcesiAccount departure, long value){
        if(!departure.isActive()){
            throw new AccountDisabledException("departure");
        }
        if(!destiny.isActive()){
            throw new AccountDisabledException("destiny");
        }
        if(departure.getBalance()<value){
            throw new AccountUnableToTransferException();
        }
        departure.setBalance(departure.getBalance()-value);
        destiny.setBalance(destiny.getBalance()+value);
    }
    private String generateAccountNumber(){
        IntStream intStream = new Random().ints(11,0,9);
        String rn = intStream.mapToObj(Integer::toString).collect(Collectors.joining());
        return String.format("%s-%s-%s",rn.substring(0,3),rn.substring(3,9),rn.substring(9,11));
    }
}
