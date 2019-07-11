package com.wipro.wipro_music_player.model;

public class SongModel {
    private String artist;
    private String title;
    private String path;
    private long length;
    private double size;

    public SongModel() {}

    public SongModel(String artist, String title, String path, long length, double size) {
        this.artist = artist;
        this.title = title;
        this.path = path;
        this.length = length;
        this.size = size;
    }

    public String getArtist() { return artist; }
    public String getTitle() { return title; }
    public String getPath() { return path; }
    public long getLength() { return length; }
    public double getSize() { return size; }

    public void setArtist(String artist) { this.artist = artist; }
    public void setTitle(String title) { this.title = title; }
    public void setPath(String path) { this.path = path; }
    public void setLength(long length) { this.length = length; }
    public void setSize(double size) { this.size = size; }
}
