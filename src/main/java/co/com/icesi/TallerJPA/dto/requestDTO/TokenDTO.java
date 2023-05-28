package co.com.icesi.TallerJPA.dto.requestDTO;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    @NotNull
    @NotBlank
    private String token;
}
