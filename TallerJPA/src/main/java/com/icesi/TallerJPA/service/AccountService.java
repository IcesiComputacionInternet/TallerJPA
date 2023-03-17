package com.icesi.TallerJPA.service;

import com.icesi.TallerJPA.dto.IcesiAccountDTO;
import com.icesi.TallerJPA.dto.IcesiAccountResponseDTO;
import com.icesi.TallerJPA.mapper.AccountMapper;
import com.icesi.TallerJPA.model.IcesiAccount;
import com.icesi.TallerJPA.repository.AccountRepository;
import com.icesi.TallerJPA.repository.UserRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(icesiAccountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(setAccountNumber(generateAccountNumber()));
        icesiAccount.setIcesiUser(userRespository.findIcesiUserByEmail(icesiAccountDTO.getEmailUser())
                .orElseThrow(()-> new RuntimeException("User not found")));
        return accountMapper.toResponse(icesiAccount);
    }

    public String generateAccountNumber(){
        Random random = new Random();
        return String.format("%03d-%06d-%02d", random.nextInt(1000),
                random.nextInt(1000000), random.nextInt(100));
    }

    public String setAccountNumber(String number){
        if(accountRepository.existsByAccountNumber(generateAccountNumber())){
            return setAccountNumber(number);
        } else {
            return number;
        }
    }


}
