package co.edu.icesi.tallerjpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.transaction.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccount {
    @Id
    private UUID accountId;
    @javax.persistence.Id
    private Long id;
    private String accountNumber;
    private long balance;
    private String type;
    private boolean active;

    @ManyToOne(cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser user;

    public void setAccountOwner(IcesiUser user) {
        this.user = user;
    }


    private List<Transaction> transactions = new ArrayList<>();
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
