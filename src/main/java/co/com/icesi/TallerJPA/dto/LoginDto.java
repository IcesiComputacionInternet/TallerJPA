package co.com.icesi.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
