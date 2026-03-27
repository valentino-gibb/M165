package com.musicplatform.backend.repository;

import com.musicplatform.backend.model.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtistRepository extends MongoRepository<Artist, String> {
}
