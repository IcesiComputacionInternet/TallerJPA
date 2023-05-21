package com.example.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Validated
@Builder
public class TransferRequestDTO {
    @NotBlank
    private String originAccountNumber;
    @NotBlank
    private String destinationAccountNumber;
    @Min(value = 0)
    @NotNull
    private long amount;
}
