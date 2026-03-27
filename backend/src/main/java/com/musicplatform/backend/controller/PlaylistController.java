package com.musicplatform.backend.controller;

import com.musicplatform.backend.model.Playlist;
import com.musicplatform.backend.repository.PlaylistRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistRepository playlistRepository;

    public PlaylistController(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @GetMapping
    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getById(@PathVariable String id) {
        return playlistRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Playlist create(@RequestBody Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Playlist> update(@PathVariable String id, @RequestBody Playlist playlist) {
        if (!playlistRepository.existsById(id)) return ResponseEntity.notFound().build();
        playlist.setId(id);
        return ResponseEntity.ok(playlistRepository.save(playlist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!playlistRepository.existsById(id)) return ResponseEntity.notFound().build();
        playlistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
