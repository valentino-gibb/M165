package com.musicplatform.backend.service;

import com.musicplatform.backend.model.Artist;
import com.musicplatform.backend.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {
    @Autowired
    private ArtistRepository artistRepository;

    public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    public Optional<Artist> getArtistById(String id) {
        return artistRepository.findById(id);
    }

    public Optional<Artist> getArtistByName(String name) {
        return artistRepository.findByName(name);
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public List<Artist> getArtistsByLabel(String label) {
        return artistRepository.findByLabel(label);
    }

    public List<Artist> getArtistsByHerkunftsland(String herkunftsland) {
        return artistRepository.findByHerkunftsland(herkunftsland);
    }

    public Artist updateArtist(String id, Artist updatedArtist) {
        Optional<Artist> existingArtist = artistRepository.findById(id);
        if (existingArtist.isPresent()) {
            Artist artist = existingArtist.get();
            if (updatedArtist.getName() != null) artist.setName(updatedArtist.getName());
            if (updatedArtist.getHerkunftsland() != null) artist.setHerkunftsland(updatedArtist.getHerkunftsland());
            if (updatedArtist.getDebütJahr() != 0) artist.setDebütJahr(updatedArtist.getDebütJahr());
            if (updatedArtist.getBiografie() != null) artist.setBiografie(updatedArtist.getBiografie());
            if (updatedArtist.getLabel() != null) artist.setLabel(updatedArtist.getLabel());
            return artistRepository.save(artist);
        }
        return null;
    }

    public boolean deleteArtist(String id) {
        if (artistRepository.existsById(id)) {
            artistRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
