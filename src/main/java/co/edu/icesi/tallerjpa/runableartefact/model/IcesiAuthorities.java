package co.edu.icesi.tallerjpa.runableartefact.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiAuthorities {


    @Id
    @GeneratedValue
    private Long authorityId;

    private String authority;
}
