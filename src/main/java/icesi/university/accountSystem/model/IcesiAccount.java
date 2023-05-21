package icesi.university.accountSystem.model;

import icesi.university.accountSystem.enums.TypeAccount;
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

    private TypeAccount type;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private IcesiUser user;
}
