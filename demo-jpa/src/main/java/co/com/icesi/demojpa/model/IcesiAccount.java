package co.com.icesi.demojpa.model;

import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@AllArgsConstructor
public class IcesiAccount {
    @Id
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private String type;
    private boolean active;

    @ManyToOne
    private IcesiUser user;
}
