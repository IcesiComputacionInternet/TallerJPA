package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiAccountCreateDTO;
import co.com.icesi.TallerJPA.dto.IcesiTransactionDTO;
import co.com.icesi.TallerJPA.dto.responseDTO.IcesiAccountCreateResponseDTO;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiAccountRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;


@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountRepository accountRepository;
    private final IcesiUserRepository userRepository;
    private final IcesiAccountMapper accountMapper;

    public IcesiAccountCreateResponseDTO save(IcesiAccountCreateDTO accountDTO){
        IcesiUser user = userRepository.findByEmail(accountDTO.getIcesiUser()).orElseThrow(() -> new RuntimeException("The user "+accountDTO.getIcesiUser()+" doesn't exist in the database"));

        IcesiAccount account= accountMapper.fromIcesiAccountDTO(accountDTO);

        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(validateUniqueAccountNumber(generateAccountNumber()));
        account.setUser(user);
        account.setActive(true);
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    //Accounts marked as deposit only can't transfer or be transferred money, only withdrawal and deposit.
    public IcesiAccount getIcesiAccountNumber(String accountNumber){
        return accountRepository.findAccountByAccountNumber(accountNumber,true)
                .orElseThrow( ()-> new RuntimeException("This account "+accountNumber+" doesn't exist in the database"));
    }
    private void validateAccountBalance(long amountAccountBalance, long money) {
        if(amountAccountBalance < money){
            throw new RuntimeException("Not enough money to transfer to the account");
        }
    }
    private void accountType(IcesiAccount account){
        if (account.getType().equalsIgnoreCase("DEPOSIT_ONLY")){
            throw new RuntimeException("This account "+account.getAccountNumber()+" can't transfer because is marked as 'deposit only'");
        }
    }
    @Transactional
    public IcesiTransactionDTO depositOnly(IcesiTransactionDTO transactionDTO) {
        IcesiAccount account = getIcesiAccountNumber(transactionDTO.getAccountNumberOrigin());
        account.setBalance(account.getBalance() + transactionDTO.getAmount());
        accountRepository.save(account);
        transactionDTO.setMessageResult("The deposit was made successfully");
        return transactionDTO;
    }
    @Transactional
    public IcesiTransactionDTO withdrawal(IcesiTransactionDTO transactionDTO) {
        IcesiAccount account = getIcesiAccountNumber(transactionDTO.getAccountNumberOrigin());
        validateAccountBalance(account.getBalance(),transactionDTO.getAmount());
        account.setBalance(account.getBalance() - transactionDTO.getAmount());
        accountRepository.save(account);
        transactionDTO.setMessageResult("The Withdrawal was made successfully");
        return transactionDTO;
    }

    @Transactional
    public IcesiTransactionDTO transferMoney(IcesiTransactionDTO transactionDTO){
        IcesiAccount accountFrom=getIcesiAccountNumber(transactionDTO.getAccountNumberOrigin());
        IcesiAccount accountTo=getIcesiAccountNumber(transactionDTO.getAccountNumberDestination());

        accountType(accountFrom);
        accountType(accountTo);

        validateAccountBalance(accountFrom.getBalance(),transactionDTO.getAmount());

        accountFrom.setBalance(accountFrom.getBalance() - transactionDTO.getAmount());
        accountTo.setBalance(accountTo.getBalance() + transactionDTO.getAmount());

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);

        transactionDTO.setMessageResult("The transfer was made successfully");
        return  transactionDTO;
    }

    //Accounts number should be unique.
    private String validateUniqueAccountNumber(String accountNumber){
        if(accountRepository.findAccountByAccountNumber(accountNumber,true).isPresent()){
            return validateUniqueAccountNumber(generateAccountNumber());
        }
        return accountNumber;
    }

    ////Accounts number should have the format xxx-xxxxxx-xx where x are numbers [0-9].
    private String generateAccountNumber(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            sb.append(random.nextInt(10));
        }
        sb.append("-");
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        sb.append("-");
        for (int i = 0; i < 2; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    //Enabled and disabled account
    @Transactional
    public String enableAccount(String accountNumber){
        IcesiAccount account=accountRepository.findAccountByAccountNumber(accountNumber,false).
                orElseThrow( ()-> new RuntimeException("Account could not be found for activation"));
        account.setActive(true);
        accountRepository.save(account);
        return "The account was enabled successfully";
    }

    @Transactional
    public String disableAccount(String accountNumber){
        IcesiAccount account=accountRepository.findAccountByAccountNumber(accountNumber,true).
                orElseThrow( ()-> new RuntimeException("Account could not be found for deactivation"));
        if(account.getBalance()>0){
            throw new RuntimeException("The account must have 0 balance for deactivation");
        }
        account.setActive(false);
        accountRepository.save(account);
        return "The account was disabled successfully";
    }
}




