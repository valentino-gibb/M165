package com.musicplatform.backend.repository;

import com.musicplatform.backend.model.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends MongoRepository<Artist, String> {
    Optional<Artist> findByName(String name);
    List<Artist> findByLabel(String label);
    List<Artist> findByCountry(String country);
}
