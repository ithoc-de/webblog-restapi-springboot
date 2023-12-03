package de.ithoc.webblog.restapi.api;

import de.ithoc.webblog.restapi.services.AuthService;
import de.ithoc.webblog.restapi.model.Article;
import de.ithoc.webblog.restapi.persistence.ArticleEntity;
import de.ithoc.webblog.restapi.persistence.ArticleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class ArticlesRestController {

    private final ModelMapper modelMapper;
    private final ArticleRepository articleRepository;
    private final AuthService authService;

    public ArticlesRestController(ModelMapper modelMapper,
                                  ArticleRepository articleRepository,
                                  AuthService authService) {
        this.modelMapper = modelMapper;
        this.articleRepository = articleRepository;
        this.authService = authService;
    }

    @Operation(operationId = "articlesGet",
            summary = "Get all articles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Articles returned"),
                    @ApiResponse(responseCode = "401", description = "Not authorized")
            })
    @GetMapping(value = "/articles", consumes = {"application/json"})
    public ResponseEntity<List<Article>> articlesGet(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        log.trace("articlesGet called");

        if (!authService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }

        List<ArticleEntity> all = articleRepository.findAll();
        List<Article> articles = all.stream()
                .map(articleEntity -> modelMapper.map(articleEntity, Article.class))
                .toList();

        return ResponseEntity.ok(articles);
    }

    @Operation(operationId = "articlesGet",
            summary = "Get all articles",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Article created"),
                    @ApiResponse(responseCode = "401", description = "Not authorized")
            })
    @PostMapping(value = "/articles", consumes = {"application/json"})
    public ResponseEntity<Void> articlesPost(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token, Article article) {
        log.trace("articlesPost called");

        if (!authService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }

        ArticleEntity articleEntity = modelMapper.map(article, ArticleEntity.class);
        articleRepository.save(articleEntity);

        return ResponseEntity.status(201).build();
    }

    @Operation(operationId = "articlesGet",
            summary = "Get all articles",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Article deleted"),
                    @ApiResponse(responseCode = "401", description = "Not authorized")
            })
    @DeleteMapping(value = "/articles", consumes = {"application/json"})
    public ResponseEntity<Void> articlesArticleIdDelete(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token, String articleId) {
        log.trace("articlesArticleIdDelete called");

        if (!authService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }

        articleRepository.deleteById(UUID.fromString(articleId));

        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "articlesGet",
            summary = "Get all articles",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Article updated"),
                    @ApiResponse(responseCode = "401", description = "Not authorized"),
                    @ApiResponse(responseCode = "404", description = "Article not found")
            })
    @PutMapping(value = "/articles", consumes = {"application/json"})
    public ResponseEntity<Void> articlesArticleIdPut(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token, String articleId, Article article) {
        log.trace("articlesArticleIdPut called");

        if (!authService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }

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

}
