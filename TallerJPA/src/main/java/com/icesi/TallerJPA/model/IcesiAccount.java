package com.icesi.TallerJPA.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiAccount {

    @Id
    private UUID accountId;

    private String accountNumber;
    private long balance;
    private String type;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "icesi_user_id")
    private IcesiUser icesiUser;
}
