package com.musicplatform.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "playlists")
public class Playlist {
    @Id
    private String id;
    private String name;
    private String beschreibung;
    private Date erstellungsDatum;
    private List<String> tags = new ArrayList<>();
    private String coverbild;
    private String userId;
    private List<String> songIds = new ArrayList<>();

    public Playlist() {}

    public Playlist(String name, String beschreibung, String userId) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.userId = userId;
        this.erstellungsDatum = new Date();
    }

    // Getter und Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Date getErstellungsDatum() {
        return erstellungsDatum;
    }

    public void setErstellungsDatum(Date erstellungsDatum) {
        this.erstellungsDatum = erstellungsDatum;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCoverbild() {
        return coverbild;
    }

    public void setCoverbild(String coverbild) {
        this.coverbild = coverbild;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }
}
