package co.com.icesi.TallerJPA.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccount {
    @Id
    private UUID accountId;

    private String accountNumber;

    private Long balance;

    private String type;

    public  boolean active;

    @ManyToOne
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser user;
}
