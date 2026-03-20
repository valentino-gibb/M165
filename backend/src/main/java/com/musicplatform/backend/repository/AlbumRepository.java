package com.musicplatform.backend.repository;

import com.musicplatform.backend.model.Album;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends MongoRepository<Album, String> {
    Optional<Album> findByTitle(String title);
    List<Album> findByArtistId(String artistId);
    List<Album> findAll();
}
