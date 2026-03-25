package com.musicplatform.backend.service;

import com.musicplatform.backend.model.Album;
import com.musicplatform.backend.model.Song;
import com.musicplatform.backend.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SongService {
    @Autowired
    private AlbumRepository albumRepository;

    public List<Song> getAllSongs() {
        return albumRepository.findAll().stream()
                .flatMap(album -> album.getSongs().stream())
                .collect(Collectors.toList());
    }

    public List<Song> getSongsByAlbumId(String albumId) {
        Optional<Album> album = albumRepository.findById(albumId);
        return album.map(Album::getSongs).orElse(new ArrayList<>());
    }

    public List<Song> getSongsByGenre(String genre) {
        return albumRepository.findAll().stream()
                .flatMap(album -> album.getSongs().stream())
                .filter(song -> genre.equals(song.getGenre()))
                .collect(Collectors.toList());
    }

    public List<Song> getSongsByLanguage(String language) {
        return albumRepository.findAll().stream()
                .flatMap(album -> album.getSongs().stream())
                .filter(song -> language.equals(song.getLanguage()))
                .collect(Collectors.toList());
    }

    public List<Song> getExplicitSongs() {
        return albumRepository.findAll().stream()
                .flatMap(album -> album.getSongs().stream())
                .filter(Song::isExplicit)
                .collect(Collectors.toList());
    }
}
