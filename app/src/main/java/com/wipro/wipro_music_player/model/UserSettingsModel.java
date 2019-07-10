package com.wipro.wipro_music_player.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class UserSettingsModel extends RealmObject {
    private String lastPlayedSongPath;
    private int defaultThemeStatus;
    private int darkThemeStatus;
    private int shuffleSwitchStatus;
    private int repeatSwitchStatus;
    private RealmList<FavouriteSongModel> favouriteSongs;

    public String getLastPlayedSongPath() { return lastPlayedSongPath; }
    public int getDefaultThemeStatus() { return defaultThemeStatus; }
    public int getDarkThemeStatus() { return darkThemeStatus; }
    public int getShuffleSwitchStatus() { return shuffleSwitchStatus; }
    public int getRepeatSwitchStatus() { return repeatSwitchStatus; }
    public RealmList<FavouriteSongModel> getFavouriteSongs() { return favouriteSongs; }

    public void setLastPlayedSongPath(String lastPlayedSongPath) { this.lastPlayedSongPath = lastPlayedSongPath; }
    public void setDefaultThemeStatus(int defaultThemeStatus) { this.defaultThemeStatus = defaultThemeStatus; }
    public void setDarkThemeStatus(int darkThemeStatus) { this.darkThemeStatus = darkThemeStatus; }
    public void setShuffleSwitchStatus(int shuffleSwitchStatus) { this.shuffleSwitchStatus = shuffleSwitchStatus; }
    public void setRepeatSwitchStatus(int repeatSwitchStatus) { this.repeatSwitchStatus = repeatSwitchStatus; }
    public void setFavouriteSongs(RealmList<FavouriteSongModel> favouriteSongs) { this.favouriteSongs = favouriteSongs; }
}
