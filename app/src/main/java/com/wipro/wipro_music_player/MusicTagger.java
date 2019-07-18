package com.wipro.wipro_music_player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class MusicTagger {
    private static Mp3File song = null;

    // Convert bytes array to the Image and set the Album Cover for the given song path
    static void setAlbumCoverFromId3v2Tag(Context context, ImageView imageView, String mp3path) {
        Log.i(Constants.LogTags.MUSIC_TAG, "Path of the Current Song: " + mp3path);

        try {
            song = new Mp3File(mp3path);
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            Log.i(Constants.LogTags.MUSIC_TAG, "Exception Occurred While Reading The mp3 File!");
        }

//        if (song != null && song.hasId3v1Tag()) {
//            Log.i(Constants.LogTags.MUSIC_TAG, "Song Has ID3v1 TAG:");
//
//            ID3v1 id3v1Tag = song.getId3v1Tag();
//            String artist = id3v1Tag.getArtist();
//            String title = id3v1Tag.getTitle();
//            String album = id3v1Tag.getAlbum();
//            String genreDescription = id3v1Tag.getGenreDescription();
//            int genre = id3v1Tag.getGenre();
//
//            Log.i(Constants.LogTags.MUSIC_TAG, "\nArtist: " + artist +
//                                                     "\nTitle: " + title +
//                                                     "\nAlbum: " + album +
//                                                     "\nGenre: " + genre + " - " + genreDescription +
//                                                     "\n***********************************************");
//        } else {
//            Log.i(Constants.LogTags.MUSIC_TAG, "NO ID3v1 TAG!");
//        }

//            File songFile = new File(mp3path);
//
//            try {
//                InputStream inStream = new FileInputStream(songFile);
//                //OutputStream outStream = new FileOutputStream(new File(context.getApplicationContext().getFilesDir().getParent()+"/myTempSong.mp3"));
//                OutputStream outStream = new FileOutputStream(new File("/storage/6365-3530/MUSIC/Deftones/MyTestSong.mp3"));
//                Log.i(Constants.LogTags.MUSIC_TAG, "Absolute Path: " + context.getApplicationContext().getFilesDir().getParent());
//                byte [] buffer = new byte[4096];
//                int length;
//
//                while ((length = inStream.read(buffer)) > 0) {
//                    outStream.write(buffer, 0, length);
//                }
//
//                inStream.close();
//                outStream.close();
//            } catch (IOException e) {
//                Log.i(Constants.LogTags.MUSIC_TAG, "Exception Occurred While Writing The mp3 Temp File!");
//            }

//            song.removeId3v1Tag();
//            try {
//                //song.save("/storage/6365-3530/MUSIC");
//                song.save(context.getApplicationContext().getFilesDir().getParent()+"/mySong.mp3");
//                Log.i(Constants.LogTags.MUSIC_TAG, "ID3v1 TAG REMOVED! Song Saved!");
//                Log.i(Constants.LogTags.MUSIC_TAG, "Absolute Path: " + context.getApplicationContext().getFilesDir().getParent());
//            } catch (IOException | NotSupportedException e) {
//                Log.i(Constants.LogTags.MUSIC_TAG, "Exception Occurred While Saving The mp3 File!");
//            }
//
//            try {
//                song.save(mp3path + "temporary");
//
//                File from = new File(mp3path + "temporary");
//
//                if (from.exists()) {
//                    File file = new File(mp3path);
//                    //long sizeold = file.length();
//                    file.delete();
//                    context.deleteFile(file.getName());
//                    File to = new File(mp3path);
//                    //long sizenew = from.length();
//                    from.renameTo(to);
//                    //long freedspace = sizeold - sizenew;
//
//                    //song.save(mp3path);
//                    Log.i(Constants.LogTags.MUSIC_TAG, "ID3v1 TAG REMOVED!");
//                } else {
//                    Log.i(Constants.LogTags.MUSIC_TAG, "Temporary File does not exist!");
//                }
//            } catch (IOException | NotSupportedException e) {
//                Log.i(Constants.LogTags.MUSIC_TAG, "ID3v1 TAG Could not be REMOVED!");
//            }
        //} else {
            //Log.i(Constants.LogTags.MUSIC_TAG, "NO ID3v1 TAG!");
        //}

//        if (song != null && song.hasId3v2Tag()) {
//            Log.i(Constants.LogTags.MUSIC_TAG, "Song Has ID3v2 TAG:");
//
//            ID3v2 id3v2Tag = song.getId3v2Tag();
//            String artist = id3v2Tag.getArtist();
//            String artist2 = id3v2Tag.getAlbumArtist();
//            String artist3 = id3v2Tag.getOriginalArtist();
//            String title = id3v2Tag.getTitle();
//            String album = id3v2Tag.getAlbum();
//            String genreDescription = id3v2Tag.getGenreDescription();
//            int genre = id3v2Tag.getGenre();
//
//            Log.i(Constants.LogTags.MUSIC_TAG, "\nArtist: " + artist +
//                                                    "\nArtist 2: " + artist2 +
//                                                    "\nArtist 3: " + artist3 +
//                                                    "\nTitle: " + title +
//                                                    "\nAlbum: " + album +
//                                                    "\nGenre: " + genre + " - " + genreDescription +
//                                                    "\n***********************************************");
//        }
//        if (song != null && song.hasCustomTag()) {
//            Log.i(Constants.LogTags.MUSIC_TAG, "Song Has Custom Tag!");
//        }

        if (song != null && song.hasId3v2Tag()) {
            ID3v2 id3v2tag = song.getId3v2Tag();
            byte [] imageData = id3v2tag.getAlbumImage();

            if (imageData != null) {
                Bitmap bitmapAlbumCover = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                imageView.setImageBitmap(bitmapAlbumCover);
                Log.i(Constants.LogTags.MUSIC_TAG, "Song Album Image Loaded from the ID3v2 Tag!");
            } else {
                setUpDefaultBitmapAlbumCover(context, imageView, "Default Song Album Image Loaded!");
            }
        } else {
            setUpDefaultBitmapAlbumCover(context, imageView, "Song Cannot Be Found or It Has No ID3v2 Tag!");
        }
    }

    // Set the Default Bitmap Album Cover
    private static void setUpDefaultBitmapAlbumCover(Context context, ImageView imageView, String message) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music);
        imageView.setImageBitmap(bitmap);
        Log.i(Constants.LogTags.MUSIC_TAG, message);
    }
}
