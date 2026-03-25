package com.musicplatform.backend.model;

import java.util.Date;

public class Song {
    private String title;
    private int duration;
    private String genre;
    private Date releaseDate;
    private boolean explicit;
    private long streamCount;
    private String lyrics;
    private String language;

    public Song() {}

    public Song(String title, int duration, String genre, Date releaseDate,
                boolean explicit, String lyrics, String language) {
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.explicit = explicit;
        this.lyrics = lyrics;
        this.language = language;
        this.streamCount = 0;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }

    public boolean isExplicit() { return explicit; }
    public void setExplicit(boolean explicit) { this.explicit = explicit; }

    public long getStreamCount() { return streamCount; }
    public void setStreamCount(long streamCount) { this.streamCount = streamCount; }

    public String getLyrics() { return lyrics; }
    public void setLyrics(String lyrics) { this.lyrics = lyrics; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
