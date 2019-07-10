package com.wipro.wipro_music_player.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FavouriteSongModel extends RealmObject {
    @PrimaryKey
    private long id;
    private String songTitle;
    private String songArtist;
    private String songPath;
    private long songLength;
    private double songSize;

    public FavouriteSongModel() {}

    public long getId() { return id; }
    public String getSongTitle() { return songTitle; }
    public String getSongArtist() { return songArtist; }
    public String getSongPath() { return songPath; }
    public long getSongLength() { return songLength; }
    public double getSongSize() { return songSize; }

    public void setId(long id) { this.id = id; }
    public void setSongTitle(String songTitle) { this.songTitle = songTitle; }
    public void setSongArtist(String songArtist) { this.songArtist = songArtist; }
    public void setSongPath(String songPath) { this.songPath = songPath; }
    public void setSongLength(long songLength) { this.songLength = songLength; }
    public void setSongSize(double songSize) { this.songSize = songSize; }
}
