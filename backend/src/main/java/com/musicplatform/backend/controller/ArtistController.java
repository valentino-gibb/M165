package com.musicplatform.backend.controller;

import com.musicplatform.backend.model.Artist;
import com.musicplatform.backend.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artists")
@CrossOrigin(origins = "http://localhost:5173")
public class ArtistController {
    @Autowired
    private ArtistService artistService;

    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        return new ResponseEntity<>(artistService.createArtist(artist), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable String id) {
        Optional<Artist> artist = artistService.getArtistById(id);
        return artist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Artist> getArtistByName(@PathVariable String name) {
        Optional<Artist> artist = artistService.getArtistByName(name);
        return artist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping("/label/{label}")
    public ResponseEntity<List<Artist>> getArtistsByLabel(@PathVariable String label) {
        return ResponseEntity.ok(artistService.getArtistsByLabel(label));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<Artist>> getArtistsByCountry(@PathVariable String country) {
        return ResponseEntity.ok(artistService.getArtistsByHerkunftsland(country));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable String id, @RequestBody Artist artist) {
        Artist updatedArtist = artistService.updateArtist(id, artist);
        if (updatedArtist != null) {
            return ResponseEntity.ok(updatedArtist);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable String id) {
        if (artistService.deleteArtist(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
