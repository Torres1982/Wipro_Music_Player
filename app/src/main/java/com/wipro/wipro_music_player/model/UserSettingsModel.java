package com.wipro.wipro_music_player.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserSettingsModel extends RealmObject {
    @PrimaryKey
    private long id;
    private int defaultThemeStatus;
    private int darkThemeStatus;
    private int shuffleSwitchStatus;
    private int repeatSwitchStatus;
    private int songsListStatus;
    private int sortingListStatus;

    public UserSettingsModel() {}

    public long getId() { return id; }
    public int getDefaultThemeStatus() { return defaultThemeStatus; }
    public int getDarkThemeStatus() { return darkThemeStatus; }
    public int getShuffleSwitchStatus() { return shuffleSwitchStatus; }
    public int getRepeatSwitchStatus() { return repeatSwitchStatus; }
    public int getSongsListStatus() { return songsListStatus; }
    public int getSortingListStatus() { return sortingListStatus; }

    public void setId(long id) { this.id = id; }
    public void setDefaultThemeStatus(int defaultThemeStatus) { this.defaultThemeStatus = defaultThemeStatus; }
    public void setDarkThemeStatus(int darkThemeStatus) { this.darkThemeStatus = darkThemeStatus; }
    public void setShuffleSwitchStatus(int shuffleSwitchStatus) { this.shuffleSwitchStatus = shuffleSwitchStatus; }
    public void setRepeatSwitchStatus(int repeatSwitchStatus) { this.repeatSwitchStatus = repeatSwitchStatus; }
    public void setSongsListStatus(int songsListStatus) { this.songsListStatus = songsListStatus; }
    public void setSortingListStatus(int sortingListStatus) { this.sortingListStatus = sortingListStatus; }
}
