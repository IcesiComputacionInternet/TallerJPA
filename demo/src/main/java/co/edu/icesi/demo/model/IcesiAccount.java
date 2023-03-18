package co.edu.icesi.demo.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
public class IcesiAccount {

    @Id
    private UUID accountId;

    @Column(unique=true)
    private String accountNumber;

    private long balance;

    private String type;

    private boolean active;

    @ManyToOne
    @JoinColumn(name="icesi_user_user_id", nullable = false)
    private IcesiUser user;

}
