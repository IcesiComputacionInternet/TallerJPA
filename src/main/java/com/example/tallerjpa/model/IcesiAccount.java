package com.example.tallerjpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccount {

    @Id
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private String type;
    private String active;


}
