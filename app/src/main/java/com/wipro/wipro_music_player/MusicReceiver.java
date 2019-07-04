package com.wipro.wipro_music_player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra(Constants.NotificationAction.NOTIFICATION_ACTION_KEY);

        // TODO Must be fixed for actions to control the Media Player
        switch (action) {
            case Constants.NotificationAction.PREVIOUS_SONG_ACTION:
                performActionPreviousSong();
                break;
            case Constants.NotificationAction.STOP_SONG_ACTION:
                performActionStopSong();
                break;
            case Constants.NotificationAction.NEXT_SONG_ACTION:
                performActionNextSong();
                break;
            default:
                Log.i(Constants.LogTags.MUSIC_TAG, "Default Broadcast Receiver Action!");
        }

        //Intent closeNotificationTrayIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //context.sendBroadcast(closeNotificationTrayIntent);
    }

    public static void performActionPreviousSong() {
        Log.i(Constants.LogTags.MUSIC_TAG, "Action 1 from Receiver!");
    }

    public static void performActionStopSong() {
        Log.i(Constants.LogTags.MUSIC_TAG, "Action 2 from Receiver!");
    }

    public static void performActionNextSong() {
        Log.i(Constants.LogTags.MUSIC_TAG, "Action 3 from Receiver!");
    }
}