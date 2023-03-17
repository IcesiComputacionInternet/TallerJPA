package com.edu.icesi.TallerJPA.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

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
    @JoinColumn(name = "icesi_user_user_id", nullable = false)
    private IcesiUser icesiUser;
}
