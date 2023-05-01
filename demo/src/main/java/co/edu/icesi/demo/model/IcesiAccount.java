package co.edu.icesi.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
