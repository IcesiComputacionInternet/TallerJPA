package com.icesi.TallerJPA.service;

import com.icesi.TallerJPA.dto.IcesiAccountDTO;
import com.icesi.TallerJPA.dto.IcesiAccountResponseDTO;
import com.icesi.TallerJPA.dto.IcesiRoleDTO;
import com.icesi.TallerJPA.enums.IcesiAccountType;
import com.icesi.TallerJPA.mapper.AccountMapper;
import com.icesi.TallerJPA.model.IcesiAccount;
import com.icesi.TallerJPA.repository.AccountRepository;
import com.icesi.TallerJPA.repository.UserRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

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
    public String activeAccount(String accountNumber){
        accountRepository.activeAccount(accountNumber);
        return "The account " + accountNumber + " was active";
    }

    @Transactional
    public String disableAccount(String accountNumber){
        accountRepository.inactiveAccount(accountNumber);
        if(!accountRepository.IcesiAccountByActive(accountNumber)){
            return "The account " + accountNumber + " was inactive";
        } else {
            throw new RuntimeException("This account can not be disable");
        }
    }


}
