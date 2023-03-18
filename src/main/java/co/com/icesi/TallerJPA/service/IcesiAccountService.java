package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiAccountDTO;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiAccountRespository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private  final IcesiAccountMapper mapper;

    private final IcesiAccountRespository mainRespository;

    private final IcesiUserRepository userRepository;


    public boolean save(IcesiAccountDTO dto){
        IcesiAccount account = mapper.fromAccountDTO(dto);
        if(account.getBalance() < 0){
            throw new RuntimeException("You can't create an account with balance below to  0");
        }else{
            if(userRepository.findbyName(account.getUser().getFirstName()).isPresent()){
                IcesiUser relationShip = userRepository.findbyName(account.getUser().getFirstName()).get();
                account.setAccountId(UUID.randomUUID());
                account.setAccountNumber(newAccountNumber());
                account.setActive(true);
                account.setUser(relationShip);
                relationShip.getAccounts().add(account);
                mainRespository.save(account);
                userRepository.save(relationShip);
                return true;
            }else {
                throw new RuntimeException("The user doesn't exist");
            }

        }

    }

    public boolean manageAccount(String accountNumber, String action){
        if(mainRespository.findByAccountNumber(accountNumber).isPresent()){
            IcesiAccount account = mainRespository.findByAccountNumber(accountNumber).get();
            if(action.equalsIgnoreCase("enable") && !account.isActive()){
                account.setActive(true);
            }else if(action.equalsIgnoreCase("disable")){
                if(account.getBalance()==0){
                    account.setActive(false);
                }else{
                    return false;
                }
            }else{
                return  false;
            }
            mainRespository.save(account);
            return true;
        }else {
            return false;
        }
    }

    public boolean  withdrawalMoney (String accountNumber, Long amount ){
        Optional<IcesiAccount> account = mainRespository.findByAccountNumber(accountNumber);
        if(account.isPresent()){
            IcesiAccount account1 = account.get();
            if(account1.getBalance()>=amount ){
                account1.setBalance(account1.getBalance()-amount);
                mainRespository.save(account1);
                return true;
            }else {
                return false;
            }
        }else{
            return  false;
        }
    }

    public boolean  depositMoney (String accountNumber, Long amount ){
        Optional<IcesiAccount> account2 = mainRespository.findByAccountNumber(accountNumber);
        if(account2.isPresent()){
            IcesiAccount account1 = account2.get();
            if(amount>0 ){
                account1.setBalance(account1.getBalance()+amount);
                mainRespository.save(account1);
                return true;
            }else {
                return false;
            }
        }else{
            return  false;
        }

    }

    public boolean transferMoney(String accountNumberSource,String accountNumberDestination, Long amount){
        Optional<IcesiAccount> accountSource = mainRespository.findByAccountNumber(accountNumberSource);
        Optional<IcesiAccount> accountDestination  = mainRespository.findByAccountNumber(accountNumberDestination);
        if(accountSource.isPresent() && accountDestination.isPresent()){
            if(!isDeposit(accountDestination.get()) && !isDeposit(accountSource.get())){
                return depositMoney(accountNumberDestination,amount) && withdrawalMoney(accountNumberSource , amount);
            } else {
                throw new RuntimeException("One of the accounts can't perform the transaction or one of them has is type deposit");
            }

        }else{
            return false;
        }

    }

    public List<IcesiAccountDTO> getAccounts(){
        return mainRespository.findAllActivated().stream().map(mapper::fromIcesiAccount).collect(Collectors.toList());
    }

    private String newAccountNumber(){
        Random random = new Random();
        String accountNumber = IntStream.range(0,11)
                .mapToObj(x -> String.valueOf(random.nextInt(10)))
                .collect(Collectors.joining());
        accountNumber = accountNumber.substring(0,3) + "-" + accountNumber.substring(3,9)+"-"+accountNumber.substring(9,11);
        if(mainRespository.findByAccountNumber(accountNumber).isPresent()){
            return newAccountNumber();
        }else {
            return accountNumber;
        }

    }

    private boolean isDeposit(IcesiAccount account){
        return account.getType().equalsIgnoreCase("deposit");
    }




}
