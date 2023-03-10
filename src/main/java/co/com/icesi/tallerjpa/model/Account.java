package co.com.icesi.tallerjpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
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
