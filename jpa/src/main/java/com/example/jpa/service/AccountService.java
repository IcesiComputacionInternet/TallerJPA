package com.example.jpa.service;

import com.example.jpa.dto.AccountRequestDTO;
import com.example.jpa.dto.AccountResponseDTO;
import com.example.jpa.mapper.AccountMapper;
import com.example.jpa.model.IcesiAccount;
import com.example.jpa.repository.AccountRepository;
import com.example.jpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    public AccountResponseDTO save(AccountRequestDTO accountDTO) {
        IcesiAccount account = accountMapper.fromAccountDTO(accountDTO);
        account.setId(UUID.randomUUID());
        account.setAccountNumber(validateAccountNumber(generateAccountNumber()));
        account.setActive(true);
        account.setUser(userRepository.findByEmail(accountDTO.getIcesiUser())
                .orElseThrow(() -> new RuntimeException("User not found")));

        return accountMapper.fromAccountToSendAccountDTO(accountRepository.save(account));
    }

    //Accounts number should have the format xxx-xxxxxx-xx where x are numbers [0-9].
    private String generateAccountNumber() {
        IntStream intStream = new Random().ints(11, 0, 9);
        String randomNumbers = intStream
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());

        return String.format("%s-%s-%s",
                randomNumbers.substring(0, 3),
                randomNumbers.substring(3, 9),
                randomNumbers.substring(9, 11));
    }

    private String validateAccountNumber(String accountNumber){
        if(accountRepository.findByAccountNumber(accountNumber)){
            return generateAccountNumber();
        }
        return accountNumber;
    }
}
