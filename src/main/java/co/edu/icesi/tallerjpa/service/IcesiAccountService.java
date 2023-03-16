package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.IcesiAccountCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiAccountShowDTO;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiAccountService {

    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiAccountMapper icesiAccountMapper;
    public IcesiAccountShowDTO save(IcesiAccountCreateDTO icesiAccountCreateDTO){
        checkConditionsToCreateAccount(icesiAccountCreateDTO);
        IcesiAccount icesiAccount = icesiAccountMapper.fromCreateIcesiAccountDTO(icesiAccountCreateDTO);
        icesiAccount.setAccountNumber(createAccountNumber()
                .orElseThrow(() -> new RuntimeException("There were problems creating the account number")));
        icesiAccount.setAccountId(UUID.randomUUID());
        return icesiAccountMapper.fromIcesiAccountToShowDTO(icesiAccountRepository.save(icesiAccount));
    }

    private void checkConditionsToCreateAccount(IcesiAccountCreateDTO icesiAccountCreateDTO){
        if(icesiAccountCreateDTO.getBalance() < 0){
            throw new RuntimeException("Accounts balance can't be below 0.");
        }
        if(!icesiAccountCreateDTO.isActive() && icesiAccountCreateDTO.getBalance() != 0){
            throw new RuntimeException("Account can only be disabled if the balance is 0.");
        }
    }

    private Optional<String> createAccountNumber(){
        boolean isUnique = false;
        String accountNumber = null;
        while (!isUnique){
            int firstDigits = (int) (Math.random() * 1000);
            int secondDigits = (int) (Math.random() * 1000000);
            int thirdDigits = (int) (Math.random() * 100);
            accountNumber = firstDigits+"-"+secondDigits+"-"+thirdDigits;
            if(!icesiAccountRepository.findByAccountNumber(accountNumber).isPresent()){
                isUnique = true;
            }
        }
        return Optional.ofNullable(accountNumber);
    }

    public void enableAccount(String accountId){
        icesiAccountRepository.enableAccount(accountId);
    }

    public void disableAccount(String accountId){
        if(getAccountById(accountId).getBalance() != 0){
            throw new RuntimeException("Account can only be disabled if the balance is 0.");
        }
        icesiAccountRepository.disableAccount(accountId);
    }

    public long withdrawalMoney(String accountId, long moneyToWithdraw){
        IcesiAccount icesiAccount = getAccountById(accountId);
        if(icesiAccount.isThereEnoughMoney(moneyToWithdraw)){
            long newBalance = icesiAccount.getBalance() - moneyToWithdraw;
            icesiAccountRepository.updateBalance(newBalance, accountId);
            return newBalance;
        }else{
            throw new RuntimeException("Not enough money to withdraw. At most you can withdraw: " + icesiAccount.getBalance());
        }
    }



    private IcesiAccount getAccountById(String accountId){
        return icesiAccountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new RuntimeException("There is no account with the id: " + accountId));
    }

    public long depositMoney(String accountId, long moneyToDeposit){
        IcesiAccount icesiAccount = getAccountById(accountId);
        long newBalance = icesiAccount.getBalance() + moneyToDeposit;
        icesiAccountRepository.updateBalance(newBalance, accountId);
        return newBalance;
    }

    public long transferMoneyTo(String fromAccountId, String toAccountId, long moneyToTransfer){
        checkConditionsToTransfer(fromAccountId, toAccountId, moneyToTransfer);
        long fromAccountNewBalance = getAccountById(fromAccountId).getBalance() - moneyToTransfer;
        long toAccountNewBalance = getAccountById(toAccountId).getBalance() + moneyToTransfer;
        icesiAccountRepository.updateBalance(fromAccountNewBalance, fromAccountId);
        icesiAccountRepository.updateBalance(toAccountNewBalance, toAccountId);
        return fromAccountNewBalance;
    }

    private void checkConditionsToTransfer(String fromAccountId, String toAccountId, long moneyToTransfer){
        IcesiAccount fromIcesiAccount = getAccountById(fromAccountId);
        if(fromIcesiAccount.isMarkedAsDepositOnly()){
            throw new RuntimeException("The account with id " + fromIcesiAccount + " is marked as deposit only so it can't transfers money");
        }
        if(fromIcesiAccount.isThereEnoughMoney(moneyToTransfer)){
            throw new RuntimeException("Not enough money to transfer. At most you can transfer: " + fromIcesiAccount.getBalance());
        }
        if(getAccountById(toAccountId).isMarkedAsDepositOnly()){
            throw new RuntimeException("The account with id " + fromIcesiAccount + " is marked as deposit only so no money can be transferred");
        }
    }
}
