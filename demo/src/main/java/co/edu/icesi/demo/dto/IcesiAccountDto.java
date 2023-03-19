package co.edu.icesi.demo.dto;


import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.util.UUID;
@Builder
@Data
public class IcesiAccountDto {
    private UUID accountId;

    private String accountNumber;

    private long balance;

    private String type;

    private boolean active;

    private IcesiUserDto user;
}
