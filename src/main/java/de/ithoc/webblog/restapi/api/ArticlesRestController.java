package de.ithoc.webblog.restapi.api;

import de.ithoc.webblog.restapi.model.Article;
import de.ithoc.webblog.restapi.model.Comment;
import de.ithoc.webblog.restapi.model.Rating;
import de.ithoc.webblog.restapi.persistence.ArticleEntity;
import de.ithoc.webblog.restapi.persistence.ArticleRepository;
import de.ithoc.webblog.restapi.persistence.CommentEntity;
import de.ithoc.webblog.restapi.persistence.CommentRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class ArticlesRestController implements ArticlesApi {

    private final ModelMapper modelMapper;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public ArticlesRestController(ModelMapper modelMapper,
                                  ArticleRepository articleRepository,
                                  CommentRepository commentRepository) {
        this.modelMapper = modelMapper;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    @PostConstruct
    private void init() {
        log.trace("init called");

        String author = UUID.randomUUID().toString();
        for (int i = 0; i < 2; i++) {
            List<CommentEntity> comments = new ArrayList<>();
            for(int j = 0; j < 2; j++) {
                comments.add(commentRepository.save(createComment()));
            }
            ArticleEntity articleEntity = createArticle(author, comments);
            articleRepository.save(articleEntity);
         }
    }

    @Override
    public ResponseEntity<List<Article>> articlesGet() {
        log.trace("articlesGet called");

        List<ArticleEntity> all = articleRepository.findAll();
        List<Article> articles = all.stream()
                .map(articleEntity -> modelMapper.map(articleEntity, Article.class))
                .toList();

        return ResponseEntity.ok(articles);
    }

    @Override
    public ResponseEntity<Void> articlesPost(Article article) {
        log.trace("articlesPost called");

        ArticleEntity articleEntity = modelMapper.map(article, ArticleEntity.class);
        articleRepository.save(articleEntity);

        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> articlesArticleIdDelete(String articleId) {
        log.trace("articlesArticleIdDelete called");

        // TODO Delete comments first

        articleRepository.deleteById(UUID.fromString(articleId));

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> articlesArticleIdPut(String articleId, Article article) {
        log.trace("articlesArticleIdPut called");

        Optional<ArticleEntity> byId = articleRepository.findById(UUID.fromString(articleId));
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        byId.get().setTitle(article.getTitle());
        byId.get().setContent(article.getContent());
        byId.get().setAuthor(article.getAuthor());
        articleRepository.save(byId.get());

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> articlesArticleIdCommentsPost(String articleId, Comment comment) {
        log.trace("articlesArticleIdCommentsPost called");

        articleRepository.findById(UUID.fromString(articleId))
                .ifPresent(articleEntity -> {
                    CommentEntity commentEntity = modelMapper.map(comment, CommentEntity.class);
                    commentRepository.save(commentEntity);
                    articleEntity.getComments().add(commentEntity);
                    articleRepository.save(articleEntity);
                });

        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> articlesArticleIdCommentsCommentIdDelete(String articleId, String commentId) {
        log.trace("articlesArticleIdCommentsCommentIdDelete called");

        // TODO Delete comment

        return ArticlesApi.super.articlesArticleIdCommentsCommentIdDelete(articleId, commentId);
    }

    @Override
    public ResponseEntity<Void> articlesArticleIdRatingsPost(String articleId, Rating rating) {
        log.trace("articlesArticleIdRatingsPost called");

        // TODO Add rating

        return ArticlesApi.super.articlesArticleIdRatingsPost(articleId, rating);
    }

    @Override
    public ResponseEntity<Void> articlesArticleIdRatingsRatingIdDelete(String articleId, String ratingId) {
        log.trace("articlesArticleIdRatingsRatingIdDelete called");

        // TODO Delete rating

        return ArticlesApi.super.articlesArticleIdRatingsRatingIdDelete(articleId, ratingId);
    }

    private static CommentEntity createComment() {
        log.trace("createComment called");

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(UUID.randomUUID().toString());
        commentEntity.setAuthor(UUID.randomUUID().toString());

        return commentEntity;
    }

    private static ArticleEntity createArticle(String author, List<CommentEntity> comments) {
        log.trace("createArticle called");

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle(UUID.randomUUID().toString());
        articleEntity.setContent(UUID.randomUUID().toString());
        articleEntity.setAuthor(author);
        articleEntity.setComments(comments);

        return articleEntity;
    }

}
