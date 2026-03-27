package com.musicplatform.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "artists")
public class Artist {
    @Id
    private String id;
    private String name;
    private String country;
    private int debutYear;
    private String biography;
    private String label;
    private List<String> albumIds = new ArrayList<>();
    private String userId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public int getDebutYear() { return debutYear; }
    public void setDebutYear(int debutYear) { this.debutYear = debutYear; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public List<String> getAlbumIds() { return albumIds; }
    public void setAlbumIds(List<String> albumIds) { this.albumIds = albumIds; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
