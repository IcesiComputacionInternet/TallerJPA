package co.edu.icesi.tallerJPA.service;

import co.edu.icesi.tallerJPA.dto.AccountDTO;
import co.edu.icesi.tallerJPA.mapper.AccountMapper;
import co.edu.icesi.tallerJPA.model.Account;
import co.edu.icesi.tallerJPA.repository.AccountRepository;
import co.edu.icesi.tallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final IcesiUserRepository icesiUserRepository;

    public void save(AccountDTO account) {
        account.setId(UUID.randomUUID());
        account.setAccountNumber(checkAccountNumber(generateAccountNumber()));
        account.setActive(true);
        account.setIcesiUser(IcesiUserRepository.findByEmail(account.getIcesiUser().getEmail())
                .orElseThrow(() -> new RuntimeException("User NOT exists")));

        accountRepository.save(accountMapper.fromAccountDTO(account));
    }

    private String checkAccountNumber(String accountNumber){
        if(accountRepository.isByAccountNumber(accountNumber)){
            return checkAccountNumber(generateAccountNumber());
        }
        return accountNumber;
    }

    private String generateAccountNumber() {
        String prefix = "001";
        String suffix = "";
        boolean unique = false;
        while (!unique) {
            suffix = "";
            for (int i = 0; i < 6; i++) {
                suffix += Integer.toString((int) (Math.random() * 10));
            }
            String accountNumber = prefix + "-" + suffix + "-00";


        }
        return prefix + "-" + suffix + "-00";
    }

    private void checkAccount(AccountDTO account) {
        if (account.getBalance() < 0){
            throw new RuntimeException("Account balance can't be negative");
        }
        if(!account.isActive()&& account.getBalance() != 0){
            throw new RuntimeException("For disable your account is necessary balance equals to zero");
        }
    }



    public void enableOrDisableAccount(String accountNumber, boolean active){
        checkAccountNumber(accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber).get();
        if(!account.isActive()&& account.getBalance() != 0){
            throw new RuntimeException("For disable your account is necessary balance equals to zero");
        }
        account.setActive(active);
    }

    public void withdrawalMoney(String accountNumber, long quantity){
        checkAccountNumber(accountNumber);
        checkQuantity(quantity);
        Account account = accountRepository.findByAccountNumber(accountNumber).get();
        checkAccountEnable(account);
        if(account.getBalance()<quantity){
            throw new RuntimeException("Insuficient balance, please deposit money and try again");
        }
        account.setBalance(account.getBalance()-quantity);
    }

    private void checkAccountEnable(Account account) {
        if(!account.isActive()){
            throw new RuntimeException("This account is disabled");
        }
    }

    private void checkQuantity(long quantity) {
        if(quantity < 0){
            throw new RuntimeException("The quantity can't be negative ");
        }
    }

    public void depositMoney(String accountNumber, long quantity){
        checkAccountNumber(accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber).get();
        checkAccountEnable(account);
        if (quantity > 0){
            account.setBalance(account.getBalance()+quantity);
        }
    }

    public void transferMoney(String senderAccount,String receiverAccount, long quantity){
        checkAccountNumber(senderAccount);
        checkAccountNumber(receiverAccount);
        checkQuantity(quantity);
        Account senderIcesiAccount= accountRepository.findByAccountNumber(senderAccount).get();
        Account receiverIcesiAccount= accountRepository.findByAccountNumber(receiverAccount).get();
        checkAvaliableForTransfer(senderIcesiAccount);
        checkAvaliableForTransfer(receiverIcesiAccount);
        checkAccountEnable(senderIcesiAccount);
        checkAccountEnable(receiverIcesiAccount);
        if(senderIcesiAccount.getBalance()<quantity){
            throw new RuntimeException("Insuficient balance, please deposit money and try again");
        }

        senderIcesiAccount.setBalance(senderIcesiAccount.getBalance()-quantity);
        receiverIcesiAccount.setBalance(receiverIcesiAccount.getBalance()+quantity);
    }

    private void checkAvaliableForTransfer(Account account) {
           if(account.getType().equals("deposit only")){
                throw new RuntimeException("Your account is only for deposit money");
            }
    }
}
