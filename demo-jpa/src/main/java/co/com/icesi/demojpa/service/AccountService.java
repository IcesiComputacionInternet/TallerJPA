package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public IcesiAccount save(AccountCreateDTO account){

        Optional<IcesiUser> userOptional = userRepository.findById(account.getUser().getUserId());

        if(userOptional.isEmpty()){
            throw new RuntimeException("User does not exist");
        }
        if(account.getBalance()<0){
            throw new RuntimeException("Balance must be greater than 0");
        }

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(generateAccountNumber());
        userOptional.get().getAccounts().add(icesiAccount);
        return accountRepository.save(icesiAccount);
    }

    public void disableAccount(String accountNumber){
        Optional<IcesiAccount> accountOptional = accountRepository.findByNumber(accountNumber);
        if(accountOptional.isEmpty()){
            throw new RuntimeException("Account does not exist");
        }
        if(accountOptional.get().getBalance()>0){
            throw new RuntimeException("Account must have 0 balance to be disabled");
        }else{
            accountOptional.get().setActive(false);
            accountRepository.save(accountOptional.get());
        }
    }

    public void enableAccount(String accountNumber){
        Optional<IcesiAccount> accountOptional = accountRepository.findByNumber(accountNumber);
        if(accountOptional.isEmpty()){
            throw new RuntimeException("Account does not exist");
        }else{
            accountOptional.get().setActive(true);
            accountRepository.save(accountOptional.get());
        }
    }

    public String generateAccountNumber() {
        Random rand = new Random();
        String accountNumber = IntStream.generate(() -> rand.nextInt(10))
                .limit(11)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(""));
        accountNumber.replaceFirst("(\\d{3})(\\d{6})(\\d{2})", "$1-$2-$3");
        if(accountRepository.findByNumber(accountNumber).isPresent()){
            generateAccountNumber();
        }
        return accountNumber;
    }


}
