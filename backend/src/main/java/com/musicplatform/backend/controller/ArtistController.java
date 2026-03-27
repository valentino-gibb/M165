package com.musicplatform.backend.controller;

import com.musicplatform.backend.model.Artist;
import com.musicplatform.backend.repository.ArtistRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistRepository artistRepository;

    public ArtistController(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @GetMapping
    public List<Artist> getAll() {
        return artistRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getById(@PathVariable String id) {
        return artistRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Artist create(@RequestBody Artist artist) {
        return artistRepository.save(artist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> update(@PathVariable String id, @RequestBody Artist artist) {
        if (!artistRepository.existsById(id)) return ResponseEntity.notFound().build();
        artist.setId(id);
        return ResponseEntity.ok(artistRepository.save(artist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!artistRepository.existsById(id)) return ResponseEntity.notFound().build();
        artistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
