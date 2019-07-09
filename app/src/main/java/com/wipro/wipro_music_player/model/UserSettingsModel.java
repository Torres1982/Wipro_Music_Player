package com.wipro.wipro_music_player.model;

import io.realm.RealmObject;

public class UserSettingsModel extends RealmObject {
    private String lastPlayedSongPath;
    private int defaultThemeStatus;
    private int darkThemeStatus;
    private int shuffleSwitchStatus;
    private int repeatSwitchStatus;
}
