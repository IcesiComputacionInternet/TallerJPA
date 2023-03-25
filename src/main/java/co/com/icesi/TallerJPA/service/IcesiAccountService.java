package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiAccountDTO;
import co.com.icesi.TallerJPA.dto.IcesiAccountDTOResponse;
import co.com.icesi.TallerJPA.dto.TransactionRequestDTO;
import co.com.icesi.TallerJPA.dto.TransactionResponseDTO;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiAccountRespository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private  final IcesiAccountMapper mapper;

    private final IcesiAccountRespository accountRespository;

    private final IcesiUserRepository userRepository;


    public IcesiAccountDTOResponse save(IcesiAccountDTO accountDTO){
        IcesiUser user = userRepository.findbyName(accountDTO.getUser().getFirstName()).orElseThrow(() -> new RuntimeException("The user doesn't exist"));
        validateAmountGraterOrEqualsZero(accountDTO.getBalance());

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
    public String enableAccount(String accountNumber){
        IcesiAccount account = getIcesiAccount(accountNumber);
        if(account.isActive()){
            throw new RuntimeException("This account was enabled already");
        }
        account.setActive(true);
        accountRespository.save(account);
        return "The account was enabled";
    }


    @Transactional
    public String disableAccount(String accountNumber){
        IcesiAccount account = getIcesiAccount(accountNumber);
        if(account.getBalance()>0){
            throw new RuntimeException("The account "+accountNumber+" can not be disabled");
        }
        account.setActive(false);
        accountRespository.save(account);
        return "The account was disabled";
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
       return  TransactionResponseDTO.builder()
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




}
