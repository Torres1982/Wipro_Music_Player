package com.wipro.wipro_music_player.util;

public class ConverterUtility {
    // Converting Bytes to MegaBytes
    public static double convertBytesToMegabytes(double size) {
        return size / (1024 * 1024);
    }

    // Converting Milliseconds to Minutes and Seconds
    public static String convertMillisecondsToMinutesAndSeconds(long millisec) {
        long minutes = (millisec / 1000) / 60;
        long seconds = (millisec / 1000) % 60;
        return minutes + ":" + seconds;
    }
}
