package co.com.icesi.TallerJpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiAccountCreateDTO {
    private long balance;
    private String type;
}
