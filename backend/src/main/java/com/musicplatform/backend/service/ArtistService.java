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

    public List<Artist> getArtistsByCountry(String country) {
        return artistRepository.findByCountry(country);
    }

    public Artist updateArtist(String id, Artist updatedArtist) {
        Optional<Artist> existingArtist = artistRepository.findById(id);
        if (existingArtist.isPresent()) {
            Artist artist = existingArtist.get();
            if (updatedArtist.getName() != null) artist.setName(updatedArtist.getName());
            if (updatedArtist.getCountry() != null) artist.setCountry(updatedArtist.getCountry());
            if (updatedArtist.getDebutYear() != 0) artist.setDebutYear(updatedArtist.getDebutYear());
            if (updatedArtist.getBiography() != null) artist.setBiography(updatedArtist.getBiography());
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
