package com.wipro.wipro_music_player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

    public class Constants {
        public interface ACTION {
            public static String MAIN_ACTION = "com.marothiatechs.customnotification.action.main";
            public static String INIT_ACTION = "com.marothiatechs.customnotification.action.init";
            public static String PREV_ACTION = "com.marothiatechs.customnotification.action.prev";
            public static String PLAY_ACTION = "com.marothiatechs.customnotification.action.play";
            public static String NEXT_ACTION = "com.marothiatechs.customnotification.action.next";
            public static String START_FOREGROUND_ACTION = "com.marothiatechs.customnotification.action.startforeground";
            public static String STOP_FOREGROUND_ACTION = "com.marothiatechs.customnotification.action.stopforeground";
        }

        public interface NOTIFICATION_ID {
            public static int FOREGROUND_SERVICE = 101;
        }

        public static Bitmap getDefaultAlbumArt(Context context) {
            Bitmap bm = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            try {
                bm = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.icon_notification, options);
            } catch (Error ee) {
            } catch (Exception e) {
            }
            return bm;
        }
}
