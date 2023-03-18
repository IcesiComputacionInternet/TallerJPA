package com.example.demo.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class IcesiAccount {

    @Id
    private UUID accountId;

    private String accountNumber;

    private long balance;

    private String type;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser icesiUser;

}
