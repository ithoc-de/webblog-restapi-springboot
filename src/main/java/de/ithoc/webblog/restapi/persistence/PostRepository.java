package de.ithoc.webblog.restapi.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository // This annotation is optional and there to simplify reading
public interface PostRepository extends JpaRepository<Post, Long> {

}
