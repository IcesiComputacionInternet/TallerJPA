package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.*;
import co.edu.icesi.tallerjpa.enums.NameIcesiRole;
import co.edu.icesi.tallerjpa.error.exception.DetailBuilder;
import co.edu.icesi.tallerjpa.error.exception.ErrorCode;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final IcesiUserMapper icesiUserMapper;
    private final IcesiUserRepository icesiUserRepository;

    public IcesiAccountShowDTO save(IcesiAccountCreateDTO icesiAccountCreateDTO, String icesiUserId){
        checkConditionsToCreateAccount(icesiAccountCreateDTO);

        IcesiUser icesiUser = getIcesiUserByEmail(icesiAccountCreateDTO.getIcesiUserEmail());

        checkIfTheUserISTryingToCreateAnAccountForHimself(icesiUser, icesiUserId);

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
        IcesiAccount icesiAccountSaved = icesiAccountRepository.save(icesiAccount);
        IcesiAccountShowDTO icesiAccountShowDTO = icesiAccountMapper.fromIcesiAccountToShowDTO(icesiAccountSaved);
        icesiAccountShowDTO.setIcesiUserDTO(icesiUserMapper.fromIcesiUserToShow(icesiAccountSaved.getIcesiUser()));
        return icesiAccountShowDTO;
    }

    private void checkIfTheUserISTryingToCreateAnAccountForHimself(IcesiUser icesiUserAccountOwner, String icesiUserId){
        boolean isTryingToCreateAnAccountForAnotherUser = !icesiUserAccountOwner.getUserId().toString().equals(icesiUserId);
        IcesiUser loggedIcesiUser = getIcesiUserById(icesiUserId);
        boolean isNotAnAdminRole = !loggedIcesiUser.getIcesiRole().getName().equals(NameIcesiRole.ADMIN.toString());
        if(isNotAnAdminRole && isTryingToCreateAnAccountForAnotherUser){
            throw createIcesiException(
                    "Forbidden: Only ADMIN can create accounts for others users",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403, "Forbidden: Only ADMIN can create accounts for others users")
            ).get();
        }
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
            if(icesiAccountRepository.findByAccountNumber(possibleAccountNumber).isEmpty()){
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

    @Transactional
    public IcesiAccountShowDTO enableAccount(String accountNumber, String icesiUserId){
        checkIfTheAccountBelongsToTheIcesiUser(accountNumber, icesiUserId);
        IcesiAccount icesiAccount = getAccountByAccountNumber(accountNumber);
        if(icesiAccount.isActive()){
            throw createIcesiException(
                    "The account was already enabled",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "isActive", "The account was already enabled")
            ).get();
        }
        icesiAccountRepository.enableAccount(icesiAccount.getAccountId().toString());
        return icesiAccountMapper.fromIcesiAccountToShowDTO(getAccountByAccountNumber(icesiAccount.getAccountNumber().toString()));
    }

    @Transactional
    public IcesiAccountShowDTO disableAccount(String accountNumber, String icesiUserId){
        checkIfTheAccountBelongsToTheIcesiUser(accountNumber, icesiUserId);
        IcesiAccount icesiAccount = getAccountByAccountNumber(accountNumber);
        if(icesiAccount.getBalance() != 0){
            throw createIcesiException(
                    "Account can only be disabled if the balance is 0",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "isActive and balance", "Account can only be disabled if the balance is 0")
            ).get();
        }
        icesiAccountRepository.disableAccount(icesiAccount.getAccountId().toString());
        return icesiAccountMapper.fromIcesiAccountToShowDTO(getAccountByAccountNumber(icesiAccount.getAccountNumber()));
    }

    @Transactional
    public TransactionWithOneAccountCreateDTO withdrawalMoney(TransactionWithOneAccountCreateDTO transactionWithdrawalCreateDTO, String icesiUserId){
        IcesiAccount icesiAccount = getAccountByAccountNumber(transactionWithdrawalCreateDTO.getAccountNumber());
        checkIfTheAccountBelongsToTheIcesiUser(icesiAccount, icesiUserId);
        checkIfTheAccountIsDisabled(icesiAccount);
        if(!icesiAccount.isThereEnoughMoneyToWithdraw(transactionWithdrawalCreateDTO.getAmount())){
            throw createIcesiException(
                    "Not enough money to withdraw. At most you can withdraw: " + icesiAccount.getBalance(),
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "balance", "Not enough money to withdraw. At most you can withdraw: " + icesiAccount.getBalance())
            ).get();
        }
        long newBalance = icesiAccount.getBalance() - transactionWithdrawalCreateDTO.getAmount();
        icesiAccountRepository.updateBalance(newBalance, icesiAccount.getAccountId());
        TransactionWithOneAccountCreateDTO transactionWithdrawalCreateDTOCopy = TransactionWithOneAccountCreateDTO.builder()
                .accountNumber(transactionWithdrawalCreateDTO.getAccountNumber()).build();
        transactionWithdrawalCreateDTOCopy.setAmount(newBalance);
        return transactionWithdrawalCreateDTOCopy;

    }

    private IcesiAccount getAccountById(String accountId){
        return icesiAccountRepository.findById(fromIdToUUID(accountId))
                .orElseThrow(createIcesiException(
                        "There is no account with the id: " + accountId,
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "account", "id", accountId)
                ));

    }

    public TransactionWithOneAccountCreateDTO depositMoney(TransactionWithOneAccountCreateDTO transactionWithOneAccountCreateDTO, String icesiUserId){
        IcesiAccount icesiAccount = getAccountByAccountNumber(transactionWithOneAccountCreateDTO.getAccountNumber());
        checkIfTheAccountBelongsToTheIcesiUser(icesiAccount, icesiUserId);
        checkIfTheAccountIsDisabled(icesiAccount);
        long newBalance = icesiAccount.getBalance() + transactionWithOneAccountCreateDTO.getAmount();
        icesiAccountRepository.updateBalance(newBalance, icesiAccount.getAccountId());
        TransactionWithOneAccountCreateDTO transactionWithOneAccountCreateDTOCopy = TransactionWithOneAccountCreateDTO.builder()
                .accountNumber(transactionWithOneAccountCreateDTO.getAccountNumber()).build();
        transactionWithOneAccountCreateDTOCopy.setAmount(newBalance);
        return transactionWithOneAccountCreateDTOCopy;
    }

    @Transactional
    public TransactionResultDTO transferMoney(TransactionCreateDTO transactionCreateDTO, String icesiUserId){
        checkThatTheAccountNumbersAreDifferent(transactionCreateDTO);
        IcesiAccount senderIcesiAccount = getAccountByAccountNumber(transactionCreateDTO.getSenderAccountNumber());
        IcesiAccount receiverIcesiAccount = getAccountByAccountNumber(transactionCreateDTO.getReceiverAccountNumber());
        checkIfTheAccountBelongsToTheIcesiUser(senderIcesiAccount, icesiUserId);
        checkConditionsToTransfer(senderIcesiAccount, receiverIcesiAccount, transactionCreateDTO.getAmount());
        long senderAccountNewBalance = senderIcesiAccount.getBalance() - transactionCreateDTO.getAmount();
        long receiverAccountNewBalance = receiverIcesiAccount.getBalance() + transactionCreateDTO.getAmount();
        icesiAccountRepository.updateBalance(senderAccountNewBalance, senderIcesiAccount.getAccountId());
        icesiAccountRepository.updateBalance(receiverAccountNewBalance, receiverIcesiAccount.getAccountId());
        TransactionResultDTO transactionResultDTO = icesiAccountMapper.fromTransactionCreateDTO(transactionCreateDTO);
        transactionResultDTO.setAmount(senderAccountNewBalance);
        transactionResultDTO.setResult("The transfer was successful");
        return transactionResultDTO;
    }

    private void checkThatTheAccountNumbersAreDifferent(TransactionCreateDTO transactionCreateDTO){
        if(transactionCreateDTO.getSenderAccountNumber().equals(transactionCreateDTO.getReceiverAccountNumber())){
            throw createIcesiException(
                    "The sender account number and receiver account number must be different",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "senderAccountNumber and receiverAccountNumber", "The sender account number and receiver account number must be different")
            ).get();
        }
    }

    private void checkConditionsToTransfer(IcesiAccount senderIcesiAccount, IcesiAccount receiverIcesiAccount, long moneyToTransfer){
        checkIfTheAccountIsDisabled(senderIcesiAccount);
        if(senderIcesiAccount.isMarkedAsDepositOnly()){
            throw createIcesiException(
                    "The account with number " + senderIcesiAccount.getAccountNumber() + " is marked as deposit only so it can't transfers money",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "type", "The account with number " + senderIcesiAccount.getAccountNumber() + " is marked as deposit only so no money can be transferred")
            ).get();
        }
        if(!senderIcesiAccount.isThereEnoughMoneyToWithdraw(moneyToTransfer)){
            throw createIcesiException(
                    "Not enough money to transfer. At most you can transfer: " + senderIcesiAccount.getBalance(),
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "balance", "Not enough money to transfer. At most you can transfer: " + senderIcesiAccount.getBalance())
            ).get();
        }
        checkIfTheAccountIsDisabled(receiverIcesiAccount);
        if(receiverIcesiAccount.isMarkedAsDepositOnly()){
            throw createIcesiException(
                    "The account with number " + receiverIcesiAccount.getAccountNumber() + " is marked as deposit only so no money can be transferred",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "type", "The account with number " + receiverIcesiAccount.getAccountNumber() + " is marked as deposit only so no money can be transferred")
            ).get();
        }
    }

    private void checkIfTheAccountIsDisabled(IcesiAccount icesiAccount){
        if (icesiAccount.isDisable()){
            throw createIcesiException(
                    "The account "+icesiAccount.getAccountNumber()+" is disabled",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "isActive", "The account "+icesiAccount.getAccountNumber()+" is disabled")
            ).get();
        }
    }

    private IcesiAccount getAccountByAccountNumber(String accountNumber){
        return icesiAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(createIcesiException(
                        "There is no account with number: " + accountNumber,
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "account", "number", accountNumber)
                ));
    }

    public IcesiAccountShowDTO getAccountByAccountNumber(String accountNumber, String icesiUserId){
        IcesiAccount icesiAccount = getAccountByAccountNumber(accountNumber);
        checkIfTheAccountBelongsToTheIcesiUser(icesiAccount, icesiUserId);
        return icesiAccountMapper.fromIcesiAccountToShowDTO(icesiAccount);
    }

    private void checkIfTheAccountBelongsToTheIcesiUser(String accountNumber, String icesiUserId){
        IcesiAccount icesiAccount = getAccountByAccountNumber(accountNumber);
        checkIfTheAccountBelongsToTheIcesiUser(icesiAccount, icesiUserId);
    }

    private void checkIfTheAccountBelongsToTheIcesiUser(IcesiAccount icesiAccount, String icesiUserId){
        IcesiUser icesiUser = getIcesiUserById(icesiUserId);
        boolean isNotAnAdminRole = !icesiUser.getIcesiRole().getName().equals(NameIcesiRole.ADMIN.toString());
        boolean theAccountDoesNotBelongToTheUser = !icesiAccount.getIcesiUser().getUserId().toString().equals(icesiUserId);
        if (isNotAnAdminRole && theAccountDoesNotBelongToTheUser){
            throw createIcesiException(
                    "The account does not belong to" + icesiUser.getEmail(),
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403, "The account does not belong to " + icesiUser.getEmail())
            ).get();
        }
    }

    private IcesiUser getIcesiUserById(String icesiUserId){
        return icesiUserRepository.findById(fromIdToUUID(icesiUserId)).orElseThrow(createIcesiException(
                "There is no icesi user with the id: " + icesiUserId,
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "icesi user", "id", icesiUserId)
        ));
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

    private IcesiUser getIcesiUserByEmail(String email){
        return icesiUserRepository.findByEmail(email)
                .orElseThrow(createIcesiException(
                        "There is no user with the email "+email,
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "Icesi user", "email", email)
                ));
    }
}
