package de.ithoc.webblog.restapi.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RatingRepository extends JpaRepository<RatingEntity, UUID> {
}
