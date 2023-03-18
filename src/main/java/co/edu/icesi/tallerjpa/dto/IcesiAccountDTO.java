package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiAccountDTO {

    private Long balance;

    private String type;

    private boolean active;

    private String icesiUserEmail;

    private String userId;

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getIcesiUserEmail() {
        return icesiUserEmail;
    }

    public void setIcesiUserEmail(String icesiUserEmail) {
        this.icesiUserEmail = icesiUserEmail;
    }
}
