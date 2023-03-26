package com.example.jpa.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccount {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private Long balance;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "icesi_user_user_id")
    private IcesiUser user;

}
