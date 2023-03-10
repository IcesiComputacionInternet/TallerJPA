package co.edu.icesi.tallerjpa.runableartefact.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@Entity
public class IcesiAccount {

    @Id
    private UUID accountId;

    private String accountNumber;

    private Long balance;

    private String type;

    private boolean active;

    @ManyToOne(cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser user;
}
