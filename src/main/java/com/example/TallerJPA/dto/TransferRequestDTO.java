package com.example.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TransferRequestDTO {
    @NotBlank
    private String originAccountNumber;
    @NotBlank
    private String destinationAccountNumber;
    @Min(value = 0)
    private long amount;
}
