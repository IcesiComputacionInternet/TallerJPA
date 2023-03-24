package co.edu.icesi.tallerjpa.model;

import co.edu.icesi.tallerjpa.enums.TypeIcesiAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccount {
    @Id
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private String type;
    private boolean active;
    @NotNull(message = "The icesi user can not be null")
    @ManyToOne
    @JoinColumn(name = "icesiUser_userId", nullable = false)
    private IcesiUser icesiUser;

    public boolean isMarkedAsDepositOnly(){
        if(type.equals(TypeIcesiAccount.DEPOSIT_ONLY.toString())){
            return true;
        }else {
            return false;
        }
    }

    public boolean isThereEnoughMoney(long necessaryMoney){
        if(balance - necessaryMoney < 0){
            return false;
        }
        return true;
    }

    public boolean isDisable(){
        return !active;
    }
}
