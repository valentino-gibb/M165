package com.musicplatform.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "albums")
public class Album {
    @Id
    private String id;
    private String title;
    private Date veröffentlichungsDatum;
    private String beschreibung;
    private String coverbild;
    private int gesamtdauer;
    private String artistId;
    private List<String> songIds = new ArrayList<>();
    private Date erstellungsDatum;

    public Album() {}

    public Album(String title, Date veröffentlichungsDatum, String beschreibung,
                 String coverbild, String artistId) {
        this.title = title;
        this.veröffentlichungsDatum = veröffentlichungsDatum;
        this.beschreibung = beschreibung;
        this.coverbild = coverbild;
        this.artistId = artistId;
        this.gesamtdauer = 0;
        this.erstellungsDatum = new Date();
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

    public Date getVeröffentlichungsDatum() {
        return veröffentlichungsDatum;
    }

    public void setVeröffentlichungsDatum(Date veröffentlichungsDatum) {
        this.veröffentlichungsDatum = veröffentlichungsDatum;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getCoverbild() {
        return coverbild;
    }

    public void setCoverbild(String coverbild) {
        this.coverbild = coverbild;
    }

    public int getGesamtdauer() {
        return gesamtdauer;
    }

    public void setGesamtdauer(int gesamtdauer) {
        this.gesamtdauer = gesamtdauer;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }

    public Date getErstellungsDatum() {
        return erstellungsDatum;
    }

    public void setErstellungsDatum(Date erstellungsDatum) {
        this.erstellungsDatum = erstellungsDatum;
    }
}
