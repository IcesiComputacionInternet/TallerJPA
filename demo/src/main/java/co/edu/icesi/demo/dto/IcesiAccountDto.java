package co.edu.icesi.demo.dto;

import co.edu.icesi.demo.model.IcesiUser;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Data
public class IcesiAccountDto {
    private UUID accountId;

    private String accountNumber;

    private long balance;

    private String type;

    private boolean active;

    private IcesiUserDto user;
}
