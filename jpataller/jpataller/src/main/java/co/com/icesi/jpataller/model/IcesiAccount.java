package co.com.icesi.jpataller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IcesiAccount {

    private UUID accountId;

    private String accountNumber;

    private long balance;

    private String type;

    private boolean active;

    @ManyToOne
    @JoinColumn(name="icesi_user_user_id", nullable = false)
    private IcesiUser accountOwner;
}
