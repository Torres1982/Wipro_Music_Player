package com.wipro.wipro_music_player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class NotificationService extends Service {
    Notification status;
    private static final String MUSIC_TAG = "NotificationService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        assert intent.getAction() != null;
        switch (intent.getAction()) {
            case Constants.ACTION.START_FOREGROUND_ACTION:
                showNotification();
                Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
                break;
            case Constants.ACTION.PREV_ACTION:
                Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
                Log.i(MUSIC_TAG, "Clicked Previous");
                break;
            case Constants.ACTION.PLAY_ACTION:
                Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
                Log.i(MUSIC_TAG, "Clicked Play");
                break;
            case Constants.ACTION.NEXT_ACTION:
                Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
                Log.i(MUSIC_TAG, "Clicked Next");
                break;
            case Constants.ACTION.STOP_FOREGROUND_ACTION:
                Log.i(MUSIC_TAG, "Received Stop Foreground Intent");
                Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
                Log.i(MUSIC_TAG, "Service Stopped");
                stopForeground(true);
                stopSelf();
                break;
        }
        return START_STICKY;
    }


    private void showNotification() {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_compact);
        RemoteViews bigViews = new RemoteViews(getPackageName(), R.layout.notification_expanded);

        //views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        //views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.music_main_image, Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, MusicPlayer.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);

        views.setOnClickPendingIntent(R.drawable.play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.drawable.play, pplayIntent);
        views.setOnClickPendingIntent(R.drawable.next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.drawable.next, pnextIntent);
        views.setOnClickPendingIntent(R.drawable.previous, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.drawable.previous, ppreviousIntent);
        views.setOnClickPendingIntent(R.drawable.expand, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.drawable.expand, pcloseIntent);
        views.setImageViewResource(R.id.button_play, R.drawable.icon_notification);
        bigViews.setImageViewResource(R.id.button_play, R.drawable.icon_notification);
        views.setTextViewText(R.id.music_title, "Song Title");
        bigViews.setTextViewText(R.id.music_title, "Song Title");
        views.setTextViewText(R.id.music_artist, "Artist Name");
        bigViews.setTextViewText(R.id.music_artist, "Artist Name");

        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.ic_launcher_background;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }
}
