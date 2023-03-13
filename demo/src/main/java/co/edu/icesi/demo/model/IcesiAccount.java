package co.edu.icesi.demo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
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
}
