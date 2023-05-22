package co.com.icesi.demojpa.model;

import co.com.icesi.demojpa.enums.IcesiAccountType;
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
    private UUID accountId;

    private String accountNumber;

    @Column(name = "balance", nullable = false, columnDefinition = "BIGINT CHECK (balance >= 0)")
    private Long balance;
    private IcesiAccountType type;
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name="icesi_user_user_id")
    private IcesiUser user;

}
