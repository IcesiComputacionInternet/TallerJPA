package icesi.university.accountSystem.services;

import icesi.university.accountSystem.dto.IcesiAccountDTO;
import icesi.university.accountSystem.mapper.IcesiAccountMapper;
import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.repository.IcesiAccountRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private  IcesiAccountRepository icesiAccountRepository;
    private IcesiAccountMapper icesiAccountMapper;

    private IcesiUserRepository icesiUserRepository;

    public IcesiAccount save(IcesiAccountDTO account){
        if(icesiAccountRepository.findById(account.getAccountId()).isPresent()){
            throw new RuntimeException("Account already exists");
        }else{
            IcesiAccount icesiAccount = icesiAccountMapper.fromIcesiAccountDTO(account);
            if(icesiAccount.getBalance()<0){
                throw new RuntimeException("The balance of account is below 0");
            }else{
                if((account.getUser().getEmail().isEmpty())){
                    throw  new RuntimeException("User to associate account doesn't found");
                }else{
                    icesiAccount.setUser(icesiUserRepository.findByEmail(account.getUser().getEmail()).get());
                    icesiAccount.setAccountId(UUID.randomUUID());
                    icesiAccount.setAccountNumber(getRandomAccountNumber());
                    return icesiAccountRepository.save(icesiAccount);
                }
            }
        }
    }

    public void activateAccount(String accountNumber){
        if(icesiAccountRepository.findByAccountNumber(accountNumber).isEmpty()){
            throw new RuntimeException("Account not found");
        }
        else{
            Optional<IcesiAccount> account = icesiAccountRepository.findByAccountNumber(accountNumber);
            if(account.get().isActive()){
                throw new RuntimeException("Account is already activated");
            }else{
                account.get().setActive(true);
                icesiAccountRepository.save(account.get());
            }
        }
    }

    public void deactivateAccount(String accountNumber){
        if(icesiAccountRepository.findByAccountNumber(accountNumber).isEmpty()){
            throw new RuntimeException("Account not found");
        }
        else{
            Optional<IcesiAccount> account = icesiAccountRepository.findByAccountNumber(accountNumber);
            if(!account.get().isActive()){
                throw new RuntimeException("Account is already deactivated");
            }else{
                if(account.get().getBalance()==0){
                    account.get().setActive(false);
                    icesiAccountRepository.save(account.get());
                }else{
                    throw new RuntimeException("Account balance is different from 0");
                }
            }
        }
    }

    public void withdrawal(String accountNumber,long amount){
        Optional<IcesiAccount> accountOptional = icesiAccountRepository.findByAccountNumber(accountNumber);
        if(accountOptional.isPresent()){
            IcesiAccount account = accountOptional.get();
            if(account.getBalance()<amount){
                throw new RuntimeException("Account doesn't have enough balance");
            }else if(!account.isActive()){
                throw new RuntimeException("Account is not active");
            }
            else{
                account.setBalance(account.getBalance()-amount);
                icesiAccountRepository.save(account);
            }
        }
    }

    public void deposit(String accountNumber,long amount){
        Optional<IcesiAccount> accountOptional = icesiAccountRepository.findByAccountNumber(accountNumber);
        if(accountOptional.isPresent()){
            IcesiAccount account = accountOptional.get();
            if(!account.isActive()){
                throw new RuntimeException("Account is not active");
            }
            else{
                account.setBalance(account.getBalance()+amount);
                icesiAccountRepository.save(account);
            }
        }
    }

    public void transfer(String srcAccountNumber,String dstAccountNumber,long amount){
        Optional<IcesiAccount> srcAccountOptional = icesiAccountRepository.findByAccountNumber(srcAccountNumber);
        Optional<IcesiAccount> dstAccountOptional = icesiAccountRepository.findByAccountNumber(dstAccountNumber);

        if(srcAccountOptional.isPresent() && dstAccountOptional.isPresent()){
            IcesiAccount srcAccount = srcAccountOptional.get();
            IcesiAccount dstAccount = dstAccountOptional.get();
            if(srcAccount.getType().equalsIgnoreCase("DEPOSIT") && dstAccount.getType().equalsIgnoreCase("DEPOSIT")){
                throw  new RuntimeException("One of accounts is deposit only");
            }else{
                srcAccount.setBalance(srcAccount.getBalance()-amount);
                dstAccount.setBalance(dstAccount.getBalance()+amount);
                icesiAccountRepository.save(srcAccount);
                icesiAccountRepository.save(dstAccount);
            }
        }else{
            throw new RuntimeException("Account Not Found");
        }

    }

    private String getRandomAccountNumber(){
        String accountNumber = "xxx-xxxxxx-xx";
        String toReplace = "x";
        String output;
        for (int i = 0;i<accountNumber.length();i++){
                String number = Integer.toString((int)(Math.random()*10));
                accountNumber = accountNumber.replaceFirst(toReplace,number);
        }

        if(!icesiAccountRepository.findByAccountNumber(accountNumber).isPresent()){
            return accountNumber;
        }else{
            return getRandomAccountNumber();
        }
    }
}
