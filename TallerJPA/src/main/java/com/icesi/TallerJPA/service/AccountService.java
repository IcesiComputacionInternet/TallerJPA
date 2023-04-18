package com.icesi.TallerJPA.service;

import com.icesi.TallerJPA.dto.request.IcesiAccountDTO;
import com.icesi.TallerJPA.dto.response.IcesiAccountResponseDTO;
import com.icesi.TallerJPA.dto.response.IcesiTransactionDTO;
import com.icesi.TallerJPA.enums.IcesiAccountType;
import com.icesi.TallerJPA.mapper.AccountMapper;
import com.icesi.TallerJPA.model.IcesiAccount;
import com.icesi.TallerJPA.repository.AccountRepository;
import com.icesi.TallerJPA.repository.UserRespository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final UserRespository userRespository;

    public IcesiAccountResponseDTO save(IcesiAccountDTO icesiAccountDTO) {
        if(icesiAccountDTO.getBalance() <= 0 ){throw new RuntimeException("Balance can't be below 0");}
        return createAccount(icesiAccountDTO);
    }

    public IcesiAccountResponseDTO createAccount(IcesiAccountDTO icesiAccountDTO){

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(icesiAccountDTO);
        icesiAccount.setIcesiUser(userRespository.findIcesiUserByEmail(icesiAccountDTO.getEmailUser())
                .orElseThrow(()-> new RuntimeException("User not found")));
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
    public IcesiTransactionDTO activeAccount(String accountNumber){
        accountRepository.activeAccount(accountNumber);

        IcesiTransactionDTO acc = createTransactionDTO();
        acc.setOrigin(accountNumber);
        acc.setMessage("The account " + accountNumber + " was active");
        return acc;
    }

    @Transactional
    public IcesiTransactionDTO disableAccount(String accountNumber){
        accountRepository.inactiveAccount(accountNumber);
        IcesiTransactionDTO acc = createTransactionDTO();
        acc.setOrigin(accountNumber);
        acc.setMessage("The account " + accountNumber + " was inactive");

        if(!accountRepository.IcesiAccountByActive(accountNumber)){
            return acc;
        } else {
            throw new RuntimeException("This account can not be disable");
        }
    }

    @Transactional
    public IcesiTransactionDTO withdrawal(String accountNumber, Long value){
        try{
            accountRepository.withdrawalAccount(accountNumber, value);
            IcesiTransactionDTO acc = createTransactionDTO();
            acc.setOrigin(accountNumber);
            acc.setAmount(value);
            acc.setMessage("The withdrawal was successful");
            return acc;
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("You don't have the amount necessary");
        }
    }

    @Transactional
    public IcesiTransactionDTO deposit(String accountNumber, Long value){
        if(value>0){
            accountRepository.depositAccount(accountNumber, value);
            IcesiTransactionDTO acc = createTransactionDTO();
            acc.setOrigin(accountNumber);
            acc.setAmount(value);
            acc.setMessage("The deposit was successful");
            return acc;
        } else {
            throw new RuntimeException("Don't deposit a negative amount");
        }
    }

    @Transactional
    public IcesiTransactionDTO transfer(String accountNumberOrigin, String accountNumberDestination, Long value){

        accountRepository.getTypeofAccount(
                accountNumberOrigin).orElseThrow(()-> new RuntimeException("Deposit Origin Account or inactive"));
        accountRepository.getTypeofAccount(
                accountNumberDestination).orElseThrow(()->new RuntimeException("Deposit Destination Account or inactive"));

        IcesiTransactionDTO acc = createTransactionDTO();

        withdrawal(accountNumberOrigin, value);
        deposit(accountNumberDestination, value);
        acc.setOrigin(accountNumberOrigin);
        acc.setDestination(accountNumberDestination);
        acc.setAmount(value);
        acc.setMessage("The transfer was successful");

        return acc;
    }

    public IcesiTransactionDTO createTransactionDTO(){
        return IcesiTransactionDTO.builder().build();
    }

}
