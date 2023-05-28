package com.example.demo.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.demo.model.enums.TypeIcesiAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccount {

    @Id
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private TypeIcesiAccount type;
    private boolean active;

    @ManyToOne(optional = false)
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser icesiUser;
}
