package de.ithoc.webblog.restapi.persistence;

import de.ithoc.webblog.restapi.model.Article;
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
@Entity(name = "article")
public class ArticleEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String content;
    private String author;

    public ArticleEntity(Article article) {
        super();
        this.setId(article.getId());
        this.setTitle(article.getTitle());
        this.setContent(article.getContent());
        this.setAuthor(article.getAuthor());
    }

    public Article toApiArticle() {
        Article article = new Article();
        article.setId(this.getId());
        article.setTitle(this.getTitle());
        article.setContent(this.getContent());
        article.setAuthor(this.getAuthor());
        return article;
    }

}
