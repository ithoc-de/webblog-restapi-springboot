package de.ithoc.webblog.restapi.api;

import de.ithoc.webblog.restapi.services.AuthService;
import de.ithoc.webblog.restapi.model.Rating;
import de.ithoc.webblog.restapi.persistence.ArticleEntity;
import de.ithoc.webblog.restapi.persistence.ArticleRepository;
import de.ithoc.webblog.restapi.persistence.RatingEntity;
import de.ithoc.webblog.restapi.persistence.RatingRepository;
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
@CrossOrigin(origins = "*")
public class RatingsController {

    private final ModelMapper modelMapper;
    private final ArticleRepository articleRepository;
    private final RatingRepository ratingRepository;
    private final AuthService authService;

    public RatingsController(ModelMapper modelMapper,
                             ArticleRepository articleRepository,
                             RatingRepository ratingRepository,
                             AuthService authService) {
        this.modelMapper = modelMapper;
        this.articleRepository = articleRepository;
        this.ratingRepository = ratingRepository;
        this.authService = authService;
    }

    @Operation(operationId = "articlesArticleIdRatingsPost",
            summary = "Create a rating (registered users only)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Rating created"),
                    @ApiResponse(responseCode = "401", description = "Not authorized"),
                    @ApiResponse(responseCode = "404", description = "Article not found")
            })
    @PostMapping(value = "/ratings/{articleId}", consumes = {"application/json"})
    public ResponseEntity<Void> articlesArticleIdRatingsPost(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String articleId, Rating rating) {
        log.trace("articlesArticleIdRatingsPost called");

        if (!authService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }

        Optional<ArticleEntity> articleEntityOptional = articleRepository.findById(UUID.fromString(articleId));

        if (articleEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ArticleEntity articleEntity = articleEntityOptional.get();

        RatingEntity ratingEntity = modelMapper.map(rating, RatingEntity.class);
        ratingRepository.save(ratingEntity);
        articleEntity.getRatings().add(ratingEntity);
        articleRepository.save(articleEntity);

        return ResponseEntity.status(201).build();
    }

    @Operation(operationId = "articlesArticleIdRatingsRatingIdDelete",
            summary = "Delete a rating (administrators only)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Rating deleted"),
                    @ApiResponse(responseCode = "401", description = "Not authorized"),
                    @ApiResponse(responseCode = "404", description = "Article not found")
            })
    @DeleteMapping(value = "/ratings/{articleId}/{ratingId}")
    public ResponseEntity<Void> articlesArticleIdRatingsRatingIdDelete(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String articleId, @PathVariable String ratingId) {
        log.trace("articlesArticleIdRatingsRatingIdDelete called");

        if (!authService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }

        Optional<ArticleEntity> articleEntityOptional = articleRepository.findById(UUID.fromString(articleId));

        if (articleEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ArticleEntity articleEntity = articleEntityOptional.get();

        articleEntity.getRatings()
                .removeIf(ratingEntity -> ratingEntity.getId().equals(UUID.fromString(ratingId)));
        articleRepository.save(articleEntity);

        return ResponseEntity.status(204).build();
    }

}
