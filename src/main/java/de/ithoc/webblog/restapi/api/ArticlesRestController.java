package de.ithoc.webblog.restapi.api;

import de.ithoc.webblog.restapi.model.Article;
import de.ithoc.webblog.restapi.persistence.ArticleEntity;
import de.ithoc.webblog.restapi.persistence.ArticleRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class ArticlesRestController implements ArticlesApi {

    private final ArticleRepository articleRepository;

    public ArticlesRestController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PostConstruct
    private void init() {
        log.trace("init called");

        String author = UUID.randomUUID().toString();
        List<ArticleEntity> articleEntities = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            articleEntities.add(createArticle(author));
        }
        articleRepository.saveAll(articleEntities);
    }

    @Override
    public ResponseEntity<List<Article>> articlesGet() {
        log.trace("articlesGet called");

        List<ArticleEntity> all = articleRepository.findAll();
        List<Article> articles = all.stream().map(ArticleEntity::toApiArticle).toList();

        return ResponseEntity.ok(articles);
    }

    @Override
    public ResponseEntity<Void> articlesPost(Article article) {
        log.trace("articlesPost called");

        ArticleEntity articleEntity = new ArticleEntity(article);
        articleRepository.save(articleEntity);

        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> articlesArticleIdDelete(String articleId) {
        log.trace("articlesArticleIdDelete called");

        Optional<ArticleEntity> byId = articleRepository.findById(UUID.fromString(articleId));
        articleRepository.deleteById(UUID.fromString(articleId));

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> articlesArticleIdPut(String articleId, Article article) {
        return ArticlesApi.super.articlesArticleIdPut(articleId, article);
    }

    private static ArticleEntity createArticle(String author) {
        log.trace("createArticle called");

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setId(UUID.randomUUID());
        articleEntity.setTitle(UUID.randomUUID().toString());
        articleEntity.setContent(UUID.randomUUID().toString());
        articleEntity.setAuthor(author);

        return articleEntity;
    }

}
