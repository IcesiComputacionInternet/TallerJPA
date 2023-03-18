package co.com.icesi.jpataller.service;

import co.com.icesi.jpataller.dto.IcesiAccountDTO;
import co.com.icesi.jpataller.dto.IcesiUserDTO;
import co.com.icesi.jpataller.mapper.IcesiAccountMapper;
import co.com.icesi.jpataller.mapper.IcesiUserMapper;
import co.com.icesi.jpataller.model.IcesiAccount;
import co.com.icesi.jpataller.model.IcesiUser;
import co.com.icesi.jpataller.repository.IcesiAccountRepository;
import co.com.icesi.jpataller.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountMapper icesiAccountMapper;
    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiUserService icesiUserService;
    private final IcesiUserRepository icesiUserRepository;

    private final IcesiUserMapper icesiUserMapper;

    public IcesiAccount createAccount(IcesiAccountDTO icesiAccountDTO) {
         if(icesiUserRepository.findById(UUID.fromString(icesiAccountDTO.getUserId())).isEmpty()) {
            throw new RuntimeException("No existe un usuario que cree esta cuenta.");
         } else if (icesiAccountDTO.getBalance() < 0) {
            throw new RuntimeException("El balance de la cuenta es menor de 0.");
         }

         IcesiAccount icesiAccount = icesiAccountMapper.fromDTO(icesiAccountDTO);
         icesiAccount.setAccountOwner(icesiUserRepository.findById(UUID.fromString(icesiAccountDTO.getUserId())).get());
         icesiAccount.setAccountNumber(generateNumber());
         icesiUserService.createAccount(icesiUserMapper.fromModel(icesiAccount.getAccountOwner()), icesiAccount.getAccountNumber());
         icesiAccount.setAccountId(UUID.randomUUID());
         return icesiAccountRepository.save(icesiAccount);

    }

    public void disableAccount(String accountNumber) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent()) {
            IcesiAccount account = icesiAccount.get();
            if (account.getBalance() == 0) {
                account.setActive(false);
                icesiAccountRepository.save(account);
            } else {
                throw new RuntimeException("El balance de la cuenta no es 0.");
            }
        } else {
            throw new RuntimeException("No hay una cuenta con ese número");
        }
    }

    public void enableAccount(String accountNumber) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isPresent()) {
            IcesiAccount account = icesiAccount.get();
            account.setActive(true);
            icesiAccountRepository.save(account);
        } else {
            throw new RuntimeException("No existe una cuenta con ese número");
        }
    }

    public void withdrawMoney(String accountNumber, long amount) {
        if (amount > 0){
            Optional<IcesiAccount> icesiAccountOptional = icesiAccountRepository.findByAccountNumber(accountNumber);
            if (icesiAccountOptional.isPresent()){
                IcesiAccount icesiAccount = icesiAccountOptional.get();
                if (icesiAccount.isActive()) {
                    if (icesiAccount.getBalance() >= amount) {
                        icesiAccount.setBalance(icesiAccount.getBalance() - amount);
                        icesiAccountRepository.save(icesiAccount);
                    } else {
                        throw new RuntimeException("No hay suficiente dinero para retirar de la cuenta.");
                    }
                } else {
                    throw new RuntimeException("La cuenta no está activa");
                }
            } else {
                throw new RuntimeException("No existe una cuenta con ese número");
            }
        } else {
            throw new RuntimeException("No puedes sacar 0 o menos de la cuenta");
        }

    }

    public void depositMoney(String accountNumber, long amount) {
        if (amount > 0){
            Optional<IcesiAccount> icesiAccountOptional = icesiAccountRepository.findByAccountNumber(accountNumber);
            if (icesiAccountOptional.isPresent()){
                IcesiAccount icesiAccount = icesiAccountOptional.get();
                if (icesiAccount.isActive()) {
                    icesiAccount.setBalance(icesiAccount.getBalance() + amount);
                    icesiAccountRepository.save(icesiAccount);
                } else {
                    throw new RuntimeException("La cuenta no está activa");
                }
            } else {
                throw new RuntimeException("No existe una cuenta con ese número");
            }
        } else {
            throw new RuntimeException("El valor a depositar debe ser mayor de 0");
        }
    }

    



    public String generateNumber() {
        Random rand = new Random();
        int firstGroup = rand.nextInt(1000);
        int secondGroup = rand.nextInt(1000000);
        int thirdGroup = rand.nextInt(100);

        return String.format("%03d-%06d-%02d", firstGroup, secondGroup, thirdGroup);
    }
}
