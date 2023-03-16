package co.com.icesi.demojpa.servicio;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class AccountService {
    //TODO llenar esta clase
    private final Random rand= new Random();
    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public IcesiAccount save(AccountCreateDTO account){
        if(account.getBalance()<0){
            throw new RuntimeException("El balance no puede ser menor a 0");
        }
        String number=genNumber();
        while(accountRepository.findByNumber(number).isPresent()){
            number=genNumber();
        }

        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(account);
        icesiAccount.setAccountNumber(number);
        icesiAccount.setAccountId(UUID.randomUUID());
        return accountRepository.save(icesiAccount);
    }

    private String genNumber(){

        return rand.ints(3,0,10)+"-"+
                rand.ints(6,0,10)+"-"+
                rand.ints(2,0,10);
    }

    //TODO disable account
    public void disableAccount(String accountNumber){
        if(accountRepository.findByNumber(accountNumber).isPresent() && accountRepository.findByNumber(accountNumber).get().getBalance()==0){
            accountRepository.disableAccount(accountNumber);
        }else if(accountRepository.findByNumber(accountNumber).isEmpty()) {
            throw new RuntimeException("No existe una cuenta con este numero");
        }else{
            throw new RuntimeException("El balance de esta cuenta no es 0");
        }
    }


    //TODO enable account
    public void enableAccount(String accountNumber){
        if(accountRepository.findByNumber(accountNumber).isPresent() && accountRepository.findByNumber(accountNumber).get().isActive()==false){
            accountRepository.enableAccount(accountNumber);
        }else if(!accountRepository.findByNumber(accountNumber).isPresent()) {
            throw new RuntimeException("No existe una cuenta con este numero");
        }else{
            throw new RuntimeException("La cuenta ya esta activada");
        }
    }

    //TODO withdrawal
    public void withdrawal(String accountNumber, long withdrawalAmount){
        if(accountRepository.findByNumber(accountNumber).isPresent() &&
                accountRepository.findByNumber(accountNumber).get().getBalance()>=withdrawalAmount &&
                accountRepository.findByNumber(accountNumber).get().isActive() &&withdrawalAmount>0){

            accountRepository.updateBalance(accountNumber,accountRepository.findByNumber(accountNumber).get().getBalance()-withdrawalAmount);

        }else if(!accountRepository.findByNumber(accountNumber).isPresent()) {
            throw new RuntimeException("No existe una cuenta con este numero");
        } else if (!accountRepository.findByNumber(accountNumber).get().isActive()) {
            throw new RuntimeException("La cuenta no esta activa");
        } else if (withdrawalAmount<=0) {
            throw new RuntimeException("La cantidad de dinero que se quiere sacar debe ser mayor que 0");
        }else{
            //Este caso se da cuando lo que el usuario quiere sacar es mayor a lo que hay en la cuenta
            throw new RuntimeException("No hay balance suficiente para sacar");
        }
    }

    //TODO deposit
    public void deposit(String accountNumber, long depositAmount){
        if(accountRepository.findByNumber(accountNumber).isPresent() &&
                accountRepository.findByNumber(accountNumber).get().isActive()){

            accountRepository.updateBalance(accountNumber,accountRepository.findByNumber(accountNumber).get().getBalance()+depositAmount);

        }else if(!accountRepository.findByNumber(accountNumber).isPresent()) {
            throw new RuntimeException("No existe una cuenta con este numero");
        } else if (depositAmount<=0) {
            throw new RuntimeException("La cantidad de dinero que se quiere depositar debe ser mayor que 0");
        }else {
            throw new RuntimeException("La cuenta no esta activa");
        }
    }

    //TODO transfer
    public void transfer(String sendAccNum, String receiveAccNum, long sendValue){
        if(accountRepository.findByNumber(sendAccNum).isPresent() &&
                accountRepository.findByNumber(receiveAccNum).isPresent()){
            IcesiAccount send = accountRepository.findByNumber(sendAccNum).get();
            IcesiAccount receive = accountRepository.findByNumber(receiveAccNum).get();
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
        }else if(accountRepository.findByNumber(sendAccNum).isEmpty()) {
            throw new RuntimeException("La cuenta que manda el dinero no existe");
        } else if (sendValue<=0) {
            throw new RuntimeException("La cantidad de dinero que se quiere enviar debe ser mayor que 0");
        } else {
            throw new RuntimeException("La cuenta que recibe el dinero no existe");
        }
    }

    private boolean checkType(String accountNumber){
        if(accountRepository.findByNumber(accountNumber).get().getType().toLowerCase().equals("deposit only")){
            return true;
        }
        return false;
    }
}
