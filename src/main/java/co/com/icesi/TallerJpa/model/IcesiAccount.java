package co.com.icesi.TallerJpa.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IcesiAccount {
    @Id
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private String type;
    private boolean active;
    @ManyToOne
    private IcesiUser icesiUser;
}
