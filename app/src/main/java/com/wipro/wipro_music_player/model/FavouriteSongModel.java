package com.wipro.wipro_music_player.model;

import io.realm.RealmObject;

class FavouriteSongModel extends RealmObject {
    private String songTitle;
    private String songArtist;
    private String songPath;

    public String getSongTitle() { return songTitle; }
    public String getSongArtist() { return songArtist; }
    public String getSongPath() { return songPath; }

    public void setSongTitle(String songTitle) { this.songTitle = songTitle; }
    public void setSongArtist(String songArtist) { this.songArtist = songArtist; }
    public void setSongPath(String songPath) { this.songPath = songPath; }
}
