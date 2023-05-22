package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.requestDTO.IcesiAccountCreateDTO;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiTransactionDTO;
import co.com.icesi.TallerJPA.dto.responseDTO.IcesiAccountCreateResponseDTO;
import co.com.icesi.TallerJPA.enums.AccountType;
import co.com.icesi.TallerJPA.error.exception.DetailBuilder;
import co.com.icesi.TallerJPA.error.exception.ErrorCode;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiAccountRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import co.com.icesi.TallerJPA.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

import static co.com.icesi.TallerJPA.error.util.IcesiExceptionBuilder.createIcesiException;


@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountRepository accountRepository;
    private final IcesiUserRepository userRepository;
    private final IcesiAccountMapper accountMapper;

    public IcesiAccountCreateResponseDTO save(IcesiAccountCreateDTO accountDTO){
        IcesiUser user = userRepository.findByEmail(accountDTO.getIcesiUser().getEmail()).orElseThrow(
                createIcesiException(
                        "The user "+accountDTO.getIcesiUser()+" doesn't exist in the database",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "Non-existent user",accountDTO.getIcesiUser().getEmail())));

        authorization(user);
        IcesiAccount account= accountMapper.fromIcesiAccountDTO(accountDTO);

        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(validateUniqueAccountNumber(generateAccountNumber()));
        account.setIcesiUser(user);
        account.setActive(true);
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    public IcesiAccount getIcesiAccountNumber(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber).orElseThrow(
                createIcesiException(
                        "This account "+accountNumber+" doesn't exist in the database",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "Account Number","accountNumber",accountNumber)));
    }

    private void validateAccountBalance(long amountAccountBalance, long money) {
        if(amountAccountBalance < money){
            throw createIcesiException(
                    "Not enough money to transfer to the account",
                    HttpStatus.NOT_ACCEPTABLE,
                    new DetailBuilder(ErrorCode.ERR_400,"","")
            ).get();
        }
    }
    private void accountType(IcesiAccount account){
        if (account.getAccountType().equals(AccountType.DEPOSIT_ONLY)){
            throw createIcesiException(
                    "This account "+account.getAccountNumber()+" can't transfer because is marked as 'deposit only'",
                    HttpStatus.NOT_ACCEPTABLE,
                    new DetailBuilder(ErrorCode.ERR_400,"Account Number",account.getAccountNumber())
            ).get();
        }
    }

    @Transactional
    public IcesiTransactionDTO depositOnly(IcesiTransactionDTO transactionDTO) {
        IcesiAccount account =accountRepository.findAccountByAccountNumber(transactionDTO.getAccountNumberOrigin()).orElseThrow(
                createIcesiException(
                        "The account "+transactionDTO.getAccountNumberOrigin()+" does not exist, withdrawal cannot be made",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "Account Number","accountNumber",transactionDTO.getAccountNumberOrigin())));
        if (!account.isActive()) {
            throw createIcesiException(
                    "This account is not active, deposit cannot be made",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_500)
            ).get();
        }

        account.setBalance(account.getBalance() + transactionDTO.getAmount());
        accountRepository.save(account);
        transactionDTO.setMessageResult("The deposit was made successfully");
        return transactionDTO;
    }
    @Transactional
    public IcesiTransactionDTO withdrawal(IcesiTransactionDTO transactionDTO) {
        IcesiAccount account =accountRepository.findAccountByAccountNumber(transactionDTO.getAccountNumberDestination()).orElseThrow(
                createIcesiException(
                        "The account "+transactionDTO.getAccountNumberDestination()+" does not exist, withdrawal cannot be made",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "Account Number","accountNumber",transactionDTO.getAccountNumberDestination())));

        if (!account.isActive()) {
            throw createIcesiException(
                    "This account is not active, withdrawal cannot be made",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_500)
            ).get();
        }

        authorizationTransaction(account.getIcesiUser());

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

        authorizationTransaction(accountFrom.getIcesiUser());

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

    private String validateUniqueAccountNumber(String accountNumber){
        if(accountRepository.findAccountByAccountNumber(accountNumber).isPresent()){
            return validateUniqueAccountNumber(generateAccountNumber());
        }
        return accountNumber;
    }

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
    public void authorization(IcesiUser user){
        String userRole = IcesiSecurityContext.getCurrentUserRole();
        String userId = IcesiSecurityContext.getCurrentUserId();
        if(userRole.equals("USER") && !userId.equals(user.getUserId().toString()) ){
            throw createIcesiException(
                    "This user does not have permissions to perform this specific action.",
                    HttpStatus.NOT_ACCEPTABLE,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();
        }
    }

    public void authorizationTransaction(IcesiUser user){
        String userId = IcesiSecurityContext.getCurrentUserId();
        if(!userId.equals(user.getUserId().toString()) ){
            throw createIcesiException(
                    "This user does not have permissions to perform this specific transaction.",
                    HttpStatus.NOT_ACCEPTABLE,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();
        }
    }

    @Transactional
    public String enableAccount(String accountNumber){
        IcesiAccount account=accountRepository.findAccountByAccountNumber(accountNumber).orElseThrow(
                createIcesiException(
                        "Account could not be found for activation",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "Non-existent account number",accountNumber)));
        if (account.isActive()) {
            throw createIcesiException(
                    "This account cannot be enabled because it is already active",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_500)
            ).get();
        }
        authorization(account.getIcesiUser());
        account.setActive(true);
        accountRepository.save(account);
        return "The account was enabled successfully";
    }

    @Transactional
    public String disableAccount(String accountNumber){
        IcesiAccount account=accountRepository.findAccountByAccountNumber(accountNumber).orElseThrow(
                createIcesiException(
                        "Account could not be found for deactivation",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "Non-existent account number",accountNumber)));
        if (account.getBalance()>0) {
            throw createIcesiException(
                    "This account cannot be disabled because it balance isn't 0",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_500)
            ).get();
        }
        authorization(account.getIcesiUser());
        account.setActive(false);
        accountRepository.save(account);
        return "The account was disabled successfully";
    }
}




