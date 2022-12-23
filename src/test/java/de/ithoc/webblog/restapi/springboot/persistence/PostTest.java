package de.ithoc.webblog.restapi.springboot.persistence;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;


public class PostTest {

    private static EntityManager entityManager;


    @Before
    public void init() {

        EntityManagerFactory entityManagerFactory
                = Persistence.createEntityManagerFactory("JPA_WEBBLOG_PERSISTENCE_UNIT");
        entityManager = entityManagerFactory.createEntityManager();

    }


    @Test
    public void saveAndRead() {

        // Test data preparation
        Post readPost = new Post("What a title","Read my splendid blog post.", LocalDateTime.now());

        entityManager.getTransaction().begin();
        entityManager.persist(readPost);
        entityManager.getTransaction().commit();

        // Run the logic
        Long id = readPost.getId();
        Post post = entityManager.find(Post.class, id);

        // Check result
        String text = readPost.getText();
        Assert.assertEquals("Read my splendid blog post.", text);

        LocalDateTime timestamp = readPost.getTimestamp();
        Assert.assertTrue(timestamp.isEqual(post.getTimestamp()));
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
