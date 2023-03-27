package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.AccountCreateDTO;
import co.edu.icesi.demo.dto.TransactionDTO;
import co.edu.icesi.demo.mapper.AccountMapper;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.AccountRepository;
import co.edu.icesi.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;

    private AccountMapper accountMapper;

    private UserRepository userRepository;

    public AccountCreateDTO save(AccountCreateDTO account){

        IcesiUser icesiUser= userRepository.findByEmail(account.getUserEmail()).orElseThrow(()-> new RuntimeException("User does not exists"));
        IcesiAccount icesiAccount= accountMapper.fromIcesiAccountDTO(account);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber(uniqueAccountNumber());
        icesiAccount.setUser(icesiUser);
        icesiAccount.setActive(true);

        return accountMapper.fromIcesiAccount(accountRepository.save(icesiAccount));

    }

   public String uniqueAccountNumber(){
        String accountNumber="";
       do{
           accountNumber=generateAccountNumber();
       }while(accountRepository.findByAccountNumber(accountNumber).isPresent());

       return accountNumber;
   }
    private String generateAccountNumber(){

        int length=11;
        Random random = new Random();
        String str = random.ints(length,0,10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());

        return String.format("%s-%s-%s", str.substring(0,3),str.substring(3,9),str.substring(9));
    }

    public TransactionDTO withdrawalMoney(TransactionDTO transactionDTO){

        IcesiAccount icesiAccount=getAccountFromRepository(transactionDTO.getAccountNumberFrom());
        validateAccountBalanceWithAmountToPull(icesiAccount.getBalance(),transactionDTO.getMoney());

        icesiAccount.setBalance(icesiAccount.getBalance()-transactionDTO.getMoney());

        accountRepository.save(icesiAccount); //update
        transactionDTO.setResult("Withdrawal successfully completed");
        return  transactionDTO;

    }

    public TransactionDTO depositMoney(TransactionDTO transactionDTO){

        IcesiAccount icesiAccount=getAccountFromRepository(transactionDTO.getAccountNumberTo());

        icesiAccount.setBalance(icesiAccount.getBalance()+transactionDTO.getMoney());

        accountRepository.save(icesiAccount); //update

        transactionDTO.setResult("Deposit successfully completed");
        return  transactionDTO;
    }
    public void validateTypeToTransfer(IcesiAccount icesiAccount){
        if(icesiAccount.getType().equals("deposit only")){
            throw new RuntimeException("Deposit only account "+ icesiAccount.getAccountNumber()+" can't transfer or be transferred money");
        }
    }
    public TransactionDTO transferMoney(TransactionDTO transactionDTO){

        IcesiAccount icesiAccountFrom=getAccountFromRepository(transactionDTO.getAccountNumberFrom());
        IcesiAccount icesiAccountTo=getAccountFromRepository(transactionDTO.getAccountNumberTo());
        validateTypeToTransfer(icesiAccountFrom);
        validateTypeToTransfer(icesiAccountTo);

        validateAccountBalanceWithAmountToPull(icesiAccountFrom.getBalance(),transactionDTO.getMoney());

        icesiAccountFrom.setBalance(icesiAccountFrom.getBalance()-transactionDTO.getMoney());
        icesiAccountTo.setBalance(icesiAccountTo.getBalance()+transactionDTO.getMoney());

        accountRepository.save(icesiAccountFrom); //update
        accountRepository.save(icesiAccountTo); //update

        transactionDTO.setResult("Transfer successfully completed");
        return  transactionDTO;
    }

    public AccountCreateDTO enableAccount(String accountNumber){

        IcesiAccount icesiAccount=accountRepository.findByAccountNumber(accountNumber,false).orElseThrow( ()-> new RuntimeException("Inactive account not found"));
        icesiAccount.setActive(true);

        accountRepository.save(icesiAccount); //update

        return accountMapper.fromIcesiAccount(icesiAccount);

    }

    public AccountCreateDTO disableAccount(String accountNumber){

        IcesiAccount icesiAccount=accountRepository.findByAccountNumber(accountNumber,true).orElseThrow( ()-> new RuntimeException("Active account not found"));

        if(icesiAccount.getBalance()>0){
            throw new RuntimeException("Balance is not 0. Account can't be disabled");
        }

        icesiAccount.setActive(false);

        accountRepository.save(icesiAccount); //update

        return accountMapper.fromIcesiAccount(icesiAccount);

    }

    public void validateAccountBalanceWithAmountToPull(long balance, long money){
        if(balance<money){
            throw new RuntimeException("Not enough money in the account to do this transaction");
        }
    }

    public IcesiAccount getAccountFromRepository(String accountNumber){

        return accountRepository.findByAccountNumber(accountNumber,true).orElseThrow( ()-> new RuntimeException("Transaction can't be made, active account "+accountNumber+" not found"));
    }

}
