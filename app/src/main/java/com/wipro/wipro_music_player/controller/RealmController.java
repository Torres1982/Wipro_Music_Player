package com.wipro.wipro_music_player.controller;

import android.util.Log;
import com.wipro.wipro_music_player.Constants;
import com.wipro.wipro_music_player.model.FavouriteSongModel;
import com.wipro.wipro_music_player.model.UserSettingsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {
    // Save the User Settings
    public static void saveUserSettings(Realm realm, int defaultThemeStatus, int darkThemeStatus, int shuffleSwitchStatus, int repeatSwitchStatus, int songsListStatus) {
        //realm.executeTransaction(r -> r.delete(UserSettingsModel.class));
        realm.executeTransaction(r -> {
            UserSettingsModel userSettingsRealm = new UserSettingsModel();
            userSettingsRealm.setId(Constants.RealmDB.DEFAULT_USER_SETTINGS_REALM_ID);
            userSettingsRealm.setDefaultThemeStatus(defaultThemeStatus);
            userSettingsRealm.setDarkThemeStatus(darkThemeStatus);
            userSettingsRealm.setShuffleSwitchStatus(shuffleSwitchStatus);
            userSettingsRealm.setRepeatSwitchStatus(repeatSwitchStatus);
            userSettingsRealm.setSongsListStatus(songsListStatus);
            realm.insertOrUpdate(userSettingsRealm);
        });
        confirmUserSettingsSavedToDb(realm);
    }

    // Display User Settings from Realm DB
    private static void confirmUserSettingsSavedToDb(Realm realm) {
        final UserSettingsModel settings = realm.where(UserSettingsModel.class).findFirst();
        assert settings != null;

        Log.i(Constants.LogTags.MUSIC_TAG, "User Settings successfully saved: " +
                        "\nDefault Theme: " + settings.getDefaultThemeStatus() +
                        "\nDark Theme: " + settings.getDarkThemeStatus() +
                        "\nShuffle Switch: " + settings.getShuffleSwitchStatus() +
                        "\nRepeat Switch: " + settings.getRepeatSwitchStatus() +
                        "\nSongs List: " + settings.getSongsListStatus());
    }

    // Retrieve User Settings from Realm DB
    public static UserSettingsModel getUserSettingsFromDb(Realm realm) {
        return realm.where(UserSettingsModel.class).equalTo("id", Constants.RealmDB.DEFAULT_USER_SETTINGS_REALM_ID).findFirst();
    }

    // Check if the chosen Song is in the Favourite Songs list
    public static  FavouriteSongModel checkIfSongIsInRealmDBbySongPath(Realm realm, String songPath) {
        return realm.where(FavouriteSongModel.class).equalTo("songPath", songPath).findFirst();
    }

    // Add the chosen song to the Favourite Songs list
    public static void addSongToFavourites(Realm realm, String title, String artist, String path, long length, double size) {
        realm.executeTransaction(r -> {
            FavouriteSongModel favouriteSong = new FavouriteSongModel();
            favouriteSong.setId(setFavouriteSongNextUniqueId(realm));
            favouriteSong.setSongTitle(title);
            favouriteSong.setSongArtist(artist);
            favouriteSong.setSongPath(path);
            favouriteSong.setSongLength(length);
            favouriteSong.setSongSize(size);
            realm.insert(favouriteSong);
            Log.i(Constants.LogTags.MUSIC_TAG, "Song: " + favouriteSong.getSongTitle() + " - " + favouriteSong.getSongArtist() + " - " + favouriteSong.getSongLength() + " - " + favouriteSong.getSongSize() + " - " + favouriteSong.getSongPath());
        });
    }

    // Check the max id in the Favourite Song DB
    private static Number checkTheMaxFavouriteSongIdFromRealmDb(Realm realm) {
        return realm.where(FavouriteSongModel.class).max("id");
    }

    // Set the Favourite Song id (based on the max id in Realm DB)
    private static int setFavouriteSongNextUniqueId(Realm realm) {
        int favouriteSongId;
        Number songId = checkTheMaxFavouriteSongIdFromRealmDb(realm);
        favouriteSongId = songId == null ? 1 : songId.intValue() + 1;
        Log.i(Constants.LogTags.MUSIC_TAG, "Next Favourite Song Unique Available Id: " + favouriteSongId);
        return favouriteSongId;
    }

    // Remove the chosen song from the Favourite Songs list
    public static void removeSongFromFavourites(Realm realm, String path) {
        realm.executeTransaction(r -> Objects.requireNonNull(realm.where(FavouriteSongModel.class).equalTo("songPath", path).findFirst()).deleteFromRealm());
    }

    // Removing all Favourite Songs from the Db list
    public static void removeAllSongsFromFavourites(Realm realm) {
        realm.executeTransaction(r -> realm.where(FavouriteSongModel.class).findAll().deleteAllFromRealm());
    }

    // Retrieve Favourite Songs list from Realm DB
    public static List<FavouriteSongModel> getFavouriteSongsFromRealmDb(Realm realm) {
        RealmResults<FavouriteSongModel> results = realm.where(FavouriteSongModel.class).findAll();
        return new ArrayList<>(results);
    }

    // Update the Song List Status
    public static void updateUserSettingsSongListStatus(Realm realm, int songStatus) {
        realm.executeTransaction(r -> {
            UserSettingsModel settings = getUserSettingsFromDb(realm);
            settings.setSongsListStatus(songStatus);
        });
    }
}
