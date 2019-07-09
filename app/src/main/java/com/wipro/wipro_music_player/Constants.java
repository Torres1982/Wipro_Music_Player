package com.wipro.wipro_music_player;

public class Constants {
    // Holds corresponding Constants for Debugging (Logs and Toast messages)
    public interface LogTags {
        String MUSIC_TAG = "MUSIC_TAG";
        String PERMISSION_TAG = "PERMISSION_TAG";
        String PERMISSION_MSG = "Storage Permission Accepted!";
    }

    // Holds corresponding Constants for Actions used by the Notification's Image Buttons
    public interface NotificationAction {
        String NOTIFICATION_ACTION_KEY = "action";
        String PREVIOUS_SONG_ACTION = NotificationIdentifier.NOTIFICATION_CHANNEL_ID + "." + NOTIFICATION_ACTION_KEY + ".prev";
        String PLAY_SONG_ACTION = NotificationIdentifier.NOTIFICATION_CHANNEL_ID + "." + NOTIFICATION_ACTION_KEY + ".play";
        String STOP_SONG_ACTION = NotificationIdentifier.NOTIFICATION_CHANNEL_ID + "." + NOTIFICATION_ACTION_KEY + ".stop";
        String NEXT_SONG_ACTION = NotificationIdentifier.NOTIFICATION_CHANNEL_ID + "." + NOTIFICATION_ACTION_KEY + ".next";
    }

    // Holds any corresponding Constants for Notification id's
    public interface NotificationIdentifier {
        int NOTIFICATION_ID = 100;
        String NOTIFICATION_CHANNEL_ID = "com.wipro.torres.music_player";
    }

    // Holds the User Settings for Realm DB
    public interface UserRealmDbSettings {
        int DEFAULT_THEME_STATUS_OFF = 0;
        int DEFAULT_THEME_STATUS_ON = 1;
        int DARK_THEME_STATUS_OFF = 0;
        int DARK_THEME_STATUS_ON = 1;
        int SHUFFLE_SWITCH_STATUS_OFF = 0;
        int SHUFFLE_SWITCH_STATUS_ON = 1;
        int REPEAT_SWITCH_STATUS_OFF = 0;
        int REPEAT_SWITCH_STATUS_ON = 1;
    }
}
