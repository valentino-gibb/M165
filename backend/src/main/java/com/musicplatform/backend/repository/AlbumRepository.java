package com.musicplatform.backend.repository;

import com.musicplatform.backend.model.Album;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlbumRepository extends MongoRepository<Album, String> {
}
