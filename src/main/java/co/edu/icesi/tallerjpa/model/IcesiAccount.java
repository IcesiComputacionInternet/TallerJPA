package co.edu.icesi.tallerjpa.model;

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
    private long balance;
    private String type;
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "IcesiUser_userId", nullable = false)
    private IcesiUser icesiUser;
}
