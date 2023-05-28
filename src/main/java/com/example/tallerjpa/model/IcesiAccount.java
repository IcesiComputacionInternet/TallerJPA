package com.example.tallerjpa.model;

import com.example.tallerjpa.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn (name = "icesi_user_id")
    private IcesiUser icesiUser;


}
