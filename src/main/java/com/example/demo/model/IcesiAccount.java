package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
public class IcesiAccount {
    @Id
    private UUID accountId;
    @Column(unique = true)
    private String accountNumber;
    private Long balance;
    private String type;
    private boolean active;

    @ManyToOne
    private IcesiUser icesiUser;
}
