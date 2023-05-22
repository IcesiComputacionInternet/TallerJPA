package com.Icesi.TallerJPA.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiAccountDTO {
    private Long balance;
    private String accountNumber;
    private String type;
    private boolean active;
}
