package co.edu.icesi.tallerJPA.model;


import co.edu.icesi.tallerJPA.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private UUID id;

    private String accountNumber;

    private long balance;

    private boolean active;

    private AccountType type;

    @ManyToOne
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser user;
}
