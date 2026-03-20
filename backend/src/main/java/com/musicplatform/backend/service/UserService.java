package com.musicplatform.backend.service;

import com.musicplatform.backend.model.User;
import com.musicplatform.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByBenutzername(String benutzername) {
        return userRepository.findByBenutzername(benutzername);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(String id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (updatedUser.getBenutzername() != null) user.setBenutzername(updatedUser.getBenutzername());
            if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
            if (updatedUser.getPassword() != null) user.setPassword(updatedUser.getPassword());
            if (updatedUser.getLand() != null) user.setLand(updatedUser.getLand());
            if (updatedUser.getProfilbild() != null) user.setProfilbild(updatedUser.getProfilbild());
            return userRepository.save(user);
        }
        return null;
    }

    public User setArtistRole(String userId, String artistId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User u = user.get();
            u.setArtistId(artistId);
            return userRepository.save(u);
        }
        return null;
    }

    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
