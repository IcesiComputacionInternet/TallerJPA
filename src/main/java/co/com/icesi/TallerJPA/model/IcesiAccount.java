package co.com.icesi.TallerJPA.model;

import co.com.icesi.TallerJPA.Enum.AccountType;
import lombok.*;

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
    private Long balance;
    private AccountType type;
    private boolean active;

    //Muchas cuentas pertenecen a un solo usuario
    @ManyToOne
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser user;



}
