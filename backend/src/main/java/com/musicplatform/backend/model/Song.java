package com.musicplatform.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "songs")
public class Song {
    @Id
    private String id;
    private String title;
    private int dauer;
    private String genre;
    private Date veröffentlichungsDatum;
    private boolean explizit;
    private long anzahlStreams;
    private String lyrics;
    private String sprache;
    private String albumId;

    public Song() {}

    public Song(String title, int dauer, String genre, Date veröffentlichungsDatum,
                boolean explizit, String lyrics, String sprache) {
        this.title = title;
        this.dauer = dauer;
        this.genre = genre;
        this.veröffentlichungsDatum = veröffentlichungsDatum;
        this.explizit = explizit;
        this.lyrics = lyrics;
        this.sprache = sprache;
        this.anzahlStreams = 0;
    }

    // Getter und Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDauer() {
        return dauer;
    }

    public void setDauer(int dauer) {
        this.dauer = dauer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Date getVeröffentlichungsDatum() {
        return veröffentlichungsDatum;
    }

    public void setVeröffentlichungsDatum(Date veröffentlichungsDatum) {
        this.veröffentlichungsDatum = veröffentlichungsDatum;
    }

    public boolean isExplizit() {
        return explizit;
    }

    public void setExplizit(boolean explizit) {
        this.explizit = explizit;
    }

    public long getAnzahlStreams() {
        return anzahlStreams;
    }

    public void setAnzahlStreams(long anzahlStreams) {
        this.anzahlStreams = anzahlStreams;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        this.sprache = sprache;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
