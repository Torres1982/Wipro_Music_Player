package com.wipro.wipro_music_player.controller;

import android.util.Log;
import com.wipro.wipro_music_player.Constants;
import com.wipro.wipro_music_player.model.FavouriteSongModel;
import com.wipro.wipro_music_player.model.UserSettingsModel;
import io.realm.Realm;

public class RealmController {
    // Save the User Settings
    public static void saveUserSettings(Realm realm, int defaultThemeStatus, int darkThemeStatus, int shuffleSwitchStatus, int repeatSwitchStatus) {
        //realm.executeTransaction(r -> r.delete(UserSettingsModel.class));
        realm.executeTransaction(r -> {
            UserSettingsModel userSettingsRealm = new UserSettingsModel();
            userSettingsRealm.setId(Constants.RealmDB.DEFAULT_USER_SETTINGS_REALM_ID);
            userSettingsRealm.setDefaultThemeStatus(defaultThemeStatus);
            userSettingsRealm.setDarkThemeStatus(darkThemeStatus);
            userSettingsRealm.setShuffleSwitchStatus(shuffleSwitchStatus);
            userSettingsRealm.setRepeatSwitchStatus(repeatSwitchStatus);
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
                        "\nRepeat Switch: " + settings.getRepeatSwitchStatus());
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
        });
    }

    // Check the max id in the Favourite Song DB
    public static Number checkTheMaxFavouriteSongIdFromRealmDb(Realm realm) {
        return realm.where(FavouriteSongModel.class).max("id");
    }
}
