package co.edu.icesi.demo.dto;

import co.edu.icesi.demo.validation.constraint.TransactionAccountConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TransactionAccountConstraint
public class TransactionDTO {

    private String accountNumberFrom;

    private String accountNumberTo;

    @Min(value=0, message = "min value is 0")
    @Max(value=1000000000L)
    private long money;

    private String result;


}
