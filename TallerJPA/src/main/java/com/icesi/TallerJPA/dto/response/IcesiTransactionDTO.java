package com.icesi.TallerJPA.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiTransactionDTO {

    @NotBlank
    private String origin;
    private String destination;
    @Min(value = 0, message = "Amount must be greater than 0")
    private Long amount;
    private String message;
}
