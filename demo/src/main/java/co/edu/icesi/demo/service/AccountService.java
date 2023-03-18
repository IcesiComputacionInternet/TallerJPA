package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.AccountCreateDTO;
import co.edu.icesi.demo.mapper.AccountMapper;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.AccountRepository;
import co.edu.icesi.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class AccountService {


    private AccountRepository accountRepository;

    private AccountMapper accountMapper;

    private UserRepository userRepository;

    public IcesiAccount save(AccountCreateDTO account){
        if(account.getBalance()<0){
            throw new RuntimeException("Account balance can't be below 0");
        }

        if(!account.isActive() && account.getBalance()>0){
            throw new RuntimeException("Account can only be disable if the balance is 0");
        }
        validateNullUser(account);
        IcesiUser icesiUser= userRepository.findByEmail(account.getUserCreateDTO().getEmail()).orElseThrow(()-> new RuntimeException("User does not exists"));
        IcesiAccount icesiAccount= accountMapper.fromIcesiAccountDTO(account);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(accountNumberSupplier().get());
        icesiAccount.setUser(icesiUser);
        icesiUser.getAccounts().add(icesiAccount);

        return accountRepository.save(icesiAccount);

    }

    public void validateNullUser(AccountCreateDTO account){
        if(account.getUserCreateDTO()==null){
            throw new RuntimeException("Account needs a user");
        }
    }

    private Supplier<String> accountNumberSupplier(){
        return ()->uniqueAccountNumber();
    }

   public String uniqueAccountNumber(){
        String accountNumber="";
       do{
           accountNumber=generateAccountNumber();
       }while(accountRepository.findByAccountNumber(accountNumber).isPresent());

       return accountNumber;
   }
    private String generateAccountNumber(){
        int max=57;
        int min=48;
        int length=11;
        Random random = new Random();
        String str = random.ints(min,max+1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint,StringBuilder::append)
                .toString();

        return String.format("%s-%s-%s", str.substring(0,3),str.substring(3,9),str.substring(9,11));
    }



    public void withdrawalMoney(IcesiAccount icesiAccount, long money){
        if(money>=0 ){
            icesiAccount.setBalance(icesiAccount.getBalance()-money);
        }else {
            throw new RuntimeException("The amount of money can't be a negative value");
        }

    }

    public void depositMoney(){

    }
    public void transferMoney(){

    }

    public void changeState(){

    }

}
