package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;
    private final UserService userService;


    public IcesiAccount save(AccountCreateDTO account){

        if(account.getBalance() < 0) {
            throw new RuntimeException("The balance cannot be less than 0");
        }else if(userRepository.findById(UUID.fromString(account.getUserId())).isEmpty()){
                throw new RuntimeException("Account user not found");
        }

        String num = generateRandomCode();
        while(accountRepository.findByAccountNumber(num).isPresent()){
            num = generateRandomCode();
        }

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(num);
        icesiAccount.setUser(userRepository.findById(UUID.fromString(account.getUserId())).get());
        userService.addAccount(icesiAccount.getUser(), icesiAccount.getAccountNumber());


        return accountRepository.save(icesiAccount);
    }

    public String generateRandomCode(){
        Random rand = new Random();
        String code = "";

        for(int i = 0; i < 14; i++){
            if(i == 3 || i == 10){
                code = code+"-";
            }else{
                code = code + String.valueOf(rand.nextInt(10));
            }
        }

        return code;
    }

    public Optional<IcesiAccount> findById(UUID fromString){
        return accountRepository.findById(fromString);
    }

    public void disableAccount(String aNumber){

        if(accountRepository.findByAccountNumber(aNumber).isEmpty()){
            throw new RuntimeException("The account doesn't exists");
        }else if(accountRepository.findByAccountNumber(aNumber).get().getBalance() > 0){
            throw new RuntimeException("The account balance must be in 0 to disable");
        }else{
            accountRepository.disable(aNumber);
        }
    }

    public void enableAccount(String aNumber){

        if(accountRepository.findByAccountNumber(aNumber).isEmpty()){
            throw new RuntimeException("The account doesn't exists");
        }else if(accountRepository.findByAccountNumber(aNumber).get().getUser().isActive()){
            accountRepository.enable(aNumber);
        }else{
            throw new RuntimeException("The account is already enable");
        }
    }

    public void transfer(long amount, String accFrom, String accTo){
        if(accountRepository.findByAccountNumber(accFrom).isEmpty()){
            throw new RuntimeException("The account sending the money doesn't exists");
        }else if(accountRepository.findByAccountNumber(accTo).isEmpty()){
            throw new RuntimeException("The account taking the money doesn't exists");
        }else if(amount <= 0){
            throw new RuntimeException("The amount must be grater than 0");
        }else{
            IcesiAccount fromAcc = accountRepository.findByAccountNumber(accFrom).get();
            IcesiAccount toAcc = accountRepository.findByAccountNumber(accFrom).get();

            if(!fromAcc.isActive()){
                throw new RuntimeException("The account sending the money is disabled");
            }else if(!toAcc.isActive()) {
                throw new RuntimeException("The account taking the money is disabled");
            }else if(depositsOnlyFilter(fromAcc.getAccountNumber())){
                throw new RuntimeException("The account sending the money is deposits only");
            }else if(depositsOnlyFilter(toAcc.getAccountNumber())){
                throw new RuntimeException("The account taking the money is deposits only");
            }else if(fromAcc.getBalance()<amount){
                throw new RuntimeException("The account sending doesn't have enough money");
            }else{
                accountRepository.updateBalance(accFrom,fromAcc.getBalance()-amount);
                accountRepository.updateBalance(accTo,toAcc.getBalance()+amount);
            }
        }
    }

    public boolean depositsOnlyFilter(String number){
        return accountRepository.findByAccountNumber(number).get().getType().equalsIgnoreCase("deposits only");
    }

    public void withdrawal(String acc, long amount){
        if(accountRepository.findByAccountNumber(acc).isEmpty()){
            throw new RuntimeException("The account doesn't exists");
        }else if(amount <= 0){
            throw new RuntimeException("The amount must be grater than 0");
        }else if(accountRepository.findByAccountNumber(acc).get().isActive()){
            throw new RuntimeException("The account is disabled");
        }else if(accountRepository.findByAccountNumber(acc).get().getBalance() < amount){
            throw new RuntimeException("The account doesn't have enough money");
        }else{
            accountRepository.updateBalance(acc, accountRepository.findByAccountNumber(acc).get().getBalance()-amount);
        }
    }

    public void deposit(String acc, long amount){
        if(accountRepository.findByAccountNumber(acc).isEmpty()){
            throw new RuntimeException("The account doesn't exists");
        }else if(amount <= 0){
            throw new RuntimeException("The amount must be grater than 0");
        }else if(accountRepository.findByAccountNumber(acc).get().isActive()){
            throw new RuntimeException("The account is disabled");
        }else if(accountRepository.findByAccountNumber(acc).get().getBalance() < amount){
            throw new RuntimeException("The account doesn't have enough money");
        }else{
            accountRepository.updateBalance(acc, accountRepository.findByAccountNumber(acc).get().getBalance()+amount);
        }
    }

}
