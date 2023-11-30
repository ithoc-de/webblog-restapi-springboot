package de.ithoc.webblog.restapi.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	// Example query coming from Spring Repository rules.
	Optional<User> findByEmail(String email);
	
}
