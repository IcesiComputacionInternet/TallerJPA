package co.com.icesi.demojpa.servicio;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.UserRepository;
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

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, UserService userService, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.accountMapper = accountMapper;
    }

    public IcesiAccount save(AccountCreateDTO account){
        if(account.getBalance()<0){
            throw new RuntimeException("El balance no puede ser menor a 0");
        } else if (userRepository.findById(UUID.fromString(account.getUserId())).isEmpty()) {
            throw new RuntimeException("No existe una cuenta con esta id");
        }

        //garantiza que no se repitan numeros entre cuentas :D
        String number=genNumber();
        while(accountRepository.findByAccountNumber(number).isPresent()){
            number=genNumber();
        }
        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        icesiAccount.setAccount(userRepository.findById(UUID.fromString(account.getUserId())).get());
        icesiAccount.setAccountNumber(number);
        userService.addAccount(icesiAccount.getAccount(), icesiAccount.getAccountNumber());
        icesiAccount.setAccountId(UUID.randomUUID());
        return accountRepository.save(icesiAccount);
    }

    public String genNumber(){
        IntStream nums = rand.ints(11,0,10);
        String number= nums.boxed().map(String::valueOf).collect(Collectors.joining());
        return String.format("%s-%s-%s",number.substring(0,3),number.substring(3,9),number.substring(9,11));
    }


    @Transactional
    public void disableAccount(String accountNumber){
        if(accountRepository.findByAccountNumber(accountNumber).isPresent() && accountRepository.findByAccountNumber(accountNumber).get().getBalance()==0){
            accountRepository.disableAccount(accountNumber);
        }else if(accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
            throw new RuntimeException("No existe una cuenta con este numero");
        }else{
            throw new RuntimeException("El balance de esta cuenta no es 0");
        }
    }



    public void enableAccount(String accountNumber){
        if(accountRepository.findByAccountNumber(accountNumber).isPresent() && !accountRepository.findByAccountNumber(accountNumber).get().isActive()){
            accountRepository.enableAccount(accountNumber);
        }else if(accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
            throw new RuntimeException("No existe una cuenta con este numero");
        }else{
            throw new RuntimeException("La cuenta ya esta activada");
        }
    }


    public void withdrawal(String accountNumber, long withdrawalAmount){
        if(accountRepository.findByAccountNumber(accountNumber).isPresent() &&
                accountRepository.findByAccountNumber(accountNumber).get().getBalance()>=withdrawalAmount &&
                accountRepository.findByAccountNumber(accountNumber).get().isActive() && withdrawalAmount>0){

            accountRepository.updateBalance(accountNumber,accountRepository.findByAccountNumber(accountNumber).get().getBalance()-withdrawalAmount);

        }else if(accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
            throw new RuntimeException("No existe una cuenta con este numero");
        } else if (!accountRepository.findByAccountNumber(accountNumber).get().isActive()) {
            throw new RuntimeException("La cuenta no esta activa");
        } else if (withdrawalAmount<=0) {
            throw new RuntimeException("La cantidad de dinero que se quiere sacar debe ser mayor que 0");
        }else{
            //Este caso se da cuando lo que el usuario quiere sacar es mayor a lo que hay en la cuenta
            throw new RuntimeException("No hay balance suficiente para sacar");
        }
    }


    public void deposit(String accountNumber, long depositAmount){
        if(accountRepository.findByAccountNumber(accountNumber).isPresent() &&
                accountRepository.findByAccountNumber(accountNumber).get().isActive() &&
                accountRepository.findByAccountNumber(accountNumber).get().isActive() && depositAmount>0){

            accountRepository.updateBalance(accountNumber,accountRepository.findByAccountNumber(accountNumber).get().getBalance()+depositAmount);

        }else if(accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
            throw new RuntimeException("No existe una cuenta con este numero");
        } else if (depositAmount<=0) {
            throw new RuntimeException("La cantidad de dinero que se quiere depositar debe ser mayor que 0");
        }else {
            throw new RuntimeException("La cuenta no esta activa");
        }
    }


    public void transfer(String sendAccNum, String receiveAccNum, long sendValue){
        if(accountRepository.findByAccountNumber(sendAccNum).isPresent() &&
                accountRepository.findByAccountNumber(receiveAccNum).isPresent() &&
                sendValue>0){

            IcesiAccount send = accountRepository.findByAccountNumber(sendAccNum).get();
            IcesiAccount receive = accountRepository.findByAccountNumber(receiveAccNum).get();

            if(send.isActive() && receive.isActive() && send.getBalance()>=sendValue &&
                    !checkType(sendAccNum) && !checkType(receiveAccNum)){

                accountRepository.updateBalance(sendAccNum,send.getBalance()-sendValue);
                accountRepository.updateBalance(receiveAccNum, receive.getBalance()+sendValue);

            }else if (!send.isActive()){
                throw new RuntimeException("La cuenta que manda el dinero no esta activa");
            }else if(!receive.isActive()){
                throw new RuntimeException("La cuenta que recibe el dinero no esta activa");
            } else if (checkType(sendAccNum)) {
                throw new RuntimeException("La cuenta que manda el dinero es de tipo 'deposit only' ");
            } else if (checkType(receiveAccNum)) {
                throw new RuntimeException("La cuenta que recibe el dinero es de tipo 'deposit only' ");
            } else{
                throw new RuntimeException("La cuenta que manda el dinero no tiene balance suficiente ");
            }
        }else if(accountRepository.findByAccountNumber(sendAccNum).isEmpty()) {
            throw new RuntimeException("La cuenta que manda el dinero no existe");
        } else if (sendValue<=0) {
            throw new RuntimeException("La cantidad de dinero que se quiere enviar debe ser mayor que 0");
        } else {
            throw new RuntimeException("La cuenta que recibe el dinero no existe");
        }
    }

    private boolean checkType(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber).get().getType().equalsIgnoreCase("deposit only");
    }
}
