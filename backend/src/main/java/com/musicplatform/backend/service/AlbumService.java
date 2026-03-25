package com.musicplatform.backend.service;

import com.musicplatform.backend.model.Album;
import com.musicplatform.backend.model.Song;
import com.musicplatform.backend.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }

    public Optional<Album> getAlbumById(String id) {
        return albumRepository.findById(id);
    }

    public Optional<Album> getAlbumByTitle(String title) {
        return albumRepository.findByTitle(title);
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public List<Album> getAlbumsByArtistId(String artistId) {
        return albumRepository.findByArtistIdsContaining(artistId);
    }

    public Album updateAlbum(String id, Album updatedAlbum) {
        Optional<Album> existingAlbum = albumRepository.findById(id);
        if (existingAlbum.isPresent()) {
            Album album = existingAlbum.get();
            if (updatedAlbum.getTitle() != null) album.setTitle(updatedAlbum.getTitle());
            if (updatedAlbum.getReleaseDate() != null) album.setReleaseDate(updatedAlbum.getReleaseDate());
            if (updatedAlbum.getDescription() != null) album.setDescription(updatedAlbum.getDescription());
            if (updatedAlbum.getCoverImage() != null) album.setCoverImage(updatedAlbum.getCoverImage());
            if (updatedAlbum.getTotalDuration() != 0) album.setTotalDuration(updatedAlbum.getTotalDuration());
            return albumRepository.save(album);
        }
        return null;
    }

    public Album addSongToAlbum(String albumId, Song song) {
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isPresent()) {
            Album a = album.get();
            a.getSongs().add(song);
            return albumRepository.save(a);
        }
        return null;
    }

    public Album removeSongFromAlbum(String albumId, String songTitle) {
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isPresent()) {
            Album a = album.get();
            a.getSongs().removeIf(s -> songTitle.equals(s.getTitle()));
            return albumRepository.save(a);
        }
        return null;
    }

    public boolean deleteAlbum(String id) {
        if (albumRepository.existsById(id)) {
            albumRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
