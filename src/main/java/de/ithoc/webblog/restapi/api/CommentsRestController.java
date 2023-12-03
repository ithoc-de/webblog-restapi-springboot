package de.ithoc.webblog.restapi.api;

import de.ithoc.webblog.restapi.persistence.ArticleEntity;
import de.ithoc.webblog.restapi.services.AuthService;
import de.ithoc.webblog.restapi.model.Comment;
import de.ithoc.webblog.restapi.persistence.ArticleRepository;
import de.ithoc.webblog.restapi.persistence.CommentEntity;
import de.ithoc.webblog.restapi.persistence.CommentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class CommentsRestController {

    private final ModelMapper modelMapper;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;

    public CommentsRestController(ModelMapper modelMapper,
                                  ArticleRepository articleRepository,
                                  CommentRepository commentRepository,
                                  AuthService authService) {
        this.modelMapper = modelMapper;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.authService = authService;
    }

    @Operation(operationId = "articlesArticleIdCommentsPost",
            summary = "Create a comment (registered users only)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Comment created"),
                    @ApiResponse(responseCode = "401", description = "Not authorized"),
                    @ApiResponse(responseCode = "404", description = "Article not found")
            })
    @PostMapping(value = "/comments/{articleId}", consumes = {"application/json"})
    public ResponseEntity<Void> articlesArticleIdCommentsPost(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String articleId, Comment comment) {
        log.trace("articlesArticleIdCommentsPost called");

        if (!authService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }

        Optional<ArticleEntity> articleEntityOptional = articleRepository.findById(UUID.fromString(articleId));
        if (articleEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ArticleEntity articleEntity = articleEntityOptional.get();

        CommentEntity commentEntity = modelMapper.map(comment, CommentEntity.class);
        commentRepository.save(commentEntity);
        articleEntity.getComments().add(commentEntity);
        articleRepository.save(articleEntity);

        return ResponseEntity.status(201).build();
    }

    @Operation(operationId = "articlesArticleIdCommentsCommentIdDelete",
            summary = "Delete a comment (administrators only)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Comment deleted"),
                    @ApiResponse(responseCode = "401", description = "Not authorized"),
                    @ApiResponse(responseCode = "404", description = "Article not found")
            })
    @DeleteMapping(value = "/comments/{articleId}/{commentId}", consumes = {"application/json"})
    public ResponseEntity<Void> articlesArticleIdCommentsCommentIdDelete(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String articleId, @PathVariable String commentId) {
        log.trace("articlesArticleIdCommentsCommentIdDelete called");

        if (!authService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }

        Optional<ArticleEntity> articleEntityOptional = articleRepository.findById(UUID.fromString(articleId));
        if (articleEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ArticleEntity articleEntity = articleEntityOptional.get();

        articleEntity.getComments()
                .removeIf(commentEntity -> commentEntity.getId().equals(UUID.fromString(commentId)));
        articleRepository.save(articleEntity);

        return ResponseEntity.status(204).build();
    }

}
