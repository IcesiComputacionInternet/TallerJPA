package com.example.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccount {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private long balance;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser user;

}
