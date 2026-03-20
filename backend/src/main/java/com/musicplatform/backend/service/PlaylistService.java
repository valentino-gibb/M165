package com.musicplatform.backend.service;

import com.musicplatform.backend.model.Playlist;
import com.musicplatform.backend.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    public Playlist createPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Optional<Playlist> getPlaylistById(String id) {
        return playlistRepository.findById(id);
    }

    public Optional<Playlist> getPlaylistByName(String name) {
        return playlistRepository.findByName(name);
    }

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public List<Playlist> getPlaylistsByUserId(String userId) {
        return playlistRepository.findByUserId(userId);
    }

    public Playlist updatePlaylist(String id, Playlist updatedPlaylist) {
        Optional<Playlist> existingPlaylist = playlistRepository.findById(id);
        if (existingPlaylist.isPresent()) {
            Playlist playlist = existingPlaylist.get();
            if (updatedPlaylist.getName() != null) playlist.setName(updatedPlaylist.getName());
            if (updatedPlaylist.getBeschreibung() != null) playlist.setBeschreibung(updatedPlaylist.getBeschreibung());
            if (updatedPlaylist.getTags() != null && !updatedPlaylist.getTags().isEmpty()) playlist.setTags(updatedPlaylist.getTags());
            if (updatedPlaylist.getCoverbild() != null) playlist.setCoverbild(updatedPlaylist.getCoverbild());
            return playlistRepository.save(playlist);
        }
        return null;
    }

    public Playlist addSongToPlaylist(String playlistId, String songId) {
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        if (playlist.isPresent()) {
            Playlist p = playlist.get();
            if (!p.getSongIds().contains(songId)) {
                p.getSongIds().add(songId);
            }
            return playlistRepository.save(p);
        }
        return null;
    }

    public Playlist removeSongFromPlaylist(String playlistId, String songId) {
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        if (playlist.isPresent()) {
            Playlist p = playlist.get();
            p.getSongIds().remove(songId);
            return playlistRepository.save(p);
        }
        return null;
    }

    public boolean deletePlaylist(String id) {
        if (playlistRepository.existsById(id)) {
            playlistRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
