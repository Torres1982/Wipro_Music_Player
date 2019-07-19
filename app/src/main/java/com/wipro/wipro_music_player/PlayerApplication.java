package com.wipro.wipro_music_player;

import android.app.Application;
import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PlayerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        //RealmConfiguration config = new RealmConfiguration.Builder().name(Constants.RealmDB.DEFAULT_REALM_CONFIGURATION_NAME).build();
        RealmConfiguration config = createDefaultRealmConfiguration();
        Realm.setDefaultConfiguration(config);
        Log.i(Constants.LogTags.MUSIC_TAG, "Realm has been initiated from the Application class!");
        getCurrentSchemaVersion(config);
    }

    // Set up the Default Realm Configuration for the Application
    private static RealmConfiguration createDefaultRealmConfiguration() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Constants.RealmDB.DEFAULT_REALM_CONFIGURATION_NAME)
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        return realmConfiguration;
    }

    // Retrieve the Current Schema Version
    private static void getCurrentSchemaVersion(RealmConfiguration config) {
        DynamicRealm dynRealm = DynamicRealm.getInstance(config);
        long version = dynRealm.getVersion();
        Log.i(Constants.LogTags.MUSIC_TAG, "Current Schema Version: " + version + ". Realm File Name: " + config.getRealmFileName());
        dynRealm.close();
    }
}
