package de.ithoc.webblog.restapi.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "rating")
public class RatingEntity {

    @Getter
    @Id
    @GeneratedValue
    private UUID id;

    private Integer stars;
    private String author;

}
