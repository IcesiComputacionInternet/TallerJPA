package co.com.icesi.icesiAccountSystem.model;
import co.com.icesi.icesiAccountSystem.enums.AccountType;
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
    @ManyToOne
    @JoinColumn(name="icesi_user_user_id", nullable=false)
    private IcesiUser user;
    @Id
    private UUID accountId;
    @Column(unique=true)
    private String accountNumber;
    private long balance;
    private AccountType type;
    private boolean active;
}
