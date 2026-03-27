package com.musicplatform.backend.controller;

import com.musicplatform.backend.model.Album;
import com.musicplatform.backend.model.Song;
import com.musicplatform.backend.repository.AlbumRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumRepository albumRepository;

    public AlbumController(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @GetMapping
    public List<Album> getAll() {
        return albumRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getById(@PathVariable String id) {
        return albumRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getSongs(@PathVariable String id) {
        return albumRepository.findById(id)
                .map(album -> ResponseEntity.ok(album.getSongs()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Album create(@RequestBody Album album) {
        return albumRepository.save(album);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> update(@PathVariable String id, @RequestBody Album album) {
        if (!albumRepository.existsById(id)) return ResponseEntity.notFound().build();
        album.setId(id);
        return ResponseEntity.ok(albumRepository.save(album));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!albumRepository.existsById(id)) return ResponseEntity.notFound().build();
        albumRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
