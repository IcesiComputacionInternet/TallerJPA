package co.com.icesi.tallerjpa.model;

import co.com.icesi.tallerjpa.enums.TypeAccount;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private UUID accountId;
    private String accountNumber;
    private Long balance;
    private TypeAccount type;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "icesi_user_user_id")
    @ToString.Exclude
    private IcesiUser user;
}
