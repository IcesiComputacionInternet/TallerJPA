package co.com.icesi.demojpa.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Category {

    @Id
    private UUID categoryId;
    private String name;
    private String description;

    @ManyToMany
    @JoinTable(
            name="category_blogpost",
            joinColumns = @JoinColumn(name="category_category_id"),
            inverseJoinColumns= @JoinColumn(name = "icesi_blog_post_post_id")
    )
    private List<IcesiBlogPost> blogPosts;
}
