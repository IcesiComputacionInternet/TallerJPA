package co.com.icesi.TallerJPA.dto.response;

import lombok.Builder;
import lombok.Data;


@Builder
public record AccountsDTO (
     String accountNumber,
     long balance
){
}
