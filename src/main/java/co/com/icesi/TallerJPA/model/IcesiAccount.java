package co.com.icesi.TallerJPA.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class IcesiAccount {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private IcesiUser user;
    @Id
    private UUID accountId;
    private String accountNumber;
    private Long balance;
    private String type;
    private boolean active;
}
