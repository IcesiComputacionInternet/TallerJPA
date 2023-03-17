package co.edu.icesi.tallerjpa.model;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class IcesiAccount {
    @Id
    private UUID accountId;

    private String accountNumber;

    private long balance;

    private String type;

    private boolean active;

    private IcesiUser user;

}
