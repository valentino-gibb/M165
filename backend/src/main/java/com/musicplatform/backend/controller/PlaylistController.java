package com.musicplatform.backend.controller;

import com.musicplatform.backend.model.Playlist;
import com.musicplatform.backend.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "http://localhost:5173")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Playlist playlist) {
        return new ResponseEntity<>(playlistService.createPlaylist(playlist), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable String id) {
        Optional<Playlist> playlist = playlistService.getPlaylistById(id);
        return playlist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Playlist> getPlaylistByName(@PathVariable String name) {
        Optional<Playlist> playlist = playlistService.getPlaylistByName(name);
        return playlist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Playlist>> getAllPlaylists() {
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Playlist>> getPlaylistsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(playlistService.getPlaylistsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable String id, @RequestBody Playlist playlist) {
        Playlist updatedPlaylist = playlistService.updatePlaylist(id, playlist);
        if (updatedPlaylist != null) {
            return ResponseEntity.ok(updatedPlaylist);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> addSongToPlaylist(@PathVariable String playlistId, @PathVariable String songId) {
        Playlist playlist = playlistService.addSongToPlaylist(playlistId, songId);
        if (playlist != null) {
            return ResponseEntity.ok(playlist);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> removeSongFromPlaylist(@PathVariable String playlistId, @PathVariable String songId) {
        Playlist playlist = playlistService.removeSongFromPlaylist(playlistId, songId);
        if (playlist != null) {
            return ResponseEntity.ok(playlist);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable String id) {
        if (playlistService.deletePlaylist(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
