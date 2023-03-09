package co.com.icesi.TallerJPA.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class IcesiAccount {
    @Id
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private String type;
    private boolean active;

    //Muchas cuentas pertenecen a un solo usuario
    @ManyToOne
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser user;



}
