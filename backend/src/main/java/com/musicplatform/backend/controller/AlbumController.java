package com.musicplatform.backend.controller;

import com.musicplatform.backend.model.Album;
import com.musicplatform.backend.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/albums")
@CrossOrigin(origins = "http://localhost:5173")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        return new ResponseEntity<>(albumService.createAlbum(album), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable String id) {
        Optional<Album> album = albumService.getAlbumById(id);
        return album.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Album> getAlbumByTitle(@PathVariable String title) {
        Optional<Album> album = albumService.getAlbumByTitle(title);
        return album.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        return ResponseEntity.ok(albumService.getAllAlbums());
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable String artistId) {
        return ResponseEntity.ok(albumService.getAlbumsByArtistId(artistId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable String id, @RequestBody Album album) {
        Album updatedAlbum = albumService.updateAlbum(id, album);
        if (updatedAlbum != null) {
            return ResponseEntity.ok(updatedAlbum);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{albumId}/songs/{songId}")
    public ResponseEntity<Album> addSongToAlbum(@PathVariable String albumId, @PathVariable String songId) {
        Album album = albumService.addSongToAlbum(albumId, songId);
        if (album != null) {
            return ResponseEntity.ok(album);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{albumId}/songs/{songId}")
    public ResponseEntity<Album> removeSongFromAlbum(@PathVariable String albumId, @PathVariable String songId) {
        Album album = albumService.removeSongFromAlbum(albumId, songId);
        if (album != null) {
            return ResponseEntity.ok(album);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable String id) {
        if (albumService.deleteAlbum(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
