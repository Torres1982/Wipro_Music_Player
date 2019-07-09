package com.wipro.wipro_music_player;

class SongModel {
    private String artist;
    private String title;
    private String path;
    private long length;
    private double size;

    SongModel() {}

    SongModel(String artist, String title, String path, long length, double size) {
        this.artist = artist;
        this.title = title;
        this.path = path;
        this.length = length;
        this.size = size;
    }

    String getArtist() {
        return artist;
    }
    String getTitle() {
        return title;
    }
    String getPath() {
        return path;
    }
    long getLength() {
        return length;
    }
    double getSize() {
        return size;
    }

    void setArtist(String artist) {
        this.artist = artist;
    }
    void setTitle(String title) {
        this.title = title;
    }
    void setPath(String path) {
        this.path = path;
    }
    void setLength(long length) {
        this.length = length;
    }
    void setSize(double size) {
        this.size = size;
    }
}
