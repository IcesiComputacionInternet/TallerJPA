package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.Enum.Action;
import co.com.icesi.TallerJPA.dto.*;
import co.com.icesi.TallerJPA.error.exception.ApplicationError;
import co.com.icesi.TallerJPA.error.exception.ApplicationException;
import co.com.icesi.TallerJPA.error.exception.ErrorCode;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiAccountRespository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import co.com.icesi.TallerJPA.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static co.com.icesi.TallerJPA.error.util.ErrorManager.createDetail;
import static co.com.icesi.TallerJPA.error.util.ErrorManager.sendDetails;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private  final IcesiAccountMapper mapper;

    private final IcesiAccountRespository accountRespository;

    private final IcesiUserRepository userRepository;

    private final IcesiSecurityContext securityContext;


    public IcesiAccountDTOResponse save(IcesiAccountDTO accountDTO){
        IcesiUser user = userRepository.findByEmail(accountDTO.getUser().getEmail()).orElseThrow(createApplicationException("The user doesn't exist", "User not found in the repository",ErrorCode.ERROR_USER_NOT_FOUND,HttpStatus.NOT_FOUND));
        validateAmountGraterOrEqualsZero(accountDTO.getBalance());
        verifyActionForItSelf(user.getUserID());
        IcesiAccount account = mapper.fromAccountDTO(accountDTO);
        account.setAccountId(UUID.randomUUID());
        account.setAccountNumber(newAccountNumber());
        account.setActive(true);
        account.setUser(user);
        user.getAccounts().add(account);
        accountRespository.save(account);
        userRepository.save(user);
        return mapper.fromIcesiAccount(account);


    }

    @Transactional
    public ActionResultDTO enableAccount(String accountNumber){
        IcesiAccount account = getIcesiAccount(accountNumber);
        canEditTheAccount(account);
        if(account.isActive()){
            throw new RuntimeException("This account was enabled already");
        }
        account.setActive(true);
        accountRespository.save(account);
        return actionFinished("The account was enabled");
    }


    @Transactional
    public ActionResultDTO disableAccount(String accountNumber){
        IcesiAccount account = getIcesiAccount(accountNumber);
        canEditTheAccount(account);
        if(account.getBalance()>0){
            throw new RuntimeException("The account "+accountNumber+" can not be disabled");
        }
        account.setActive(false);
        accountRespository.save(account);
        return actionFinished("The account was disabled");
    }

    @Transactional
    public TransactionResponseDTO  withdrawalMoney (TransactionRequestDTO transactionRequestDTO ){
       IcesiAccount account = getIcesiAccount(transactionRequestDTO.getAccountNumberFrom());
        validateAmount(transactionRequestDTO.getAmount(), account);
        Long prevBalance = account.getBalance();
        account.setBalance(account.getBalance()- transactionRequestDTO.getAmount());
        accountRespository.save(account);

        return TransactionResponseDTO.builder()
                .oldStateOfTheAccount("Previous balance: "+prevBalance)
                .newStateOfTheAccount("New balance: "+account.getBalance())
                .result("Withdrawal was made successfully")
                .build();

    }



    @Transactional
    public TransactionResponseDTO  depositMoney (TransactionRequestDTO transactionRequestDTO ){
        IcesiAccount account = getIcesiAccount(transactionRequestDTO.getAccountNumberFrom());
        Long prevBalance = account.getBalance();
        account.setBalance(account.getBalance() + transactionRequestDTO.getAmount());
        accountRespository.save(account);

        return TransactionResponseDTO.builder()
                .oldStateOfTheAccount("The previous balance of the account : "+account.getAccountNumber()+" was: "+prevBalance)
                .newStateOfTheAccount("The new balance the account "+account.getAccountNumber()+" is:  "+account.getBalance())
                .result("Deposit made successfully")
                .build();

    }

    @Transactional
    public TransactionResponseDTO transferMoney(TransactionRequestDTO transactionRequestDTO){
        IcesiAccount accountSource = getIcesiAccount(transactionRequestDTO.getAccountNumberFrom());
        IcesiAccount accountDestination = getIcesiAccount(transactionRequestDTO.getAccountNumberTo());


        isDeposit(accountSource);
        isDeposit(accountDestination);
        validateAmount(transactionRequestDTO.getAmount(), accountSource);
        Long accountSourceOldBalance = accountSource.getBalance();
        Long accounntDestinationOldBalance = accountDestination.getBalance();

        accountSource.setBalance(accountSource.getBalance()- transactionRequestDTO.getAmount());
        accountDestination.setBalance(accountDestination.getBalance()+ transactionRequestDTO.getAmount());
        accountRespository.save(accountSource);
        accountRespository.save(accountDestination);
        return TransactionResponseDTO.builder()
                .oldStateOfTheAccount("The balance of the account of origin  "+accountSource.getAccountNumber()+" was: "+accountSourceOldBalance+"\n"+
                        "The balance of the account of destination "+accountDestination.getAccountNumber()+" was: "+accounntDestinationOldBalance+"\n")
                .newStateOfTheAccount("The new balance the account  of origin"+accountSource.getAccountNumber()+" is:  "+accountSource.getBalance()+"\n"+
                        "The new balance the account "+accountDestination.getAccountNumber()+" is:  "+accountDestination.getBalance())
                .result("Transfer was made successfully")
                .build();

    }

    public List<IcesiAccountDTOResponse> getAccounts(){
        return accountRespository.findAllActivated().stream().map(mapper::fromIcesiAccount).collect(Collectors.toList());
    }

    private String newAccountNumber(){
        Random random = new Random();
        String accountNumber = IntStream.range(0,11)
                .mapToObj(x -> String.valueOf(random.nextInt(10)))
                .collect(Collectors.joining());
        accountNumber = accountNumber.substring(0,3) + "-" + accountNumber.substring(3,9)+"-"+accountNumber.substring(9,11);
         if(existAccountNumber(accountNumber)){
             return newAccountNumber();
         }
         return accountNumber;

    }

    private IcesiAccount getIcesiAccount(String accountNumber) {
        return accountRespository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("The account: "+ accountNumber +" was not found"));
    }

    private void validateAmount(long amount, IcesiAccount account) {
        if(account.getBalance() < amount){
            throw new RuntimeException("The balance is Less your current balance is" + account.getBalance());
        }
    }

    private boolean existAccountNumber(String accountNumber){
        return accountRespository.findByAccountNumber(accountNumber).isPresent();
    }

    private void isDeposit(IcesiAccount account){
        if (account.getType().equalsIgnoreCase("deposit")){
            throw new RuntimeException("The account: "+account.getAccountNumber()+" is type deposit");
        }
    }

    private void validateAmountGraterOrEqualsZero(long amount){
        if(amount<0){
            throw new RuntimeException("You can't create an account with balance below to  0");
        }
    }

    private void canEditTheAccount(IcesiAccount account){
        var userId = securityContext.getCurrenUserId();
        IcesiUser userTocheck = userRepository.findById(UUID.fromString(userId)).orElseThrow(createApplicationException("User not found", "User not found in the repository",ErrorCode.ERROR_USER_NOT_FOUND,HttpStatus.NOT_FOUND));
        if(!(userTocheck.getRole().getName().equals("ADMIN")) && !(userTocheck.getAccounts().contains(account))){
          throw  createApplicationException("Can not edit","You can not perform the operation in this account",ErrorCode.ERROR_NOT_BELONG,HttpStatus.FORBIDDEN).get();
        }


    }

    private  void verifyActionForItSelf(UUID userId) {
        var currenUserId = securityContext.getCurrenUserId();
        var currentRole = securityContext.getCurrenUserRole();
        if(!(userId.equals(UUID.fromString(currenUserId))) && !(currentRole.equalsIgnoreCase("ADMIN") || currentRole.equalsIgnoreCase("BANK"))){
            throw  createApplicationException("Cant create the account","You can not perform this operation, the account must belong to you",ErrorCode.ERROR_NOT_BELONG,HttpStatus.FORBIDDEN).get();
        }
    }

    private static Supplier<ApplicationException> createApplicationException(String message, String detail,ErrorCode errorCode, HttpStatus status) {
        return () -> new ApplicationException(message, ApplicationError.builder()
                .details(List.of(sendDetails(createDetail(detail, errorCode))))
                .status(status)
                .build());
    }

    private ActionResultDTO actionFinished(String actionPerformed){
        return  ActionResultDTO.builder().type(Action.UPDATE).actionPerformed(actionPerformed).build();
    }


}
