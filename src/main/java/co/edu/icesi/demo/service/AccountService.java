package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.AccountDTO;
import co.edu.icesi.demo.dto.TransactionDTO;
import co.edu.icesi.demo.error.exception.DetailBuilder;
import co.edu.icesi.demo.error.exception.ErrorCode;
import co.edu.icesi.demo.mapper.AccountMapper;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.AccountRepository;
import co.edu.icesi.demo.repository.UserRepository;
import co.edu.icesi.demo.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.edu.icesi.demo.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;

    private AccountMapper accountMapper;

    private UserRepository userRepository;

    public AccountDTO save(AccountDTO account){

        IcesiUser icesiUser= userRepository.findByEmail(account.getUserEmail()).orElseThrow( createIcesiException(
                "User does not exists",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "User with email",account.getUserEmail() )
        ));
        manageAuthorization(icesiUser);

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
        manageTransactionAuthorization(icesiAccount.getUser());
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
            throw createIcesiException(
                    "Deposit only account "+ icesiAccount.getAccountNumber()+" can't transfer or be transferred money",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Deposit only account can't transfer or be transferred money: Account Number",icesiAccount.getAccountNumber() )
            ).get();
        }
    }
    public TransactionDTO transferMoney(TransactionDTO transactionDTO){

        IcesiAccount icesiAccountFrom=getAccountFromRepository(transactionDTO.getAccountNumberFrom());
        manageTransactionAuthorization(icesiAccountFrom.getUser());
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

    public AccountDTO enableAccount(String accountNumber){

        IcesiAccount icesiAccount=accountRepository.findByAccountNumber(accountNumber,false).orElseThrow(createIcesiException(
                "Inactive account not found",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "Inactive account number",accountNumber )
        ));
        manageAuthorization(icesiAccount.getUser());
        icesiAccount.setActive(true);

        accountRepository.save(icesiAccount); //update

        return accountMapper.fromIcesiAccount(icesiAccount);

    }

    public AccountDTO disableAccount(String accountNumber){

        IcesiAccount icesiAccount=accountRepository.findByAccountNumber(accountNumber,true).orElseThrow( createIcesiException(
                "Active account not found",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "Active account number",accountNumber )
        ));
        manageAuthorization(icesiAccount.getUser());
        if(icesiAccount.getBalance()>0){

            throw createIcesiException(
                    "Balance is not 0. Account can't be disabled",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Balance is not 0. Account can't be disabled","")
            ).get();
        }

        icesiAccount.setActive(false);

        accountRepository.save(icesiAccount); //update

        return accountMapper.fromIcesiAccount(icesiAccount);

    }

    public void validateAccountBalanceWithAmountToPull(long balance, long money){
        if(balance<money){

            throw createIcesiException(
                    "Not enough money in the account to do this transaction",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "Not enough money in the account to do this transaction","" )
            ).get();
        }
    }

    public IcesiAccount getAccountFromRepository(String accountNumber){

        return accountRepository.findByAccountNumber(accountNumber,true).orElseThrow(createIcesiException(
                "Transaction can't be made",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "Transaction can't be made: Active account",accountNumber )
        ));
    }

    public void manageAuthorization(IcesiUser user){
        if(IcesiSecurityContext.getCurrentUserRole().equals("USER") && !IcesiSecurityContext.getCurrentUserId().equals(user.getUserId().toString()) ){
            throw createIcesiException(
                    "Unauthorized: User cannot perform this action",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403, "Unauthorized: User cannot perform this action")
            ).get();
        }
    }

    public void manageTransactionAuthorization(IcesiUser user){
        if(!IcesiSecurityContext.getCurrentUserId().equals(user.getUserId().toString()) ){
            throw createIcesiException(
                    "Unauthorized: User cannot perform this transaction",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403, "Unauthorized: User cannot perform this transaction")
            ).get();
        }
    }


    public void AdminAuthorizationOnly(){
        if(!IcesiSecurityContext.getCurrentUserRole().equals("ADMIN")){
            throw createIcesiException(
                    "Unauthorized: Admin only",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403, "Unauthorized: Admin only")
            ).get();
        }
    }
    public AccountDTO getAccount(String accountNumber) {
        return  accountMapper.fromIcesiAccount(accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                createIcesiException(
                        "Account number not found",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "Account number",accountNumber )
                )
        ));
    }

    public List<AccountDTO> getAllAccounts() {
        IcesiUser user=userRepository.findById(UUID.fromString(IcesiSecurityContext.getCurrentUserId())).get();
        return user.getAccounts().stream()
                .map(accountMapper::fromIcesiAccount)
                .toList();
    }
}
