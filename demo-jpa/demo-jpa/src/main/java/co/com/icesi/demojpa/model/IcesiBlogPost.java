package co.com.icesi.demojpa.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class IcesiBlogPost {

    @Id
    private UUID postId;
    private String content;
    private String title;

    @ManyToOne
    @JoinColumn(name="icesi_user_user_id", nullable = false)
    private IcesiUser icesiUser;

    @ManyToMany(mappedBy = "blogPosts")
    private List<Category> categories;

}
