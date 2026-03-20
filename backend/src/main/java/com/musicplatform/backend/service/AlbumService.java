package com.musicplatform.backend.service;

import com.musicplatform.backend.model.Album;
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
        return albumRepository.findByArtistId(artistId);
    }

    public Album updateAlbum(String id, Album updatedAlbum) {
        Optional<Album> existingAlbum = albumRepository.findById(id);
        if (existingAlbum.isPresent()) {
            Album album = existingAlbum.get();
            if (updatedAlbum.getTitle() != null) album.setTitle(updatedAlbum.getTitle());
            if (updatedAlbum.getVeröffentlichungsDatum() != null) album.setVeröffentlichungsDatum(updatedAlbum.getVeröffentlichungsDatum());
            if (updatedAlbum.getBeschreibung() != null) album.setBeschreibung(updatedAlbum.getBeschreibung());
            if (updatedAlbum.getCoverbild() != null) album.setCoverbild(updatedAlbum.getCoverbild());
            if (updatedAlbum.getGesamtdauer() != 0) album.setGesamtdauer(updatedAlbum.getGesamtdauer());
            return albumRepository.save(album);
        }
        return null;
    }

    public Album addSongToAlbum(String albumId, String songId) {
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isPresent()) {
            Album a = album.get();
            if (!a.getSongIds().contains(songId)) {
                a.getSongIds().add(songId);
            }
            return albumRepository.save(a);
        }
        return null;
    }

    public Album removeSongFromAlbum(String albumId, String songId) {
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isPresent()) {
            Album a = album.get();
            a.getSongIds().remove(songId);
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
