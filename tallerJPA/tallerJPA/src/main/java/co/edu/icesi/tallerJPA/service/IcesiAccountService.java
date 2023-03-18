package co.edu.icesi.tallerJPA.service;

import co.edu.icesi.tallerJPA.dto.IcesiAccountCreateDTO;
import co.edu.icesi.tallerJPA.dto.IcesiAccountToShowDTO;
import co.edu.icesi.tallerJPA.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerJPA.model.IcesiAccount;
import co.edu.icesi.tallerJPA.model.IcesiUser;
import co.edu.icesi.tallerJPA.repository.IcesiAccountRepository;
import co.edu.icesi.tallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiAccountMapper icesiAccountMapper;
    private final IcesiUserRepository icesiUserRepository;

    public IcesiAccountToShowDTO save(IcesiAccountCreateDTO account){
        checkAccount(account);
        IcesiAccount icesiAccount = icesiAccountMapper.fromIcesiAccountCreateDTO(account);
        IcesiUser icesiUser = icesiUserRepository.findByEmail(account.getIcesiUserDTO().getEmail()).
                orElseThrow(()-> new RuntimeException("Not exist an user"));
        icesiAccount.setUser(icesiUser);
        icesiAccount.setAccountNumber(generateAccountNumber());
        icesiAccount.setId(UUID.randomUUID());
        return icesiAccountMapper.fromIcesiAccountToShowDTO(icesiAccountRepository.save(icesiAccount));
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
            IcesiAccount existingAccount = icesiAccountRepository.findByAccountNumber(accountNumber).orElse(null);
            if (existingAccount == null) {
                unique = true;
            }
        }
        return prefix + "-" + suffix + "-00";
    }

    private void checkAccount(IcesiAccountCreateDTO account) {
        if (account.getBalance() < 0){
            throw new RuntimeException("Account balance can't be negative");
        }
        if(!account.isActive()&& account.getBalance() != 0){
            throw new RuntimeException("For disable your account is necessary balance equals to zero");
        }
    }

    public void checkAccountNumber(String accountNumber){
        if(icesiAccountRepository.findByAccountNumber(accountNumber).isEmpty()){
            throw new RuntimeException("This account does not exists");
        }
    }

    public void enableOrDisableAccount(String accountNumber, boolean active){
        checkAccountNumber(accountNumber);
        IcesiAccount icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber).get();
        if(!icesiAccount.isActive()&& icesiAccount.getBalance() != 0){
            throw new RuntimeException("For disable your account is necessary balance equals to zero");
        }
        icesiAccount.setActive(active);
    }

    public void withdrawalMoney(String accountNumber, long quantity){
        checkAccountNumber(accountNumber);
        checkQuantity(quantity);
        IcesiAccount icesiAccount=icesiAccountRepository.findByAccountNumber(accountNumber).get();
        checkAccountEnable(icesiAccount);
        if(icesiAccount.getBalance()<quantity){
            throw new RuntimeException("Insuficient balance, please deposit money and try again");
        }
        icesiAccount.setBalance(icesiAccount.getBalance()-quantity);
    }

    private void checkAccountEnable(IcesiAccount icesiAccount) {
        if(!icesiAccount.isActive()){
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
        IcesiAccount icesiAccount=icesiAccountRepository.findByAccountNumber(accountNumber).get();
        checkAccountEnable(icesiAccount);
        if (quantity > 0){
            icesiAccount.setBalance(icesiAccount.getBalance()+quantity);
        }
    }

    public void transferMoney(String senderAccount,String receiverAccount, long quantity){
        checkAccountNumber(senderAccount);
        checkAccountNumber(receiverAccount);
        checkQuantity(quantity);
        IcesiAccount senderIcesiAccount=icesiAccountRepository.findByAccountNumber(senderAccount).get();
        IcesiAccount receiverIcesiAccount=icesiAccountRepository.findByAccountNumber(receiverAccount).get();
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

    private void checkAvaliableForTransfer(IcesiAccount icesiAccount) {
           if(icesiAccount.getType().equals("deposit only")){
                throw new RuntimeException("Your account is only for deposit money");
            }
    }
}
