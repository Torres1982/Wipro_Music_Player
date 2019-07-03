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
        String PREVIOUS_SONG_ACTION = "com.marothiatechs.customnotification.action.prev";
        String PLAY_SONG_ACTION = "com.marothiatechs.customnotification.action.play";
        String STOP_SONG_ACTION = "com.marothiatechs.customnotification.action.stop";
        String NEXT_SONG_ACTION = "com.marothiatechs.customnotification.action.next";
    }

    // Holds any corresponding Constants for Notification id's
    public interface NotificationIdentifier {
        int NOTIFICATION_ID = 100;
        String NOTIFICATION_CHANNEL_ID = "com.wipro.torres.wipro_music_player";
    }
}
