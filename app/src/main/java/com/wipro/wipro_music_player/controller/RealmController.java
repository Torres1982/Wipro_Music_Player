package com.wipro.wipro_music_player.controller;

import android.util.Log;
import com.wipro.wipro_music_player.Constants;
import com.wipro.wipro_music_player.model.UserSettingsModel;
import io.realm.Realm;

public class RealmController {
    // Save the User Settings
    public static void saveUserSettings(Realm realm, int defaultThemeStatus, int darkThemeStatus, int shuffleSwitchStatus, int repeatSwitchStatus) {
        //realm.executeTransaction(r -> r.delete(UserSettingsModel.class));
        realm.executeTransaction(r -> {
            UserSettingsModel userSettingsRealm = new UserSettingsModel();
            userSettingsRealm.setId(1);
            userSettingsRealm.setDefaultThemeStatus(defaultThemeStatus);
            userSettingsRealm.setDarkThemeStatus(darkThemeStatus);
            userSettingsRealm.setShuffleSwitchStatus(shuffleSwitchStatus);
            userSettingsRealm.setRepeatSwitchStatus(repeatSwitchStatus);
            realm.insertOrUpdate(userSettingsRealm);
        });

        getUserSettings(realm);
    }

    // Retrieve User Settings from Realm DB
    private static void getUserSettings(Realm realm) {
        final UserSettingsModel settings = realm.where(UserSettingsModel.class).findFirst();
        assert settings != null;

        Log.i(Constants.LogTags.MUSIC_TAG, "User Settings successfully saved: " +
                        "\nDefault Theme: " + settings.getDefaultThemeStatus() +
                        "\nDark Theme: " + settings.getDarkThemeStatus() +
                        "\nShuffle Switch: " + settings.getShuffleSwitchStatus() +
                        "\nRepeat Switch: " + settings.getRepeatSwitchStatus());
    }
}


// Set up the Default Realm Configuration for the Application
//    private static RealmConfiguration createDefaultRealmConfiguration() {
//        // Generate the random Encryption Key
//        byte [] encryptionKey = new byte [64];
//        new SecureRandom().nextBytes(encryptionKey);
//
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
//                .name(Constants.RealmDB.DEFAULT_REALM_CONFIGURATION_NAME)
//                .encryptionKey(encryptionKey)
//                .schemaVersion(0)
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        return realmConfiguration;
//    }

// Get the instance of Realm
//    private static Realm getRealmInstance() {
//        return Realm.getInstance(createDefaultRealmConfiguration());
//    }
