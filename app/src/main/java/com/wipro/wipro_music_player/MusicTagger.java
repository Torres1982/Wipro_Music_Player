package com.wipro.wipro_music_player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.wipro.wipro_music_player.model.AlbumCover;
import com.wipro.wipro_music_player.util.RetrofitUtility;
import com.wipro.wipro_music_player.util.SortUtility;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MusicTagger {
    private static Mp3File song = null;
    //private static String imageStringUrl;

//    private static class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
//        private final WeakReference<ImageView> imageView;
//
//        ImageDownloader(ImageView imageView) {
//            this.imageView = new WeakReference<>(imageView);
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            try {
//                URL url = new URL(strings[0]);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                Log.i(Constants.LogTags.MUSIC_TAG, "Input Stream Connection SUCCESS!");
//                return BitmapFactory.decodeStream(input);
//            } catch (IOException e) {
//                Log.i(Constants.LogTags.MUSIC_TAG, "I/O Exception while opening Stream Connection!");
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//
//            if (imageView.get() != null) {
//                imageView.get().setImageBitmap(bitmap);
//            }
//        }
//    }

    // Handles downloads of Image URL from the Internet using Retrofit 2.0 API
//    private static String fetchAlbumCoverFromInternet(int albumCoverId) {
//        Call<AlbumCover> serviceCall = RetrofitUtility.getRetrofitServiceCall().getAlbumCover(albumCoverId);
//
//        serviceCall.enqueue(new Callback<AlbumCover>() {
//            @Override
//            public void onResponse(@NonNull Call<AlbumCover> call, @NonNull Response<AlbumCover> response) {
//                if (response.isSuccessful()) {
//                    assert response.body() != null;
//                    imageStringUrl = response.body().getThumbnailUrl();
//                    Log.i(Constants.LogTags.MUSIC_TAG, "Responded SUCCESS with a Status Code " + response.code() + " " + imageStringUrl);
//                } else {
//                    Log.i(Constants.LogTags.MUSIC_TAG, "Responded FAIL with a Status Code " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<AlbumCover> call, @NonNull Throwable t) {
//                Log.i(Constants.LogTags.MUSIC_TAG, "Responded FATAL ERROR - on Failure!");
//            }
//        });
//        return imageStringUrl;
//    }

    // Convert bytes array to the Image and set the Album Cover for the given song path
    static void setAlbumCoverFromId3v2Tag(Context context, ImageView imageView, String mp3path) {
        try {
            song = new Mp3File(mp3path);
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            Log.i(Constants.LogTags.MUSIC_TAG, "Exception Occurred While Reading The mp3 File!");
        }

        //imageStringUrl = fetchAlbumCoverFromInternet(SortUtility.getRandomIntegerBetweenMinAndMaxValuesRange(1, 5000));
        //Log.i(Constants.LogTags.MUSIC_TAG, "Image String URL: " + imageStringUrl);

        if (song != null && song.hasId3v2Tag()) {
            ID3v2 id3v2tag = song.getId3v2Tag();
            byte [] imageAlbumData = id3v2tag.getAlbumImage();

            if (imageAlbumData != null) {
                Bitmap bitmapAlbumCover = BitmapFactory.decodeByteArray(imageAlbumData, 0, imageAlbumData.length);
                imageView.setImageBitmap(bitmapAlbumCover);
                Log.i(Constants.LogTags.MUSIC_TAG, "Song Album Image Loaded from the ID3v2 Tag!");
            } else {
                setUpDefaultBitmapAlbumCover(context, imageView, "Default Song Album Image Loaded!");
                //new ImageDownloader(imageView).execute(imageStringUrl); // Used by Async Task
                //fetchAlbumCoverFromInternet(SortUtility.getRandomIntegerBetweenMinAndMaxValuesPassed(1, 5000));
            }
            getID3v2TagInformation();
        } else {
            // Image Downloaded using Retrofit 2.0 and AsyncTask
            //new ImageDownloader(imageView).execute(imageStringUrl); // Used by Async Task
            //fetchAlbumCoverFromInternet(SortUtility.getRandomIntegerBetweenMinAndMaxValuesRange(1, 5000));
            setUpDefaultBitmapAlbumCover(context, imageView, "Song Cannot Be Found or It Has No ID3v2 Tag!");
        }
    }

    // Get information from the Id3v2Tag
    private static void getID3v2TagInformation() {
        Log.i(Constants.LogTags.MUSIC_TAG, "Song Has ID3v2 TAG:");

        ID3v2 id3v2Tag = song.getId3v2Tag();
        String artist = id3v2Tag.getArtist();
        String title = id3v2Tag.getTitle();
        String album = id3v2Tag.getAlbum();
        String genreDescription = id3v2Tag.getGenreDescription();
        int genre = id3v2Tag.getGenre();

        Log.i(Constants.LogTags.MUSIC_TAG,
                "\nArtist: " + artist +
                "\nTitle: " + title +
                "\nAlbum: " + album +
                "\nGenre: " + genre + " - " + genreDescription +
                "\n***********************************************");
    }

    // Set the Default Bitmap Album Cover
    private static void setUpDefaultBitmapAlbumCover(Context context, ImageView imageView, String message) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music);
        imageView.setImageBitmap(bitmap);
        Log.i(Constants.LogTags.MUSIC_TAG, message);
    }
}


// **************************************** ID3v1Tag ***************************************
//        if (song != null && song.hasId3v1Tag()) {
//                Log.i(Constants.LogTags.MUSIC_TAG, "Song Has ID3v1 TAG:");
//
//                ID3v1 id3v1Tag = song.getId3v1Tag();
//                String artist = id3v1Tag.getArtist();
//                String title = id3v1Tag.getTitle();
//                String album = id3v1Tag.getAlbum();
//                String genreDescription = id3v1Tag.getGenreDescription();
//                int genre = id3v1Tag.getGenre();
//
//                Log.i(Constants.LogTags.MUSIC_TAG, "\nArtist: " + artist +
//                "\nTitle: " + title +
//                "\nAlbum: " + album +
//                "\nGenre: " + genre + " - " + genreDescription +
//                "\n***********************************************");
//                song.removeId3v1Tag();
//                try {
//                song.save(context.getApplicationContext().getFilesDir().getParent()+"/myTemporarySong.mp3");
//                Log.i(Constants.LogTags.MUSIC_TAG, "ID3v1 TAG REMOVED! Song saved at: " + context.getApplicationContext().getFilesDir().getParent());
//                } catch (IOException | NotSupportedException e) {
//                Log.i(Constants.LogTags.MUSIC_TAG, "ID3v1 TAG Could not be REMOVED!");
//                }
//                } else {
//                Log.i(Constants.LogTags.MUSIC_TAG, "NO ID3v1 TAG!");
//                }

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
