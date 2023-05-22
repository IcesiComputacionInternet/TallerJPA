package co.edu.icesi.tallerjpa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class IcesiUser {

    @Id
    private UUID userId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    @JsonIgnoreProperties("icesiUserLint")
    @ManyToOne
    @JoinColumn(name = "icesi_role_role_id")
    private IcesiRole icesirole;

    @OneToMany(mappedBy = "accountId")
    private List<IcesiAccount> accounts;

    public UUID getId() {
        return userId;
    }

    public void setId(UUID userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public IcesiRole getIcesirole() {
        return icesirole;
    }

    public void setIcesirole(IcesiRole icesirole) {
        this.icesirole = icesirole;
    }

    public List<IcesiAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<IcesiAccount> accounts) {
        this.accounts = accounts;
    }



}
