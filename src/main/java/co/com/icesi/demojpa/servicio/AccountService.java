package co.com.icesi.demojpa.servicio;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.TransactionDTO;
import co.com.icesi.demojpa.dto.response.ResponseAccountDTO;
import co.com.icesi.demojpa.error.util.IcesiExceptionBuilder;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.mapper.response.AccountResponseMapper;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AccountService {

    private final Random rand= new Random();
    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final UserService userService;
    private final AccountMapper accountMapper;

    private final AccountResponseMapper accountResponseMapper;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, UserService userService, AccountMapper accountMapper, AccountResponseMapper accountResponseMapper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.accountMapper = accountMapper;
        this.accountResponseMapper = accountResponseMapper;
    }

    public ResponseAccountDTO save(AccountCreateDTO account){
        if(account.getBalance()<0){
            throw new RuntimeException("El balance no puede ser menor a 0");
        }

        IcesiUser icesiUser = userRepository.findById(UUID.fromString(account.getUserId())).orElseThrow(() -> new RuntimeException("No existe una cuenta con esta id"));

        String number=genNumber();
        while(accountRepository.findByAccountNumber(number).isPresent()){
            number=genNumber();
        }

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        icesiAccount.setAccount(icesiUser);
        icesiAccount.setAccountNumber(number);
        userService.addAccount(icesiAccount.getAccount(), icesiAccount.getAccountNumber());
        icesiAccount.setAccountId(UUID.randomUUID());
        return accountResponseMapper.fromIcesiAccount( accountRepository.save(icesiAccount));
    }

    public String genNumber(){
        IntStream nums = rand.ints(11,0,10);
        String number= nums.boxed().map(String::valueOf).collect(Collectors.joining());
        return String.format("%s-%s-%s",number.substring(0,3),number.substring(3,9),number.substring(9,11));
    }


    @Transactional
    public void disableAccount(String accountNumber){

        IcesiAccount icesiAccount =  accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("No existe una cuenta con este numero"));

        if(icesiAccount.getBalance()!=0){
            throw new RuntimeException("El balance de esta cuenta no es 0");
        }

        accountRepository.disableAccount(accountNumber);

    }


    public void enableAccount(String accountNumber){

        accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("No existe una cuenta con este numero"));

        if(accountRepository.findByAccountNumber(accountNumber).get().isActive()){
            throw new RuntimeException("La cuenta ya esta activada");
        }
        accountRepository.enableAccount(accountNumber);

    }
    @Transactional
    public void withdrawal(String accountNumber, long withdrawalAmount){

        IcesiAccount icesiAccount= accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("No existe una cuenta con este numero"));

        if (!icesiAccount.isActive()) {
            throw new RuntimeException("La cuenta no esta activa");
        }

        if (withdrawalAmount<=0) {
            throw new RuntimeException("La cantidad de dinero que se quiere sacar debe ser mayor que 0");
        }

        if(icesiAccount.getBalance()<withdrawalAmount){
            throw new RuntimeException("No hay balance suficiente para sacar");
        }

        accountRepository.updateBalance(accountNumber,icesiAccount.getBalance()-withdrawalAmount);

    }
    @Transactional
    public void deposit(String accountNumber, long depositAmount){

        IcesiAccount icesiAccount= accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("No existe una cuenta con este numero"));

        if (depositAmount<=0) {
            throw new RuntimeException("La cantidad de dinero que se quiere depositar debe ser mayor que 0");
        }

        if(!icesiAccount.isActive()){
            throw new RuntimeException("La cuenta no esta activa");
        }

        accountRepository.updateBalance(accountNumber,icesiAccount.getBalance()+depositAmount);
    }
    @Transactional
    public TransactionDTO transfer(TransactionDTO transactionDTO){
        String sendAccNum = transactionDTO.getAccountNumberOrigin();
        String receiveAccNum = transactionDTO.getAccountNumberDestination();
        long sendValue = transactionDTO.getAmount();
        IcesiAccount sendIcesiAccount = accountRepository.findByAccountNumber(sendAccNum).orElseThrow(()->
                IcesiExceptionBuilder.createIcesiException("La cuenta que manda el dinero no existe", HttpStatus.NOT_FOUND,"ACCOUNT_NOT_FOUND") );

        System.out.println("receiveAccNum: "+receiveAccNum);
        IcesiAccount receiveIcesiAccount = accountRepository.findByAccountNumber(receiveAccNum).orElseThrow(()->
                IcesiExceptionBuilder.createIcesiException("La cuenta que recibe el dinero no existe", HttpStatus.NOT_FOUND,"ACCOUNT_NOT_FOUND") );

        if (sendValue<=0) {
            throw new RuntimeException("La cantidad de dinero que se quiere enviar debe ser mayor que 0");
        }
        if (!sendIcesiAccount.isActive()){
            throw new RuntimeException("La cuenta que manda el dinero no esta activa");
        }
        if (!receiveIcesiAccount.isActive()){
            throw new RuntimeException("La cuenta que recibe el dinero no esta activa");
        }
        if (checkType(sendIcesiAccount)) {
            throw new RuntimeException("La cuenta que manda el dinero es de tipo 'deposit only' ");
        }
        if (checkType(receiveIcesiAccount)) {
            throw new RuntimeException("La cuenta que recibe el dinero es de tipo 'deposit only' ");
        }
        if(sendIcesiAccount.getBalance()<sendValue){
            throw new RuntimeException("La cuenta que manda el dinero no tiene balance suficiente ");
        }

        accountRepository.updateBalance(sendAccNum,sendIcesiAccount.getBalance()-sendValue);
        accountRepository.updateBalance(receiveAccNum, receiveIcesiAccount.getBalance()+sendValue);
        transactionDTO.setResultMessage("El nuevo balance es de: "+(sendIcesiAccount.getBalance()-sendValue));
        return transactionDTO;
    }

    public ResponseAccountDTO getAccount(String accountNumber){
        return accountResponseMapper.fromIcesiAccount(accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> IcesiExceptionBuilder.createIcesiException("No existe una cuenta con este numero", HttpStatus.NOT_FOUND,"ACCOUNT_NOT_FOUND")));
    }

    private boolean checkType(IcesiAccount account){
        return account.getType().equalsIgnoreCase("deposit only");
    }
}
