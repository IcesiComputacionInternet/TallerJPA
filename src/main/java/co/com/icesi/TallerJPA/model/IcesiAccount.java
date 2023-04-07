package co.com.icesi.TallerJPA.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String type;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser user;
}
