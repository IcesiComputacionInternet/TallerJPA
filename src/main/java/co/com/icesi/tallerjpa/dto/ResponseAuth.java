package co.com.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseAuth {
    private String token;
    private ResponseUserDTO user;
}
