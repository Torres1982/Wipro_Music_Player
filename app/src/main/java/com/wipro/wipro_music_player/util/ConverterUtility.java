package com.wipro.wipro_music_player.util;

public class ConverterUtility {
    // Converting Bytes to MegaBytes
    public static double convertBytesToMegabytes(double size) {
        return size / (1024 * 1024);
    }

    // Round the Double value to a specific number of decimal places
    public static double roundDoubleValue(double value, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.round(value * scale) / scale;
    }

    // Converting Milliseconds to Minutes and Seconds
    public static String convertMillisecondsToMinutesAndSeconds(long millisec) {
        long minutes = (millisec / 1000) / 60;
        long seconds = (millisec / 1000) % 60;
        return minutes + ":" + seconds;
    }
}
