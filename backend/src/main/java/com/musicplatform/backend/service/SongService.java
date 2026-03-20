package com.musicplatform.backend.service;

import com.musicplatform.backend.model.Song;
import com.musicplatform.backend.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    public Song createSong(Song song) {
        return songRepository.save(song);
    }

    public Optional<Song> getSongById(String id) {
        return songRepository.findById(id);
    }

    public Optional<Song> getSongByTitle(String title) {
        return songRepository.findByTitle(title);
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public List<Song> getSongsByGenre(String genre) {
        return songRepository.findByGenre(genre);
    }

    public List<Song> getSongsBySprache(String sprache) {
        return songRepository.findBySprache(sprache);
    }

    public List<Song> getSongsByAlbumId(String albumId) {
        return songRepository.findByAlbumId(albumId);
    }

    public List<Song> getExplicitSongs() {
        return songRepository.findByExplizitTrue();
    }

    public Song updateSong(String id, Song updatedSong) {
        Optional<Song> existingSong = songRepository.findById(id);
        if (existingSong.isPresent()) {
            Song song = existingSong.get();
            if (updatedSong.getTitle() != null) song.setTitle(updatedSong.getTitle());
            if (updatedSong.getDauer() != 0) song.setDauer(updatedSong.getDauer());
            if (updatedSong.getGenre() != null) song.setGenre(updatedSong.getGenre());
            if (updatedSong.getVeröffentlichungsDatum() != null) song.setVeröffentlichungsDatum(updatedSong.getVeröffentlichungsDatum());
            song.setExplizit(updatedSong.isExplizit());
            if (updatedSong.getLyrics() != null) song.setLyrics(updatedSong.getLyrics());
            if (updatedSong.getSprache() != null) song.setSprache(updatedSong.getSprache());
            return songRepository.save(song);
        }
        return null;
    }

    public Song incrementStreamCount(String id) {
        Optional<Song> song = songRepository.findById(id);
        if (song.isPresent()) {
            Song s = song.get();
            s.setAnzahlStreams(s.getAnzahlStreams() + 1);
            return songRepository.save(s);
        }
        return null;
    }

    public boolean deleteSong(String id) {
        if (songRepository.existsById(id)) {
            songRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
