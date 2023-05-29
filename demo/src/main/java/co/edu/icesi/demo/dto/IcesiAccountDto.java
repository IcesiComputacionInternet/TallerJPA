package co.edu.icesi.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.UUID;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccountDto {
    private UUID accountId;

    private String accountNumber;

    private long balance;

    private String type;

    private boolean active;

    private IcesiUserDto user;
}
