package de.ithoc.webblog.restapi.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String content;
    private String author;

}
