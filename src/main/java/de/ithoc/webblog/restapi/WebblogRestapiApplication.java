package de.ithoc.webblog.restapi;

import de.ithoc.webblog.restapi.persistence.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;
import java.util.UUID;


@SpringBootApplication
@Slf4j
public class WebblogRestapiApplication {

	private final ModelMapper modelMapper;
	private final ArticleRepository articleRepository;
	private final CommentRepository commentRepository;
	private final RatingRepository ratingRepository;

	public WebblogRestapiApplication(ModelMapper modelMapper,
									 ArticleRepository articleRepository,
									 CommentRepository commentRepository,
									 RatingRepository ratingRepository) {
		this.modelMapper = modelMapper;
		this.articleRepository = articleRepository;
		this.commentRepository = commentRepository;
		this.ratingRepository = ratingRepository;
	}

	/**
	 * Spring Boot's entry point.
	 */
	public static void main(String[] args) {
		SpringApplication.run(WebblogRestapiApplication.class, args);
	}

	@PostConstruct
	private void init() {
		log.trace("init called");

		String author = UUID.randomUUID().toString();
		for (int i = 0; i < 2; i++) {
			ArticleEntity articleEntity = createArticle(author);
			articleRepository.save(articleEntity);

			for (int j = 0; j < 2; j++) {
				articleEntity.getComments().add(commentRepository.save(createComment()));
			}

			for (int j = 0; j < 2; j++) {
				articleEntity.getRatings().add(ratingRepository.save(createRating()));
			}

			articleRepository.save(articleEntity);
		}
	}


	private RatingEntity createRating() {
		log.trace("createRating called");

		RatingEntity ratingEntity = new RatingEntity();
		ratingEntity.setStars(new Random().nextInt(5) + 1);
		ratingEntity.setAuthor(UUID.randomUUID().toString());

		return ratingEntity;
	}

	private static CommentEntity createComment() {
		log.trace("createComment called");

		CommentEntity commentEntity = new CommentEntity();
		commentEntity.setContent(UUID.randomUUID().toString());
		commentEntity.setAuthor(UUID.randomUUID().toString());

		return commentEntity;
	}

	private static ArticleEntity createArticle(String author) {
		log.trace("createArticle called");

		ArticleEntity articleEntity = new ArticleEntity();
		articleEntity.setTitle(UUID.randomUUID().toString());
		articleEntity.setContent(UUID.randomUUID().toString());
		articleEntity.setAuthor(author);

		return articleEntity;
	}

}
