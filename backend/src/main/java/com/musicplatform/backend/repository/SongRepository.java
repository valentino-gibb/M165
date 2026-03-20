package com.musicplatform.backend.repository;

import com.musicplatform.backend.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends MongoRepository<Song, String> {
    Optional<Song> findByTitle(String title);
    List<Song> findByGenre(String genre);
    List<Song> findBySprache(String sprache);
    List<Song> findByAlbumId(String albumId);
    List<Song> findByExplizitTrue();
}
