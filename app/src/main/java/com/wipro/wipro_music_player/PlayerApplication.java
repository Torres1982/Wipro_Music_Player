package com.wipro.wipro_music_player;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PlayerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name(Constants.RealmDB.DEFAULT_REALM_CONFIGURATION_NAME).build();
        Realm.setDefaultConfiguration(config);
        Log.i(Constants.LogTags.MUSIC_TAG, "Realm has been initiated from the Application class!");
    }
}
