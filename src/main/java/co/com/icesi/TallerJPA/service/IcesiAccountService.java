package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiAccountDTO;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.repository.IcesiAccountRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private  final IcesiAccountMapper mapper;

    private final IcesiAccountRespository respository;
    //TODO: implement

    public IcesiAccount save(IcesiAccountDTO dto){
        IcesiAccount account = mapper.fromAccountDTO(dto);
        if(account.getBalance() <= 0){
            throw new RuntimeException("You can't create an account with balance 0");
        }else{
            account.setAccountId(UUID.randomUUID());
            account.setAccountNumber(newAccountNumber());
            return respository.save(account);
        }

    }

    public boolean manageAccount(String id, String action){
        UUID accountID = UUID.fromString(id);
        if(respository.findByAccountId(accountID).isPresent()){
            IcesiAccount account = respository.findByAccountId(accountID).get();
            if(action.equalsIgnoreCase("enable")){
                account.setActive(true);
            }else if(action.equalsIgnoreCase("disable")){
                account.setActive(false);
            }else{
                return  false;
            }
            respository.save(account);
            return true;
        }else {
            return false;
        }
    }

    public boolean  withdrawalMoney (String accountNumber, Long amount ){
        Optional<IcesiAccount> account = respository.findByAccountNumber(accountNumber);
        if(account.isPresent()){
            IcesiAccount account1 = account.get();
            if(account1.getBalance()>=amount ){
                account1.setBalance(account1.getBalance()-amount);
                respository.save(account1);
                return true;
            }else {
                return false;
            }
        }else{
            return  false;
        }

    }

    public boolean  depositMoney (String accountNumber, Long amount ){
        Optional<IcesiAccount> account2 = respository.findByAccountNumber(accountNumber);
        if(account2.isPresent()){
            IcesiAccount account1 = account2.get();
            if(amount>0 ){
                account1.setBalance(account1.getBalance()+amount);
                respository.save(account1);
                return true;
            }else {
                return false;
            }
        }else{
            return  false;
        }

    }

    public boolean transferMoney(String accountNumberSource,String accountNumberDestination, Long amount){
        Optional<IcesiAccount> accountSource = respository.findByAccountNumber(accountNumberSource);
        Optional<IcesiAccount> accountDestination  = respository.findByAccountNumber(accountNumberDestination);
        if(accountSource.isPresent() && accountDestination.isPresent()&& amount >0){
            if(!isDeposit(accountDestination.get()) && !isDeposit(accountSource.get())){
                return depositMoney(accountNumberDestination,amount) && withdrawalMoney(accountNumberSource , amount);
            } else {
                throw new RuntimeException("One of the accounts can not transfer money");
            }

        }else{
            return false;
        }

    }

    private String newAccountNumber(){
        Random random = new Random();
        String accountNumber = IntStream.range(0,9)
                .mapToObj(x -> String.valueOf(random.nextInt(10)))
                .collect(Collectors.joining());
        accountNumber = accountNumber.substring(0,3) + "-" + accountNumber.substring(3,9)+"-"+accountNumber.substring(9,11);
        if(respository.findByAccountNumber(accountNumber).isPresent()){
            return newAccountNumber();
        }else {
            return accountNumber;
        }

    }

    private boolean isDeposit(IcesiAccount account){
        return account.getType().equalsIgnoreCase("deposit");
    }




}
