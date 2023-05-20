package co.com.icesi.TallerJpa.model;

import co.com.icesi.TallerJpa.enums.AccountType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IcesiAccount {
    @Id
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private AccountType type;
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser icesiUser;
}
