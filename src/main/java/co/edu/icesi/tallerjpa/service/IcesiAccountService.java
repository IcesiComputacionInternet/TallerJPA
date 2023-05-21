package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.IcesiAccountCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiAccountShowDTO;
import co.edu.icesi.tallerjpa.dto.TransactionCreateDTO;
import co.edu.icesi.tallerjpa.dto.TransactionResultDTO;
import co.edu.icesi.tallerjpa.enums.NameIcesiRole;
import co.edu.icesi.tallerjpa.error.exception.DetailBuilder;
import co.edu.icesi.tallerjpa.error.exception.ErrorCode;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static co.edu.icesi.tallerjpa.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiAccountMapper icesiAccountMapper;
    private final IcesiUserRepository icesiUserRepository;
    public IcesiAccountShowDTO save(IcesiAccountCreateDTO icesiAccountCreateDTO){
        checkConditionsToCreateAccount(icesiAccountCreateDTO);

        IcesiUser icesiUser = icesiUserRepository.findByEmail(icesiAccountCreateDTO.getIcesiUserDTO().getEmail())
                .orElseThrow(createIcesiException(
                        "There is no user with the email "+icesiAccountCreateDTO.getIcesiUserDTO().getEmail(),
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "Icesi user", "email", icesiAccountCreateDTO.getIcesiUserDTO().getEmail())
                ));

        String accountNumber = createAccountNumber(100)
                .orElseThrow(createIcesiException(
                        "Internal server error",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        new DetailBuilder(ErrorCode.ERR_500, "There were errors creating the account number, please try again later")
                ));

        IcesiAccount icesiAccount = icesiAccountMapper.fromCreateIcesiAccountDTO(icesiAccountCreateDTO);
        icesiAccount.setIcesiUser(icesiUser);
        icesiAccount.setAccountNumber(accountNumber);
        icesiAccount.setAccountId(UUID.randomUUID());
        return icesiAccountMapper.fromIcesiAccountToShowDTO(icesiAccountRepository.save(icesiAccount));
    }

    private void checkConditionsToCreateAccount(IcesiAccountCreateDTO icesiAccountCreateDTO){
        if(icesiAccountCreateDTO.getBalance() < 0){
            throw createIcesiException(
                    "Accounts balance can't be below 0",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "balance", "Accounts balance can't be below 0")
            ).get();
        }
        if(!icesiAccountCreateDTO.isActive() && icesiAccountCreateDTO.getBalance() != 0){
            throw createIcesiException(
                    "Account can only be disabled if the balance is 0",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "isActive", "Account can only be disabled if the balance is 0")
            ).get();
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

    public IcesiAccountShowDTO enableAccount(String accountId, String icesiUserId){
        checkIfTheAccountBelongsToTheIcesiUser(accountId, icesiUserId);
        if(getAccountById(accountId).isActive()){
            throw createIcesiException(
                    "The account was already enabled",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "isActive", "The account was already enabled")
            ).get();
        }
        icesiAccountRepository.enableAccount(accountId);
        return icesiAccountMapper.fromIcesiAccountToShowDTO(getAccountById(accountId));
    }

    public IcesiAccountShowDTO disableAccount(String accountId, String icesiUserId){
        checkIfTheAccountBelongsToTheIcesiUser(accountId, icesiUserId);
        if(getAccountById(accountId).getBalance() != 0){
            throw createIcesiException(
                    "Account can only be disabled if the balance is 0",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "isActive and balance", "Account can only be disabled if the balance is 0")
            ).get();
        }
        icesiAccountRepository.disableAccount(accountId);
        return icesiAccountMapper.fromIcesiAccountToShowDTO(getAccountById(accountId));
    }

    public TransactionResultDTO withdrawalMoney(TransactionCreateDTO transactionCreateDTO, String icesiUserId){
        IcesiAccount icesiAccount = getAccountById(transactionCreateDTO.getSenderAccountId());
        checkIfTheAccountBelongsToTheIcesiUser(icesiAccount, icesiUserId);
        checkIfTheAccountIsDisabled(icesiAccount);
        if(!icesiAccount.isThereEnoughMoney(transactionCreateDTO.getAmount())){
            throw createIcesiException(
                    "Not enough money to withdraw. At most you can withdraw: " + icesiAccount.getBalance(),
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "balance", "Not enough money to withdraw. At most you can withdraw: " + icesiAccount.getBalance())
            ).get();
        }
        long newBalance = icesiAccount.getBalance() - transactionCreateDTO.getAmount();
        icesiAccountRepository.updateBalance(newBalance, transactionCreateDTO.getSenderAccountId());
        TransactionResultDTO transactionResultDTO = icesiAccountMapper.fromTransactionCreateDTO(transactionCreateDTO);
        transactionResultDTO.setBalance(newBalance);
        transactionResultDTO.setResult("The withdrawal was successful");
        return transactionResultDTO;

    }

    private IcesiAccount getAccountById(String accountId){
        return icesiAccountRepository.findById(fromIdToUUID(accountId))
                .orElseThrow(createIcesiException(
                        "There is no account with the id: " + accountId,
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "account", "id", accountId)
                ));

    }

    public TransactionResultDTO depositMoney(TransactionCreateDTO transactionCreateDTO, String icesiUserId){
        IcesiAccount icesiAccount = getAccountById(transactionCreateDTO.getReceiverAccountId());
        checkIfTheAccountBelongsToTheIcesiUser(icesiAccount, icesiUserId);
        checkIfTheAccountIsDisabled(icesiAccount);
        long newBalance = icesiAccount.getBalance() + transactionCreateDTO.getAmount();
        icesiAccountRepository.updateBalance(newBalance, transactionCreateDTO.getReceiverAccountId());
        TransactionResultDTO transactionResultDTO = icesiAccountMapper.fromTransactionCreateDTO(transactionCreateDTO);
        transactionResultDTO.setBalance(newBalance);
        transactionResultDTO.setResult("The deposit was successful");
        return transactionResultDTO;
    }

    public TransactionResultDTO transferMoney(TransactionCreateDTO transactionCreateDTO, String icesiUserId){
        IcesiAccount senderIcesiAccount = getAccountById(transactionCreateDTO.getSenderAccountId());
        IcesiAccount receiverIcesiAccount = getAccountById(transactionCreateDTO.getReceiverAccountId());
        checkIfTheAccountBelongsToTheIcesiUser(senderIcesiAccount, icesiUserId);
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
            throw createIcesiException(
                    "The account with id " + senderIcesiAccount.getAccountId() + " is marked as deposit only so it can't transfers money",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "type", "The account with id " + senderIcesiAccount.getAccountId() + " is marked as deposit only so it can't transfers money")
            ).get();
        }
        if(!senderIcesiAccount.isThereEnoughMoney(moneyToTransfer)){
            throw createIcesiException(
                    "Not enough money to transfer. At most you can transfer: " + senderIcesiAccount.getBalance(),
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "balance", "Not enough money to transfer. At most you can transfer: " + senderIcesiAccount.getBalance())
            ).get();
        }
        checkIfTheAccountIsDisabled(receiverIcesiAccount);
        if(receiverIcesiAccount.isMarkedAsDepositOnly()){
            throw createIcesiException(
                    "The account with id " + receiverIcesiAccount.getAccountId() + " is marked as deposit only so no money can be transferred",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "type", "The account with id " + receiverIcesiAccount.getAccountId() + " is marked as deposit only so no money can be transferred")
            ).get();
        }
    }

    private void checkIfTheAccountIsDisabled(IcesiAccount icesiAccount){
        if (icesiAccount.isDisable()){
            throw createIcesiException(
                    "The account "+icesiAccount.getAccountId()+" is disabled",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "isActive", "The account "+icesiAccount.getAccountId()+" is disabled")
            ).get();
        }
    }

    public IcesiAccountShowDTO getAccountByAccountNumber(String accountNumber, String icesiUserId){
        IcesiAccount icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(createIcesiException(
                        "There is no account with the number: " + accountNumber,
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "account", "the number", accountNumber)
                ));
        checkIfTheAccountBelongsToTheIcesiUser(icesiAccount, icesiUserId);
        return icesiAccountMapper.fromIcesiAccountToShowDTO(icesiAccount);
    }

    private void checkIfTheAccountBelongsToTheIcesiUser(String accountId, String icesiUserId){
        IcesiAccount icesiAccount = getAccountById(accountId);
        checkIfTheAccountBelongsToTheIcesiUser(icesiAccount, icesiUserId);
    }

    private void checkIfTheAccountBelongsToTheIcesiUser(IcesiAccount icesiAccount, String icesiUserId){
        IcesiUser icesiUser = icesiUserRepository.findById(fromIdToUUID(icesiUserId)).orElseThrow(createIcesiException(
                "There is no icesi user with the id: " + icesiUserId,
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "icesi user", "id", icesiUserId)
        ));
        if(icesiUser.getIcesiRole().getName().equals(NameIcesiRole.USER)){
            if (!icesiAccount.getIcesiUser().getUserId().equals(icesiUserId)){
                throw createIcesiException(
                        "The account does not belong to" + icesiUser.getEmail(),
                        HttpStatus.FORBIDDEN,
                        new DetailBuilder(ErrorCode.ERR_400, "The account does not belong to" + icesiUser.getEmail())
                ).get();
            }
        }
    }

    private UUID fromIdToUUID(String icesiUserId){
        try{
            return UUID.fromString(icesiUserId);
        }catch (IllegalArgumentException illegalArgumentException){
            throw createIcesiException(
                    "Invalid account id",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "id", "Invalid account id")
            ).get();
        }
    }
}
