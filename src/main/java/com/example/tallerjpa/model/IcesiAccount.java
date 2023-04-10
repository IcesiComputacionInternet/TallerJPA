package com.example.tallerjpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccount {

    @Id
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private AccountType type;
    private boolean active;

    @ManyToOne
    @JoinColumn (name = "icesi_user_id")
    private IcesiUser icesiUser;


}
