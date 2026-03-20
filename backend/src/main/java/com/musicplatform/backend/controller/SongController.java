package com.musicplatform.backend.controller;

import com.musicplatform.backend.model.Song;
import com.musicplatform.backend.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "http://localhost:5173")
public class SongController {
    @Autowired
    private SongService songService;

    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        return new ResponseEntity<>(songService.createSong(song), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable String id) {
        Optional<Song> song = songService.getSongById(id);
        return song.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Song> getSongByTitle(@PathVariable String title) {
        Optional<Song> song = songService.getSongByTitle(title);
        return song.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Song>> getSongsByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(songService.getSongsByGenre(genre));
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<Song>> getSongsByLanguage(@PathVariable String language) {
        return ResponseEntity.ok(songService.getSongsBySprache(language));
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<Song>> getSongsByAlbum(@PathVariable String albumId) {
        return ResponseEntity.ok(songService.getSongsByAlbumId(albumId));
    }

    @GetMapping("/explicit")
    public ResponseEntity<List<Song>> getExplicitSongs() {
        return ResponseEntity.ok(songService.getExplicitSongs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable String id, @RequestBody Song song) {
        Song updatedSong = songService.updateSong(id, song);
        if (updatedSong != null) {
            return ResponseEntity.ok(updatedSong);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/stream")
    public ResponseEntity<Song> incrementStreamCount(@PathVariable String id) {
        Song song = songService.incrementStreamCount(id);
        if (song != null) {
            return ResponseEntity.ok(song);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable String id) {
        if (songService.deleteSong(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
