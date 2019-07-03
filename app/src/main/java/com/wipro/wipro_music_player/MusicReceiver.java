package com.wipro.wipro_music_player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");

        switch (action) {
            case Constants.NotificationAction.PREVIOUS_SONG_ACTION:
                performActionPreviousSong();
                break;
            case Constants.NotificationAction.PLAY_SONG_ACTION:
                performActionPlaySong();
                break;
            case Constants.NotificationAction.NEXT_SONG_ACTION:
                performActionNextSong();
                break;
            default:
                Log.i(Constants.LogTags.MUSIC_TAG, "Default Broadcast Receiver Action!");
        }

        Intent closeNotificationTrayIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeNotificationTrayIntent);
    }

    public void performActionPreviousSong() { }
    public void performActionPlaySong() { }
    public void performActionNextSong() { }
}

//    public static void createNotif(Context context){
//
//    ...
//        //This is the intent of PendingIntent
//        Intent intentAction = new Intent(context,ActionReceiver.class);
//
//        //This is optional if you have more than one buttons and want to differentiate between two
//        intentAction.putExtra("action","actionName");
//
//        pIntentlogin = PendingIntent.getBroadcast(context,1,intentAction,PendingIntent.FLAG_UPDATE_CURRENT);
//        drivingNotifBldr = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.steeringwheel)
//                .setContentTitle("NoTextZone")
//                .setContentText("Driving mode it ON!")
//                //Using this action button I would like to call logTest
//                .addAction(R.drawable.smallmanwalking, "Turn OFF driving mode", pIntentlogin)
//                .setOngoing(true);
//    ...
//
//    }
