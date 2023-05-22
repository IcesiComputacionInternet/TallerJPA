package co.com.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class JwtResponse {
    private String token;
}
