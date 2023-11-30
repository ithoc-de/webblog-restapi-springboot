package de.ithoc.webblog.restapi.api;

import de.ithoc.webblog.restapi.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class ArticlesRestController implements ArticlesApi {

    @Override
    public ResponseEntity<List<Article>> articlesGet() {
        log.trace("articlesGet called");

        String author = UUID.randomUUID().toString();
        List<Article> articles = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            articles.add(createArticle(author));
        }

        return ResponseEntity.ok(articles);
    }

    private static Article createArticle(String author) {
        log.trace("createArticle called");

        Article article = new Article();
        article.setId(UUID.randomUUID());
        article.setTitle(UUID.randomUUID().toString());
        article.setContent(UUID.randomUUID().toString());
        article.setAuthor(author);

        return article;
    }

}
