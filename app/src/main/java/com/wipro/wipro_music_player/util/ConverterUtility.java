package com.wipro.wipro_music_player.util;

import java.util.Random;

public class ConverterUtility {
    // Get the String value of the song size (double)
    public static String getDoubleToStringValueOfConvertedAndRoundedSongSize(double size) {
        double convertedSongSize = ConverterUtility.convertBytesToMegabytes(size);
        double convertedSongSizeRounded = ConverterUtility.roundDoubleValue(convertedSongSize, 2);
        return (Double.toString(convertedSongSizeRounded)) + " MB";
    }

    // Converting Bytes to MegaBytes
    private static double convertBytesToMegabytes(double size) {
        return size / (1024 * 1024);
    }

    // Round the Double value to a specific number of decimal places
    private static double roundDoubleValue(double value, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.round(value * scale) / scale;
    }

    // Converting Milliseconds to Minutes and Seconds
    public static String convertMillisecondsToMinutesAndSeconds(long millisec) {
        long minutes = (millisec / 1000) / 60;
        long seconds = (millisec / 1000) % 60;
        String time = minutes + ":" + seconds;

        if (seconds < 10) time =  minutes + ":0" + seconds;
        return time;
    }

    // Get the progress percentage for the Seek Bar
    public static int getSeekBarProgressRatio(long totalTime, long currentTime) {
        long total = (int) (totalTime / 1000);
        long current = (int) (currentTime / 1000);
        double ratio = (((double) current) / total) * 100;
        return (int) ratio;
    }

    // Get the Time progression to where the Progress Bar is moved
    public static int getSeekBarProgressTime(int progress, int totalTime) {
        totalTime = totalTime / 1000;
        int newProgressTime = (int) (((double) progress / 100) * totalTime);
        return newProgressTime * 1000;
    }

    // Random Song index (integer) generator
    public static int generateRandomSongIndex(int listSize) {
        Random random = new Random();
        int min = 0;
        int max = listSize - 1;
        return random.nextInt(max - min) + min;
    }
}
