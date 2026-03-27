package com.musicplatform.backend.repository;

import com.musicplatform.backend.model.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlaylistRepository extends MongoRepository<Playlist, String> {
}
