package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.AccountDTO;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.repository.AccountRepository;
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

    public void save(AccountDTO account) {

        String accountNumber = validateAccountNumber(generateAccountNumber());

        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(accountNumber);
        account.setBalance(100L);

        accountRepository.save(accountMapper.fromAccountDTO(account));
    }

    private String validateAccountNumber(String accountNumber){
        if(accountRepository.existsByAccountNumber(accountNumber)){
            return validateAccountNumber(generateAccountNumber());
        }
        return accountNumber;
    }

    private String generateAccountNumber() {
        Random random = new Random();
        IntStream intStream = random.ints(10, 0, 9);

        String randomNumbers = intStream
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());

        return String.format("%s-%s-%s",
                randomNumbers.substring(0, 3),
                randomNumbers.substring(3, 9),
                (int) Math.floor(Math.random() * 100));
    }
}
