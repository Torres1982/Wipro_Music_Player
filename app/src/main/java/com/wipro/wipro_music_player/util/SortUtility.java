package com.wipro.wipro_music_player.util;

import com.wipro.wipro_music_player.model.SongModel;
import java.util.List;
import java.util.Random;

import static java.util.Comparator.comparing;

public class SortUtility {
    // Sorting the Music List in ascending order by Artist
    public static void sortMusicListAscendingByArtist(List<SongModel> list) {
        list.sort(comparing(SongModel::getArtist));
    }

    // Sorting the Music List in ascending order by Song Title
    public static void sortMusicListAscendingBySongTitle(List<SongModel> list) {
        list.sort(comparing(SongModel::getTitle));
    }

    // Returns random integer given minimum and maximum values range
    public static int getRandomIntegerBetweenMinAndMaxValuesRange(int minimum, int maximum) {
        Random rand = new Random();
        return minimum + rand.nextInt((maximum - minimum) + 1);
    }
}
