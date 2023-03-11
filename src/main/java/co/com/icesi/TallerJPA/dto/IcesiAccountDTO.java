package co.com.icesi.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccountDTO {
    private String accountNumber;

    private Long balance;

    private String type;

    public  boolean active;

}
