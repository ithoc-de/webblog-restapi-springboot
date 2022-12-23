package de.ithoc.webblog.restapi.springboot.persistence;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;


public class RatingTest {

    private static EntityManager entityManager;


    @Before
    public void init() {

        EntityManagerFactory entityManagerFactory
                = Persistence.createEntityManagerFactory("JPA_WEBBLOG_PERSISTENCE_UNIT");
        entityManager = entityManagerFactory.createEntityManager();

    }


    @Test
    public void ratePost() {

        // Test data preparation
        Post existingPost = new Post("What a title","Read my splendid blog post.", LocalDateTime.now());
        save(existingPost);

        User user = new User();
        user.setEmail("oliver.hock@videa.services");
        user.setName("Oli Hock");

        save(user);

        // Run the logic
        Rating rating = new Rating();
        rating.setAuthor(user);
        rating.setGrade((byte) 3);
        rating.setAuthor(user);

        entityManager.getTransaction().begin();
        entityManager.persist(rating);
        entityManager.getTransaction().commit();

        existingPost.getRatings().add(rating);
        save(existingPost);

        // Check result
        Rating checkRating = entityManager.find(Rating.class, rating.getId());
        Assert.assertNotNull(checkRating);

    }

    private void save(User user) {
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }


    /**
     * To save a post the entity manager opens a transaction, saves and commit it to store post on database.
     *
     * @param post
     */
    private void save(Post post) {
        entityManager.getTransaction().begin();
        entityManager.persist(post);
        entityManager.getTransaction().commit();
    }

}
