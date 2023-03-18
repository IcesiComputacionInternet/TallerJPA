package co.com.icesi.jpataller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
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
}
