package com.icesi.TallerJPA.model;

import com.icesi.TallerJPA.enums.IcesiAccountType;
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

    @Column(name = "balance", nullable = false, columnDefinition = "BIGINT CHECK (balance >= 0)")
    private long balance;
    private IcesiAccountType type;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "icesi_user_id")
    private IcesiUser icesiUser;
}
