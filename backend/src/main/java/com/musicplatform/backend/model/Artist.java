package com.musicplatform.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "artists")
public class Artist {
    @Id
    private String id;
    private String name;
    private String herkunftsland;
    private int debütJahr;
    private String biografie;
    private String label;
    private Date erstellungsDatum;

    public Artist() {}

    public Artist(String name, String herkunftsland, int debütJahr, String biografie, String label) {
        this.name = name;
        this.herkunftsland = herkunftsland;
        this.debütJahr = debütJahr;
        this.biografie = biografie;
        this.label = label;
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

    public String getHerkunftsland() {
        return herkunftsland;
    }

    public void setHerkunftsland(String herkunftsland) {
        this.herkunftsland = herkunftsland;
    }

    public int getDebütJahr() {
        return debütJahr;
    }

    public void setDebütJahr(int debütJahr) {
        this.debütJahr = debütJahr;
    }

    public String getBiografie() {
        return biografie;
    }

    public void setBiografie(String biografie) {
        this.biografie = biografie;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getErstellungsDatum() {
        return erstellungsDatum;
    }

    public void setErstellungsDatum(Date erstellungsDatum) {
        this.erstellungsDatum = erstellungsDatum;
    }
}
