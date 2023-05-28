package co.edu.icesi.tallerjpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class IcesiTokenDTO {
    private String token;
}
