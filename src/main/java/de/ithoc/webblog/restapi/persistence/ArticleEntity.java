package de.ithoc.webblog.restapi.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "article")
public class ArticleEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String content;
    private String author;


    @OneToMany(cascade = CascadeType.ALL)
    private List<CommentEntity> comments;

}
