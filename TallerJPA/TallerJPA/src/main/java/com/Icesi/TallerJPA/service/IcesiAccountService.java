package com.Icesi.TallerJPA.service;

import com.Icesi.TallerJPA.dto.IcesiAccountDTO;
import com.Icesi.TallerJPA.dto.IcesiTransactionsDTO;
import com.Icesi.TallerJPA.enums.ErrorConstants;
import com.Icesi.TallerJPA.mapper.IcesiAccountMapper;
import com.Icesi.TallerJPA.model.IcesiAccount;
import com.Icesi.TallerJPA.repository.IcesiAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;



@Service
@AllArgsConstructor
public class IcesiAccountService {

    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiAccountMapper icesiAccountMapper;


    public IcesiAccount save(IcesiAccountDTO icesiAccountCreateDto) {
        icesiAccountCreateDto.setAccountNumber(generateAccountNumbers());
        if (icesiAccountRepository.findByAccountNumber(icesiAccountCreateDto.getAccountNumber()).isPresent()) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_06.getMessage()));
        }
        if (accountBalance(icesiAccountCreateDto.getBalance())) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_14.getMessage()));
        }
        IcesiAccount icesiAccount = icesiAccountMapper.fromIcesiAccountDTO(icesiAccountCreateDto);
        icesiAccount.setAccountId(UUID.randomUUID());

        return icesiAccountRepository.save(icesiAccount);
    }

    private void validInformation(IcesiAccountDTO icesiAccountCreateDto) {
        if (icesiAccountRepository.findByAccountNumber(icesiAccountCreateDto.getAccountNumber()).isPresent()) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_06.getMessage()));
        }
        if (accountBalance(icesiAccountCreateDto.getBalance())) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_07.getMessage()));
        }
    }


    public String generateAccountNumbers() {
        return generateNumberAccount(3).get() + "-" + generateNumberAccount(6).get() + "-" + generateNumberAccount(2).get();
    }

    public Supplier<String> generateNumberAccount(int length) {
        return () -> generateParts(length);
    }


    public boolean accountBalance(long balance) {
        return 0 > balance;
    }

    public IcesiAccountDTO enableAccount(String accountNumber){
        boolean foundAccount = icesiAccountRepository.findByAccountNumber(accountNumber).isPresent();
        if(!foundAccount){
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_08.getMessage()));
        }
        icesiAccountRepository.enableAccount(accountNumber);
        return IcesiAccountDTO.builder().accountNumber(accountNumber).active(true).build();
    }


    public IcesiAccountDTO disableAccount(String accountNumber){
        boolean foundAccount = icesiAccountRepository.findByAccountNumber(accountNumber).isPresent();
        if(!foundAccount){
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_08.getMessage()));
        }
        icesiAccountRepository.disableAccount(accountNumber);
        return IcesiAccountDTO.builder().accountNumber(accountNumber).build();
    }


    public String generateParts(int size) {

        StringBuilder idAccount = new StringBuilder();
        for (int i = 0; i < size; i++) {
            Random random = new Random();
            idAccount.append(random.nextInt(10));
        }

        return idAccount.toString();
    }

    public void withdrawals(IcesiAccountDTO icesiAccountDTO, String numberAccount, int value) {
        boolean accountExists =icesiAccountRepository.findByAccountNumber(numberAccount).isPresent();
        if (!accountExists) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_08.getMessage()));
        }
        long balance = icesiAccountDTO.getBalance();
        if (balance < value) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_10.getMessage()));
        }
        icesiAccountDTO.setBalance(balance - value);
    }

    public void depositCash(IcesiAccountDTO account, String numberAccount, int depositCash) {
        boolean accountExists = icesiAccountRepository.findByAccountNumber(numberAccount).isPresent();
        if (!accountExists) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_08.getMessage()));
        }
        if (depositCash <= 0) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_12.getMessage()));
        }
        account.setBalance(account.getBalance() + depositCash);
    }

    public void sendMoney(IcesiAccountDTO sourceAccount, IcesiAccountDTO destinationAccount, int cashToTransfer) {
        if (cashToTransfer <= 0) {throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_12.getMessage()));}

        boolean existAccountSource = icesiAccountRepository.findByAccountNumber(sourceAccount.getAccountNumber()).isPresent();
        boolean existAccountDestination = icesiAccountRepository.findByAccountNumber(destinationAccount.getAccountNumber()).isPresent();

        if (!existAccountSource && !existAccountDestination) {throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_08.getMessage()));}
        validateAccountTypeOrigin(sourceAccount, destinationAccount);
        if (sourceAccount.getBalance() < cashToTransfer) {throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_10.getMessage()));}
        sourceAccount.setBalance(sourceAccount.getBalance() - cashToTransfer);
        destinationAccount.setBalance(destinationAccount.getBalance() + cashToTransfer);

    }

    private void validateAccountTypeOrigin(IcesiAccountDTO icesiAccountCreateDto, IcesiAccountDTO destinationAccount) {
        icesiAccountRepository.findByAccountNumber(icesiAccountCreateDto.getAccountNumber()).orElseThrow(() -> new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_08.getMessage())));
        icesiAccountRepository.findByAccountNumber(destinationAccount.getAccountNumber()).orElseThrow(() -> new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_08.getMessage())));

        if (icesiAccountCreateDto.getType().equalsIgnoreCase("Deposit Only") || destinationAccount.getType().equalsIgnoreCase("Deposit Only")) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_13.getMessage()));
        }


    }

}
