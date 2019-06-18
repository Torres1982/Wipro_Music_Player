package com.wipro.wipro_music_player;

public class SongModel {
    private String artist;
    private String title;

    SongModel() {}

    SongModel(String artist, String title) {
        this.artist = artist;
        this.title = title;
    }

    String getArtist() {
        return artist;
    }
    public String getTitle() {
        return title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
