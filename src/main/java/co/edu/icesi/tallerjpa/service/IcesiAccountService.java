package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.IcesiAccountCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiAccountShowDTO;
import co.edu.icesi.tallerjpa.dto.TransactionCreateDTO;
import co.edu.icesi.tallerjpa.dto.TransactionResultDTO;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiAccountMapper icesiAccountMapper;
    private final IcesiUserRepository icesiUserRepository;
    public IcesiAccountShowDTO save(IcesiAccountCreateDTO icesiAccountCreateDTO){
        checkConditionsToCreateAccount(icesiAccountCreateDTO);

        IcesiUser icesiUser = icesiUserRepository.findByEmail(icesiAccountCreateDTO.getIcesiUserDTO().getEmail())
                .orElseThrow(() -> new RuntimeException("There is no user with the email "+icesiAccountCreateDTO.getIcesiUserDTO().getEmail()));

        String accountNumber = createAccountNumber(100)
                .orElseThrow(() -> new RuntimeException("There were errors creating the account number, please try again later"));

        IcesiAccount icesiAccount = icesiAccountMapper.fromCreateIcesiAccountDTO(icesiAccountCreateDTO);
        icesiAccount.setIcesiUser(icesiUser);
        icesiAccount.setAccountNumber(accountNumber);
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

    private Optional<String> createAccountNumber(int maximumAttempts){
        boolean isUnique = false;
        String accountNumber = null;
        int minDigit = 0, maxDigit = 9;
        int attempts = 0;
        while (!isUnique && attempts < maximumAttempts){
            String possibleAccountNumber = createRandomDigits(3, minDigit, maxDigit)+"-"+createRandomDigits(6, minDigit, maxDigit)+"-"+createRandomDigits(2, minDigit, maxDigit);
            if(!icesiAccountRepository.findByAccountNumber(possibleAccountNumber).isPresent()){
                isUnique = true;
                accountNumber = possibleAccountNumber;
            }
            attempts++;
        }
        return Optional.ofNullable(accountNumber);
    }

    private String createRandomDigits(int length, int minDigit, int maxDigit){
        return IntStream.range(0, length)
                .mapToObj(i -> Integer.toString(minDigit + (int)(Math.random()*((maxDigit - minDigit) + 1))))
                .collect(Collectors.joining(""));
    }

    public IcesiAccountShowDTO enableAccount(String accountId){
        if(getAccountById(accountId).isActive()){
            throw new RuntimeException("The account was already enabled");
        }
        icesiAccountRepository.enableAccount(accountId);
        return icesiAccountMapper.fromIcesiAccountToShowDTO(getAccountById(accountId));
    }

    public IcesiAccountShowDTO disableAccount(String accountId){
        if(getAccountById(accountId).getBalance() != 0){
            throw new RuntimeException("Account can only be disabled if the balance is 0.");
        }
        icesiAccountRepository.disableAccount(accountId);
        return icesiAccountMapper.fromIcesiAccountToShowDTO(getAccountById(accountId));
    }

    public TransactionResultDTO withdrawalMoney(TransactionCreateDTO transactionCreateDTO){
        IcesiAccount icesiAccount = getAccountById(transactionCreateDTO.getSenderAccountId());
        checkIfTheAccountIsDisabled(icesiAccount);
        if(!icesiAccount.isThereEnoughMoney(transactionCreateDTO.getAmount())){
            throw new RuntimeException("Not enough money to withdraw. At most you can withdraw: " + icesiAccount.getBalance());
        }
        long newBalance = icesiAccount.getBalance() - transactionCreateDTO.getAmount();
        icesiAccountRepository.updateBalance(newBalance, transactionCreateDTO.getSenderAccountId());
        TransactionResultDTO transactionResultDTO = icesiAccountMapper.fromTransactionCreateDTO(transactionCreateDTO);
        transactionResultDTO.setBalance(newBalance);
        transactionResultDTO.setResult("The withdrawal was successful");
        return transactionResultDTO;

    }

    private IcesiAccount getAccountById(String accountId){
        return icesiAccountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new RuntimeException("There is no account with the id: " + accountId));
    }

    public TransactionResultDTO depositMoney(TransactionCreateDTO transactionCreateDTO){
        IcesiAccount icesiAccount = getAccountById(transactionCreateDTO.getReceiverAccountId());
        checkIfTheAccountIsDisabled(icesiAccount);
        long newBalance = icesiAccount.getBalance() + transactionCreateDTO.getAmount();
        icesiAccountRepository.updateBalance(newBalance, transactionCreateDTO.getReceiverAccountId());
        TransactionResultDTO transactionResultDTO = icesiAccountMapper.fromTransactionCreateDTO(transactionCreateDTO);
        transactionResultDTO.setBalance(newBalance);
        transactionResultDTO.setResult("The deposit was successful");
        return transactionResultDTO;
    }

    public TransactionResultDTO transferMoney(TransactionCreateDTO transactionCreateDTO){
        IcesiAccount senderIcesiAccount = getAccountById(transactionCreateDTO.getSenderAccountId());
        IcesiAccount receiverIcesiAccount = getAccountById(transactionCreateDTO.getReceiverAccountId());
        checkConditionsToTransfer(senderIcesiAccount, receiverIcesiAccount, transactionCreateDTO.getAmount());
        long senderAccountNewBalance = senderIcesiAccount.getBalance() - transactionCreateDTO.getAmount();
        long receiverAccountNewBalance = receiverIcesiAccount.getBalance() + transactionCreateDTO.getAmount();
        icesiAccountRepository.updateBalance(senderAccountNewBalance, transactionCreateDTO.getSenderAccountId());
        icesiAccountRepository.updateBalance(receiverAccountNewBalance, transactionCreateDTO.getReceiverAccountId());
        TransactionResultDTO transactionResultDTO = icesiAccountMapper.fromTransactionCreateDTO(transactionCreateDTO);
        transactionResultDTO.setBalance(senderAccountNewBalance);
        transactionResultDTO.setResult("The transfer was successful");
        return transactionResultDTO;
    }

    private void checkConditionsToTransfer(IcesiAccount senderIcesiAccount, IcesiAccount receiverIcesiAccount, long moneyToTransfer){
        checkIfTheAccountIsDisabled(senderIcesiAccount);
        if(senderIcesiAccount.isMarkedAsDepositOnly()){
            throw new RuntimeException("The account with id " + senderIcesiAccount.getAccountId() + " is marked as deposit only so it can't transfers money");
        }
        if(!senderIcesiAccount.isThereEnoughMoney(moneyToTransfer)){
            throw new RuntimeException("Not enough money to transfer. At most you can transfer: " + senderIcesiAccount.getBalance());
        }
        checkIfTheAccountIsDisabled(receiverIcesiAccount);
        if(receiverIcesiAccount.isMarkedAsDepositOnly()){
            throw new RuntimeException("The account with id " + receiverIcesiAccount.getAccountId() + " is marked as deposit only so no money can be transferred");
        }
    }

    private void checkIfTheAccountIsDisabled(IcesiAccount icesiAccount){
        if (icesiAccount.isDisable()){
            throw new RuntimeException("The account "+icesiAccount.getAccountId()+" is disabled");
        }
    }

    public IcesiAccountShowDTO getAccountByAccountNumber(String accountNumber){
        return icesiAccountMapper.fromIcesiAccountToShowDTO(icesiAccountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new RuntimeException("There is no account with the number: " + accountNumber)
        ));
    }
}
