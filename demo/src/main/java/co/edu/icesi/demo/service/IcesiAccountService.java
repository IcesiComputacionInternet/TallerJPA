package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.IcesiAccountDto;
import co.edu.icesi.demo.mapper.IcesiAccountMapper;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.repository.IcesiAccountRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Builder
public class IcesiAccountService {
    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiAccountMapper icesiAccountMapper;

    public IcesiAccount saveAccount(IcesiAccountDto accountToSave){
        if(icesiAccountRepository.findByAccountNumber(accountToSave.getAccountNumber()).isPresent()){
            throw new RuntimeException("Account number taken");
        }
        if(accountToSave.getBalance()<0){
            throw new RuntimeException("Balance can´t be below 0");
        }
        IcesiAccount icesiAccount = icesiAccountMapper.fromIcesiAccountDto(accountToSave);
        icesiAccount.setAccountId(UUID.randomUUID());
        //Extremely questionable way of generating the account number tbh
        String accountNumber = "";
        for (int i =0;i<13;i++){
            if(i==3 || i==10){
                accountNumber+="-";
            }else{
                accountNumber+= ""+Math.floor(Math.random()*9);
            }
        }
        icesiAccount.setAccountNumber(accountNumber);
        return icesiAccountRepository.save(icesiAccount);
    }
}
