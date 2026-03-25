package com.musicplatform.backend.controller;

import com.musicplatform.backend.model.Song;
import com.musicplatform.backend.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "http://localhost:5173")
public class SongController {
    @Autowired
    private SongService songService;

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
        return ResponseEntity.ok(songService.getSongsByLanguage(language));
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<Song>> getSongsByAlbum(@PathVariable String albumId) {
        return ResponseEntity.ok(songService.getSongsByAlbumId(albumId));
    }

    @GetMapping("/explicit")
    public ResponseEntity<List<Song>> getExplicitSongs() {
        return ResponseEntity.ok(songService.getExplicitSongs());
    }
}
