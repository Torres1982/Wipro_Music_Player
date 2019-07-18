package com.wipro.wipro_music_player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

class MusicTagger {
    private static Mp3File song = null;

    // Convert bytes array to the Image and set the Album Cover for the given song path
    static void setAlbumCoverFromId3v2Tag(Context context, ImageView imageView, String mp3path) {
        try {
            song = new Mp3File(mp3path);
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            Log.i(Constants.LogTags.MUSIC_TAG, "Exception Occurred While Reading The mp3 File!");
        }

        if (song != null && song.hasId3v2Tag()) {
            ID3v2 id3v2tag = song.getId3v2Tag();
            byte [] imageData = id3v2tag.getAlbumImage();

            if (imageData != null) {
                Bitmap bitmapAlbumCover = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                imageView.setImageBitmap(bitmapAlbumCover);
                Log.i(Constants.LogTags.MUSIC_TAG, "Song Album Image Loaded from the ID3 Tag!");
            } else {
                setUpDefaultBitmapAlbumCover(context, imageView, "Default Song Album Image Loaded!");
            }
        } else {
            setUpDefaultBitmapAlbumCover(context, imageView, "Song Cannot Be Found or It Has No ID3 Tag!");
        }
    }

    // Set the Default Bitmap Album Cover
    private static void setUpDefaultBitmapAlbumCover(Context context, ImageView imageView, String message) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music);
        imageView.setImageBitmap(bitmap);
        Log.i(Constants.LogTags.MUSIC_TAG, message);
    }
}
