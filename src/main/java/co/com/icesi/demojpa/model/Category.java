package co.com.icesi.demojpa.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name="CATEGORY")
public class Category {

    @Id
    private UUID categoryId;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "category")
    private List<IcesiBlogPost> icesiBlogPosts;

}
