package com.wipro.wipro_music_player;

public class SongModel {
    private String artist;
    private String title;
    private long length;

    SongModel() {}

    SongModel(String artist, String title) {
        this.artist = artist;
        this.title = title;
    }

    SongModel(String artist, String title, long length) {
        this.artist = artist;
        this.title = title;
        this.length = length;
    }

    String getArtist() {
        return artist;
    }
    public String getTitle() {
        return title;
    }
    long getLength() {
        return length;
    }

    void setArtist(String artist) {
        this.artist = artist;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    void setLength(long length) {
        this.length = length;
    }
}
