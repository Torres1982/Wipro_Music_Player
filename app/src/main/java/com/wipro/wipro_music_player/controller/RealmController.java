package com.wipro.wipro_music_player.controller;

import android.util.Log;
import com.wipro.wipro_music_player.Constants;
import com.wipro.wipro_music_player.model.UserSettingsModel;
import java.security.SecureRandom;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmController {
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

    // Save the User Settings
    public static void saveUserSettings(int defaultThemeStatus, int darkThemeStatus, int shuffleSwitchStatus, int repeatSwitchStatus) {
        //Realm realm = getRealmInstance();
        Realm realm = Realm.getDefaultInstance();
        UserSettingsModel userSettingsModel = new UserSettingsModel();

        realm.executeTransactionAsync(realmExecute -> {
            UserSettingsModel userSettingsReal = realmExecute.copyToRealmOrUpdate(userSettingsModel);
            userSettingsReal.setId(1);
            userSettingsReal.setDefaultThemeStatus(defaultThemeStatus);
            userSettingsReal.setDarkThemeStatus(darkThemeStatus);
            userSettingsReal.setShuffleSwitchStatus(shuffleSwitchStatus);
            userSettingsReal.setRepeatSwitchStatus(repeatSwitchStatus);
        }, () -> Log.i(Constants.LogTags.MUSIC_TAG, "User settings successfully saved to Realm DB!"),
        error -> Log.i(Constants.LogTags.MUSIC_TAG, "User settings could not be saved to Realm DB!"));

        realm.close();

//        UserSettingsModel userSettingsReal = new UserSettingsModel();
//        userSettingsReal.setId(1);
//        userSettingsReal.setDefaultThemeStatus(defaultThemeStatus);
//        userSettingsReal.setDarkThemeStatus(darkThemeStatus);
//        userSettingsReal.setShuffleSwitchStatus(shuffleSwitchStatus);
//        userSettingsReal.setRepeatSwitchStatus(repeatSwitchStatus);
//
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(userSettings);
//        realm.commitTransaction();
    }
}
