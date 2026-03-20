package com.musicplatform.backend.repository;

import com.musicplatform.backend.model.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends MongoRepository<Playlist, String> {
    Optional<Playlist> findByName(String name);
    List<Playlist> findByUserId(String userId);
}
